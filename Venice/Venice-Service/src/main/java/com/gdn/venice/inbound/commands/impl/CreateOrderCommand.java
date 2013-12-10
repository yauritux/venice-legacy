package com.gdn.venice.inbound.commands.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.constants.LoggerLevel;
import com.gdn.venice.exception.VeniceInternalException;
import com.gdn.venice.inbound.commands.Command;
import com.gdn.venice.inbound.receivers.OrderReceiver;
import com.gdn.venice.util.CommonUtil;

/**
 * 
 * @author yauritux
 *
 */
public class CreateOrderCommand implements Command {
	private static Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
	private static final Logger LOG = loggerFactory.getLog4JLogger(CreateOrderCommand.class.getName());
	
	private OrderReceiver orderReceiver;
	
	public CreateOrderCommand(OrderReceiver receiver) {
		orderReceiver = receiver;
	}
	
	@Override
	public void execute() {
		try {
			orderReceiver.createOrder();
		} catch (VeniceInternalException vie) {
			CommonUtil.logException(vie, LOG, LoggerLevel.ERROR);
		}
	}
}
