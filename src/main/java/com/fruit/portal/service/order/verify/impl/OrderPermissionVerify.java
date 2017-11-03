package com.fruit.portal.service.order.verify.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;

/**
 * 订单权限校验
 * 
 * @author ovfintech
 *
 */
public class OrderPermissionVerify implements Verify {

	private static final Logger logger = LoggerFactory.getLogger(OrderPermissionVerify.class);

	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getOldOrder() != null) {
			OrderDTO oldOrder = verifyInfo.getOldOrder();
			if (oldOrder.getUserId() != verifyInfo.getUserInfo().getUserId()) {
				verifyResult.setExcuteResult(false);
				verifyResult.setErrorCode(111007);
				verifyResult.setMsg("该用户没有权限修改此订单");
				logger.debug("out method verify");
				return verifyResult;
			}
		}
		verifyResult.setExcuteResult(true);
		logger.debug("out method verify");
		return verifyResult;
	}

}
