/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.vo;

import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : PageVO.java
 */
public class PageVO
{
    private int page_size;

    private int page_no;

    private int total_count;

    private int start_count;

    private int end_count;

    private int page_count;

    private List<Integer> page_index;

    public int getEnd_count()
    {
        return end_count;
    }

    public void setEnd_count(int end_count)
    {
        this.end_count = end_count;
    }

    public int getPage_count()
    {
        return page_count;
    }

    public void setPage_count(int page_count)
    {
        this.page_count = page_count;
    }

    public List<Integer> getPage_index()
    {
        return page_index;
    }

    public void setPage_index(List<Integer> page_index)
    {
        this.page_index = page_index;
    }

    public int getPage_no()
    {
        return page_no;
    }

    public void setPage_no(int page_no)
    {
        this.page_no = page_no;
    }

    public int getPage_size()
    {
        return page_size;
    }

    public void setPage_size(int page_size)
    {
        this.page_size = page_size;
    }

    public int getStart_count()
    {
        return start_count;
    }

    public void setStart_count(int start_count)
    {
        this.start_count = start_count;
    }

    public int getTotal_count()
    {
        return total_count;
    }

    public void setTotal_count(int total_count)
    {
        this.total_count = total_count;
    }
}
