/*
 *
 * Copyright (c) 2017 by wuhan  Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.neworder;

import com.fruit.newOrder.biz.dto.*;
import com.fruit.newOrder.biz.service.*;
import com.fruit.portal.vo.neworder.GoodsCommodityInfoVO;
import com.fruit.portal.vo.neworder.GoodsProductInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description:
 * 商品相关
 * Create Author  : paul
 * Create Date    : 2017-09-07
 * Project        : partal-main-web
 * File Name      : GoodsInfoShowService.java
 */
@Service
public class GoodsInfoShowService
{
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @Autowired
    private GoodsVarietyService goodsVarietyService;

    @Autowired
    private GoodsProductInfoService goodsProductInfoService;

    @Autowired
    private GoodsCommodityInfoService goodsCommodityInfoService;

    @Autowired
    private GoodsProductPropertyService goodsProductPropertyService;

    @Autowired
    private GoodsCommodityPropertyService goodsCommodityPropertyService;

    @Autowired
    private GoodsProductPicService goodsProductPicService;

    @Autowired
    private GoodsCommodityPicService goodsCommodityPicService;


    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    /**
     * 查询所有商品类别
     */
    public List<GoodsCategoryDTO> loadAllGoodsCategories(){
        List<GoodsCategoryDTO> goodsCategories = goodsCategoryService.loadAllList();
        return goodsCategories;
    }

    /**
     * 根据商品类别查询所有品种
     */
    public List<GoodsVarietyDTO> loadGoodsVarietiesByCategory(int categoryId){
        List<GoodsVarietyDTO> goodsVarieties = goodsVarietyService.loadAllListByCategoryId(categoryId);
        return goodsVarieties;
    }


    /**
     * 根据品种获取所有产品
     * @param varietyId
     * @param withCommodities 是否在返回的产品列表中包含商品
     * @return
     */
    public List<GoodsProductInfoVO> loadGoodsProductInfosByVariety(int varietyId,boolean withCommodities){
        List<GoodsProductInfoDTO> goodsProductInfos = goodsProductInfoService.loadAllListByVarietyId(varietyId);

        //根据产品获取产品的图片信息
        List<GoodsProductInfoVO> goodsProductInfoVOs = getGoodsProductInfoVOLists(goodsProductInfos,withCommodities);

        return goodsProductInfoVOs;
    }


    /**
     * 根据产品获取所有商品
     */
    public List<GoodsCommodityInfoVO> loadGoodsCommodityInfosByProduct(int productId){
        List<GoodsCommodityInfoDTO> goodsCommodityInfos = null;
        //先获取产品的属性list
        List<GoodsProductPropertyDTO> goodsProductProperties =  goodsProductPropertyService.loadAllListByProductId(productId);

        List<Integer> goodsCommodityIds = null;
        if(CollectionUtils.isNotEmpty(goodsProductProperties)){
            goodsCommodityIds = goodsCommodityPropertyService.loadCommodityIdsByPropertyValueId(goodsProductProperties.get(0).getPropertyValueId());

            List<Integer> goodsCommodityIdsOther = null;
            for (int i=1;i<goodsProductProperties.size();++i){
                goodsCommodityIdsOther = goodsCommodityPropertyService.loadCommodityIdsByPropertyValueId(goodsProductProperties.get(i).getPropertyValueId());
                goodsCommodityIds.retainAll(goodsCommodityIdsOther);//获取交集
            }
        }

        if(CollectionUtils.isNotEmpty(goodsProductProperties)){
            //根据获取的商品id查询得出商品goodsCommodityIds
            goodsCommodityInfos = goodsCommodityInfoService.loadAllListByIds(goodsCommodityIds);
        }
        List<GoodsCommodityInfoVO> goodsCommodityVOs =  getGoodsCommodityInfoVOLists(goodsCommodityInfos);

        Collections.sort(goodsCommodityVOs, new Comparator<GoodsCommodityInfoVO>(){

            public int compare(GoodsCommodityInfoVO o1, GoodsCommodityInfoVO o2) {

                //
                if(o1.getSortid() > o2.getSortid()){
                    return 1;
                }
                if(o1.getSortid() == o2.getSortid()){
                    return 0;
                }
                return -1;
            }
        });


        return goodsCommodityVOs;
    }

    /**
     * 根据商品类别获取所有商品
     */
    public List<GoodsCommodityInfoVO> loadGoodsCommodityInfosByCategory(int categoryId){

        List<GoodsCommodityInfoDTO> goodsCommodityInfos = goodsCommodityInfoService.loadAllListByCategoryId(categoryId);

        List<GoodsCommodityInfoVO> goodsCommodityVOs =  getGoodsCommodityInfoVOLists(goodsCommodityInfos);
        return goodsCommodityVOs;
    }

    /**
     * 根据商品品种获取所有商品
     */
    public List<GoodsCommodityInfoVO> loadGoodsCommodityInfosByVariety(int varietyId){

        List<GoodsCommodityInfoDTO> goodsCommodityInfos = goodsCommodityInfoService.loadAllListByVarietyId(varietyId);

        List<GoodsCommodityInfoVO> goodsCommodityVOs =  getGoodsCommodityInfoVOLists(goodsCommodityInfos);
        return goodsCommodityVOs;
    }


