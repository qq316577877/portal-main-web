package com.fruit.portal.vo.order;

import java.util.List;

import com.fruit.base.biz.dto.EnterpriseInfoDTO;

public class LogisticsDetailVo {

	private List<LogisticsDetailBean> logisticsDetails;

	private String orderNo;

	private String ContainerNo;

	private EnterpriseInfoDTO innerExpress;

	private EnterpriseInfoDTO outerExpress;

	public List<LogisticsDetailBean> getLogisticsDetails() {
		return logisticsDetails;
	}

	public void setLogisticsDetails(List<LogisticsDetailBean> logisticsDetails) {
		this.logisticsDetails = logisticsDetails;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getContainerNo() {
		return ContainerNo;
	}

	public void setContainerNo(String containerNo) {
		ContainerNo = containerNo;
	}

	public EnterpriseInfoDTO getInnerExpress() {
		return innerExpress;
	}

	public void setInnerExpress(EnterpriseInfoDTO innerExpress) {
		this.innerExpress = innerExpress;
	}

	public EnterpriseInfoDTO getOuterExpress() {
		return outerExpress;
	}

	public void setOuterExpress(EnterpriseInfoDTO outerExpress) {
		this.outerExpress = outerExpress;
	}
}
