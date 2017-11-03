package com.fruit.portal.service.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.*;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.portal.service.neworder.NewOrderForOtherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fruit.account.biz.service.UserDeliveryAddressService;
import com.fruit.account.biz.service.UserProfileService;
import com.fruit.account.biz.service.sys.SysUserProductLoanService;
import com.fruit.base.biz.common.BizFileEnum;
import com.fruit.base.biz.dto.BizFileDTO;
import com.fruit.base.biz.dto.EnterpriseInfoDTO;
import com.fruit.base.biz.service.BizFileService;
import com.fruit.base.biz.service.EnterpriseInfoService;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.common.OrderUpdateTypeEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.ContainerDetailDTO;
import com.fruit.order.biz.dto.ContainerDetailTmpDTO;
import com.fruit.order.biz.dto.ContainerTmpDTO;
import com.fruit.order.biz.dto.DeliveryInfoDTO;
import com.fruit.order.biz.dto.LogisticsDTO;
import com.fruit.order.biz.dto.LogisticsDetailDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.dto.ProductDTO;
import com.fruit.order.biz.dto.ProductPropertyDTO;
import com.fruit.order.biz.dto.ProductPropertyValueDTO;
import com.fruit.order.biz.request.OrderSearchRequest;
import com.fruit.order.biz.service.ContainerDetailService;
import com.fruit.order.biz.service.ContainerDetailTmpService;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.order.biz.service.DeliveryInfoService;
import com.fruit.order.biz.service.LogisticsDetailService;
import com.fruit.order.biz.service.LogisticsService;
import com.fruit.order.biz.service.OrderService;
import com.fruit.order.biz.service.ProductPropertyService;
import com.fruit.order.biz.service.ProductPropertyValueService;
import com.fruit.order.biz.service.ProductService;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.loan.LoanInfoModel;
import com.fruit.portal.service.StableInfoService;
import com.fruit.portal.service.account.SupplierService;
import com.fruit.portal.service.common.FileUploadService;
import com.fruit.portal.service.common.UrlConfigService;
import com.fruit.portal.service.loan.LoanInformationService;
import com.fruit.portal.service.order.verify.BaseVerifyEnum;
import com.fruit.portal.service.order.verify.OrderVerifyEnum;
import com.fruit.portal.service.order.verify.Verify;
import com.fruit.portal.service.order.verify.VerifyInfo;
import com.fruit.portal.service.order.verify.VerifyResult;
import com.fruit.portal.service.trade.OrderTaskHelper;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.vo.PageResult;
import com.fruit.portal.vo.order.ApplyLoanInfoVo;
import com.fruit.portal.vo.order.BizFileVo;
import com.fruit.portal.vo.order.ContainerDetailVo;
import com.fruit.portal.vo.order.ContainerInfoForLoan;
import com.fruit.portal.vo.order.ContainerLoanVo;
import com.fruit.portal.vo.order.DeliveryInfoVo;
import com.fruit.portal.vo.order.EnterpriseInfoVo;
import com.fruit.portal.vo.order.LogisticsDetailBean;
import com.fruit.portal.vo.order.LogisticsDetailVo;
import com.fruit.portal.vo.order.OrderBasicInfo;
import com.fruit.portal.vo.order.OrderContainer;
import com.fruit.portal.vo.order.OrderContainerDetail;
import com.fruit.portal.vo.order.OrderDetailShowBean;
import com.fruit.portal.vo.order.OrderDetailVo;
import com.fruit.portal.vo.order.OrderLogistics;
import com.fruit.portal.vo.order.OrderVo;
import com.fruit.portal.vo.order.ProductInfoVo;
import com.fruit.portal.vo.order.ProductPropertyVo;
import com.fruit.portal.vo.order.UserSupplierVo;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import com.ovfintech.cache.client.CacheClient;

@Service
public class OrderGeneralService {

	@Resource
	private CacheClient cacheClient;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductPropertyService productPropertyService;

	@Autowired
	private ProductPropertyValueService productPropertyValueService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ContainerService containerService;

	@Autowired
	private ContainerDetailService containerDetailService;

	@Autowired
	private LogisticsService logisticsService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private UserDeliveryAddressService userDeliveryAddressService;

	@Autowired
	private DeliveryInfoService deliveryInfoService;

	@Autowired
	private EnterpriseInfoService enterpriseInfoService;

	@Autowired
	private LogisticsDetailService logisticsDetailService;

	@Autowired
	private OrderTaskHelper orderTaskHelper;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired(required = false)
	protected List<EventPublisher> eventPublishers;

	@Autowired
	private BizFileService bizFileService;

	@Autowired
	private UrlConfigService urlConfigService;

	@Autowired
	private MetadataProvider metadataProvider;

	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerDetailTmpService containerDetailTmpService;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private LoanInformationService loanInformationService;

	@Autowired
	private SysUserProductLoanService sysUserProductLoanService;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private StableInfoService stableInfoService;

	@Autowired
	private UserEnterpriseService userEnterpriseService;

	@Autowired
	private NewOrderForOtherService newOrderForOtherService;

	@Resource
	private Verify baseVerify;

	private static final int REDIS_TIME_OUT = 3;

	private static final Logger logger = LoggerFactory.getLogger(OrderGeneralService.class);

