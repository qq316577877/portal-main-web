/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.model.loan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:    用户信贷信息
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-16
 * Project        : fruit
 * File Name      : LoanUserCreditInfoModel.java
 */
public class LoanUserCreditInfoModel implements Serializable
{
    private int id;

    private int userId;

    private String username;

    private String identity;

    private String mobile;

    private BigDecimal creditLine;

    private BigDecimal balance;

    private int type;

    private String typeDesc;

    private int status;

    private String statusDesc;

    private String ctrNo;

    private String crCstNo;

    private String rejectNote;

    private String description;

    private Date expireTime;

    private Date addTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public BigDecimal getCreditLine() {
        return creditLine;
    }

    public void setCreditLine(BigDecimal creditLine) {
        this.creditLine = creditLine;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCtrNo() {
        return ctrNo;
    }

    public void setCtrNo(String ctrNo) {
        this.ctrNo = ctrNo == null ? null : ctrNo.trim();
    }

    public String getCrCstNo() {
        return crCstNo;
    }

    public void setCrCstNo(String crCstNo) {
        this.crCstNo = crCstNo == null ? null : crCstNo.trim();
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote == null ? null : rejectNote.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
