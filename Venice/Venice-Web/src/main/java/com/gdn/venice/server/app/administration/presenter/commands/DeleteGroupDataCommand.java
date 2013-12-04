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
import com.gdn.venice.facade.RafGroupSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Group Maintenance
 * 
 * @author Roland
 */

public class DeleteGroupDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteGroupDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafGroup> rafGroupList = new ArrayList<RafGroup>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafGroup rafGroup = new RafGroup();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFGROUP_GROUPID)) {
					rafGroup.setGroupId(new Long(data.get(DataNameTokens.RAFGROUP_GROUPID)));
				} 
			}						
			rafGroupList.add(rafGroup);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafGroupSessionEJBRemote sessionHome = (RafGroupSessionEJBRemote) locator.lookup(RafGroupSessionEJBRemote.class, "RafGroupSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<rafGroupList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.RAFGROUP_GROUPID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(rafGroupList.get(i).getGroupId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFGROUP_GROUPID));
				criteria.add(simpleCriteria);
			}
			
			rafGroupList = sessionHome.findByRafGroupLike(rafGroup, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeRafGroupList((ArrayList<RafGroup>)rafGroupList);
									
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
