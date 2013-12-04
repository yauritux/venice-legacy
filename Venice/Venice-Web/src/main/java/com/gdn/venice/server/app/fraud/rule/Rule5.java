package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule5;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;

/**
 * Class for calculate fraud rule 5: Rush or overnight shipping
 * 
 * @author Roland
 */

public class Rule5 {
	protected static Logger _log = null;
    
    public Rule5() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule5");
    }
	
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 5");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			FrdParameterRule5SessionEJBRemote rule5SessionHome = (FrdParameterRule5SessionEJBRemote) locator.lookup(FrdParameterRule5SessionEJBRemote.class, "FrdParameterRule5SessionEJBBean");									
							
			//ambil logistic provider dari item pertama saja, karena sekarang logistic provider adalah per order bukan per order item.
			List<VenOrderItem> orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = "+venOrder.getOrderId(), 0, 1);
			if(orderItemList.size()>0){
				List<FrdParameterRule5> parameterList = rule5SessionHome.queryByRange("select o from FrdParameterRule5 o where o.shippingType='"+orderItemList.get(0).getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderCode()+"'", 0, 1);
				if(parameterList.size()>0){						
					fraudPoint=parameterList.get(0).getRiskPoint();
					_log.info("shipping type match: "+parameterList.get(0).getShippingType());
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
		_log.info("Done execute rule 5, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
