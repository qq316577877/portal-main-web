package com.fruit.portal.service.order;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.ContainerDetailDTO;
import com.fruit.order.biz.dto.ContainerDetailTmpDTO;
import com.fruit.order.biz.dto.ContainerTmpDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerDetailService;
import com.fruit.order.biz.service.ContainerDetailTmpService;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.portal.vo.order.OrderProcessRequest;

@Service
public class ConfirmSubmitProcessor extends DefaultOrderProcessor {

	@Autowired
	private ContainerService containerService;

	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerDetailTmpService containerDetailTmpService;

	@Autowired
	private ContainerDetailService containerDetailService;

	public ConfirmSubmitProcessor() {
		super(OrderEventEnum.CONFIRM);
	}

	@Override
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {
		// 复制货柜信息到正式表
		List<ContainerTmpDTO> oldOrderContainers = containerTmpService.listByOrderNo(orderDTO.getNo());
		if (oldOrderContainers != null && oldOrderContainers.size() != 0) {
			for (ContainerTmpDTO ctdto : oldOrderContainers) {
				ContainerDTO cdto = new ContainerDTO();
				BeanUtils.copyProperties(ctdto, cdto);
				cdto.setStatus(ContainerStatusEnum.SUBMITED.getStatus());
				containerService.create(cdto);
				ctdto.setStatus(ContainerStatusEnum.SUBMITED.getStatus());
				containerTmpService.updateById(ctdto);
				List<ContainerDetailTmpDTO> oldOrderContainerDetails = containerDetailTmpService
						.listByContainerNo(ctdto.getNo());
				if (oldOrderContainerDetails != null && oldOrderContainerDetails.size() != 0) {
					for (ContainerDetailTmpDTO cdtdto : oldOrderContainerDetails) {
						ContainerDetailDTO cddto = new ContainerDetailDTO();
						BeanUtils.copyProperties(cdtdto, cddto);
						cddto.setStatus(ContainerStatusEnum.SUBMITED.getStatus());
						containerDetailService.create(cddto);
						cdtdto.setStatus(ContainerStatusEnum.SUBMITED.getStatus());
						containerDetailTmpService.updateById(cdtdto);
					}
				}
			}
		}
	}
}
