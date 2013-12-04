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
 * Add Command for Role Profile Detail
 * 
 * @author Roland
 */

public class AddRoleDetailProfileDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddRoleDetailProfileDataCommand(RafDsRequest request){
		this.request=request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<RafRoleProfile> rafRoleProfileListCheck = new ArrayList<RafRoleProfile>();
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();	
		Boolean status=false;
		String profileId="";
		
		//because only user id can be changed in screen, so only user id sent from servlet, role id must be sent as parameter.
		String roleId=request.getParams().get(DataNameTokens.RAFROLE_ROLEID).toString();
		try{
			locator = new Locator<Object>();
			RafRoleProfileSessionEJBRemote sessionHome = (RafRoleProfileSessionEJBRemote) locator.lookup(RafRoleProfileSessionEJBRemote.class, "RafRoleProfileSessionEJBBean");
			dataList=request.getData();
			
			RafRoleProfile rafRoleProfile = new RafRoleProfile();			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID)){
						RafProfile profile = new RafProfile();
						profile.setProfileId(new Long(data.get(key)));
						rafRoleProfile.setRafProfile(profile);
						profileId=new Long(data.get(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID)).toString();
					} else if(key.equals(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID)){
						RafRole role =  new RafRole();
						role.setRoleId(new Long(data.get(key)));
						rafRoleProfile.setRafRole(role);
					}
				}
			}
			
			//check first if the user and role already exist in database
			String query = "select o from RafRoleProfile o where o.rafProfile.profileId="+profileId+" and o.rafRole.roleId="+roleId;
			rafRoleProfileListCheck = sessionHome.queryByRange(query, 0, 0);
			if(rafRoleProfileListCheck.size()>0){
				status=true;
			}else{
				status=false;
			}
			
			if(status==false){
				//data is unique so update the database
				rafRoleProfile=sessionHome.persistRafRoleProfile(rafRoleProfile);
				rafDsResponse.setStatus(0);
			}else{
				//data already exist
				rafDsResponse.setStatus(2);
			}
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
