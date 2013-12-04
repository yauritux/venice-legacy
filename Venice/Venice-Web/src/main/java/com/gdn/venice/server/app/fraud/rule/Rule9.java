package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule9;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAddress;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

/**
 * Class for calculate fraud rule 9: Shipping to a single address, but transactions placed on multiple cards
 * 
 * @author Roland
 */

public class Rule9 {
	protected static Logger _log = null;
    
    public Rule9() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule9");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 9");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		String shippingAddressCode="";
		String paymentCode="";
		
		try{
			locator = new Locator<Object>();
			VenOrderItemSessionEJBRemote itemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			FrdParameterRule9SessionEJBRemote rule9SessionHome = (FrdParameterRule9SessionEJBRemote) locator.lookup(FrdParameterRule9SessionEJBRemote.class, "FrdParameterRule9SessionEJBBean");
			VenOrderItemAddressSessionEJBRemote orderItemAddressSessionHome = (VenOrderItemAddressSessionEJBRemote) locator.lookup(VenOrderItemAddressSessionEJBRemote.class, "VenOrderItemAddressSessionEJBBean");
				
			//get the order item list
			List<VenOrderItem> itemList = itemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			
			if(itemList.size()>0){
				String shippingAddress = null;
				String shippingAddress2 = null;
				String streetAddress1="";
				String postalCode="";
				for(int i=0;i<itemList.size();i++){
					List<VenOrderItemAddress> itemAddressList = orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.orderItemId="+itemList.get(i).getOrderItemId(), 0, 1);
					//get shipping address and compare
					if(itemAddressList.size()>0){
						 streetAddress1=itemAddressList.get(0).getVenAddress().getStreetAddress1()!=null?itemAddressList.get(0).getVenAddress().getStreetAddress1().toString().toUpperCase():"";
						 postalCode=itemAddressList.get(0).getVenAddress().getPostalCode()!=null?itemAddressList.get(0).getVenAddress().getPostalCode():"";
						 shippingAddress=streetAddress1.trim()+postalCode.trim();
						for(int j=i+1;j<itemList.size();j++){
							List<VenOrderItemAddress> itemAddressList2 = orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.orderItemId="+itemList.get(j).getOrderItemId(), 0, 1);							
							if(itemAddressList2.size()>0){
								 streetAddress1=itemAddressList2.get(0).getVenAddress().getStreetAddress1()!=null?itemAddressList2.get(0).getVenAddress().getStreetAddress1().toString().toUpperCase():"";
								 postalCode=itemAddressList2.get(0).getVenAddress().getPostalCode()!=null?itemAddressList2.get(0).getVenAddress().getPostalCode():"";							
								shippingAddress2=streetAddress1.trim()+postalCode.trim();
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
			
			//get the payment allocation list and compare
			List<VenOrderPaymentAllocation> allocationList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			if(allocationList.size()>1){
				paymentCode="N";				
			}else{
				paymentCode="1";
			}
			
			List<FrdParameterRule9> parameterList = rule9SessionHome.queryByRange("select o from FrdParameterRule9 o where o.code='"+shippingAddressCode+paymentCode+"'", 0, 1);				
			if(parameterList.size()>0){			
				fraudPoint=parameterList.get(0).getRiskPoint();
				_log.info("code match: "+shippingAddressCode+paymentCode);
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
	_log.info("Done execute rule 9, fraudPoint is: "+fraudPoint);
	return fraudPoint;
	}
}
