/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Description:
 * <p/> 首页缓存数据定时刷新任务
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : HomeCacheRefreshJob.java
 */
@Service
public class HomeCacheRefreshJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeCacheRefreshJob.class);

//    @Autowired
//    private HomePageService homePageService;

    public void runJob()
    {
//        this.homePageService.refreshProductStatisData();
    }
}
