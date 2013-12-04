package com.gdn.venice.server.app.fraud.rule;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote;
import com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule30;
import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenContactDetail;

/**
 * Class for calculate fraud rule 30: Phone Area Code Customer
 *  * 
 * @author Arifin
 */
public class Rule30 {
	protected static Logger _log = null;
    
    public Rule30() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule30");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 30");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
		    FrdParameterRule30SessionEJBRemote rule30SessionHome = (FrdParameterRule30SessionEJBRemote) locator.lookup(FrdParameterRule30SessionEJBRemote.class, "FrdParameterRule30SessionEJBBean");
			VenAddressSessionEJBRemote orderAddressSessionHome = (VenAddressSessionEJBRemote) locator.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");			
			FrdRuleConfigTresholdSessionEJBRemote frdRuleConfigTresholdSessionHome = (FrdRuleConfigTresholdSessionEJBRemote) locator.lookup(FrdRuleConfigTresholdSessionEJBRemote.class, "FrdRuleConfigTresholdSessionEJBBean");
			VenContactDetailSessionEJBRemote venContactDetailSessionSessionHome =  (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
		/*
		 * get informasi addess sesuai party dari order baru
		 */
			List<VenAddress> venAddressList = orderAddressSessionHome.queryByRange("select o from VenAddress o  where o.addressId in (select b.venAddress.addressId from VenOrderAddress b where b.venOrder.orderId="+orderId+")", 0, 1);
	
			
			if(venAddressList.size()>0){
				/**
				 * besihkan nama2 yang tidak dibutuhkan agar query lebih mudah
				 * setelah itu query sesuai dengan nama kota dan kode kote
				 * jika kota dan kode order beda, maka akan ke risk point
				 */
				if(venAddressList.get(0).getVenCity()!=null){
					String city=venAddressList.get(0).getVenCity().getCityName().toLowerCase().replace("kota","").replace("barat","").replace("pusat","").replace("timur","").replace("selatan","").replace(".","").replace("utara","").replace("kab","").replace("tenggara","").trim().toUpperCase();
					List<FrdParameterRule30> frdParameterRule30List=rule30SessionHome.queryByRange("select o from FrdParameterRule30 o where UPPER(o.namaKota) ='"+city+"' ",0,0);			
					if(frdParameterRule30List.size()>0){
						List<VenContactDetail> venContactDetailList =venContactDetailSessionSessionHome.queryByRange("select o from VenContactDetail o where o.venContactDetailType.contactDetailTypeId="+DataConstantNameTokens.VEN_CONTACT_DETAIL_TYPE_PHONE+
									" and o.contactDetailId in (select a.venContactDetail.contactDetailId from VenOrderContactDetail a where a.venOrder.orderId="+orderId+")", 0, 0);				
					if(venContactDetailList.size()>0){
							for(VenContactDetail itemCon : venContactDetailList){
								if(itemCon.getContactDetail().length()>=frdParameterRule30List.get(0).getKodeKota().length()){
									if(!frdParameterRule30List.get(0).getKodeKota().toString().trim().equals(itemCon.getContactDetail().substring(0, frdParameterRule30List.get(0).getKodeKota().length()).toString().trim())){
										List<FrdRuleConfigTreshold> frdRuleConfigTresholdList=frdRuleConfigTresholdSessionHome.queryByRange("select o from FrdRuleConfigTreshold o where o.key='"+DataConstantNameTokens.FRD_RULE_CONFIG_TRESHOLD_FRD_PARAMETER_RULE_30+"'", 0, 0);
										fraudPoint=new Integer(frdRuleConfigTresholdList.get(0).getValue());		
										_log.info("parameter match");
										break;
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
		_log.info("Done execute rule 30, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
