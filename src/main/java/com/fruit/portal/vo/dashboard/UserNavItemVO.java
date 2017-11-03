/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.vo.dashboard;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : fruit
 * File Name      : UserNavItemVO.java
 */
public class UserNavItemVO
{
    private String name;

    private boolean active;

    private String url;

    private int warn_num;

    public UserNavItemVO(String name, String url, boolean active)
    {
        this.name = name;
        this.url = url;
        this.active = active;
    }

    public UserNavItemVO(String name, String url)
    {
       this(name, url, true);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getWarn_num()
    {
        return warn_num;
    }

    public void setWarn_num(int warn_num)
    {
        this.warn_num = warn_num;
    }
}
