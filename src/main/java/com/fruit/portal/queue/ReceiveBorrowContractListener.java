package com.fruit.portal.queue;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fruit.portal.model.BorrowMQbean;
import com.fruit.portal.service.econtract.LoanApplyQuotaService;
import com.fruit.portal.utils.JsonUtil;

public class ReceiveBorrowContractListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ReceiveBorrowContractListener.class);

	@Resource
	private LoanApplyQuotaService loanApplyQuotaService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				BorrowMQbean bean= JsonUtil.toBean(((TextMessage) message).getText(), BorrowMQbean.class);
				long contractId = bean.getContractId();
				int userId = bean.getUserId();
				loanApplyQuotaService.bankSignContract(contractId,userId);
			} catch (Exception ex) {
				logger.error("JMSException {}", ex);
			}
		} else {
			logger.error("IllegalArgumentException, Message must be of type TextMessage");
		}
	}

}
