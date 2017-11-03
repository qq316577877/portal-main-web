package com.fruit.portal.service.neworder.process;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.newOrder.biz.common.ContainerStatusEnum;
import com.fruit.newOrder.biz.common.EventRoleEnum;
import com.fruit.newOrder.biz.common.OrderEventEnum;
import com.fruit.newOrder.biz.common.OrderStatusEnum;
import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.dto.GoodsCategoryDTO;
import com.fruit.newOrder.biz.dto.GoodsVarietyDTO;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.request.OrderProcessRequest;
import com.fruit.newOrder.biz.request.OrderProcessResponse;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.newOrder.biz.service.GoodsCategoryService;
import com.fruit.newOrder.biz.service.GoodsVarietyService;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.newOrder.biz.service.impl.DefaultOrderProcessor;
import com.fruit.newOrder.biz.service.impl.OrderStateMachine;
import com.fruit.portal.service.common.MessageService;
import com.fruit.portal.service.common.RuntimeConfigurationService;
import com.fruit.portal.service.wechat.WeChatBaseService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.WechatConstants;
import com.fruit.portal.vo.wechat.TemplateParamVO;
import com.fruit.portal.vo.wechat.TemplateVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * 取消订单
 */
@Service
public class confirmProcessor extends DefaultOrderProcessor {



	public confirmProcessor() {
		super(OrderEventEnum.CONFIRM);
	}

	@Autowired
	private OrderNewInfoService orderNewInfoService;

	@Autowired
	private ContainerInfoService containerInfoService;

	@Autowired
	private GoodsVarietyService goodsVarietyService;


	@Autowired
	private OrderStateMachine newOrderStateMachine;


	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RuntimeConfigurationService runtimeConfigurationService;

	@Autowired
	private WeChatBaseService weChatBaseService;

	@Autowired
	private UserEnterpriseService userEnterpriseService;


	@Override
	protected OrderProcessResponse handleExtraBefore(OrderProcessRequest request) {

		OrderProcessResponse orderProcessResponse = new OrderProcessResponse(true,"","");

		int userId = request.getUserId();//用户ID
		String orderNo = request.getOrderNo();//订单号
		OrderEventEnum event = request.getEvent();//订单事件
		EventRoleEnum role = event.getRole(); //事件所属角色


		OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
		if(orderNewInfoDTO == null ){
			orderProcessResponse.setMessage("订单不存在");
			orderProcessResponse.setSuccessful(false);
			return orderProcessResponse;
		}

		if(orderNewInfoDTO.getUserId() != userId){
			orderProcessResponse.setMessage("不能操作他人订单");
			orderProcessResponse.setSuccessful(false);
			return orderProcessResponse;
		}

		//设置原状态
		request.setStatusBefore(orderNewInfoDTO.getStatus());

		GoodsVarietyDTO goodsVarietyDTO = goodsVarietyService.loadById(orderNewInfoDTO.getVarietyId());

		//创建货柜
		for(int i = 0;i < orderNewInfoDTO.getContainerNum() ; i ++){
			ContainerInfoDTO model = new ContainerInfoDTO();
			model.setOrderNo(orderNewInfoDTO.getOrderNo());
			model.setUserid(orderNewInfoDTO.getUserId());
			model.setContainerNo("货柜"+(i+1));//默认的货柜批次号
			model.setContainerSerialNo(BizUtils.getUUID());
			model.setStatus(ContainerStatusEnum.UNSUBMIT.getStatus());
			model.setDeliveryType(1);//默认海运
			model.setUpdateUser(request.getUserId());
			model.setAddTime(new Date());
			model.setContainerName(goodsVarietyDTO.getName());

			containerInfoService.create(model);
		}


		return orderProcessResponse;
	}



	@Override
	protected void  handleExtraAfter(OrderProcessRequest request, OrderStatusEnum nextStatus){

		OrderNewInfoDTO orderNewInfoDTO = request.getOrderInfo();

		final String content = newOrderStateMachine.getSmsTemplate(request.getStatusBefore(), OrderEventEnum.CONFIRM)
				.replace("{orderNo}", orderNewInfoDTO.getOrderNo());

		final int userIdSms = orderNewInfoDTO.getUserId();
		UserAccountDTO userInfo = userAccountService.loadById(userIdSms);
		Validate.notNull(userInfo, "用户信息不存在");
		UserEnterpriseDTO userEnterpriseDTO = userEnterpriseService.loadByUserId(userInfo.getId());
		final String mobile = userInfo.getMobile();


		messageService.sendSms(userIdSms, mobile, content);

		//微信提醒
		String openid = userInfo.getOpenid();
		if(StringUtils.isNotBlank(openid)) {
			TemplateVO template = new TemplateVO();
			template.setToUser(openid);
			String templateId = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.template_2);
			String urlEnter = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.enter2);
			template.setTemplateId(templateId);
			template.setUrl(urlEnter);

			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			java.util.List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
			dataListP.add(new TemplateParamVO("first", "尊敬的客户，您的订单合同已签订，请按合同约定日期支付采购预付款.", "#000000"));
			dataListP.add(new TemplateParamVO("keyword1", orderNewInfoDTO.getOrderNo(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword2", userEnterpriseDTO.getName(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword3", format.format(new Date()), "#000000"));
			dataListP.add(new TemplateParamVO("remark", "如有疑问，请致电4008-265-128。", "#000000"));

			template.setTemplateParamList(dataListP);

			weChatBaseService.sendMessage(template);
		}

	}


}
