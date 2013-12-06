package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudCaseHistoryPK;
import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for History Log
 * 
 * @author Roland
 */

public class AddFraudCaseHistoryLogData implements RafDsCommand {

	RafDsRequest request;
	
	public AddFraudCaseHistoryLogData(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			FrdFraudCaseHistorySessionEJBRemote sessionHome = (FrdFraudCaseHistorySessionEJBRemote) locator.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");
			dataList=request.getData();
			FrdFraudCaseHistory history = new FrdFraudCaseHistory();
			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID)){
						FrdFraudCaseHistoryPK frdFraudCaseHistoryPK= new FrdFraudCaseHistoryPK();
						frdFraudCaseHistoryPK.setFraudSuspicionCaseId(new Long(data.get(key)));
						frdFraudCaseHistoryPK.setFraudCaseHistoryDate(new Timestamp(System.currentTimeMillis()));
						history.setId(frdFraudCaseHistoryPK);
						
						//tes insert status id disini, karena status id null
						FrdFraudCaseStatus frdFraudCaseStatus = new FrdFraudCaseStatus();
						frdFraudCaseStatus.setFraudCaseStatusId(new Long (4));
						history.setFrdFraudCaseStatus(frdFraudCaseStatus);
					} else if(key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID)){
						FrdFraudCaseStatus frdFraudCaseStatus = new FrdFraudCaseStatus();
						frdFraudCaseStatus.setFraudCaseStatusId(new Long (data.get(key)));
						history.setFrdFraudCaseStatus(frdFraudCaseStatus);
					} else if(key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES)){
						history.setFraudCaseHistoryNotes(data.get(key));
					} 
				}
			}
					
			history=sessionHome.persistFrdFraudCaseHistory(history);
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
