package com.gdn.venice.server.app.fraud.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for History Log
 * 
 * @author Roland
 */

public class FetchFraudCaseHistoryLogData implements RafDsCommand {

	RafDsRequest request;
	
	public FetchFraudCaseHistoryLogData(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdFraudCaseHistorySessionEJBRemote sessionHome = (FrdFraudCaseHistorySessionEJBRemote) locator.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");
				
			List<FrdFraudCaseHistory> historyList = null;		
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			
			if (criteria == null) {
				String query = "select o from FrdFraudCaseHistory o join fetch o.frdFraudSuspicionCase where o.frdFraudSuspicionCase.fraudSuspicionCaseId =  "+ request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).toString()+" order by o.id.fraudCaseHistoryDate asc";		
				historyList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				FrdFraudCaseHistory history = new FrdFraudCaseHistory();
				JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
				caseIdCriteria.setFieldName(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDSUSPICIONCASEID);
				caseIdCriteria.setOperator("equals");
				caseIdCriteria.setValue(request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDSUSPICIONCASEID));
				criteria.add(caseIdCriteria);
				historyList = sessionHome.findByFrdFraudCaseHistoryLike(history, criteria, 0, 0);
			}
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0; i<historyList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdFraudCaseHistory list = historyList.get(i);				
				map.put(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDSUSPICIONCASEID, list.getId()!=null && list.getId().getFraudSuspicionCaseId()!=null?list.getId().getFraudSuspicionCaseId().toString():"");
		        String s =  sdf.format(list.getId().getFraudCaseHistoryDate());
		        StringBuilder sb = new StringBuilder(s);
				map.put(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYDATE, sb.toString());
				map.put(DataNameTokens.FRDFRAUDCASEHISTORY_FRDFRAUDCASESTATUS_FRAUDSCASESTATUSDESC, list.getFrdFraudCaseStatus()!=null && list.getFrdFraudCaseStatus().getFraudCaseStatusDesc()!=null?list.getFrdFraudCaseStatus().getFraudCaseStatusDesc().toString():"");								 
				map.put(DataNameTokens.FRDFRAUDCASEHISTORY_FRAUDCASEHISTORYNOTES, Util.isNull(list.getFraudCaseHistoryNotes(), "").toString());											
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
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
