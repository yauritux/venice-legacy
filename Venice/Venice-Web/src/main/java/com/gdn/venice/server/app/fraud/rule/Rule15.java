package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule15SessionEJBRemote;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule15;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.util.VeniceConstants;

/**
 * Class for calculate fraud rule 15: Bin number not registered
 * 
 * @author Roland
 */

public class Rule15 {
	protected static Logger _log = null;
    
    public Rule15() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule15");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 15");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule15> parameterList=null;
			String binNumber="";
			locator = new Locator<Object>();
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			VenBinCreditLimitEstimateSessionEJBRemote binCreditLimitSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
			FrdParameterRule15SessionEJBRemote rule15SessionHome = (FrdParameterRule15SessionEJBRemote) locator.lookup(FrdParameterRule15SessionEJBRemote.class, "FrdParameterRule15SessionEJBBean");												
					
			//get the payment allocation list
			List<VenOrderPaymentAllocation> allocationList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			if(allocationList.size()>0){					
				//check every payment in case of multiple payment
				for(int i=0;i<allocationList.size();i++){
					if(allocationList.get(i).getVenOrderPayment().getVenPaymentType().getPaymentTypeCode().equalsIgnoreCase(VeniceConstants.VEN_PAYMENT_TYPE_CC)){
						//get payment credit card number
						String creditCardNumber = null;
						creditCardNumber = allocationList.get(i).getVenOrderPayment().getMaskedCreditCardNumber();
						if(!creditCardNumber.isEmpty() || !creditCardNumber.equals(null)){		
							//check CC limit from BIN number
							if(creditCardNumber.length()>6){
								binNumber = creditCardNumber.substring(0, 6);
							}							
							List<VenBinCreditLimitEstimate> binCreditLimitList = binCreditLimitSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.isActive=true and o.binNumber like '"+binNumber+"'", 0, 1);
														
							if(binCreditLimitList.isEmpty() || binCreditLimitList.size()<0){										
								//get the matching code from parameter and set the risk point
								parameterList = rule15SessionHome.queryByRange("select o from FrdParameterRule15 o where o.code='BIN-NR'", 0, 1);
								if(parameterList.size()>0){				
									fraudPoint+=parameterList.get(0).getRiskPoint();
									_log.info("parameter match: "+parameterList.get(0).getDescription());
								}
							}else{
								_log.info("bin number found, no risk point");
							}
						}else{
							_log.info("CC number is empty, upload migs data first");
						}
					}else{
						_log.info("payment type not CC");
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
		_log.info("Done execute rule 15, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
