package com.gdn.venice.exportimport.finance.dataexport.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.finance.dataexport.MerchantPaymentExport;

/**
 * Servlet implementation class SalesRecordReportLauncherServlet
 */
public class MerchantPaymentReportLauncherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String BASE_FILE_NAME = "MerchantPaymentReport";
	protected static Logger _log = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MerchantPaymentReportLauncherServlet() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.report.servlet.MerchantPaymentReportLauncherServlet");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}
	
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String apPaymentIds = request.getParameter("apPaymentIds");
		
		OutputStream out = null;
		
		try{
				
			String outputFileName = BASE_FILE_NAME + System.currentTimeMillis() + ".xls";
			
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition",  "attachment; filename="+outputFileName);
		   
		    HSSFWorkbook wb = new HSSFWorkbook();
		    HSSFSheet sheet = wb.createSheet(BASE_FILE_NAME);	    
		 
		     out = response.getOutputStream();	
		     MerchantPaymentExport merchantPaymentExport = new MerchantPaymentExport(wb);
		    
		     wb  =  merchantPaymentExport.exportExcel(apPaymentIds, sheet);     
		     wb.write(out);
		     out.close();
		
		} catch (Exception e)   {
			throw new ServletException("Exception in Excel Sample Servlet", e);
	    } finally   {
		     if (out != null)
		      out.close();		    
	    }
	}

}
