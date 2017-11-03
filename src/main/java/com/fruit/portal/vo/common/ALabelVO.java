/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.common;

import java.io.Serializable;

/**
 * Description:
 * 用于展示a标签信息的VO
 * Create Author  : terry
 * Create Date    : 2017-05-1512-12
 * Project        : message-biz
 * File Name      : ALabelVO.java
 */
public class ALabelVO implements Serializable
{
    private static final long serialVersionUID = 8207666229007919290L;

    private String url;

    private String label;

    public ALabelVO()
    {
    }

    public ALabelVO(String url, String label)
    {
        this.url = url;
        this.label = label;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }
}
