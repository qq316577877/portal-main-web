/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.common;

import com.fruit.message.biz.common.MessageTypeEnum;
import com.fruit.message.biz.utils.RedBankClient;
import com.fruit.portal.model.ContextObject;
import com.fruit.portal.service.BaseService;
import com.fruit.portal.service.ContextManger;
import com.fruit.portal.utils.BizConstants;
import com.ovfintech.arch.common.event.EventChannel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.useragent.UserAgent;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : points-biz-api
 * File Name      : RedBankService.java
 */
@Service
public class RedBankService extends BaseService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedBankService.class);

    private static final String KEY_MC_SERVER_IP = "mc.server.ip";

    private static final String KEY_PRODUCER_ID = "mc.producer.id";

    private static final String KEY_PRODUCER_PASSWORD = "mc.producer.password";


    @Autowired
    private EnvService envService;

    private RedBankClient redBankClient;

    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;

    @Autowired(required = false)
    private List<EventPublisher> eventPublishers;

    @PostConstruct
    protected void init()
    {
        if (null == redBankClient && null != envService)
        {
            String serverIp = envService.getConfig(KEY_MC_SERVER_IP);
            String producerIdStr = envService.getConfig(KEY_PRODUCER_ID);
            String password = envService.getConfig(KEY_PRODUCER_PASSWORD);
            Validate.notNull(producerIdStr, "初始化MC账号信息失败");
            Validate.notNull(password, "初始化MC账号信息失败");
            Validate.notNull(serverIp, "初始化MC账号信息失败");
            int producerId = NumberUtils.toInt(producerIdStr, 0);
            Validate.isTrue(producerId > 0, "初始化MC账号信息失败");
            redBankClient = new RedBankClient(serverIp, producerId, password, false);
        }
    }

    /**
     * 异步发送消息
     *
     * @param bizId
     * @param type
     * @param extraData
     * @param userId
     */
    public void asyncFireEvent(final long bizId, final MessageTypeEnum type, final Map<String, String> extraData, final int userId)
    {
//        this.asyncFireEvent(bizId, type.getKey(), extraData, userId);
    }

    /**
     * 异步发送消息
     *
     * @param bizId
     * @param type
     * @param extraData
     * @param userId
     */
    public void asyncFireEvent(final long bizId, final String type, final Map<String, String> extraData, final int userId)
    {
        Validate.notNull(type, "参数错误");
//        this.taskEventChannel.publish(new TaskEvent(new ITask()
//        {
//            @Override
//            public void doTask()
//            {
//                try
//                {
//                    redBankClient.fire(Long.toString(bizId), type, extraData, Integer.toString(userId)) ;
//                }
//                catch (Exception e)
//                {
//                    LOGGER.error("Fire message error.", e);
//                    EventHelper.triggerEvent(RedBankService.this.eventPublishers, "RedBankService." + e.getMessage(),
//                            "error when send message with server : " + redBankClient.getServerUrl(), EventLevel.IMPORTANT, e,
//                            ServerIpUtils.getServerIp());
//                }
//            }
//        }));
    }

    /**
     * 提前扩展参数
     *
     * @return
     */
    private Map<String, String> extractExtraData()
    {
        Map<String, String> extraData = new HashMap<String, String>();
        ContextObject context = ContextManger.getContext();
        if (null != context)
        {
            UserAgent userAgentModel = context.getUserAgentModel();
            if (null != userAgentModel && null != userAgentModel.getOperatingSystem())
            {
                extraData.put(BizConstants.EXTRA_DATA_KEY_PLATFORM, userAgentModel.getOperatingSystem().name());
            }
            extraData.put(BizConstants.EXTRA_DATA_KEY_USER_IP, context.getUserIp());
        }
        return extraData;
    }

}
