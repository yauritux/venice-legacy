package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule8;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

/**
 * Class for calculate fraud rule 8: Payment Type
 * 
 * @author Roland
 */

public class Rule8 {
	protected static Logger _log = null;
    
    public Rule8() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule8");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 8");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule8> parameterList=null;
			locator = new Locator<Object>();
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			FrdParameterRule8SessionEJBRemote rule8SessionHome = (FrdParameterRule8SessionEJBRemote) locator.lookup(FrdParameterRule8SessionEJBRemote.class, "FrdParameterRule8SessionEJBBean");
												
			//get the payment allocation list
			List<VenOrderPaymentAllocation> allocationList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			if(allocationList.size()>0){
				//check the payment type and set the matching risk point				
				for(int i=0;i<allocationList.size();i++){					
					parameterList = rule8SessionHome.queryByRange("select o from FrdParameterRule8 o where o.paymentType='"+allocationList.get(i).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode()+"'", 0, 1);
					if(parameterList.size()>0){							
						if(allocationList.get(i).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equalsIgnoreCase(parameterList.get(0).getPaymentType())){								
							fraudPoint+=parameterList.get(0).getRiskPoint();
							_log.info("payment type match: "+parameterList.get(0).getPaymentType());
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
		_log.info("Done execute rule 8, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
