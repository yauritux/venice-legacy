package com.gdn.venice.facade.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinPeriodSessionEJBRemote;
import com.gdn.venice.persistence.FinPeriod;

/**
 * Utility class with various methods for returning periods
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FinancePeriodUtil {
	protected static Logger _log = null;
	
	public FinancePeriodUtil(){
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.util.FinancePeriodUtil");
	}
	
	/**
	 * Returns the current finance period based on the system date and time
	 * @return the current period
	 * @throws Exception 
	 */
	public static FinPeriod getCurrentPeriod() throws Exception{
		Locator<Object> locator = new Locator<Object>();
		FinPeriodSessionEJBRemote finPeriodHome = (FinPeriodSessionEJBRemote) locator
		.lookup(FinPeriodSessionEJBRemote.class, "FinPeriodSessionEJBBean");
		List<FinPeriod> periodList = finPeriodHome.queryByRange("select o from FinPeriod o where CURRENT_DATE between o.fromDatetime and o.toDatetime", 0, 0);
		locator.close();
		
		if(periodList == null || periodList.isEmpty()){
			String errMsg = "No finance period found for the current system date";
			_log.error(errMsg);
			throw new Exception(errMsg);
		}
		return periodList.get(0);
	}
	
	/**
	 * Returns the bounding period for a given date
	 * @param date
	 * @return the period within which the date falls
	 * @throws Exception 
	 */
	public FinPeriod getBoundingPeriod(Date date) throws Exception{
		Locator<Object> locator = new Locator<Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		FinPeriodSessionEJBRemote finPeriodHome = (FinPeriodSessionEJBRemote) locator
		.lookup(FinPeriodSessionEJBRemote.class, "FinPeriodSessionEJBBean");
		List<FinPeriod> periodList = finPeriodHome.queryByRange("select o from FinPeriod o where o.fromDatetime < '" + sdf.format(date) + "' and o.toDatetime > '" + sdf.format(date) + "'", 0, 0);
		locator.close();
		
		if(periodList == null || periodList.isEmpty()){
			String errMsg = "No bounding finance period found for the requested system date";
			_log.error(errMsg);
			throw new Exception(errMsg);
		}

		return periodList.get(0);		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		FinancePeriodUtil util = new FinancePeriodUtil();		
		try {
			FinPeriod period = FinancePeriodUtil.getCurrentPeriod();
			_log.debug("Current Period From:" + period.getFromDatetime());
			_log.debug("Current Period To:" + period.getToDatetime());
			
			Calendar c = Calendar.getInstance();
			c.set(2012, 06, 30);
			
			period = util.getBoundingPeriod(c.getTime());
			_log.debug("c.getTime():" + c.getTime());
			_log.debug("Period From:" + period.getFromDatetime());
			_log.debug("Period To:" + period.getToDatetime());
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
