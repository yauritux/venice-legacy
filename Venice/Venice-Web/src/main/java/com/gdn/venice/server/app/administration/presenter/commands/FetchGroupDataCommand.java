package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafGroupSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Group Maintenance
 * 
 * @author Anto
 */

public class FetchGroupDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchGroupDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			RafGroupSessionEJBRemote sessionHome = (RafGroupSessionEJBRemote) locator.lookup(RafGroupSessionEJBRemote.class, "RafGroupSessionEJBBean");			
			List<RafGroup> rafGroup = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafGroup o";			
				rafGroup = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				RafGroup rg = new RafGroup();
				rafGroup = sessionHome.findByRafGroupLike(rg, criteria, 0, 0);
			}
			
			for(int i=0; i<rafGroup.size();i++){				
				HashMap<String, String> map = new HashMap<String, String>();
				RafGroup list = rafGroup.get(i);				
				map.put(DataNameTokens.RAFGROUP_GROUPID, Util.isNull(list.getGroupId(), "").toString());
				map.put(DataNameTokens.RAFGROUP_GROUPNAME, Util.isNull(list.getGroupName(), "").toString());
				map.put(DataNameTokens.RAFGROUP_GROUPDESC, Util.isNull(list.getGroupDesc(), "").toString());
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
