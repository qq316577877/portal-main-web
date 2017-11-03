/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.account;

import com.fruit.portal.vo.common.IdValueVO;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-26
 * Project        : message-biz
 * File Name      : FavoriteProductVO.java
 */
public class FavoriteSearchResultVO implements Serializable
{
    private static final long serialVersionUID = 6163184388959123364L;

    private List<IdValueVO> brands;

    private List<IdValueVO> stock_status;

    private int page_size = 15;

    private int page_no = 1;

    private int total_count = 0;

    private int page_count = 1;

    private List<FavoriteProductVO> favorites;

    private String list_url;

    private String search_url;

    private String detail_url;

    public List<IdValueVO> getBrands()
    {
        return brands;
    }

    public void setBrands(List<IdValueVO> brands)
    {
        this.brands = brands;
    }

    public List<IdValueVO> getStock_status()
    {
        return stock_status;
    }

    public void setStock_status(List<IdValueVO> stock_status)
    {
        this.stock_status = stock_status;
    }

    public int getPage_size()
    {
        return page_size;
    }

    public void setPage_size(int page_size)
    {
        this.page_size = page_size;
    }

    public int getPage_no()
    {
        return page_no;
    }

    public void setPage_no(int page_no)
    {
        this.page_no = page_no;
    }

    public int getTotal_count()
    {
        return total_count;
    }

    public void setTotal_count(int total_count)
    {
        this.total_count = total_count;
    }

    public int getPage_count()
    {
        return page_count;
    }

    public void setPage_count(int page_count)
    {
        this.page_count = page_count;
    }

    public List<FavoriteProductVO> getFavorites()
    {
        return favorites;
    }

    public void setFavorites(List<FavoriteProductVO> favorites)
    {
        this.favorites = favorites;
    }

    public String getList_url()
    {
        return list_url;
    }

    public void setList_url(String list_url)
    {
        this.list_url = list_url;
    }

    public String getSearch_url()
    {
        return search_url;
    }

    public void setSearch_url(String search_url)
    {
        this.search_url = search_url;
    }

    public String getDetail_url()
    {
        return detail_url;
    }

    public void setDetail_url(String detail_url)
    {
        this.detail_url = detail_url;
    }
}
