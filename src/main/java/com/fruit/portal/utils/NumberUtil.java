package com.fruit.portal.utils;

import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

public class NumberUtil
{

    /**
     * 验证字符串是否是数字
     */
    public static boolean isNumber(String str)
    {
        if (str != null && !"".equals(str))
        {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
            java.util.regex.Matcher match = pattern.matcher(str);
            if (match.matches() == false)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成指定范围内的随机数([min, max))<br>
     * 注：此方法是线程安全的
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * 随机生成一个六位数([100000, 999999])<br>
     * 注：此方法是线程安全的
     *
     * @return
     */
    public static int random6Figure()
    {
        return randomInt(100000, 1000000);
    }

    /**
     * 判断两个浮点数是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Double a, Double b)
    {
        if (null == a || null == b)
        {
            return false;
        }
        double exp = 10E-10;
        double temp = Math.abs(a - b);
        if (temp > -1 * exp && temp < exp)
        {
            return true;
        }
        return false;
    }


    /**
     * 将double类型数据转换为百分比格式，并保留小数点后FractionDigits位
     * @param d
     * @param fractionDigits 小数点后保留几位
     * @return
     */
    public static String getPercentFormat(double d ,int fractionDigits){
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(fractionDigits);// 小数点后保留几位
        String str = nf.format(d);
        return str;
    }


}
