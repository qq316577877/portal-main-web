package com.fruit.portal.service;/*
 * Create Author  : chao.ji
 * Create  Time   : 15/3/27 下午2:24
 * Project        : promotion
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

import com.fruit.base.file.upload.aliyun.client.FileUploadClient;
import com.fruit.portal.service.common.UrlConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService
{

    @Autowired
    protected UrlConfigService urlConfigService;

}
