package com.gdn.venice.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateToXsdDatetimeFormatterClient {

    private DateTimeFormat simpleDateFormat = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss");
    private DateTimeFormat ddmmyyyyDateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");

    
    public DateToXsdDatetimeFormatterClient () {}


    /**
    *  Parse an xml date string into java.util.Date (without timezone).
    */
    public Date parse(String xmlDateTime)  {
    	if (xmlDateTime == null ||  xmlDateTime.length() != 19) {
    		return null;
    	}

        StringBuilder sb = new StringBuilder(xmlDateTime);
        return simpleDateFormat.parse(sb.toString());
    }
    
    /**
     *  Format an xml date string from java.util.Date (without timezone).
     */
    public String format(Date xmlDateTime) {
    	if (xmlDateTime == null) {
    		return "";
    	}
        String s =  simpleDateFormat.format(xmlDateTime);
        StringBuilder sb = new StringBuilder(s);
        return sb.toString();
    }
    
    /**
     *  Format an xml date string from java.util.Date to dd-MM-yyyy
     */
    public String formatToDDMMYYYY(Date xmlDateTime) {
    	if (xmlDateTime == null) {
    		return "";
    	}
        String s =  ddmmyyyyDateFormat.format(xmlDateTime);
        StringBuilder sb = new StringBuilder(s);
        return sb.toString();
    }

}