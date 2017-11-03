package com.fruit.portal.queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class PushBorrowContractProxy {

	/**
	 * 日志类
	 */
	private static final Logger logger = LoggerFactory.getLogger(PushBorrowContractProxy.class);

	private JmsTemplate jmsTemplate;

	private Destination borrowContractDestination;

	public void sendMsgCon(final String message) {
		
		logger.info("push borrow contract queue , the message is {}", message);
		
		this.jmsTemplate.send(this.borrowContractDestination, new MessageCreator() {

			@SuppressWarnings("finally")
			@Override
			public Message createMessage(Session session) {
				Message msg = null;
				try {
					msg = session.createTextMessage(message);
				} catch (JMSException e) {
					logger.error("sendMsgCon:fail");
				} finally {
					return msg;
				}
			}
		});
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getBorrowContractDestination() {
		return borrowContractDestination;
	}

	public void setBorrowContractDestination(Destination borrowContractDestination) {
		this.borrowContractDestination = borrowContractDestination;
	}

}
