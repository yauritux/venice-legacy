package com.gdn.venice.exportimport.finance.dataexport;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang.ArrayUtils;
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
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

public class SalesRecordExport {
	protected static Logger _log = null;
	HSSFWorkbook wb = null;
	
	Locator<Object> locator;

	/**
	 * Basic constructor
	 * @throws Exception 
	 */
	public SalesRecordExport(HSSFWorkbook wb) throws Exception {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataexport.SalesRecordExport");
		locator = new Locator<Object>();
		this.wb = wb;
	}
	
	private BigDecimal getOrderItemAdjustment(Long orderItemId) throws Exception{
		VenOrderItemAdjustmentSessionEJBRemote orderItemAdjustmentHome = (VenOrderItemAdjustmentSessionEJBRemote) locator
			.lookup(VenOrderItemAdjustmentSessionEJBRemote.class, "VenOrderItemAdjustmentSessionEJBBean");
		
		String query = "select o from VenOrderItemAdjustment o left join fetch o.venOrderItem where o.venOrderItem.orderItemId = " + orderItemId;
		
		List<VenOrderItemAdjustment> orderItemAdjustmentList = orderItemAdjustmentHome.queryByRange(query, 0, 0);
		BigDecimal orderItemAdjustment = new BigDecimal(0);		
		if (orderItemAdjustmentList.size() > 0) {
			for (int j=0;j<orderItemAdjustmentList.size();j++) {
				orderItemAdjustment = orderItemAdjustment.add(orderItemAdjustmentList.get(j).getAmount());
			}
		}
		
		return orderItemAdjustment;
	}

	private String getOrderItemAdjustmentList(Long orderItemId) throws Exception{
		VenOrderItemAdjustmentSessionEJBRemote orderItemAdjustmentHome = (VenOrderItemAdjustmentSessionEJBRemote) locator
			.lookup(VenOrderItemAdjustmentSessionEJBRemote.class, "VenOrderItemAdjustmentSessionEJBBean");
		
		String query = "select o from VenOrderItemAdjustment o left join fetch o.venOrderItem where o.venOrderItem.orderItemId = " + orderItemId;
		
		List<VenOrderItemAdjustment> orderItemAdjustmentList = orderItemAdjustmentHome.queryByRange(query, 0, 0);
		StringBuilder orderItemAdjustment = new StringBuilder();
		if (orderItemAdjustmentList.size() > 0) {
			for (int j=0;j<orderItemAdjustmentList.size();j++) {
				orderItemAdjustment = orderItemAdjustment.append(orderItemAdjustmentList.get(j).getVenPromotion().getPromotionCode());
				orderItemAdjustment = orderItemAdjustment.append(", Rp ");
				orderItemAdjustment = orderItemAdjustment.append(orderItemAdjustmentList.get(j).getAmount());
				if (j < orderItemAdjustmentList.size()-1){
					orderItemAdjustment = orderItemAdjustment.append(", ");
				}
			}
		}
		
		return orderItemAdjustment.toString();
	}
	
