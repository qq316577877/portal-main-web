/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.account;

import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.dto.UserSupplierDTO;
import com.fruit.account.biz.service.UserSupplierService;
import com.fruit.portal.meta.*;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserSupplierRequest;
import com.fruit.portal.service.ContextManger;
import com.fruit.portal.vo.account.SupplierVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Description:
 * 用户供应商相关
 * Create Author  : paul
 * Create Date    : 2017-05-18
 * Project        : partal-main-web
 * File Name      : SupplierService.java
 */
@Service
public class SupplierService
{
    @Autowired
    private UserSupplierService userSupplierService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;

    private static final Object LOCKER_OBJ = new Object();

    private static final Map<Integer, Object> DB_LOCKER_MAP = new HashMap<Integer, Object>(100);

    private static final int MAX_ADD_NUMBER = 20;

    /**
     * 加载用户所有收货供应商
     *
     * @param userId
     * @return
     */
    public List<UserSupplierDTO> loadAllSupplier(int userId)
    {
        List<UserSupplierDTO> supplierDtos = userSupplierService.listByUserId(userId);
        if (null == supplierDtos)
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            return supplierDtos;
        }
    }

    /**
     * 加载指定的收货供应商
     *
     * @param id
     * @return
     */
    public UserSupplierDTO loadSupplierById(int id)
    {
        return userSupplierService.loadById(id);
    }



    public void updateSupplier(UserModel userModel,UserSupplierDTO supplier)
    {
        if (null != supplier && supplier.getId() > 0)
        {
            userSupplierService.update(supplier);
            this.memberService.asyncSaveUpdateLog(userModel, "Update supplier", UserInfoUpdateLogTypeEnum.SUPPLIER_UPDATE.getType(), ContextManger.getContext().getUserIp());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public UserSupplierDTO addSupplier(UserModel userModel, UserSupplierRequest userSupplierRequest)
    {

        //查询该客户已新增的银行卡数量，已达最高数量不允许新增
        Validate.isTrue(canAddByUpperLimit(userModel.getUserId()),"您已有供应商已达上限："+MAX_ADD_NUMBER+"！");

        UserSupplierDTO supplierDTO = new UserSupplierDTO();

        BeanUtils.copyProperties(userSupplierRequest, supplierDTO);//将传入的参数copy到supplierDTO

        userSupplierService.create(supplierDTO);
        this.memberService.asyncSaveUpdateLog(userModel, "Add supplier", UserInfoUpdateLogTypeEnum.SUPPLIER_UPDATE.getType(), ContextManger.getContext().getUserIp());
        return supplierDTO;
    }

    /**
     * 通过userId查询此客户已经存在的供应商数量，与设定的上限数进行对比，如果超过上限，则不允许新增
     * @param userId
     * @return
     */
    private boolean canAddByUpperLimit(int userId){
        int count = userSupplierService.countByUserId(userId);
        if(count>=MAX_ADD_NUMBER){
            return false;
        }else{
            return true;
        }
    }



    @Transactional(rollbackFor = Exception.class)
    public void deleteSupplier(UserModel userModel,int id)
    {
        userSupplierService.delete(id);
        this.memberService.asyncSaveUpdateLog(userModel, "Delete supplier", UserInfoUpdateLogTypeEnum.SUPPLIER_UPDATE.getType(), ContextManger.getContext().getUserIp());
    }

    public SupplierVo wrapVO(UserSupplierDTO supplierDTO)
    {
        SupplierVo supplierVo = new SupplierVo();

        BeanUtils.copyProperties(supplierDTO, supplierVo);//将supplierDTOcopy到supplierVo

        //设置国省市区
        MetaCountry country = metadataProvider.getCountry(supplierDTO.getCountryId());
        MetaProvince province = metadataProvider.getProvince(supplierDTO.getProvinceId());
        MetaCity city = metadataProvider.getCity(supplierDTO.getCityId());
        MetaArea area = metadataProvider.getArea(supplierDTO.getDistrictId());
        if(country!=null){
            supplierVo.setCountryName(country.getName());
        }
        if(province!=null){
            supplierVo.setProvinceName(province.getName());
        }
        if(city!=null){
            supplierVo.setCityName(city.getName());
        }
        if(area!=null){
            supplierVo.setDistrictName(area.getName());
        }
        return supplierVo;
    }

    public List<SupplierVo> wrapVOs(List<UserSupplierDTO> supplierDTOs)
    {
        List<SupplierVo> result = null;
        if (CollectionUtils.isNotEmpty(supplierDTOs))
        {
            result = new ArrayList<SupplierVo>(supplierDTOs.size());
            for (UserSupplierDTO supplierDTO : supplierDTOs)
            {
                SupplierVo supplierVo = this.wrapVO(supplierDTO);
                result.add(supplierVo);
            }
        }
        return result;
    }
}
