package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote;
import com.gdn.venice.persistence.RafApplicationObject;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Module Configuration
 * 
 * @author Roland
 */

public class FetchModuleConfigurationDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchModuleConfigurationDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			RafApplicationObjectSessionEJBRemote sessionHome = (RafApplicationObjectSessionEJBRemote) locator.lookup(RafApplicationObjectSessionEJBRemote.class, "RafApplicationObjectSessionEJBBean");			
			List<RafApplicationObject> module = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from RafApplicationObject o";			
				module = sessionHome.queryByRange(query, 0, 50);
			} else {
				RafApplicationObject rao = new RafApplicationObject();
				module = sessionHome.findByRafApplicationObjectLike(rao, criteria, 0, 0);
			}
			
			for(int i=0; i<module.size();i++){				
				HashMap<String, String> map = new HashMap<String, String>();
				RafApplicationObject list = module.get(i);				
				map.put(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID, Util.isNull(list.getApplicationObjectId(), "").toString());
				map.put(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTUUID, Util.isNull(list.getApplicationObjectUuid(), "").toString());
				map.put(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID, Util.isNull(list.getRafApplicationObjectType().getApplicationObjectTypeId(), "").toString());
				map.put(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTCANONICALNAME, Util.isNull(list.getApplicationObjectCanonicalName(), "").toString());
				if(list.getRafApplicationObject()!= null){
					map.put(DataNameTokens.RAFAPPLICATIONOBJECT_PARENTAPPLICATIONOBJECTID, Util.isNull(list.getRafApplicationObject().getApplicationObjectId(), "").toString());
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