	private List<FinSalesRecord> getSalesRecordList(List<String> salesRecordIdList) throws Exception {
		
		FinSalesRecordSessionEJBRemote salesRecordHome = (FinSalesRecordSessionEJBRemote) locator
				.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");

		StringBuilder commaDelimitedId = new StringBuilder();
		
		for(int i = 0;i < salesRecordIdList.size(); i++){
			commaDelimitedId.append(salesRecordIdList.get(i));
			
			if(i != salesRecordIdList.size() - 1){
				commaDelimitedId.append(",");
			}
		}
		
		String query = "select o from FinSalesRecord o left join fetch o.venOrderItem inner join fetch o.venOrderItem.venSettlementRecords where o.salesRecordId in ( " + commaDelimitedId.toString() + " )";
		List<FinSalesRecord> finSalesRecordList = salesRecordHome.queryByRange(query, 0, 0);

		return finSalesRecordList;
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

	public HSSFWorkbook exportExcel(List<String> params, HSSFSheet sheet)
			throws ServletException {
		
		int startRow = 0;
		int startCol = 0;

		CellStyle headerCellstyle = getHeaderStyle();
		CellStyle style = getStyle();
		CellStyle styleAlighRight = getStyleAlighRight();
		CellStyle style2 = getStyle2();
		CellStyle style2AlighRight = getStyle2AlighRight();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String[] headerTitles = {
				"ID",
				"Reconcile Date",
				"Order Date",
				"Order ID",
				"Merchant ID",
				"Merchant Name",
				"Merchant Status",
				"Payment Type",
				"Order Item ID",
				"SKU",
				"Item Desc.",
				"Quantity",
				"Price Per Unit",
				"Order Item Amount",
				"Commission",
				"Transaction Fee",
				"Logistic Vendor",
				"Shipping Cost",
				"Insurance Cost",
				"Total Shipping & Insurance Cost",
			    "Handling Fee",
				"Adjustment",
				"Gift Wrap",
				"Customer Down Payment",		
				"Status",
				"Payment Status",
				"Promo"
		};
		
		String[] headersWithNumberFormat = {
				"Quantity",
				"Price Per Unit",
				"Order Item Amount",
				"Commission",
				"Transaction Fee",
				"Shipping Cost",
				"Insurance Cost",
				"Total Shipping & Insurance Cost",
			    "Handling Fee",
				"Adjustment",
				"Customer Down Payment"
		};
		
		int[] fieldWithNumberFormat = getFieldsWithNumberFormatIndex(headerTitles, headersWithNumberFormat);
		
		
		try {

			List<FinSalesRecord> finSalesRecordList = getSalesRecordList(params);
			
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

			if (!finSalesRecordList.isEmpty()) {

				_log.debug("Start WriteExcel ");

				for (int i = 0; i < finSalesRecordList.size(); i++) {
					
					HSSFRow row = sheet.createRow(++startRow);

					FinSalesRecord salesRecord = finSalesRecordList.get(i);
					//ID
					HSSFCell nameCell = row.createCell(startCol);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getSalesRecordId()!=null)? salesRecord.getSalesRecordId().toString():""));
					//Reconcile Date
					nameCell = row.createCell(startCol + 1);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getReconcileDate() != null)?simpleDateFormat.format(salesRecord.getReconcileDate()):""));
					//Order Date
					nameCell = row.createCell(startCol + 2);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenOrder()!=null && salesRecord.getVenOrderItem().getVenOrder().getOrderDate()!=null)?simpleDateFormat.format(salesRecord.getVenOrderItem().getVenOrder().getOrderDate()):""));
					//Order ID
					nameCell = row.createCell(startCol + 3);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenOrder()!=null)? salesRecord.getVenOrderItem().getVenOrder().getWcsOrderId():""));
					//Merchant ID
					nameCell = row.createCell(startCol + 4);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null)? salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getWcsMerchantId():""));
					//Merchant Name
					nameCell = row.createCell(startCol + 5);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!=null)? salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():""));
					//Merchant Status
					nameCell = row.createCell(startCol + 6);
					String commissionType = (salesRecord.getVenOrderItem().getVenSettlementRecords()!=null && salesRecord.getVenOrderItem().getVenSettlementRecords().size() > 0)?salesRecord.getVenOrderItem().getVenSettlementRecords().get(0).getCommissionType():"";
					if(commissionType.equals("CM")){
						commissionType="Commission";
					}else if(commissionType.equals("RB")){
						commissionType="Rebate";
					}else if(commissionType.equals("TD")){
						commissionType="Trading";
					}else if(commissionType.equals("MP")){
						commissionType="Trading MP";
					}else if(commissionType.equals("KS")){
						commissionType="Consignment";
					}else{
						commissionType="";
					}
					
					nameCell.setCellValue(new HSSFRichTextString(commissionType));
					
					//Payment Type
					nameCell = row.createCell(startCol + 7);
					VenOrderPaymentAllocationSessionEJBRemote paymentSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
					String paymentQuery = "select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + salesRecord.getVenOrderItem().getVenOrder().getOrderId();
					
					List<VenOrderPaymentAllocation> paymentList = paymentSessionHome.queryByRange(paymentQuery, 0, 1);
					String paymentType="";
					if(paymentList.size()>0){
						paymentType = paymentList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode();
					}
					
					nameCell.setCellValue(new HSSFRichTextString(paymentType));
					
					//Order Item ID
					nameCell = row.createCell(startCol + 8);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null)? salesRecord.getVenOrderItem().getWcsOrderItemId():""));
					//SKU
					nameCell = row.createCell(startCol + 9);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem().getVenMerchantProduct()!=null)? salesRecord.getVenOrderItem().getVenMerchantProduct().getWcsProductSku():""));
					//Item Desc
					nameCell = row.createCell(startCol + 10);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getVenMerchantProduct()!=null)? salesRecord.getVenOrderItem().getVenMerchantProduct().getWcsProductName():""));
					//Quantity
					nameCell = row.createCell(startCol + 11);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getQuantity()!=null)? salesRecord.getVenOrderItem().getQuantity().toString():""));
					//Price Per Unit
					nameCell = row.createCell(startCol + 12);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getPrice()!=null)? formatDouble(salesRecord.getVenOrderItem().getPrice().doubleValue()):""));
					//Order Item Amount
					nameCell = row.createCell(startCol + 13);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getTotal()!=null)? formatDouble(salesRecord.getVenOrderItem().getTotal().doubleValue()):""));
					//Commission
					nameCell = row.createCell(startCol + 14);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getGdnCommissionAmount()!=null)? formatDouble(salesRecord.getGdnCommissionAmount().doubleValue()):""));
					//Transaction Fee
					nameCell = row.createCell(startCol + 15);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getGdnTransactionFeeAmount()!=null)? formatDouble(salesRecord.getGdnTransactionFeeAmount().doubleValue()):""));
					//Logistic Vendor
					nameCell = row.createCell(startCol + 16);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getLogLogisticService() != null && salesRecord.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider() != null)? salesRecord.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderCode():""));
					
					//Shipping Cost
					BigDecimal shippingCost = new BigDecimal(0);
					BigDecimal insuranceCost = new BigDecimal(0);
											
					if(salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getShippingCost()!=null){
						shippingCost =  salesRecord.getVenOrderItem().getShippingCost();
					}
					
					if(salesRecord.getVenOrderItem()!=null && salesRecord.getVenOrderItem().getInsuranceCost()!=null){
						insuranceCost = salesRecord.getVenOrderItem().getInsuranceCost();
					}
					
					nameCell = row.createCell(startCol + 17);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(shippingCost.doubleValue())));
					//Insurance Cost
					nameCell = row.createCell(startCol + 18);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(insuranceCost.doubleValue())));
					//Total Shipping & Insurance Cost
					nameCell = row.createCell(startCol + 19);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(shippingCost.add(insuranceCost).doubleValue())));
					//Handling Fee
					nameCell = row.createCell(startCol + 20);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getGdnHandlingFeeAmount()!=null)? formatDouble(salesRecord.getGdnHandlingFeeAmount().doubleValue()):""));
					//Adjustment
					nameCell = row.createCell(startCol + 21);
					nameCell.setCellValue(new HSSFRichTextString(formatDouble(getOrderItemAdjustment(salesRecord.getVenOrderItem().getOrderItemId()).doubleValue())));
					//Gift Wrap
					nameCell = row.createCell(startCol + 22);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getGdnGiftWrapChargeAmount()!=null)? salesRecord.getGdnGiftWrapChargeAmount().toString():""));
					//Customer Down Payment
					nameCell = row.createCell(startCol + 23);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getCustomerDownpayment()!=null)? formatDouble(salesRecord.getCustomerDownpayment().doubleValue()):""));
					//Status
					nameCell = row.createCell(startCol + 24);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getFinApprovalStatus()!=null)? salesRecord.getFinApprovalStatus().getApprovalStatusDesc():""));
					//Payment Status
					nameCell = row.createCell(startCol + 25);
					nameCell.setCellValue(new HSSFRichTextString((salesRecord.getFinApPayment() != null)?"Paid":"Unpaid"));
					//Promo
					nameCell = row.createCell(startCol + 26);
					nameCell.setCellValue(new HSSFRichTextString(getOrderItemAdjustmentList(salesRecord.getVenOrderItem().getOrderItemId())));	

					for (int j = startCol; j < 27 + startCol; j++) {
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
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
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

