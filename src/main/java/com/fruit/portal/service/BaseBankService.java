package com.fruit.portal.service;/*
 * Create Author  : paul
 * Create  Time   : 17/5/23 下午5:24
 * Project        : promotion
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */


import com.fruit.base.biz.dto.BankInfoDTO;
import com.fruit.base.biz.service.BankInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseBankService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseBankService.class);

    @Autowired
    private BankInfoService bankInfoService;


    /**
     * 根据id加载银行信息
     * @param id
     * @return
     */
    public BankInfoDTO loadBankInfoById(int id)
    {
        return bankInfoService.loadById(id);
    }


    /**
     * 加载所有的银行信息
     * @return
     */
    public List<BankInfoDTO> loadAll()
    {
        return bankInfoService.loadAll();
    }

}
