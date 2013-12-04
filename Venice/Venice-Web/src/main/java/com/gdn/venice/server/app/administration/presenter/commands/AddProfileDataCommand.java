package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafProfileSessionEJBRemote;
import com.gdn.venice.persistence.RafProfile;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Blacklist Maintenance
 * 
 * @author Anto
 */

public class AddProfileDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddProfileDataCommand(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			RafProfileSessionEJBRemote sessionHome = (RafProfileSessionEJBRemote) locator.lookup(RafProfileSessionEJBRemote.class, "RafProfileSessionEJBBean");
			dataList=request.getData();
			
			RafProfile rafProfile = new RafProfile();
			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.RAFPROFILE_PROFILEID)){
						rafProfile.setProfileId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.RAFPROFILE_PROFILENAME)){
						rafProfile.setProfileName(data.get(key));
					} else if(key.equals(DataNameTokens.RAFPROFILE_PROFILEDESC)){
						rafProfile.setProfileDesc(data.get(key));
					} 
				}
			}
					
			rafProfile=sessionHome.persistRafProfile(rafProfile);
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
