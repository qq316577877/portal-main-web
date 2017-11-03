package com.fruit.portal.service.order.verify.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.portal.service.order.verify.BaseVerifyEnum;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;
import com.fruit.portal.utils.ApplicationContextUtil;

public class BaseVerify implements Verify{

	private static final Logger logger = LoggerFactory.getLogger(BaseVerify.class);
	
	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		// 结果集
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getBaseVerifyEnums() != null) {
			for (BaseVerifyEnum verifyEnum : verifyInfo.getBaseVerifyEnums()) {
				Verify verify = (Verify) ApplicationContextUtil.getBean(verifyEnum.getValue());
				verifyResult = verify.verify(verifyInfo);
				// 有一个校验未通过，结束校验并直接返回
				if (!verifyResult.getExcuteResult()) {
					logger.debug("out method verify");
					return verifyResult;
				}
			}
		}
		logger.debug("out method verify");
		verifyResult.setExcuteResult(true);
		return verifyResult;
	}

}
