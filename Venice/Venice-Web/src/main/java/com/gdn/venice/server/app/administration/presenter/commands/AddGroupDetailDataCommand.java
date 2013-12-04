package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafGroupRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.persistence.RafGroupRole;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Group Detail
 * 
 * @author Roland
 */

public class AddGroupDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddGroupDetailDataCommand(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<RafGroupRole> rafGroupRoleListCheck = new ArrayList<RafGroupRole>();
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();	
		Boolean status=false;
		String roleId="";
		
		//because only role id can be changed in screen, so only role id sent from servlet, group id must be sent as parameter.
		String groupId=request.getParams().get(DataNameTokens.RAFGROUP_GROUPID).toString();
		try{
			locator = new Locator<Object>();
			RafGroupRoleSessionEJBRemote sessionHome = (RafGroupRoleSessionEJBRemote) locator.lookup(RafGroupRoleSessionEJBRemote.class, "RafGroupRoleSessionEJBBean");
			dataList=request.getData();
			
			RafGroupRole rafGroupRole = new RafGroupRole();			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID)){
						RafGroup group = new RafGroup();
						group.setGroupId(new Long(data.get(key)));
						rafGroupRole.setRafGroup(group);
					} else if(key.equals(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID)){
						RafRole role =  new RafRole();
						role.setRoleId(new Long(data.get(key)));
						rafGroupRole.setRafRole(role);
						roleId=new Long(data.get(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID)).toString();
					}
				}
			}
			
			//check first if the group and role already exist in database
			String query = "select o from RafGroupRole o where o.rafGroup.groupId="+groupId+" and o.rafRole.roleId="+roleId;
			rafGroupRoleListCheck = sessionHome.queryByRange(query, 0, 0);
			if(rafGroupRoleListCheck.size()>0){
				status=true;
			}else{
				status=false;
			}
			
			if(status==false){
				//data is unique so update the database
				rafGroupRole=sessionHome.persistRafGroupRole(rafGroupRole);
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
