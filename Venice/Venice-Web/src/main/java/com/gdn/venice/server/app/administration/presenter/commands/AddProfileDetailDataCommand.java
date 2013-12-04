package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote;
import com.gdn.venice.persistence.RafApplicationObject;
import com.gdn.venice.persistence.RafPermissionType;
import com.gdn.venice.persistence.RafProfile;
import com.gdn.venice.persistence.RafProfilePermission;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Profile Detail
 * 
 * @author Roland
 */

public class AddProfileDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddProfileDetailDataCommand(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<RafProfilePermission> rafProfilePermissionListCheck = new ArrayList<RafProfilePermission>();
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();	
		Boolean status=false;
		String profileId="";
		String screenId="";
		String permissionTypeId="";
		
		//because only screen and permission can be changed in screen, so only screen and permission id sent from servlet, profile id must be sent as parameter.
		profileId=request.getParams().get(DataNameTokens.RAFPROFILE_PROFILEID).toString();
		try{
			locator = new Locator<Object>();
			RafProfilePermissionSessionEJBRemote sessionHome = (RafProfilePermissionSessionEJBRemote) locator.lookup(RafProfilePermissionSessionEJBRemote.class, "RafProfilePermissionSessionEJBBean");
			dataList=request.getData();
			
			RafProfilePermission rafProfilePermission = new RafProfilePermission();		
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					
					if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID)) {
						RafProfile profile = new RafProfile();
						profile.setProfileId(new Long(profileId));
						rafProfilePermission.setRafProfile(profile);
					} else if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID)) {						
						screenId=new Long(data.get(key)).toString();
						RafApplicationObject screen = new RafApplicationObject();
						screen.setApplicationObjectId(new Long(screenId));
						rafProfilePermission.setRafApplicationObject(screen);
					} else if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID)) {						
						permissionTypeId=new Long(data.get(key)).toString();
						RafPermissionType permission =  new RafPermissionType();
						permission.setPermissionTypeId(new Long(permissionTypeId));
						rafProfilePermission.setRafPermissionType(permission);
					} 
				}
			}
			
			//check first if the profile and screen already exist in database
			String query = "select o from RafProfilePermission o  where o.rafProfile.profileId="+profileId+" and o.rafApplicationObject.applicationObjectId="+screenId+" and o.rafPermissionType.permissionTypeId="+permissionTypeId;
			rafProfilePermissionListCheck = sessionHome.queryByRange(query, 0, 0);
			if(rafProfilePermissionListCheck.size()>0){
				status=true;
			}else{
				status=false;
			}
			
			if(status==false){
				//data is unique so update the database
				rafProfilePermission=sessionHome.persistRafProfilePermission(rafProfilePermission);
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
