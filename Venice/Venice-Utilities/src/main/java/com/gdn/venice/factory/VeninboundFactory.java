package com.gdn.venice.factory;

import com.gdn.venice.validator.OrderItemValidator;
import com.gdn.venice.validator.OrderValidator;
import com.gdn.venice.validator.factory.OrderItemValidatorFactory;
import com.gdn.venice.validator.factory.OrderValidatorFactory;

/**
 * 
 * @author yauritux
 *
 */
public class VeninboundFactory {
	
	public static OrderValidator getOrderValidator(OrderValidatorFactory factory) {
		return factory.getOrderValidator();
	}
	
	public static OrderItemValidator getOrderItemValidator(OrderItemValidatorFactory factory) {
		return factory.getOrderItemValidator();
	}
}
