package com.gdn.venice.server.app.fraud.rule;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote;
import com.gdn.venice.facade.fraud.FraudRuleWithNativeQueryRemote;
import com.gdn.venice.persistence.FrdParameterRule13;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.util.VeniceConstants;

/**
 * Class for calculate fraud rule 13: IP address company
 * 
 * @author Roland
 */

public class Rule13 {
	protected static Logger _log = null;
    
    public Rule13() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule13");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 13");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
//			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			FrdParameterRule13SessionEJBRemote rule13SessionHome = (FrdParameterRule13SessionEJBRemote) locator.lookup(FrdParameterRule13SessionEJBRemote.class, "FrdParameterRule13SessionEJBBean");
			FraudRuleWithNativeQueryRemote rule13NativeSessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
			
			
			List<FrdParameterRule13> parameterList = rule13SessionHome.queryByRange("select o from FrdParameterRule13 o", 0, 0);				
			if(parameterList.size()>0){	
                String dateStart="";
                String dateEnd="";
				for(int i=0;i<parameterList.size();i++){
					SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");					
                    Date toWeek = DateUtils.addDays(venOrder.getOrderDate(),-parameterList.get(i).getTimespan());   
                    dateStart=dateTimestamp.format(toWeek.getTime());
                    dateEnd=dateTimestamp.format(venOrder.getOrderDate().getTime());     
					
					//get the payment allocation list for order with same ip, and different cc
                    List<BigDecimal> allocationList = rule13NativeSessionHome.queryByRangeValueList("select count(c.masked_credit_card_number) from ven_order_payment_allocation a "+
        					" inner join ven_order b on a.order_id=b.order_id inner join ven_order_payment c on a.order_payment_id=c.order_payment_id"+
        					" where c.payment_type_id="+VeniceConstants.VEN_PAYMENT_TYPE_ID_CC+" and b.ip_address ='"+venOrder.getIpAddress()+"' and b.ip_address is not null and c.masked_credit_card_number is not null and b.order_date between '"+dateStart+"' and '"+dateEnd+"' group by c.masked_credit_card_number ");
                        
                    _log.debug(i+" allocationList: "+allocationList.size());
					if(allocationList.size()>0){										
						if(allocationList.size()>parameterList.get(i).getMinCcNumber()){
							fraudPoint=parameterList.get(i).getRiskPoint();
							_log.info("parameter match: "+parameterList.get(i).getDescription());
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
		_log.info("Done execute rule 13, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
