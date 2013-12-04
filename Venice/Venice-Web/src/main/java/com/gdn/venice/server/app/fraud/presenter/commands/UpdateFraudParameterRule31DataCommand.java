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
 * Update Command for Parameter Rule 31 - Genuine List
 * 
 * @author Roland
 */

public class UpdateFraudParameterRule31DataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateFraudParameterRule31DataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdParameterRule31> rule31List = new ArrayList<FrdParameterRule31>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdParameterRule31 entityRule31 = new FrdParameterRule31();
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();			
			FrdParameterRule31SessionEJBRemote sessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.FRDPARAMETERRULE31_ID)){
						try{
							entityRule31 = sessionHome.queryByRange("select o from FrdParameterRule31 o where o.id="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityRule31.setId(new Long(data.get(key)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.FRDPARAMETERRULE31_EMAIL)){
						entityRule31.setEmail(data.get(key));
					} else if(key.equals(DataNameTokens.FRDPARAMETERRULE31_CCNUMBER)){
						entityRule31.setNoCc(data.get(key));
					} 
				}						
				
				rule31List.add(entityRule31);			
			}
					
			sessionHome.mergeFrdParameterRule31List((ArrayList<FrdParameterRule31>)rule31List);
			
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
