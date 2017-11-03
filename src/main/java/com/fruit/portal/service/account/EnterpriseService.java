/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.account;

import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.common.UserMemberStatusEnum;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.message.biz.common.MessageTypeEnum;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.EnterpriseRequest;
import com.fruit.portal.service.BaseService;
import com.fruit.portal.service.ContextManger;
import com.fruit.portal.service.common.FileUploadService;
import com.fruit.portal.service.common.RedBankService;
import com.fruit.portal.vo.common.IdValueVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1512-07
 * Project        : message-biz
 * File Name      : EnterpriseService.java
 */
@Service
public class EnterpriseService extends BaseService
{
    public static final List<IdValueVO> ENTER_TYPES = new ArrayList<IdValueVO>();

    static
    {
        ENTER_TYPES.add(new IdValueVO(1, "个人"));
        ENTER_TYPES.add(new IdValueVO(2, "企业"));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseService.class);

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedBankService redBankService;

    @Autowired
    private UserModifyService userModifyService;


    @Autowired
    private MetadataProvider metadataProvider;


    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private LoanUserAuthInfoService loanUserAuthInfoService;

    /**
     * 检查企业名是否可用
     *
     * @param enterpriseName
     * @return 企业名未注册则返回true，否则返回false
     */
    public boolean isEnterpriseNameAvailable(int userId, String enterpriseName)
    {
        UserEnterpriseDTO userEnterpriseDTO = this.userEnterpriseService.loadByEnterpriseName(enterpriseName);
        return null == userEnterpriseDTO || userId == userEnterpriseDTO.getUserId();
    }


