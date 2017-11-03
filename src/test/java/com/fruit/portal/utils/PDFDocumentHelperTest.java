/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.utils;

import com.ovfintech.arch.pdf.client.itext.PDFDocumentHelper;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;

public class PDFDocumentHelperTest
{

    @Test
    public void testGenerate() throws Exception
    {
        PDFDocumentHelper.generate(new File("/Users/xiaopengli/work/git/ku/arch/arch-pdf-client/src/test/resources/html/image.html"),
                "/data/appdatas/pdf/testGenerate.pdf");
    }

    @Test
    public void testGenerate1() throws Exception
    {
        File sourceFile = new File("/Users/xiaopengli/work/git/ku/portal/portal-main-web/src/main/resources/config/contract/contract.html");
        PDFDocumentHelper.generate(FileUtils.readFileToString(sourceFile),
                "/data/appdatas/pdf/contract.pdf");
    }

}