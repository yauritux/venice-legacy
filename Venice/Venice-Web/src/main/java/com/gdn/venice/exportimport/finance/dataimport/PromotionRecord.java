package com.gdn.venice.exportimport.finance.dataimport;

import com.gdn.venice.hssf.PojoInterface;

public class PromotionRecord implements PojoInterface{
	
	private String promoCode;

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

}
