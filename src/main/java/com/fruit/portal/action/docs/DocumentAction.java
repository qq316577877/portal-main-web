/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.action.docs;

import com.fruit.portal.action.BaseAction;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.fruit.portal.utils.UrlUtils;
import org.springframework.stereotype.Component;

/**
 * 帮助文档
 */
@Component
@UriMapping("/document")
public class DocumentAction extends BaseAction
{
    @UriMapping(value = {"/privacy"}, interceptors = "documentNavigatorInterceptor")
    public String privacy()
    {
        return "docs/privacy";
    }

    @UriMapping(value = {"/rule"}, interceptors = "documentNavigatorInterceptor")
    public String rule()
    {
        return "docs/rule";
    }

    @UriMapping(value = {"/service"}, interceptors = "documentNavigatorInterceptor")
    public String service()
    {
        return "docs/service";
    }

    @UriMapping(value = {"/items"}, interceptors = "documentNavigatorInterceptor")
    public String showItems()
    {
        return "docs/items";
    }

    @UriMapping(value = {"/join"}, interceptors = "documentNavigatorInterceptor")
    public String join()
    {
        return "docs/join";
    }

    @UriMapping(value = {"/about"}, interceptors = "documentNavigatorInterceptor")
    public String contactUs()
    {
        return "docs/about_us";
    }

    @UriMapping(value = {"/contact"}, interceptors = "documentNavigatorInterceptor")
    public String connectUs()
    {
        return "docs/contact_us";
    }

    @UriMapping(value = {"/funds_security"}, interceptors = "documentNavigatorInterceptor")
    public String fundSecurity()
    {
        return "docs/funds_security";
    }

    @UriMapping(value = {"/question"}, interceptors = "documentNavigatorInterceptor")
    public String showQuestion()
    {
        super.setAttribute("qa_url", UrlUtils.getDocumentQuestionsDetailUrl(super.getDomain()));
        return "docs/question";
    }

    @UriMapping(value = {"/question/detail"}, interceptors = "documentNavigatorInterceptor")
    public String showQuestionDetail()
    {
        WebContext.getRequest().setAttribute("userguide_search_helper_url", UrlUtils.getDocumentUserGuideUrl(super.getDomain()) + "/search");
        return "docs/question_detail";
    }

    @UriMapping(value = {"/userguide"}, interceptors = "documentNavigatorInterceptor")
    public String showUserGuide()
    {
        super.setAttribute("userguide_multisearch_url", UrlUtils.getDocumentUserGuideUrl(super.getDomain()) + "/multisearch");
        super.setAttribute("userguide_search_helper_url", UrlUtils.getDocumentUserGuideUrl(super.getDomain())+ "/search");
        super.setAttribute("userguide_buy_url", UrlUtils.getDocumentUserGuideUrl(super.getDomain())+ "/buy");
        super.setAttribute("userguide_sell_url", UrlUtils.getDocumentUserGuideUrl(super.getDomain())+ "/sell");
        super.setAttribute("userguide_refund_url", UrlUtils.getDocumentUserGuideUrl(super.getDomain())+ "/refund");
        return "docs/userguide";
    }

    @UriMapping(value = {"/userguide/multisearch"}, interceptors = "documentNavigatorInterceptor")
    public String showUserGuideMultiSearch()
    {
        return "docs/userguide_multisearch";
    }

    @UriMapping(value = {"/userguide/buy"}, interceptors = "documentNavigatorInterceptor")
    public String showUserGuideBuy()
    {
        WebContext.getRequest().setAttribute("userguide_search_helper_url", UrlUtils.getDocumentUserGuideUrl(super.getDomain()) + "/search");
        return "docs/userguide_buy";
    }

    @UriMapping(value = {"/userguide/sell"}, interceptors = "documentNavigatorInterceptor")
    public String showUserGuideSell()
    {
        return "docs/userguide_sell";
    }

    @UriMapping(value = {"/userguide/refund"}, interceptors = "documentNavigatorInterceptor")
    public String showUserGuideRefund()
    {
        return "docs/userguide_refund";
    }

    @UriMapping(value = {"/property"}, interceptors = "documentNavigatorInterceptor")
    public String property()
    {
        return "docs/property";
    }

    @UriMapping(value = {"/userguide/search"}, interceptors = "documentNavigatorInterceptor")
    public String searchHelper()
    {
        return "docs/userguide_search";
    }


    //通关服务
    @UriMapping(value = {"/customsClearanceService"}, interceptors = "documentNavigatorInterceptor")
    public String customsClea()
    {
        return "docs/customs_clearance_service";
    }

    //资金服务
    @UriMapping(value = {"/fundService"}, interceptors = "documentNavigatorInterceptor")
    public String fundService()
    {
        super.setAttribute("my_aplication_page_url", UrlUtils.getLoanMyAplicationUrl(super.getDomain()));
        return "docs/fund_service";
    }

    //进口服务
    @UriMapping(value = {"/importService"}, interceptors = "documentNavigatorInterceptor")
    public String importService()
    {
        return "docs/import_service";
    }

    //物流服务
    @UriMapping(value = {"/logisticsService"}, interceptors = "documentNavigatorInterceptor")
    public String logisticsService()
    {
        return "docs/logistics_service";
    }

}
