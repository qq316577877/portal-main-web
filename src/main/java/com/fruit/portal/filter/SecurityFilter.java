/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.filter;

import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.validation.utils.RequestParameterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SecurityFilter implements Filter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    private ApplicationContext applicationContext;

    private String env;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        ServletContext servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.env = EnvService.getEnv();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        LOGGER.warn("[security] get url = {} ", UrlUtils.getFullUrl(httpServletRequest));

        Map<String, String> paramMap = RequestParameterUtils.buildParamMap(httpServletRequest);
        String sign = paramMap.remove("sign");
        String timestamp = paramMap.remove("timestamp");
        boolean valid = true;
                //SecurityUtils.validateEncrypt(this.cacheCluster.getAppkey(), this.cacheCluster.getAppSecret(), sign, paramMap, timestamp, 1.0F);
        if (this.env.equals("product") && !valid)
        {
            LOGGER.error("[security] unauthorized operation url = {} ", UrlUtils.getFullUrl(httpServletRequest));
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().write("unauthorized request");
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
