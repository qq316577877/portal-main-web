package com.fruit.portal.service.order.verify.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.account.biz.dto.UserSupplierDTO;
import com.fruit.portal.service.account.SupplierService;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;
import com.fruit.portal.vo.order.OrderBasicInfo;

/**
 * 供应商信息校验
 * 
 * @author ovfintech
 *
 */
public class SupplierVerify implements Verify {

	@Resource
	private SupplierService supplierService;

	private static final Logger logger = LoggerFactory.getLogger(SupplierVerify.class);

	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getOrderInfo() != null) {
			OrderBasicInfo orderInfo = verifyInfo.getOrderInfo();
			int supplierId = orderInfo.getSupplierId();
			if (supplierId == 0) {
				verifyResult.setExcuteResult(false);
				verifyResult.setErrorCode(111005);
				verifyResult.setMsg("参数错误");
				logger.debug("out method verify");
				return verifyResult;
			}
			UserSupplierDTO userSupplier = supplierService.loadSupplierById(supplierId);
			if (userSupplier == null) {
				verifyResult.setExcuteResult(false);
				verifyResult.setErrorCode(111006);
				verifyResult.setMsg("供应商不存在");
				logger.debug("out method verify");
				return verifyResult;
			}
		}
		verifyResult.setExcuteResult(true);
		logger.debug("out method verify");
		return verifyResult;
	}

}
