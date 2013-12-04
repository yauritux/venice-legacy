package com.gdn.venice.facade.logistics.notification;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.log4j.Logger;
import org.hibernate.ejb.EntityManagerImpl;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.logistics.dataexport.EmailSender;
import com.gdn.venice.logistics.dataexport.PickupOrder;
import com.gdn.venice.logistics.dataexport.PickupOrderGenerator;
import com.gdn.venice.logistics.dataexport.PickupOrderTemp;
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.logistics.integration.AirwayBillEngineConnector;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;
import com.gdn.venice.util.VeniceConstants;

/**
 * Session Bean implementation class LogisticsPickupReportSessionEJBBean
 */
@Stateless(mappedName = "LogisticsPickupReportSessionEJBBean")
public class LogisticsPickupReportSessionEJBBean implements LogisticsPickupReportSessionEJBBeanRemote, LogisticsPickupReportSessionEJBBeanLocal {
	protected static Logger _log = null;
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	private EntityManager emForJDBC;
	
	AirwayBillEngineConnector awbConn;
	
	
	
	private static final String PICKUP_ORDER_LIST_SQL = "select o.order_id, oi.order_item_id, wcs_order_id, wcs_order_item_id, wcs_product_name, insurance_cost, shipping_cost, total, rma_flag, gift_wrap_flag, gift_wrap_price, gift_card_note," +
																									"oi.quantity as item_quantity, " +
																									"l.service_code, wcs_product_sku," +
																									" mp.full_or_legal_name as merchant_name, pc.full_or_legal_name as customer_name, pr.full_or_legal_name as recipient_name, merchant_pic, merchant_pic_phone, pickup_date_time," +
																									" oi.special_handling_instructions as item_special_handling, mpi.special_handling_instructions as pickup_special_handling," +
																									" a.street_address_1 as pickup_street_address_1, a.street_address_2 as pickup_street_address_2, a.kecamatan as pickup_kecamatan, a.kelurahan as pickup_kelurahan, c.city_name as pickup_city, s.state_name as pickup_state, a.postal_code as pickup_postal_code,"+
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
																									" where oi.wcs_order_item_id = ? " + 
																									" order by c.city_code asc, mp.full_or_legal_name asc";
	
	private static final String SHIPPING_ADDRESS_LIST_SQL =  "select street_address_1, kecamatan, kelurahan, city_name, state_name, postal_code from ven_order_item_address oia" +
																										" join ven_address a on oia.address_id=a.address_id" +
																										" join ven_city c on a.city_id=c.city_id" +
																										" join ven_state s on a.state_id=s.state_id" +
																										" where oia.order_item_id=?";
	
	private static final String RECIPIENT_CONTACT_DETAIL_LIST_SQL = "select o.order_item_id, contact_detail_type_id, contact_detail from ven_order_item_contact_detail o " +
																														" join ven_contact_detail c on o.contact_detail_id=c.contact_detail_id " +
																														" where o.order_item_id=?";

	private static final String SENDER_CONTACT_DETAIL_LIST_SQL = "select o.order_id,  contact_detail_type_id, contact_detail from ven_order_contact_detail o " +
																													" join ven_contact_detail c on o.contact_detail_id=c.contact_detail_id " +
																													" where o.order_id=?";
	
	private static final String LOGISTIC_PROVIDER_SQL = "select logistics_provider_id from log_logistics_provider where logistics_provider_code = ?";
	
	private static final String UPDATE_LOG_AIRWAYBILL = "update log_airway_bill set logistics_provider_id = ?, sequence = ? where order_item_id = (select order_item_id from ven_order_item where wcs_order_item_id = ?)";
	
	private static String filePath = "";
	
    /**
     * Default constructor. 
     */
    public LogisticsPickupReportSessionEJBBean() {
        // TODO Auto-generated constructor stubsuper();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.logistics.notification.LogisticsPickupReportSessionEJBBean");
		
		awbConn = new AirwayBillEngineClientConnector();
		
		filePath = System.getenv("VENICE_HOME") + "/files/export/pickup/";
    }
    
