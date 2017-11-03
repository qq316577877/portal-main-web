/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.filter;

import com.alibaba.fastjson.JSON;
import com.fruit.portal.model.account.UserLoginInfo;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.BizConstants;
import com.fruit.portal.utils.CookieUtil;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.validation.utils.RequestParameterUtils;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeChatFilter implements Filter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatFilter.class);

    public static final int SYS_LOGIN_SIGN_EXPIRED_TIME = 60 * 30; //  30分钟有效

    public static final String OPENDID_KEY = "WEHCAT-OPENID-";

    private CacheClient cacheClient;

    private ApplicationContext applicationContext;

    private String env;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        ServletContext servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.env = EnvService.getEnv();
        this.cacheClient = (CacheClient) applicationContext.getBean("cacheClient");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException,
            ServletException
    {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String openid = request.getParameter("openid");
        if(StringUtils.isNotBlank(openid)) {

            String token = cacheClient.get(OPENDID_KEY+openid);
            if(StringUtils.isNotBlank(token)) {
                UserLoginInfo userLoginInfo = CookieUtil.getUserInfoFromPassport(token);
                request.setAttribute(BizConstants.COOKIE_USER, token);
                request.setAttribute(BizConstants.ATTR_UID, userLoginInfo.getUserId());
            }

        }
//        request.setAttribute(BizConstants.ATRR_PARAMETER_REQUEST,requesMap);
//
//        //解决跨域问题
        response.setContentType("text/json; charset=utf-8");

    //        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type");
        response.setHeader("Cache-Control", "no-cache");


        chain.doFilter(request, response);
    }

    private  <T> T getBodyObject(ServletInputStream servletInputStream,Class<T> c)
    {
        try
        {
            String str = IOUtils.toString(servletInputStream);
            return JSON.parseObject(str, c);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("参数错误");
        }
    }

    @Override
    public void destroy()
    {
    }

}
