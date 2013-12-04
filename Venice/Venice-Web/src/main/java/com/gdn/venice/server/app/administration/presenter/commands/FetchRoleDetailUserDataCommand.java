package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafUserRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Role User Detail
 * 
 * @author Roland
 */

public class FetchRoleDetailUserDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchRoleDetailUserDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		try{
			locator = new Locator<Object>();			
			RafUserRoleSessionEJBRemote sessionHome = (RafUserRoleSessionEJBRemote) locator.lookup(RafUserRoleSessionEJBRemote.class, "RafUserRoleSessionEJBBean");			
			List<RafUserRole> rafUserRole = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafUserRole o where o.rafRole.roleId="+request.getParams().get(DataNameTokens.RAFROLE_ROLEID).toString();
				rafUserRole = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafUserRole ru = new RafUserRole();
				JPQLSimpleQueryCriteria userIdCriteria = new JPQLSimpleQueryCriteria();
				userIdCriteria.setFieldName(DataNameTokens.RAFROLE_RAFUSERROLES_USERID);
				userIdCriteria.setOperator("equals");
				userIdCriteria.setValue(request.getParams().get(DataNameTokens.RAFROLE_ROLEID));
				userIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFROLE_RAFUSERROLES_USERID));
				criteria.add(userIdCriteria);
				rafUserRole = sessionHome.findByRafUserRoleLike(ru, criteria, 0, 0);
			}
			
			for(int i=0; i<rafUserRole.size();i++){				
				HashMap<String, String> map = null;				
				RafUserRole list = rafUserRole.get(i);				
				
				//check if the group has role
				if(!list.getRafUserRoleId().toString().isEmpty()){
					map=new HashMap<String, String>();
					map.put(DataNameTokens.RAFUSERROLE_RAFUSERROLEID, Util.isNull(list.getRafUserRoleId(), "").toString());
					map.put(DataNameTokens.RAFROLE_RAFUSERROLES_USERID, Util.isNull(list.getRafUser().getUserId(), "").toString());
					map.put(DataNameTokens.RAFROLE_RAFUSERROLES_ROLEID, Util.isNull(list.getRafRole().getRoleId(), "").toString());					
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
