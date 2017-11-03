/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.utils;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2014-10-13
 * Project        : promotion
 * File Name      : BizConstants.java
 */
public class BizConstants
{
    public static final String UTF_8 = "UTF-8";

    public static final String SLASH = "/";

    public static final String COOKIE_GUEST = "gid";

    public static final String COOKIE_USER = "oid";

    public static final String COOKIE_SHORT_URL_TRACE = "_SR_TRACE_";

    public static final String ATTR_USER_AGENT_MODEL = "_ATTR_USER_AGENT_MODEL";

    public static final String ATTR_SHORT_URL_UID = "_ATTR_SR_UID_";

    public static final String ATTR_SHORT_URL_REFER = "_ATTR_SR_REFER_";

    public static final String ATTR_UID = "uid";

    public static final String ATTR_OPENID = "wid";

    public static final String ATRR_PARAMETER_RESULTS = "_parameter_results";

    public static final String ATRR_PARAMETER_REQUEST= "_parameter_request";

    /**
     * 用户密码MD5加密使用的盐
     */
    public static final String USER_PASSWORD_MD5_SALT = ">_>@www.fruit.com@<_<";

    /**
     * 短信验证码过期时间(ms),默认30min
     */
    public static final int SMS_CAPTCHA_EXPIRATION_TIME = 60 * 1000 * 30;

    /**
     * 用户自动登录cookie(加密)的过期时间，默认为5天。
     */
    public static final int USER_COOKIE_AUTO_LOGIN_MAX_AGE = 60 * 60 * 24 * 5;

    /**
     * 用户信息cookie(非加密，明文)的过期时间，默认为365天。
     */
    public static final int USER_ACCOUNT_COOKIE_MAX_AGE = 60 * 60 * 24 * 365;

    /**
     * 修改用户邮箱凭证的过期时间(ms)，默认为24h。
     */
    public static final int MAIL_CERTIFICATE_EXPIRATION_TIME = 60 * 1000 * 60 * 24;

    public static final int DEFAULT_PAGE_SIZE = 15;

    public static final String COOKIE_WECHAT_OPENID = "kuoid";

    public static final String COOKIE_WECHAT_NICKNAME = "kunickname";

    public static final String ATTR_WECHAT_OPENID = "_ATTR_WECHAT_OPENID";

    /**
     * 如果一次文件导出5分钟没有获得结果，则视为导出失败
     */
    public static final long EXPORT_TIMEOUT = 1000 * 60 * 5;

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";


    public static final String EXTRA_DATA_KEY_ORDER_EVENT = "order_event";

    public static final String EXTRA_DATA_KEY_USER_IP = "user_ip";

    public static final String EXTRA_DATA_KEY_PLATFORM = "platform";

    public static final int COOKIE_SITE_TYPE_MAX_AGE =  3600 * 24 * 7;

    /**
     * 九创金服商城的ID
     *
     */
    public static final int KUMALL_ID = 1;

    /**
     * 九创金服东莞直营店的ID
     */
    public static final int KU_DGMALL_ID = 11475;

    /**
     * 资金申请确认短信验证码过期时间(ms),默认2min
     */
    public static final int SMS_LOAN_CAPTCHA_EXPIRATION_TIME = 60 * 1000 * 2;
}
