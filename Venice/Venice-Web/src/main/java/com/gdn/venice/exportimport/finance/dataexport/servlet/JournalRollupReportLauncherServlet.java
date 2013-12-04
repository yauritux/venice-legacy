package com.gdn.venice.exportimport.finance.dataexport.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.finance.dataexport.JournalRollupExport;

/**
 * Servlet implementation class JournalRollupReportLauncherServlet
 */
public class JournalRollupReportLauncherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String BASE_FILE_NAME = "Journal";
	protected static Logger _log = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JournalRollupReportLauncherServlet() {
    	super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.report.servlet.JournalRollupReportLauncherServlet");
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
		// TODO Auto-generated method stub
	}
	
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		long journalGroupId = Long.parseLong(request.getParameter("journalGroupId"));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("journalGroupId", journalGroupId);
	
		OutputStream out = null;
		
		try{
				
			String outputFileName = BASE_FILE_NAME + System.currentTimeMillis() + ".xls";
			
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition",  "attachment; filename="+outputFileName);
		   
		    HSSFWorkbook wb = new HSSFWorkbook();
		    HSSFSheet sheet = wb.createSheet(BASE_FILE_NAME);	    
		 
		     out = response.getOutputStream();	
		     JournalRollupExport journalRollupExport = new JournalRollupExport(wb);
		    
		     wb  =  journalRollupExport.exportExcel(params, sheet);     
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
