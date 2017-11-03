package com.fruit.portal.service.econtract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.OrderService;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.ovft.contract.api.bean.EcontractSignBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.bean.VerifyCaptchaBean;
import com.ovft.contract.api.service.EcontractService;

@Service
public class DebtContractService {

	private static final Logger logger = LoggerFactory.getLogger(DebtContractService.class);

	@Autowired
	private LoanAuthCreditService loanAuthCreditService;

	@Autowired
	private LoanUserAuthInfoService loanUserAuthInfoService;

	@Autowired
	private LoanUserCreditInfoService loanUserCreditInfoService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private EcontractService econtractService;
	
	@Autowired
	private EnvService envService;

	/**
	 * 合同未生成发送短信
	 * 
	 * @param orderId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public AjaxResult sendAnxinCaptcha(String orderId) {
		AjaxResult ajaxResult = new AjaxResult();
		OrderDTO order = orderService.loadByNo(orderId);
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(order.getUserId());
//		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(order.getUserId());
		String smsTemplateId = "";
		if("product".equals(EnvService.getEnv())){
			//生产环境的时候
			smsTemplateId = envService.getConfig("contract.sms.template.id");
		}
		String source = envService.getConfig("contract.source");
		ResponseVo response = econtractService.sendCaptcha(new EcontractSignBean(loanUserAuthInfo.getCustomerId(),
				order.getTransactionNo(), 1,smsTemplateId,source));
		if (!response.isSuccess()) {
			logger.info("合同服务发送短信失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("合同服务发送短信失败 具体原因： " + response.getMessage());
			return ajaxResult;
		}
		return ajaxResult;
	}

	@SuppressWarnings("rawtypes")
	public AjaxResult verifyAnxinCaptcha(String orderId, String captchaCode) {
		AjaxResult ajaxResult = new AjaxResult();
		OrderDTO order = orderService.loadByNo(orderId);
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(order.getUserId());
//		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(order.getUserId());
		String source = envService.getConfig("contract.source");
		ResponseVo response = econtractService.verifyCaptcha(new VerifyCaptchaBean(loanUserAuthInfo.getCustomerId(),
				order.getTransactionNo(), captchaCode,source));
		if (!response.isSuccess()) {
			String errorMessage = response.getMessage();
			logger.info("合同服务校验短信失败 具体原因: {}", errorMessage);
			if(errorMessage.contains("短信验证码不正确") || errorMessage.contains("60030501")){
				ajaxResult.setCode(60030501);
				ajaxResult.setMsg("短信验证码不正确！");
			}else{
				ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
				ajaxResult.setMsg("合同服务授权失败！");
			}
		}
		return ajaxResult;
	}
}
