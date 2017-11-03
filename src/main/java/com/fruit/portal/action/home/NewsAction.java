package com.fruit.portal.action.home;


import com.fruit.portal.action.BaseAction;
import com.fruit.portal.service.home.HomeDataService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/*
 * Create Author  : paul
 * Create  Time   : 17/6/23 下午17:26
 * Project        : portal
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

/**
 * 首页
 */
@Component
@UriMapping("/news")
public class NewsAction extends BaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsAction.class);

    @Autowired
    private HomeDataService homeDataService;

    @UriMapping(value = {"/detail"})
    public String action()
    {
        try
        {
        	String id = super.getStringParameter("id");
            return "news/news_show_" + id;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/news].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

}
