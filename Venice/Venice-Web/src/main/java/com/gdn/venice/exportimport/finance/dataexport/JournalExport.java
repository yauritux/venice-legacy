package com.gdn.venice.exportimport.finance.dataexport;

import java.math.BigDecimal;
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
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.util.VeniceConstants;

public class JournalExport {
	protected static Logger _log = null;
	HSSFWorkbook wb = null;

	/**
	 * Basic constructor
	 */
	public JournalExport(HSSFWorkbook wb) {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.finance.dataexport.JournalExport");
		this.wb = wb;
	}

	private List<FinJournalTransaction> getFinJournalTransactionList(
			long journalGroupId) throws Exception {
		Locator<Object> locator = new Locator<Object>();
		FinJournalTransactionSessionEJBRemote journalHome = (FinJournalTransactionSessionEJBRemote) locator
				.lookup(FinJournalTransactionSessionEJBRemote.class,
						"FinJournalTransactionSessionEJBBean");

		String query = "select o from FinJournalTransaction o join fetch o.finJournalApprovalGroup where o.finJournalApprovalGroup.journalGroupId = "
				+ journalGroupId+" order by o.wcsOrderID desc, o.finAccount.accountId asc";
		List<FinJournalTransaction> finJournalTransactionList = journalHome
				.queryByRange(query, 0, 0);

		return finJournalTransactionList;
	}
	
	private List<FinArFundsInReconRecord> getReconRecordList(long journalGroupId) throws Exception {
		Locator<Object> locator = new Locator<Object>();
		
		FinArFundsInReconRecordSessionEJBRemote recordSessionHome = (FinArFundsInReconRecordSessionEJBRemote) locator
		.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");

		String selectMethod = "select o from FinArFundsInReconRecord o where o.reconciliationRecordId in "+
		"(select a.finArFundsInReconRecords.reconciliationRecordId from FinArFundsInJournalTransaction a "+
		"where a.finJournalTransactions.finJournalApprovalGroup.journalGroupId = " + journalGroupId+")";
	
		List<FinArFundsInReconRecord> finArFundsInReconRecordList = recordSessionHome.queryByRange(selectMethod, 0, 0);
		
		return finArFundsInReconRecordList;
	}

