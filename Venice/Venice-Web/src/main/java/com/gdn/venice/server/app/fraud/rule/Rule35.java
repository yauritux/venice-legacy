package com.gdn.venice.server.app.fraud.rule;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote;
import com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote;
import com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdBlacklistReason;
import com.gdn.venice.persistence.FrdParameterRule35;
import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;

/**
 * Class for calculate fraud rule 35: Grey list
 * 
 * @author Roland
 */

public class Rule35 {
	protected static Logger _log = null;
    
    public Rule35() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule35");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 35");
		int fraudPoint=0;	
		Locator<Object> locator = null;
		Boolean result=false;
		ArrayList<String> blacklistReason=new ArrayList<String>();
		try{
			locator = new Locator<Object>();
			FrdParameterRule35SessionEJBRemote sessionHome = (FrdParameterRule35SessionEJBRemote) locator.lookup(FrdParameterRule35SessionEJBRemote.class, "FrdParameterRule35SessionEJBBean");
			VenOrderContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote paymentSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			FrdBlacklistReasonSessionEJBRemote blacklistReasonSessionHome = (FrdBlacklistReasonSessionEJBRemote) locator.lookup(FrdBlacklistReasonSessionEJBRemote.class, "FrdBlacklistReasonSessionEJBBean");
			FrdRuleConfigTresholdSessionEJBRemote frdRuleConfigTresholdSessionHome = (FrdRuleConfigTresholdSessionEJBRemote) locator.lookup(FrdRuleConfigTresholdSessionEJBRemote.class, "FrdRuleConfigTresholdSessionEJBBean");
			
			List<FrdParameterRule35> nameGreyList = sessionHome.queryByRange("select o from FrdParameterRule35 o where upper(o.customerName) = '"+venOrder.getVenCustomer().getVenParty().getFullOrLegalName().toUpperCase()+"'", 0, 0);
			if(nameGreyList.size()>0){
				_log.info("customer name grey list found");
				result=true;
				
				blacklistReason.add("Customer name grey list");
			}			
			
			List<FrdParameterRule35> emailGreyList = null;
			List<VenOrderContactDetail> contactDetailEmailBlacklistList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = "+venOrder.getOrderId()+" and o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_EMAIL, 0, 0);
			if(contactDetailEmailBlacklistList.size()>0){
				emailGreyList = sessionHome.queryByRange("select o from FrdParameterRule35 o where upper(o.email) like '"+contactDetailEmailBlacklistList.get(0).getVenContactDetail().getContactDetail().toUpperCase()+"'", 0, 0);
				if(emailGreyList.size()>0){
					_log.info("customer email grey list found");
					result=true;
					
					blacklistReason.add("Customer email grey list");
				}
			}
			
			List<FrdParameterRule35> noCcGreyList = null;
			List<VenOrderPaymentAllocation> paymentList = paymentSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = "+venOrder.getOrderId(), 0, 0);
			if(paymentList.size()>0){
				for(int i=0;i<paymentList.size();i++){
					noCcGreyList = sessionHome.queryByRange("select o from FrdParameterRule35 o where o.ccNumber like '"+paymentList.get(i).getVenOrderPayment().getMaskedCreditCardNumber()+"'", 0, 0);
					if(noCcGreyList.size()>0){
						_log.info("customer cc grey list found");
						result=true;
						
						blacklistReason.add("Customer cc number grey list");
					}
				}
			}			
			
			//insert grey list reason
			if(result==true){
				FrdBlacklistReason reason = new FrdBlacklistReason();
				reason.setOrderId(venOrder.getOrderId());
				reason.setWcsOrderId(venOrder.getWcsOrderId());
				for(int i=0;i<blacklistReason.size();i++){
					reason.setBlacklistReason(blacklistReason.get(i));
					blacklistReasonSessionHome.persistFrdBlacklistReason(reason);
				}
				
				List<FrdRuleConfigTreshold> frdRuleConfigTresholdList=frdRuleConfigTresholdSessionHome.queryByRange("select o from FrdRuleConfigTreshold o where o.key='"+DataConstantNameTokens.FRD_RULE_CONFIG_TRESHOLD_FRD_PARAMETER_RULE_35+"'", 0, 1);
				fraudPoint=new Integer(frdRuleConfigTresholdList.get(0).getValue());	
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
		_log.info("Done execute rule 35, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
