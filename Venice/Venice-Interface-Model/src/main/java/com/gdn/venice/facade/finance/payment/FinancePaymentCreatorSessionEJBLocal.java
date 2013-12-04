package com.gdn.venice.facade.finance.payment;
import java.util.ArrayList;

import javax.ejb.Local;

/**
 * Session bean local interface for finance payment creation
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Local
public interface FinancePaymentCreatorSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #createRefundPayment(			
	 * 		Long finArFundsInRefundId,
	 * 		ArrayList<Long> manualJournalTransactionList,
	 *		Double penaltyAmount,
	 *		Double paymentAmount,
	 *		Long bankAccountId)
	 */
	public Boolean createRefundPayment(			
			Long finArFundsInRefundId,
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #createMerchantPayment(
	 * 		ArrayList<Long> salesRecordIdList, 
	 *		ArrayList<Long> manualJournalTransactionList,
	 *		Double penaltyAmount,
	 *		Double paymentAmount,
	 *		Long bankAccountId)
	 */
	public Boolean createMerchantPayment(
			ArrayList<Long> salesRecordIdList, 
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId,
			Double pph23Amount);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #createLogisticsPayment(			
	 * 		ArrayList<Long> finApInvoiceIdList, 
	 *		ArrayList<Long> manualJournalTransactionList,
	 *		Double penaltyAmount,
	 *		Double paymentAmount,
	 *		Long bankAccountId)
	 */
	public Boolean createLogisticsPayment(
			ArrayList<Long> finApInvoiceIdList, 
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId);
}
