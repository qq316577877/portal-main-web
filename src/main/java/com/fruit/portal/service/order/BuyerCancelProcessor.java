package com.fruit.portal.service.order;

import java.util.List;

import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import org.apache.commons.collections.CollectionUtils;
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
public class BuyerCancelProcessor extends DefaultOrderProcessor {

	@Autowired
	private ContainerService containerService;

	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerDetailTmpService containerDetailTmpService;

	@Autowired
	private ContainerDetailService containerDetailService;

	@Autowired
	private LoanInfoService loanInfoService;

	@Override
	protected void setExtraOrderInfo(OrderDTO orderDTO) {

	}

	public BuyerCancelProcessor() {
		super(OrderEventEnum.USER_CANCEL);
	}

	@Override
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {

		//先判断有无贷款，如果有先取消贷款
		List<LoanInfoDTO> loanInfos = loanInfoService.listByOrderNo(orderDTO.getNo());
		if(CollectionUtils.isNotEmpty(loanInfos)){
			loanInfoService.deleteByOrderNo(orderDTO.getUserId(),orderDTO.getNo());
		}

		// 取消订单同时要更新货柜状态
		if(isQueryTmp(status)){
			List<ContainerTmpDTO> orderContainers = containerTmpService.listByOrderNo(orderDTO.getNo());
			if (orderContainers != null && orderContainers.size() != 0) {
				for (ContainerTmpDTO ctdto : orderContainers) {
					ctdto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
					containerTmpService.updateById(ctdto);
					List<ContainerDetailTmpDTO> orderContainerDetails = containerDetailTmpService
							.listByContainerNo(ctdto.getNo());
					if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
						for (ContainerDetailTmpDTO cdtdto : orderContainerDetails) {
							cdtdto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
							containerDetailTmpService.updateById(cdtdto);
						}
					}
				}
			}
		}else{
			List<ContainerDTO> containers = containerService.listByOrderNo(orderDTO.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					dto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
					containerService.updateById(dto);
					List<ContainerDetailDTO> orderContainerDetails = containerDetailService
							.listByContainerNo(dto.getNo());
					if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
						for (ContainerDetailDTO cdtdto : orderContainerDetails) {
							cdtdto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
							containerDetailService.updateById(cdtdto);
						}
					}
				}
			}
		}
	}
	
}
