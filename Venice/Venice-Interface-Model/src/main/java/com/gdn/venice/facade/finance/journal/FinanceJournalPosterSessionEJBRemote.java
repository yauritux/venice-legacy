package com.gdn.venice.facade.finance.journal;

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.persistence.LogAirwayBill;

/**
 * Session bean remote interface for finance journal posting
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Remote
public interface FinanceJournalPosterSessionEJBRemote {

	/**
	 * Posts a list of cash receive journal transactions along with a rolled up
	 * journal entry. This is called when the funds-in reconciliation record is
	 * approved (grouped by approval). Note that a debit must also be posted
	 * against the bank account that the record has come from. o BANK - DEBIT o
	 * UANG MUKA PELANGGAN - CREDIT
	 * 
	 * The call is handled from the UI via BPM
	 * 
	 * @param finArFundsInReconRecordIdList
	 * @return returns true if the posting of transactions succeeds else false.
	 */
	public Boolean postCashReceiveJournalTransactions(
			ArrayList<Long> finArFundsInReconRecordIdList);

	/**
	 * Posts an allocation journal transaction that reduces the allocation from
	 * the origin order and allocates the amount to the destination order. A
	 * journal entry is created in the allocation journal and another in the
	 * rolled up journal (1:1)
	 * 
	 * Note EJB only - can't be exposed as WS due to non-XML-serializable return
	 * type
	 * 
	 * This is called from the UI when the user allocates part of a payment from
	 * one order to another.
	 * 
	 * Note that either sourceFundsInReconRecordId or sourceVenOrderPaymentId
	 * MUST be NULL o this is required to handle orphan payment allocation
	 * 
	 * Case 1) sourceFundsInReconRecordId NOT NULL
	 * ------------------------------------------- *** Firstly the
	 * destinationVenOrderPaymentId MUST be provided.
	 * 
	 * If the source is a sourceFundsInReconRecordId and the destination payment
	 * type is CC then the destination payment payment_confirmation_number shall
	 * be updated with the payment_confirmation_number from the source
	 * FundsInReconRecord.
	 * 
	 * Case 2) sourceVenOrderPaymentId NOT NULL
	 * ------------------------------------------ ***
	 * destinationVenOrderPaymentId may be NULL
	 * 
	 * If destinationVenOrderPaymentId is NULL then the source payment is
	 * allocated to the order and any existing payment allocations in the
	 * destination order remain untouched.
	 * 
	 * If destinationVenOrderPaymentId is NOT NULL the allocations for the
	 * nominated destinationVenOrderPaymentId are to be replaced by the
	 * allocations from the source payment identified by
	 * (sourceVenOrderPaymentId).
	 * 
	 * @param sourceVenOrderPaymentId
	 *            is the payment to allocate from
	 * @param sourceFundsInReconRecordId
	 *            is the reconciliation record to alocate from
	 * @param destinationVenOrderPaymentId
	 *            is the payment to allocate to (if null then no change needed).
	 * @param allocationAmount
	 * @param destinationVenOrderId
	 * @return
	 */
	public Boolean allocatePaymentAndpostAllocationJournalTransaction(
			Long sourceVenWCSOrderId,Long sourceVenOrderPaymentId, 
			Long fundsInReconRecordId, Long destinationVenOrderPaymentId,
			Double allocationAmount, Long destinationVenOrderId);

	/**
	 * Creates a refund journal transaction and a fee transaction (either a bank
	 * fee or a GDN fee). A rolled up journal entry is also created for the
	 * journal transaction and the fee transaction (1:1).
	 * 
	 * Called from the UI when the user refunds an amount.
	 * 
	 * Note EJB only - can't be exposed as WS due to non-XML-serializable return
	 * type
	 * 
	 * @param reconciliationRecordId
	 * @param refundAmount
	 *            the amount to refund
	 * @param fee
	 *            the fees to be taken from the payment prior to refund
	 * @param refundType
	 * 			  the type of refund (customer or bank)
	 * @return true if the operation succeeds else false
	 */
	public Boolean postRefundJournalTransaction(Long reconciliationRecordId,
			Double refundAmount, Double fee, int refundType, boolean printjournal);

	/**
	 * Creates a balancing journal transaction that will act as an adjustment to
	 * balance sales journal against cash received. The value will be the
	 * difference between the two journals.
	 * 
	 * This is called from the UI and usually is used to balance off small
	 * amounts.
	 * 
	 * Note EJB only - can't be exposed as WS due to non-XML-serializable return
	 * type
	 * 
	 * @param cashReceivedJournalId
	 * @param salesJournalId
	 * @return true if the operation succeeds else false
	 */
	public Boolean postBalancingJournalTransaction(Long cashReceivedJournalId,
			Long salesJournalId);

	/**
	 * Creates the invoice in FinAPInvoice and creates the journal transactions
	 * plus the rolled up journal. The scenario is for logistics debt
	 * acknowledgement. The invoice number comes from the file entity that the
	 * reconciliation records are attached to.
	 * 
	 * This is to be called from the logistics module UI on approval via BPM.
	 * 
	 * Note EJB only - can't be exposed as WS due to non-XML-serializable return
	 * type
	 * 
	 * @param logInvoiceReconRecordId
	 * @return true if the operation succeeds else false
	 */
	public Boolean postLogisticsDebtAcknowledgementJournalTransaction(
			FinApInvoice finApInvoice, LogAirwayBill logAirwayBill);

	/**
	 * Creates a list of sales journal transactions based on the sales records
	 * that have been created when orders are placed and sold etc.
	 * 
	 * Tax journal transactions are created based on 10% when the Boolean pkp is
	 * true.
	 * 
	 * Called from the CX event in MTA
	 * 
	 * @param finSalesRecordId
	 * @return true if the operation succeeds else false
	 */
	public Boolean postSalesJournalTransaction(Long finSalesRecordId,
			Boolean pkp);

	/**
	 * Creates a payment journal transaction based on the payment record.
	 * 
	 * This is to be called from BPM after the payment is approved.
	 * 
	 * 
	 * @param finApPaymentId
	 *            is the payment to post to journal
	 * @param finAccountId
	 *            is the id of the bank account ot which the debit transaction
	 *            will be written
	 * @return true if the operation succeeds else false
	 */
	public Boolean postPaymentJournalTransaction(Long finApPaymentId);

	/**
	 * Creates a manual journal entry with a credit or debit amoutn and
	 * comments.
	 * 
	 * This is called from the UI when the finance user wishes to make an
	 * adjustment ot any account.
	 * 
	 * @param journalApprovalGroupDesc
	 * @param journalApprovalGroupTimestamp
	 * @param journalEntryList
	 * @return
	 */
	public Boolean postManualJournalTransaction(
			String journalApprovalGroupDesc,
			Date journalApprovalGroupTimestamp,
			ArrayList<ManualJournalEntryDTO> journalEntryList);

	public abstract Boolean postRefundJournalTransaction(Long finSalesRecordId,
			Boolean pkp);
	/**
	 * 
	 * @param finArFundsInReconRecordIdList
	 * @return
	 */
	public Boolean postVirtualAccountH1Journal(ArrayList<Long> finArFundsInReconRecordIdList);
}
