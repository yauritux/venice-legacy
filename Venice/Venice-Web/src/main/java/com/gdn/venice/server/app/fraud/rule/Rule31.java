package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule31;
/**
 * Class for calculate fraud rule 31: List Genuine Transaction by BCA
 * 
 * @author Arifin
 */
public class Rule31 {
	protected static Logger _log = null;
    
    public Rule31() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule31");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 31");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
		    FrdParameterRule31SessionEJBRemote rule31SessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
			/*
			 * cek apakah customer dalam order ini memiliki no creditcard sesuai dengan tabel List Genuine atau tidak, jika ada maka return 1 yang menandakan pengurangan risk point		
			 */
		    List<FrdParameterRule31> frdParameterRule31List=rule31SessionHome.queryByRange("select a from FrdParameterRule31 a where a.email in (select b.customerUserName from VenCustomer b where b.customerId in (select c.venCustomer.customerId from VenOrder c where c.orderId="+orderId+" )) " +
		    		"and a.noCc in (select o.venOrderPayment.maskedCreditCardNumber from VenOrderPaymentAllocation o where o.venOrder.orderId="+orderId+")", 0, 0);
				
				if(frdParameterRule31List.size()>0){					
					fraudPoint=1;					
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
		_log.info("Done execute rule 31, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}