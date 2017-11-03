package com.fruit.portal.action.wechat.common;

import com.fruit.account.biz.service.UserAccountService;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.service.common.GraphicCaptchaService;
import com.fruit.portal.service.common.MessageService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码相关
 */
@Component
@UriMapping("/wechat/captcha")
public class CaptchaWeChatAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaWeChatAction.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private GraphicCaptchaService graphicCaptchaService;



    @UriMapping(value = "/send_sms_direct_ajax", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult sendSmsDirect()
    {

        Map requesMap  = super.getBodyObject(HashMap.class);
        String mobile = (String) requesMap.get("mobile");
        int type = Integer.parseInt(String.valueOf( requesMap.get("type")));
        try
        {
            int userId = super.getLoginUserId();
            messageService.sendSmsCaptcha(userId, mobile, type);
            return new AjaxResult();
        }
        catch (Exception e)
        {
            LOGGER.error("[/wechat/captcha/send_sms_direct_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), "验证码发送失败!");
        }
    }


}
