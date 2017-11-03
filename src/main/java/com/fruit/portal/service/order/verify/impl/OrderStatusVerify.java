package com.fruit.portal.service.order.verify.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;

public class OrderStatusVerify implements Verify {

	private static final Logger logger = LoggerFactory.getLogger(OrderStatusVerify.class);
			
	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getOldOrder() != null) {
			OrderDTO oldOrder = verifyInfo.getOldOrder();
			if (oldOrder.getStatus() > OrderStatusEnum.SAVED.getStatus()) {
				verifyResult.setExcuteResult(false);
				verifyResult.setErrorCode(111008);
				verifyResult.setMsg("该订单已提交审核，不能重复提交");
				logger.debug("out method verify");
				return verifyResult;
			}
		}
		verifyResult.setExcuteResult(true);
		logger.debug("out method verify");
		return verifyResult;
	}

}
