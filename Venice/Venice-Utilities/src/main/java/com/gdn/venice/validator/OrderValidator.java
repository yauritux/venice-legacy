package com.gdn.venice.validator;

import com.gdn.integration.jaxb.Order;
import com.gdn.venice.constants.OrderConstants;
import com.gdn.venice.constants.VeniceExceptionConstants;
import com.gdn.venice.exception.InvalidOrderException;
import com.gdn.venice.exception.InvalidOrderFulfillmentStatusException;
import com.gdn.venice.exception.InvalidOrderLogisticInfoException;
import com.gdn.venice.exception.InvalidOrderStatusException;
import com.gdn.venice.exception.InvalidOrderTimestampException;

/**
 * 
 * @author yauritux
 *
 */
public abstract class OrderValidator {
	
	private OrderItemValidator itemValidator;
	
	public void setOrderItemValidator(OrderItemValidator itemValidator) {
		this.itemValidator = itemValidator;
	}
	
	public OrderItemValidator getItemValidator() {
		return itemValidator;
	}
	
	public final void checkOrder(Order order) throws InvalidOrderException {
		if (!isOrderExist(order)) {
			throw new InvalidOrderException(
				"createOrder: no order received"
				, VeniceExceptionConstants.VEN_EX_000002);
		}
		if (!isOrderIDValid(order)) {
			throw new InvalidOrderException(	
				"createOrder: message received with no identifier information record"
				, VeniceExceptionConstants.VEN_EX_000005);
		}
		if (!isOrderTimeStampValid(order)) {
			throw new InvalidOrderTimestampException(
				"createOrder: message received with no timestamp information record"
				, VeniceExceptionConstants.VEN_EX_000008);
		}
		if (!isOrderStatusValid(order)) {
			throw new InvalidOrderStatusException(
				"createOrder: message received with invalid status information record"
				, VeniceExceptionConstants.VEN_EX_000009);
		}
		if (!isOrderFulfillmentStatusValid(order)) {
			throw new InvalidOrderFulfillmentStatusException(
				"createOrder: message received fulfillment status = VEN_FULFILLMENT_STATUS_ONE(1). Orders cannot be created with fulfillment status = 1"
				, VeniceExceptionConstants.VEN_EX_000010);
		}
		if (!isAmountValid(order)) {
			throw new InvalidOrderException(
			    "createOrder: message received with no amount"
				, VeniceExceptionConstants.VEN_EX_000003);
		}
		if (!isCustomerValid(order)) {
			throw new InvalidOrderException(
				"createOrder: message received with no customer information record"
				, VeniceExceptionConstants.VEN_EX_000004);
		}
		if (!isItemsNotEmpty(order)) {
			throw new InvalidOrderException(
				"createOrder: message received with no order item information record"
				, VeniceExceptionConstants.VEN_EX_000006);
		}
		if (!isPaymentValid(order)) {
			throw new InvalidOrderException(
				"createOrder: message received with no payment information record"
				, VeniceExceptionConstants.VEN_EX_000007);
		}
		if (!isLogisticInfoValid(order)) {
			throw new InvalidOrderLogisticInfoException(
				"createOrder: message received with no logistic info (shipping) provided."
				, VeniceExceptionConstants.VEN_EX_000011);
		}
	}
	
	protected boolean isOrderExist(Order order) {
		if (order == null) {
			return false;
		}
		return true;
	}
	
	protected boolean isOrderIDValid(Order order) {
		if (order.getOrderId() == null) {
			return false;
		}
		return true;
	}
	
	protected boolean isOrderTimeStampValid(Order order) {
		if (order.getTimestamp() == null) {
			return false;
		}
		return true;
	}
		
	protected boolean isOrderFulfillmentStatusValid(Order order) {
		if (order.getFullfillmentStatus() != null 
				&& order.getFullfillmentStatus() == OrderConstants.VEN_FULFILLMENT_STATUS_ONE.code()) {
			return false;
		}
		return true;
	}
	
	protected abstract boolean isOrderStatusValid(Order order);
	
	protected abstract boolean isAmountValid(Order order);
	
	protected abstract boolean isCustomerValid(Order order);
	
	protected abstract boolean isItemsNotEmpty(Order order);
	
	protected abstract boolean isPaymentValid(Order order);
	
	protected abstract boolean isLogisticInfoValid(Order order);
}