    /**
     * 企业认证
     * @param userModel
     * @param enterpriseRequest 企业认证需要信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void enterpriseAuth(UserModel userModel, EnterpriseRequest enterpriseRequest,String userIp){
        UserEnterpriseDTO userEnterpriseDTO = this.save(userModel,enterpriseRequest);
        this.userModifyService.updateEnterpriseVerifyStatus(userModel, userEnterpriseDTO.getStatus(), userIp);
    }


    /**
     *
     * @param userModel
     * @param enterpriseRequest  企业认证需要信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserEnterpriseDTO save(UserModel userModel, EnterpriseRequest enterpriseRequest)
    {

        UserEnterpriseDTO enterpriseDTO = this.userEnterpriseService.loadByUserId(userModel.getUserId());
        if (null == enterpriseDTO || enterpriseDTO.getStatus() == UserEnterpriseStatusEnum.DELETED.getStatus()) // 新注册
        {
            if (null == enterpriseDTO) // 没有就新建，有就更新
            {
                enterpriseDTO = new UserEnterpriseDTO();
            }
        }
        else if (null != enterpriseDTO && enterpriseDTO.getStatus() == UserEnterpriseStatusEnum.REJECTED.getStatus()) // 重新提交
        {
        }
        else
        {
            throw new IllegalArgumentException("认证信息已提交!");
        }

        if(enterpriseRequest.getType()==UserTypeEnum.ENTERPRISE.getType()){//如果是企业认证，则需要看企业名称是够已存在
            Validate.isTrue(isEnterpriseNameAvailable(userModel.getUserId(), enterpriseRequest.getEnterpriseName()), "企业名已存在!");
        }


        BeanUtils.copyProperties(enterpriseRequest, enterpriseDTO);//将传入的参数copy到enterpriseDTO

        enterpriseDTO.setUserId(userModel.getUserId());


        enterpriseDTO.setStatus(UserEnterpriseStatusEnum.VERIFYING.getStatus()); // 新注册、修改后的企业认证信息都是认证中状态
        enterpriseDTO.setLastEditor(userModel.getMobile());
        if (enterpriseDTO.getId() > 0)
        {
            this.userEnterpriseService.update(enterpriseDTO);
            this.memberService.asyncSaveUpdateLog(userModel, "Update fields", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), ContextManger.getContext().getUserIp());
        }
        else
        {
            this.userEnterpriseService.create(enterpriseDTO);
            this.memberService.asyncSaveUpdateLog(userModel, "First verify", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), ContextManger.getContext().getUserIp());
        }
        this.redBankService.asyncFireEvent(userModel.getUserId(), MessageTypeEnum.USER_NEW_ENTERPRISE_VERIFY, null, userModel.getUserId());
        return enterpriseDTO;
    }

    public UserEnterpriseDTO loadEnterpriseByUserId(int userId)
    {
        return userEnterpriseService.loadByUserId(userId);
    }


    public EnterpriseRequest loadEnterpriseByUserId(int userId,UserModel userModel)
    {
        UserEnterpriseDTO enterpriseDTO = userEnterpriseService.loadByUserId(userId);

        EnterpriseRequest enterpriseRequest = null;
        if(enterpriseDTO !=null){
            enterpriseRequest = new EnterpriseRequest();
            BeanUtils.copyProperties(userModel, enterpriseRequest);
            BeanUtils.copyProperties(enterpriseDTO, enterpriseRequest);
            enterpriseRequest.setEmail(userModel.getMail());


            UserEnterpriseStatusEnum statusEnum = UserEnterpriseStatusEnum.get(enterpriseDTO.getStatus());
            if (null != statusEnum)
            {
                enterpriseRequest.setEnterpriseVerifyStatusDesc(statusEnum.getMessage());
            }

            UserTypeEnum typeEnum = UserTypeEnum.get(enterpriseDTO.getType());
            if (null != typeEnum)
            {
                enterpriseRequest.setTypeDesc(typeEnum.getMessage());
            }

            if(enterpriseDTO.getCountryId()>0){
                enterpriseRequest.setCountryName(metadataProvider.getCountryName(enterpriseDTO.getCountryId()));
            }

            if(enterpriseDTO.getProvinceId()>0){
                enterpriseRequest.setProvinceName(metadataProvider.getProvinceName(enterpriseDTO.getProvinceId()));
            }

            if(enterpriseDTO.getCityId()>0){
                enterpriseRequest.setCityName(metadataProvider.getCityName(enterpriseDTO.getCityId()));
            }

            if(enterpriseDTO.getDistrictId()>0){
                enterpriseRequest.setDistrictName(metadataProvider.getAreaName(enterpriseDTO.getDistrictId()));
            }

            if(StringUtils.isNotEmpty(enterpriseDTO.getLicence())){
                enterpriseRequest.setLicenceUrl(fileUploadService.buildDiskUrl(enterpriseDTO.getLicence()));
            }

            if(StringUtils.isNotEmpty(enterpriseDTO.getIdentityFront())){
                enterpriseRequest.setIdentityFrontUrl(fileUploadService.buildDiskUrl(enterpriseDTO.getIdentityFront()));
            }

            if(StringUtils.isNotEmpty(enterpriseDTO.getIdentityBack())){
                enterpriseRequest.setIdentityBackUrl(fileUploadService.buildDiskUrl(enterpriseDTO.getIdentityBack()));
            }

            if(StringUtils.isNotEmpty(enterpriseDTO.getAttachmentOne())){
                enterpriseRequest.setAttachmentOneUrl(fileUploadService.buildDiskUrl(enterpriseDTO.getAttachmentOne()));
            }

            if(StringUtils.isNotEmpty(enterpriseDTO.getAttachmentTwo())){
                enterpriseRequest.setAttachmentTwoUrl(fileUploadService.buildDiskUrl(enterpriseDTO.getAttachmentTwo()));
            }

        }
        return enterpriseRequest;
    }


//    @Transactional(rollbackFor = Exception.class)
//    public void quickRegisterEnterprise(int userId, String path)
//    {
//        UserEnterpriseDTO enterpriseDTO = userEnterpriseService.loadByUserId(userId);
//        Validate.isTrue(enterpriseDTO == null, "已提交企业认证");
//
//        enterpriseDTO = new UserEnterpriseDTO();
//        enterpriseDTO.setUserId(userId);
//        enterpriseDTO.setType(0);
//        enterpriseDTO.setName("企业" + userId);
//        enterpriseDTO.setProvinceId(0);
//        enterpriseDTO.setCityId(0);
//        enterpriseDTO.setAddress("");
//        enterpriseDTO.setStatus(UserEnterpriseStatusEnum.VERIFYING.getStatus()); // 新注册、修改后的企业认证信息都是认证中状态
//        enterpriseDTO.setLastEditor(String.valueOf(userId));
//        this.userEnterpriseService.quickAdd(enterpriseDTO);
//    }


//    @Transactional(rollbackFor = Exception.class)
//    public void updateDescription(UserModel userModel, String description)
//    {
//        UserEnterpriseDTO enterpriseDTO = this.userEnterpriseService.loadByUserId(userModel.getUserId());
//        Validate.notNull(enterpriseDTO, "请先提交企业认证");
//        enterpriseDTO.setDescription(description);
//        this.userEnterpriseService.update(enterpriseDTO);
//        this.memberService.asyncSaveUpdateLog(userModel, "User update enterprise description", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), ContextManger.getContext().getUserIp());
//    }

    public Map<String, Object> getMemberInfoByUserId(int userId) {
        UserEnterpriseDTO enterpriseDTO = loadEnterpriseByUserId(userId);
        LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(userId);

        UserMemberStatusEnum statusEnum ;
        int authStatus = 0;
        int memberStatus = 0;
        Map<String, Object> dataMap = new HashMap<String, Object>();


        if(loanUserAuthInfoDTO != null){
            dataMap.put("authInfo",loanUserAuthInfoDTO);
            authStatus = loanUserAuthInfoDTO.getStatus();
        }else {
            dataMap.put("authInfo","");
        }

        if(enterpriseDTO != null ){
            dataMap.put("enterpriseInfo",enterpriseDTO);
            memberStatus = enterpriseDTO.getStatus();
            if(enterpriseDTO.getCountryId()>0){
                dataMap.put("countryName",metadataProvider.getCountryName(enterpriseDTO.getCountryId()));
            }

            if(enterpriseDTO.getProvinceId()>0){
                dataMap.put("provinceName",metadataProvider.getProvinceName(enterpriseDTO.getProvinceId()));
            }

            if(enterpriseDTO.getCityId()>0){
                dataMap.put("cityName",metadataProvider.getCityName(enterpriseDTO.getCityId()));
            }

            if(enterpriseDTO.getDistrictId()>0){
                dataMap.put("areaName",metadataProvider.getAreaName(enterpriseDTO.getDistrictId()));
            }
        }else{
            dataMap.put("enterpriseInfo","");
        }

        if(memberStatus == 0){
            memberStatus =2;
        }

        statusEnum = UserMemberStatusEnum.get(Integer.parseInt(String.valueOf(memberStatus)+String.valueOf(authStatus)));
        if(statusEnum != null){
            dataMap.put("status",statusEnum.getStatus());
            dataMap.put("statusDesc",statusEnum.getMessage());
        }else{
            dataMap.put("status",-1);
            dataMap.put("statusDesc","状态异常，请检查确认");
        }
        return dataMap;
    }
}
