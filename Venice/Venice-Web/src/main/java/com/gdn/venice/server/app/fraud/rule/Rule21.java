package com.gdn.venice.server.app.fraud.rule;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.fraud.FraudRuleWithNativeQueryRemote;
import com.gdn.venice.persistence.FrdParameterRule21;
import com.gdn.venice.persistence.VenOrder;
/**
 * Class for calculate fraud rule 21: Order timestamp Blacklist
 * 
 * @author Arifin
 */
public class Rule21 {
	protected static Logger _log = null;
    
    public Rule21() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule21");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 21");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			  FraudRuleWithNativeQueryRemote rule21SessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
					
				/*
				 * query berdasarkan time order
				 */
				SimpleDateFormat dd = new SimpleDateFormat("HH:mm:ss");
				String time=dd.format(venOrder.getOrderDate().getTime());
				 List<FrdParameterRule21> frdParameterRule21List=rule21SessionHome.queryByRangeNativeRule21("select * from Frd_Parameter_Rule_21 where '"+time+"' between min_Time and max_Time");
	
				 if(frdParameterRule21List.size()>0){
					 fraudPoint=frdParameterRule21List.get(0).getRiskPoint();
					 _log.info("parameter match");
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
		_log.info("Done execute rule 21, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
