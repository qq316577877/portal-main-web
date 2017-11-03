package com.fruit.portal.vo.order;

import java.math.BigDecimal;

public class ApplyLoanInfoVo {

	private String transactionNo;

	private String productName;

	private Integer productId;

	private BigDecimal loanQuota;

	private BigDecimal applyQuota;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public BigDecimal getLoanQuota() {
		return loanQuota;
	}

	public void setLoanQuota(BigDecimal loanQuota) {
		this.loanQuota = loanQuota;
	}

	public BigDecimal getApplyQuota() {
		return applyQuota;
	}

	public void setApplyQuota(BigDecimal applyQuota) {
		this.applyQuota = applyQuota;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
}
