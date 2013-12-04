package com.gdn.venice.exportimport.logistics.dataexport.servlet;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Servlet implementation class InvoiceReportExportServlet.
 * 
 * @author Roland
 * 
 */
@SuppressWarnings("deprecation")
public class InvoiceReportExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;

	public static long LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_WEIGHTMISMATCH = 2;
	public static long LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_PRICEPERKGMISMATCH = 3;
	public static long LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_INSURANCECOSTMISMATCH = 4;
	public static long LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_OTHERCHARGEMISMATCH = 5;
	public static long LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_GIFTWRAPHARGEMISMATCH = 6;
	public static long LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_TOTALCHARGEMISMATCH = 7;
	public static long LOG_ACTION_APPLIED_VENICE = 0;
	public static long LOG_ACTION_APPLIED_PROVIDER = 1;
	public static long LOG_ACTION_APPLIED_MANUAL = 2;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InvoiceReportExportServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.logistics.dataexport.InvoiceReportExportServlet");
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		_log.info("start InvoiceReportExportServlet");
		
		_log.debug("parameter");
		List<LogInvoiceAirwaybillRecord> airwayBillRecordList = new ArrayList<LogInvoiceAirwaybillRecord>();
		List<LogInvoiceReconRecord> reconRecordList = new ArrayList<LogInvoiceReconRecord>();
		
		Locator<Object> locator = null;
		OutputStream out = null;

		String logistic = (String) request.getParameter("logistic");
		String invoiceNumber = (String) request.getParameter("invoiceNumber");
		String recon = (String) request.getParameter("recon");

		if (recon.equals("null"))
			recon = "All";

		_log.debug("logistic: " + logistic);
		_log.debug("recon: " + recon);

		try {
			String shortname = "InvoiceReconReport"	+ System.currentTimeMillis() + ".xls";
			locator = new Locator<Object>();

			LogInvoiceAirwaybillRecordSessionEJBRemote airwayBillRecordSessionHome = (LogInvoiceAirwaybillRecordSessionEJBRemote) locator
					.lookup(LogInvoiceAirwaybillRecordSessionEJBRemote.class, "LogInvoiceAirwaybillRecordSessionEJBBean");
			LogInvoiceReconRecordSessionEJBRemote reconRecordSessionHome = (LogInvoiceReconRecordSessionEJBRemote) locator
			.lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");

			_log.debug("query for get airway bill list");
			String queryAirwayBill;

			if (recon.equalsIgnoreCase("all")) {
				queryAirwayBill = "select o from LogInvoiceAirwaybillRecord o join fetch o.logAirwayBills where o.logInvoiceReportUpload.invoiceNumber = '"+ invoiceNumber+ "' and o.logInvoiceReportUpload.logLogisticsProvider.logisticsProviderCode = '"+ logistic + "'";
			} else {
				queryAirwayBill = "select o from LogInvoiceAirwaybillRecord o join fetch o.logAirwayBills where o.logInvoiceReportUpload.invoiceNumber = '"+ invoiceNumber+ "' and o.logInvoiceReportUpload.logLogisticsProvider.logisticsProviderCode = '"+ logistic+ "' and o.invoiceResultStatus = 'Problem Exists'";
			}

			_log.debug("queryAirwayBill: " + queryAirwayBill);

			airwayBillRecordList = airwayBillRecordSessionHome.queryByRange(queryAirwayBill, 0, 0);

			_log.debug("airwayBillRecordList size: " + airwayBillRecordList.size());
			_log.debug("start looping to fill bean");

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+ shortname);

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("InvoiceReport");

			// Create the column headings
			HSSFRow header1 = sheet.createRow((short) 0);
			header1.createCell(0).setCellValue(new HSSFRichTextString("Invoice Number: " + invoiceNumber));
			HSSFRow header2 = sheet.createRow((short) 1);
			header2.createCell(0).setCellValue(new HSSFRichTextString("Logistic Provider: " + logistic));
			HSSFRow header3 = sheet.createRow((short) 2);
			header3.createCell(0).setCellValue(new HSSFRichTextString("Recon Status: " + recon));
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));
			HSSFRow headertgl = sheet.createRow((short) 4);

			// style
			CellStyle headerCellstyle = wb.createCellStyle();
			headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
			headerCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
			headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
			headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
			headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);

			CellStyle detailCellstyle = wb.createCellStyle();
			detailCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
			detailCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			detailCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
			detailCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			detailCellstyle.setBorderRight(CellStyle.BORDER_THIN);
			detailCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			detailCellstyle.setBorderTop(CellStyle.BORDER_THIN);
			detailCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			detailCellstyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			detailCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			detailCellstyle.setAlignment(CellStyle.ALIGN_CENTER);

			int startRow = 6;
			int startCol = 0;


			headertgl.createCell(startCol).setCellValue(new HSSFRichTextString("AWB Number"));
			// set style for header
			HSSFCell cell = headertgl.getCell(startCol);
			cell.setCellStyle(headerCellstyle);
			
			headertgl.createCell(startCol + 1).setCellValue(new HSSFRichTextString("Reconcile Status"));
			// set style for header
			cell = headertgl.getCell(startCol + 1);
			cell.setCellStyle(headerCellstyle);
			
			headertgl.createCell(startCol + 2).setCellValue(new HSSFRichTextString("GDN Ref No"));
			// set style for header
			cell = headertgl.getCell(startCol + 2);
			cell.setCellStyle(headerCellstyle);
			
			headertgl.createCell(startCol + 3).setCellValue(new HSSFRichTextString("Weight"));
			// set style for header
			cell = headertgl.getCell(startCol + 3);
			cell.setCellStyle(headerCellstyle);

			headertgl.createCell(startCol + 8).setCellValue(new HSSFRichTextString("Price/Kg"));
			// set style for header
			cell = headertgl.getCell(startCol + 8);
			cell.setCellStyle(headerCellstyle);

			headertgl.createCell(startCol + 13).setCellValue(new HSSFRichTextString("Other Charge"));
			// set style for header
			cell = headertgl.getCell(startCol + 13);
			cell.setCellStyle(headerCellstyle);

			headertgl.createCell(startCol + 18).setCellValue(new HSSFRichTextString("Gift Wrap Charge"));
			// set style for header
			cell = headertgl.getCell(startCol + 18);
			cell.setCellStyle(headerCellstyle);
			
			headertgl.createCell(startCol + 23).setCellValue(new HSSFRichTextString("Insurance Charge"));
			// set style for header
			cell = headertgl.getCell(startCol + 23);
			cell.setCellStyle(headerCellstyle);

			headertgl.createCell(startCol + 28).setCellValue(new HSSFRichTextString("Total Charge"));
			// set style for header
			cell = headertgl.getCell(startCol + 28);
			cell.setCellStyle(headerCellstyle);

			headertgl.createCell(startCol + 33).setCellValue(new HSSFRichTextString("Failure"));
			// set style for header
			cell = headertgl.getCell(startCol + 33);
			cell.setCellStyle(headerCellstyle);
			
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,startRow - 1, startCol, startCol));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,startRow - 1, startCol + 1, startCol + 1));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,startRow - 1, startCol + 2, startCol + 2));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,startRow - 2, startCol + 3, startCol + 7));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,startRow - 2, startCol + 8, startCol + 12));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,startRow - 2, startCol + 13, startCol + 17));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,startRow - 2, startCol + 18, startCol + 22));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,	startRow - 2, startCol + 23, startCol + 27));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,	startRow - 2, startCol + 28, startCol + 32));
			sheet.addMergedRegion(new CellRangeAddress(startRow - 2,	startRow - 1, startCol + 33, startCol + 33));

			HSSFRow headerRow = sheet.createRow((short) startRow-1);
			
			headerRow.createCell(startCol + 3).setCellValue(new HSSFRichTextString("venice"));
			headerRow.createCell(startCol + 4).setCellValue(new HSSFRichTextString("logistic"));
			headerRow.createCell(startCol + 5).setCellValue(new HSSFRichTextString("manual"));
			headerRow.createCell(startCol + 6).setCellValue(new HSSFRichTextString("applied"));
			headerRow.createCell(startCol + 7).setCellValue(new HSSFRichTextString("comment"));
			
			headerRow.createCell(startCol + 8).setCellValue(new HSSFRichTextString("venice"));
			headerRow.createCell(startCol + 9).setCellValue(new HSSFRichTextString("logistic"));
			headerRow.createCell(startCol + 10).setCellValue(new HSSFRichTextString("manual"));
			headerRow.createCell(startCol + 11).setCellValue(new HSSFRichTextString("applied"));
			headerRow.createCell(startCol + 12).setCellValue(new HSSFRichTextString("comment"));
			
			headerRow.createCell(startCol + 13).setCellValue(new HSSFRichTextString("venice"));
			headerRow.createCell(startCol + 14).setCellValue(new HSSFRichTextString("logistic"));
			headerRow.createCell(startCol + 15).setCellValue(new HSSFRichTextString("manual"));
			headerRow.createCell(startCol + 16).setCellValue(new HSSFRichTextString("applied"));
			headerRow.createCell(startCol + 17).setCellValue(new HSSFRichTextString("comment"));
			
			headerRow.createCell(startCol + 18).setCellValue(new HSSFRichTextString("venice"));
			headerRow.createCell(startCol + 19).setCellValue(new HSSFRichTextString("logistic"));
			headerRow.createCell(startCol + 20).setCellValue(new HSSFRichTextString("manual"));
			headerRow.createCell(startCol + 21).setCellValue(new HSSFRichTextString("applied"));
			headerRow.createCell(startCol + 22).setCellValue(new HSSFRichTextString("comment"));
			
			headerRow.createCell(startCol + 23).setCellValue(new HSSFRichTextString("venice"));
			headerRow.createCell(startCol + 24).setCellValue(new HSSFRichTextString("logistic"));
			headerRow.createCell(startCol + 25).setCellValue(new HSSFRichTextString("manual"));
			headerRow.createCell(startCol + 26).setCellValue(new HSSFRichTextString("applied"));
			headerRow.createCell(startCol + 27).setCellValue(new HSSFRichTextString("comment"));
			
			headerRow.createCell(startCol + 28).setCellValue(new HSSFRichTextString("venice"));
			headerRow.createCell(startCol + 29).setCellValue(new HSSFRichTextString("logistic"));
			headerRow.createCell(startCol + 30).setCellValue(new HSSFRichTextString("manual"));
			headerRow.createCell(startCol + 31).setCellValue(new HSSFRichTextString("applied"));
			headerRow.createCell(startCol + 32).setCellValue(new HSSFRichTextString("comment"));

			// set style for header
			for (int i = startCol+3; i <= startCol + 32; i++) {
				cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
			}
			
			// number for storing the detail header, to add color later
			int rowNumber = 1;

			String gdnRef;
			
			_log.debug("looping for generating excel rows");
			// Looping for generating excel rows
			HSSFRow row = null;
			for (int i = 0; i < airwayBillRecordList.size(); i++) {
				_log.debug("looping airway bill");
				LogInvoiceAirwaybillRecord awb = airwayBillRecordList.get(i);

				row = sheet.createRow(startRow+i);

				gdnRef = "";
				for(int j = 0; j<awb.getLogAirwayBills().size(); j++){
					if(!gdnRef.equals("")){
						gdnRef = gdnRef.concat(";");
					}
					
					LogAirwayBill awbill = awb.getLogAirwayBills().get(j);
					gdnRef = gdnRef.concat(awbill.getVenOrderItem().getVenOrder().getRmaFlag()?"R-":"O-")
						.concat(awbill.getVenOrderItem().getVenOrder().getWcsOrderId()).concat("-")
						.concat(awbill.getVenOrderItem().getWcsOrderItemId()).concat("-")
						.concat(awbill.getSequence().toString());
				}
				
				cell = row.createCell(startCol);
				cell.setCellValue(new HSSFRichTextString(awb.getAirwayBillNumber()));
				cell = row.createCell(startCol + 1);
				cell.setCellValue(new HSSFRichTextString(awb.getInvoiceResultStatus()));
				cell = row.createCell(startCol + 2);
				cell.setCellValue(new HSSFRichTextString(gdnRef));

				if(recon.equalsIgnoreCase("all")){
					cell = row.createCell(startCol + 3);
					cell.setCellValue(new HSSFRichTextString(awb.getVenicePackageWeight().toString()));
					cell = row.createCell(startCol + 4);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderPackageWeight().toString()));
					cell = row.createCell(startCol + 5);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 6);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderPackageWeight().toString()));
					cell = row.createCell(startCol + 7);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 8);
					cell.setCellValue(new HSSFRichTextString(awb.getVenicePricePerKg().toString()));
					cell = row.createCell(startCol + 9);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderPricePerKg().toString()));
					cell = row.createCell(startCol + 10);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 11);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderPricePerKg().toString()));
					cell = row.createCell(startCol + 12);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 13);
					cell.setCellValue(new HSSFRichTextString(awb.getVeniceOtherCharge().toString()));
					cell = row.createCell(startCol + 14);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderOtherCharge().toString()));
					cell = row.createCell(startCol + 15);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 16);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderOtherCharge().toString()));
					cell = row.createCell(startCol + 17);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 18);
					cell.setCellValue(new HSSFRichTextString(awb.getVeniceGiftWrapCharge().toString()));
					cell = row.createCell(startCol + 19);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderGiftWrapCharge().toString()));
					cell = row.createCell(startCol + 20);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 21);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderGiftWrapCharge().toString()));
					cell = row.createCell(startCol + 22);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 23);
					cell.setCellValue(new HSSFRichTextString(awb.getVeniceInsuranceCharge().toString()));
					cell = row.createCell(startCol + 24);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderInsuranceCharge().toString()));
					cell = row.createCell(startCol + 25);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 26);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderInsuranceCharge().toString()));
					cell = row.createCell(startCol + 27);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 28);
					cell.setCellValue(new HSSFRichTextString(awb.getVeniceTotalCharge().toString()));
					cell = row.createCell(startCol + 29);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderTotalCharge().toString()));
					cell = row.createCell(startCol + 30);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 31);
					cell.setCellValue(new HSSFRichTextString(awb.getProviderTotalCharge().toString()));
					cell = row.createCell(startCol + 32);
					cell.setCellValue(new HSSFRichTextString(""));
				} else {
					cell = row.createCell(startCol + 3);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 4);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 5);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 6);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 7);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 8);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 9);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 10);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 11);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 12);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 13);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 14);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 15);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 16);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 17);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 18);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 19);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 20);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 21);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 22);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 23);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 24);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 25);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 26);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 27);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 28);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 29);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 30);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 31);
					cell.setCellValue(new HSSFRichTextString(""));
					cell = row.createCell(startCol + 32);
					cell.setCellValue(new HSSFRichTextString(""));
				}
				
				String failure = "";
				
				reconRecordList = reconRecordSessionHome.queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId="+awb.getInvoiceAirwaybillRecordId(), 0, 0);
				
				for (LogInvoiceReconRecord reconRecord : reconRecordList) {
					Long resultId = reconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId();
					String applied = "";
					if(reconRecord.getLogActionApplied() != null){
						if(reconRecord.getLogActionApplied().getActionAppliedId() ==  LOG_ACTION_APPLIED_VENICE)
							applied = reconRecord.getVeniceData();
						else if(reconRecord.getLogActionApplied().getActionAppliedId() ==  LOG_ACTION_APPLIED_PROVIDER)
							applied = reconRecord.getProviderData();
						else if(reconRecord.getLogActionApplied().getActionAppliedId() ==  LOG_ACTION_APPLIED_MANUAL)
							applied = reconRecord.getManuallyEnteredData();
					}
					
					if ( resultId == LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_WEIGHTMISMATCH) {
						failure = failure.concat(failure.equals("")?"weight":", weight");
						cell = row.createCell(startCol + 3);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getVeniceData()));
						cell = row.createCell(startCol + 4);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getProviderData()));
						cell = row.createCell(startCol + 5);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getManuallyEnteredData()));
						cell = row.createCell(startCol + 6);
						cell.setCellValue(new HSSFRichTextString(applied));
						cell = row.createCell(startCol + 7);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getComment()));
					} else if ( resultId == LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_PRICEPERKGMISMATCH) {
						failure = failure.concat(failure.equals("")?"price per kg":", price per kg");
						cell = row.createCell(startCol + 8);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getVeniceData()));
						cell = row.createCell(startCol + 9);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getProviderData()));
						cell = row.createCell(startCol + 10);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getManuallyEnteredData()));
						cell = row.createCell(startCol + 11);
						cell.setCellValue(new HSSFRichTextString(applied));
						cell = row.createCell(startCol + 12);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getComment()));
					} else if ( resultId == LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_OTHERCHARGEMISMATCH) {
						failure = failure.concat(failure.equals("")?"other charge":", other charge");
						cell = row.createCell(startCol + 13);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getVeniceData()));
						cell = row.createCell(startCol + 14);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getProviderData()));
						cell = row.createCell(startCol + 15);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getManuallyEnteredData()));
						cell = row.createCell(startCol + 16);
						cell.setCellValue(new HSSFRichTextString(applied));
						cell = row.createCell(startCol + 17);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getComment()));
					} else if ( resultId == LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_GIFTWRAPHARGEMISMATCH) {
						failure = failure.concat(failure.equals("")?"gift wrap charge":", gift wrap charge");
						cell = row.createCell(startCol + 18);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getVeniceData()));
						cell = row.createCell(startCol + 19);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getProviderData()));
						cell = row.createCell(startCol + 20);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getManuallyEnteredData()));
						cell = row.createCell(startCol + 21);
						cell.setCellValue(new HSSFRichTextString(applied));
						cell = row.createCell(startCol + 22);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getComment()));
					} else if ( resultId == LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_INSURANCECOSTMISMATCH) {
						failure = failure.concat(failure.equals("")?"insurance charge":", insurance charge");
						cell = row.createCell(startCol + 23);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getVeniceData()));
						cell = row.createCell(startCol + 24);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getProviderData()));
						cell = row.createCell(startCol + 25);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getManuallyEnteredData()));
						cell = row.createCell(startCol + 26);
						cell.setCellValue(new HSSFRichTextString(applied));
						cell = row.createCell(startCol + 27);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getComment()));
					} else if ( resultId == LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_TOTALCHARGEMISMATCH) {
						failure = failure.concat(failure.equals("")?"total charge":", total charge");
						cell = row.createCell(startCol + 28);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getVeniceData()));
						cell = row.createCell(startCol + 29);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getProviderData()));
						cell = row.createCell(startCol + 30);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getManuallyEnteredData()));
						cell = row.createCell(startCol + 31);
						cell.setCellValue(new HSSFRichTextString(applied));
						cell = row.createCell(startCol + 32);
						cell.setCellValue(new HSSFRichTextString(reconRecord.getComment()));
					}
				}
				
				if(!failure.equals("")){
					failure = "Mismatch data: "+ failure;
				}
				cell = row.createCell(startCol + 33);
				cell.setCellValue(new HSSFRichTextString(failure));
				
				// set style for airway bill list
				for (int l = startCol; l <= startCol + 33; l++) {
					cell = row.getCell(l);
					cell.setCellStyle(detailCellstyle);
				}
				rowNumber++;
			}
			
			for (int l = startCol; l <= startCol + 33; l++) {
				sheet.autoSizeColumn(l);
			}

			// write to stream
			_log.debug("write to stream");
			out = response.getOutputStream();
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				locator.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
