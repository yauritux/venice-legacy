package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Customer Blacklist Maintenance
 * 
 * @author Roland
 */

public class DeleteCustomerBlackListDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteCustomerBlackListDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdCustomerWhitelistBlacklist> blacklistList = new ArrayList<FrdCustomerWhitelistBlacklist>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdCustomerWhitelistBlacklist entityBlacklist = new FrdCustomerWhitelistBlacklist();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)) {
					entityBlacklist.setId(new Long(data.get(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID)));
				} 
			}						
			blacklistList.add(entityBlacklist);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FrdCustomerWhitelistBlacklistSessionEJBRemote sessionHome = (FrdCustomerWhitelistBlacklistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistBlacklistSessionEJBRemote.class, "FrdCustomerWhitelistBlacklistSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<blacklistList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(blacklistList.get(i).getId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID));
				criteria.add(simpleCriteria);
			}
			
			blacklistList = sessionHome.findByFrdCustomerWhitelistBlacklistLike(entityBlacklist, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeFrdCustomerWhitelistBlacklistList((ArrayList<FrdCustomerWhitelistBlacklist>)blacklistList);			
									
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
