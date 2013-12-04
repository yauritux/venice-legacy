package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafRoleProfileSessionEJBRemote;
import com.gdn.venice.persistence.RafRoleProfile;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Role Profile Detail
 * 
 * @author Roland
 */

public class FetchRoleDetailProfileDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchRoleDetailProfileDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		try{
			locator = new Locator<Object>();			
			RafRoleProfileSessionEJBRemote sessionHome = (RafRoleProfileSessionEJBRemote) locator.lookup(RafRoleProfileSessionEJBRemote.class, "RafRoleProfileSessionEJBBean");			
			List<RafRoleProfile> rafRoleProfile = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafRoleProfile o where o.rafRole.roleId="+request.getParams().get(DataNameTokens.RAFROLE_ROLEID).toString();
				rafRoleProfile = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafRoleProfile ru = new RafRoleProfile();
				JPQLSimpleQueryCriteria userIdCriteria = new JPQLSimpleQueryCriteria();
				userIdCriteria.setFieldName(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID);
				userIdCriteria.setOperator("equals");
				userIdCriteria.setValue(request.getParams().get(DataNameTokens.RAFROLE_ROLEID));
				userIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID));
				criteria.add(userIdCriteria);
				rafRoleProfile = sessionHome.findByRafRoleProfileLike(ru, criteria, 0, 0);
			}
			
			for(int i=0; i<rafRoleProfile.size();i++){				
				HashMap<String, String> map = null;				
				RafRoleProfile list = rafRoleProfile.get(i);				
				
				//check if the group has role
				if(list != null && list.getRafRoleProfileId() != null && !list.getRafRoleProfileId().toString().isEmpty()){
					map=new HashMap<String, String>();
					map.put(DataNameTokens.RAFROLEPROFILE_RAFROLEPROFILEID, Util.isNull(list.getRafRoleProfileId(), "").toString());
					map.put(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID, Util.isNull(list.getRafProfile().getProfileId(), "").toString());
					map.put(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID, Util.isNull(list.getRafRole().getRoleId(), "").toString());					
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
