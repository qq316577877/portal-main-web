/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.account;

import com.fruit.portal.meta.MetaProvince;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-28
 * Project        : message-biz
 * File Name      : CityAndAddressVO.java
 */
public class CityAndAddressVO<T> implements Serializable
{
    private static final long serialVersionUID = 6487258539295280317L;

    private Map<Integer, MetaProvince> meta_city;

    private List<T> address_list;

    public Map<Integer, MetaProvince> getMeta_city()
    {
        return meta_city;
    }

    public void setMeta_city(Map<Integer, MetaProvince> meta_city)
    {
        this.meta_city = meta_city;
    }

    public List<T> getAddress_list()
    {
        return address_list;
    }

    public void setAddress_list(List<T> address_list)
    {
        this.address_list = address_list;
    }
}
