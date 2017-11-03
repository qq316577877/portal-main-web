package com.fruit.portal.action.order;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.common.OrderUpdateTypeEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.request.OrderSearchRequest;
import com.fruit.order.biz.service.OrderService;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.service.econtract.DebtContractService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.fruit.portal.service.order.OrderGeneralService;
import com.fruit.portal.service.order.OrderProcessService;
import com.fruit.portal.vo.PageResult;
import com.fruit.portal.vo.order.OrderDetailVo;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.bean.VerifyCaptchaBean;
import com.ovft.contract.api.service.EcontractService;

@Component
@UriMapping("/order/center")
public class OrderCenterAction extends BaseAction {

	@Autowired
	private OrderGeneralService orderGeneralService;

	@Autowired
	private OrderProcessService orderProcessService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private DebtContractService debtContractService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private LoanAuthCreditService loanAuthCreditService;

	@Autowired
	private EcontractService econtractService;

	private static final Logger logger = LoggerFactory.getLogger(OrderCenterAction.class);

	/*
	 * 跳转下单页
	 * 
	 * @return
	 */
	@UriMapping(value = "/list", interceptors = { "userLoginCheckInterceptor", "userNavigatorInterceptor" })
	public String toOrderList() {
		try {
			return "/order/order_list";
		} catch (IllegalArgumentException e) {
			logger.error("[/order/center/list].Exception:{}", e);
			return FTL_ERROR_400;
		}
	}

	/**
	 * 分页查询订单列表
	 * 
	 * @return
	 */
	@UriMapping(value = "/find_order_byPage_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<PageResult<OrderDetailVo>> searchOrderByPage() {
		String str = super.getBodyString();
		OrderSearchRequest request = JSON.parseObject(str, OrderSearchRequest.class);
		int userId = super.getLoginUserId();
		request.setUserId(userId);
		return new AjaxResult<PageResult<OrderDetailVo>>(orderGeneralService.searchOrderByPage(request));
	}

	/**
	 * 取消订单
	 * 
	 * @return
	 */
	@UriMapping(value = "/order_cancle_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<Map<String, Object>> cancalOrder() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.USER_CANCEL, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 确认收货
	 * 
	 * @return
	 */
	@UriMapping(value = "/confirm_receive_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult<Map<String, Object>> confirmReceive() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.RECEIVE, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/*
	 * 确认提交订单
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@UriMapping(value = "/confirm_submit_ajax", interceptors = "userLoginCheckInterceptor")
	public AjaxResult confirmSubmit() {
		try {
			int userId = super.getLoginUserId();
			String orderId = super.getStringParameter("orderId");
			int needLoan = super.getIntParameter("needLoan", 0);
			String domain = super.getDomain();
			String captchaCode = super.getStringParameter("captchaCode");
			if (needLoan == 1) {
				AjaxResult ajaxResult = new AjaxResult();
				OrderDTO order = orderService.loadByNo(orderId);
				LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(order.getUserId());
				String source = envService.getConfig("contract.source");
				ResponseVo response = econtractService.verifyCaptcha(new VerifyCaptchaBean(loanUserAuthInfo.getCustomerId(),
						order.getTransactionNo(), captchaCode,source));
				if (!response.isSuccess()) {
					String errorMessage = response.getMessage();
					logger.info("合同服务校验短信失败 具体原因: {}", errorMessage);
					if(errorMessage.contains("短信验证码不正确") || errorMessage.contains("60030501")){
						ajaxResult.setCode(60030501);
						ajaxResult.setMsg("短信验证码不正确！");
					}else{
						ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
						ajaxResult.setMsg("合同服务授权失败！");
					}
					return ajaxResult;
				}
			}
			return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.CONFIRM, domain,
					OrderUpdateTypeEnum.STATUS.getType());
		} catch (Exception e) {
			logger.info("/order/center/confirm_submit_ajax occur exception :", e);
			return new AjaxResult<Map<String, Object>>(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}

	@UriMapping(value = "/importOrder", interceptors = { "userLoginCheckInterceptor", "userNavigatorInterceptor" })
	public String showMemberLevel() {
		try {
			return "order/import_order";
		} catch (IllegalArgumentException e) {
			logger.error("[/order/center/importOrder].Exception:{}", e);
			return FTL_ERROR_400;
		}
	}
}