	public HSSFWorkbook exportExcel(Map<String, Object> params, HSSFSheet sheet)
			throws ServletException {
		
		int startRow = 4;
		int startCol = 0;

		CellStyle headerCellstyle = getHeaderStyle();
		CellStyle style = getStyle();
		CellStyle styleAlighRight = getStyleAlighRight();
		CellStyle style2 = getStyle2();
		CellStyle style2AlighRight = getStyle2AlighRight();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");

		long journalGroupId = Long.parseLong(params.get("journalGroupId").toString());

		try {

			List<FinJournalTransaction> finJournalTransactionList = getFinJournalTransactionList(journalGroupId);
			List<FinArFundsInReconRecord> finArFundsInReconRecordList = getReconRecordList(journalGroupId);
			
			BigDecimal debit = new BigDecimal(0);
			BigDecimal credit = new BigDecimal(0);

			// Create the column headings
			HSSFRow header = sheet.createRow((short) 1);
			header.createCell(5).setCellValue(
					new HSSFRichTextString(finJournalTransactionList.get(0)
							.getFinJournal().getJournalDesc().toUpperCase()));

			HSSFRow headertgl = sheet.createRow((short) 2);
			headertgl.createCell(2).setCellValue(
					new HSSFRichTextString(finJournalTransactionList.get(0)
							.getFinJournalApprovalGroup().getJournalGroupDesc()
							.toUpperCase()));
			// Create the column headings
			HSSFRow headerRow = sheet.createRow((short) startRow);
			headerRow.createCell(startCol).setCellValue(
					new HSSFRichTextString("Date"));
			headerRow.createCell(startCol + 1).setCellValue(
					new HSSFRichTextString("Reff"));
			headerRow.createCell(startCol + 2).setCellValue(
					new HSSFRichTextString("Payment Type"));
			headerRow.createCell(startCol + 3).setCellValue(
					new HSSFRichTextString("Account"));
			headerRow.createCell(startCol + 4).setCellValue(
					new HSSFRichTextString("Debit"));
			headerRow.createCell(startCol + 5).setCellValue(
					new HSSFRichTextString("Credit"));
			headerRow.createCell(startCol + 6).setCellValue(
					new HSSFRichTextString("Status"));
			headerRow.createCell(startCol + 7).setCellValue(
					new HSSFRichTextString("Comments"));
			

			for (int i = startCol; i < 8 + startCol; i++) {
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				// Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}

			if (!finJournalTransactionList.isEmpty()) {

				_log.debug("Start WriteExcel ");
				String selectMethod=!finArFundsInReconRecordList.isEmpty()?(
						finArFundsInReconRecordList.get(0).getVenOrderPayment()!=null?(finArFundsInReconRecordList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount)?
		     			finArFundsInReconRecordList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc()+" "+finJournalTransactionList.get(0).getVenBank().getBankShortName():
			     			finArFundsInReconRecordList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc()):
			     				finArFundsInReconRecordList.get(0).getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeDesc()
			     			):"";
				
				for (int i = 0; i < finJournalTransactionList.size(); i++) {
					
					HSSFRow row = sheet.createRow(++startRow);

					FinJournalTransaction finJournalTransaction = finJournalTransactionList.get(i);
					
					HSSFCell nameCell = row.createCell(startCol);
					nameCell.setCellValue(new HSSFRichTextString(
							simpleDateFormat.format(finJournalTransaction
									.getTransactionTimestamp())));
					nameCell = row.createCell(startCol + 1);
					nameCell.setCellValue(new HSSFRichTextString(
							(finJournalTransaction.getWcsOrderID() != null) ? finJournalTransaction
									.getWcsOrderID() : ""));
					
					nameCell = row.createCell(startCol + 2);
					nameCell.setCellValue(new HSSFRichTextString(selectMethod));
					
					nameCell = row.createCell(startCol +3);
					nameCell.setCellValue(new HSSFRichTextString(
							(finJournalTransaction.getFinAccount() != null) ? finJournalTransaction
									.getFinAccount().getAccountDesc()
									.toString()
									: ""));
					if (!finJournalTransaction.getCreditDebitFlag()) {
						// DEBIT
						nameCell = row.createCell(startCol +4);
						nameCell.setCellValue(new Double(finJournalTransaction
										.getTransactionAmount()+""));
						nameCell = row.createCell(startCol +5);
						nameCell.setCellValue(new HSSFRichTextString(""));
						
						debit = debit.add(finJournalTransaction.getTransactionAmount());
						
					} else {
						// CREDIT
						nameCell = row.createCell(startCol + 4);
						nameCell.setCellValue(new HSSFRichTextString(""));
						nameCell = row.createCell(startCol + 5);
						nameCell.setCellValue(new Double(finJournalTransaction
										.getTransactionAmount().toString()));
						
						credit = credit.add(finJournalTransaction.getTransactionAmount());
					}

					nameCell = row.createCell(startCol + 6);
					nameCell.setCellValue(new HSSFRichTextString(
							(finJournalTransaction.getFinTransactionStatus() != null) ? finJournalTransaction
									.getFinTransactionStatus()
									.getTransactionStatusDesc() : ""));
					nameCell = row.createCell(startCol + 7);
					nameCell.setCellValue(new HSSFRichTextString(
							finJournalTransaction.getComments()));

					for (int j = startCol; j < 8 + startCol; j++) {
						HSSFCell cells = row.getCell(j);
						if (startRow % 2 == 0){
							cells.setCellStyle(style);
							
							if(j == startCol + 4 || j == startCol + 5){
								cells.setCellStyle(styleAlighRight);
							}
							
						}else{
							cells.setCellStyle(style2);
							
							if(j == startCol + 4 || j == startCol + 5){
								cells.setCellStyle(style2AlighRight);
							}
						}
						sheet.autoSizeColumn(i);
					}
					
				}
				
				HSSFRow summaryRow = sheet.createRow((short) ++startRow);
				summaryRow.createCell(startCol + 4).setCellValue(new Double(debit.toString()));
				summaryRow.createCell(startCol + 5).setCellValue(new Double(credit.toString()));
				
				CellStyle summaryStyle = getSummaryCellStyle();
				
				for(int i=startCol+4; i<=startCol+5; i++){
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
	
	private CellStyle getStyleAlighRight(){
		CellStyle styleAlighRight = wb.createCellStyle();
		styleAlighRight.setBorderBottom(CellStyle.BORDER_THIN);
		styleAlighRight.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleAlighRight.setBorderLeft(CellStyle.BORDER_THIN);
		styleAlighRight.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleAlighRight.setBorderRight(CellStyle.BORDER_THIN);
		styleAlighRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleAlighRight.setBorderTop(CellStyle.BORDER_THIN);
		styleAlighRight.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleAlighRight.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleAlighRight.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleAlighRight.setAlignment(CellStyle.ALIGN_RIGHT);
		
		return styleAlighRight;
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
	
	private CellStyle getStyle2AlighRight(){
		CellStyle style2AlighRight = wb.createCellStyle();
		style2AlighRight.setBorderBottom(CellStyle.BORDER_THIN);
		style2AlighRight.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2AlighRight.setBorderLeft(CellStyle.BORDER_THIN);
		style2AlighRight.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style2AlighRight.setBorderRight(CellStyle.BORDER_THIN);
		style2AlighRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2AlighRight.setBorderTop(CellStyle.BORDER_THIN);
		style2AlighRight.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style2AlighRight.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		style2AlighRight.setAlignment(CellStyle.ALIGN_RIGHT);
		
		return style2AlighRight;
	}
	
	private CellStyle getSummaryCellStyle(){
		CellStyle summaryStyle = wb.createCellStyle();
		
		summaryStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		summaryStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		summaryStyle.setBorderBottom(CellStyle.BORDER_DOUBLE);
		
		return summaryStyle;
	}

}
