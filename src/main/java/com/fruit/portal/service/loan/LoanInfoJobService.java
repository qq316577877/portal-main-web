/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.service.loan;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.common.RepaymentMethodsTypeEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.request.LoanInfoQueryRequestModel;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.socket.config.LoanInterfaceConfig;
import com.fruit.loan.biz.socket.util.DateUtil;
import com.fruit.loan.biz.socket.util.MathUtil;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.order.biz.service.OrderService;
import com.fruit.portal.model.loan.LoanInfoModel;
import com.fruit.portal.model.loan.LoanInfoQueryRequest;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.common.RuntimeConfigurationService;
import com.fruit.portal.service.order.OrderGeneralService;
import com.fruit.portal.service.wechat.WeChatBaseService;
import com.fruit.portal.utils.NumberUtil;
import com.fruit.portal.utils.WechatConstants;
import com.fruit.portal.vo.PageResult;
import com.fruit.portal.vo.order.ContainerInfoForLoan;
import com.fruit.portal.vo.wechat.TemplateParamVO;
import com.fruit.portal.vo.wechat.TemplateVO;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


/**
 * Description:
 * 用户资金服务相关
 * Create Author  : paul
 * Create Date    : 2017-06-26
 * Project        : partal-main-web
 * File Name      : LoanInfoJobService.java
 */
@Service
public class LoanInfoJobService
{
    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private LoanMessageService loanMessageService;

    @Autowired
    private LoanUserAuthInfoService loanUserAuthInfoService;

    @Autowired
    private OrderGeneralService orderGeneralService;

