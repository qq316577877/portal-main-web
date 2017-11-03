/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.loan;


import com.fruit.loan.biz.https.util.UUIDUtils;
import com.ovft.fss.api.bean.UploadRequest;
import com.ovft.fss.api.bean.UploadResponse;
import com.ovft.fss.api.service.UploadService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Description:
 * 用户资金服务电子合同相关
 * Create Author  : paul
 * Create Date    : 2017-08-11
 * Project        : partal-main-web
 * File Name      : LoanContractService.java
 */
@Service
public class LoanContractService
{

    @Autowired
    private UploadService uploadService;

    /**
     * 调用电子合同服务中的上传图片服务
     * @param open
     * @param item
     * @return
     */
    public UploadResponse doContractUpload(boolean open, FileItem item)
    {
        String filename = item.getName();
        String extension = FilenameUtils.getExtension(filename);
        Validate.isTrue(StringUtils.isNotBlank(extension), "文件名错误!");
        byte[] contents = item.get();
        String newFileName = UUIDUtils.getUUID()+"."+extension;

        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setContents(contents);
        uploadRequest.setOpen(open);
        uploadRequest.setAppname("www.fruit.com");
        uploadRequest.setFilename(newFileName);
        uploadRequest.setViewInPage(true);

        UploadResponse response = uploadService.upload(uploadRequest);
        return response;
    }



}
