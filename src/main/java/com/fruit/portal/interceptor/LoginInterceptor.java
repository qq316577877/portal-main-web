/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.interceptor;

import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import org.springframework.stereotype.Service;

/**
 * Description:    处理用户登录情况
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1503-17
 * Project        : Beauty
 * File Name      : LoginInterceptor.java
 */
@Service("loginInterceptor")
public class LoginInterceptor implements IDispatchInterceptor
{
    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        InterceptorResult interceptorResult;
        interceptorResult = new InterceptorResult(true);
        return interceptorResult;
    }
}
