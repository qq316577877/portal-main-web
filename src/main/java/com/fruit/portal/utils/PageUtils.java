/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.utils;

import com.fruit.account.biz.common.PageModel;
import com.fruit.portal.vo.PageVO;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : PageUtils.java
 */
public class PageUtils
{
    public static List<Integer> createPageIndex(int pageCount, int pageNo)
    {
        List<Integer> pageIndexs = new LinkedList<Integer>();
        pageIndexs.add(1);
        for (int index = 1; index <= pageCount; index++)
        {
            int step = index - pageNo;
            if (step > -4 && step < 4)
            {
                if (!pageIndexs.contains(index))
                {
                    pageIndexs.add(index);
                }
            }
            else if (step < 0)
            {
                if (!pageIndexs.contains(-1))
                {
                    pageIndexs.add(-1);
                }
            }
            else if (step > 0)
            {
                if (!pageIndexs.contains(0))
                {
                    pageIndexs.add(0);
                }
            }
        }

        if (!pageIndexs.contains(pageCount))
        {
            pageIndexs.add(pageCount);
        }
        return pageIndexs;
    }

    public static PageVO wrapPageVO(PageModel<?> pageModel, int pageNo, int pageSize)
    {
        PageVO pageVO = new PageVO();
        pageVO.setPage_no(pageNo);
        pageVO.setPage_size(pageSize);
        pageVO.setTotal_count(pageModel.getTotalCount());
        int pageCount = (int) (Math.ceil(pageModel.getTotalCount() * 1.0 / pageModel.getPageSize()));
        if (pageCount > 10)
        {
            pageCount = 10;
        }
        pageVO.setPage_count(pageCount);
        int start = pageSize * (pageNo - 1);
        int end = pageSize * pageNo;
        pageVO.setStart_count(start + 1);
        pageVO.setEnd_count(end > pageModel.getTotalCount() ? pageModel.getTotalCount() : end);
        List<Integer> pageIndexs = PageUtils.createPageIndex(pageVO.getPage_count(), pageVO.getPage_no());
        pageVO.setPage_index(pageIndexs);
        return pageVO;
    }
}
