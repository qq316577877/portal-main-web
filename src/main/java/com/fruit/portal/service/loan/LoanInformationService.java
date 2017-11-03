/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.fruit.loan.biz.common.RepaymentMethodsTypeEnum;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.loan.biz.socket.util.MathUtil;
import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.utils.NumberUtil;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.request.LoanInfoQueryRequestModel;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.OrderService;
import com.fruit.portal.model.loan.LoanInfoModel;
import com.fruit.portal.model.loan.LoanInfoQueryRequest;
import com.fruit.portal.service.common.RuntimeConfigurationService;
import com.fruit.portal.service.order.OrderGeneralService;
import com.fruit.portal.vo.PageResult;
import com.fruit.portal.vo.order.ContainerInfoForLoan;


/**
 * Description:
 * 用户资金服务相关
 * Create Author  : paul
 * Create Date    : 2017-06-13
 * Project        : partal-main-web
 * File Name      : LoanInformationService.java
 */
@Service
public class LoanInformationService
{
    @Autowired
    private LoanInfoService loanInfoService;


    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EnvService envService;


    @Autowired
    private OrderGeneralService orderGeneralService ;

    @Autowired
    private ContainerTmpService containerTmpService ;

    @Autowired
    private LoanUserCreditInfoService loanUserCreditInfoService ;

    @Autowired
    private EcontractService econtractService;

    @Autowired
    private OrderNewInfoService orderNewInfoService;

    @Autowired
    private ContainerInfoService containerInfoService;


    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static final Logger logger = LoggerFactory.getLogger(LoanInformationService.class);


    /**
     * 分页查询申请列表
     * @param queryRequest
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageResult<LoanInfoModel> searchloanInfoApplyByPage(LoanInfoQueryRequest queryRequest, int pageNo, int pageSize) {
        PageResult<LoanInfoModel> result = new PageResult<LoanInfoModel>();

        LoanInfoQueryRequestModel request = this.transfer(queryRequest);

        List<LoanInfoModel> list = loanInfoApplyList(request,pageNo,pageSize);
        int count = loanInfoService.countByExample(request);

        result.setList(list);
        result.setTotalRecords(count);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        return result;
    }


    /**
     * 查询申请列表
     * @param queryRequest
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<LoanInfoModel> loanInfoApplyList(LoanInfoQueryRequestModel queryRequest, int pageNo, int pageSize)
    {

        List<LoanInfoModel> result = Collections.emptyList();

        List<LoanInfoDTO> loanInfoDTOs = loanInfoService.searchByKeyword(queryRequest, pageNo, pageSize);

        if (CollectionUtils.isNotEmpty(loanInfoDTOs))
        {
            List<LoanInfoModel> loanInfoList = this.getLoanInfoModelLists(loanInfoDTOs);
            result = loanInfoList;

        }

        return result;
    }



    /**
     * 分页查询服务列表
     * @param queryRequest
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageResult<LoanInfoModel> searchloanInfoServiceByPage(LoanInfoQueryRequest queryRequest, int pageNo, int pageSize) {
        PageResult<LoanInfoModel> result = new PageResult<LoanInfoModel>();

        LoanInfoQueryRequestModel request = this.transfer(queryRequest);

        List<LoanInfoModel> list = loanInfoServiceList(request,pageNo,pageSize);
        int count = loanInfoService.countByExample(request);

        result.setList(list);
        result.setTotalRecords(count);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        return result;
    }


    /**
     * 查询服务列表
     * @param queryRequest
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<LoanInfoModel> loanInfoServiceList(LoanInfoQueryRequestModel queryRequest, int pageNo, int pageSize)
    {

        List<LoanInfoModel> result = Collections.emptyList();

        List<LoanInfoDTO> loanInfoDTOs = loanInfoService.searchByKeyword(queryRequest, pageNo, pageSize);

        if (CollectionUtils.isNotEmpty(loanInfoDTOs))
        {
            List<LoanInfoModel> loanInfoList = this.getLoanInfoModelLists(loanInfoDTOs);
            result = loanInfoList;
        }

        return result;
    }


    private LoanInfoQueryRequestModel transfer(LoanInfoQueryRequest queryRequest)
    {
        LoanInfoQueryRequestModel request = new LoanInfoQueryRequestModel();
        try
        {
            org.apache.commons.beanutils.BeanUtils.copyProperties(request, queryRequest);

            String keyword = request.getKeyword();
            List<String> includeTransactionNoList = Collections.EMPTY_LIST;
            if (StringUtils.isNotBlank(keyword))
            {
                includeTransactionNoList = this.searchTransactionNoList(keyword);
            }
            request.setIncludeTransactionNoList(includeTransactionNoList);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return request;
    }


    /**
     * 通过keyword查询货柜号list
     * @param keyword
     * @return
     */
    public List<String> searchTransactionNoList(String keyword)
    {

        return containerTmpService.searchTransactionNoList(keyword);
    }




