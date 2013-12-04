package com.gdn.venice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdn.integration.jaxb.Order;
import com.gdn.venice.exception.InvalidOrderException;
import com.gdn.venice.validator.OrderValidator;


public class OrderUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(OrderUtil.class);
	
	public static void checkOrder(Order order, OrderValidator validator) throws InvalidOrderException {
		LOG.debug("Order=" + order);
		validator.checkOrder(order);
		LOG.debug("Order has passed the validator");
	}		
}
