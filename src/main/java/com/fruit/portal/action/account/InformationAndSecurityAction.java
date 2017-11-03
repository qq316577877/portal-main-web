package com.fruit.portal.action.account;


import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserResponseInfo;
import com.fruit.portal.service.account.UserModifyService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 用户账号相关
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-28
 * Project        : message-biz
 * File Name      : InformationAndSecurityAction.java
 */
@Component
@UriMapping("/member/info")
public class InformationAndSecurityAction extends AccountBaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationAndSecurityAction.class);

    @Autowired
    private UserModifyService userModifyService;

    @UriMapping(value = "/", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String show()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);
            Map<String, Object> user = new HashMap<String, Object>();

            user.put("enterprise_name", userModel.getEnterpriseName());
            user.put("mobile", userModel.getMobile());
            user.put("type", userModel.getType());
            user.put("name", userModel.getName());
            user.put("identity", userModel.getIdentity());
            user.put("enterpriseVerifyStatus", userModel.getEnterpriseVerifyStatus());
            user.put("mail", userModel.getMail());
            user.put("phoneNum", userModel.getPhoneNum());
            user.put("qq", userModel.getQQ());


            user.put("member_audit_url",this.urlConfigService.getMemberEnterpriseVerifyUrl());
            HttpServletRequest request = WebContext.getRequest();
            setUserInfos(userModel, request);

            request.setAttribute("__DATA", super.toJson(user));
            return "/account/security";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/show].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    @UriMapping(value = "/level", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String showMemberLevel()
    {
        try
        {
            return "account/member_level";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/level].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    private void setUserInfos(UserModel userModel, HttpServletRequest request)
    {
        request.setAttribute("enterprise_name", userModel.getEnterpriseName());
        request.setAttribute("mobile", userModel.getMobile());
        request.setAttribute("qq", userModel.getQQ());
        request.setAttribute("password", "******");// 用户密码无须查询
        request.setAttribute("enterprise_url", UrlUtils.getMemberEnterpriseVerifyUrl(super.getDomain()));
        request.setAttribute("email", userModel.getMail());
    }

    @UriMapping(value = "/check_current_mobile_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult modifyMobileVerify()
    {
        try
        {
            Map<String, Object> validationResults = super.getParamsValidationResults();
            String captchaStr = (String) validationResults.get("captcha");

            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            String certificate = this.userModifyService.generateResetMobileCertificate(userModel, captchaStr);
            Map<String, String> result = new HashMap<String, String>();
            result.put("certificate", certificate);
            return new AjaxResult(result);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/check_current_mobile_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/new_mobile_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult modifyMobile()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Map<String, Object> validationResults = super.getParamsValidationResults();
            String newMobile = (String) validationResults.get("mobile");
            String certificate = (String) validationResults.get("certificate");
            String captcha = (String) validationResults.get("captcha");
            this.userModifyService.resetMobile(userModel, newMobile, certificate, captcha, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/new_mobile_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/binding_mail_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult bindingMail()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            String mail = super.getStringParameter("mail");
            Validate.isTrue(BizUtils.emailValidate(mail), "邮箱格式错误!");
            this.userModifyService.bindingMail(userModel, mail, super.getDomain(), super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/binding_mail_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/modify_pwd_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult modifyPwd()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);


            Map<String, Object> validationResults = super.getParamsValidationResults();
            String oldPwd = (String) validationResults.get("oldPassword");
            String newPwd = (String) validationResults.get("newPassword");

            newPwd = newPwd.trim();

            // check old pwd
            if (!this.userModifyService.checkPwd(userModel.getMobile(), oldPwd))
            {
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "旧密码错误!");
            }
            // modify pwd
            this.userModifyService.modifyPwd(userModel, newPwd, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/modify_pwd_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/modify_qq_ajax" ,interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult modifyQQ()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);


            Map<String, Object> validationResults = super.getParamsValidationResults();
            String qq = (String) validationResults.get("qq");

            this.userModifyService.modifyQQ(userModel, qq, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/modify_qq_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/modify_phonenum_ajax" ,interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult modifyPhoneNum()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);


            Map<String, Object> validationResults = super.getParamsValidationResults();
            String phoneNum = (String) validationResults.get("phoneNum");

            this.userModifyService.modifyPhoneNum(userModel, phoneNum, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/modify_phonenum_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询会员基本信息
     * @return
     */
    @UriMapping(value = "/get_user_information_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getUserInformation()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserResponseInfo userResponseInfo = new UserResponseInfo();
            BeanUtils.copyProperties(userModel, userResponseInfo);


            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(userResponseInfo);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/info/get_user_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

}
