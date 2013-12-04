package com.gdn.venice.validator;

import com.gdn.integration.jaxb.Order;
import com.gdn.integration.jaxb.OrderItem;
import com.gdn.venice.exception.InvalidOrderItemException;

/**
 * 
 * @author yauritux
 *
 */
public class OrderCreationValidator extends OrderValidator {
	
	@Override
	protected boolean isOrderStatusValid(Order order) {
		if ((order.getStatus() == null) 
				|| (!(order.getStatus().equalsIgnoreCase("C")))) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean isAmountValid(Order order) {
		if (order.getAmount() == null) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean isCustomerValid(Order order) {
		if (order.getCustomer() == null) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean isItemsNotEmpty(Order order) {
		if (order.getOrderItems() == null 
				|| order.getOrderItems().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean isPaymentValid(Order order) {
		if (order.getPayments() == null
				|| order.getPayments().isEmpty()) {
			return false;
		}
		return true;
	}	
	
	@Override
	protected boolean isLogisticInfoValid(Order order) {
		for (OrderItem item: order.getOrderItems()) {
			try {
				getItemValidator().isOrderItemValid(item);
			} catch (InvalidOrderItemException e) {
				return false;
			} catch (NullPointerException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