    private List<LoanInfoModel> getLoanInfoModelLists(List<LoanInfoDTO> loanInfoDTOs)
    {
        List<FutureTask<LoanInfoModel>> tasks = new ArrayList<FutureTask<LoanInfoModel>>(loanInfoDTOs.size());
        List<LoanInfoModel> loanInfoModelList = new ArrayList<LoanInfoModel>(loanInfoDTOs.size());
        if (CollectionUtils.isNotEmpty(loanInfoDTOs))
        {
            for (LoanInfoDTO user : loanInfoDTOs)
            {
                FutureTask<LoanInfoModel> futureTask = new FutureTask(new LoanInfoModelCallable(user));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<LoanInfoModel> task : tasks)
            {
                try
                {
                    LoanInfoModel loanInfoModel = task.get();
                    loanInfoModelList.add(loanInfoModel);
                }
                catch (InterruptedException e)
                {
                    //
                }
                catch (ExecutionException e)
                {
                    //
                }
            }
        }
        return loanInfoModelList;
    }



    class LoanInfoModelCallable implements Callable<LoanInfoModel>
    {
        private LoanInfoDTO loanInfo;

        public LoanInfoModelCallable(LoanInfoDTO loanInfo)
        {
            this.loanInfo = loanInfo;
        }

        @Override
        public LoanInfoModel call() throws Exception
        {
            try {
                return loadLoanInfoModel(loanInfo, false,true);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }




    /**
     * 加载资金服务信息，后一个参数为是否加载订单信息,是否加载货柜信息
     * @param loanInfoDTO
     * @param loadOrder
     * @param loadOrderContainer
     * @return
     */
    protected LoanInfoModel loadLoanInfoModel(LoanInfoDTO loanInfoDTO, boolean loadOrder,boolean loadOrderContainer)
    {
        LoanInfoModel loanInfoModel = null;
        if (loanInfoDTO != null)
        {
            loanInfoModel = new LoanInfoModel();
            BeanUtils.copyProperties(loanInfoDTO, loanInfoModel);

            // 获取合同ID 查询合同URL
            Long contractId =loanInfoDTO.getContractId();
            //dubbo接口查询合同URL地址
            String contractUrl = "";
            if(contractId > 0){
                String source = envService.getConfig("contract.source");
                ResponseVo response = econtractService.queryContractUrlById(new QueryEContractBean(contractId,source));//
                if(response.isSuccess()){
                    contractUrl = (String)response.getData();
                }
            }

            loanInfoModel.setContractUrl(contractUrl);

            LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(loanInfoDTO.getUserId());
            if(loanUserCreditInfoDTO!=null){
                loanInfoModel.setName(loanUserCreditInfoDTO.getUsername());//申请人姓名
            }


            LoanInfoStatusEnum statusEnum = LoanInfoStatusEnum.get(loanInfoDTO.getStatus());
            if (null != statusEnum)
            {
                //如果是保证金还款成功 或者 保证金还款失败 ，则用户端显示的都是还款失败
                if(statusEnum.getStatus()==LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus()
                        || statusEnum.getStatus()==LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus() ){
                    loanInfoModel.setStatusDesc(LoanInfoStatusEnum.REPAYMENT_FAILURE.getMessage());
                }else{
                    loanInfoModel.setStatusDesc(statusEnum.getMessage());
                }

                //如果是待还款状态，借据到期日赋值=强制还款时间
                if(statusEnum.getStatus()==LoanInfoStatusEnum.REPAYMENT.getStatus()){
                    loanInfoModel.setDbtExpDt(loanInfoDTO.getExpiresTime());
                }
            }

            if(loanInfoDTO.getStatus()==LoanInfoStatusEnum.REPAYMENTS.getStatus()){
                BigDecimal repaymentAmount = loanInfoDTO.getRepaymentAmount();//实还总额
                BigDecimal offerLoan =  loanInfoDTO.getOfferLoan();//实际放款金额
                BigDecimal repaymentInterest =  repaymentAmount.subtract(offerLoan);//利息
                loanInfoModel.setRepaymentInterest(repaymentInterest);
            }else{
                loanInfoModel.setRepaymentInterest(new BigDecimal("0.00"));
            }

            //还款方式
            int method = Integer.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL,BaseRuntimeConfig.REPAYMENT_METHODS));
            loanInfoModel.setPayMethod(RepaymentMethodsTypeEnum.get(method).getMessage());

            double percent = Double.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL,BaseRuntimeConfig.PERFORMANCE_RATE));
            String percentStr = NumberUtil.getPercentFormat(percent,2);
            loanInfoModel.setPerformanceRate(percentStr);

