/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.common;

import com.fruit.base.biz.common.SmsStatusEnum;
import com.fruit.base.biz.dto.SmsCaptchaDTO;
import com.fruit.base.biz.service.SmsCaptchaService;
import com.fruit.base.file.upload.aliyun.client.UploadRequest;
import com.fruit.base.file.upload.aliyun.client.UploadResponse;
import com.fruit.portal.model.ContextObject;
import com.fruit.portal.service.BaseService;
import com.fruit.portal.service.ContextManger;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.vo.common.GraphicCaptchaVO;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Description:   图形验证码服务
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-16
 * Project        : fruit
 * File Name      : PortalMessageService.java
 */
@Service
public class GraphicCaptchaService extends BaseService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphicCaptchaService.class);

    private static final String CAPTCHA_IMG_EXTENSION = "png";

    private static final String CAPTCHA_IMG_POSTFIX = ".png";

    /**
     * 图形验证码过期时间(ms),默认5min
     */
    private static final int CAPTCHA_EXPIRATION_TIME = 60 * 1000 * 5;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private FileUploadService fileUploadService;

    public GraphicCaptchaVO create(int userId, String userToken, int type)
    {
        String userTag = userId > 0 ? Integer.toString(userId) : userToken;
        userId = userId > 0 ? userId : 1;

        // 生成验证码
        String capText = captchaProducer.createText();
        byte[] imgContent = this.generateCaptchaImg(capText);
        String filename = userTag + "-" + BizUtils.getUUID() + CAPTCHA_IMG_POSTFIX;
        Validate.notNull(imgContent, "创建图片失败");

        // 上传文件存储服务
        UploadRequest uploadRequest = new UploadRequest();
        uploadRequest.setContents(imgContent);
        uploadRequest.setOpen(true);
        uploadRequest.setUserId(userId);
        uploadRequest.setFileName(filename);
        uploadRequest.setFileExtension(CAPTCHA_IMG_EXTENSION);
        uploadRequest.setViewInPage(true);
        UploadResponse response = this.fileUploadService.uploadCaptchaImg(uploadRequest);
        Validate.isTrue(response.isSuccessful(), response.getErrorMessage());

        SmsCaptchaDTO captchaDTO = saveCaptcha(userId, type, capText);
        return new GraphicCaptchaVO(captchaDTO.getId(), response.getUrl());
    }

    private byte[] generateCaptchaImg(String capText)
    {
        BufferedImage capImage = captchaProducer.createImage(capText);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(150 * 50);
        try
        {
            ImageIO.write(capImage, CAPTCHA_IMG_EXTENSION, baos);
            baos.flush();
            return baos.toByteArray();
        }
        catch (IOException e)
        {
            LOGGER.error("Generate graphic captcha image error.", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(baos);
        }
    }

    private SmsCaptchaDTO saveCaptcha(int userId, int type, String capText)
    {
        SmsCaptchaDTO captchaDTO = new SmsCaptchaDTO();
        captchaDTO.setUserId(userId);
        captchaDTO.setType(type);
        captchaDTO.setCaptcha(capText);
        ContextObject context = ContextManger.getContext();
        if (null != context)
        {
            captchaDTO.setUserIp(context.getUserIp());
        }
        captchaDTO.setStatus(SmsStatusEnum.NOT_VERIFY.getValue());
        this.smsCaptchaService.create(captchaDTO);
        return captchaDTO;
    }

    public void validateAndExpendCaptcha(int id, String captcha)
    {
        Validate.isTrue(id > 0, "验证码不存在");
        Validate.notEmpty(captcha, "验证码不能为空");
        SmsCaptchaDTO captchaDTO = this.smsCaptchaService.loadById(id);
        Validate.notNull(captchaDTO, "验证码不存在");
        Validate.isTrue(captchaDTO.getStatus() == SmsStatusEnum.NOT_VERIFY.getValue(), "验证码已失效");
        Validate.isTrue(StringUtils.equals(captcha, captchaDTO.getCaptcha()), "验证码错误");
        boolean expired = GraphicCaptchaService.isCaptchaExpired(captchaDTO.getAddTime());
        if (expired)
        {
            this.smsCaptchaService.updateStatus(captchaDTO.getId(), SmsStatusEnum.EXPIRED.getValue());
            throw new IllegalArgumentException("验证码已过期!");
        }
        int updated = this.smsCaptchaService.updateStatus(captchaDTO.getId(), SmsStatusEnum.VERIFIED.getValue());
        Validate.isTrue(updated == 1, "验证码错误");
    }

    /**
     * 判断图形验证码是否已过期 (过期时间为<code>CAPTCHA_EXPIRATION_TIME</code>所定义的时间，默认5分钟)
     *
     * @param addTime
     * @return 验证码已过期则返回true，否则返回false
     */
    public static boolean isCaptchaExpired(Date addTime)
    {
        if (null != addTime && (addTime.getTime() + CAPTCHA_EXPIRATION_TIME) < System.currentTimeMillis())
        {
            return true;
        }
        return false;
    }

}
