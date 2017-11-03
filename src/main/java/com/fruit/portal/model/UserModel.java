/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.model;

import java.io.Serializable;

/**
 * Description:    用户基础信息
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : fruit
 * File Name      : UserModel.java
 */
public class UserModel implements Serializable
{
    private int status;

    private int userId;

    private String password;

    private String mobile;

    private String mail;

    private String enterpriseName;

    private String enterpriseAddress;

    private String phoneNum;

    private String identity;

    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private int enterpriseVerifyStatus;

    private String description;

    private int countryId;

    private String countryName;

    private int provinceId;

    private int cityId;

    private String cityName = "";

    private int districtId;

    private String districtName = "";

    private String provinceName = "";

    private String QQ;

    private String openid;

    private String token;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getEnterpriseName()
    {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName)
    {
        this.enterpriseName = enterpriseName;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getEnterpriseVerifyStatus()
    {
        return enterpriseVerifyStatus;
    }

    public void setEnterpriseVerifyStatus(int enterpriseVerifyStatus)
    {
        this.enterpriseVerifyStatus = enterpriseVerifyStatus;
    }

    public int getCountryId()
    {
        return countryId;
    }

    public void setCountryId(int countryId)
    {
        this.countryId = countryId;
    }

    public int getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }

    public int getCityId()
    {
        return cityId;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getProvinceName()
    {
        return provinceName;
    }

    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public String getEnterpriseAddress()
    {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress)
    {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getQQ()
    {
        return QQ;
    }

    public void setQQ(String QQ)
    {
        this.QQ = QQ;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }


    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public String getIdentity()
    {
        return identity;
    }

    public void setIdentity(String identity)
    {
        this.identity = identity;
    }

    public int getDistrictId()
    {
        return districtId;
    }

    public void setDistrictId(int districtId)
    {
        this.districtId = districtId;
    }

    public String getDistrictName()
    {
        return districtName;
    }

    public void setDistrictName(String districtName)
    {
        this.districtName = districtName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("UserModel{");
        sb.append("countryId=").append(countryId);
        sb.append(", status=").append(status);
        sb.append(", userId=").append(userId);
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", mail='").append(mail).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", enterpriseName='").append(enterpriseName).append('\'');
        sb.append(", enterpriseAddress='").append(enterpriseAddress).append('\'');
        sb.append(", phoneNum='").append(phoneNum).append('\'');
        sb.append(", type=").append(type);
        sb.append(", enterpriseVerifyStatus=").append(enterpriseVerifyStatus);
        sb.append(", provinceId=").append(provinceId);
        sb.append(", cityId=").append(cityId);
        sb.append(", cityName='").append(cityName).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append(", QQ='").append(QQ).append('\'');
        sb.append(", countryName='").append(countryName).append('\'');
        sb.append(", identity='").append(identity).append('\'');
        sb.append(", districtId=").append(districtId);
        sb.append(", districtName='").append(districtName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
