/*
 *
 * Copyright (c) 2017 by wuhan  Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.neworder;

import com.alibaba.fastjson.JSON;
import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.newOrder.biz.common.*;
import com.fruit.newOrder.biz.dto.*;
import com.fruit.newOrder.biz.request.ContainerSearchRequest;
import com.fruit.newOrder.biz.request.OrderProcessRequest;
import com.fruit.newOrder.biz.request.OrderProcessResponse;
import com.fruit.newOrder.biz.request.OrderSearchRequest;
import com.fruit.newOrder.biz.service.*;
import com.fruit.newOrder.biz.service.impl.OrderProcessService;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.service.account.DeliveryAddressService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.fruit.portal.service.wechat.WeChatBaseService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.vo.PageResult;
import com.fruit.portal.vo.account.AddressVo;
import com.fruit.portal.vo.neworder.*;
import com.fruit.portal.vo.wechat.TemplateParamVO;
import com.fruit.portal.vo.wechat.TemplateVO;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovft.contract.api.bean.*;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.IllegalStateException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Description:
 * 商品相关
 * Create Author  : ivan
 * Create Date    : 2017-09-20
 * Project        : partal-main-web
 * File Name      : OrdersInfoService.java
 */
@Service
public class OrdersInfoService
{

    private static final Logger logger = LoggerFactory.getLogger(OrdersInfoService.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    @Autowired
    private OrderNewInfoService orderNewInfoService;

    @Autowired
    private OrderGoodsInfoService orderGoodsInfoService;

    @Autowired
    private OrderProcessService newOrderProcessService;

    @Autowired
    private WeChatBaseService weChatBaseService;

    @Autowired
    private  GoodsVarietyService goodsVarietyService;

    @Autowired
    private  GoodsCommodityInfoService goodsCommodityInfoService;

    @Autowired
    private LoanAuthCreditService loanAuthCreditService;

    @Autowired
    private EnvService envService;

    @Autowired
    private EcontractService econtractService;

    @Autowired
    private ContainerInfoService containerInfoService;

    @Autowired
    private ContainerGoodsInfoService containerGoodsInfoService;

    @Autowired
    private DeliveryAddressService delieryAddressService;

    @Autowired
    private LoanSmsContactsConfigService loanSmsContactsConfigService;

    @Autowired
    private ContainerLogisticsDetailService containerLogisticsDetailService;


    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;

    private static final int REDIS_TIME_OUT = 3;

    //全部
    private static final List<Integer> ORDER_INFO_STATUS_LIST = new ArrayList<Integer>();

    static
    {
        OrderStatusEnum[] values = OrderStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (OrderStatusEnum status : values)
            {
                ORDER_INFO_STATUS_LIST.add(status.getStatus());
            }
        }
    }

    //待提交
    private static final List<Integer> ORDER_INFO_STATUS_CONFIRMED_LIST = new ArrayList<Integer>();
    static
    {
        ORDER_INFO_STATUS_CONFIRMED_LIST.add(OrderStatusEnum.SYS_CONFIRMED.getStatus());

    }

    //已生效
    private static final List<Integer> ORDER_INFO_STATUS_SIGNED_LIST = new ArrayList<Integer>();

