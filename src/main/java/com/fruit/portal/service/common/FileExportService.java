/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.common;

import com.fruit.base.biz.common.FileExportStatusEnum;
import com.fruit.base.biz.dto.FileExportRecordDTO;
import com.fruit.base.biz.service.FileExportReportService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : points-biz-api
 * File Name      : FileExportRecordService.java
 */
@Service
public class FileExportService
{
    @Autowired
    private FileExportReportService fileExportReportService;

    public FileExportRecordDTO createRecord(int userId, int bizType, String operator, String description, String platform, String userIp)
    {
        Validate.isTrue(userId > 0);
        FileExportRecordDTO recordDTO = new FileExportRecordDTO();
        recordDTO.setUserId(userId);
        recordDTO.setBizType(bizType);
        recordDTO.setOperator(operator);
        recordDTO.setDescription(description);
        recordDTO.setPlatform(platform);
        recordDTO.setUserIp(userIp);
        recordDTO.setStatus(FileExportStatusEnum.PROCESSING.getStatus());
        fileExportReportService.create(recordDTO);
        return recordDTO;
    }

    public FileExportRecordDTO loadLastByUserIdAndType(int userId, int bizType)
    {
        return fileExportReportService.loadLastByUserIdAndType(userId, bizType);
    }

    public FileExportRecordDTO loadById(int id)
    {
        return fileExportReportService.loadById(id);
    }

    public void updateRecord(int recordId, int status, String filePath, String error)
    {
        FileExportRecordDTO recordDTO = fileExportReportService.loadById(recordId);
        Validate.notNull(recordDTO, "查无记录");
        recordDTO.setStatus(status);
        recordDTO.setFilePath(filePath);
        recordDTO.setFailureMsg(error);
        fileExportReportService.update(recordDTO);
    }

}
