package com.gdn.venice.validator.factory;

import com.gdn.venice.factory.VeninboundFactory;
import com.gdn.venice.validator.OrderCreationValidator;
import com.gdn.venice.validator.OrderValidator;

/**
 * 
 * @author yauritux
 *
 */
public class OrderCreationValidatorFactory implements OrderValidatorFactory {
	
	public OrderValidator getOrderValidator() {
		OrderValidator orderValidator = new OrderCreationValidator();
		orderValidator.setOrderItemValidator(VeninboundFactory.getOrderItemValidator(
				new OrderItemCreationValidatorFactory()));
		return orderValidator;
	}	
}
