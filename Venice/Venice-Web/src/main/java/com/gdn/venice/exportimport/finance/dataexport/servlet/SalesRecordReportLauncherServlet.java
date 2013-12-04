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
import com.gdn.venice.exportimport.finance.dataexport.SalesRecordExport;

/**
 * Servlet implementation class SalesRecordReportLauncherServlet
 */
public class SalesRecordReportLauncherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String BASE_FILE_NAME = "SalesRecord";
	protected static Logger _log = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalesRecordReportLauncherServlet() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.report.servlet.SalesRecordReportLauncherServlet");
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
	
	private List<String> convertStringDelimiterToList(String delimitedString){
		
		String[] string = delimitedString.split(";");
		List<String> valueList = new ArrayList<String>(); 
		
		for(String value:string){
			valueList.add(value);
		}
		
		return valueList;
	}
	
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String salesRecordIds = request.getParameter("salesRecordIds");
		
		List<String> salesRecordIdList = convertStringDelimiterToList(salesRecordIds);
	
		OutputStream out = null;
		
		try{
				
			String outputFileName = BASE_FILE_NAME + System.currentTimeMillis() + ".xls";
			
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition",  "attachment; filename="+outputFileName);
		   
		    HSSFWorkbook wb = new HSSFWorkbook();
		    HSSFSheet sheet = wb.createSheet(BASE_FILE_NAME);	    
		 
		     out = response.getOutputStream();	
		     SalesRecordExport salesRecordExport = new SalesRecordExport(wb);
		    
		     wb  =  salesRecordExport.exportExcel(salesRecordIdList, sheet);     
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
