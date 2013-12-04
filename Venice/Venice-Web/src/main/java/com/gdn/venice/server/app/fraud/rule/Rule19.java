package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule19;
import com.gdn.venice.persistence.VenAddress;
/**
 * Class for calculate fraud rule 19: Validity of Address
 * @author Arifin
 */
public class Rule19 {
	protected static Logger _log = null;
    
    public Rule19() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule19");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 19");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			VenAddressSessionEJBRemote orderAddressSessionHome = (VenAddressSessionEJBRemote) locator.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");			
				
			FrdParameterRule19SessionEJBRemote rule19SessionHome = (FrdParameterRule19SessionEJBRemote) locator.lookup(FrdParameterRule19SessionEJBRemote.class, "FrdParameterRule19SessionEJBBean");			
			/*
			 * get data customer addreee, shipping adress dan billing address
			 */
			List<VenAddress> customerList = orderAddressSessionHome.queryByRange("select o from VenAddress o  where o.addressId in (select b.venAddress.addressId from VenOrderAddress b where b.venOrder.orderId="+orderId+")", 0, 1);	
			List<VenAddress> shippingList = orderAddressSessionHome.queryByRange("select o.streetAddress1 from VenAddress o where o.addressId in (select a.venAddress.addressId from VenOrderPayment a where a.orderPaymentId in (select b.venOrderPayment.orderPaymentId from VenOrderPaymentAllocation b where b.venOrder.orderId="+orderId+")) group by o.streetAddress1 ", 0, 0);			
			List<VenAddress> billingList = orderAddressSessionHome.queryByRange("select o.streetAddress1 from VenAddress o where o.addressId in (select a.venAddress.addressId from VenOrderItem a where a.venOrder.orderId="+orderId+") group by o.streetAddress1 ", 0, 0);
			
			 	/*
				 * compare 
				 */
			if(customerList.size()>0 && shippingList.size()>0 && billingList.size()>0){
				String results="";
				String streetAddress1=customerList.get(0).getStreetAddress1()!=null?customerList.get(0).getStreetAddress1().toString().toUpperCase():"";
				if(customerList.size()==1 && shippingList.size()==1 && billingList.size()==1){					
					results=streetAddress1.equals((shippingList.get(0)+"").toUpperCase()) && streetAddress1.equals((billingList.get(0)+"").toUpperCase())?"full":streetAddress1.equals((shippingList.get(0)+"").toUpperCase()) || streetAddress1.equals((billingList.get(0)+"").toUpperCase()) || (shippingList.get(0)+"").toUpperCase().equals((billingList.get(0)+"").toUpperCase())?"true":"false";				
				}else{
					for(int i=0;i<shippingList.size();i++){
						for(int j=0;j<billingList.size();j++){
							results=streetAddress1.equals((shippingList.get(i)+"").toUpperCase()) || streetAddress1.equals((billingList.get(0)+"").toUpperCase()) || (shippingList.get(i)+"").toUpperCase().equals((billingList.get(0)+"").toUpperCase())?"true":"false";							
							if(results.equals("true")){
								break;
							}
						}	
						if(results.equals("true")){
							break;
						}
					}
				}
				
				 List<FrdParameterRule19> frdParameterRule19List=rule19SessionHome.queryByRange("select o from FrdParameterRule19 o where o.code='"+results+"'",0,0);
				 if(frdParameterRule19List.size()>0){
					 fraudPoint=frdParameterRule19List.get(0).getRiskPoint();
					 _log.info("parameter match");
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
		_log.info("Done execute rule 19, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
