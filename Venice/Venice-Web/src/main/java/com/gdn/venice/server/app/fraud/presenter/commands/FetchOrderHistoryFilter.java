package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

/**
 * Fetch filter order
 * 
 * @author Arifin
 */

public class FetchOrderHistoryFilter implements RafDsCommand {

	RafDsRequest request;
	
	public FetchOrderHistoryFilter(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			VenOrderItemSessionEJBRemote orderItemsessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");		
			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");		
			VenContactDetailSessionEJBRemote cpsessionHome = (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");		
			VenAddressSessionEJBRemote venAddressSessionHome = (VenAddressSessionEJBRemote) locator.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");		
			VenOrderPaymentAllocationSessionEJBRemote allocationsessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");		
			VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");	
			
			List<VenOrderItem> itemList = null;		
			String query="";		
			if(request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)!=""){
				/*
				 * get data order item
				 */
				query = "select o from VenOrderItem o left join fetch o.venOrder left join fetch o.venOrder.venOrderContactDetails where o.venOrder.wcsOrderId='"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"'";

				itemList = orderItemsessionHome.queryByRange(query,0, 0);		
				int count=0;
				List<VenContactDetail> cpList=null;
				List<VenOrderPaymentAllocation> allocationList=null;
				String phone="",mobile="",email="";
				/*
				 * jika order item tidak null
				 * maka akan difilter 
				 */
				if(!itemList.isEmpty() && itemList!=null){
					
					query = "select o from VenOrderAddress o where o.venOrder.orderId = " + itemList.get(0).getVenOrder().getOrderId();
					
					List<VenOrderAddress> orderAddressList = orderAddressSessionHome.queryByRange(query, 0, 0);
					
					itemList.get(0).getVenOrder().setVenOrderAddresses(orderAddressList);
					/*
					 * get Cp dari Order tersebut
					 */
					if(itemList.get(0).getVenOrder().getVenOrderContactDetails()!=null && itemList.get(0).getVenOrder().getVenOrderContactDetails().size()>0){
						
						cpList= new ArrayList<VenContactDetail>();
						for(VenOrderContactDetail intemCon :itemList.get(0).getVenOrder().getVenOrderContactDetails()){
							cpList.add(intemCon.getVenContactDetail());							
						}
					}else{
						cpList=cpsessionHome.queryByRange("select o from VenContactDetail o where o.venParty.partyId="+itemList.get(0).getVenOrder().getVenCustomer().getVenParty().getPartyId(), 0, 0);
					}
					if(cpList!=null && !cpList.isEmpty()){
					/*
					 * set no mobile.no phone dan email sesuai contact detai tersebut
					 */
						for(VenContactDetail itemCp:cpList){
							if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_MOBILE){			
								if(!mobile.equals("") ){
									mobile=mobile+",";
								}
								if(!itemCp.getContactDetail().equals("") && !itemCp.getContactDetail().equals("-") && !itemCp.getContactDetail().equals("0")){
										mobile=mobile+"'"+itemCp.getContactDetail()+"'";
								}
							}else if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_PHONE){
								if(!phone.equals("")){
									phone=phone+",";
								}
								if(!itemCp.getContactDetail().equals("") && !itemCp.getContactDetail().equals("-") && !itemCp.getContactDetail().equals("0")){
									 phone=phone+"'"+itemCp.getContactDetail()+"'";
								}
							}else if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_EMAIL){
								if(!email.equals("")){
									email=email+",";
								}
								if(!itemCp.getContactDetail().equals("") && !itemCp.getContactDetail().equals("-") && !itemCp.getContactDetail().equals("0")){
										email=email+"'"+itemCp.getContactDetail()+"'";
								}
							}
						}												
					}					
					/*
					 * get informasi mengenai payment
					 */
					allocationList=allocationsessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+itemList.get(0).getVenOrder().getOrderId(), 0, 0);
					
					for(int i=1;i<10;i++){						
						count=0;
						query="";
						HashMap<String, String> map = new HashMap<String, String>();
						
						map.put(DataNameTokens.ORDERHISTORY_ID, ""+i);
						if(i==1){
							/*
							 * filter by name customer
							 */
							if(itemList.get(0).getVenOrder().getVenCustomer().getVenParty()!=null ){
								if(!itemList.get(0).getVenOrder().getVenCustomer().getCustomerUserName().toUpperCase().equals("ANONYMOUS") && itemList.get(0).getVenOrder().getVenCustomer().getVenParty()!=null){
										query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and UPPER(o.venCustomer.venParty.fullOrLegalName) like '"+itemList.get(0).getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName().toUpperCase()+"'";
										List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
										count=orderList.isEmpty()?0:orderList.size();
								}						
							
							}							
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(itemList.get(0).getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName(), "").toString());						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Customer Full Name");
						}else if(i==2){
							/*
							 * filter by phone number
							 */
							if(!phone.equals("")){
								query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where u.contactDetail in ("+phone+") and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_PHONE+") "+
								"or o.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where r.venContactDetail.contactDetail in ("+phone+") and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_PHONE+") )";
								List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
								count=orderList.isEmpty()?0:orderList.size();
							}
							
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(phone, "").toString());						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Customer Phone Number");
						}else if(i==3 ){
							/*
							 * filter by mobile number
							 */
							if(!mobile.equals("")){
								query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where u.contactDetail in ("+mobile+") and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_MOBILE+") "+
								"or o.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where r.venContactDetail.contactDetail in ("+mobile+") and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_MOBILE+")) ";
								List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
								count=orderList.isEmpty()?0:orderList.size();
							}						
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(mobile, "").toString());						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Customer Mobile Number");
						}else if(i==4){
							/*
							 * filter by email customer
							 */
							if(!email.equals("")){
								query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where UPPER(u.contactDetail) in ("+email.toUpperCase()+") and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_EMAIL+") "+
								"or o.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where UPPER(r.venContactDetail.contactDetail) in ("+email.toUpperCase()+") and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_EMAIL+") )";
								List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
								count=orderList.isEmpty()?0:orderList.size();
							}
							
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(email, "").toString());						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Customer Email");
						}else if(i==5){					
							/*
							 * filter by address order
							 */
							List<VenAddress> address=null;
							String adressOrder="";
							if(itemList.get(0).getVenOrder().getVenOrderAddresses()!=null && itemList.get(0).getVenOrder().getVenOrderAddresses().size()>0){								
								address= new ArrayList<VenAddress>();		
								for(VenOrderAddress intemAdd :itemList.get(0).getVenOrder().getVenOrderAddresses()){
									address.add(intemAdd.getVenAddress());
								}
							}else{
								address=venAddressSessionHome.queryByRange("select u from VenAddress u where u.addressId in (select o.venAddress.addressId from VenPartyAddress o where o.venParty.partyId="+itemList.get(0).getVenOrder().getVenCustomer().getVenParty().getPartyId()+") order by u.addressId desc", 0, 1);
							}
							if(address!=null && !address.isEmpty() && address.size()>0){	
								String addressItem =address.get(0).getStreetAddress1()!=null?address.get(0).getStreetAddress1().toUpperCase():"";
								query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenPartyAddress u where UPPER(u.venAddress.streetAddress1) in ('"+addressItem+"')) or "+
								"o.orderId in (select u.venOrder.orderId from VenOrderAddress u where UPPER(u.venAddress.streetAddress1) in ('"+addressItem+"'))) ";
								List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
								count=orderList.isEmpty()?0:orderList.size();
								adressOrder=addressItem;
							}
							
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(adressOrder, "").toString());									
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Customer Address");
							
						}else if(i==6){
							/*
							 * filter by shipping address
							 */
							if(itemList.get(0).getVenAddress().getStreetAddress1()!=null){
								query = "select u from VenOrder u where u.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and u.orderId in (select o.venOrder.orderId from VenOrderItem o where UPPER(o.venAddress.streetAddress1) like '"+itemList.get(0).getVenAddress().getStreetAddress1().toUpperCase()+"' group by o.venOrder.orderId)";
								List<VenOrder> itemsList = orderSessionHome.queryByRange(query, 0, 0);
								count=itemsList.isEmpty()?0:itemsList.size();
							}		
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(itemList.get(0).getVenAddress().getStreetAddress1(), "").toString());						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Shipping Address");
						}else if(i==7){
							/*
							 * filter by billing address
							 */
							if(allocationList!=null && !allocationList.isEmpty()){
								query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and o.orderId in (select u.venOrder.orderId from VenOrderPaymentAllocation u where UPPER(u.venOrderPayment.venAddress.streetAddress1) like '"+allocationList.get(0).getVenOrderPayment().getVenAddress().getStreetAddress1().toUpperCase()+"')";
								List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
								count=orderList.isEmpty()?0:orderList.size();
							}													
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(allocationList!=null  && !allocationList.isEmpty()?allocationList.get(0).getVenOrderPayment().getVenAddress().getStreetAddress1():"", "").toString());						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Billing Address");
						}else if(i==8){
							/*
							 * filter by ip
							 */
							if(itemList.get(0).getVenOrder().getIpAddress()!=null){
								query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and o.ipAddress like '"+itemList.get(0).getVenOrder().getIpAddress()+"'";
								List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
								count=orderList.isEmpty()?0:orderList.size();
							}						
						
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, Util.isNull(itemList.get(0).getVenOrder().getIpAddress(), "").toString());						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"IP Address");
						}else if(i==9){
							/*
							 * get info no CC yang order gunakan 
							 * dan bandingkan dengan order lain
							 */
							String cc="";
							if(allocationList!=null && !allocationList.isEmpty()){
								for(VenOrderPaymentAllocation itempayment : allocationList){
									if(!cc.equals("")){
										cc=cc+",";
									}
									cc=cc+"'"+itempayment.getVenOrderPayment().getMaskedCreditCardNumber()+"'";
								}
							}
							if(allocationList!=null && !allocationList.isEmpty() && !cc.equals("")){
								query="select o from VenOrder o where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and o.orderId in (select u.venOrder.orderId from VenOrderPaymentAllocation u where u.venOrderPayment.maskedCreditCardNumber in ("+cc+"))";
								List<VenOrder> orderList = orderSessionHome.queryByRange(query, 0, 0);
								count=orderList.isEmpty()?0:orderList.size();
							}
							map.put(DataNameTokens.ORDERHISTORY_STRINGFILTER, cc);						
							map.put(DataNameTokens.ORDERHISTORY_DESCRIPTION,"Credit Card Number");
						}
						map.put(DataNameTokens.VENORDER_WCSORDERID, request.getParams().get(DataNameTokens.VENORDER_WCSORDERID));		
						map.put(DataNameTokens.ORDERHISTORY_TOTALORDERHISTORY, ""+count);
						
						dataList.add(map);	
					}
				}
		
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
