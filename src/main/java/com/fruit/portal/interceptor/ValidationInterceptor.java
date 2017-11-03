/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.interceptor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.utils.BizConstants;
import com.ovfintech.arch.common.utils.WebUtils;
import com.ovfintech.arch.validation.ValidationFactory;
import com.ovfintech.arch.validation.exception.BusinessException;
import com.ovfintech.arch.validation.utils.RequestParameterUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-19
 */
@Service("validationInterceptor")
public class ValidationInterceptor implements IDispatchInterceptor
{
    private final static Logger LOGGER = LoggerFactory.getLogger(ValidationInterceptor.class);

    @Autowired
    private ValidationFactory validationFactory;

    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        HttpServletRequest request = WebContext.getRequest();
        String pathInfo = request.getPathInfo();

        int code = 200;
        Transaction transaction = Cat.newTransaction("ParamValidate", String.valueOf(pathInfo));
        Map<String, String> paramMap = RequestParameterUtils.buildParamMap(request);
        try
        {
            Map<String, Object> resultMap = validationFactory.validateParamMap(pathInfo, paramMap);
            request.setAttribute(BizConstants.ATRR_PARAMETER_RESULTS, resultMap);
            transaction.setStatus(Transaction.SUCCESS);
        }
        catch (BusinessException e)
        {
            transaction.setStatus(e);
            code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
            LOGGER.warn("[validation] failed to validate {}, cause: {}" , paramMap, e.getMessage());
            boolean asyncRequest = WebUtils.isAsyncRequest(request);
            if(asyncRequest)
            {
                AjaxResult ajaxResult = new AjaxResult();
                ajaxResult.setCode(code);
                ajaxResult.setMsg("参数不正确");
                return new InterceptorResult(false, ajaxResult);
            }
            else
            {
                return new InterceptorResult(false, "error/400");
            }
        }
        catch (RuntimeException e)
        {
            code = AjaxResultCode.SERVER_ERROR.getCode();
            transaction.setStatus(e);
            throw e;
        }
        finally
        {
            transaction.complete();
            Cat.getProducer().logEvent("ParamValidate", pathInfo + "." +String.valueOf(code));
        }
        return new InterceptorResult(true);
    }
}
