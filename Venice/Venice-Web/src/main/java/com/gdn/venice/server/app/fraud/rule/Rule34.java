package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule34;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderItemAddress;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

/**
 * Class for calculate fraud rule 34: IP geolocation information
 * 
 * @author Roland
 */

public class Rule34 {
	protected static Logger _log = null;
    
    public Rule34() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule34");
    }
    
	public int execute(Long orderId, String ipAddress) throws Exception{
		_log.info("Start execute rule 34");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule34> parameterList =null;
			List<VenOrderAddress> customerAddressList =null;
			List<VenOrderItemAddress> shippingAddressList =null;
			List<VenOrderPaymentAllocation> paymentAllocationList =null;
			String parameterCity=null, customerCity=null, shippingCity=null, billingCity=null;
			locator = new Locator<Object>();
			FrdParameterRule34SessionEJBRemote rule34SessionHome = (FrdParameterRule34SessionEJBRemote) locator.lookup(FrdParameterRule34SessionEJBRemote.class, "FrdParameterRule34SessionEJBBean");
			VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
			VenOrderItemAddressSessionEJBRemote orderItemAddressSessionHome = (VenOrderItemAddressSessionEJBRemote) locator.lookup(VenOrderItemAddressSessionEJBRemote.class, "VenOrderItemAddressSessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			
			parameterList=rule34SessionHome.queryByRange("select o from FrdParameterRule34 o where o.ipAddress = '"+ipAddress+"'" , 0, 1);
			if(parameterList.size()>0){
				parameterCity=parameterList.get(0).getCityName().toUpperCase();
				_log.info("parameterCity: "+parameterCity);
				
				customerAddressList=orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId="+orderId, 0, 1);
				if(customerAddressList.size()>0){
					customerCity=customerAddressList.get(0).getVenAddress().getVenCity()!=null?customerAddressList.get(0).getVenAddress().getVenCity().getCityName().toUpperCase():"";
					_log.info("customerCity: "+customerCity);
				}
				
				shippingAddressList=orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.venOrder.orderId="+orderId, 0, 1);
				if(shippingAddressList.size()>0){
					shippingCity=shippingAddressList.get(0).getVenAddress().getVenCity()!=null?shippingAddressList.get(0).getVenAddress().getVenCity().getCityName().toUpperCase():"";
					_log.info("shippingCity: "+shippingCity);
				}
				
				paymentAllocationList=orderPaymentAllocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+orderId, 0, 1);
				if(paymentAllocationList.size()>0){
					billingCity=paymentAllocationList.get(0).getVenOrderPayment().getVenAddress().getVenCity()!=null?paymentAllocationList.get(0).getVenOrderPayment().getVenAddress().getVenCity().getCityName().toUpperCase():"";
					_log.info("billingCity: "+billingCity);
				}
				
				int count=0;
				if(customerCity!=null && customerCity.contains(parameterCity)){
					count+=1;
				}
				if(shippingCity!=null && shippingCity.contains(parameterCity)){
					count+=1;
				}
				if(billingCity!=null && billingCity.contains(parameterCity)){
					count+=1;
				}
				
				if(count==0){
					fraudPoint=10;
				}else if(count>0 && count <3){
					fraudPoint=5;
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
		_log.info("Done execute rule 34, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
