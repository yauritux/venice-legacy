package com.djarum.raf.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;

/**
 * 
 * A formatter for converting a java.util.Date to xsd:datetime string (no timezone).
 * 
 * <p>
 * <b>author:</b> <a href="mailto:henry@pwsindonesia.com">Henry Chandra</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 *
 */
public class DateToXsdDatetimeFormatter {

	/*
	 * This is the standard date format used by the system
	 */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public DateToXsdDatetimeFormatter () {}
    
    /**
     * Parse an xml date string into java.util.Date (without timezone).
     * @param xmlDateTime
     * @return the corresponding java.util.Date
     * @throws ParseException
     */
    public Date parse(String xmlDateTime) throws ParseException  {
        if ( xmlDateTime.length() != 19 )  {
            throw new ParseException("Date not in expected xml datetime format", 0);
        }

        StringBuilder sb = new StringBuilder(xmlDateTime);
        return simpleDateFormat.parse(sb.toString());
    }
    
    /**
     * Format an xml date string from java.util.Date (without timezone).
     * @param xmlDateTime
     * @return the corresponding xsd:datetime string
     * @throws IllegalFormatException
     */
    public String format(Date xmlDateTime) throws IllegalFormatException  {
        String s =  simpleDateFormat.format(xmlDateTime);
        StringBuilder sb = new StringBuilder(s);
        return sb.toString();
    }

}