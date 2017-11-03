package com.fruit.portal.action.home;

import com.alibaba.fastjson.JSON;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.service.home.HomeDataService;
import com.fruit.portal.vo.home.HomeVO;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/*
 * Create Author  : chao.ji
 * Create  Time   : 15/3/2 下午3:43
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
@UriMapping("/")
public class HomeAction extends BaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeAction.class);

    @Autowired
    private HomeDataService homeDataService;

    @UriMapping(value = {"/home"})
    public String action()
    {
        try
        {
            int userId = super.getLoginUserId();
            HomeVO homeData = this.homeDataService.getHomeDataCache();
            homeData.getUser().setLogin_status(userId > 0 ? 1 : 0);
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("__DATA", JSON.toJSONString(homeData));

            return "home/main";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/home].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

}
