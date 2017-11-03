/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service.account;

import com.fruit.account.biz.common.LoginLogTypeEnum;
import com.fruit.account.biz.common.UserStatusEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserLoginLogDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserLoginLogService;
import com.fruit.portal.event.ITask;
import com.fruit.portal.event.TaskEvent;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.StableInfoService;
import com.fruit.portal.service.common.RedBankService;
import com.fruit.portal.utils.BizUtils;
import com.ovfintech.arch.common.event.EventChannel;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description: 登录
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-16
 * Project        : fruit
 * File Name      : UserLoginService.java
 */
@Service
public class UserLoginService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    private static final String MC_EVENT_KEY_USER_LOGIN = "uc.login";

    @Autowired
    private UserAccountService userAccountService;


    @Autowired
    private UserLoginLogService loginLogService;

    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;

    @Autowired
    private StableInfoService stableInfoService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedBankService redBankService;

    public UserModel userLogin(String mobile, String pwd)
    {
        String password = BizUtils.md5Password(pwd);
        UserAccountDTO userAccountDTO = userAccountService.loadByMobile(mobile);
        Validate.isTrue(null != userAccountDTO && password.equals(userAccountDTO.getPassword()), "账号密码错误!");
        Validate.isTrue(userAccountDTO.getStatus() != UserStatusEnum.FROZEN.getStatus(), "您的账号已被冻结，请联系客服!");
        return this.memberService.loadUserModel(userAccountDTO);
    }

    public void userLoginLog(UserModel userModel, String ip, String agent, String platform, String operator, int type)
    {
        if (null != userModel)
        {
            int userId = userModel.getUserId();
            UserLoginLogDTO loginLogDTO = new UserLoginLogDTO();
            loginLogDTO.setUserId(userId);
            loginLogDTO.setEnterpriseName(userModel.getEnterpriseName());
            loginLogDTO.setUserIp(ip);
            loginLogDTO.setOperator(operator);
            loginLogDTO.setType(type);
            loginLogDTO.setUserType(userModel.getType());
            loginLogService.create(loginLogDTO);

            this.redBankService.asyncFireEvent(userId, MC_EVENT_KEY_USER_LOGIN, null, userId);
        }
    }

    public void autoLoginLog(final int userId, final String userIp, final String agent, final String platform)
    {
        Validate.isTrue(userId > 0);
        this.taskEventChannel.publish(new TaskEvent(new ITask()
        {
            @Override
            public void doTask()
            {
                UserModel userModel = stableInfoService.getUserModel(userId);
                if (null != userModel)
                {
                    long lastSessionTime = stableInfoService.getLastSessionTime(userId);
                    Date currentDate = new Date();
                    stableInfoService.setLastSessionTime(userId, currentDate.getTime());

                    if (!DateUtils.isSameDay(currentDate, new Date(lastSessionTime))) // 如果不是同一天，记录新的登录日志
                    {
                        UserLoginLogDTO loginLogDTO = loginLogService.loadLast(userId);
                        if (null == loginLogDTO || !DateUtils.isSameDay(currentDate, loginLogDTO.getAddTime()))
                        {
                            userLoginLog(userModel, userIp, agent, platform, "User auto login", LoginLogTypeEnum.AUTOMATIC.getType());
                        }
                    }
                }
            }
        }));
    }

    public static void main(String[] args)
    {
        System.out.println(BizUtils.md5Password("123456"));
    }
}
