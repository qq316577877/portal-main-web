
package com.fruit.portal.action;

import com.alibaba.fastjson.JSON;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.StableInfoService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.common.UrlConfigService;
import com.fruit.portal.utils.BizConstants;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.JacksonMapper;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.UserAgentEnvUtils;
import com.ovfintech.arch.utils.ip.RemoteIpGetter;
import com.ovfintech.arch.utils.useragent.Browser;
import com.ovfintech.arch.utils.useragent.OperatingSystem;
import com.ovfintech.arch.utils.useragent.UserAgent;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class BaseAction
{
    public static final String USER_AGENT = "User-Agent";

    public static final String SUCCESS = "success";

    public static final String FTL_BLANK = "error/blank";

    public static final String FTL_ERROR_404 = "error/404";

    public static final String FTL_ERROR_400 = "error/400";

    public static final String FTL_ERROR_500 = "error/500";

    @Autowired
	private MessageSource messageSource;

    @Autowired
    protected EnvService envService;

    @Autowired
    protected StableInfoService stableInfoService;

    @Autowired
    protected UrlConfigService urlConfigService;

    private String domain;

    private String generalDomain;

    private String scurityDomain;

	@Autowired(required = false)
	private List<EventPublisher> publishers;

	protected int getLoginUserId()
	{
		return getLoginUserId(WebContext.getRequest());
	}

    protected String getLoginUserOpenid()
    {
        return getLoginOpenId(WebContext.getRequest());
    }

	public static int getLoginUserId(HttpServletRequest request)
	{
        int userId = 0;
		Object userIdObj  = request.getAttribute(BizConstants.ATTR_UID);

        if(userIdObj instanceof Integer)
        {
            userId = (Integer)userIdObj;
        }
        else if(userIdObj instanceof String)
        {
            userId = NumberUtils.toInt((String) userIdObj);
        }
		return userId;
	}

    public static String getLoginOpenId(HttpServletRequest request)
    {
        String openId = "";
        Object openIdObj  = request.getAttribute(BizConstants.ATTR_OPENID);

       if(openIdObj instanceof String)
        {
            openId = String.valueOf(openIdObj);
        }
        return openId;
    }

	protected String getGuestToken()
	{
		HttpServletRequest request = WebContext.getRequest();
        Object tokenObj  = request.getAttribute(BizConstants.COOKIE_GUEST);

        String guestToken = "";
        if(tokenObj instanceof String)
        {
            guestToken = (String)tokenObj;
        }
		return guestToken;
	}

	public static String toJson(Object object)
	{
		return JacksonMapper.toJson(object);
    }

	public static ObjectMapper getMapper()
	{
		return JacksonMapper.getMapper();
	}

	protected String getStringParameter(String name)
	{
		return HtmlUtils.htmlEscape(WebContext.getRequest().getParameter(name));
	}

	public String i18n(String message, Object... args)
	{
		try
		{
			return this.messageSource.getMessage(message, args, WebContext.getRequest().getLocale());
		}
		catch (NoSuchMessageException e)
		{
			return message;
		}
	}

	protected int getIntParameter(String name)
	{
		return this.getIntParameter(name, 0);
	}

	protected long getLongParameter(String name)
	{
        return NumberUtils.toLong(WebContext.getRequest().getParameter(name), 0);
    }

	protected int getIntParameter(String name, int defaultValue)
	{
		return NumberUtils.toInt(WebContext.getRequest().getParameter(name), defaultValue);
	}

	protected void notifyError(String errMsg, Exception nte)
	{
		EventHelper.triggerEvent(this.publishers, errMsg, errMsg, EventLevel.IMPORTANT, nte, RemoteIpGetter.getRemoteAddr(WebContext.getRequest()));
	}


    protected String getDomain()
    {
        if(this.domain == null)
        {
            this.domain = this.envService.getDomain();
        }
        return this.domain;
    }

    protected String getGeneralDomain()
    {
        if(this.generalDomain == null)
        {
            this.generalDomain = this.envService.getDomain();
        }
        return this.generalDomain;
    }

    protected String getSecurityDomain()
    {
        if(this.scurityDomain == null)
        {
            this.scurityDomain = this.envService.getSecurityDomain();
        }
        return this.scurityDomain;
    }

    protected UserAgent parseUserAgent()
    {
        return parseUserAgent(getUserAgent());
    }

    protected UserAgent parseUserAgent(String userAgent)
    {
        try
        {
            return UserAgentEnvUtils.parseUserAgent(userAgent);
        }
        catch (RuntimeException e)
        {
            return new UserAgent(OperatingSystem.UNKNOWN, Browser.UNKNOWN);
        }
    }

    protected String getUserAgent()
    {
        return BizUtils.abbreviate(WebContext.getRequest().getHeader(USER_AGENT), 250);
    }

    protected UserAgent getUserAgentModel()
    {
        return (UserAgent) WebContext.getRequest().getAttribute(BizConstants.ATTR_USER_AGENT_MODEL);
    }

    protected UserModel loadUserModel(int userId)
    {
        UserModel userModel = null;
        if(userId > 0)
        {
            userModel = this.stableInfoService.getUserModel(userId);
        }
        return userModel;
    }

    protected Map<String, Object> getParamsValidationResults()
    {
        HttpServletRequest request = WebContext.getRequest();
        return (Map<String, Object>) request.getAttribute(BizConstants.ATRR_PARAMETER_RESULTS);
    }

    protected <T> T getBodyObject(Class<T> c)
    {
        try
        {
            String str = IOUtils.toString(WebContext.getRequest().getInputStream());
            return JSON.parseObject(str, c);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("参数错误");
        }
    }

    protected String getBodyString()
    {
        try
        {
            return IOUtils.toString(WebContext.getRequest().getInputStream());
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("参数错误");
        }
    }

    protected String getUserIp()
    {
        return RemoteIpGetter.getRemoteAddr(WebContext.getRequest());
    }

    protected void setAttribute(String key, Object value)
    {
        WebContext.getRequest().setAttribute(key, value);
    }

    protected int getTraceUserId()
    {
        HttpServletRequest request = WebContext.getRequest();
        Object value = request.getAttribute(BizConstants.ATTR_SHORT_URL_UID);
        return value != null ? (Integer) value : 0;
    }

    protected String getTraceRefer()
    {
        HttpServletRequest request = WebContext.getRequest();
        Object value = request.getAttribute(BizConstants.ATTR_SHORT_URL_REFER);
        return value != null ? (String) value : "";
    }

    protected UrlConfigService getUrlConfigService()
    {
        return urlConfigService;
    }

    protected String getHomeUrl()
    {
        return this.getUrlConfigService().getHomeUrl();
    }

    protected String getLoginUrl()
    {
        return this.getUrlConfigService().getLoginUrlWithRedir(UrlUtils.getDefaultRedirUrl(WebContext.getRequest()));
    }

    protected String getLogoutUrl()
    {
        return this.getUrlConfigService().getLogoutUrl();
    }
}
