/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.utils;

import com.fruit.portal.vo.common.IdValueVO;
import com.ovfintech.arch.captcha.qclound.QCloudCaptchaCheckResult;
import com.ovfintech.arch.captcha.qclound.QCloudCaptchaRequest;
import com.ovfintech.arch.captcha.qclound.QCloudCaptchaUtils;
import com.ovfintech.arch.utils.ip.RemoteIpGetter;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-18
 * Project        : fruit
 * File Name      : BizUtils.java
 */
public class BizUtils
{
    public static final String UTF_8 = "UTF-8";

    private static final String YYYY_M_MDD_H_HMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD_CN = "yyyy年MM月dd日";

    private static final String DECIMAL_FORAMT = "#0.##";

    private static final String DISCOUNT_FORAMT = "0.###";

    private static final int SECOND_MILLSEC = 1000;

    private static final int MINUTE_MILLSEC = SECOND_MILLSEC * 60;

    private static final int HOUR_MILLSEC = MINUTE_MILLSEC * 60;

    private static final int DAY_MILLSEC = HOUR_MILLSEC * 24;

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");

    private static String USER_BINDING_MAIL_EMAIL_CONTENT = null;

    public static List<Integer> createIntList(String idsStr)
    {
        List<Integer> ids = null;
        if (StringUtils.isNotBlank(idsStr))
        {
            String[] segments = idsStr.split(",");
            ids = new ArrayList<Integer>(segments.length);
            for (String segment : segments)
            {
                ids.add(NumberUtils.toInt(segment));
            }
        }
        return ids;
    }

    public static List<Integer> createIntListWidthValidate(String idsStr, int min)
    {
        List<Integer> ids = null;
        if (StringUtils.isNotBlank(idsStr))
        {
            String[] segments = idsStr.split(",");
            ids = new ArrayList<Integer>(segments.length);
            for (String segment : segments)
            {
                int value = NumberUtils.toInt(segment);
                Validate.isTrue(value > min, "参数错误!");
                ids.add(value);
            }
        }
        return ids;
    }

    public static List<Integer> createIntList(String idsStr, Collection<Integer> validValues)
    {
        List<Integer> ids = null;
        if (StringUtils.isNotBlank(idsStr))
        {
            String[] segments = idsStr.split(",");
            ids = new ArrayList<Integer>(segments.length);
            for (String segment : segments)
            {
                int value = NumberUtils.toInt(segment);
                if (CollectionUtils.isNotEmpty(validValues) && validValues.contains(value))
                {
                    ids.add(value);
                }
            }
        }
        return ids;
    }

    public static void tranformUpperCase(List<String> keywordSuggestions)
    {
        CollectionUtils.transform(keywordSuggestions, new Transformer()
        {
            @Override
            public Object transform(Object input)
            {
                return ((String) input).toUpperCase();
            }
        });
    }

    /**
     * 字符串截断
     *
     * @param source
     * @param maxLength
     * @return
     */
    public static String abbreviate(String source, int maxLength)
    {
        String result = "";
        if (StringUtils.isNotBlank(source))
        {
            if (source.length() > maxLength)
            {
                result = source.substring(0, maxLength);
            }
            else
            {
                result = source;
            }
        }
        return result;
    }

    /**
     * 功能描述：获取UUID,无连接符<p>
     * <p/>
     * 前置条件：<p>
     * <p/>
     * 方法影响： <p>
     * <p/>
     * Author shang.gao, 2013-9-27
     *
     * @return
     * @since open-platform-common 2.0
     */
    public static String getUUID()
    {
        UUID uuid = UUID.randomUUID();
        return (digits(uuid.getMostSignificantBits() >> 32, 8) + digits(uuid.getMostSignificantBits() >> 16, 4)
                + digits(uuid.getMostSignificantBits(), 4) + digits(uuid.getLeastSignificantBits() >> 48, 4)
                + digits(uuid.getLeastSignificantBits(), 12));
    }

