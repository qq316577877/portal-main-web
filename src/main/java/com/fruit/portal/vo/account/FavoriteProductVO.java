/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.account;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-25
 * Project        : message-biz
 * File Name      : FavoriteProductModel.java
 */
public class FavoriteProductVO implements Serializable
{
    private static final long serialVersionUID = 2745776796130034985L;

    private int id;

    private int product_id;

    private String code;

    private int brand_id;

    private String brand;

    private String description;

    private BigDecimal price;

    private int offer_num;

    private String pic_url;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getProduct_id()
    {
        return product_id;
    }

    public void setProduct_id(int product_id)
    {
        this.product_id = product_id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getBrand_id()
    {
        return brand_id;
    }

    public void setBrand_id(int brand_id)
    {
        this.brand_id = brand_id;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public int getOffer_num()
    {
        return offer_num;
    }

    public void setOffer_num(int offer_num)
    {
        this.offer_num = offer_num;
    }

    public String getPic_url()
    {
        return pic_url;
    }

    public void setPic_url(String pic_url)
    {
        this.pic_url = pic_url;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof FavoriteProductVO))
        {
            return false;
        }
        FavoriteProductVO that = (FavoriteProductVO) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode()
    {
        return getId();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("FavoriteProductVO{");
        sb.append("id=").append(id);
        sb.append(", product_id=").append(product_id);
        sb.append(", code='").append(code).append('\'');
        sb.append(", brand_id=").append(brand_id);
        sb.append(", brand='").append(brand).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", offer_num=").append(offer_num);
        sb.append(", pic_url='").append(pic_url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
