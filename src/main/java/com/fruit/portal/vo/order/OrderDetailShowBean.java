package com.fruit.portal.vo.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailShowBean {

	private long id;

	private String orderNo;

	private BigDecimal totalAmount;

	private BigDecimal productAmount;

	private BigDecimal agencyAmount;

	private BigDecimal premiumAmount;

	private BigDecimal finalAmount;

	private BigDecimal advance;

	private BigDecimal restPay;

	private int orderStatus;

	private String orderStatusDesc;

	private List<OrderContainer> orderContainers;

	private long logisticsId;

	private int orderType;

	private int logisticsType;

	private int tradeType;

	private int preClearance;

	private int clearance;

	private EnterpriseInfoVo clearanceCompany;

	private int insurance;

	private EnterpriseInfoVo innerExpress;

	private EnterpriseInfoVo outerExpress;

	private DeliveryInfoVo deliveryAddress;

	private String contractUrl;

	private String voucherUrl;

	private int payType;

	private Integer needLoan;

	private UserSupplierVo supplier;

	private String placeOrderTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLogisticsId() {
		return logisticsId;
	}

	public void setLogisticsId(long logisticsId) {
		this.logisticsId = logisticsId;
	}

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

	public int getInsurance() {
		return insurance;
	}

	public void setInsurance(int insurance) {
		this.insurance = insurance;
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

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public Integer getNeedLoan() {
		return needLoan;
	}

	public void setNeedLoan(Integer needLoan) {
		this.needLoan = needLoan;
	}

	public List<OrderContainer> getOrderContainers() {
		return orderContainers;
	}

	public void setOrderContainers(List<OrderContainer> orderContainers) {
		this.orderContainers = orderContainers;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public EnterpriseInfoVo getClearanceCompany() {
		return clearanceCompany;
	}

	public void setClearanceCompany(EnterpriseInfoVo clearanceCompany) {
		this.clearanceCompany = clearanceCompany;
	}

	public EnterpriseInfoVo getInnerExpress() {
		return innerExpress;
	}

	public void setInnerExpress(EnterpriseInfoVo innerExpress) {
		this.innerExpress = innerExpress;
	}

	public EnterpriseInfoVo getOuterExpress() {
		return outerExpress;
	}

	public void setOuterExpress(EnterpriseInfoVo outerExpress) {
		this.outerExpress = outerExpress;
	}

	public DeliveryInfoVo getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryInfoVo deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public UserSupplierVo getSupplier() {
		return supplier;
	}

	public void setSupplier(UserSupplierVo supplier) {
		this.supplier = supplier;
	}

	public String getPlaceOrderTime() {
		return placeOrderTime;
	}

	public void setPlaceOrderTime(String placeOrderTime) {
		this.placeOrderTime = placeOrderTime;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount) {
		this.productAmount = productAmount;
	}

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public int getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(int logisticsType) {
		this.logisticsType = logisticsType;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getAdvance() {
		return advance;
	}

	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}

	public BigDecimal getRestPay() {
		return restPay;
	}

	public void setRestPay(BigDecimal restPay) {
		this.restPay = restPay;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
}
