package com.gdn.venice.server.app.fraud.rule;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote;
import com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule18;
import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.gdn.venice.persistence.VenAddress;
/**
 * Class for calculate fraud rule 18: Validity of Wording Customer, Shipping, Billing Address
 * @author Arifin
 */
public class Rule18 {
	protected static Logger _log = null;
    
    public Rule18() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule18");
    }
    
	public int execute(Long orderId) throws Exception{
		_log.info("Start execute rule 18");
		int fraudPoint=0;	
		boolean result=true;
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			VenAddressSessionEJBRemote orderAddressSessionHome = (VenAddressSessionEJBRemote) locator.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");			
			FrdRuleConfigTresholdSessionEJBRemote frdRuleConfigTresholdSessionHome = (FrdRuleConfigTresholdSessionEJBRemote) locator.lookup(FrdRuleConfigTresholdSessionEJBRemote.class, "FrdRuleConfigTresholdSessionEJBBean");
			
			FrdParameterRule18SessionEJBRemote rule18SessionHome = (FrdParameterRule18SessionEJBRemote) locator.lookup(FrdParameterRule18SessionEJBRemote.class, "FrdParameterRule18SessionEJBBean");			
			/*
			 * get data customer addreee, shipping adress dan billing address
			 */
			List<VenAddress> customerList = orderAddressSessionHome.queryByRange("select o from VenAddress o  where o.addressId in (select b.venAddress.addressId from VenOrderAddress b where b.venOrder.orderId="+orderId+")", 0, 1);	
			List<VenAddress> shippingList = orderAddressSessionHome.queryByRange("select o.streetAddress1 from VenAddress o where o.addressId in (select a.venAddress.addressId from VenOrderPayment a where a.orderPaymentId in (select b.venOrderPayment.orderPaymentId from VenOrderPaymentAllocation b where b.venOrder.orderId="+orderId+")) group by o.streetAddress1 ", 0, 0);			
			List<VenAddress> billingList = orderAddressSessionHome.queryByRange("select o.streetAddress1 from VenAddress o where o.addressId in (select a.venAddress.addressId from VenOrderItem a where a.venOrder.orderId="+orderId+") group by o.streetAddress1 ", 0, 0);
			/*
			 * get code kriteria untuk pengecekan
			 */
			List<FrdParameterRule18> frdParameterRule18List = rule18SessionHome.queryByRange("select o from FrdParameterRule18 o ", 0, 0);
			String code="";
			for(FrdParameterRule18 frtItem:frdParameterRule18List){
				if(!code.equals("")){
					code=code+"|";
				}
				code=code+"("+frtItem.getCode().toLowerCase()+")(\\d+)";
			}
			/*
			 * maching string dari address dan code
			 * jika ada salah satu yang tidak cocok dengan kriteria maka kena risk point
			 */
			Pattern p = Pattern.compile(code);
			Matcher m=null;
			if(customerList.size()>0){
				 m = p.matcher((customerList.get(0).getStreetAddress1()!=null?customerList.get(0).getStreetAddress1().toLowerCase():"").replace(".", "").replace(" ", ""));
				 result=m.find()?true:false;
			}			
			if(result){
				for(int i=0;i< shippingList.size() && result;i++){
					m = p.matcher((shippingList.get(i)+"").toLowerCase().replace(".", "").replace(" ", ""));
					result=m.find()?true:false;
				}				
				for(int j=0;j< billingList.size() && result;j++){
					m = p.matcher((billingList.get(j)+"").toLowerCase().replace(".", "").replace(" ", ""));
					result=m.find()?true:false;
				}				
			}		
			if(!result){
				List<FrdRuleConfigTreshold> frdRuleConfigTresholdList=frdRuleConfigTresholdSessionHome.queryByRange("select o from FrdRuleConfigTreshold o where o.key='"+DataConstantNameTokens.FRD_RULE_CONFIG_TRESHOLD_FRD_PARAMETER_RULE_18+"'", 0, 0);
				fraudPoint=new Integer(frdRuleConfigTresholdList.get(0).getValue());		
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
		_log.info("Done execute rule 18, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
