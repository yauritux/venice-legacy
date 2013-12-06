package com.gdn.venice.server.app.fraud.presenter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudFileAttachment;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class FraudCaseAttachmentServlet
 * 
 * @author Roland
 */

public class FraudCaseAttachmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;
	private String notificationText = "";
	private Boolean status = true;
	public FraudCaseAttachmentServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataimport.servlet.FraudCaseAttachmentServlet");
	}

	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.info("FraudCaseAttachmentServlet:hello");

		notificationText = "<html>\n" + "<head>"
		+ "<title>Processing Report Completed</title>" + "</head>\n"
		+ "<body onload=\"alert('REPLACE')\">" + "<p>TEST</p>\n"
		+ "</body>\n" + "</html>";
		
		String successMsg ="";
		if (isMultipart) { 
			// import			
			String filePath = System.getenv("VENICE_HOME") + "/files/import/fraud-attachment/";
			ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem> fileItemsList = null;
			String fileName = "";
			
			// Parse the request
			if (status.equals(true)) {
				try {
					 _log.debug("Parse the file");
					fileItemsList = servletFileUpload.parseRequest(request);
					for (FileItem item : fileItemsList) {
						// process only file upload - discard other form item types
						if (item.isFormField())
							continue;

						fileName = item.getName();
						if (fileName != null) {
							fileName = FilenameUtils.getName(fileName);
						}
					}
				} catch (Exception e) {
					status = false;
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while parsing the file upload: "+ e.getMessage());
					String errMsg = "An exception occured when parsing the file upload:"+ e.getMessage();
					e.printStackTrace();
					 _log.error(errMsg);
					notificationText = notificationText.replaceFirst("REPLACE",	errMsg);
					response.getOutputStream().println(notificationText);
				}
			}

			if (status.equals(true)) {
				_log.info("Opening file for writing:" + filePath + fileName);			
				File uploadedFile = new File(filePath + fileName);
				Iterator<FileItem> it = fileItemsList.iterator();
				while (it.hasNext()) {
					FileItem fileItem = (FileItem) it.next();
					if (!fileItem.isFormField()) {
						try {
							if (uploadedFile.createNewFile()) {
								_log.debug("Write the file:" + filePath + fileName);
								fileItem.write(uploadedFile);
								response.setStatus(HttpServletResponse.SC_CREATED);
								response.flushBuffer();
								} else{
									status=false;
									String errMsg = "The file name already exists in server:" + fileName;
									notificationText = notificationText.replaceFirst("REPLACE",	errMsg);
									response.getOutputStream().println(notificationText);
							}
						} catch (Exception e) {
							status=false;
							response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An exception occured when writing file upload : " + e.getMessage());
							String errMsg = "An exception occured when writing file upload:" + fileName + " :" + e.getMessage();
							e.printStackTrace();
							_log.error(errMsg);
							notificationText = notificationText.replaceFirst("REPLACE", errMsg);
							response.getOutputStream().println(notificationText);
						}
					}
				}
			}

			//save data ke database disini
			if(status.equals(true)){
				String caseId = request.getParameter("caseid");
				String desc = request.getParameter("desc");				
				try {				
					Locator<FrdFraudFileAttachment> attachmentLocator=null;
					try{
						attachmentLocator = new Locator<FrdFraudFileAttachment>();
						FrdFraudFileAttachmentSessionEJBRemote sessionHome = (FrdFraudFileAttachmentSessionEJBRemote) attachmentLocator.lookup(FrdFraudFileAttachmentSessionEJBRemote.class, "FrdFraudFileAttachmentSessionEJBBean");
						
						FrdFraudFileAttachment attachment = new FrdFraudFileAttachment();
						FrdFraudSuspicionCase frdFraudSuspicionCase = new FrdFraudSuspicionCase();
						frdFraudSuspicionCase.setFraudSuspicionCaseId(new Long(caseId));
						attachment.setFrdFraudSuspicionCase(frdFraudSuspicionCase);
						attachment.setCreatedBy(Util.getUserName(request));
						attachment.setFraudFileAttachmentDescription(desc);
						attachment.setFileLocation(filePath+fileName);
						attachment.setFileName(fileName);
						attachment = sessionHome.persistFrdFraudFileAttachment(attachment);
					} catch(Exception e){
						status=false;
						e.printStackTrace();
					}finally{
						try{
							if(attachmentLocator!=null){
								attachmentLocator.close();
							}
						}catch(Exception e){
							status=false;
							e.printStackTrace();
						}
					}			
				} catch (Exception e) {
					status=false;
					String errMsg = "An exception occured when uploading the attachment. Please contact the system administrator.";
					_log.error(errMsg + e.getMessage());
					e.printStackTrace();
					notificationText = notificationText.replaceFirst("REPLACE",	errMsg);
					response.getOutputStream().println(notificationText);
				}
			}
			
			if(status.equals(true)){
				successMsg = "File uploaded successfully. Please refresh the page.";
				notificationText = notificationText.replaceFirst("REPLACE",	successMsg);
				response.getOutputStream().println(notificationText);
			}else if(status.equals(false)){
				successMsg = "File upload failed.";
				notificationText = notificationText.replaceFirst("REPLACE",	successMsg);
				response.getOutputStream().println(notificationText);
			}
			
			//set the status back to true for another upload without refreshing the page.
			status=true;
		}else {
			response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Request contents type is not supported by the servlet.");
			}
	}
}
