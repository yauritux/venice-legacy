package com.gdn.venice.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;

public class DateToXsdDatetimeFormatter {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private SimpleDateFormat ddmmyyyyDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    
    public DateToXsdDatetimeFormatter () {}

    /**
     *  Parse an xml date string from dd-MM-yyyy into java.util.Date 
     */
    public Date parseFromDDMMYYYY (String xmlDate) throws ParseException {
    	if (xmlDate == null) {
    		return null;
    	}
    	StringBuilder sb = new StringBuilder(xmlDate);
        return ddmmyyyyDateFormat.parse(sb.toString());
    }

    /**
    *  Parse an xml date string into java.util.Date (without timezone).
    */
    public Date parse(String xmlDateTime) throws ParseException  {
    	if (xmlDateTime == null) {
    		return null;
    	}
        if ( xmlDateTime.length() != 19 )  {
            throw new ParseException("Date not in expected xml datetime format", 0);
        }

        StringBuilder sb = new StringBuilder(xmlDateTime);
        return simpleDateFormat.parse(sb.toString());
    }
    
    /**
     *  Format an xml date string from java.util.Date (without timezone).
     */
    public String format(Date xmlDateTime) throws IllegalFormatException  {
    	if (xmlDateTime == null) {
    		return "";
    	}
        String s =  simpleDateFormat.format(xmlDateTime);
        StringBuilder sb = new StringBuilder(s);
        return sb.toString();
    }

}