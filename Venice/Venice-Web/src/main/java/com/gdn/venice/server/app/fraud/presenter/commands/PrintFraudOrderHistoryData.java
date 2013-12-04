package com.gdn.venice.server.app.fraud.presenter.commands;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.util.VeniceConstants;

public class PrintFraudOrderHistoryData {
	protected static Logger _log = null;
	HSSFWorkbook wb=null;


	/**
	 * Basic constructor
	 */
	public PrintFraudOrderHistoryData(HSSFWorkbook wb) {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.presenter.commands.PrintFraudOrderHistoryData");
		this.wb=wb;
	}
	
	public HSSFWorkbook ExportExcel(Map<String, Object> params,HSSFSheet sheet) throws ServletException {		
			
		Locator<Object> locator = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String orderId = (String) params.get("oid");
		String filterId = (String) params.get("filterid");
		String filter = (String) params.get("filter");		
				
		try{				
			
			locator = new Locator<Object>();			
			
			VenOrderItemSessionEJBRemote orderitemsessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");		
			VenOrderPaymentAllocationSessionEJBRemote allocationsessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");		
			VenBinCreditLimitEstimateSessionEJBRemote binSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
			VenOrderItemStatusHistorySessionEJBRemote itemStatusHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");
			
			/*
			 * 1. cari order yang nama customernya sama
			 * 2. cari order yang no telp sama
			 * 3. cari order yang no Hp sama
			 * 4. cari order yang email sama
			 * 5. cari order yang customer address sama
			 * 6. cari order yang shipping address sama
			 * 7. cari order yang billing address sama
			 * 8. cari order yang no CC sama
			 */		
			List<VenOrderItem> itemList = new ArrayList<VenOrderItem>();		
			String query="";			
			
			if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_FULLNAME_FILTER)){
				if(!filter.equals("")){	
						query = "select o from VenOrderItem o " +
								"left join fetch o.venOrder "+
								"left join fetch o.venOrder.venOrderContactDetails " +
								"where o.venOrder.wcsOrderId <> '"+orderId+
								"' and UPPER(o.venOrder.venCustomer.venParty.fullOrLegalName) like '"+filter.toUpperCase()+"' "+
								"order by o.venOrder.wcsOrderId";
	                  	itemList = orderitemsessionHome.queryByRange(query,0,0);
				}
			} if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_PHONE_FILTER)){
				if(!filter.equals("")){
					query="select o from VenOrderItem o " +
						    "left join fetch o.venOrder "+
						    "left join fetch o.venOrder.venOrderContactDetails " +
							"where o.venOrder.wcsOrderId <> '"+orderId+
							"' and ( o.venOrder.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where u.contactDetail in ("+filter+") " +
							"and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_PHONE+") "+
							"or o.venOrder.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where r.venContactDetail.contactDetail in ("+filter+") " +
							"and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_PHONE+") )"+
							"order by o.venOrder.wcsOrderId";
                  	itemList = orderitemsessionHome.queryByRange(query,0,0);	
				}
			} else if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_MOBILE_FILTER)){
				if(!filter.equals("")){	
					query="select o from VenOrderItem o " +
				    		"left join fetch o.venOrder "+
				    		"left join fetch o.venOrder.venOrderContactDetails " +
							"where o.venOrder.wcsOrderId <> '"+orderId+
							"' and ( o.venOrder.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where u.contactDetail in ("+filter+") " +
							"and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_MOBILE+") "+
							"or o.venOrder.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where r.venContactDetail.contactDetail in ("+filter+") " +
							"and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_MOBILE+") )"+
							"order by o.venOrder.wcsOrderId";
   					itemList = orderitemsessionHome.queryByRange(query,0,0);						
				}
			}else if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_EMAIL_FILTER)){
				if(!filter.equals("")){					
					query="select o from VenOrderItem o " +
				    			"left join fetch o.venOrder "+
				    			"left join fetch o.venOrder.venOrderContactDetails " +
				    			"where o.venOrder.wcsOrderId <> '"+orderId+
				    			"' and ( o.venOrder.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where UPPER(u.contactDetail) in ("+filter.toUpperCase()+") " +
				    			"and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_EMAIL+") "+
				    			"or o.venOrder.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where UPPER(r.venContactDetail.contactDetail) in ("+filter.toUpperCase()+") " +
				    			"and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_EMAIL+")) "+
								"order by o.venOrder.wcsOrderId";
					itemList = orderitemsessionHome.queryByRange(query,0,0);						
				}
			}if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_ADDRESS_FILTER)){
				if(!filter.equals("")){	
					query="select o from VenOrderItem o " +
				    			"left join fetch o.venOrder "+
				    			"left join fetch o.venOrder.venOrderContactDetails " +
				    			"where o.venOrder.wcsOrderId <> '"+orderId+
				    			"' and (o.venOrder.venCustomer.venParty.partyId in (select u.venParty.partyId from VenPartyAddress u where UPPER(REPLACE(u.venAddress.streetAddress1,'\n','')) in ('"+filter.toUpperCase()+"'))  "+
				    			"or o.venOrder.orderId in (select u.venOrder.orderId from VenOrderAddress u where UPPER(REPLACE(u.venAddress.streetAddress1,'\n','')) in ('"+filter.toUpperCase()+"'))) "+
				    			"order by o.venOrder.wcsOrderId";
					itemList = orderitemsessionHome.queryByRange(query,0,0);					
				}
			} else if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_SHIPPING_ADDRESS_FILTER)){
				if(!filter.equals("")){	
					query="select o from VenOrderItem o " +
								"left join fetch o.venOrder "+
								"left join fetch o.venOrder.venOrderContactDetails " +
								"where o.venOrder.wcsOrderId <> '"+orderId+
								"' and o.venOrder.orderId in (select u.venOrder.orderId from VenOrderItem u where UPPER(REPLACE(u.venAddress.streetAddress1,'\n','')) like '"+filter.toUpperCase()+"')"+
								"order by o.venOrder.wcsOrderId";
					itemList = orderitemsessionHome.queryByRange(query,0,0);					
				}
			}else if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_BILLING_ADDRESS_FILTER)){
				if(!filter.equals("")){	
					query="select o from VenOrderItem o " +
			    			"left join fetch o.venOrder "+
			    			"left join fetch o.venOrder.venOrderContactDetails " +
			    			"where o.venOrder.wcsOrderId <> '"+orderId+
			    			"' and o.venOrder.orderId in (select u.venOrder.orderId from VenOrderPaymentAllocation u where UPPER(REPLACE(u.venOrderPayment.venAddress.streetAddress1,'\n','')) like '"+filter.toUpperCase()+"')"+
							"order by o.venOrder.wcsOrderId";
					itemList = orderitemsessionHome.queryByRange(query,0,0);
				}				
			}else if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_IP_ADDRESS_FILTER)){
				if(!filter.equals("")){	
					query="select o from VenOrderItem o " +
								"left join fetch o.venOrder "+
								"left join fetch o.venOrder.venOrderContactDetails " +
								"where o.venOrder.wcsOrderId <> '"+orderId+
								"' and o.venOrder.ipAddress like '"+filter+"'"+
								"order by o.venOrder.wcsOrderId";
					itemList = orderitemsessionHome.queryByRange(query,0,0);					
				}
			}	else if(filterId.equals(VeniceConstants.FRD_ORDER_HISTORY_CC_NUMBER_FILTER)){
				if(!filter.equals("")){	
					query="select o from VenOrderItem o " +
								"left join fetch o.venOrder "+
								"left join fetch o.venOrder.venOrderContactDetails " +
								"where o.venOrder.wcsOrderId <> '"+orderId+"' and  " +
						  		"o.venOrder.orderId in (select u.venOrder.orderId from VenOrderPaymentAllocation u where u.venOrderPayment.maskedCreditCardNumber in ("+filter+"))"+
								"order by o.venOrder.wcsOrderId";
					itemList = orderitemsessionHome.queryByRange(query,0,0);					
				}
			}	
			
		int startRow = 4;
		int startCol=0;

		// Create the column headings
		 HSSFRow header = sheet.createRow((short) 1);
		 header.createCell(5).setCellValue(new HSSFRichTextString("ORDER DETAIL FRAUD"));
		 // Create the column headings
		 HSSFRow headerRow = sheet.createRow((short) startRow);
		 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("No"));	 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Order Id"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Order Item Id"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Order Date"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Amount"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("IP Address")); 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Customer Full Name"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Customer Phone Number"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Customer Mobile Number"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Customer Email")); 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Product Name"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Order Qty"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Product Price"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Shipping Address"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Billing Address")); 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Billing Method")); 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Payment Status")); 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("FP Date"));
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Credit Card Number")); 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Issuer Bank")); 
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("Jenis kartu"));	
		 headerRow.createCell(++startCol).setCellValue(new HSSFRichTextString("ECI")); 
		 
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
		    
			for(int i=0; i<=startCol; i++){
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				//Autosize the columns while we are there
				if(i==0 || i==startCol)
						sheet.autoSizeColumn(i);
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
			    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			    style.setFillPattern(CellStyle.SOLID_FOREGROUND);	
			    
			    CellStyle style2 = wb.createCellStyle();
			    style2.setBorderBottom(CellStyle.BORDER_THIN);
			    style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			    style2.setBorderLeft(CellStyle.BORDER_THIN);
			    style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			    style2.setBorderRight(CellStyle.BORDER_THIN);
			    style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
			    style2.setBorderTop(CellStyle.BORDER_THIN);
			    style2.setTopBorderColor(IndexedColors.BLACK.getIndex());    	
			    style2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			    
				
			  	System.out.println("Start WriteExcel ");		
			  	String orderGroup ="";
				int noUrutOrder=0;
			  	boolean firstRow=true;
			  	for (VenOrderItem order : itemList){
					HSSFRow row = sheet.createRow(++startRow);
					List<VenOrderPaymentAllocation> paymentList =null;
					List<VenOrderItemStatusHistory> itemStatusHistoryList = null;
					startCol=0;
					if(orderGroup.equals("")){
						orderGroup=order.getVenOrder().getWcsOrderId();	
						noUrutOrder++;
					}
					HSSFCell nameCell = row.createCell(startCol);
					
					if(orderGroup.equals(order.getVenOrder().getWcsOrderId())){				
						if(firstRow==true){
							nameCell.setCellValue(new HSSFRichTextString(noUrutOrder==1?noUrutOrder+"":null));
							firstRow=false;
						}else{
							nameCell.setCellValue(new HSSFRichTextString(null));							
						}						
					}else{
						noUrutOrder++;
				    	orderGroup=order.getVenOrder().getWcsOrderId();
				    	nameCell.setCellValue(new HSSFRichTextString(noUrutOrder+""));	
					}								 				   
			 
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getWcsOrderId()));
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getWcsOrderItemId()));
					 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getOrderDate()!=null?dateFormat.format(order.getVenOrder().getOrderDate().getTime()):null));
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder()!=null?formatDouble(new Double(order.getVenOrder().getAmount().toString())):"0"));	 	 
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenOrder().getIpAddress()));	 	 	    	
			    	 
			    	 String phone="",mobile="",email="",customer="";			    	
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
			    			 for(VenContactDetail itemCp:order.getVenOrder().getVenCustomer().getVenParty().getVenContactDetails()){
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
					 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(customer));	 	 						 
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(phone));
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(mobile));	 	 
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(email));			    	 	    	 			    	 
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenMerchantProduct().getWcsProductName()));
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getQuantity().toString()));	
					 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getPrice()!=null?formatDouble(new Double(order.getPrice().toString())):"0"));
			    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(order.getVenAddress().getStreetAddress1()));
			    	 
			    	 query="select o from VenOrderPaymentAllocation o where o.venOrder.orderId ="+order.getVenOrder().getOrderId();
			    	 paymentList = allocationsessionHome.queryByRange(query, 0, 0);		
			    	 if(paymentList!=null && !paymentList.isEmpty()){
			    		 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(paymentList.get(0).getVenOrderPayment().getVenAddress()!=null?paymentList.get(0).getVenOrderPayment().getVenAddress().getStreetAddress1():""));	
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc()));
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(paymentList.get(0).getVenOrderPayment().getVenPaymentStatus().getPaymentStatusCode()));
				    	 
				    	 itemStatusHistoryList = itemStatusHistorySessionHome.queryByRange("select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId = " + order.getOrderItemId(), 0, 0);
						 order.setVenOrderItemStatusHistories(itemStatusHistoryList);
						 Date dts=null;
				    	 if(order.getVenOrderItemStatusHistories()!=null){
				    		 for(VenOrderItemStatusHistory  itemStatus :order.getVenOrderItemStatusHistories()){
				    			 if(itemStatus.getVenOrderStatus().getOrderStatusId().equals(VeniceConstants.VEN_ORDER_STATUS_FP)){
				    				 dts=itemStatus.getId().getHistoryTimestamp();
				    				 break;
				    			 }			    			 
				    		 }			    		 
				    	 }
				    	 
						 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(dts!=null?new SimpleDateFormat("dd-MMM-yy").format(dts):""));
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(paymentList.get(0).getVenOrderPayment().getMaskedCreditCardNumber()));	
				    		
				    	    String creditCardNumber = "";
							String binNumber="", bankName="",type="";
							creditCardNumber = paymentList.get(0).getVenOrderPayment().getMaskedCreditCardNumber()!=null?paymentList.get(0).getVenOrderPayment().getMaskedCreditCardNumber():"";
							if(!creditCardNumber.equals("")){		
								//check CC limit from BIN number
								if(creditCardNumber.length()>6){
									binNumber = creditCardNumber.substring(0, 6);
								}			
							}
							List<VenBinCreditLimitEstimate> binCreditLimitList = binSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.isActive=true and o.binNumber like '"+binNumber+"'", 0, 1);
							if(binCreditLimitList.size()>0){
								bankName=binCreditLimitList.get(0).getBankName();
								type=binCreditLimitList.get(0).getVenCardType().getCardTypeDesc()+" - "+binCreditLimitList.get(0).getDescription();
							}
							
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(bankName));
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(type));
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(paymentList.get(0).getVenOrderPayment().getThreeDsSecurityLevelAuth()));	
			    	 }else{
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(""));	
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(""));
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(""));	
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(""));	
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(""));
				    	 nameCell = row.createCell(++startCol);nameCell.setCellValue(new HSSFRichTextString(""));	
			    	 }
			    	
				     
			}			
			 System.out.println("End WriteExcel ");
		}else
			System.out.println("Record is null ");			
		} catch (Exception e)   {
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
