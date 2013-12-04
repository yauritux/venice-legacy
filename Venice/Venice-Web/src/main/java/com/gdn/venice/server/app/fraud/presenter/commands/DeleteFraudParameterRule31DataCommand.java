package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule31;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Remove Command for Parameter Rule 31 - Genuine List
 * 
 * @author Roland
 */

public class DeleteFraudParameterRule31DataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteFraudParameterRule31DataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdParameterRule31> list = new ArrayList<FrdParameterRule31>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdParameterRule31 entityRule31 = new FrdParameterRule31();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if(key.equals(DataNameTokens.FRDPARAMETERRULE31_ID)){
					entityRule31.setId(new Long(data.get(key)));
				} 
			}						
			list.add(entityRule31);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FrdParameterRule31SessionEJBRemote sessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
			sessionHome.removeFrdParameterRule31List((ArrayList<FrdParameterRule31>)list);
			
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
