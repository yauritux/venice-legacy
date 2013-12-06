package com.gdn.venice.inbound.services.impl;

import org.springframework.stereotype.Service;

import com.gdn.integration.jaxb.Order;
import com.gdn.venice.exception.InvalidOrderException;
import com.gdn.venice.factory.VeninboundFactory;
import com.gdn.venice.inbound.commands.Command;
import com.gdn.venice.inbound.commands.impl.CreateOrderCommand;
import com.gdn.venice.inbound.receivers.OrderReceiver;
import com.gdn.venice.inbound.receivers.impl.OrderReceiverImpl;
import com.gdn.venice.inbound.services.OrderService;
import com.gdn.venice.util.OrderUtil;
import com.gdn.venice.validator.factory.OrderCreationValidatorFactory;

/**
 * 
 * @author yauritux
 * 
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Override
	public boolean createOrder(Order order) throws InvalidOrderException {
		OrderUtil.checkOrder(order, VeninboundFactory.getOrderValidator(new OrderCreationValidatorFactory()));
				
		OrderReceiver orderReceiver = new OrderReceiverImpl(order);
		Command createOrderCmd = new CreateOrderCommand(orderReceiver);
		createOrderCmd.execute();
		
		return true;
	}

}
