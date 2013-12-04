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
import com.gdn.venice.facade.RafProfileSessionEJBRemote;
import com.gdn.venice.persistence.RafProfile;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Blacklist Maintenance
 * 
 * @author Anto
 */

public class DeleteProfileDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteProfileDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafProfile> rafProfileList = new ArrayList<RafProfile>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafProfile rafProfile = new RafProfile();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFPROFILE_PROFILEID)) {
					rafProfile.setProfileId(new Long(data.get(DataNameTokens.RAFPROFILE_PROFILEID)));
				} 
			}						
			rafProfileList.add(rafProfile);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafProfileSessionEJBRemote sessionHome = (RafProfileSessionEJBRemote) locator.lookup(RafProfileSessionEJBRemote.class, "RafProfileSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<rafProfileList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.RAFPROFILE_PROFILEID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(rafProfileList.get(i).getProfileId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFPROFILE_PROFILEID));
				criteria.add(simpleCriteria);
			}
			
			rafProfileList = sessionHome.findByRafProfileLike(rafProfile, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeRafProfileList((ArrayList<RafProfile>)rafProfileList);
									
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
