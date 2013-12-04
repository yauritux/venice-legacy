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
 * Update Command for Customer Blacklist Maintenance
 * 
 * @author Roland
 */

public class UpdateCustomerBlackListDataCommand implements RafDsCommand {
	RafDsRequest request;
	String username;
	
	public UpdateCustomerBlackListDataCommand(RafDsRequest request, String username) {
		this.request = request;
		this.username = username;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdCustomerWhitelistBlacklist> blacklistList = new ArrayList<FrdCustomerWhitelistBlacklist>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdCustomerWhitelistBlacklist entityBlacklist = new FrdCustomerWhitelistBlacklist();
		
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FrdCustomerWhitelistBlacklistSessionEJBRemote sessionHome = (FrdCustomerWhitelistBlacklistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistBlacklistSessionEJBRemote.class, "FrdCustomerWhitelistBlacklistSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)) {
						try{
							entityBlacklist = sessionHome.queryByRange("select o from FrdCustomerWhitelistBlacklist o where o.id = "+ new Long(data.get(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)), 0, 1).get(0);
						}catch(NumberFormatException e){
							entityBlacklist.setId(new Long(data.get(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)));
						}
					}
				}
				
				iter = data.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)) {
						entityBlacklist.setId(new Long(data.get(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)));					
					} else if (key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME)) {
						entityBlacklist.setCustomerFullName(data.get(key));
					} else if (key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_ADDRESS)) {
						entityBlacklist.setAddress(data.get(key));
					} else if (key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_PHONENUMBER)) {
						entityBlacklist.setPhoneNumber(data.get(key));
					} else if (key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_EMAIL)) {
						entityBlacklist.setEmail(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_DESCRIPTION)){
						entityBlacklist.setDescription(data.get(key));
					}else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_HANDPHONENUMBER)){
						entityBlacklist.setHandphoneNumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGPHONENUMBER)){
						entityBlacklist.setShippingPhoneNumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGHANDPHONENUMBER)){
						entityBlacklist.setShippingHandphoneNumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGADDRESS)){
						entityBlacklist.setShippingAddress(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CCNUMBER)){
						entityBlacklist.setCcNumber(data.get(key));
					}else if(key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP)){
						Long logDate =new Long (data.get(key));
						entityBlacklist.setOrderTimestamp(new Timestamp(logDate));
					}
				}			
				entityBlacklist.setCreatedBy(username);
				entityBlacklist.setTimestamp(new Timestamp(System.currentTimeMillis()));
				blacklistList.add(entityBlacklist);			
			}
			
			sessionHome.mergeFrdCustomerWhitelistBlacklistList((ArrayList<FrdCustomerWhitelistBlacklist>)blacklistList);
			
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
