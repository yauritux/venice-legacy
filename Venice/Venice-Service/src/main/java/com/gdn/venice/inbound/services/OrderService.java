package com.gdn.venice.inbound.services;

import com.gdn.integration.jaxb.Order;
import com.gdn.venice.exception.VeniceInternalException;

/**
 * 
 * @author yauritux
 *
 */
public interface OrderService {
	
	public boolean createOrder(Order order) throws VeniceInternalException;
}
