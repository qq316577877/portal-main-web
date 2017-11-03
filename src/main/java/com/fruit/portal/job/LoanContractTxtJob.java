/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.loan.biz.task.service.LoanContractTxtService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.NetworkUtil;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-10
 * Project        : fruit
 * File Name      : LoanContractTxtJob.java
 */
@Service
public class LoanContractTxtJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanContractTxtJob.class);

    @Autowired
    private LoanContractTxtService loanContractTxtService;

    @Autowired
    private EnvService envService;

    public void runJob()
    {
        if(!this.canRun())
        {
            LOGGER.info("请在配置文件中配置task.server");
            return;
        }

        this.loanContractTxtService.loanContractTxtAvailable();//合同文件
        this.loanContractTxtService.loanReceiptTxtReconciliation();//借据文件
        this.loanContractTxtService.loanTransactionStreamTxt();//交易流水文件
        this.loanContractTxtService.loanRepaymentPeriodTxt();//还款期供文件
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
