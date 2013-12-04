package com.gdn.venice.validator.factory;

import com.gdn.venice.validator.OrderValidator;

/**
 * 
 * @author yauritux
 *
 */
public interface OrderValidatorFactory {
	
	public OrderValidator getOrderValidator();	
}
