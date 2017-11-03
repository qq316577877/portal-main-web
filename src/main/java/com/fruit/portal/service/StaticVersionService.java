/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service;

import com.fruit.base.biz.dto.StaticFileVersionDTO;
import com.fruit.base.biz.service.StaticFileVersionService;
import com.fruit.portal.service.common.EnvService;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : StaticVersionService.java
 */
@Service
public class StaticVersionService implements InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticVersionService.class);

    private static final String STATIC_VERSION_PREFIX = "fruit.static.file.version";

    private static final String PROJECT = "portal-main-web";

    @Resource
    protected CacheClient cacheClient;

    @Autowired
    private EnvService envService;

    @Autowired
    private StaticFileVersionService staticFileVersionService;

    private String staticServer;

    public String getStaticServer()
    {
        String result = this.cacheClient.get(STATIC_VERSION_PREFIX);
        if (StringUtils.isEmpty(result))
        {
            String staticVersion = this.loadStaticVersion();
            result = staticServer + (StringUtils.isNotBlank(staticVersion) ? "/" + staticVersion : "");
            this.cacheClient.set(STATIC_VERSION_PREFIX, result);
        }
        return result;
    }

    private String loadStaticVersion()
    {
        StaticFileVersionDTO versionDTO = this.staticFileVersionService.loadLatestByProject(PROJECT);
        String staticVersion = null;
        if (versionDTO != null)
        {
            staticVersion = versionDTO.getVersion();
        }
        LOGGER.warn("[StaticVersion] current static version is {}", staticVersion);
        return staticVersion;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.staticServer = this.envService.getConfig("static.server");
    }
}
