package com.gdn.venice.logistics.dataexport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.djarum.raf.utilities.Log4jLoggerFactory;

/**
 * class untuk export hasil activity atau invoice report yang gagal diupload.
 * 
 * @author Roland
 * 
 */
public class ActivityInvoiceFailedToUploadExport {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = null;
	HSSFWorkbook wb=null;

	public ActivityInvoiceFailedToUploadExport(HSSFWorkbook wb) {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataexport.ActivityInvoiceFailedToUploadExport");
		this.wb=wb;
	}
	
	public HSSFWorkbook ExportExcel(ArrayList<String> failedMessage,HSSFSheet sheet) throws ServletException {	
		_log.info("start ActivityInvoiceFailedToUploadExport");				
		try{					 			
			int startRow = 0;
			int startCol=0;
			
			_log.debug("create heading");
			// Create the column headings
			HSSFRow headerRow = sheet.createRow((short) startRow);
			headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("GDN Reference Not Found in Venice"));
			
			CellStyle headerCellstyle = wb.createCellStyle();
			headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
			headerCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
			headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
			headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
			headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	    			       	
			
			_log.debug("set header style");
			for(int i=startCol; i<=startCol; i++){
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				sheet.autoSizeColumn(i);
			}				   
			
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
					
			_log.debug("Start WriteExcel");		
			for (int i = 0; i < failedMessage.size(); i++) {
				_log.debug("gdn ref: "+failedMessage.get(i));
				startRow=startRow+1;
				HSSFRow row = sheet.createRow(startRow);
				HSSFCell nameCell = row.createCell(startCol);nameCell.setCellValue(new HSSFRichTextString(failedMessage.get(i)));		
				
				//set style for airway bill list
				for(int l=startCol; l<=startCol; l++){
					HSSFCell cell2 = row.getCell(l);
					cell2.setCellStyle(detailCellstyle);
					sheet.autoSizeColumn(l);
				} 
			}
			_log.debug("End WriteExcel ");
		} catch (Exception e)   {
			throw new ServletException("Exception in Excel Sample Servlet", e);
	    }   
		return wb;
	}
	
	public HSSFWorkbook ExportExcel(HashMap<String, String> gdnRefNotFoundList, HashMap<String, String> failedRecord,List<FailedStatusUpdate> failedStatusUpdateList, HSSFSheet sheet, String source) throws ServletException {	
		_log.info("start ActivityInvoiceFailedToUploadExport");				
		try{					 			
			int startRow = 0;
			int startCol=0;
			
			_log.debug("Start WriteExcel");	
			// header style
			CellStyle headerCellstyle = wb.createCellStyle();
			headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
			headerCellstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
			headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
			headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
			headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			headerCellstyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);	    			    
			// detail style
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
			 
			 if(!source.equals("invoice")){
				// Create the column headings
				HSSFRow headerGDNRefNotFound = sheet.createRow((short) startRow);
				headerGDNRefNotFound.createCell(startCol).setCellValue(new HSSFRichTextString("GDN Reference Not Found in Venice"));
				headerGDNRefNotFound.createCell(startCol + 1).setCellValue(new HSSFRichTextString("Problem"));
			
				for(int i=startCol; i<=startCol + 1; i++){
					HSSFCell cell = headerGDNRefNotFound.getCell(i);
					cell.setCellStyle(headerCellstyle);
				}

				 Iterator gdnRefIterator = gdnRefNotFoundList.entrySet().iterator();
				
				 while (gdnRefIterator.hasNext()) {
					startRow++;
					
					Map.Entry gdnRefMap = (Map.Entry) gdnRefIterator.next();
					
					_log.debug("gdn ref: "+gdnRefMap.getKey());
					
					HSSFRow row = sheet.createRow(startRow);
					HSSFCell gdnReffCell = row.createCell(startCol);gdnReffCell.setCellValue(new HSSFRichTextString(gdnRefMap.getKey().toString()));		
					HSSFCell problemCell = row.createCell(startCol + 1);problemCell.setCellValue(new HSSFRichTextString(gdnRefMap.getValue().toString()));		
					
					//set style for airway bill list
					for(int l=startCol; l<=startCol + 1; l++){
						HSSFCell cell2 = row.getCell(l);
						cell2.setCellStyle(detailCellstyle);
					} 
				}
			}
			 
			HSSFRow headerRecordProblem;
			if(source.equals("invoice")) {
				startRow = 0;
				headerRecordProblem = sheet.createRow((short) startRow);
				headerRecordProblem.createCell(startCol).setCellValue(new HSSFRichTextString("Airwaybill Number"));
			} else {
				startRow = startRow + 2;
				headerRecordProblem = sheet.createRow((short) startRow);
				headerRecordProblem.createCell(startCol).setCellValue(new HSSFRichTextString("Rec No"));
			}
			
			headerRecordProblem.createCell(startCol + 1).setCellValue(new HSSFRichTextString("Problem"));
			
			for(int i=startCol; i<=startCol + 1; i++){
				HSSFCell cell = headerRecordProblem.getCell(i);
				cell.setCellStyle(headerCellstyle);
			}	
			
			Iterator it = failedRecord.entrySet().iterator();
			
			while (it.hasNext()) {
				startRow++;
				
				Map.Entry pair = (Map.Entry) it.next();
				HSSFRow row = sheet.createRow(startRow);
				HSSFCell nameCell = row.createCell(startCol);nameCell.setCellValue(new HSSFRichTextString(pair.getKey().toString().replace(".0", "")));
				HSSFCell problemDescCell = row.createCell(startCol + 1);problemDescCell.setCellValue(new HSSFRichTextString(pair.getValue().toString()));
				
				//set style
				for(int l=startCol; l<=startCol + 1; l++){
					HSSFCell cell2 = row.getCell(l);
					cell2.setCellStyle(detailCellstyle);
				} 
			}
			
			startRow = startRow + 2;
			if(!source.equals("invoice")) {
				HSSFRow headerStatusUpdateProblem = sheet.createRow((short) startRow);
				headerStatusUpdateProblem.createCell(startCol).setCellValue(new HSSFRichTextString("GDN Ref"));
				headerStatusUpdateProblem.createCell(startCol + 1).setCellValue(new HSSFRichTextString("AWB Logistic"));
				headerStatusUpdateProblem.createCell(startCol + 2).setCellValue(new HSSFRichTextString("AWB MTA"));
				headerStatusUpdateProblem.createCell(startCol + 3).setCellValue(new HSSFRichTextString("Order Item Status"));
				headerStatusUpdateProblem.createCell(startCol + 4).setCellValue(new HSSFRichTextString("AWB Status"));
				
				for(int i=startCol; i<=startCol + 4; i++){
					HSSFCell cell = headerStatusUpdateProblem.getCell(i);
					cell.setCellStyle(headerCellstyle);
				}	
				
				for(FailedStatusUpdate failedStatusUpdate : failedStatusUpdateList){
					startRow++;
					
					HSSFRow row = sheet.createRow(startRow);
					HSSFCell gdnRefCell = row.createCell(startCol); gdnRefCell.setCellValue(new HSSFRichTextString(failedStatusUpdate.getGdnRef()));
					HSSFCell awbLogisticCell = row.createCell(startCol + 1); awbLogisticCell.setCellValue(new HSSFRichTextString(failedStatusUpdate.getAirwayBillNoLogistic()));
					HSSFCell awbMTACell = row.createCell(startCol + 2); awbMTACell.setCellValue(new HSSFRichTextString(failedStatusUpdate.getAirwayBillNoMTA()));
					HSSFCell orderItemStatusCell = row.createCell(startCol + 3); orderItemStatusCell.setCellValue(new HSSFRichTextString(failedStatusUpdate.getOrderItemStatus()));
					HSSFCell awbStatusCell = row.createCell(startCol + 4); awbStatusCell.setCellValue(new HSSFRichTextString(failedStatusUpdate.getAirwayBillStatus()));
					
					//set style
					for(int l=startCol; l<=startCol + 1; l++){
						HSSFCell cell2 = row.getCell(l);
						cell2.setCellStyle(detailCellstyle);
						sheet.autoSizeColumn(l);
					} 
				}
			}
			
			_log.debug("End WriteExcel ");
		} catch (Exception e)   {
			throw new ServletException("Exception in Excel Sample Servlet", e);
	    }   
		return wb;
	}
}
