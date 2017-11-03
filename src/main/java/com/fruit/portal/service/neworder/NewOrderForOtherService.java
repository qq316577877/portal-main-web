package com.fruit.portal.service.neworder;

import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.portal.vo.order.ContainerInfoForLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewOrderForOtherService {


	@Autowired
	private ContainerInfoService containerNewInfoService;


	/**
	 * 新订单获取给信贷的信息---货柜信息
	 * @param transactionNo
	 * @return
     */
	public ContainerInfoForLoan handllerByNewContainer(String transactionNo){
		ContainerInfoForLoan result = new ContainerInfoForLoan();
		ContainerInfoDTO containerNew = containerNewInfoService.loadByContainerSerialNo(transactionNo);

		result.setNo(containerNew.getContainerNo());
		result.setOrderNo(containerNew.getOrderNo());
		result.setTransactionNo(containerNew.getContainerSerialNo());
		result.setProductName(containerNew.getContainerName());
		result.setDeliveryTime(containerNew.getDeliveryTime());
		result.setPreReceiveTime(containerNew.getPreReceiveTime());
		result.setStatus(containerNew.getStatus());
		result.setProductAmount(containerNew.getProductAmount());
		return result;
	}
}
