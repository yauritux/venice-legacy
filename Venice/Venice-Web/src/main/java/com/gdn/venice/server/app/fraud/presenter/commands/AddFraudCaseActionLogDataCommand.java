package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudActionLog;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for Action Log
 * 
 * @author Roland
 */

public class AddFraudCaseActionLogDataCommand implements RafDsCommand {

	RafDsRequest request;
	String username;
	
	public AddFraudCaseActionLogDataCommand(RafDsRequest request, String username) {
		this.request=request;
		this.username = username;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();

		Locator<Object> locator=null;
		try {
			locator = new Locator<Object>();
			FrdFraudActionLogSessionEJBRemote sessionHome = (FrdFraudActionLogSessionEJBRemote) locator.lookup(FrdFraudActionLogSessionEJBRemote.class, "FrdFraudActionLogSessionEJBBean");
			
			FrdFraudActionLog actionLogEntity = new FrdFraudActionLog();
			List<HashMap<String, String>> dataList = request.getData();
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_SUSPICIONCASEID)){
						FrdFraudSuspicionCase fraudCaseEntity = new FrdFraudSuspicionCase();
						fraudCaseEntity.setFraudSuspicionCaseId(new Long(data.get(key)));
						actionLogEntity.setFrdFraudSuspicionCase(fraudCaseEntity);
						actionLogEntity.setCreatedBy(username);
						actionLogEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						actionLogEntity.setModifiedBy(username);
						actionLogEntity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						actionLogEntity.setIsActive(true);
					} else if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)){
						actionLogEntity.setFraudActionId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME)){
						Long actionLogDate =new Long (data.get(key));
						actionLogEntity.setFraudActionLogDate(new Timestamp(actionLogDate));
					} else if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE)){
						actionLogEntity.setFraudActionLogType(data.get(key));
					} else if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID)){
						VenParty partyEntity = new VenParty();
						partyEntity.setPartyId(new Long(data.get(key)));
						actionLogEntity.setVenParty(partyEntity);
					} else if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES)){
						actionLogEntity.setFraudActionLogNotes(data.get(key));
					} 
				}
			}
			
			actionLogEntity = sessionHome.persistFrdFraudActionLog(actionLogEntity);
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
		
		rafDsResponse.setData(new ArrayList<HashMap<String, String>>());
		return rafDsResponse;
	}
}
