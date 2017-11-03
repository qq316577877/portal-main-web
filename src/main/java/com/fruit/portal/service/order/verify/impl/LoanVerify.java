package com.fruit.portal.service.order.verify.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;

public class LoanVerify implements Verify {

	private static final Logger logger = LoggerFactory.getLogger(LoanVerify.class);
	
	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		VerifyResult verifyResult = new VerifyResult();
		//暂时没有校验逻辑, 预留后期资金服务校验
		verifyResult.setExcuteResult(true);
		logger.debug("out method verify");
		return verifyResult;
	}

}
