/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.interceptor;

import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.UrlUtils;
import com.fruit.portal.vo.docs.DocumentNavVO;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于我们导航
 */
@Service("documentNavigatorInterceptor")
public class DocumentNavigatorInterceptor implements IDispatchInterceptor
{
    @Autowired
    private EnvService envService;

    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        HttpServletRequest request = WebContext.getRequest();
        String urlPath = request.getRequestURI();
        String domain = this.envService.getDomain();

        List<DocumentNavVO> navVOs = new ArrayList<DocumentNavVO>();
        navVOs.add(this.createNavVO(urlPath, "公司简介", UrlUtils.getDocumentContactUsUrl(domain)));
        navVOs.add(this.createNavVO(urlPath, "联系我们", UrlUtils.getDocumentConnectUsUrl(domain)));
        navVOs.add(this.createNavVO(urlPath, "法律声明", UrlUtils.getDocumentRuleUrl(domain)));
        navVOs.add(this.createNavVO(urlPath, "服务条款", UrlUtils.getDocumentServiceUrl(domain)));
        navVOs.add(this.createNavVO(urlPath, "收款账号", UrlUtils.getDocumentPrivacyUrl(domain)));
        navVOs.add(this.createNavVO(urlPath, "常见问题", UrlUtils.getDocumentQuestionsUrl(domain)));

//        navVOs.add(this.createNavVO(urlPath, "知识产权", UrlUtils.getDocumentPropertyUrl(domain)));
//        navVOs.add(this.createNavVO(urlPath, "诚聘英才", UrlUtils.getDocumentJoinUrl(domain)));
//        navVOs.add(this.createNavVO(urlPath, "资金安全", UrlUtils.getDocumentFundsSecurityUrl(domain)));
//        navVOs.add(this.createNavVO(urlPath, "使用指南", UrlUtils.getDocumentUserGuideUrl(domain)));
//        navVOs.add(this.createNavVO(urlPath, "网站公告", UrlUtils.getDocumentReleaseNotesUrl(domain)));

        request.setAttribute("__about_navs", navVOs);
        return new InterceptorResult(true);
    }

    protected DocumentNavVO createNavVO(String urlPath, String name, String url)
    {
        DocumentNavVO navVO = new DocumentNavVO(name, url);
        String domain = this.envService.getDomain();
        String path = url.replace(domain, "");
        navVO.setActive(urlPath.startsWith(path));
        return navVO;
    }

}
