package com.fruit.portal.service.order;

import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.portal.vo.order.OrderProcessRequest;
import com.fruit.portal.vo.order.OrderProcessResponse;

public interface IOrderProcessor {

	OrderEventEnum getEvent();

    OrderProcessResponse process(OrderProcessRequest request);
}
