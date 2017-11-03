/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.web;

import com.fruit.portal.service.StaticVersionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
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
 * File Name      : StativVersionSerlvet.java
 */
public class StativVersionServlet extends HttpServlet
{
    @Autowired
    private StaticVersionService staticVersionService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String version = req.getParameter("version");
        String type = req.getParameter("type");
        String result = version;
        if (StringUtils.isNotBlank(version))
        {
            if("h5".equals(type))
            {
                try
                {
//                    this.staticVersionH5Service.resetVersion(version);
                }
                catch (RuntimeException e)
                {
                    result = "ERROR:" + e.getMessage();
                }
            }
            else
            {
                try
                {
//                    this.staticVersionService.resetVersion(version);
                }
                catch (RuntimeException e)
                {
                    result = "ERROR:" + e.getMessage();
                }
            }

        }
        resp.getWriter().print(result);
    }

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        this.staticVersionService = applicationContext.getBean(StaticVersionService.class);
    }
}
