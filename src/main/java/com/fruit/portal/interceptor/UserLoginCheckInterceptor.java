/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.interceptor;

import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.common.UrlConfigService;
import com.fruit.portal.utils.BizConstants;
import com.fruit.portal.utils.UrlUtils;
import com.fruit.portal.web.RedirectResult;
import com.ovfintech.arch.common.utils.WebUtils;
import com.ovfintech.arch.utils.useragent.OperatingSystem;
import com.ovfintech.arch.utils.useragent.UserAgent;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:    检查用户登录态，强制用户登录，未登录的用户区别ajax和http请求，
 * <pre>
 *
 *     ajax返回com.fruit.portal.model.AjaxResultCode#USER_UNLOGIN，同时返回登录url
 *     http 则直接跳转到登录页，同时附带登录完毕的跳转url
 *     </pre>
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-17
 * Project        : fruit
 * File Name      : LoginInterceptor.java
 */
@Service("userLoginCheckInterceptor")
public class UserLoginCheckInterceptor implements IDispatchInterceptor
{
    @Autowired
    private EnvService envService;

    @Autowired
    private UrlConfigService urlConfigService;

    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        InterceptorResult interceptorResult;

        HttpServletRequest request = WebContext.getRequest();
        int userId = BaseAction.getLoginUserId(request);
        if(userId <= 0)
        {
            interceptorResult = new InterceptorResult(false);
            boolean asyncRequest = WebUtils.isAsyncRequest(request);
            if(asyncRequest)
            {
                interceptorResult.setResult(createAjaxResponse(request));
            }
            else
            {
                String loginUrlWithRedir = this.urlConfigService.getLoginUrlWithRedir(UrlUtils.getDefaultRedirUrl(request));
                RedirectResult result = new RedirectResult(loginUrlWithRedir);
                interceptorResult.setResult(result);
            }
        }
        else
        {
            interceptorResult = new InterceptorResult(true);
        }
        return interceptorResult;
    }

    protected boolean isMobile(HttpServletRequest request)
    {
        UserAgent agent = (UserAgent) request.getAttribute(BizConstants.ATTR_USER_AGENT_MODEL);
        return agent != null
                && (agent.getOperatingSystem().getGroup() == OperatingSystem.ANDROID
                || agent.getOperatingSystem().getGroup() == OperatingSystem.IOS);
    }

    public AjaxResult createAjaxResponse(HttpServletRequest request)
    {
        String loginUrl = this.urlConfigService.getLoginUrl();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("url", loginUrl);
        AjaxResult ajaxResult = new AjaxResult(AjaxResultCode.USER_UNLOGIN.getCode(), "未登录");
        ajaxResult.setData(dataMap);
        return ajaxResult;

    }

}
