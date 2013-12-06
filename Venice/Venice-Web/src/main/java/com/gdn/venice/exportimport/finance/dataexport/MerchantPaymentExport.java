package com.gdn.venice.exportimport.finance.dataexport;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinApPaymentSessionEJBRemote;
import com.gdn.venice.facade.VenMerchantSessionEJBRemote;
import com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote;
import com.gdn.venice.finance.integration.bean.Merchant;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenMerchant;
import com.gdn.venice.persistence.VenSettlementRecord;

public class MerchantPaymentExport {
	protected static Logger _log = null;
	protected static final String AIRWAYBILL_ENGINE_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/conf/airwaybill-engine.properties";
	
	HSSFWorkbook wb = null;
	HttpClient client = null;
	GetMethod method = null;
	int statusCode;
	
	Locator<Object> locator;

	/**
	 * Basic constructor
	 * @throws Exception 
	 */
	public MerchantPaymentExport(HSSFWorkbook wb) throws Exception {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.finance.dataexport.SalesRecordExport");
		locator = new Locator<Object>();
		this.wb = wb;
	}
	
	private String formatDouble(Double value) {
		NumberFormat nf = new DecimalFormat("#,###,###,###,###");
		return "Rp " + nf.format(value.doubleValue()).replace(',', '.');
	}
	
	private int[] getFieldsWithNumberFormatIndex(String[] headerTitles, String[] fieldsWithNumberFormat){
		int[] fieldsWithNumberFormatIndex = new int[fieldsWithNumberFormat.length];
		
		for (int i = 0; i < fieldsWithNumberFormat.length; i++) {
			
			for (int j = 0; j < headerTitles.length; j++) {
				if(headerTitles[j].equals(fieldsWithNumberFormat[i])){
					fieldsWithNumberFormatIndex[i] = j;
					break;
				}
			}
		}
		
		return fieldsWithNumberFormatIndex;
	}

