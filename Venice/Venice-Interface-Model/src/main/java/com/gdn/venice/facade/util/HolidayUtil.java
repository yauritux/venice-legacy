package com.gdn.venice.facade.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenHolidaySessionEJBRemote;
import com.gdn.venice.persistence.VenHoliday;

/**
 * HolidayUtil.java
 * 
 * This class provides a bunch of utilities for determining which days are holidays
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class HolidayUtil {
	/**
	 * Gets the number of holidays between 2 dates.
	 * ... note that only holidays on weekdays are counted
	 * @param dateFrom
	 * @param dateTo
	 * @return the number of holidays
	 */
	public static int getNumHolidaysBetweenDates(Date dateFrom, Date dateTo) {
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			VenHolidaySessionEJBRemote holidayHome = (VenHolidaySessionEJBRemote) locator.lookup(VenHolidaySessionEJBRemote.class, "VenHolidaySessionEJBBean");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String tsFormattedDateFrom = "'" + sdf.format(dateFrom) + "'";
			String tsFormattedDateTo = "'" + sdf.format(dateTo) + "'";

			List<VenHoliday> holidayList = holidayHome.queryByRange("select o from VenHoliday o where o.holidayDate between "+ tsFormattedDateFrom + " and " + tsFormattedDateTo, 0, 0);
			int numHolidays = 0;
			for(VenHoliday holiday:holidayList){
				Calendar calHoliday = Calendar.getInstance();
				calHoliday.setTime(holiday.getHolidayDate());
				if(calHoliday.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || calHoliday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
					++numHolidays;
				}
			}
			return numHolidays;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return 0;
	}

	/**
	 * Checks if the date is a holiday or a weekend
	 * @param date
	 * @return true if the day is a holiday or weekend else false
	 */
	public static Boolean isHolidayOrWeekend(Date date) {
		try {
			//Check if it is a weekend first
			if(isWeekend(date)){
				return true;
			}
			
			//Check if it is a holiday
			return isHoliday(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Definitely a working day
		return false;
	}
	
	/**
	 * Returns true if the date is a weekend date
	 * @param date
	 * @return true if date is Saturday or Sunday else false
	 */
	public static Boolean isWeekend(Date date){
		//Check if it is a weekend first
		Calendar calDay = Calendar.getInstance();
		calDay.setTime(date);
		if(calDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if the date is a holiday
	 * @param date
	 * @return true if the day is a holiday else false
	 */
	public static Boolean isHoliday(Date date) {
		Locator<Object> locator = null;
		try {
			//Check if it is a holiday
			locator = new Locator<Object>();
			VenHolidaySessionEJBRemote holidayHome = (VenHolidaySessionEJBRemote) locator.lookup(VenHolidaySessionEJBRemote.class, "VenHolidaySessionEJBBean");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String tsFormattedDateFrom = "'" + sdf.format(date) + "'";
			
			String query="select o from VenHoliday o where o.holidayDate = " + tsFormattedDateFrom;		
			List<VenHoliday> holidayList = holidayHome.queryByRange(query, 0, 0);
			
			//If the list is not empty then return true because it must be a holiday
			if(!holidayList.isEmpty()){
				//It is a holiday
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//Not a holiday
		return false;
	}
	
	/**
	 * Returns true if date is the last working day of the week (normally Friday)
	 * @param date
	 * @return true if date is the last working day
	 */
	public static Boolean isLastWorkingDayOfWeek(Date date){
		//If there are working days after date then return false
		if(weekHasWorkingDayAfter(date)){
			return false;
		}
		
		//Must be the last working day
		return true;
	}
	
	/**
	 * Returns true if week within which the day falls has a holiday after it
	 * @param date
	 * @return true if a holiday exists after this day in the week
	 */
	public static Boolean weekHasWorkingDayAfter(Date date){
		Calendar calDay = Calendar.getInstance();
		calDay.setTime(date);
		if(isWeekend(date)){
			return false;
		}
		
		// Check each week day after date for a holiday
		while (calDay.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
			calDay.add(Calendar.DAY_OF_YEAR, 1);// Increment the day
			if (!isHoliday(calDay.getTime()) && calDay.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the first working succeeding todayDate in the following week
	 * 
	 * @param todayDate
	 * @return the Calendar that is the first working succeeding todayDate in the following week
	 */
	public static Calendar getNextWorkingDayFollowingWeek(Date todayDate){
		Calendar calDay = Calendar.getInstance();
		calDay.setTime(todayDate);
		
		//Whatever day it is roll it forward to the next Monday
		while(calDay.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
			calDay.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		//Check all forward days until we get a working day
		while(true){
			if(!isHoliday(calDay.getTime()) && !isWeekend(calDay.getTime())){
				return calDay;
			}
			calDay.add(Calendar.DAY_OF_YEAR, 1);
		}
	}
	
	/**
	 * Returns the next working day from a given day.
	 * Note that this method should only be called when
	 * there is a working day remaining otherwise an exception
	 * will be thrown.
	 * 
	 * @param todayDate
	 * @return the next working day
	 * @throws Exception 
	 */
	public static Calendar getNextWorkingDayOfWeek(Date todayDate) throws Exception{
		Calendar calDay = Calendar.getInstance();
		calDay.setTime(todayDate);
		
		//Check each day for a working day until the saturday
		while(calDay.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY){
			calDay.add(Calendar.DAY_OF_YEAR, 1);
			
			//cek tanggal next working date holiday atau bukan	  
			todayDate = calDay.getTime(); 
			if(!isHoliday(todayDate) && calDay.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY){
				return calDay;
			}
		}
		throw new Exception("No working day remaining in current week");
	}
	
	/**
	 * Returns the number of working days between two dates
	 * @param startDate the date to start with
	 * @param endDate the date to end with
	 * @return the number of working days
	 */
	public static Integer getWorkingDaysBetweenDates(Date startDate, Date endDate){
		Integer workingDays = 0;
		
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.setTime(startDate);
		
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(endDate);
		
		while(!startDateCal.after(endDateCal)){
			if(!isHoliday(startDate) && !isWeekend(startDate)){
				++ workingDays;
			}
			startDateCal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		return workingDays;
	}
}
