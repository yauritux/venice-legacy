package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule25SessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule25;
import com.gdn.venice.persistence.VenOrderItem;
/**
 * Class for calculate fraud rule 25: Company Shipping Address
 * 
 * @author Arifin
 */
public class Rule25 {
	protected static Logger _log = null;
    
    public Rule25() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule25");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 25");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
		    FrdParameterRule25SessionEJBRemote rule25SessionHome = (FrdParameterRule25SessionEJBRemote) locator.lookup(FrdParameterRule25SessionEJBRemote.class, "FrdParameterRule25SessionEJBBean");
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			/*
			 * get alamat perusahaan ayng telah dideklarasikan di tabel FrdParameterRule25
			 */
			List<FrdParameterRule25> FrdParameterRule25List=rule25SessionHome.queryByRange("select o from FrdParameterRule25 o", 0, 0);
			
			if(FrdParameterRule25List.size()>0){
				for(FrdParameterRule25 Item:FrdParameterRule25List){	
					/*
					 * query apakah shipping addressnya order ini mengandung alamat di tabel FrdParameterRule25
					 */
						List<VenOrderItem> VenOrderItemList=orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId="+orderId+" and  UPPER(o.venAddress.streetAddress1) LIKE '%"+Item.getCode().toUpperCase()+"%' ", 0, 0);				    	
						/*
						 * jika size list lebih dari 0, berarti orderitem mengandung alamat perusahaan
						 * return 1 untuk flag agar risk point dikurangi dengan point (pointRule6+pointRule9+pointRule11+pointRule17+pointRule18+pointRule19)
						 */
						if(VenOrderItemList.size()>0){
							fraudPoint=1;
							_log.info("parameter match");
							break;
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
		_log.info("Done execute rule 25, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
