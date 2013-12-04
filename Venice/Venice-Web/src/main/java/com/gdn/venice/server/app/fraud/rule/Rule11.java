package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule11;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAddress;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

/**
 * Class for calculate fraud rule 11: Multiple transactions on one card or a similar card with a single billing address, but multiple shipping addresses
 * 
 * @author Roland
 */

public class Rule11 {
	protected static Logger _log = null;
    
    public Rule11() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule11");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 11");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		String shippingAddressCode="";
		String billingAddressCode="";
		
		try{
			locator = new Locator<Object>();
			VenOrderItemSessionEJBRemote itemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote paymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			FrdParameterRule11SessionEJBRemote rule11SessionHome = (FrdParameterRule11SessionEJBRemote) locator.lookup(FrdParameterRule11SessionEJBRemote.class, "FrdParameterRule11SessionEJBBean");
			VenOrderItemAddressSessionEJBRemote orderItemAddressSessionHome = (VenOrderItemAddressSessionEJBRemote) locator.lookup(VenOrderItemAddressSessionEJBRemote.class, "VenOrderItemAddressSessionEJBBean");							
			
			//get the order item list
			List<VenOrderItem> itemList = itemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			String streetAddress1="";
			String postalCode="";		
			if(itemList.size()>0){
				String shippingAddress = null;
				String shippingAddress2 = null;
				for(int i=0;i<itemList.size();i++){
					List<VenOrderItemAddress> itemAddressList = orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.orderItemId="+itemList.get(i).getOrderItemId(), 0, 1);
					//get shipping address and compare
					if(itemAddressList.size()>0){
						streetAddress1=itemAddressList.get(0).getVenAddress()!=null?(itemAddressList.get(0).getVenAddress().getStreetAddress1()!=null?itemAddressList.get(0).getVenAddress().getStreetAddress1().trim():""):"";
						postalCode=itemAddressList.get(0).getVenAddress()!=null?(itemAddressList.get(0).getVenAddress().getPostalCode()!=null?itemAddressList.get(0).getVenAddress().getPostalCode().trim():""):"";
						shippingAddress=streetAddress1+postalCode;
						for(int j=i+1;j<itemList.size();j++){
							List<VenOrderItemAddress> itemAddressList2 = orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.orderItemId="+itemList.get(j).getOrderItemId(), 0, 1);							
							if(itemAddressList2.size()>0){
								 streetAddress1=itemAddressList2.get(0).getVenAddress()!=null?(itemAddressList2.get(0).getVenAddress().getStreetAddress1()!=null?itemAddressList2.get(0).getVenAddress().getStreetAddress1().trim():""):"";
								 postalCode=itemAddressList2.get(0).getVenAddress()!=null?(itemAddressList2.get(0).getVenAddress().getPostalCode()!=null?itemAddressList2.get(0).getVenAddress().getPostalCode().trim():""):"";
								shippingAddress2=streetAddress1+postalCode;
								if(!shippingAddress.equalsIgnoreCase(shippingAddress2)){
									shippingAddressCode="N";
									break;
								}
							}
						}
					}
				}
				if(!shippingAddressCode.equals("N")){
					shippingAddressCode="1";
				}						
			}
			
			//get the payment list
			List<VenOrderPaymentAllocation> paymentAllocationList = paymentAllocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			if(paymentAllocationList.size()>0){
				String billingAddress = null;
				for(int i=0;i<paymentAllocationList.size();i++){
					//get billing address and compare
					streetAddress1=paymentAllocationList.get(i).getVenOrderPayment().getVenAddress()!=null?(paymentAllocationList.get(i).getVenOrderPayment().getVenAddress().getStreetAddress1()!=null?paymentAllocationList.get(i).getVenOrderPayment().getVenAddress().getStreetAddress1().trim():""):"";
					postalCode=paymentAllocationList.get(i).getVenOrderPayment().getVenAddress()!=null?(paymentAllocationList.get(i).getVenOrderPayment().getVenAddress().getPostalCode()!=null?paymentAllocationList.get(i).getVenOrderPayment().getVenAddress().getPostalCode().trim():""):"";
					billingAddress=streetAddress1+postalCode;
					for(int j=i+1;j<paymentAllocationList.size();j++){
						streetAddress1=paymentAllocationList.get(j).getVenOrderPayment().getVenAddress()!=null?(paymentAllocationList.get(j).getVenOrderPayment().getVenAddress().getStreetAddress1()!=null?paymentAllocationList.get(j).getVenOrderPayment().getVenAddress().getStreetAddress1().trim():""):"";
						postalCode=paymentAllocationList.get(j).getVenOrderPayment().getVenAddress()!=null?(paymentAllocationList.get(j).getVenOrderPayment().getVenAddress().getPostalCode()!=null?paymentAllocationList.get(j).getVenOrderPayment().getVenAddress().getPostalCode().trim():""):"";
						if(!billingAddress.equalsIgnoreCase(streetAddress1+postalCode)){
							billingAddressCode="D";									
							break;
						}
					}
				}
				if(!billingAddressCode.equals("D")){
					billingAddressCode="S";
				}						
			}
				
			List<FrdParameterRule11> parameterList = rule11SessionHome.queryByRange("select o from FrdParameterRule11 o where o.code ='"+shippingAddressCode+billingAddressCode+"'", 0, 1);	
			//get the matching code from parameter and set the risk point
			if(parameterList.size()>0){
				fraudPoint=parameterList.get(0).getRiskPoint();
				_log.info("code match: "+shippingAddressCode+billingAddressCode);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception();
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		_log.info("Done execute rule 11, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
