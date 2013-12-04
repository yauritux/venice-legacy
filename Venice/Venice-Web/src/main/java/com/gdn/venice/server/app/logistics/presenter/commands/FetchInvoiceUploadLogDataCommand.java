package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceUploadLog;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchInvoiceUploadLogDataCommand implements RafDsCommand {
private RafDsRequest request;
	
	private static final String sql = "select o from LogInvoiceUploadLog o order by o.uploadTimestamp desc";
	
	public FetchInvoiceUploadLogDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			
			LogInvoiceUploadLogSessionEJBRemote sessionHome = (LogInvoiceUploadLogSessionEJBRemote) locator
				.lookup(LogInvoiceUploadLogSessionEJBRemote.class, "LogInvoiceUploadLogSessionEJBBean");
			
			List<LogInvoiceUploadLog> fileUploadLogList = null;
			
			LogInvoiceUploadLog fileUploadLog = new LogInvoiceUploadLog();
			
			if (criteria!=null) {
				fileUploadLogList = sessionHome.findByLogInvoiceUploadLogLike(fileUploadLog, criteria, 0, 0);
			} else {
				fileUploadLogList = sessionHome.queryByRange(sql, 0, 50);
			}
			
			for (LogInvoiceUploadLog logFileUploadLog : fileUploadLogList) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_INVOICEUPLOADLOGID, logFileUploadLog.getInvoiceUploadLogId().toString());
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_INVOICENUMBER, logFileUploadLog.getInvoiceNumber()!=null ?logFileUploadLog.getInvoiceNumber():"");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADNAME, logFileUploadLog.getFileUploadName()!=null ?logFileUploadLog.getFileUploadName():"");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADNAMEANDLOC, logFileUploadLog.getFileUploadNameAndLoc()!=null ?logFileUploadLog.getFileUploadNameAndLoc():"");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_ACTUALFILEUPLOADNAME, logFileUploadLog.getActualFileUploadName() != null ? logFileUploadLog.getActualFileUploadName():"");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_FILEUPLOADFORMAT, logFileUploadLog.getFileUploadFormat() != null ? logFileUploadLog.getFileUploadFormat() :"");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAME, logFileUploadLog.getFailedFileUploadName() != null ? logFileUploadLog.getFailedFileUploadName() : "");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_FAILEDFILEUPLOADNAMEANDLOC, logFileUploadLog.getFailedFileUploadNameAndLoc() != null ? logFileUploadLog.getFailedFileUploadNameAndLoc() : "");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_UPLOADSTATUS, logFileUploadLog.getUploadStatus() != null ?  logFileUploadLog.getUploadStatus() : "");
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_TIMESTAMP, formatter.format(logFileUploadLog.getUploadTimestamp()));
				map.put(DataNameTokens.LOGINVOICEUPLOADLOG_UPLOADEDBY, logFileUploadLog.getUploadedBy() != null ?  logFileUploadLog.getUploadedBy() : "");
				
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
