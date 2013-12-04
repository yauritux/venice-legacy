package com.gdn.venice.facade.finance.fundsin;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInReport;

/**
 * Session bean remote interface for finance reconciliation helper bean.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Remote
public interface FundsInReconciliationHelperSessionEJBRemote {
    /**
     * Deletes a funds-in report based on the report identifier
     * which is part of the description stored with the report.
     * 
     * Note that several other actions and validations need to be applied.
     * 1) Restrict deletion 
	 *		o if the reports hits a record referencing an order with more than one payment
	 *		o if the order is in any status other than "C"
	 *		o any recon record in the report has been processed in any way
     * 2) Update fin_ar_funds_in_recon_record (each record linked to the file)
	 * 		o reconcilliation_record_timestamp = null
	 *		o order_payment_id = null
	 * 		o payment_report_id = null
     *  	o provider_report_payment_date = null
	 *		o provider_report_payment_id = null
	 *		o provider_report_paid_amount = null
	 *		o provider_report_fee_amount = null
	 *		o remaining_balance_amount = CALCULATED
	 *		o payment_confirmation_number = null
	 *		o recon_result_id = 4
	 *
     * @param bank
     * @param reportType
     * @param reportIdentifier
     * @return
     */
	public Boolean deleteFundsInReport(List<FinArFundsInReport> finArFundsInReportRecordList);
}
