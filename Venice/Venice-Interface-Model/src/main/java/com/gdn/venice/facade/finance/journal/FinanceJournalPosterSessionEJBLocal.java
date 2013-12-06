package com.gdn.venice.facade.finance.journal;

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.persistence.LogAirwayBill;

/**
 * Session bean local interface for finance journal posting.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Local
public interface FinanceJournalPosterSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postCashReceiveJournalTransaction(ArrayList<Long>
	 * finArFundsInReconRecordIdList)
	 */
	public Boolean postCashReceiveJournalTransactions(
			ArrayList<Long> finArFundsInReconRecordIdList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postAllocationJournalTransaction(Long sourceVenOrderPaymentId, Long
	 * fundsInReconRecordId, Long destinationVenOrderPaymentId, Double
	 * allocationAmount, Long destinationVenOrderId)
	 */
	public Boolean allocatePaymentAndpostAllocationJournalTransaction(
			Long sourceVenWCSOrderId,Long sourceVenOrderPaymentId,
			Long fundsInReconRecordId, Long destinationVenOrderPaymentId,
			Double allocationAmount, Long destinationVenOrderId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postRefundJournalTransaction(Long reconciliationRecordId, Double
	 * refundAmount, Double fee, Long accountId, int refundType)
	 */
	public Boolean postRefundJournalTransaction(Long reconciliationRecordId,
			Double refundAmount, Double fee, int refundType,boolean printjournal);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postBalancingJournalTransaction(Long cashReceivedJournalId, Long
	 * salesJournalId)
	 */
	public Boolean postBalancingJournalTransaction(Long cashReceivedJournalId,
			Long salesJournalId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postLogisticsDebtAcknowledgementJournalTransactions(ArrayList<Long>
	 * logInvoiceReconRecordId)
	 */
	public Boolean postLogisticsDebtAcknowledgementJournalTransaction(
			FinApInvoice finApInvoice, LogAirwayBill logAirwayBill);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postSalesJournalTransactions(ArrayList<Long> finSalesRecordId, Boolean
	 * pkp)
	 */
	public Boolean postSalesJournalTransaction(Long finSalesRecordId,
			Boolean pkp);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postPaymentJournalTransaction(Long finApPaymentId)
	 */
	public Boolean postPaymentJournalTransaction(Long finApPaymentId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #postManualJournalTransaction(String journalApprovalGroupDesc, Date
	 * journalApprovalGroupTimestamp, ArrayList<ManualJournalEntryDTO>
	 * journalEntryList)
	 */
	public Boolean postManualJournalTransaction(
			String journalApprovalGroupDesc,
			Date journalApprovalGroupTimestamp,
			ArrayList<ManualJournalEntryDTO> journalEntryList);

	public abstract Boolean postRefundJournalTransaction(Long finSalesRecordId,
			Boolean pkp);
	
	/**
	 * @param finArFundsInReconRecordIdList
	 * @return
	 */
	public Boolean postVirtualAccountH1Journal(ArrayList<Long> finArFundsInReconRecordIdList);
}
