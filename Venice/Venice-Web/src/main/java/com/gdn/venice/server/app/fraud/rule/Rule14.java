package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote;
import com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote;
import com.gdn.venice.persistence.FrdEmailType;
import com.gdn.venice.persistence.FrdParameterRule14;
import com.gdn.venice.persistence.VenOrder;

/**
 * Class for calculate fraud rule 14: Order from internet addresses that make use of free email services
 * 
 * @author Roland
 */

public class Rule14 {
	protected static Logger _log = null;
    
    public Rule14() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule14");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 14");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			FrdEmailTypeSessionEJBRemote emailTypeSessionHome = (FrdEmailTypeSessionEJBRemote) locator.lookup(FrdEmailTypeSessionEJBRemote.class, "FrdEmailTypeSessionEJBBean");
			FrdParameterRule14SessionEJBRemote rule14SessionHome = (FrdParameterRule14SessionEJBRemote) locator.lookup(FrdParameterRule14SessionEJBRemote.class, "FrdParameterRule14SessionEJBBean");
			
			String emailType="corporate";				
			
			//get customer email, substring to get the mail server (after @)
			String email = venOrder.getVenCustomer().getCustomerUserName();						
			String mailServer = null;						
			
			if(email!=null){
				if(email.contains("@")){
					_log.debug("party name: "+venOrder.getVenCustomer().getVenParty().getFullOrLegalName());
					String[] parts = email.split("@");
					mailServer = parts[1];
					_log.debug("mail server: "+mailServer);
				}
			}

			
			if(mailServer!=null){
				String[] parts = mailServer.split("\\.");
				mailServer=parts[0];
				_log.debug("mail server after split: "+mailServer);
				//get email type list
				List<FrdEmailType> emailTypeList = emailTypeSessionHome.queryByRange("select o from FrdEmailType o where UPPER(o.emailServerPattern)='"+mailServer.toUpperCase()+"'", 0, 1);
				if(emailTypeList.size()>0){	
					emailType=emailTypeList.get(0).getEmailType();
				}
			}
				
			//compare email type with parameter and get risk point
			List<FrdParameterRule14> parameterList = rule14SessionHome.queryByRange("select o from FrdParameterRule14 o where o.emailType='"+emailType+"'", 0, 1);
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
		_log.info("Done execute rule 14, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
