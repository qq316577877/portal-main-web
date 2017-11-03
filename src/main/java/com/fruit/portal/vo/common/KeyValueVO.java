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
 * 用于展示String类型键值对的VO
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1512-28
 * Project        : message-biz
 * File Name      : KeyValueVO.java
 */
public class KeyValueVO implements Serializable
{
    private static final long serialVersionUID = -5984675572089338172L;

    private String key;

    private String value;

    public KeyValueVO()
    {
    }

    public KeyValueVO(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
