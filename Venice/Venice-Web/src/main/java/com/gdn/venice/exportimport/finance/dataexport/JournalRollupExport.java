package com.gdn.venice.exportimport.finance.dataexport;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote;
import com.gdn.venice.persistence.FinRolledUpJournalEntry;


public class JournalRollupExport {
	protected static Logger _log = null;
	HSSFWorkbook wb = null;

	/**
	 * Basic constructor
	 */
	public JournalRollupExport(HSSFWorkbook wb) {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.finance.dataexport.JournalRollupExport");
		this.wb = wb;
	}

	private List<FinRolledUpJournalEntry> getFinRolledUpJournalEntryList(
			long journalGroupId) throws Exception {
		Locator<Object> locator = new Locator<Object>();
		FinRolledUpJournalEntrySessionEJBRemote journalHome = (FinRolledUpJournalEntrySessionEJBRemote) locator
				.lookup(FinRolledUpJournalEntrySessionEJBRemote.class,
						"FinRolledUpJournalEntrySessionEJBBean");

		String query = "select o from FinRolledUpJournalEntry o where o.finRolledUpJournalHeader.ruJournalHeaderId = "+ journalGroupId;
		List<FinRolledUpJournalEntry> finRolledUpJournalEntryList = journalHome
				.queryByRange(query, 0, 0);

		return finRolledUpJournalEntryList;
	}

	private String formatDouble(Double value) {
		NumberFormat nf = new DecimalFormat("#,###,###,###,###.##");
		String number = nf.format(value.doubleValue()),
		numbers[] = number.split("\\.");
		String valueString;
		if(numbers.length <= 1){
			valueString = numbers[0].replace(',', '.') + ",00";			
			if(numbers.length == 0)
				valueString = "0,00";			
		}
		else valueString = numbers[0].replace(',', '.') + "," + numbers[1];
		return "Rp " + valueString;
	}

