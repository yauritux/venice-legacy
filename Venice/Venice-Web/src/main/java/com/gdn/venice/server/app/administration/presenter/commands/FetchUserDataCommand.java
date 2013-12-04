package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserSessionEJBRemote;
import com.gdn.venice.persistence.RafUser;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Profile Maintenance
 * 
 * @author Anto
 */

public class FetchUserDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchUserDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			RafUserSessionEJBRemote sessionHome = (RafUserSessionEJBRemote) locator.lookup(RafUserSessionEJBRemote.class, "RafUserSessionEJBBean");			
			List<RafUser> rafUser = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafUser o";			
				rafUser = sessionHome.queryByRange(query, 0, 50);
			} else {
				RafUser ru = new RafUser();
				rafUser = sessionHome.findByRafUserLike(ru, criteria, 0, 0);
			}
			
			for(int i=0; i<rafUser.size();i++){				
				HashMap<String, String> map = new HashMap<String, String>();
				RafUser list = rafUser.get(i);				
				map.put(DataNameTokens.RAFUSER_USERID, Util.isNull(list.getUserId(), "").toString());
				map.put(DataNameTokens.RAFUSER_LOGINNAME, Util.isNull(list.getLoginName(), "").toString());
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