    private static String digits(long val, int digits)
    {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1).toLowerCase();
    }

    public static String getTradeNo()
    {
        return DateFormatUtils.format(new Date(), YYYY_M_MDD_H_HMMSS) + "-" + getUUID();
    }

    /**
     * 获取6位的随机数(用于短信验证码)
     *
     * @return
     */
    public static int random6Int()
    {
        return (int) (Math.random() * 900000 + 100000);
    }

    public static String buildCaptchaUrl(String token, String callback)
    {
        QCloudCaptchaRequest request = new QCloudCaptchaRequest();
        request.setUid(token);
        request.setBusinessId(1);
        request.setSceneId(1);
        request.setCaptchaType(1);
        request.setUserIp(RemoteIpGetter.getRemoteAddr(WebContext.getRequest()));
        return QCloudCaptchaUtils.buildCaptchaUrl(request, callback);
    }

    public static QCloudCaptchaRequest createCaptchaRequest(String mobile)
    {
        QCloudCaptchaRequest request = new QCloudCaptchaRequest();
        request.setUid(mobile);
        request.setBusinessId(1);
        request.setSceneId(1);
        request.setCaptchaType(1);
        request.setUserIp(RemoteIpGetter.getRemoteAddr(WebContext.getRequest()));
        return request;
    }

    public static boolean picCaptchaVerify(String token, String ticket)
    {
        QCloudCaptchaRequest request = createCaptchaRequest(token);
        QCloudCaptchaCheckResult result = QCloudCaptchaUtils.checkCaptcha(request, ticket);
        return result.isPass();
    }

    /**
     * 对密码做md5摘要
     *
     * @param pwd
     * @return
     */
    public static String md5Password(String pwd)
    {
        return Digests.md5Hex(BizConstants.USER_PASSWORD_MD5_SALT + pwd + BizConstants.USER_PASSWORD_MD5_SALT);
    }

    public static String filter(String source)
    {
        StringBuilder stringBuilder = new StringBuilder();
        int length = source.length();
        for (int index = 0; index < length; index++)
        {
            char c = source.charAt(index);
            if (c == '-' || c == '.' || c == '/' || c == '#' || c == ' ' || c == '\t' || c == '*')
            {
                continue;
            }
            else
            {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    public static boolean contains(String source)
    {
        boolean hasSpecial = false;
        int length = source.length();
        for (int index = 0; index < length; index++)
        {
            char c = source.charAt(index);
            if (c == '-' || c == '.' || c == '/' || c == '#' || c == ' ' || c == '\t')
            {
                hasSpecial = true;
                break;
            }
        }
        return hasSpecial;
    }

    public static String format(Date date)
    {
        return DateFormatUtils.format(date, BizUtils.YYYY_MM_DD_HH_MM_SS);
    }

    public static String formatDateCN(Date date)
    {
        return DateFormatUtils.format(date, BizUtils.YYYY_MM_DD_CN);
    }

    public static String formatTS(Date date)
    {
        return DateFormatUtils.format(date, BizUtils.YYYY_M_MDD_H_HMMSS);
    }

    public static void main(String[] args)
    {
        System.out.println(md5Password("123456"));
    }

    public static boolean emailValidate(String email)
    {
        if (StringUtils.isNotBlank(email))
        {
            return EMAIL_PATTERN.matcher(email).matches();
        }
        return false;
    }

    public static String formatPrice(BigDecimal price)
    {
        NumberFormat numberInstance = new DecimalFormat(DECIMAL_FORAMT);
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setMinimumFractionDigits(2);
        return numberInstance.format(price);
    }

    public static String formatDiscount(BigDecimal price)
    {
        NumberFormat numberInstance = new DecimalFormat(DISCOUNT_FORAMT);
//        numberInstance.setMaximumFractionDigits(1);
//        numberInstance.setMinimumFractionDigits(1);
        return numberInstance.format(price);
    }

    /**
     * 计算商品折扣率
     *
     * @param originalPrice
     * @param afterDiscount
     * @return 如果任一参数为NULL，则返回空字符串，否则返回afterDiscount/originalPrice的值，且仅保留小数点后3位
     */
    public static String computeDiscount(BigDecimal originalPrice, BigDecimal afterDiscount)
    {
        String discount = "";
        if (null != originalPrice && null != afterDiscount)
        {
            if (originalPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                return "";
            }
            BigDecimal dis = afterDiscount.divide(originalPrice, 3, BigDecimal.ROUND_HALF_UP);
            discount = BizUtils.formatDiscount(dis);
        }
        return discount;
    }

    @Deprecated
    public static List<IdValueVO> map2VOList(Map<Integer, String> map)
    {
        List<IdValueVO> result = new ArrayList<IdValueVO>();
        if (MapUtils.isNotEmpty(map))
        {
            for (Map.Entry<Integer, String> entry : map.entrySet())
            {
                result.add(new IdValueVO(entry.getKey(), entry.getValue()));
            }
        }
        return result;
    }

    public static List<IdValueVO> map2VOList(Map<Integer, String> map, List<Integer> selectedIds)
    {
        List<IdValueVO> result = new ArrayList<IdValueVO>();
        if (MapUtils.isNotEmpty(map))
        {
            for (Map.Entry<Integer, String> entry : map.entrySet())
            {
                IdValueVO idValueVO = new IdValueVO(entry.getKey(), entry.getValue());
                result.add(idValueVO);
                if (CollectionUtils.isNotEmpty(selectedIds) && selectedIds.contains(entry.getKey()))
                {
                    idValueVO.setSelected(1);
                }
            }
        }
        return result;
    }

    public static String escapeProductCode(String productCode)
    {
        String result = "";
        if(StringUtils.isNotBlank(productCode))
        {
            result = productCode.replace(" ", "&#160;");
        }
        return result;

    }

    public static String generateTransactionNO(int userId)
    {
        return DateFormatUtils.format(new Date(), YYYY_M_MDD_H_HMMSS) + "-" + Integer.toString(userId) + "-" + getUUID();
    }

    public static  String buildAnonymousLocation(String province, String city)
    {
        return province.equals(city) ? city + "市" : province + "省";
    }

    public static  String buildLocation(String province, String city)
    {
        if (StringUtils.isBlank(province) && StringUtils.isBlank(city))
        {
            return "未知";
        }
        return province.equals(city) ? city + "市" : province + "省" + city + "市";
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    /**
     * 获取一天的凌晨时间：yyyy-MM-dd 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getBeforDawn(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
        {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天的午夜时间：yyyy-MM-dd 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getMidnight(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
        {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static String trimString(String stringValue)
    {
        return StringUtils.trimToEmpty(stringValue).replace('\u00A0', ' ').trim();
    }

    public static boolean isRobotRequest(String userAgent) {
        if (userAgent == null)
            return false;

        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("spider")) {
            return true;
        } else if (userAgent.contains("bot")) {
            return true;
        } else if (userAgent.contains("360jk yunjiankong")) {
            return true;
        } else if (userAgent.contains("yahoo")) {
            return true;
        } else if (userAgent.contains("nutch")) {
            return true;
        } else if (userAgent.contains("gougou")) {
            return true;
        } else if (userAgent.contains("scooter")) {
            return true;
        } else if (userAgent.contains("lilina")) {
            return true;
        }
        return false;
    }

    /**
     * 计算当前时间到指定时间点之间的毫秒数，如果当前时间在指定时间点或之后则返回0，否则返回值均大于0
     *
     * @param time
     * @return
     */
    public static long computeRemainingTimeMillis(Date time)
    {
        long remainingMillsec = 0;
        Date date = new Date();
        if (time != null && time.after(new Date(0)) && date.before(time))
        {
            remainingMillsec = (time.getTime() - date.getTime());
        }
        return remainingMillsec;
    }

    public static String computeRemainingTime(Date time)
    {
        long remainingMillsec = BizUtils.computeRemainingTimeMillis(time);
        if (remainingMillsec > 0)
        {
            int days = (int) (remainingMillsec / DAY_MILLSEC);
            int hours = (int) ((remainingMillsec % DAY_MILLSEC) / HOUR_MILLSEC);
            int minutes = (int) ((remainingMillsec % HOUR_MILLSEC) / MINUTE_MILLSEC);
            int seconds = (int) ((remainingMillsec % MINUTE_MILLSEC) / SECOND_MILLSEC);
            return days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        }
        else
        {
            return "";
        }
    }

}
