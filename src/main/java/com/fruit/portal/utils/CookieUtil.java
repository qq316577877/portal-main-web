/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.utils;

import com.fruit.portal.model.account.UserLoginInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-18
 * Project        : message-biz
 * File Name      : CookieUtil.java
 */
public class CookieUtil
{

    /**
     * 根据用户信息生成passport cookie
     *
     * @param userInfo
     * @return
     */
    public static String generatePassportByUserInfo(UserLoginInfo userInfo)
    {
        if (null == userInfo)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(userInfo.getPwdCode()).append("|");
        sb.append(userInfo.getUserId()).append('|');
        sb.append(userInfo.getOpenid()).append('|');
        sb.append(userInfo.getLoginTime());
        return AESHelper.encryptHexString(sb.toString());
    }

    /**
     * 根据passport cookie解析出用户信息: id, password, loginTime
     *
     * @param passport
     * @return
     */
    public static UserLoginInfo getUserInfoFromPassport(String passport)
    {
        String decrypted = AESHelper.decrypt(passport);
        Validate.notNull(decrypted, "unauthorized access");

        String [] strs = decrypted.split("\\|");
        Validate.isTrue(strs.length == 4, "unauthorized access");

        int pwdCode = NumberUtils.toInt(strs[0], 0);
        int userId = NumberUtils.toInt(strs[1], 0);
        String openId = String.valueOf(strs[2]);
        long loginTime = NumberUtils.toLong(strs[3], 0);
        Validate.isTrue(userId > 0, "unauthorized access");

        return new UserLoginInfo(userId, pwdCode, openId,loginTime);
    }

    public static void clearCookie(String name, String domain, HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0)
        {
            return;
        }
        for (Cookie cookie : cookies)
        {
            if (name.equals(cookie.getName()))
            {
                Cookie newCookie = new Cookie(cookie.getName(), "");
                newCookie.setMaxAge(0);
                newCookie.setDomain(domain);
                newCookie.setPath("/");
                response.addCookie(newCookie);
            }
        }
    }
}
