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
import com.gdn.venice.facade.RafRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for User
 * 
 * @author Roland
 */

public class DeleteRoleDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteRoleDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafRole> rafRoleList = new ArrayList<RafRole>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafRole rafRole = new RafRole();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFROLE_ROLEID)) {
					rafRole.setRoleId(new Long(data.get(DataNameTokens.RAFROLE_ROLEID)));
				} 
			}						
			rafRoleList.add(rafRole);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafRoleSessionEJBRemote sessionHome = (RafRoleSessionEJBRemote) locator.lookup(RafRoleSessionEJBRemote.class, "RafRoleSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<rafRoleList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.RAFROLE_ROLEID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(rafRoleList.get(i).getRoleId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFROLE_ROLEID));
				criteria.add(simpleCriteria);
			}
			
			rafRoleList = sessionHome.findByRafRoleLike(rafRole, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeRafRoleList((ArrayList<RafRole>)rafRoleList);
									
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
