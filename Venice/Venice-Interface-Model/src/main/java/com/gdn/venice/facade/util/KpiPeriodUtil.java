package com.gdn.venice.facade.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote;
import com.gdn.venice.persistence.KpiMeasurementPeriod;

/**
 * Utility class with various methods for returning KPI periods
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class KpiPeriodUtil {
	protected static Logger _log = null;
	
	public KpiPeriodUtil(){
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.util.KpiPeriodUtil");
	}
	
	/**
	 * Returns the current KPI measurement period based on the system date and time
	 * @return the current period
	 * @throws Exception 
	 */
	public static KpiMeasurementPeriod getCurrentPeriod() throws Exception{
		Locator<Object> locator = new Locator<Object>();
		KpiMeasurementPeriodSessionEJBRemote kpiMeasurementPeriodHome = (KpiMeasurementPeriodSessionEJBRemote) locator
		.lookup(KpiMeasurementPeriodSessionEJBRemote.class, "KpiMeasurementPeriodSessionEJBBean");
		List<KpiMeasurementPeriod> periodList = kpiMeasurementPeriodHome.queryByRange("select o from KpiMeasurementPeriod o where CURRENT_DATE between o.fromDateTime and o.toDateTime", 0, 0);
		locator.close();
		
		if(periodList == null || periodList.isEmpty()){
			String errMsg = "No KPI measurement period found for the current system date";
			_log.error(errMsg);
			throw new Exception(errMsg);
		}
		
		return periodList.get(0);
	}
	
	/**
	 * Returns the bounding KPI measurement period for a given date
	 * @param date
	 * @return the period within which the date falls
	 * @throws Exception 
	 */
	public KpiMeasurementPeriod getBoundingPeriod(Date date) throws Exception{
		Locator<Object> locator = new Locator<Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		KpiMeasurementPeriodSessionEJBRemote KpiMeasurementPeriodHome = (KpiMeasurementPeriodSessionEJBRemote) locator
		.lookup(KpiMeasurementPeriodSessionEJBRemote.class, "KpiMeasurementPeriodSessionEJBBean");
		String stmt = "select o from KpiMeasurementPeriod o where o.fromDateTime < '" + sdf.format(date) + "' and o.toDateTime > '" + sdf.format(date) + "'";
		_log.debug("Statement:" + stmt);
		List<KpiMeasurementPeriod> periodList = KpiMeasurementPeriodHome.queryByRange(stmt, 0, 0);
		locator.close();
		
		if(periodList == null || periodList.isEmpty()){
			String errMsg = "A serious error has occured - No bounding KPI measurement period found for the requested system date";
			_log.error(errMsg);
			throw new Exception(errMsg);
		}

		return periodList.get(0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		KpiPeriodUtil util = new KpiPeriodUtil();
		
		try {
			KpiMeasurementPeriod period = KpiPeriodUtil.getCurrentPeriod();
			_log.debug("Current Period From:" + period.getFromDateTime());
			_log.debug("Current Period To:" + period.getToDateTime());
			
			Calendar c = Calendar.getInstance();
			c.set(2010, 12, 30);
			
			period = util.getBoundingPeriod(c.getTime());
			_log.debug("c.getTime():" + c.getTime());
			_log.debug("Period From:" + period.getFromDateTime());
			_log.debug("Period To:" + period.getToDateTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
