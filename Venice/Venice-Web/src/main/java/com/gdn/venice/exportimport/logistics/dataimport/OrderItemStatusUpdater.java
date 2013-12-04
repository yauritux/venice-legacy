package com.gdn.venice.exportimport.logistics.dataimport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ejb.EJBException;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenRecipient;
import com.gdn.venice.util.VeniceConstants;

/**
 * Updates the status of an order item based on the reconciliation status of the
 * AWB
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class OrderItemStatusUpdater {

	protected static Logger _log = null;

	public OrderItemStatusUpdater() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataimport.servlet.OrderItemStatusUpdater");
	}

	/**
	 * Changes the status of the order item based on the status of all the
	 * distribution carts and airway bills for the order item
	 * 
	 * @param wcsOrderItemId
	 * @return
	 * @throws ServletException
	 */
	public Boolean updateOrderItemStatus(String wcsOrderItemId, Connection conn)
			throws EJBException {
		if(wcsOrderItemId == null || wcsOrderItemId.isEmpty()){
			return Boolean.TRUE;
		}
		
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			VenOrderItemSessionEJBRemote venOrderItemHome = (VenOrderItemSessionEJBRemote) locator
					.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			String orderItemSQL = "select * from ven_order_item where wcs_order_item_id = ?";
			PreparedStatement psOrderItem = conn.prepareStatement(orderItemSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			psOrderItem.setString(1, wcsOrderItemId);
			
			ResultSet rsOrderItem = psOrderItem.executeQuery();
			
			rsOrderItem.last();
			int totalOrderItem = rsOrderItem.getRow();
			rsOrderItem.beforeFirst();
			
			if(totalOrderItem == 0){
				return Boolean.TRUE;
			}

			rsOrderItem.next();
			
			VenOrderItem venOrderItem = new VenOrderItem();
			
			venOrderItem.setEtd(rsOrderItem.getInt("etd"));
			venOrderItem.setGiftCardFlag(rsOrderItem.getBoolean("gift_card_flag"));
			venOrderItem.setGiftCardNote(rsOrderItem.getString("gift_card_note"));
			venOrderItem.setGiftWrapPrice(rsOrderItem.getBigDecimal("gift_wrap_price"));
			venOrderItem.setGiftWrapFlag(rsOrderItem.getBoolean("gift_wrap_flag"));
			venOrderItem.setInsuranceCost(rsOrderItem.getBigDecimal("insurance_cost"));
			venOrderItem.setMaxEstDate(rsOrderItem.getTimestamp("max_est_date"));
			venOrderItem.setMerchantSettlementFlag(rsOrderItem.getBoolean("merchant_settlement_flag"));
			venOrderItem.setMinEstDate(rsOrderItem.getTimestamp("min_est_date"));
			venOrderItem.setOrderItemId(rsOrderItem.getLong("order_item_id"));
			venOrderItem.setPackageCount(rsOrderItem.getInt("package_count"));
			venOrderItem.setPrice(rsOrderItem.getBigDecimal("price"));
			venOrderItem.setQuantity(rsOrderItem.getInt("quantity"));
			venOrderItem.setSaltCode(rsOrderItem.getString("salt_code"));
			venOrderItem.setShippingCost(rsOrderItem.getBigDecimal("shipping_cost"));
			venOrderItem.setShippingWeight(rsOrderItem.getBigDecimal("shipping_weight"));
			venOrderItem.setSpecialHandlingInstructions(rsOrderItem.getString("special_handling_instructions"));
			venOrderItem.setTotal(rsOrderItem.getBigDecimal("total"));
			venOrderItem.setWcsOrderItemId(rsOrderItem.getString("wcs_order_item_id"));
			venOrderItem.setLogisticsPricePerKg(rsOrderItem.getBigDecimal("logistics_price_per_kg"));
			
			VenAddress shippingAddress = new VenAddress();
			shippingAddress.setAddressId(rsOrderItem.getLong("shipping_address_id"));
			venOrderItem.setVenAddress(shippingAddress);
			
			VenMerchantProduct product = new VenMerchantProduct();
			product.setProductId(rsOrderItem.getLong("product_id"));
			venOrderItem.setVenMerchantProduct(product);
			
			VenOrder order = new VenOrder();
			order.setOrderId(rsOrderItem.getLong("order_id"));
			venOrderItem.setVenOrder(order);
			
			VenOrderStatus orderStatus = new VenOrderStatus();
			orderStatus.setOrderStatusId(rsOrderItem.getLong("order_status_id"));
			venOrderItem.setVenOrderStatus(orderStatus);
			
			VenRecipient venRecipient = new VenRecipient();
			venRecipient.setRecipientId(rsOrderItem.getLong("recipient_id"));
			venOrderItem.setVenRecipient(venRecipient);
			
			LogLogisticService logisticService = new LogLogisticService();
			logisticService.setLogisticsServiceId(rsOrderItem.getLong("logistics_service_id"));
			venOrderItem.setLogLogisticService(logisticService);
			
			rsOrderItem.close();
			psOrderItem.close();

//			VenDistributionCartSessionEJBRemote venDistributionCartHome = (VenDistributionCartSessionEJBRemote) locator
//					.lookup(VenDistributionCartSessionEJBRemote.class, "VenDistributionCartSessionEJBBean");
//			ArrayList<VenDistributionCart> venDistributioncartList = (ArrayList<VenDistributionCart>) venDistributionCartHome
//					.queryByRange("select o from VenDistributionCart o where o.venOrderItem.orderItemId =" + venOrderItem.getOrderItemId(), 0, 0);
			
			String airwayBillSQL = "select status from log_airway_bill where distribution_cart_id = ?";
			PreparedStatement psAirwayBill = conn.prepareStatement(airwayBillSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			String distributionCartSQL = "select distribution_cart_id from ven_distribution_cart where order_item_id = ?";
			PreparedStatement psDistributionCart = conn.prepareStatement(distributionCartSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			psDistributionCart.setLong(1, venOrderItem.getOrderItemId());
			
			ResultSet rsDistributionCart = psDistributionCart.executeQuery();
			
			while (rsDistributionCart.next()) {
				
				psAirwayBill.setLong(1, rsDistributionCart.getLong("distribution_cart_id"));
				ResultSet rsAirwayBill = psAirwayBill.executeQuery();
				
				rsAirwayBill.last();
				int totalAirwayBill = rsAirwayBill.getRow();
				rsAirwayBill.beforeFirst();
				
				Boolean allD = true;
				Boolean allCX = true;
				Boolean allRT = true;
				
				while (rsAirwayBill.next()) {
					if (rsAirwayBill.getString("status") == null || !rsAirwayBill.getString("status").equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)) {
						allD = false;
					}
					if (rsAirwayBill.getString("status") == null || !rsAirwayBill.getString("status").equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE)) {
						allCX = false;
					}
					if (rsAirwayBill.getString("status") == null || !rsAirwayBill.getString("status").equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_DEX_14)) {
						allRT = false;
					}		
				}
				
				rsAirwayBill.close();
				rsAirwayBill = null;
				
				// Make sure that there are actually airway bills then update the status accordingly
				if (totalAirwayBill > 0) {
					if (allD) {
						// Set the status to D and merge
						VenOrderStatus venOrderStatus = new VenOrderStatus();
						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_D);
						venOrderItem.setVenOrderStatus(venOrderStatus);
						venOrderItemHome.mergeVenOrderItem(venOrderItem);
					}
					if (allCX) {
						// Set the status to CX and merge
						VenOrderStatus venOrderStatus = new VenOrderStatus();
						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
						venOrderItem.setVenOrderStatus(venOrderStatus);
						venOrderItemHome.mergeVenOrderItem(venOrderItem);
					}
					if (allRT) {
						// Set the status to RT and merge
						VenOrderStatus venOrderStatus = new VenOrderStatus();
						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_RT);
						venOrderItem.setVenOrderStatus(venOrderStatus);
						venOrderItemHome.mergeVenOrderItem(venOrderItem);
					}
				}
				
			}
			
			rsDistributionCart.close();
			psDistributionCart.close();
			
