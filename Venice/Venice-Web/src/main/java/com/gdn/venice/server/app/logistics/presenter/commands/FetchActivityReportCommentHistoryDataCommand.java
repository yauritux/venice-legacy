package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote;
import com.gdn.venice.persistence.LogActivityReconCommentHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchActivityReportCommentHistoryDataCommand implements RafDsCommand {
	RafDsRequest request;
		
	public FetchActivityReportCommentHistoryDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		String activityReconRecordId = request.getParams().get(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID);		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		if (activityReconRecordId!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<LogActivityReconCommentHistory> activityReconCommentHistoryLocator = null;			
			try {
				activityReconCommentHistoryLocator = new Locator<LogActivityReconCommentHistory>();
				
				LogActivityReconCommentHistorySessionEJBRemote activityReconCommentHistoryHome = (LogActivityReconCommentHistorySessionEJBRemote) activityReconCommentHistoryLocator
				.lookup(LogActivityReconCommentHistorySessionEJBRemote.class, "LogActivityReconCommentHistorySessionEJBBean");
										
				LogActivityReconCommentHistory logActivityReconCommentHistory = new LogActivityReconCommentHistory();
				
				String query = "Select ch from LogActivityReconCommentHistory ch where ch.logActivityReconRecord.activityReconRecordId=" + activityReconRecordId;				
				List<LogActivityReconCommentHistory> logActivityReconCommentHistoryList  = activityReconCommentHistoryHome.queryByRange(query, 0, 0);
				
				for (int i=0;i<logActivityReconCommentHistoryList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					
					logActivityReconCommentHistory = logActivityReconCommentHistoryList.get(i);					
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					
					map.put(DataNameTokens.LOGACTIVITYRECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP, logActivityReconCommentHistory.getId()!=null?formatter.format(logActivityReconCommentHistory.getId().getHistoryTimestamp()):"");
					map.put(DataNameTokens.LOGACTIVITYRECONCOMMENTHISTORY_COMMENT, logActivityReconCommentHistory.getComment());
					map.put(DataNameTokens.LOGACTIVITYRECONCOMMENTHISTORY_USERLOGONNAME, logActivityReconCommentHistory.getUserLogonName());
					
					dataList.add(map);
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(logActivityReconCommentHistoryList.size());
				rafDsResponse.setEndRow(request.getStartRow()+logActivityReconCommentHistoryList.size());
			} catch (Exception e) {
				e.printStackTrace();
				rafDsResponse.setStatus(-1);
			} finally {
				try {
					activityReconCommentHistoryLocator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			rafDsResponse.setData(dataList);
			
		} else {
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(0);
			rafDsResponse.setEndRow(request.getStartRow());
		}	
		return rafDsResponse;		
	}
}
