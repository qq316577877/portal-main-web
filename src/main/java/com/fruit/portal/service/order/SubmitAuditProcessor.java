package com.fruit.portal.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.dto.ContainerDetailTmpDTO;
import com.fruit.order.biz.dto.ContainerTmpDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerDetailTmpService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.portal.vo.order.OrderProcessRequest;

/*
 * 提交审核
 */
@Service
public class SubmitAuditProcessor extends DefaultOrderProcessor {


	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerDetailTmpService containerDetailTmpService;

	public SubmitAuditProcessor() {
		super(OrderEventEnum.SUBMIT);
	}

	@Override
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {

		// 提交审核订单同时要更新货柜状态
		List<ContainerTmpDTO> orderContainers = containerTmpService.listByOrderNo(orderDTO.getNo());
		if (orderContainers != null && orderContainers.size() != 0) {
			for (ContainerTmpDTO ctdto : orderContainers) {
				ctdto.setStatus(ContainerStatusEnum.AUDIT.getStatus());
				containerTmpService.updateById(ctdto);
				List<ContainerDetailTmpDTO> orderContainerDetails = containerDetailTmpService.listByContainerNo(ctdto
						.getNo());
				if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
					for (ContainerDetailTmpDTO cdtdto : orderContainerDetails) {
						cdtdto.setStatus(ContainerStatusEnum.AUDIT.getStatus());
						containerDetailTmpService.updateById(cdtdto);
					}
				}
			}
		}
	}
}
