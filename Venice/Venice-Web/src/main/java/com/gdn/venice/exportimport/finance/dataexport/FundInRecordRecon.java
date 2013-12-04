package com.gdn.venice.exportimport.finance.dataexport;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.util.VeniceConstants;

public class FundInRecordRecon {
	protected static Logger _log = null;
	HSSFWorkbook wb=null;


	/**
	 * Basic constructor
	 */
	public FundInRecordRecon(HSSFWorkbook wb) {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataexport.Report.FundInRecordRecon");
		this.wb=wb;
	}
	
	public HSSFWorkbook ExportExcel(Map<String, Object> params,HSSFSheet sheet) throws ServletException {		
			
		Locator<Object> locator = null;
		List<FinArFundsInReconRecord> fundInReconRecordList = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = (Date) params.get("fromDate");
		Date endDate = (Date) params.get("toDate");
		
		_log.debug("fromDate "+startDate);
		_log.debug("toDate "+endDate);
		
		try{				
			
		locator = new Locator<Object>();
		
		FinArFundsInReconRecordSessionEJBRemote sessionHome = (FinArFundsInReconRecordSessionEJBRemote) locator.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
		String query = "select o from FinArFundsInReconRecord o where o.reconcilliationRecordTimestamp" +
				" between '" + new Timestamp(startDate.getTime()) + "' and '" + new Timestamp(endDate.getTime()) + "' and" +
				" o.finArFundsInActionApplied.actionAppliedId <> "+ VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED +" and" +
				" o.finApprovalStatus.approvalStatusId = "+ VeniceConstants.FIN_APPROVAL_STATUS_APPROVED ;
		
		_log.debug("query = "+query);	
		_log.debug("query = "+query);
		
		fundInReconRecordList = sessionHome.queryByRange(query, 0, 0);
			
		int startRow = 4;
		int startCol=0;

		// Create the column headings
		 HSSFRow header = sheet.createRow((short) 1);
		 header.createCell(5).setCellValue(new HSSFRichTextString("FUND IN RECONCILEMENT REPORT"));
		
		 HSSFRow headertgl = sheet.createRow((short) 2);
		 headertgl.createCell(2).setCellValue(new HSSFRichTextString("TANGGAL : "+dateFormat.format(startDate) +" s/d "+dateFormat.format(endDate)));
		 // Create the column headings
		 HSSFRow headerRow = sheet.createRow((short) startRow);
		 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("Order ID"));
		 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order Date"));
		 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("Payment Type"));
		 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("Payment Report ID")); 
		 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("VA/CC/I")); 
		 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Payment Amount"));
		 headerRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("Paid Amount"));
		 headerRow.createCell(startCol+7).setCellValue(new HSSFRichTextString("Diff"));
		 headerRow.createCell(startCol+8).setCellValue(new HSSFRichTextString("Bank Fee")); 
		 headerRow.createCell(startCol+9).setCellValue(new HSSFRichTextString("Recon"));
		 headerRow.createCell(startCol+10).setCellValue(new HSSFRichTextString("Action Status"));
		 headerRow.createCell(startCol+11).setCellValue(new HSSFRichTextString("Approval"));
		 headerRow.createCell(startCol+12).setCellValue(new HSSFRichTextString("Refund Amount"));
		 headerRow.createCell(startCol+13).setCellValue(new HSSFRichTextString("Note")); 
		 
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
		   
			for(int i=startCol; i<14+startCol; i++){
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				//Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}    
				
		if (!fundInReconRecordList.isEmpty()){					
			  	BigDecimal sumPaidAmountKlikPAYInstallment = new BigDecimal(0);
			  	BigDecimal sumPaidAmountKlikPAYFullPayment = new BigDecimal(0);
			  	BigDecimal sumPaidAmountMIGSCreditCard = new BigDecimal(0);
			  	BigDecimal sumPaidAmountKlikBCA = new BigDecimal(0);
			  	BigDecimal sumPaidAmountMandiriKlikpay = new BigDecimal(0); 
			  	BigDecimal sumPaidAmountVirtualAccountBCA= new BigDecimal(0);
			  	BigDecimal sumPaidAmountVirtualAccountMandiri = new BigDecimal(0);
			  	BigDecimal sumPaidAmountCIMBClicks = new BigDecimal(0);
			  	BigDecimal sumPaidAmountMandiriInstPayment = new BigDecimal(0);
			  	BigDecimal sumPaidAmountXLTunai = new BigDecimal(0);
			  	BigDecimal sumPaidAmountBRI = new BigDecimal(0);
				  
			  	
			  	BigDecimal sumFeeAmountKlikPAYInstallment = new BigDecimal(0);
			  	BigDecimal sumFeeAmountKlikPAYFullPayment = new BigDecimal(0);
			  	BigDecimal sumFeeAmountMIGSCreditCard = new BigDecimal(0);
			  	BigDecimal sumFeeAmountKlikBCA = new BigDecimal(0);
				BigDecimal sumFeeAmountMandiriKlikpay = new BigDecimal(0);
				BigDecimal sumFeeAmountVirtualAccountBCA = new BigDecimal(0);
				BigDecimal sumFeeAmountVirtualAccountMandiri = new BigDecimal(0);
				BigDecimal sumFeeAmountCIMBClicks = new BigDecimal(0);
				BigDecimal sumFeeAmountMandiriInstPayment = new BigDecimal(0);
				BigDecimal sumFeeAmountXLTunai = new BigDecimal(0);
				BigDecimal sumFeeAmountBRI = new BigDecimal(0);
				  
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
			    
				
			  	_log.debug("Start WriteExcel ");		
			  	String tempStringDesc=null;
			for (FinArFundsInReconRecord finArFundsInReconRecord : fundInReconRecordList){
					HSSFRow row = sheet.createRow(++startRow);		
				
					if(finArFundsInReconRecord.getVenOrderPayment()==null){
						if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA)){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount+"BCA";
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA)){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount+"Mandiri";
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB)){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikBCA;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB)){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_MandiriKlikpay;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB)){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_CIMBClicks;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB)){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_XLTunai;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC)){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_MIGSCreditCard;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC) ){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYInstallment;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB) ){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYKlikBCA;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC) ){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikPAYKartuKredit;
						}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC) ){
							tempStringDesc=VeniceConstants.VEN_WCS_PAYMENT_TYPE_MandiriInstallment;
						}	else if (finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB)) {
							tempStringDesc =VeniceConstants.VEN_WCS_PAYMENT_TYPE_BRI;		
						}		
					}else{
						tempStringDesc=finArFundsInReconRecord.getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc();
						if(finArFundsInReconRecord.getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeId().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_ID_VirtualAccount)){
							tempStringDesc=tempStringDesc+finArFundsInReconRecord.getVenOrderPayment().getVenBank().getBankShortName();
						}
					}
			    
					 BigDecimal paid = finArFundsInReconRecord.getProviderReportPaidAmount()!=null?finArFundsInReconRecord.getProviderReportPaidAmount():new BigDecimal(0);
			    	 BigDecimal fee = finArFundsInReconRecord.getProviderReportFeeAmount()!=null?finArFundsInReconRecord.getProviderReportFeeAmount():new BigDecimal(0);
			    	 
			    	 HSSFCell nameCell = row.createCell(startCol);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getWcsOrderId()));
			    	 nameCell = row.createCell(startCol+1);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getOrderDate()!=null?dateFormat.format(finArFundsInReconRecord.getOrderDate()):null));
			    	 nameCell = row.createCell(startCol+2);nameCell.setCellValue(new HSSFRichTextString(tempStringDesc));	 	 
			    	 nameCell = row.createCell(startCol+3);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getFinArFundsInReport()!=null?finArFundsInReconRecord.getFinArFundsInReport().getPaymentReportId().toString():""));	 
			    	 nameCell = row.createCell(startCol+4);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getNomorReff()));	 	 
			    	 nameCell = row.createCell(startCol+5);nameCell.setCellValue(new Double(finArFundsInReconRecord.getPaymentAmount()!=null?finArFundsInReconRecord.getPaymentAmount().toString():"0"));	 	 
			    	 nameCell = row.createCell(startCol+6);nameCell.setCellValue(new Double(paid.toString()));
			    	 nameCell = row.createCell(startCol+7);nameCell.setCellValue(new Double(finArFundsInReconRecord.getRemainingBalanceAmount()!=null?finArFundsInReconRecord.getRemainingBalanceAmount()+"":"0"));	 	 
			    	 nameCell = row.createCell(startCol+8);nameCell.setCellValue(new Double(fee.toString()));	 	 
			    	 nameCell = row.createCell(startCol+9);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getFinArReconResult().getReconResultDesc()));
			    	 nameCell = row.createCell(startCol+10);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getFinArFundsInActionApplied().getActionAppliedDesc()));
		    	  	 nameCell = row.createCell(startCol+11);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getFinApprovalStatus().getApprovalStatusDesc()));
			    	 nameCell = row.createCell(startCol+12);nameCell.setCellValue(new Double(finArFundsInReconRecord.getRefundAmount()+""));	 	 
			    	 nameCell = row.createCell(startCol+13);nameCell.setCellValue(new HSSFRichTextString(finArFundsInReconRecord.getComment()));	 	 
				     
			    	 for(int i=startCol; i<14+startCol; i++){
							HSSFCell cells = row.getCell(i);
							if (startRow%2==0)
								cells.setCellStyle(style);
							else
								cells.setCellStyle(style2);														
							sheet.autoSizeColumn(i);
						}				
			    
			    	 if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA)){
							 sumPaidAmountVirtualAccountBCA=sumPaidAmountVirtualAccountBCA.add(paid);
					    	 sumFeeAmountVirtualAccountBCA=sumFeeAmountVirtualAccountBCA.add(fee);
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA)){
							 sumPaidAmountVirtualAccountMandiri=sumPaidAmountVirtualAccountMandiri.add(paid);
					    	 sumFeeAmountVirtualAccountMandiri=sumFeeAmountVirtualAccountMandiri.add(fee);	
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB)){
							sumPaidAmountKlikBCA=sumPaidAmountKlikBCA.add(paid);
							sumFeeAmountKlikBCA=sumFeeAmountKlikBCA.add(fee);		
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB)){
							sumPaidAmountMandiriKlikpay=sumPaidAmountMandiriKlikpay.add(paid);
							sumFeeAmountMandiriKlikpay=sumFeeAmountMandiriKlikpay.add(fee);	
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB)){
							sumPaidAmountCIMBClicks=sumPaidAmountCIMBClicks.add(paid);
							sumFeeAmountCIMBClicks=sumFeeAmountCIMBClicks.add(fee);	
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB)){
						sumPaidAmountXLTunai=sumPaidAmountXLTunai.add(paid);
				    	 sumFeeAmountXLTunai=sumFeeAmountXLTunai.add(fee);	
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC)){
							sumPaidAmountMIGSCreditCard=sumPaidAmountMIGSCreditCard.add(paid);
							sumFeeAmountMIGSCreditCard=sumFeeAmountMIGSCreditCard.add(fee);
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC) || 
								finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC) ){
							sumPaidAmountKlikPAYInstallment=sumPaidAmountKlikPAYInstallment.add(paid);
							sumFeeAmountKlikPAYInstallment=sumFeeAmountKlikPAYInstallment.add(fee);
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB) ){
							sumPaidAmountKlikPAYFullPayment=sumPaidAmountKlikPAYFullPayment.add(paid);
							sumFeeAmountKlikPAYFullPayment=sumFeeAmountKlikPAYFullPayment.add(fee);
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC) ){
						sumPaidAmountMandiriInstPayment=sumPaidAmountMandiriInstPayment.add(paid);
						sumFeeAmountMandiriInstPayment=sumFeeAmountMandiriInstPayment.add(fee);
					}else if(finArFundsInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB) ){
						sumPaidAmountBRI=sumPaidAmountBRI.add(paid);
						sumFeeAmountBRI=sumFeeAmountBRI.add(fee);
					}	
			    	 
				     }
			startRow=startRow+2;
			
			HSSFRow headerSumRow = sheet.createRow((short) startRow);
			headerSumRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("PAYMENT TYPE          "));
			headerSumRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("TOTAL PAID AMOUNT"));
			headerSumRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("TOTAL BANK FEE       "));
			headerSumRow.createCell(startCol+7).setCellValue(new HSSFRichTextString("NET AMOUNT      "));
			
			for(int i=startCol+4; i<=startCol+7; i++){
				HSSFCell cell = headerSumRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				//Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}    

			HSSFRow detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("KlikPay (Credit Card)"));			
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountKlikPAYInstallment.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountKlikPAYInstallment.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountKlikPAYInstallment.subtract(sumFeeAmountKlikPAYInstallment).toString()));

			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("KlikPay (Electronic Banking)"));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountKlikPAYFullPayment.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountKlikPAYFullPayment.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountKlikPAYFullPayment.subtract(sumFeeAmountKlikPAYFullPayment).toString()));
				
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Total KlikPay (Credit Card)+KlikPay (Electronic Banking)"));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountKlikPAYInstallment.add(sumPaidAmountKlikPAYFullPayment).toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountKlikPAYInstallment.add(sumFeeAmountKlikPAYFullPayment).toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountKlikPAYInstallment.add(sumPaidAmountKlikPAYFullPayment).subtract(sumFeeAmountKlikPAYInstallment.add(sumFeeAmountKlikPAYFullPayment)).toString()));
	
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(""));
			detailRow.createCell(startCol+5).setCellValue(new HSSFRichTextString(""));
			detailRow.createCell(startCol+6).setCellValue(new HSSFRichTextString(""));
			detailRow.createCell(startCol+7).setCellValue(new HSSFRichTextString(""));
	
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("MIGSCreditCard"));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountMIGSCreditCard.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountMIGSCreditCard.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountMIGSCreditCard.subtract(sumFeeAmountMIGSCreditCard).toString()));
			
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikBCA));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountKlikBCA.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountKlikBCA.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountKlikBCA.subtract(sumFeeAmountKlikBCA).toString()));
			
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Total MIGSCreditCard+"+VeniceConstants.VEN_WCS_PAYMENT_TYPE_KlikBCA));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountMIGSCreditCard.add(sumPaidAmountKlikBCA).toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(new Double(sumFeeAmountMIGSCreditCard.add(sumFeeAmountKlikBCA).toString())));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountMIGSCreditCard.add(sumPaidAmountKlikBCA).subtract(sumFeeAmountMIGSCreditCard.add(sumFeeAmountKlikBCA)).toString()));
				
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(""));
			detailRow.createCell(startCol+5).setCellValue(new HSSFRichTextString(""));
			detailRow.createCell(startCol+6).setCellValue(new HSSFRichTextString(""));
			detailRow.createCell(startCol+7).setCellValue(new HSSFRichTextString(""));
	
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_MandiriKlikpay));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountMandiriKlikpay.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountMandiriKlikpay.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountMandiriKlikpay.subtract(sumFeeAmountMandiriKlikpay).toString()));
		
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount+"BCA"));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountVirtualAccountBCA.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountVirtualAccountBCA.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountVirtualAccountBCA.subtract(sumFeeAmountVirtualAccountBCA).toString()));
			
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount+"Mandiri"));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountVirtualAccountMandiri.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountVirtualAccountMandiri.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountVirtualAccountMandiri.subtract(sumFeeAmountVirtualAccountMandiri).toString()));
	
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_CIMBClicks));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountCIMBClicks.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountCIMBClicks.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountCIMBClicks.subtract(sumFeeAmountCIMBClicks).toString()));
			
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_XLTunai));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountXLTunai.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountXLTunai.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountXLTunai.subtract(sumFeeAmountXLTunai).toString()));
			
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_MandiriInstallment));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountMandiriInstPayment.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountMandiriInstPayment.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountMandiriInstPayment.subtract(sumFeeAmountMandiriInstPayment).toString()));
			
			detailRow = sheet.createRow((short) ++startRow);
			detailRow.createCell(startCol+4).setCellValue(new HSSFRichTextString(VeniceConstants.VEN_WCS_PAYMENT_TYPE_BRI));
			detailRow.createCell(startCol+5).setCellValue(new Double(sumPaidAmountBRI.toString()));
			detailRow.createCell(startCol+6).setCellValue(new Double(sumFeeAmountBRI.toString()));
			detailRow.createCell(startCol+7).setCellValue(new Double(sumPaidAmountBRI.subtract(sumFeeAmountBRI).toString()));
			
			
				for(int j=startRow-14;j<=startRow;j++){	
					HSSFRow detailRows = sheet.getRow(j);	
						for(int i=startCol+4; i<=startCol+7; i++){
							HSSFCell cell = detailRows.getCell(i);
							if (startRow-12== j || startRow-8== j ){
								cell.setCellStyle(style);
								sheet.autoSizeColumn(i);
							}else {
								cell.setCellStyle(style2);								
							}								
						}    
				}
		}else
			_log.debug("Record is null ");	
		_log.debug("End WriteExcel ");
		
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

}
