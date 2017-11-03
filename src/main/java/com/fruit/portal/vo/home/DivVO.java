/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.vo.home;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1512-12
 * Project        : fruit
 * File Name      : DivVO.java
 */
public class DivVO
{
    private String imgUrl;

    private String link;

    private String title;

    public DivVO()
    {

    }

    public DivVO(String imgUrl, String link, String title)
    {
        this.imgUrl = imgUrl;
        this.link = link;
        this.title = title;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
