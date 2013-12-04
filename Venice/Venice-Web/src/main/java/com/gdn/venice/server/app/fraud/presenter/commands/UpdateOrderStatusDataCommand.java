package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote;
import com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenOrderContactDetail;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderItemStatusHistoryPK;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.persistence.VenOrderStatusHistoryPK;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Update Command for Update Order Status
 * 
 * @author Roland
 */

public class UpdateOrderStatusDataCommand implements RafRpcCommand {
	String orderId;
	String caseId;
	String method;
	String username;
	
	public UpdateOrderStatusDataCommand(String caseId, String method, String username) {
		this.caseId = caseId;
		this.method = method;
		this.username = username;
	}

	@Override
	public String execute() {
		Locator<Object> locator = null;
		VenOrder order = new VenOrder();
		VenOrderStatus venOrderStatus = new VenOrderStatus();
		VenOrderSessionEJBRemote orderSessionHome = null;

		try{
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			List<FrdFraudSuspicionCase> suspicionCaseList = null;	
			FrdFraudSuspicionCase fraudCaseSuspicion = new FrdFraudSuspicionCase();
			
			suspicionCaseList = sessionHome.queryByRange("select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId =  "+ caseId, 0, 1);
			
			fraudCaseSuspicion = suspicionCaseList.get(0);
			orderId = fraudCaseSuspicion.getVenOrder().getOrderId().toString();
			
			order = orderSessionHome.queryByRange("select o from VenOrder o where o.orderId="+new Long(orderId), 0, 1).get(0);
						
			if (method.equals("updateOrderStatusToSF")) {
				venOrderStatus.setOrderStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_SF);
			} else if (method.equals("updateOrderStatusToFP")) {
				venOrderStatus.setOrderStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FP);
			} else if (method.equals("updateOrderStatusToFC")) {
				venOrderStatus.setOrderStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FC);
			}
			order.setVenOrderStatus(venOrderStatus);
			
			orderSessionHome.mergeVenOrder(order);
		}catch(Exception e){
			e.printStackTrace();
			return "-1";
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Now update the order items based on originalOrderItemList
		try {
			locator = new Locator<Object>();
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator
			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
						
			List<VenOrderItem> originalOrderItemList = orderItemSessionHome.queryByRange("select oi from VenOrderItem oi where oi.venOrder.orderId=" + orderId, 0,0);			
			ArrayList<VenOrderItem> orderItemList = new ArrayList<VenOrderItem>();
			
			for (int i=0;i<originalOrderItemList.size();i++) {
				VenOrderItem orderItem = new VenOrderItem();		
				orderItem=originalOrderItemList.get(i);		
				orderItem.setVenOrderStatus(venOrderStatus);				
				orderItemList.add(orderItem);
			}
			orderItemSessionHome.mergeVenOrderItemList(orderItemList);
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//now add history order and order item
		Locator<Object> historyLocator = null;
		
		try {
			historyLocator = new Locator<Object>();
			VenOrderStatusHistorySessionEJBRemote orderHistorySessionHome = (VenOrderStatusHistorySessionEJBRemote) historyLocator
			.lookup(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");
			
			VenOrderItemStatusHistorySessionEJBRemote orderItemHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) historyLocator
			.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");
			
			VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
			venOrderStatusHistoryPK.setOrderId(new Long(orderId));
			venOrderStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));
			
			VenOrderStatusHistory orderStatusHistory = new VenOrderStatusHistory();
			orderStatusHistory.setId(venOrderStatusHistoryPK);
			orderStatusHistory.setStatusChangeReason("Updated by " + username);
			orderStatusHistory.setVenOrderStatus(venOrderStatus);			
			
			orderHistorySessionHome.persistVenOrderStatusHistory(orderStatusHistory);	
						
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) historyLocator
			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			String select = "select oi from VenOrderItem oi where oi.venOrder.orderId=" + orderId;
			
			List<VenOrderItem> originalOrderItemList = orderItemSessionHome.queryByRange(select, 0,0);					
			for (int i=0;i<originalOrderItemList.size();i++) {				
				VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
				venOrderItemStatusHistoryPK.setOrderItemId(originalOrderItemList.get(i).getOrderItemId());
				venOrderItemStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));
				
				VenOrderItemStatusHistory orderItemStatusHistory = new VenOrderItemStatusHistory();
				orderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
				orderItemStatusHistory.setStatusChangeReason("Updated by " + username);
				orderItemStatusHistory.setVenOrderStatus(venOrderStatus);

				orderItemHistorySessionHome.persistVenOrderItemStatusHistory(orderItemStatusHistory);	
			}			

			if (method.equals("updateOrderStatusToFC")){
				//if order status is FC, insert new customer blacklist record.
				System.out.println("order status is set  to FC, insert new customer blacklist record");
				String tempAddress="", tempPhone="",tempHandPhone="", tempEmail="",tempShippingPhone="",tempShippingHandPhone="",tempShippingAddress="",noCreditCard="",descFraud="";
				FrdCustomerWhitelistBlacklistSessionEJBRemote sessionHome = (FrdCustomerWhitelistBlacklistSessionEJBRemote) historyLocator.lookup(FrdCustomerWhitelistBlacklistSessionEJBRemote.class, "FrdCustomerWhitelistBlacklistSessionEJBBean");
				FrdEntityBlacklistSessionEJBRemote ipBlacklistsessionHome = (FrdEntityBlacklistSessionEJBRemote) historyLocator.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
				VenOrderContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenOrderContactDetailSessionEJBRemote) historyLocator.lookup(VenOrderContactDetailSessionEJBRemote.class, "VenOrderContactDetailSessionEJBBean");
				VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) historyLocator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
				VenOrderItemContactDetailSessionEJBRemote orderItemContactDetailSessionHome = (VenOrderItemContactDetailSessionEJBRemote) historyLocator.lookup(VenOrderItemContactDetailSessionEJBRemote.class, "VenOrderItemContactDetailSessionEJBBean");
				VenAddressSessionEJBRemote shippingAddressSessionHome = (VenAddressSessionEJBRemote) historyLocator.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");
				VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) historyLocator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
				FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) historyLocator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
				
				List<VenOrder> orderList = orderSessionHome.queryByRange("select o from VenOrder o where o.orderId = "+orderId, 0, 1);
				
				if(orderList.size()>0){
					System.out.println("get customer address");
					List<VenOrderAddress> orderAddressBlacklistList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId ="+orderList.get(0).getOrderId(), 0, 1);
						if(orderAddressBlacklistList.size()>0){
						for(int i=0;i<orderAddressBlacklistList.size();i++){
							tempAddress+=orderAddressBlacklistList.get(i).getVenAddress().getStreetAddress1()+" ";
						}
					}
					
					System.out.println("get customer email");
					List<VenOrderContactDetail> contactDetailEmailBlacklistList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = "+orderList.get(0).getOrderId()+" and o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_EMAIL, 0, 1);
					if(contactDetailEmailBlacklistList.size()>0){
						for(int i=0;i<contactDetailEmailBlacklistList.size();i++){			
							tempEmail+=contactDetailEmailBlacklistList.get(i).getVenContactDetail().getContactDetail()+" ";
						}
					}
					
					System.out.println("get customer phone");
					List<VenOrderContactDetail> contactDetailPhoneBlacklistList = orderContactDetailSessionHome.queryByRange("select o from VenOrderContactDetail o where o.venOrder.orderId = "+orderList.get(0).getOrderId()+" and (o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_PHONE+" or o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_MOBILE+")", 0, 0);
					if(contactDetailPhoneBlacklistList.size()>0){
						for(int i=0;i<contactDetailPhoneBlacklistList.size();i++){
							if(contactDetailPhoneBlacklistList.get(i).getVenContactDetail().getVenContactDetailType().getContactDetailTypeId().equals(DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_PHONE)){
								tempPhone=contactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail();
							}else{
								tempHandPhone=contactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail();
							}
						}
					}		
					
					System.out.println("get Shipping phone");
					List<VenOrderItemContactDetail> shippingContactDetailPhoneBlacklistList = orderItemContactDetailSessionHome.queryByRange("select o from VenOrderItemContactDetail o where o.venOrderItem.venOrder.orderId = "+orderList.get(0).getOrderId()+" and (o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_PHONE+" or o.venContactDetail.venContactDetailType.contactDetailTypeId ="+DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_MOBILE+")", 0, 0);
					if(shippingContactDetailPhoneBlacklistList.size()>0){
						for(int i=0;i<shippingContactDetailPhoneBlacklistList.size();i++){
							if(shippingContactDetailPhoneBlacklistList.get(i).getVenContactDetail().getVenContactDetailType().getContactDetailTypeId().equals(DataConstantNameTokens.VEN_CONTACT_DETAIL_ID_PHONE)){
								tempShippingPhone=shippingContactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail();
							}else{
								tempShippingHandPhone=shippingContactDetailPhoneBlacklistList.get(i).getVenContactDetail().getContactDetail();
							}
						}
					}	
					
					
					System.out.println("get Shipping Address");
					List<VenAddress> shippingAddressBlacklistList = shippingAddressSessionHome.queryByRange("select o from VenAddress o where o.addressId =  (select u.venAddress.addressId from VenOrderItem u where u.venOrder.orderId="+orderList.get(0).getOrderId()+")", 0, 1);
					if(shippingAddressBlacklistList.size()>0){
						for(int i=0;i<shippingAddressBlacklistList.size();i++){					
								tempShippingAddress=shippingAddressBlacklistList.get(i).getStreetAddress1();						
						}
					}	
					
					System.out.println("get nomor Credit Card");
					List<VenOrderPaymentAllocation> ccBlacklistList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = "+orderList.get(0).getOrderId(), 0, 0);
					if(ccBlacklistList.size()>0){
						for(int i=0;i<ccBlacklistList.size();i++){							
								noCreditCard=ccBlacklistList.get(i).getVenOrderPayment().getMaskedCreditCardNumber()!=null?ccBlacklistList.get(i).getVenOrderPayment().getMaskedCreditCardNumber():"";							
						}
					}	
									
					List<FrdFraudSuspicionCase> fraudCaseList = fraudCaseSessionHome.queryByRange("select o from FrdFraudSuspicionCase o where o.venOrder.orderId = " + orderList.get(0).getOrderId(), 0, 1);
					if(fraudCaseList.size()>0){
						descFraud=fraudCaseList.get(0).getSuspicionReason();
					}
					
					FrdCustomerWhitelistBlacklist customerBlacklist = new FrdCustomerWhitelistBlacklist();
					customerBlacklist.setCustomerFullName(orderList.get(0).getVenCustomer().getVenParty().getFullOrLegalName());
					customerBlacklist.setAddress(tempAddress);
					customerBlacklist.setPhoneNumber(tempPhone);
					customerBlacklist.setHandphoneNumber(tempHandPhone);
					customerBlacklist.setShippingPhoneNumber(tempShippingPhone);
					customerBlacklist.setShippingHandphoneNumber(tempShippingHandPhone);
					customerBlacklist.setShippingAddress(tempShippingAddress);
					customerBlacklist.setCcNumber(noCreditCard);
					customerBlacklist.setEmail(tempEmail);
					customerBlacklist.setDescription(descFraud);
					customerBlacklist.setCreatedBy(username);
					customerBlacklist.setOrderTimestamp(orderList.get(0).getOrderDate());
					customerBlacklist.setTimestamp(new Timestamp(System.currentTimeMillis()));
					
					sessionHome.persistFrdCustomerWhitelistBlacklist(customerBlacklist);
										
					//cek ip blacklist sudah ada atau belum									
					List<FrdEntityBlacklist> ipAddressList = null;
					ipAddressList = ipBlacklistsessionHome.queryByRange("select o from FrdEntityBlacklist o where o.blacklistString='"+orderList.get(0).getIpAddress()+"'", 0, 0);
					
					if(ipAddressList.size()<1){
						//insert new ip blacklist record
						FrdEntityBlacklist ipBlacklist = new FrdEntityBlacklist();
						ipBlacklist.setBlacklistString(orderList.get(0).getIpAddress());
						ipBlacklist.setBlackOrWhiteList("BLACKLIST");
						ipBlacklist.setDescription("Based on investigation process for order ID: "+orderList.get(0).getWcsOrderId());
						ipBlacklist.setCreatedby(username);
						ipBlacklist.setBlacklistTimestamp(new Timestamp(System.currentTimeMillis()));
						
						ipBlacklistsessionHome.persistFrdEntityBlacklist(ipBlacklist);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		} finally {
			try {
				historyLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		return "0";
	}
}

