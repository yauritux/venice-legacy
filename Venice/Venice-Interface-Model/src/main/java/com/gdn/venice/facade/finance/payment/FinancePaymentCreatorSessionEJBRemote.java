package com.gdn.venice.facade.finance.payment;
import java.util.ArrayList;

import javax.ejb.Remote;

/**
 * Session bean remote interface for finance payment creation
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
public interface FinancePaymentCreatorSessionEJBRemote {


	/**
	 * Creates a payment based on a list of refund records that are created 
	 * during the refund journaling process from funds-in.
	 * 
	 * This is called by the UI when the user wants to create a refund payment.
	 * The payment is to be paid from a specific bank account to a party.
	 *
	 * @param finArFundsInRefundId
	 * @param manualJournalTransactionList
	 * @param penaltyAmount
	 * @param paymentAmount
	 * @param bankAccountId
	 * @return
	 */
	public Boolean createRefundPayment(
			Long finArFundsInRefundId,
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId);

	/**
	 * Creates a payment based on the list of sales records.
	 * 
	 * This is called by the UI when finance wishes to process payment for a
	 * merchant.
	 * 
	 * @param salesRecordIdList this list of sales records to be included in the payment
	 * @param manualJournalTransactionList the list of manual journal records used for the penalty amount
	 * @param penaltyAmount the penalty amount for the payment
	 * @param paymentAmount the payment amount
	 * @param bankAccountId the bank account from which the payment is to be made
	 * @return
	 */
	public Boolean createMerchantPayment(
			ArrayList<Long> salesRecordIdList, 
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId,
			Double pph23Amount);

	/**
	 * Creates a payment for a logistic provider based on a list of logistics
	 * invoices.
	 * 
	 * Will be called from th UI when finance processes a payment for a
	 * logistics provider.
	 * 
	 * @param finApInvoiceIdList
	 * @param manualJournalTransactionList
	 * @param penaltyAmount
	 * @param paymentAmount
	 * @param bankAccountId
	 * @return
	 */
	public Boolean createLogisticsPayment(
			ArrayList<Long> finApInvoiceIdList, 
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId);

}