    @Autowired(required = false)
    private List<EventPublisher> eventPublishers;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Resource
    private LoanSmsContactsConfigService loanSmsContactsConfigService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private WeChatBaseService weChatBaseService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanInfoJobService.class);


    /**
     * 找到所有需要发短信提醒还款的
     */
    public void loanPreparePayMessageNotice()
    {
        Date beginDate = new Date();
        LOGGER.info("进入任务loanPreparePayMessageNotice===========,开始扫描是否达到短信提醒还款条件，开始时间：{}",beginDate);

        try {
            /*查询出loan_info表中Status为待还款状态的*/
            int status = LoanInfoStatusEnum.REPAYMENT.getStatus();

            Date repayDate = getPrepayDate();//查询今天提醒的提前还款时间

            //获取开始时间
            Date beginTime = DateUtil.getDateBeginDate(repayDate);
            //获取结束时间
            Date endTime = DateUtil.getDateEndDate(repayDate);

            List<LoanInfoDTO> loanInfoDTOList = loanInfoService.listPrepaysByStatus(status,beginTime,endTime);

            handlerInfos(loanInfoDTOList,repayDate);//发送短信提醒


        }catch(Exception e){
            LOGGER.error("loanPreparePayMessageNotice job error.{}",e);

            //异常告警
            EventHelper.triggerEvent(this.eventPublishers, "LoanInfoJobService." + e.getMessage(),
                    "error when loanPreparePayMessageNotice " , EventLevel.IMPORTANT, e,
                    ServerIpUtils.getServerIp());
        }



        Date endDate = new Date();
        LOGGER.info("loanPreparePayMessageNotice===========,完成时间：{}",endDate);
    }


    /**
     * 短信提醒
     * @param loanInfoDTOList
     * @param prepayDate
     */
    private void handlerInfos(List<LoanInfoDTO> loanInfoDTOList,Date prepayDate) throws Exception{
        if (CollectionUtils.isNotEmpty(loanInfoDTOList)) {
            for (LoanInfoDTO loanInfoDTO : loanInfoDTOList) {
                if (checkIsNoticeAvaliableBytime(loanInfoDTO.getExpiresTime(), prepayDate)) {//再次确认时间
                    //根据userId查询实名认证信息
                    LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(loanInfoDTO.getUserId());

                    //根据transactionId查询货柜信息
                    ContainerInfoForLoan containerInfoForLoan  = this.orderGeneralService.queryContainerInfo(loanInfoDTO.getOrderNo(),loanInfoDTO.getTransactionNo());

                    if(loanUserAuthInfoDTO!=null && containerInfoForLoan!=null){
                        //发送提醒短信
                        sendMessageOfCredit(loanInfoDTO,loanUserAuthInfoDTO,containerInfoForLoan);
                    }
                }
            }
        }
    }


    /**
     * 获取此次任务跑动时，提前还款的时间
     *
     * @return
     */
    private Date getPrepayDate(){
        //此参数说明，如果此条记录 提前还款时间是明天，则符合要求
        //提前还款提前提醒天数，提前1天
        int time = Integer.valueOf(runtimeConfigurationService.getConfig(LoanInterfaceConfig.DAYS_OF_PREPARE_PAY_NOTICE));
        return DateUtil.addDay(new Date(),time);
    }


    /**
     * 判断当前是否是提前还款前一天
     * @param expiresTime 数据库中的loan_info表 expiresTime字段
     * @param prepayDate  通过计算之后得出的提前还款时间
     * @return
     */
    private boolean checkIsNoticeAvaliableBytime(Date expiresTime,Date prepayDate){
        int check = DateUtil.getIntervalDays(expiresTime,prepayDate);//两个日期相隔天数
        if(check==0){//两个日期相隔天数为0
            return true;//应该通知
        }else{
            return false;
        }
    }


    /**
     * 发送资金到期还款短信通知
     */
    private void sendMessageOfCredit(LoanInfoDTO loanInfoDTO,LoanUserAuthInfoDTO loanUserAuthInfoDTO,ContainerInfoForLoan containerInfoForLoan){

        BigDecimal offerLoan = loanInfoDTO.getOfferLoan();//本金
        Date OfferTime = loanInfoDTO.getOfferTime();
        Date expiresTime = loanInfoDTO.getExpiresTime();//到期强制还款时间
        String expiresTimeStr= DateUtil.getDate(expiresTime);
        int interestDays = DateUtil.getIntervalDays(OfferTime,expiresTime);
        //利率
        double percent = Double.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL,BaseRuntimeConfig.PERFORMANCE_RATE));
        //利息
        double interest = calculateInterest(offerLoan,percent,interestDays);

        double principal = offerLoan.doubleValue() + interest;

        String template = "【九创金服】尊敬的客户，您的订单{0}中的货柜{1}所使用资金将于{2}到期，请于{3}15:00前预存{4}元，以免影响您的会员等级。如有疑问，请致电4008-265-128，感谢您的理解和支持！";
        String content = MessageFormat.format(template, containerInfoForLoan.getOrderNo(),containerInfoForLoan.getNo()+" "+containerInfoForLoan.getProductName(),expiresTimeStr,expiresTimeStr+" ",principal);
        loanMessageService.sendSms(loanInfoDTO.getUserId(),loanUserAuthInfoDTO.getMobile(),content);


        //银行客户经理
        LoanSmsContactsConfigDTO loanSmsContactsConfigDTOBank = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                LoanSmsBizTypeEnum.BANKER_CONTRACT.getType());

        loanMessageService.sendSms(loanInfoDTO.getUserId(), getCurrentConfig(LoanSmsBizTypeEnum.BANKER_PHONE), content);

        //平台管理员
        LoanSmsContactsConfigDTO loanSmsContactsConfigDTOSupplier = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                LoanSmsBizTypeEnum.SUPPLIER_PHONE.getType());
        loanMessageService.sendSms(loanInfoDTO.getUserId(), getCurrentConfig(LoanSmsBizTypeEnum.SUPPLIER_PHONE), content);

        //微信模板消息推送
        UserAccountDTO userAccountDTO = userAccountService.loadById(loanInfoDTO.getUserId());
        String openid = userAccountDTO.getOpenid();
        if (StringUtils.isNotBlank(openid)) {
            TemplateVO templateWechat = new TemplateVO();
            templateWechat.setToUser(openid);

            String templateId = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.template_3);
            String urlEnter = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.doamin);
            templateWechat.setTemplateId(templateId);
            templateWechat.setUrl(urlEnter.replaceFirst("state=1","state=2"));

            StringBuilder firstStr = new StringBuilder("尊敬的客户，您有货柜已签收.");
            firstStr.append(" 货柜批次号：");
            firstStr.append(containerInfoForLoan.getNo());
            firstStr.append(" 货柜状态：");
            firstStr.append(" 待还款");

            StringBuilder remark = new StringBuilder("");
            remark.append(" 商品金额：");
            remark.append(containerInfoForLoan.getProductAmount()+" 元");
            remark.append(" 贷款金额：");
            remark.append(offerLoan+" 元");
            remark.append(" 如有疑问，请致电4008-265-128，感谢您的理解和支持！");

            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
            dataListP.add(new TemplateParamVO("first", firstStr.toString(), "#000000"));
            dataListP.add(new TemplateParamVO("keyword1", containerInfoForLoan.getProductName(), "#000000"));
            dataListP.add(new TemplateParamVO("keyword2", containerInfoForLoan.getOrderNo(), "#000000"));
            dataListP.add(new TemplateParamVO("keyword3", "该货柜所使用资金将于"+expiresTimeStr+"到期，请于"+expiresTimeStr+"15:00前预存"+principal+"元，以免影响您的会员等级。", "#ff0000"));
            dataListP.add(new TemplateParamVO("remark", remark.toString(), "#000000"));


            templateWechat.setTemplateParamList(dataListP);

            weChatBaseService.sendMessage(templateWechat);
        }


    }


    /**
     * 通过本金、利率、借款天数来计算利息
     * @param offerLoan
     * @param percent
     * @param interestDays
     */
    private double calculateInterest(BigDecimal offerLoan,double percent,int interestDays){
        double interest = offerLoan.doubleValue()*percent*interestDays/360;

        BigDecimal interestBig = MathUtil.doubleToBigDecima(interest);//保留两位小数

        return interestBig.doubleValue();
    }

    private String getCurrentConfig(LoanSmsBizTypeEnum loanSmsBizTypeEnum) {
        String value;

        LoanSmsContactsConfigDTO config = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                loanSmsBizTypeEnum.getType());

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
}
