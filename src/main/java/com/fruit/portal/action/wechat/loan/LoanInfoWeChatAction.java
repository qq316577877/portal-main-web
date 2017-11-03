package com.fruit.portal.action.wechat.loan;


import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanInfoModel;
import com.fruit.portal.model.loan.LoanInfoQueryRequest;
import com.fruit.portal.service.common.RuntimeConfigurationService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.fruit.portal.service.loan.LoanInformationService;
import com.fruit.portal.vo.PageResult;
import com.fruit.portal.vo.common.IdValueVO;
import com.fruit.portal.vo.loan.LoanInfoVo;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资金服务详情相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-06-13
 * Project        : portal
 */
@Component
@UriMapping("/wechat/loan/info")
public class LoanInfoWeChatAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanInfoWeChatAction.class);

    @Autowired
    private LoanInformationService loanInformationService;

    @Autowired
    private LoanAuthCreditService loanAuthCreditService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    private static final List<IdValueVO> LOAN_INFO_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        LOAN_INFO_STATUS_LIST.add(new IdValueVO(-1, "全部"));
        LoanInfoStatusEnum[] values = LoanInfoStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanInfoStatusEnum status : values)
            {
                LOAN_INFO_STATUS_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
            }
        }
    }

    //只需要 全部、待还款、已还款
    private static final List<IdValueVO> LOAN_INFO_STATUS_SERVICE_LIST = new ArrayList<IdValueVO>();

    static
    {
        LOAN_INFO_STATUS_SERVICE_LIST.add(new IdValueVO(-1, "全部"));
        LoanInfoStatusEnum[] values = LoanInfoStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanInfoStatusEnum status : values)
            {
                if(status.getStatus()==LoanInfoStatusEnum.REPAYMENT.getStatus()
                        || status.getStatus()==LoanInfoStatusEnum.REPAYMENTS.getStatus()
                        ){
                    LOAN_INFO_STATUS_SERVICE_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
                }
            }
        }
    }

    //只需要 全部、待审核、待放款、已放款
    private static final List<IdValueVO> LOAN_INFO_STATUS_APPLY_LIST = new ArrayList<IdValueVO>();

    static
    {
        LOAN_INFO_STATUS_APPLY_LIST.add(new IdValueVO(-1, "全部"));
        LoanInfoStatusEnum[] values = LoanInfoStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanInfoStatusEnum status : values)
            {
                if(status.getStatus()==LoanInfoStatusEnum.PENDING_AUDIT.getStatus()
                        || status.getStatus()==LoanInfoStatusEnum.LOAN_PENDING.getStatus()
                        || status.getStatus()==LoanInfoStatusEnum.SECURED_LOAN.getStatus()
                        ){
                    LOAN_INFO_STATUS_APPLY_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
                }
            }
        }
    }



    /**
     * 查询我的资金服务列表--所需状态所有list
     * @return
     */
    @UriMapping(value = "/get_loan_info_status_list_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getLoanInfoStatusList()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            dataMap.put("statusList", LOAN_INFO_STATUS_LIST);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/info/get_loan_info_status_list_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询我的资金服务列表--申请列表所需的status list
     * @return
     */
    @UriMapping(value = "/get_loan_info_status_apply_list_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getLoanInfoStatusApplyList()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            dataMap.put("statusList", LOAN_INFO_STATUS_APPLY_LIST);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/info/get_loan_info_status_apply_list_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询我的资金服务列表--服务列表所需的status list
     * @return
     */
    @UriMapping(value = "/get_loan_info_status_service_list_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getLoanInfoStatusServiceList()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            dataMap.put("statusList", LOAN_INFO_STATUS_SERVICE_LIST);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/info/get_loan_info_status_service_list_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     *  申请列表数据
      * @return
     */
    @UriMapping(value = "/get_loan_info_list_apply_ajax", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor", "validationInterceptor"})
    public AjaxResult getLoanInfoApplyList()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        Map requesMap  = super.getBodyObject(HashMap.class);
        int pageNo = Integer.parseInt(String.valueOf( requesMap.get("pageIndex")));
        int pageSize = Integer.parseInt(String.valueOf( requesMap.get("pageSize")));
        String keyword = (String) requesMap.get("keyword");
        int status = Integer.parseInt(String.valueOf( requesMap.get("status")));//默认-1，如果是-1，则为空

        int userId = getLoginUserId();
        try
        {
            //如果是-1则为空
            LoanInfoStatusEnum statusEnum = -1 == status ? null : LoanInfoStatusEnum.get(status);

            LoanInfoQueryRequest queryRequest = new LoanInfoQueryRequest();
            queryRequest.setKeyword(keyword);
            queryRequest.setUserId(userId);
            if(status<0){
                List<Integer> statusList = new ArrayList<Integer>();
                statusList.add(LoanInfoStatusEnum.PENDING_AUDIT.getStatus());
                statusList.add(LoanInfoStatusEnum.LOAN_PENDING.getStatus());
                statusList.add(LoanInfoStatusEnum.SECURED_LOAN.getStatus());
                statusList.add(LoanInfoStatusEnum.NOT_THROUGH.getStatus());
                statusList.add(LoanInfoStatusEnum.TO_CONFIRMED.getStatus());

                queryRequest.setStatusList(statusList);
            }else{
                queryRequest.setStatus(statusEnum);
            }

            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(true);

            PageResult<LoanInfoModel> loanInfoModelListPage = loanInformationService.searchloanInfoApplyByPage(queryRequest, pageNo, pageSize);

//            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(loanInfoModelListPage);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/lopan/info/get_loan_info_list_apply_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     *  服务列表数据
     * @return
     */
    @UriMapping(value = "/get_loan_info_list_service_ajax", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor", "validationInterceptor"})
    public AjaxResult getLoanInfoServiceList()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        Map<String,Object> requesMap  = super.getBodyObject(HashMap.class);
        int pageNo = Integer.parseInt(String.valueOf( requesMap.get("pageIndex")));
        int pageSize = Integer.parseInt(String.valueOf( requesMap.get("pageSize")));
        String keyword = (String) requesMap.get("keyword");
        int status = Integer.parseInt(String.valueOf( requesMap.get("status")));//默认-1，如果是-1，则为空

        int userId = getLoginUserId();
        try
        {
            Map<String,Object> dataMap  = new HashMap<String, Object>();
            //如果是-1则为空
            LoanInfoStatusEnum statusEnum = -1 == status ? null : LoanInfoStatusEnum.get(status);

            LoanInfoQueryRequest queryRequest = new LoanInfoQueryRequest();
            queryRequest.setKeyword(keyword);
            if(status<0){
                List<Integer> statusList = new ArrayList<Integer>();
                statusList.add(LoanInfoStatusEnum.SECURED_LOAN.getStatus());
                statusList.add(LoanInfoStatusEnum.REPAYMENT.getStatus());
                statusList.add(LoanInfoStatusEnum.REPAYMENTS.getStatus());
                statusList.add(LoanInfoStatusEnum.REPAYMENT_FAILURE.getStatus());
                statusList.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus());
                statusList.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus());
                statusList.add(LoanInfoStatusEnum.RECONCILIATION_CONFIRMED.getStatus());
                queryRequest.setStatusList(statusList);
            }else{
                queryRequest.setStatus(statusEnum);
            }
            queryRequest.setUserId(userId);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(true);

            PageResult<LoanInfoModel> loanInfoModelListPage = loanInformationService.searchloanInfoServiceByPage(queryRequest, pageNo, pageSize);

            dataMap.put("loanListPage",loanInfoModelListPage);
            //计算已贷款额度
            BigDecimal  usedAmount = new BigDecimal(0.00);
            LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanAuthCreditService.loadloanUserCreditInfoByUserId(userId);
            if(loanUserCreditInfoDTO != null){
                usedAmount = loanUserCreditInfoDTO.getCreditLine().subtract(loanUserCreditInfoDTO.getBalance());
            }
            dataMap.put("usedAmount",usedAmount);
//            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/lopan/info/get_loan_info_list_service_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/get_loan_info_details_ajax" , interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult showLoanInfoDetails()
    {
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            long id = Long.parseLong(String.valueOf(requesMap.get("id")));

            LoanInfoModel loanInfoModell = loanInformationService.loadloanInfoById(id);

            int code = AjaxResultCode.OK.getCode();
            String msg = SUCCESS;
            AjaxResult ajaxResult = new AjaxResult(code, msg);

            ajaxResult.setData(loanInfoModell);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/info/get_loan_info_details_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 创建资金服务信息
     * 一次性插入一个订单的，一个订单多个货柜
     *
     * @return
     */
    @UriMapping(value = "/create_loan_info_ajax" , interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult<String> createLoanInfos() {
        LoanInfoVo loanInfoList = super.getBodyObject(LoanInfoVo.class);
        Validate.notNull(loanInfoList);
        List<LoanInfoDTO> loanInfos = loanInfoList.getLoanInfos();
        Validate.isTrue(CollectionUtils.isNotEmpty(loanInfos));

        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try {
            int userId = getLoginUserId();

            if (CollectionUtils.isNotEmpty(loanInfos))
            {
                //插入
                loanInformationService.createLoanInfos(loanInfos,userId);
            }

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/info/create_loan_info_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }




    /**
     * 查询资金服务信息
     * 通过流水号list
     * 一次性查询多条
     *
     * @return
     */
    @UriMapping(value = "/loadLoanInfosByTransactionNoList" , interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult<String> loadLoanInfosByTransactionNoList() {
        LoanInfoVo loanInfoVo = super.getBodyObject(LoanInfoVo.class);
        Validate.notNull(loanInfoVo);
        List<String> transactionNoList = loanInfoVo.getTransactionNoList();
        Validate.isTrue(CollectionUtils.isNotEmpty(transactionNoList));

        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        List<LoanInfoModel> loanInfoModelList = null;

        try {
            int userId = getLoginUserId();

            if (CollectionUtils.isNotEmpty(transactionNoList))
            {
                loanInfoModelList = loanInformationService.loadLoanInfosByTransactionNoList(transactionNoList);
            }

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("loanInfos", loanInfoModelList);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/info/loadLoanInfosByTransactionNoList].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询资金服务信息
     * 通过流水号
     *
     * @return
     */
    @UriMapping(value = "/loadLoanInfosByTransactionNo" , interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult<String> loadLoanInfosByTransactionNo() {

        Map requesMap  = super.getBodyObject(HashMap.class);
    	String transactionNo = (String) requesMap.get("transactionNo");
        Validate.notNull(transactionNo);

        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        LoanInfoModel loanInfoModel = null;

        try {
            loanInfoModel = loanInformationService.loadLoanInfosByTransactionNo(transactionNo);

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("loanInfo", loanInfoModel);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/info/loadLoanInfosByTransactionNo].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 查询我的资金服务年利率、月利率
     * @return
     */
    @UriMapping(value = "/get_loan_info_interest_rate_ajax")
    public AjaxResult getLoanInfoInterestRate()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);

            //年利率
            double yearInterestRate = Double.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.PERFORMANCE_RATE));
            //月利率
            double monthInterestRate = Double.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.MONTH_PERFORMANCE_RATE));

            dataMap.put("yearInterestRate", yearInterestRate);
            dataMap.put("monthInterestRate", monthInterestRate);
            ajaxResult.setData(dataMap);
            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/loan/info/get_loan_info_interest_rate].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }
}