	@Override
	public void createChangeShippingNotification(String[] wcsOrderItemIds){
    	
    	List<AirwayBillTransaction> changeShippingAirwayBillTransactionList = new ArrayList<AirwayBillTransaction>();
    	
    	for (int i = 0; i < wcsOrderItemIds.length; i++) {
		
    		List<AirwayBillTransaction> tempAirwayBillTransactions = new ArrayList<AirwayBillTransaction>();
    		
    		try{
        		tempAirwayBillTransactions = awbConn.getAirwayBillTransactionByItem(wcsOrderItemIds[i]);
        	}catch (Exception e) {
        		String errMsg = "An exception occured while attemping to get airway bill transaction :" + e.getMessage();
    			_log.error(errMsg, e);
    		}
        	
        	/* Loop thru list of airway bills */
        	for (AirwayBillTransaction airwayBillTransaction : tempAirwayBillTransactions) {
        		/* Retrieve airway bill w/ new logistic provider which is the active airway bill */
    			if(airwayBillTransaction.getStatus().equals(AirwayBillTransaction.STATUS_EMAIL_SENT)){
    				changeShippingAirwayBillTransactionList.add(airwayBillTransaction);
    				break;
    			}
    		}
		}
    	
    	createChangeShippingNotification(changeShippingAirwayBillTransactionList);
    	
    }
	
	private void updateLogAirwaybill(String wcsOrderItemId, String logisticProviderCode, int sequence){
		
		try{
			Long logisticProviderId = fetchLogisticProvider(logisticProviderCode);
			
			if(emForJDBC == null)
				emForJDBC = emf.createEntityManager();
	    	
	    	Connection conn =  (Connection) ((EntityManagerImpl)emForJDBC).getSession().connection();
	    	
	    	PreparedStatement psLogisticProvider = conn.prepareStatement(UPDATE_LOG_AIRWAYBILL);
	    	psLogisticProvider.setLong(1, logisticProviderId);
	    	psLogisticProvider.setInt(2, sequence);
	    	psLogisticProvider.setString(3, wcsOrderItemId);
	    	
	    	psLogisticProvider.execute();
	    	
	    	psLogisticProvider.close();
	    	conn.close();
			
		}catch (Exception e) {
			_log.error("Log Airwaybill is not updated due to " + e.getMessage(), e);
		}
		
	}
    
    public void createChangeShippingNotification(List<AirwayBillTransaction> changeShippingAirwayBillTransactionList){
    	
    	String fileName = "";
    	String newLogisticProvider = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    	
		ArrayList<PickupOrder> pickupOrderList = new ArrayList<PickupOrder>();
		
		for (AirwayBillTransaction changeShippingAirwayBillTransaction:changeShippingAirwayBillTransactionList) {
			
			if(newLogisticProvider.equals("")) newLogisticProvider = changeShippingAirwayBillTransaction.getKodeLogistik();
			int newSequence = Integer.parseInt(changeShippingAirwayBillTransaction.getGdnRef().split("-")[3]);
			pickupOrderList.add(fetchPickupOrder(changeShippingAirwayBillTransaction.getOrderItemId(), changeShippingAirwayBillTransaction.getKodeLogistik()).get(0));
			
			// update logistic provider for current order item
			updateLogAirwaybill(changeShippingAirwayBillTransaction.getOrderItemId(), newLogisticProvider, newSequence);
		}
		
		fileName = newLogisticProvider + " Order Pengambilan " + sdf.format(changeShippingAirwayBillTransactionList.get(0).getTanggalPickup())+ ".xls";
		
		_log.debug("file name: "+fileName);
    	
    	PickupOrderGenerator puog = new PickupOrderGenerator();
		puog.exportData(filePath + fileName, pickupOrderList);
		
		_log.info("start send change shipping email");
		
		try{
			EmailSender es = new EmailSender(" Change Shipping");
			if (!es.sendFiles()) {
				_log.error("send files failed...");
			}else{
				_log.info("done send email");
			}
		}catch (Exception e) {
			String errMessage = "Problem sending email";
			_log.error(errMessage, e);
			
			throw new EJBException(errMessage, e);
		}
		
		_log.info("done  change shipping notification");
    	
    }
    
