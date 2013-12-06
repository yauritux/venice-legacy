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
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Action Log
 * 
 * @author Roland
 */

public class UpdateFraudCaseActionLogDataCommand implements RafDsCommand {
	RafDsRequest request;
	String username;
	
	public UpdateFraudCaseActionLogDataCommand(RafDsRequest request, String username) {
		this.request=request;
		this.username = username;
	}
	
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdFraudActionLog> actionLogList = new ArrayList<FrdFraudActionLog>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdFraudActionLog actionLog = new FrdFraudActionLog();

		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudActionLogSessionEJBRemote sessionHome = (FrdFraudActionLogSessionEJBRemote) locator
			.lookup(FrdFraudActionLogSessionEJBRemote.class, "FrdFraudActionLogSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)) {
						try{
							actionLog = sessionHome.queryByRange("select o from FrdFraudActionLog o where o.fraudActionId="+new Long(data.get(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							actionLog.setFraudActionId(new Long(data.get(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)));
						}
						actionLog.setModifiedBy(username);
						actionLog.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)) {
						actionLog.setFraudActionId(new Long(data.get(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)));
						actionLog.setModifiedBy(username);
						actionLog.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					}  else if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME)){
						Long actionLogDate =new Long (data.get(key));
						actionLog.setFraudActionLogDate(new Timestamp(actionLogDate));
					} else if (key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE)) {
						actionLog.setFraudActionLogType(data.get(key));	
					} else if (key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID)) {
						VenParty party = new VenParty();
						party.setPartyId(new Long(data.get(key)));
						actionLog.setVenParty(party);
					} else if(key.equals(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES)){
						actionLog.setFraudActionLogNotes(data.get(key));
					}
				}						
				
				actionLogList.add(actionLog);			
			}

			sessionHome.mergeFrdFraudActionLogList((ArrayList<FrdFraudActionLog>)actionLogList);	
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
