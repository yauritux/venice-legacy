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
 * Delete Command for Profile Detail
 * 
 * @author Roland
 */

public class DeleteProfileDetailDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteProfileDetailDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafProfilePermission> rafRolePermissionList = new ArrayList<RafProfilePermission>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafProfilePermission rafProfilePermission = new RafProfilePermission();
		String profileId="";
		String screenId="";
		String permissionTypeId="";
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID)) {
					RafProfile profile = new RafProfile();
					profile.setProfileId(new Long(data.get(key)));
					rafProfilePermission.setRafProfile(profile);
					profileId=new Long(data.get(key)).toString();
				} else if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID)) {
					RafApplicationObject screen = new RafApplicationObject();
					screen.setApplicationObjectId(new Long(data.get(key)));
					rafProfilePermission.setRafApplicationObject(screen);
					screenId=new Long(data.get(key)).toString();
				} else if (key.equals(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID)) {
					RafPermissionType permissionType = new RafPermissionType();
					permissionType.setPermissionTypeId(new Long(data.get(key)));
					rafProfilePermission.setRafPermissionType(permissionType);
					permissionTypeId=new Long(data.get(key)).toString();
				} 
			}						
			rafRolePermissionList.add(rafProfilePermission);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafProfilePermissionSessionEJBRemote sessionHome = (RafProfilePermissionSessionEJBRemote) locator.lookup(RafProfilePermissionSessionEJBRemote.class, "RafProfilePermissionSessionEJBBean");
			String query="";
			
			for (int i=0;i<rafRolePermissionList.size();i++) {
				query = "select o from RafProfilePermission o  where o.rafProfile.profileId="+profileId+" and o.rafApplicationObject.applicationObjectId="+screenId+" and o.rafPermissionType.permissionTypeId="+permissionTypeId;
				rafRolePermissionList=sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				sessionHome.removeRafProfilePermissionList((ArrayList<RafProfilePermission>)rafRolePermissionList);
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
