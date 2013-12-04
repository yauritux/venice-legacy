package com.djarum.raf.utilities;

import java.util.ArrayList;

/**
 * Utility with some basic math functions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2012
 * 
 */
public class MathUtil {
	
	/**
	 * Returns the average of a list of integers
	 * @param list the list of integers
	 * @return the integral average
	 */
	public static Integer avg(ArrayList<Integer> list){
		
		Integer sum = 0;
		for(Integer entry:list){
			sum = sum + entry;
		}
		Double avgDouble = sum.doubleValue() / list.size();
		return avgDouble.intValue();
	}

}
