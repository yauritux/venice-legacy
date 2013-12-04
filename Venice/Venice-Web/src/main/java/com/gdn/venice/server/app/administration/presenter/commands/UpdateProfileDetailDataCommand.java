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
 * Update Command for Profile Detail
 * 
 * @author Roland
 */

public class UpdateProfileDetailDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateProfileDetailDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<RafProfilePermission> rafProfilePermissionList = new ArrayList<RafProfilePermission>();
		List<HashMap<String,String >> dataList = request.getData();		
		
		RafProfilePermission rafProfilePermission = new RafProfilePermission();
			
		Locator<Object> locator = null;
		try {
			if(checkDuplicateData(dataList)){
				//data is unique so update the database
				locator= new Locator<Object>();
				RafProfilePermissionSessionEJBRemote sessionHome = (RafProfilePermissionSessionEJBRemote) locator.lookup(RafProfilePermissionSessionEJBRemote.class, "RafProfilePermissionSessionEJBBean");

				for (int i=0;i<dataList.size();i++) {
					Map<String, String> data = dataList.get(i);
					Iterator<String> iter = data.keySet().iterator();
		
					while (iter.hasNext()) {
						String key = iter.next();
						if(key.equals(DataNameTokens.RAFPROFILEPERMISSION_RAFPROFILEPERMISSIONID)){
							try{
								rafProfilePermission = sessionHome.queryByRange("select o from RafProfilePermission o where o.rafProfilePermissionId="+new Long(data.get(key)), 0, 1).get(0);
							}catch(IndexOutOfBoundsException e){
								rafProfilePermission.setRafProfilePermissionId(new Long(data.get(key)));
							}
							break;
						}
					}						
					
					iter = data.keySet().iterator();
					
					while (iter.hasNext()) {
						String key = iter.next();
						if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID)) {
							RafProfile profile = new RafProfile();
							profile.setProfileId(new Long(data.get(key)));
							rafProfilePermission.setRafProfile(profile);
						} else if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID)) {											
							RafApplicationObject screen = new RafApplicationObject();
							screen.setApplicationObjectId(new Long(data.get(key)));
							rafProfilePermission.setRafApplicationObject(screen);
						} else if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID)) {											
							RafPermissionType permission =  new RafPermissionType();
							permission.setPermissionTypeId(new Long(data.get(key)));
							rafProfilePermission.setRafPermissionType(permission);
						}
					}						
					
					rafProfilePermissionList.add(rafProfilePermission);			
				}
				
				sessionHome.mergeRafProfilePermissionList((ArrayList<RafProfilePermission>)rafProfilePermissionList);
				rafDsResponse.setStatus(0);
			}else{
				//data already exist
				rafDsResponse.setStatus(2);
			}
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
	
	public boolean checkDuplicateData(List <HashMap<String, String>> dataList){
		Locator<Object> locator = null;
		boolean set=true;

		try{
			locator = new Locator<Object>();
			RafProfilePermissionSessionEJBRemote sessionHome = (RafProfilePermissionSessionEJBRemote) locator.lookup(RafProfilePermissionSessionEJBRemote.class, "RafProfilePermissionSessionEJBBean");
			String query="";
			String profileIdTemp="";
			String screenIdTemp="";
			String permissionIdTemp="";
			List<RafProfilePermission> rafProfilePermissionListCheck = new ArrayList<RafProfilePermission>();
			
			Map<String, String> data = dataList.get(0);
					
			query = "select o from RafProfilePermission o where o.rafProfilePermissionId=" + data.get(DataNameTokens.RAFPROFILEPERMISSION_RAFPROFILEPERMISSIONID);
			rafProfilePermissionListCheck = sessionHome.queryByRange(query, 0, 0);

			if (data.get(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID) != null) {
				profileIdTemp = data.get(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID);
			} else {
				profileIdTemp = rafProfilePermissionListCheck.get(0).getRafProfile().getProfileId().toString();
			}
			if (data.get(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID) != null) {
				screenIdTemp = data.get(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID);
			} else {
				screenIdTemp = rafProfilePermissionListCheck.get(0).getRafApplicationObject().getApplicationObjectId().toString();
			}
			if (data.get(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID) != null) {
				permissionIdTemp = data.get(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID);
			} else {
				permissionIdTemp = rafProfilePermissionListCheck.get(0).getRafPermissionType().getPermissionTypeId().toString();
			}				

			query="select o from RafProfilePermission o where o.rafProfile.profileId="+profileIdTemp+" and o.rafApplicationObject.applicationObjectId="+screenIdTemp+" and o.rafPermissionType.permissionTypeId="+permissionIdTemp;
			List<RafProfilePermission> rafProfilePermissionListDetailCheckDuplicate = new ArrayList<RafProfilePermission>();
			rafProfilePermissionListDetailCheckDuplicate=sessionHome.queryByRange(query, 0, 0);
			
			if(rafProfilePermissionListDetailCheckDuplicate.size()>0){
				set=false;
			}else{
				set=true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return set;
	}
}
