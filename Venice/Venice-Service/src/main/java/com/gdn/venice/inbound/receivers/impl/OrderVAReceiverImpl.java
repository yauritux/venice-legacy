package com.gdn.venice.inbound.receivers.impl;

import com.gdn.integration.jaxb.Order;
import com.gdn.venice.inbound.receivers.OrderReceiver;

/**
 * 
 * @author yauritux
 *
 */
public class OrderVAReceiverImpl implements OrderReceiver {
	
	private Order order;
	
	public OrderVAReceiverImpl(Order order) {
		super();
		this.order = order;
	}

	@Override
	public boolean createOrder() {
		return true;
	}

	@Override
	public boolean updateOrder() {
		return true;
	}

}
