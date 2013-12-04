package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafRoleProfileSessionEJBRemote;
import com.gdn.venice.persistence.RafProfile;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.persistence.RafRoleProfile;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Role User Detail
 * 
 * @author Roland
 */

public class DeleteRoleDetailProfileDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteRoleDetailProfileDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafRoleProfile> rafRoleProfileList = new ArrayList<RafRoleProfile>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafRoleProfile rafRoleProfile = new RafRoleProfile();
		String roleId="";
		String profileId="";
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID)) {
					RafProfile profile = new RafProfile();
					profile.setProfileId(new Long(data.get(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID)));
					rafRoleProfile.setRafProfile(profile);
					profileId=new Long(data.get(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID)).toString();
				} else if (key.equals(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID)) {
					RafRole role = new RafRole();
					role.setRoleId(new Long(data.get(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID)));
					rafRoleProfile.setRafRole(role);
					roleId=new Long(data.get(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID)).toString();
				} 
			}						
			rafRoleProfileList.add(rafRoleProfile);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafRoleProfileSessionEJBRemote sessionHome = (RafRoleProfileSessionEJBRemote) locator.lookup(RafRoleProfileSessionEJBRemote.class, "RafRoleProfileSessionEJBBean");
			String query="";
			
			for (int i=0;i<rafRoleProfileList.size();i++) {
				query = "select o from RafRoleProfile o  where o.rafProfile.profileId="+profileId+" and o.rafRole.roleId="+roleId;

				rafRoleProfileList=sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				sessionHome.removeRafRoleProfileList((ArrayList<RafRoleProfile>)rafRoleProfileList);
			}									
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
