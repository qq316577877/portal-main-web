package com.fruit.portal.action.wechat.common;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.action.account.AccountBaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.service.common.GraphicCaptchaService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.service.common.RuntimeConfigurationService;
import com.fruit.portal.service.wechat.WeChatBaseService;
import com.fruit.portal.utils.WechatConstants;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信授权、发送信息
 */
@Component
@UriMapping("/wechat/base")
public class WeChatBaseAction extends AccountBaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatBaseAction.class);


    @Autowired
    private WeChatBaseService weChatBaseService;

    @Autowired
    private  UserAccountService userAccountService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Resource
    private CacheClient cacheClient;


    public static final String OPENDID_KEY = "WEHCAT-OPENID-";

    public static final String LOGIN_URL = "login";

    @UriMapping(value = "/login_auth_ajax")
    public AjaxResult auth()
    {
        AjaxResult result = new AjaxResult ();
        Map requesMap  = super.getBodyObject(HashMap.class);

        String code = String.valueOf(requesMap.get("code"));
        String state = String.valueOf(requesMap.get("state"));


        Map<String,Object> dataMap = new HashMap<String,Object>();

        result = weChatBaseService.getUserTokenInfo(code);

        if(result.getCode() == AjaxResultCode.OK.getCode()) {
            String openid = String.valueOf(result.getData());
            dataMap.put("openid", openid);
            dataMap.put("userInfo", "");
            if(StringUtils.isNotBlank(state) ){
                String wehchar_url = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.WECHAT_ENTER+state);

                dataMap.put("redirect_uri", wehchar_url);
            }

            String userToken = this.cacheClient.get(OPENDID_KEY+openid);
            if(StringUtils.isNotBlank(userToken)){
                //已登陆
                UserAccountDTO dto = userAccountService.loadByOpendId(openid);
                if(dto != null) {
                    UserModel userModel = memberService.loadUserModel(dto);
                    if (userModel != null) {
                        //绑定过微信则
                        this.userLogin(userModel, false);
                    }
                    dataMap.put("userInfo", userModel);
                }else{
                    LOGGER.info("用户微信登陆后，绑定的微信用户信息被删除");
                }

            }else{
                dataMap.put("redirect_uri", LOGIN_URL);
            }


            result.setData(dataMap);
        }

        return result;

    }


    @UriMapping(value = "/statToUrl_ajax")
    public AjaxResult statToUrl()
    {
        AjaxResult result = new AjaxResult ();
        Map requesMap  = super.getBodyObject(HashMap.class);

        String state = String.valueOf(requesMap.get("state"));


        Map<String,Object> dataMap = new HashMap<String,Object>();


        if(StringUtils.isNotBlank(state) ){
            String wehchar_url = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.WECHAT_ENTER+state);

            dataMap.put("redirect_uri", wehchar_url);
        }else{
            dataMap.put("redirect_uri", LOGIN_URL);
        }


        result.setData(dataMap);


        return result;

    }


    @UriMapping(value = "/domainUrl")
    public AjaxResult domainUrl()
    {
        AjaxResult result = new AjaxResult ();


        String wehchar_url = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.doamin);



        result.setData(wehchar_url);


        return result;

    }


}
