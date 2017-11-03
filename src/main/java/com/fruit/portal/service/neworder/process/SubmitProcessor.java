package com.fruit.portal.service.neworder.process;

import com.fruit.newOrder.biz.common.EventRoleEnum;
import com.fruit.newOrder.biz.common.OrderEventEnum;
import com.fruit.newOrder.biz.common.OrderModeTypeEnum;
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
 * 提交审核
 */
@Service
public class SubmitProcessor extends DefaultOrderProcessor {



	public SubmitProcessor() {
		super(OrderEventEnum.SUBMIT);
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
		Map<String,Object> dataMap = request.getParams();//参数
		OrderNewInfoDTO  orderNewInfoDTOUpdate = request.getOrderInfo();//(OrderNewInfoDTO) dataMap.get("orderNewInfo");
		List<OrderGoodsInfoDTO>  orderGoodsInfoList = request.getOrderGoodsInfoList();//(List<OrderGoodsInfoDTO>) dataMap.get("orderGoodsInfoList");

		OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
		if(orderNewInfoDTO != null ){
			orderProcessResponse.setMessage("订单重复!");
			orderProcessResponse.setSuccessful(false);
			return orderProcessResponse;
		}

		int modeType = orderNewInfoDTOUpdate.getModeType();

		int containerNum = orderNewInfoDTOUpdate.getContainerNum();

		int goodsNum = orderNewInfoDTOUpdate.getGoodsNum();

		if(modeType == OrderModeTypeEnum.BOX.getType()){
			if(orderGoodsInfoList == null || orderGoodsInfoList.size() < 1 || goodsNum < 1){
				orderProcessResponse.setMessage("您的购买的商品不少了，我们拒绝接单!");
				orderProcessResponse.setSuccessful(false);
				return orderProcessResponse;
			}
		}else if(modeType == OrderModeTypeEnum.CONTAINER.getType()){
			if(containerNum < 1){
				orderProcessResponse.setMessage("您的购买的货柜不少了，我们拒绝接单!");
				orderProcessResponse.setSuccessful(false);
				return orderProcessResponse;
			}
		}else{
			orderProcessResponse.setMessage("下单方式不支持");
			orderProcessResponse.setSuccessful(false);
			return orderProcessResponse;
		}

		//创建订单前的状态
		request.setStatusBefore(0);

		return orderProcessResponse;
	}

}
