package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.util.VeniceConstants;
/**
 * Class for calculate fraud rule 33: Eci
 * 
 * @author Arifin
 */
public class Rule33 {
	protected static Logger _log = null;
    
    public Rule33() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule33");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 33");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
				
			VenOrderPaymentAllocationSessionEJBRemote sessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");			
			/*
			 * get payment
			 */
				 List<VenOrderPaymentAllocation> sessionHomeList=sessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+venOrder.getOrderId()+" and o.venOrderPayment.venPaymentType.paymentTypeId="+VeniceConstants.VEN_PAYMENT_TYPE_ID_CC,0,0);
				 if(sessionHomeList.size()>0){
					 /*
					  * cek Eci
					  */
					 if(sessionHomeList.get(0).getVenOrderPayment()!=null){
						 if(sessionHomeList.get(0).getVenOrderPayment().getThreeDsSecurityLevelAuth()!=null){
							 if(sessionHomeList.get(0).getVenOrderPayment().getThreeDsSecurityLevelAuth().equals(VeniceConstants.E_COMMERCE_INDICATOR_5)){
									fraudPoint=-100;
								}else if(sessionHomeList.get(0).getVenOrderPayment().getThreeDsSecurityLevelAuth().equals(VeniceConstants.E_COMMERCE_INDICATOR_7)){
									fraudPoint=500;										
								}
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
		_log.info("Done execute rule 33, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
