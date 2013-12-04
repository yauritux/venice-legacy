package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote;
import com.gdn.venice.persistence.RafApplicationObject;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Module Configuration
 * 
 * @author Roland
 */

public class DeleteModuleConfigurationDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteModuleConfigurationDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafApplicationObject> rafApplicationObjectList = new ArrayList<RafApplicationObject>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafApplicationObject rafApplicationObject = new RafApplicationObject();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID)) {
					rafApplicationObject.setApplicationObjectId(new Long(data.get(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID)));
				} 
			}						
			rafApplicationObjectList.add(rafApplicationObject);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafApplicationObjectSessionEJBRemote sessionHome = (RafApplicationObjectSessionEJBRemote) locator.lookup(RafApplicationObjectSessionEJBRemote.class, "RafApplicationObjectSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<rafApplicationObjectList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(rafApplicationObjectList.get(i).getApplicationObjectId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID));
				criteria.add(simpleCriteria);
			}
			
			rafApplicationObjectList = sessionHome.findByRafApplicationObjectLike(rafApplicationObject, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeRafApplicationObjectList((ArrayList<RafApplicationObject>)rafApplicationObjectList);
									
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
