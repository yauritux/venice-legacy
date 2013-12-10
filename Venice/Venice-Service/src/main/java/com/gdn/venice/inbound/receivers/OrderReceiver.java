package com.gdn.venice.inbound.receivers;

import com.gdn.venice.exception.VeniceInternalException;


/**
 * 
 * @author yauritux
 *
 */
public interface OrderReceiver {
	
	public boolean createOrder() throws VeniceInternalException;
	public boolean updateOrder();
}
