package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdParameterRule23SessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule23;
import com.gdn.venice.persistence.VenContactDetail;
/**
 * Class for calculate fraud rule 23: Customer name vs Customer email
 * 
 * @author Arifin
 */
public class Rule23 {
	protected static Logger _log = null;
    
    public Rule23() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule23");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 23");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");			
			FrdParameterRule23SessionEJBRemote frdParameterRule23SessionHome = (FrdParameterRule23SessionEJBRemote) locator.lookup(FrdParameterRule23SessionEJBRemote.class, "FrdParameterRule23SessionEJBBean");			
			
			/*
			 * get data order
			 */
			List<VenContactDetail> venContactDetailList = venContactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.venContactDetailType.contactDetailTypeId="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_EMAIL+
					" and o.contactDetailId in (select a.venContactDetail.contactDetailId from VenOrderContactDetail a where a.venOrder.orderId="+orderId+")", 0, 0);
			if(venContactDetailList.size()>0){
				String results="";
				/*
				 * bandingkan email customer dengan mana customer
				 */
				for(VenContactDetail item:venContactDetailList){
					String name= item.getVenParty().getFullOrLegalName().replace(" ", "").replace(".", "").replace(",", "").replace("_", "").replace("-", "").replace("/", "").toUpperCase();
					String emailCustomer =(item.getContactDetail().split("@")[0]+"").replace(".", "").replace(",", "").replace("_", "").replace("-", "").replace("/", "").replace(" ", "").trim().toUpperCase();
					results=name.toUpperCase().equals(emailCustomer)?"full":"";
					if(name.replace(emailCustomer, "").equals("") && !results.equals("full")){
						results="full";
					}					
					if(!results.equals("full")){
						if( item.getVenParty().getFullOrLegalName().replace(".", " ").replace("_", " ").replace("-", " ").trim().contains(" ")){
							String[] names=   item.getVenParty().getFullOrLegalName().replace(".", " ").replace("_", " ").replace("-", " ").trim().split(" ");	 
							int i=0;
							for(String iname:names){
								if(emailCustomer.contains(iname.toUpperCase())){
									i++;	
									emailCustomer=emailCustomer.replace(iname.toUpperCase(), "");
								}
							}
							results=i==names.length?"full":i>0?"true":results.equals("true")?"true":"false";
						}else{
							String emails=""+item.getContactDetail().split("@")[0];	
							results=emails.toUpperCase().contains(name)?"true":results.equals("true")?"true":"false";							
						}						
					}
				}
				
				List<FrdParameterRule23> FrdParameterRule23List = frdParameterRule23SessionHome.queryByRange("select o from FrdParameterRule23 o where o.code='"+results+"'", 0, 0);				
				if(FrdParameterRule23List.size()>0){
					fraudPoint=FrdParameterRule23List.get(0).getRiskPoint();
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
		_log.info("Done execute rule 23, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
