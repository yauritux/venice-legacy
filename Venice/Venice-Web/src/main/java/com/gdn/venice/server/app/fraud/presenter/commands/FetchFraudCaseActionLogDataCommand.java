package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudActionLogSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudActionLog;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Action Log
 * 
 * @author Roland
 */

public class FetchFraudCaseActionLogDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchFraudCaseActionLogDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdFraudActionLogSessionEJBRemote sessionHome = (FrdFraudActionLogSessionEJBRemote) locator.lookup(FrdFraudActionLogSessionEJBRemote.class, "FrdFraudActionLogSessionEJBBean");
				
			List<FrdFraudActionLog> actionLogList = null;
						
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();		
			if (criteria == null) {
				String query = "select o from FrdFraudActionLog o join fetch o.frdFraudSuspicionCase where o.isActive=true and o.frdFraudSuspicionCase.fraudSuspicionCaseId =  "+ request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).toString();		
				actionLogList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				FrdFraudActionLog actionLog = new FrdFraudActionLog();
				criteria.setBooleanOperator("and");
				JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
				caseIdCriteria.setFieldName(DataNameTokens.FRDFRAUDCASEACTIONLOG_SUSPICIONCASEID);
				caseIdCriteria.setOperator("equals");
				caseIdCriteria.setValue(request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDCASEACTIONLOG_SUSPICIONCASEID));
				criteria.setBooleanOperator("and");
				JPQLSimpleQueryCriteria actionLogCriteria = new JPQLSimpleQueryCriteria();
				actionLogCriteria.setFieldName(DataNameTokens.FRDFRAUDCASEACTIONLOG_ISACTIVE);
				actionLogCriteria.setOperator("equals");
				actionLogCriteria.setValue("true");
				actionLogCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDCASEACTIONLOG_ISACTIVE));
				criteria.add(caseIdCriteria);
				criteria.add(actionLogCriteria);
				actionLogList = sessionHome.findByFrdFraudActionLogLike(actionLog, criteria, 0, 0);
			}

			for(int i=0; i<actionLogList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdFraudActionLog list = actionLogList.get(i);		
				map.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID, Util.isNull(list.getFraudActionId(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME, Util.isNull(list.getFraudActionLogDate(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE, Util.isNull(list.getFraudActionLogType(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID, list.getVenParty()!=null && list.getVenParty().getPartyId()!=null?list.getVenParty().getPartyId().toString():"");
				map.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYNAME, list.getVenParty()!=null && list.getVenParty().getFullOrLegalName()!=null?list.getVenParty().getFullOrLegalName().toString():"");		
				map.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES, Util.isNull(list.getFraudActionLogNotes(), "").toString());
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
