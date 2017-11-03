/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.home;

import com.alibaba.fastjson.JSON;
import com.fruit.portal.biz.common.ClickItemTypeEnum;
import com.fruit.portal.biz.dto.ClickItemDTO;
import com.fruit.portal.biz.service.ClickItemService;
import com.fruit.portal.service.common.FileUploadService;
import com.fruit.portal.vo.home.ClickItemVO;
import com.fruit.portal.vo.home.DivVO;
import com.fruit.portal.vo.home.HomeVO;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-06-01
 * Project        : www.fruit.com
 * File Name      : HomePageService.java
 */
@Service
public class HomeDataService
{

    private static String HOME_DATA_KEY = "__HOME-DATA";

    @Resource
    protected CacheClient cacheClient;

    @Autowired
    private ClickItemService clickItemService;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     *
     *
     * @return
     */
    public HomeVO getHomeDataCache()
    {
        String result = this.cacheClient.get(HOME_DATA_KEY);
        if(result == null){
            HomeVO homeVO = this.loadHomeData();
            this.cacheClient.set(HOME_DATA_KEY, JSON.toJSONString(homeVO));
            return homeVO;
        }
        return JSON.parseObject(result,HomeVO.class);
    }

    public void removeHomeDataCache()
    {
        this.cacheClient.del(HOME_DATA_KEY);
    }

    private HomeVO loadHomeData()
    {
        HomeVO homeVO = new HomeVO();
        homeVO.setSlider(this.loadSliderVos());
        homeVO.setNotice(this.loadNoticeVos());
        homeVO.setNews(this.loadNewsVos());
        return homeVO;
    }

    private List<DivVO> loadSliderVos()
    {
        List<DivVO> result = new ArrayList<DivVO>();
        List<ClickItemDTO> clickItemDTOs = this.clickItemService.listByType(ClickItemTypeEnum.BANNER.getType(), 5);
        if (CollectionUtils.isNotEmpty(clickItemDTOs))
        {
            for (ClickItemDTO itemDTO : clickItemDTOs)
            {
                String imgUrl = this.fileUploadService.buildDiskUrl(itemDTO.getImgUrl());
                DivVO divVO = new DivVO(imgUrl, itemDTO.getLink(), itemDTO.getTitle());
                result.add(divVO);
            }
        }
        return result;
    }

    private List<ClickItemVO> loadNoticeVos()
    {
        return this.loadByTypeAndSize(ClickItemTypeEnum.NOTICE.getType(), 5);
    }

    private List<ClickItemVO> loadNewsVos()
    {
        return this.loadByTypeAndSize(ClickItemTypeEnum.NEWS.getType(), 5);
    }

    private List<ClickItemVO> loadByTypeAndSize(int type, int size)
    {
        List<ClickItemVO> result = new ArrayList<ClickItemVO>();
        List<ClickItemDTO> clickItemDTOs = this.clickItemService.listByType(type, size);
        if (CollectionUtils.isNotEmpty(clickItemDTOs))
        {
            for (ClickItemDTO itemDTO : clickItemDTOs)
            {
                ClickItemVO itemVO = new ClickItemVO();
                BeanUtils.copyProperties(itemDTO, itemVO, new String [] {"time"});
                String imgUrl = this.fileUploadService.buildDiskUrl(itemDTO.getImgUrl());
                itemVO.setImgUrl(imgUrl);
                itemVO.setTime(DateFormatUtils.ISO_DATE_FORMAT.format(itemDTO.getTime()));
                result.add(itemVO);
            }
        }
        return result;

    }

}
