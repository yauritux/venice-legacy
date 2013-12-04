package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote;
import com.gdn.venice.persistence.LogFileUploadLog;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchFileUploadLogDataCommand implements RafDsCommand {
	private RafDsRequest request;
	
	private static final String sql = "select o from LogFileUploadLog o order by o.uploadTimestamp desc";
	
	public FetchFileUploadLogDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		
		Locator locator = null;
		
		try{
			locator = new Locator();
			
			LogFileUploadLogSessionEJBRemote sessionHome = (LogFileUploadLogSessionEJBRemote) locator
				.lookup(LogFileUploadLogSessionEJBRemote.class, "LogFileUploadLogSessionEJBBean");
			
			List<LogFileUploadLog> fileUploadLogList = null;
			
			LogFileUploadLog fileUploadLog = new LogFileUploadLog();
			
			if (criteria!=null) {
				fileUploadLogList = sessionHome.findByLogFileUploadLogLike(fileUploadLog, criteria, 0, 0);
			} else {
				fileUploadLogList = sessionHome.queryByRange(sql, 0, 20);
			}
			
			for (LogFileUploadLog logFileUploadLog : fileUploadLogList) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADLOGID, logFileUploadLog.getFileUploadLogId().toString());
				map.put(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADNAME, logFileUploadLog.getFileUploadName()!=null ?logFileUploadLog.getFileUploadName():"");
				map.put(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADNAMEANDLOC, logFileUploadLog.getFileUploadNameAndLoc()!=null ?logFileUploadLog.getFileUploadNameAndLoc():"");
				map.put(DataNameTokens.LOGFILEUPLOADLOG_ACTUALFILEUPLOADNAME, logFileUploadLog.getActualFileUploadName() != null ? logFileUploadLog.getActualFileUploadName():"");
				map.put(DataNameTokens.LOGFILEUPLOADLOG_FILEUPLOADFORMAT, logFileUploadLog.getFileUploadFormat() != null ? logFileUploadLog.getFileUploadFormat() :"");
				map.put(DataNameTokens.LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAME, logFileUploadLog.getFailedFileUploadName() != null ? logFileUploadLog.getFailedFileUploadName() : "");
				map.put(DataNameTokens.LOGFILEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC, logFileUploadLog.getFailedFileUploadNameAndLoc() != null ? logFileUploadLog.getFailedFileUploadNameAndLoc() : "");
				map.put(DataNameTokens.LOGFILEUPLOADLOG_UPLOADSTATUS, logFileUploadLog.getUploadStatus() != null ?  logFileUploadLog.getUploadStatus() : "");
				map.put(DataNameTokens.LOGFILEUPLOADLOG_TIMESTAMP, formatter.format(logFileUploadLog.getUploadTimestamp()));
				map.put(DataNameTokens.LOGFILEUPLOADLOG_USERNAME, logFileUploadLog.getUploadUsername() != null ?  logFileUploadLog.getUploadUsername() : "");
				
				dataList.add(map);
				
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(fileUploadLogList.size());
			rafDsResponse.setEndRow(request.getStartRow()+fileUploadLogList.size());
			
		}catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
