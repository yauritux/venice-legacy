package com.gdn.venice.exportimport.logistics.dataimport.servlet;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.logistics.dataimport.LogisticsServletConstants;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote;
import com.gdn.venice.logistics.dataimport.LogisticsConstants;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.persistence.LogInvoiceUploadLog;
import com.gdn.venice.util.VeniceConstants;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class InvoiceReportImportNCSServlet.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@SuppressWarnings("unused")
public class InvoiceReportImportNCSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name="InvoiceUploadConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(name="InvoiceUploadQueue")
	private Queue queue;
		
	/*
	 * The row number in the import file if an error occurs
	 */
	private Integer errorRowNumber = 1;
	protected static Logger _log = null;
	private String notificationText = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InvoiceReportImportNCSServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataimport.servlet.InvoiceReportImportNCSServlet");
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.info("InvoiceReportImportNCSServlet:hello");

		String invoiceNumber = request.getParameter("invoiceNumber");
		Locator<Object> locator = null;
		notificationText = LogisticsServletConstants.JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT;
		
		Map<String, String> failedRecord = new HashMap<String, String>();
		
		if (isMultipart) { // import
			String filePath = System.getenv("VENICE_HOME") + LogisticsServletConstants.INVOICE_REPORT_FOLDER;
			SimpleDateFormat sdf = new SimpleDateFormat(LogisticsServletConstants.DATE_TIME_FORMAT_STRING);
			String fileName = "";
			if (invoiceNumber == null || invoiceNumber.isEmpty()) {
				String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_INVOICE_NUMBER_NULL;
				_log.error(errMsg);
				notificationText = notificationText.replaceFirst("REPLACE", errMsg);
				response.getOutputStream().println(notificationText);
				return;
			} else {				
				try{
					locator = new Locator<Object>();
					LogInvoiceReportUploadSessionEJBRemote reportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
					.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
					
					List<LogInvoiceReportUpload> logInvoiceReportUploadList = reportUploadHome.queryByRange("select o from LogInvoiceReportUpload o where o.invoiceNumber = '" + invoiceNumber + "'", 0, 0);
					if(!logInvoiceReportUploadList.isEmpty()){
						LogInvoiceReportUpload logInvoiceReportUpload = logInvoiceReportUploadList.get(0);
						if(logInvoiceReportUpload.getLogApprovalStatus().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_SUBMITTED || 
								logInvoiceReportUpload.getLogApprovalStatus().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_APPROVED || 
								logInvoiceReportUpload.getLogReportStatus().getReportStatusId() == VeniceConstants.LOG_REPORT_STATUS_RECONCILED){
							String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_INVOICE_SUBMITTED_APPROVED;
							_log.error(errMsg);
							notificationText = notificationText.replaceFirst("REPLACE", errMsg);
							response.getOutputStream().println(notificationText);
							return;
						}
					}

				}catch(Exception e){
					String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_INVOICE_NUMBER_CHECK + e.getMessage();
					e.printStackTrace();
					_log.error(errMsg);
					notificationText = notificationText.replaceFirst("REPLACE", errMsg.replace("\"", " "));
				}		
				fileName = sdf.format(new Date()) + "-" + invoiceNumber + "-invoiceReportNCS.xls";
			}

			_log.debug("Opening file for writing:" + filePath + fileName);

			File uploadedFile = new File(filePath + fileName);
			ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem> fileItemsList = null;

			try {
				fileItemsList = servletFileUpload.parseRequest(request);
			} catch (FileUploadException e) {
				String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_FILE_PARSE + e.getMessage();
				e.printStackTrace();
				_log.error(errMsg);
				notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));
				response.getOutputStream().println(notificationText);
				return;
			}

			String actualFileName = "";
			Iterator<FileItem> it = fileItemsList.iterator();
			while (it.hasNext()) {
				FileItem fileItem = (FileItem) it.next();
				if (!fileItem.isFormField()) {
					try {
						_log.debug("Actual Upload file name " + fileItem.getName());
						actualFileName =  fileItem.getName();
						fileItem.write(uploadedFile);
					} catch (Exception e) {
						String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_UPLOAD_FILE_WRITE + fileName + " :" + e.getMessage();
						e.printStackTrace();
						_log.error(errMsg);
						notificationText = notificationText.replaceFirst("REPLACE", errMsg.replace("\"", " "));
						response.getOutputStream().println(notificationText);
						return;
					}
				}
			}
			
			if(queue == null){
				_log.debug("Queue null ");
			}
			
			// file upload log
			LogInvoiceUploadLog fileUploadLog = new LogInvoiceUploadLog();
			fileUploadLog.setInvoiceNumber(invoiceNumber);
			fileUploadLog.setFileUploadFormat("NCS");
			fileUploadLog.setFileUploadName(fileName);
			fileUploadLog.setFileUploadNameAndLoc(filePath + fileName);
			fileUploadLog.setActualFileUploadName(actualFileName);
			String username = "";
			
			if(request.getParameter("username") == null)
				username = "Roland";
			else
				username = request.getParameter("username");
			
			fileUploadLog.setUploadedBy(username);

			fileUploadLog.setUploadTimestamp(new Timestamp(new Date().getTime()));
			fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_PROCESSING);
			
			try{
				locator = new Locator<Object>();
				LogInvoiceUploadLogSessionEJBRemote logInvoiceUploadLogHome = 
						(LogInvoiceUploadLogSessionEJBRemote) locator.lookup(LogInvoiceUploadLogSessionEJBRemote.class, "LogInvoiceUploadLogSessionEJBBean");
				
				fileUploadLog = logInvoiceUploadLogHome.persistLogInvoiceUploadLog(fileUploadLog);
				
				Connection connection = null;
				Session session = null;
				MessageProducer producer = null;
				
				try{
					connection = connectionFactory.createConnection();
					connection.start();
					
					session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					producer = session.createProducer(queue);
					
					ObjectMessage objectMessage = session.createObjectMessage();
					objectMessage.setObject(fileUploadLog);
					
					producer.send(objectMessage);
					
					
				}catch (Exception e) {
					
					_log.error("Error sending message", e);
					
					fileUploadLog.setUploadStatus(LogisticsConstants.UPLOAD_STATUS_FAIL);
					
					try{						
						logInvoiceUploadLogHome.mergeLogInvoiceUploadLog(fileUploadLog);
						
					}catch (Exception e2) {
						_log.error("Error saving file upload log", e2);
					}finally{
						if(locator != null){
							try {
								locator.close();
							} catch (Exception e2) {
								_log.error("Error closing locator", e2);
							}
						}
					}
				}finally{
					// closing JMS connection, session and producer					
					if(producer != null){
						try {
							producer.close();
						} catch (JMSException e) {
							_log.error("Error closing JMS messsage producer", e);
						}
					}
					
					if(session != null){
						try {
							session.close();
						} catch (JMSException e) {
							_log.error("Error closing JMS session", e);
						}
					}
					
					if(connection != null){
						try {
							connection.close();
						} catch (JMSException e) {
							_log.error("Error closing JMS connection", e);
						}
					}
					
					// return upload message & upload id
					String successMsg = LogisticsConstants.UPLOAD_SUCCESS_MESSAGE;
					notificationText = notificationText.replaceFirst("REPLACE", successMsg);
					notificationText = notificationText.replaceFirst("UPLOAD_ID", fileUploadLog.getInvoiceUploadLogId().toString());
					response.getOutputStream().println(notificationText);
				}
			}catch (Exception e) {
				_log.error("Error saving file upload log", e);
			}finally{
				if(locator != null){
					try {
						locator.close();
					} catch (Exception e) {
						_log.error("Error closing locator", e);
					}
				}
			}			
		}
	}
}
