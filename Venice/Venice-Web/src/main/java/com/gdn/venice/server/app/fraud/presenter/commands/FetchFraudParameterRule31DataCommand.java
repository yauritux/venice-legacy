package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote;
import com.gdn.venice.persistence.FrdParameterRule31;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Parameter Rule 31 - Genuine List
 * 
 * @author Roland
 */

public class FetchFraudParameterRule31DataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchFraudParameterRule31DataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();
			FrdParameterRule31SessionEJBRemote sessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
			List<FrdParameterRule31> parameterRule31List = null;
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FrdParameterRule31 o";			
				parameterRule31List = sessionHome.queryByRange(query, 0, 50);
			} else {
				FrdParameterRule31 bl = new FrdParameterRule31();
				parameterRule31List = sessionHome.findByFrdParameterRule31Like(bl, criteria, 0, 0);
			}
			
			for(int i=0; i<parameterRule31List.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdParameterRule31 list = parameterRule31List.get(i);
				map.put(DataNameTokens.FRDPARAMETERRULE31_ID, list.getId().toString());
				map.put(DataNameTokens.FRDPARAMETERRULE31_EMAIL, Util.isNull(list.getEmail(), "").toString());
				map.put(DataNameTokens.FRDPARAMETERRULE31_CCNUMBER, Util.isNull(list.getNoCc(), "").toString());
				
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
