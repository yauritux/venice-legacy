package com.gdn.venice.server.app.fraud.rule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FrdParameterRule16SessionEJBRemote;
import com.gdn.venice.facade.FrdRuleConfigTresholdSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.fraud.FraudRuleWithNativeQueryRemote;
import com.gdn.venice.persistence.FrdParameterRule16;
import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.gdn.venice.persistence.VenCustomer;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.util.VeniceConstants;

/**
 * Class for calculate fraud rule 16: Same customer with different credit card
 * 
 * @author Roland
 */

public class Rule16 {
	protected static Logger _log = null;
    
    public Rule16() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.server.app.fraud.rule.Rule16");
    }
    
	public int execute(VenOrder venOrder) throws Exception{
		_log.info("Start execute rule 16");
			int fraudPoint=0;	
			Locator<Object> locator = null;
			
			try{
				locator = new Locator<Object>();
				FraudRuleWithNativeQueryRemote orderSessionHome = (FraudRuleWithNativeQueryRemote) locator.lookup(FraudRuleWithNativeQueryRemote.class, "FraudRuleWithNativeQuerySessionEJBBean");
				FrdParameterRule16SessionEJBRemote rule16SessionHome = (FrdParameterRule16SessionEJBRemote) locator.lookup(FrdParameterRule16SessionEJBRemote.class, "FrdParameterRule16SessionEJBBean");
				FrdRuleConfigTresholdSessionEJBRemote configSessionHome = (FrdRuleConfigTresholdSessionEJBRemote) locator.lookup(FrdRuleConfigTresholdSessionEJBRemote.class, "FrdRuleConfigTresholdSessionEJBBean");
				VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
				VenOrderContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) locator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
							
				int numOfTrans=0;
	            String dateStart="";
	            String dateEnd="";
				
				//get day span from config
				List<FrdRuleConfigTreshold> configList = configSessionHome.queryByRange("select o from FrdRuleConfigTreshold o where o.key = 'DAY_SPAN_FOR_FRAUD_PARAMETER16'", 0, 1);
				int daySpan=0;
				if(configList.size()>0){
					daySpan = new Integer (configList.get(0).getValue());
				}				
								
				//get order date, lalu query ven order dengan waktu sesuai daySpan
				SimpleDateFormat dateTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");								
	            Date toWeek = DateUtils.addDays(venOrder.getOrderDate(),-daySpan);   
	            dateStart=dateTimestamp.format(toWeek.getTime());
	            dateEnd=dateTimestamp.format(venOrder.getOrderDate().getTime());          
									
				//query order based on day span
				List<VenOrder> orderListInAMonth=orderSessionHome.queryByRangeVenOrder("select * from ven_order o where o.order_id<>"+venOrder.getOrderId()+" and o.order_status_id = "+ VeniceConstants.VEN_ORDER_STATUS_C+" and o.order_date between '"+dateStart+"' and '"+dateEnd+"'");
				if(orderListInAMonth.size()>0){											
					String orderInAMonthEmail=null;
					Boolean found=false;
					Long partyId=new Long(0);
					Long customerId=new Long(0);
					String custAddress=null;
					String custEmail = null;
					
					//cek customer address								
					List<VenOrderAddress> orderAddressList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId = "+venOrder.getOrderId(), 0, 1);
					if(orderAddressList.size()>0){
						custAddress = (orderAddressList.get(0).getVenAddress().getStreetAddress1()!=null?orderAddressList.get(0).getVenAddress().getStreetAddress1().trim():"")+(orderAddressList.get(0).getVenAddress().getPostalCode()!=null?orderAddressList.get(0).getVenAddress().getPostalCode().trim():"");
					}
					
					//cek customer email
					List<VenOrderContactDetail> contactDetailList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venContactDetail.venContactDetailType.contactDetailTypeId=0 and o.venOrder.orderId = "+venOrder.getOrderId(), 0, 0);
					if(contactDetailList.size()>0){
						custEmail = contactDetailList.get(0).getVenContactDetail().getContactDetail();
					}
					
					for(int j=0;j<orderListInAMonth.size();j++){
						//cek customer name atau username dalam sebulan 
						customerId=orderSessionHome.queryByRangeGetId("select customer_id from ven_order o where o.order_id="+orderListInAMonth.get(j).getOrderId());				
						List<VenCustomer> customerList=orderSessionHome.queryByRangeVenCustomer("select * from ven_customer c where c.customer_id="+customerId);
						
						if(customerList.size()>0){
							partyId=orderSessionHome.queryByRangeGetId("select party_id from ven_customer c where c.customer_id="+customerList.get(0).getCustomerId());
							List<VenParty> partyList=orderSessionHome.queryByRangeVenParty("select * from ven_party p where p.party_id="+partyId);
							if(partyList.size()>0){
								if((customerList.get(0).getCustomerUserName().equalsIgnoreCase(venOrder.getVenCustomer().getCustomerUserName()) || 
									(partyList.get(0).getFullOrLegalName().equalsIgnoreCase(venOrder.getVenCustomer().getVenParty().getFullOrLegalName())))){
								numOfTrans++;
								found=true;
								_log.info("customer name/username match, numOfTrans: "+numOfTrans);
								}
							}
						}							
						
						if(found.equals(false)){					
							//cek contact dalam sebulan
							if(contactDetailList.size()>0){
								List<VenOrderContactDetail> contactDetailListInAMonth = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venContactDetail.venContactDetailType.contactDetailTypeId=0 and o.venOrder.orderId = "+orderListInAMonth.get(j).getOrderId(), 0, 1);
								if(contactDetailListInAMonth.size()>0){
									orderInAMonthEmail=contactDetailListInAMonth.get(0).getVenContactDetail().getContactDetail();
									if(custEmail.equalsIgnoreCase(orderInAMonthEmail)){
										numOfTrans++;
										found=true;
										_log.info("email match, numOfTrans: "+numOfTrans);
									}										
								}
							}
						}
						
						if(found.equals(false)){
							//cek address dalam sebulan
							if(orderAddressList.size()>0){																
								List<VenOrderAddress> orderAddressListInAMonth = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId = "+orderListInAMonth.get(j).getOrderId(), 0, 1);
								if(orderAddressListInAMonth.size()>0){
									String streetAddress1=orderAddressListInAMonth.get(0).getVenAddress()!=null?(orderAddressListInAMonth.get(0).getVenAddress().getStreetAddress1()!=null?orderAddressListInAMonth.get(0).getVenAddress().getStreetAddress1().trim():""):"";
									String postalCode=orderAddressListInAMonth.get(0).getVenAddress()!=null?(orderAddressListInAMonth.get(0).getVenAddress().getPostalCode()!=null?orderAddressListInAMonth.get(0).getVenAddress().getPostalCode().trim():""):"";				
									String orderInAMonthAddress=streetAddress1+postalCode;																				
									if(custAddress.equalsIgnoreCase(orderInAMonthAddress)){
										numOfTrans++;
										found=true;
										_log.info("address match, numOfTrans: "+numOfTrans);
									}									
								}
							}
						}					
					}
				}
						
				_log.debug("total numOfTrans: "+numOfTrans);
			
			if(numOfTrans>1){
				//get the matching code from parameter and set the risk point
				List<FrdParameterRule16> parameterList = rule16SessionHome.queryByRange("select o from FrdParameterRule16 o where o.code='SAME_NAME_EMAIL_ADDRESS'", 0, 1);				
				if(parameterList.size()>0){					
					fraudPoint=parameterList.get(0).getRiskPoint();
					_log.info("parameter match: "+parameterList.get(0).getDescription()+"riskPoint: "+fraudPoint);					
					fraudPoint=fraudPoint*numOfTrans;
					_log.info("riskPoint * numOfTrans: "+fraudPoint);			
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
		_log.info("Done execute rule 16, fraudPoint is: "+fraudPoint);
		return fraudPoint;
	}
}
