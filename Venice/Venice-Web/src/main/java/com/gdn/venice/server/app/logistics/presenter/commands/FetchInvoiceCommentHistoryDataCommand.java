package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceReconCommentHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.djarum.raf.utilities.Locator;

public class FetchInvoiceCommentHistoryDataCommand implements RafDsCommand {
	RafDsRequest request;
		
	public FetchInvoiceCommentHistoryDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		String invoiceReconRecordId = request.getParams().get(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID);
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		if (invoiceReconRecordId!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<LogInvoiceReconCommentHistory> invoiceReconCommentHistoryLocator = null;
			
			try {
				invoiceReconCommentHistoryLocator = new Locator<LogInvoiceReconCommentHistory>();
				
				LogInvoiceReconCommentHistorySessionEJBRemote invoiceReconCommentHistoryHome = (LogInvoiceReconCommentHistorySessionEJBRemote) invoiceReconCommentHistoryLocator
				.lookup(LogInvoiceReconCommentHistorySessionEJBRemote.class, "LogInvoiceReconCommentHistorySessionEJBBean");
			
							
				LogInvoiceReconCommentHistory logInvoiceReconCommentHistory = new LogInvoiceReconCommentHistory();
				
				String query = "Select ch from LogInvoiceReconCommentHistory ch where ch.logInvoiceReconRecord.invoiceReconRecordId=" + invoiceReconRecordId;
				
				List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistoryList  = invoiceReconCommentHistoryHome.queryByRange(query, 0, 0);
				
				for (int i=0;i<logInvoiceReconCommentHistoryList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					
					logInvoiceReconCommentHistory = logInvoiceReconCommentHistoryList.get(i);
					
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					
					map.put(DataNameTokens.LOGINVOICERECONCOMMENTHISTORY_ID_HISTORYTIMESTAMP, logInvoiceReconCommentHistory.getId()!=null?formatter.format(logInvoiceReconCommentHistory.getId().getHistoryTimestamp()):"");
					map.put(DataNameTokens.LOGINVOICERECONCOMMENTHISTORY_COMMENT, logInvoiceReconCommentHistory.getComment());
					map.put(DataNameTokens.LOGINVOICERECONCOMMENTHISTORY_USERLOGONNAME, logInvoiceReconCommentHistory.getUserLogonName());
					
					dataList.add(map);
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(logInvoiceReconCommentHistoryList.size());
				rafDsResponse.setEndRow(request.getStartRow()+logInvoiceReconCommentHistoryList.size());
			} catch (Exception e) {
				e.printStackTrace();
				rafDsResponse.setStatus(-1);
			} finally {
				try {
					invoiceReconCommentHistoryLocator.close();
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
