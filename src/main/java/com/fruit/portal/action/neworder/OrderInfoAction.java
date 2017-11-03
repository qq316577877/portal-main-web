package com.fruit.portal.action.neworder;

import com.fruit.newOrder.biz.common.*;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.request.OrderProcessRequest;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.service.neworder.BuyContractService;
import com.fruit.portal.service.neworder.OrdersInfoService;
import com.fruit.portal.vo.PageResult;
import com.fruit.portal.vo.neworder.ContainerInfoVO;
import com.fruit.portal.vo.neworder.OrderNewInfoVO;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟旋 on 2017/9/20.
 */

@Component
@UriMapping("/neworder/center")
public class OrderInfoAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoAction.class);

    @Autowired
    private OrdersInfoService ordersInfoService;

    @Autowired
    private BuyContractService buyContractService;


    /*
	 * 下单页
	 *
	 * @return
	 */
    @UriMapping(value = "/submit", interceptors = { "userLoginCheckInterceptor" })
    public AjaxResult orderSubmit() {
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            int userId = super.getLoginUserId();
            String domain = super.getDomain();
            String remoteIp = WebContext.getRequest().getRemoteHost();

            OrderNewInfoDTO request = new OrderNewInfoDTO();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            request.setUserId(userId);
            request.setUserIp(remoteIp);

            //是否需要资金服务
            boolean flag = (boolean) requesMap.get("needLoan");
            int needLoan = flag ? 1 : 0;
            request.setNeedLoan(needLoan);
            //意向发货开始时间
            String startDate = String.valueOf(requesMap.get("intentStartDate"));
            Validate.notEmpty(startDate,"请输入意向发货开始时间");
            request.setIntentStartDate(formatter.parse(startDate));
            //意向发货完成时间
            String endDate = String.valueOf(requesMap.get("intentEndDate"));
            Validate.notEmpty(endDate,"请输入意向发货完成时间");
            request.setIntentEndDate(formatter.parse(endDate));
            //下单商品类别
            int varietyId =  Integer.parseInt(String.valueOf(requesMap.get("varietyId")));
            Validate.isTrue(varietyId>0,"请选择具体商品类别");
            request.setVarietyId(varietyId);
            //下单商品数量
            int containerNum =  Integer.parseInt(String.valueOf(requesMap.get("containerNum")));
            request.setContainerNum(containerNum);
            //下单商品数量
            int goodsNum =  Integer.parseInt(String.valueOf(requesMap.get("goodsNum")));
            request.setGoodsNum(goodsNum);

            //下单方式
            int modeType =  Integer.parseInt(String.valueOf(requesMap.get("modeType")));
            Validate.isTrue(modeType > 0,"您的下单方式暂时系统不支持!");
            request.setModeType(modeType);

            //操作渠道
            int channel =  Integer.parseInt(String.valueOf(requesMap.get("channel")));
            Validate.isTrue(channel > 0,"您的下单渠道暂时系统不支持!");
            request.setChannel(channel);

            //商品明细
            List<Map<String,Object>> goodsList = (List<Map<String, Object>>) requesMap.get("goodsList");

            if(OrderModeTypeEnum.BOX.getType() == modeType){
                //明细下单判断明细
                Validate.isTrue(CollectionUtils.isNotEmpty(goodsList),"您的购买的商品不少了，我们拒绝接单!");
            }
            //默认代采
            request.setType(OrderTypeEnum.AGENCY.getType());

           return ordersInfoService.createOrder(request,goodsList,domain,super.getLoginUserOpenid());

        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/submit].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/submit].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"系统错误");
        }
    }

    /**
     * 订单列表查询 状态选项
     * @
     */
    @UriMapping(value = "/get_order_status_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult getStatusOrderFind(){


        List <Map<String,Object>> condtion = new ArrayList<Map<String,Object>>(3);

        Map<String,Object> allMap = new HashMap<String,Object>();
        allMap.put("status",SearchOrderCondtionEnum.ALL.getType());
        allMap.put("statusDesc",SearchOrderCondtionEnum.ALL.getMessage());
        condtion.add(allMap);

        Map<String,Object> submitMap = new HashMap<String,Object>();
        submitMap.put("status",SearchOrderCondtionEnum.SUBMITING.getType());
        submitMap.put("statusDesc",SearchOrderCondtionEnum.SUBMITING.getMessage());
        condtion.add(submitMap);

        Map<String,Object> signedMap = new HashMap<String,Object>();
        signedMap.put("status",SearchOrderCondtionEnum.SIGNED.getType());
        signedMap.put("statusDesc",SearchOrderCondtionEnum.SIGNED.getMessage());
        condtion.add(signedMap);


        return  new AjaxResult(condtion);
    }


    /**
     * 分页查询订单列表
     *
     * @return
     */
    @UriMapping(value = "/find_order_byPage_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult<PageResult<OrderNewInfoVO>> searchOrderByPage() {

        Map requesMap  = super.getBodyObject(HashMap.class);

        int userId = super.getLoginUserId();
        int pageSize = Integer.parseInt(String.valueOf(requesMap.get("pageSize")));
        int pageNo = Integer.parseInt(String.valueOf(requesMap.get("pageNo")));
        int status = Integer.parseInt(String.valueOf(requesMap.get("status")));



        PageResult<OrderNewInfoVO> result = new PageResult<OrderNewInfoVO>();

        if (pageSize < 1) {
            pageSize = 10;
        }

        result.setPageSize(pageSize);
        result.setPageNo(pageNo);

        result = ordersInfoService.searchOrderByPage(result,status,userId);

        return  new AjaxResult<PageResult<OrderNewInfoVO>>(result);
    }

    /**
     * 取消订单
     *
     * @return
     */
    @UriMapping(value = "/order_cancle_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult cancalOrder() {
        int userId = super.getLoginUserId();
        Map requesMap  = super.getBodyObject(HashMap.class);
        String orderNo = String.valueOf(requesMap.get("orderNo"));
        int channel = Integer.parseInt(String.valueOf(requesMap.get("channel")));

        String domain = super.getDomain();
        return ordersInfoService.cancelOrder(userId, orderNo, domain,channel);
    }

    /**
     * 订单详情
     *
     * @return
     */
    @UriMapping(value = "/order_detail_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult getDetailOrder() {

        Map requesMap  = super.getBodyObject(HashMap.class);
        String orderNo = String.valueOf(requesMap.get("orderNo"));

        return  ordersInfoService.searchOrderDetailByOrderNo(orderNo);

    }

