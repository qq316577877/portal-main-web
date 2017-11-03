/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.action.common;

import com.fruit.portal.action.BaseAction;
import com.fruit.portal.meta.MetaCountry;
import com.fruit.portal.meta.MetaProvince;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-17
 * Project        : www.fruit.com
 * File Name      : CityAction.java
 */
@Component
@UriMapping(value = "/common")
public class CountryAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryAction.class);

    @Autowired
    private MetadataProvider metadataProvider;

    @UriMapping(value = "/supported_countries_ajax")
    public AjaxResult<Collection<MetaCountry>> getAllCountries()
    {
        try{
            Collection<MetaCountry> countries = this.metadataProvider.getCountryOnlyIndexMap().values();
            return new AjaxResult<Collection<MetaCountry>>(countries);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/common/supported_countries_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }

    }

    @UriMapping(value = "/supported_cities_ajax")
    public AjaxResult<Collection<MetaProvince>> getAllCities()
    {
        //获取省市区三级城市
        try{
            int countryId = super.getIntParameter("countryId");
            MetaCountry country = this.metadataProvider.getCountry(countryId);
            Validate.notNull(country);
            return new AjaxResult<Collection<MetaProvince>>(country.getProvinces());
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/common/supported_cities_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }

    }

}
