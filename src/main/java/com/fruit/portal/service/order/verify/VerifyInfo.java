package com.fruit.portal.service.order.verify;

import java.util.List;

import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.vo.order.OrderBasicInfo;
import com.fruit.portal.vo.order.OrderContainer;
import com.fruit.portal.vo.order.OrderLogistics;

public class VerifyInfo {

	/**
	 * 产品信息
	 */
	private List<OrderContainer> productInfos;

	/**
	 * 用户信息
	 */
	private UserModel userInfo;

	/**
	 * 订单信息(包含供应商)
	 */
	private OrderBasicInfo orderInfo;

	/**
	 * 旧订单信息
	 */
	private OrderDTO oldOrder;

	/**
	 * 物流信息
	 */
	private OrderLogistics logisticsInfo;

	/**
	 * 
	 */
	private List<BaseVerifyEnum> baseVerifyEnums;

	/**
	 * 
	 */
	private List<OrderVerifyEnum> orderVerifyEnums;

	public List<OrderContainer> getProductInfos() {
		return productInfos;
	}

	public void setProductInfos(List<OrderContainer> productInfos) {
		this.productInfos = productInfos;
	}

	public UserModel getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserModel userInfo) {
		this.userInfo = userInfo;
	}

	public OrderBasicInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderBasicInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public List<BaseVerifyEnum> getBaseVerifyEnums() {
		return baseVerifyEnums;
	}

	public void setBaseVerifyEnums(List<BaseVerifyEnum> baseVerifyEnums) {
		this.baseVerifyEnums = baseVerifyEnums;
	}

	public List<OrderVerifyEnum> getOrderVerifyEnums() {
		return orderVerifyEnums;
	}

	public void setOrderVerifyEnums(List<OrderVerifyEnum> orderVerifyEnums) {
		this.orderVerifyEnums = orderVerifyEnums;
	}

	public OrderDTO getOldOrder() {
		return oldOrder;
	}

	public void setOldOrder(OrderDTO oldOrder) {
		this.oldOrder = oldOrder;
	}

	public OrderLogistics getLogisticsInfo() {
		return logisticsInfo;
	}

	public void setLogisticsInfo(OrderLogistics logisticsInfo) {
		this.logisticsInfo = logisticsInfo;
	}
}
