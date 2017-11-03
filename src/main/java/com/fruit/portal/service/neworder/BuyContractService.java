package com.fruit.portal.service.neworder;


import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.ovft.contract.api.bean.EcontractSignBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.bean.VerifyCaptchaBean;
import com.ovft.contract.api.service.EcontractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyContractService {

	private static final Logger logger = LoggerFactory.getLogger(BuyContractService.class);

	@Autowired
	private LoanAuthCreditService loanAuthCreditService;

	@Autowired
	private OrderNewInfoService orderNewInfoService;

	@Autowired
	private EcontractService econtractService;
	
	@Autowired
	private EnvService envService;

	/**
	 * 合同未生成发送短信
	 * 
	 * @param orderNo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public AjaxResult sendAnxinCaptcha(String orderNo) {
		AjaxResult ajaxResult = new AjaxResult();
		OrderNewInfoDTO order = orderNewInfoService.loadByOrderNo(orderNo);
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(order.getUserId());
		String smsTemplateId = "";
		if("product".equals(EnvService.getEnv())){
			//生产环境的时候
			smsTemplateId = envService.getConfig("contract.sms.template.id");
		}
		String source = envService.getConfig("contract.source");
		ResponseVo response = econtractService.sendCaptcha(new EcontractSignBean(loanUserAuthInfo.getCustomerId(),
				order.getOrderSerialNo(), 0,smsTemplateId,source));
		if (!response.isSuccess()) {
			logger.info("合同服务发送短信失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("合同服务发送短信失败 具体原因： " + response.getMessage());
			return ajaxResult;
		}
		return ajaxResult;
	}

	@SuppressWarnings("rawtypes")
	public AjaxResult verifyAnxinCaptcha(String orderNo, String captchaCode) {
		AjaxResult ajaxResult = new AjaxResult();
		OrderNewInfoDTO order = orderNewInfoService.loadByOrderNo(orderNo);
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(order.getUserId());
		String source = envService.getConfig("contract.source");
		ResponseVo response = econtractService.verifyCaptcha(new VerifyCaptchaBean(loanUserAuthInfo.getCustomerId(),
				order.getOrderSerialNo(), captchaCode,source));
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
