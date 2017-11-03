/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.model.account;

import com.fruit.portal.vo.common.KeyValueVO;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : BillListRequest.java
 */
public class BillListRequest
{

    private int userId;

    private int buyerId;

    private int sellerId;

    private int period;

    private String periodSelected;

    private List<KeyValueVO> periods;

    private Date beginTime;

    private Date endTime;

    private int type;

    private String typeSelected;

    private List<KeyValueVO> types;

    private int channel;

    private String channelSelected;

    private List<KeyValueVO> channels;

    private int withdraw;

    private String withdrawSelected;

    private List<KeyValueVO> withdraws;

    private String keyword;

    private int pageNo;

    private int pageSize;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getBuyerId()
    {
        return buyerId;
    }

    public void setBuyerId(int buyerId)
    {
        this.buyerId = buyerId;
    }

    public int getSellerId()
    {
        return sellerId;
    }

    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }

    public int getPeriod()
    {
        return period;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public String getPeriodSelected()
    {
        return periodSelected;
    }

    public void setPeriodSelected(String periodSelected)
    {
        this.periodSelected = periodSelected;
    }

    public List<KeyValueVO> getPeriods()
    {
        return periods;
    }

    public void setPeriods(List<KeyValueVO> periods)
    {
        this.periods = periods;
    }

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getTypeSelected()
    {
        return typeSelected;
    }

    public void setTypeSelected(String typeSelected)
    {
        this.typeSelected = typeSelected;
    }

    public List<KeyValueVO> getTypes()
    {
        return types;
    }

    public void setTypes(List<KeyValueVO> types)
    {
        this.types = types;
    }

    public int getChannel()
    {
        return channel;
    }

    public void setChannel(int channel)
    {
        this.channel = channel;
    }

    public String getChannelSelected()
    {
        return channelSelected;
    }

    public void setChannelSelected(String channelSelected)
    {
        this.channelSelected = channelSelected;
    }

    public List<KeyValueVO> getChannels()
    {
        return channels;
    }

    public void setChannels(List<KeyValueVO> channels)
    {
        this.channels = channels;
    }

    public int getWithdraw()
    {
        return withdraw;
    }

    public void setWithdraw(int withdraw)
    {
        this.withdraw = withdraw;
    }

    public String getWithdrawSelected()
    {
        return withdrawSelected;
    }

    public void setWithdrawSelected(String withdrawSelected)
    {
        this.withdrawSelected = withdrawSelected;
    }

    public List<KeyValueVO> getWithdraws()
    {
        return withdraws;
    }

    public void setWithdraws(List<KeyValueVO> withdraws)
    {
        this.withdraws = withdraws;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }
}
