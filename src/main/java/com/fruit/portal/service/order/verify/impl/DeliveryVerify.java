package com.fruit.portal.service.order.verify.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.account.biz.service.UserDeliveryAddressService;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;
import com.fruit.portal.vo.order.OrderLogistics;

/**
 * 收货地址校验
 * 
 * @author ovfintech
 *
 */
public class DeliveryVerify implements Verify {

	@Resource
	private UserDeliveryAddressService userDeliveryAddressService;

	private static final Logger logger = LoggerFactory.getLogger(DeliveryVerify.class);

	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getLogisticsInfo() != null) {
			OrderLogistics logistics = verifyInfo.getLogisticsInfo();
			Integer deliveryId = logistics.getDeliveryId();
			UserDeliveryAddressDTO deliveryAddressDTO = userDeliveryAddressService.loadById(deliveryId);
			if (deliveryAddressDTO == null) {
				verifyResult.setExcuteResult(false);
				verifyResult.setErrorCode(111009);
				verifyResult.setMsg("收货地址不存在");
				logger.debug("out method verify");
				return verifyResult;
			}
		}
		verifyResult.setExcuteResult(true);
		logger.debug("out method verify");
		return verifyResult;
	}

}
