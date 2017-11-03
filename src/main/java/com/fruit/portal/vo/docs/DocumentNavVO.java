/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.vo.docs;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : fruit
 * File Name      : DocumentNavVO.java
 */
public class DocumentNavVO
{
    private String name;

    private boolean active;

    private String url;

    public DocumentNavVO(String name, String url)
    {
        this.url = url;
        this.name = name;
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
}
