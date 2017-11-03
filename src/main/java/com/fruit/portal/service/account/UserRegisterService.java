/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service.account;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.utils.BizUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UserRegisterService.java
 */
@Service
public class UserRegisterService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisterService.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private MemberService memberService;



    /**
     * 检查手机号是否已注册
     *
     * @param mobile
     * @return 手机号已注册则返回true，否则返回false
     */
    public boolean isMobileRegistered(String mobile)
    {
        UserAccountDTO userAccountDTO = userAccountService.loadByMobile(mobile);
        return null != userAccountDTO;
    }



    /**
     * 注册用户基本信息
     *
     * @param pwd
     * @param mobile
     * @param qq
     * @param email
     * @param addUserId
     * @return 整个则返回用户的id，否则返回-1
     */
    @Transactional(rollbackFor = Exception.class)
    public UserModel registerAccount(String pwd, String mobile, String qq,String email, int addUserId, String userIp)
    {
        Validate.isTrue(!isMobileRegistered(mobile), "手机号已注册!");

        UserAccountDTO userAccount = new UserAccountDTO();
        userAccount.setPassword(BizUtils.md5Password(pwd));
        userAccount.setMobile(mobile);
        userAccount.setQQ(qq);
        userAccount.setMail(email);
        userAccount.setAddUserId(addUserId);

        return this.memberService.createUserAccount(userIp, userAccount, "pc register success");
    }

    /**
     * 注册用户基本信息
     *
     * @param dto
     * @return 整个则返回用户的id，否则返回-1
     */
    @Transactional(rollbackFor = Exception.class)
    public UserModel registerAccount(UserAccountDTO  dto,String userIp)
    {
        Validate.isTrue(!isMobileRegistered(dto.getMobile()), "手机号已注册!");


        return this.memberService.createUserAccount(userIp, dto, "pc register success");
    }

}
