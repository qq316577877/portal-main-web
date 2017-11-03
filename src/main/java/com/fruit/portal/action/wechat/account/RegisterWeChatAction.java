/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.action.wechat.account;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.portal.action.account.AccountBaseAction;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.EnterpriseRequest;
import com.fruit.portal.service.account.EnterpriseService;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.service.account.UserRegisterService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 用户注册相关
 * <p/>
 * Create Author  : Paul
 * Create Date    : 2017-05-16
 * Project        : account-biz
 * File Name      : RegisterAction.java
 */
@Component
@UriMapping("/wechat")
public class RegisterWeChatAction extends AccountBaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterWeChatAction.class);

    @Autowired
    private UserRegisterService userRegisterService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EnterpriseService enterpriseService;

    private final ObjectMapper  jsonMapper = new ObjectMapper();


    @UriMapping(value = "/register_ajax")
    public AjaxResult showRegister()
    {
        AjaxResult resultAjax = new AjaxResult<Map>();
        //返回数据
        Map  resultMap = new HashMap<String,Object>();

        try {

            resultMap.put("login_url",super.getUrlConfigService().getLoginWeChatUrl());
            String token = "";
            int loginUserId = super.getLoginUserId();
            if (loginUserId > 0)  // 已登录用户使用mobile
            {
                UserModel userModel = super.loadUserModel(loginUserId);
                token = userModel.getMobile();
            } else   // 未登录用户使用guest token
            {
                token = super.getGuestToken();
            }
            if (StringUtils.isNotBlank(token)) {
                String url = BizUtils.buildCaptchaUrl(token, "callback");
                resultMap.put("captcha_url",url);
            }
            resultMap.put("user_agreement_url", UrlUtils.getDocumentServiceUrl(super.getDomain()));
            resultMap.put("forget_pwd_url",UrlUtils.getForgetPwdWechatUrl(super.getDomain()));

            resultAjax.setData(resultMap);
            return resultAjax;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/register_ajax].Exception:{}",e);

            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode() , "内部错误!");
        }
    }

    @UriMapping(value = "/check_mobile_sendsms_ajax" ,interceptors = "validationInterceptor")
    public AjaxResult isMobileRegisteredOrNot()
    {
       // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        String mobile = (String) requesMap.get("mobile");
        int type =  Integer.parseInt(String.valueOf(  requesMap.get("type")));
        int userId = 0;
        try
        {
            if (this.userRegisterService.isMobileRegistered(mobile))
            {
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "该手机号已注册!");
            }
            //如果没有注册则发送注册短信验证码
            boolean successful = messageService.sendSmsCaptcha(userId, mobile, type);
            if(!successful){
                throw new RuntimeException("sms send failed for: " + userId + "," + mobile + "," + type);
            }
            return new AjaxResult(AjaxResultCode.OK.getCode());
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/check_mobile_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    @UriMapping(value = "/register_account_ajax")
    public AjaxResult registerAccount()
    {
        //Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        String password = (String) requesMap.get("password");
        String mobile = (String) requesMap.get("mobile");
        String captcha = (String) requesMap.get("mobileCaptcha");
        String qq = (String) requesMap.get("qq");
        String email = (String) requesMap.get("email");
        String openid = (String) requesMap.get("openid");

        Map jsonMap = new HashMap<String, Object>();

        try
        {
            messageService.expendCaptcha(mobile, captcha, SmsTypeEnum.USER_REGISTER.getValue());
            // 注册成功直接登录
            int addUserId = super.getTraceUserId();
            UserAccountDTO userAccount = new UserAccountDTO();
            userAccount.setPassword(BizUtils.md5Password(password));
            userAccount.setMobile(mobile);
            userAccount.setQQ(qq);
            userAccount.setMail(email);
            userAccount.setAddUserId(addUserId);
            userAccount.setOpenid(openid);

            UserModel userModel = this.userRegisterService.registerAccount(userAccount, super.getUserIp());
            super.userLogin(userModel, false);

            jsonMap.put("userId",userModel.getUserId());
            jsonMap.put("mobile", userModel.getMobile());
            jsonMap.put("qq",userModel.getQQ());
            jsonMap.put("email",userModel.getMail());

            jsonMap.put("register_enterprise_url",UrlUtils.getRegisterEnterpriseWechatUrl(super.getDomain()));

            return new AjaxResult(jsonMap);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/register_account_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"内部错误");
        }
    }

    @UriMapping(value = "/register/enterprise_ajax" ,interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult showRegisterEnterprise()
    {

        try
        {
            //Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
            Map requesMap  = super.getBodyObject(HashMap.class);

            Map jsonMap = new HashMap<String, Object>();
            int type =  Integer.parseInt(String.valueOf( requesMap.get("type")));
            if(type < 1){
                type =  UserTypeEnum.ENTERPRISE.getType();
            }

            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);
            EnterpriseRequest enterpriseRequest = enterpriseService.loadEnterpriseByUserId(userId,userModel);

            jsonMap.put("register_enterprise_url",UrlUtils.getRegisterEnterpriseWechatUrl(super.getDomain()));
            jsonMap.put("type",type);
            jsonMap.put("enterpriseInfo",enterpriseRequest);
            jsonMap.put("meta_country", metadataProvider.getCountryOnlyIndexMap().values());


            return new AjaxResult(jsonMap);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/register/enterprise_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"内部错误!");
        }
    }

    @UriMapping(value = "/register/bindwx_ajax")
    public AjaxResult bindWechat()
    {

        try
        {
           // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
            Map requesMap  = super.getBodyObject(HashMap.class);

            Map jsonMap = new HashMap<String, Object>();
            int userid =  Integer.parseInt(String.valueOf(  requesMap.get("userid")));
            String openid = (String) requesMap.get("openid");

            if(memberService.userAccountBindWx(userid,openid,super.getUserIp())){
                return new AjaxResult(jsonMap);
            }else{
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),"您的用户信息失效!");
            }

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/register/bindwx_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"内部错误!");
        }
    }



    @UriMapping(value = "/is_mobile_registered_ajax" ,interceptors = "validationInterceptor")
    public AjaxResult isMobileRegistered()
    {
        Map requesMap  = super.getBodyObject(HashMap.class);

        String mobile = (String) requesMap.get("mobile");
        try
        {
            if (this.userRegisterService.isMobileRegistered(mobile))
            {
                return new AjaxResult();
            }
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "该手机号未注册!");
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/is_mobile_registered_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


}
