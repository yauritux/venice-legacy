package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule38SessionEJBRemote;
import com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule38;
import com.gdn.venice.persistence.VenMigsUploadMaster;

/**
 * Class for calculate fraud rule 38: MIGS History with different credit card
 *
 * @author Arifin
 */

public class Rule38 {
	protected static Logger _log = null;
    
    public Rule38() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule38");
    }
    
    public int execute(Long orderId, String wcsOrderId) throws Exception{
    	_log.info("Start execute rule 38");
        int fraudPoint=0;  
        Locator<Object> locator = null;
        List<VenMigsUploadMaster> migsUploadList = null;
        try{
            locator = new Locator<Object>();
            VenMigsUploadMasterSessionEJBRemote sessionHome = (VenMigsUploadMasterSessionEJBRemote) locator.lookup(VenMigsUploadMasterSessionEJBRemote.class, "VenMigsUploadMasterSessionEJBBean");
            FrdParameterRule38SessionEJBRemote ruleHome = (FrdParameterRule38SessionEJBRemote) locator.lookup(FrdParameterRule38SessionEJBRemote.class, "FrdParameterRule38SessionEJBBean");
                       
            migsUploadList=sessionHome.queryByRange("select o from  VenMigsUploadMaster o where o.merchantTransactionReference like '"+wcsOrderId+"-%' "+
            		"and o.cardNumber not in (select b.venOrderPayment.maskedCreditCardNumber from VenOrderPaymentAllocation b where b.venOrder.orderId="+orderId+") and o.responseCode not like '%Approved'", 0, 0);
            
            if(migsUploadList.size()>0){
  	            List<FrdParameterRule38> ruleList = ruleHome.queryByRange("select o from FrdParameterRule38 o", 0, 0);
  	          if(ruleList.size()>0){
            	fraudPoint= ruleList.get(0).getRiskPoint() * migsUploadList.size();
        		_log.info("parameter match");
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
        _log.info("Done execute rule 38, fraudPoint is: "+fraudPoint);
        return fraudPoint;
    }
}
