package com.gdn.venice.logistics.batch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.util.HolidayUtil;
import com.gdn.venice.integration.outbound.Publisher;
import com.gdn.venice.logistics.dataexport.EmailSender;
import com.gdn.venice.logistics.dataexport.PickupOrder;
import com.gdn.venice.logistics.dataexport.PickupOrderGenerator;
import com.gdn.venice.logistics.dataexport.PickupOrderTemp;
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.logistics.integration.AirwayBillEngineConnector;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

/**
 * Export class for pickup reports.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class LogisticsPickupReportBatchJob {

	protected static Logger _log = null;
	
	private static final String PICKUP_ORDER_LIST_SQL = "select * from ( " + 
																							    " select distinct on(oi.order_item_id) oi.order_item_id, o.order_id, wcs_order_id, wcs_order_item_id, wcs_product_name, insurance_cost, shipping_cost, total, rma_flag, gift_wrap_flag, gift_wrap_price, gift_card_note, gdn_reference," +
																								" oi.quantity as item_quantity, " +
																								" l.service_code, wcs_product_sku," +
																								" mp.full_or_legal_name as merchant_name, pc.full_or_legal_name as customer_name, pr.full_or_legal_name as recipient_name, merchant_pic, merchant_pic_phone, pickup_date_time," +
																								" oi.special_handling_instructions as item_special_handling, mpi.special_handling_instructions as pickup_special_handling," +
																								" a.street_address_1 as pickup_street_address_1, a.street_address_2 as pickup_street_address_2, a.kecamatan as pickup_kecamatan, a.kelurahan as pickup_kelurahan, c.city_code as pickup_city_code, c.city_name as pickup_city, s.state_name as pickup_state, a.postal_code as pickup_postal_code,"+
																								" ma.street_address_1 as merchant_street_address_1, ma.street_address_2 as merchant_street_address_2, mc.city_name as merchant_city, ms.state_name as merchant_state"+
																								" from ven_order_item oi join ven_order o on oi.order_id=o.order_id" +
																								" join log_logistic_service l on oi.logistics_service_id=l.logistics_service_id" +
																								" join log_logistics_provider lp on l.logistics_provider_id=lp.logistics_provider_id" +
																								" join log_merchant_pickup_instructions mpi on oi.order_item_id=mpi.order_item_id" +
																								" join ven_address a on mpi.pickup_address_id=a.address_id" +
																								" join ven_city c on a.city_id=c.city_id" +
																								" join ven_state s on a.state_id=s.state_id" +
																								" join ven_merchant m on mpi.merchant_id=m.merchant_id" +
																								" join ven_party mp on m.party_id=mp.party_id" +
																								" join (select * from ven_party_address where address_id in (select max(address_id) from ven_party_address group by party_id)) mpa on mp.party_id=mpa.party_id" +
																								" join ven_address ma on mpa.address_id=ma.address_id" +
																								" join ven_city mc on ma.city_id=mc.city_id" +
																								" join ven_state ms on ma.state_id=ms.state_id" +
																								" join log_airway_bill lab on oi.order_item_id=lab.order_item_id" +
																								" join ven_merchant_product mpr on oi.product_id=mpr.product_id" +
																								" join ven_customer cus on o.customer_id=cus.customer_id" +
																								" join ven_party pc on cus.party_id=pc.party_id" +
																								" join ven_recipient r on oi.recipient_id=r.recipient_id" +
																								" join ven_party pr on r.party_id=pr.party_id" +
																								" where"+
																								" o.blocked_flag <> TRUE" +
																								" and wcs_order_item_id = ?" +
																								" and oi.order_status_id = 8" +
																								" order by oi.order_item_id, mpa.address_id desc" +
																								" ) a order by a.pickup_city_code asc, a.merchant_name asc";
	
	private static final String LOGISTIC_SERVICE_SQL = "select logistics_provider_id from log_logistics_provider where logistics_provider_code = ?";
	
	private static final String UPDATE_LOG_AIRWAYBILL = "update log_airway_bill set logistics_provider_id = ?, sequence = ? where order_item_id = ?";
	
	
	private static final String SENDER_CONTACT_DETAIL_LIST_SQL = "select o.order_id,  contact_detail_type_id, contact_detail from ven_order_contact_detail o " +
																													" join ven_contact_detail c on o.contact_detail_id=c.contact_detail_id " +
																													" where o.order_id=?";
	
	private static final String UPDATE_ORDER_ITEM_SQL = "update ven_order_item set " +
																									" order_status_id = " +VeniceConstants.VEN_ORDER_STATUS_ES+
																									" where order_item_id = ?";
	
	private static final String INSERT_ORDER_ITEM_HISTORY_SQL = "insert into ven_order_item_status_history (order_item_id, history_timestamp, order_status_id, status_change_reason) " +
																												" values (?, ?, ?, 'Updated by System')";
	
	//retur
	private static final String PICKUP_RETUR_LIST_SQL = "select * from (" +
																							" select distinct on(oi.retur_item_id) oi.retur_item_id, o.retur_id, wcs_retur_id, wcs_retur_item_id, wcs_product_name, insurance_cost, shipping_cost, total, rma_flag, gift_wrap_flag, gift_wrap_price, gift_card_note, gdn_reference," + 
																							" oi.quantity as item_quantity,  " +
																							" l.service_code, wcs_product_sku," + 
																							" mp.full_or_legal_name as merchant_name, pc.full_or_legal_name as customer_name, pr.full_or_legal_name as recipient_name, merchant_pic, merchant_pic_phone, pickup_date_time," + 
																							" oi.special_handling_instructions as item_special_handling, mpi.special_handling_instructions as pickup_special_handling," + 
																							" a.street_address_1 as pickup_street_address_1, a.street_address_2 as pickup_street_address_2, a.kecamatan as pickup_kecamatan, a.kelurahan as pickup_kelurahan, c.city_code as pickup_city_code, c.city_name as pickup_city, s.state_name as pickup_state, a.postal_code as pickup_postal_code," +
																							" ma.street_address_1 as merchant_street_address_1, ma.street_address_2 as merchant_street_address_2, mc.city_name as merchant_city, ms.state_name as merchant_state" +
																							" from ven_retur_item oi join ven_retur o on oi.retur_id=o.retur_id" + 
																							" join log_logistic_service l on oi.logistics_service_id=l.logistics_service_id" + 
																							" join log_logistics_provider lp on l.logistics_provider_id=lp.logistics_provider_id" + 
																							" join log_merchant_pickup_instructions mpi on oi.retur_item_id=mpi.retur_item_id" + 
																							" join ven_address a on mpi.pickup_address_id=a.address_id" + 
																							" join ven_city c on a.city_id=c.city_id" + 
																							" join ven_state s on a.state_id=s.state_id" + 
																							" join ven_merchant m on mpi.merchant_id=m.merchant_id" +
																							" join ven_party mp on m.party_id=mp.party_id" + 
																							" join (select * from ven_party_address where address_id in (select max(address_id) from ven_party_address group by party_id)) mpa on mp.party_id=mpa.party_id" + 
																							" join ven_address ma on mpa.address_id=ma.address_id" + 
																							" join ven_city mc on ma.city_id=mc.city_id" + 
																							" join ven_state ms on ma.state_id=ms.state_id" + 
																							" join log_airway_bill_retur lab on oi.retur_item_id=lab.retur_item_id" + 
																							" join ven_merchant_product mpr on oi.product_id=mpr.product_id" + 
																							" join ven_customer cus on o.customer_id=cus.customer_id" + 
																							" join ven_party pc on cus.party_id=pc.party_id" + 
																							" join ven_recipient r on oi.recipient_id=r.recipient_id" + 
																							" join ven_party pr on r.party_id=pr.party_id" + 
																							" where" +
																							" o.blocked_flag <> TRUE" +
																							" and wcs_retur_item_id = ?" +
																							" and oi.retur_status_id = 8" +
																							" order by oi.retur_item_id, mpa.address_id desc" +
																							" ) a order by a.pickup_city_code asc, a.merchant_name asc";
	
	private static final String UPDATE_LOG_AIRWAYBILL_RETUR = "update log_airway_bill_retur set logistics_provider_id = ?, sequence = ? where retur_item_id = ?";
	
	private static final String SENDER_CONTACT_DETAIL_RETUR_LIST_SQL = "select o.retur_id,  contact_detail_type_id, contact_detail from ven_retur_contact_detail o " +
																															" join ven_contact_detail c on o.contact_detail_id=c.contact_detail_id " +
																															" where o.retur_id=?";
	
	private static final String UPDATE_RETUR_ITEM_SQL = "update ven_retur_item set " +
																								" retur_status_id = " +VeniceConstants.VEN_ORDER_STATUS_ES+
																								" where retur_item_id = ?";

	private static final String INSERT_RETUR_ITEM_HISTORY_SQL = "insert into ven_retur_item_status_history (retur_item_id, history_timestamp, retur_status_id, status_change_reason) " +
																											" values (?, ?, ?, 'Updated by System')";
	
	private static String CONFIG_FILE = System.getenv("VENICE_HOME") + "/admin/config.properties";
	Properties prop = new Properties();
	private String dbHost = "";
	private String dbPort = "";
	private String dbUsername = "";
	private String dbPassword = "";
	private String environment = "";
	private String dbName = "";
	private static Connection conn;
	
	private AirwayBillEngineConnector awbConn = new AirwayBillEngineClientConnector();

	/**
	 * Basic constructor
	 */
	public LogisticsPickupReportBatchJob()  throws FileNotFoundException, IOException, ClassNotFoundException, SQLException{
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataexport.main.LogisticsPickupReportMain");
		
		prop.load(new FileInputStream(CONFIG_FILE));
		environment = prop.getProperty("environment");
		dbHost = prop.getProperty(environment + ".dbHost");
		dbPort = prop.getProperty(environment + ".dbPort");
		dbUsername = prop.getProperty(environment + ".dbUsername");
		dbPassword = prop.getProperty(environment + ".dbPassword");
		dbName = prop.getProperty(environment + ".dbName");
		
		System.out.println("environment: "+environment);
		System.out.println("dbHost: "+dbHost);
		System.out.println("dbPort: "+dbPort);
		
		setupDBConnection();
	}
	
	private void setupDBConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");		
		conn = DriverManager.getConnection("jdbc:postgresql://" + dbHost +":" + dbPort + "/" + dbName, dbUsername, dbPassword);
	}
	
	/**
	 * 
	 * @param rsPickupList
	 * @param airwayBillTransaction
	 * @return mapped PickupOrderTemp
	 * @throws SQLException
	 */
	private PickupOrderTemp pickUpOrderTempMapper(ResultSet rsPickupList, AirwayBillTransaction airwayBillTransaction, String orderOrRetur) throws SQLException{
		
		PickupOrderTemp pickupOrderTemp = new PickupOrderTemp();
		
		if(orderOrRetur.equals("order")){
			pickupOrderTemp.setOrderId(rsPickupList.getString("order_id"));
			pickupOrderTemp.setOrderItemId(rsPickupList.getString("order_item_id"));
			pickupOrderTemp.setWcsOrderId(rsPickupList.getString("wcs_order_id"));
			pickupOrderTemp.setWcsOrderItemId(rsPickupList.getString("wcs_order_item_id"));
		}else if(orderOrRetur.equals("retur")){
			pickupOrderTemp.setOrderId(rsPickupList.getString("retur_id"));
			pickupOrderTemp.setOrderItemId(rsPickupList.getString("retur_item_id"));
			pickupOrderTemp.setWcsOrderId(rsPickupList.getString("wcs_retur_id"));
			pickupOrderTemp.setWcsOrderItemId(rsPickupList.getString("wcs_retur_item_id"));
		}
		
		pickupOrderTemp.setWcsProductName(rsPickupList.getString("wcs_product_name"));
		pickupOrderTemp.setRmaFlag(rsPickupList.getBoolean("rma_flag"));
		pickupOrderTemp.setGiftWrapFlag(rsPickupList.getBoolean("gift_wrap_flag"));
		pickupOrderTemp.setGiftCardNote(rsPickupList.getString("gift_card_note"));
		pickupOrderTemp.setServiceCode(airwayBillTransaction.getServiceType());
		pickupOrderTemp.setWcsProductSku(rsPickupList.getString("wcs_product_sku"));
		pickupOrderTemp.setMerchantName(rsPickupList.getString("merchant_name"));
		pickupOrderTemp.setCustomerName(rsPickupList.getString("customer_name"));
		pickupOrderTemp.setRecipientName(airwayBillTransaction.getNamaPenerima());
		pickupOrderTemp.setMerchantPic(rsPickupList.getString("merchant_pic"));
		pickupOrderTemp.setMerchantPicPhone(rsPickupList.getString("merchant_pic_phone"));
		pickupOrderTemp.setItemspecialHandling(rsPickupList.getString("item_special_handling"));
		pickupOrderTemp.setPickupSpecialHandling(rsPickupList.getString("pickup_special_handling"));
		pickupOrderTemp.setPickupStreetAddressLine1(rsPickupList.getString("pickup_street_address_1"));
		pickupOrderTemp.setPickupStreetAddressLine2(rsPickupList.getString("pickup_street_address_2"));
		pickupOrderTemp.setPickupKecamatan(rsPickupList.getString("pickup_kecamatan"));
		pickupOrderTemp.setPickupKelurahan(rsPickupList.getString("pickup_kelurahan"));
		pickupOrderTemp.setPickupCity(rsPickupList.getString("pickup_city"));
		pickupOrderTemp.setPickupState(rsPickupList.getString("pickup_state"));
		pickupOrderTemp.setPickupPostalCode(rsPickupList.getString("pickup_postal_code"));
		pickupOrderTemp.setMerchantStreetAddressLine1(rsPickupList.getString("merchant_street_address_1"));
		pickupOrderTemp.setMerchantStreetAddressLine2(rsPickupList.getString("merchant_street_address_2"));
		pickupOrderTemp.setMerchantCity(rsPickupList.getString("merchant_city"));
		pickupOrderTemp.setMerchantState(rsPickupList.getString("merchant_state"));
		
		pickupOrderTemp.setInsuranceCost(airwayBillTransaction.getInsuranceCost());
		pickupOrderTemp.setShippingCost(airwayBillTransaction.getShippingCost());
		pickupOrderTemp.setTotal(airwayBillTransaction.getAirwaybillShippingCost());
		pickupOrderTemp.setGiftWrapPrice(airwayBillTransaction.getGiftWrap());
		pickupOrderTemp.setItemQuantity(airwayBillTransaction.getQtyProduk());
		pickupOrderTemp.setPickupDateTime(airwayBillTransaction.getTanggalPickup());
		pickupOrderTemp.setAirwayBillNumber(airwayBillTransaction.getAirwayBillNo());			
		pickupOrderTemp.setAirwayBillTransaction(airwayBillTransaction);
		
		return pickupOrderTemp;		
	}

	/**
	 * Exports the pickup report for a logistics provider taking the list of
	 * order items that are in PU status, exporting the file and then setting
	 * the order items to ES status.
	 * 
	 * Note that the ES status will be picked up in the outbound integration.
	 * 
	 * @param logLogisticsProvider
	 * @return returns the file name and path of the report just generated
	 * @throws Exception
	 */
	public String exportPickupReport(String logisticsProvider) throws Exception {
		
		String filePath = System.getenv("VENICE_HOME") + "/files/export/pickup/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = "";
		int totalPickupList = 0;
		
		PreparedStatement psPickupList = null;
		ResultSet rsPickupList = null;
		PreparedStatement psRecipientContactDetailList = null;
		ResultSet rsRecipientContactDetailList = null;
		PreparedStatement psSenderContactDetailList = null;
		ResultSet rsSenderContactDetailList = null;
		PreparedStatement psOrderItem = null;
		PreparedStatement psOrderItemHistory = null;
		
		Boolean reportGenerated = false;
		
		// Determine if the run is in the morning or the afternoon
		_log.debug("determine if the run is in the morning or the afternoon");
		Calendar calendarCurrent = new GregorianCalendar();
		Boolean morningRun = true;
		if (calendarCurrent.get(Calendar.AM_PM) == Calendar.PM) {
			morningRun = false;
			_log.info("it is afternoon run");
		}else{
			_log.info("it is morning run");
		}
		
		/*
		 * In accordance with VENICE-27 RPX is only processed in the afternoon
		 * In accordance with VENICE-218 RPX is processed in the morning and afternoon
		 */
//		_log.debug("check RPX morning run");
//		if(logisticsProvider.equals("RPX") && morningRun){
//			_log.debug("exiting export pickup report processing for RPX because RPX is only processed in the afternoon...");
//			return "";
//		}else{
//			_log.debug("it is afternoon run, so process RPX too");
//		}

		// If the run is on the weekend then just exit gracefully
		_log.debug("check weekend or holiday");
		if (HolidayUtil.isHolidayOrWeekend(calendarCurrent.getTime())) {
			_log.debug("exiting export pickup report processing because date is a weekend or holiday...");
			return "";
		}else{
			_log.debug("date is not weekend or holiday");
		}

		/*
		 * Determine if the run is the last run of the week or not
		 *  - Could be any day based on scheduled holidays but
		 *    it would normally be a Friday
		 */
		_log.debug("check last run of the week");
		Boolean lastRunOfWeek = false;
		if (HolidayUtil.isLastWorkingDayOfWeek(calendarCurrent.getTime())) {
			lastRunOfWeek = true;
			_log.debug("it is last run of the week");
		}else{
			_log.debug("it is not last run of the week");
		}

		

		/*
		 * If it is the morning run the name the file to the current day
		 * afternoon If it is the afternoon run then name the file to the
		 * following day morning If it is Friday afternoon then name the file to
		 * Monday morning
		 */
		if (morningRun) {
			/*
			 *  Name the file accoding to the current date and AM or PM
			 *  	o Note that a file produced in the morning is for afternoon and vice versa
			 */
			fileName = logisticsProvider + " Order Pengambilan " + sdf.format(new Date()) + "1400" + ".xls";
			_log.info("morning run");
		} else {
			/* If it is the last run of the week then name the file as the
			 * first working day of the following week 
			 */
			if (lastRunOfWeek) {
				Calendar firstWorkingDayOfWeek = new GregorianCalendar();
				// Get the first working day of the week
				firstWorkingDayOfWeek = HolidayUtil.getNextWorkingDayFollowingWeek(firstWorkingDayOfWeek.getTime());
				Date dFirstDayOfWeek = firstWorkingDayOfWeek.getTime();
				fileName = logisticsProvider + " Order Pengambilan " + sdf.format(dFirstDayOfWeek) + "1000" + ".xls";
				_log.debug("file name: "+fileName);
				_log.debug("last run of week (next working day following week schedule)");
			} else {
				// Name the file to the next working day
				Calendar calendarNextWorkingDay = new GregorianCalendar();
				calendarNextWorkingDay = HolidayUtil.getNextWorkingDayOfWeek(calendarNextWorkingDay.getTime());
				Date dNextWorkingDay = calendarNextWorkingDay.getTime();
				fileName = logisticsProvider + " Order Pengambilan " + sdf.format(dNextWorkingDay) + "1000" + ".xls";
				_log.debug("file name: "+fileName);
				_log.debug("regular afternoon run (next working day schedule)");
			}
		}		
	
		
		try {
			
			//get today's date
			SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");		
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			Calendar calendar = Calendar.getInstance();  
			String dateParam = dateTimestamp.format(calendar.getTime());
			Date date = (Date)dateFormat.parse(dateParam); 						
			int daySpan=7;	

            Date toWeek = DateUtils.addDays(date,daySpan);   
            String dateEnd=dateTimestamp.format(toWeek.getTime());
            String dateStart=dateTimestamp.format(date);                    
            
            _log.debug("logistic provider: "+logisticsProvider);
            _log.debug("date start "+dateStart+" date End "+dateEnd);
            
			/*
			 * Processs the list of items first...
			 *  o populate all the required South data  
			 *  o remove any items that are not to be sent
			 */				
			ArrayList<PickupOrder> pickupOrderList = new ArrayList<PickupOrder>();		
			ArrayList<PickupOrderTemp> pickupOrderTempList = new ArrayList<PickupOrderTemp>();	
			
            List<AirwayBillTransaction> airwayBillTransactions = awbConn.getAirwayBillReadyForES(logisticsProvider, date, toWeek);
            
            totalPickupList = (airwayBillTransactions != null?airwayBillTransactions.size():0);

            if(totalPickupList > 0){

				_log.debug("Airway bill engine returned " + totalPickupList + " rows, check any items that are not to be sent");
				
				for (AirwayBillTransaction airwayBillTransaction : airwayBillTransactions){
		            Boolean isRetur = airwayBillTransaction.getGdnRef().contains("R-");
		            String orderOrRetur = "order";
					if(isRetur){
						_log.debug("Query for retur Item");
						psPickupList = conn.prepareStatement(PICKUP_RETUR_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						orderOrRetur = "retur";
					}else{
						_log.debug("Query for order Item");
						psPickupList = conn.prepareStatement(PICKUP_ORDER_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					}

					psPickupList.setString(1, airwayBillTransaction.getOrderItemId());
					rsPickupList = psPickupList.executeQuery();
					
					rsPickupList.last();
					int totalLocalPickupList = rsPickupList.getRow();
					rsPickupList.beforeFirst();
					rsPickupList.next();
					
					if(totalLocalPickupList > 0){
						_log.debug(orderOrRetur+" Item " + airwayBillTransaction.getOrderItemId() + " is available");
					}else{
						_log.debug(orderOrRetur+" Item " + airwayBillTransaction.getOrderItemId() + " is NOT available, this retur will not be sent");
						continue;
					}
										
					//store in pickupOrder, add to list if it is meet the date/time criteria for pickup
					PickupOrderTemp pickupOrderTemp = pickUpOrderTempMapper(rsPickupList, airwayBillTransaction, orderOrRetur);
					
					/*
					 * Remove any items from the list that do not not meet the
					 * date/time criteria for pickup
					 * 
					 * o Mon-Fri Afternoon Pickup:
					 *   (Mon-Fri Morning Run)
					 *   Items to be picked up the same day 
					 *   in the afternoon are included in the 
					 *   morning run 
					 * 
					 * o Tue-Fri Morning Pickup
					 *   (Mon-Thu Afternoon Run)
					 *   Items to be picked up in the morning 
					 *   next day are included in the afternoon 
					 *   run 
					 * 
					 * o Monday Morning Pickup
					 *   (Friday Afternoon Run)
					 *   The Friday afternoon run includes 
					 *   everything for Monday morning
					 *   
					 */
					
					Calendar puDateTimeCal = new GregorianCalendar();
					puDateTimeCal.setTimeInMillis((rsPickupList.getTimestamp("pickup_date_time")).getTime());

					// If the run is the morning run else
					if (morningRun) {
						/*
						 * Mon-Fri:MorningRun If the item is not scheduled for
						 * pickup in the afternoon then remove it on the current
						 * date
						 */
						if ((puDateTimeCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) 
								&& puDateTimeCal.get(Calendar.DAY_OF_YEAR) > calendarCurrent.get(Calendar.DAY_OF_YEAR))
								|| puDateTimeCal.get(Calendar.AM_PM) != Calendar.PM) {
							_log.debug("morning run... Item is not scheduled for current afternoon so removing from export list");
							--totalPickupList;
							continue;
						}else{
							pickupOrderTempList.add(pickupOrderTemp);
						}
					} else {
						if (lastRunOfWeek) {
							/*
							 * Fri:Afternoon If the item is not scheduled for
							 * pickup in the morning of the first day of the week 
							 * for any provider other than RPX then remove it.
							 * 
							 * Note that if the items were scheduled in the previous
							 * year then we should not remove them. So we compare
							 * the scheduled year to the current year also.
							 */
							Calendar calendarFirstWorkignDayOfWeek = HolidayUtil.getNextWorkingDayFollowingWeek(new Date());
							if (puDateTimeCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) 
									&& puDateTimeCal.get(Calendar.DAY_OF_YEAR) > calendarFirstWorkignDayOfWeek.get(Calendar.DAY_OF_YEAR)
//									|| (!logisticsProvider.equals("RPX") && puDateTimeCal.get(Calendar.AM_PM) != Calendar.AM)) {
									|| puDateTimeCal.get(Calendar.AM_PM) != Calendar.AM) {
								_log.debug("last run of week... either the item pickup is NOT scheduled for the first day of following week OR the item is NOT via RPX AND NOT for morning pickup... removing from export list");
								--totalPickupList;
								continue;
							}else{
								pickupOrderTempList.add(pickupOrderTemp);
							}
						} else {
							/*
							 * Mon-Thur:Afternoon If the item is not scheduled
							 * for pickup in the next working day of the week.
							 * 
							 * Note that if the items were scheduled in the previous
							 * year then we should not remove them. So we compare
							 * the scheduled year to the current year also.
							 * 
							 */
							Calendar calendarNextWorkingDayOfWeek = HolidayUtil.getNextWorkingDayOfWeek(new Date());
							if (puDateTimeCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) 
									&& puDateTimeCal.get(Calendar.DAY_OF_YEAR) > calendarNextWorkingDayOfWeek.get(Calendar.DAY_OF_YEAR)
//									|| (!logisticsProvider.equals("RPX") && puDateTimeCal.get(Calendar.AM_PM) != Calendar.AM)) {								
								|| puDateTimeCal.get(Calendar.AM_PM) != Calendar.AM){
								_log.debug("regular afternoon run (next working day schedule)... the item is NOT scheduled for pickup next working day this week OR the item is NOT via RPX and NOT scheduled for morning pickup... removing from export list");
								--totalPickupList;
								continue;
							}else{
								pickupOrderTempList.add(pickupOrderTemp);
							}
						}
					}
				}            	
            }else{
            	_log.info("Airway Bill Engine returned 0 rows");
            }
			
			
			if(pickupOrderTempList.size()>0){	
				/*
				 * Process the distribution carts for the items remaining in the list
				 */
				_log.info("item list size to process: "+pickupOrderTempList.size());
				for(int i=0;i<pickupOrderTempList.size();i++){
				
					PickupOrder pickupOrder = new PickupOrder();
					PickupOrderTemp pickupList = pickupOrderTempList.get(i);
					
		            Boolean isRetur = pickupList.getRmaFlag();
		            String orderOrRetur = "order";
		            
					if(isRetur){
						orderOrRetur = "retur";
					}
		            
					_log.info(orderOrRetur+" to be processed WCS Order/Retur Id:" +pickupList.getWcsOrderId()+ " WCS Order/Retur ItemId:" + pickupList.getWcsOrderItemId());

					pickupOrder.setCustomerName(pickupList.getCustomerName());
					pickupOrder.setDescription(pickupList.getWcsProductName());
					pickupOrder.setGiftCard(pickupList.getGiftCardNote());
					pickupOrder.setGiftWrap(pickupList.getGiftWrapFlag()!=null ? "Y" : "T");

					// Calculate the sum insured if the item is insured
					if (pickupList.getInsuranceCost().compareTo(new BigDecimal(0)) > 0) {
						
						Double insuredAmount = (pickupList.getItemQuantity() * pickupList.getAirwayBillTransaction().getHargaProduk()) + (pickupList.getShippingCost()==null?Double.valueOf("0"):pickupList.getShippingCost().doubleValue()) + (pickupList.getGiftWrapPrice()==null?Double.valueOf("0"):pickupList.getGiftWrapPrice().doubleValue()) + (pickupList.getAirwayBillTransaction().getFixedPrice()==null?Double.valueOf("0"):pickupList.getAirwayBillTransaction().getFixedPrice().doubleValue());
						pickupOrder.setInsuredAmount(insuredAmount);
						
					} else {
						pickupOrder.setInsuredAmount(new Double(0));
					}

					pickupOrder.setIsInsured(pickupList.getInsuranceCost().compareTo(new BigDecimal(0)) > 0 ? true : false);
					pickupOrder.setLogisticProviderName(logisticsProvider);

					pickupOrder.setOrderOrRMA(pickupList.getRmaFlag() ? "R" : "O");
					pickupOrder.setGdnRefNo(pickupList.getAirwayBillTransaction().getGdnRef());
					pickupOrder.setOrderId(pickupList.getWcsOrderId());
					pickupOrder.setOrderItemId(pickupList.getWcsOrderItemId());
					pickupOrder.setQuantity(pickupList.getItemQuantity());

					pickupOrder.setMerchantName(pickupList.getMerchantName());
					pickupOrder.setMerchantPhone(pickupList.getMerchantPicPhone());
					pickupOrder.setMerchantAddressLine1(pickupList.getMerchantStreetAddressLine1());
					pickupOrder.setMerchantAddressLine2(pickupList.getMerchantStreetAddressLine2());
					pickupOrder.setMerchantCity(pickupList.getMerchantCity());
					pickupOrder.setMerchantProvince(pickupList.getMerchantState());
					pickupOrder.setPickupDateTime(pickupList.getPickupDateTime());
					
					pickupOrder.setPicName(pickupList.getMerchantPic());
					pickupOrder.setPicPhone(pickupList.getMerchantPicPhone());
					pickupOrder.setPickupPointAddressLine1(pickupList.getPickupStreetAddressLine1());
					pickupOrder.setPickupPointAddressLine2(pickupList.getPickupStreetAddressLine2());
					pickupOrder.setPickupPointCity(pickupList.getPickupCity());
					pickupOrder.setPickupPointKecamatan(pickupList.getPickupKecamatan());
					pickupOrder.setPickupPointKelurahan(pickupList.getPickupKelurahan());
					pickupOrder.setPickupPointProvince(pickupList.getPickupState());
					pickupOrder.setPickupPointZipCode(pickupList.getPickupPostalCode());
						
					pickupOrder.setRecipientAddress(pickupList.getAirwayBillTransaction().getAlamatPenerima1());
					pickupOrder.setRecipientCity(pickupList.getAirwayBillTransaction().getKotaKabupatenPenerima());
					pickupOrder.setRecipientKecamatan(pickupList.getAirwayBillTransaction().getKelurahanKecamatanPenerima());
					pickupOrder.setRecipientKelurahan(pickupList.getAirwayBillTransaction().getKelurahanKecamatanPenerima());
					pickupOrder.setRecipientProvince(pickupList.getAirwayBillTransaction().getPropinsiPenerima());
					pickupOrder.setRecipientZipCode(pickupList.getAirwayBillTransaction().getKodeposPenerima());
						
					pickupOrder.setRecipientMobile(pickupList.getAirwayBillTransaction().getNoHPPicPenerima());
					pickupOrder.setRecipientPhone(pickupList.getAirwayBillTransaction().getNoHPPicPenerima());
					pickupOrder.setRecipientName(pickupList.getRecipientName());
					
					//get weight to determine if pickup using car or motorcycle
					//if weight >= 5 kg then use car, else use motorcycle
					//get the weight per distribution cart / per gdn reference		
					//TODO: get weight from awb engine
					BigDecimal weight = new BigDecimal(0);
					weight = new BigDecimal(pickupList.getAirwayBillTransaction().getBeratCetak());
						
					if(weight.compareTo(new BigDecimal(5))>0){
						pickupOrder.setPickupMethod("Mobil");
					}else{
						pickupOrder.setPickupMethod("Motor");
					}						

					if(isRetur){						
						psSenderContactDetailList = conn.prepareStatement(SENDER_CONTACT_DETAIL_RETUR_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					}else{
						psSenderContactDetailList = conn.prepareStatement(SENDER_CONTACT_DETAIL_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					}
					
					psSenderContactDetailList.setLong(1, new Long(pickupList.getOrderId()));
					rsSenderContactDetailList = psSenderContactDetailList.executeQuery();
					
					rsSenderContactDetailList.last();
					int totalSenderContactDetailList = rsSenderContactDetailList.getRow();
					rsSenderContactDetailList.beforeFirst();
					
					String senderMobile = "";
					String senderPhone = "";
					String senderEmail = "";
					if(totalSenderContactDetailList>0){
						rsSenderContactDetailList.beforeFirst();
						while (rsSenderContactDetailList.next()) {
							if (rsSenderContactDetailList.getInt("contact_detail_type_id")==VeniceConstants.VEN_CONTACT_TYPE_MOBILE) {
								senderMobile = rsSenderContactDetailList.getString("contact_detail");
							}
							if (rsSenderContactDetailList.getInt("contact_detail_type_id")==VeniceConstants.VEN_CONTACT_TYPE_PHONE) {
								senderPhone = rsSenderContactDetailList.getString("contact_detail");
							}
							if (rsSenderContactDetailList.getInt("contact_detail_type_id")==VeniceConstants.VEN_CONTACT_TYPE_EMAIL) {
							senderEmail = rsSenderContactDetailList.getString("contact_detail");
							}
						}
					}
					
					pickupOrder.setSenderMobile(senderMobile);
					pickupOrder.setSenderPhone(senderPhone);
					pickupOrder.setSenderEmail(senderEmail);
					
					_log.debug("sender info, email: " + pickupOrder.getSenderEmail() + ", mobile: " + pickupOrder.getSenderMobile() + ", phone: " + pickupOrder.getSenderPhone());
					
					pickupOrder.setAirwayBillNo(pickupList.getAirwayBillNumber());
					
					// Map the service type codes
//					if (pickupList.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_NCS_REG)) {
//						pickupOrder.setServiceType("REGULER");
//					} else if (pickupList.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_NCS_EXP)) {
//						pickupOrder.setServiceType("KIRIMAN 1 HARI");
//					} else if (pickupList.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_RPX_REG)) {
//						pickupOrder.setServiceType("EP");
//					} else if (pickupList.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_RPX_EXP)) {
//						pickupOrder.setServiceType("PP");
//					}
					
					pickupOrder.setServiceType(pickupList.getServiceCode());

					pickupOrder.setSkuId(pickupList.getWcsProductSku());
					
					//Default to the special handling from WCS originally
					if(pickupList.getItemspecialHandling() != null && !pickupList.getItemspecialHandling().isEmpty()){
						pickupOrder.setSpecialHandlingInstruction(pickupList.getItemspecialHandling());
					}
					
					/*
					 * If the instructions are still null 
					 * then set to the instruction sent from MTA if not null
					 */
					if ((pickupOrder.getSpecialHandlingInstruction() == null || pickupOrder.getSpecialHandlingInstruction().isEmpty())
							&& pickupList.getPickupSpecialHandling() != null && !pickupList.getPickupSpecialHandling().isEmpty()) {
						pickupOrder.setSpecialHandlingInstruction(pickupList.getPickupSpecialHandling());
					}

					// Add the pickup order to the list
					pickupOrderList.add(pickupOrder);
	
					PickupOrderGenerator puog = new PickupOrderGenerator();
					puog.exportData(filePath + fileName, pickupOrderList);
					reportGenerated = true;
				}
			}else{
				_log.info("no order item list to process");
			}
			
			if(reportGenerated){
				// Set all of the order items in the list to status ES
				_log.debug("processing ES status for " + totalPickupList + " items.");
				VenOrderStatus statusES = new VenOrderStatus();
				statusES.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_ES);
				for(int i=0;i<pickupOrderTempList.size();i++){
					
					/**
					 * to determine whether update order item to ES or NOT, depends on airway bill engine response
					 * data before airwaybill engine will be updated directly
					 */
					boolean isUpdateESAllowed = true;
					
					String gdnRefStatus = awbConn.getGDNRefStatus(pickupOrderTempList.get(i).getAirwayBillTransaction().getGdnRef());
		            Boolean isRetur = pickupOrderTempList.get(i).getRmaFlag();
		            String orderOrRetur = "order";
		            
					if(isRetur){
						orderOrRetur = "retur";
					}
					/**
					 * when airway bill status already ES, skip updateAirwayBillToES
					 */					
					if(!gdnRefStatus.equals(AirwayBillTransaction.STATUS_EMAIL_SENT)){
						_log.debug("processing ES status for "+orderOrRetur+" item id: " + pickupOrderTempList.get(i).getWcsOrderItemId() + " to airwaybill engine");
						/* update item status ES on airway bill engine */
						isUpdateESAllowed = awbConn.updateAirwayBillToES(pickupOrderTempList.get(i).getAirwayBillNumber(), logisticsProvider, "Venice Scheduler");
						if(isUpdateESAllowed){
							_log.debug("Successfully processing ES status for "+orderOrRetur+" item id: " + pickupOrderTempList.get(i).getWcsOrderItemId() + " to airwaybill engine");
						}else{
							_log.debug("Fail processing ES status for "+orderOrRetur+" item id: " + pickupOrderTempList.get(i).getWcsOrderItemId() + " to airwaybill engine");
						}
					}else{
						_log.debug("AWB No for "+orderOrRetur+" item id: " + pickupOrderTempList.get(i).getWcsOrderItemId() + " already ES");
					}
					
					if(isUpdateESAllowed){
						_log.debug("processing ES status for "+orderOrRetur+" item id: " + pickupOrderTempList.get(i).getWcsOrderItemId());	
						
						if(isRetur){
							psOrderItem = conn.prepareStatement(UPDATE_RETUR_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						}else{
							psOrderItem = conn.prepareStatement(UPDATE_ORDER_ITEM_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						}	
						psOrderItem.setLong(1, new Long(pickupOrderTempList.get(i).getOrderItemId()));
						int result=psOrderItem.executeUpdate();						
						_log.debug(orderOrRetur+" item: " +pickupOrderTempList.get(i).getWcsOrderItemId() + " updated to ES");
						
						if(result>0){
							//add order/retur item status history 
							_log.debug("add "+orderOrRetur+" item status history");
							
							updateLogAirwaybill(new Long(pickupOrderTempList.get(i).getOrderItemId()), logisticsProvider, Integer.parseInt(pickupOrderTempList.get(i).getAirwayBillTransaction().getGdnRef().split("-")[3]), isRetur);
							
							if(isRetur){
								psOrderItemHistory = conn.prepareStatement(INSERT_RETUR_ITEM_HISTORY_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							}else{
								psOrderItemHistory = conn.prepareStatement(INSERT_ORDER_ITEM_HISTORY_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							}
							psOrderItemHistory.setLong(1, new Long(pickupOrderTempList.get(i).getOrderItemId()));
							psOrderItemHistory.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
							psOrderItemHistory.setLong(3, VeniceConstants.VEN_ORDER_STATUS_ES);
							psOrderItemHistory.executeUpdate();		
							_log.debug("done add "+orderOrRetur+" item status history for order item id: " +pickupOrderTempList.get(i).getWcsOrderItemId());
							
							_log.debug("start publish "+orderOrRetur+" item status");
							VenOrderItem item = new VenOrderItem();
							item.setWcsOrderItemId(pickupOrderTempList.get(i).getWcsOrderItemId());																		
							item.setVenOrderStatus(statusES);
							
							VenOrder order = new VenOrder();
							order.setWcsOrderId(pickupOrderTempList.get(i).getWcsOrderId());
							if(isRetur){
								_log.debug("set rma flag true");
								order.setRmaFlag(true);
							}else{
								order.setRmaFlag(false);
							}
							item.setVenOrder(order);
							
							Publisher publisher = new Publisher();					
							publisher.publishUpdateOrderItemStatus(item, conn);
							_log.debug("done publish "+orderOrRetur+" item status");		
						}
					}
				}
			}else{
				_log.debug("no item to be updated to ES");
			}
						
			if (reportGenerated) {
				_log.info("report generated..." + filePath + fileName);
				return filePath + fileName;
			} else{
				_log.info("no report generated");
				return "";
			}
		} catch (Exception e) {
			String errMsg = "an exception occured when processing the pickup report for " + logisticsProvider+": ";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new Exception(errMsg);
		} finally{
			try{
				if(psPickupList!=null) psPickupList.close();
				if(rsPickupList!=null) rsPickupList.close();
				if(psRecipientContactDetailList!=null) psRecipientContactDetailList.close();
				if(rsRecipientContactDetailList!=null) rsRecipientContactDetailList.close();
				if(psSenderContactDetailList!=null) psSenderContactDetailList.close();
				if(rsSenderContactDetailList!=null) rsSenderContactDetailList.close();
				if(psOrderItem!=null) psOrderItem.close();
				if(psOrderItemHistory!=null) psOrderItemHistory.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void updateLogAirwaybill(Long orderItemId, String logisticProviderCode, int sequence, Boolean isRetur) throws Exception{		
		try{
			Long logisticProviderId = fetchLogisticProvider(logisticProviderCode);
			PreparedStatement psLogisticProvider = null;
			if(isRetur){
				psLogisticProvider = conn.prepareStatement(UPDATE_LOG_AIRWAYBILL_RETUR);
			}else{
				psLogisticProvider = conn.prepareStatement(UPDATE_LOG_AIRWAYBILL);
			}
	    	psLogisticProvider.setLong(1, logisticProviderId);
	    	psLogisticProvider.setInt(2, sequence);
	    	psLogisticProvider.setLong(3, orderItemId);
	    	
	    	psLogisticProvider.execute();
	    	
	    	psLogisticProvider.close();
			
		}catch (Exception e) {
			_log.error("Logistic Provider is not updated due to " + e.getMessage(), e);
			throw new Exception("Error when updating log airway bill: "+isRetur);
		}		
	}
	
	private Long fetchLogisticProvider(String logisticProviderCode) throws Exception{
    	
    	PreparedStatement psLogisticProvider = conn.prepareStatement(LOGISTIC_SERVICE_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    	psLogisticProvider.setString(1, logisticProviderCode);
    	
    	ResultSet rsLogisticProvider = psLogisticProvider.executeQuery();
    	
    	rsLogisticProvider.last();
		int numLogisticProvider = rsLogisticProvider.getRow();
		rsLogisticProvider.beforeFirst();
		
		if(numLogisticProvider == 0)
			throw new Exception("Couldn't find Logistic Provider " + logisticProviderCode);
    	
		rsLogisticProvider.next();
		
		Long logisticProviderId = rsLogisticProvider.getLong("logistics_provider_id");
		
		rsLogisticProvider.close();
		psLogisticProvider.close();
		
		return logisticProviderId;
		
    }

	/**
	 * This is the main that initiates the whole process of exporting the
	 * order item data for order items that are at PU status in the database.
	 * 
	 * the email program is called directly afterward.
	 * 
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		LogisticsPickupReportBatchJob logisticsPickupReportMain = new LogisticsPickupReportBatchJob();
		_log.info("start LogisticPickupReport Scheduler");
		try {				
			String ncsFileName = logisticsPickupReportMain.exportPickupReport("NCS");
			if (ncsFileName != null && !ncsFileName.isEmpty()) {
				_log.info("exported NCS pickup report:" + ncsFileName);
			}
			
			String jneFileName = logisticsPickupReportMain.exportPickupReport("JNE");
			if (jneFileName != null && !jneFileName.isEmpty()) {
				_log.info("exported JNE pickup report:" + jneFileName);
			}
			
			String rpxFileName = logisticsPickupReportMain.exportPickupReport("RPX");
			if (rpxFileName != null && !rpxFileName.isEmpty()) {
				_log.info("exported RPX pickup report:" + rpxFileName);
			}	
			
			String msgFileName = logisticsPickupReportMain.exportPickupReport("MSG");
			if (msgFileName != null && !msgFileName.isEmpty()) {
				_log.info("exported MSG pickup report:" + msgFileName);
			}	

			/*
			 * Call the email sender to send the files to the respective provider
			 */
			_log.info("start send email");
			EmailSender es = new EmailSender();
			if (!es.sendFiles()) {
				_log.error("send files failed...");
			}else{
				_log.info("done send email");
			}
			_log.info("done LogisticPickupReport Scheduler");
		} catch (Exception e) {
			String errMsg = "an exception occured when processing the pickup report: ";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
		} finally{
			if(conn!=null) conn.close();
		}
	}
}
