/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.account;

import com.fruit.portal.meta.MetaArea;
import com.fruit.portal.meta.MetaCity;
import com.fruit.portal.meta.MetaCountry;
import com.fruit.portal.meta.MetaProvince;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-18 11-28
 * Project        : account-biz
 * File Name      : CountryProCityAreaVO.java
 */
public class CountryProCityAreaVO<T> implements Serializable
{
    private static final long serialVersionUID = 6487258539295280317L;

    private Map<Integer, MetaCountry> meta_country;

    private Map<Integer, MetaProvince> meta_province;

    private Map<Integer, MetaCity> meta_city;

    private Map<Integer, MetaArea> meta_area;

    private List<T> address_list;

    public Map<Integer, MetaCountry> getMeta_country() {
        return meta_country;
    }

    public void setMeta_country(Map<Integer, MetaCountry> meta_country) {
        this.meta_country = meta_country;
    }

    public Map<Integer, MetaProvince> getMeta_province() {
        return meta_province;
    }

    public void setMeta_province(Map<Integer, MetaProvince> meta_province) {
        this.meta_province = meta_province;
    }

    public Map<Integer, MetaCity> getMeta_city() {
        return meta_city;
    }

    public void setMeta_city(Map<Integer, MetaCity> meta_city) {
        this.meta_city = meta_city;
    }

    public Map<Integer, MetaArea> getMeta_area() {
        return meta_area;
    }

    public void setMeta_area(Map<Integer, MetaArea> meta_area) {
        this.meta_area = meta_area;
    }

    public List<T> getAddress_list() {
        return address_list;
    }

    public void setAddress_list(List<T> address_list) {
        this.address_list = address_list;
    }
}
