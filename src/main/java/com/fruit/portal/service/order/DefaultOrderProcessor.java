package com.fruit.portal.service.order;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.common.OrderEventRoleEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.common.OrderUpdateTypeEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.OrderService;
import com.fruit.portal.service.trade.OrderStateMachine;
import com.fruit.portal.service.trade.OrderTaskHelper;
import com.fruit.portal.vo.order.OrderProcessRequest;
import com.fruit.portal.vo.order.OrderProcessResponse;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;

@Service("defaultOrderProcessor")
public class DefaultOrderProcessor implements IOrderProcessor {

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderProcessor.class);

	protected OrderEventEnum event;

	@Autowired
	private OrderService orderService;

	@Autowired
	protected OrderStateMachine orderStateMachine;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private OrderTaskHelper orderTaskHelper;

	@Autowired(required = false)
	protected List<EventPublisher> eventPublishers;

	public DefaultOrderProcessor(OrderEventEnum event, boolean needNotify) {
		this.event = event;
	}

	protected DefaultOrderProcessor(OrderEventEnum event) {
		this(event, false);
	}

	public DefaultOrderProcessor() {
		this(OrderEventEnum.SAVE, false);
	}

	@Override
	public OrderEventEnum getEvent() {
		return event;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public OrderProcessResponse process(OrderProcessRequest request) {
		boolean successful = false;
		String message = "";
		try {
			final int userId = request.getUserId();
			final String orderId = request.getOrderId();
			OrderEventEnum event = request.getEvent();
			OrderDTO orderDTO = orderService.loadByNo(orderId);
			OrderEventRoleEnum role = event.getRole();
			Validate.isTrue((role == OrderEventRoleEnum.USER && orderDTO.getUserId() == userId)
					|| (role == OrderEventRoleEnum.SYS), "用户权限错误");
			final int status = orderDTO.getStatus();
			OrderStatusEnum nextStatus = null;
			switch (role) {
			case USER:
				nextStatus = this.orderStateMachine.computeStatusForBuyer(status, event);
				break;
			case SYS:
				nextStatus = this.orderStateMachine.computeStatusForSystem(status, event);
				break;
			}
			if (nextStatus == null) {
				throw new IllegalStateException("no next status for " + role);
			}
			
			if(!handleExtraBefore(orderDTO)){
				successful = false;
				message = "订单状态或货柜状态异常";
				return new OrderProcessResponse(successful, message);
			}
			this.updateOrder(request, orderDTO, nextStatus);

			successful = true;

			final int toStatus = nextStatus.getStatus();
			final int type = request.getOperatorType();

			//统计表
			orderLogService.addRecordTime(orderDTO.getNo(),toStatus);

			// 同时记录订单变更日志
			orderTaskHelper.submitRunnable(new Runnable() {
				@Override
				public void run() {
					orderLogService.addLog(orderId, userId, type, status, toStatus, OrderUpdateTypeEnum.get(type)
							.getMessage());
				}
			});
		} catch (IllegalArgumentException e) {
			message = "请求不正确：" + e.getMessage();
			LOGGER.warn("[bad args] bad arguments for process: " + request);
		} catch (IllegalStateException e) {
			message = "未知的订单状态";
			LOGGER.warn("[bad states] bad state for process: " + request + ", " + e.getMessage());
		} catch (RuntimeException e) {
			message = "系统繁忙";
			LOGGER.error("can not process: " + request, e);
			EventHelper.triggerEvent(this.eventPublishers, "change.order." + request.getOrderId(),
					"failed to change order " + JSON.toJSONString(request), EventLevel.URGENT, e,
					ServerIpUtils.getServerIp());
			throw new RuntimeException("系统繁忙", e);
		}

		return new OrderProcessResponse(successful, message);
	}

	protected void updateOrder(OrderProcessRequest request, OrderDTO orderDTO, OrderStatusEnum nextStatus) {
		int status = orderDTO.getStatus();
		orderDTO.setStatus(nextStatus.getStatus());
		orderDTO.setLastEditor(request.getEvent().getRole() + ":" + request.getUserId());
		this.setExtraOrderInfo(orderDTO);
		this.handleExtra(orderDTO, status, request);
		orderService.update(orderDTO);
	}

	/**
	 * 处理其他操作
	 *
	 * @param orderDTO
	 * @param status
	 * @param request
	 */
	protected boolean handleExtraBefore(OrderDTO orderDTO) {
		return true;
	}
	
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {
	}

	protected void setExtraOrderInfo(OrderDTO orderDTO) {
	}
	
	/**
	 * 根据订单状态判断查询正式表还是临时表
	 * 
	 * @param status
	 * @return
	 */
	protected boolean isQueryTmp(int status) {
		if (status == 1 || status == 2 || status == 3) {
			return true;
		}
		return false;
	}
}
