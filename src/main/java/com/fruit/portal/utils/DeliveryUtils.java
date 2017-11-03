/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1512-07
 * Project        : fruit
 * File Name      : DeliveryUtils.java
 */
public final class DeliveryUtils
{
    public static String postData(String url, Map<String, String> params, String codePage) throws Exception
    {
        final HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5 * 1000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5 * 1000);

        final PostMethod method = new PostMethod(url);
        if (params != null)
        {
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, codePage);
            method.setRequestBody(assembleRequestParams(params));
        }
        String result = "";
        try
        {
            httpClient.executeMethod(method);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), codePage));
            String line;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }
            reader.close();
            result = stringBuilder.toString();
        }
        catch (final Exception e)
        {
            throw e;
        }
        finally
        {
            method.releaseConnection();
        }
        return result;
    }

    /**
     * 组装http请求参数
     *
     * @param data
     * @return
     */
    private static NameValuePair[] assembleRequestParams(Map<String, String> data)
    {
        final List<NameValuePair> nameValueList = new ArrayList<NameValuePair>();

        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> entry = it.next();
            nameValueList.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }

        return nameValueList.toArray(new NameValuePair[nameValueList.size()]);
    }
}
