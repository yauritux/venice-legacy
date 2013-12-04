package com.gdn.venice.validator;

import com.gdn.integration.jaxb.OrderItem;
import com.gdn.venice.constants.VeniceExceptionConstants;
import com.gdn.venice.exception.InvalidOrderItemException;

/**
 * 
 * @author yauritux
 *
 */
public abstract class OrderItemValidator {
	
	public final void isOrderItemValid(OrderItem item) throws InvalidOrderItemException {
		if (!isShippingPartyProvided(item.getLogisticsInfo().getLogisticsProvider()
				.getParty().getFullOrLegalName())) {
			throw new InvalidOrderItemException(
			      "Invalid Logistic Information. No Shipping Party."
				  , VeniceExceptionConstants.VEN_EX_000011);
		}
	}

	protected abstract boolean isShippingPartyProvided(String providerName);
}