    /**
     * 线程设置infoDTO
     * @param infoDTOs
     * @param withCommodities 是否在返回的产品列表中包含商品
     * @return
     */
    private List<GoodsProductInfoVO> getGoodsProductInfoVOLists(List<GoodsProductInfoDTO> infoDTOs,boolean withCommodities)
    {
        List<FutureTask<GoodsProductInfoVO>> tasks = new ArrayList<FutureTask<GoodsProductInfoVO>>(infoDTOs.size());

        List<GoodsProductInfoVO> infoModelList = new ArrayList<GoodsProductInfoVO>(infoDTOs.size());
        if (CollectionUtils.isNotEmpty(infoDTOs))
        {
            for (GoodsProductInfoDTO infoDTO : infoDTOs)
            {
                FutureTask<GoodsProductInfoVO> futureTask = new FutureTask(new GoodsProductInfoVoCallable(infoDTO,withCommodities));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<GoodsProductInfoVO> task : tasks)
            {
                try
                {
                    GoodsProductInfoVO infoModel = task.get();
                    infoModelList.add(infoModel);
                }
                catch (InterruptedException e)
                {
                    //
                }
                catch (ExecutionException e)
                {
                    //
                }
            }
        }
        return infoModelList;
    }


    class GoodsProductInfoVoCallable implements Callable<GoodsProductInfoVO>
    {
        private GoodsProductInfoDTO infoDTO;

        private boolean withCommodities;

        public GoodsProductInfoVoCallable(GoodsProductInfoDTO infoDTO,boolean withCommodities)
        {
            this.infoDTO = infoDTO;
            this.withCommodities = withCommodities;
        }

        @Override
        public GoodsProductInfoVO call() throws Exception
        {
            try {
                return loadGoodsProductInfoVo(infoDTO,withCommodities);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }


    /**
     * 加载产品信息---图片和商品信息
     * @param infoDTO
     * @param withCommodities  是否在返回的产品列表中包含商品信息
     * @return
     */
    protected GoodsProductInfoVO loadGoodsProductInfoVo(GoodsProductInfoDTO infoDTO,boolean withCommodities)
    {
        GoodsProductInfoVO infoVO = null;
        if (infoDTO != null)
        {
            infoVO = new GoodsProductInfoVO();

            BeanUtils.copyProperties(infoDTO, infoVO);

            List<GoodsProductPicDTO> goodsProductPicDTOs = goodsProductPicService.loadAllListByProductId(infoDTO.getId());
            infoVO.setGoodsProductPicDTOs(goodsProductPicDTOs);

            if(withCommodities){//返回的产品列表中加入商品信息
                List<GoodsCommodityInfoVO> goodsCommodityInfos = loadGoodsCommodityInfosByProduct(infoDTO.getId());
                infoVO.setGoodsCommodityInfos(goodsCommodityInfos);
            }
        }
        return infoVO;
    }


    private List<GoodsCommodityInfoVO> getGoodsCommodityInfoVOLists(List<GoodsCommodityInfoDTO> infoDTOs)
    {
        List<FutureTask<GoodsCommodityInfoVO>> tasks = new ArrayList<FutureTask<GoodsCommodityInfoVO>>(infoDTOs.size());

        List<GoodsCommodityInfoVO> infoModelList = new ArrayList<GoodsCommodityInfoVO>(infoDTOs.size());
        if (CollectionUtils.isNotEmpty(infoDTOs))
        {
            for (GoodsCommodityInfoDTO infoDTO : infoDTOs)
            {
                FutureTask<GoodsCommodityInfoVO> futureTask = new FutureTask(new GoodsCommodityInfoVOCallable(infoDTO));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<GoodsCommodityInfoVO> task : tasks)
            {
                try
                {
                    GoodsCommodityInfoVO infoModel = task.get();
                    infoModelList.add(infoModel);
                }
                catch (InterruptedException e)
                {
                    //
                }
                catch (ExecutionException e)
                {
                    //
                }
            }
        }
        return infoModelList;
    }


    class GoodsCommodityInfoVOCallable implements Callable<GoodsCommodityInfoVO>
    {
        private GoodsCommodityInfoDTO infoDTO;

        public GoodsCommodityInfoVOCallable(GoodsCommodityInfoDTO infoDTO)
        {
            this.infoDTO = infoDTO;
        }

        @Override
        public GoodsCommodityInfoVO call() throws Exception
        {
            try {
                return loadGoodsCommodityInfoVO(infoDTO);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }


    /**
     * 加载产品信息---图片
     * @param infoDTO
     * @return
     */
    protected GoodsCommodityInfoVO loadGoodsCommodityInfoVO(GoodsCommodityInfoDTO infoDTO)
    {
        GoodsCommodityInfoVO infoVO = null;
        if (infoDTO != null)
        {
            infoVO = new GoodsCommodityInfoVO();

            BeanUtils.copyProperties(infoDTO, infoVO);

            List<GoodsCommodityPicDTO> goodsCommodityPicDTOs = goodsCommodityPicService.loadAllListByCommodityId(infoDTO.getId());

            infoVO.setGoodsCommodityPicDTOs(goodsCommodityPicDTOs);
        }
        return infoVO;
    }



}
