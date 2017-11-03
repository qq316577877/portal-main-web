package com.fruit.portal.action.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.account.biz.dto.UserSupplierDTO;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.common.OrderUpdateTypeEnum;
import com.fruit.order.biz.model.Product;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.service.account.DeliveryAddressService;
import com.fruit.portal.service.account.SupplierService;
import com.fruit.portal.service.order.OrderGeneralService;
import com.fruit.portal.service.order.OrderProcessService;
import com.fruit.portal.vo.account.AddressVo;
import com.fruit.portal.vo.account.SupplierVo;
import com.fruit.portal.vo.order.ContainerLoanVo;
import com.fruit.portal.vo.order.EnterpriseInfoVo;
import com.fruit.portal.vo.order.LogisticsDetailVo;
import com.fruit.portal.vo.order.OrderDetailShowBean;
import com.fruit.portal.vo.order.OrderLogistics;
import com.fruit.portal.vo.order.OrderVo;
import com.fruit.portal.vo.order.ProductInfoVo;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;

@Component
@UriMapping("/order")
public class OrderAction extends BaseAction {

	@Autowired
	private OrderGeneralService orderGeneralService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private DeliveryAddressService delieryAddressService;

	@Autowired
	private OrderProcessService orderProcessService;

	private static final Logger logger = LoggerFactory.getLogger(OrderAction.class);

	/**
	 * 查询所有商品信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/find_all_goods")
	public AjaxResult<Map<String, ProductInfoVo>> findAllGoods() {
		return new AjaxResult<Map<String, ProductInfoVo>>(orderGeneralService.findAllGoods());
	}

	/**
	 * 根据商品ID查询商品信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/find_good_byId_ajax")
	public AjaxResult<ProductInfoVo> findGoodsById() {
		String str = super.getBodyString();
		Product p = JSON.parseObject(str, Product.class);
		String productId = p.getId().toString();
		return new AjaxResult<ProductInfoVo>(orderGeneralService.getGoodsInfoById(productId));
	}

	/**
	 * 跳转下单页
	 * 
	 * @return
	 */
	@UriMapping(value = "/create", interceptors = "userLoginCheckInterceptor")
	public String toCreateOrder() {
		int userId = super.getLoginUserId();
		UserModel userModel = stableInfoService.getUserModel(userId);
		if (userModel == null || userModel.getEnterpriseVerifyStatus() != UserEnterpriseStatusEnum.VERIFIED.getStatus()) {
			return "redirect:" + this.urlConfigService.getMemberEnterpriseVerifyUrl();
		}
		List<UserSupplierDTO> supplierDTOs = supplierService.loadAllSupplier(userId);
		List<SupplierVo> supplierList = this.supplierService.wrapVOs(supplierDTOs);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("supplierList", supplierList);
		HttpServletRequest request = WebContext.getRequest();
		request.setAttribute("__DATA", super.toJson(data));
		return "/order/create";
	}

	/**
	 * 代采查看供应商信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/supplier_query_agent_ajax")
	public AjaxResult<List<SupplierVo>> querySupplierForAgent() {
		List<UserSupplierDTO> supplierDTOs = supplierService.loadAllSupplier(0);
		List<SupplierVo> supplierList = this.supplierService.wrapVOs(supplierDTOs);
		return new AjaxResult<List<SupplierVo>>(supplierList);
	}

	/**
	 * 创建订单
	 * 
	 * @return
	 */
	@UriMapping(value = "/create_order_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<String> createOrder() {
		try {
			OrderVo order = super.getBodyObject(OrderVo.class);
			// 入参校验
			if (order == null || order.getSupplierId() == 0) {
				return new AjaxResult<String>(AjaxResultCode.REQUEST_ERROR_METHOD.getCode(), "缺少必要参数");
			}
			order.setUserId(super.getLoginUserId());
			String remoteIp = WebContext.getRequest().getRemoteHost();
			order.setUserIp(remoteIp);
			return orderGeneralService.createOrder(order);
		} catch (Exception e) {
			logger.error("[/order/create_order_ajax].Exception:{}", e);
			return new AjaxResult<String>(AjaxResultCode.SERVER_ERROR.getCode(), e.getMessage());
		}
	}