	@Override
	public void createChangeShippingNotification(String wcsOrderItemId){
    	
    	List<AirwayBillTransaction> airwayBillTransactionList = null;
    	
    	try{
    		airwayBillTransactionList = awbConn.getAirwayBillTransactionByItem(wcsOrderItemId);
    	}catch (Exception e) {
    		String errMsg = "An exception occured while attemping to get airway bill transaction :" + e.getMessage();
			_log.error(errMsg, e);
			e.printStackTrace();
			throw new EJBException(errMsg);
		}
    	
    	AirwayBillTransaction latestAirwayBillTransaction = new AirwayBillTransaction();
    	
    	/* Loop thru list of airway bills */
    	for (AirwayBillTransaction airwayBillTransaction : airwayBillTransactionList) {
    		/* Retrieve airway bill w/ new logistic provider which is the active airway bill */
			if(airwayBillTransaction.getStatus().equals(AirwayBillTransaction.STATUS_EMAIL_SENT)){
				latestAirwayBillTransaction = airwayBillTransaction;
				break;
			}
		}
    	
    	createChangeShippingNotification(wcsOrderItemId, latestAirwayBillTransaction.getKodeLogistik());
    	
    }
    
    @Override
	public void createChangeShippingNotification(String wcsOrderItemId, String newLogisticProvider){
    	
    	String fileName = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    	
    	ArrayList<PickupOrder> pickupOrderList = fetchPickupOrder(wcsOrderItemId, newLogisticProvider);

    	fileName = newLogisticProvider + " Order Pengambilan " + sdf.format(pickupOrderList.get(0).getPickupDateTime())+ ".xls";
		
		_log.debug("file name: "+fileName);
    	
    	PickupOrderGenerator puog = new PickupOrderGenerator();
		puog.exportData(filePath + fileName, pickupOrderList);
		
		_log.info("start send change shipping email");
		
		try{
			EmailSender es = new EmailSender(" Change Shipping");
			if (!es.sendFiles()) {
				_log.error("send files failed...");
			}else{
				_log.info("done send email");
			}
		}catch (Exception e) {
			String errMessage = "Problem sending email";
			_log.error(errMessage, e);
			
			throw new EJBException(errMessage, e);
		}
		
		_log.info("done  change shipping notification");
    	
    }
    
