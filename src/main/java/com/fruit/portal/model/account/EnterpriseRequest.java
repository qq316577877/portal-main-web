/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.model.account;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-17
 * Project        : fruit
 * File Name      : EnterpriseRequest.java
 */
public class EnterpriseRequest
{
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    private String mobile;

    private String email;

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    private String QQ;

    public int getEnterpriseVerifyStatus() {
        return enterpriseVerifyStatus;
    }

    public void setEnterpriseVerifyStatus(int enterpriseVerifyStatus) {
        this.enterpriseVerifyStatus = enterpriseVerifyStatus;
    }

    private int enterpriseVerifyStatus;

    private String enterpriseVerifyStatusDesc;

    private int type;

    private String typeDesc;

    public String getEnterpriseVerifyStatusDesc() {
        return enterpriseVerifyStatusDesc;
    }

    public void setEnterpriseVerifyStatusDesc(String enterpriseVerifyStatusDesc) {
        this.enterpriseVerifyStatusDesc = enterpriseVerifyStatusDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    private String enterpriseName;

    private String name;

    private String phoneNum;

    private String identity;

    private int countryId;

    private int provinceId;

    private int cityId;

    private int districtId;

    private String countryName;

    private String provinceName;

    private String cityName;

    private String districtName;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    String address;

    String identityFront;

    String identityFrontUrl;

    String identityBack;

    String identityBackUrl;

    String credential;

    String licence;

    String licenceUrl;

    String attachmentOne;

    String attachmentOneUrl;

    String attachmentTwo;

    String attachmentTwoUrl;

    public String getIdentityFrontUrl() {
        return identityFrontUrl;
    }

    public void setIdentityFrontUrl(String identityFrontUrl) {
        this.identityFrontUrl = identityFrontUrl;
    }

    public String getIdentityBackUrl() {
        return identityBackUrl;
    }

    public void setIdentityBackUrl(String identityBackUrl) {
        this.identityBackUrl = identityBackUrl;
    }

    public String getLicenceUrl() {
        return licenceUrl;
    }

    public void setLicenceUrl(String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    public String getAttachmentOneUrl() {
        return attachmentOneUrl;
    }

    public void setAttachmentOneUrl(String attachmentOneUrl) {
        this.attachmentOneUrl = attachmentOneUrl;
    }

    public String getAttachmentTwoUrl() {
        return attachmentTwoUrl;
    }

    public void setAttachmentTwoUrl(String attachmentTwoUrl) {
        this.attachmentTwoUrl = attachmentTwoUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentityFront() {
        return identityFront;
    }

    public void setIdentityFront(String identityFront) {
        this.identityFront = identityFront;
    }

    public String getIdentityBack() {
        return identityBack;
    }

    public void setIdentityBack(String identityBack) {
        this.identityBack = identityBack;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAttachmentOne() {
        return attachmentOne;
    }

    public void setAttachmentOne(String attachmentOne) {
        this.attachmentOne = attachmentOne;
    }

    public String getAttachmentTwo() {
        return attachmentTwo;
    }

    public void setAttachmentTwo(String attachmentTwo) {
        this.attachmentTwo = attachmentTwo;
    }
}
