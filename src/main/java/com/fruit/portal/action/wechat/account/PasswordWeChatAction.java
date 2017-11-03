/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.action.wechat.account;

import com.fruit.portal.action.account.AccountBaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.service.account.UserModifyService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.utils.BizUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@UriMapping("/wechat/password")
public class PasswordWeChatAction extends AccountBaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordWeChatAction.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserModifyService userModifyService;

    @UriMapping(value = "/mobile_sendsms_ajax" )
    public AjaxResult passWordSendSms()
    {
        Map requesMap  = super.getBodyObject(HashMap.class);

        String mobile = (String) requesMap.get("mobile");
        int type =  Integer.parseInt(String.valueOf(  requesMap.get("type")));
        int userId = 0;
        try
        {
            boolean successful = messageService.sendSmsCaptcha(userId, mobile, type);
            if(!successful){
                throw new RuntimeException("sms send failed for: " + userId + "," + mobile + "," + type);
            }
            return new AjaxResult(AjaxResultCode.OK.getCode());
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/password/check_mobile_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    @UriMapping(value = "/check_mobile_captcha_ajax")
    public AjaxResult checkMoblieCaptcha()
    {
       // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        String mobile = (String) requesMap.get("mobile");
        String mobileCaptcha = (String) requesMap.get("mobileCaptcha");
        try
        {
            // 验证短信
            String certificate = this.userModifyService.generateResetPwdCertificate(mobile, mobileCaptcha);
            Map<String, String> data = new HashMap<String, String>();
            data.put("certificate", certificate);

            return new AjaxResult(data);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/password/check_mobile_captcha_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    @UriMapping(value = "/reset_password_ajax")
    public AjaxResult resetPassword()
    {
       // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        String newPwd = (String) requesMap.get("password");
        String certificate = (String) requesMap.get("certificate");
        try
        {
            UserModel userModel = this.userModifyService.resetPassword(certificate, newPwd, super.getUserIp());
            super.userLogin(userModel, true); // 老是忘记密码这种人就应该自动登录啊

            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("mobile", userModel.getMobile());
            jsonMap.put("username", userModel.getName());
            return new AjaxResult(jsonMap);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/password/reset_password_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "参数错误:" + e.getMessage());
        }
    }


}
