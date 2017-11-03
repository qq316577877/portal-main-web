/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.exception;

import com.fruit.portal.service.ContextManger;
import com.fruit.portal.service.common.EnvService;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.UserAgentEnvUtils;
import com.ovfintech.arch.utils.useragent.OperatingSystem;
import com.ovfintech.arch.utils.useragent.UserAgent;
import com.ovfintech.arch.validation.utils.RequestParameterUtils;
import com.ovfintech.arch.web.mvc.common.WebUtils;
import com.ovfintech.arch.web.mvc.dispatch.exception.ExceptionHandler;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.utils.JacksonMapper;
import com.fruit.portal.utils.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service("portalExceptionHandler")
public class PortalExceptionHandler implements ExceptionHandler
{
    public static final String ERROR_PATH = "error.path";

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalExceptionHandler.class);

    @Autowired(required = false)
    private List<EventPublisher> publisherList;

    @Autowired
    private EnvService envService;

    @Override
    public void handle(HttpAccessException e, HttpServletRequest request, HttpServletResponse response)
    {
        Map<String, String> paramMap = RequestParameterUtils.buildParamMap(request);
        String params = ToStringBuilder.reflectionToString(paramMap);
        LOGGER.error("exception occur " + UrlUtils.getFullUrl(request) +
                ", params:" + params +", Context=" + ContextManger.getContext() +
                ", status " + e.getStatus() + ", code "
                + e.getErrorCode() + ", msg " + e.getMessage() , e);

        if (WebUtils.isAsyncRequest(request))
        {
            try
            {
                AjaxResult ajaxResult = new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), "系统繁忙，请稍后重试");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(JacksonMapper.toJson(ajaxResult));
            }
            catch (IOException e1)
            {
                LOGGER.error("write result error", e1);
            }
        }
        else
        {
            response.setStatus(e.getStatus());

            try
            {
                String errorUrl = UrlUtils.getErrorUrl(this.envService.getDomain(), 500);
                if(e.getStatus() == HttpServletResponse.SC_NOT_FOUND)
                {
                    errorUrl = UrlUtils.getErrorUrl(this.envService.getDomain(), 404);
                }
                response.sendRedirect("http:" + errorUrl);
            }
            catch (IOException io)
            {
                LOGGER.error("MVC-ExHandler", io);
            }
        }

        if (StringUtils.isNotBlank(e.getErrorCode()) && !(e.getStatus() == HttpServletResponse.SC_NOT_FOUND || e.getStatus() == HttpServletResponse.SC_FORBIDDEN))
        {
            EventHelper.triggerEvent(this.publisherList, e.getErrorCode(), e.getMessage() + "," + params + ":" + ContextManger.getContext(), EventLevel.IMPORTANT, e, WebUtils.getRemoteIpAddr(request));
        }
    }

    protected boolean isMobileWeb(HttpServletRequest req)
    {
        boolean result;
        String userAgent = req.getHeader("User-Agent");
        UserAgent userAgent1 = UserAgentEnvUtils.parseUserAgent(userAgent);
        OperatingSystem os = userAgent1.getOperatingSystem();
        switch (os.getGroup())
        {
            case ANDROID:
            case IOS:
            {
                result = true;
                break;
            }
            default:
            {
                result = false;
            }
        }
        return result;
    }
}
