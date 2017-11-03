package com.fruit.portal.service.order.verify.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.portal.service.order.verify.OrderVerifyEnum;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;

public class CreateLogisticsVerify implements Verify {

	private static final Logger logger = LoggerFactory.getLogger(CreateOrderVerify.class);

	private Map<String, Verify> verifyMaps = new HashMap<String, Verify>();

	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		// 结果集
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getOrderVerifyEnums() != null) {
			for (OrderVerifyEnum verifyEnum : verifyInfo.getOrderVerifyEnums()) {
				Verify verify = verifyMaps.get(verifyEnum.getValue());
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

	public Map<String, Verify> getVerifyMaps() {
		return verifyMaps;
	}

	public void setVerifyMaps(Map<String, Verify> verifyMaps) {
		this.verifyMaps = verifyMaps;
	}

}
