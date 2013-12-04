package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote;
import com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelist;
import com.gdn.venice.persistence.VenMigsUploadMaster;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch White List Information By Order
 * 
 * @author Arifin
 */

public class FetchWHiteListOrderHistory implements RafDsCommand {

	RafDsRequest request;
	
	public FetchWHiteListOrderHistory(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdCustomerWhitelistSessionEJBRemote sessionHome = (FrdCustomerWhitelistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistSessionEJBRemote.class, "FrdCustomerWhitelistSessionEJBBean");		
			VenOrderItemSessionEJBRemote orderItemsessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");		
			VenMigsUploadMasterSessionEJBRemote migsSessionHome = (VenMigsUploadMasterSessionEJBRemote) locator.lookup(VenMigsUploadMasterSessionEJBRemote.class, "VenMigsUploadMasterSessionEJBBean");
				
			List<FrdCustomerWhitelist> whiteList = null;			
			List<VenOrderItem> itemList = null;		
			List<VenMigsUploadMaster> migsList = null;	
			String query="";
			String email=null;
			String address=null;
			String cc=null;
			String ib=null;
			String exdate=null;
			/*
			 * order id tidak boleh kosong
			 */
			if(request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)!=""){
				query = "select o from VenOrderItem o where o.venOrder.wcsOrderId='"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"'";
				itemList = orderItemsessionHome.queryByRange(query,0, 0);
				if(itemList!=null & !itemList.isEmpty()){
					 email= itemList.get(0).getVenOrder().getVenCustomer().getCustomerUserName();
					 address=itemList.get(0).getVenAddress().getStreetAddress1();
				}
				
				query ="select o from VenMigsUploadMaster o where o.merchantTransactionReference like '"+request.getParams().get(DataNameTokens.VENORDER_WCSORDERID)+"%'";
				migsList = migsSessionHome.queryByRange(query, 0, 0);		
				if(migsList!=null & !migsList.isEmpty()){
					 cc=migsList.get(0).getCardNumber();
					 ib=migsList.get(0).getAcquirerId();
					 exdate=migsList.get(0).getCardExpiryMonth()+"/"+migsList.get(0).getCardExpiryYear();
				}
			}				
			/*
			 * query table whitelist sesuai dengan email, address , no cc, bank, exp date
			 */
			email=email!=null?"UPPER(o.email) like '"+email.toUpperCase()+"'":"o.email is null";
			address=address!=null?"UPPER(o.shippingaddress) like '"+address.toUpperCase()+"'":"o.shippingaddress is null";
			cc=cc!=null?"o.creditcardnumber like '"+cc+"'":"o.creditcardnumber is null";
			ib=ib!=null?"o.issuerbank like '"+ib+"'":"o.issuerbank is null";
			exdate=exdate!=null?"o.expireddate like '"+exdate+"'":"o.expireddate is null";
			query = "select o from FrdCustomerWhitelist o where "+email+" and " +address+" and " +cc+" and " +ib+" and " +exdate;						
			whiteList = sessionHome.queryByRange(query, 0, 0);
			for(int i=0; i<whiteList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdCustomerWhitelist list = whiteList.get(i);
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL, Util.isNull(list.getEmail(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS, Util.isNull(list.getShippingaddress(), "").toString());		
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER, Util.isNull(list.getCreditcardnumber(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK, Util.isNull(list.getIssuerbank(), "").toString());		
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE, Util.isNull(list.getExpireddate(), "").toString());
				dataList.add(map);				
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
