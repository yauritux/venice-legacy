package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserSessionEJBRemote;
import com.gdn.venice.persistence.RafUser;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for User
 * 
 * @author Anto
 */

public class DeleteUserDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteUserDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafUser> rafUserList = new ArrayList<RafUser>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafUser rafUser = new RafUser();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFUSER_USERID)) {
					rafUser.setUserId(new Long(data.get(DataNameTokens.RAFUSER_USERID)));
				} 
			}						
			rafUserList.add(rafUser);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafUserSessionEJBRemote sessionHome = (RafUserSessionEJBRemote) locator.lookup(RafUserSessionEJBRemote.class, "RafUserSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<rafUserList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.RAFUSER_USERID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(rafUserList.get(i).getUserId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFGROUP_GROUPID));
				criteria.add(simpleCriteria);
			}
			
			rafUserList = sessionHome.findByRafUserLike(rafUser, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeRafUserList((ArrayList<RafUser>)rafUserList);
									
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
