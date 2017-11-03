package com.fruit.portal.action.econtract;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fruit.portal.model.AjaxResultCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.econtract.LoanApplyQuotaService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;

/**
 * 2017-08-08<br>
 * 接入电子合同
 * 
 * @author ovfintech
 *
 */
@Component
@UriMapping("/loan/auth")
public class LoanApplyAction extends BaseAction {

	private static final Logger logger = LoggerFactory.getLogger(LoanApplyAction.class);

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
	@UriMapping(value = "/quota/apply", interceptors = "userLoginCheckInterceptor")
	public String toApplyQuota() {
		int userId = super.getLoginUserId();
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		if (loanUserAuthInfo != null && loanUserAuthInfo.getCustomerId() != 0) {
			logger.info("已完成开户  自动跳转到签署借款合同页");
			return "redirect:" + this.urlConfigService.getSignBorrowUrl();
		}
		return "/loan/loan_certificate";
	}

	/**
	 * 开通安心签账户 并上传印章
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@UriMapping(value = "/contract/account_open_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult openAccount() {
		int userId = super.getLoginUserId();
		String path = super.getStringParameter("sealPath");
		return loanApplyQuotaService.openAccount(userId, path);
	}

	/**
	 * 跳转到签订借款合同页
	 * 
	 * @return
	 */
	@UriMapping(value = "/quota/borrow", interceptors = "userLoginCheckInterceptor")
	public String borrow() {
		int userId = super.getLoginUserId();
		HttpServletRequest request = WebContext.getRequest();
		Map<String, Object> data = new HashMap<String, Object>();
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);
		if (loanUserCreditInfoDTO != null
				&& (loanUserCreditInfoDTO.getStatus() == LoanUserCreditStatusEnum.TO_CONFIRM.getStatus() || loanUserCreditInfoDTO
						.getStatus() == LoanUserCreditStatusEnum.PASS.getStatus())) {
			logger.info("客户已完成签署合同自动跳转到我的申请");
			return "redirect:" + this.urlConfigService.getLoanMyAplicationUrl();
		}

		if (StringUtils.isBlank(loanUserCreditInfoDTO.getCtrBankNo()) || StringUtils.isBlank(loanUserCreditInfoDTO.getInsureCtrNo())) {
			logger.info("您的合同信息客户经理正在快马加鞭的录入哦，请稍后再试!");
			data.put("errMsg","您的合同信息客户经理正在快马加鞭的录入哦，请稍后再试!");
		}else {
			long contractId = loanApplyQuotaService.createBorrowContract(userId);
			String source = envService.getConfig("contract.source");
			ResponseVo response = econtractService.queryContractUrlById(new QueryEContractBean(contractId, source));
			String contractPath = "";
			if (!response.isSuccess()) {
				logger.info("查询借款合同地址失败 具体原因: {}", response.getMessage());
			}
			contractPath = (String) response.getData();
			data.put("contractId", contractId);
			data.put("contractPath", contractPath);
		}


		request.setAttribute("__DATA", super.toJson(data));
		return "/loan/loan_signcontract";
	}

	/**
	 * 签署合同发送短信验证码
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@UriMapping(value = "/contract/captcha_send_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult anxinsendCaptcha() {
		int userId = super.getLoginUserId();
		return loanApplyQuotaService.anxinsendCaptcha(userId);
	}

	/**
	 * 客户签署借款合同<br>
	 * 考虑签署合同性能问题<br>
	 * 银行签署合同不和客户一起签署<br>
	 * 客户签署借款合同后 推送到消息队列中<br>
	 * 通过侦听队列 让银行进行进行签名<br>
	 * 此举意义拆分签署合同 提高性能
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@UriMapping(value = "/contract/online_sign_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult signBorrowContract() {
		int userId = super.getLoginUserId();
		String contractId = super.getStringParameter("contractId");
		String captchaCode = super.getStringParameter("captchaCode");
		return loanApplyQuotaService.signBorrowContract(userId, Long.parseLong(contractId), captchaCode);
	}
}
