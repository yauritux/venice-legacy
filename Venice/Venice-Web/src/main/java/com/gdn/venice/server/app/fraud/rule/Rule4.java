package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule4;
import com.gdn.venice.persistence.VenOrder;

/**
 * Class for calculate fraud rule 4: Order made up of big ticket items
 * 
 * @author Roland
 */

public class Rule4 {
	protected static Logger _log = null;
    
    public Rule4() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule4");
    }
	
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 4");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			FrdParameterRule4SessionEJBRemote rule4SessionHome = (FrdParameterRule4SessionEJBRemote) locator.lookup(FrdParameterRule4SessionEJBRemote.class, "FrdParameterRule4SessionEJBBean");			
			_log.debug("amount: "+venOrder.getAmount());
			
			List<FrdParameterRule4> parameterList = rule4SessionHome.queryByRange("select o from FrdParameterRule4 o where "+venOrder.getAmount()+" between o.minValue and o.maxValue", 0, 1);			
			if(parameterList.size()>0){						
				fraudPoint=parameterList.get(0).getRiskPoint();	
				_log.info("parameter match: "+parameterList.get(0).getDescription());
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
		_log.info("Done execute rule 4, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
