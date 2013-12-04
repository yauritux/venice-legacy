package com.gdn.venice.server.app.fraud.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote;
import com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.fraud.FraudRuleWithNativeQueryRemote;
import com.gdn.venice.persistence.FrdParameterRule10;
import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.util.VeniceConstants;

/**
 * Class for calculate fraud rule 10: Multiple transactions on one card over a very short period of time
 * 
 * @author Roland
 */

public class Rule10 {
	protected static Logger _log = null;
    
    public Rule10() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule10");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 10");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule10> parameterList =null;
			locator = new Locator<Object>();
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			FraudRuleWithNativeQueryRemote allocationNativeSessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
			VenBinCreditLimitEstimateSessionEJBRemote binCreditLimitSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
			FrdParameterRule10SessionEJBRemote rule10SessionHome = (FrdParameterRule10SessionEJBRemote) locator.lookup(FrdParameterRule10SessionEJBRemote.class, "FrdParameterRule10SessionEJBBean");
			FrdRuleConfigTresholdSessionEJBRemote configSessionHome = (FrdRuleConfigTresholdSessionEJBRemote) locator.lookup(FrdRuleConfigTresholdSessionEJBRemote.class, "FrdRuleConfigTresholdSessionEJBBean");
									
            String dateStart="";
            String dateEnd="";
				
			//get the payment allocation list
			List<VenOrderPaymentAllocation> allocationList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+venOrder.getOrderId(), 0, 0);
			if(allocationList.size()>0){
				//get day span from config
				List<FrdRuleConfigTreshold> configList = configSessionHome.queryByRange("select o from FrdRuleConfigTreshold o where o.key = 'DAY_SPAN_FOR_FRAUD_PARAMETER10'", 0, 1);
				int daySpan=0;
				if(configList.size()>0){
					daySpan = new Integer (configList.get(0).getValue());
				}
				
				//check every payment in case of multiple payment
				for(int i=0;i<allocationList.size();i++){
					if(allocationList.get(i).getVenOrderPayment().getVenPaymentType().getPaymentTypeCode().equalsIgnoreCase(VeniceConstants.VEN_PAYMENT_TYPE_CC)){
						//get payment credit card number
						String creditCardNumber = null;
						creditCardNumber = allocationList.get(i).getVenOrderPayment().getMaskedCreditCardNumber();
						if(!creditCardNumber.isEmpty() || !creditCardNumber.equals(null)){		
							//check CC limit from BIN number
							String binNumber = creditCardNumber.substring(0, 6);									
							List<VenBinCreditLimitEstimate> binCreditLimitList = binCreditLimitSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.isActive=true and o.binNumber like '"+binNumber+"'", 0, 1);
														
							if(binCreditLimitList.size()>0){
								BigDecimal limit=binCreditLimitList.get(0).getCreditLimitEstimate();									
								
								//get payment date, lalu query ven order payment yang CC numbernya sama dan waktu sesuai daySpan
								SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");													
			                    Date toWeek = DateUtils.addDays(allocationList.get(i).getVenOrderPayment().getPaymentTimestamp(),-daySpan);   
			                    dateStart=dateTimestamp.format(toWeek.getTime());
			                    dateEnd=dateTimestamp.format(allocationList.get(i).getVenOrderPayment().getPaymentTimestamp().getTime());                    
								
								//query payment list based on CC number and day span
			                    Double paymentList=allocationNativeSessionHome.queryByRangeValue("select COALESCE(sum(op.amount),0) from ven_order_payment_allocation opa inner join ven_order_payment op on opa.order_payment_id=op.order_payment_id where op.masked_credit_card_number like '"+creditCardNumber+"' and op.payment_timestamp between '"+dateStart+"' and '"+dateEnd+"'");
			     			                    
			                    BigDecimal amount = new BigDecimal(paymentList);
								int percentage=0;								
									//calculate usage percentage
									BigDecimal usage=(amount.divide(limit, 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
									percentage=new Integer(usage.intValue());
									
									parameterList = rule10SessionHome.queryByRange("select o from FrdParameterRule10 o where "+percentage+" between o.minPercentageUsage and o.maxPercentageUsage", 0, 1);				
									if(parameterList.size()>0){				
										fraudPoint+=parameterList.get(0).getRiskPoint();
										_log.info("percentage match: "+parameterList.get(0).getDescription());				
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
		_log.info("Done execute rule 10, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
