package com.fruit.portal.action.wechat.loan;


import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.base.biz.dto.BankInfoDTO;
import com.fruit.base.biz.service.BankInfoService;
import com.fruit.loan.biz.common.LoanUserApplyCreditStatusEnum;
import com.fruit.loan.biz.common.LoanUserAuthStatusEnum;
import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.loan.biz.common.LoanUserMarriageCodeEnum;
import com.fruit.loan.biz.dto.LoanUserApplyCreditDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanAuthCreditModel;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.BaseBankService;
import com.fruit.portal.service.account.EnterpriseService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.service.econtract.LoanApplyQuotaService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.fruit.portal.utils.UrlUtils;
import com.fruit.portal.vo.common.IdValueVO;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资金服务申请相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-06-14
 * Project        : portal
 */
@Component
@UriMapping("/wechat/loan/auth")
public class LoanAuthWeChatAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanAuthWeChatAction.class);


    @Autowired
    private LoanAuthCreditService loanAuthCreditService;

    @Autowired
    private EcontractService econtractService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private LoanApplyQuotaService loanApplyQuotaService;



    private static final List<IdValueVO> LOAN_MARITALSTATUS_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        LoanUserMarriageCodeEnum[] values = LoanUserMarriageCodeEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanUserMarriageCodeEnum status : values)
            {
                LOAN_MARITALSTATUS_STATUS_LIST.add(new IdValueVO(status.getCode(), status.getMessage()));
            }
        }
    }




    /**
     * 查询用户的实名认证信息
     * @return
     */
    @UriMapping(value = "/get_loan_user_auth_information_ajax" , interceptors = {"userLoginCheckInterceptor" })
    public AjaxResult showLoanAuthInfoDetails()
    {
        try {
            int userId = getLoginUserId();

            LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);

            int code = AjaxResultCode.OK.getCode();
            String msg = SUCCESS;
            AjaxResult ajaxResult = new AjaxResult(code, msg);

            ajaxResult.setData(loanUserAuthInfo);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/auth/get_loan_user_auth_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }





    /**
     * 查询用户的申请贷款信息
     * @return
     */
    @UriMapping(value = "/get_loan_user_apply_credit_information_ajax" , interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult showLoanApplyCreditInfoDetails()
    {
        try {
            int userId = getLoginUserId();

            //如果没有，则返回的null
            LoanAuthCreditModel loanUserApplyCredit = loanAuthCreditService.loadloanUserApplyCreditInfoByUserId(userId);

            int code = AjaxResultCode.OK.getCode();
            String msg = SUCCESS;
            AjaxResult ajaxResult = new AjaxResult(code, msg);

            ajaxResult.setData(loanUserApplyCredit);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/auth/get_loan_user_apply_credit_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 查询用户的信贷信息-贷款额度等
     * @return
     */
    @UriMapping(value = "/get_loan_user_credit_information_ajax" , interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult showLoanUserCreditInfoDetails()
    {
        try {
            int userId = getLoginUserId();

            Map<String,Object> dataMap = new HashMap<String,Object>();

            //如果没有，则返回的null
            LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanAuthCreditService.loadloanUserCreditInfoByUserId(userId);

            //实名认证信息
            LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);

            if(loanUserAuthInfo == null || loanUserAuthInfo.getStatus() != LoanUserAuthStatusEnum.PASS.getStatus()){
                return  new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(),"请先通过会员认证");
            }

            if(loanUserCreditInfoDTO!=null){
                // 获取合同ID 查询合同URL
                Long contractId =loanUserCreditInfoDTO.getContractId();
                //dubbo接口查询合同URL地址
                String contractUrl = "";
                if(contractId > 0) {
                    String source = envService.getConfig("contract.source");
                    ResponseVo response = econtractService.queryContractUrlById(new QueryEContractBean(contractId,source));//
                    if (response.isSuccess()) {
                        contractUrl = (String) response.getData();

                        //微信端电子合同展示  需要转换一下
                        String contractUrlDomain = envService.getConfig("contract.domain");
                        String contractUrlFormatterDomain = envService.getConfig("contract.formatter.domain");
                        LOGGER.info("get_loan_user_credit_information_ajax转换前合同地址: {}", contractUrl);
                        contractUrl = contractUrl.replace(contractUrlDomain,contractUrlFormatterDomain);
                        LOGGER.info("get_loan_user_credit_information_ajax转换后合同地址: {}", contractUrl);
                    }
                }
                //设置合同url
                loanUserCreditInfoDTO.setContractUrl(contractUrl);
            }

            dataMap.put("loanUserCreditInfo",loanUserCreditInfoDTO);

            int code = AjaxResultCode.OK.getCode();
            String msg = SUCCESS;
            AjaxResult ajaxResult = new AjaxResult(code, msg);

            //查询印章
            int customerId = loanUserAuthInfo.getCustomerId();
            dataMap.put("stepFlag","1"); //默认第一步
            if(customerId > 0){
                ajaxResult = loanApplyQuotaService.getEseal(loanUserAuthInfo.getCustomerId());
                Map sealMap = (Map) ajaxResult.getData();
                if(sealMap != null && !sealMap.isEmpty()){
                    dataMap.put("stepFlag","2");
                }
            }


            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/auth/get_loan_user_credit_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 申请贷款
     * @return
     */
    @UriMapping(value = "/add_loan_user_credit_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult addLoanUserCreditPersonal()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        int userId = getLoginUserId();

        Map requesMap  = super.getBodyObject(HashMap.class);

        String username = (String) requesMap.get("username");//申请人
        String identity = (String) requesMap.get("identity");//身份证号码
        String mobile = (String) requesMap.get("mobile");//联系电话
        int maritalStatus = Integer.parseInt(String.valueOf(requesMap.get("maritalStatus")));//婚姻状况
        String partnerName = (String) requesMap.get("partnerName");//配偶姓名
        String partnerIdentity = (String) requesMap.get("partnerIdentity");//配偶身份证号码
        int countryId = Integer.parseInt(String.valueOf(requesMap.get("countryId")));
        int provinceId = Integer.parseInt(String.valueOf(requesMap.get("provinceId")));
        int cityId = Integer.parseInt(String.valueOf( requesMap.get("cityId")));
        int districtId =Integer.parseInt(String.valueOf( requesMap.get("districtId")));
        String address = (String) requesMap.get("address");//详细地址


        try
        {
            //是否实名认证，会员认证
            UserEnterpriseDTO userEnterpriseDTO =  enterpriseService.loadEnterpriseByUserId(userId);
            Validate.notNull(userEnterpriseDTO,"请先通过会员认证.");


            LoanUserApplyCreditDTO loanUserApplyCreditDTO = new LoanUserApplyCreditDTO();
            loanUserApplyCreditDTO.setType(UserTypeEnum.PERSONAL.getType());//类型  默认为个人.

            if(userEnterpriseDTO.getType() == UserTypeEnum.ENTERPRISE.getType()){
                loanUserApplyCreditDTO.setEnterpriseName(userEnterpriseDTO.getEnterpriseName());
                loanUserApplyCreditDTO.setCredential(userEnterpriseDTO.getCredential());
                loanUserApplyCreditDTO.setType(UserTypeEnum.ENTERPRISE.getType());//类型
            }

            loanUserApplyCreditDTO.setUserId(userId);
            loanUserApplyCreditDTO.setUsername(username);
            loanUserApplyCreditDTO.setIdentity(identity);
            loanUserApplyCreditDTO.setMobile(mobile);
            loanUserApplyCreditDTO.setMaritalStatus(maritalStatus);
            loanUserApplyCreditDTO.setPartnerName(partnerName);
            loanUserApplyCreditDTO.setPartnerIdentity(partnerIdentity);
            loanUserApplyCreditDTO.setCountryId(countryId);
            loanUserApplyCreditDTO.setProvinceId(provinceId);
            loanUserApplyCreditDTO.setCityId(cityId);
            loanUserApplyCreditDTO.setDistrictId(districtId);
            loanUserApplyCreditDTO.setAddress(address);




            //申请贷款
            LoanAuthCreditModel loanUserApplyCreditReturn = null;
            try {
                loanUserApplyCreditReturn = loanAuthCreditService.addLoanUserAuthPersonal(loanUserApplyCreditDTO);
            } catch (Exception e) {
                LOGGER.error("[/wehat/loan/auth/add_loan_user_credit_ajax].Exception:{}",e);
                code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
//                msg = e.getMessage();
                msg = "申请贷款失败！";
                AjaxResult ajaxResult = new AjaxResult(code, msg);
                return ajaxResult;
            }

            Validate.isTrue(null != loanUserApplyCreditReturn, "申请贷款失败!");
            //如果未成功
            if(loanUserApplyCreditReturn.getStatus() != LoanUserApplyCreditStatusEnum.PASS.getStatus()){
                code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
                msg = loanUserApplyCreditReturn.getRejectNote();
            }

            AjaxResult ajaxResult = new AjaxResult(code, msg);
            HashMap<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("info",loanUserApplyCreditReturn);

            ajaxResult.setData(jsonMap);//如果申请失败，则在返回信息里面

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/auth/add_loan_user_personal_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }









}
