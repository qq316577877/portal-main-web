/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.loan;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.loan.biz.common.*;
import com.fruit.loan.biz.dto.LoanUserApplyCreditDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanUserApplyCreditService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.loan.LoanAuthCreditModel;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.model.loan.LoanUserCreditInfoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

/**
 * Description:
 * 用户资金服务申请相关
 * Create Author  : paul
 * Create Date    : 2017-06-14
 * Project        : partal-main-web
 * File Name      : LoanAuthCreditService.java
 */
@Service
public class LoanAuthCreditService
{
    @Autowired
    private LoanUserAuthInfoService loanUserAuthInfoService;


    @Autowired
    private LoanUserApplyCreditService loanUserApplyCreditService;


    @Autowired
    private LoanUserCreditInfoService loanUserCreditInfoService;


    @Autowired
    private MetadataProvider metadataProvider;


    private ExecutorService executorService = Executors.newFixedThreadPool(5);



    /**
     * 查询用户实名认证详情
     * @param userId
     * @return
     */
    public LoanUserAuthInfoModel loadloanUserAuthInfoByUserId(int userId)
    {
        LoanUserAuthInfoModel loanUserAuthInfoModel = null;

        LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(userId);

        if(loanUserAuthInfoDTO!=null){
            loanUserAuthInfoModel = new LoanUserAuthInfoModel();
            BeanUtils.copyProperties(loanUserAuthInfoDTO, loanUserAuthInfoModel);

            LoanUserAuthStatusEnum statusEnum = LoanUserAuthStatusEnum.get(loanUserAuthInfoDTO.getStatus());
            if (null != statusEnum)
            {
                loanUserAuthInfoModel.setStatusDesc(statusEnum.getMessage());
            }
        }

        return loanUserAuthInfoModel;
    }




    /**
     * 查询用户申请贷款信息
     * @param userId
     * @return
     */
    public LoanAuthCreditModel loadloanUserApplyCreditInfoByUserId(int userId)
    {
        LoanAuthCreditModel loanAuthCreditModel = null;

        LoanUserApplyCreditDTO loanUserApplyCreditDTO = loanUserApplyCreditService.loadByUserId(userId);

        if(loanUserApplyCreditDTO!=null){
            loanAuthCreditModel = setUserApplyCreditInfoByDTO(loanUserApplyCreditDTO);
        }

        return loanAuthCreditModel;
    }


    /**
     * 根据 loanUserApplyCreditDTO修改为 LoanAuthCreditModel
     * @param loanUserApplyCreditDTO
     * @return
     */
    private LoanAuthCreditModel setUserApplyCreditInfoByDTO(LoanUserApplyCreditDTO loanUserApplyCreditDTO)
    {
        LoanAuthCreditModel loanAuthCreditModel = null;
        if(loanUserApplyCreditDTO!=null){
            loanAuthCreditModel = new LoanAuthCreditModel();

            BeanUtils.copyProperties(loanUserApplyCreditDTO, loanAuthCreditModel);

            //typeDesc
            UserTypeEnum typeEnum = UserTypeEnum.get(loanUserApplyCreditDTO.getType());
            if (null != typeEnum)
            {
                loanAuthCreditModel.setTypeDesc(typeEnum.getMessage());
            }

            //maritalStatus
            LoanUserMarriageCodeEnum maritalStatusEnum = LoanUserMarriageCodeEnum.get(loanUserApplyCreditDTO.getMaritalStatus());
            if (null != maritalStatusEnum)
            {
                loanAuthCreditModel.setMaritalStatusDesc(maritalStatusEnum.getMessage());
            }

            //statusDesc
            LoanUserApplyCreditStatusEnum statusEnum = LoanUserApplyCreditStatusEnum.get(loanUserApplyCreditDTO.getStatus());
            if (null != statusEnum)
            {
                loanAuthCreditModel.setStatusDesc(statusEnum.getMessage());
            }


            //CountryName  ProvinceName  CityName  DistrictName
            if(loanUserApplyCreditDTO.getCountryId()>0){
                loanAuthCreditModel.setCountryName(metadataProvider.getCountryName(loanUserApplyCreditDTO.getCountryId()));
            }

            if(loanUserApplyCreditDTO.getProvinceId()>0){
                loanAuthCreditModel.setProvinceName(metadataProvider.getProvinceName(loanUserApplyCreditDTO.getProvinceId()));
            }

            if(loanUserApplyCreditDTO.getCityId()>0){
                loanAuthCreditModel.setCityName(metadataProvider.getCityName(loanUserApplyCreditDTO.getCityId()));
            }

            if(loanUserApplyCreditDTO.getDistrictId()>0){
                loanAuthCreditModel.setDistrictName(metadataProvider.getAreaName(loanUserApplyCreditDTO.getDistrictId()));
            }

        }


        return loanAuthCreditModel;
    }



    /**
     * 查询用户信贷信息
     * @param userId
     * @return
     */
    public LoanUserCreditInfoDTO loadloanUserCreditInfoByUserId(int userId)
    {

        LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);

