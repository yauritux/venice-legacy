package com.gdn.venice.server.app.finance.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.util.VeniceConstants;

public class FetchJournalDetailDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchJournalDetailDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
	
		RafDsResponse rafDsResponse = new RafDsResponse();
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<Object> locator = null;
		List<FinJournalTransaction>  finJournalTransactionList =null;
		List<FinArFundsInReconRecord>  finArFundsInReconRecordList =null;
		
		try {
			locator = new Locator<Object>();
			
			FinJournalTransactionSessionEJBRemote sessionHome = (FinJournalTransactionSessionEJBRemote) locator
			.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote recordSessionHome = (FinArFundsInReconRecordSessionEJBRemote) locator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
			String journalGroupId = request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
			if (criteria == null) {
			String select = "select o from FinJournalTransaction o where o.finJournalApprovalGroup.journalGroupId = " + journalGroupId;
				finJournalTransactionList = sessionHome.queryByRange(select, 0, 0);
			}else{
				FinJournalTransaction finJournalTransactionItems = new FinJournalTransaction();
				criteria.setBooleanOperator("and");
				JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
				caseIdCriteria.setFieldName(DataNameTokens.FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
				caseIdCriteria.setOperator("equals");
				caseIdCriteria.setValue(request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
				caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
				criteria.add(caseIdCriteria);
				finJournalTransactionList = sessionHome.findByFinJournalTransactionLike(finJournalTransactionItems, criteria, 0, 0);
			}			
			
		
			HashMap<String, String> mapGroup = new HashMap<String, String>();
			String allGroup="";
			for (int i=0;i<finJournalTransactionList.size();i++) {
				FinJournalTransaction finJ= finJournalTransactionList.get(i);
				if(finJ.getGroupJournal()!=null){
					if((finJ.getGroupJournal()+"").equals(finJ.getFinAccount().getAccountId()+"") && !allGroup.contains(finJ.getGroupJournal()+"")){
						mapGroup.put(finJ.getGroupJournal()+"", finJ.getFinAccount().getAccountDesc());
						allGroup=allGroup+finJ.getGroupJournal()+",";
					}					
				}				
			}		
			String selectMethod ="";
			for (int i=0;i<finJournalTransactionList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				FinJournalTransaction finJournalTransaction = finJournalTransactionList.get(i);
				
				if(i==0){
					selectMethod = "select o from FinArFundsInReconRecord o where o.reconciliationRecordId in "+
						"(select a.finArFundsInReconRecords.reconciliationRecordId from FinArFundsInJournalTransaction a "+
						"where a.finJournalTransactions.finJournalApprovalGroup.journalGroupId = " + journalGroupId+")";
					
					finArFundsInReconRecordList = recordSessionHome.queryByRange(selectMethod, 0, 0);
					if(!finArFundsInReconRecordList.isEmpty() && finArFundsInReconRecordList.size()>0){
						selectMethod=finArFundsInReconRecordList.get(0).getVenOrderPayment()!=null?finArFundsInReconRecordList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeCode().equals(VeniceConstants.VEN_WCS_PAYMENT_TYPE_VirtualAccount)?
								     			finArFundsInReconRecordList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc()+" "+finJournalTransaction.getVenBank().getBankShortName():
								     			finArFundsInReconRecordList.get(0).getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc():
								     			finArFundsInReconRecordList.get(0).getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeDesc();						
					}else
						selectMethod="";
				}				
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.FINJOURNALTRANSACTION_TRANSACTIONID, finJournalTransaction.getTransactionId()!=null?finJournalTransaction.getTransactionId().toString():"");
				map.put(DataNameTokens.FINJOURNALTRANSACTION_FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC,
						(finJournalTransaction.getFinJournalApprovalGroup()!=null)?
								finJournalTransaction.getFinJournalApprovalGroup().getJournalGroupDesc():"");
				
				map.put(DataNameTokens.FINJOURNALTRANSACTION_REFF,
						(finJournalTransaction.getWcsOrderID()!=null)?
								finJournalTransaction.getWcsOrderID():"");
				
				map.put(DataNameTokens.FINJOURNALTRANSACTION_PAYMENT_TYPE,selectMethod);
				
				map.put(DataNameTokens.FINJOURNALTRANSACTION_TRANSACTIONTIMESTAMP, 
						(finJournalTransaction.getTransactionTimestamp()!=null)?
								formatter.format(new Timestamp(finJournalTransaction.getTransactionTimestamp().getTime()-25200000)):"");
				map.put(DataNameTokens.FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTDESC, 
						(finJournalTransaction.getFinAccount()!=null)?
								finJournalTransaction.getFinAccount().getAccountDesc().toString():"");
				
				if (finJournalTransaction.getTransactionAmount()!=null) {
					if (finJournalTransaction.getCreditDebitFlag()) {
						map.put(DataNameTokens.FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, finJournalTransaction.getTransactionAmount().toString());
						map.put(DataNameTokens.FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, "");
					} else {
						map.put(DataNameTokens.FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, "");
						map.put(DataNameTokens.FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, finJournalTransaction.getTransactionAmount().toString());
					}
					
				}
				map.put(DataNameTokens.FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC, 
						(finJournalTransaction.getFinTransactionStatus()!=null)?
								finJournalTransaction.getFinTransactionStatus().getTransactionStatusDesc():"");
				
				map.put(DataNameTokens.FINJOURNALTRANSACTION_COMMENTS, finJournalTransaction.getComments());
				map.put(DataNameTokens.FINJOURNALTRANSACTION_GROUP_JOURNAL, mapGroup.get(finJournalTransaction.getGroupJournal()+""));
				
								
				dataList.add(map);
				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(finJournalTransactionList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finJournalTransactionList.size());
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
