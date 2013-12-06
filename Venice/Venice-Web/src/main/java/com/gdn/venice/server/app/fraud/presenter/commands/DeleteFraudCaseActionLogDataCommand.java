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
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Action Log
 * 
 * @author Roland
 */

public class DeleteFraudCaseActionLogDataCommand implements RafDsCommand {
	RafDsRequest request;
	String username;
	
	public DeleteFraudCaseActionLogDataCommand(RafDsRequest request, String username) {
		this.request = request;
		this.username = username;
	}
	
	@Override
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
						try
						{
							actionLog = sessionHome.queryByRange("select o from FrdFraudActionLog o where o.fraudActionId="+new Long(data.get(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							actionLog.setFraudActionId(new Long(data.get(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID)));
						}
						actionLog.setCreatedBy(username);
						actionLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						actionLog.setModifiedBy(username);
						actionLog.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						actionLog.setIsActive(false);
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
