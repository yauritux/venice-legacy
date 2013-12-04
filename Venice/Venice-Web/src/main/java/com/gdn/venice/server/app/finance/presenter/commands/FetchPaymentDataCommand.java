package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FinApPaymentSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchPaymentDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchPaymentDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
		JPQLAdvancedQueryCriteria criteriaAndTaskCriteria = null;
		String idRecord="";
		//check for taskid parameter, it shall be there if this screen is called from ToDoList for approval purpose
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.TASKID)!=null) {
			String taskId = request.getParams().get(DataNameTokens.TASKID);
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
			
			bpmAdapter.synchronize();
			HashMap<String,String> fundInReconRecordIds = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.APPAYMENTID);
			
//			JPQLAdvancedQueryCriteria taskCriteria = new JPQLAdvancedQueryCriteria("or");
			
//			for (int i=0;i<fundInReconRecordIds.size();i++) {
//				String fundInReconRecordId = fundInReconRecordIds.get("data"+i);
//				if (fundInReconRecordId != null) {
//					JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
//					simpleCriteria.setFieldName(DataNameTokens.FINAPPAYMENT_APPAYMENTID);
//					simpleCriteria.setOperator("equals");
//					simpleCriteria.setValue(fundInReconRecordId);
//					simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINAPPAYMENT_APPAYMENTID));
//					taskCriteria.add(simpleCriteria);
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
			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
			inCriteria.setFieldName(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
			
			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
			inCriteria.setFieldName(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
	
			String fundsInReconRecordIdList = "";
			for(String value:fundInReconRecordIds.values()){
				System.out.println(value);
				if(fundsInReconRecordIdList.isEmpty()){
					fundsInReconRecordIdList = value;		
				}
				else{
					fundsInReconRecordIdList = fundsInReconRecordIdList + "," + value;					
				}				
				if(value.matches("\\d*") && !value.equals("")){
					if(idRecord.equals("")) idRecord=value;
					else idRecord=idRecord+","+value;
					System.out.println("masuk "+value);
				}
			}
			inCriteria.setValue(fundsInReconRecordIdList);
			inCriteria.setOperator("IN");
			
			criteriaAndTaskCriteria.add(inCriteria);			
		}
		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<FinJournalTransaction> finJournalTransactionLocator = null;
		
		//Check for journal Group Id, it will be there if the screen is called from Journal Screen
		String journalGroupId = null;
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
			journalGroupId = request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
		}
		
		Locator<FinApPayment> finApPaymentLocator = null;		
		try {
			List<FinJournalTransaction>  finJournalTransactionList = null;
			
			//Check for journal Group Id, it will be there if the screen is called from Journal Screen
			//This section queries for finJournalTransactions that are part of the finJournalApprovalGroup
			if (journalGroupId!=null) {
				finJournalTransactionLocator = new Locator<FinJournalTransaction>();
				
				FinJournalTransactionSessionEJBRemote finJournalTransactionSessionHome = (FinJournalTransactionSessionEJBRemote) finJournalTransactionLocator
				.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");
				
				String select = "select o from FinJournalTransaction o where o.finJournalApprovalGroup.journalGroupId = " + journalGroupId;			
				finJournalTransactionList = finJournalTransactionSessionHome.queryByRange(select, 0, 0);
			}
			
			finApPaymentLocator = new Locator<FinApPayment>();
			
			FinApPaymentSessionEJBRemote sessionHome = (FinApPaymentSessionEJBRemote) finApPaymentLocator
			.lookup(FinApPaymentSessionEJBRemote.class, "FinApPaymentSessionEJBBean");
			
			List<FinApPayment> finApPaymentList = null;	
			if(finJournalTransactionList!=null && !finJournalTransactionList.isEmpty()){
				String select = "select o from FinApPayment o where o.apPaymentId in ( select a.finApPayment.apPaymentId from FinArFundsInRefund a where "+
				"a.finArFundsInReconRecord.reconciliationRecordId in (select b.finArFundsInReconRecords.reconciliationRecordId from FinArFundsInJournalTransaction b "+
				"where b.finJournalTransactions.transactionId in (select u.transactionId from FinJournalTransaction u where u.finJournalApprovalGroup.journalGroupId = " + journalGroupId+")))";				
				finApPaymentList = sessionHome.queryByRange(select, 0, 0);
				
			}else	if ((criteria == null && criteriaAndTaskCriteria == null) || (criteria!=null && !criteria.getListIterator().hasNext()) || (criteriaAndTaskCriteria!=null && !criteriaAndTaskCriteria.getListIterator().hasNext())) {
				String select = "select o from FinApPayment o";				
				finApPaymentList = sessionHome.queryByRange(select, 0, 0);
			} else {
				FinApPayment finApPayment = new FinApPayment();
				if (criteriaAndTaskCriteria!=null) {
//					finApPaymentList = sessionHome.queryByRange("select o from FinApPayment o where o.apPaymentId in (select u.finApPayment.apPaymentId from FinArFundsInRefund u where u.finArFundsInReconRecord.reconciliationRecordId in ("+idRecord+"))",0, 0);
					finApPaymentList = sessionHome.queryByRange("select o from FinApPayment o where o.apPaymentId in ("+idRecord+")",0, 0);
				} else {
					finApPaymentList = sessionHome.findByFinApPaymentLike(finApPayment, criteria, 0, 0);
				}
			}						
			for (int i=0;i<finApPaymentList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();				
				FinApPayment finApPayment = finApPaymentList.get(i);			
				
				map.put(DataNameTokens.FINAPPAYMENT_APPAYMENTID, finApPayment.getApPaymentId()!=null?finApPayment.getApPaymentId().toString():"");
				map.put(DataNameTokens.FINAPPAYMENT_FINACCOUNT_ACCOUNTDESC, (finApPayment.getFinAccount()!=null)?finApPayment.getFinAccount().getAccountDesc():"");
				map.put(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, (finApPayment.getVenParty()!=null)?finApPayment.getVenParty().getFullOrLegalName():"");
				map.put(DataNameTokens.FINAPPAYMENT_AMOUNT, (finApPayment.getAmount()!=null)?finApPayment.getAmount().toString():"");
				map.put(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, (finApPayment.getPenaltyAmount()!=null)?finApPayment.getPenaltyAmount().toString():"");
				map.put(DataNameTokens.FINAPPAYMENT_PPH23_AMOUNT, (finApPayment.getPph23Amount()!=null)?finApPayment.getPph23Amount().toString():"");
				map.put(DataNameTokens.FINAPPAYMENT_FINAPPROVALSTATUS_APPROVALSTATUSDESC, (finApPayment.getFinApprovalStatus()!=null)?finApPayment.getFinApprovalStatus().getApprovalStatusDesc():"");
				
//				boolean bExclude = false;
//				
//				//finJournalTransactionList will not be null, if journalGroupId is set (the screen is called from Journal Screen)
//				//This section filters the fundInReconRecords which are related to specific journalTransaction
//				if (finJournalTransactionList!=null) { 
//					bExclude = true;
//					List<FinJournalTransaction> journalTransactions = finApPayment.getFinJournalTransactions();
//					for (int j=0;j<finJournalTransactionList.size();j++) {
//						if (finJournalTransactionList.get(j).getTransactionId()!=null && journalTransactions!=null && !journalTransactions.isEmpty()) {
//							String transactionId = finJournalTransactionList.get(j).getTransactionId().toString();
//							for (int k=0;k<journalTransactions.size();k++) {
//								if (journalTransactions.get(k).getTransactionId()!=null && journalTransactions.get(k).getTransactionId().toString().equals(transactionId)) {
//									bExclude = false;
//									break;
//								}
//							}
//							if (!bExclude) {
//								break;
//							}
//						}
//					}
//				} else if (journalGroupId!=null) {
//					bExclude = true;
//				}
//				if (!bExclude) {
					dataList.add(map);
//				}				
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
				if (finJournalTransactionLocator!=null) {
					finJournalTransactionLocator.close();
				}
				finApPaymentLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}	
}
