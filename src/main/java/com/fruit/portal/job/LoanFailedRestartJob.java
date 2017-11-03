/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.loan.biz.task.service.LoanFailedRestartService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.NetworkUtil;

/**
 * Description:代扣失败的再次代扣任务
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-17
 * Project        : fruit
 * File Name      : LoanFailedRestartJob.java
 */
@Service
public class LoanFailedRestartJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanFailedRestartJob.class);

    @Autowired
    private LoanFailedRestartService loanFailedRestartService;

    @Autowired
    private EnvService envService;

    public void runJob()
    {
        if(!this.canRun())
        {
            LOGGER.info("请在配置文件中配置task.server");
            return;
        }

        this.loanFailedRestartService.loanWithholdFailedRestartPay();
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
