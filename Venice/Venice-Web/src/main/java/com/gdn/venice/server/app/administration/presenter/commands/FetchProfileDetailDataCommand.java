package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote;
import com.gdn.venice.persistence.RafProfilePermission;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Profile Detail
 * 
 * @author Roland
 */

public class FetchProfileDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchProfileDetailDataCommand(RafDsRequest request){
		this.request=request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		try{
			locator = new Locator<Object>();			
			RafProfilePermissionSessionEJBRemote sessionHome = (RafProfilePermissionSessionEJBRemote) locator.lookup(RafProfilePermissionSessionEJBRemote.class, "RafProfilePermissionSessionEJBBean");			
			List<RafProfilePermission> rafProfilePermission = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafProfilePermission o where o.rafProfile.profileId="+request.getParams().get(DataNameTokens.RAFPROFILE_PROFILEID).toString();
				rafProfilePermission = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafProfilePermission rp = new RafProfilePermission();
				JPQLSimpleQueryCriteria userIdCriteria = new JPQLSimpleQueryCriteria();
				userIdCriteria.setFieldName(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID);
				userIdCriteria.setOperator("equals");
				userIdCriteria.setValue(request.getParams().get(DataNameTokens.RAFPROFILE_PROFILEID));
				userIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID));
				criteria.add(userIdCriteria);
				rafProfilePermission = sessionHome.findByRafProfilePermissionLike(rp, criteria, 0, 0);
			}
			
			for(int i=0; i<rafProfilePermission.size();i++){				
				HashMap<String, String> map = null;				
				RafProfilePermission list = rafProfilePermission.get(i);				
				
				//check if the profile has detail
				if(list != null && list.getRafProfile() != null && list.getRafProfile().getProfileId() != null && !list.getRafProfile().getProfileId().toString().isEmpty()){
					map=new HashMap<String, String>();
					map.put(DataNameTokens.RAFPROFILEPERMISSION_RAFPROFILEPERMISSIONID, Util.isNull(list.getRafProfilePermissionId(), "").toString());
					map.put(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID, Util.isNull(list.getRafProfile().getProfileId(), "").toString());
					map.put(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID, Util.isNull(list.getRafApplicationObject().getApplicationObjectId(), "").toString());
					map.put(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID, Util.isNull(list.getRafPermissionType().getPermissionTypeId(), "").toString());					
				}
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
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
