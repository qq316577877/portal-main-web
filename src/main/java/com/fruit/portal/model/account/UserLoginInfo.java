/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.model.account;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-17
 * Project        : www.fruit.com
 * File Name      : UserLoginInfo.java
 */
public class UserLoginInfo
{
    private int userId;

    private int pwdCode;

    private String openid;

    private long loginTime;

    public UserLoginInfo(int userId, int pwdCode)
    {
        this(userId, pwdCode, System.currentTimeMillis());
    }

    public UserLoginInfo(int userId, int pwdCode, long loginTime)
    {
        this.userId = userId;
        this.pwdCode = pwdCode;
        this.loginTime = loginTime;
    }

    public UserLoginInfo(int userId, int pwdCode,String openid)
    {
        this(userId, pwdCode, openid,System.currentTimeMillis());
    }

    public UserLoginInfo(int userId, int pwdCode, String openid,long loginTime)
    {
        this.userId = userId;
        this.pwdCode = pwdCode;
        this.loginTime = loginTime;
        this.openid = openid;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getPwdCode()
    {
        return pwdCode;
    }

    public void setPwdCode(int pwdCode)
    {
        this.pwdCode = pwdCode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(long loginTime)
    {
        this.loginTime = loginTime;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(userId).append("|").append(pwdCode).append("|").append(openid).append('|').append(loginTime);
        return sb.toString();
    }
}
