package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule40;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;

/**
 * Class for calculate fraud rule 40: Handphone area vs customer location
 * 
 * @author Roland
 */

public class Rule40 {
	protected static Logger _log = null;
    
    public Rule40() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule40");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 40");
		int fraudPoint=5;	
		Locator<Object> locator = null;
		
		try{
			List<FrdParameterRule40> parameterList =null;
			List<VenOrderContactDetail> contactList =null;
			List<VenOrderAddress> addressList =null;
			String noHp=null;
			locator = new Locator<Object>();
			FrdParameterRule40SessionEJBRemote rule40SessionHome = (FrdParameterRule40SessionEJBRemote) locator.lookup(FrdParameterRule40SessionEJBRemote.class, "FrdParameterRule40SessionEJBBean");
			VenOrderContactDetailSessionEJBRemote contactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
			VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
						
			addressList=orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId="+orderId, 0, 1);
			contactList = contactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venContactDetail.venContactDetailType.contactDetailTypeId=1 and o.venOrder.orderId="+orderId, 0, 0);
			if(addressList.size()>0){
				if(addressList.get(0).getVenAddress().getVenCity()!=null){
					String cityName=addressList.get(0).getVenAddress().getVenCity().getCityName().toUpperCase();
					System.out.println("city name: "+cityName);
					if(contactList.size()>0){
						for(int i=0;i<contactList.size();i++){
							noHp=contactList.get(i).getVenContactDetail().getContactDetail();
							if(noHp.length()>7){
								noHp=noHp.substring(0, 7);
								System.out.println("no hp after substring: "+noHp);
								parameterList = rule40SessionHome.queryByRange("select o from FrdParameterRule40 o where o.noHp = '"+noHp+"'" , 0, 0);
								if(parameterList.size()>0){	
									for(int j=0;j<parameterList.size();j++){
										if(cityName.contains(parameterList.get(j).getCityName().toUpperCase())){
											System.out.println("city match: "+parameterList.get(0).getCityName());
											fraudPoint=0;
											break;
										}
									}
								}
							}
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
		_log.info("Done execute rule 40, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