    private  Properties getAirwayBillEngineProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(AIRWAYBILL_ENGINE_PROPERTIES_FILE));
		} catch (Exception e) {
			_log.error("Error getting airwaybill-engine.properties", e);
			e.printStackTrace();
			return null;
		}
		return properties;
	}
    
	public HSSFWorkbook exportExcel(String params, HSSFSheet sheet)
			throws ServletException {
		
		int startRow = 0;
		int startCol = 0;

		CellStyle headerCellstyle = getHeaderStyle();
		CellStyle style = getStyle();
		CellStyle styleAlighRight = getStyleAlighRight();
		CellStyle style2 = getStyle2();
		CellStyle style2AlighRight = getStyle2AlighRight();
		
		String[] headerTitles = {
				"Merchant ID",
				"Merchant Name",
				"PPh23",
				"No Rekening",
				"Cabang",
				"A/N",
				"Status Merchant",
				"Order Item Amount",
				"Commission",
				"Transaction Fee",
				"PPH 23",
				"A/R Amount",
				"Final Payment"
		};
		
		String[] headersWithNumberFormat = {
				"Order Item Amount",
				"Commission",
				"Transaction Fee",
				"PPH 23",
				"Final Payment"
		};
		
		int[] fieldWithNumberFormat = getFieldsWithNumberFormatIndex(headerTitles, headersWithNumberFormat);		
		
		try {
			client = new HttpClient();
			method = new GetMethod();
			ObjectMapper mapper = new ObjectMapper();
			
			FinApPaymentSessionEJBRemote paymentHome = (FinApPaymentSessionEJBRemote) locator
				.lookup(FinApPaymentSessionEJBRemote.class, "FinApPaymentSessionEJBBean");
			
			VenMerchantSessionEJBRemote merchantHome = (VenMerchantSessionEJBRemote) locator
			.lookup(VenMerchantSessionEJBRemote.class, "VenMerchantSessionEJBBean");
			
			VenSettlementRecordSessionEJBRemote settlementHome = (VenSettlementRecordSessionEJBRemote) locator
			.lookup(VenSettlementRecordSessionEJBRemote.class, "VenSettlementRecordSessionEJBBean");
			
			List<FinApPayment> finApPaymentList = paymentHome.queryByRange("select distinct o from FinApPayment o join fetch o.finSalesRecords where o.apPaymentId in ("+params+")", 0, 0);
			
			// Create the column headings
			HSSFRow headerRow = sheet.createRow((short) startRow);
			for (int i = 0; i < headerTitles.length; i++) {
				headerRow.createCell(startCol + i).setCellValue(new HSSFRichTextString(headerTitles[i]));
			}
			
			for (int i = startCol; i < headerTitles.length + startCol; i++) {
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				// Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}

			if (!finApPaymentList.isEmpty()) {

				_log.debug("Start WriteExcel ");

				for(int i = 0; i<finApPaymentList.size(); i++){
					FinApPayment apPayment = finApPaymentList.get(i);
					
					VenMerchant merchant = merchantHome.queryByRange("select o from VenMerchant o where o.venParty.partyId="+apPayment.getVenParty().getPartyId(), 0, 1).get(0);

					VenSettlementRecord settlement = settlementHome.queryByRange("select o from VenSettlementRecord o where o.venOrderItem.orderItemId="+
							apPayment.getFinSalesRecords().get(0).getVenOrderItem().getOrderItemId(), 0, 1).get(0);
					
					HSSFRow row = sheet.createRow(++startRow);
					
					//Merchant ID
					HSSFCell nameCell = row.createCell(startCol);
					nameCell.setCellValue(new HSSFRichTextString(merchant.getWcsMerchantId()));
					//Merchant Name
					nameCell = row.createCell(startCol + 1);
					nameCell.setCellValue(new HSSFRichTextString(apPayment.getVenParty().getFullOrLegalName()));
					//PPh23
					nameCell = row.createCell(startCol + 2);
					nameCell.setCellValue(new HSSFRichTextString(settlement.getPph23()?"Ya":"Tidak"));
					
					Merchant merchantBank = null;
					method.setPath(getAirwayBillEngineProperties().getProperty("mtaAddress")+"merchantWS?merchantCode="+merchant.getWcsMerchantId());
					statusCode = client.executeMethod(method);
					try
					{
						if (statusCode != HttpStatus.SC_OK) {
			                System.out.println("Get bank data for merchant: " + merchant.getWcsMerchantId() + " failed: " + method.getStatusLine());
			            } else {
			                InputStream response = method.getResponseBodyAsStream();
			                merchantBank = mapper.readValue(response, Merchant.class);
			            }
			        } catch(UnrecognizedPropertyException uex){
			            System.out.println("Get bank data for merchant: " + merchant.getWcsMerchantId() + " failed: merchant code is not exist");
			        } finally {
			            method.releaseConnection();
			          }

					//No Rekening
					nameCell = row.createCell(startCol + 3);
					nameCell.setCellValue(new HSSFRichTextString(merchantBank==null?"":merchantBank.getAcctNo()));
					//Cabang bank
					nameCell = row.createCell(startCol + 4);
					nameCell.setCellValue(new HSSFRichTextString(merchantBank==null?"":merchantBank.getBankBranch()));
					//Atas nama
					nameCell = row.createCell(startCol + 5);
					nameCell.setCellValue(new HSSFRichTextString(merchantBank==null?"":merchantBank.getAcctName()));
					//Status Merchant
					nameCell = row.createCell(startCol + 6);
					nameCell.setCellValue(new HSSFRichTextString(settlement.getCommissionType().equals("CM")?"Commision":"Rebate"));
					
					BigDecimal total = new BigDecimal(0),
						commission = new BigDecimal(0),
						transactionFee = new BigDecimal(0);
					for (FinSalesRecord salesRecord : apPayment.getFinSalesRecords()) {
						total = total.add(new BigDecimal(salesRecord.getVenOrderItem().getQuantity()).multiply(salesRecord.getVenOrderItem().getPrice()));
						commission = commission.add(salesRecord.getGdnCommissionAmount());
						transactionFee = transactionFee.add(salesRecord.getGdnTransactionFeeAmount());
					}
					total = total.setScale(2, RoundingMode.HALF_UP);
					commission = commission.setScale(2, RoundingMode.HALF_UP);
					transactionFee = transactionFee.setScale(2, RoundingMode.HALF_UP);
					
					//Order Item Amount
					nameCell = row.createCell(startCol + 7);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(total.doubleValue())));
					//Commission
					nameCell = row.createCell(startCol + 8);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(commission.doubleValue())));
					//Transaction Fee
					nameCell = row.createCell(startCol + 9);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(transactionFee.doubleValue())));
					//PPH 23
					nameCell = row.createCell(startCol + 10);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(apPayment.getPph23Amount()==null?0.0:apPayment.getPph23Amount().doubleValue())));
					//A/R Amount
					nameCell = row.createCell(startCol + 11);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(apPayment.getPenaltyAmount().doubleValue())));
					//Final Payment
					BigDecimal finalPayment = settlement.getCommissionType().equals("CM")?total.subtract(commission).subtract(transactionFee).add(apPayment.getPph23Amount()==null?new BigDecimal(0):apPayment.getPph23Amount()).subtract(apPayment.getPenaltyAmount()):total.subtract(apPayment.getPenaltyAmount());
					finalPayment = finalPayment.setScale(2, RoundingMode.HALF_UP);
					nameCell = row.createCell(startCol + 12);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(finalPayment.doubleValue())));
					
					for (int j = startCol; j < 13 + startCol; j++) {
						HSSFCell cells = row.getCell(j);
						if (startRow % 2 == 0){
							cells.setCellStyle(style);
							
							if(ArrayUtils.contains(fieldWithNumberFormat, j )){
								cells.setCellStyle(styleAlighRight);
							}
							
						}else{
							cells.setCellStyle(style2);
							
							if(ArrayUtils.contains(fieldWithNumberFormat, j )){
								cells.setCellStyle(style2AlighRight);
							}
						}
						sheet.autoSizeColumn(i);
					}					
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
	
}

