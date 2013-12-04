package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote;
import com.gdn.venice.persistence.RafApplicationObject;
import com.gdn.venice.persistence.RafApplicationObjectType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Module Configuration
 * 
 * @author Roland
 */

public class UpdateModuleConfigurationDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateModuleConfigurationDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafApplicationObject> rafApplicationObjectList = new ArrayList<RafApplicationObject>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafApplicationObject rafApplicationObject = new RafApplicationObject();
								
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafApplicationObjectSessionEJBRemote sessionHome = (RafApplicationObjectSessionEJBRemote) locator.lookup(RafApplicationObjectSessionEJBRemote.class, "RafApplicationObjectSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID)) {
						try{
							rafApplicationObject = sessionHome.queryByRange("select o from RafApplicationObject o where o.applicationObjectId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							rafApplicationObject.setApplicationObjectId(new Long(data.get(key)));
						}
						break;
					} 
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTUUID)) {
						rafApplicationObject.setApplicationObjectUuid(data.get(key));
					} else if (key.equals(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID)) {
						RafApplicationObjectType rafApplicationObjectType = new RafApplicationObjectType();
						rafApplicationObjectType.setApplicationObjectTypeId(new Long(data.get(key)));
						rafApplicationObject.setRafApplicationObjectType(rafApplicationObjectType);	
					} else if (key.equals(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTCANONICALNAME)) {
						rafApplicationObject.setApplicationObjectCanonicalName(data.get(key));	
					} else if (key.equals(DataNameTokens.RAFAPPLICATIONOBJECT_PARENTAPPLICATIONOBJECTID)) {
						RafApplicationObject parentModule = new RafApplicationObject();
						parentModule.setApplicationObjectId(new Long(data.get(key)));
						rafApplicationObject.setRafApplicationObject(parentModule);	
					} 
				}						
				rafApplicationObjectList.add(rafApplicationObject);			
			}

			sessionHome.mergeRafApplicationObjectList((ArrayList<RafApplicationObject>)rafApplicationObjectList);
			
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
