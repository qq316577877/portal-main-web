package com.fruit.portal.action.econtract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.service.econtract.DebtContractService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;

/**
 * 借据相关
 * 
 * @author ovfintech
 *
 */
@Component
@UriMapping("/debt")
public class DebtContractAction extends BaseAction {

	@Autowired
	private DebtContractService debtContractService;

	@SuppressWarnings("rawtypes")
	@UriMapping(value = "/order_send_anxin_ajax", interceptors = { "userLoginCheckInterceptor", "validationInterceptor" })
	public AjaxResult sendAnxinCaptcha() {
		String orderId = super.getStringParameter("orderId");
		return debtContractService.sendAnxinCaptcha(orderId);
	}

	@SuppressWarnings("rawtypes")
	@UriMapping(value = "/order_verify_anxin_ajax", interceptors = { "userLoginCheckInterceptor", "validationInterceptor" })
	public AjaxResult verifyAnxinCaptcha() {
		String orderId = super.getStringParameter("orderId");
		String captchaCode = super.getStringParameter("captchaCode");
		return debtContractService.verifyAnxinCaptcha(orderId, captchaCode);
	}
}