            if (loadOrder)
            {
                loadOrderInfo(loanInfoDTO, loanInfoModel);
            }else{
                loanInfoModel.setOrderStatus(0);
                loanInfoModel.setOrderStatusDesc("");
            }

            if(loadOrderContainer){
                loadOrderContainerInfo(loanInfoDTO, loanInfoModel);
            }else {
                loanInfoModel.setContainerNo("");//要展示的货柜编号

                //货柜状态
                loanInfoModel.setContainerStatus(0);
                //货柜状态中文
                loanInfoModel.setContainerStatusDesc("");

                //发货时间
                loanInfoModel.setDeliveryTime(null);

                //预计到货时间
                loanInfoModel.setPreReceiveTime(null);
            }

        }
        return loanInfoModel;
    }

    /**
     * 加载资金服务里订单的订单信息
     * @param loanInfoDTO
     * @param loanInfoModel
     */
    private void loadOrderNewInfo(LoanInfoDTO loanInfoDTO, LoanInfoModel loanInfoModel)
    {
        //根据资金服务里的订单编号查询订单信息
        OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(loanInfoDTO.getOrderNo());

        if (orderNewInfoDTO != null)
        {
            loanInfoModel.setOrderStatus(orderNewInfoDTO.getStatus());
            loanInfoModel.setOrderStatusDesc(com.fruit.newOrder.biz.common.OrderStatusEnum.get(orderNewInfoDTO.getStatus()).getUserDesc());
        }
    }


    /**
     * 给予资金服务中货柜的货柜信息
     * @param loanInfoDTO
     * @param loanInfoModel
     */
    private void loadOrderContainerNewInfo(LoanInfoDTO loanInfoDTO, LoanInfoModel loanInfoModel)
    {
        //根据资金服务里的货柜编号查询货柜信息
        ContainerInfoDTO containerInfoDTO = this.containerInfoService.loadByContainerSerialNo(loanInfoDTO.getTransactionNo());


        if (containerInfoDTO != null)
        {

            loanInfoModel.setContainerNo(containerInfoDTO.getContainerNo());//要展示的货柜编号

            //货柜状态
            loanInfoModel.setContainerStatus(containerInfoDTO.getStatus());
            //货柜状态中文
            loanInfoModel.setContainerStatusDesc(com.fruit.newOrder.biz.common.ContainerStatusEnum.get(containerInfoDTO.getStatus()).getUserDesc());

            //发货时间
            loanInfoModel.setDeliveryTime(containerInfoDTO.getDeliveryTime());

            //预计到货时间
            loanInfoModel.setPreReceiveTime(containerInfoDTO.getPreReceiveTime());

        }
    }

    /**
     * 加载资金服务里订单的订单信息
     * @param loanInfoDTO
     * @param loanInfoModel
     */
    private void loadOrderInfo(LoanInfoDTO loanInfoDTO, LoanInfoModel loanInfoModel)
    {
        //根据资金服务里的订单编号查询订单信息
        OrderDTO orderDTO = this.orderService.loadByNo(loanInfoDTO.getOrderNo());

         if (orderDTO != null)
        {
            loanInfoModel.setOrderStatus(orderDTO.getStatus());
            loanInfoModel.setOrderStatusDesc(OrderStatusEnum.get(orderDTO.getStatus()).getUserDesc());
        }else{
             loadOrderNewInfo(loanInfoDTO,loanInfoModel);
         }
    }


    /**
     * 给予资金服务中货柜的货柜信息
     * @param loanInfoDTO
     * @param loanInfoModel
     */
    private void loadOrderContainerInfo(LoanInfoDTO loanInfoDTO, LoanInfoModel loanInfoModel)
    {
        //根据资金服务里的货柜编号查询货柜信息
        try {

            //新订单货柜信息加载
            loadOrderContainerNewInfo(loanInfoDTO,loanInfoModel);
            //如果加载失败

            if(StringUtils.isBlank(loanInfoModel.getContainerNo())){
                ContainerInfoForLoan containerInfoForLoan = this.orderGeneralService.queryContainerInfo(loanInfoDTO.getOrderNo(), loanInfoDTO.getTransactionNo());
                if (containerInfoForLoan != null && StringUtils.isNotBlank(containerInfoForLoan.getNo())) {

                    loanInfoModel.setContainerNo(containerInfoForLoan.getNo());//要展示的货柜编号

                    //货柜状态
                    loanInfoModel.setContainerStatus(containerInfoForLoan.getStatus());
                    //货柜状态中文
                    loanInfoModel.setContainerStatusDesc(ContainerStatusEnum.getStatusDesc(containerInfoForLoan.getStatus()));

                    //发货时间
                    loanInfoModel.setDeliveryTime(containerInfoForLoan.getDeliveryTime());

                    //预计到货时间
                    loanInfoModel.setPreReceiveTime(containerInfoForLoan.getPreReceiveTime());

                }
            }

        }catch (Exception ex){
            logger.error("加载货柜信息异常."+loanInfoDTO.getOrderNo());
        }
    }





    /**
     * 查询详情
     * @param id
     * @return
     */
    public LoanInfoModel loadloanInfoById(long id)
    {
        LoanInfoDTO loanInfoDTO = loanInfoService.loadById(id);

        LoanInfoModel loanInfoModel = null;

        if (loanInfoDTO!=null)
        {
            loanInfoModel = loadLoanInfoModel(loanInfoDTO,true,true);
        }

        return loanInfoModel;
    }




    /**
     * 创建资金服务
     * @param loanInfoDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public long createLoanInfo(LoanInfoDTO loanInfoDTO)
    {

        //最低贷款金额
        String value = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL,BaseRuntimeConfig.MINIMUM_LOAN_AMOUNT);

        BigDecimal minimumLoanAmount = MathUtil.stringToBigDecima(value);

        long id = loanInfoService.create(loanInfoDTO,minimumLoanAmount);

        return id;
    }






    /**
     * 创建资金服务
     * @param loanInfoDTOs
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createLoanInfos(List<LoanInfoDTO> loanInfoDTOs,int userId)
    {
        //最低贷款金额
        String value = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL,BaseRuntimeConfig.MINIMUM_LOAN_AMOUNT);

        BigDecimal minimumLoanAmount = MathUtil.stringToBigDecima(value);

        loanInfoService.createLoanInfos(loanInfoDTOs,userId,minimumLoanAmount);

    }


    /**
     * 查询资金服务，通过TransactionNoList
     * @param transactionNoList
     * @return
     */
    public List<LoanInfoModel> loadLoanInfosByTransactionNoList(List<String> transactionNoList)
    {
        List<LoanInfoModel> loanInfoModelList = null;
        List<LoanInfoDTO> loanInfoDTOs =  loanInfoService.loadByTransactionNoList(transactionNoList);

        if (CollectionUtils.isNotEmpty(loanInfoDTOs))
        {
            loanInfoModelList = new ArrayList<LoanInfoModel>(loanInfoDTOs.size());
            for (LoanInfoDTO loanInfoDTO : loanInfoDTOs)
            {

                LoanInfoModel loadInfoModel = this.loadLoanInfoModel(loanInfoDTO,false,true);

                loanInfoModelList.add(loadInfoModel);
            }
        }

        return loanInfoModelList;
    }

    /**
     * 查询资金服务，通过TransactionNoList
     * @param transactionNo
     * @return
     */
    public LoanInfoModel loadLoanInfosByTransactionNo(String transactionNo)
    {
        LoanInfoModel loanInfoModel = null;
        LoanInfoDTO loanInfoDTO =  loanInfoService.loadByTransactionNo(transactionNo);

        if (null != loanInfoDTO)
        {
        	loanInfoModel = this.loadLoanInfoModel(loanInfoDTO,false,false);
        }

        return loanInfoModel;
    }
}
