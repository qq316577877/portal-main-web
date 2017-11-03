/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.interceptor;

import com.fruit.portal.utils.BizConstants;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 微信OpenId
 */
@Service("wechatOpenIdInterceptor")
public class WechatOpenIdInterceptor implements IDispatchInterceptor
{
    private final static Logger LOGGER = LoggerFactory.getLogger(WechatOpenIdInterceptor.class);

    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        HttpServletRequest request = WebContext.getRequest();

        String openId = request.getParameter("oid");
        if(StringUtils.isBlank(openId))
        {
            Cookie[] cookies = request.getCookies();
            if(cookies != null)
            {
                for (Cookie cookie : cookies)
                {
                    String name = cookie.getName();
                    if (BizConstants.COOKIE_WECHAT_OPENID.equals(name))
                    {
                        openId = cookie.getValue();
                    }
                }
            }
        }

        if(StringUtils.isNotBlank(openId))
        {
            request.setAttribute(BizConstants.ATTR_WECHAT_OPENID, openId);
        }

        return new InterceptorResult(true);
    }
}