//			for (VenDistributionCart cart : venDistributioncartList) {
//				LogAirwayBillSessionEJBRemote venAirwayBillHome = (LogAirwayBillSessionEJBRemote) locator
//						.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
//				ArrayList<LogAirwayBill> logAirwayBillList = (ArrayList<LogAirwayBill>) venAirwayBillHome
//						.queryByRange("select o from LogAirwayBill o where o.venDistributionCart.distributionCartId =" + cart.getDistributionCartId(), 0, 0);
//				/*
//				 * Check the status of the airway bills and wait for all status
//				 * to be equal before changing the status of the order item
//				 * (triggers the publish)
//				 */
//				Boolean allD = true;
//				Boolean allCX = true;
//				Boolean allRT = true;
//				for (LogAirwayBill airwayBill : logAirwayBillList) {					
//					if (airwayBill.getStatus() == null || !airwayBill.getStatus().equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)) {
//						allD = false;
//					}
//					if (airwayBill.getStatus() == null || !airwayBill.getStatus().equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_MDE)) {
//						allCX = false;
//					}
//					if (airwayBill.getStatus() == null || !airwayBill.getStatus().equalsIgnoreCase(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_DEX_14)) {
//						allRT = false;
//					}					
//				}
//				// Make sure that there are actually airway bills then update the status accordingly
//				if (!logAirwayBillList.isEmpty()) {
//					if (allD) {
//						// Set the status to D and merge
//						VenOrderStatus venOrderStatus = new VenOrderStatus();
//						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_D);
//						venOrderItem.setVenOrderStatus(venOrderStatus);
//						venOrderItemHome.mergeVenOrderItem(venOrderItem);
//					}
//					if (allCX) {
//						// Set the status to CX and merge
//						VenOrderStatus venOrderStatus = new VenOrderStatus();
//						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
//						venOrderItem.setVenOrderStatus(venOrderStatus);
//						venOrderItemHome.mergeVenOrderItem(venOrderItem);
//					}
//					if (allRT) {
//						// Set the status to RT and merge
//						VenOrderStatus venOrderStatus = new VenOrderStatus();
//						venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_RT);
//						venOrderItem.setVenOrderStatus(venOrderStatus);
//						venOrderItemHome.mergeVenOrderItem(venOrderItem);
//					}
//				}
//			}
		} catch (Exception e) {
			String errMsg = "An exception occured when performing update of the order item status based on the activity report upload:" + wcsOrderItemId;
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return Boolean.TRUE;
	}
}
