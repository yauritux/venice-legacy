package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserSessionEJBRemote;
import com.gdn.venice.persistence.RafUser;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for User
 * 
 * @author Anto
 */

public class UpdateUserDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateUserDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafUser> rafUserList = new ArrayList<RafUser>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafUser rafUser = new RafUser();
						
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafUserSessionEJBRemote sessionHome = (RafUserSessionEJBRemote) locator.lookup(RafUserSessionEJBRemote.class, "RafUserSessionEJBBean");

			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.RAFUSER_USERID)) {
						try{
							rafUser = sessionHome.queryByRange("select o from RafUser o where o.userId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							rafUser.setUserId(new Long(data.get(key)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.RAFUSER_LOGINNAME)) {
						rafUser.setLoginName(data.get(key));
					}
				}						
				
				rafUserList.add(rafUser);			
			}
			
			sessionHome.mergeRafUserList((ArrayList<RafUser>)rafUserList);
			
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