    static
    {
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.SIGNED_CONTRACT.getStatus());
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.PREPAYMENTS.getStatus());
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.SETTLEMENTED.getStatus());
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.FINISHED.getStatus());
    }

    //全部
    private static final List<Integer> CONTAINER_INFO_STATUS_LIST = new ArrayList<Integer>();

    static
    {
        ContainerStatusEnum[] values = ContainerStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (ContainerStatusEnum status : values)
            {
                CONTAINER_INFO_STATUS_LIST.add(status.getStatus());
            }
        }
    }

    //待发货
    private static final List<Integer> CONTAINER_INFO_STATUS_CONFIRMED_LIST = new ArrayList<Integer>();
    static
    {
        CONTAINER_INFO_STATUS_CONFIRMED_LIST.add(ContainerStatusEnum.SUBMITED.getStatus());

    }

    //已发货
    private static final List<Integer> CONTAINER_INFO_STATUS_SIGNED_LIST = new ArrayList<Integer>();

    static
    {
        CONTAINER_INFO_STATUS_SIGNED_LIST.add(ContainerStatusEnum.SHIPPED.getStatus());
        CONTAINER_INFO_STATUS_SIGNED_LIST.add(ContainerStatusEnum.CLEARED.getStatus());
        CONTAINER_INFO_STATUS_SIGNED_LIST.add(ContainerStatusEnum.SETTLEMENTED.getStatus());
        CONTAINER_INFO_STATUS_SIGNED_LIST.add(ContainerStatusEnum.PLATFORM_HANDLE.getStatus());
        CONTAINER_INFO_STATUS_SIGNED_LIST.add(ContainerStatusEnum.RECEIVED.getStatus());
    }


    /**
     * 创建订单 发生异常则进行回滚
     *
     * @param orderNewInfoDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult<String> createOrder(OrderNewInfoDTO orderNewInfoDTO, List<Map<String,Object>> goodsList,String domain,String userOpenId) {

        OrderProcessRequest  orderProcessRequest = new OrderProcessRequest();


        AjaxResult result = new AjaxResult<>();

        try {


            OrderNewInfoDTO order = new OrderNewInfoDTO();

            List<OrderGoodsInfoDTO>  orderGoodsInfoList = new ArrayList<OrderGoodsInfoDTO>();

            BeanUtils.copyProperties(orderNewInfoDTO, order);
            order.setOrderSerialNo(BizUtils.getUUID());//流水号
            order.setStatus(OrderStatusEnum.SUBMITED.getStatus());
            StringBuilder uid = new StringBuilder();
            boolean flag = false;
            while (!flag) {
                uid = new StringBuilder();
                uid.append("1").append(orderNewInfoDTO.getType());
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                uid.append(sdf.format(new Date())).append((int) (Math.random() * (9999 - 1000 + 1)) + 1000);
                if (orderNewInfoService.loadByOrderNo(uid.toString()) == null) {
                    flag = true;
                }
            }
            String orderNo = uid.toString();

            order.setOrderNo(orderNo);
            order.setUpdateUser(orderNewInfoDTO.getUserId());


            // 第三步写订单表order_new_info 订单商品信息表

            if(order.getModeType() == OrderModeTypeEnum.BOX.getType()){

                for (Map<String,Object> goods : goodsList){
                    //明细下单
                    OrderGoodsInfoDTO  dto = new OrderGoodsInfoDTO();
                    dto.setOrderNo(orderNo);
                    dto.setUserId(orderNewInfoDTO.getUserId());

                    dto.setCommodityId(Integer.parseInt( String.valueOf(goods.get("commodityId"))));
                    dto.setCommodityNum(Integer.parseInt( String.valueOf(goods.get("commodityNum"))));
                    dto.setStatus(DBStatusEnum.NORMAL.getStatus());
                    dto.setUpdateUser(orderNewInfoDTO.getUserId());

                    orderGoodsInfoList.add(dto);


                }
            }


            orderProcessRequest.setUserId(order.getUserId());
            orderProcessRequest.setOrderNo(order.getOrderNo());
            orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());
            orderProcessRequest.setEvent(OrderEventEnum.SUBMIT);
            orderProcessRequest.setPlatform(order.getChannel());
            orderProcessRequest.setUserIp(order.getUserIp());
            orderProcessRequest.setDomain(domain);
            orderProcessRequest.setChannel(order.getChannel());
            orderProcessRequest.setOrderInfo(order);
            orderProcessRequest.setOrderGoodsInfoList(orderGoodsInfoList);

            OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);
            result.setMsg(orderProcessResponse.getMessage());
            if(orderProcessResponse.isSuccessful()){
                result.setCode(AjaxResultCode.OK.getCode());
            }else{
                result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            }
        } catch (Exception e) {
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    orderNewInfoDTO.getUserId(), e.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.order." + orderNewInfoDTO.getUserId(),
                    "failed to commit order " + JSON.toJSONString(orderNewInfoDTO), EventLevel.URGENT, e,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("提交订单出现异常");
            throw new RuntimeException("提交订单出现异常" + e.getMessage());
        }finally {
        }
        return result;
    }


    /**
     * 生成采购合同
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult createBuyContract(String  orderNo,int userId) {
        AjaxResult result = new AjaxResult();
        try {
            OrderNewInfoDTO order = orderNewInfoService.loadByOrderNo(orderNo);
            Map<String, Object> data = new HashMap<String, Object>();
            LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);


            if (order.getContractStatus() == ContractStatusEnum.NOT_GENERATED.getStatus()
                    || order.getContractStatus() == ContractStatusEnum.UNSIGNED.getStatus()) {
                CreateEcontractBean createEcontractBean = new CreateEcontractBean();
                createEcontractBean.setCustomerId(loanUserAuthInfo.getCustomerId());
                createEcontractBean.setProjectCode(order.getOrderSerialNo());
                createEcontractBean.setSource(getSource());
                createEcontractBean.setTemplateId(Integer.parseInt(envService.getConfig("ordercontract.template.id")));
                createEcontractBean.setParameters(buildParams(order, loanUserAuthInfo));
                ResponseVo response = econtractService.createEcontract(createEcontractBean);
                if (!response.isSuccess()) {
                    logger.info("创建采购合同失败 具体原因: {}", response.getMessage());
                    return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), response.getMessage());
                }
                Long contractId = (Long) response.getData();
                if (contractId == null || contractId < 1) {
                    logger.info("创建采购合同失败 具体原因: 强制转换出现异常");
                    return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), response.getMessage());
                }
                // 写入订单信息表
                OrderNewInfoDTO model = new OrderNewInfoDTO();
                BeanUtils.copyProperties(order, model);
                model.setContractId(contractId);
                model.setContractStatus(ContractStatusEnum.UNSIGNED.getStatus());

                orderNewInfoService.update(model);
                data.put("contractId", contractId);
            } else {
                data.put("contractId", order.getContractId());

            }
            result.setData(data);
        }catch (Exception ex){
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    orderNo, ex.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.orderConract." + orderNo,
                    "failed to commit order " + JSON.toJSONString(orderNo), EventLevel.URGENT, ex,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("创建采购合同出现异常");
            throw new RuntimeException("创建采购合同出现异常" + ex.getMessage());
        }finally {

        }
        return result;
    }
    /**
     * 查看采购合同
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult viewBuyContract(String  orderNo,int userId){
        AjaxResult result = new AjaxResult();
        try {
            OrderNewInfoDTO order = orderNewInfoService.loadByOrderNo(orderNo);
            Map<String, Object> data = new HashMap<String, Object>();

            if(order.getContractStatus() == ContractStatusEnum.NOT_GENERATED.getStatus()) {

                result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
                result.setMsg("查看采购合同失败，合同还未创建.");
                return result;
            }else{
                data.put("contractId", order.getContractId());

                ResponseVo responseVo = econtractService.queryContractUrlById(new QueryEContractBean(Long.parseLong(String.valueOf(data.get("contractId"))),getSource()));
                String contractPath = "";
                if (!responseVo.isSuccess()) {
                    logger.info("查询采购合同地址失败 具体原因: {}", responseVo.getMessage());
                }else{
                    contractPath = (String) responseVo.getData();

                    //微信端电子合同展示  需要转换一下
                    String contractUrlDomain = envService.getConfig("contract.domain");
                    String contractUrlFormatterDomain = envService.getConfig("contract.formatter.domain");
                    logger.info("转换前采购合同地址: {}", contractPath);
                    contractPath=contractPath.replace(contractUrlDomain,contractUrlFormatterDomain);
                    logger.info("转换后采购合同地址: {}", contractPath);
                }
                data.put("contractPath", contractPath);

                result.setData(data);

            }

        }catch (Exception ex){
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    orderNo, ex.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.orderConract." + orderNo,
                    "failed to commit order " + JSON.toJSONString(orderNo), EventLevel.URGENT, ex,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("查看采购合同出现异常.");
            throw new RuntimeException("查看采购合同出现异常:" + ex.getMessage());
        }finally {

        }

        return  result;

    }

    /**
     * 采购合同签署
     * @param
     * @param order
     * @param captchaCode
     * @return
     */
    public void signBuyContract(OrderNewInfoDTO order,  String captchaCode,int userId) throws Exception {

        //用户信息
        LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);

        Validate.notNull(order);

        int contractStatus = order.getContractStatus();

        final String projectCode = order.getOrderSerialNo();
        LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                LoanSmsBizTypeEnum.SUPPLIER_CONTRACT.getType());

        LoanSmsContactsConfigDTO supplerSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                LoanSmsBizTypeEnum.SUPPLIER_SERIALNO.getType());

        final int customerIdSupplier = Integer.parseInt(getCurrentConfig(loanSmsContactsConfigDTO));
        final int customerIdUser = loanUserAuthInfo.getCustomerId();
        final String supplierSerailNo = getCurrentConfig(supplerSmsContactsConfigDTO);

        final String signLocationIp  = WebContext.getRequest().getRemoteHost();

        long contractId = order.getContractId();

        if(ContractStatusEnum.NOT_GENERATED.getStatus() == contractStatus){
            // 生成合同
            AjaxResult ajaxResult = this.createBuyContract(order.getOrderNo(),userId);

            if (ajaxResult.getCode() != AjaxResultCode.OK.getCode()) {
                throw new IllegalArgumentException("创建合同失败.");
            }else{
                Map dataMap = (Map) ajaxResult.getData();
                contractId = Long.parseLong(String.valueOf(dataMap.get("contractId")));
                contractStatus = ContractStatusEnum.UNSIGNED.getStatus();
            }
        }


        if(ContractStatusEnum.UNSIGNED.getStatus() == contractStatus){
            // 短信验证
            VerifyCaptchaBean verifyCaptchaBean = new VerifyCaptchaBean();
            verifyCaptchaBean.setCustomerId(customerIdUser);
            verifyCaptchaBean.setSource(getSource());
            verifyCaptchaBean.setProjectCode(projectCode);
            verifyCaptchaBean.setCaptchCode(captchaCode);
            ResponseVo resVo = econtractService.verifyCaptcha(verifyCaptchaBean);

            if (!resVo.isSuccess()) {
              throw new IllegalArgumentException(resVo.getMessage());
            }
        }

        if (contractStatus == ContractStatusEnum.UNSIGNED.getStatus()) {
            // 客户未签名
            EcontractCaptchaSignBean custSignBean = new EcontractCaptchaSignBean();
            custSignBean.setContractId(contractId);
            custSignBean.setCustomerId(customerIdUser);
            custSignBean.setProjectCode(order.getOrderSerialNo());
            custSignBean.setSource(getSource());
            custSignBean.setCaptchCode(captchaCode);
            custSignBean.setSignature("乙方：(盖章)");
            custSignBean.setXaxisOffset(50f);
            custSignBean.setYaxisOffset(-50f);
            custSignBean.setSignLocationIp(signLocationIp);
            ResponseVo custSignResponse = econtractService.signEcontract(custSignBean);
            if (!custSignResponse.isSuccess()) {
                logger.info("合同编号为 {}的采购客户(编号为： {} )签署失败", contractId, loanUserAuthInfo.getCustomerId());
                throw new IllegalArgumentException(custSignResponse.getMessage());
            }

            contractStatus = ContractStatusEnum.SUPPLIER_NOT_SIGN.getStatus();
        }

        if (contractStatus == ContractStatusEnum.SUPPLIER_NOT_SIGN.getStatus()) {
            // 供应商未签署 创意
            SignWithoutCaptchaBean supplierSignBean = new SignWithoutCaptchaBean();
            supplierSignBean.setContractId(contractId);
            supplierSignBean.setCustomerId(customerIdSupplier);
            supplierSignBean.setProjectCode(supplierSerailNo);
            supplierSignBean.setSource(getSource());
            supplierSignBean.setSignature("甲方：(盖章)");
            supplierSignBean.setXaxisOffset(50f);
            supplierSignBean.setYaxisOffset(-50f);
            supplierSignBean.setSignLocationIp(signLocationIp);
            ResponseVo supplierSignResponse = econtractService.signEcontractWithoutCaptcha(supplierSignBean);

            if (!supplierSignResponse.isSuccess()) {
                logger.info("合同编号为 {}的采购客户(编号为： {} )签署失败", contractId, loanUserAuthInfo.getCustomerId());
                throw new IllegalArgumentException(supplierSignResponse.getMessage());
            }
            contractStatus = ContractStatusEnum.SIGNED_COMPLETED.getStatus();
        }

        order.setContractId(contractId);
        order.setContractStatus(contractStatus);
    }

    private Map<String,String> buildParams(OrderNewInfoDTO order,LoanUserAuthInfoModel loanUserAuthInfo) {

        Map<String, String> parameters = new HashMap<String, String>();

        List<OrderGoodsInfoDTO> goodsList = orderGoodsInfoService.loadListByOrderNo(order.getOrderNo());

        OrderNewInfoVO orderNewInfoV0 = transfer(order);
        List<OrderGoodsInfoVO> voList = transfer(goodsList);
        // 参数组装
        DecimalFormat amountFormat = new DecimalFormat("###,###,##0.00");
        DecimalFormat numFormat = new DecimalFormat("###,###,###");

        Calendar calendar = Calendar.getInstance();
        Date nowDate = new Date();
        calendar.setTime(nowDate);

        parameters.put("orderNo", order.getOrderNo());//编号
        parameters.put("secondParty", loanUserAuthInfo.getUsername());//乙方
        parameters.put("goodsNum", numFormat.format(order.getGoodsNum()));//商品数量
        parameters.put("totalAmount", amountFormat.format(order.getTotalAmount()));//总金额
        parameters.put("varietyName", orderNewInfoV0.getVarietyName());//果品名称

        parameters.put("firstSealYear",String.valueOf(calendar.get(Calendar.YEAR)));
        parameters.put("firstSealMonth",String.valueOf(calendar.get(Calendar.MONTH) + 1));
        parameters.put("firstSealDay",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        parameters.put("secondSealYear",String.valueOf(calendar.get(Calendar.YEAR)));
        parameters.put("secondSealMonth",String.valueOf(calendar.get(Calendar.MONTH) + 1));
        parameters.put("secondSealDay",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

        StringBuilder sb = new StringBuilder();
        int idx = 0;
        for(OrderGoodsInfoVO good :voList){
            idx++;
            sb.append("<tr>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(idx);
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(orderNewInfoV0.getVarietyName());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getCommodityName());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getCommodityNum());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getDealPrice());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getTotalAmount());
            sb.append("</p>");
            sb.append("</td>");



            sb.append("</tr>");

        }



        parameters.put("goodsList",sb.toString());//商品信息



        return parameters;
    }

    /**
     * 创建订单 发生异常则进行回滚
     *
     * @param orderProcessRequest
     * @param
     *@param loanAmount
     * @param captchaCode @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult<String> confirmOrder(OrderProcessRequest orderProcessRequest, BigDecimal loanAmount, String captchaCode) {


        AjaxResult result = new AjaxResult<>();

        try {

            String orderNo = orderProcessRequest.getOrderNo();

            Validate.isTrue(StringUtils.isNotBlank(orderNo) && !"null".equals(orderNo),"订单号不能为空");
            //订单信息处理
            OrderNewInfoDTO order  = orderNewInfoService.loadByOrderNo(orderProcessRequest.getOrderNo());
            Validate.notNull(order,"订单信息不存在");
            //合同处理
            try {
                this.signBuyContract(order, captchaCode, orderProcessRequest.getUserId());
            }catch (IllegalArgumentException lex){
                throw new IllegalStateException("您的合同签署异常，请检查输入是否正确.");
            }catch (Exception e2){
                throw new IllegalStateException("您的合同创建出现故障.请稍后再试.");
            }

            if(order.getContractStatus() != ContractStatusEnum.SIGNED_COMPLETED.getStatus()){
                return  new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"合同签署未完成");
            }


            order.setLoanAmount(loanAmount);
            order.setStatus(OrderStatusEnum.SIGNED_CONTRACT.getStatus());
            order.setUpdateUser(orderProcessRequest.getUserId());

            //请求信息
            orderProcessRequest.setPlatform(order.getChannel());
            orderProcessRequest.setUserIp(order.getUserIp());
            orderProcessRequest.setChannel(order.getChannel());
            orderProcessRequest.setOrderInfo(order);


            OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);
            result.setMsg(orderProcessResponse.getMessage());
            if(orderProcessResponse.isSuccessful()){
                result.setCode(AjaxResultCode.OK.getCode());
            }else{
                result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            }
        } catch (Exception e) {
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    orderProcessRequest.getUserId(), e.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.order." + orderProcessRequest.getUserId(),
                    "failed to commit order " + JSON.toJSONString(orderProcessRequest), EventLevel.URGENT, e,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("提交订单出现异常");
            throw new RuntimeException("提交订单出现异常:" + e.getMessage());
        }finally {
        }
        return result;
    }

    /**
     * 分页查询订单
     *
     * @param result
     * @return
     */
    public PageResult<OrderNewInfoVO> searchOrderByPage(PageResult<OrderNewInfoVO> result,int status,int userId) {

        OrderSearchRequest request = new OrderSearchRequest();
        SearchOrderCondtionEnum searchOrderCondtionEnum = SearchOrderCondtionEnum.get(status);
        List<Integer>  statusList = new ArrayList<Integer>();

        switch (searchOrderCondtionEnum){
            case ALL:
                statusList  = ORDER_INFO_STATUS_LIST;
                break;
            case SUBMITING:
                statusList  = ORDER_INFO_STATUS_CONFIRMED_LIST;
                break;
            case SIGNED:
                statusList  = ORDER_INFO_STATUS_SIGNED_LIST;
                break;
            default:
                break;
        }
        request.setStatusList(statusList);
        request.setUserId(userId);
        request.setPageNo(result.getPageNo());
        request.setPageSize(result.getPageSize());


        int count = orderNewInfoService.countByRequest(request);
        result.setTotalRecords(count);
        List<OrderNewInfoDTO> orderDtos = orderNewInfoService.loadListByRequest(request);

        List<OrderNewInfoVO> orderVoList = new ArrayList<OrderNewInfoVO>(orderDtos.size());

        for(OrderNewInfoDTO dto : orderDtos){
            OrderNewInfoVO vo = transfer(dto);

            orderVoList.add(vo);


        }
        result.setPageNo(result.getPageNo() == 0 ? 1 : result.getPageNo());
        result.setList(orderVoList);

        return result;
    }

    /**
     * 查询订单详情
     *
     * @param orderNo
     * @return
     */
    public AjaxResult searchOrderDetailByOrderNo(String orderNo) {

        OrderNewInfoDTO orderNewInfoDTO ;
        try {
            Validate.notEmpty(orderNo, "订单号不能为空.");
            orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
            Validate.notNull(orderNewInfoDTO, "您查询的订单不存在.");
        }catch (IllegalArgumentException e){
            return  new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }

        return  new AjaxResult(transfer(orderNewInfoDTO));

    }

    private OrderNewInfoVO transfer(OrderNewInfoDTO orderNewInfoDTO) {
        OrderNewInfoVO orderNewInfoVO = new OrderNewInfoVO();
        BeanUtils.copyProperties(orderNewInfoDTO,orderNewInfoVO);

        GoodsVarietyDTO goodsVarietyDTO = goodsVarietyService.loadById(orderNewInfoVO.getVarietyId());
        orderNewInfoVO.setVarietyName(goodsVarietyDTO.getName());

        orderNewInfoVO.setStatusDesc( OrderStatusEnum.get(orderNewInfoVO.getStatus()).getUserDesc());

        orderNewInfoVO.setModeTypeDesc( OrderModeTypeEnum.get(orderNewInfoVO.getModeType()).getMessage());

        orderNewInfoVO.setChannelDesc(ChannelEnum.get(orderNewInfoVO.getChannel()).getMessage());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        orderNewInfoVO.setAddTimeStr(formatter.format(orderNewInfoVO.getAddTime()));

        List<OrderGoodsInfoDTO> goodslist = orderGoodsInfoService.loadListByOrderNo(orderNewInfoVO.getOrderNo());

        orderNewInfoVO.setGoodsInfoList(transfer(goodslist));

        return  orderNewInfoVO;
    }

    /**
     * @param goodslist
     * @return
     */
    private List<OrderGoodsInfoVO> transfer(List<OrderGoodsInfoDTO> goodslist) {
        List<OrderGoodsInfoVO>  ordergoodsVOlist = new ArrayList<OrderGoodsInfoVO> ();

        for(OrderGoodsInfoDTO dto:goodslist){
            OrderGoodsInfoVO vo = new OrderGoodsInfoVO();
            BeanUtils.copyProperties(dto,vo);
            GoodsCommodityInfoDTO goodsCommodityInfoDTO = goodsCommodityInfoService.loadById(vo.getCommodityId());
            vo.setCommodityName(goodsCommodityInfoDTO.getName());

            ordergoodsVOlist.add(vo);
            goodsCommodityInfoDTO = null;
        }

        return  ordergoodsVOlist;
    }

    /**
     * 取消订单 发生异常则进行回滚
     *
     * @param userId,orderNo,domain
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult<String> cancelOrder(int userId ,String orderNo ,String domain,int channel) {

        AjaxResult result = new AjaxResult();

        OrderProcessRequest  orderProcessRequest = new OrderProcessRequest();

        //订单信息处理
        OrderNewInfoDTO order  = orderNewInfoService.loadByOrderNo(orderNo);

        orderProcessRequest.setUserId(userId);
        orderProcessRequest.setOrderNo(orderNo);
        orderProcessRequest.setDomain(domain);
        orderProcessRequest.setChannel(channel);
        orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());
        orderProcessRequest.setEvent(OrderEventEnum.USER_CANCEL);
        orderProcessRequest.setUpdateGoodsFlag(false);//不更新商品信息
        orderProcessRequest.setOrderInfo(order);

        OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);

        if(!orderProcessResponse.isSuccessful()){
            result.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
            result.setMsg(orderProcessResponse.getMessage());
        }

        return  result;
    }

    /**
     * 获取短信发送手机号
     *
     * @param config
     */
    private String getCurrentConfig(LoanSmsContactsConfigDTO config) {
        String value;
        if ("product".equals(EnvService.getEnv())) {
            value = config.getProduct();
        } else if ("beta".equals(EnvService.getEnv())) {
            value = config.getBeta();
        } else if ("dev".equals(EnvService.getEnv())) {
            value = config.getDev();
        } else {
            value = config.getAlpha();
        }
        return value;
    }


    public PageResult<ContainerInfoVO> searchContainerByPage(PageResult<ContainerInfoVO> result, int status, int userId) {

        ContainerSearchRequest request = new ContainerSearchRequest();
        SearchContainerCondtionEnum searchContainerCondtionEnum = SearchContainerCondtionEnum.get(status);
        List<Integer> statusList = new ArrayList<Integer>();

        switch (searchContainerCondtionEnum) {
            case ALL:
                statusList = CONTAINER_INFO_STATUS_LIST;
                break;
            case SHIPPING:
                statusList = CONTAINER_INFO_STATUS_CONFIRMED_LIST;
                break;
            case SHIPED:
                statusList = CONTAINER_INFO_STATUS_SIGNED_LIST;
                break;
            default:
                break;
        }
        request.setStatusList(statusList);
        request.setUserId(userId);
        request.setPageNo(result.getPageNo());
        request.setPageSize(result.getPageSize());


        int count = containerInfoService.countByRequest(request);
        result.setTotalRecords(count);

        if (count > 0) {
            List<ContainerInfoDTO> containerDtos = containerInfoService.loadListByRequest(request);

            result.setPageNo(result.getPageNo() == 0 ? 1 : result.getPageNo());
            result.setList(transferContainer(containerDtos));
        }

        return result;

    }


    private List<ContainerInfoVO> transferContainer(List<ContainerInfoDTO>  dtoList){

        List<ContainerInfoVO> containerVoList = new ArrayList<ContainerInfoVO>(dtoList.size());

        for(ContainerInfoDTO dto :dtoList){
            ContainerInfoVO vo = new ContainerInfoVO();
            BeanUtils.copyProperties(dto,vo);
            //状态描述
            vo.setStatusDesc(ContainerStatusEnum.get(vo.getStatus()).getSysDesc());
            containerVoList.add(vo);
        }

        return containerVoList;
    }


    public AjaxResult searchContainerDetailByContainerId(long containerId) {
        AjaxResult result = new AjaxResult();
        try {
            Validate.isTrue(containerId > 0, "货柜ID不能为空.");
            ContainerInfoDTO containerDto = containerInfoService.loadById(containerId);
            Validate.notNull(containerDto, "您查询的货柜不存在.");


            result.setData(transferContainer(containerDto));
        }catch (IllegalArgumentException e){
            return  new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }

        return  result;
    }

    private ContainerInfoVO transferContainer(ContainerInfoDTO dto){


        ContainerInfoVO vo = new ContainerInfoVO();
        BeanUtils.copyProperties(dto,vo);
        //状态描述
        vo.setStatusDesc(ContainerStatusEnum.get(vo.getStatus()).getSysDesc());
        //运输方式
        if(vo.getDeliveryType() == 1){
            vo.setDeliveryTypeDesc("海运");
        }else{
            vo.setDeliveryTypeDesc("陆运");
        }
        //收货地址
        if(vo.getDeliveryId() > 0){
            UserDeliveryAddressDTO userDeliveryAddressDTO = delieryAddressService.loadAddressById(vo.getDeliveryId());
            AddressVo addressVo = delieryAddressService.wrapVO(userDeliveryAddressDTO);
            vo.setDeliveryAdress(addressVo);
        }

        //商品明细

        List<ContainerGoodsInfoDTO> goodsList = containerGoodsInfoService.loadListByContainerSerialNo(vo.getContainerSerialNo());
        List<ContainerGoodsInfoVO> containerGoodsList = new ArrayList<ContainerGoodsInfoVO>(goodsList.size());
        for(ContainerGoodsInfoDTO goodDto : goodsList){
            ContainerGoodsInfoVO gVo = new ContainerGoodsInfoVO();
            BeanUtils.copyProperties(goodDto,gVo);

            GoodsCommodityInfoDTO goodsCommodityInfoDTO = goodsCommodityInfoService.loadById(goodDto.getCommodityId());
            gVo.setCommodityName(goodsCommodityInfoDTO.getName());

            GoodsVarietyDTO goodsVarietyDTO =  goodsVarietyService.loadById(goodsCommodityInfoDTO.getVarietyId());
            gVo.setVarietyName(goodsVarietyDTO.getName());
            containerGoodsList.add(gVo);
        }
        vo.setContainerGoodsList(containerGoodsList);

        return vo;
    }

    private String getSource()  {
        return  envService.getConfig("contract.source");
    }

    public List<LogisticsHeardVO> getLogisticsDetailHeardList(long containerId) {
        List<LogisticsHeardVO>  voList = new ArrayList<LogisticsHeardVO>();
        Validate.isTrue(containerId>0,"货柜不存在");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContainerInfoDTO container = containerInfoService.loadById(containerId);
        LogisticsTypeEnum[]  typeList = LogisticsTypeEnum.values();

        for (LogisticsTypeEnum typeEnum : typeList ){
            //查询所有类型的最新一条物流
            List<ContainerLogisticsDetailDTO> logisticsDetaiList = containerLogisticsDetailService.loadListByContainerSerialNo(container.getContainerSerialNo(),typeEnum.getType());
            if(CollectionUtils.isNotEmpty(logisticsDetaiList)){
                //最新一条
                ContainerLogisticsDetailDTO dto = logisticsDetaiList.get(0);

                LogisticsHeardVO vo= new LogisticsHeardVO();
                BeanUtils.copyProperties(dto, vo);
                vo.setLogisticsId(dto.getId());
                vo.setContainerId(container.getId());
                vo.setType(dto.getType());
                vo.setTypeDesc(LogisticsTypeEnum.get(dto.getType()).getTypeDesc());
                vo.setAddTimeStr(sdf.format(dto.getAddTime()));

                voList.add(vo);
            }

        }


        return voList;
    }


}
