package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Customer Blacklist Maintenance
 * 
 * @author Roland
 */

public class AddCustomerBlackListDataCommand implements RafDsCommand {

	RafDsRequest request;
	String username;
	
	public AddCustomerBlackListDataCommand(RafDsRequest request, String username){
		this.request=request;
		this.username = username;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			FrdCustomerWhitelistBlacklistSessionEJBRemote sessionHome = (FrdCustomerWhitelistBlacklistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistBlacklistSessionEJBRemote.class, "FrdCustomerWhitelistBlacklistSessionEJBBean");
			dataList=request.getData();
			
			FrdCustomerWhitelistBlacklist blacklist = new FrdCustomerWhitelistBlacklist();
			blacklist.setTimestamp(new Timestamp(System.currentTimeMillis()));	
			blacklist.setCreatedBy(username);
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)){
						blacklist.setId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME)){
						blacklist.setCustomerFullName(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_ADDRESS)){
						blacklist.setAddress(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_PHONENUMBER)){
						blacklist.setPhoneNumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_EMAIL)){
						blacklist.setEmail(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_DESCRIPTION)){
						blacklist.setDescription(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_HANDPHONENUMBER)){
						blacklist.setHandphoneNumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGPHONENUMBER)){
						blacklist.setShippingPhoneNumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGHANDPHONENUMBER)){
						blacklist.setShippingHandphoneNumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGADDRESS)){
						blacklist.setShippingAddress(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CCNUMBER)){
						blacklist.setCcNumber(data.get(key));
					}else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP)){
						Long logDate =new Long (data.get(key));
						blacklist.setOrderTimestamp(new Timestamp(logDate));
					}
				}
			}
					
			blacklist=sessionHome.persistFrdCustomerWhitelistBlacklist(blacklist);
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
