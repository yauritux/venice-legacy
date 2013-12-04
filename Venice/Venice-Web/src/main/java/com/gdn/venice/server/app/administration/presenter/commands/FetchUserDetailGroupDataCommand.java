package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote;
import com.gdn.venice.persistence.RafUserGroupMembership;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for User Group Detail
 * 
 * @author Roland
 */

public class FetchUserDetailGroupDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchUserDetailGroupDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		try{
			locator = new Locator<Object>();			
			RafUserGroupMembershipSessionEJBRemote sessionHome = (RafUserGroupMembershipSessionEJBRemote) locator.lookup(RafUserGroupMembershipSessionEJBRemote.class, "RafUserGroupMembershipSessionEJBBean");			
			List<RafUserGroupMembership> rafUserGroupMembership = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafUserGroupMembership o where o.rafUser.userId="+request.getParams().get(DataNameTokens.RAFUSER_USERID).toString();
				rafUserGroupMembership = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafUserGroupMembership ru = new RafUserGroupMembership();
				JPQLSimpleQueryCriteria userIdCriteria = new JPQLSimpleQueryCriteria();
				userIdCriteria.setFieldName(DataNameTokens.RAFUSER_RAFUSERROLES_USERID);
				userIdCriteria.setOperator("equals");
				userIdCriteria.setValue(request.getParams().get(DataNameTokens.RAFUSER_USERID));
				userIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFUSER_RAFUSERROLES_USERID));
				criteria.add(userIdCriteria);
				rafUserGroupMembership = sessionHome.findByRafUserGroupMembershipLike(ru, criteria, 0, 0);
			}
			
			for(int i=0; i<rafUserGroupMembership.size();i++){				
				HashMap<String, String> map = null;				
				RafUserGroupMembership list = rafUserGroupMembership.get(i);				
				
				//check if the group has role
				if(!list.getRafUserGroupMembershipId().toString().isEmpty()){
					map=new HashMap<String, String>();
					map.put(DataNameTokens.RAFUSERGROUP_RAFUSERGROUPID, Util.isNull(list.getRafUserGroupMembershipId(), "").toString());
					map.put(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID, Util.isNull(list.getRafUser().getUserId(), "").toString());
					map.put(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID, Util.isNull(list.getRafGroup().getGroupId(), "").toString());					
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