        return loanUserCreditInfoDTO;
    }


    /**
     * 根据 LoanUserCreditInfoDTO修改为 LoanUserCreditInfoModel
     * @param loanUserCreditInfoDTO
     * @return
     */
    private LoanUserCreditInfoModel setUserCreditInfoInfoByDTO(LoanUserCreditInfoDTO loanUserCreditInfoDTO)
    {
        LoanUserCreditInfoModel loanUserCreditInfoModel = null;
        if(loanUserCreditInfoDTO!=null){
            loanUserCreditInfoModel = new LoanUserCreditInfoModel();

            BeanUtils.copyProperties(loanUserCreditInfoDTO, loanUserCreditInfoModel);

            //typeDesc
            UserTypeEnum typeEnum = UserTypeEnum.get(loanUserCreditInfoDTO.getType());
            if (null != typeEnum)
            {
                loanUserCreditInfoModel.setTypeDesc(typeEnum.getMessage());
            }


            //statusDesc
            LoanUserCreditStatusEnum statusEnum = LoanUserCreditStatusEnum.get(loanUserCreditInfoDTO.getStatus());
            if (null != statusEnum)
            {
                loanUserCreditInfoModel.setStatusDesc(statusEnum.getMessage());
            }

        }

        return loanUserCreditInfoModel;
    }


    /**
     * 实名认证
     * @param loanUserAuthInfoDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public LoanUserAuthInfoDTO addLoanUserAuth(LoanUserAuthInfoDTO loanUserAuthInfoDTO) throws Exception
    {
        LoanUserAuthInfoDTO loanUserAuthInfoAuthDTO = null;

        int id = loanUserAuthInfoService.loanCreateNameAuthentication(loanUserAuthInfoDTO);//实名认证接口

        if(id>0){
            //返回的id来查询信息
            loanUserAuthInfoAuthDTO = loanUserAuthInfoService.loadById(id);
        }


        return loanUserAuthInfoAuthDTO;
    }


    /**
     * 申请贷款--个人
     * @param loanUserApplyCreditDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public LoanAuthCreditModel addLoanUserAuthPersonal(LoanUserApplyCreditDTO loanUserApplyCreditDTO) throws Exception
    {
        LoanAuthCreditModel loanAuthCreditModel = new LoanAuthCreditModel();

        String famAdr = setFamAdrByModel(loanUserApplyCreditDTO);
        loanUserApplyCreditDTO.setAddress(famAdr);//设置详细家庭地址

        int id = loanUserApplyCreditService.loanCreateUserAndCreateApplyCredit(loanUserApplyCreditDTO);
        //返回的id来查询信息
        LoanUserApplyCreditDTO loanUserApplyCreditDTOReturn = loanUserApplyCreditService.loadById(id);

        if(loanUserApplyCreditDTOReturn!=null){
            loanAuthCreditModel = setUserApplyCreditInfoByDTO(loanUserApplyCreditDTOReturn);
        }

        return loanAuthCreditModel;
    }


    /**
     * 根据LoanUserApplyCreditDTO 获取详细的家庭住址
     * @param loanUserApplyCreditDTO
     * @return
     */
    private String setFamAdrByModel(LoanUserApplyCreditDTO loanUserApplyCreditDTO){
        StringBuffer famAdrBuffer = new StringBuffer("");
        if(loanUserApplyCreditDTO.getCountryId()>0){
            String countryName = metadataProvider.getCountryName(loanUserApplyCreditDTO.getCountryId());
            famAdrBuffer.append(countryName);
        }

        if(loanUserApplyCreditDTO.getProvinceId()>0){
            String provinceName = metadataProvider.getProvinceName(loanUserApplyCreditDTO.getProvinceId());
            famAdrBuffer.append(provinceName);
        }

        if(loanUserApplyCreditDTO.getCityId()>0){
            String cityName = metadataProvider.getCityName(loanUserApplyCreditDTO.getCityId());
            famAdrBuffer.append(cityName);
        }

        if(loanUserApplyCreditDTO.getDistrictId()>0){
            String areaName = metadataProvider.getAreaName(loanUserApplyCreditDTO.getDistrictId());
            famAdrBuffer.append(areaName);
        }
        String address = loanUserApplyCreditDTO.getAddress();

        famAdrBuffer.append("-"+address);

        return famAdrBuffer.toString();
    }


    /**
     * 申请贷款--企业
     * @param loanUserApplyCreditDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public LoanAuthCreditModel addLoanUserAuthEnterprise(LoanUserApplyCreditDTO loanUserApplyCreditDTO) throws Exception
    {
        LoanAuthCreditModel loanAuthCreditModel = new LoanAuthCreditModel();

        String famAdr = setFamAdrByModel(loanUserApplyCreditDTO);
        loanUserApplyCreditDTO.setAddress(famAdr);//设置详细家庭地址

        int id = loanUserApplyCreditService.loanCreateUserAndCreateApplyCredit(loanUserApplyCreditDTO);
        //返回的id来查询信息
        LoanUserApplyCreditDTO loanUserApplyCreditDTOReturn = loanUserApplyCreditService.loadById(id);


        if(loanUserApplyCreditDTOReturn!=null){
            loanAuthCreditModel = setUserApplyCreditInfoByDTO(loanUserApplyCreditDTOReturn);
        }

        return loanAuthCreditModel;
    }



}
