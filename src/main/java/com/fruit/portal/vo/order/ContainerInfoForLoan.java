package com.fruit.portal.vo.order;

import java.math.BigDecimal;
import java.util.Date;

public class ContainerInfoForLoan {

	private String no;

	private String orderNo;

	private String transactionNo;

	private int productId;

	private String productName;

	private BigDecimal productAmount;
	
	private Date deliveryTime;

    private Date preReceiveTime;

	private int status;

	private String editor;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getPreReceiveTime() {
		return preReceiveTime;
	}

	public void setPreReceiveTime(Date preReceiveTime) {
		this.preReceiveTime = preReceiveTime;
	}

	public BigDecimal getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount) {
		this.productAmount = productAmount;
	}
}
