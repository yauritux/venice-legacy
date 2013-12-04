package com.gdn.venice.exportimport.logistics.dataimport.servlet;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.finance.dataimport.servlet.FinanceImportServletConstants;
import com.gdn.venice.exportimport.logistics.dataimport.InventoryRecord;
import com.gdn.venice.exportimport.logistics.dataimport.LogisticsServletConstants;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.hssf.ExcelToPojo;
import com.gdn.venice.hssf.PojoInterface;
import com.gdn.venice.persistence.VenMerchantProduct;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

/**
 * Servlet implementation class LogisticsInventoryImportServlet
 */
public class LogisticsInventoryImportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private Integer errorRowNumber = 1;

	protected static Logger _log = null;
	
	private String notificationText = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogisticsInventoryImportServlet() {
    	super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataimport.servlet.LogisticsInventoryImportServlet");
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.debug("LogisticsInventoryImportServlet:hello");
		
		notificationText = LogisticsServletConstants.JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT;
		
		if (isMultipart) { // import
			
			process(request, response);
			
		}
    }
    
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String filePath = System.getenv("VENICE_HOME") + "/files/import/logistics/";
		SimpleDateFormat sdf = new SimpleDateFormat(LogisticsServletConstants.DATE_TIME_FORMAT_STRING);
		String fileName = sdf.format(new Date()) + "-inventory.xls";
		
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
			x = new ExcelToPojo(InventoryRecord.class, System.getenv("VENICE_HOME") + LogisticsServletConstants.TEMPLATE_FOLDER + "InventoryRecord.xml", filePath + fileName, 0, 0);
			x = x.getPojo();
		} catch (Exception e1) {
			String errMsg = LogisticsServletConstants.EXCEPTION_TEXT_FILE_PARSE + e1.getMessage() + ". Processing row number " + (x != null && x.getErrorRowNumber()!= null ?x.getErrorRowNumber():"1" + "\n");
			_log.error(errMsg);
			e1.printStackTrace();
			notificationText = notificationText.replaceFirst("REPLACE", errMsg.replace("\"", " "));
			PrintWriter out = response.getWriter();
			out.print(notificationText);
			out.flush();
			return;
		}
		
		ArrayList<PojoInterface> result = x.getPojoResult();
		
		try {
			if(result.isEmpty()){
				throw new Exception(FinanceImportServletConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS);
			}
		} catch (Exception e) {
			String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_UPLOAD_EXCEPTION + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
			_log.error(errMsg);
			e.printStackTrace();
			notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));
			response.getOutputStream().println(notificationText);
			return;
		}
		
		saveInventory(request, response, result);
		
		String successMsg = "Inventory uploaded successfully... please refresh";
		notificationText = notificationText.replaceFirst("REPLACE", successMsg);
		
		response.getOutputStream().println(notificationText);
		
    }
    
    private void saveInventory(HttpServletRequest request, HttpServletResponse response, ArrayList<PojoInterface> result) throws ServletException, IOException{
    	Locator<VenMerchantProduct> venMerchantProductLocator=null;
    	VenMerchantProductSessionEJBRemote venMerchantProductHome;
    	
    	try {
    		venMerchantProductLocator = new Locator<VenMerchantProduct>();
    		venMerchantProductHome = (VenMerchantProductSessionEJBRemote) venMerchantProductLocator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
    	} catch (Exception e) {
			String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_UPLOAD_EXCEPTION + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
			_log.error(errMsg);
			e.printStackTrace();
			notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));
			response.getOutputStream().println(notificationText);
			return;
		}
    	
    	VenMerchantProduct venMerchantProduct;
    	
    	for (PojoInterface element : result) {
			InventoryRecord inventoryRecord = (InventoryRecord) element;
			
			venMerchantProduct = new VenMerchantProduct();
			venMerchantProduct.setWcsProductSku(inventoryRecord.getProductSKU());
			venMerchantProduct.setWcsProductName(inventoryRecord.getProductName());
			venMerchantProduct.setCostOfGoodsSold(new BigDecimal(inventoryRecord.getCostOfGoodsSold()));
			
			String query = "select o from VenMerchantProduct o where o.wcsProductSku = '" + inventoryRecord.getProductSKU() + "'";
			
			List<VenMerchantProduct> venMerchantProductList = venMerchantProductHome.queryByRange(query, 0, 0);
			
			if(venMerchantProductList.size() == 0){
				venMerchantProductHome.persistVenMerchantProduct(venMerchantProduct);
			}else if(venMerchantProductList.size() == 1){
				// put existing product id to the object to allow merging
				VenMerchantProduct existingVenMerchantProduct = venMerchantProductList.get(0);
//				venMerchantProduct.setProductId(existingVenMerchantProduct.getProductId());				
//				venMerchantProductHome.mergeVenMerchantProduct(venMerchantProduct);
				existingVenMerchantProduct.setWcsProductSku(inventoryRecord.getProductSKU());
				existingVenMerchantProduct.setWcsProductName(inventoryRecord.getProductName());
				existingVenMerchantProduct.setCostOfGoodsSold(new BigDecimal(inventoryRecord.getCostOfGoodsSold()));
				venMerchantProductHome.mergeVenMerchantProduct(existingVenMerchantProduct);
			}
		}
    }

}
