package com.fruit.portal.action.loan;


import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.base.biz.dto.BankInfoDTO;
import com.fruit.base.biz.service.BankInfoService;
import com.fruit.loan.biz.common.*;
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
import com.fruit.portal.service.common.MessageService;
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
@UriMapping("/loan/auth")
public class LoanAuthAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanAuthAction.class);


    @Autowired
    private LoanAuthCreditService loanAuthCreditService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BankInfoService bankInfoService;

    @Autowired
    private BaseBankService baseBankService;

    @Autowired
    private LoanUserAuthInfoService loanUserAuthInfoService;

    @Autowired
    private EcontractService econtractService;


    @Autowired
    private UserAccountService userAccountService;



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
     * 实名认证页面
     * @return
     */
    @UriMapping(value = "/authentication", interceptors = {"userLoginCheckInterceptor"})
    public String authenticationPage()
    {
        int userId = getLoginUserId();
        try
        {

            //此处加预埋数据
            List<BankInfoDTO> bankList = this.baseBankService.loadAll();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("bankList", bankList);//银行列表信息
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("__DATA", super.toJson(data));


            return "/loan/loan_authentication";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/authentication].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    /**
     * 我的申请
     * @return
     */
    @UriMapping(value = "/loanAplication", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String myApplicationPage()
    {
        try
        {
            int userId = getLoginUserId();
            HttpServletRequest request = WebContext.getRequest();
            //如果没有，则返回的null
            LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanAuthCreditService.loadloanUserCreditInfoByUserId(userId);
            if(loanUserCreditInfoDTO == null){//未授信、没有申请过额度
                LoanUserAuthInfoModel loanUserAuthInfoModel = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
                if(loanUserAuthInfoModel!=null && loanUserAuthInfoModel.getStatus()==LoanUserAuthStatusEnum.PASS.getStatus()){
                    request.setAttribute("auth_status",1);//已实名认证
                    request.setAttribute("loan_url",UrlUtils.loanUserAuthSucceedUrl(super.getDomain()));//申请贷款
                }else{
                    request.setAttribute("auth_status",0);//未实名认证
                    request.setAttribute("auth_url",UrlUtils.getLoanAuthenticationUrl(super.getDomain()));//实名认证
                }
                return "/loan/loan_credit_line";
            }else{
                if(loanUserCreditInfoDTO.getCreditLine()==null ||
                        (loanUserCreditInfoDTO.getStatus()!= LoanUserCreditStatusEnum.PASS.getStatus()
                                && loanUserCreditInfoDTO.getStatus()!= LoanUserCreditStatusEnum.UNAUTHORIZED.getStatus())
                        ){//未授信
                    request.setAttribute("auth_status",1);//已实名认证
                    request.setAttribute("auth_url",UrlUtils.getLoanAuthenticationUrl(super.getDomain()));//实名认证
                    request.setAttribute("loan_url",UrlUtils.loanUserAuthSucceedUrl(super.getDomain()));//申请贷款
                    return "/loan/loan_credit_line";
                }else {
                    return "/loan/loan_apply_list";//我的资金申请服务
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/loanAplication].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    /**
     * 我的授信额度页面  ---即我的申请页面(未授信)
     * @return
     */
    @UriMapping(value = "/creditLine", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String creditLinePage()
    {
        int userId = getLoginUserId();
        try
        {
            return "/loan/loan_credit_line";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/creditLine].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }





    /**
     * 申请贷款-提交申请 页面
     * @return
     */
    @UriMapping(value = "/loanDatum", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String loanDatumPage()
    {
        int userId = getLoginUserId();
        try
        {
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("letter_url",UrlUtils.getLoanAuthLetterUrl(super.getDomain()));//个人信用报告查询授权书页面

            return "/loan/loan_datum";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/loanDatum].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    /**
     * 申请贷款-成功 页面
     * @return
     */
    @UriMapping(value = "/loanSuccess", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String loanSuccessPage()
    {
        int userId = getLoginUserId();
        try
        {
            return "/loan/loan_success";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/loanSuccess].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    /**
     * 个人信用报告查询授权书页面
     * @return
     */
    @UriMapping(value = "/loanAuthLetter", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String loanAuthLetterPage()
    {
        int userId = getLoginUserId();
        try
        {
            return "/loan/loan_auth_letter";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/loanAuthLetter].Exception:{}",e);
            return FTL_ERROR_400;
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
            LOGGER.error("[/loan/auth/get_loan_user_auth_information_ajax].Exception:{}",e);
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
            LOGGER.error("[/loan/auth/get_loan_user_apply_credit_information_ajax].Exception:{}",e);
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

            //如果没有，则返回的null
            LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanAuthCreditService.loadloanUserCreditInfoByUserId(userId);

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
                    }
                }
                //设置合同url
                loanUserCreditInfoDTO.setContractUrl(contractUrl);
            }


            int code = AjaxResultCode.OK.getCode();
            String msg = SUCCESS;
            AjaxResult ajaxResult = new AjaxResult(code, msg);

            ajaxResult.setData(loanUserCreditInfoDTO);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/get_loan_user_credit_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    /**
     * 申请实名认证
     * @return
     */
    @UriMapping(value = "/add_loan_user_auth_information_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult addLoanUserAuth()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        int userId = getLoginUserId();
        Map<String, Object> params = getParamsValidationResults();
        String username = (String) params.get("username");
        String identity = (String) params.get("identity");
        String mobile = (String) params.get("mobile");
        String captcha = (String) params.get("captcha");
        String bankCard = (String) params.get("bankCard");
        int bankId = (Integer) params.get("bankId");

        try
        {
            //先验证手机短信验证码
            messageService.expendCaptcha(mobile, captcha, SmsTypeEnum.NAME_AUTH.getValue());

            LoanUserAuthInfoModel loanUserAuth  = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
            if(loanUserAuth!=null && loanUserAuth.getStatus()==LoanUserAuthStatusEnum.PASS.getStatus()){
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "用户已实名认证!");
            }

            LoanUserAuthInfoDTO loanUserAuthInfoNow = loanUserAuthInfoService.loadByIdentity(identity);
            //如果该身份证，已经认证通过，则不允许再进行实名认证
            if(loanUserAuthInfoNow!=null && loanUserAuthInfoNow.getStatus()==LoanUserAuthStatusEnum.PASS.getStatus()){
                UserAccountDTO userAccountDTO = userAccountService.loadById(loanUserAuthInfoNow.getUserId());
                if(userAccountDTO!=null){
                    String accountMobile = userAccountDTO.getMobile();
                    String showMobile = accountMobile.substring(0, 3) + "****" + accountMobile.substring(7, accountMobile.length());
                    Validate.isTrue(loanUserAuthInfoNow==null,"该名称及证件号码已通过账号【"+showMobile+"】在本平台提交实名认证，请直接登录！若身份信息被冒用，可联系服务热线400-826-5128解决！");
                }
            }

            BankInfoDTO bankInfoDTO = bankInfoService.loadById(bankId);
            Validate.isTrue(null != bankInfoDTO, "传入的银行ID参数错误!");
            String bankName = bankInfoDTO.getName();

            LoanUserAuthInfoDTO loanUserAuthInfoDTO = new LoanUserAuthInfoDTO();
            loanUserAuthInfoDTO.setUserId(userId);
            loanUserAuthInfoDTO.setUsername(username);
            loanUserAuthInfoDTO.setIdentity(identity);
            loanUserAuthInfoDTO.setMobile(mobile);
            loanUserAuthInfoDTO.setBankId(bankId);
            loanUserAuthInfoDTO.setBankName(bankName);
            loanUserAuthInfoDTO.setBankCard(bankCard);

            LoanUserAuthInfoDTO loanUserAuthInfoAuthDTO = null;
            try {
                loanUserAuthInfoAuthDTO = loanAuthCreditService.addLoanUserAuth(loanUserAuthInfoDTO);
            } catch (Exception e) {
                LOGGER.error("[/loan/auth/add_loan_user_auth_information_ajax].Exception:{}",e);
                code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
//                msg = e.getMessage();
                msg = "实名认证失败，请确认输入信息后重新认证!";
                AjaxResult ajaxResult = new AjaxResult(code, msg);
                return ajaxResult;
            }

            Validate.isTrue(null != loanUserAuthInfoAuthDTO, "实名认证失败，请确认输入信息后重新认证!");
            if(loanUserAuthInfoAuthDTO.getStatus() != LoanUserAuthStatusEnum.PASS.getStatus()){
                code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
                msg = loanUserAuthInfoAuthDTO.getRejectNote();
            }


            AjaxResult ajaxResult = new AjaxResult(code, msg);

            HashMap<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("url", UrlUtils.loanUserAuthSucceedUrl(super.getDomain()));
            jsonMap.put("info", loanUserAuthInfoAuthDTO);

            ajaxResult.setData(jsonMap);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/add_loan_user_auth_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }




    /**
     * 查询申请贷款--所需婚姻状态所有list
     * @return
     */
    @UriMapping(value = "/get_loan_credit_maritalStatus_list_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getLoanMaritalStatusList()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            dataMap.put("statusList", LOAN_MARITALSTATUS_STATUS_LIST);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/get_loan_credit_maritalStatus_list_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 申请贷款--个人
     * @return
     */
    @UriMapping(value = "/add_loan_user_credit_personal_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult addLoanUserCreditPersonal()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        int userId = getLoginUserId();

        Map<String, Object> params = getParamsValidationResults();
        String username = (String) params.get("username");
        String identity = (String) params.get("identity");
        String mobile = (String) params.get("mobile");
        int maritalStatus = (Integer) params.get("maritalStatus");
        String partnerName = (String) params.get("partnerName");
        String partnerIdentity = (String) params.get("partnerIdentity");
        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        String address = (String) params.get("address");


        try
        {
            LoanUserApplyCreditDTO loanUserApplyCreditDTO = new LoanUserApplyCreditDTO();
            loanUserApplyCreditDTO.setType(UserTypeEnum.PERSONAL.getType());//个人
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
                LOGGER.error("[/loan/auth/add_loan_user_credit_personal_ajax].Exception:{}",e);
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
            jsonMap.put("url", UrlUtils.loanSucceedUrl(super.getDomain()));
            jsonMap.put("info",loanUserApplyCreditReturn);

            ajaxResult.setData(jsonMap);//如果申请失败，则在返回信息里面

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/add_loan_user_credit_personal_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }





    /**
     * 申请贷款--企业
     * @return
     */
    @UriMapping(value = "/add_loan_user_credit_enterprise_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult addLoanUserCreditEnterprise()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        int userId = getLoginUserId();
        Map<String, Object> params = getParamsValidationResults();
        String enterpriseName = (String) params.get("enterpriseName");
        String credential = (String) params.get("credential");
        String username = (String) params.get("username");
        String identity = (String) params.get("identity");
        String mobile = (String) params.get("mobile");
        int maritalStatus = (Integer) params.get("maritalStatus");
        String partnerName = (String) params.get("partnerName");
        String partnerIdentity = (String) params.get("partnerIdentity");
        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        String address = (String) params.get("address");

        try
        {
            LoanUserApplyCreditDTO loanUserApplyCreditDTO = new LoanUserApplyCreditDTO();
            loanUserApplyCreditDTO.setType(UserTypeEnum.ENTERPRISE.getType());//企业
            loanUserApplyCreditDTO.setUserId(userId);
            loanUserApplyCreditDTO.setEnterpriseName(enterpriseName);
            loanUserApplyCreditDTO.setCredential(credential);
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
                loanUserApplyCreditReturn = loanAuthCreditService.addLoanUserAuthEnterprise(loanUserApplyCreditDTO);
            } catch (Exception e) {
                LOGGER.error("[/loan/auth/add_loan_user_credit_personal_ajax].Exception:{}",e);
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
            jsonMap.put("url", UrlUtils.loanSucceedUrl(super.getDomain()));
            jsonMap.put("info",loanUserApplyCreditReturn);
            ajaxResult.setData(jsonMap);//如果申请失败，则在返回信息里面
            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/auth/add_loan_user_credit_personal_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }








}
