/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1503-19
 * Project        : Beauty
 * File Name      : UrlUtils.java
 */
public class UrlUtils
{

    private static final String HTTP_SCHEMA = "http://";

    public static String toUtf8String(String source)
    {
        if (StringUtils.isNotBlank(source))
        {
            try
            {
                return URLEncoder.encode(source, "UTF8");
            }
            catch (UnsupportedEncodingException e)
            {
                return "";
            }
        }
        else
        {
            return "";
        }
    }

    public static String getDefaultRedirUrl(HttpServletRequest request)
    {
        return toUtf8String(getFullUrl(request));
    }

    public static String getFullUrl(HttpServletRequest request)
	{
        String protocol = StringUtils.isNotBlank(request.getScheme()) ? request.getScheme() : HTTP_SCHEMA;
        int serverPort = request.getServerPort();
		String portSegment = serverPort == 80 ? "" : ":" + serverPort;
		String serverName = request.getServerName();
		String queryString = request.getQueryString();
		return  protocol + "://" + serverName + portSegment + request.getRequestURI() + (StringUtils.isNotBlank(queryString) ? ("?" + queryString) : "");
	}

    public static String getDocumentPrivacyUrl(String domain)
    {
        return domain + "/document/privacy";
    }

    public static String getDocumentRuleUrl(String domain)
    {
        return domain + "/document/rule";
    }

    public static String getDocumentServiceUrl(String domain)
    {
        return domain + "/document/service";
    }

    public static String getDocumentJoinUrl(String domain)
    {
        return domain + "/document/join";
    }

    public static String getDocumentContactUsUrl(String domain)
    {
        return domain + "/document/about";
    }

    public static String getDocumentConnectUsUrl(String domain)
    {
        return domain + "/document/contact";
    }

    public static String getDocumentQuestionsUrl(String domain)
    {
        return domain + "/document/question";
    }

    public static String getDocumentQuestionsDetailUrl(String domain)
    {
        return domain + "/document/question/detail";
    }

    public static String getDocumentUserGuideUrl(String domain)
    {
        return domain + "/document/userguide";
    }

    public static String getDocumentReleaseNotesUrl(String domain)
    {
        return domain + "/document/releasenotes";
    }

    public static String getDocumentFundsSecurityUrl(String domain)
    {
        return domain + "/document/funds_security";
    }

    public static String getDocumentPropertyUrl(String domain)
    {
        return domain + "/document/property";
    }

    public static String getRegisterUrl(String domain)
    {
        return domain + "/member/register";
    }

    public static String getRegisterSucceedUrl(String domain)
    {
        return domain + "/member/register_succeed";
    }

    public static String loanUserAuthSucceedUrl(String domain)
    {
        return domain + "/loan/auth/loanDatum";
    }

    public static String loanSucceedUrl(String domain)
    {
        return domain + "/loan/auth/loanSuccess";
    }

    public static String getLoanAuthenticationUrl(String domain)
    {
        return domain + "/loan/auth/authentication";
    }

    public static String getLoanAuthLetterUrl(String domain)
    {
        return domain + "/loan/auth/loanAuthLetter";
    }

    public static String getRegisterEnterpriseUrl(String domain)
    {
        return domain + "/member/register/enterprise";
    }

    public static String getRegisterEnterpriseWechatUrl(String domain)
    {
        return domain + "/wechat/register/enterprise_ajax";
    }

    public static String getForgetPwdUrl(String domain)
    {
        return domain + "/member/password/find";
    }

    public static String getForgetPwdWechatUrl(String domain)
    {
        return domain + "/wechat/password/find";
    }

    public static String getDashboardUrl(String domain)
    {
        return domain + "/member/dashboard/home";
    }

    public static String getMemberAccountUrl(String domain)
    {
        return domain + "/member/account/show";
    }

    public static String getLoanMyAplicationUrl(String domain)
    {
        return domain + "/loan/auth/loanAplication";
    }

    public static String getMemberEnterpriseVerifyUrl(String domain)
    {
        return domain + "/member/enterprise/auth/show";
    }

    public static String getErrorUrl(String domain, int errorCode)
    {
        return domain + "/error/" + errorCode;
    }


}
