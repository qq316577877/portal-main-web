package com.fruit.portal.service.order;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.vo.order.OrderProcessRequest;
import com.fruit.portal.vo.order.OrderProcessResponse;
import com.ovfintech.cache.client.CacheClient;

@Service
public class OrderProcessService {

	@Autowired
	private OrderProcessFactory orderProcessFactory;

	@Resource
	private CacheClient cacheClient;

	/**
	 * 操作订单
	 * 
	 * @return
	 */
	public AjaxResult<Map<String, Object>> operateOrder(int userId, String orderId, OrderEventEnum event,
			String domain, int operatorType) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		boolean successful = false;
		int code = AjaxResultCode.OK.getCode();
		try {
			// 使用分布式锁防止重复提交
			long count = cacheClient.setnx("USER_" + userId, String.valueOf(userId));
			if(count==0){
				code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
				return new AjaxResult<Map<String, Object>>(code, "不能重复提交", null);
			}

			cacheClient.expire("USER_" + userId, 3);
			OrderProcessRequest request = new OrderProcessRequest();
			request.setOrderId(orderId);
			request.setUserId(userId);
			request.setEvent(event);
			request.setOperatorType(operatorType);
			OrderProcessResponse response = orderProcessFactory.process(request);
			successful = response.isSuccessful();
			String msg = response.getMessage();
			String url = domain + "order/detail/id=" + orderId;
			dataMap.put("url", url);
			dataMap.put("message", msg);
			dataMap.put("successful", successful ? 1 : 0);
			return new AjaxResult<Map<String, Object>>(code, msg, dataMap);

		} catch (IllegalArgumentException e) {
			code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
			String msg = e.getMessage();
			dataMap.put("successful", successful ? 1 : 0);
			return new AjaxResult<Map<String, Object>>(code, msg, dataMap);
		} catch (RuntimeException e) {
			code = AjaxResultCode.SERVER_ERROR.getCode();
			String msg = e.getMessage();
			dataMap.put("successful", successful ? 1 : 0);
			return new AjaxResult<Map<String, Object>>(code, msg, dataMap);
		}finally {
			// 释放锁
			cacheClient.del("USER_" + userId);
		}
	}

}
