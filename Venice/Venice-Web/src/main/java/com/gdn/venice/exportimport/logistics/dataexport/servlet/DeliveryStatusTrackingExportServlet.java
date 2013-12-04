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
import com.gdn.venice.exportimport.logistics.dataexport.AirwayBill;
import com.gdn.venice.exportimport.logistics.dataexport.OrderItemHistory;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderStatus;

/**
 * Servlet implementation class DeliveryStatusTrackingExportServlet.
 * 
 * @author Roland
 * 
 */
@SuppressWarnings({ "unused", "deprecation" })
public class DeliveryStatusTrackingExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeliveryStatusTrackingExportServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataexport.DeliveryStatusTrackingExportServlet");
	}
	
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		_log.info("start DeliveryStatusTrackingExportServlet");
		String result =null;
		
		ArrayList<AirwayBill> airwayBillDataList = new ArrayList<AirwayBill>();	
		ArrayList<OrderItemHistory> historyDataList = new ArrayList<OrderItemHistory>();
		ArrayList<Integer> rowNumTemp = new ArrayList<Integer>();
		
		Locator<Object> locator = null;
		OutputStream out = null;
		
		String logistic = (String) request.getParameter("logistic");
		String orderStatus = (String) request.getParameter("status");
		String sFromDate = request.getParameter("date");		
			
		Date fromDate = null;
		SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
		
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
	
		_log.debug("logistic: "+logistic);
		_log.debug("order status: "+orderStatus);
		_log.debug("date: "+dateParam);	
      		
		try {
			String shortname="DeliveryStatusTrackingReport"+System.currentTimeMillis() + ".xls";
			locator = new Locator<Object>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			LogAirwayBillSessionEJBRemote airwayBillSessionHome = (LogAirwayBillSessionEJBRemote) locator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			VenOrderItemStatusHistorySessionEJBRemote orderItemStatusHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
			.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");		
			
			LogMerchantPickupInstructionSessionEJBRemote pickupInstructionHome = (LogMerchantPickupInstructionSessionEJBRemote) locator
			.lookup(LogMerchantPickupInstructionSessionEJBRemote.class, "LogMerchantPickupInstructionSessionEJBBean");
			
			VenOrderStatusSessionEJBRemote orderStatusHome = (VenOrderStatusSessionEJBRemote) locator
			.lookup(VenOrderStatusSessionEJBRemote.class, "VenOrderStatusSessionEJBBean");
			
			LogLogisticsProviderSessionEJBRemote providerHome = (LogLogisticsProviderSessionEJBRemote) locator
			.lookup(LogLogisticsProviderSessionEJBRemote.class, "LogLogisticsProviderSessionEJBBean");
						
			_log.debug("query for get airway bill list");
			List<LogAirwayBill> airwayBillList = null;	
			List<VenOrderItemStatusHistory> orderItemStatusHistoryList = null;
			List<LogMerchantPickupInstruction> merchantInstructionList = null;
			String merchantOrigin="";
			
			String queryAirwayBill = "select o from LogAirwayBill o where o.airwayBillTimestamp>= '" + dateParam + "'";
			if(!logistic.equals("all")){				
				queryAirwayBill+=" and o.logLogisticsProvider.logisticsProviderId="+logistic;				
			}
			if(!orderStatus.equals("all")){
				queryAirwayBill+=" and o.venOrderItem.venOrderStatus.orderStatusId="+orderStatus;				
			}
									
			_log.debug("queryAirwayBill: "+queryAirwayBill);
			
			airwayBillList = airwayBillSessionHome.queryByRange(queryAirwayBill, 0, 0);
			
			//set value logistic provider dan order status di header						
			if(!logistic.equals("all") ){
				List<LogLogisticsProvider> logisticList = providerHome.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderId="+logistic, 0, 1);
				logistic=logisticList.get(0).getLogisticsProviderCode();
			}else{
				logistic="All Provider";
			}
			if(!orderStatus.equals("all") ){
				List<VenOrderStatus> statusList = orderStatusHome.queryByRange("select o from VenOrderStatus o where o.orderStatusId="+orderStatus, 0, 1);
				orderStatus=statusList.get(0).getOrderStatusCode();
			}else{
				orderStatus="All Status";
			}
			
				_log.debug("airwayBillList size: "+airwayBillList.size());
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
					awb.setAirwayBillId(veniceAirwayBillData.getAirwayBillId().toString());
					awb.setWcsOrderId(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder().getWcsOrderId()!=null?veniceAirwayBillData.getVenOrderItem().getVenOrder().getWcsOrderId():"");
					awb.setWcsOrderItemId(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getWcsOrderItemId()!=null?veniceAirwayBillData.getVenOrderItem().getWcsOrderItemId():"");
					awb.setOrderItemId(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getOrderItemId()!=null?veniceAirwayBillData.getVenOrderItem().getOrderItemId().toString():"");
					awb.setGdnReference(veniceAirwayBillData.getGdnReference()!=null?veniceAirwayBillData.getGdnReference():"");
					awb.setShippingWeight(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getShippingWeight()!=null?veniceAirwayBillData.getVenOrderItem().getShippingWeight().toString():"");
					awb.setQuantity(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getQuantity()!=null?veniceAirwayBillData.getVenOrderItem().getQuantity().toString():"");
					awb.setOrderDate(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrder().getOrderDate()!=null?veniceAirwayBillData.getVenOrderItem().getVenOrder().getOrderDate():null);
					awb.setOrderItemStatus(veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrderStatus()!=null && veniceAirwayBillData.getVenOrderItem().getVenOrderStatus().getOrderStatusCode()!=null?veniceAirwayBillData.getVenOrderItem().getVenOrderStatus().getOrderStatusCode():"");
					awb.setMerchantName((veniceAirwayBillData.getVenOrderItem()!=null && veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct()!=null &&
							veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!= null && veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!= null)
							&& veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName()!=null?veniceAirwayBillData.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
					
					merchantInstructionList=pickupInstructionHome.queryByRange("select o from LogMerchantPickupInstruction o where o.venOrderItem.orderItemId =" + veniceAirwayBillData.getVenOrderItem().getOrderItemId(), 0, 0);
					if(merchantInstructionList.size()>0){
						merchantOrigin=merchantInstructionList.get(0).getVenAddress().getVenCity()!=null?merchantInstructionList.get(0).getVenAddress().getVenCity().getCityName():"";
					}
					awb.setMerchantOrigin(merchantOrigin);
					awb.setMtaSettlement(veniceAirwayBillData.getMtaData().toString()=="true"?"Yes":"No");
					awb.setDestination(veniceAirwayBillData.getDestination()!=null?veniceAirwayBillData.getDestination():"");
					awb.setAirwaybillTimestamp(veniceAirwayBillData.getAirwayBillTimestamp()!=null?veniceAirwayBillData.getAirwayBillTimestamp():null);								
					awb.setLogisticService(veniceAirwayBillData.getVenOrderItem().getLogLogisticService()!=null && veniceAirwayBillData.getVenOrderItem().getLogLogisticService().getLogisticsServiceDesc()!=null?veniceAirwayBillData.getVenOrderItem().getLogLogisticService().getLogisticsServiceDesc():"");
					awb.setAirwaybillNumber(veniceAirwayBillData.getAirwayBillNumber()!=null?veniceAirwayBillData.getAirwayBillNumber():"");
					awb.setDeliveryStatus(veniceAirwayBillData.getStatus()!=null?veniceAirwayBillData.getStatus():"");
					awb.setReceived(veniceAirwayBillData.getReceived()!=null?veniceAirwayBillData.getReceived():null);
					awb.setReceiver(veniceAirwayBillData.getRecipient()!=null?veniceAirwayBillData.getRecipient():"");
					awb.setRelation(veniceAirwayBillData.getRelation()!=null?veniceAirwayBillData.getRelation():"");			
					airwayBillDataList.add(awb);		
					
					//get order item status detail
					String queryHistory = "select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId="+awb.getOrderItemId()+" order by o.id.historyTimestamp desc";				
					orderItemStatusHistoryList = orderItemStatusHistorySessionHome.queryByRange(queryHistory, 0, 0);
					
					if(orderItemStatusHistoryList.size()>0){
						_log.debug("masuk detail");
						for(int j=0; j<orderItemStatusHistoryList.size();j++){
							OrderItemHistory history = new OrderItemHistory();
							VenOrderItemStatusHistory list = orderItemStatusHistoryList.get(j);
							
					        history.setOrderItemId(list.getVenOrderItem().getOrderItemId().toString());
					        history.setHistoryTimestamp(list.getId().getHistoryTimestamp());
					        history.setHistoryStatus(list.getVenOrderStatus().getOrderStatusCode()!=null && list.getVenOrderStatus().getOrderStatusCode()!=null?list.getVenOrderStatus().getOrderStatusCode():"");								 
					        history.setHistoryNotes(list.getStatusChangeReason()!=null?list.getStatusChangeReason():"");		
					        historyDataList.add(history);
						}
					}
				}
	
				_log.debug("airwayBillDataList size: "+airwayBillDataList.size());
				_log.debug("start export data to excel");
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				response.setContentType("application/vnd.ms-excel");
			    response.setHeader("Content-Disposition",  "attachment; filename="+shortname);

				HSSFWorkbook wb = new HSSFWorkbook(); 
				HSSFSheet sheet = wb.createSheet("DeliveryStatusTrackingReport");	
				
				// Create the column headings
				 String judul="Delivery Status Tracking Report for: "+logistic+", Order Status: "+orderStatus;
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
				
				 HSSFRow headerRow = sheet.createRow((short) startRow);
				 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("No."));
				 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order ID"));
				 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("Order Item ID"));
				 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("GDN Reference")); 
				 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Weight"));
				 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Quantity"));
				 headerRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("Order Date"));
				 headerRow.createCell(startCol+7).setCellValue(new HSSFRichTextString("Order Item Status"));
				 headerRow.createCell(startCol+8).setCellValue(new HSSFRichTextString("Merchant Name"));
				 headerRow.createCell(startCol+9).setCellValue(new HSSFRichTextString("Merchant Origin"));
				 headerRow.createCell(startCol+10).setCellValue(new HSSFRichTextString("MTA Settlement"));
				 headerRow.createCell(startCol+11).setCellValue(new HSSFRichTextString("Destination"));
				 headerRow.createCell(startCol+12).setCellValue(new HSSFRichTextString("AWB Timestamp")); 
				 headerRow.createCell(startCol+13).setCellValue(new HSSFRichTextString("Service"));
				 headerRow.createCell(startCol+14).setCellValue(new HSSFRichTextString("Airway Bill Number"));
				 headerRow.createCell(startCol+15).setCellValue(new HSSFRichTextString("Delivery Status")); 
				 headerRow.createCell(startCol+16).setCellValue(new HSSFRichTextString("Received Date")); 
				 headerRow.createCell(startCol+17).setCellValue(new HSSFRichTextString("Receiver")); 
				 headerRow.createCell(startCol+18).setCellValue(new HSSFRichTextString("Relation"));		    
				   
				//set style for header
				for(int i=startCol; i<=startCol+18; i++){
					HSSFCell cell = headerRow.getCell(i);
					cell.setCellStyle(headerCellstyle);
				}    
				
				//number for row number for the airway bill list
				Integer number=1;
				
				//number for storing the detail header, to add color later
				int rowNumber=1;
				
				_log.debug("looping for generating excel rows");
				//Looping for generating excel rows
				_log.debug("airwayBillDataList.size(): "+airwayBillDataList.size());
				HSSFRow row = null;
				for (int i = 0; i < airwayBillDataList.size(); i++) {	
					AirwayBill awb = (AirwayBill) airwayBillDataList.get(i);	
					
					startRow=startRow+1;
					row = sheet.createRow(startRow);
					
					_log.debug("processing row: "+i+" order id: "+awb.getWcsOrderId() +", order item id: "+awb.getWcsOrderItemId());
					HSSFCell cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(number.toString()));
					cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderId()));
					cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(awb.getWcsOrderItemId()));	 	 
					cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(awb.getGdnReference()));
					cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(awb.getShippingWeight()));
					cell = row.createCell(startCol+5);cell.setCellValue(new HSSFRichTextString(awb.getQuantity()));
					cell = row.createCell(startCol+6);cell.setCellValue(new HSSFRichTextString(awb.getOrderDate()!=null?sdf.format(awb.getOrderDate()):""));
					cell = row.createCell(startCol+7);cell.setCellValue(new HSSFRichTextString(awb.getOrderItemStatus()));					
					cell = row.createCell(startCol+8);cell.setCellValue(new HSSFRichTextString(awb.getMerchantName()));
					cell = row.createCell(startCol+9);cell.setCellValue(new HSSFRichTextString(awb.getMerchantOrigin()));
					cell = row.createCell(startCol+10);cell.setCellValue(new HSSFRichTextString(awb.getMtaSettlement()));
					cell = row.createCell(startCol+11);cell.setCellValue(new HSSFRichTextString(awb.getDestination()));	
					cell = row.createCell(startCol+12);cell.setCellValue(new HSSFRichTextString(awb.getAirwaybillTimestamp()!=null?sdf.format(awb.getAirwaybillTimestamp()):""));	 
					cell = row.createCell(startCol+13);cell.setCellValue(new HSSFRichTextString(awb.getLogisticService()));
					cell = row.createCell(startCol+14);cell.setCellValue(new HSSFRichTextString(awb.getAirwaybillNumber()));	 
			    	cell = row.createCell(startCol+15);cell.setCellValue(new HSSFRichTextString(awb.getDeliveryStatus()));	
			    	cell = row.createCell(startCol+16);cell.setCellValue(new HSSFRichTextString(awb.getReceived()!=null?sdf.format(awb.getReceived()):""));
			    	cell = row.createCell(startCol+17);cell.setCellValue(new HSSFRichTextString(awb.getReceiver()));
			    	cell = row.createCell(startCol+18);cell.setCellValue(new HSSFRichTextString(awb.getRelation()));
					number=number+1;
					rowNumber=rowNumber+1;
					rowNumTemp.add(rowNumber);							
					
					//add detail		
					boolean writeHeader=false;
					HSSFRow detailRowHeader = null;
					HSSFRow detailRow = null;
					for (int k = 0; k < historyDataList.size(); k++) {
						OrderItemHistory historyData = historyDataList.get(k);
						if(historyData.getOrderItemId().equals(awb.getOrderItemId())){
							//set the header just once
							if(writeHeader==false){
								startRow=startRow+1;
								//add detail header
								_log.debug("add detail header");
								detailRowHeader = sheet.createRow((short) startRow);
								detailRowHeader.createCell(startCol).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+1).setCellValue(new HSSFRichTextString("Status"));
								detailRowHeader.createCell(startCol+2).setCellValue(new HSSFRichTextString("Date/Time"));
								detailRowHeader.createCell(startCol+3).setCellValue(new HSSFRichTextString("Notes")); 
								detailRowHeader.createCell(startCol+4).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+5).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+6).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+7).setCellValue(new HSSFRichTextString("")); 
								detailRowHeader.createCell(startCol+8).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+9).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+10).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+11).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+12).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+13).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+14).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+15).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+16).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+17).setCellValue(new HSSFRichTextString(""));
								detailRowHeader.createCell(startCol+18).setCellValue(new HSSFRichTextString(""));
								rowNumber=rowNumber+1;
												
								//set style for header in detail
								for(int l=startCol; l<=startCol+18; l++){
									HSSFCell cell2 = detailRowHeader.getCell(l);
									if(l>0 && l<=3){
										cell2.setCellStyle(headerCellstyle);
									}else{
										cell2.setCellStyle(detailCellstyle);
									}
								} 
								
								writeHeader=true;
							}
							startRow=startRow+1;
							detailRow = sheet.createRow((short) startRow);
							HSSFCell detailCell = detailRow.createCell(startCol);detailCell.setCellValue(new HSSFRichTextString(""));
							detailCell = detailRow.createCell(startCol+1);detailCell.setCellValue(new HSSFRichTextString(historyData.getHistoryStatus()));
							String s =  sdf.format(historyData.getHistoryTimestamp());
					        StringBuilder sb = new StringBuilder(s);
					    	detailCell = detailRow.createCell(startCol+2);detailCell.setCellValue(new HSSFRichTextString(sb.toString()));	 	 
					    	detailCell = detailRow.createCell(startCol+3);detailCell.setCellValue(new HSSFRichTextString(historyData.getHistoryNotes()));
					    	detailCell = detailRow.createCell(startCol+4);detailCell.setCellValue(new HSSFRichTextString(""));	 
					    	detailCell = detailRow.createCell(startCol+5);detailCell.setCellValue(new HSSFRichTextString(""));	 
					    	detailCell = detailRow.createCell(startCol+6);detailCell.setCellValue(new HSSFRichTextString(""));	 
					    	detailCell = detailRow.createCell(startCol+7);detailCell.setCellValue(new HSSFRichTextString(""));	 
					    	detailCell = detailRow.createCell(startCol+8);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+9);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+10);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+11);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+12);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+13);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+14);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+15);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+16);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+17);detailCell.setCellValue(new HSSFRichTextString(""));
					    	detailCell = detailRow.createCell(startCol+18);detailCell.setCellValue(new HSSFRichTextString(""));
							rowNumber=rowNumber+1;
							
						}					
					}
					
					//set style for detail
					for(int l=startCol; l<=startCol+18; l++){
						HSSFCell cell2 = detailRow.getCell(l);
						cell2.setCellStyle(detailCellstyle);
					} 
										
					//add blank row to separate each airway bill list
					_log.debug("add blank row");	
					startRow=startRow+1;
					row = sheet.createRow((short) startRow);
					row.createCell(startCol).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+1).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+2).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+3).setCellValue(new HSSFRichTextString("")); 
					row.createCell(startCol+4).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+5).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+6).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+7).setCellValue(new HSSFRichTextString("")); 
					row.createCell(startCol+8).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+9).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+10).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+11).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+12).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+13).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+14).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+15).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+16).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+17).setCellValue(new HSSFRichTextString(""));
					row.createCell(startCol+18).setCellValue(new HSSFRichTextString(""));					
					rowNumber=rowNumber+1;						
				}
				
				//set style for airway bill list
				for(int l=startCol; l<=startCol+18; l++){
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
				locator.close();
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
