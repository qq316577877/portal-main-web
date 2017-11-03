/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.vo.account;

import java.io.Serializable;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-29
 * Project        : message-biz
 * File Name      : InvoiceVO.java
 */
public class InvoiceVO implements Serializable
{
    private static final long serialVersionUID = 1062307295408508512L;

    private int id;

    private String title;

    private int type;

    private String type_desc;

    private String tax_code;

    private String bank;

    private String bank_account;

    private String register_address;

    private String register_phone;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getType_desc()
    {
        return type_desc;
    }

    public void setType_desc(String type_desc)
    {
        this.type_desc = type_desc;
    }

    public String getTax_code()
    {
        return tax_code;
    }

    public void setTax_code(String tax_code)
    {
        this.tax_code = tax_code;
    }

    public String getBank()
    {
        return bank;
    }

    public void setBank(String bank)
    {
        this.bank = bank;
    }

    public String getBank_account()
    {
        return bank_account;
    }

    public void setBank_account(String bank_account)
    {
        this.bank_account = bank_account;
    }

    public String getRegister_address()
    {
        return register_address;
    }

    public void setRegister_address(String register_address)
    {
        this.register_address = register_address;
    }

    public String getRegister_phone()
    {
        return register_phone;
    }

    public void setRegister_phone(String register_phone)
    {
        this.register_phone = register_phone;
    }
}
