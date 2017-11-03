/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service;

import com.fruit.portal.event.ITask;
import com.fruit.portal.model.UserModel;
import com.ovfintech.arch.common.event.EventChannel;
import com.ovfintech.arch.log.IMarinLog;
import com.ovfintech.arch.log.MarinLog;
import com.ovfintech.arch.log.MarinPrinter;
import com.fruit.portal.event.TaskEvent;
import com.fruit.portal.model.LogModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : LoggerManager.java
 */
@Service
public class LoggerManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerManager.class);

    @Autowired
    private StableInfoService stableInfoService;

    @Autowired
    @Qualifier("bizLogMarinPrinter")
    private MarinPrinter marinPrinter;

    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;

    public void addBizLog(final LogModel logModel)
    {
        this.taskEventChannel.publish(new TaskEvent(new ITask()
        {
            @Override
            public void doTask()
            {
                printLog(logModel);
            }
        }));
    }

    protected void printLog(LogModel logModel)
    {
        String userName = "";
        String mobile = "";
        String enterpriseName = "";
        int userType = 0;
        int cityId = 0;
        int provinceId = 0;
        String city = "";
        int userId = logModel.getUserId();
        if(userId > 0)
        {
            UserModel userModel = stableInfoService.getUserModel(userId);
            if(userModel != null)
            {
                userName = userModel.getMobile();
                enterpriseName = userModel.getEnterpriseName();
                mobile = userModel.getMobile();
                userType = userModel.getType();
                cityId = userModel.getCityId();
                provinceId = userModel.getProvinceId();
                city = userModel.getCityName();
            }
        }

        IMarinLog marinLog = new MarinLog();
        marinLog.putInt("user_id", userId);
        marinLog.putInt("user_type", userType);
        marinLog.putInt("city_id", cityId);
        marinLog.putInt("province_id", provinceId);
        marinLog.putString("user_name", userName);
        marinLog.putString("mobile", mobile);
        marinLog.putString("enterprise_name", enterpriseName);
        marinLog.putString("city", city);
        marinLog.putDate("time", logModel.getAddTime());
        marinLog.putInt("response_code", logModel.getResponseCode());
        marinLog.putInt("spend_time", logModel.getSpendTime());
        marinLog.putString("url", logModel.getUrl());
        marinLog.putString("params", logModel.getParams());
        marinLog.putString("request_id", logModel.getRequestId());
        marinLog.putString("refer", logModel.getRefer());
        marinLog.putString("server_ip", logModel.getServerIp());
        marinLog.putString("user_ip", logModel.getUserIp());
        marinLog.putString("user_agent", logModel.getUserAgent());
        marinLog.putString("os", logModel.getOs());
        marinLog.putInt("async", logModel.isAsync() ? 1 : 0);
        marinPrinter.print(marinLog);
    }
}
