/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.account;

import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.dto.UserBankDTO;
import com.fruit.account.biz.service.UserBankService;
import com.fruit.base.biz.dto.BankInfoDTO;
import com.fruit.base.biz.service.BankInfoService;
import com.fruit.portal.meta.MetaArea;
import com.fruit.portal.meta.MetaCity;
import com.fruit.portal.meta.MetaProvince;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserBankRequest;
import com.fruit.portal.service.ContextManger;
import com.fruit.portal.vo.account.BankVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Description:
 * 用户银行卡相关
 * Create Author  : paul
 * Create Date    : 2017-05-18
 * Project        : partal-main-web
 * File Name      : BankService.java
 */
@Service
public class BankService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BankService.class);

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BankInfoService bankInfoService;


    private static final Object LOCKER_OBJ = new Object();

    private static final Map<Integer, Object> DB_LOCKER_MAP = new HashMap<Integer, Object>(100);

    private static final int MAX_ADD_NUMBER = 20;

    /**
     * 加载用户所有用户银行卡
     *
     * @param userId
     * @return
     */
    public List<UserBankDTO> loadAllBank(int userId)
    {
        List<UserBankDTO> bankDtos = userBankService.listByUserId(userId);
        LOGGER.info("[loadAllBank] selected banks are {}", bankDtos);
        if (null == bankDtos)
        {
            return Collections.EMPTY_LIST;
        }
        //20170525产品经理：银行卡去掉是否默认功能
//        else
//        {
//            Collections.sort(bankDtos, new Comparator<UserBankDTO>()
//            {
//                @Override
//                public int compare(UserBankDTO o1, UserBankDTO o2)
//                {
//                    int isDefault = o2.getIsDefault() - o1.getIsDefault();
//                    return isDefault == DBIsDefaultStatusEnum.ISNOTDEFAULT.getValue() ? o1.getId() - o2.getId() : isDefault; // 默认银行卡排第一，其他银行卡按添加时间顺序排列(因为id是自增的)
//                }
//            });
//        }
        return bankDtos;
    }

    /**
     * 加载指定的用户银行卡
     *
     * @param id
     * @return
     */
    public UserBankDTO loadBankById(int id)
    {
        return userBankService.loadById(id);
    }


    /**
     * 加载银行卡
     *
     * @param userId
     * @param bankCard
     * @return
     */
    public UserBankDTO loadBankByUserIdAndBankCard(int userId,String bankCard)
    {
        UserBankDTO bankDto = userBankService.loadByUserIdAndBankCard(userId,bankCard);

        return bankDto;
    }
    
    

    /**
     * 加载用户的默认用户银行卡(如果用户没有设置默认用户银行卡，则返回用户的第一个用户银行卡，若无用户银行卡则返回null)
     *
     * @param userId
     * @return
     */
    public UserBankDTO loadDefaultBank(int userId)
    {
        UserBankDTO result = userBankService.loadDefaultBank(userId);
        if (null == result)
        {
            List<UserBankDTO> banks = loadAllBank(userId);
            if (CollectionUtils.isNotEmpty(banks))
            {
                result = banks.get(0);
            }
        }
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateBank(UserModel userModel,UserBankDTO bank)
    {
        if (null != bank && bank.getId() > 0)
        {
            //20170525产品经理：银行卡去掉是否默认功能
//            if (bank.getIsDefault() == DBIsDefaultStatusEnum.ISDEFAULT.getValue())
//            {
//                cleanDefaultBank(bank.getUserId());
//            }
            userBankService.update(bank);
            this.memberService.asyncSaveUpdateLog(userModel, "Update bankCard", UserInfoUpdateLogTypeEnum.BANK_INFO_UPDATE.getType(), ContextManger.getContext().getUserIp());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public UserBankDTO addBank(UserModel userModel, UserBankRequest userBankRequest)
    {
        Validate.isTrue(StringUtils.isNotBlank(userBankRequest.getBankCard()), "银行卡号为空!");

        UserBankDTO bankDTOs = loadBankByUserIdAndBankCard(userModel.getUserId(),userBankRequest.getBankCard());
        Validate.isTrue(bankDTOs==null, "该银行卡号已存在!");

        //查询该客户已新增的银行卡数量，已达最高数量不允许新增
        Validate.isTrue(canAddByUpperLimit(userModel.getUserId()),"您已有银行卡已达上限："+MAX_ADD_NUMBER+"张！");


        UserBankDTO bankDTO = new UserBankDTO();

        BeanUtils.copyProperties(userBankRequest, bankDTO);//将传入的参数copy到bankDTO

        //20170525产品经理：银行卡去掉是否默认功能
//        if (DBIsDefaultStatusEnum.ISDEFAULT.getValue() == bankDTO.getIsDefault())
//        {
//            cleanDefaultBank(bankDTO.getUserId());
//        }
        userBankService.create(bankDTO);

        this.memberService.asyncSaveUpdateLog(userModel, "Add bankCard", UserInfoUpdateLogTypeEnum.BANK_INFO_UPDATE.getType(), ContextManger.getContext().getUserIp());

        return bankDTO;
    }


    /**
     * 通过userId查询此客户已经存在的银行卡数量，与设定的上限数进行对比，如果超过上限，则不允许新增
     * @param userId
     * @return
     */
    private boolean canAddByUpperLimit(int userId){
        int count = userBankService.countByUserId(userId);
        if(count>=MAX_ADD_NUMBER){
            return false;
        }else{
            return true;
        }
    }


    //20170525产品经理：银行卡去掉是否默认功能
//    /**
//     * 将指定用户的默认银行卡(如果有)设置为非默认银行卡
//     *
//     * @param userId
//     * @return
//     */
//    private void cleanDefaultBank(int userId)
//    {
//        UserBankDTO userBankDTO = userBankService.loadDefaultBank(userId);
//        if (null != userBankDTO)
//        {
//            int isDefault = DBIsDefaultStatusEnum.ISNOTDEFAULT.getValue(); // 0表示未选中
//            userBankDTO.setIsDefault(isDefault);
//            userBankService.update(userBankDTO);
//        }
//    }

    //20170525产品经理：银行卡去掉是否默认功能
//    public void setDefaultBank(UserModel userModel,int userId, int id)
//    {
//        Object dbLocker = null;
//        synchronized (LOCKER_OBJ)
//        {
//            dbLocker = DB_LOCKER_MAP.get(userId);
//            if (null == dbLocker)
//            {
//                if (DB_LOCKER_MAP.size() > 75)
//                {
//                    DB_LOCKER_MAP.clear();
//                }
//                dbLocker = new Object();
//                DB_LOCKER_MAP.put(userId, dbLocker);
//            }
//        }
//        synchronized (dbLocker)
//        {
//            cleanDefaultBank(userId);
//            UserBankDTO userBankDTO = userBankService.loadById(id);
//            if (null != userBankDTO)
//            {
//                userBankDTO.setIsDefault(DBIsDefaultStatusEnum.ISDEFAULT.getValue());
//                userBankService.update(userBankDTO);
//                this.memberService.asyncSaveUpdateLog(userModel, "Set default bankCard", UserInfoUpdateLogTypeEnum.BANK_INFO_UPDATE.getType(), ContextManger.getContext().getUserIp());
//            }
//        }
//    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteBank(UserModel userModel,int id)
    {
        userBankService.delete(id);
        this.memberService.asyncSaveUpdateLog(userModel, "Delete bankCard", UserInfoUpdateLogTypeEnum.BANK_INFO_UPDATE.getType(), ContextManger.getContext().getUserIp());
    }

    public BankVo wrapVO(UserBankDTO bankDTO)
    {
        BankVo bankVo = new BankVo();

        BeanUtils.copyProperties(bankDTO, bankVo);//将bankDTOcopy到bankVo

        //设置省市区
        MetaProvince province = metadataProvider.getProvince(bankDTO.getProvinceId());
        MetaCity city = metadataProvider.getCity(bankDTO.getCityId());
        MetaArea area = metadataProvider.getArea(bankDTO.getDistrictId());
        BankInfoDTO bankInfoDTO = bankInfoService.loadById(bankDTO.getBankTypeId());
        bankVo.setProvinceName(province.getName());
        bankVo.setCityName(city.getName());
        bankVo.setDistrictName(area.getName());
        bankVo.setBankTypeName(bankInfoDTO.getName());

        return bankVo;
    }

    public List<BankVo> wrapVOs(List<UserBankDTO> bankDTOs)
    {
        List<BankVo> result = null;
        if (CollectionUtils.isNotEmpty(bankDTOs))
        {
            result = new ArrayList<BankVo>(bankDTOs.size());
            for (UserBankDTO bankDTO : bankDTOs)
            {
                BankVo bankVo = this.wrapVO(bankDTO);
                result.add(bankVo);
            }
        }
        return result;
    }
}
