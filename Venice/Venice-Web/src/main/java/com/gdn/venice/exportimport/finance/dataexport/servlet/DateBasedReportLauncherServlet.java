package com.gdn.venice.exportimport.finance.dataexport.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.ReportLauncher;
import com.gdn.venice.exportimport.finance.dataexport.FundInRecordRecon;
import com.gdn.venice.exportimport.finance.dataexport.RefundReportExport;

/**
 * Servlet implementation class DateBasedReportLauncherServlet
 */
public class DateBasedReportLauncherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;
	protected String reportFileName = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DateBasedReportLauncherServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.report.servlet.DateBasedReportLauncherServlet");
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		/*
		 * Note that dates passed as HTTP parameters from development mode in
		 * SmartClient may be formatted differently to dates passed from the
		 * deployed EAR
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
		Date fromDate = null;
		Date toDate = null;
		if (request.getParameter("fromDate") == null
				|| request.getParameter("fromDate").isEmpty()
				|| request.getParameter("toDate") == null
				|| request.getParameter("toDate").isEmpty()
				|| request.getParameter("reportFileName") == null
				|| request.getParameter("reportFileName").isEmpty()) {
			response.getWriter().write("DateBasedReportLauncherServlet Usage: DateBasedReportLauncherServlet?fromDate=EEE MMM dd hhh:mm:ss z yyyy&toDate=EEE MMM dd hhh:mm:ss z yyyy&reportFileName=filename.pdf");
			response.flushBuffer();
			return;
		}
		
	
		try {
			/*
			 * Must handle the two possible timezone formats (GMT *00 or
			 * WIB/WITA/WIT) 
			 * o timezones sent over HTTP params can be different in 
			 * development mode vs deployed on the AS because of 
			 * browser settings and Smart GWT
			 */
			String sFromDate = request.getParameter("fromDate");
			String sToDate = request.getParameter("toDate");
			if (sFromDate.contains("GMT 700") || sToDate.contains("GMT 700")) {
				sFromDate = sFromDate.replace("GMT 700", "WIT");
				sToDate = sToDate.replace("GMT 700", "WIT");
			}else if (sFromDate.contains("GMT 800") || sToDate.contains("GMT 800")){
				sFromDate = sFromDate.replace("GMT 800", "WITA");
				sToDate = sToDate.replace("GMT 800", "WITA");				
			}else if (sFromDate.contains("GMT 900") || sToDate.contains("GMT 900")){
				sFromDate = sFromDate.replace("GMT 900", "WIB");
				sToDate = sToDate.replace("GMT 900", "WIB");				
			}
				
			fromDate = sdf.parse(sFromDate);
			toDate = sdf.parse(sToDate);
			reportFileName = request.getParameter("reportFileName");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		_log.debug("Hello DateBasedReportLauncherServlet From Date:" + fromDate + "To Date:" + toDate);		
		if(reportFileName.equals("RefundReport")){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("fromDate", fromDate);
			params.put("toDate", toDate);		
		
			OutputStream out = null;
			try{

			String outputFileName = reportFileName +System.currentTimeMillis()+ ".xls";
			
			response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition",  "attachment; filename="+outputFileName);
		   
		    HSSFWorkbook wb = new HSSFWorkbook();
		    HSSFSheet sheet = wb.createSheet(""+reportFileName);	    
		 
		     out = response.getOutputStream();	
		     RefundReportExport refundReportExport = new RefundReportExport(wb);
		    
		     wb  =  refundReportExport.ExportExcel(params, sheet);     
		     wb.write(out);
		     out.close();
			
			} catch (Exception e)   {
				throw new ServletException("Exception in Excel Sample Servlet", e);
		    } finally   {
			     if (out != null)
			      out.close();		    
					
		    }
		}else if (!reportFileName.equals("FundsInReconciliationReport") ) {
		
		ReportLauncher launcher = new ReportLauncher();
		launcher.setDbDriver("org.postgresql.Driver");
		launcher.setDbPwd("password");
		launcher.setDbUname("venice");
		launcher.setDbUrl("jdbc:postgresql:venice");
		launcher.setJasperFileName(reportFileName + ".jasper");
		launcher.setJrxmlFileName(reportFileName + ".jrxml");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromDate", fromDate);
		params.put("toDate", toDate);

		launcher.setParams(params);
		String outputFileName = reportFileName + +System.currentTimeMillis() + ".pdf";
		launcher.setPdfFileName(outputFileName);
		launcher.outputToPDF();

		response.setHeader("Content-length", "" + String.valueOf(new File(launcher.getPdfFileFullPath()).length()));
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + outputFileName + "\"");

		FileInputStream inputStream = new FileInputStream(launcher.getPdfFileFullPath());
		ServletOutputStream stream = response.getOutputStream();

		BufferedInputStream buf = new BufferedInputStream(inputStream);
		int readBytes = 0;

		try {
			// read from the file; write to the ServletOutputStream
			while ((readBytes = buf.read()) != -1)
				stream.write(readBytes);
		} catch (IOException ioe) {
			throw new ServletException(ioe.getMessage());
		} finally {
			if (stream != null)
				stream.close();
			if (buf != null)
				buf.close();
		}
	}else if (reportFileName.equals("FundsInReconciliationReport")){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromDate", fromDate);
		params.put("toDate", toDate);		
	
		OutputStream out = null;
		try{
				
		String outputFileName = reportFileName +System.currentTimeMillis() + ".xls";
		
		response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-Disposition",  "attachment; filename="+outputFileName);
	   
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet(""+reportFileName);	    
	 
	     out = response.getOutputStream();	
	     FundInRecordRecon fundInRecordRecon = new FundInRecordRecon(wb);
	    
	     wb  =  fundInRecordRecon.ExportExcel(params, sheet);     
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
}
