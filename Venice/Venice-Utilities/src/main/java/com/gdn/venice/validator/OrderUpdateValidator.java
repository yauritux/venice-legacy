package com.gdn.venice.validator;

import com.gdn.integration.jaxb.Order;

/**
 * 
 * @author yauritux
 *
 */
public class OrderUpdateValidator extends OrderValidator {
	
	@Override
	public boolean isOrderStatusValid(Order order) {
		if (order.getStatus() == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isAmountValid(Order order) {
		return true;
	}

	@Override
	public boolean isCustomerValid(Order order) {
		return true;
	}

	@Override
	public boolean isItemsNotEmpty(Order order) {
		return true;
	}

	@Override
	public boolean isPaymentValid(Order order) {
		return true;
	}
	
	@Override
	public boolean isLogisticInfoValid(Order order) {
		return true;
	}

}
