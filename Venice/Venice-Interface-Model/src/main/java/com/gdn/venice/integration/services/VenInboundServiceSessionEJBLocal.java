package com.gdn.venice.integration.services;
import javax.ejb.Local;
import com.gdn.integration.jaxb.Order;
import com.gdn.integration.jaxb.MerchantProduct;

/**
 * VenInboundServiceSessionEJBLocal.java
 * 
 * Session Bean local interface for inbound integration transactions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Local
public interface VenInboundServiceSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#createOrder
	 * (com.gdn.integration.jaxb.Order)
	 */
	public Boolean createOrder(Order order);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#createOrderVAPayment
	 * (com.gdn.integration.jaxb.Order)
	 */
	public Boolean createOrderVAPayment(Order order);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#updateOrder
	 * (com.gdn.integration.jaxb.Order)
	 */
	public Boolean updateOrder(Order order);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#updateOrderStatus
	 * (com.gdn.integration.jaxb.Order)
	 */
	public Boolean updateOrderStatus(Order order);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#updateOrderItemStatus
	 * (com.gdn.integration.jaxb.Order)
	 */
	public Boolean updateOrderItemStatus(Order order);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.integration.services.VenInboundServiceSessionEJBRemote#notifyProductStockLevel
	 * (com.gdn.integration.jaxb.MerchantProduct)
	 */
	public Boolean notifyProductStockLevel(MerchantProduct merchantProduct);

}
