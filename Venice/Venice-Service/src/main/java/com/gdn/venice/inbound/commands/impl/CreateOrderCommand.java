package com.gdn.venice.inbound.commands.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger LOG = LoggerFactory.getLogger(CreateOrderCommand.class);
	
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
