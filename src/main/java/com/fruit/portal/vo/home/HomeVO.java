/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.vo.home;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-06-01
 * Project        : fruit
 * File Name      : HomeVO.java
 */
public class HomeVO implements Serializable
{
    private UserInfoVO user = new UserInfoVO();

    private List<DivVO> slider;

    private List<ClickItemVO> notice;

    private List<ClickItemVO> news;

    public UserInfoVO getUser()
    {
        return user;
    }

    public void setUser(UserInfoVO user)
    {
        this.user = user;
    }

    public List<DivVO> getSlider()
    {
        return slider;
    }

    public void setSlider(List<DivVO> slider)
    {
        this.slider = slider;
    }

    public List<ClickItemVO> getNotice()
    {
        return notice;
    }

    public void setNotice(List<ClickItemVO> notice)
    {
        this.notice = notice;
    }

    public List<ClickItemVO> getNews()
    {
        return news;
    }

    public void setNews(List<ClickItemVO> news)
    {
        this.news = news;
    }
}
