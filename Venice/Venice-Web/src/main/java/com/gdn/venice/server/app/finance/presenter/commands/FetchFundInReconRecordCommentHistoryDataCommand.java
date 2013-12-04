package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInReconCommentSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconComment;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchFundInReconRecordCommentHistoryDataCommand implements RafDsCommand {
	RafDsRequest request;
		
	public FetchFundInReconRecordCommentHistoryDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		String fundInReconRecordId = request.getParams().get(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		if (fundInReconRecordId!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<FinArFundsInReconComment> fundInReconCommentLocator = null;
			
			try {
				fundInReconCommentLocator = new Locator<FinArFundsInReconComment>();
				
				FinArFundsInReconCommentSessionEJBRemote fundInReconCommentHome = (FinArFundsInReconCommentSessionEJBRemote) fundInReconCommentLocator
				.lookup(FinArFundsInReconCommentSessionEJBRemote.class, "FinArFundsInReconCommentSessionEJBBean");
			
							
				FinArFundsInReconComment fundInReconComment = new FinArFundsInReconComment();
				
				String query = "Select ch from FinArFundsInReconComment ch where ch.finArFundsInReconRecord.reconciliationRecordId=" + fundInReconRecordId;
				
				List<FinArFundsInReconComment> logActivityReconCommentHistoryList  = fundInReconCommentHome.queryByRange(query, 0, 0);
				
				for (int i=0;i<logActivityReconCommentHistoryList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					
					fundInReconComment = logActivityReconCommentHistoryList.get(i);
					
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					
					map.put(DataNameTokens.FINARFUNDSINRECONCOMMENT_ID_COMMENTTIMESTAMP, fundInReconComment.getId()!=null?formatter.format(fundInReconComment.getId().getCommentTimestamp()):"");
					map.put(DataNameTokens.FINARFUNDSINRECONCOMMENT_COMMENT, fundInReconComment.getComment());
					map.put(DataNameTokens.FINARFUNDSINRECONCOMMENT_USERLOGONNAME, fundInReconComment.getUserLogonName());
					
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
					fundInReconCommentLocator.close();
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
