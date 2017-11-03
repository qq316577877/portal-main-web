package com.fruit.portal.service.order.verify;

/**
 * 校验总控
 * 
 * @author ovfintech
 *
 */
public enum BaseVerifyEnum {

	/**
	 * 创建订单校验
	 */
	CREATEORDER("createOrderVerify"),

	/**
	 * 创建物流服务校验
	 */
	CREATELOGISTICS("createLogisticsVerify");

	private String value;

	BaseVerifyEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
