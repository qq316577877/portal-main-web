/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.action.common;

import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.fruit.portal.action.BaseAction;
import org.springframework.stereotype.Component;

/**
 * Error相关
 */
@Component
@UriMapping("/error")
public class ErrorAction extends BaseAction
{

    @UriMapping(value = "/404")
    public String show404()
    {
        return "error/404";
    }

    @UriMapping(value = "/403")
    public String show403()
    {
        return "error/403";
    }

    @UriMapping(value = "/400")
    public String show400()
    {
        return "error/400";
    }

    @UriMapping(value = "/500")
    public String show500()
    {
        return "error/500";
    }

}
