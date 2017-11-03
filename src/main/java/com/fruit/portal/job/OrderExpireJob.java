/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.NetworkUtil;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : OrderExpireJob.java
 */
@Service
public class OrderExpireJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderExpireJob.class);

//    @Autowired
//    private OrderExpireService orderExpireService;

    @Autowired
    private EnvService envService;

    public void runJob()
    {
//        if(!this.canRun())
//        {
//            return;
//        }
//
//        Date current = new Date();
//        Date endTime = DateUtils.addSeconds(current, 3);
//        Date beginTime = DateUtils.addMinutes(current, -5);

//        this.orderExpireService.handleExpiration(beginTime, endTime);
    }

    private boolean canRun()
    {
        String taskServer = this.envService.getConfig("task.server");
        String serverIp = NetworkUtil.getNetworkAddress();
        boolean match = taskServer.equals(serverIp);
        if(!match)
        {
            LOGGER.warn("[run] ignore with non-task server {}, task server should be {}", serverIp, taskServer);
        }
        return match;
    }

}