//    /**
//     * 确认收货
//     *
//     * @return
//     */
//    @UriMapping(value = "/confirm_receive_ajax", interceptors = "userLoginCheckInterceptor")
//    public AjaxResult<Map<String, Object>> confirmReceive() {
//        int userId = super.getLoginUserId();
//        String orderId = super.getStringParameter("orderId");
//        String domain = super.getDomain();
//        return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.RECEIVE, domain,
//                OrderUpdateTypeEnum.STATUS.getType());
//    }


    @SuppressWarnings("rawtypes")
    @UriMapping(value = "/order_send_anxin_ajax", interceptors = { "userLoginCheckInterceptor" })
    public AjaxResult sendAnxinCaptcha() {
        Map requesMap  = super.getBodyObject(HashMap.class);

        String orderNo = String.valueOf(requesMap.get("orderNo"));

        return buyContractService.sendAnxinCaptcha(orderNo);
    }

    /**
     * 查看和生成合同
     *
     * @return
     */
    @UriMapping(value = "/contract/view", interceptors = "userLoginCheckInterceptor")
    public AjaxResult orderContract() {
        int userId = super.getLoginUserId();
        Map requesMap  = super.getBodyObject(HashMap.class);
        try {
        String orderNo = String.valueOf(requesMap.get("orderNo"));

        return  ordersInfoService.viewBuyContract(orderNo,userId);
        } catch (Exception e) {
            logger.info("/neworder/center/contract/view occur exception :", e);
            return new AjaxResult<Map<String, Object>>(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }

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
            OrderProcessRequest orderProcessRequest = new OrderProcessRequest();
            Map requesMap  = super.getBodyObject(HashMap.class);

            String orderNo = String.valueOf(requesMap.get("orderNo"));

            BigDecimal loanAmount = new BigDecimal(String.valueOf(requesMap.get("loanAmount")));
            String captchaCode = String.valueOf(requesMap.get("captchaCode"));

            orderProcessRequest.setUserId(userId);
            orderProcessRequest.setOrderNo(orderNo);
            orderProcessRequest.setChannel(ChannelEnum.WECHAT.getType());
            orderProcessRequest.setDomain(super.getDomain());
            orderProcessRequest.setEvent(OrderEventEnum.CONFIRM);
            orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());

            return ordersInfoService.confirmOrder(orderProcessRequest,loanAmount,captchaCode);
        } catch (Exception e) {
            logger.info("/neworder/center/confirm_submit_ajax occur exception :", e);
            return new AjaxResult<Map<String, Object>>(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 货柜列表查询 状态选项
     * @
     */
    @UriMapping(value = "/get_container_status_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult getStatusContainerFind(){


        List <Map<String,Object>> condtion = new ArrayList<Map<String,Object>>(3);

        Map<String,Object> allMap = new HashMap<String,Object>();
        allMap.put("status",SearchContainerCondtionEnum.ALL.getType());
        allMap.put("statusDesc",SearchContainerCondtionEnum.ALL.getMessage());
        condtion.add(allMap);

        Map<String,Object> submitMap = new HashMap<String,Object>();
        submitMap.put("status",SearchContainerCondtionEnum.SHIPPING.getType());
        submitMap.put("statusDesc",SearchContainerCondtionEnum.SHIPPING.getMessage());
        condtion.add(submitMap);

        Map<String,Object> signedMap = new HashMap<String,Object>();
        signedMap.put("status",SearchContainerCondtionEnum.SHIPED.getType());
        signedMap.put("statusDesc",SearchContainerCondtionEnum.SHIPED.getMessage());
        condtion.add(signedMap);


        return  new AjaxResult(condtion);
    }


    /**
     * 分页查询货柜列表
     *
     * @return
     */
    @UriMapping(value = "/find_container_byPage_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult<PageResult<ContainerInfoVO>> searchContainerByPage() {

        Map requesMap  = super.getBodyObject(HashMap.class);

        int userId = super.getLoginUserId();
        int pageSize = Integer.parseInt(String.valueOf(requesMap.get("pageSize")));
        int pageNo = Integer.parseInt(String.valueOf(requesMap.get("pageNo")));
        int status = Integer.parseInt(String.valueOf(requesMap.get("status")));



        PageResult<ContainerInfoVO> result = new PageResult<ContainerInfoVO>();

        if (pageSize < 1) {
            pageSize = 10;
        }

        result.setPageSize(pageSize);
        result.setPageNo(pageNo);

        result = ordersInfoService.searchContainerByPage(result,status,userId);

        return  new AjaxResult<PageResult<ContainerInfoVO>>(result);
    }

    /**
     * 货柜详情
     *
     * @return
     */
    @UriMapping(value = "/container_detail_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult getDetailContainer() {

        Map requesMap  = super.getBodyObject(HashMap.class);
        long containerId = Long.parseLong(String.valueOf(requesMap.get("containerId")));

        return  ordersInfoService.searchContainerDetailByContainerId(containerId);

    }


    /**
     * 查询物流详情列表
     *
     * @return
     */
    @UriMapping(value = "/logistics_detail_list_ajax")
    public AjaxResult queryLogisticsList() {
        try {

            Map requesMap  = super.getBodyObject(HashMap.class);
            long containerId = Long.parseLong(String.valueOf(requesMap.get("containerId")));
            return new AjaxResult(ordersInfoService.getLogisticsDetailHeardList(containerId));
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                logger.error("[/neworder/container/logistics_init_ajax].Exception:{}", e);
                return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
            }
            logger.error("[/neworder/container/logistics_init_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


}
