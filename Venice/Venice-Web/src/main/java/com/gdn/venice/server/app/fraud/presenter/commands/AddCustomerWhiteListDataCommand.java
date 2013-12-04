package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote;
import com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelist;
import com.gdn.venice.persistence.VenMigsUploadMaster;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Customer  Maintenance
 * 
 * @author Arifin
 */

public class AddCustomerWhiteListDataCommand implements RafDsCommand {

	RafDsRequest request;
	String username;
	
	public AddCustomerWhiteListDataCommand(RafDsRequest request, String username){
		this.request=request;
		this.username = username;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<VenOrderPaymentAllocation> allocationList =null;
		List<VenOrderItem> orderItemList = null;	
		List<VenMigsUploadMaster> migsItemList = null;	
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			FrdCustomerWhitelistSessionEJBRemote sessionHome = (FrdCustomerWhitelistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistSessionEJBRemote.class, "FrdCustomerWhitelistSessionEJBBean");
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			VenMigsUploadMasterSessionEJBRemote migsSessionHome = (VenMigsUploadMasterSessionEJBRemote) locator.lookup(VenMigsUploadMasterSessionEJBRemote.class, "VenMigsUploadMasterSessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			
			dataList=request.getData();
			
			FrdCustomerWhitelist whiteList = new FrdCustomerWhitelist();
			whiteList.setCreated(new Timestamp(System.currentTimeMillis()));	
			whiteList.setCreatedby(username);
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				Iterator<String> iters=data.keySet().iterator();
								
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERID)){						
						if(!data.get(key).equals("")){
							whiteList.setOrderid(data.get(key));
							orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.wcsOrderId='"+data.get(key)+"'", 0, 0);
							if(!orderItemList.isEmpty() && orderItemList!=null && orderItemList.size()>0){
								Long date =new Long (orderItemList.get(0).getVenOrder().getOrderDate().getTime());
								whiteList.setOrdertimestamp(new Timestamp(date));
								whiteList.setCustomername(orderItemList.get(0).getVenOrder().getVenCustomer().getVenParty().getFullOrLegalName());
								whiteList.setShippingaddress(orderItemList.get(0).getVenAddress().getStreetAddress1());
								whiteList.setEmail(orderItemList.get(0).getVenOrder().getVenCustomer().getCustomerUserName());
							}

							migsItemList = migsSessionHome.queryByRange("select o from VenMigsUploadMaster o where o.merchantTransactionReference like '"+data.get(key)+"%'", 0, 0);
							if(!migsItemList.isEmpty() && migsItemList!=null && migsItemList.size()>0){					
								whiteList.setCreditcardnumber(migsItemList.get(0).getCardNumber());
								whiteList.setIssuerbank(migsItemList.get(0).getAcquirerId());
								whiteList.setExpireddate(migsItemList.get(0).getCardExpiryMonth()+"/"+migsItemList.get(0).getCardExpiryYear());
							}
							if(!orderItemList.isEmpty() && orderItemList!=null && orderItemList.size()>0){
									allocationList = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+orderItemList.get(0).getVenOrder().getOrderId(), 0, 0);
									if(!allocationList.isEmpty() && allocationList!=null && allocationList.size()>0){
										for(int k=0;k<allocationList.size();k++){
											//loop payment allocation, karena bisa multi payment, cari payment yang ada eci nya saja
											if(allocationList.get(k).getVenOrderPayment().getThreeDsSecurityLevelAuth()!=null || !allocationList.get(k).getVenOrderPayment().getThreeDsSecurityLevelAuth().isEmpty()){
												whiteList.setEci(allocationList.get(k).getVenOrderPayment().getThreeDsSecurityLevelAuth());
											}
										}
									}
							}	
						}
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE)){
						Long logDate =new Long (data.get(key));
						whiteList.setGenuinedate(new Timestamp(logDate));
					}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_REMARK)){
						whiteList.setRemark(data.get(key));
					}
				}
				
				if(whiteList.getOrderid()==null){
					while(iters.hasNext()){
						String key=iters.next();
						if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP)){
							Long logDate =new Long (data.get(key));
							whiteList.setOrdertimestamp(new Timestamp(logDate));
						}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME)){
							whiteList.setCustomername(data.get(key));
						}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS)){
							whiteList.setShippingaddress(data.get(key));
						}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL)){
							whiteList.setEmail(data.get(key));
						}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER)){
							whiteList.setCreditcardnumber(data.get(key));
						}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK)){
							whiteList.setIssuerbank(data.get(key));
						}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ECI)){
							whiteList.setEci(data.get(key));
						}else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE)){
							whiteList.setExpireddate(data.get(key));
						}
						
					}												
				}
			}					
			whiteList=sessionHome.persistFrdCustomerWhitelist(whiteList);
			rafDsResponse.setStatus(0);
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
