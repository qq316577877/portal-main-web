/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.vo.dashboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : fruit
 * File Name      : UserNavGroupVO.java
 */
public class UserNavGroupVO
{
    private String name;

    private boolean enable;

    private List<UserNavItemVO> items = new ArrayList<UserNavItemVO>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isEnable()
    {
        return enable;
    }

    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }

    public List<UserNavItemVO> getItems()
    {
        return items;
    }

    public void setItems(List<UserNavItemVO> items)
    {
        this.items = items;
    }
}
