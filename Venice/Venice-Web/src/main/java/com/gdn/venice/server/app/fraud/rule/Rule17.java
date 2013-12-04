package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule17;
import com.gdn.venice.persistence.VenOrder;

/**
 * Class for calculate fraud rule 17: City blacklist
 * 
 * @author Roland
 */

public class Rule17 {
	protected static Logger _log = null;
    
    public Rule17() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule17");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 17");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule17> parameterList =null;
			locator = new Locator<Object>();
			FrdParameterRule17SessionEJBRemote rule17SessionHome = (FrdParameterRule17SessionEJBRemote) locator.lookup(FrdParameterRule17SessionEJBRemote.class, "FrdParameterRule17SessionEJBBean");
	
			parameterList = rule17SessionHome.queryByRange("select sum(o.riskPoint) from FrdParameterRule17 o where UPPER(o.cityName) in "+
			"(select UPPER(a.venCity.cityName) from VenAddress a  where a.addressId in (select b.venAddress.addressId from VenOrderItem b where b.venOrder.orderId="+venOrder.getOrderId()+"))",0,0); //'"+orderItemList.get(i).getVenAddress().getVenCity().getCityName().toUpperCase()+"'", 0, 1);
			_log.debug(parameterList.size()+" "+parameterList.get(0));
			if(parameterList.size() > 0 && parameterList.get(0) != null){						
				fraudPoint=new Integer(parameterList.get(0)+"");
				_log.info("shipping city match, risk point: "+fraudPoint);
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
		_log.info("Done execute rule 17, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
