package com.gdn.venice.server.app.fraud.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.fraud.FraudRuleWithNativeQueryRemote;
import com.gdn.venice.persistence.FrdParameterRule7;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.util.VeniceConstants;

/**
 * Class for calculate fraud rule 7: Transactions with similar account number
 * 
 * @author Roland
 */

public class Rule7 {
	protected static Logger _log = null;
    
    public Rule7() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule7");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 7");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			FrdParameterRule7SessionEJBRemote rule7SessionHome = (FrdParameterRule7SessionEJBRemote) locator.lookup(FrdParameterRule7SessionEJBRemote.class, "FrdParameterRule7SessionEJBBean");
			FraudRuleWithNativeQueryRemote rule7NativeSessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
									
			List<FrdParameterRule7> parameterList = rule7SessionHome.queryByRange("select o from FrdParameterRule7 o", 0, 0);				
			if(parameterList.size()>0){		
				//check payment type, if it is CC, then check rule, else no need to check	
				//get the payment allocation list
				List<VenOrderPaymentAllocation> allocationList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
				if(allocationList.size()>0){
					//check every payment in case of multiple payment
					for(int i=0;i<allocationList.size();i++){
						//check the payment type
						if(allocationList.get(i).getVenOrderPayment().getVenPaymentType().getPaymentTypeCode().equalsIgnoreCase(VeniceConstants.VEN_PAYMENT_TYPE_CC)){
							//get payment credit card number
							String creditCardNumber = null;
							creditCardNumber = allocationList.get(i).getVenOrderPayment().getMaskedCreditCardNumber();
							if(!creditCardNumber.isEmpty() || !creditCardNumber.equals(null)){								
								List<VenOrderPaymentAllocation> paymentList=null;
								Integer daySpan=0;
				                String dateStart="";
				                String dateEnd="";
								SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								for(int j=0;j<parameterList.size();j++){
									//get parameter daySpan
									daySpan=parameterList.get(j).getDaySpan();									
									//get payment date, lalu query ven order payment allocation yang CC numbernya sama dan waktu sesuai daySpan									
				                    Date toWeek = DateUtils.addDays(allocationList.get(i).getVenOrderPayment().getPaymentTimestamp(),-daySpan);   
				                    dateStart=dateTimestamp.format(toWeek.getTime());
				                    dateEnd=dateTimestamp.format(allocationList.get(i).getVenOrderPayment().getPaymentTimestamp().getTime()); 
				                    _log.debug("dateStart: "+dateStart);
				                    _log.debug("dateEnd: "+dateEnd);
				                    _log.debug("creditCardNumber: "+creditCardNumber);
									
									paymentList = rule7NativeSessionHome.queryByRangeRuleVenOrderPaymentAllocation("select * from ven_order_payment_allocation opa join ven_order_payment op on opa.order_payment_id=op.order_payment_id where opa.order_id<>"+venOrder.getOrderId()+" and op.masked_credit_card_number like '"+creditCardNumber+"' and op.payment_timestamp between '" + dateStart + "' and '"+dateEnd+"'");
									if(paymentList.size()>0){
										fraudPoint+=parameterList.get(j).getRiskPoint();
										_log.info("CC number found, risk point: "+parameterList.get(j).getRiskPoint());
									}	
								}
							}else{
								_log.info("CC number is empty, upload migs data first");
							}
						}else{
							_log.info("payment type not CC");
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
		_log.info("Done execute rule 7, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
