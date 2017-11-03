package com.fruit.portal.action.wechat.account;


import com.fruit.portal.action.account.AccountBaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserResponseInfo;
import com.fruit.portal.service.account.UserModifyService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 用户账号相关
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-09-05
 * Project        : account-biz
 * File Name      : UserInformationAction.java
 */
@Component
@UriMapping("/wechat/user/info")
public class UserInformationAction extends AccountBaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInformationAction.class);

    @Autowired
    private UserModifyService userModifyService;


    @UriMapping(value = "/check_current_mobile_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult modifyMobileVerify()
    {
        try
        {
            Map<String,Object> requesMap  = super.getBodyObject(HashMap.class);
            String captchaStr = (String) requesMap.get("captcha");

            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            String certificate = this.userModifyService.generateResetMobileCertificate(userModel, captchaStr);
            Map<String, String> result = new HashMap<String, String>();
            result.put("certificate", certificate);
            return new AjaxResult(result);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/user/info/check_current_mobile_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/new_mobile_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult modifyMobile()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Map<String,Object> requesMap  = super.getBodyObject(HashMap.class);
            String newMobile = (String) requesMap.get("mobile");
            String certificate = (String) requesMap.get("certificate");
            String captcha = (String) requesMap.get("captcha");
            this.userModifyService.resetMobile(userModel, newMobile, certificate, captcha, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/user/info/new_mobile_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/binding_mail_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult bindingMail()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Map<String,Object> requesMap  = super.getBodyObject(HashMap.class);
            String mail = (String) requesMap.get("mail");
            Validate.isTrue(BizUtils.emailValidate(mail), "邮箱格式错误!");
            this.userModifyService.bindingMail(userModel, mail, super.getDomain(), super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/user/info/binding_mail_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/modify_pwd_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult modifyPwd()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);


            Map<String,Object> requesMap  = super.getBodyObject(HashMap.class);
            String oldPwd = (String) requesMap.get("oldPassword");
            String newPwd = (String) requesMap.get("newPassword");

            newPwd = newPwd.trim();

            // check old pwd
            if (!this.userModifyService.checkPwd(userModel.getMobile(), oldPwd))
            {
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), "旧密码错误!");
            }
            // modify pwd
            this.userModifyService.modifyPwd(userModel, newPwd, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/user/info/modify_pwd_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/modify_qq_ajax" ,interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult modifyQQ()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);


            Map<String,Object> requesMap  = super.getBodyObject(HashMap.class);
            String qq = (String) requesMap.get("qq");

            this.userModifyService.modifyQQ(userModel, qq, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/user/info/modify_qq_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/modify_phonenum_ajax" ,interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult modifyPhoneNum()
    {
        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);


            Map<String,Object> requesMap  = super.getBodyObject(HashMap.class);
            String phoneNum = (String) requesMap.get("phoneNum");

            this.userModifyService.modifyPhoneNum(userModel, phoneNum, super.getUserIp());
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/user/info/modify_phonenum_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询会员基本信息
     * @return
     */
    @UriMapping(value = "/get_user_information_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getUserInformation()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserResponseInfo userResponseInfo = new UserResponseInfo();
            BeanUtils.copyProperties(userModel, userResponseInfo);


            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(userResponseInfo);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/user/info/get_user_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

}
