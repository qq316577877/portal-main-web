package com.fruit.portal.service.order.verify.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;
import com.fruit.portal.vo.order.OrderBasicInfo;

/**
 * 订单基本信息校验
 * 
 * @author ovfintech
 *
 */
public class OrderBasicVerify implements Verify {

	private static final Logger logger = LoggerFactory.getLogger(OrderBasicVerify.class);

	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getOrderInfo() != null) {
			OrderBasicInfo orderInfo = verifyInfo.getOrderInfo();
			int type = orderInfo.getType();
			if (type == 1 || type == 2) {
				verifyResult.setExcuteResult(true);
				logger.debug("out method verify");
				return verifyResult;
			}
		}
		verifyResult.setExcuteResult(false);
		verifyResult.setErrorCode(111006);
		verifyResult.setMsg("参数错误");
		logger.debug("out method verify");
		return verifyResult;
	}

}
