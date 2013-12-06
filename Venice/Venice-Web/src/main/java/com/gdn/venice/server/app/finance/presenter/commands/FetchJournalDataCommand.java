package com.gdn.venice.server.app.finance.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FinJournalApprovalGroupSessionEJBRemote;
import com.gdn.venice.persistence.FinJournalApprovalGroup;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.util.VeniceConstants;

public class FetchJournalDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	boolean bManualJournal;
	
	public FetchJournalDataCommand(RafDsRequest request, String userName, boolean bManualJournal) {
		this.bManualJournal = bManualJournal;
		this.request = request;
		this.userName = userName;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
		JPQLAdvancedQueryCriteria criteriaAndTaskCriteria = null;
		//check for taskid parameter, it shall be there if this screen is called from ToDoList for approval purpose
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.TASKID)!=null) {
			String taskId = request.getParams().get(DataNameTokens.TASKID);
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
			
			bpmAdapter.synchronize();
			HashMap<String,String> journalGroupIds = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.JOURNALGROUPID);
			
//			JPQLAdvancedQueryCriteria taskCriteria = new JPQLAdvancedQueryCriteria("or");
//			if(journalGroupIds != null){
//				for (int i=0;i<journalGroupIds.size();i++) {
//					String fundInReconRecordId = journalGroupIds.get("data"+i);
//					if (fundInReconRecordId != null) {
//						JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
//						simpleCriteria.setFieldName(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
//						simpleCriteria.setOperator("equals");
//						simpleCriteria.setValue(fundInReconRecordId);
//						simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
//						taskCriteria.add(simpleCriteria);
//					}					
//				}
//			}
			
			criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria("and");
			if (criteria!=null) {
				criteriaAndTaskCriteria.add(criteria);
			}
//			criteriaAndTaskCriteria.add(taskCriteria);			
			request.setCriteria(criteriaAndTaskCriteria);
			
			/*
			 * Build a new simple criteria as an IN() list
			 */
			JPQLSimpleQueryCriteria inCriteria = new JPQLSimpleQueryCriteria();
			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
			inCriteria.setFieldName(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
			
			String airwayBillIdList = "";
			for(String value:journalGroupIds.values()){
				if(airwayBillIdList.isEmpty()){
					airwayBillIdList = value;
				}
				else{
					airwayBillIdList = airwayBillIdList + "," + value;
				}
			}
			inCriteria.setValue(airwayBillIdList);
			inCriteria.setOperator("IN");
			
			criteriaAndTaskCriteria.add(inCriteria);			
		}
		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<FinJournalApprovalGroup> finJournalApprovalGroupLocator = null;
		
		try {
			finJournalApprovalGroupLocator = new Locator<FinJournalApprovalGroup>();
			
			FinJournalApprovalGroupSessionEJBRemote sessionHome = (FinJournalApprovalGroupSessionEJBRemote) finJournalApprovalGroupLocator
			.lookup(FinJournalApprovalGroupSessionEJBRemote.class, "FinJournalApprovalGroupSessionEJBBean");
			
			List<FinJournalApprovalGroup> finJournalApprovalGroupList = null;
			
			if ((criteria == null && criteriaAndTaskCriteria == null) || (criteria!=null && !criteria.getListIterator().hasNext()) || (criteriaAndTaskCriteria!=null && !criteriaAndTaskCriteria.getListIterator().hasNext())) {
				
				String select = "select o from FinJournalApprovalGroup o";
				if (bManualJournal) {
					select = "select o from FinJournalApprovalGroup o where o.finJournal.journalId="+VeniceConstants.FIN_JOURNAL_MANUAL;
				}
				
				finJournalApprovalGroupList = sessionHome.queryByRange(select, 0, 50);
			} else {
				FinJournalApprovalGroup finJournalAppprovalGroup = new FinJournalApprovalGroup();
				
				if (criteriaAndTaskCriteria!=null) {
					finJournalApprovalGroupList = sessionHome.findByFinJournalApprovalGroupLike(finJournalAppprovalGroup, criteriaAndTaskCriteria, 0, 0);
				} else {
					finJournalApprovalGroupList = sessionHome.findByFinJournalApprovalGroupLike(finJournalAppprovalGroup, criteria, 0, 0);
				}
			}
						
			for (int i=0;i<finJournalApprovalGroupList.size();i++) {				
				HashMap<String, String> map = new HashMap<String, String>();				
				FinJournalApprovalGroup finJournalAppprovalGroup = finJournalApprovalGroupList.get(i);
				
				if (bManualJournal) {
					if (finJournalAppprovalGroup.getFinJournal()!=null && finJournalAppprovalGroup.getFinJournal().getJournalId()!=VeniceConstants.FIN_JOURNAL_MANUAL) {
						continue;
					}
				} 				
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, finJournalAppprovalGroup.getJournalGroupId()!=null?finJournalAppprovalGroup.getJournalGroupId().toString():"");
				map.put(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID, 
						(finJournalAppprovalGroup.getFinJournal()!=null && finJournalAppprovalGroup.getFinJournal().getJournalId()!=null)?finJournalAppprovalGroup.getFinJournal().getJournalId().toString():"");
				map.put(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC, 
						(finJournalAppprovalGroup.getFinJournal()!=null)?finJournalAppprovalGroup.getFinJournal().getJournalDesc():"");
				map.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP, 
						(finJournalAppprovalGroup.getJournalGroupTimestamp()!=null)?formatter.format(new Timestamp(finJournalAppprovalGroup.getJournalGroupTimestamp().getTime()-25200000)):"");
				map.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC, finJournalAppprovalGroup.getJournalGroupDesc());
				map.put(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC, 
						(finJournalAppprovalGroup.getFinApprovalStatus()!=null)?finJournalAppprovalGroup.getFinApprovalStatus().getApprovalStatusDesc():"");
				
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				finJournalApprovalGroupLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}	
}
