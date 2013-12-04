package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Role
 * 
 * @author Roland
 */

public class FetchRoleDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchRoleDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			RafRoleSessionEJBRemote sessionHome = (RafRoleSessionEJBRemote) locator.lookup(RafRoleSessionEJBRemote.class, "RafRoleSessionEJBBean");			
			List<RafRole> rafRole = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafRole o";			
				rafRole = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafRole role = new RafRole();
				rafRole = sessionHome.findByRafRoleLike(role, criteria, 0, 0);
			}
			
			for(int i=0; i<rafRole.size();i++){				
				HashMap<String, String> map = new HashMap<String, String>();
				RafRole list = rafRole.get(i);				
				map.put(DataNameTokens.RAFROLE_ROLEID, Util.isNull(list.getRoleId(), "").toString());
				map.put(DataNameTokens.RAFROLE_ROLENAME, Util.isNull(list.getRoleName(), "").toString());
				map.put(DataNameTokens.RAFROLE_ROLEDESC, Util.isNull(list.getRoleDesc(), "").toString());
				if(list.getRafRole()!=null){
					map.put(DataNameTokens.RAFROLE_PARENTROLE, Util.isNull(list.getRafRole().getRoleId(), "").toString());
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
