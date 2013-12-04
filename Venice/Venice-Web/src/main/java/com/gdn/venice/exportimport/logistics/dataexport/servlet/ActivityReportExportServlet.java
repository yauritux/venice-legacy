package com.gdn.venice.exportimport.logistics.dataexport.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.logistics.dataexport.ActivityInvoiceReconRecord;
import com.gdn.venice.exportimport.logistics.dataexport.AirwayBill;
import com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote;
import com.gdn.venice.persistence.LogActivityReconRecord;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;

/**
 * Servlet implementation class ActivityReportExportServlet.
 * 
 * @author Roland
 * 
 */
@SuppressWarnings({ "unused", "deprecation" })
public class ActivityReportExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;
	
	public static long LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_TRACKINGNUMBERDOESNOTEXIST = 0;
	public static long LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_PICKUPDATELATE = 1;
	public static long LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SETTLEMENTCODEMISMATCH= 2;
	public static long LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SERVICEMISMATCH = 3;
	public static long LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_RECIPIENTMISMATCH = 4;
	public static long LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_WEIGHTMISMATCH = 5;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActivityReportExportServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataexport.ActivityReportExportServlet");
	}
	
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		_log.info("start ActivityReportExportServlet");
		String result =null;
		
		_log.debug("parameter");		
		ArrayList<AirwayBill> airwayBillDataList = new ArrayList<AirwayBill>();	
		ArrayList<ActivityInvoiceReconRecord> reconRecordDataList;
		ArrayList<Integer> rowNumTemp = new ArrayList<Integer>();
		
		Locator<LogAirwayBillSessionEJBRemote> airwayBillLocator = null;
		Locator<LogMerchantPickupInstructionSessionEJBRemote> pickupInstructionLocator = null;
		Locator<LogActivityReconRecordSessionEJBRemote> reconRecordLocator = null;
		List<LogActivityReconRecord> activityReconRecordList = null;
		OutputStream out = null;
		
		String logistic = (String) request.getParameter("logistic");
		String approval = (String) request.getParameter("approval");
		String recon = (String) request.getParameter("recon");
		String sFromDate = request.getParameter("date");		
			
		Date fromDate = null;
		SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if (sFromDate.contains("GMT 700")) {
			sFromDate = sFromDate.replace("GMT 700", "WIT");
		}else if (sFromDate.contains("GMT 800")){
			sFromDate = sFromDate.replace("GMT 800", "WITA");		
		}else if (sFromDate.contains("GMT 900")){
			sFromDate = sFromDate.replace("GMT 900", "WIB");		
		}
		
		try {
			fromDate = sdf2.parse(sFromDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
		String dateParam = dateTimestamp.format(fromDate);
	
		if(logistic.equals("null"))
			logistic = "all";
		
		if(approval.equals("null"))
			approval = "all";
		
		if(recon.equals("null"))
			recon = "all";
		
		_log.debug("logistic: "+logistic);
		_log.debug("approval: "+approval);
		_log.debug("recon: "+recon);
		_log.debug("date: "+dateParam);	
      		
		try {
			String shortname="ActivityReport"+System.currentTimeMillis() + ".xls";
			airwayBillLocator = new Locator<LogAirwayBillSessionEJBRemote>();
			reconRecordLocator = new Locator<LogActivityReconRecordSessionEJBRemote>();
			pickupInstructionLocator = new Locator<LogMerchantPickupInstructionSessionEJBRemote>();
			
			LogAirwayBillSessionEJBRemote airwayBillSessionHome = (LogAirwayBillSessionEJBRemote) airwayBillLocator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			LogActivityReconRecordSessionEJBRemote reconRecordSessionHome = (LogActivityReconRecordSessionEJBRemote) reconRecordLocator
			.lookup(LogActivityReconRecordSessionEJBRemote.class, "LogActivityReconRecordSessionEJBBean");
			
			LogMerchantPickupInstructionSessionEJBRemote pickupInstructionHome = (LogMerchantPickupInstructionSessionEJBRemote) pickupInstructionLocator
			.lookup(LogMerchantPickupInstructionSessionEJBRemote.class, "LogMerchantPickupInstructionSessionEJBBean");
			
			_log.debug("query for get airway bill list");
			List<LogAirwayBill> airwayBillList = null;	
			
			String queryAirwayBill = "select o from LogAirwayBill o where o.venOrderItem.venOrder.orderDate>='" + dateParam + "'";
			if(!logistic.equals("all")){				
				queryAirwayBill+=" and o.logLogisticsProvider.logisticsProviderId="+logistic;				
			}
			if(!approval.equals("all")){
				queryAirwayBill+=" and o.logApprovalStatus2.approvalStatusId="+approval;				
			}
			if(!recon.equals("all") && !recon.equals("None")){
				queryAirwayBill+=" and o.activityResultStatus='"+recon+"'";
			}else if(recon.equals("None")){
				queryAirwayBill+=" and o.activityResultStatus is null";
			}
									
			_log.debug("queryAirwayBill: "+queryAirwayBill);
			
			airwayBillList = airwayBillSessionHome.queryByRange(queryAirwayBill, 0, 0);
			
			//set value logistic provider dan approval status di header
			if(airwayBillList.size()>0 && !logistic.equals("all") ){
				logistic=airwayBillList.get(0).getLogLogisticsProvider().getLogisticsProviderCode();
			}else{
				logistic="All Provider";
			}
			if(airwayBillList.size()>0 && !approval.equals("all") ){
				approval=airwayBillList.get(0).getLogApprovalStatus2().getApprovalStatusDesc();
			}else{
				approval="All Status";
			}
			if(airwayBillList.size()>0 && !recon.equals("all") && !recon.equals("None")){
				recon=airwayBillList.get(0).getActivityResultStatus();
			}else if(recon.equals("None")){
				recon="None";
			}else{
				recon="All Status";
			}
			
				_log.debug("airwayBillList size: "+airwayBillList.size());
				_log.debug("start looping to fill bean");
				//Remove airway bill(s) with invoice result status is "Invalid GDN Ref", this is to avoid cluttering the activity report
				//screen from invalid airway bills from invoice report
				for (int i=0;i<airwayBillList.size();i++) {
					LogAirwayBill airwayBill = airwayBillList.get(i);
					if (airwayBill.getInvoiceResultStatus()!=null && airwayBill.getInvoiceResultStatus().toUpperCase().contains("Invalid GDN Ref".toUpperCase())) {
						airwayBillList.remove(i);
					}
				}						
				
				for (int i=0;i<airwayBillList.size();i++) {				
					AirwayBill awb = new AirwayBill();
					LogAirwayBill veniceAirwayBillData = airwayBillList.get(i);			
					_log.debug("set value to airway bill for airway bill id: "+veniceAirwayBillData.getAirwayBillId());
					
					List<LogMerchantPickupInstruction> merchantInstructionList=pickupInstructionHome.queryByRange("select o from LogMerchantPickupInstruction o where o.venOrderItem.orderItemId =" + veniceAirwayBillData.getVenOrderItem().getOrderItemId(), 0, 0);
				
					awb.setAirwayBillId(veniceAirwayBillData.getAirwayBillId().toString());
					awb.setWcsOrderId(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder().getWcsOrderId()!=null?veniceAirwayBillData.getVenOrderItem().getVenOrder().getWcsOrderId():"");
					awb.setWcsOrderItemId(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getWcsOrderItemId()!=null?veniceAirwayBillData.getVenOrderItem().getWcsOrderItemId():"");
					awb.setGdnReference(veniceAirwayBillData.getGdnReference()!=null?veniceAirwayBillData.getGdnReference():"");
					awb.setActivityResultStatus(veniceAirwayBillData.getActivityResultStatus()!=null?veniceAirwayBillData.getActivityResultStatus():"");
					awb.setAirwaybillTimestamp(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder().getOrderDate()!=null?veniceAirwayBillData.getVenOrderItem().getVenOrder().getOrderDate():null);
					awb.setOrderItemStatus(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrderStatus()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrderStatus().getOrderStatusCode()!=null?veniceAirwayBillData.getVenOrderItem().getVenOrderStatus().getOrderStatusCode():"");
					awb.setMerchantName((veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct()!=null &&
									veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!= null && veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!= null)
									&& veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName()!=null?veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
					
					awb.setMerchantOrigin((merchantInstructionList!=null && merchantInstructionList.size() > 0)?(merchantInstructionList.get(0).getVenAddress().getVenCity()!=null?merchantInstructionList.get(0).getVenAddress().getVenCity().getCityName():""):"");
					awb.setLogisticProvider(veniceAirwayBillData.getLogLogisticsProvider().getLogisticsProviderCode());
					awb.setDestination(veniceAirwayBillData.getDestination());
					awb.setDestinationZipCode(veniceAirwayBillData.getZip());
					awb.setAirwayBillPickupDatetime(veniceAirwayBillData.getAirwayBillPickupDateTime()!=null?veniceAirwayBillData.getAirwayBillPickupDateTime():null);
					awb.setAirwaybillNumber(veniceAirwayBillData.getAirwayBillNumber()!=null?veniceAirwayBillData.getAirwayBillNumber():"");
					awb.setApprovalStatus(veniceAirwayBillData.getLogApprovalStatus2()!=null && veniceAirwayBillData.getLogApprovalStatus2().getApprovalStatusDesc()!=null?veniceAirwayBillData.getLogApprovalStatus2().getApprovalStatusDesc():"");
					awb.setApprovedBy(veniceAirwayBillData.getActivityApprovedByUserId()!=null?veniceAirwayBillData.getActivityApprovedByUserId():"");
					airwayBillDataList.add(awb);
	
					_log.debug("query for get recon record list");
					//get airway bill detail from LogActivityReconRecord					
					String queryActivityReconRecord = "select o from LogActivityReconRecord o where o.logAirwayBill.airwayBillId="+veniceAirwayBillData.getAirwayBillId();
					activityReconRecordList = reconRecordSessionHome.queryByRange(queryActivityReconRecord, 0, 0);
					_log.debug("activityReconRecordList.size: "+activityReconRecordList.size());
					
					if(activityReconRecordList.size()>0){
						_log.debug("masuk if , ada detail");
						reconRecordDataList = new ArrayList<ActivityInvoiceReconRecord>();
						
						for (int j=0;j<activityReconRecordList.size();j++) {
							ActivityInvoiceReconRecord reconRecord = new ActivityInvoiceReconRecord();
							LogActivityReconRecord veniceReconRecordData = activityReconRecordList.get(j);
	
							_log.debug("set value to recon record for airway bill id: "+veniceReconRecordData.getLogAirwayBill().getAirwayBillId());
							reconRecord.setAirwayBillId(veniceReconRecordData.getLogAirwayBill().getAirwayBillId().toString());
							reconRecord.setReconRecordResult(veniceReconRecordData.getLogReconActivityRecordResult()!=null && veniceReconRecordData.getLogReconActivityRecordResult().getReconRecordResultDesc()!=null?veniceReconRecordData.getLogReconActivityRecordResult().getReconRecordResultDesc():"");
							reconRecord.setVeniceData(veniceReconRecordData.getVeniceData()!=null?veniceReconRecordData.getVeniceData():"");
							reconRecord.setProviderData(veniceReconRecordData.getProviderData()!=null?veniceReconRecordData.getProviderData():"");
							reconRecord.setManualData(veniceReconRecordData.getManuallyEnteredData()!=null?veniceReconRecordData.getManuallyEnteredData():"");
							reconRecord.setPic(veniceReconRecordData.getUserLogonName()!=null?veniceReconRecordData.getUserLogonName():"");
							reconRecord.setActionApplied(veniceReconRecordData.getLogActionApplied()!=null && veniceReconRecordData.getLogActionApplied().getActionAppliedDesc()!=null?veniceReconRecordData.getLogActionApplied().getActionAppliedDesc():"");
							reconRecord.setReconResultId(veniceReconRecordData.getLogReconActivityRecordResult().getReconRecordResultId());
							
							reconRecordDataList.add(reconRecord);
						}
						awb.setActivityInvoiceReconRecordList(reconRecordDataList);
					}
				}
	
				_log.debug("airwayBillDataList size: "+airwayBillDataList.size());
				_log.debug("start export data to excel");
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				response.setContentType("application/vnd.ms-excel");
			    response.setHeader("Content-Disposition",  "attachment; filename="+shortname);

				HSSFWorkbook wb = new HSSFWorkbook(); 
				HSSFSheet sheet = wb.createSheet("ActivityReport");	
				
				// Create the column headings
				 String judul="Activity Report for: "+logistic+", Approval Status: "+approval+", Recon Status: "+recon;
				 HSSFRow header = sheet.createRow((short) 0);
				 header.createCell(0).setCellValue(new HSSFRichTextString(judul));
				 sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
				 sheet.addMergedRegion(new CellRangeAddress(1,1,0,5));
				 HSSFRow headertgl = sheet.createRow((short) 1);
				 _log.debug("date: "+dateFormat.format(fromDate));
				 headertgl.createCell(0).setCellValue(new HSSFRichTextString("Dari tanggal: "+dateFormat.format(fromDate)));
				 
				 //style
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
				 
				 int startRow = 2;
				 int startCol=0;

				 headertgl.createCell(startCol+17).setCellValue(new HSSFRichTextString("Pickup Date Mismatch"));
				 
				 //set style for header
				HSSFCell cell = headertgl.getCell(startCol+17);
				cell.setCellStyle(headerCellstyle);
				 
				 sheet.addMergedRegion(new CellRangeAddress(
						 startRow - 1, //first row (0-based)
						 startRow - 1, //last row  (0-based)
						 startCol+17, //first column (0-based)
						 startCol+20  //last column  (0-based)
			     ));
				 
				 headertgl.createCell(startCol+21).setCellValue(new HSSFRichTextString("Service Mismatch"));
				 
				//set style for header
				cell = headertgl.getCell(startCol+21);
				cell.setCellStyle(headerCellstyle);
				 
				 sheet.addMergedRegion(new CellRangeAddress(
						 startRow - 1, //first row (0-based)
						 startRow - 1, //last row  (0-based)
						 startCol+21, //first column (0-based)
						 startCol+24  //last column  (0-based)
			     ));
				 
				 headertgl.createCell(startCol+25).setCellValue(new HSSFRichTextString("Settlement Code Mismatch"));
				 
				//set style for header
				cell = headertgl.getCell(startCol+25);
				cell.setCellStyle(headerCellstyle);
				 
				 sheet.addMergedRegion(new CellRangeAddress(
						 startRow - 1, //first row (0-based)
						 startRow - 1, //last row  (0-based)
						 startCol+25, //first column (0-based)
						 startCol+28  //last column  (0-based)
			     ));
				 
				 headertgl.createCell(startCol+29).setCellValue(new HSSFRichTextString("Recipient Mismatch"));
				 
				 sheet.addMergedRegion(new CellRangeAddress(
						 startRow - 1, //first row (0-based)
						 startRow - 1, //last row  (0-based)
						 startCol+29, //first column (0-based)
						 startCol+32  //last column  (0-based)
			     ));
				 
				//set style for header
				cell = headertgl.getCell(startCol+29);
				cell.setCellStyle(headerCellstyle);
				 
				 HSSFRow headerRow = sheet.createRow((short) startRow);
				 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("No."));
				 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order ID"));
				 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("Order Item ID"));
				 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("GDN Reference")); 
				 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Recon Status"));
				 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Order Item Status"));
				 headerRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("Merchant"));
				 headerRow.createCell(startCol+7).setCellValue(new HSSFRichTextString("Origin Merchant"));
				 headerRow.createCell(startCol+8).setCellValue(new HSSFRichTextString("Logistic Provider"));
				 headerRow.createCell(startCol+9).setCellValue(new HSSFRichTextString("Pickup Date")); 
				 headerRow.createCell(startCol+10).setCellValue(new HSSFRichTextString("Destination City"));
				 headerRow.createCell(startCol+11).setCellValue(new HSSFRichTextString("Destination Zip Code"));
				 headerRow.createCell(startCol+12).setCellValue(new HSSFRichTextString("AWB Timestamp"));
				 headerRow.createCell(startCol+13).setCellValue(new HSSFRichTextString("Airway Bill Number"));
				 headerRow.createCell(startCol+14).setCellValue(new HSSFRichTextString("Approval Status"));
				 headerRow.createCell(startCol+15).setCellValue(new HSSFRichTextString("Approved By"));		
				 headerRow.createCell(startCol+16).setCellValue(new HSSFRichTextString("PIC Recon"));	
				 
				 headerRow.createCell(startCol+17).setCellValue(new HSSFRichTextString("MTA Data"));	
				 headerRow.createCell(startCol+18).setCellValue(new HSSFRichTextString("Log Data"));	
				 headerRow.createCell(startCol+19).setCellValue(new HSSFRichTextString("Manual Data"));	
				 headerRow.createCell(startCol+20).setCellValue(new HSSFRichTextString("Action Applied"));	
				 
				 headerRow.createCell(startCol+21).setCellValue(new HSSFRichTextString("MTA Data"));	
				 headerRow.createCell(startCol+22).setCellValue(new HSSFRichTextString("Log Data"));	
				 headerRow.createCell(startCol+23).setCellValue(new HSSFRichTextString("Manual Data"));	
				 headerRow.createCell(startCol+24).setCellValue(new HSSFRichTextString("Action Applied"));	
				 
				 headerRow.createCell(startCol+25).setCellValue(new HSSFRichTextString("MTA Data"));	
				 headerRow.createCell(startCol+26).setCellValue(new HSSFRichTextString("Log Data"));	
				 headerRow.createCell(startCol+27).setCellValue(new HSSFRichTextString("Manual Data"));	
				 headerRow.createCell(startCol+28).setCellValue(new HSSFRichTextString("Action Applied"));	
				 
				 headerRow.createCell(startCol+29).setCellValue(new HSSFRichTextString("MTA Data"));	
				 headerRow.createCell(startCol+30).setCellValue(new HSSFRichTextString("Log Data"));	
				 headerRow.createCell(startCol+31).setCellValue(new HSSFRichTextString("Manual Data"));	
				 headerRow.createCell(startCol+32).setCellValue(new HSSFRichTextString("Action Applied"));	
				   
				//set style for header
				for(int i=startCol; i<=startCol+32; i++){
					cell = headerRow.getCell(i);
					cell.setCellStyle(headerCellstyle);
				}    
				
				//number for row number for the airway bill list
				Integer number=1;
				
				//number for storing the detail header, to add color later
				int rowNumber=1;
				
				_log.debug("looping for generating excel rows");
				//Looping for generating excel rows
				HSSFRow row = null;
				for (int i = 0; i < airwayBillDataList.size(); i++) {
					_log.debug("looping airway bill");					
					AirwayBill awb = (AirwayBill) airwayBillDataList.get(i);	
					
					// when logistic contains mismatch data 
					if(awb.getActivityInvoiceReconRecordList().size() > 0){
						for(ActivityInvoiceReconRecord reconRecordData:awb.getActivityInvoiceReconRecordList()){
							
							startRow=startRow+1;
							row = sheet.createRow(startRow);
							
							_log.debug("processing row: "+i+" order id: "+awb.getWcsOrderId() +", order item id: "+awb.getWcsOrderItemId());
							cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(number.toString()));
							cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderId()));
							cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderItemId()));	
							cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(awb.getGdnReference()));
							cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(awb.getActivityResultStatus()));	 
							cell = row.createCell(startCol+5);cell.setCellValue(new HSSFRichTextString(awb.getOrderItemStatus()));	
							cell = row.createCell(startCol+6);cell.setCellValue(new HSSFRichTextString(awb.getMerchantName()));
							cell = row.createCell(startCol+7);cell.setCellValue(new HSSFRichTextString(awb.getMerchantOrigin()));
							cell = row.createCell(startCol+8);cell.setCellValue(new HSSFRichTextString(awb.getLogisticProvider()));
							cell = row.createCell(startCol+9);cell.setCellValue(new HSSFRichTextString(awb.getAirwayBillPickupDatetime()!=null?sdf.format(awb.getAirwayBillPickupDatetime()):""));
							cell = row.createCell(startCol+10);cell.setCellValue(new HSSFRichTextString(awb.getDestination()));
							cell = row.createCell(startCol+11);cell.setCellValue(new HSSFRichTextString(awb.getDestinationZipCode()));
							cell = row.createCell(startCol+12);cell.setCellValue(new HSSFRichTextString(awb.getAirwaybillTimestamp()!=null?sdf.format(awb.getAirwaybillTimestamp()):""));
							cell = row.createCell(startCol+13);cell.setCellValue(new HSSFRichTextString(awb.getAirwaybillNumber()));	 
					    	cell = row.createCell(startCol+14);cell.setCellValue(new HSSFRichTextString(awb.getApprovalStatus()));
					    	cell = row.createCell(startCol+15);cell.setCellValue(new HSSFRichTextString(awb.getApprovedBy()));	
					    	
					    	cell = row.createCell(startCol+16);cell.setCellValue(new HSSFRichTextString(reconRecordData.getPic()));
					    	if(reconRecordData.getReconResultId() == LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_PICKUPDATELATE){
					    		cell = row.createCell(startCol+17);cell.setCellValue(new HSSFRichTextString(reconRecordData.getVeniceData()));	 
						    	cell = row.createCell(startCol+18);cell.setCellValue(new HSSFRichTextString(reconRecordData.getProviderData()));
						    	cell = row.createCell(startCol+19);cell.setCellValue(new HSSFRichTextString(reconRecordData.getManualData()));	
						    	cell = row.createCell(startCol+20);cell.setCellValue(new HSSFRichTextString(reconRecordData.getActionApplied()));
						    	cell = row.createCell(startCol+21);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+22);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+23);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+24);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+25);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+26);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+27);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+28);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+29);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+30);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+31);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+32);cell.setCellValue(new HSSFRichTextString(""));
					    	}
					    	
					    	if(reconRecordData.getReconResultId() == LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SERVICEMISMATCH){
					    		cell = row.createCell(startCol+17);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+18);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+19);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+20);cell.setCellValue(new HSSFRichTextString(""));
					    		cell = row.createCell(startCol+21);cell.setCellValue(new HSSFRichTextString(reconRecordData.getVeniceData()));	 
						    	cell = row.createCell(startCol+22);cell.setCellValue(new HSSFRichTextString(reconRecordData.getProviderData()));
						    	cell = row.createCell(startCol+23);cell.setCellValue(new HSSFRichTextString(reconRecordData.getManualData()));	
						    	cell = row.createCell(startCol+24);cell.setCellValue(new HSSFRichTextString(reconRecordData.getActionApplied()));
						    	cell = row.createCell(startCol+25);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+26);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+27);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+28);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+29);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+30);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+31);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+32);cell.setCellValue(new HSSFRichTextString(""));
					    	}
					    	
					    	if(reconRecordData.getReconResultId() == LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SETTLEMENTCODEMISMATCH){
					    		cell = row.createCell(startCol+17);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+18);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+19);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+20);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+21);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+22);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+23);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+24);cell.setCellValue(new HSSFRichTextString(""));
					    		cell = row.createCell(startCol+25);cell.setCellValue(new HSSFRichTextString(reconRecordData.getVeniceData()));	 
						    	cell = row.createCell(startCol+26);cell.setCellValue(new HSSFRichTextString(reconRecordData.getProviderData()));
						    	cell = row.createCell(startCol+27);cell.setCellValue(new HSSFRichTextString(reconRecordData.getManualData()));	
						    	cell = row.createCell(startCol+28);cell.setCellValue(new HSSFRichTextString(reconRecordData.getActionApplied()));
						    	cell = row.createCell(startCol+29);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+30);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+31);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+32);cell.setCellValue(new HSSFRichTextString(""));
					    	}
					    	
					    	if(reconRecordData.getReconResultId() == LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_RECIPIENTMISMATCH){
					    		cell = row.createCell(startCol+17);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+18);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+19);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+20);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+21);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+22);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+23);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+24);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+25);cell.setCellValue(new HSSFRichTextString(""));	 
						    	cell = row.createCell(startCol+26);cell.setCellValue(new HSSFRichTextString(""));
						    	cell = row.createCell(startCol+27);cell.setCellValue(new HSSFRichTextString(""));	
						    	cell = row.createCell(startCol+28);cell.setCellValue(new HSSFRichTextString(""));
					    		cell = row.createCell(startCol+29);cell.setCellValue(new HSSFRichTextString(reconRecordData.getVeniceData()));	 
						    	cell = row.createCell(startCol+30);cell.setCellValue(new HSSFRichTextString(reconRecordData.getProviderData()));
						    	cell = row.createCell(startCol+31);cell.setCellValue(new HSSFRichTextString(reconRecordData.getManualData()));	
						    	cell = row.createCell(startCol+32);cell.setCellValue(new HSSFRichTextString(reconRecordData.getActionApplied()));
					    	}
					    	
					    	number=number+1;
							rowNumber=rowNumber+1;
							rowNumTemp.add(rowNumber); 					    	
						}
					// no mismatch data	
					}else{
						startRow=startRow+1;
						row = sheet.createRow(startRow);
						
						_log.debug("processing row: "+i+" order id: "+awb.getWcsOrderId() +", order item id: "+awb.getWcsOrderItemId());
						cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(number.toString()));
						cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderId()));
						cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderItemId()));	
						cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(awb.getGdnReference()));
						cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(awb.getActivityResultStatus()));	 
						cell = row.createCell(startCol+5);cell.setCellValue(new HSSFRichTextString(awb.getOrderItemStatus()));	
						cell = row.createCell(startCol+6);cell.setCellValue(new HSSFRichTextString(awb.getMerchantName()));
						cell = row.createCell(startCol+7);cell.setCellValue(new HSSFRichTextString(awb.getMerchantOrigin()));
						cell = row.createCell(startCol+8);cell.setCellValue(new HSSFRichTextString(awb.getLogisticProvider()));
						cell = row.createCell(startCol+9);cell.setCellValue(new HSSFRichTextString(awb.getAirwayBillPickupDatetime()!=null?sdf.format(awb.getAirwayBillPickupDatetime()):""));
						cell = row.createCell(startCol+10);cell.setCellValue(new HSSFRichTextString(awb.getDestination()));
						cell = row.createCell(startCol+11);cell.setCellValue(new HSSFRichTextString(awb.getDestinationZipCode()));
						cell = row.createCell(startCol+12);cell.setCellValue(new HSSFRichTextString(awb.getAirwaybillTimestamp()!=null?sdf.format(awb.getAirwaybillTimestamp()):""));
						cell = row.createCell(startCol+13);cell.setCellValue(new HSSFRichTextString(awb.getAirwaybillNumber()));	 
				    	cell = row.createCell(startCol+14);cell.setCellValue(new HSSFRichTextString(awb.getApprovalStatus()));
				    	cell = row.createCell(startCol+15);cell.setCellValue(new HSSFRichTextString(awb.getApprovedBy()));	
				    	cell = row.createCell(startCol+16);cell.setCellValue(new HSSFRichTextString(""));	
				    	cell = row.createCell(startCol+17);cell.setCellValue(new HSSFRichTextString(""));	 
				    	cell = row.createCell(startCol+18);cell.setCellValue(new HSSFRichTextString(""));
				    	cell = row.createCell(startCol+19);cell.setCellValue(new HSSFRichTextString(""));	
				    	cell = row.createCell(startCol+20);cell.setCellValue(new HSSFRichTextString(""));
				    	cell = row.createCell(startCol+21);cell.setCellValue(new HSSFRichTextString(""));	 
				    	cell = row.createCell(startCol+22);cell.setCellValue(new HSSFRichTextString(""));
				    	cell = row.createCell(startCol+23);cell.setCellValue(new HSSFRichTextString(""));	
				    	cell = row.createCell(startCol+24);cell.setCellValue(new HSSFRichTextString(""));
				    	cell = row.createCell(startCol+25);cell.setCellValue(new HSSFRichTextString(""));	 
				    	cell = row.createCell(startCol+26);cell.setCellValue(new HSSFRichTextString(""));
				    	cell = row.createCell(startCol+27);cell.setCellValue(new HSSFRichTextString(""));	
				    	cell = row.createCell(startCol+28);cell.setCellValue(new HSSFRichTextString(""));
				    	cell = row.createCell(startCol+29);cell.setCellValue(new HSSFRichTextString(""));	 
				    	cell = row.createCell(startCol+30);cell.setCellValue(new HSSFRichTextString(""));
				    	cell = row.createCell(startCol+31);cell.setCellValue(new HSSFRichTextString(""));	
				    	cell = row.createCell(startCol+32);cell.setCellValue(new HSSFRichTextString(""));
				    	
				    	number=number+1;
						rowNumber=rowNumber+1;
						rowNumTemp.add(rowNumber);											    	
					}		
				}
				
				//set style for airway bill list
				for(int l=startCol; l<=startCol+32; l++){
					HSSFCell cell2 = row.getCell(l);
					cell2.setCellStyle(detailCellstyle);
					//Autosize the columns while we are there
					sheet.autoSizeColumn(l);
				}   
					
				//write to stream
				_log.debug("write to stream");	
				out = response.getOutputStream();			
				wb.write(out);
				out.flush();
				out.close();			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {		    
			try {
				airwayBillLocator.close();
				reconRecordLocator.close();
				pickupInstructionLocator.close();
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
