package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.EJBException;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.gdn.venice.persistence.FinArFundsInActionAppliedHistory;
import com.gdn.venice.persistence.FinArFundsInAllocatePayment;
import com.gdn.venice.persistence.FinArFundsInJournalTransaction;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInRefund;
import com.gdn.venice.persistence.FinArReconResult;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

/**
 * Command for processing refunds in funds-in
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class CancelAllocationFundInActionCommand implements RafRpcCommand{
	HashMap<String, String> cancelAllocationDataMap;
	
	/**
	 * Constructor to extract the form parameters
	 * @param parameter
	 */
	public CancelAllocationFundInActionCommand(String parameter) {
		cancelAllocationDataMap = Util.formHashMapfromXML(parameter);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	public String execute() {
		Locator<Object> locator = null;
		String errorMsg = "";
		try {
			locator = new Locator<Object>();
				
			FinArFundsInReconRecordSessionEJBRemote fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
			FinArFundsInJournalTransactionSessionEJBRemote finArFundsInJournalTransactionHome = (FinArFundsInJournalTransactionSessionEJBRemote) locator
			.lookup(FinArFundsInJournalTransactionSessionEJBRemote.class, "FinArFundsInJournalTransactionSessionEJBBean");
								
			FinArFundsInAllocatePaymentSessionEJBRemote finArFundsInAllocateHome = (FinArFundsInAllocatePaymentSessionEJBRemote) locator
			.lookup(FinArFundsInAllocatePaymentSessionEJBRemote.class, "FinArFundsInAllocatePaymentSessionEJBBean");
			
			FinArFundsInRefundSessionEJBRemote refundHome = (FinArFundsInRefundSessionEJBRemote) locator
			.lookup(FinArFundsInRefundSessionEJBRemote.class, "FinArFundsInRefundSessionEJBBean");
			
			FinArFundsInActionAppliedHistorySessionEJBRemote actionHome = (FinArFundsInActionAppliedHistorySessionEJBRemote) locator
			.lookup(FinArFundsInActionAppliedHistorySessionEJBRemote.class, "FinArFundsInActionAppliedHistorySessionEJBBean");
							
				for (int i=0;i<cancelAllocationDataMap.size();i++) {
					String cancelAllocationDataString = cancelAllocationDataMap.get("CANCELDATA" + i);
		
					List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList = null;			
					List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionLists = null;			
					List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistorylist = null;
					
					if (cancelAllocationDataString!=null) {
						Pattern p1 = Pattern.compile("[\\{\\}\\=\\, ]++");
						String[] split = p1.split( cancelAllocationDataString );
			
						HashMap<String,String> cancelMap = new HashMap<String,String>();
						for ( int j=1; j< split.length; j+=2 ) {
							cancelMap.put( split[j], split[j+1] );
						}
						
						Long reconciliationRecordId = new Long(cancelMap.get("RECONCILIATIONRECORDID"));
	
								FinArFundsInReconRecord desc = null;
									FinArFundsInReconRecord source = null;
									List<FinArFundsInAllocatePayment> itemsAllocate = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecord="+reconciliationRecordId, 0, 0);
	
									List<FinArFundsInAllocatePayment> itemsAllocateSrc = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecordSource in (select a.idReconRecordDest from FinArFundsInAllocatePayment a where a.idReconRecord="+reconciliationRecordId+")", 0, 0);
									if(!itemsAllocateSrc.isEmpty()){										
										errorMsg="Dana telah diallokasikan kepada Order lain!!!";
										throw new EJBException("Dana telah diallokasikan kepada Order lain!!!");
									}								
									
									if(!itemsAllocate.isEmpty()){
										List<FinArFundsInReconRecord> destinationList = fundsInReconRecordHome
										.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " + itemsAllocate.get(0).getIdReconRecordDest(), 0, 0);	
										List<FinArFundsInReconRecord> sourceList = fundsInReconRecordHome
										.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " + itemsAllocate.get(0).getIdReconRecordSource(), 0, 0);
										if(!destinationList.isEmpty()){desc=destinationList.get(0);}
										if(!sourceList.isEmpty()){source=sourceList.get(0);}
									if(desc!=null){		
										/**
										 * order lama direconcile ulang 
										 * paid amount + allocation amount dan direconcile ulang
										 * jika order pernah di approved maka status menjadi approved
										 * jika order pernah diallocate ke order lain maka status actiontakennya allocate to order	
										 */
											source.setProviderReportPaidAmount(source.getProviderReportPaidAmount().add(itemsAllocate.get(0).getAmount()));		
											source.setRemainingBalanceAmount((source.getPaymentAmount()!=null?source.getPaymentAmount():new BigDecimal(0)).subtract(source.getProviderReportPaidAmount()));
											BigDecimal remainingBalanceAmount =source.getRemainingBalanceAmount().abs().compareTo(new BigDecimal(5000)) <= 0 ? new BigDecimal(0):source.getRemainingBalanceAmount();
											FinArReconResult finArReconResults = new FinArReconResult();	
											if(source.getWcsOrderId()==null){
												finArReconResults.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NOT_RECOGNIZED);
											}else if(remainingBalanceAmount.compareTo(new BigDecimal(0)) == 0){
												finArReconResults.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_ALL);
											}else if(remainingBalanceAmount.compareTo(new BigDecimal(0)) > 0 && source.getPaymentAmount()==null){
												finArReconResults.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NONE);
											}else if(remainingBalanceAmount.compareTo(new BigDecimal(0)) > 0 ){
												finArReconResults.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_PARTIAL);
											}else{
												finArReconResults.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_OVERPAID);
											}
											source.setFinArReconResult(finArReconResults);				
											
											finArFundsInJournalTransactionList = finArFundsInJournalTransactionHome.queryByRange("select o from FinArFundsInJournalTransaction o where o.finArFundsInReconRecords.reconciliationRecordId="+source.getReconciliationRecordId(), 0, 0);
											FinApprovalStatus finApprovalStatuss = new FinApprovalStatus();
											if(finArFundsInJournalTransactionList!=null && finArFundsInJournalTransactionList.size()>0){
												finApprovalStatuss.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
											}else{
												finApprovalStatuss.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
											}
											source.setFinApprovalStatus(finApprovalStatuss);
											
											FinArFundsInActionApplied finArFundsInActionAppliedss = new FinArFundsInActionApplied();
											List<FinArFundsInRefund> itemRefundList = refundHome.queryByRange("select o from FinArFundsInRefund o where o.finArFundsInReconRecord.reconciliationRecordId="+source.getReconciliationRecordId()+" order by o.refundRecordId desc", 0, 0);
											if(!itemRefundList.isEmpty()){												
												for(FinArFundsInRefund n : itemRefundList){
													if((n.getRefundType()!=null?n.getRefundType():"").contains("Bank") && n.getApAmount().compareTo(new BigDecimal(0))>0 && finArFundsInActionAppliedss.getActionAppliedId()==null){
														finArFundsInActionAppliedss.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_BANK);	
													}else{
														finArFundsInActionAppliedss.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_CUSTOMER);	
													}
												}
											}							
											List<FinArFundsInAllocatePayment> itemAllocate = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecordSource="+source.getReconciliationRecordId() +" and o.idReconRecord <>"+itemsAllocate.get(0).getIdReconRecord()+" order by o.idReconRecord desc", 0, 0);
											if(itemAllocate!=null && itemAllocate.size()>0){
												finArFundsInActionAppliedss.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_ALLOCATED);												
											}else{
												finArFundsInActionAppliedss.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
											}													
											source.setFinArFundsInActionApplied(finArFundsInActionAppliedss);		
											source= fundsInReconRecordHome.mergeFinArFundsInReconRecord(source);
											
											/**
											 * order yang baru di reconcile ulang
											 * jika order pernah menerima dana dari order lain maka paid amount - allocation amount(dana dari order lama)  dan direconcile ulang
											 * jika order pernah di approved maka status menjadi approved
											 * set info report /type report dari order yang pernah didapat
											 * jika order tidak pernah menerima dana dari order lain maka tampilkan record yang lama dan hapus record yang baru
											 */											
											List<FinArFundsInAllocatePayment> itemAllocates = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecordDest="+desc.getReconciliationRecordId()+" and o.idReconRecord <>"+itemsAllocate.get(0).getIdReconRecord()+" order by o.idReconRecord desc", 0, 0);	
											if(itemAllocates!=null && itemAllocates.size()>0){
												
												desc.setProviderReportPaidAmount(desc.getProviderReportPaidAmount().subtract(itemsAllocate.get(0).getAmount()));				
												desc.setRemainingBalanceAmount((desc.getPaymentAmount()!=null?desc.getPaymentAmount():new BigDecimal(0)).subtract(desc.getProviderReportPaidAmount()));
												remainingBalanceAmount =desc.getRemainingBalanceAmount().abs().compareTo(new BigDecimal(5000)) <= 0 ? new BigDecimal(0):desc.getRemainingBalanceAmount();
												FinArReconResult finArReconResult = new FinArReconResult();								
												if(remainingBalanceAmount.compareTo(new BigDecimal(0)) == 0){
													finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_ALL);												
												}else if(remainingBalanceAmount.compareTo(new BigDecimal(0)) > 0 ){
													finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_PARTIAL);
												}else{
													finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_OVERPAID);
												}
												desc.setFinArReconResult(finArReconResult);				
												
												finArFundsInJournalTransactionLists = finArFundsInJournalTransactionHome.queryByRange("select o from FinArFundsInJournalTransaction o where o.finArFundsInReconRecords.reconciliationRecordId="+desc.getReconciliationRecordId(), 0, 0);
												FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
												if(finArFundsInJournalTransactionLists!=null && finArFundsInJournalTransactionLists.size()>0){
													finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
												}else{
													finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
												}
												desc.setFinApprovalStatus(finApprovalStatus);
												
												List<FinArFundsInReconRecord> finArFundsInReconList = fundsInReconRecordHome
												.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " + itemAllocates.get(0).getIdReconRecordSource(), 0, 0);
												if(finArFundsInReconList!=null && finArFundsInReconList.size()>0){
														desc.setFinArFundsInReport(finArFundsInReconList.get(0).getFinArFundsInReport());
														desc.setProviderReportPaymentId(finArFundsInReconList.get(0).getProviderReportPaymentId());
														desc.setProviderReportPaymentDate(finArFundsInReconList.get(0).getProviderReportPaymentDate());
														desc.setPaymentConfirmationNumber(finArFundsInReconList.get(0).getPaymentConfirmationNumber()!=null?finArFundsInReconList.get(0).getPaymentConfirmationNumber():null);
														desc.setNomorReff(finArFundsInReconList.get(0).getWcsOrderId()!=null?finArFundsInReconList.get(0).getWcsOrderId():finArFundsInReconList.get(0).getNomorReff());																
														desc.setUniquePayment(finArFundsInReconList.get(0).getUniquePayment()!=null?finArFundsInReconList.get(0).getUniquePayment():null);							
														desc.setFinArFundsIdReportTime(finArFundsInReconList.get(0).getFinArFundsIdReportTime()!=null?finArFundsInReconList.get(0).getFinArFundsIdReportTime():null);	
														
												}
												FinArFundsInActionApplied finArFundsInActionApplied = new FinArFundsInActionApplied();
												finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_ALLOCATED);			
												desc.setFinArFundsInActionApplied(finArFundsInActionApplied);	
												fundsInReconRecordHome.mergeFinArFundsInReconRecord(desc);
											}else{
												List<FinArFundsInReconRecord> finArFundsInReconList = fundsInReconRecordHome
												.queryByRange("select o from FinArFundsInReconRecord o where o.wcsOrderId = '" + desc.getWcsOrderId()+"' and o.finArFundsInActionApplied.actionAppliedId="+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
												FinArFundsInReconRecord items =null;
												if(finArFundsInReconList!=null && finArFundsInReconList.size()>0){
													items = finArFundsInReconList.get(0);
													FinArFundsInActionApplied finArFundsInActionApplied = new FinArFundsInActionApplied();
													List<FinArFundsInRefund> itemRefund= refundHome.queryByRange("select o from FinArFundsInRefund o where o.finArFundsInReconRecord.reconciliationRecordId="+items.getReconciliationRecordId()+" order by o.refundRecordId desc", 0, 0);
													if(!itemRefund.isEmpty()){		
														for(FinArFundsInRefund n : itemRefund){
																if((n.getRefundType()!=null?n.getRefundType():"").contains("Bank") && n.getApAmount().compareTo(new BigDecimal(0))>0 && finArFundsInActionApplied.getActionAppliedId()==null){
																	finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_BANK);	
																}else{
																	finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_CUSTOMER);	
																}
														}
													}else{						
														finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);		
													}
													
													finArFundsInJournalTransactionLists = finArFundsInJournalTransactionHome.queryByRange("select o from FinArFundsInJournalTransaction o where o.finArFundsInReconRecords.reconciliationRecordId="+items.getReconciliationRecordId(), 0, 0);
													FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
													if(finArFundsInJournalTransactionLists!=null && finArFundsInJournalTransactionLists.size()>0){
														finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
													}else{
														finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
													}
													items.setFinApprovalStatus(finApprovalStatus);
													
													items.setFinArFundsInActionApplied(finArFundsInActionApplied);													
													fundsInReconRecordHome.mergeFinArFundsInReconRecord(items);		
													
													finArFundsInActionAppliedHistorylist = actionHome.queryByRange("Select o from FinArFundsInActionAppliedHistory o where o.finArFundsInReconRecords.reconciliationRecordId="+desc.getReconciliationRecordId(), 0, 0);
													if(!finArFundsInActionAppliedHistorylist.isEmpty()){
														for(FinArFundsInActionAppliedHistory j : finArFundsInActionAppliedHistorylist){
															j.setFinArFundsInReconRecords(items);
															actionHome.mergeFinArFundsInActionAppliedHistory(j);
														}
													}
													fundsInReconRecordHome.removeFinArFundsInReconRecord(desc);
												}
											}												
											
											finArFundsInAllocateHome.removeFinArFundsInAllocatePaymentList(itemsAllocate);
									}
								}
						}
				}
				} catch (Exception ex) {
//					String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
					ex.printStackTrace();					
					try {
					} catch (Exception e) {
						e.printStackTrace();						
					}
					return "-1" + ":" + errorMsg;
				} finally {
					try {
						locator.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}			
		return "0";
	}	
}