	/**
	 * 查询所有的商品信息
	 * 
	 * @return
	 */
	public Map<String, ProductInfoVo> findAllGoods() {
		// 先去查缓存，为空再去load DB
		Map<String, String> goodsMap = cacheClient.hgetAll("goods");
		Map<String, ProductInfoVo> products = new HashMap<String, ProductInfoVo>();
		if (goodsMap == null || goodsMap.isEmpty()) {
			// 查询商品基本信息
			List<ProductDTO> dtos = productService.listAll();
			if (dtos != null && dtos.size() > 0) {
				for (ProductDTO dto : dtos) {
					ProductInfoVo p = new ProductInfoVo();
					BeanUtils.copyProperties(dto, p);
					// 同时加载商品属性信息
					List<ProductPropertyVo> productDetails = new ArrayList<ProductPropertyVo>();
					Integer productId = p.getId();
					List<ProductPropertyDTO> productPropertyDtos = productPropertyService.listByProductId(productId);
					for (ProductPropertyDTO productPropertyDto : productPropertyDtos) {
						List<ProductPropertyValueDTO> productPropertyValueDtos = productPropertyValueService
								.listByProductPropertyId(productPropertyDto.getId());
						ProductPropertyVo vo = new ProductPropertyVo();
						vo.setEngName(productPropertyDto.getEngName());
						vo.setName(productPropertyDto.getName());
						vo.setValues(productPropertyValueDtos);
						productDetails.add(vo);
					}
					p.setProductDetails(productDetails);
					products.put(String.valueOf(p.getId()), p);
					cacheClient.hset("goods", String.valueOf(p.getId()), JSON.toJSONString(p));
					cacheClient.expire("goods", 1800);
				}
			}
			return products;
		}
		for (Map.Entry<String, String> entry : goodsMap.entrySet()) {
			ProductInfoVo vo = JSON.parseObject(entry.getValue(), ProductInfoVo.class);
			products.put(entry.getKey(), vo);
		}
		return products;
	}

	/**
	 * 根据商品ID查询商品信息
	 * 
	 * @param productId
	 * @return
	 */
	public ProductInfoVo getGoodsInfoById(String productId) {
		// 先去查缓存，为空再去load DB
		String result = cacheClient.hget("goods", productId);
		ProductInfoVo p = new ProductInfoVo();
		if (result == null || "".equals(result)) {
			ProductDTO dto = productService.loadById(Integer.parseInt(productId));
			BeanUtils.copyProperties(dto, p);
			List<ProductPropertyVo> productDetails = new ArrayList<ProductPropertyVo>();
			List<ProductPropertyDTO> productPropertyDtos = productPropertyService.listByProductId(Integer
					.parseInt(productId));
			for (ProductPropertyDTO productPropertyDto : productPropertyDtos) {
				List<ProductPropertyValueDTO> productPropertyValueDtos = productPropertyValueService
						.listByProductPropertyId(productPropertyDto.getId());
				ProductPropertyVo vo = new ProductPropertyVo();
				vo.setEngName(productPropertyDto.getEngName());
				vo.setName(productPropertyDto.getName());
				vo.setValues(productPropertyValueDtos);
				productDetails.add(vo);
			}
			p.setProductDetails(productDetails);
			cacheClient.hset("goods", String.valueOf(p.getId()), JSON.toJSONString(p));
			cacheClient.expire("goods", 1800);
			return p;
		}
		return JSON.parseObject(result, ProductInfoVo.class);
	}