	/**
	 * 跳转到物流服务页
	 * 
	 * @return
	 */
	@UriMapping(value = "/logistics", interceptors = "userLoginCheckInterceptor")
	public String toLogistics() {
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			String orderId = super.getStringParameter("orderId");
			int type = super.getIntParameter("type");
			int userId = super.getLoginUserId();
			List<UserDeliveryAddressDTO> deliveryAddressDTOs = delieryAddressService.loadAllAddress(userId);
			List<AddressVo> receiveAddress = delieryAddressService.wrapVOs(deliveryAddressDTOs);
			if (type == 2) {
				Map<String, EnterpriseInfoVo> enterpriseMap = orderGeneralService.queryEnterprise(userId);
				data.put("clearanceCompany", enterpriseMap.get("clearanceCompany"));
				data.put("innerExpress", enterpriseMap.get("innerExpress"));
				data.put("outerExpress", enterpriseMap.get("outerExpress"));
			}
			if (type == 1) {
				Map<String, EnterpriseInfoVo> enterpriseMap = orderGeneralService.queryEnterprise(0);
				data.put("clearanceCompany", enterpriseMap.get("clearanceCompany"));
				data.put("innerExpress", enterpriseMap.get("innerExpress"));
				data.put("outerExpress", enterpriseMap.get("outerExpress"));
			}
			data.put("receiveAddress", receiveAddress);
			data.put("orderId", orderId);
			data.put("type", type);
			HttpServletRequest request = WebContext.getRequest();
			request.setAttribute("__DATA", super.toJson(data));
			return "/order/logistics";
		} catch (Exception e) {
			logger.error("[/order/logistics].Exception:{}", e);
			return FTL_ERROR_400;
		}
	}

	/**
	 * 保存物流服务信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/create_logistics", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<Boolean> createLogistics() {
		int userId = super.getLoginUserId();
		OrderLogistics dto = super.getBodyObject(OrderLogistics.class);
		Integer deliveryId = dto.getDeliveryId();
		if (deliveryId == null || deliveryId == 0) {
			return new AjaxResult<Boolean>(AjaxResultCode.REQUEST_INVALID.getCode(), "请选择收货人", false);
		}
		dto.setUserId(userId);
		AjaxResult<Boolean> flag = orderGeneralService.createLogistics(dto);
		String orderId = dto.getOrderNo();
		String domain = super.getDomain();
		orderProcessService.operateOrder(userId, orderId, OrderEventEnum.SUBMIT, domain,
				OrderUpdateTypeEnum.STATUS.getType());
		return flag;
	}

	/*
	 * 下单成功后跳转
	 */
	@UriMapping(value = "/submitted", interceptors = "userLoginCheckInterceptor")
	public String finised() {
		int userId = super.getLoginUserId();
		UserModel userInfo = stableInfoService.getUserModel(userId);
		Map<String, Object> data = new HashMap<String, Object>();
		if(userInfo == null){
			data.put("mobile","");
		}else{
			data.put("mobile", userInfo.getMobile());
		}
		HttpServletRequest request = WebContext.getRequest();
		request.setAttribute("__DATA", super.toJson(data));
		return "/order/order_result";
	}

	/**
	 * 查看订单详情
	 * 
	 * @return
	 */
	@UriMapping(value = "/detail", interceptors = "userLoginCheckInterceptor")
	public String queryOrderDetail() {
		String orderId = super.getStringParameter("id");
		HttpServletRequest request = WebContext.getRequest();
		int userId = super.getLoginUserId();
		OrderDetailShowBean orders = orderGeneralService.queryOrderDetail(orderId, userId);
		if (null == orders) {
			return FTL_ERROR_400;
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderDetail", orders);
		request.setAttribute("__DATA", super.toJson(data));
		int status = orders.getOrderStatus();
		if (status > 1) {
			return "/order/order_detail";
		} else {
			return "/order/order_detail_one";
		}
	}

	@UriMapping(value = "/detail1")
	public AjaxResult<OrderDetailShowBean> queryOrderDetail1() {
		String id = super.getStringParameter("id");
		int userId = super.getLoginUserId();
		OrderDetailShowBean orders = orderGeneralService.queryOrderDetail(id, userId);
		return new AjaxResult<OrderDetailShowBean>(orders);
	}

	/**
	 * 资金服务查询货柜信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/container_detail_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<List<ContainerLoanVo>> queryContainer() {
		String orderId = super.getStringParameter("orderId");
		int userId = super.getLoginUserId();
		return new AjaxResult<List<ContainerLoanVo>>(orderGeneralService.queryContainer(orderId, userId));
	}

	/**
	 * 查询物流详情
	 * 
	 * @return
	 */
	@UriMapping(value = "/logistics_detail_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<LogisticsDetailVo> queryLogistics() {
		String id = super.getStringParameter("id");
		return new AjaxResult<LogisticsDetailVo>(orderGeneralService.queryLogistics(id));
	}

	/**
	 * 判断该用户是否能使用通关物流服务
	 * 
	 * @return
	 */
	@UriMapping(value = "/verify_enterprise_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<Boolean> verifyHasSomeCompany() {
		int userId = super.getLoginUserId();
		boolean flag = orderGeneralService.hasEnterprise(userId);
		if (!flag) {
			return new AjaxResult<Boolean>(AjaxResultCode.REQUEST_INVALID.getCode(),
					"对不起，您不能选择该订单类型，请联系平台客服为您配置合作物流公司、清关公司", flag);
		}
		return new AjaxResult<Boolean>(flag);
	}
}
