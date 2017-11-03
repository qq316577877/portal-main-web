/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service.account;

import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.StableInfoService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.utils.AESHelper;
import com.fruit.portal.utils.BizUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:   账户修改
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UserModifyService.java
 */
@Service
public class UserModifyService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private StableInfoService stableInfoService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;


    public boolean checkPwd(String mobile, String pwd)
    {
        String password = BizUtils.md5Password(pwd);
        UserAccountDTO userAccountDTO = this.userAccountService.loadByMobile(mobile);
        if (null != userAccountDTO && password.equals(userAccountDTO.getPassword()))
        {
            return true;
        }
        return false;
    }

    /**
     * 找回密码的用户校验
     *
     * @param mobile
     * @return 生成用户修改密码的凭证并返回，如果用户不存在则返回null
     */
    public String generateResetPwdCertificate(String mobile, String captcha)
    {
        this.messageService.checkCaptchaAlive(mobile, captcha, SmsTypeEnum.RESET_PASSWORD.getValue(), true);
        UserAccountDTO account = userAccountService.loadByMobile(mobile);
        Validate.isTrue(null != account, "账号错误!");
        String certificate = AESHelper.encryptHexString(captcha + "|" + account.getId()); // 验证码+用户ID 生成修改密码凭证，默认有效时间为短信验证码的有效时间
        return certificate;
    }

    /**
     * 根据修改密码凭证修改用户密码
     *
     * @param certificate
     * @param newPwd
     * @return 密码修改成功则返回UserModel，否则返回null
     */
    @Transactional(rollbackFor = Exception.class)
    public UserModel resetPassword(String certificate, String newPwd, String userIp)
    {

        Validate.isTrue(StringUtils.isNotBlank(certificate), "参数错误!");
        Validate.isTrue(StringUtils.isNotBlank(newPwd), "参数错误!");
        String decrypt = AESHelper.decrypt(certificate);
        Validate.isTrue(StringUtils.isNotBlank(decrypt),"请重新验证手机验证码");
        String[] infos = decrypt.split("\\|");
        Validate.isTrue(infos.length == 2);
        String captcha = infos[0];
        int userId = Integer.valueOf(infos[1]);
        UserModel userModel = stableInfoService.getUserModel(userId);
        Validate.isTrue(null != userModel, "系统错误：用户不存在!");
        newPwd = BizUtils.md5Password(newPwd);
        this.messageService.expendCaptcha(userModel.getMobile(), captcha, SmsTypeEnum.RESET_PASSWORD.getValue());
        this.userAccountService.updatePassword(userId, newPwd, userModel.getMobile());
        this.stableInfoService.removeUserModel(userId);
        asyncSaveUpdateLog(userModel, "reset password", UserInfoUpdateLogTypeEnum.PASSWORD_UPDATE.getType(), userIp);
        return userModel;
    }

    /**
     * 生成用户修改手机号的certificate
     *
     * @param userModel
     * @return
     */
    public String generateResetMobileCertificate(UserModel userModel, String captcha)
    {
        Validate.isTrue(null != userModel);
        Validate.isTrue(null != userModel.getMobile());
        this.messageService.checkCaptchaAlive(userModel.getMobile(), captcha, SmsTypeEnum.RESET_MOBILE.getValue(), true);
        String certificate = AESHelper.encryptHexString(captcha + "|" + userModel.getMobile()); // 验证码+用户手机号 生成修改密码凭证，默认有效时间为短信验证码的有效时间
        return certificate;
    }

    /**
     * 修改用户手机号
     *
     * @param userModel
     * @param newMobile
     * @param certificate
     * @param captcha
     * @param userIp
     * @return 修改成功则更新并返回缓存
     */
    @Transactional(rollbackFor = Exception.class)
    public UserModel resetMobile(UserModel userModel, String newMobile, String certificate, String captcha, String userIp)
    {
        Validate.isTrue(null != userModel);
        Validate.isTrue(null != userModel.getMobile());

        Validate.isTrue(StringUtils.isNotBlank(certificate), "参数错误!");
        Validate.isTrue(StringUtils.isNotBlank(newMobile), "手机号为空!");
        String decrypt = AESHelper.decrypt(certificate);
        Validate.isTrue(StringUtils.isNotBlank(decrypt), "参数错误!");
        String[] infos = decrypt.split("\\|");
        Validate.isTrue(infos.length == 2);
        String oldCaptcha = infos[0];
        String oldMobile = infos[1];
        // 旧手机号必须正确
        Validate.isTrue(StringUtils.isNotBlank(oldMobile) && StringUtils.equals(oldMobile, userModel.getMobile()));
        Validate.isTrue(!StringUtils.equals(oldMobile, newMobile), "新手机号必须与旧手机号不同!");
        // 消费短信验证码
        this.messageService.expendCaptcha(userModel.getMobile(), oldCaptcha, SmsTypeEnum.RESET_MOBILE.getValue());
        this.messageService.expendCaptcha(newMobile, captcha, SmsTypeEnum.RESET_MOBILE.getValue());
        // 修改手机号码
        UserAccountDTO other = userAccountService.loadByMobile(newMobile);
        Validate.isTrue(null == other, "该手机号已注册!");
        this.userAccountService.updateMobile(userModel.getUserId(), newMobile, userModel.getMobile());
        this.stableInfoService.removeUserModel(userModel.getUserId()); // 删除缓存用户
        UserModel updated = this.stableInfoService.getUserModel(userModel.getUserId());
        asyncSaveUpdateLog(updated, "reset mobile", UserInfoUpdateLogTypeEnum.MOBILE_UPDATE.getType(), userIp);
        return updated;
    }

    /**
     * 修改用户密码
     *
     * @param userModel
     * @param newPwd
     * @param userIp
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyPwd(UserModel userModel, String newPwd, String userIp)
    {
        this.userAccountService.updatePassword(userModel.getUserId(), BizUtils.md5Password(newPwd), userModel.getMobile());
        this.stableInfoService.removeUserModel(userModel.getUserId());
        this.asyncSaveUpdateLog(userModel, "modify password", UserInfoUpdateLogTypeEnum.PASSWORD_UPDATE.getType(), userIp);
    }

    /**
     * 修改用户QQ
     *
     * @param userModel
     * @param qq
     */
    @Transactional(rollbackFor = Exception.class)
    public UserModel modifyQQ(UserModel userModel, String qq, String userIp)
    {
        this.userAccountService.updateQQ(userModel.getUserId(), qq, userModel.getMobile());
        this.asyncSaveUpdateLog(userModel, "update QQ", UserInfoUpdateLogTypeEnum.QQ_UPDATE.getType(), userIp);
        this.stableInfoService.removeUserModel(userModel.getUserId()); // 删除缓存用户
        return this.stableInfoService.getUserModel(userModel.getUserId());
    }


    @Transactional(rollbackFor = Exception.class)
    public UserModel updateEnterpriseVerifyStatus(UserModel userModel, int status, String userIp)
    {
        userAccountService.updateEnterpriseVerifyStatus(userModel.getUserId(), status, userModel.getMobile());
        this.stableInfoService.removeUserModel(userModel.getUserId()); // 删除缓存用户
        UserModel updated = stableInfoService.getUserModel(userModel.getUserId());
        this.asyncSaveUpdateLog(updated, "update enterprise verify status", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), userIp);
        return updated;
    }

    @Transactional(rollbackFor = Exception.class)
    protected void asyncSaveUpdateLog(final UserModel userModel, final String field, final int type, final String userIp)
    {
        this.memberService.asyncSaveUpdateLog(userModel, field, type, userIp);
    }


    /**
     * 绑定用户邮箱——不需要发送验证邮件
     *
     * @param userModel
     * @param mail
     * @return 绑定成功则更新并返回缓存
     */
    @Transactional(rollbackFor = Exception.class)
    public UserModel bindingMail(UserModel userModel, String mail, String domain, String userIp)
    {
        Validate.isTrue(null != userModel);
        Validate.isTrue(userModel.getUserId() > 0);
        Validate.isTrue(StringUtils.isNotBlank(mail));
        Validate.isTrue(!StringUtils.equals(userModel.getMail(), mail), "您已绑定该邮箱!");

        this.userAccountService.updateEmail(userModel.getUserId(), mail, userModel.getMobile());

        this.asyncSaveUpdateLog(userModel, "update mail", UserInfoUpdateLogTypeEnum.MAIL_UPDATE.getType(), userIp);
        this.stableInfoService.removeUserModel(userModel.getUserId()); // 删除缓存用户
        return this.stableInfoService.getUserModel(userModel.getUserId());
    }



    /**
     * 修改用户phoneNum    已认证用户才有phoneNum
     *
     * @param userModel
     * @param phoneNum
     */
    @Transactional(rollbackFor = Exception.class)
    public UserModel modifyPhoneNum(UserModel userModel, String phoneNum, String userIp)
    {
        Validate.isTrue(null != userModel);
        Validate.isTrue(userModel.getUserId() > 0);
        Validate.isTrue(StringUtils.isNotBlank(phoneNum));

        UserEnterpriseDTO enterpriseDTO = userEnterpriseService.loadByUserId(userModel.getUserId());

        boolean notRegisted = (null == enterpriseDTO || enterpriseDTO.getStatus() == UserEnterpriseStatusEnum.DELETED.getStatus());
        Validate.isTrue(!notRegisted, "未提交认证信息，没有联系电话!");

        this.userEnterpriseService.updatePhoneNum(enterpriseDTO.getId(), phoneNum, userModel.getMobile());

        this.asyncSaveUpdateLog(userModel, "Update enterprise phoneNum", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), userIp);
        this.stableInfoService.removeUserModel(userModel.getUserId()); // 删除缓存用户
        return this.stableInfoService.getUserModel(userModel.getUserId());
    }

}
