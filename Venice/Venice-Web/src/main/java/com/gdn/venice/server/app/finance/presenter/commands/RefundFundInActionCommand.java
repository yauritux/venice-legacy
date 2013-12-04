package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.EJBException;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
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
public class RefundFundInActionCommand implements RafRpcCommand{
	HashMap<String, String> refundDataMap;
	
	/**
	 * Constructor to extract the form parameters
	 * @param parameter
	 */
	public RefundFundInActionCommand(String parameter) {
		refundDataMap = Util.formHashMapfromXML(parameter);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	public String execute() {
		String errorMsg=null;
		for (int i=0;i<refundDataMap.size();i++) {
			String refundDataString = refundDataMap.get("REFUNDDATA" + i);
			
			if (refundDataString!=null) {
			
				Pattern p1 = Pattern.compile("[\\{\\}\\=\\, ]++");
				String[] split = p1.split( refundDataString );
	
				HashMap<String,String> refundMap = new HashMap<String,String>();
				for ( int j=1; j< split.length; j+=2 ) {
					refundMap.put( split[j], split[j+1] );
				}
				
				Long reconciliationRecordId = new Long(refundMap.get("RECONCILIATIONRECORDID"));
				Long idRefund= new Long(refundMap.get("IDREFUND"));
				int account = 0;
				if (refundMap.get("ACCOUNT")!=null && refundMap.get("ACCOUNT").equals("Customer")) {
					account = 0;
				} else if (refundMap.get("ACCOUNT")!=null && refundMap.get("ACCOUNT").equals("Bank")) {
					account = 1;
				}
				String cancelRefund =refundMap.get("CANCEL");
				Double fee = new Double(refundMap.get("FEE"));
				Double refundAmount = new Double(refundMap.get("REFUNDAMOUNT"));
				System.out.println("refund = "+ refundAmount);
				System.out.println("IdReconRecord = "+ reconciliationRecordId);
				
				Locator<FinanceJournalPosterSessionEJBRemote> financeJournalPosterLocator = null;
				Locator<Object> locator = null;
				List<FinArFundsInJournalTransaction> finArFundsInJournalTransactionList = null;
				try {
					locator = new Locator<Object>();
					financeJournalPosterLocator = new Locator<FinanceJournalPosterSessionEJBRemote>();
					FinanceJournalPosterSessionEJBRemote sessionHome = (FinanceJournalPosterSessionEJBRemote) financeJournalPosterLocator
							.lookup(FinanceJournalPosterSessionEJBRemote.class,
									"FinanceJournalPosterSessionEJBBean");
					FinArFundsInJournalTransactionSessionEJBRemote finArFundsInJournalTransactionHome = (FinArFundsInJournalTransactionSessionEJBRemote) locator
					.lookup(FinArFundsInJournalTransactionSessionEJBRemote.class, "FinArFundsInJournalTransactionSessionEJBBean");
					
					FinArFundsInReconRecordSessionEJBRemote fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
					.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
					
					FinArFundsInRefundSessionEJBRemote refundHome = (FinArFundsInRefundSessionEJBRemote) locator
					.lookup(FinArFundsInRefundSessionEJBRemote.class, "FinArFundsInRefundSessionEJBBean");
					
					FinArFundsInAllocatePaymentSessionEJBRemote finArFundsInAllocateHome = (FinArFundsInAllocatePaymentSessionEJBRemote) locator
					.lookup(FinArFundsInAllocatePaymentSessionEJBRemote.class, "FinArFundsInAllocatePaymentSessionEJBBean");
									
					
					if(cancelRefund.equals("FALSE")){
						List<FinArFundsInReconRecord> reconRecordListTemp = fundsInReconRecordHome
						.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " + reconciliationRecordId+" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);				
		
						if(!reconRecordListTemp.isEmpty()){
							if(reconRecordListTemp.get(0).getProviderReportPaidAmount().subtract(reconRecordListTemp.get(0).getRefundAmount()).subtract(new BigDecimal(refundAmount)).compareTo(new BigDecimal(0))>=0){
								sessionHome.postRefundJournalTransaction(reconciliationRecordId, refundAmount, fee, account,false);
							}else{
								errorMsg="Refund Amount harus lebih kecil atau sama dengan Paid Amount!";
								throw new EJBException("Refund Amount harus lebih kecil atau sama dengan Paid Amount!");
							}			
						}								
					}else{
						List<FinArFundsInReconRecord> reconRecordListTemp = fundsInReconRecordHome
						.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " + reconciliationRecordId+" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);				
						if(!reconRecordListTemp.isEmpty() && reconRecordListTemp.size()>0){
																			
								FinArFundsInReconRecord item = reconRecordListTemp.get(0);
								item.setRefundAmount(item.getRefundAmount().subtract(new BigDecimal(refundAmount)));
								
								BigDecimal paid = (item.getPaymentAmount()!=null?item.getPaymentAmount():new BigDecimal(0)).subtract(item.getProviderReportPaidAmount().subtract(item.getRefundAmount()));
								item.setRemainingBalanceAmount(paid);
								BigDecimal remainingBalanceAmount =item.getRemainingBalanceAmount().abs().compareTo(new BigDecimal(5000)) <= 0 ? new BigDecimal(0):item.getRemainingBalanceAmount();
								FinArReconResult finArReconResult = new FinArReconResult();
								if (remainingBalanceAmount.compareTo(new BigDecimal(0)) == 0) {
									finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_ALL);
								} else if (remainingBalanceAmount.compareTo(new BigDecimal(0)) > 0) {
									finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_PARTIAL);
								} else {
									finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_OVERPAID);
								}
								 //payment not recognized
								if(item.getRemainingBalanceAmount().compareTo(new BigDecimal(0)) < 0 && (item.getWcsOrderId()==null || item.getWcsOrderId()=="")){
									finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NOT_RECOGNIZED);
								}							
								item.setFinArReconResult(finArReconResult);							
								
														
								finArFundsInJournalTransactionList = finArFundsInJournalTransactionHome.queryByRange("select o from FinArFundsInJournalTransaction o where o.finArFundsInReconRecords.reconciliationRecordId="+item.getReconciliationRecordId(), 0, 0);
								FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
								if(finArFundsInJournalTransactionList!=null && finArFundsInJournalTransactionList.size()>0){
									finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
								}else{
									finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
								}
								item.setFinApprovalStatus(finApprovalStatus);
								
								FinArFundsInActionApplied finArFundsInActionApplied = new FinArFundsInActionApplied();	
									List<FinArFundsInRefund> itemRefundList = refundHome.queryByRange("select o from FinArFundsInRefund o where o.finArFundsInReconRecord.reconciliationRecordId="+reconciliationRecordId+" order by o.refundRecordId desc", 0, 0);
									for(FinArFundsInRefund n : itemRefundList){
										if(n.getRefundRecordId().equals(idRefund)){											
											n.setApAmount(new BigDecimal(0));
											 refundHome.mergeFinArFundsInRefund(n);
										}else if(finArFundsInActionApplied.getActionAppliedId()==null && n.getApAmount().compareTo(new BigDecimal(0))>0){
											if((n.getRefundType()!=null?n.getRefundType():"").contains("Bank")){
													finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_BANK);	
											}else{
												    finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_CUSTOMER);	
											}
										}
									}									
								List<FinArFundsInAllocatePayment> itemAllocate = finArFundsInAllocateHome.queryByRange("select o from FinArFundsInAllocatePayment o where o.idReconRecordSource="+reconciliationRecordId, 0, 0);
								if(itemAllocate!=null && itemAllocate.size()>0){
									finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_ALLOCATED);												
								}else if(finArFundsInActionApplied.getActionAppliedId()==null){
									finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
								}
								item.setFinArFundsInActionApplied(finArFundsInActionApplied);	
								item= fundsInReconRecordHome.mergeFinArFundsInReconRecord(item);
							}
					}
				} catch (Exception ex) {
//					String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
					ex.printStackTrace();
					try {
						financeJournalPosterLocator.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return "-1" + ":" + errorMsg;
				} finally {
					try {
						financeJournalPosterLocator.close();
						locator.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		return "0";
	}	
}
