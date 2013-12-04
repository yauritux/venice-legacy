package com.gdn.venice.exportimport.finance.dataimport.servlet;

/**
 * Constants such as error messages that are used across finance report import servlets
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public interface FinanceImportServletConstants {

	
	/**
	 * Text used as a template for the HTML output of exception text that is displayed to the user using javaScript alert. 
	 */
	public static String JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT = "<html>\n" + "<head>"
			+ "<title>Processing Report Completed</title>" + "</head>\n"
			+ "<body onload=\"alert('REPLACE')\">" + "<p>TEST</p>\n"
			+ "</body>\n" + "</html>";
	
	/**
	 * Text for an upload exception for the file itself
	 */
	public static String EXCEPTION_TEXT_UPLOAD_EXCEPTION = "An exception occured while uploading the selected file. Please contact the systems administrator.";
	
	/**
	 * Text for an exception rasied when the file is not unique
	 */
	public static String EXCEPTION_TEXT_FILE_NOT_UNIQUE = "The file selected for uploading has already been uploaded by another process. Funds-in transaction files can be uploaded ONCE ONLY.";
	
	/**
	 * Text for an exception raised when there is a problem creating the funds in report
	 */
	public static String EXCEPTION_TEXT_FUNDS_IN_REPORT_CREATION_EXCEPTION = "An exeption occured while creating the funds in report record. Please contact the systems administrator.";
	
	/**
	 * Text for an exception that is raised when there is no corresponding payment in the Venice database
	 * Note that the VENICE: tag MUST remain in the trailing characters of this constant as it is replaced on the fly in the error messages
	 */
	public static String EXCEPTION_TEXT_NO_CORRESPONDING_PAYMENT = "Records in the report being processed contain authorization codes that have no corresponding order/payment record in VENICE:";
	
	/**
	 * Text for an exception that is raised when a payment record is not found in payment schedule of the Venice database
	 * Note that the VENICE: tag MUST remain in the trailing characters of this constant as it is replaced on the fly in the error messages
	 */
	public static String EXCEPTION_TEXT_PAYMENT_NOT_FOUND = "Payments were found in the import file that do not exist in the payment schedule in VENICE:";
	
	/**
	 * Tex for a general exception that is raised on importing a payment report 
	 */
	public static String EXCEPTION_TEXT_PAYMENT_REPORT_UPLOAD_EXCEPTION = "An exception occured when uploading the payment report. If the error persists please contact the systems administrator.";
	
	/**
	 * A message to be displayed when the file format is possibly incorrect and there are zero processable rows
	 */
	public static String EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS = "Upload contained zero rows to process.";

}
