/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UrlConfigService.java
 */
@Service
public class UrlConfigService
{
    @Autowired
    private EnvService envService;

    public String getHomeUrl()
    {
        return this.envService.getDomain();
    }

    public String getLoginUrlWithRedir(String redir)
    {
        return this.getLoginUrl() + "?redir=" + (StringUtils.isNotBlank(redir) ? redir : this.getHomeUrl());
    }

    public String getLoginUrl()
    {
        return this.envService.getSecurityDomain() + "/member/login";
    }

    public String getLoginWeChatUrl()
    {
        return this.envService.getSecurityDomain() + "/wechat/login";
    }

    public String getLogoutUrl()
    {
        return this.envService.getSecurityDomain() + "/member/logout";
    }

    public Object getRegisterUrl()
    {
        return this.envService.getSecurityDomain() + "/member/register";
    }

    public Object getDashboardUrl()
    {
//        return this.envService.getSecurityDomain() + "/member/dashboard";
        return this.envService.getSecurityDomain() + "/member/info";
    }

    public String getMailVerifyUrl(String certificate)
    {
        return this.envService.getSecurityDomain() + "/member/account/binding_mail?sign=" + certificate;
    }

    public String getMailModifySuccessfulUrl(String username)
    {
        return this.envService.getSecurityDomain() + "/member/account/binding_mail/successful?username=" + username;
    }

    public String getMailModifyFailedUrl(String error_msg)
    {
        return this.envService.getSecurityDomain() + "/member/account/binding_mail/failed?error_msg=" + error_msg;
    }

    public String getResetPwdUrl(String certificate)
    {
        return this.envService.getSecurityDomain() + "/member/password/reset?certificate=" + certificate;
    }

    public String getResetPwdSucceedUrl(String username)
    {
        return this.envService.getSecurityDomain() + "/member/password/reset_succeed?username=" + username;
    }

    public String getDashboardPlaceOrderUrl()
    {
        return this.envService.getSecurityDomain() + "/order/center/importOrder";
    }

    public String getOrderCenterUrl()
    {
        return this.envService.getSecurityDomain() + "/order/center/list";
    }

    public String getOrderDetailUrl(String orderId)
    {
        return this.envService.getSecurityDomain() + "/order/detail?id=" + orderId;
    }

    public String getMemberBankListUrl()
    {
        return this.envService.getSecurityDomain() + "/member/bank";
    }

    public String getMemberDeliveryAddressUrl()
    {
        return this.envService.getSecurityDomain() + "/member/delivery_address";
    }

    public String getLoanMyAplicationUrl()
    {
        return this.envService.getSecurityDomain() + "/loan/auth/loanAplication";
    }




    public String getLoanApplyListUrl()
    {
        return this.envService.getSecurityDomain() + "/loan/info/applylist";
    }

    public String getLoanListUrl()
    {
        return this.envService.getSecurityDomain() + "/loan/info/servicelist";
    }

    public String getSupplierListUrl()
    {
        return this.envService.getSecurityDomain() + "/member/supplier";
    }

    public String getMemberEnterpriseVerifyUrl()
    {
        return this.envService.getSecurityDomain() + "/member/register/enterpriseIn";
    }

    public String getMemberLevelUrl()
    {
        return this.envService.getSecurityDomain() + "/member/info/level";
    }

    public String getMemberInfoUrl()
    {
        return this.envService.getSecurityDomain() + "/member/info";
    }


    public String getCustomsCleaUrl()
    {
        return this.envService.getSecurityDomain() + "/document/customsClearanceService";
    }

    public String getFundServiceUrl()
    {
        return this.envService.getSecurityDomain() + "/document/fundService";
    }

    public String getImportServiceUrl()
    {
        return this.envService.getSecurityDomain() + "/document/importService";
    }


    public String getLogisticsServiceUrl()
    {
        return this.envService.getSecurityDomain() + "/document/logisticsService";
    }

    public String getOrderCreateUrl()
    {
        return this.envService.getSecurityDomain() + "/order/create";
    }
    
    public String getSignBorrowUrl(){
    	return this.envService.getSecurityDomain() + "/loan/auth/quota/borrow";
    }
    
}
