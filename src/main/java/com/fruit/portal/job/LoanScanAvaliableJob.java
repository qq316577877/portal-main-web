/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.loan.biz.task.service.LoanScanAvailableService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.NetworkUtil;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-10
 * Project        : fruit
 * File Name      : LoanScanAvaliableJob.java
 */
@Service
public class LoanScanAvaliableJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanScanAvaliableJob.class);

    @Autowired
    private LoanScanAvailableService loanScanAvailableService;

    @Autowired
    private EnvService envService;

    public void runJob()
    {
        if(!this.canRun())
        {
            LOGGER.info("请在配置文件中配置task.server");
            return;
        }

        this.loanScanAvailableService.loanIsTheScanAvailable();
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