	public HSSFWorkbook exportExcel(Map<String, Object> params, HSSFSheet sheet)
			throws ServletException {
		
		int startRow = 4;
		int startCol = 0;

		CellStyle headerCellstyle = getHeaderStyle();
		CellStyle style = getStyle();
		CellStyle style2 = getStyle2();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");

		long journalGroupId = Long.parseLong(params.get("journalGroupId").toString());

		try {

			List<FinRolledUpJournalEntry> finRolledUpJournalEntryList = getFinRolledUpJournalEntryList(journalGroupId);
			
			BigDecimal debit = new BigDecimal(0);
			BigDecimal credit = new BigDecimal(0);

			// Create the column headings
			HSSFRow headertgl = sheet.createRow((short) 2);
			headertgl.createCell(1).setCellValue(
					new HSSFRichTextString(finRolledUpJournalEntryList.get(0).getFinRolledUpJournalHeader().getRuJournalHeaderDesc()));
			// Create the column headings
			HSSFRow headerRow = sheet.createRow((short) startRow);
			headerRow.createCell(startCol).setCellValue(
					new HSSFRichTextString("Date"));
			headerRow.createCell(startCol + 1).setCellValue(
					new HSSFRichTextString("Account"));
			headerRow.createCell(startCol + 2).setCellValue(
					new HSSFRichTextString("Debit"));
			headerRow.createCell(startCol + 3).setCellValue(
					new HSSFRichTextString("Credit"));
			headerRow.createCell(startCol + 4).setCellValue(
					new HSSFRichTextString("Status"));
			

			for (int i = startCol; i < 5 + startCol; i++) {
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				// Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}

			if (!finRolledUpJournalEntryList.isEmpty()) {
				_log.debug("Start WriteExcel ");
				String idGroup = "";
				CellStyle styleNew=style;
				for (int i = 0; i < finRolledUpJournalEntryList.size(); i++) {
					
					HSSFRow row = sheet.createRow(++startRow);

					FinRolledUpJournalEntry finRolledUpJournalEntry = finRolledUpJournalEntryList.get(i);

					HSSFCell nameCell = row.createCell(startCol);
					nameCell.setCellValue(new HSSFRichTextString(
							simpleDateFormat.format(finRolledUpJournalEntry
									.getRuJournalEntryTimestamp())));
					nameCell = row.createCell(startCol + 1);					
					nameCell.setCellValue(new HSSFRichTextString(
							(finRolledUpJournalEntry.getFinAccount() != null) ? finRolledUpJournalEntry
									.getFinAccount().getAccountDesc()
									.toString()
									: ""));
					if (!finRolledUpJournalEntry.getCreditDebitFlag()) {
						// DEBIT
						nameCell = row.createCell(startCol + 2);
						nameCell.setCellValue(new HSSFRichTextString(
								formatDouble(new Double(finRolledUpJournalEntry
										.getRuValue().toString()))));
						nameCell = row.createCell(startCol + 3);
						nameCell.setCellValue(new HSSFRichTextString(""));
						
						debit = debit.add(finRolledUpJournalEntry.getRuValue());
						
					} else {
						// CREDIT
						nameCell = row.createCell(startCol + 2);
						nameCell.setCellValue(new HSSFRichTextString(""));
						nameCell = row.createCell(startCol + 3);
						nameCell.setCellValue(new HSSFRichTextString(
								formatDouble(new Double(finRolledUpJournalEntry
										.getRuValue().toString()))));						
						credit = credit.add(finRolledUpJournalEntry.getRuValue());
					}

					nameCell = row.createCell(startCol + 4);
					nameCell.setCellValue(new HSSFRichTextString(
							(finRolledUpJournalEntry.getFinRolledUpJournalStatus()!= null) ? finRolledUpJournalEntry.getFinRolledUpJournalStatus().getJournalEntryStatusDesc() : ""));
					
					if(idGroup.equals("") ){			
						idGroup=""+finRolledUpJournalEntry.getGroupId();
					}else if(!idGroup.equals(""+finRolledUpJournalEntry.getGroupId())){
						idGroup=""+finRolledUpJournalEntry.getGroupId();
						if(styleNew==style2)styleNew=style;
						else styleNew = style2;
					}
					for (int j = startCol; j < 5 + startCol; j++) {
						HSSFCell cells = row.getCell(j);		
						styleNew.setAlignment(CellStyle.ALIGN_LEFT);
							cells.setCellStyle(styleNew);							
							if(j == startCol + 2 || j == startCol + 3){
								styleNew.setAlignment(CellStyle.ALIGN_RIGHT);
								cells.setCellStyle(styleNew);
							}					
						sheet.autoSizeColumn(i);
					}
				}
				
				HSSFRow summaryRow = sheet.createRow((short) ++startRow);
				summaryRow.createCell(startCol + 2).setCellValue(new HSSFRichTextString(
						formatDouble(new Double(debit.toString()))));
				summaryRow.createCell(startCol + 3).setCellValue(new HSSFRichTextString(
						formatDouble(new Double(credit.toString()))));
				
				CellStyle summaryStyle = getSummaryCellStyle();
				
				for(int i=startCol+2; i<=startCol+3; i++){
					HSSFCell cell = summaryRow.getCell(i);
					cell.setCellStyle(summaryStyle);
					sheet.autoSizeColumn(i);
				}   				
			}
		} catch (Exception e) {
			throw new ServletException("Exception in Excel Sample Servlet", e);
		}

		return wb;
	}
	
	private CellStyle getHeaderStyle(){
		CellStyle headerCellstyle = wb.createCellStyle();
		headerCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerCellstyle
				.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerCellstyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerCellstyle.setBorderRight(CellStyle.BORDER_THIN);
		headerCellstyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerCellstyle.setBorderTop(CellStyle.BORDER_THIN);
		headerCellstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerCellstyle.setFillForegroundColor(IndexedColors.SKY_BLUE
				.getIndex());
		headerCellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerCellstyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		return headerCellstyle;
	}
	
	private CellStyle getStyle(){
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		return style;
	}
	
	private CellStyle getStyle2(){
		CellStyle style2 = wb.createCellStyle();
		style2.setBorderBottom(CellStyle.BORDER_THIN);
		style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderLeft(CellStyle.BORDER_THIN);
		style2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderRight(CellStyle.BORDER_THIN);
		style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderTop(CellStyle.BORDER_THIN);
		style2.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT
				.getIndex());
		
		return style2;
	}
	
	private CellStyle getSummaryCellStyle(){
		CellStyle summaryStyle = wb.createCellStyle();
		
		summaryStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		summaryStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		summaryStyle.setBorderBottom(CellStyle.BORDER_DOUBLE);
		
		return summaryStyle;
	}

}