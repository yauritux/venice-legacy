package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

/**
 * Fetch same Order
 * 
 * @author Arifin
 */

public class FetchSameOrderHistory implements RafDsCommand {

	RafDsRequest request;
	
	public FetchSameOrderHistory(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			VenOrderSessionEJBRemote ordersessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");		
			VenOrderItemSessionEJBRemote orderitemsessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");		
			VenOrderPaymentAllocationSessionEJBRemote allocationsessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");		
			VenContactDetailSessionEJBRemote cpsessionHome = (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");		
			VenBinCreditLimitEstimateSessionEJBRemote binSessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");

			/*
			 * 1. cari order yang nama customernya sama
			 * 2. cari order yang no telp sama
			 * 3. cari order yang no Hp sama
			 * 4. cari order yang email sama
			 * 5. cari order yang customer address sama
			 * 6. cari order yang shipping address sama
			 * 7. cari order yang billing address sama
			 * 8. cari order yang no CC sama
			 */		
			List<VenOrder> itemList = null;		
			List<VenOrder> count=null;
			String query="";			
			if(request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)!=""){
				if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_FULLNAME_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){	
							query = "select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and UPPER(o.venCustomer.venParty.fullOrLegalName) like '"+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).toUpperCase()+"' and o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"'";
							itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
							count = ordersessionHome.queryByRange(query,0, 0);
					}
				} if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_PHONE_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){
						query="select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where u.contactDetail in ("+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER)+") and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_PHONE+") "+
						"or o.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where r.venContactDetail.contactDetail in ("+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER)+") and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_PHONE+") )";
						itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
							count = ordersessionHome.queryByRange(query,0, 0);
					}
				} else if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_MOBILE_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){	
						query="select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where u.contactDetail in ("+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER)+") and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_MOBILE+") "+
						"or o.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where r.venContactDetail.contactDetail in ("+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER)+") and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_MOBILE+") )";
							itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
							count = ordersessionHome.queryByRange(query,0, 0);
					}
				}else if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_EMAIL_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){		
						query="select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenContactDetail u where UPPER(u.contactDetail) in ("+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).toUpperCase()+") and u.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_EMAIL+") "+
						"or o.orderId in (select r.venOrder.orderId from VenOrderContactDetail r where UPPER(r.venContactDetail.contactDetail) in ("+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).toUpperCase()+") and r.venContactDetail.venContactDetailType.contactDetailTypeId="+VeniceConstants.VEN_CONTACT_TYPE_EMAIL+")) ";
						itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
							count = ordersessionHome.queryByRange(query,0, 0);
					}
				}if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_CUSTOMER_ADDRESS_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){	
						query="select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and ( o.venCustomer.venParty.partyId in (select u.venParty.partyId from VenPartyAddress u where UPPER(u.venAddress.streetAddress1) in ('"+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).toUpperCase()+"')) or "+
						"o.orderId in (select u.venOrder.orderId from VenOrderAddress u where UPPER(u.venAddress.streetAddress1) in ('"+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).toUpperCase()+"')) )";
						itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
						count = ordersessionHome.queryByRange(query,0, 0);
					}
				} else if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_SHIPPING_ADDRESS_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){	
						query = "select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and o.orderId in (select u.venOrder.orderId from VenOrderItem u where UPPER(u.venAddress.streetAddress1) like '"+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).toUpperCase()+"' group by  u.venOrder.orderId)";
						itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
						count = ordersessionHome.queryByRange(query,0, 0);
					}
				}else if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_BILLING_ADDRESS_FILTER)){
					query="select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and o.orderId in (select u.venOrder.orderId from VenOrderPaymentAllocation u where UPPER(u.venOrderPayment.venAddress.streetAddress1) like '"+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).toUpperCase()+"')";
					itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
					count = ordersessionHome.queryByRange(query,0, 0);
				}else if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_IP_ADDRESS_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){	
						query="select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and o.ipAddress like '"+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER)+"'";
						itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
						count = ordersessionHome.queryByRange(query,0, 0);
					}
				}	else if(request.getParams().get(DataNameTokens.VENORDER_ORDERID).equals(VeniceConstants.FRD_ORDER_HISTORY_CC_NUMBER_FILTER)){
					if(!request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER).equals("")){	
						query="select o from VenOrder o left join fetch o.venOrderContactDetails where o.wcsOrderId <> '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"' and  o.orderId in (select u.venOrder.orderId from VenOrderPaymentAllocation u where u.venOrderPayment.maskedCreditCardNumber in ("+request.getParams().get(DataNameTokens.ORDERHISTORY_STRINGFILTER)+"))";
						itemList = ordersessionHome.queryByRange(query,request.getStartRow(), request.getEndRow()-request.getStartRow());
						count = ordersessionHome.queryByRange(query,0, 0);
					}
				}	
			}				
			int startRow=new Integer(request.getStartRow());
			
			if(itemList!=null && !itemList.isEmpty()){
				for(int i=0; i<itemList.size();i++){
					startRow=startRow+1;
					HashMap<String, String> map = new HashMap<String, String>();
					VenOrder list = itemList.get(i);
					
					List<VenOrderItem> orItems=null;
					List<VenOrderPaymentAllocation> allocations=null;
					List<VenContactDetail> cpList=null;		
					map.put(DataNameTokens.VENORDER_ORDERID, Util.isNull(list.getOrderId(), "").toString());
					map.put(DataNameTokens.VENORDER_WCSORDERID, Util.isNull(list.getWcsOrderId(), "").toString());	
					map.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, Util.isNull(list.getVenOrderStatus().getOrderStatusCode(), "").toString());	
					map.put(DataNameTokens.VENORDER_ORDERDATE, Util.isNull(list.getOrderDate(), "").toString());
					map.put(DataNameTokens.VENORDER_AMOUNT, Util.isNull(list.getAmount(), "").toString());		
					map.put(DataNameTokens.VENORDER_IPADDRESS, Util.isNull(list.getIpAddress(), "").toString());	
					
					 String phone="",mobile="",email="",customer="";			    	
			    	 if(list.getVenOrderContactDetails()!=null && list.getVenOrderContactDetails().size()>0){
			    		 if(list.getVenCustomer().getVenParty()!=null){
			    			 customer=list.getVenCustomer().getVenParty().getFullOrLegalName();
			    		 }
			    		 for(VenOrderContactDetail itemVenCont : list.getVenOrderContactDetails()){			    			
			    			 if(itemVenCont.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_MOBILE && mobile.equals("")){								
									mobile=itemVenCont.getVenContactDetail().getContactDetail();
								}else if(itemVenCont.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_PHONE && phone.equals("")){
									phone=itemVenCont.getVenContactDetail().getContactDetail();
								}else if(itemVenCont.getVenContactDetail().getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_EMAIL && email.equals("")){
									email=itemVenCont.getVenContactDetail().getContactDetail();
								}
			    		 }
	    			 }else	if(list.getVenCustomer()!=null){
						if(list.getVenCustomer().getVenParty()!=null){
							 customer=list.getVenCustomer().getVenParty().getFullOrLegalName();
							/*
							 * get informasi contact detail untuk order tersebut
							 */
							cpList=cpsessionHome.queryByRange("select o from VenContactDetail o where o.venParty.partyId="+list.getVenCustomer().getVenParty().getPartyId(), 0, 0);
							if(cpList!=null && !cpList.isEmpty()){
								for(VenContactDetail itemCp:cpList){
									if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_MOBILE && mobile.equals("")){								
										mobile=itemCp.getContactDetail();
									}else if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_PHONE && phone.equals("")){
										phone=itemCp.getContactDetail();
									}else if(itemCp.getVenContactDetailType().getContactDetailTypeId()==VeniceConstants.VEN_CONTACT_TYPE_EMAIL && email.equals("")){
										email=itemCp.getContactDetail();
									}
								}	
							}
						}	
					}			    	 
			    	 	map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, Util.isNull(customer, "").toString());
			    	 	map.put(DataNameTokens.ORDERHISTORY_PHONE_NUMBER, Util.isNull(phone, "").toString());
						map.put(DataNameTokens.ORDERHISTORY_MOBILE_NUMBER, Util.isNull(mobile, "").toString());
						map.put(DataNameTokens.ORDERHISTORY_EMAIL, Util.isNull(email, "").toString());		
					/*
					 * get data untuk informasi order item
					 */					
					orItems=orderitemsessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId="+list.getOrderId(), 0, 0);				
					if(!orItems.isEmpty() && orItems!=null){
							map.put(DataNameTokens.ORDERHISTORY_PRODUCT_NAME,Util.isNull(orItems.get(0).getVenMerchantProduct().getWcsProductName(), "").toString());
							map.put(DataNameTokens.ORDERHISTORY_QTY, Util.isNull(orItems.size(), "").toString());	
							map.put(DataNameTokens.ORDERHISTORY_SHPIPPING_ADDRESS, Util.isNull(orItems.get(0).getVenAddress().getStreetAddress1(), "").toString());	
					}else{
						map.put(DataNameTokens.ORDERHISTORY_QTY, "");	
						map.put(DataNameTokens.ORDERHISTORY_SHPIPPING_ADDRESS,"");	
						map.put(DataNameTokens.ORDERHISTORY_PRODUCT_NAME,"");
					}
					/*
					 * get data untuk infomasi payment
					 */
					allocations=allocationsessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+list.getOrderId(), 0, 0);	
					if(!allocations.isEmpty() && allocations!=null){
						map.put(DataNameTokens.ORDERHISTORY_BILLING_METHOD, Util.isNull(allocations.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc(), "").toString());
						map.put(DataNameTokens.ORDERHISTORY_STATUS, Util.isNull(allocations.get(0).getVenOrderPayment().getVenPaymentStatus().getPaymentStatusCode(), "").toString());		
						map.put(DataNameTokens.ORDERHISTORY_CC, Util.isNull(allocations.get(0).getVenOrderPayment().getMaskedCreditCardNumber(), "").toString());
						
						String creditCardNumber = "";
						String binNumber="", bankName="";
						creditCardNumber = allocations.get(0).getVenOrderPayment().getMaskedCreditCardNumber()!=null?allocations.get(0).getVenOrderPayment().getMaskedCreditCardNumber():"";
						if(!creditCardNumber.equals("")){		
							//check CC limit from BIN number
							if(creditCardNumber.length()>6){
								binNumber = creditCardNumber.substring(0, 6);
							}			
						}
						List<VenBinCreditLimitEstimate> binCreditLimitList = binSessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.isActive=true and o.binNumber like '"+binNumber+"'", 0, 1);
						if(binCreditLimitList.size()>0){
							bankName=binCreditLimitList.get(0).getBankName();
						}
												
						map.put(DataNameTokens.ORDERHISTORY_ISSUER, Util.isNull(bankName, "").toString());		
						map.put(DataNameTokens.ORDERHISTORY_ECI, Util.isNull(allocations.get(0).getVenOrderPayment().getThreeDsSecurityLevelAuth(), "").toString());	
					}else{
						map.put(DataNameTokens.ORDERHISTORY_BILLING_METHOD, "");
						map.put(DataNameTokens.ORDERHISTORY_STATUS, "");		
						map.put(DataNameTokens.ORDERHISTORY_CC, "");
						map.put(DataNameTokens.ORDERHISTORY_ISSUER, "");		
						map.put(DataNameTokens.ORDERHISTORY_ECI, "");	
					}										
					dataList.add(map);	
				}
				if(count.size()<=(request.getStartRow()+itemList.size())){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(DataNameTokens.VENORDER_ORDERID, "");
					map.put(DataNameTokens.VENORDER_WCSORDERID, "END");		
					map.put(DataNameTokens.VENORDER_ORDERDATE, "");
					map.put(DataNameTokens.VENORDER_AMOUNT, "");		
					map.put(DataNameTokens.VENORDER_IPADDRESS, "");	
					map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, "");
					map.put(DataNameTokens.ORDERHISTORY_PHONE_NUMBER, "");
					map.put(DataNameTokens.ORDERHISTORY_MOBILE_NUMBER, "");
					map.put(DataNameTokens.ORDERHISTORY_EMAIL, "");	
					map.put(DataNameTokens.ORDERHISTORY_QTY, "");	
					map.put(DataNameTokens.ORDERHISTORY_SHPIPPING_ADDRESS,"");	
					map.put(DataNameTokens.ORDERHISTORY_PRODUCT_NAME,"");
					map.put(DataNameTokens.ORDERHISTORY_BILLING_METHOD, "");
					map.put(DataNameTokens.ORDERHISTORY_STATUS, "");		
					map.put(DataNameTokens.ORDERHISTORY_CC, "");
					map.put(DataNameTokens.ORDERHISTORY_ISSUER, "");		
					map.put(DataNameTokens.ORDERHISTORY_ECI, "");
					dataList.add(map);	
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
