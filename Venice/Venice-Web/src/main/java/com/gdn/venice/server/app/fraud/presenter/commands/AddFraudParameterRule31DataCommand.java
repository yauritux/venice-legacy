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
 * Add Command for Parameter Rule 31 - Genuine List
 * 
 * @author Roland
 */

public class AddFraudParameterRule31DataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddFraudParameterRule31DataCommand(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			FrdParameterRule31SessionEJBRemote sessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
			dataList=request.getData();
			
			FrdParameterRule31 entityRule31 = new FrdParameterRule31();
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FRDPARAMETERRULE31_ID)){
						entityRule31.setId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.FRDPARAMETERRULE31_EMAIL)){
						entityRule31.setEmail(data.get(key));				
					}else if(key.equals(DataNameTokens.FRDPARAMETERRULE31_CCNUMBER)){
						entityRule31.setNoCc(data.get(key));
					}
				}
			}
			sessionHome.persistFrdParameterRule31(entityRule31);

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
