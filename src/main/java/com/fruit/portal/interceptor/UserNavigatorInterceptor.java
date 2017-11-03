/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.interceptor;

import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.StableInfoService;
import com.fruit.portal.service.common.UrlConfigService;
import com.fruit.portal.vo.dashboard.UserNavGroupVO;
import com.fruit.portal.vo.dashboard.UserNavItemVO;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户导航
 */
@Service("userNavigatorInterceptor")
public class UserNavigatorInterceptor implements IDispatchInterceptor
{
    @Autowired
    private StableInfoService stableInfoService;

    @Autowired
    private UrlConfigService urlConfigService;

    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        HttpServletRequest request = WebContext.getRequest();
        int userId = BaseAction.getLoginUserId(request);
        List<UserNavGroupVO> userNavGroupVOList = new ArrayList<UserNavGroupVO>(5);
        if (userId > 0)
        {
            UserModel userModel = this.stableInfoService.getUserModel(userId);
            if (userModel != null)
            {
                userNavGroupVOList.add(this.createAccountNav(userModel, uriMeta.getUri()));
                userNavGroupVOList.add(this.createOrderNav(userModel, uriMeta.getUri()));
                userNavGroupVOList.add(this.createLoanNav(userModel, uriMeta.getUri()));
                userNavGroupVOList.add(this.createSupplierNav(userModel, uriMeta.getUri()));
            }
        }
        request.setAttribute("user_nav_list", userNavGroupVOList);
        return new InterceptorResult(true);
    }

    private UserNavGroupVO createAccountNav(UserModel userModel,String urlPath)
    {
        UserNavGroupVO groupVO = new UserNavGroupVO();
        groupVO.setEnable(true);
        groupVO.setName("账户设置");

        List<UserNavItemVO> itemVOs = groupVO.getItems();
        itemVOs.add(this.createNavItem(urlPath, "账户信息", this.urlConfigService.getMemberInfoUrl()));
        itemVOs.add(this.createNavItem(urlPath, "会员认证", this.urlConfigService.getMemberEnterpriseVerifyUrl()));
        itemVOs.add(this.createNavItem(urlPath, "会员等级", this.urlConfigService.getMemberLevelUrl()));
        itemVOs.add(this.createNavItem(urlPath, "银行账号", this.urlConfigService.getMemberBankListUrl()));
        itemVOs.add(this.createNavItem(urlPath, "收货地址", this.urlConfigService.getMemberDeliveryAddressUrl()));
        return groupVO;
    }

    private UserNavGroupVO createOrderNav(UserModel userModel, String urlPath)
    {
        UserNavGroupVO groupVO = new UserNavGroupVO();
        groupVO.setEnable(userModel.getEnterpriseVerifyStatus() == UserEnterpriseStatusEnum.VERIFIED.getStatus());
        groupVO.setName("订单中心");

        List<UserNavItemVO> itemVOs = groupVO.getItems();
        itemVOs.add(this.createNavItem(urlPath, "进口下单", this.urlConfigService.getDashboardPlaceOrderUrl()));
        itemVOs.add(this.createNavItem(urlPath, "我的订单", this.urlConfigService.getOrderCenterUrl()));
        return groupVO;
    }

    private UserNavGroupVO createLoanNav(UserModel userModel, String urlPath)
    {
        UserNavGroupVO groupVO = new UserNavGroupVO();
        groupVO.setEnable(userModel.getEnterpriseVerifyStatus() == UserEnterpriseStatusEnum.VERIFIED.getStatus());
//        groupVO.setEnable(false);// TODO: 2017/7/7  暂时屏蔽此功能
        groupVO.setName("资金服务");

        List<UserNavItemVO> itemVOs = groupVO.getItems();
        itemVOs.add(this.createNavItem(urlPath, "我的申请", this.urlConfigService.getLoanMyAplicationUrl()));
        itemVOs.add(this.createNavItem(urlPath, "我的贷款", this.urlConfigService.getLoanListUrl()));
        return groupVO;
    }

    private UserNavGroupVO createSupplierNav(UserModel userModel, String urlPath)
    {
        UserNavGroupVO groupVO = new UserNavGroupVO();
        groupVO.setEnable(userModel.getEnterpriseVerifyStatus() == UserEnterpriseStatusEnum.VERIFIED.getStatus());
        groupVO.setName("伙伴管理");

        List<UserNavItemVO> itemVOs = groupVO.getItems();
        itemVOs.add(this.createNavItem(urlPath, "供应商信息", this.urlConfigService.getSupplierListUrl()));
        return groupVO;
    }

    private UserNavItemVO createNavItem(String urlPath, String name, String url)
    {
        UserNavItemVO itemVO = new UserNavItemVO(name, url);
        itemVO.setActive(itemVO.getUrl().endsWith(urlPath));
        return itemVO;
    }
}
