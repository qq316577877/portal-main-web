/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.common;

import java.io.Serializable;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : GraphicCaptchaVO.java
 */
public class GraphicCaptchaVO implements Serializable
{

    private static final long serialVersionUID = 2882031293877850498L;

    private int id;

    private String captcha;

    public GraphicCaptchaVO()
    {
    }

    public GraphicCaptchaVO(int id, String captcha)
    {
        this.id = id;
        this.captcha = captcha;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCaptcha()
    {
        return captcha;
    }

    public void setCaptcha(String captcha)
    {
        this.captcha = captcha;
    }

}
