package com.fruit.portal.utils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtilApache
{

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * APACHE 3.0 写法
     */
    @SuppressWarnings("deprecation")
    public static String doPost(String url, String json) throws Exception
    {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        // 设置Http Post数据
        method.setRequestBody(json);
        try
        {
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK)
            {
                response = method.getResponseBodyAsString();
            }
        }
        catch (IOException e)
        {
        }
        finally
        {
            method.releaseConnection();
        }
        if (StringUtils.isNotBlank(response))
        {
            response = URLDecoder.decode(response, DEFAULT_CHARSET);
        }
        return response;
    }

    public static String doPost(String url, Map<String, String> params) throws Exception
    {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        // 设置Http Post数据
        if (params != null)
        {
            NameValuePair[] nv = new NameValuePair[params.entrySet().size()];
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                nv[i] = new NameValuePair(entry.getKey().toString(), entry.getValue());
                i++;
            }
            method.setRequestBody(nv);
        }
        try
        {
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK)
            {
                response = method.getResponseBodyAsString();
            }
        }
        catch (IOException e)
        {
        }
        finally
        {
            method.releaseConnection();
        }
        if (StringUtils.isNotBlank(response))
        {
            response = URLDecoder.decode(response, DEFAULT_CHARSET);
        }
        return response;
    }

    public static String doGet(String queryString) throws IOException
    {
        String response = null;
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(queryString);
        try
        {
            if (queryString != null && queryString.trim().equals(""))
            {
                method.setQueryString(URIUtil.encodeQuery(queryString));
            }
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK)
            {
                InputStream stream = method.getResponseBodyAsStream();
                response = IOUtils.toString(stream, Charset.forName(BizUtils.UTF_8));
            }
        }
        finally
        {
            method.releaseConnection();
        }
        return URLDecoder.decode(response, "UTF-8");

    }

}
