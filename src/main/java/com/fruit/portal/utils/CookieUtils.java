/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.utils;

import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CookieUtils
{
    private static final int SECONDS_OF_DAY = 86400; // 一天的总秒数

    private static final int EXPIRE_DATE = 31;// 保持登录的天数

    private static final String USER_INFO_COOKIE_NAME = "kur";

    // dper中使用的分隔符
    private static final char SPLIT = '|';

    /**
     * 等号
     */
    private static final String EQUAL = "=";

    /**
     * 分号
     */
    private static final String SEMICOLON = ";";

    public static int getDpAccountId(HttpServletRequest request)
    {
        String encryptUserInfoString = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie != null)
                {
                    if (USER_INFO_COOKIE_NAME.equals(cookie.getName()))
                    {
                        encryptUserInfoString = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return unencryptUserId(encryptUserInfoString);
    }

    public static int getDpAccountIdFromHeaders(Header[] headers)
    {
        if (headers != null)
        {
            for (Header header : headers)
            {
                if (header != null)
                {
                    if ("Set-Cookie".equals(header.getName()) && StringUtils.contains(header.getValue(), USER_INFO_COOKIE_NAME))
                    {
                        String[] cookies = StringUtils.split(header.getValue(), ";");
                        for (String cookie : cookies)
                        {
                            String[] nameValue = StringUtils.split(cookie, "=");
                            if (ArrayUtils.getLength(nameValue) == 2 || USER_INFO_COOKIE_NAME.equals(nameValue[0]))
                            {
                                return unencryptUserId(nameValue[1]);
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static int unencryptUserId(String encryptUserInfoString)
    {
        if (!StringUtils.isEmpty(encryptUserInfoString))
        {
            String userInfoStr = EncryptionUtils.decrypt4NetAndJava(encryptUserInfoString);
            return NumberUtils.toInt(parseUserId(userInfoStr));
        }
        else
        {
            return 0;
        }
    }

    /**
     * 从dper中解析用户ID
     */
    private static String parseUserId(String dper)
    {
        // 以'|'分隔dper，确认格式
        String[] tokens = dper.split("[|]");
        if (tokens == null || tokens.length < 5)
        {
            return null;
        }
        // 解析用户ID
        String userId = tokens[0];
        try
        {
            if (Integer.parseInt(userId) < 1)
            {
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            return null;
        }
        // 解析过期时间
        try
        {
            Double expired = Double.valueOf(tokens[3]);
            long currentSeconds = Calendar.getInstance().getTime().getTime() / 1000;
            if (expired.intValue() < currentSeconds)
            {
                // 判断dper中的过期时间
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            return null;
        }
        return userId;
    }

    public static String getCookieDomain()
    {
        return "ku.com";
    }
//	public static void addCookie(String key, String value, HttpServletResponse response)
//	{
//		Cookie cookie = new Cookie(key, value);
//		cookie.setDomain(getCookieDomain());
//		response.addCookie(cookie);
//	}

    public static void addCookie(String key, String value, int timelong, HttpServletResponse response)
    {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(timelong);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getCookieValue(String key, HttpServletRequest request)
    {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie != null)
                {
                    if (key.equals(cookie.getName()))
                    {
                        value = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return value;
    }

    public static void addDperCookie(String dper, boolean keepLogin, HttpServletResponse response)
    {
        SimpleDateFormat cookieFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        cookieFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        int maxAge = -1;// 默认为会话cookie
        if (keepLogin && !StringUtils.isBlank(dper))
        {
            maxAge = EXPIRE_DATE * SECONDS_OF_DAY;
        }
        // 为给cookie[dper]添加HttpOnly属性，手动设置header[Set-Cookie]
        StringBuilder dperCookie = new StringBuilder();
        dperCookie.append(USER_INFO_COOKIE_NAME);
        dperCookie.append(EQUAL);
        dperCookie.append(dper);
        dperCookie.append(SEMICOLON);
        dperCookie.append("Domain");
        dperCookie.append(EQUAL);
        dperCookie.append(getCookieDomain());
        dperCookie.append(SEMICOLON);
        if (maxAge > 0)
        {
            dperCookie.append("Expires");
            dperCookie.append(EQUAL);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.SECOND, maxAge);
            dperCookie.append(cookieFormat.format(now.getTime()));
            dperCookie.append(SEMICOLON);
        }
        dperCookie.append("Path");
        dperCookie.append(EQUAL);
        dperCookie.append("/");
        dperCookie.append(SEMICOLON);
        dperCookie.append("HttpOnly");
        response.addHeader("Set-Cookie", dperCookie.toString());
    }

}
