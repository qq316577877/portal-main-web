/*
 *
 * Copyright (c) 2010-2015 by Shanghai 0ku Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.account;

import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.dto.UserInfoUpdateLogDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.account.biz.service.UserInfoUpdateLogService;
import com.fruit.portal.event.ITask;
import com.fruit.portal.event.TaskEvent;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.UserModel;
import com.ovfintech.arch.common.event.EventChannel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : account-biz
 * File Name      : MemberService.java
 */
@Service
public class MemberService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private UserInfoUpdateLogService infoUpdateLogService;


    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;

    public UserModel loadUserModel(int userId)
    {
        UserModel userModel = null;
        if (userId > 0)
        {
            UserAccountDTO userAccountDTO = userAccountService.loadById(userId);
            if (null != userAccountDTO)
            {
                return loadUserModel(userAccountDTO);
            }
        }
        return userModel;
    }

    public UserAccountDTO loadUserAccount(String mobile)
    {
        if (StringUtils.isNotBlank(mobile))
        {
            return userAccountService.loadByMobile(mobile);
        }
        return null;
    }


    public UserAccountDTO loadUserAccount(int userId)
    {
        return this.userAccountService.loadById(userId);
    }

    public UserModel loadUserModel(UserAccountDTO userAccountDTO)
    {
        UserModel userModel = null;
        if (userAccountDTO != null)
        {
            userModel = new UserModel();
            userModel.setStatus(userAccountDTO.getStatus());
            userModel.setMobile(userAccountDTO.getMobile());
            userModel.setMail(userAccountDTO.getMail());
            userModel.setUserId(userAccountDTO.getId());
            userModel.setType(userAccountDTO.getType());
            userModel.setEnterpriseVerifyStatus(userAccountDTO.getEnterpriseVerifyStatus());
            userModel.setQQ(userAccountDTO.getQQ());
            userModel.setOpenid(userAccountDTO.getOpenid());
            userModel.setPassword(userAccountDTO.getPassword());//将password存入缓存
            UserEnterpriseDTO userEnterpriseDTO = this.userEnterpriseService.loadByUserId(userAccountDTO.getId());
            if (userEnterpriseDTO != null)
            {
                userModel.setEnterpriseAddress(userEnterpriseDTO.getAddress());
                userModel.setEnterpriseName(userEnterpriseDTO.getName());
                userModel.setPhoneNum(userEnterpriseDTO.getPhoneNum());
                userModel.setIdentity(userEnterpriseDTO.getIdentity());
                userModel.setCountryId(userEnterpriseDTO.getCountryId());
                userModel.setProvinceId(userEnterpriseDTO.getProvinceId());
                userModel.setCityId(userEnterpriseDTO.getCityId());
                userModel.setDistrictId(userEnterpriseDTO.getDistrictId());

                if(userModel.getCountryId()>0){
                    userModel.setCountryName(this.metadataProvider.getCountryName(userModel.getCountryId()));
                }
                if(userModel.getProvinceId()>0){
                    userModel.setProvinceName(this.metadataProvider.getProvinceName(userModel.getProvinceId()));
                }
                if(userModel.getCityId()>0){
                    userModel.setCityName(this.metadataProvider.getCityName(userModel.getCityId()));
                }
                if(userModel.getDistrictId()>0){
                    userModel.setDistrictName(this.metadataProvider.getAreaName(userModel.getDistrictId()));
                }

            }
        }
        return userModel;
    }

    /**
     * 在数据库事物中创建用户账户的相关信息(UserAccount)
     *
     * @param userIp
     * @param userAccount
     * @param memo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public UserModel createUserAccount(String userIp, UserAccountDTO userAccount, String memo)
    {
        userAccountService.create(userAccount);

        UserModel userModel = this.loadUserModel(userAccount);
        this.asyncSaveUpdateLog(userModel, memo, UserInfoUpdateLogTypeEnum.NEW_COMER.getType(), userIp);
        return userModel;
    }


    @Transactional(rollbackFor = Exception.class)
    public void asyncSaveUpdateLog(final UserModel userModel, final String field, final int type, final String userIp)
    {
        this.taskEventChannel.publish(new TaskEvent(new ITask()
        {
            @Override
            public void doTask()
            {
                if (null != userModel)
                {
                    UserInfoUpdateLogDTO infoUpdateLogDTO = new UserInfoUpdateLogDTO();
                    infoUpdateLogDTO.setUserId(userModel.getUserId());
                    infoUpdateLogDTO.setEnterpriseName(userModel.getEnterpriseName());
                    infoUpdateLogDTO.setUserIp(userIp);
                    infoUpdateLogDTO.setType(type);
                    infoUpdateLogDTO.setInfo(field);
                    infoUpdateLogService.create(infoUpdateLogDTO);
                }
            }
        }));
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean userAccountBindWx(int userid, String openid,String userIp)
    {
        boolean flag = false;
        flag = userAccountService.bindWx(userid,openid);
        UserAccountDTO userAccountDTO = userAccountService.loadById(userid);
        UserModel userModel = this.loadUserModel(userAccountDTO);
        this.asyncSaveUpdateLog(userModel, "openid", UserInfoUpdateLogTypeEnum.NEW_COMER.getType(), userIp);

        return flag;
    }


}
