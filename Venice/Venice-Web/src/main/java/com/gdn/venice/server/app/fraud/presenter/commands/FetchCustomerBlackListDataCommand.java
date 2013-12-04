package com.gdn.venice.server.app.fraud.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Customer Blacklist Maintenance
 * 
 * @author Roland
 */

public class FetchCustomerBlackListDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchCustomerBlackListDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdCustomerWhitelistBlacklistSessionEJBRemote sessionHome = (FrdCustomerWhitelistBlacklistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistBlacklistSessionEJBRemote.class, "FrdCustomerWhitelistBlacklistSessionEJBBean");			
			List<FrdCustomerWhitelistBlacklist> blackList = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FrdCustomerWhitelistBlacklist o";			
				blackList = sessionHome.queryByRange(query, 0, 50);
			} else {
				FrdCustomerWhitelistBlacklist bl = new FrdCustomerWhitelistBlacklist();
				blackList = sessionHome.findByFrdCustomerWhitelistBlacklistLike(bl, criteria, 0, 0);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0; i<blackList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdCustomerWhitelistBlacklist list = blackList.get(i);
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID, Util.isNull(list.getId(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME, Util.isNull(list.getCustomerFullName(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_ADDRESS, Util.isNull(list.getAddress(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_PHONENUMBER, Util.isNull(list.getPhoneNumber(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_EMAIL, Util.isNull(list.getEmail(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_DESCRIPTION, Util.isNull(list.getDescription(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_CREATEDBY, Util.isNull(list.getCreatedBy(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_HANDPHONENUMBER, Util.isNull(list.getHandphoneNumber(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGPHONENUMBER, Util.isNull(list.getShippingPhoneNumber(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGHANDPHONENUMBER, Util.isNull(list.getShippingHandphoneNumber(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGADDRESS, Util.isNull(list.getShippingAddress(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_CCNUMBER, Util.isNull(list.getCcNumber() ,"").toString());
				if(list.getOrderTimestamp()!=null){
					String s =  sdf.format(list.getOrderTimestamp());
			        StringBuilder sb = new StringBuilder(s);
					map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP, Util.isNull(sb.toString(), "").toString());
				}else{
					map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP, "");
				}						
				String s =  sdf.format(list.getTimestamp());
				StringBuilder sb = new StringBuilder(s);
				map.put(DataNameTokens.FRDCUSTOMERBLACKLIST_BLACKLISTTIMESTAMP, sb.toString());
				
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
