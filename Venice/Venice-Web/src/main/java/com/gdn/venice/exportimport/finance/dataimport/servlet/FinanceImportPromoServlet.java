package com.gdn.venice.exportimport.finance.dataimport.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.VenPromotionSessionEJBRemote;
import com.gdn.venice.exportimport.finance.dataimport.PromotionRecord;
import com.gdn.venice.exportimport.logistics.dataimport.LogisticsServletConstants;
import com.gdn.venice.hssf.ExcelToPojo;
import com.gdn.venice.hssf.PojoInterface;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.persistence.VenPromotionType;

/**
 * Servlet implementation class FinanceImportPromoServlet
 */
public class FinanceImportPromoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
	private Integer errorRowNumber = 1;
	protected static Logger _log = null;	
	private String notificationText = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinanceImportPromoServlet() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportPromoServlet");
    }

    @SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.debug("FinanceImportPromoServlet:hello");
		
		notificationText = "<html>\n" + "<head>"
		+ "<title>Processing Promo Code Completed</title>" + "</head>\n"
		+ "<body onload=\"alert('REPLACE')\">" + "<p>TEST</p>\n"
		+ "</body>\n" + "</html>";
				
		if (isMultipart) { // import			
	    	String filePath = System.getenv("VENICE_HOME") + "/files/import/finance/";
			SimpleDateFormat sdf = new SimpleDateFormat(LogisticsServletConstants.DATE_TIME_FORMAT_STRING);
			String fileName = sdf.format(new Date()) + "-promo.xls";

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
				notificationText = notificationText.replaceFirst("REPLACE", errMsg);
				response.getOutputStream().println(notificationText);
			}
			
			Iterator<FileItem> it = fileItemsList.iterator();
			while (it.hasNext()) {
				FileItem fileItem = (FileItem) it.next();

				if (!fileItem.isFormField()) {
					try {
						fileItem.write(uploadedFile);
					} catch (Exception e) {
						String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_UPLOAD_FILE_WRITE + fileName + " :" + e.getMessage();
						e.printStackTrace();
						_log.error(errMsg);
						notificationText = notificationText.replaceFirst("REPLACE", errMsg);
						response.getOutputStream().println(notificationText);
					}
				}
			}
			
			ExcelToPojo x = null;
			try {
				x = new ExcelToPojo(PromotionRecord.class, System.getenv("VENICE_HOME") + LogisticsServletConstants.TEMPLATE_FOLDER + "PromotionRecord.xml", filePath + fileName, 0, 0);
				x = x.getPojoToExcel(1, "","");
			} catch (Exception e1) {
				String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_FILE_PARSE + e1.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber()!= null ?x.getErrorRowNumber():"1" + "\n");
				_log.error(errMsg);
				e1.printStackTrace();
				notificationText = notificationText.replaceFirst("REPLACE", errMsg.replace("\"", " "));
				PrintWriter out = response.getWriter();
				out.print(notificationText);
				out.flush();
			}

			ArrayList<PojoInterface> result = x.getPojoResult();
			
			try {
				if(!result.isEmpty()){
			    	int promoType = Integer.parseInt(request.getParameter("type").toString());
					
					_log.info("Saving uploaded promo, Promo Type : " + promoType);
			    	
					Locator<VenPromotion> venPromotionLocator=null;
			    	try {
						venPromotionLocator = new Locator<VenPromotion>();
						VenPromotionSessionEJBRemote venPromotionSessionHome = (VenPromotionSessionEJBRemote) venPromotionLocator.lookup(VenPromotionSessionEJBRemote.class, "VenPromotionSessionEJBBean");
			    	
						List<VenPromotion> promotionList = null;
						
						VenPromotionType venPromotionType = new VenPromotionType();
						venPromotionType.setPromotionType(new Long(promoType));
					
						for (PojoInterface element : result) {
							PromotionRecord promotionRecord = (PromotionRecord) element;
							VenPromotion venPromotion = new VenPromotion();
											
							promotionList = venPromotionSessionHome.queryByRange("select o from VenPromotion o where o.promotionCode='"+promotionRecord.getPromoCode()+"'", 0, 0);
								
							_log.debug("promo size: "+promotionList.size());
							if(promotionList.size()>0){
								_log.debug("promo code found, update the promo type for promo code: "+promotionRecord.getPromoCode());
								for(int i=0;i<promotionList.size();i++){
									venPromotion = promotionList.get(i);
									venPromotion.setVenPromotionType(venPromotionType);
									venPromotionSessionHome.mergeVenPromotion(venPromotion);	
								}
							}else{
								venPromotion.setPromotionCode(promotionRecord.getPromoCode());
								venPromotion.setVenPromotionType(venPromotionType);
								venPromotionSessionHome.persistVenPromotion(venPromotion);	
							}
						} 
					} catch (Exception e) {
						String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_UPLOAD_EXCEPTION + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
						_log.error(errMsg);
						e.printStackTrace();
						notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));
						response.getOutputStream().println(notificationText);
					}finally{
						try{
							if(venPromotionLocator!=null){
								venPromotionLocator.close();
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}			
				}else{
					throw new Exception(FinanceImportServletConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
				}
				String successMsg = "Promo Code uploaded successfully";
				notificationText = notificationText.replaceFirst("REPLACE",	successMsg);
			} catch (Exception e) {
				String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_UPLOAD_EXCEPTION + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
				_log.error(errMsg);
				e.printStackTrace();
				notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));
				response.getOutputStream().println(notificationText);
			}		
		}
		response.getOutputStream().println(notificationText);
    }
}
