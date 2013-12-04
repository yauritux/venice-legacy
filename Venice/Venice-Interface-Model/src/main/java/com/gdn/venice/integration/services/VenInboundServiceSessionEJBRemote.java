package com.gdn.venice.integration.services;
import javax.ejb.Remote;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebMethod;

import com.gdn.integration.jaxb.MerchantProduct;
import com.gdn.integration.jaxb.Order;

/**
 * VenInboundServiceSessionEJBRemote.java
 * 
 * Session Bean remote interface for inbound integration transactions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Remote
@WebService(name = "VenInboundServiceSessionEJBPortType", targetNamespace = "http://integration.venice.gdn.com/services")
public interface VenInboundServiceSessionEJBRemote {

	/**
	 * Create an order
	 * @param order
	 * @return true if the operation succeeds else false
	 */
	@WebMethod
	public Boolean createOrder(
		    @WebParam(name = "order", targetNamespace = "http://integration.venice.gdn.com/services")  
		    Order order);
	
	/**
	 * Create an OrderVAPayment
	 * @param order
	 * @return true if the operation succeeds else false
	 */
	@WebMethod
	public Boolean createOrderVAPayment(
		    @WebParam(name = "order", targetNamespace = "http://integration.venice.gdn.com/services")  
			Order order);

	/**
	 * Update an order
	 * @param order
	 * @return true if the operation succeeds else false
	 */
	@WebMethod
	public Boolean updateOrder(
		    @WebParam(name = "order", targetNamespace = "http://integration.venice.gdn.com/services")  
			Order order);
	
	/**
	 * Update the status of an order
	 * @param order
	 * @return true if the operation succeeds else false
	 */
	@WebMethod
	public Boolean updateOrderStatus(
		    @WebParam(name = "order", targetNamespace = "http://integration.venice.gdn.com/services")  
			Order order);
	
	/**
	 * Update the status of an order item
	 * @param order
	 * @return true if the operation succeeds else false
	 */
	@WebMethod
	public Boolean updateOrderItemStatus(
		    @WebParam(name = "order", targetNamespace = "http://integration.venice.gdn.com/services")  
			Order order);
	
	/**
	 * Process the notifyProductStockLevel message by adding the product
	 * to the database if it doesn't exist
	 * @param merchantProduct
	 * @return true if the operation succeeds else false
	 */
	@WebMethod
	public Boolean notifyProductStockLevel(
		    @WebParam(name = "merchantProduct", targetNamespace = "http://integration.venice.gdn.com/services")  
			MerchantProduct merchantProduct);

}
