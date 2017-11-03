
package com.fruit.portal.filter;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserStatusEnum;
import com.fruit.portal.model.ContextObject;
import com.fruit.portal.model.LogModel;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserLoginInfo;
import com.fruit.portal.service.ContextManger;
import com.fruit.portal.service.LoggerManager;
import com.fruit.portal.service.StableInfoService;
import com.fruit.portal.service.StaticVersionService;
import com.fruit.portal.service.account.UserLoginService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.common.UrlConfigService;
import com.fruit.portal.utils.BizConstants;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.CookieUtil;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.common.utils.WebUtils;
import com.ovfintech.arch.utils.ServerIpUtils;
import com.ovfintech.arch.utils.ip.RemoteIpGetter;
import com.ovfintech.arch.validation.utils.RequestParameterUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 功能描述:  统一过滤器
 *
 * @author :  <p>
 * @version 1.0 2017-05-10
 */
public class EnvFilter implements Filter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvFilter.class);

    /**
     * 访客cookie的过期时间，默认为30天。
     */
    private static final int GUEST_COOKIE_MAX_AGE = 60 * 60 * 24 * 30;

    private static final String CAT_USER_TYPE = "User.Type";

    private static final String CAT_URL_TYPE = "URL.Type";

    private static final String ASYNC = "Async";

    private static final String SYNC = "Sync";

    private List<String> freePrefixList = Arrays.asList("/spy/", "/sv/", "/b/", "/heart/");

    private ApplicationContext applicationContext;

    private EnvService envService;

    private StaticVersionService staticVersionService;

    private StableInfoService stableInfoService;

    private UserLoginService userLoginService;

    private LoggerManager loggerManager;

    private UrlConfigService urlConfigService;

    private List<EventPublisher> eventPublishers;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        ServletContext servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.envService = applicationContext.getBean(EnvService.class);
        this.staticVersionService = applicationContext.getBean(StaticVersionService.class);
        this.stableInfoService = applicationContext.getBean(StableInfoService.class);
        this.loggerManager = applicationContext.getBean(LoggerManager.class);
        this.userLoginService = applicationContext.getBean(UserLoginService.class);
        this.urlConfigService = applicationContext.getBean(UrlConfigService.class);
        this.eventPublishers = new ArrayList<EventPublisher>(applicationContext.getBeansOfType(EventPublisher.class).values());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String pathInfo = servletRequest.getRequestURI();
        servletRequest.setAttribute("_current_request_uri", pathInfo);
        if ("/favicon.ico".equals(pathInfo))
        {
            return;
        }
        for (String freePrefix : freePrefixList)
        {
            if (pathInfo.startsWith(freePrefix))
            {
                chain.doFilter(request, response);
                return;
            }
        }

        Transaction transaction = Cat.newTransaction("MainFilter", String.valueOf(((HttpServletRequest) request).getPathInfo()));
        ContextObject context = ContextManger.getContext();
        long startTimeMillis = System.currentTimeMillis();
        int responseCode = HttpServletResponse.SC_OK;
        try
        {
            this.loadUserAccount(servletRequest, servletResponse);

            String domain = envService.getDomain();

            if (!WebUtils.isAsyncRequest(servletRequest))
            {
                this.loadGlobalUrls(servletRequest,domain);//全局URL
                this.loadDocsUrls(servletRequest,domain);//文档URL
            }

            this.handleUserLoginLog(servletRequest, context.getUserId());

            servletRequest.setAttribute("env", EnvService.getEnv());
            chain.doFilter(request, response);
            transaction.setStatus(Transaction.SUCCESS);
        }
        catch (RuntimeException e)
        {
            responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            String fullUrl = UrlUtils.getFullUrl(servletRequest);
            LOGGER.error("[Main] error when handle request: " + fullUrl, e);
            EventHelper.triggerEvent(this.eventPublishers, "EnvFilter." + e.getMessage(),
                    "error when handle request: " + fullUrl, EventLevel.IMPORTANT, e,
                    RemoteIpGetter.getRemoteAddr(servletRequest));
            transaction.setStatus(e);
        }
        finally
        {
            long currentTimeMillis = System.currentTimeMillis();
            long spendTime = currentTimeMillis - startTimeMillis;
            this.addBizLog(servletRequest, responseCode, (int) spendTime);
            Cat.logEvent(CAT_URL_TYPE, WebUtils.isAsyncRequest(servletRequest) ? ASYNC : SYNC);
            transaction.complete();
        }

    }

    /**
     * @param httpServletRequest
     * @param httpServletResponse
     */
    private void loadUserAccount(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        String userToken = "";
        UserLoginInfo userLoginInfo = null;

        Cookie[] cookies = httpServletRequest.getCookies();
        boolean firstVisit = true;
        Cookie guestCookie = null; // 临时用户
        Cookie userCookie = null; // 正式登陆用户cookie
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (BizConstants.COOKIE_GUEST.equals(cookie.getName())) // 已注入cookie的临时用户
                {
                    guestCookie = cookie;
                    firstVisit = false;
                }
                else if (BizConstants.COOKIE_USER.equals(cookie.getName())) //已登录用户
                {
                    userCookie = cookie;
                    firstVisit = false;
                }
            }
        }

        if (firstVisit) // 种临时cookie
        {
            userToken = BizUtils.getUUID();
            Cookie gust = new Cookie(BizConstants.COOKIE_GUEST, userToken);
            gust.setDomain(this.envService.getCookieDomain());
            gust.setPath("/");
            gust.setMaxAge(GUEST_COOKIE_MAX_AGE);
            httpServletResponse.addCookie(gust);
        }
        else
        {
            if (null != guestCookie)
            {
                httpServletRequest.setAttribute(BizConstants.COOKIE_GUEST, guestCookie.getValue());
                userToken = guestCookie.getValue();
            }

            if (null != userCookie)
            {
                String value = userCookie.getValue();
                userLoginInfo = CookieUtil.getUserInfoFromPassport(value);
                httpServletRequest.setAttribute(BizConstants.COOKIE_USER, value);
                httpServletRequest.setAttribute(BizConstants.ATTR_UID, userLoginInfo.getUserId());
                httpServletRequest.setAttribute(BizConstants.ATTR_OPENID, userLoginInfo.getOpenid());
            }
        }

        ContextManger.load(httpServletRequest, httpServletResponse);
        ContextObject context = ContextManger.getContext();
        context.setUserToken(userToken);
        if (userLoginInfo != null)
        {
            UserModel userModel = this.stableInfoService.getUserModel(userLoginInfo.getUserId());
            if (userModel != null)
            {
                context.setUserId(userLoginInfo.getUserId());
                context.setUserType(userModel.getType());

                String userName = "";
                if (userModel != null)
                {
                    userName = userModel.getEnterpriseName();
                    if (StringUtils.isBlank(userName))
                    {
                        userName = userModel.getEnterpriseName();
                    }
                }
                httpServletRequest.setAttribute("_header_username", userName);
                httpServletRequest.setAttribute("_header_user_login", ContextManger.getContext().getUserId() > 0 ? 1 : 0);
                //用户是否已认证
                httpServletRequest.setAttribute("_header_user_verify", userModel.getEnterpriseVerifyStatus() == UserEnterpriseStatusEnum.VERIFIED.getStatus());
            }
            this.checkUserStatus(userLoginInfo, httpServletRequest, httpServletResponse);
        }
    }

    private void checkUserStatus(UserLoginInfo userLoginInfo, HttpServletRequest request, HttpServletResponse response)
    {
        String pathInfo = request.getRequestURI();
        UserModel userModel = this.stableInfoService.getUserModel(userLoginInfo.getUserId());
        if (userModel != null)
        {
            int status = userModel.getStatus();
            Date currentDate = new Date();
            boolean overtime = (!DateUtils.isSameDay(currentDate, new Date(this.stableInfoService.getLastSessionTime(userModel.getUserId())))) // 最后会话时间不是当天
                    && (currentDate.getTime() - userLoginInfo.getLoginTime() > BizConstants.USER_ACCOUNT_COOKIE_MAX_AGE); // 登陆时间超过5天
            boolean invalid = overtime || (userLoginInfo.getPwdCode() != userModel.getPassword().hashCode())
                    || status == UserStatusEnum.DELETED.getStatus() || status == UserStatusEnum.FROZEN.getStatus();
            if (invalid) // 登陆超时、密码已修改、已删除、拒绝访问
            {
                CookieUtil.clearCookie(BizConstants.COOKIE_USER, this.envService.getCookieDomain(), request, response);
                if (!(pathInfo.endsWith("error/403") && pathInfo.endsWith("member/login")))
                {
                    try
                    {
                        response.sendRedirect(this.urlConfigService.getLoginUrl());
                    }
                    catch (IOException e)
                    {
                        //
                    }
                }
            }

            Cat.logEvent("User.Status", String.valueOf(status));
        }
    }

    /**
     * 全局的url
     * @param servletRequest
     */
    private void loadGlobalUrls(HttpServletRequest servletRequest,String domain)
    {
        servletRequest.setAttribute("static_file_server", this.staticVersionService.getStaticServer());
        servletRequest.setAttribute("home_page_url", this.urlConfigService.getHomeUrl());
        servletRequest.setAttribute("login_url", this.urlConfigService.getLoginUrl());
        servletRequest.setAttribute("login_redir_url", this.urlConfigService.getLoginUrlWithRedir(UrlUtils.getDefaultRedirUrl(servletRequest)));
        servletRequest.setAttribute("logout_url", this.urlConfigService.getLogoutUrl());
        servletRequest.setAttribute("register_url", this.urlConfigService.getRegisterUrl());
        servletRequest.setAttribute("dashboard_url", this.urlConfigService.getDashboardUrl());
        servletRequest.setAttribute("order_center_url", this.urlConfigService.getOrderCenterUrl());

        servletRequest.setAttribute("user_guide_url", UrlUtils.getDocumentQuestionsUrl(domain));
        servletRequest.setAttribute("doc_question_detail_url", UrlUtils.getDocumentQuestionsDetailUrl(domain));
    }


    /**
     * 首页头，文档的url
     * @param servletRequest
     */
    private void loadDocsUrls(HttpServletRequest servletRequest,String domain)
    {
        servletRequest.setAttribute("customs_clea_service_page_url", this.urlConfigService.getCustomsCleaUrl());//通关服务
        servletRequest.setAttribute("fund_service_page_url", this.urlConfigService.getFundServiceUrl());//资金服务
        servletRequest.setAttribute("import_service_page_url", this.urlConfigService.getImportServiceUrl());//进口服务
        servletRequest.setAttribute("logistics_service_page_url", this.urlConfigService.getLogisticsServiceUrl());//物流服务
        servletRequest.setAttribute("create_order_page_url", this.urlConfigService.getOrderCreateUrl());//立即下单

        servletRequest.setAttribute("company_profile_page_url", UrlUtils.getDocumentContactUsUrl(domain));//公司简介
        servletRequest.setAttribute("connect_us_page_url", UrlUtils.getDocumentConnectUsUrl(domain));//联系我们
        servletRequest.setAttribute("legal_statement_page_url", UrlUtils.getDocumentRuleUrl(domain));//法律声明
        servletRequest.setAttribute("doc_question_url", UrlUtils.getDocumentQuestionsUrl(domain));//常见问题目录页面
    }

    private void addBizLog(HttpServletRequest servletRequest, int responseCode, int spendTime)
    {
        String pathInfo = servletRequest.getRequestURI();
        ContextObject context = ContextManger.getContext();
        LogModel logModel = new LogModel();
        logModel.setSpendTime(spendTime);
        logModel.setUserId(context.getUserId());
        logModel.setUserToken(context.getUserToken());
        logModel.setUrl(pathInfo);
        logModel.setResponseCode(responseCode);
        logModel.setServerIp(ServerIpUtils.getServerIp());
        logModel.setAddTime(new Date());
        this.loadRequestInfo(servletRequest, logModel);
        this.loggerManager.addBizLog(logModel);
    }

    private void loadRequestInfo(HttpServletRequest servletRequest, LogModel logModel)
    {
        try
        {
            ContextObject context = ContextManger.getContext();
            logModel.setUserIp(context.getUserIp());
            logModel.setRefer(servletRequest.getHeader("referer"));
            logModel.setParams(this.getParams(servletRequest));
            logModel.setAsync(WebUtils.isAsyncRequest(servletRequest));
            logModel.setUserAgent(context.getUserAgent());
            logModel.setOs(context.getUserAgentModel().getOperatingSystem().getName());
        }
        catch (RuntimeException e)
        {
            LOGGER.error("[loadRequestInfo] error load request info: " +
                    UrlUtils.getFullUrl(servletRequest), e);
        }
    }

    private String getParams(HttpServletRequest servletRequest)
    {
        Map<String, String> paramMap = RequestParameterUtils.buildParamMap(servletRequest);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : paramMap.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("pwd".equals(key))
            {
                value = "******";
            }
            stringBuilder.append(key).append("=").append(value).append("&");
        }
        return stringBuilder.toString();
    }

    private void handleUserLoginLog(HttpServletRequest request, int userId)
    {
        ContextObject context = ContextManger.getContext();
        request.setAttribute(BizConstants.ATTR_USER_AGENT_MODEL, context.getUserAgentModel());
        if (userId > 0)
        {
            String userIp = RemoteIpGetter.getRemoteAddr(request);
            this.userLoginService.autoLoginLog(userId, userIp, BizUtils.abbreviate(context.getUserAgent(), 250),
                    context.getUserAgentModel().getOperatingSystem().getName());
        }
    }


    @Override
    public void destroy()
    {
    }

}
