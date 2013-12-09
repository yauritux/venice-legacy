package com.gdn.venice.inbound.receivers;

import org.springframework.stereotype.Component;
import com.gdn.venice.exception.VeniceInternalException;



/**
 * 
 * @author yauritux
 *
 */
@Component
public interface OrderReceiver {
	
	public boolean createOrder() throws VeniceInternalException;
	public boolean updateOrder();
}
