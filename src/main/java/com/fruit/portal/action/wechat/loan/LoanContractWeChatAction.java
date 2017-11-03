package com.fruit.portal.action.wechat.loan;


import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.action.loan.LoanAuthAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.econtract.LoanApplyQuotaService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.fruit.portal.service.loan.LoanContractService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户资金服务电子合同相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-06-14
 * Project        : portal
 */
@Component
@UriMapping("/wechat/loan")
public class LoanContractWeChatAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanAuthAction.class);


    @Autowired
    private LoanApplyQuotaService loanApplyQuotaService;

    @Autowired
    private LoanUserCreditInfoService loanUserCreditInfoService;

    @Autowired
    private EcontractService econtractService;

    @Autowired
    private LoanAuthCreditService loanAuthCreditService;



    /**
     * 判断是否开户
     *
     * @return
     */
    @UriMapping(value = "/contract/check", interceptors = "userLoginCheckInterceptor")
    public AjaxResult chectContractStatus() {
        int userId = super.getLoginUserId();
        Map<String,Object>  data = new HashMap<>();
        LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
        if (loanUserAuthInfo != null && loanUserAuthInfo.getCustomerId() != 0) {
            LOGGER.info("已完成开户  自动跳转到签署借款合同页");
            data.put("status","1");
        }else{
            data.put("status","0");
        }

        return new AjaxResult(data);
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

        Map requesMap  = super.getBodyObject(HashMap.class);

        String sealPath = (String) requesMap.get("sealPath");
        //过滤掉目录
        int index = sealPath.lastIndexOf("/");
        if(index >= 0){
            sealPath = sealPath.substring(index+1);
        }

        return loanApplyQuotaService.openOrEsealAccount(userId, sealPath);
    }


    /**
     * 签订借款合同页
     *
     * @return
     */
    @UriMapping(value = "/contract/borrow_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult borrow() {
        int userId = super.getLoginUserId();

        LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);
        if (loanUserCreditInfoDTO != null
                && (loanUserCreditInfoDTO.getStatus() == LoanUserCreditStatusEnum.TO_CONFIRM.getStatus() || loanUserCreditInfoDTO
                .getStatus() == LoanUserCreditStatusEnum.PASS.getStatus())) {
            LOGGER.info("客户已完成签署合同自动跳转到我的申请");
            return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(),"您已完成签署合同.");
        }

        if (StringUtils.isBlank(loanUserCreditInfoDTO.getCtrBankNo()) || StringUtils.isBlank(loanUserCreditInfoDTO.getInsureCtrNo())) {
            LOGGER.info("您的合同信息客户经理正在快马加鞭的录入哦，请稍后再试!");
            return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(),"您的合同信息客户经理正在快马加鞭的录入哦，请稍后再试!");
        }



        long contractId = loanApplyQuotaService.createBorrowContract(userId);
        String source = envService.getConfig("contract.source");
        ResponseVo response = econtractService.queryContractUrlById(new QueryEContractBean(contractId,source));
        String contractPath = "";
        if (!response.isSuccess()) {
            LOGGER.info("查询借款合同地址失败 具体原因: {}", response.getMessage());
        }
        contractPath = (String) response.getData();
        //微信端电子合同展示  需要转换一下
        String contractUrlDomain = envService.getConfig("contract.domain");
        String contractUrlFormatterDomain = envService.getConfig("contract.formatter.domain");
        LOGGER.info("转换前借款合同地址: {}", contractPath);
        contractPath = contractPath.replace(contractUrlDomain,contractUrlFormatterDomain);
        LOGGER.info("转换后借款合同地址: {}", contractPath);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("contractId", contractId);
        data.put("contractPath", contractPath);

        return new AjaxResult(data);
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

        Map requesMap  = super.getBodyObject(HashMap.class);

        Long contractId = Long.parseLong(String.valueOf(requesMap.get("contractId")));
        String captchaCode = (String) requesMap.get("captchaCode");
        return loanApplyQuotaService.signBorrowContract(userId, contractId, captchaCode);
    }


}
