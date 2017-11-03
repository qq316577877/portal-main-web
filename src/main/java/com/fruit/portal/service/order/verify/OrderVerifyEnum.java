package com.fruit.portal.service.order.verify;

public enum OrderVerifyEnum {

	/**
	 * 产品基本信息校验
	 */
	PRODUCT("productVerify"),

	/**
	 * 订单基本信息校验
	 */
	ORDERBASIC("orderBasicVerify"),

	/**
	 * 订单权限校验
	 */
	ORDERPERMISSION("orderPermissionVerify"),

	/**
	 * 订单状态校验
	 */
	ORDERSTATUS("orderStatusVerify"),

	/**
	 * 供应商信息校验
	 */
	SUPPLIER("supplierVerify"),

	/**
	 * 收货地址信息校验
	 */
	DELIVERY("deliveryVerify"),

	/**
	 * 物流服务信息校验
	 */
	LOGISTICS("logisticsVerify"),

	/**
	 * 资金服务校验
	 */
	LOAN("loanVerify");

	private String value;

	private OrderVerifyEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
