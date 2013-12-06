package com.gdn.venice.facade.logistics.invoice;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogInvoiceReportUpload;

/**
 * LogAWBInvoiceApprovalHelperSessionEJBRemote.java
 * 
 * Remote interface for the airway bill approval helper EJB.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Remote
public interface LogAWBInvoiceApprovalHelperSessionEJBRemote {

	/**
	 * Applies the invoice reconciliation data to the airway bill.
	 * Should be called when the AWB reconciliation has been approved.
	 * @param logAirwayBill
	 * @return
	 */
	public LogInvoiceReportUpload applyInvoiceReconciliationData(LogInvoiceReportUpload invoiceReportUpload);
	
	/**
	 * Created a logistics finance invoice based on the report name that has been reconciled.
	 * @param fileName
	 * @return
	 */
	public Boolean createLogisticsFinanceInvoice(LogInvoiceReportUpload invoiceReportUpload);
	
}
