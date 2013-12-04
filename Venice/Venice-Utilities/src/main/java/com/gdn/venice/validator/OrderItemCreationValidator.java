package com.gdn.venice.validator;



/**
 * 
 * @author yauritux
 *
 */
public class OrderItemCreationValidator extends OrderItemValidator {
	
	@Override
	protected boolean isShippingPartyProvided(String providerName) {
		if (providerName.trim().equalsIgnoreCase("Select Shipping")) {
			return false;
		}
		return true;
	}
}
