package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule39;
import com.gdn.venice.persistence.VenOrderContactDetail;

/**
 * Class for calculate fraud rule 39: Pasca bayar
 * 
 * @author Roland
 */

public class Rule39 {
	protected static Logger _log = null;
    
    public Rule39() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule39");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 39");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule39> parameterList =null;
			List<VenOrderContactDetail> contactList =null;
			String noHp=null;
			locator = new Locator<Object>();
			FrdParameterRule39SessionEJBRemote rule39SessionHome = (FrdParameterRule39SessionEJBRemote) locator.lookup(FrdParameterRule39SessionEJBRemote.class, "FrdParameterRule39SessionEJBBean");
			VenOrderContactDetailSessionEJBRemote contactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
		
			contactList = contactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where  o.venOrder.orderId="+orderId+" and (o.venContactDetail.venContactDetailType.contactDetailTypeId="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_MOBILE+" or o.venContactDetail.venContactDetailType.contactDetailTypeId="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_PHONE+")", 0, 0);
			if(contactList.size()>0){
				for(int i=0;i<contactList.size();i++){
					noHp=contactList.get(i).getVenContactDetail().getContactDetail();
					if(noHp.length()>4){
						noHp=noHp.substring(0, 4);
						parameterList = rule39SessionHome.queryByRange("select o from FrdParameterRule39 o where o.noHp = '"+noHp+"'" , 0, 1);
						if(parameterList.size()>0){	
							_log.info("no hp/telepon match: "+parameterList.get(0).getDescription());
							fraudPoint=parameterList.get(0).getRiskPoint();
							break;
						}
					}
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
		_log.info("Done execute rule 39, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
