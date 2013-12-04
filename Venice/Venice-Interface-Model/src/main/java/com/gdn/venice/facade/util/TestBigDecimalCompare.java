package com.gdn.venice.facade.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestBigDecimalCompare {
	public static void main(String[] args){
		System.out.println("start test");
		BigDecimal pricePerKg = new BigDecimal(5000);
		BigDecimal providerPricePerKg = new BigDecimal(5001);
		BigDecimal correctPricePerKg = new BigDecimal("0");
		correctPricePerKg = correctPricePerKg.setScale(2, RoundingMode.UP);
		correctPricePerKg = pricePerKg;
		
		BigDecimal selisih = new BigDecimal("0");
		selisih=correctPricePerKg.subtract(providerPricePerKg).abs();
		if(selisih.compareTo(BigDecimal.ONE)<=0){
			System.out.println("selisih is less than or equal to 1");
		}
	}
}
