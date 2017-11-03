package com.fruit.portal.vo.order;

import java.util.List;

public class OrderLogistics {

	private String orderNo;

	private int userId;

	private int logisticsType;

	private int tradeType;

	private int preClearance;

	private int clearance;

	private int clearanceCompanyId;

	private int insurance;

	private int innerExpressId;

	private int outerExpressId;

	private String contractUrl;

	private String voucherUrl;

	private int payType;

	private int status;

	private Integer needLoan;

	private List<ApplyLoanInfoVo> loadInfo;

	private Integer deliveryId;

	public int getTradeType() {
		return tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

	public int getPreClearance() {
		return preClearance;
	}

	public void setPreClearance(int preClearance) {
		this.preClearance = preClearance;
	}

	public int getClearance() {
		return clearance;
	}

	public void setClearance(int clearance) {
		this.clearance = clearance;
	}

	public int getClearanceCompanyId() {
		return clearanceCompanyId;
	}

	public void setClearanceCompanyId(int clearanceCompanyId) {
		this.clearanceCompanyId = clearanceCompanyId;
	}

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
	}

	public int getInnerExpressId() {
		return innerExpressId;
	}

	public void setInnerExpressId(int innerExpressId) {
		this.innerExpressId = innerExpressId;
	}

	public int getOuterExpressId() {
		return outerExpressId;
	}

	public void setOuterExpressId(int outerExpressId) {
		this.outerExpressId = outerExpressId;
	}

	public String getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}

	public String getVoucherUrl() {
		return voucherUrl;
	}

	public void setVoucherUrl(String voucherUrl) {
		this.voucherUrl = voucherUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getNeedLoan() {
		return needLoan;
	}

	public void setNeedLoan(Integer needLoan) {
		this.needLoan = needLoan;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public List<ApplyLoanInfoVo> getLoadInfo() {
		return loadInfo;
	}

	public void setLoadInfo(List<ApplyLoanInfoVo> loadInfo) {
		this.loadInfo = loadInfo;
	}

	public int getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(int logisticsType) {
		this.logisticsType = logisticsType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
