package com.gdn.venice.logistics.batch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.djarum.raf.utilities.Log4jLoggerFactory;

public class OrderTrackingReportBatchJob {

	protected static Logger _log;
	
	private static String CONFIG_FILE = "config.properties";
	private static long LOGRECONACTIVITYRECORDRESULT_PU_DATE_MISMATCH = 1;
	private static long VEN_ORDER_STATUS_CX = 16;
	
	private static String ARG_DAILY = "daily";
	private static String ARG_WEEKLY = "weekly";
	
	private static String type;
	private Calendar startDate = Calendar.getInstance();
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private SimpleDateFormat fileDateTimeFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private String reportLocation = "";
	private String dbHost = "";
	private String dbPort = "";
	private String dbUsername = "";
	private String dbPassword = "";
	private String environment = "";
	
	private Connection conn;
	private Statement stOrder;
	private Statement stOrderHistory;
	
	Properties prop = new Properties();
	
	public OrderTrackingReportBatchJob() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger(this.getClass().getName());
		
		prop.load(new FileInputStream(CONFIG_FILE));
		
		environment = prop.getProperty("environment");
		reportLocation = prop.getProperty(environment + ".reportLocation");
		dbHost = prop.getProperty(environment + ".dbHost");
		dbPort = prop.getProperty(environment + ".dbPort");
		dbUsername = prop.getProperty(environment + ".dbUsername");
		dbPassword = prop.getProperty(environment + ".dbPassword");
		
