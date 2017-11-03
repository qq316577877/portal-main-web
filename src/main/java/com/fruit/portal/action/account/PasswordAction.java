/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.action.account;

import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.service.account.UserModifyService;
import com.fruit.portal.utils.BizUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 会员密码相关(找回密码)
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-29
 * Project        : message-biz
 * File Name      : PasswordAction.java
 */
@Component
@UriMapping("/member/password")
public class PasswordAction extends AccountBaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordAction.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserModifyService userModifyService;

    @UriMapping(value = "/find")
    public String forgetPwd()
    {
        try {
            String token = super.getGuestToken();
            if (StringUtils.isNotBlank(token)) {
                String url = BizUtils.buildCaptchaUrl(token, "callback");
                WebContext.getRequest().setAttribute("CAPTCHA_URL", url);
            }
            return "/account/find_pwd";
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/password/find].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/check_mobile_captcha_ajax", interceptors = "validationInterceptor")
    public AjaxResult accountVerify()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        String mobile = (String) validationResults.get("mobile");
        String mobileCaptcha = (String) validationResults.get("mobileCaptcha");
        try
        {
            // 验证短信
            String certificate = this.userModifyService.generateResetPwdCertificate(mobile, mobileCaptcha);
            Map<String, String> data = new HashMap<String, String>();
            data.put("certificate", certificate);
            data.put("reset_url", super.getUrlConfigService().getResetPwdUrl(certificate));
            return new AjaxResult(data);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/password/check_mobile_captcha_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/reset")
    public String showReset()
    {
        try{
            return "/account/find_pwd_reset";
        }catch(IllegalArgumentException e)
        {
            LOGGER.error("[/member/password/reset].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/reset_password_ajax", interceptors = "validationInterceptor")
    public AjaxResult resetPassword()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        String newPwd = (String) validationResults.get("password");
        String certificate = (String) validationResults.get("certificate");
        try
        {
            UserModel userModel = this.userModifyService.resetPassword(certificate, newPwd, super.getUserIp());
            super.userLogin(userModel, true); // 老是忘记密码这种人就应该自动登录啊
            String url = super.getUrlConfigService().getResetPwdSucceedUrl(userModel.getMobile());
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("url", url);
            return new AjaxResult(jsonMap);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/password/reset_password_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "参数错误:" + e.getMessage());
        }
    }

    @UriMapping(value = "/reset_succeed")
    public String succeed()
    {
        try {
            String username = super.getStringParameter("username");
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("username", username);
            return "/account/find_pwd_ok";
        }catch(IllegalArgumentException e)
        {
            LOGGER.error("[/member/password/reset_succeed].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

}
