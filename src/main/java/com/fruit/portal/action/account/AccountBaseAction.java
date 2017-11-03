/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.action.account;

import com.fruit.account.biz.common.LoginLogTypeEnum;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserLoginInfo;
import com.fruit.portal.service.account.UserLoginService;
import com.fruit.portal.utils.BizConstants;
import com.fruit.portal.utils.CookieUtil;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : message-biz
 * File Name      : AccountBaseAction.java
 */
public class AccountBaseAction extends BaseAction
{
    @Autowired
    private UserLoginService userLoginService;

    public void userLogin(UserModel userModel, boolean autoLogin)
    {
        HttpServletResponse response = WebContext.getResponse();
        this.addUserCookie(userModel, autoLogin ? BizConstants.USER_COOKIE_AUTO_LOGIN_MAX_AGE : -1, response);
        CookieUtil.clearCookie(BizConstants.COOKIE_GUEST, this.envService.getCookieDomain(), WebContext.getRequest(), WebContext.getResponse());
        String userAgent = getUserAgent();
        this.userLoginService.userLoginLog(userModel, getUserIp(), userAgent, parseUserAgent(userAgent).getOperatingSystem().getName(), userModel.getMobile(), LoginLogTypeEnum.MANUAL.getType());
    }


    protected void addUserCookie(UserModel userModel, int maxAge, HttpServletResponse response)
    {
        int pwd = userModel.getPassword().hashCode();
        UserLoginInfo userLoginInfo = new UserLoginInfo(userModel.getUserId(), pwd,userModel.getOpenid());
        // 种用户登录详细信息cookie，保存用户的常用信息(加密)
        Cookie user = new Cookie(BizConstants.COOKIE_USER, CookieUtil.generatePassportByUserInfo(userLoginInfo));
        wechatTokenSet(CookieUtil.generatePassportByUserInfo(userLoginInfo),userModel);
        user.setDomain(super.envService.getCookieDomain());
        user.setPath("/");
        user.setMaxAge(maxAge);
        response.addCookie(user);
    }

    public void userLogout()
    {
        CookieUtil.clearCookie(BizConstants.COOKIE_USER, this.envService.getCookieDomain(), WebContext.getRequest(), WebContext.getResponse());
    }

    protected void wechatTokenSet(String token,UserModel userModel)
    {

    }


}
