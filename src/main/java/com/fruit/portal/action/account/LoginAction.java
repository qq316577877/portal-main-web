package com.fruit.portal.action.account;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.service.account.UserLoginService;
import com.fruit.portal.service.account.UserModifyService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.UrlUtils;
import com.fruit.portal.web.RedirectResult;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/*
 * Create Author  : Paul
 * Create  Time   : 17/5/16 下午21:43
 * Project        : portal
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

/**
 * 登录相关
 */
@Component
@UriMapping("/member")
public class LoginAction extends AccountBaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAction.class);

    public static final String V1_LOGIN_URL = "http://v1.auth.0ku.com/inventory/business/validate";

    public static final int SYS_LOGIN_SIGN_EXPIRED_TIME = 1000 * 30; //  30秒钟有效

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserModifyService userModifyService;

    @Autowired
    private UserLoginService userLoginService;

    @UriMapping(value = "/login")
    public String show()
    {
        try {
            int userId = super.getLoginUserId();
            if (userId > 0) {
                String redir = this.getRedirectUrl();
                return "redirect:" + redir + "/home";
            }

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("forget_pwd_url", UrlUtils.getForgetPwdUrl(super.getDomain()));
            return "/account/login";
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/login].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    protected String getRedirectUrl()
    {
        String redir = super.getStringParameter("redir");
        if(StringUtils.isNotBlank(redir))
        {
            try
            {
                redir = URLDecoder.decode(redir, BizUtils.UTF_8);
            }
            catch (UnsupportedEncodingException e)
            {
                redir = super.getHomeUrl();
            }
        }
        else
        {
            redir = super.getHomeUrl();
        }
        return redir;
    }

    /**
     * 1.查询用户是否存在(手机号)；<br/>
     *
     * @return
     */
    @UriMapping(value = "/login_ajax")
    public AjaxResult login()
    {
        Map requesMap  = super.getBodyObject(HashMap.class);
        String mobile =  (String) requesMap.get("mobile");
        String password = (String) requesMap.get("password");
        int autoLogin = (Integer) requesMap.get("auto_login");
//
//        Map<String, Object> paramsValidated = super.getParamsValidationResults();
//        String mobile = (String) paramsValidated.get("mobile");
//        String password = (String) paramsValidated.get("password");
//        int autoLogin = (Integer) paramsValidated.get("auto_login");
        try
        {

            this.loginWithPassword(mobile, password, autoLogin);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/login]，mobile：" + mobile + "，password：" + password+".Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    protected int loginWithPassword(String mobile, String password, int autoLogin)
    {
        UserAccountDTO userAccountDTO = memberService.loadUserAccount(mobile);
        Validate.isTrue(null != userAccountDTO, "账号密码错误!");
        UserModel userModel = null;
        if (StringUtils.isNotBlank(userAccountDTO.getPassword())) // 密码不为空
        {
            userModel = this.userLoginService.userLogin(mobile, password);
            Validate.isTrue(null != userModel, "服务繁忙，请重试");
            super.userLogin(userModel, autoLogin == 1);
            return userModel.getUserId();
        }
        else // 密码为空
        {
            throw new IllegalArgumentException("请输入登录密码，如果忘记密码，请点击“忘记密码”重置密码");
        }

    }

    @UriMapping(value = "/logout")
    public RedirectResult logout()
    {
        try {
            int id = super.getLoginUserId();
            if (id > 0) {
                super.userLogout();
            }
            RedirectResult result = new RedirectResult(super.getUrlConfigService().getLoginUrl());    //特殊情况，不允许添加默认的redir，防止跳转到logout
            return result;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/logout].Exception:{}",e);
            return new RedirectResult(FTL_ERROR_400);
        }
    }

}
