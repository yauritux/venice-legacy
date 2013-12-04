package com.gdn.venice.inbound.receivers;

import com.gdn.venice.exception.InvalidOrderException;


/**
 * 
 * @author yauritux
 *
 */
public interface OrderReceiver {
	
	public boolean createOrder() throws InvalidOrderException;
	public boolean updateOrder();
}
