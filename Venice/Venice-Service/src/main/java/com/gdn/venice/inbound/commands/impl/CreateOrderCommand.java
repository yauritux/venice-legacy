package com.gdn.venice.inbound.commands.impl;

import com.gdn.venice.inbound.commands.Command;
import com.gdn.venice.inbound.receivers.OrderReceiver;

/**
 * 
 * @author yauritux
 *
 */
public class CreateOrderCommand implements Command {
	
	private OrderReceiver orderReceiver;
	
	public CreateOrderCommand(OrderReceiver receiver) {
		orderReceiver = receiver;
	}
	
	@Override
	public void execute() {
		orderReceiver.createOrder();
	}
}
