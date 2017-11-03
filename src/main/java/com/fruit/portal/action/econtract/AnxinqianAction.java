package com.fruit.portal.action.econtract;


import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.service.econtract.LoanApplyQuotaService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovft.contract.api.service.EcontractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
 * 2017-08-08<br>
 * 接入电子合同
 * 
 * @author ovfintech
 *
 */
@Component
@UriMapping("/wechat/axq")
public class AnxinqianAction extends BaseAction {

	private static final Logger logger = LoggerFactory.getLogger(AnxinqianAction.class);

	@Autowired
	private LoanAuthCreditService loanAuthCreditService;

	@Autowired
	private LoanApplyQuotaService loanApplyQuotaService;

	@Autowired
	private LoanUserCreditInfoService loanUserCreditInfoService;

	@Autowired
	private EcontractService econtractService;

	/**
	 * 跳转获取额度页
	 * 
	 * @return
	 */
	@UriMapping(value = "/status", interceptors = "userLoginCheckInterceptor")
	public AjaxResult queryStatus() {
		int userId = super.getLoginUserId();

		return loanApplyQuotaService.getStatusOpenAccount(userId);



	}

	/**
	 * 开通安心签账户
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@UriMapping(value = "/openAccount", interceptors = "userLoginCheckInterceptor")
	public AjaxResult openAccount() {
		int userId = super.getLoginUserId();
		return loanApplyQuotaService.openAccount(userId);
	}

	/**
	 * 上传印章
	 * 
	 * @return
	 */
	@UriMapping(value = "/eseal", interceptors = "userLoginCheckInterceptor")
	public AjaxResult uploadEseal() {
		int userId = super.getLoginUserId();

		Map requesMap  = super.getBodyObject(HashMap.class);
		String path = String.valueOf(requesMap.get("sealPath"));

		return loanApplyQuotaService.createEseal(userId,path);

	}

}
