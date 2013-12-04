package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafGroupSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Group Maintenance
 * 
 * @author Roland
 */

public class AddGroupDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddGroupDataCommand(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			RafGroupSessionEJBRemote sessionHome = (RafGroupSessionEJBRemote) locator.lookup(RafGroupSessionEJBRemote.class, "RafGroupSessionEJBBean");
			dataList=request.getData();
			
			RafGroup rafGroup = new RafGroup();
			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.RAFGROUP_GROUPID)){
						rafGroup.setGroupId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.RAFGROUP_GROUPNAME)){
						rafGroup.setGroupName(data.get(key));
					} else if(key.equals(DataNameTokens.RAFGROUP_GROUPDESC)){
						rafGroup.setGroupDesc(data.get(key));
					} 
				}
			}
					 
			rafGroup=sessionHome.persistRafGroup(rafGroup);
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
