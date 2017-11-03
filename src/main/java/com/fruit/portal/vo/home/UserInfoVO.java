/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.home;

import java.io.Serializable;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-06-13
 * Project        : www.fruit.com
 * File Name      : UserInfoVO.java
 */
public class UserInfoVO implements Serializable
{
    private int login_status;

    public int getLogin_status()
    {
        return login_status;
    }

    public void setLogin_status(int login_status)
    {
        this.login_status = login_status;
    }
}
