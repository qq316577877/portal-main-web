/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.filter;

import com.fruit.portal.service.common.EnvService;
import com.ovfintech.arch.utils.ip.RemoteIpGetter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * <p>
 * Create Author  : lilzhang
 * Create Date    : 2017-05-20
 * Project        : 0kuProject1
 * File Name      : BlacklistFilter.java
 */
public class BlacklistFilter implements Filter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BlacklistFilter.class);

    private ApplicationContext applicationContext;

    private EnvService envService;

    private Set<String> blacklist;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        ServletContext servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.envService = applicationContext.getBean(EnvService.class);
        String blacklistIps = this.envService.getConfig("ip.blacklist");

        if (StringUtils.isNotBlank(blacklistIps))
        {
            String[] ips = blacklistIps.split(",");
            blacklist = new HashSet<String>(ips.length);

            for (int i = 0; i < ips.length; i++)
            {
                String ip = StringUtils.trimToNull(ips[i]);
                if (ip != null)
                {
                    blacklist.add(ip);
                }
            }
        }
        int size = (blacklist == null ? 0 : blacklist.size());
        LOGGER.warn("[blacklist] init. size: " + size);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String remoteAddr = RemoteIpGetter.getRemoteAddr(httpServletRequest);
        if (blacklist != null && blacklist.contains(remoteAddr))
        {
            LOGGER.error(String.format("[blacklist] block ip: %s", remoteAddr));
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.getWriter().write("FORBIDDEN");
        }
        else
        {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy()
    {
    }
}
