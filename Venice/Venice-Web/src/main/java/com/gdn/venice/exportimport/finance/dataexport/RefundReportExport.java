package com.gdn.venice.exportimport.finance.dataexport;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInRefund;
import com.gdn.venice.util.VeniceConstants;

public class RefundReportExport {
	protected static Logger _log = null;
	HSSFWorkbook wb=null;


	/**
	 * Basic constructor
	 */
	public RefundReportExport(HSSFWorkbook wb) {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataexport.Report.RefundReportExport");
		this.wb=wb;
	}
	
	public HSSFWorkbook ExportExcel(Map<String, Object> params,HSSFSheet sheet) throws ServletException {		
			
		Locator<Object> locator = null;
		List<FinArFundsInRefund> finArFundsInRefundList = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = (Date) params.get("fromDate");
		Date endDate = (Date) params.get("toDate");
		
		_log.debug("fromDate "+startDate);
		_log.debug("toDate "+endDate);
		
		try{				
			
		locator = new Locator<Object>();
		
		FinArFundsInRefundSessionEJBRemote refundRecordHome = (FinArFundsInRefundSessionEJBRemote) locator
		.lookup(FinArFundsInRefundSessionEJBRemote.class, "FinArFundsInRefundSessionEJBBean");		
			
		String query = "select o from FinArFundsInRefund o where o.refundTimestamp" +
				" between '" + new Timestamp(startDate.getTime()) + "' and '" + new Timestamp(endDate.getTime()) + "' and" +
				" o.finArFundsInReconRecord.finArFundsInActionApplied.actionAppliedId <> "+ VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED +" and" +
				" o.finArFundsInReconRecord.finApprovalStatus.approvalStatusId = "+ VeniceConstants.FIN_APPROVAL_STATUS_APPROVED ;
		
		_log.debug("query = "+query);	
		_log.debug("query = "+query);
		
		finArFundsInRefundList = refundRecordHome.queryByRange(query, 0, 0);
			
		int startRow = 4;
		int startCol=0;

		// Create the column headings
		 HSSFRow header = sheet.createRow((short) 1);
		 header.createCell(5).setCellValue(new HSSFRichTextString("REFUND"));
		
		 HSSFRow headertgl = sheet.createRow((short) 2);
		 headertgl.createCell(2).setCellValue(new HSSFRichTextString("TANGGAL : "+dateFormat.format(startDate) +" s/d "+dateFormat.format(endDate)));
		 // Create the column headings
		 HSSFRow headerRow = sheet.createRow((short) startRow);
		 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("Refund Date"));
		 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order Date"));
		 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("OrderID"));
		 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("Payment Type")); 
		 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Action Taken"));
		 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Reconcilement Status"));
		 headerRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("VA/CC Number"));
		 headerRow.createCell(startCol+7).setCellValue(new HSSFRichTextString("Payment Amount")); 
		 headerRow.createCell(startCol+8).setCellValue(new HSSFRichTextString("Paid Amount"));
		 headerRow.createCell(startCol+9).setCellValue(new HSSFRichTextString("Refund Value"));
		 headerRow.createCell(startCol+10).setCellValue(new HSSFRichTextString("Payment Processing Date"));
		 headerRow.createCell(startCol+11).setCellValue(new HSSFRichTextString("Note")); 
		 
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
		   
			for(int i=startCol; i<12+startCol; i++){
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				//Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}    
				
		if (!finArFundsInRefundList.isEmpty()){					
			  	BigDecimal sumPaymentAmount = new BigDecimal(0);BigDecimal sumPaidAmount = new BigDecimal(0);
			  	BigDecimal sumRefundValue = new BigDecimal(0);			  
		
				  
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
			for (FinArFundsInRefund item : finArFundsInRefundList){
					HSSFRow row = sheet.createRow(++startRow);		
					
			    	 HSSFCell nameCell = row.createCell(startCol);nameCell.setCellValue(new HSSFRichTextString(item.getRefundTimestamp()!=null?dateFormat.format(item.getRefundTimestamp()):null));
			    	 nameCell = row.createCell(startCol+1);nameCell.setCellValue(new HSSFRichTextString(item.getFinArFundsInReconRecord().getOrderDate()!=null?dateFormat.format(item.getFinArFundsInReconRecord().getOrderDate()):null));
			    	 nameCell = row.createCell(startCol+2);nameCell.setCellValue(new HSSFRichTextString(item.getFinArFundsInReconRecord().getWcsOrderId()!=null?item.getFinArFundsInReconRecord().getWcsOrderId():null));	 	 
			    	 nameCell = row.createCell(startCol+3);nameCell.setCellValue(new HSSFRichTextString(item.getFinArFundsInReconRecord().getVenOrderPayment()!=null?item.getFinArFundsInReconRecord().getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc():item.getFinArFundsInReconRecord().getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeDesc()));	 	 
			    	 nameCell = row.createCell(startCol+4);nameCell.setCellValue(new HSSFRichTextString(item.getFinArFundsInReconRecord().getFinArFundsInActionApplied().getActionAppliedDesc()));	 	 
			    	 nameCell = row.createCell(startCol+5);nameCell.setCellValue(new HSSFRichTextString(item.getFinArFundsInReconRecord().getFinArReconResult().getReconResultDesc()));
			    	 nameCell = row.createCell(startCol+6);nameCell.setCellValue(new HSSFRichTextString(item.getFinArFundsInReconRecord().getNomorReff()));	 	 
			    	 nameCell = row.createCell(startCol+7);nameCell.setCellValue(new HSSFRichTextString(formatDouble(new Double(item.getFinArFundsInReconRecord().getPaymentAmount()!=null?item.getFinArFundsInReconRecord().getPaymentAmount().toString():"0"))));	 	 
				     nameCell = row.createCell(startCol+8);nameCell.setCellValue(new HSSFRichTextString(formatDouble(new Double(item.getFinArFundsInReconRecord().getProviderReportPaidAmount()!=null?item.getFinArFundsInReconRecord().getProviderReportPaidAmount().toString():"0"))));
		    	  	 nameCell = row.createCell(startCol+9);nameCell.setCellValue(new HSSFRichTextString(formatDouble(new Double(item.getApAmount().toString()))));
			    	 nameCell = row.createCell(startCol+10);nameCell.setCellValue(new HSSFRichTextString(item.getApPaymentTimestamp()!=null?dateFormat.format(item.getApPaymentTimestamp()):null));	 	 
			    	 nameCell = row.createCell(startCol+11);nameCell.setCellValue(new HSSFRichTextString(item.getFinArFundsInReconRecord().getComment()));	 	 
			    	
			    	 sumPaymentAmount=sumPaymentAmount.add(item.getFinArFundsInReconRecord().getPaymentAmount()!=null?item.getFinArFundsInReconRecord().getPaymentAmount():new BigDecimal(0));
			    	 sumPaidAmount=sumPaidAmount.add(item.getFinArFundsInReconRecord().getProviderReportPaidAmount()!=null?item.getFinArFundsInReconRecord().getProviderReportPaidAmount():new BigDecimal(0));
			    	 sumRefundValue=sumRefundValue.add(item.getApAmount()!=null?item.getApAmount():new BigDecimal(0));
						
			    	 
			    	 for(int i=startCol; i<12+startCol; i++){
							HSSFCell cells = row.getCell(i);
							if (startRow%2==0)
								cells.setCellStyle(style);
							else
								cells.setCellStyle(style2);														
							sheet.autoSizeColumn(i);
						}	
			    	 
			}
			startRow=startRow+1;
			
			HSSFRow headerSumRow = sheet.createRow((short) startRow);
			headerSumRow.createCell(startCol+7).setCellValue(new HSSFRichTextString(formatDouble(new Double(""+sumPaymentAmount))));
			headerSumRow.createCell(startCol+8).setCellValue(new HSSFRichTextString(formatDouble(new Double(""+sumPaidAmount))));
			headerSumRow.createCell(startCol+9).setCellValue(new HSSFRichTextString(formatDouble(new Double(""+sumRefundValue))));
			
			for(int i=startCol+7; i<=startCol+9; i++){
				HSSFCell cell = headerSumRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				//Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}    
		}else
			_log.debug("Record Refund is null ");	
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
	private String formatDouble(Double value){
		NumberFormat nf = new DecimalFormat("#,###,###,###,###");
		return "Rp " + nf.format(value.doubleValue()).replace(',', '.');
	}
}
