package com.gdn.venice.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;

/**
 * This is a date formatter to handle GWT formatted dates
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
 public class GwtDateFormatter {
	
	/**
	 * Takes the date formatted as a string that comes across from GWT and returns a date
	 * @param gwtDate
	 * @return the corresponding java.util.Date
	 * @throws ServletException
	 */
	public static Date parse(String gwtDate) throws ServletException{
		/* This is the format that GWT dates are expressed in:
		 * 	Mon Jan 30 2012 00:00:00 GMT+0700 (WIT)
		 */
		SimpleDateFormat sdf = null;
		Date parsedDate = null;
		
		if(gwtDate.contains("/")){
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				parsedDate = sdf.parse(gwtDate);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ServletException("A ParseException occured when parsign a GWT date:" + e.getMessage());
			}
		}else{
			sdf = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss zzz");
			
			String dateStringToParse = gwtDate.substring(0,24);
			if(gwtDate.contains("GMT+0700")){
				dateStringToParse = dateStringToParse + " " + "WIT";
			}else if(gwtDate.contains("GMT+0800")){
				dateStringToParse = dateStringToParse + " " + "CIT";
			}else if(gwtDate.contains("GMT+0900")){
				dateStringToParse = dateStringToParse + " " + "EIT";
			}
			
			try {
				parsedDate = sdf.parse(dateStringToParse);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ServletException("A ParseException occured when parsign a GWT date:" + e.getMessage());
			}
		}
		
		return parsedDate;
	}
	
	/**
	 * Formats the date string to the format that the GWT client expects in a list grid
	 * @param date
	 * @return the formatted date string
	 */
	public static String format(Date date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		return sdf.format(date);

		
	}

}
