package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInActionAppliedHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchActionTakenHistoryDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchActionTakenHistoryDataCommand(RafDsRequest request ) {
		this.request = request;
		}
	@Override
	public RafDsResponse execute() {
		String idReconRecordSource = request.getParams().get(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
		RafDsResponse rafDsResponse = new RafDsResponse();
		
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<Object> locator = null;
			
			try {
				locator = new Locator<Object>();			
				
				FinArFundsInActionAppliedHistorySessionEJBRemote actionHome = (FinArFundsInActionAppliedHistorySessionEJBRemote) locator
				.lookup(FinArFundsInActionAppliedHistorySessionEJBRemote.class, "FinArFundsInActionAppliedHistorySessionEJBBean");
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				String query = "Select o from FinArFundsInActionAppliedHistory o where o.finArFundsInReconRecords.reconciliationRecordId=" + idReconRecordSource;				
				List<FinArFundsInActionAppliedHistory>actionList  = actionHome.queryByRange(query, 0, 0);			
				System.out.println(query);
				for (int i=0;i<actionList.size();i++) {
							HashMap<String, String> map = new HashMap<String, String>();													
							map.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_ID, actionList.get(i).getActionTakenId()+"");
							map.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_DATE, formatter.format(actionList.get(i).getActionTakenTimestamp())+"");
							map.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_APPLIED_DESC, actionList.get(i).getFinArFundsInActionApplied().getActionAppliedDesc()+"");
							map.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_NOMORREFF, actionList.get(i).getReferenceId()+"");				
							map.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_AMOUNT, actionList.get(i).getAmount()+"");
						dataList.add(map);					
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(actionList.size());
				rafDsResponse.setEndRow(request.getStartRow()+actionList.size());
			} catch (Exception e) {
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
