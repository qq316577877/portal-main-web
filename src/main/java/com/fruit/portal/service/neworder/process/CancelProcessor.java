package com.fruit.portal.service.neworder.process;

import com.fruit.newOrder.biz.common.EventRoleEnum;
import com.fruit.newOrder.biz.common.OrderEventEnum;
import com.fruit.newOrder.biz.common.OrderModeTypeEnum;
import com.fruit.newOrder.biz.common.OrderStatusEnum;
import com.fruit.newOrder.biz.dto.OrderGoodsInfoDTO;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.request.OrderProcessRequest;
import com.fruit.newOrder.biz.request.OrderProcessResponse;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.newOrder.biz.service.impl.DefaultOrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * 取消订单
 */
@Service
public class CancelProcessor extends DefaultOrderProcessor {



	public CancelProcessor() {
		super(OrderEventEnum.USER_CANCEL);
	}

	@Autowired
	private OrderNewInfoService orderNewInfoService;


	@Override
	protected OrderProcessResponse handleExtraBefore(OrderProcessRequest request) {

		OrderProcessResponse orderProcessResponse = new OrderProcessResponse(true,"","");

		int userId = request.getUserId();//用户ID
		String orderNo = request.getOrderNo();//订单号
		OrderEventEnum event = request.getEvent();//订单事件
		EventRoleEnum role = event.getRole(); //事件所属角色


		OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
		if(orderNewInfoDTO == null ){
			orderProcessResponse.setMessage("订单不存在");
			orderProcessResponse.setSuccessful(false);
			return orderProcessResponse;
		}

		if(orderNewInfoDTO.getUserId() != userId){
			orderProcessResponse.setMessage("不能操作他人订单");
			orderProcessResponse.setSuccessful(false);
			return orderProcessResponse;
		}

		//设置原状态
		request.setStatusBefore(orderNewInfoDTO.getStatus());

		return orderProcessResponse;
	}


}