    private Long fetchLogisticProvider(String logisticProviderCode) throws Exception{
    	if(emForJDBC == null)
			emForJDBC = emf.createEntityManager();
    	
    	Connection conn =  (Connection) ((EntityManagerImpl)emForJDBC).getSession().connection();
    	
    	PreparedStatement psLogisticProvider = conn.prepareStatement(LOGISTIC_PROVIDER_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
		conn.close();
		
		return logisticProviderId;
		
    }
    
    private ArrayList<PickupOrder> fetchPickupOrder(String wcsOrderItemId, String newLogisticProvider){
    	
    	if(emForJDBC == null)
			emForJDBC = emf.createEntityManager();
    	
    	Connection conn =  (Connection) ((EntityManagerImpl)emForJDBC).getSession().connection();
    	
    	ArrayList<PickupOrder> pickupOrderList = new ArrayList<PickupOrder>();
    	
    	PreparedStatement psShippingAddressList = null;
		ResultSet rsShippingAddressList = null;
		PreparedStatement psRecipientContactDetailList = null;
		ResultSet rsRecipientContactDetailList = null;
		PreparedStatement psSenderContactDetailList = null;
		ResultSet rsSenderContactDetailList = null;
		PreparedStatement psPickupOrder = null;
		ResultSet rsPickupOrder = null;
    	
    	try {
    		
			psPickupOrder = conn.prepareStatement(PICKUP_ORDER_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			psPickupOrder.setString(1, wcsOrderItemId);
			
			rsPickupOrder = psPickupOrder.executeQuery();
			
			rsPickupOrder.last();
			int totalpickupOrderTemp = rsPickupOrder.getRow();
			rsPickupOrder.beforeFirst();
			
			if(totalpickupOrderTemp>0){
				
				_log.debug("query returned " + totalpickupOrderTemp + " rows, check any items that are not to be sent");
				rsPickupOrder.beforeFirst();
				
				rsPickupOrder.next();
					
				AirwayBillTransaction airwayBillTransaction = new AirwayBillTransaction();
				
				try{
					airwayBillTransaction = awbConn.getAirwayBillTransaction(rsPickupOrder.getString("wcs_order_id"), rsPickupOrder.getString("wcs_order_item_id"), false);
				}catch (Exception e) {
					String errMessage = "Unable to retrieve airway bill transaction";
					_log.error(errMessage, e);
					
					throw new EJBException(errMessage, e);
				}
				
				//store in pickupOrder, add to list if it is meet the date/time criteria for pickup
				PickupOrderTemp pickupOrderTemp = new PickupOrderTemp();
				pickupOrderTemp.setOrderId(rsPickupOrder.getString("order_id"));
				pickupOrderTemp.setOrderItemId(rsPickupOrder.getString("order_item_id"));
				pickupOrderTemp.setWcsOrderId(rsPickupOrder.getString("wcs_order_id"));
				pickupOrderTemp.setWcsOrderItemId(rsPickupOrder.getString("wcs_order_item_id"));
				pickupOrderTemp.setWcsProductName(rsPickupOrder.getString("wcs_product_name"));
				pickupOrderTemp.setInsuranceCost(airwayBillTransaction.getInsuranceCost());
				pickupOrderTemp.setShippingCost(airwayBillTransaction.getShippingCost());
				pickupOrderTemp.setTotal(airwayBillTransaction.getAirwaybillShippingCost());
				pickupOrderTemp.setRmaFlag(rsPickupOrder.getBoolean("rma_flag"));
				pickupOrderTemp.setGiftWrapFlag(rsPickupOrder.getBoolean("gift_wrap_flag"));
				pickupOrderTemp.setGiftWrapPrice(airwayBillTransaction.getGiftWrap());
				pickupOrderTemp.setGiftCardNote(rsPickupOrder.getString("gift_card_note"));
				pickupOrderTemp.setItemQuantity(airwayBillTransaction.getQtyProduk());
//				pickupOrderTemp.setDcSequence(rsPickupOrder.getInt("dc_sequence"));
				pickupOrderTemp.setServiceCode(rsPickupOrder.getString("service_code"));
				pickupOrderTemp.setWcsProductSku(rsPickupOrder.getString("wcs_product_sku"));
				pickupOrderTemp.setMerchantName(rsPickupOrder.getString("merchant_name"));
				pickupOrderTemp.setCustomerName(rsPickupOrder.getString("customer_name"));
				pickupOrderTemp.setRecipientName(rsPickupOrder.getString("recipient_name"));
				pickupOrderTemp.setMerchantPic(rsPickupOrder.getString("merchant_pic"));
				pickupOrderTemp.setMerchantPicPhone(rsPickupOrder.getString("merchant_pic_phone"));
				pickupOrderTemp.setPickupDateTime(airwayBillTransaction.getTanggalPickup());
				pickupOrderTemp.setItemspecialHandling(rsPickupOrder.getString("item_special_handling"));
				pickupOrderTemp.setPickupSpecialHandling(rsPickupOrder.getString("pickup_special_handling"));
				pickupOrderTemp.setPickupStreetAddressLine1(rsPickupOrder.getString("pickup_street_address_1"));
				pickupOrderTemp.setPickupStreetAddressLine2(rsPickupOrder.getString("pickup_street_address_2"));
				pickupOrderTemp.setPickupKecamatan(rsPickupOrder.getString("pickup_kecamatan"));
				pickupOrderTemp.setPickupKelurahan(rsPickupOrder.getString("pickup_kelurahan"));
				pickupOrderTemp.setPickupCity(rsPickupOrder.getString("pickup_city"));
				pickupOrderTemp.setPickupState(rsPickupOrder.getString("pickup_state"));
				pickupOrderTemp.setPickupPostalCode(rsPickupOrder.getString("pickup_postal_code"));
				pickupOrderTemp.setMerchantStreetAddressLine1(rsPickupOrder.getString("merchant_street_address_1"));
				pickupOrderTemp.setMerchantStreetAddressLine2(rsPickupOrder.getString("merchant_street_address_2"));
				pickupOrderTemp.setMerchantCity(rsPickupOrder.getString("merchant_city"));
				pickupOrderTemp.setMerchantState(rsPickupOrder.getString("merchant_state"));
				pickupOrderTemp.setAirwayBillNumber(airwayBillTransaction.getAirwayBillNo());
				
				pickupOrderTemp.setAirwayBillTransaction(airwayBillTransaction);
				
				
				Calendar puDateTimeCal = new GregorianCalendar();
				puDateTimeCal.setTimeInMillis((rsPickupOrder.getTimestamp("pickup_date_time")).getTime());

				PickupOrder pickupOrder = new PickupOrder();
				
				
				_log.info("item to be processed WCS OrderId:" +pickupOrderTemp.getWcsOrderId()+ " WCS OrderItemId:" + pickupOrderTemp.getWcsOrderItemId());

				pickupOrder.setCustomerName(pickupOrderTemp.getCustomerName());
				pickupOrder.setDescription(pickupOrderTemp.getWcsProductName());
//				pickupOrder.setDistributionCartSeq(pickupOrderTemp.getDcSequence().toString());
				pickupOrder.setGiftCard(pickupOrderTemp.getGiftCardNote());
				pickupOrder.setGiftWrap(pickupOrderTemp.getGiftWrapFlag()!=null ? "Y" : "T");

				// Calculate the sum insured if the item is insured
				if (pickupOrderTemp.getInsuranceCost().compareTo(new BigDecimal(0)) > 0) {
					Double insuredAmount = (pickupOrderTemp.getItemQuantity() * pickupOrderTemp.getAirwayBillTransaction().getHargaProduk()) + pickupOrderTemp.getShippingCost().doubleValue() + (pickupOrderTemp.getGiftWrapPrice()==null?Double.valueOf("0"):pickupOrderTemp.getGiftWrapPrice().doubleValue()) + (pickupOrderTemp.getAirwayBillTransaction().getFixedPrice()==null?Double.valueOf("0"):pickupOrderTemp.getAirwayBillTransaction().getFixedPrice().doubleValue());
//						Double totalSumInsured = pickupOrderTemp.getTotal().doubleValue() + pickupOrderTemp.getGiftWrapPrice().doubleValue() + pickupOrderTemp.getShippingCost().doubleValue();
//						Double cartSumInsured = pickupOrderTemp.getCartQuantity().doubleValue() / pickupOrderTemp.getItemQuantity().doubleValue() * totalSumInsured;
					pickupOrder.setInsuredAmount(insuredAmount);
				} else {
					pickupOrder.setInsuredAmount(new Double(0));
				}

				pickupOrder.setIsInsured(pickupOrderTemp.getInsuranceCost().compareTo(new BigDecimal(0)) > 0 ? true : false);
				pickupOrder.setLogisticProviderName(newLogisticProvider);

				pickupOrder.setGdnRefNo(airwayBillTransaction.getGdnRef());
				pickupOrder.setOrderOrRMA(pickupOrderTemp.getRmaFlag() ? "R" : "O");
				pickupOrder.setOrderId(pickupOrderTemp.getWcsOrderId());
				pickupOrder.setOrderItemId(pickupOrderTemp.getWcsOrderItemId());
				pickupOrder.setQuantity(pickupOrderTemp.getItemQuantity());

				pickupOrder.setMerchantName(pickupOrderTemp.getMerchantName());
				pickupOrder.setMerchantPhone(pickupOrderTemp.getMerchantPicPhone());
				pickupOrder.setMerchantAddressLine1(pickupOrderTemp.getMerchantStreetAddressLine1());
				pickupOrder.setMerchantAddressLine2(pickupOrderTemp.getMerchantStreetAddressLine2());
				pickupOrder.setMerchantCity(pickupOrderTemp.getMerchantCity());
				pickupOrder.setMerchantProvince(pickupOrderTemp.getMerchantState());
				pickupOrder.setPickupDateTime(pickupOrderTemp.getPickupDateTime());
				
				pickupOrder.setPicName(pickupOrderTemp.getMerchantPic());
				pickupOrder.setPicPhone(pickupOrderTemp.getMerchantPicPhone());
				pickupOrder.setPickupPointAddressLine1(pickupOrderTemp.getPickupStreetAddressLine1());
				pickupOrder.setPickupPointAddressLine2(pickupOrderTemp.getPickupStreetAddressLine2());
				pickupOrder.setPickupPointCity(pickupOrderTemp.getPickupCity());
				pickupOrder.setPickupPointKecamatan(pickupOrderTemp.getPickupKecamatan());
				pickupOrder.setPickupPointKelurahan(pickupOrderTemp.getPickupKelurahan());
				pickupOrder.setPickupPointProvince(pickupOrderTemp.getPickupState());
				pickupOrder.setPickupPointZipCode(pickupOrderTemp.getPickupPostalCode());
				psShippingAddressList = conn.prepareStatement(SHIPPING_ADDRESS_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psShippingAddressList.setLong(1, new Long(pickupOrderTemp.getOrderItemId()));
				rsShippingAddressList = psShippingAddressList.executeQuery();
				
				rsShippingAddressList.last();
				int totalShippingAddressList = rsShippingAddressList.getRow();
				rsShippingAddressList.beforeFirst();
				
				if(totalShippingAddressList>0){
					rsShippingAddressList.beforeFirst();
					rsShippingAddressList.next();
					
					pickupOrder.setRecipientAddress(pickupOrderTemp.getAirwayBillTransaction().getAlamatPenerima1());
					pickupOrder.setRecipientCity(pickupOrderTemp.getAirwayBillTransaction().getKotaKabupatenPenerima());
					pickupOrder.setRecipientKecamatan(pickupOrderTemp.getAirwayBillTransaction().getKelurahanKecamatanPenerima());
					pickupOrder.setRecipientKelurahan(pickupOrderTemp.getAirwayBillTransaction().getKelurahanKecamatanPenerima());
					pickupOrder.setRecipientProvince(pickupOrderTemp.getAirwayBillTransaction().getPropinsiPenerima());
					pickupOrder.setRecipientZipCode(pickupOrderTemp.getAirwayBillTransaction().getKodeposPenerima());
					
				}
				
				psRecipientContactDetailList = conn.prepareStatement(RECIPIENT_CONTACT_DETAIL_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psRecipientContactDetailList.setLong(1, new Long(pickupOrderTemp.getOrderItemId()));
				rsRecipientContactDetailList = psRecipientContactDetailList.executeQuery();
				
				rsRecipientContactDetailList.last();
				int totalRecipientContactDetailList = rsRecipientContactDetailList.getRow();
				rsRecipientContactDetailList.beforeFirst();
				
				String recipientMobile = "";
				String recipientPhone = "";
				if(totalRecipientContactDetailList>0){
					rsRecipientContactDetailList.beforeFirst();
					while (rsRecipientContactDetailList.next()) {
						if (rsRecipientContactDetailList.getInt("contact_detail_type_id")==VeniceConstants.VEN_CONTACT_TYPE_MOBILE) {
							recipientMobile = rsRecipientContactDetailList.getString("contact_detail");
						}
						if (rsRecipientContactDetailList.getInt("contact_detail_type_id")==VeniceConstants.VEN_CONTACT_TYPE_PHONE) {
							recipientPhone = rsRecipientContactDetailList.getString("contact_detail");
						}					
					}
				}
				pickupOrder.setRecipientMobile(pickupOrderTemp.getAirwayBillTransaction().getNoHPPicPenerima());
				pickupOrder.setRecipientPhone(recipientPhone);
				pickupOrder.setRecipientName(pickupOrderTemp.getRecipientName());
				
				//get weight to determine if pickup using car or motorcycle
				//if weight >= 5 kg then use car, else use motorcycle
				//get the weight per distribution cart / per gdn reference		
				//TODO: get weight from awb engine
				BigDecimal weight = new BigDecimal(pickupOrderTemp.getAirwayBillTransaction().getBeratCetak());
				if(weight.compareTo(new BigDecimal(5))>0){
					pickupOrder.setPickupMethod("Mobil");
				}else{
					pickupOrder.setPickupMethod("Motor");
				}						

				psSenderContactDetailList = conn.prepareStatement(SENDER_CONTACT_DETAIL_LIST_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psSenderContactDetailList.setLong(1, new Long(pickupOrderTemp.getOrderId()));
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
				
				pickupOrder.setAirwayBillNo(pickupOrderTemp.getAirwayBillNumber());
				
				// Map the service type codes
				if (pickupOrderTemp.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_NCS_REG)) {
					pickupOrder.setServiceType("REGULER");
				} else if (pickupOrderTemp.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_NCS_EXP)) {
					pickupOrder.setServiceType("KIRIMAN 1 HARI");
				} else if (pickupOrderTemp.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_RPX_REG)) {
					pickupOrder.setServiceType("EP");
				} else if (pickupOrderTemp.getServiceCode().equals(VeniceConstants.LOG_LOGISTICS_SERVICE_CODE_RPX_EXP)) {
					pickupOrder.setServiceType("PP");
				}

				pickupOrder.setSkuId(pickupOrderTemp.getWcsProductSku());
				
				//Default to the special handling from WCS originally
				if(pickupOrderTemp.getItemspecialHandling() != null && !pickupOrderTemp.getItemspecialHandling().isEmpty()){
					pickupOrder.setSpecialHandlingInstruction(pickupOrderTemp.getItemspecialHandling());
				}
				
				/*
				 * If the instructions are still null 
				 * then set to the instruction sent from MTA if not null
				 */
				if ((pickupOrder.getSpecialHandlingInstruction() == null || pickupOrder.getSpecialHandlingInstruction().isEmpty())
						&& pickupOrderTemp.getPickupSpecialHandling() != null && !pickupOrderTemp.getPickupSpecialHandling().isEmpty()) {
					pickupOrder.setSpecialHandlingInstruction(pickupOrderTemp.getPickupSpecialHandling());
				}

				// Add the pickup order to the list
				pickupOrderList.add(pickupOrder);

				
					
				
			}else{
				_log.info("query returned 0 rows");
			}
			
			
		} catch (SQLException e) {
			
			_log.error(e.getMessage(), e);
			
		}finally{
			try {
				
				if(rsPickupOrder!=null) rsPickupOrder.close();
				if(psPickupOrder!=null) psPickupOrder.close();
				if(psShippingAddressList!=null) psShippingAddressList.close();
				if(rsShippingAddressList!=null) rsShippingAddressList.close();
				if(psRecipientContactDetailList!=null) psRecipientContactDetailList.close();
				if(rsRecipientContactDetailList!=null) rsRecipientContactDetailList.close();
				if(psSenderContactDetailList!=null) psSenderContactDetailList.close();
				if(rsSenderContactDetailList!=null) rsSenderContactDetailList.close();
				
				conn.close();
				conn = null;
			} catch (SQLException e) {
				_log.error("Fail closing database connection", e);
			}
		}
    	
    	
		
		return pickupOrderList;
		
    }

}