		setupDBConnection();
	}
	
	private void setupDBConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");
		
		conn = DriverManager.getConnection("jdbc:postgresql://" + dbHost +":" + dbPort + "/venice", dbUsername, dbPassword);
	}
	
	private void start(){
		ResultSet rsOrder = fetchAirwayBillData();
		ResultSet rsOrderHistory = fetchOrderItemHistoryData();
		
		createReport(rsOrder, rsOrderHistory);
	}
	
	private ResultSet fetchAirwayBillData(){
		/**
		 * daily report contains data from 3 month back
		 * weekly contain data from May 2012
		 */
		if(type.equalsIgnoreCase(ARG_DAILY)){
			startDate.add(Calendar.MONTH, -3);
		}else if(type.equalsIgnoreCase(ARG_WEEKLY)){
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			startDate.set(Calendar.MONTH, 5);
			startDate.set(Calendar.YEAR, 2012);
		}
		
		_log.info("Report Type => " + type);
		_log.info("Start Date => " + dateFormat.format(startDate.getTime()));
		
		StringBuilder qb = new StringBuilder();
		
		qb.append("select ");
		qb.append("    vo.wcs_order_id, ");
		qb.append("    voi.wcs_order_item_id, ");
		qb.append("    lab.gdn_reference, ");
		qb.append("    vmp.wcs_product_sku, ");
		qb.append("    vmp.wcs_product_name, ");
		qb.append("    vdc.package_weight, ");
		qb.append("    vdc.quantity, ");
		qb.append("    vo.order_date, ");
		qb.append("    vos.order_status_code, ");
		qb.append("    vpm.full_or_legal_name, ");
		
		qb.append("    lab.origin, ");
		qb.append("    lab.mta_data, ");
		qb.append("    lab.destination, ");
		qb.append("    lab.zip, ");
		qb.append("    llp.logistics_provider_code, ");
		qb.append("    lab.airway_bill_pickup_date_time, ");
		qb.append("    larr.venice_data, ");
		qb.append("    larr.provider_data, ");
		qb.append("    lab.actual_pickup_date, ");
		qb.append("    lab.airway_bill_timestamp, ");
		
		qb.append("    lab.airway_bill_number, ");
		qb.append("    voish.history_timestamp, ");
		qb.append("    lab.status, ");
		qb.append("    lab.received, ");
		qb.append("    lab.recipient, ");
		qb.append("    lab.relation, ");
		qb.append("    voi.etd, ");
		qb.append("    voi.min_est_date, ");
		qb.append("    voi.max_est_date, ");
		qb.append("    lab.price_per_kg, ");
		
		qb.append("    vwpt.wcs_payment_type_desc ");
		
		qb.append("from log_airway_bill lab"); 
		qb.append("	inner join ven_order_item voi"); 
		qb.append("		on voi.order_item_id = lab.order_item_id"); 
		qb.append("	inner join ven_order vo"); 
		qb.append("		on vo.order_id = voi.order_id"); 
		qb.append("     left join log_merchant_pickup_instructions lmpi"); 
		qb.append("	    on lmpi.order_item_id = voi.order_item_id"); 
		qb.append("     left join ven_merchant_product vmp"); 
		qb.append("	    on vmp.product_id = voi.product_id"); 
		qb.append("     left join ven_merchant vm"); 
		qb.append("	    on vm.merchant_id = vmp.merchant_id"); 
		qb.append("     left join ven_party vpm"); 
		qb.append("	    on vpm.party_id = vm.party_id"); 
		qb.append("     left join log_logistics_provider llp"); 
		qb.append("	    on llp.logistics_provider_id = lab.logistics_provider_id"); 
		qb.append("     left join log_activity_recon_record larr"); 
		qb.append("	    on larr.airway_bill_id = lab.airway_bill_id and larr.recon_record_result_id = "); 
		qb.append(LOGRECONACTIVITYRECORDRESULT_PU_DATE_MISMATCH);
		qb.append("	 left join ven_order_payment_allocation vopa"); 
		qb.append("	    on vopa.order_id = vo.order_id"); 
		qb.append("	 left join ven_order_payment vop"); 
		qb.append("	    on vop.order_payment_id = vopa.order_payment_id"); 
		qb.append("	 left join ven_wcs_payment_type vwpt"); 
		qb.append("	    on vwpt.wcs_payment_type_id = vop.wcs_payment_type_id"); 
		qb.append("     left join ven_order_item_status_history voish"); 
		qb.append("	    on voish.order_item_id = voi.order_item_id and voish.order_status_id = "); 
		qb.append(VEN_ORDER_STATUS_CX);
		qb.append("	 left join ven_distribution_cart vdc"); 
		qb.append("	    on vdc.distribution_cart_id = lab.distribution_cart_id"); 
		qb.append("	 left join ven_order_status vos"); 
		qb.append("	    on vos.order_status_id = voi.order_status_id"); 
		qb.append(" where "); 
		qb.append("	vo.order_date >=  '"); 
		qb.append(new SimpleDateFormat("yyyy-MM-dd").format(startDate.getTime()));
		qb.append("' ");
		qb.append("order by vo.order_date"); 

		_log.info(qb.toString());
		
		try {
			stOrder = conn.createStatement();
			ResultSet rs = stOrder.executeQuery(qb.toString());
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private ResultSet fetchOrderItemHistoryData(){
		/**
		 * daily report contains data from 3 month back
		 * weekly contain data from May 2012
		 */
		if(type.equalsIgnoreCase(ARG_DAILY)){
			startDate.add(Calendar.MONTH, -3);
		}else if(type.equalsIgnoreCase(ARG_WEEKLY)){
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			startDate.set(Calendar.MONTH, 5);
			startDate.set(Calendar.YEAR, 2012);
		}
		
		_log.info("Report Type => " + type);
		_log.info("Start Date => " + dateFormat.format(startDate.getTime()));
		
		StringBuilder qb = new StringBuilder();
		
		qb.append("select ");
		qb.append("    o.wcs_order_id, ");
		qb.append("    oi.wcs_order_item_id, ");
		qb.append("    ab.gdn_reference, ");
		qb.append("    os.order_status_code, ");
		qb.append("    oish.history_timestamp, ");
		qb.append("    oish.status_change_reason ");
		qb.append("from ven_order_item_status_history oish"); 
		qb.append("	inner join ven_order_item oi"); 
		qb.append("		on oish.order_item_id = oi.order_item_id"); 
		qb.append("	inner join ven_order o"); 
		qb.append("		on o.order_id = oi.order_id"); 
		qb.append("     inner join ven_order_status os"); 
		qb.append("	    on os.order_status_id = oish.order_status_id"); 
		qb.append("     inner join log_airway_bill ab"); 
		qb.append("	    on ab.order_item_id = oi.order_item_id"); 
		qb.append(" where "); 
		qb.append("	o.order_date >=  '"); 
		qb.append(new SimpleDateFormat("yyyy-MM-dd").format(startDate.getTime()));
		qb.append("' ");
		qb.append("order by "); 
		qb.append("     o.wcs_order_id,");
		qb.append("     oi.wcs_order_item_id, ");
		qb.append("     oish.order_status_id,");
		qb.append("     oish.history_timestamp");

		_log.info(qb.toString());
		
		try {
			stOrderHistory = conn.createStatement();
			ResultSet rs = stOrderHistory.executeQuery(qb.toString());
			
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void createReport(ResultSet rsOrder, ResultSet rsOrderHistory){
		
		HSSFWorkbook wb = new HSSFWorkbook(); 
		HSSFSheet sheet = wb.createSheet("Delivery Status Tracking Report");	
		
		// Create the column headings
		 String title="Order Tracking Report";
		 HSSFRow header = sheet.createRow((short) 0);
		 header.createCell(0).setCellValue(new HSSFRichTextString(title));
		 sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
		 sheet.addMergedRegion(new CellRangeAddress(1,1,0,5));
		 HSSFRow headertgl = sheet.createRow((short) 1);
		 headertgl.createCell(0).setCellValue(new HSSFRichTextString("Start Date: "+dateFormat.format(startDate.getTime())));
		 
		 //style
		 CellStyle headerCellstyle = wb.createCellStyle();
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
		 detailCellstyle.setAlignment(CellStyle.ALIGN_LEFT);	 
		 
		 int startRow = 2;
		 int startCol=0;
		
		 HSSFRow headerRow = sheet.createRow((short) startRow);
		 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("No."));
		 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order ID"));
		 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("Order Item ID"));
		 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("GDN Reference"));
		 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("SKU ID"));
		 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Product Name"));
		 headerRow.createCell(startCol+6).setCellValue(new HSSFRichTextString("Weight"));
		 headerRow.createCell(startCol+7).setCellValue(new HSSFRichTextString("Quantity"));
		 headerRow.createCell(startCol+8).setCellValue(new HSSFRichTextString("Order Date"));
		 headerRow.createCell(startCol+9).setCellValue(new HSSFRichTextString("Order Item Status"));
		 headerRow.createCell(startCol+10).setCellValue(new HSSFRichTextString("Merchant Name"));
		 headerRow.createCell(startCol+11).setCellValue(new HSSFRichTextString("Merchant Origin"));
		 headerRow.createCell(startCol+12).setCellValue(new HSSFRichTextString("MTA Settlement"));
		 headerRow.createCell(startCol+13).setCellValue(new HSSFRichTextString("Destination"));
		 headerRow.createCell(startCol+14).setCellValue(new HSSFRichTextString("Destination ZIP Code"));
		 headerRow.createCell(startCol+15).setCellValue(new HSSFRichTextString("Service"));
		 headerRow.createCell(startCol+16).setCellValue(new HSSFRichTextString("PU Date"));
		 headerRow.createCell(startCol+17).setCellValue(new HSSFRichTextString("Actual PU Date (MTA)"));
		 headerRow.createCell(startCol+18).setCellValue(new HSSFRichTextString("Actual PU Date (3PL)"));
		 headerRow.createCell(startCol+19).setCellValue(new HSSFRichTextString("Actual PU Date (Final)"));
		 headerRow.createCell(startCol+20).setCellValue(new HSSFRichTextString("Pickup Status"));
		 headerRow.createCell(startCol+21).setCellValue(new HSSFRichTextString("AWB Timestamp")); 
		 headerRow.createCell(startCol+22).setCellValue(new HSSFRichTextString("Airway Bill Number"));
		 headerRow.createCell(startCol+23).setCellValue(new HSSFRichTextString("Approved Date (CX Finance)"));
		 headerRow.createCell(startCol+24).setCellValue(new HSSFRichTextString("Delivery Status")); 
		 headerRow.createCell(startCol+25).setCellValue(new HSSFRichTextString("Received Date")); 
		 headerRow.createCell(startCol+26).setCellValue(new HSSFRichTextString("Receiver")); 
		 headerRow.createCell(startCol+27).setCellValue(new HSSFRichTextString("Relation"));		  
		 headerRow.createCell(startCol+28).setCellValue(new HSSFRichTextString("ETD (days)"));		
		 headerRow.createCell(startCol+29).setCellValue(new HSSFRichTextString("Price/kg"));		
		 headerRow.createCell(startCol+30).setCellValue(new HSSFRichTextString("Payment Method"));		
		 headerRow.createCell(startCol+31).setCellValue(new HSSFRichTextString("SLA Logistic"));
		 headerRow.createCell(startCol+32).setCellValue(new HSSFRichTextString("Min Est Date"));  
		 headerRow.createCell(startCol+33).setCellValue(new HSSFRichTextString("Max Est Date"));
		 headerRow.createCell(startCol+34).setCellValue(new HSSFRichTextString("SLA GDN"));
		 headerRow.createCell(startCol+35).setCellValue(new HSSFRichTextString("Total Delivery Time"));
		 
		//set style for header
		for(int i=startCol; i<=startCol+35; i++){
			HSSFCell cell = headerRow.getCell(i);
			cell.setCellStyle(headerCellstyle);
			//Autosize the columns while we are there
			sheet.autoSizeColumn(i);
		}    
		
		//number for row number for the airway bill list
		Integer number=1;
		
		//number for storing the detail header, to add color later
		int rowNumber=1;
		
		_log.debug("looping for generating excel rows");
		//Looping for generating excel rows
		try {
			
			while(rsOrder.next()){
				
				startRow++;
				HSSFRow row = sheet.createRow(startRow);
				_log.info("row : " + number);
				_log.info("order item id " +rsOrder.getString("wcs_order_item_id"));
				HSSFCell cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(number.toString()));
				cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("wcs_order_id"))); // ven_order
				cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("wcs_order_item_id")));	//ven_order_item 	 
				cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("gdn_reference"))); 
				cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("wcs_product_sku"))); // ven_merchant_product
				cell = row.createCell(startCol+5);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("wcs_product_name"))); // ven_merchant_product
				cell = row.createCell(startCol+6);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("package_weight"))); // ven_distribution_cart
				cell = row.createCell(startCol+7);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("quantity"))); // ven_distribution_cart
				cell = row.createCell(startCol+8);cell.setCellValue(new HSSFRichTextString(dateTimeFormat.format(rsOrder.getTimestamp("order_date"))));//ven_order
				cell = row.createCell(startCol+9);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("order_status_code"))); // ven_order_status					
				cell = row.createCell(startCol+10);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("full_or_legal_name")));// from ven_party
				cell = row.createCell(startCol+11);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("origin")));
				cell = row.createCell(startCol+12);cell.setCellValue(new HSSFRichTextString((rsOrder.getBoolean("mta_data"))?"Yes":"No"));
				cell = row.createCell(startCol+13);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("destination")));	
				cell = row.createCell(startCol+14);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("zip")));
				cell = row.createCell(startCol+15);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("logistics_provider_code"))); // log_logistics_provider
				cell = row.createCell(startCol+16);cell.setCellValue(new HSSFRichTextString(rsOrder.getTimestamp("airway_bill_pickup_date_time")!=null?dateTimeFormat.format(rsOrder.getTimestamp("airway_bill_pickup_date_time")):""));// log_airway_bill	 
				cell = row.createCell(startCol+17);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("venice_data")!=null?rsOrder.getString("venice_data"):""));// log_activity_recon_record
				cell = row.createCell(startCol+18);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("provider_data")!=null?rsOrder.getString("provider_data"):""));// log_activity_recon_record
				cell = row.createCell(startCol+19);cell.setCellValue(new HSSFRichTextString(rsOrder.getDate("actual_pickup_date")!=null?dateTimeFormat.format(rsOrder.getDate("actual_pickup_date")):"")); // log_airway_bill
				cell = row.createCell(startCol+20);cell.setCellValue(new HSSFRichTextString(getPickupStatus(rsOrder)));
				cell = row.createCell(startCol+21);cell.setCellValue(new HSSFRichTextString(rsOrder.getTimestamp("airway_bill_timestamp")!=null?dateTimeFormat.format(rsOrder.getTimestamp("airway_bill_timestamp")):"")); // log_airway_bill
				cell = row.createCell(startCol+22);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("airway_bill_number")));// log_airway_bill
				cell = row.createCell(startCol+23);cell.setCellValue(new HSSFRichTextString(rsOrder.getDate("history_timestamp")!=null?dateTimeFormat.format(rsOrder.getDate("history_timestamp")):""));// ven_order_item_status_history
				cell = row.createCell(startCol+24);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("status")!=null?rsOrder.getString("status"):""));	
				cell = row.createCell(startCol+25);cell.setCellValue(new HSSFRichTextString(rsOrder.getDate("received")!=null?dateTimeFormat.format(rsOrder.getDate("received")):""));
				cell = row.createCell(startCol+26);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("recipient")!=null?rsOrder.getString("recipient"):""));
		    	cell = row.createCell(startCol+27);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("relation")!=null?rsOrder.getString("relation"):""));
		    	
		    	Integer etd = rsOrder.getInt("etd"); // ven_order_item
		    	Timestamp minEstDate = rsOrder.getTimestamp("min_est_date");// ven_order_item
		    	Timestamp maxEstDate = rsOrder.getTimestamp("max_est_date");// ven_order_item
		    	// ETD receive from WCS
		    	cell = row.createCell(startCol+28);cell.setCellValue(new HSSFRichTextString(etd!=null?etd.toString():""));
		    	cell = row.createCell(startCol+29);cell.setCellValue(new HSSFRichTextString(rsOrder.getBigDecimal("price_per_kg")!=null?rsOrder.getBigDecimal("price_per_kg").toString():""));// log_airway_bill
		    	cell = row.createCell(startCol+30);cell.setCellValue(new HSSFRichTextString(rsOrder.getString("wcs_payment_type_desc")!=null?rsOrder.getString("wcs_payment_type_desc"):""));// ven_wcs_payment_type
		    	// SLA Logistic compare actual PU date final, received date and ETD
		    	cell = row.createCell(startCol+31);cell.setCellValue(new HSSFRichTextString(getSLALogistic(rsOrder)));
		    	// Min Est Date receive from WCS
		    	cell = row.createCell(startCol+32);cell.setCellValue(new HSSFRichTextString(minEstDate!=null?dateFormat.format(minEstDate):""));
		    	// Max Est Date receive from WCS
		    	cell = row.createCell(startCol+33);cell.setCellValue(new HSSFRichTextString(maxEstDate!=null?dateFormat.format(maxEstDate):""));
		    	// SLA GDN compare received date, payment method and SLA Logistic
		    	cell = row.createCell(startCol+34);cell.setCellValue(new HSSFRichTextString(getSLAGDN(rsOrder)));
		    	// total delivery time 
		    	cell = row.createCell(startCol+35);cell.setCellValue(new HSSFRichTextString(getTotalDeliveryTime(rsOrder)));
		    	
				//set style for airway bill list
				for(int l=startCol; l<=startCol+35; l++){
					HSSFCell cell2 = row.getCell(l);
					cell2.setCellStyle(detailCellstyle);
				} 	
				
				number++;
				rowNumber++;	
				
			}

			for(int l=startCol; l<=startCol+35; l++){
				//Autosize the columns while we are there
				sheet.autoSizeColumn(l);
			} 	
			
			rsOrder.close();
			stOrder.close();
			
			sheet = wb.createSheet("Detail History Status");
			
			 startRow = 1;
			 startCol=0;
			
			 headerRow = sheet.createRow((short) startRow);
			 headerRow.createCell(startCol).setCellValue(new HSSFRichTextString("Order ID"));
			 headerRow.createCell(startCol+1).setCellValue(new HSSFRichTextString("Order Item ID"));
			 headerRow.createCell(startCol+2).setCellValue(new HSSFRichTextString("GDN Ref"));
			 headerRow.createCell(startCol+3).setCellValue(new HSSFRichTextString("Status"));
			 headerRow.createCell(startCol+4).setCellValue(new HSSFRichTextString("Date/Time"));
			 headerRow.createCell(startCol+5).setCellValue(new HSSFRichTextString("Notes"));
			 
			//set style for header
			for(int i=startCol; i<=startCol+5; i++){
				HSSFCell cell = headerRow.getCell(i);
				cell.setCellStyle(headerCellstyle);
				//Autosize the columns while we are there
				sheet.autoSizeColumn(i);
			}    
			
			//number for row number for the airway bill list
			number=1;
			
			//number for storing the detail header, to add color later
			rowNumber=1;
			
			_log.debug("looping for generating excel rows");
			//Looping for generating excel rows
				
			while(rsOrderHistory.next()){
				
				startRow++;
				HSSFRow row = sheet.createRow(startRow);
				_log.info("row : " + number);
				_log.info("order item id " +rsOrderHistory.getString("wcs_order_item_id"));
				
				HSSFCell cell = row.createCell(startCol);cell.setCellValue(new HSSFRichTextString(rsOrderHistory.getString("wcs_order_id")));
				cell = row.createCell(startCol+1);cell.setCellValue(new HSSFRichTextString(rsOrderHistory.getString("wcs_order_item_id")));
				cell = row.createCell(startCol+2);cell.setCellValue(new HSSFRichTextString(rsOrderHistory.getString("gdn_reference")));
				cell = row.createCell(startCol+3);cell.setCellValue(new HSSFRichTextString(rsOrderHistory.getString("order_status_code"))); 
				cell = row.createCell(startCol+4);cell.setCellValue(new HSSFRichTextString(dateTimeFormat.format(rsOrderHistory.getTimestamp("history_timestamp"))));
				cell = row.createCell(startCol+5);cell.setCellValue(new HSSFRichTextString(rsOrderHistory.getString("status_change_reason"))); 
		    	
				//set style for airway bill list
				for(int l=startCol; l<=startCol+5; l++){
					HSSFCell cell2 = row.getCell(l);
					cell2.setCellStyle(detailCellstyle);
				} 	
				
				number++;
				rowNumber++;	
				
			}
			
			for(int l=startCol; l<=startCol+5; l++){
				//Autosize the columns while we are there
				sheet.autoSizeColumn(l);
			} 	
			
			rsOrderHistory.close();
			stOrderHistory.close();
			conn.close();
			
			StringBuilder fileName = new StringBuilder();
			
			if(type.equalsIgnoreCase(ARG_DAILY)){
				fileName.append("Daily");
			}else if(type.equalsIgnoreCase(ARG_WEEKLY)){
				fileName.append("Weekly");
			}
			
			fileName.append("OrderTrackingReport"+fileDateTimeFormat.format(new Date())+ ".xls");
			
			FileOutputStream out = new FileOutputStream(reportLocation + fileName.toString());
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String getSLAGDN(ResultSet rs){
		try{
			
			if(rs.getTimestamp("min_est_date") == null || 
				rs.getTimestamp("max_est_date")== null || 
				rs.getDate("received") == null ){
				
				return "";
			}
			
			Calendar minEstDate = Calendar.getInstance();
			minEstDate.setTime(rs.getTimestamp("min_est_date"));
			
			Calendar maxEstDate = Calendar.getInstance();
			maxEstDate.setTime(rs.getTimestamp("max_est_date"));
			
			Calendar receivedDate = Calendar.getInstance();
			receivedDate.setTime(rs.getDate("received"));
			
			int estDayDiff = Days.daysBetween(new DateTime(minEstDate), new DateTime(maxEstDate)).getDays();
			
			if(minEstDate.compareTo(receivedDate) == 0){
				return "Ontime 0 day";
			}
			
			if(maxEstDate.compareTo(receivedDate) == 0){
				return "Ontime " + estDayDiff + " days Max";
			}
			
			if(receivedDate.before(minEstDate)){
				int earlyDayDiff = Days.daysBetween(new DateTime(receivedDate), new DateTime(minEstDate)).getDays();
				
				return "Early " + earlyDayDiff + " day(s)"; 
			}
			
			if(receivedDate.before(maxEstDate)){
				int ontimeDayDiff = Days.daysBetween(new DateTime(minEstDate), new DateTime(receivedDate)).getDays();
				
				return "Ontime " + ontimeDayDiff + " day(s)";
			}
			
			if(receivedDate.after(maxEstDate)){
				int lateDayDiff = Days.daysBetween(new DateTime(maxEstDate), new DateTime(receivedDate)).getDays();
				
				return "Late " + lateDayDiff + " day(s)";
			}
		
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private String getPickupStatus(ResultSet rs){
		try {
			Date puDateSystem = dateFormat.parse(dateFormat.format(rs.getTimestamp("airway_bill_pickup_date_time")));
			Date puDateFinal      = dateFormat.parse(dateFormat.format(rs.getTimestamp("actual_pickup_date")));
			
			if(puDateFinal.compareTo(puDateSystem) == 0){
				return "Pickup Ontime";
			}else if(puDateFinal.compareTo(puDateSystem) == -1){
				return "Pickup Early";
			}else{
				return "Pickup Late";
			}
			
		} catch (Exception e) {
		}
		
		return "";
	}
	
	private String getSLALogistic(ResultSet rs){
		
		try {
			if(rs.getInt("etd") == 0 || 
				rs.getTimestamp("actual_pickup_date") == null || 
				rs.getDate("received") == null ){
				
				return "";
			}
		
			//ETD
			int etd = rs.getInt("etd");
			//Pickup Date
			Calendar puDate = Calendar.getInstance();
			puDate.setTime(rs.getTimestamp("actual_pickup_date"));
			//Received Date
			Calendar receivedDate = Calendar.getInstance();
			receivedDate.setTime(rs.getDate("received"));
			//Temp Date, initialized as PU Date
			Calendar tempDate = Calendar.getInstance();
			tempDate = puDate;
			//Get day diff between received date and pickup date
			int actualDayDiff = Days.daysBetween(new DateTime(puDate), new DateTime(receivedDate)).getDays();
			//Get total week between received date and pickup date
			int numOfWeek = ( actualDayDiff + 7 -1 ) / 7;
			//loop thru weeks to get number of weekends 
			for (int i = 0; i < numOfWeek; i++) {
				// get weekend date for current week
				Calendar saturday = Calendar.getInstance();
				saturday = tempDate;
				saturday.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				// get weekend date for current week
				Calendar sunday = Calendar.getInstance();
				sunday = tempDate;
				sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				
				// add additional days to ETD when there is weekend in current week
				if(receivedDate.after(saturday) || receivedDate.after(sunday)){
					etd = etd + 2;
				}
				// increment week
				tempDate.add(Calendar.WEEK_OF_MONTH, 1);
			}
			
			if(etd == actualDayDiff){
				return "Ontime";
			}else if(etd > actualDayDiff){
				return "Early " + (etd - actualDayDiff) + " day(s)";
			}else{
				return "Late";
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
		
	}
	
	private String getTotalDeliveryTime(ResultSet rs){
		
		int dayDiff;
		
		try {
			
			if(rs.getDate("received")==null){
				return "";
			}
			
			dayDiff = Days.daysBetween(new DateTime(rs.getTimestamp("order_date")), new DateTime(rs.getDate("received"))).getDays();
			return String.valueOf(dayDiff);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 
	 * @param args "daily" or "weekly"
	 */
	public static void main(String[] args){
		try {
			OrderTrackingReportBatchJob job = new OrderTrackingReportBatchJob();
			
			if(args.length > 0){
				
				type = args[0];
				
				if(type.equalsIgnoreCase(ARG_DAILY) || type.equalsIgnoreCase(ARG_WEEKLY)){
					_log.info("Start OrderTrackingReportBatchJob");
					long startTime = System.currentTimeMillis();
					job.start();
					long endTime = System.currentTimeMillis();
					_log.info("Done OrderTrackingReportBatchJob");
					
					String time = String.format("%d min, %d sec", 
						    TimeUnit.MILLISECONDS.toMinutes(endTime - startTime),
						    TimeUnit.MILLISECONDS.toSeconds(endTime - startTime) - 
						    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime - startTime))
					);
					
					_log.info("Execution Time = " + time);
				}else{
					_log.error("invalid argument, use \"daily\" or \"weekly\"");
				}
				
			}else{
				_log.error("argument \"daily\" or \"weekly\" is needed");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
