package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule1;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.util.Util;

/**
 * Class for calculate fraud rule 1: First time shopper
 * 
 * @author Roland
 */

public class Rule1 {
	protected static Logger _log = null;
    
    public Rule1() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule1");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 1");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			FrdParameterRule1SessionEJBRemote rule1SessionHome = (FrdParameterRule1SessionEJBRemote) locator.lookup(FrdParameterRule1SessionEJBRemote.class, "FrdParameterRule1SessionEJBBean");												
			String type="";
			if(Util.isNull(venOrder.getVenCustomer().getUserType(), "").equals("R")){
				type="Registered shopper";
			}else{
				type="Unregistered shopper";
			}
			
			List<FrdParameterRule1> parameterList = rule1SessionHome.queryByRange("select o from FrdParameterRule1 o where o.customerType='"+type+"'", 0, 1);			
			if(parameterList.size()>0){
				fraudPoint=parameterList.get(0).getRiskPoint();
				_log.info("customer type match: "+type);
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
		_log.info("Done execute rule 1, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
