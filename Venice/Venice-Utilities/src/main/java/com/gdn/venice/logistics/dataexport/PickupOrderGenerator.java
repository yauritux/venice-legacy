package com.gdn.venice.logistics.dataexport;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.gdn.venice.hssf.DataArgs;
import com.gdn.venice.hssf.DataColumn;
import com.gdn.venice.hssf.DataRow;
import com.gdn.venice.hssf.Export;
import com.gdn.venice.hssf.GCell;
import com.gdn.venice.hssf.GCellStyle;
import com.gdn.venice.hssf.RGBColor;

@SuppressWarnings("deprecation")
/**
 * A class to generate the pickup order report... significantly modified by DF and HC.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PickupOrderGenerator {
	
	/**
	 * Exports the logistics data to Excel
	 * @param fileName
	 * @param pickupOrders
	 */
	public void exportData(String fileName, List<PickupOrder> pickupOrders) {
		
		//Define required variable
		Export e = new Export();
		DataArgs data = new DataArgs();
		data.setFileName(fileName);
			
		//Define columns
		data.addColumn(new DataColumn("Nama Logistik"));
		data.addColumn(new DataColumn("Tanggal Pengambilan"));
		data.addColumn(new DataColumn("Waktu Pengambilan"));
		data.addColumn(new DataColumn("Merchant PIC"));
		data.addColumn(new DataColumn("Nomor Telepon merchant PIC"));
		data.addColumn(new DataColumn("Alamat Pickup Point (line 1 & 2)"));
		data.addColumn(new DataColumn("Kecamatan - Kelurahan"));
		data.addColumn(new DataColumn("Kota Pickup Point"));
		data.addColumn(new DataColumn("Propinsi Pick up Point"));
		data.addColumn(new DataColumn("Kode pos Pick up Point"));
		data.addColumn(new DataColumn("Nama Merchant"));
		data.addColumn(new DataColumn("Alamat Origin (line 1 & 2)"));
		data.addColumn(new DataColumn("Kota Origin"));
		data.addColumn(new DataColumn("Propinsi Origin"));
		data.addColumn(new DataColumn("Nomor Telepon Merchant"));
		data.addColumn(new DataColumn("GDN Ref. Number"));
		data.addColumn(new DataColumn("Airway Bill No"));
		data.addColumn(new DataColumn("SKU ID"));
		data.addColumn(new DataColumn("Deskripsi Barang"));
		data.addColumn(new DataColumn("Quantity"));
		data.addColumn(new DataColumn("Insurance ( Y / T )"));
		data.addColumn(new DataColumn("Insured Amount"));
		data.addColumn(new DataColumn("Jenis Layanan Pengiriman"));
		data.addColumn(new DataColumn("a. Gift Wrap"));
		data.addColumn(new DataColumn("b. Gift Card"));
		data.addColumn(new DataColumn("Instruction buat Special Handling"));
		data.addColumn(new DataColumn("Nama Pemesan"));
		data.addColumn(new DataColumn("Nama Penerima"));
		data.addColumn(new DataColumn("Alamat Penerima"));
		data.addColumn(new DataColumn("Kecamatan - Keluarahan Penerima"));
		data.addColumn(new DataColumn("Kota Penerima"));
		data.addColumn(new DataColumn("Propinsi Penerima"));
		data.addColumn(new DataColumn("Kode Pos"));
		data.addColumn(new DataColumn("Nomor Telepon Penerima"));
		data.addColumn(new DataColumn("Nomor Handphone Penerima"));
		data.addColumn(new DataColumn("Email Pengirim"));
		data.addColumn(new DataColumn("Nomor Telepon Pengirim"));
		data.addColumn(new DataColumn("Nomor Handphone Pengirim"));
		data.addColumn(new DataColumn("Metode Pickup"));
				
		/*
		 * Moved this outside the loop as it was exhausting the number of styles
		 * also changed the coloring to white inline with new requirements -DF
		 */
		//Create cell style
		GCellStyle style = new GCellStyle();		
		style.setBackgroundColor(new RGBColor((byte)255, (byte)255, (byte)255));
				
		for(int i = 0; i <= 38; i++){
			if(i==0){
				data.addSuperColumn(new DataColumn("Pickup Receipt"));
			} else if (i==10) {
				data.addSuperColumn(new DataColumn("Identitas Merchant * untuk INFO yang diterakan pada Airwaybill"));
			} else	if (i==15) {
				data.addSuperColumn(new DataColumn("Informasi Barang"));
			} else if (i==22) {
				data.addSuperColumn(new DataColumn("Logistics Services"));
			} else if (i==26) {
				data.addSuperColumn(new DataColumn("Identitas Penerima"));
			} else	if (i==35) {
				data.addSuperColumn(new DataColumn("Identitas Pengirim"));
			} else	if (i==38) {
				data.addSuperColumn(new DataColumn("Metode Pengiriman"));
			} else {
				data.addSuperColumn(new DataColumn());
			}
		}
		
		//Looping for generating excel rows
		for (int i = 0; i < pickupOrders.size(); i++) {
			//Get row
			PickupOrder p = (PickupOrder) pickupOrders.get(i);
								
			//Create row
			DataRow dr = new DataRow();
			
			//Need to format the dates and times correctly from java.util.Date - DF
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Date format
			SimpleDateFormat stf = new SimpleDateFormat("HH:mm"); //Time format
			
			dr.addCells(new GCell[] {
					new GCell (p.getLogisticProviderName()),
					//modified the following lines of code to format dates and times correctly
					new GCell (sdf.format(p.getPickupDateTime()), style),
					new GCell (stf.format(p.getPickupDateTime()), style),
					new GCell (p.getPicName(), style),
					new GCell (p.getPicPhone(), style),
					new GCell (p.getPickupPointAddressLine1() + " " + (p.getPickupPointAddressLine2()==null ?"":p.getPickupPointAddressLine2()), style),
					new GCell (p.getPickupPointKecamatan() + " - " + p.getPickupPointKelurahan(), style),
					new GCell (p.getPickupPointCity(), style),
					new GCell (p.getPickupPointProvince(), style),
					new GCell (p.getPickupPointZipCode(), style),
					new GCell (p.getMerchantName(), style),
					new GCell (p.getMerchantAddressLine1() + " " + (p.getMerchantAddressLine2()== null ?"":p.getMerchantAddressLine2()), style),
					new GCell (p.getMerchantCity(), style),
					new GCell (p.getMerchantProvince(), style),
					new GCell (p.getMerchantPhone(), style),
					new GCell (p.getGdnRefNo(), style),
					new GCell (p.getAirwayBillNo(), style),
					new GCell (p.getSkuId(), style),
					new GCell (p.getDescription(), style),
					new GCell (p.getQuantity().toString(), style),
					new GCell (p.getIsInsured() ? "Y" : "T", style),
					new GCell (formatDouble(p.getInsuredAmount()), style),
					new GCell (p.getServiceType(), style),
					new GCell (p.getGiftWrap(), style),
					new GCell (p.getGiftCard(), style),
					new GCell (p.getSpecialHandlingInstruction(), style),
					new GCell (p.getCustomerName(), style),
					new GCell (p.getRecipientName(), style),
					new GCell (p.getRecipientAddress(), style),
					new GCell (p.getRecipientKecamatan() + " - " + p.getRecipientKelurahan(), style),
					new GCell (p.getRecipientCity(), style),
					new GCell (p.getRecipientProvince(), style),
					new GCell (p.getRecipientZipCode(), style),
					new GCell (p.getRecipientPhone(), style),
					new GCell (p.getRecipientMobile(), style),
					new GCell (p.getSenderEmail(), style),
					new GCell (p.getSenderPhone(), style),
					new GCell (p.getSenderMobile(), style),					
					new GCell (p.getPickupMethod(), style)});
			data.addRow(dr);			
		}
		
		//Write the cells to the sheet first so we can manipulate the rows
		e.writeCellsToSheet(data);

		/*
		 * Create a style that is suitable for the output of the report - DF
		 */
	    CellStyle headerCellstyle = e.getWb().createCellStyle();
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

		//Create the merged regions - DF
		e.getSheet().addMergedRegion(new CellRangeAddress(0,0,0,9));
		e.getSheet().addMergedRegion(new CellRangeAddress(0,0,10,14));
		e.getSheet().addMergedRegion(new CellRangeAddress(0,0,15,20));
		e.getSheet().addMergedRegion(new CellRangeAddress(0,0,22,25));
		e.getSheet().addMergedRegion(new CellRangeAddress(0,0,26,34));
		e.getSheet().addMergedRegion(new CellRangeAddress(0,0,35,38));
		
	    //Create the cells in the first row
		for(int i = 0; i <= 38; i++){
			HSSFCell cell = e.getSheet().getRow(0).getCell(i);
			cell.setCellStyle(headerCellstyle);
			HSSFCell headercell = e.getSheet().getRow(1).getCell(i);
			headercell.setCellStyle(headerCellstyle);
			//Autosize the columns while we are there
			e.getSheet().autoSizeColumn(i);
		}
		
		e.writeWorkBookToFile(data);
	}
	
	/**
	 * Formats a Double to the string format required for the report 
	 * @param value
	 * @return
	 */
	private String formatDouble(Double value){
		NumberFormat nf = new DecimalFormat("#,###,###,###,###");
		return "Rp " + nf.format(value.doubleValue()).replace(',', '.');
	}
}
