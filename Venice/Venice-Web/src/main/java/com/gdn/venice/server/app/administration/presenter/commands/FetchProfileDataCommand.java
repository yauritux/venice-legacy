package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafProfileSessionEJBRemote;
import com.gdn.venice.persistence.RafProfile;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Profile Maintenance
 * 
 * @author Anto
 */

public class FetchProfileDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchProfileDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			RafProfileSessionEJBRemote sessionHome = (RafProfileSessionEJBRemote) locator.lookup(RafProfileSessionEJBRemote.class, "RafProfileSessionEJBBean");			
			List<RafProfile> rafProfile = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafProfile o";			
				rafProfile = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafProfile rf = new RafProfile();
				rafProfile = sessionHome.findByRafProfileLike(rf, criteria, 0, 0);
			}
			
			for(int i=0; i<rafProfile.size();i++){				
				HashMap<String, String> map = new HashMap<String, String>();
				RafProfile list = rafProfile.get(i);				
				map.put(DataNameTokens.RAFPROFILE_PROFILEID, Util.isNull(list.getProfileId(), "").toString());
				map.put(DataNameTokens.RAFPROFILE_PROFILENAME, Util.isNull(list.getProfileName(), "").toString());
				map.put(DataNameTokens.RAFPROFILE_PROFILEDESC, Util.isNull(list.getProfileDesc(), "").toString());
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
