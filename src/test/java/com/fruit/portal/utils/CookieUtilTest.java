/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.utils;

import com.fruit.portal.model.account.UserLoginInfo;
import org.testng.annotations.Test;

public class CookieUtilTest
{

    @Test
    public void testGeneratePassportByUserInfo() throws Exception
    {
        UserLoginInfo userInfo = new UserLoginInfo(1, "123456".hashCode());
        String kuid = CookieUtil.generatePassportByUserInfo(userInfo);
        System.out.println(kuid);
    }

    @Test
    public void testGetIdFromPassport() throws Exception
    {

    }
}