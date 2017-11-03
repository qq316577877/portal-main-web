/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.web;

import com.fruit.portal.utils.BizUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.result.DispatchResult;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DirectWriterResult implements DispatchResult
{
    private String result;

    public DirectWriterResult(String result)
    {
        this.result = result;
    }


    @Override
    public void handle(UriMeta uriMeta, Object response) throws IOException
    {
        HttpServletResponse resp = WebContext.getResponse();
        resp.setCharacterEncoding(BizUtils.UTF_8);
        resp.setContentLength(result.getBytes(BizUtils.UTF_8).length);
        resp.getWriter().write(result);
    }
}