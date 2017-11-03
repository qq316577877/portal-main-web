/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.action.account;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.EnterpriseRequest;
import com.fruit.portal.service.StableInfoService;
import com.fruit.portal.service.account.EnterpriseService;
import com.fruit.portal.service.account.UserRegisterService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 用户注册相关
 * <p/>
 * Create Author  : Paul
 * Create Date    : 2017-05-16
 * Project        : account-biz
 * File Name      : RegisterAction.java
 */
@Component
@UriMapping("/member")
public class RegisterAction extends AccountBaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterAction.class);

    @Autowired
    private UserRegisterService userRegisterService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private StableInfoService stableInfoService;

    @Autowired
    private EnterpriseService enterpriseService;



    @UriMapping(value = "/register")
    public String showRegister()
    {
        try {
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("login_url", super.getUrlConfigService().getLoginUrl());
            String token = "";
            int loginUserId = super.getLoginUserId();
            if (loginUserId > 0)  // 已登录用户使用mobile
            {
                UserModel userModel = super.loadUserModel(loginUserId);
                token = userModel.getMobile();
            } else   // 未登录用户使用guest token
            {
                token = super.getGuestToken();
            }
            if (StringUtils.isNotBlank(token)) {
                String url = BizUtils.buildCaptchaUrl(token, "callback");
                request.setAttribute("CAPTCHA_URL", url);
            }
            request.setAttribute("user_agreement_url", UrlUtils.getDocumentServiceUrl(super.getDomain()));
            request.setAttribute("forget_pwd_url", UrlUtils.getForgetPwdUrl(super.getDomain()));
            return "account/register";
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/register].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/check_mobile_ajax" ,interceptors = "validationInterceptor")
    public AjaxResult isMobileRegisteredOrNot()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        String mobile = (String) validationResults.get("mobile");

        try
        {
            if (this.userRegisterService.isMobileRegistered(mobile))
            {
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "该手机号已注册!");
            }
            return new AjaxResult(AjaxResultCode.OK.getCode(), "该手机号未注册!");
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/check_mobile_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/is_mobile_registered_ajax" ,interceptors = "validationInterceptor")
    public AjaxResult isMobileRegistered()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        String mobile = (String) validationResults.get("mobile");
        try
        {
            if (this.userRegisterService.isMobileRegistered(mobile))
            {
                return new AjaxResult();
            }
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "该手机号未注册!");
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/is_mobile_registered_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/is_mobile_not_registered_ajax",interceptors = "validationInterceptor")
    public AjaxResult isMobileNotRegistered()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        String mobile = (String) validationResults.get("mobile");
        try
        {
            if (this.userRegisterService.isMobileRegistered(mobile))
            {
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "手机号码已注册，请直接登录!");
            }
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/is_mobile_not_registered_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/register_account_ajax", interceptors = {"validationInterceptor"})
    public AjaxResult registerAccount()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        String password = (String) validationResults.get("password");
        String mobile = (String) validationResults.get("mobile");
        String captcha = (String) validationResults.get("mobileCaptcha");
        String qq = (String) validationResults.get("qq");
        String email = (String) validationResults.get("email");
//        int userId = 0;
        try
        {
            messageService.expendCaptcha(mobile, captcha, SmsTypeEnum.USER_REGISTER.getValue());
            // 注册成功直接登录
            int addUserId = super.getTraceUserId();
            UserModel userModel = this.userRegisterService.registerAccount(password, mobile, qq,email, addUserId, super.getUserIp());
            super.userLogin(userModel, false);
            HashMap<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("url", UrlUtils.getRegisterSucceedUrl(super.getDomain()));
            return new AjaxResult(jsonMap);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/register_account_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/register/enterprise")
    public String showRegisterEnterprise()
    {
        try
        {
            int type = super.getIntParameter("type", UserTypeEnum.ENTERPRISE.getType());
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("register_succeed_url", UrlUtils.getRegisterSucceedUrl(super.getDomain()));
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("meta_country", metadataProvider.getCountryOnlyIndexMap().values());
            WebContext.getRequest().setAttribute("__DATA", super.toJson(data));

            String ftl = "account/register_enterprise";
            if (type == UserTypeEnum.PERSONAL.getType())
            {
                ftl = "account/register_personal";
            }
            return ftl;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/register_enterprise].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/register_succeed", interceptors = "userLoginCheckInterceptor")
    public String succeed()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = stableInfoService.getUserModel(userId);

            WebContext.getRequest().setAttribute("mobile", StringUtils.isBlank(userModel.getEnterpriseName()) ? userModel.getMobile() : userModel.getEnterpriseName());

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("register_enterprise_url", UrlUtils.getRegisterEnterpriseUrl(super.getDomain()));


            return "account/register_succeed.ftl";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/register_succeed].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    @UriMapping(value = "/register/enterpriseIn" ,interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String showRegisterEnterpriseIn()
    {
        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);
            EnterpriseRequest enterpriseRequest = enterpriseService.loadEnterpriseByUserId(userId,userModel);

            HttpServletRequest request = WebContext.getRequest();
            Map<String, Object> data = new HashMap<String, Object>();

            String ftl = "";

            if(enterpriseRequest!=null){
                ftl = "account/member_audit_status";
                data.put("url",UrlUtils.getRegisterEnterpriseUrl(super.getDomain()));
                data.put("enterpriseInfo", enterpriseRequest);
            }else{
                int type = super.getIntParameter("type", UserTypeEnum.ENTERPRISE.getType());
                request.setAttribute("register_succeed_url", UrlUtils.getRegisterSucceedUrl(super.getDomain()));

                if (type == UserTypeEnum.PERSONAL.getType())
                {
                    ftl = "account/register_personal_in";
                }else{
                    ftl = "account/register_enterprise_in";
                }
            }

            data.put("meta_country", metadataProvider.getCountryOnlyIndexMap().values());
            request.setAttribute("__DATA", super.toJson(data));

            return ftl;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/register/enterpriseIn].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    @UriMapping(value = "/register/memberLevel")
    public String showMemberLevel()
    {
        try
        {
            return "account/member_level";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/register/memberLevel].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

}
