/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.web;

import com.fruit.portal.action.BaseAction;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.BizConstants;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.utils.ip.RemoteIpGetter;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : RedirectServlet.java
 */
public class RedirectServlet extends HttpServlet
{
//    private UrlRedirectService redirectService;

    private EnvService envService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String result = "";
        String requestURI = req.getRequestURI();
        if(StringUtils.isNotBlank(requestURI))
        {
            String path = requestURI.replace("/p/", "");
            if(StringUtils.isBlank(path))
            {
                result = "http://" + UrlUtils.getErrorUrl(this.envService.getDomain(), 404);
            }
            else
            {
                String userAgent = BizUtils.abbreviate(req.getHeader(BaseAction.USER_AGENT), 250);
                String userIp = RemoteIpGetter.getRemoteAddr(req);

//                UrlDispatchRequest request = new UrlDispatchRequest();
//                request.setUserAgent(userAgent);
//                request.setUserIp(userIp);
//                request.setUserToken(getGuestToken(req));
//                request.setUserId(BaseAction.getLoginUserId(req));
//                request.setPath(path);
//                UrlDispatchResponse response = this.redirectService.dispatch(request);
//                if(response.isSuccessful())
//                {
//                    result = response.getRedir();
//                    String queryString = req.getQueryString();
//                    if(StringUtils.isNotBlank(queryString))
//                    {
//                        result += ("?" + queryString);
//                    }
//
//                    this.addTraceCookie(resp, request.getPath());
//                }
//                else
//                {
//                    result = "http://" + UrlUtils.getErrorUrl(this.envService.getDomain(), 404);
//                }
            }
        }
        else
        {
            result = UrlUtils.getErrorUrl(this.envService.getDomain(), 404);
        }

        resp.sendRedirect(result);
    }

    private void addTraceCookie(HttpServletResponse resp, String path)
    {
        Cookie cookie = new Cookie(BizConstants.COOKIE_SHORT_URL_TRACE, path);
        cookie.setDomain(this.envService.getCookieDomain());
        cookie.setMaxAge(10 * 3600);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
//        this.redirectService = applicationContext.getBean(UrlRedirectService.class);
        this.envService = applicationContext.getBean(EnvService.class);
    }

    protected String getGuestToken(HttpServletRequest request)
    {
        Object tokenObj  = request.getAttribute(BizConstants.COOKIE_GUEST);

        String guestToken = "";
        if(tokenObj instanceof String)
        {
            guestToken = (String)tokenObj;
        }
        return guestToken;
    }
}