	/**
	 * 创建订单 发生异常则进行回滚
	 * 
	 * @param orderVo
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult<String> createOrder(OrderVo orderVo) {
		String orderName = "";
		try {
			// 使用分布式锁防止重复生成订单
			long count = cacheClient.setnx("USER_" + orderVo.getUserId(), String.valueOf(orderVo.getUserId()));
			if (count == 0) {
				return new AjaxResult<String>(AjaxResultCode.REQUEST_INVALID.getCode(), "不能重复提交订单", null);
			}
			cacheClient.expire("USER_" + orderVo.getUserId(), REDIS_TIME_OUT);
			// 组装校验参数
			VerifyInfo verifyInfo = buildOrderVerifyInfo(orderVo);
			// 组装校验类型
			buildOrderVerifyTypes(verifyInfo);

			String orderId = orderVo.getOrderId();
			OrderDTO oldOrder = orderService.loadByNo(orderId);
			if (oldOrder != null) {
				// 更新订单
				return updateOrder(orderVo, oldOrder, verifyInfo);
			}

			VerifyResult verifyResult = baseVerify.verify(verifyInfo);
			if (!verifyResult.getExcuteResult()) {
				return new AjaxResult<String>(verifyResult.getErrorCode(), verifyResult.getMsg(), null);
			}
			OrderDTO order = new OrderDTO();

			BeanUtils.copyProperties(orderVo, order);
			order.setTransactionNo(BizUtils.getUUID());
			order.setStatus(OrderStatusEnum.SAVED.getStatus());
			StringBuilder uid = new StringBuilder();
			boolean flag = false;
			while (!flag) {
				uid = new StringBuilder();
				uid.append("1").append(orderVo.getType());
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
				uid.append(sdf.format(new Date())).append((int) (Math.random() * (9999 - 1000 + 1)) + 1000);
				if (orderService.loadByNo(uid.toString()) == null) {
					flag = true;
				}
			}
			orderName = uid.toString();
			order.setNo(orderName);
			order.setLastEditor("USER :" + orderVo.getUserId());

			BigDecimal productAmount = BigDecimal.ZERO;
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal agencyAmount = BigDecimal.ZERO;
			BigDecimal premiumAmount = BigDecimal.ZERO;
			BigDecimal finalAmount = BigDecimal.ZERO;

			List<OrderContainer> orderContainers = orderVo.getOrderContainers();
			int i = 1;
			for (OrderContainer oc : orderContainers) {
				ContainerTmpDTO dto = new ContainerTmpDTO();
				BeanUtils.copyProperties(oc, dto);
				dto.setAddTime(new Date());
				dto.setUpdateTime(new Date());
				dto.setOrderNo(orderName);
				dto.setTransactionNo(BizUtils.getUUID());
				String batchNumber = uid.toString();
				if (i < 10) {
					batchNumber += "0";
				}
				batchNumber += i;
				dto.setNo(batchNumber);
				dto.setEditor("USER :" + orderVo.getUserId());
				dto.setStatus(ContainerStatusEnum.SAVED.getStatus());
				BigDecimal containerProductPrice = BigDecimal.ZERO;
				BigDecimal containerQuantity = BigDecimal.ZERO;
				// 第一步写货柜详情表order_container_detail
				List<OrderContainerDetail> ocds = oc.getOrderContainerDetails();
				for (OrderContainerDetail ocd : ocds) {
					ContainerDetailTmpDTO cddto = new ContainerDetailTmpDTO();
					BeanUtils.copyProperties(ocd, cddto, new String[] { "productDetail" });
					cddto.setProductDetail(JSON.toJSONString(ocd.getProductDetail()));
					cddto.setContainerNo(batchNumber);
					cddto.setStatus(ContainerStatusEnum.SAVED.getStatus());
					BigDecimal oddtoTotalPrice = ocd.getPrice().multiply(ocd.getQuantity());
					cddto.setTotalPrice(oddtoTotalPrice);
					containerProductPrice = containerProductPrice.add(oddtoTotalPrice);
					containerQuantity = containerQuantity.add(ocd.getQuantity());
					containerDetailTmpService.create(cddto);
				}
				dto.setTotalQuantity(containerQuantity);
				dto.setProductAmount(containerProductPrice);
				dto.setAgencyAmount(BigDecimal.ZERO);
				dto.setPremiumAmount(BigDecimal.ZERO);
				dto.setTotalPrice(containerProductPrice);
				// 第二步写订单货柜表order_container
				containerTmpService.insertSelective(dto);
				productAmount = productAmount.add(containerProductPrice);
				totalAmount = totalAmount.add(dto.getTotalPrice());
				agencyAmount = agencyAmount.add(dto.getAgencyAmount());
				premiumAmount = premiumAmount.add(dto.getPremiumAmount());
				i++;
			}
			finalAmount = totalAmount;
			order.setProductAmount(productAmount);
			order.setTotalAmount(totalAmount);
			order.setAgencyAmount(agencyAmount);
			order.setPremiumAmount(premiumAmount);
			order.setFinalAmount(finalAmount);
			// 第三步写订单表order_info
			orderService.create(order);
			// 同时添加订单变更日志
			final String orderNo = orderName;
			final int userId = orderVo.getUserId();

			//统计表
			orderLogService.addRecordTime(order.getNo(),OrderStatusEnum.SAVED.getStatus());

			orderTaskHelper.submitRunnable(new Runnable() {

				@Override
				public void run() {
					orderLogService.addLog(orderNo, userId, OrderUpdateTypeEnum.SAVE.getType(), 0,
							OrderStatusEnum.SAVED.getStatus(), OrderUpdateTypeEnum.SAVE.getMessage());
				}
			});
		} catch (Exception e) {
			logger.error("the userId {} occur exception when committing order, the exception is : {}",
					orderVo.getUserId(), e.getMessage());
			EventHelper.triggerEvent(this.eventPublishers, "create.order." + orderVo.getUserId(),
					"failed to commit order " + JSON.toJSONString(orderVo), EventLevel.URGENT, e,
					ServerIpUtils.getServerIp());
			throw new RuntimeException("提交订单出现异常" + e.getMessage());
		}finally {
			cacheClient.del("USER_" + orderVo.getUserId());
		}
		return new AjaxResult<String>(orderName);
	}

	private AjaxResult<String> updateOrder(OrderVo orderVo, OrderDTO oldOrder, VerifyInfo verifyInfo) {
		//添加必要的校验
		verifyInfo.getOrderVerifyEnums().add(OrderVerifyEnum.ORDERPERMISSION);
		verifyInfo.getOrderVerifyEnums().add(OrderVerifyEnum.ORDERSTATUS);
		verifyInfo.setOldOrder(oldOrder);
		VerifyResult verifyResult = baseVerify.verify(verifyInfo);
		if (!verifyResult.getExcuteResult()) {
			return new AjaxResult<String>(verifyResult.getErrorCode(), verifyResult.getMsg(), null);
		}
		BeanUtils.copyProperties(orderVo, oldOrder);
		final String orderNo = orderVo.getOrderId();
		oldOrder.setLastEditor("USER :" + orderVo.getUserId());

		BigDecimal productAmount = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal agencyAmount = BigDecimal.ZERO;
		BigDecimal premiumAmount = BigDecimal.ZERO;
		BigDecimal finalAmount = BigDecimal.ZERO;

		List<OrderContainer> orderContainers = orderVo.getOrderContainers();
		List<ContainerTmpDTO> oldOrderContainers = containerTmpService.listByOrderNo(oldOrder.getNo());
		// 删除原来的全部货柜信息
		if (oldOrderContainers != null && oldOrderContainers.size() != 0) {
			for (ContainerTmpDTO cdto : oldOrderContainers) {
				cdto.setStatus(0);
				containerTmpService.updateById(cdto);
				List<ContainerDetailTmpDTO> oldOrderContainerDetails = containerDetailTmpService.listByContainerNo(cdto
						.getNo());
				// 删除原来的全部货柜详情
				for (ContainerDetailTmpDTO ocdtd : oldOrderContainerDetails) {
					ocdtd.setStatus(0);
					containerDetailTmpService.updateById(ocdtd);
				}
			}
		}
		int i = 1;
		for (OrderContainer oc : orderContainers) {
			ContainerTmpDTO dto = new ContainerTmpDTO();
			BeanUtils.copyProperties(oc, dto);
			dto.setAddTime(new Date());
			dto.setUpdateTime(new Date());
			dto.setTransactionNo(BizUtils.getUUID());
			String batchNumber = oldOrder.getNo();
			if (i < 10) {
				batchNumber += "0";
			}
			batchNumber += i;
			dto.setNo(batchNumber);
			dto.setOrderNo(orderNo);
			dto.setTransactionNo(BizUtils.getUUID());
			dto.setStatus(ContainerStatusEnum.SAVED.getStatus());
			dto.setEditor("USER :" + orderVo.getUserId());
			BigDecimal containerProductPrice = BigDecimal.ZERO;
			BigDecimal containerQuantity = BigDecimal.ZERO;
			// 第一步写货柜详情表order_container_detail
			List<OrderContainerDetail> ocds = oc.getOrderContainerDetails();
			for (OrderContainerDetail ocd : ocds) {
				ContainerDetailTmpDTO cddto = new ContainerDetailTmpDTO();
				BeanUtils.copyProperties(ocd, cddto, new String[] { "productDetail" });
				cddto.setProductDetail(JSON.toJSONString(ocd.getProductDetail()));
				cddto.setContainerNo(batchNumber);
				cddto.setStatus(ContainerStatusEnum.SAVED.getStatus());
				BigDecimal oddtoTotalPrice = ocd.getPrice().multiply(ocd.getQuantity());
				cddto.setTotalPrice(oddtoTotalPrice);
				containerProductPrice = containerProductPrice.add(oddtoTotalPrice);
				containerQuantity = containerQuantity.add(ocd.getQuantity());
				containerDetailTmpService.create(cddto);
			}
			dto.setTotalQuantity(containerQuantity);
			dto.setProductAmount(containerProductPrice);
			dto.setAgencyAmount(BigDecimal.ZERO);
			dto.setPremiumAmount(BigDecimal.ZERO);
			dto.setTotalPrice(containerProductPrice);
			// 第二步写订单货柜表order_container
			containerTmpService.insertSelective(dto);
			productAmount = productAmount.add(containerProductPrice);
			totalAmount = totalAmount.add(dto.getTotalPrice());
			agencyAmount = agencyAmount.add(dto.getAgencyAmount());
			premiumAmount = premiumAmount.add(dto.getPremiumAmount());
			i++;
		}
		finalAmount = totalAmount;
		oldOrder.setProductAmount(productAmount);
		oldOrder.setTotalAmount(totalAmount);
		oldOrder.setAgencyAmount(agencyAmount);
		oldOrder.setPremiumAmount(premiumAmount);
		oldOrder.setFinalAmount(finalAmount);
		// 第三步写订单表order_info
		orderService.update(oldOrder);

		//统计表
		orderLogService.addRecordTime(oldOrder.getNo(),OrderStatusEnum.SAVED.getStatus());

		// 同时添加订单变更日志
		final int userId = orderVo.getUserId();
		orderTaskHelper.submitRunnable(new Runnable() {

			@Override
			public void run() {
				orderLogService.addLog(orderNo, userId, OrderUpdateTypeEnum.SAVE.getType(), 0,
						OrderStatusEnum.SAVED.getStatus(), OrderUpdateTypeEnum.SAVE.getMessage());
			}
		});
		return new AjaxResult<String>(orderNo);
	}

	/**
	 * 创建物流服务信息 发生异常则进行回滚
	 * 
	 * @param orderLogistics
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult<Boolean> createLogistics(OrderLogistics orderLogistics) {
		int userId = 0;
		try {
			userId = orderLogistics.getUserId();
			final String key = "USER_" + userId;
			final String content = String.valueOf(userId);
			// 使用分布式锁防止重复生成物流服务信息
			long count = cacheClient.setnx(key, content);
			if(count == 0){
				return new AjaxResult<Boolean>(AjaxResultCode.REQUEST_INVALID.getCode(), "不能重复提交", null);
			}

			cacheClient.expire("USER_" + userId, REDIS_TIME_OUT);

			// 组装校验参数
			VerifyInfo verifyInfo =buildLogiticsVerifyInfo(orderLogistics);
			// 组装校验类型
			buildLogiticsVerifyTypes(verifyInfo);
			VerifyResult verifyResult = baseVerify.verify(verifyInfo);
			if (!verifyResult.getExcuteResult()) {
				return new AjaxResult<Boolean>(verifyResult.getErrorCode(), verifyResult.getMsg(), null);
			}
			UserDeliveryAddressDTO deliveryAddressDTO = userDeliveryAddressService.loadById(orderLogistics
					.getDeliveryId());
			OrderDTO order = orderService.loadByNo(orderLogistics.getOrderNo());
			LogisticsDTO dto = new LogisticsDTO();
			BeanUtils.copyProperties(orderLogistics, dto);
			dto.setType(orderLogistics.getLogisticsType());
			logisticsService.insertSelective(dto);
			// 添加收货地址信息

			DeliveryInfoDTO deliveryInfoDto = new DeliveryInfoDTO();
			BeanUtils.copyProperties(deliveryAddressDTO, deliveryInfoDto, new String[] { "id", "addTime",
					"updateTime" });
			deliveryInfoDto.setOrderNo(orderLogistics.getOrderNo());
			deliveryInfoDto.setDeliveryId(deliveryAddressDTO.getId());
			deliveryInfoService.create(deliveryInfoDto);
			// 如果需要资金服务，更新订单表
			Integer needLoan = orderLogistics.getNeedLoan();
			if (needLoan != null && needLoan == 1) {
				order.setNeedLoan(needLoan);
				List<ApplyLoanInfoVo> loanInfos = orderLogistics.getLoadInfo();
				if (loanInfos != null && loanInfos.size() != 0) {
					for (ApplyLoanInfoVo loan : loanInfos) {
						LoanInfoDTO loanInfoDTO = new LoanInfoDTO();
						loanInfoDTO.setOrderNo(order.getNo());
						loanInfoDTO.setTransactionNo(loan.getTransactionNo());
						loanInfoDTO.setAvailableLoan(loan.getLoanQuota());
						BigDecimal applyQuota = loan.getApplyQuota();
						if (applyQuota.compareTo(new BigDecimal(100)) < 1) {
							throw new IllegalArgumentException("贷款不能少于100");
						}
						loanInfoDTO.setAppliyLoan(applyQuota);
						loanInfoDTO.setProductId(loan.getProductId());
						loanInfoDTO.setProdictName(loan.getProductName());
						loanInfoDTO.setUserId(userId);
						loanInformationService.createLoanInfo(loanInfoDTO);
					}
				}
			}
			order.setPayType(orderLogistics.getPayType());
			orderService.update(order);

		} catch (Exception e) {
			logger.error("the userId {} occur exception when committing order, the exception is : {}", orderLogistics.getUserId(),
					e.getMessage());
			EventHelper.triggerEvent(this.eventPublishers, "create.logistics." + orderLogistics.getUserId(), "failed to commit order "
					+ JSON.toJSONString(orderLogistics), EventLevel.URGENT, e, ServerIpUtils.getServerIp());
			throw new RuntimeException("提交订单出现异常" + e.getMessage());
		}finally {
			// 释放锁
			cacheClient.del("USER_" + userId);
		}
		return new AjaxResult<Boolean>(true);
	}

	/**
	 * 分页查询订单
	 * 
	 * @param request
	 * @return
	 */
	public PageResult<OrderDetailVo> searchOrderByPage(OrderSearchRequest request) {
		PageResult<OrderDetailVo> result = new PageResult<OrderDetailVo>();
		int limit = request.getPageSize();
		if (limit == 0) {
			limit = 10;
		}
		int pageNo = request.getPageNo();
		int count = orderService.selectCountByExample(request);
		result.setTotalRecords(count);
		List<OrderDTO> orderDtos = orderService.paginateExpiredOrdersByPage(request);
		List<OrderDetailVo> list = new ArrayList<OrderDetailVo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (orderDtos != null && orderDtos.size() != 0) {
			for (OrderDTO order : orderDtos) {
				OrderDetailVo vo = new OrderDetailVo();
				vo.setDate(sdf.format(order.getAddTime()));
				vo.setOrderNo(order.getNo());
				vo.setOrderStatus(order.getStatus());
				vo.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getUserDesc());
				vo.setOrderDetailUrl(urlConfigService.getOrderDetailUrl(order.getNo()));
				//获取采购商
				int userid = order.getUserId();
				String purchaseName = "";
				UserEnterpriseDTO dto = userEnterpriseService.loadByUserId(userid);
				if(dto != null) {
					purchaseName = dto.getType() == UserTypeEnum.PERSONAL.getType() ? dto.getName() : dto.getEnterpriseName();
				}
				vo.setPurchaseName(purchaseName);

				UserSupplierDTO userSupplier = supplierService.loadSupplierById(order.getSupplierId());
				if (userSupplier == null) {
					vo.setSupplierName("");
				} else {
					vo.setSupplierName(userSupplier.getSupplierName());
				}
				List<ContainerDetailVo> containerDetails = new ArrayList<ContainerDetailVo>();
				if (isQueryTmp(order.getStatus())) {
					// 查询临时表
					List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
					if (containers != null && containers.size() != 0) {
						for (ContainerTmpDTO cdto : containers) {
							ContainerDetailVo cvo = new ContainerDetailVo();
							cvo.setContainerId(cdto.getNo());
							cvo.setContainerStatus(cdto.getStatus());
							cvo.setContainerStatusDesc(ContainerStatusEnum.getStatusDesc(cdto.getStatus()));
							cvo.setProductName(cdto.getProductName());
							cvo.setTransactionNo(cdto.getTransactionNo());
							// 查询贷款信息
							LoanInfoModel loanInfoModel = loanInformationService.loadLoanInfosByTransactionNo(cdto
									.getTransactionNo());
							if (loanInfoModel != null) {
								cvo.setAppliyLoan(loanInfoModel.getAppliyLoan());
								cvo.setLoanStatus(loanInfoModel.getStatus());
								cvo.setLoanStatusDesc(loanInfoModel.getStatusDesc());
							}
							containerDetails.add(cvo);
						}
					}
					vo.setContainerDetails(containerDetails);
				} else {
					List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
					if (containers != null && containers.size() != 0) {
						for (ContainerDTO cdto : containers) {
							ContainerDetailVo cvo = new ContainerDetailVo();
							cvo.setContainerId(cdto.getNo());
							cvo.setContainerStatus(cdto.getStatus());
							cvo.setContainerStatusDesc(ContainerStatusEnum.getStatusDesc(cdto.getStatus()));
							cvo.setProductName(cdto.getProductName());
							// 稍后补上贷款信息
							LoanInfoModel loanInfoModel = loanInformationService.loadLoanInfosByTransactionNo(cdto
									.getTransactionNo());
							if (loanInfoModel != null) {
								cvo.setAppliyLoan(loanInfoModel.getAppliyLoan());
								cvo.setLoanStatus(loanInfoModel.getStatus());
								cvo.setLoanStatusDesc(loanInfoModel.getStatusDesc());
							}
							containerDetails.add(cvo);
						}
					}
					vo.setContainerDetails(containerDetails);
				}
				list.add(vo);
			}
			result.setList(list);
		}
		result.setPageNo(pageNo == 0 ? 1 : pageNo);
		result.setPageSize(limit);
		return result;
	}

	/**
	 * 查询订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderDetailShowBean queryOrderDetail(String orderId, int userId) {
		OrderDetailShowBean orderDetail = new OrderDetailShowBean();
		OrderDTO order = orderService.loadByNo(orderId);
		if (null == order || userId != order.getUserId()) {
			return null;
		}
		BeanUtils.copyProperties(order, orderDetail);
		orderDetail.setOrderNo(orderId);
		orderDetail.setOrderType(order.getType());
		orderDetail.setOrderStatus(order.getStatus());
		orderDetail.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getUserDesc());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		orderDetail.setPlaceOrderTime(sdf.format(order.getAddTime()));
		// 加载收货地址信息
		DeliveryInfoDTO deliveryInfoDto = deliveryInfoService.loadByOrderNo(orderId);
		if (deliveryInfoDto != null) {
			DeliveryInfoVo deliveryInfo = new DeliveryInfoVo();
			BeanUtils.copyProperties(deliveryInfoDto, deliveryInfo);
			deliveryInfo.setCountryName(metadataProvider.getCountryName(deliveryInfoDto.getCountryId()));
			deliveryInfo.setProvinceName(metadataProvider.getProvinceName(deliveryInfoDto.getProvinceId()));
			deliveryInfo.setCityName(metadataProvider.getCityName(deliveryInfoDto.getCityId()));
			deliveryInfo.setDistrictName(metadataProvider.getAreaName(deliveryInfoDto.getDistrictId()));
			orderDetail.setDeliveryAddress(deliveryInfo);
		}
		// 加载供应商信息
		UserSupplierDTO userSupplier = supplierService.loadSupplierById(order.getSupplierId());
		UserSupplierVo supplier = new UserSupplierVo();
		if (userSupplier != null) {
			BeanUtils.copyProperties(userSupplier, supplier);
			supplier.setCountryName(metadataProvider.getCountryName(userSupplier.getCountryId()));
			supplier.setProvinceName(metadataProvider.getProvinceName(userSupplier.getProvinceId()));
			supplier.setCityName(metadataProvider.getCityName(userSupplier.getCityId()));
			supplier.setDistrictName(metadataProvider.getAreaName(userSupplier.getDistrictId()));
		} else {
			supplier.setCountryName("");
			supplier.setProvinceName("");
			supplier.setCityName("");
			supplier.setDistrictName("");
		}
		orderDetail.setSupplier(supplier);
		List<OrderContainer> orderContainers = new ArrayList<OrderContainer>();

		if (isQueryTmp(order.getStatus())) {
			List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerTmpDTO dto : containers) {
					OrderContainer oc = new OrderContainer();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());
					String result = cacheClient.hget("goods", String.valueOf(dto.getProductId()));
					ProductInfoVo p = new ProductInfoVo();
					if (result == null || "".equals(result)) {
						ProductDTO pdto = productService.loadById(dto.getProductId());
						BeanUtils.copyProperties(pdto, p);
					} else {
						p = JSON.parseObject(result, ProductInfoVo.class);
					}
					oc.setMaxQuantity(p.getCapacitySize());
					List<OrderContainerDetail> orderContainerDetails = new ArrayList<OrderContainerDetail>();
					List<ContainerDetailTmpDTO> containerDetailDtos = containerDetailTmpService.listByContainerNo(dto
							.getNo());
					if (containerDetailDtos != null && containerDetailDtos.size() != 0) {
						for (ContainerDetailTmpDTO cdto : containerDetailDtos) {
							OrderContainerDetail ocd = new OrderContainerDetail();
							BeanUtils.copyProperties(cdto, ocd, new String[] { "productDetail" });
							@SuppressWarnings("unchecked")
							Map<String, String> productDetail = JSON
									.parseObject(cdto.getProductDetail(), HashMap.class);
							ocd.setProductDetail(productDetail);
							orderContainerDetails.add(ocd);
						}
					}
					oc.setOrderContainerDetails(orderContainerDetails);
					orderContainers.add(oc);
				}
			}
		} else {
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					OrderContainer oc = new OrderContainer();
					BeanUtils.copyProperties(dto, oc);
					oc.setBatchNumber(dto.getNo());
					String result = cacheClient.hget("goods", String.valueOf(dto.getProductId()));
					ProductInfoVo p = new ProductInfoVo();
					if (result == null || "".equals(result)) {
						ProductDTO pdto = productService.loadById(dto.getProductId());
						BeanUtils.copyProperties(pdto, p);
					} else {
						p = JSON.parseObject(result, ProductInfoVo.class);
					}
					oc.setMaxQuantity(p.getCapacitySize());
					List<OrderContainerDetail> orderContainerDetails = new ArrayList<OrderContainerDetail>();
					List<ContainerDetailDTO> containerDetailDtos = containerDetailService
							.listByContainerNo(dto.getNo());
					if (containerDetailDtos != null && containerDetailDtos.size() != 0) {
						for (ContainerDetailDTO cdto : containerDetailDtos) {
							OrderContainerDetail ocd = new OrderContainerDetail();
							BeanUtils.copyProperties(cdto, ocd, new String[] { "productDetail" });
							@SuppressWarnings("unchecked")
							Map<String, String> productDetail = JSON
									.parseObject(cdto.getProductDetail(), HashMap.class);
							ocd.setProductDetail(productDetail);
							orderContainerDetails.add(ocd);
						}
					}
					oc.setOrderContainerDetails(orderContainerDetails);
					orderContainers.add(oc);
				}
			}
		}
		orderDetail.setOrderContainers(orderContainers);
		LogisticsDTO logisticsDto = logisticsService.loadByOrderNo(order.getNo());
		if (logisticsDto != null) {
			BeanUtils.copyProperties(logisticsDto, orderDetail);
			orderDetail.setLogisticsId(logisticsDto.getId());
			orderDetail.setLogisticsType(logisticsDto.getType());
			// 加载清关,国内国外物流公司信息
			EnterpriseInfoDTO clearanceCompanyDto = enterpriseInfoService
					.loadById(logisticsDto.getClearanceCompanyId());
			EnterpriseInfoVo clearanceCompany = new EnterpriseInfoVo();
			BeanUtils.copyProperties(clearanceCompanyDto, clearanceCompany);
			orderDetail.setClearanceCompany(clearanceCompany);
			EnterpriseInfoDTO innerExpressDto = enterpriseInfoService.loadById(logisticsDto.getInnerExpressId());
			EnterpriseInfoVo innerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(innerExpressDto, innerExpress);
			orderDetail.setInnerExpress(innerExpress);
			EnterpriseInfoDTO outerExpressDto = enterpriseInfoService.loadById(logisticsDto.getOuterExpressId());
			EnterpriseInfoVo outerExpress = new EnterpriseInfoVo();
			BeanUtils.copyProperties(outerExpressDto, outerExpress);
			orderDetail.setOuterExpress(outerExpress);
		}
		return orderDetail;
	}

	/**
	 * 资金服务查询货柜信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<ContainerLoanVo> queryContainer(String orderId, int userId) {
		List<ContainerLoanVo> result = new ArrayList<ContainerLoanVo>();
		OrderDTO order = orderService.loadByNo(orderId);
		if (isQueryTmp(order.getStatus())) {
			List<ContainerTmpDTO> containers = containerTmpService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerTmpDTO dto : containers) {
					ContainerLoanVo vo = new ContainerLoanVo();
					vo.setContainerId(dto.getNo());
					vo.setProductName(dto.getProductName());
					vo.setProductId(dto.getProductId());
					vo.setTransactionNo(dto.getTransactionNo());
					UserProductLoanInfoDTO quota = sysUserProductLoanService.loadByExample(dto.getProductId(), userId);
					if (quota != null) {
						vo.setLoanQuota(quota.getProductLoan());
					} else {
						vo.setLoanQuota(BigDecimal.ZERO);
					}
					result.add(vo);
				}
			}
		} else {
			List<ContainerDTO> containers = containerService.listByOrderNo(order.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					ContainerLoanVo vo = new ContainerLoanVo();
					vo.setContainerId(dto.getNo());
					vo.setProductName(dto.getProductName());
					vo.setProductId(dto.getProductId());
					vo.setTransactionNo(dto.getTransactionNo());
					UserProductLoanInfoDTO quota = sysUserProductLoanService.loadByExample(dto.getProductId(), userId);
					if (quota != null) {
						vo.setLoanQuota(quota.getProductLoan());
					} else {
						vo.setLoanQuota(BigDecimal.ZERO);
					}
					LoanInfoModel loanInfo = loanInformationService
							.loadLoanInfosByTransactionNo(dto.getTransactionNo());
					if (loanInfo != null) {
						vo.setApplyQuota(loanInfo.getAppliyLoan());
						vo.setConfirmLoan(loanInfo.getConfirmLoan());
						vo.setServiceFee(loanInfo.getServiceFee());
					}
					result.add(vo);
					result.add(vo);
				}
			}
		}
		return result;
	}

	/*
	 * 根据货柜Id查询货柜信息,给贷款服务用
	 * 
	 * @param orderId
	 * 
	 * @param containerId
	 * 
	 * @return
	 */
	public ContainerInfoForLoan queryContainerInfo(String orderId, String transactionNo) {
		ContainerInfoForLoan result = new ContainerInfoForLoan();
		OrderDTO order = orderService.loadByNo(orderId);

		if(order!=null){
			if (isQueryTmp(order.getStatus())) {
				ContainerTmpDTO container = containerTmpService.loadByTransactionNo(transactionNo);
				if (container != null) {
					BeanUtils.copyProperties(container, result);
				}
			} else {
				ContainerDTO container = containerService.loadByTransactionNo(transactionNo);
				if (container != null) {
					BeanUtils.copyProperties(container, result);
				}
			}
		}else{
			//新货柜的内容
			result = newOrderForOtherService.handllerByNewContainer(transactionNo);
		}

		return result;
	}

	/**
	 * 查询物流详情
	 * 
	 * @param containerNo
	 * @return
	 */
	public LogisticsDetailVo queryLogistics(String containerNo) {
		LogisticsDetailVo result = new LogisticsDetailVo();
		ContainerDTO container = containerService.loadByContainerId(containerNo);
		result.setContainerNo(containerNo);
		result.setOrderNo(container.getOrderNo());
		List<LogisticsDetailDTO> logisticsDetais = logisticsDetailService.loadByContainerNo(containerNo);
		List<LogisticsDetailBean> list = new ArrayList<LogisticsDetailBean>();
		if (logisticsDetais != null && logisticsDetais.size() != 0) {
			for (LogisticsDetailDTO dto : logisticsDetais) {
				LogisticsDetailBean bean = new LogisticsDetailBean();
				BeanUtils.copyProperties(dto, bean);
				List<BizFileVo> fileList = new ArrayList<BizFileVo>();
				List<BizFileDTO> files = bizFileService.listByBizIdAndType(String.valueOf(dto.getId()),
						BizFileEnum.LOGISTICS_DETAIL.getType());
				if (files != null && files.size() != 0) {
					for (BizFileDTO biz : files) {
						BizFileVo bizVo = new BizFileVo();
						BeanUtils.copyProperties(biz, bizVo);
						bizVo.setUrl(fileUploadService.buildDiskUrl(biz.getPath()));
						fileList.add(bizVo);
					}
				}
				bean.setFilePaths(fileList);
				list.add(bean);
			}
			long logisticsId = logisticsDetais.get(0).getLogisticsId();
			LogisticsDTO logisticsDto = logisticsService.loadById(logisticsId);
			result.setInnerExpress(enterpriseInfoService.loadById(logisticsDto.getInnerExpressId()));
			result.setOuterExpress(enterpriseInfoService.loadById(logisticsDto.getOuterExpressId()));
		}
		result.setLogisticsDetails(list);
		return result;
	}

	/**
	 * 根据用户Id查询其配置的物流清关公司信息
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, EnterpriseInfoVo> queryEnterprise(int userId) {
		Map<String, EnterpriseInfoVo> enterpriseMap = new HashMap<String, EnterpriseInfoVo>();
		UserProfileDTO dto = userProfileService.loadByUserId(userId);
		EnterpriseInfoDTO clearanceCompanyDto = enterpriseInfoService.loadById(dto.getNationalClearance());
		EnterpriseInfoDTO innerExpressDto = enterpriseInfoService.loadById(dto.getNationalLogistics());
		EnterpriseInfoDTO outerExpressDto = enterpriseInfoService.loadById(dto.getInternationalLogistics());
		EnterpriseInfoVo clearanceCompany = new EnterpriseInfoVo();
		EnterpriseInfoVo innerExpress = new EnterpriseInfoVo();
		EnterpriseInfoVo outerExpress = new EnterpriseInfoVo();
		BeanUtils.copyProperties(clearanceCompanyDto, clearanceCompany);
		BeanUtils.copyProperties(innerExpressDto, innerExpress);
		BeanUtils.copyProperties(outerExpressDto, outerExpress);
		enterpriseMap.put("clearanceCompany", clearanceCompany);
		enterpriseMap.put("innerExpress", innerExpress);
		enterpriseMap.put("outerExpress", outerExpress);
		return enterpriseMap;
	}

	/**
	 * 判断用户是否配置清关公司物流公司信息
	 * 
	 * @param userId
	 * @return
	 */
	public boolean hasEnterprise(int userId) {
		UserProfileDTO dto = userProfileService.loadByUserId(userId);
		if (dto == null || dto.getNationalClearance() == 0 || dto.getNationalLogistics() == 0
				|| dto.getInternationalLogistics() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 根据订单状态判断查询正式表还是临时表
	 * 
	 * @param status
	 * @return
	 */
	private boolean isQueryTmp(int status) {
		if (status == 1 || status == 2 || status == 3) {
			return true;
		}
		return false;
	}

	/**
	 * 组装创建订单校验入参
	 * 
	 * @param order
	 * @return
	 */
	private VerifyInfo buildOrderVerifyInfo(OrderVo order) {
		VerifyInfo verifyInfo = new VerifyInfo();
		UserModel userInfo = stableInfoService.getUserModel(order.getUserId());
		verifyInfo.setUserInfo(userInfo);
		verifyInfo.setProductInfos(order.getOrderContainers());
		OrderBasicInfo orderBasicInfo = new OrderBasicInfo();
		orderBasicInfo.setOrderId(order.getOrderId());
		orderBasicInfo.setSupplierId(order.getSupplierId());
		orderBasicInfo.setType(order.getType());
		orderBasicInfo.setUserIp(order.getUserIp());
		verifyInfo.setOrderInfo(orderBasicInfo);
		return verifyInfo;
	}

	/**
	 * 组装创建订单校验子类
	 * @param verifyInfo
	 */
	private void buildOrderVerifyTypes(VerifyInfo verifyInfo) {
		List<BaseVerifyEnum> baseVerifyEnums = new ArrayList<BaseVerifyEnum>();
		baseVerifyEnums.add(BaseVerifyEnum.CREATEORDER);
		List<OrderVerifyEnum> orderVerifyEnums = new ArrayList<OrderVerifyEnum>();
		orderVerifyEnums.add(OrderVerifyEnum.PRODUCT);
		orderVerifyEnums.add(OrderVerifyEnum.ORDERBASIC);
		orderVerifyEnums.add(OrderVerifyEnum.SUPPLIER);
		verifyInfo.setOrderVerifyEnums(orderVerifyEnums);
		verifyInfo.setBaseVerifyEnums(baseVerifyEnums);
	}
	
	/**
	 * 组装创建订单校验入参
	 * 
	 * @param orderLogistics
	 * @return
	 */
	private VerifyInfo buildLogiticsVerifyInfo(OrderLogistics orderLogistics) {
		VerifyInfo verifyInfo = new VerifyInfo();
		UserModel userInfo = stableInfoService.getUserModel(orderLogistics.getUserId());
		verifyInfo.setUserInfo(userInfo);
		OrderDTO order = orderService.loadByNo(orderLogistics.getOrderNo());
		verifyInfo.setOldOrder(order);
		verifyInfo.setLogisticsInfo(orderLogistics);
		return verifyInfo;
	}

	/**
	 * 组装创建订单校验子类
	 * @param verifyInfo
	 */
	private void buildLogiticsVerifyTypes(VerifyInfo verifyInfo) {
		List<BaseVerifyEnum> baseVerifyEnums = new ArrayList<BaseVerifyEnum>();
		baseVerifyEnums.add(BaseVerifyEnum.CREATELOGISTICS);
		List<OrderVerifyEnum> orderVerifyEnums = new ArrayList<OrderVerifyEnum>();
		orderVerifyEnums.add(OrderVerifyEnum.DELIVERY);
		orderVerifyEnums.add(OrderVerifyEnum.LOGISTICS);
		orderVerifyEnums.add(OrderVerifyEnum.LOAN);
		orderVerifyEnums.add(OrderVerifyEnum.ORDERSTATUS);
		verifyInfo.setOrderVerifyEnums(orderVerifyEnums);
		verifyInfo.setBaseVerifyEnums(baseVerifyEnums);
	}
}
