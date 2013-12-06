package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInJournalTransaction;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.util.VeniceConstants;

public class FetchFundInReconciliationDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchFundInReconciliationDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		JPQLAdvancedQueryCriteria criteriaAndTaskCriteria = null;
		String agingCriteriaValue = null;
		String reconStatusCriteriaValue = null;

		String fundsInReconRecordIdList = "";		
	
		//get value for Aging and Recon Status (if Any)
		if (criteria!=null) {
			List<JPQLSimpleQueryCriteria> simpleCriteriaList = criteria.getSimpleCriteria();
			for (int i=0;i<simpleCriteriaList.size();i++) {
				if (simpleCriteriaList.get(i).getFieldName().equals(DataNameTokens.FINARFUNDSINRECONRECORD_AGING)) {
					agingCriteriaValue = simpleCriteriaList.get(i).getValue();
					criteria.remove(simpleCriteriaList.get(i));
				} else if (simpleCriteriaList.get(i).getFieldName().equals(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS)) {
					reconStatusCriteriaValue = simpleCriteriaList.get(i).getValue();
					criteria.remove(simpleCriteriaList.get(i));
				}
			}
		}
				
		//check for taskid parameter, it shall be there if this screen is called from ToDoList for approval purpose
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.TASKID)!=null) {
			String taskId = request.getParams().get(DataNameTokens.TASKID);
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
			
			bpmAdapter.synchronize();
			HashMap<String,String> fundInReconRecordIds = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.FUNDINRECONRECORDID);
			
			//seems this is not used, so commented it
//			JPQLAdvancedQueryCriteria taskCriteria = new JPQLAdvancedQueryCriteria("or");
			
//			for (int i=0;i<fundInReconRecordIds.size();i++) {				
//				String fundInReconRecordId = fundInReconRecordIds.get("data"+i);
//				if (fundInReconRecordId != null) {
//					JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
//					simpleCriteria.setFieldName(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
//					simpleCriteria.setOperator("equals");
//					simpleCriteria.setValue(fundInReconRecordId);
//					simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
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
			 * Not sure of the side effects here so I decided to just leave the
			 * multiple OR criteria in the request and remove it before performing
			 * the query and replace it with IN(1,2,3,4,5) etc.
			 */
