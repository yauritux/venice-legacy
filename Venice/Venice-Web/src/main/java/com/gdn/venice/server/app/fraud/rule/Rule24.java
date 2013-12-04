package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule24;
import com.gdn.venice.persistence.VenOrder;

/**
 * Class for calculate fraud rule 24: Total Order Amount 
 * 
 * @author Arifin
 */

public class Rule24 {
	protected static Logger _log = null;
    
    public Rule24() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule24");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 24");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			FrdParameterRule24SessionEJBRemote rule24SessionHome = (FrdParameterRule24SessionEJBRemote) locator.lookup(FrdParameterRule24SessionEJBRemote.class, "FrdParameterRule24SessionEJBBean");
				/*
				 * cek amount order dan dapatkan risk point sesuai amount Order
				 */
				List<FrdParameterRule24> frdParameterRule24List = rule24SessionHome.queryByRange("select o from FrdParameterRule24 o where ("+venOrder.getAmount()+" between o.minValue and o.maxValue and o.maxValue>0) or ("+venOrder.getAmount()+" > o.minValue and o.maxValue=0)",0, 0);
				if(frdParameterRule24List.size()>0){
					fraudPoint=frdParameterRule24List.get(0).getRiskPoint();
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
		_log.info("Done execute rule 24, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
