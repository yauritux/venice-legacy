package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule20;
import com.gdn.venice.persistence.VenOrder;
/**
 * Class for calculate fraud rule 20: UMR by 33 Province
 * 
 * @author Arifin
 */
public class Rule20 {
	protected static Logger _log = null;
    
    public Rule20() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule20");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 20");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			FrdParameterRule20SessionEJBRemote rule20SessionHome = (FrdParameterRule20SessionEJBRemote) locator.lookup(FrdParameterRule20SessionEJBRemote.class, "FrdParameterRule20SessionEJBBean");			
			/*
			 * get umr sesuai provinsi dari order
			 */
				 List<FrdParameterRule20> frdParameterRule20List=rule20SessionHome.queryByRange("select o from FrdParameterRule20 o where UPPER(o.provinsi) in (select UPPER(a.venState.stateName) from VenAddress a  where a.addressId in (select b.venAddress.addressId from VenOrderAddress b where b.venOrder.orderId="+venOrder.getOrderId()+") ) ",0,0);
				 if(frdParameterRule20List.size()>0){
					 /*
					  * risk point dengan rumus (amountOrder x 10) / (priceUMR X 4) 
					  */
					 _log.info("parameter match, Amount Order : "+venOrder.getAmount()+" UMR : "+frdParameterRule20List.get(0).getUmr());
					 fraudPoint=(int) Math.round((new Double(venOrder.getAmount().toString())*10)/(new Double(frdParameterRule20List.get(0).getUmr().toString())*4));
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
		_log.info("Done execute rule 20, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
