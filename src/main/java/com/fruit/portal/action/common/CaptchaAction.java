package com.fruit.portal.action.common;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.service.common.GraphicCaptchaService;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.vo.common.GraphicCaptchaVO;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 验证码相关
 */
@Component
@UriMapping("/captcha")
public class CaptchaAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaAction.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private GraphicCaptchaService graphicCaptchaService;

    @UriMapping(value = "/send_sms_ajax", interceptors = "validationInterceptor")
    public AjaxResult sendSms()
    {
        Map<String, Object> validatedMap = super.getParamsValidationResults();
        String mobile = (String) validatedMap.get("mobile");
        int type = (Integer) validatedMap.get("type");
        int captchaId = (Integer) validatedMap.get("id");
        String captcha = (String) validatedMap.get("captcha");
        // 验证图形验证码是否正确
        try
        {
            this.graphicCaptchaService.validateAndExpendCaptcha(captchaId, captcha);
            int userId = 0;
            if (type != SmsTypeEnum.USER_REGISTER.getValue()
                    && type != SmsTypeEnum.RESET_MOBILE.getValue())// 已注册用户取数据库ID（注册操作、换新号码操作除外）
            {
                UserAccountDTO user = userAccountService.loadByMobile(mobile);
                Validate.isTrue(null != user, "该手机尚未注册!");
                userId = user.getId();
            }
            boolean successful = messageService.sendSmsCaptcha(userId, mobile, type);
            if (!successful)
            {
                throw new RuntimeException("sms send failed for: " + userId + "," + mobile + "," + type);
            }
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/captcha/send_sms_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    @UriMapping(value = "/send_sms_direct_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult sendSmsDirect()
    {
        Map<String, Object> validatedMap = super.getParamsValidationResults();
        String mobile = (String) validatedMap.get("mobile");
        int type = (Integer) validatedMap.get("type");
        try
        {
            int userId = super.getLoginUserId();
            messageService.sendSmsCaptcha(userId, mobile, type);
            return new AjaxResult();
        }
        catch (Exception e)
        {
            LOGGER.error("[/captcha/send_sms_direct_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), "验证码发送失败!");
        }
    }

    @UriMapping(value = "/pic_generate_ajax")
    public AjaxResult generateGraphicCaptcha()
    {
        int type = super.getIntParameter("type", SmsTypeEnum.USER_REGISTER.getValue());
        int userId = super.getLoginUserId();
        String token = super.getGuestToken();

        try
        {
            GraphicCaptchaVO captcha = this.graphicCaptchaService.create(userId, token, type);
            Validate.notNull(captcha, "系统异常！请重试！");
            return new AjaxResult(captcha);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/captcha/pic_generate_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/pic_verify_ajax")
    public AjaxResult validateGraphicCaptcha()
    {
        try
        {
            int id = super.getIntParameter("id", 0);
            String captcha = super.getStringParameter("captcha");
            Validate.isTrue(id > 0, "参数错误");
            Validate.notEmpty(captcha, "参数错误");
            this.graphicCaptchaService.validateAndExpendCaptcha(id, captcha);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/captcha/pic_verify_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), e.getMessage());
        }
    }


    /**
     * 发送资金服务申请确认、实名认证短信
     * @return
     */
    @UriMapping(value = "/send_loan_sms_direct_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult sendLoanSmsDirect()
    {
        Map<String, Object> validatedMap = super.getParamsValidationResults();
        String mobile = (String) validatedMap.get("mobile");//此参数必须为银行预留手机号
        int type = (Integer) validatedMap.get("type");
        try
        {
            int userId = super.getLoginUserId();
            messageService.sendLoanSmsCaptcha(userId, mobile, type);
            return new AjaxResult();
        }
        catch (Exception e)
        {
            LOGGER.error("[/captcha/send_loan_sms_direct_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), "验证码发送失败!");
        }
    }


}
