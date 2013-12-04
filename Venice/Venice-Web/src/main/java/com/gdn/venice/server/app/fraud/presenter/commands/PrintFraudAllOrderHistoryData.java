package com.gdn.venice.server.app.fraud.presenter.commands;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionPoint;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenProductCategory;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

public class PrintFraudAllOrderHistoryData {
	protected static Logger _log = null;
	HSSFWorkbook wb=null;


	/**
	 * Basic constructor
	 */
	public PrintFraudAllOrderHistoryData(HSSFWorkbook wb) {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.presenter.commands.PrintFraudAllOrderHistoryData");
		this.wb=wb;
	}
	
	public HSSFWorkbook ExportExcel(Map<String, Object> params,HSSFSheet sheet) throws ServletException {		

		Locator<Object> locator = null;
		String statusId = (String) params.get("statusId");
		String from = (String) params.get("from");
		String end = (String) params.get("end");		
				
		try{				
			
			locator = new Locator<Object>();			
			
			VenOrderItemSessionEJBRemote orderitemsessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");		
			VenOrderPaymentAllocationSessionEJBRemote allocationsessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");		
			FrdFraudSuspicionPointSessionEJBRemote frdFraudSusPointsSessionHome = (FrdFraudSuspicionPointSessionEJBRemote) locator.lookup(FrdFraudSuspicionPointSessionEJBRemote.class, "FrdFraudSuspicionPointSessionEJBBean");		
			VenBinCreditLimitEstimateSessionEJBRemote binSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");		
			VenOrderContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
			VenOrderItemContactDetailSessionEJBRemote orderItemContactDetailSessionHome = (VenOrderItemContactDetailSessionEJBRemote) locator.lookup(VenOrderItemContactDetailSessionEJBRemote.class, "VenOrderItemContactDetailSessionEJBBean");
			VenOrderItemStatusHistorySessionEJBRemote itemStatusHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");
			LogAirwayBillSessionEJBRemote awbSessionHome = (LogAirwayBillSessionEJBRemote) locator.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			VenContactDetailSessionEJBRemote contactSessionHome =  (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
			VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
			
			List<VenOrderItem> itemList = null;		
			String query="";				
			/*
			 * status all untuk status VA,C,SF,FC,FP,X
			 */
			if(statusId.equals("all")){
				statusId="0,1,2,3,4,6";				
			}			
			
			Date fromDate = null;
			Date endDate = null;
			SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
			
			if (from.contains("GMT 700")) {
				from = from.replace("GMT 700", "WIT");
			}else if (from.contains("GMT 800")){
				from = from.replace("GMT 800", "WITA");		
			}else if (from.contains("GMT 900")){
				from = from.replace("GMT 900", "WIB");		
			}
			
			if (end.contains("GMT 700")) {
				end = end.replace("GMT 700", "WIT");
			}else if (end.contains("GMT 800")){
				end = end.replace("GMT 800", "WITA");		
			}else if (end.contains("GMT 900")){
				end = end.replace("GMT 900", "WIB");		
			}
			
			try {
				fromDate = sdf2.parse(from);
				endDate = sdf2.parse(end);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			String dateFromParam = dateTimestamp.format(fromDate.getTime());
			String dateEndParam = dateTimestamp.format(endDate.getTime());
			
			query = "select distinct o from VenOrderItem o " +
			"left join fetch o.venOrderItemAdjustments " +
			"left outer join fetch o.venOrder "+
			"left join fetch o.venRecipient "+
			"left join fetch o.venRecipient.venParty "+
			"where o.venOrder.venOrderStatus.orderStatusId in ("+statusId+") and ( o.venOrder.orderDate > '"+dateFromParam+
			"' and o.venOrder.orderDate < '"+dateEndParam+"' ) order by o.venOrder.orderId asc, o.venOrder.orderDate asc ";

		itemList = orderitemsessionHome.queryByRange(query,0,0);
		
		System.out.println(query);
			
			
		int startRow = 4;
		int startCol=0;
		int countCol=0;
		
		 countCol=startCol;

		// Create the column headings
		 HSSFRow header = sheet.createRow((short) 1);
		 header.createCell(5).setCellValue(new HSSFRichTextString("ORDER DETAIL FRAUD"));
		 // Create the column headings
		 HSSFRow headerRow = sheet.createRow((short) startRow);
		 headerRow.createCell(countCol).setCellValue(new HSSFRichTextString("Month"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Year"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("No Urut"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Date"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Id"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Item"));	
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Id"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Date"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Amount"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("IP Address")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Type"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Username"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Full Name"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Phone Number"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Mobile Number"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Email")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Address")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer District")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer City")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer State")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Country")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Post Code")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Customer Date of Birth")); 
		 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Item Id"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Product SKU"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Product Name"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Qty"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Product Price"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Product Category"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping Type")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping Address"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping District"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping City"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping State"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping Country"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping Post Code"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping Cost"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Special Handling Cost"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Gift Wrap"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Adjustment"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Promo Code"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping Phone"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping HandPhone"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Price Per KG"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Fraud Status"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Order Id"));
		 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing Id"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing Method (MIS)"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Authorization Code")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Tenor")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Installment")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Interest")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Interest(%)")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Total Instalment + Interest"));  
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing Address")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing District")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing City")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing State")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing Country")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing Post Code")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing Amount")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Payment Status")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("VA Number")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Klik BCA User ID")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Mandiri User ID")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("GDN Handling Fee")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("KlikPay Full Approval Code")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("KlikPay Inst Approval Code")); 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Billing Final")); 

		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Category")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Mechant")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Free Shipping Code")); 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Product Price"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Shipping Cost")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("GDN Handling Fee")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Total Sales")); 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Date FP")); 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Date fp")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Status Order items")); 		 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("CC No")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("BIN")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Bank Penerbit")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Tipe")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("ECI")); 
		 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 1")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 2")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 3")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 4")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 5")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 6")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 7")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 8")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 9")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 10")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 11")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 12")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 13")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 14")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 15")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 16")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 17")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 18")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 19")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 20")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 21")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 22")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 23")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 24")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 25")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 26")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 27")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 28")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 29")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 30")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 31")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 32")); 
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 33"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 34"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 35"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 37"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 38"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 39"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("RP 40"));
		 headerRow.createCell(++countCol).setCellValue(new HSSFRichTextString("Total")); 
		 
		 CellStyle headerCellstyle = wb.createCellStyle();
		    headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
		    headerCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		    headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
		    headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		    headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
		    headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		    headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
		    headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		    headerCellstyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		    headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		    headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	  

			for(int i=startCol; i<=countCol+startCol; i++){
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				//Autosize the columns while we are there

				if(i+40>=countCol+startCol ) 
					sheet.autoSizeColumn(i);
				else if(i<7)
					sheet.setColumnWidth(i, 3000);
				else
					sheet.setColumnWidth(i, 5000);
			}    
		    
		if (!itemList.isEmpty()){								  					  
				CellStyle style = wb.createCellStyle();
			    style.setBorderBottom(CellStyle.BORDER_THIN);
			    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    style.setBorderLeft(CellStyle.BORDER_THIN);
			    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			    style.setBorderRight(CellStyle.BORDER_THIN);
			    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			    style.setBorderTop(CellStyle.BORDER_THIN);
			    style.setTopBorderColor(IndexedColors.BLACK.getIndex());    	
			    style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			    style.setFillPattern(CellStyle.SOLID_FOREGROUND);				 
				
			  	System.out.println("Start WriteExcel ");	
				String orderGroup ="";
				int noUrutOrder=0;
			  	for (VenOrderItem order : itemList){
					HSSFRow row = sheet.createRow(++startRow);
					List<VenOrderPaymentAllocation> paymentList =null;
					List<VenOrderContactDetail> orderContactList = null;
					List<VenOrderItemContactDetail> orderItemContactList = null;
					List<VenOrderItemStatusHistory> itemStatusHistoryList = null;
					List<LogAirwayBill> awbList = null;
					List<FrdFraudSuspicionPoint> frdFraudSuspicionPointList=null;				
					List<VenBinCreditLimitEstimate> binCreditLimitList=null;
					List<LogAirwayBill> logAirwayBillList=null;
					List<VenOrderAddress> orderAddressList = null;
					List<VenContactDetail> contactList = null;
					
					VenAddress venAddressList = null;
					
					// this query was separated from above query due to "cannot simultaneously fetch multiple bags" error from hibernate
					query = "select o from VenOrderContactDetail o where o.venOrder.orderId = "+order.getVenOrder().getOrderId();
					orderContactList = orderContactDetailSessionHome.queryByRange(query, 0, 0);
					order.getVenOrder().setVenOrderContactDetails(orderContactList);
					
					query = "select o from VenOrderItemContactDetail o where o.venOrderItem.orderItemId = "+order.getOrderItemId();
					orderItemContactList = orderItemContactDetailSessionHome.queryByRange(query, 0, 0);
					order.setVenOrderItemContactDetails(orderItemContactList);
					
					query="select o from VenOrderPaymentAllocation o where o.venOrder.orderId ="+order.getVenOrder().getOrderId();			 		    		 
			    	paymentList = allocationsessionHome.queryByRange(query, 0, 0);	
			    	
			    	query = "select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId = " + order.getOrderItemId();
			    	itemStatusHistoryList = itemStatusHistorySessionHome.queryByRange(query, 0, 0);
			    	order.setVenOrderItemStatusHistories(itemStatusHistoryList);
			    	
			    	query = "select o from LogAirwayBill o where o.venOrderItem.orderItemId = " + order.getOrderItemId();
			    	awbList = awbSessionHome.queryByRange(query, 0, 0);
			    	order.setLogAirwayBills(awbList);
			    	
			    	query = "select o from VenOrderAddress o where o.venOrder.orderId = " + order.getVenOrder().getOrderId();
			    	orderAddressList = orderAddressSessionHome.queryByRange(query, 0, 0);
			    	order.getVenOrder().setVenOrderAddresses(orderAddressList);
			    	
			    	query = "select o from VenContactDetail o where o.venParty.partyId = " + order.getVenRecipient().getVenParty().getPartyId();
			    	contactList = contactSessionHome.queryByRange(query, 0, 0);
			    	order.getVenRecipient().getVenParty().setVenContactDetails(contactList);
			    	
					countCol=startCol;
					if(orderGroup.equals("")){
						orderGroup=order.getVenOrder().getWcsOrderId();
						noUrutOrder++;
					}
			    	
					HSSFCell nameCell = row.createCell(countCol);nameCell.setCellValue(new Integer(order.getVenOrder().getOrderDate()!=null?new SimpleDateFormat("MM").format(order.getVenOrder().getOrderDate().getTime()):"0"));
			    	nameCell = row.createCell(++countCol);nameCell.setCellValue(new Integer(order.getVenOrder().getOrderDate()!=null?new SimpleDateFormat("yyyy").format(order.getVenOrder().getOrderDate().getTime()):"0"));			
			    	
			    	if(orderGroup.equals(order.getVenOrder().getWcsOrderId())){
		    			 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(noUrutOrder==1?noUrutOrder+"":null));
			    	}else{
			    		 noUrutOrder++;
				    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(noUrutOrder+""));	
			    	}		
			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getOrderDate()!=null?new SimpleDateFormat("dd-MMM-yy").format(order.getVenOrder().getOrderDate().getTime()):null));			
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getVenOrder().getWcsOrderId()));			
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getWcsOrderItemId()));	
			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getVenOrder().getWcsOrderId()));					
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getOrderDate()!=null?new SimpleDateFormat("dd-MMM-yy HH:mm").format(order.getVenOrder().getOrderDate().getTime()):null));
					 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getVenOrder().getAmount().add(paymentList!=null && paymentList.size()>0 ? paymentList.get(0).getVenOrderPayment().getHandlingFee():new BigDecimal(0)).toString()));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getIpAddress()));	 	 
			    	 	    	
			    	 String phone="",mobile="",email="",customer="",typeCus="Unregistered shopper",userName="";
			    	 if(order.getVenOrder().getVenCustomer()!=null){
			    		 if(Util.isNull(order.getVenOrder().getVenCustomer().getUserType(), "").equals("R")){
		    				 typeCus="Registered shopper";
		    			 }
		    			 userName=order.getVenOrder().getVenCustomer().getCustomerUserName();		    		
			    	 }			    
			    	 
			    	 if(order.getVenOrder().getVenOrderContactDetails()!=null && order.getVenOrder().getVenOrderContactDetails().size()>0){
			    		 if(order.getVenOrder().getVenCustomer().getVenParty()!=null){
			    			 customer=order.getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName();
			    		 }
			    	 	for(VenOrderContactDetail itemVenCont : order.getVenOrder().getVenOrderContactDetails()){			    			
			    			 if(itemVenCont.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_MOBILE && mobile.equals("")){								
									mobile=itemVenCont.getVenContactDetail().getContactDetail();
								}else if(itemVenCont.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_PHONE && phone.equals("")){
									phone=itemVenCont.getVenContactDetail().getContactDetail();
								}else if(itemVenCont.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_EMAIL && email.equals("")){
									email=itemVenCont.getVenContactDetail().getContactDetail();
								}
			    		 }
	    			 }else if(order.getVenOrder().getVenCustomer().getVenParty()!=null){			    			
			    		 customer=order.getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName();
			    		 if(order.getVenOrder().getVenCustomer().getVenParty().getVenContactDetails()!=null){			    			
			    			 for(VenContactDetail itemCp : order.getVenOrder().getVenCustomer().getVenParty().getVenContactDetails()){
									if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_MOBILE && mobile.equals("")){								
										mobile=itemCp.getContactDetail();
									}else if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_PHONE && phone.equals("")){
										phone=itemCp.getContactDetail();
									}else if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_EMAIL && email.equals("")){
										email=itemCp.getContactDetail();
									}
								}		
			    	
			    		 }		    			
		    		 }					
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(typeCus));	 
					 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(userName));	 	 						 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(customer));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(phone));	 	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(mobile));			    	 	    	 			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(email));			    	 
			
			    	 if(order.getVenOrder().getVenOrderAddresses()!=null && order.getVenOrder().getVenOrderAddresses().size()>0){
			    		 venAddressList=order.getVenOrder().getVenOrderAddresses().get(0).getVenAddress();
			    	 }else if(order.getVenOrder().getVenCustomer().getVenParty().getVenPartyAddresses()!=null && order.getVenOrder().getVenCustomer().getVenParty().getVenPartyAddresses().size()>0){
			    		 venAddressList=order.getVenOrder().getVenCustomer().getVenParty().getVenPartyAddresses().get(0).getVenAddress(); 
			    	 }			    		
			    	 		    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(venAddressList!=null?venAddressList.getStreetAddress1():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(venAddressList!=null?venAddressList.getKelurahan()+"/"+venAddressList.getKecamatan():""));	    	 			    	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(venAddressList!=null && venAddressList.getVenCity() != null ?venAddressList.getVenCity().getCityName():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(venAddressList!=null && venAddressList.getVenState() != null?venAddressList.getVenState().getStateName():""));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(venAddressList!=null && venAddressList.getVenCountry() !=null ?venAddressList.getVenCountry().getCountryCode():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(venAddressList!=null?venAddressList.getPostalCode():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getVenCustomer().getDateOfBirth()!=null?new SimpleDateFormat("dd-MMM-yy").format(order.getVenOrder().getVenCustomer().getDateOfBirth()):null));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getWcsOrderItemId()));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenMerchantProduct().getWcsProductSku()));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenMerchantProduct().getWcsProductName()));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getQuantity().toString()));	
			    	 //20
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getPrice().toString()));	
			    	 String category ="",category1="";
			    	 if(order.getVenMerchantProduct().getVenProductCategories()!=null){			    		 
			    		 for(VenProductCategory itemCat: order.getVenMerchantProduct().getVenProductCategories()){
			    			 if(itemCat.getLevel().equals(2)){
			    				 category =itemCat.getProductCategory().contains("(")?itemCat.getProductCategory().split("\\(")[0]:itemCat.getProductCategory();
			    			 }
			    			 if(itemCat.getLevel().equals(1)){
			    				 category1 =itemCat.getProductCategory().contains("(")?itemCat.getProductCategory().split("\\(")[0]:itemCat.getProductCategory();
			    			 }
			    		 }
					}
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(category));			
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getLogLogisticService().getLogisticsServiceDesc()));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenAddress().getStreetAddress1()!= null?order.getVenAddress().getStreetAddress1():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenAddress().getKecamatan()+"/"+order.getVenAddress().getKelurahan()));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenAddress().getVenCity()!=null?order.getVenAddress().getVenCity().getCityName():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenAddress().getVenState()!=null?order.getVenAddress().getVenState().getStateName():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenAddress().getVenCountry()!=null?order.getVenAddress().getVenCountry().getCountryCode():""));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenAddress().getPostalCode()));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double((order.getShippingCost()!=null?order.getShippingCost():new BigDecimal(0)).add(order.getInsuranceCost()!=null?order.getInsuranceCost():new BigDecimal(0)).toString()));	
			    	//30
			     	 BigDecimal otherCost=new BigDecimal(0);
			    	 if(order.getLogAirwayBills()!=null){
			    		 logAirwayBillList=order.getLogAirwayBills();
			    		 for(LogAirwayBill itemLog : logAirwayBillList){
			    			 otherCost=otherCost.add(itemLog.getOtherCharge()!=null?itemLog.getOtherCharge():new BigDecimal(0));
			    		 }
			    	 }
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(otherCost.toString())); //special handling cost
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getGiftWrapPrice()!=null?order.getGiftWrapPrice().toString():"0"));
			       	 BigDecimal adj=new BigDecimal(0);
			       	 String freeShippingCode="",promoCode="";
			    	 if(order.getVenOrderItemAdjustments()!=null){	
			    		 BigDecimal shippingAmount =(order.getShippingCost()!=null?order.getShippingCost():new BigDecimal(0)).add(order.getInsuranceCost()!=null?order.getInsuranceCost():new BigDecimal(0));
			    		 for(VenOrderItemAdjustment itemAdj: order.getVenOrderItemAdjustments()){
			    			 if(itemAdj.getAmount()!=null){
				    			 adj=adj.add(itemAdj.getAmount());
			    			 }
			    			 
			    			 //untuk order baru
			    			 if(itemAdj.getVenPromotion().getPromotionName().toLowerCase().contains("free shipping")){
			    				 freeShippingCode=itemAdj.getVenPromotion().getPromotionCode();
			    			 }
			    			 
			    			 //untuk order lama
			    			 if(freeShippingCode==""){
			    				 if(shippingAmount.add(itemAdj.getAmount()).compareTo(new BigDecimal(0))==0){
			    					 freeShippingCode=itemAdj.getVenPromotion().getPromotionCode();
			    				 }
			    			 }
			    			 
			    			 if(itemAdj.getVenPromotion().getPromotionCode()!=null){
			    				 if(!promoCode.equals(""))  promoCode = promoCode+" and ";
				    				 	promoCode = promoCode + itemAdj.getVenPromotion().getPromotionCode();
			    			 }
			    		 }
					}
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(adj.toString()));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(promoCode));	
			    	 mobile=""; phone="";
			    	 if(order.getVenOrderItemContactDetails()!=null){						    		 
			    		 for(VenOrderItemContactDetail itemRec: order.getVenOrderItemContactDetails()){
			    			 if(itemRec.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_MOBILE && mobile.equals("")){								
									mobile=itemRec.getVenContactDetail().getContactDetail();
								}else if(itemRec.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_PHONE && phone.equals("")){
									phone=itemRec.getVenContactDetail().getContactDetail();
								}
			    		 }
					}
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(phone));		
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(mobile));		    	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getLogisticsPricePerKg()!=null?order.getLogisticsPricePerKg().toString():"0"));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getVenOrderStatus().getOrderStatusCode()));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(order.getVenOrder().getWcsOrderId()));	

			      	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?paymentList.get(0).getVenOrderPayment().getWcsPaymentId().toString():""));
			    	//40
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?paymentList.get(0).getVenOrderPayment().getPaymentConfirmationNumber():""));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getTenor()!=null?paymentList.get(0).getVenOrderPayment().getTenor().toString():"0"):"0"));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getInstallment()!=null?paymentList.get(0).getVenOrderPayment().getInstallment().toString():"0"):"0"));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getInterest()!=null?paymentList.get(0).getVenOrderPayment().getInterest().toString():"0"):"0"));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getInterestInstallment()!=null?paymentList.get(0).getVenOrderPayment().getInterestInstallment().toString():"0"):"0"));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(paymentList!=null && paymentList.size()>0?((paymentList.get(0).getVenOrderPayment().getInstallment()!=null?paymentList.get(0).getVenOrderPayment().getInstallment():new BigDecimal("0")).add(paymentList.get(0).getVenOrderPayment().getInterest()!=null?paymentList.get(0).getVenOrderPayment().getInterest():new BigDecimal(0)).toString()):"0"));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getVenAddress().getStreetAddress1()):""));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getVenAddress().getKecamatan()+"/"+paymentList.get(0).getVenOrderPayment().getVenAddress().getKelurahan()):""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?((paymentList.get(0).getVenOrderPayment().getVenAddress().getVenCity()!= null ? paymentList.get(0).getVenOrderPayment().getVenAddress().getVenCity().getCityName():"")):""));	
			    	//50
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getVenAddress().getVenState() != null ? paymentList.get(0).getVenOrderPayment().getVenAddress().getVenState().getStateName():""):""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getVenAddress().getVenCountry() != null ? paymentList.get(0).getVenOrderPayment().getVenAddress().getVenCountry().getCountryCode():""):""));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?paymentList.get(0).getVenOrderPayment().getVenAddress().getPostalCode():""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getAmount()!=null?paymentList.get(0).getVenOrderPayment().getAmount().toString():"0"):"0"));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getVenPaymentStatus().getPaymentStatusId()==0?"MApproving":"MApproved"):""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?paymentList.get(0).getVenOrderPayment().getVirtualAccountNumber():""));
			    	 String ibBCA="",ibMandiri="",fullPay="",InstalPay="";
			    	 String ccOrIb="";
			    	 if(paymentList!=null && paymentList.size()>0){
			    		 if(paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikBCA))
			    			    ibBCA=paymentList.get(0).getVenOrderPayment().getInternetBankingId();
			    		else if(paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_MandiriKlikpay))	 
			    				ibMandiri=paymentList.get(0).getVenOrderPayment().getInternetBankingId();
			    		else if(paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYFullPayment)){	 
			    			fullPay=paymentList.get(0).getVenOrderPayment().getPaymentConfirmationNumber();
			    		 	if(fullPay==null) ccOrIb="-IB";
			    		 	else ccOrIb="-CC";			    		 	
			    		}else if(paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYInstallment))	 
			    			InstalPay=paymentList.get(0).getVenOrderPayment().getPaymentConfirmationNumber();					    		
			    	 }			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(ibBCA));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(ibMandiri));	
			    	 
			    	 if(orderGroup.equals(order.getVenOrder().getWcsOrderId()) && noUrutOrder!=1){
			    		 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double("0"));	
			    	 }else{
				    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getHandlingFee()!=null?paymentList.get(0).getVenOrderPayment().getHandlingFee().toString():"0"):"0"));	
			    	 }	
			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(fullPay));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(InstalPay));				  
			    	
			      	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode()+ccOrIb:""));	
			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(category1));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenMerchantProduct().getVenMerchant().getVenParty()!=null?order.getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():""));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(freeShippingCode));
			    	 
			    	 BigDecimal priceShipp = (order.getShippingCost()!=null?order.getShippingCost():new BigDecimal(0)).add(order.getInsuranceCost()!=null?order.getInsuranceCost():new BigDecimal(0));			    	
			    	 BigDecimal pricehandling = new BigDecimal(0);
			    	 if(!orderGroup.equals(order.getVenOrder().getWcsOrderId())){
				    	orderGroup=order.getVenOrder().getWcsOrderId();
					    pricehandling = paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getHandlingFee()!=null?paymentList.get(0).getVenOrderPayment().getHandlingFee():new BigDecimal(0)):new BigDecimal(0);
					 }else if(noUrutOrder==1){
						pricehandling = paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getHandlingFee()!=null?paymentList.get(0).getVenOrderPayment().getHandlingFee():new BigDecimal(0)):new BigDecimal(0);
					 }
			    	 
			    	 BigDecimal price = order.getPrice().subtract(priceShipp);
			    	 BigDecimal totprice = price.multiply(new BigDecimal(order.getQuantity())).add(priceShipp).add(pricehandling);
			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(price.toString()));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(priceShipp.toString()));
					 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(pricehandling.toString()));			    	 
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(totprice.toString()));
			    	 
			    	 Date dts=null;
			    	 if(order.getVenOrderItemStatusHistories()!=null){
			    		 for(VenOrderItemStatusHistory  itemStatus :order.getVenOrderItemStatusHistories()){
			    			 if(itemStatus.getVenOrderStatus().getOrderStatusId().equals(VeniceConstants.VEN_ORDER_STATUS_FP)){
			    				 dts=itemStatus.getId().getHistoryTimestamp();
			    				 break;
			    			 }			    			 
			    		 }			    		 
			    	 }
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(dts!=null?new SimpleDateFormat("dd-MMM-yy HH:mm").format(dts):""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(dts!=null?new SimpleDateFormat("dd-MMM-yy").format(dts):""));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrderStatus().getOrderStatusCode()));		
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?paymentList.get(0).getVenOrderPayment().getMaskedCreditCardNumber():""));
			    	 String type="",number="", issuingBank="";
			    	 if(paymentList!=null && paymentList.size()>0){
			    		 if(paymentList.get(0).getVenOrderPayment().getMaskedCreditCardNumber()!=null){
			    			 if(paymentList.get(0).getVenOrderPayment().getMaskedCreditCardNumber().length()>6){
			    					binCreditLimitList = binSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.isActive=true and o.binNumber like '"+paymentList.get(0).getVenOrderPayment().getMaskedCreditCardNumber().substring(0, 6)+"'", 0, 1);
										if(binCreditLimitList.size()>0){
										type=binCreditLimitList.get(0).getVenCardType().getCardTypeDesc()+" - "+binCreditLimitList.get(0).getDescription();
										number=binCreditLimitList.get(0).getBinNumber();
										issuingBank=binCreditLimitList.get(0).getBankName();
									}
			    			 }				    				
			    		 }			    		 
			    	 }			    	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(number));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(issuingBank));	
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(type));
			    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(paymentList!=null && paymentList.size()>0?(paymentList.get(0).getVenOrderPayment().getThreeDsSecurityLevelAuth()!=null?new Integer(paymentList.get(0).getVenOrderPayment().getThreeDsSecurityLevelAuth()).toString():""):""));	
			    	 
			    	 frdFraudSuspicionPointList=frdFraudSusPointsSessionHome.queryByRange("select o from  FrdFraudSuspicionPoint o where o.frdFraudSuspicionCase.fraudSuspicionCaseId in (select u.fraudSuspicionCaseId from "+
			    			 "FrdFraudSuspicionCase u where u.venOrder.orderId="+order.getVenOrder().getOrderId()+" ) order by o.fraudSuspicionPointsId asc", 0, 0);

			    	 if(frdFraudSuspicionPointList!=null && frdFraudSuspicionPointList.size()>0){
			    		 for(FrdFraudSuspicionPoint itemPoint: frdFraudSuspicionPointList){
					    	 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(itemPoint.getRiskPoints().toString()));				    			 
			    		 }				    		 
			    		 if(frdFraudSuspicionPointList.size()<39){
			    			 for(int i=0;i<39-frdFraudSuspicionPointList.size();i++){
			    				 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(""));	
			    			 }
			    			 
			    		 }
			    		 nameCell = row.createCell(++countCol);nameCell.setCellValue(new Double(frdFraudSuspicionPointList.get(0).getFrdFraudSuspicionCase().getFraudTotalPoints().toString()));
						    
			    	 }else{
			    		 for(int i=0;i<40;i++){
		    				 nameCell = row.createCell(++countCol);nameCell.setCellValue(new HSSFRichTextString(""));	
		    			 }				 			
			    	 }				    	 
			    	 for(int i=startCol; i<=countCol+startCol; i++){
							HSSFCell cell = row.getCell(i);
							 cell.setCellStyle(style);	
						}    
			}			
			 System.out.println("End WriteExcel ");
		}else
			System.out.println("Record is null ");			
		} catch (Exception e)   {
			_log.error("Exception in Excel Sample Servlet", e);
			throw new ServletException("Exception in Excel Sample Servlet", e);
	    } finally   {		     
		     if (locator != null)
				try {
					locator.close();
				} catch (Exception e) {				
					e.printStackTrace();
				}				
	    }	    
		return wb;
	}
	
	private String formatDouble(Double value){
		NumberFormat nf = new DecimalFormat("#,###,###,###,###");
		return "Rp " + nf.format(value.doubleValue()).replace(',', '.');
	}
}
