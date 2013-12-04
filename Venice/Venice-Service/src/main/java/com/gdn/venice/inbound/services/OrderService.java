package com.gdn.venice.inbound.services;

import org.springframework.stereotype.Component;

import com.gdn.integration.jaxb.Order;
import com.gdn.venice.exception.InvalidOrderException;

/**
 * 
 * @author yauritux
 *
 */
@Component
public interface OrderService {
	
	public boolean createOrder(Order order) throws InvalidOrderException;
}
