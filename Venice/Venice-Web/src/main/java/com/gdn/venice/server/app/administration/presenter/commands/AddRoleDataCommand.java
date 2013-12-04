package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Role
 * 
 * @author Roland
 */

public class AddRoleDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddRoleDataCommand(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			RafRoleSessionEJBRemote sessionHome = (RafRoleSessionEJBRemote) locator.lookup(RafRoleSessionEJBRemote.class, "RafRoleSessionEJBBean");
			dataList=request.getData();
			
			RafRole rafRole = new RafRole();
			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.RAFROLE_ROLEID)){
						rafRole.setRoleId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.RAFROLE_ROLENAME)){
						rafRole.setRoleName(data.get(key));
					} else if(key.equals(DataNameTokens.RAFROLE_ROLEDESC)){
						rafRole.setRoleDesc(data.get(key));
					} else if(key.equals(DataNameTokens.RAFROLE_PARENTROLE)){
						RafRole parent = new RafRole();
						parent.setRoleId(new Long(data.get(key)));
						rafRole.setRafRole(parent);
					} 
				}
			}
					 
			rafRole=sessionHome.persistRafRole(rafRole);
			rafDsResponse.setStatus(0);
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
