package com.gdn.venice.validator.factory;

import com.gdn.venice.validator.OrderUpdateValidator;
import com.gdn.venice.validator.OrderValidator;

/**
 * 
 * @author yauritux
 *
 */
public class OrderUpdateValidatorFactory implements OrderValidatorFactory {

	public OrderValidator getOrderValidator() {
		return new OrderUpdateValidator();
	}
}
