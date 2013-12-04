package com.gdn.venice.validator.factory;

import com.gdn.venice.validator.OrderItemCreationValidator;
import com.gdn.venice.validator.OrderItemValidator;

/**
 * 
 * @author yauritux
 *
 */
public class OrderItemCreationValidatorFactory implements OrderItemValidatorFactory {
	
	public OrderItemValidator getOrderItemValidator() {
		return new OrderItemCreationValidator();		
	}	
}
