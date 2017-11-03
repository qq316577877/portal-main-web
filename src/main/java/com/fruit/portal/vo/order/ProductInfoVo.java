package com.fruit.portal.vo.order;

import java.util.List;

public class ProductInfoVo {

	private int id;

	private String name;

	private String enName;

	private int capacitySize;

	private String unit;

	private int status;

	private List<ProductPropertyVo> productDetails;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public int getCapacitySize() {
		return capacitySize;
	}

	public void setCapacitySize(int capacitySize) {
		this.capacitySize = capacitySize;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<ProductPropertyVo> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductPropertyVo> productDetails) {
		this.productDetails = productDetails;
	}
}
