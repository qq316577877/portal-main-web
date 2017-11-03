/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.model.loan;

import com.fruit.loan.biz.common.LoanInfoStatusEnum;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-13
 * Project        : fruit
 * File Name      : LoanInfoQueryRequest.java
 */
public class LoanInfoQueryRequest implements Serializable
{
    private String keyword;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private LoanInfoStatusEnum status;

    private List<Integer> notIncludeStatus;

    private List<Integer> statusList;



    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public List<Integer> getNotIncludeStatus() {
        return notIncludeStatus;
    }

    public void setNotIncludeStatus(List<Integer> notIncludeStatus) {
        this.notIncludeStatus = notIncludeStatus;
    }

    private String sortKey;

    public LoanInfoStatusEnum getStatus() {
        return status;
    }

    public void setStatus(LoanInfoStatusEnum status) {
        this.status = status;
    }

    private boolean desc;

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }


    public String getSortKey()
    {
        return sortKey;
    }

    public void setSortKey(String sortKey)
    {
        this.sortKey = sortKey;
    }

    public boolean isDesc()
    {
        return desc;
    }

    public void setDesc(boolean desc)
    {
        this.desc = desc;
    }
}
