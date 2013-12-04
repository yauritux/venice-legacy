package com.gdn.venice.logistics.dataimport;

import java.text.SimpleDateFormat;

/**
 * Servlet constants that are reused across the logistics import servlets
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public interface LogisticsConstants {
	/**
	 * Text used as a template for the HTML output of exception text that is displayed to the user using javaScript alert. 
	 */
	public static String JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT = "<html>\n" + "<head>"
			+ "<title>Processing Report Completed</title>" + "</head>\n"
			+ "<body onload=\"alert('REPLACE')\">" + "<p>TEST</p>\n"
			+ "</body>\n" + "</html>";

	public static String ACTIVITY_REPORT_FOLDER = "/files/import/activity/";
	
	public static String INVOICE_REPORT_FOLDER = "/files/import/invoice/";
	
	public static String TEMPLATE_FOLDER = "/files/template/";
	
	public static String DATE_TIME_FORMAT_STRING = "yyyy.MM.dd HH:mm:ss";
	
	public static String FILE_DATE_TIME_FORMAT = "yyyy-MM-dd HH-mm-ss";
	
	public static String DATE_FORMAT_STRING = "dd-MMM-yyyy";
	
	public static String TIME_FORMAT_STRING = "hh:mm aaa";
	
	public static String EXCEPTION_TEXT_FILE_PARSE = "An exception occured when parsing the file upload:";
	
	public static String EXCEPTION_TEXT_UPLOAD_FILE_WRITE = "Upload failed! An exception occured when writing to file:";
	
	public static String EXCEPTION_TEXT_TEMPLATE_NOT_FOUND = "Report template file not found";
	
	public static String EXCEPTION_TEXT_AIRWAY_BILL_NOT_FOUND_BY_GDN_REF = "Airway bill from report not found in database by matching GDN Reference:";
	
	public static String EXCEPTION_TEXT_AIRWAY_BILL_ACTIVITY_STATUS_IS_APPROVED = "Airway bill from activity report has already been submitted or approved:";

	public static String EXCEPTION_TEXT_AIRWAY_BILL_INVOICE_STATUS_IS_APPROVED = "Airway bill from invoice report has already been submitted or approved:";
		
	public static String EXCEPTION_TEXT_NO_ORDER_ITEM_FOUND = "No order item could be found in the venice database for airway bill: ";
	
	public static String EXCEPTION_TEXT_AWB_STATUS_NOT_PU_PP_ES_CX_D = "Airway bill status that is not PU, PP, ES, CX, or D for order item:";
	
	public static String EXCEPTION_TEXT_INVOICE_NUMBER_NULL = "Invoice number has not been provided. An invoice number must be provided to upload the report!";
	
	public static String EXCEPTION_TEXT_INVOICE_NO_EXISTS_AND_APPROVED = "An invoice with the invoice number already exists and has been approved:";
	
	public static String EXCEPTION_TEXT_INVOICE_APPROVED = "an invoice with this number has already been reconciled and approved. A unique invoice number must be provided!";
	
	public static String EXCEPTION_TEXT_INVOICE_NUMBER_CHECK = "An exception occured when checking the invoice number:";
	
	public static String EXCEPTION_TEXT_GENERAL = "An exception occured when uploading the report. If the error persists please contact the systems administrator.";
	
	public static String RESULT_STATUS_NO_DATA_FROM_MTA = "No Data from MTA";
	
	public static String UPLOAD_SUCCESS_MESSAGE = "Report upload is being processed. Your upload Id is  UPLOAD_ID ,please check upload status regularly";
	
	/**
	 * A message to be displayed when the file format is possibly incorrect and there are zero processable rows
	 */
	public static String EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS = "Upload contained zero rows to process, check import file format.";
	
	public static String UPLOAD_STATUS_SUCCESS = "Success";
	
	public static String UPLOAD_STATUS_FAIL = "Fail";
	
	public static String UPLOAD_STATUS_PROCESSING = "Processing";

}
