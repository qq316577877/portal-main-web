package com.fruit.portal.action.wechat.account;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.portal.action.account.AccountBaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.service.account.UserLoginService;
import com.fruit.portal.service.account.UserModifyService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/*
 * Create Author  : Paul
 * Create  Time   : 17/5/16 下午21:43
 * Project        : portal
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

/**
 * 登录相关
 */
@Component
@UriMapping("/wechat")
public class LoginWeChatAction extends AccountBaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginWeChatAction.class);

    public static final String V1_LOGIN_URL = "http://v1.auth.0ku.com/inventory/business/validate";

    public static final int SYS_LOGIN_SIGN_EXPIRED_TIME = 60 * 30; //  30分钟有效

    public static final String OPENDID_KEY = "WEHCAT-OPENID-";

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserModifyService userModifyService;

    @Autowired
    private UserLoginService userLoginService;

    @Resource
    private CacheClient cacheClient;

    @Autowired
    private  UserAccountService userAccountService;

    /**
     * 1.查询用户是否存在(手机号)；<br/>
     *
     * @return
     */
    @UriMapping(value = "/login_ajax")
    public AjaxResult login()
    {
         Map requesMap  = super.getBodyObject(HashMap.class);

        String mobile = (String) requesMap.get("mobile");
        String password = (String) requesMap.get("password");
        String openid = (String) requesMap.get("openid");

        if(StringUtils.isBlank(openid)){
            openid  = super.getStringParameter("openid");
        }

        Validate.notEmpty(openid,"获取您的微信ID失败.");

        int  autoLogin =  Integer.parseInt(String.valueOf( requesMap.get("auto_login")));

        try
        {
            UserModel model =  this.loginWithPassword(mobile, password, autoLogin,openid);


            return new AjaxResult(model);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/login_ajax]，mobile：" + mobile + "，password：" + password+".Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    protected UserModel loginWithPassword(String mobile, String password, int autoLogin,String openid)
    {
        UserAccountDTO userAccountDTO = memberService.loadUserAccount(mobile);
        Validate.isTrue(null != userAccountDTO, "账号密码错误!");
        UserModel userModel = null;

        if (StringUtils.isNotBlank(userAccountDTO.getPassword())) // 密码不为空
        {
            userModel = this.userLoginService.userLogin(mobile, password);

            //清除缓存信息
            if(StringUtils.isNotBlank(userModel.getOpenid())){
                cacheClient.del(OPENDID_KEY+userModel.getOpenid());
            }
            cacheClient.del(OPENDID_KEY+openid);

            //解绑其他账号
            userAccountService.unbindWx(openid);
            //绑定过其他微信
            userAccountService.unbindWx(userModel.getUserId());
            //绑定微信openid
            userAccountService.bindWx(userModel.getUserId(),openid);

            userModel.setOpenid(openid);



            Validate.isTrue(null != userModel, "服务繁忙，请重试");
            this.userLogin(userModel, autoLogin == 1);
            LOGGER.info("登陆用户信息:"+userModel.getOpenid());
            return userModel;
        }
        else // 密码为空
        {
            throw new IllegalArgumentException("请输入登录密码，如果忘记密码，请点击“忘记密码”重置密码");
        }
    }

    @UriMapping(value = "/logout_ajax")
    public AjaxResult logout()
    {
        try {
            int id = super.getLoginUserId();
            Map requesMap  = super.getBodyObject(HashMap.class);
            String openid = (String) requesMap.get("openid");

            if (id > 0) {
                super.userLogout();
                //清除缓存数据
                this.cacheClient.del(OPENDID_KEY+openid);
                LOGGER.info("清除登陆用户信息:"+openid);
            }
            return new AjaxResult();
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/logout].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"内部错误!");
        }
    }

    @Override
    protected void wechatTokenSet(String token,UserModel userModel) {
        Long count = this.cacheClient.setnx(OPENDID_KEY+userModel.getOpenid(), token);

//        cacheClient.expire(OPENDID_KEY+userModel.getOpenid(), SYS_LOGIN_SIGN_EXPIRED_TIME);
//        userModel.setToken(token);
    }

}
