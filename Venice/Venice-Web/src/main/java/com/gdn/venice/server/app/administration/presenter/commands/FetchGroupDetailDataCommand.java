package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafGroupRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafGroupRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Group Detail
 * 
 * @author Roland
 */

public class FetchGroupDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchGroupDetailDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		try{
			locator = new Locator<Object>();			
			RafGroupRoleSessionEJBRemote sessionHome = (RafGroupRoleSessionEJBRemote) locator.lookup(RafGroupRoleSessionEJBRemote.class, "RafGroupRoleSessionEJBBean");			
			List<RafGroupRole> rafGroupRole = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafGroupRole o where o.rafGroup.groupId="+request.getParams().get(DataNameTokens.RAFGROUP_GROUPID).toString();
				rafGroupRole = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafGroupRole rg = new RafGroupRole();
				JPQLSimpleQueryCriteria groupIdCriteria = new JPQLSimpleQueryCriteria();
				groupIdCriteria.setFieldName(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID);
				groupIdCriteria.setOperator("equals");
				groupIdCriteria.setValue(request.getParams().get(DataNameTokens.RAFGROUP_GROUPID));
				groupIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID));
				criteria.add(groupIdCriteria);
				rafGroupRole = sessionHome.findByRafGroupRoleLike(rg, criteria, 0, 0);
			}
			
			for(int i=0; i<rafGroupRole.size();i++){				
				HashMap<String, String> map = null;				
				RafGroupRole list = rafGroupRole.get(i);				
				
				//check if the group has role
				if(!list.getRafGroupRoleId().toString().isEmpty()){
					map=new HashMap<String, String>();
					map.put(DataNameTokens.RAFGROUPROLE_RAFGROUPROLEID, Util.isNull(list.getRafGroupRoleId(), "").toString());
					map.put(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID, Util.isNull(list.getRafGroup().getGroupId(), "").toString());
					map.put(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID, Util.isNull(list.getRafRole().getRoleId(), "").toString());					
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
