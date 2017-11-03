package com.fruit.portal.action.wechat.account;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.loan.biz.common.LoanUserAuthStatusEnum;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.EnterpriseRequest;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.account.EnterpriseService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.service.econtract.LoanApplyQuotaService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业认证相关
 * <p/>
 * Create Author  : terry
 * Create  Time   : 2017-05-16
 * Project        : portal
 */
@Component
@UriMapping("/wechat/enterprise/auth")
public class UserEnterpiseWeChatAction extends BaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEnterpiseWeChatAction.class);

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private LoanAuthCreditService loanAuthCreditService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private LoanUserAuthInfoService loanUserAuthInfoService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private LoanApplyQuotaService loanApplyQuotaService;


    @UriMapping(value = "/is_enter_name_available_ajax",interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult isEnterpriseNameAvailable()
    {
        Map requesMap  = super.getBodyObject(HashMap.class);

        String enterpriseName = StringUtils.trimToEmpty((String) requesMap.get("enterpriseName"));

         try
        {
            int userId = getLoginUserId();

            if (enterpriseService.isEnterpriseNameAvailable(userId, enterpriseName))
            {
                return new AjaxResult();
            }
            else
            {
                return new AjaxResult(AjaxResultCode.REQUEST_MISSING_PARAM.getCode(), "企业名已存在!");
            }
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/enterprise/auth/is_enter_name_available_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    /**
     * 查询会员认证信息(个人与企业通用)
     * @return
     */
    @UriMapping(value = "/get_user_auth_information_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getUserAuthInformation()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            EnterpriseRequest enterpriseRequest = enterpriseService.loadEnterpriseByUserId(userId,userModel);


            AjaxResult ajaxResult = new AjaxResult(code, msg);

            ajaxResult.setData(enterpriseRequest);


            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/enterprise/auth/get_user_auth_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询认证状态
     * @return
     */
    @UriMapping(value = "/auth_status_ajax", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult getUserAuthStatus()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            Map<String, Object> dataMap = enterpriseService.getMemberInfoByUserId(userId);

            Map<String,Object> rstMap  = new HashMap<>();

            rstMap.put("status",dataMap.get("status"));
            rstMap.put("statusDesc",dataMap.get("statusDesc"));

            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(rstMap);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/enterprise/auth/auth_status_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询认证结果
     * @return
     */
    @UriMapping(value = "/enterprise_auth_info_ajax", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult getUserAuthInfo()
    {

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            Map<String, Object> dataMap = enterpriseService.getMemberInfoByUserId(userId);

            AjaxResult ajaxResult = loanApplyQuotaService.getStatusOpenAccount(userId);

            if(ajaxResult.getCode() == AjaxResultCode.OK.getCode()){
               int status = (int) ajaxResult.getData();
                dataMap.put("axqStatus",status);
            }

            ajaxResult.setData(dataMap);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/enterprise/auth/enterprise_auth_info_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 认证第一步：验证身份、银行卡等
     * @return
     */
    @UriMapping(value = "/check_auth_first_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult checkPersonalAuthFirst()
    {
        Map requesMap  = super.getBodyObject(HashMap.class);

        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        int userId = getLoginUserId();
        int type = Integer.parseInt(String.valueOf(requesMap.get("type")));//认证类型

        String username = StringUtils.trimToEmpty((String) requesMap.get("username"));
        String identity = (String) requesMap.get("identity");//身份证
        String bankCard = (String) requesMap.get("bankCard");
        String mobile = (String) requesMap.get("mobile");
        String captcha = (String) requesMap.get("captcha");


        try
        {
            //先验证手机短信验证码
           messageService.expendCaptcha(mobile, captcha, SmsTypeEnum.NAME_AUTH.getValue());

            LoanUserAuthInfoModel loanUserAuth  = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
            if(loanUserAuth!=null && loanUserAuth.getStatus()== LoanUserAuthStatusEnum.PASS.getStatus()){
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


            LoanUserAuthInfoDTO loanUserAuthInfoDTO = new LoanUserAuthInfoDTO();
            loanUserAuthInfoDTO.setUserId(userId);
            loanUserAuthInfoDTO.setUsername(username);
            loanUserAuthInfoDTO.setIdentity(identity);
            loanUserAuthInfoDTO.setMobile(mobile);
            loanUserAuthInfoDTO.setBankCard(bankCard);
            loanUserAuthInfoDTO.setType(type);

            LoanUserAuthInfoDTO loanUserAuthInfoAuthDTO = null;
            try {
                loanUserAuthInfoAuthDTO = loanAuthCreditService.addLoanUserAuth(loanUserAuthInfoDTO);
            } catch (Exception e) {
                LOGGER.error("[/wechat/enterprise/auth/check_auth_first_ajax].Exception:{}",e);
                code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
//                msg = e.getMessage();
                msg = "会员认证失败，请确认输入信息后重新认证!";
                AjaxResult ajaxResult = new AjaxResult(code, msg);
                return ajaxResult;
            }

            Validate.isTrue(null != loanUserAuthInfoAuthDTO, "会员认证失败，请确认输入信息后重新认证!");
            if(loanUserAuthInfoAuthDTO.getStatus() != LoanUserAuthStatusEnum.PASS.getStatus()){
                code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
                msg = loanUserAuthInfoAuthDTO.getRejectNote();
            }


            AjaxResult ajaxResult = new AjaxResult(code, msg);

            HashMap<String, Object> jsonMap = new HashMap<String, Object>();

            jsonMap.put("info", loanUserAuthInfoAuthDTO);

            ajaxResult.setData(jsonMap);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/enterprise/auth/check_auth_first_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    /**
     * 提交认证：个人
     * @return
     */
    @UriMapping(value = "/personal_auth_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult savePersonalAuth()
    {
        Map requesMap  = super.getBodyObject(HashMap.class);

        int type = UserTypeEnum.PERSONAL.getType();//个人认证
        int countryId = Integer.parseInt(String.valueOf( requesMap.get("countryId")));
        int provinceId = Integer.parseInt(String.valueOf( requesMap.get("provinceId")));
        int cityId = Integer.parseInt(String.valueOf( requesMap.get("cityId")));
        int districtId = Integer.parseInt(String.valueOf( requesMap.get("districtId")));//行政区
        String address = (String) requesMap.get("address");//详细地址
        String phoneNum = (String) requesMap.get("phoneNum");//联系电话
        String identityFront = (String) requesMap.get("identityFront");//身份证前面
        String identityBack = (String) requesMap.get("identityBack");//身份证后面

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);
            LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(userId);
            Validate.notNull(loanUserAuthInfoDTO,"请先通过第一步认证.");
            String name = loanUserAuthInfoDTO.getUsername();//实名认证后的姓名
            String identity = loanUserAuthInfoDTO.getIdentity();//实名认证后的身份证号码

            EnterpriseRequest enterpriseRequest = new EnterpriseRequest();

            enterpriseRequest.setType(type);
            enterpriseRequest.setName(name);
            enterpriseRequest.setPhoneNum(phoneNum);
            enterpriseRequest.setIdentity(identity);
            enterpriseRequest.setCountryId(countryId);
            enterpriseRequest.setProvinceId(provinceId);
            enterpriseRequest.setCityId(cityId);
            enterpriseRequest.setDistrictId(districtId);
            enterpriseRequest.setAddress(address);
            enterpriseRequest.setIdentityFront(identityFront);
            enterpriseRequest.setIdentityBack(identityBack);
//            enterpriseRequest.setAttachmentOne(attachmentOne);
//            enterpriseRequest.setAttachmentTwo(attachmentTwo);

            enterpriseService.enterpriseAuth(userModel,  enterpriseRequest,super.getUserIp());

            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/enterprise/auth/personal_auth_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 提交认证  企业
     * @return
     */
    @UriMapping(value = "/enterprise_auth_ajax", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult saveEnterpriseAuth()
    {

        Map requesMap  = super.getBodyObject(HashMap.class);

        String enterpriseName = StringUtils.trimToEmpty((String) requesMap.get("enterpriseName"));
        String credential = (String) requesMap.get("credential");//证照号

        int type = UserTypeEnum.ENTERPRISE.getType();//企业认证

        int countryId = Integer.parseInt(String.valueOf( requesMap.get("countryId")));
        int provinceId = Integer.parseInt(String.valueOf( requesMap.get("provinceId")));
        int cityId = Integer.parseInt(String.valueOf( requesMap.get("cityId")));
        int districtId = Integer.parseInt(String.valueOf( requesMap.get("districtId")));//行政区
        String address = (String) requesMap.get("address");
        String phoneNum = (String) requesMap.get("phoneNum");//联系电话
        String identityFront = (String) requesMap.get("identityFront");
        String identityBack = (String) requesMap.get("identityBack");
//        String licence = (String) requesMap.get("licence");//营业执照 或 社会信用代码证图片

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(userId);
            Validate.notNull(loanUserAuthInfoDTO,"请先通过第一步认证.");
            String name = loanUserAuthInfoDTO.getUsername();//实名认证后的姓名
            String identity = loanUserAuthInfoDTO.getIdentity();//实名认证后的身份证号码

            EnterpriseRequest enterpriseRequest = new EnterpriseRequest();

            enterpriseRequest.setType(type);
            enterpriseRequest.setEnterpriseName(enterpriseName);
            enterpriseRequest.setName(name);
            enterpriseRequest.setPhoneNum(phoneNum);
            enterpriseRequest.setIdentity(identity);
            enterpriseRequest.setCountryId(countryId);
            enterpriseRequest.setProvinceId(provinceId);
            enterpriseRequest.setCityId(cityId);
            enterpriseRequest.setDistrictId(districtId);
            enterpriseRequest.setAddress(address);
            enterpriseRequest.setIdentityFront(identityFront);
            enterpriseRequest.setIdentityBack(identityBack);
            enterpriseRequest.setCredential(credential);
//            enterpriseRequest.setLicence(licence);

            enterpriseService.enterpriseAuth(userModel,  enterpriseRequest,super.getUserIp());

            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/enterprise/auth/enterprise_auth_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


}
