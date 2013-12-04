package com.gdn.venice.server.app.fraud.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote;
import com.gdn.venice.facade.fraud.FraudRuleWithNativeQueryRemote;
import com.gdn.venice.persistence.FrdParameterRule22;
import com.gdn.venice.persistence.VenOrder;
/**
 * Class for calculate fraud rule 22: Customer Shopping Limit per Month
 * 
 * @author Arifin
 */
public class Rule22 {
	protected static Logger _log = null;
    
    public Rule22() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule22");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 22");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			FrdParameterRule22SessionEJBRemote rule22SessionHome = (FrdParameterRule22SessionEJBRemote) locator.lookup(FrdParameterRule22SessionEJBRemote.class, "FrdParameterRule22SessionEJBBean");		
			 FraudRuleWithNativeQueryRemote rule22NativeSessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
				
					SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					/*
					 * range 1 bulan sebelumnya 
					 * sum order amount
					 */
					Date toWeek = DateUtils.addMonths(venOrder.getOrderDate(),-1);	
					String dateEnd=dd.format(venOrder.getOrderDate().getTime());
					String dateStart=dd.format(toWeek.getTime());
					
					Double order = rule22NativeSessionHome.queryByRangeValue("select COALESCE(sum(amount),0) from ven_order where order_id='"+venOrder.getOrderId()+"' or (customer_id="+venOrder.getVenCustomer().getCustomerId()+" and order_date between '"+dateStart+"' and '"+dateEnd+"')");
					
					_log.debug(" sumAmount: "+order);
						List<FrdParameterRule22> frdParameterRule22List = rule22SessionHome.queryByRange("select o from FrdParameterRule22 o where ("+order+" between o.minValue and o.maxValue and o.maxValue>0) or ("+order+" > o.minValue and o.maxValue=0)",0, 0);
						if(frdParameterRule22List.size()>0){
							fraudPoint=frdParameterRule22List.get(0).getRiskPoint();
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
		_log.info("Done execute rule 22, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