//			criteriaAndTaskCriteria.remove(taskCriteria);
			
			/*
			 * Build a new simple criteria as an IN() list
			 */
			JPQLSimpleQueryCriteria inCriteria = new JPQLSimpleQueryCriteria();
			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
			inCriteria.setFieldName(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
			
			for(String value:fundInReconRecordIds.values()){
				if(fundsInReconRecordIdList.isEmpty()){
					try{
						fundsInReconRecordIdList = new Long(value).toString();
					}catch(NumberFormatException e){
						//DO nothing
					}
				}
				else{
					try{
						fundsInReconRecordIdList = fundsInReconRecordIdList + "," + new Long(value);
					}catch(NumberFormatException e){
						//DO nothing
					}
				}
			}
			inCriteria.setValue(fundsInReconRecordIdList);
			inCriteria.setOperator("IN");
			
			criteriaAndTaskCriteria.add(inCriteria);
		}
		
		Locator<FinArFundsInJournalTransaction> finJournalTransactionLocator = null;
		
		//Check for journal Group Id, it will be there if the screen is called from Journal Screen
		String journalGroupId = null;
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
			journalGroupId = request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
		}
		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<FinArFundsInReconRecord> fundInReconRecordLocator = null;
		Locator<VenOrder> venOrderLocator=null;		
		
		try {
			venOrderLocator = new Locator<VenOrder>();
			
//			List<FinArFundsInJournalTransaction>  finJournalTransactionList = null;
			List<FinArFundsInReconRecord> fundInReconRecordList = null;
			List<FinArFundsInReconRecord> fundInReconRecordListSize = null;
			fundInReconRecordLocator = new Locator<FinArFundsInReconRecord>();			
			int count=0;
			
			VenOrderSessionEJBRemote venOrderSessionHome = (VenOrderSessionEJBRemote) venOrderLocator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote sessionHome = (FinArFundsInReconRecordSessionEJBRemote) fundInReconRecordLocator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			//Check for journal Group Id, it will be there if the screen is called from Journal Screen
			//This section queries for finJournalTransactions that are part of the finJournalApprovalGroup
			if (journalGroupId!=null) {
				finJournalTransactionLocator = new Locator<FinArFundsInJournalTransaction>();				
			
				String select = "select o from FinArFundsInReconRecord o where o.reconciliationRecordId in "+
				"(select a.finArFundsInReconRecords.reconciliationRecordId from FinArFundsInJournalTransaction a "+
				"where a.finJournalTransactions.finJournalApprovalGroup.journalGroupId = " + journalGroupId+")";
				
				fundInReconRecordList = sessionHome.queryByRange(select, 0, 0);				
				
			}else{
					if ((criteria == null && criteriaAndTaskCriteria == null)
							|| (criteria!=null && !criteria.getListIterator().hasNext())
							|| (criteriaAndTaskCriteria!=null && !criteriaAndTaskCriteria.getListIterator().hasNext())) {
						
						String select = "select o from FinArFundsInReconRecord o where o.finArFundsInActionApplied.actionAppliedId <> "+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED;
						fundInReconRecordList = sessionHome.queryByRange(select, 0, 0);					
					} else {	
						FinArFundsInReconRecord finArFundsInReconRecord = new FinArFundsInReconRecord();
						if (criteriaAndTaskCriteria!=null) {
							String sql = "select o from FinArFundsInReconRecord o where o.reconciliationRecordId IN ("+fundsInReconRecordIdList+") and o.finArFundsInActionApplied.actionAppliedId <> "+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED;							
							fundInReconRecordList = sessionHome.queryByRange(sql, 0,0);						
						} else {
							JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
							simpleCriteria.setFieldName(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID);
							simpleCriteria.setOperator("notEquals");
							simpleCriteria.setValue(""+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED);
							simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID));
							criteria.add(simpleCriteria);						
							fundInReconRecordList = sessionHome.findByFinArFundsInReconRecordLike(finArFundsInReconRecord, criteria, 0, 0);
							}
					}					
			}
			String fetch = "true";;
			if(count<=(request.getStartRow()+(fundInReconRecordList.size()>0?fundInReconRecordList.size():0))){
				fetch="false";
			}
			boolean show =true;
			for (int i=0;i<fundInReconRecordList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				show =true;
				FinArFundsInReconRecord fundInReconRecord = fundInReconRecordList.get(i);
				// Calculate Aging here
				Date now = fundInReconRecord.getReconcilliationRecordTimestamp()!=null?fundInReconRecord.getReconcilliationRecordTimestamp():Calendar.getInstance().getTime();
				Date orderDate = fundInReconRecord.getOrderDate();
				long aging=new Long(0);
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				if (now!=null && orderDate!=null) {
					aging = Math.round((now.getTime() - orderDate.getTime()) / (1000 * 60 * 60 * 24));
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_AGING, new Long(aging).toString());
				}
				
				
				if(fundInReconRecord.getWcsOrderId()!=null){
					List<VenOrder>  venOrderList = null;
					String selectOrder = "select u from VenOrder u where u.wcsOrderId =  '"+fundInReconRecord.getWcsOrderId()+"'";
					venOrderList = venOrderSessionHome.queryByRange(selectOrder, 0, 0);
					if(venOrderList!=null ) {
						if(venOrderList.get(0).getVenOrderStatus().getOrderStatusId().equals(VeniceConstants.VEN_ORDER_STATUS_X)){	
							if(aging>5 && fundInReconRecord.getReconcilliationRecordTimestamp()==null) show=false;														
						}
						if(show){
							map.put(DataNameTokens.FINARFUNDSINRECONRECORD_STATUSORDER, venOrderList!=null?venOrderList.get(0).getVenOrderStatus().getOrderStatusCode():"");
						}			
					}
				}else{
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_STATUSORDER, "");
				}				
				if(show){
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, fundInReconRecord.getReconciliationRecordId()!=null?fundInReconRecord.getReconciliationRecordId().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_WCSORDERID, fundInReconRecord.getWcsOrderId());
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_ORDERDATE, fundInReconRecord.getOrderDate()!=null?formatter.format(fundInReconRecord.getOrderDate()):"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILLIATIONDATE, fundInReconRecord.getReconcilliationRecordTimestamp()!=null?formatter.format(fundInReconRecord.getReconcilliationRecordTimestamp()):"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKID, (fundInReconRecord.getVenOrderPayment()!=null && fundInReconRecord.getVenOrderPayment().getVenBank()!=null)?fundInReconRecord.getVenOrderPayment().getVenBank().getBankId().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, (fundInReconRecord.getVenOrderPayment()!=null && fundInReconRecord.getVenOrderPayment().getVenBank()!=null)?fundInReconRecord.getVenOrderPayment().getVenBank().getBankShortName():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION, fundInReconRecord.getFinArFundsInReport()!=null?fundInReconRecord.getFinArFundsInReport().getFileNameAndLocation():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_ORDERPAYMENTID, (fundInReconRecord.getVenOrderPayment() != null && fundInReconRecord.getVenOrderPayment().getOrderPaymentId()!=null)?fundInReconRecord.getVenOrderPayment().getOrderPaymentId().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID, fundInReconRecord.getVenOrderPayment()!=null?fundInReconRecord.getVenOrderPayment().getWcsPaymentId():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEID, (fundInReconRecord.getVenOrderPayment()!=null && fundInReconRecord.getVenOrderPayment().getVenWcsPaymentType()!=null)?fundInReconRecord.getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeId().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, (fundInReconRecord.getVenOrderPayment()!=null && fundInReconRecord.getVenOrderPayment().getVenWcsPaymentType()!=null)?fundInReconRecord.getVenOrderPayment().getVenWcsPaymentType().getWcsPaymentTypeDesc():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTID,(fundInReconRecord.getFinArFundsInReport()!=null?fundInReconRecord.getFinArFundsInReport().getPaymentReportId()+"":""));
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEID,(fundInReconRecord.getFinArFundsInReport()!=null?fundInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().toString():""));
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORT_FINARFUNDINREPORTTYPE_PAYMENTREPORTTYPEDESC,(fundInReconRecord.getFinArFundsInReport()!=null?fundInReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeDesc():""));
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_NOMOR_REFF,fundInReconRecord.getNomorReff()!=null?fundInReconRecord.getNomorReff():"");
						//map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT,  (fundInReconRecord.getVenOrderPayment()!=null && fundInReconRecord.getVenOrderPayment().getAmount()!=null)?fundInReconRecord.getVenOrderPayment().getAmount().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT,  (fundInReconRecord.getPaymentAmount()!=null && fundInReconRecord.getPaymentAmount()!=null)?fundInReconRecord.getPaymentAmount().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT, fundInReconRecord.getProviderReportPaidAmount()!=null?fundInReconRecord.getProviderReportPaidAmount().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_REFUNDAMOUNT, fundInReconRecord.getRefundAmount()!=null?fundInReconRecord.getRefundAmount().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT, fundInReconRecord.getRemainingBalanceAmount()!=null?fundInReconRecord.getRemainingBalanceAmount().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC, fundInReconRecord.getFinArReconResult()!=null?fundInReconRecord.getFinArReconResult().getReconResultDesc():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSID, fundInReconRecord.getFinApprovalStatus()!=null?fundInReconRecord.getFinApprovalStatus().getApprovalStatusId().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC, fundInReconRecord.getFinApprovalStatus()!=null?fundInReconRecord.getFinApprovalStatus().getApprovalStatusDesc():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDID, fundInReconRecord.getFinArFundsInActionApplied()!=null?fundInReconRecord.getFinArFundsInActionApplied().getActionAppliedId().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINACTION_APPLIEDDESC,fundInReconRecord.getFinArFundsInActionApplied()!=null?fundInReconRecord.getFinArFundsInActionApplied().getActionAppliedDesc():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTFEEAMOUNT, fundInReconRecord.getProviderReportFeeAmount()!=null?fundInReconRecord.getProviderReportFeeAmount().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENT, fundInReconRecord.getComment());
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_COMMENTHISTORY, "&#9658");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FETCH, fetch);
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_ID, fundInReconRecord.getFinArFundsIdReportTime()!=null ?fundInReconRecord.getFinArFundsIdReportTime().getReportTimeId().toString():"");
						map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDINREPORTTIME_REPORT_TIME_DESC, fundInReconRecord.getFinArFundsIdReportTime()!=null?fundInReconRecord.getFinArFundsIdReportTime().getReportTimeDesc().toString():"");
						
						
						
						//Calculate Reconciliation Status here
						String reconResult = map.get(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC);
						String approvalResult = map.get(DataNameTokens.FINARFUNDSINRECONRECORD_FINAPPROVALSTATUS_APPROVALSTATUSDESC);
						
						if (reconResult!=null && approvalResult!=null) {
							if (reconResult.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_PAYMENTNOTRECOGNIZED) || reconResult.equals(DataConstantNameTokens.FINARRECONRESULT_RECONRESULTDESC_NOPAYMENT)  && approvalResult.equals(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSDESC_NEW)) {
								map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS, DataConstantNameTokens.FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_UNRECONCILED);
							} else {
								map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS, DataConstantNameTokens.FINRECONCILIATIORESULT_RECONCILIATIONRESULTDESC_RECONCILED);
							}
						}
						
						boolean bExclude = false;
		
						//Aging filter
						if (agingCriteriaValue != null) {
							if (map.get(DataNameTokens.FINARFUNDSINRECONRECORD_AGING) != null &&
									!map.get(DataNameTokens.FINARFUNDSINRECONRECORD_AGING).equals(agingCriteriaValue)) {
								bExclude = true;
							}
						}
						
						//Reconciliation Status filter
						if (reconStatusCriteriaValue != null) {
							if (map.get(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS) != null &&
									!map.get(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONSTATUS).equals(reconStatusCriteriaValue)) {
								bExclude = true;
							}
						}
						
						if (!bExclude) {
							dataList.add(map);
						}
				}
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
				if (fundInReconRecordLocator!=null) {
					fundInReconRecordLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}	
}
