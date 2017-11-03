package com.fruit.portal.service.order.verify.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fruit.order.biz.dto.ProductDTO;
import com.fruit.order.biz.dto.ProductPropertyDTO;
import com.fruit.order.biz.dto.ProductPropertyValueDTO;
import com.fruit.order.biz.service.ProductPropertyService;
import com.fruit.order.biz.service.ProductPropertyValueService;
import com.fruit.order.biz.service.ProductService;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;
import com.fruit.portal.vo.order.OrderContainer;
import com.fruit.portal.vo.order.OrderContainerDetail;
import com.fruit.portal.vo.order.ProductInfoVo;
import com.fruit.portal.vo.order.ProductPropertyVo;
import com.ovfintech.cache.client.CacheClient;

/**
 * 产品信息校验类
 * 
 * @author ovfintech
 *
 */
public class ProductVerify implements Verify {

	@Resource
	private CacheClient cacheClient;

	@Resource
	private ProductService productService;

	@Resource
	private ProductPropertyService productPropertyService;

	@Resource
	private ProductPropertyValueService productPropertyValueService;

	private static final Logger logger = LoggerFactory.getLogger(ProductVerify.class);

	@Override
	public VerifyResult verify(VerifyInfo verifyInfo) {
		logger.debug("into method verify");
		VerifyResult verifyResult = new VerifyResult();
		if (verifyInfo != null && verifyInfo.getProductInfos() != null) {
			List<OrderContainer> containers = verifyInfo.getProductInfos();

			for (OrderContainer container : containers) {
				List<OrderContainerDetail> orderContainerDetails = container.getOrderContainerDetails();
				// 货柜详情为空
				if (orderContainerDetails == null) {
					verifyResult.setExcuteResult(false);
					verifyResult.setMsg("参数错误");
					verifyResult.setErrorCode(111004);
					logger.debug("out method verify");
					return verifyResult;
				}
				int productId = container.getProductId();
				String result = cacheClient.hget("goods", String.valueOf(productId));
				if (result == null || "".equals(result)) {
					// 缓存为空，直接去数据库查询并校验(这里查出数据后不缓存)
					ProductDTO dto = productService.loadById(productId);
					if (dto == null) {
						verifyResult.setExcuteResult(false);
						verifyResult.setErrorCode(111003);
						verifyResult.setMsg("产品已下架");
						logger.debug("out method verify");
						return verifyResult;
					}
					List<ProductPropertyDTO> productPropertyDtos = productPropertyService.listByProductId(productId);
					Map<String, List<String>> propertyDetailMap = new HashMap<String, List<String>>();
					Set<String> propertySet = new HashSet<String>();
					for (ProductPropertyDTO property : productPropertyDtos) {
						propertySet.add(property.getEngName());
						List<ProductPropertyValueDTO> productPropertyValueDtos = productPropertyValueService
								.listByProductPropertyId(property.getId());
						List<String> propertyList = new ArrayList<String>();
						for (ProductPropertyValueDTO propertyDetail : productPropertyValueDtos) {
							propertyList.add(propertyDetail.getValue());
						}
						propertyDetailMap.put(property.getEngName(), propertyList);
					}
					for (OrderContainerDetail containerDetail : orderContainerDetails) {
						Map<String, String> detailMap = containerDetail.getProductDetail();
						// 校验货柜中的产品属性及属性值
						for (Entry<String, String> entry : detailMap.entrySet()) {
							if (!propertySet.contains(entry.getKey())) {
								verifyResult.setExcuteResult(false);
								verifyResult.setErrorCode(111001);
								verifyResult.setMsg("产品已下架");
								logger.debug("out method verify");
								return verifyResult;
							}
							List<String> properties = propertyDetailMap.get(entry.getKey());
							if (!properties.contains(entry.getValue())) {
								verifyResult.setExcuteResult(false);
								verifyResult.setErrorCode(111002);
								verifyResult.setMsg("产品已下架");
								logger.debug("out method verify");
								return verifyResult;
							}
						}
					}
				} else {
					ProductInfoVo p = JSON.parseObject(result, ProductInfoVo.class);
					List<ProductPropertyVo> productPropertyDtos = p.getProductDetails();
					Map<String, List<String>> propertyDetailMap = new HashMap<String, List<String>>();
					Set<String> propertySet = new HashSet<String>();
					for (ProductPropertyVo property : productPropertyDtos) {
						propertySet.add(property.getEngName());
						List<ProductPropertyValueDTO> productPropertyValueDtos = property.getValues();
						List<String> propertyList = new ArrayList<String>();
						for (ProductPropertyValueDTO propertyDetail : productPropertyValueDtos) {
							propertyList.add(propertyDetail.getValue());
						}
						propertyDetailMap.put(property.getEngName(), propertyList);
					}
					for (OrderContainerDetail containerDetail : orderContainerDetails) {
						Map<String, String> detailMap = containerDetail.getProductDetail();
						// 校验货柜中的产品属性及属性值
						for (Entry<String, String> entry : detailMap.entrySet()) {
							if (!propertySet.contains(entry.getKey())) {
								verifyResult.setExcuteResult(false);
								verifyResult.setErrorCode(111001);
								verifyResult.setMsg("产品已下架");
								logger.debug("out method verify");
								return verifyResult;
							}
							List<String> properties = propertyDetailMap.get(entry.getKey());
							if (!properties.contains(entry.getValue())) {
								verifyResult.setExcuteResult(false);
								verifyResult.setErrorCode(111002);
								verifyResult.setMsg("产品已下架");
								logger.debug("out method verify");
								return verifyResult;
							}
						}
					}
				}
			}
		}
		verifyResult.setExcuteResult(true);
		logger.debug("out method verify");
		return verifyResult;
	}

}
