/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service.common;

import com.dianping.open.common.config.ConfigParamService;
import com.fruit.portal.config.EncryptPropertyPlaceholderConfigurer;
import com.ovfintech.arch.config.Environment;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Properties;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-19
 * Project        : fruit
 * File Name      : EnvService.java
 */
@Service
public class EnvService implements ConfigParamService, InitializingBean
{
    private Properties properties;

    private static String env;

    private static String domain;

    private static String securityDomain;

    private static String cookieDomain;

    public String getConfig(String key)
    {
        String property = this.properties.getProperty(key);
        return EncryptPropertyPlaceholderConfigurer.decryptProperty(property);
    }

    public String getDomain()
    {
        return domain;
    }

    public String getSecurityDomain()
    {
        return securityDomain;
    }

    public String getCookieDomain()
    {
        return cookieDomain;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.properties = new Properties();
        env = Environment.getEnv();
        String filePath = "config/properties/${env}.properties";
        filePath = filePath.replace("${env}", env);
        InputStream inputStream = EnvService.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream != null)
        {
            this.properties.load(inputStream);
            domain = this.getConfig("domain");
            securityDomain = this.getConfig("domain.security");
            cookieDomain = this.getConfig("cookie.domain");
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static String getEnv()
    {
        return env;
    }

    @Override
    public String getParam(String param)
    {
        return this.properties.getProperty(param);
    }

    @Override
    public String getRootKey()
    {
        return "";
    }


    /**
     * 获取当前环境，如果是正式环境，则访问地址都是https
     * @return
     */
    public static String getCurrentConfigOfHttp(){
        String value;
        if ("product".equals(getEnv())) {
            value = "https:";
        }else{
            value = "http:";
        }
        return value;
    }
}
