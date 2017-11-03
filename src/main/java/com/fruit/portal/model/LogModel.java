/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : LogModel.java
 */
public class LogModel implements Serializable
{
    private int spendTime;

    private int responseCode;

    private int userId;

    private int userType;

    private String userToken;

    private String userName;

    private String url;

    private String serverIp;

    private String userIp;

    private String userAgent;

    private String params;

    private String refer;

    private String requestId;

    private String referRequestId;

    private String os;

    private Date addTime;

    private boolean async;

    public int getUserType()
    {
        return userType;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public String getParams()
    {
        return params;
    }

    public void setParams(String params)
    {
        this.params = params;
    }

    public String getRefer()
    {
        return refer;
    }

    public void setRefer(String refer)
    {
        this.refer = refer;
    }

    public String getReferRequestId()
    {
        return referRequestId;
    }

    public void setReferRequestId(String referRequestId)
    {
        this.referRequestId = referRequestId;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public int getSpendTime()
    {
        return spendTime;
    }

    public void setSpendTime(int spendTime)
    {
        this.spendTime = spendTime;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserIp()
    {
        return userIp;
    }

    public void setUserIp(String userIp)
    {
        this.userIp = userIp;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken(String userToken)
    {
        this.userToken = userToken;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }

    public Date getAddTime()
    {
        return addTime;
    }

    public void setAddTime(Date addTime)
    {
        this.addTime = addTime;
    }

    public boolean isAsync()
    {
        return async;
    }

    public void setAsync(boolean async)
    {
        this.async = async;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("LogModel{");
        sb.append("addTime=").append(addTime);
        sb.append(", spendTime=").append(spendTime);
        sb.append(", responseCode=").append(responseCode);
        sb.append(", userId=").append(userId);
        sb.append(", userType=").append(userType);
        sb.append(", userToken='").append(userToken).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", serverIp='").append(serverIp).append('\'');
        sb.append(", userIp='").append(userIp).append('\'');
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append(", params='").append(params).append('\'');
        sb.append(", refer='").append(refer).append('\'');
        sb.append(", requestId='").append(requestId).append('\'');
        sb.append(", referRequestId='").append(referRequestId).append('\'');
        sb.append(", os='").append(os).append('\'');
        sb.append(", async=").append(async);
        sb.append('}');
        return sb.toString();
    }
}
