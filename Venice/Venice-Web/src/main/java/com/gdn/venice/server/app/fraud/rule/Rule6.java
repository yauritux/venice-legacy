package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule6;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAddress;

/**
 * Class for calculate fraud rule 6: Shipping to an international address
 * 
 * @author Roland
 */

public class Rule6 {
	protected static Logger _log = null;
    
    public Rule6() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule6");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 6");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			List<FrdParameterRule6> parameterList =null;
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			FrdParameterRule6SessionEJBRemote rule6SessionHome = (FrdParameterRule6SessionEJBRemote) locator.lookup(FrdParameterRule6SessionEJBRemote.class, "FrdParameterRule6SessionEJBBean");
			VenOrderItemAddressSessionEJBRemote orderItemAddressSessionHome = (VenOrderItemAddressSessionEJBRemote) locator.lookup(VenOrderItemAddressSessionEJBRemote.class, "VenOrderItemAddressSessionEJBBean");
					
			//get order item, get recipient, get party address, get address, get country
			List<VenOrderItem> orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = "+venOrder.getOrderId(), 0, 0);
			if(orderItemList.size()>0){
				String countryCode="";
				
				//loop order item list, cek country di alamat setiap order item
				for(int i=0;i<orderItemList.size();i++){
					//get shipping address
					List<VenOrderItemAddress> itemAddressList = orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.orderItemId="+orderItemList.get(i).getOrderItemId(), 0, 1);
					if(itemAddressList.size()>0){		
						countryCode=itemAddressList.get(0).getVenAddress().getVenCountry().getCountryCode();
						if(!countryCode.equals("ID")){
							countryCode="INT";
						}
					}
					parameterList = rule6SessionHome.queryByRange("select o from FrdParameterRule6 o where o.shippingCountry='"+countryCode+"'", 0, 1);
					if(parameterList.size()>0){						
						fraudPoint+=parameterList.get(0).getRiskPoint();	
						_log.info("country match: "+parameterList.get(0).getDescription());
					}				
				}
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
		_log.info("Done execute rule 6, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
