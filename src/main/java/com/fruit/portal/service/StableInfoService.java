/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.portal.service;

import com.alibaba.fastjson.JSON;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.utils.BizConstants;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: 常用的信息获取服务，带内存级别缓存，适用于不经常变化的信息，比如用户信息等
 * <p/>
 * Create Author : terry
 * Create Date : 2017-05-15
 * Project : fruit
 * File Name : StableInfoService.java
 */
@Service
public class StableInfoService {

	@Resource
	protected CacheClient cacheClient;

	@Autowired
	private MemberService memberService;

	private static String MEMBER_INFO_PREFIX = "MIF-";

	private static String MEMBER_SESSION_TIME_PREFIX = "MST-";

	public UserModel getUserModel(int userId){
		String key = MEMBER_INFO_PREFIX + String.valueOf(userId);
		String result = this.cacheClient.get(key);
		if(result == null){
			UserModel userModel = this.memberService.loadUserModel(userId);
			if(userModel != null){
                this.cacheClient.set(key, JSON.toJSONString(userModel));
                this.cacheClient.expire(key, BizConstants.COOKIE_SITE_TYPE_MAX_AGE);
            }else{
                this.cacheClient.set(key, "NULL");
			}
			return userModel;
		}
		if("NULL".equals(result)){
			return null;
		}
		return JSON.parseObject(result,UserModel.class);
	}

	public void removeUserModel(int userId) {
        String key = MEMBER_INFO_PREFIX + String.valueOf(userId);
        this.cacheClient.del(key);
    }

	public String getUserEnpteriseName(int userId) {
		UserModel userModel = this.getUserModel(userId);
		return userModel != null ? userModel.getEnterpriseName() : "";
	}

	public void setLastSessionTime(int userId, long sessionTime)
	{
		String key = MEMBER_SESSION_TIME_PREFIX + String.valueOf(userId);
		this.cacheClient.set(key, Long.toString(sessionTime));
		this.cacheClient.expire(key, BizConstants.COOKIE_SITE_TYPE_MAX_AGE);
	}

	public long getLastSessionTime(int userId)
	{
		String key = MEMBER_SESSION_TIME_PREFIX + String.valueOf(userId);
		String str = this.cacheClient.get(key);
		return NumberUtils.toLong(str, 0);
	}
}
