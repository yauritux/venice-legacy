package com.djarum.raf.utilities;

import java.util.Calendar;

/**
 * Utility for converting between java.util.Date and javax.sql.Time/Timestamp etc.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class SQLDateUtility{
	
	/**
	* Convert the java.util.Date to java.sql.Date.
	* @param utilDate the time.
	* @return the converted time.
	*/
	public static final java.sql.Date utilDateToSqlDate(java.util.Date utilDate) {
	return new java.sql.Date(utilDate.getTime());
	}
	
	/**
	* Convert the java.util.Date to java.sql.Time.
	* @param utilDate the time.
	* @return the converted time.
	*/
	public static final java.sql.Time utilDateToSqlTime(java.util.Date utilDate) {
	return new java.sql.Time(utilDate.getTime());
	}


	/**
	* Convert the java.util.Date to java.sql.Timestamp.
	* @param utilDate the time.
	* @return the converted time.
	*/
	public static final java.sql.Timestamp utilDateToSqlTimestamp(java.util.Date utilDate) {
	return new java.sql.Timestamp(utilDate.getTime());
	}
	
	/**
	 * Determines the number of days between two dates in Calendar objects
	 * @param startDate
	 * @param endDate
	 * @return the difference in days
	 */
	public static long daysBetween(final Calendar startDate,
			final Calendar endDate) {
		Calendar sDate = (Calendar) startDate.clone();
		long daysBetween = 0;

		int y1 = sDate.get(Calendar.YEAR);
		int y2 = endDate.get(Calendar.YEAR);
		int m1 = sDate.get(Calendar.MONTH);
		int m2 = endDate.get(Calendar.MONTH);

		// **year optimization**
		while (((y2 - y1) * 12 + (m2 - m1)) > 12) {

			// move to Jan 01
			if (sDate.get(Calendar.MONTH) == Calendar.JANUARY
					&& sDate.get(Calendar.DAY_OF_MONTH) == sDate
							.getActualMinimum(Calendar.DAY_OF_MONTH)) {

				daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_YEAR);
				sDate.add(Calendar.YEAR, 1);
			} else {
				int diff = 1 + sDate.getActualMaximum(Calendar.DAY_OF_YEAR)
						- sDate.get(Calendar.DAY_OF_YEAR);
				sDate.add(Calendar.DAY_OF_YEAR, diff);
				daysBetween += diff;
			}
			y1 = sDate.get(Calendar.YEAR);
		}

		// ** optimize for month **
		// while the difference is more than a month, add a month to start month
		while ((m2 - m1) % 12 > 1) {
			daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_MONTH);
			sDate.add(Calendar.MONTH, 1);
			m1 = sDate.get(Calendar.MONTH);
		}

		// process remainder date
		while (sDate.before(endDate)) {
			sDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}

		return daysBetween;
	}
}