package com.gdn.venice.facade.callback;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.persistence.LogInvoiceReportUpload;

public class LogInvoiceReportUploadSessionEJBCallback implements
		SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public LogInvoiceReportUploadSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.LogInvoiceReportUploadSessionEJBCallback");
	}

	@SuppressWarnings("unused")
	@Override
	public Boolean onPrePersist(Object businessObject) {
		LogInvoiceReportUpload logInvoiceReportUpload = (LogInvoiceReportUpload) businessObject;
		_log.debug("onPrePersist()");
		return Boolean.TRUE;
	}

	@SuppressWarnings("unused")
	@Override
	public Boolean onPostPersist(Object businessObject) {
		LogInvoiceReportUpload logInvoiceReportUpload = (LogInvoiceReportUpload) businessObject;
		_log.debug("onPostPersist()");
		return Boolean.TRUE;
	}

	@SuppressWarnings("unused")
	@Override
	public Boolean onPreMerge(Object businessObject) {
		LogInvoiceReportUpload logInvoiceReportUpload = (LogInvoiceReportUpload) businessObject;
		_log.debug("onPreMerge()");

//		/*
//		 * Get the existing logInvoiceReportUpload record and determine if this
//		 * is the state transition to RECONCILED. 
//		 * 
//		 * If it is then post then create the logistics debt 
//		 * acknowledgement journal and the invoice record for
//		 * finance.
//		 */
//		Locator<Object> locator = null;
//		try {
//			 locator = new Locator<Object>();
//			LogInvoiceReportUploadSessionEJBRemote logInvoiceReportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
//					.lookup(LogInvoiceReportUploadSessionEJBRemote.class,
//							"LogInvoiceReportUploadSessionEJBBean");
//
//			FinApInvoiceSessionEJBRemote finApInvoiceHome = (FinApInvoiceSessionEJBRemote) locator
//					.lookup(FinApInvoiceSessionEJBRemote.class,
//							"FinApInvoiceSessionEJBBean");
//
//			FinanceJournalPosterSessionEJBRemote financeJournalPosterHome = (FinanceJournalPosterSessionEJBRemote) locator
//					.lookup(FinanceJournalPosterSessionEJBRemote.class,
//							"FinanceJournalPosterSessionEJBBean");
//
//			LogInvoiceReconRecordSessionEJBRemote logInvoiceReconRecordHome = (LogInvoiceReconRecordSessionEJBRemote) locator
//					.lookup(LogInvoiceReconRecordSessionEJBRemote.class,
//							"LogInvoiceReconRecordSessionEJBBean");
//
//			List<LogInvoiceReportUpload> logInvoiceReportUploadList = logInvoiceReportUploadHome
//					.queryByRange(
//							"select o from LogInvoiceReportUpload o where o.invoiceReportUploadId = "
//									+ logInvoiceReportUpload
//											.getInvoiceReportUploadId(), 0, 0);
//
//			LogInvoiceReportUpload existingLogInvoiceReportUpload = logInvoiceReportUploadList
//					.get(0);
//
//			/*
//			 * If the status of the invoice report is fully reconciled for the
//			 * first time then create the invoice and create the debt
//			 * acknowledgement journal entries
//			 */
//			if (logInvoiceReportUpload.getLogReportStatus().getReportStatusId() == VeniceConstants.LOG_REPORT_STATUS_RECONCILED
//					&& existingLogInvoiceReportUpload.getLogReportStatus()
//							.getReportStatusId() != VeniceConstants.LOG_REPORT_STATUS_RECONCILED) {
//
//				/*
//				 * Only create the invoice and the LDA journals once
//				 * if somehow it gets reconciled a second time from 
//				 * database twiddling etc. then the twiddler will have
//				 * to also twiddle the LDA and invoice records.
//				 */
//				if (logInvoiceReportUpload.getFinApInvoice() == null) {
//					FinApInvoice invoice = new FinApInvoice();
//
//					/*
//					 * Sum the reconciliation records where the row is
//					 * reconciled OK
//					 */
//					List<LogInvoiceReconRecord> logInvoiceReconRecordList = logInvoiceReconRecordHome
//							.queryByRange(
//									"select o from LogInvoiceReconRecord o where o.logInvoiceReportUpload.invoiceReportUploadId = "
//											+ logInvoiceReportUpload
//													.getInvoiceReportUploadId(),
//									0, 0);
//					BigDecimal invoiceTotal = new BigDecimal(0);
//					List<LogAirwayBill> processedAirwayBillList = new ArrayList<LogAirwayBill>();
//					for (LogInvoiceReconRecord record : logInvoiceReconRecordList) {
//						if (!processedAirwayBillList.contains(record
//								.getLogAirwayBill())) {
//							invoiceTotal = invoiceTotal.add(record
//									.getLogAirwayBill().getTotalCharge());
//							processedAirwayBillList.add(record
//									.getLogAirwayBill());
//						}
//					}
//					invoice.setInvoiceAmount(invoiceTotal);
//					invoice.setInvoiceDate(existingLogInvoiceReportUpload
//							.getReportTimestamp());
//
//					/*
//					 * Not in requirements but make the due date of the invoice
//					 * 14 days from reconciliation
//					 */
//					Calendar invoiceDueDate = Calendar.getInstance();
//					invoiceDueDate.setTime(invoice.getInvoiceDate());
//					invoiceDueDate.add(14, Calendar.DAY_OF_YEAR);
//					invoice.setInvoiceDueDate(invoiceDueDate.getTime());
//					/*
//					 * Set the tax amount for invoice to zero
//					 * because theree is no requiremnet for tax 
//					 * accounting on invoices.
//					 */
//					invoice.setInvoiceTaxAmount(new BigDecimal(0)); 
//					invoice.setInvoiceTimestamp(new Timestamp(System
//							.currentTimeMillis()));
//					invoice.setLogInvoiceReportUploads(logInvoiceReportUploadList);
//					invoice.setVenParty(logInvoiceReportUpload
//							.getLogLogisticsProvider().getVenParty());
//
//					// Persist the invoice
//					invoice = finApInvoiceHome.persistFinApInvoice(invoice);
//
//					// Merge the invoice ID into the report.
//					existingLogInvoiceReportUpload.setFinApInvoice(invoice);
//					logInvoiceReportUploadHome
//							.mergeLogInvoiceReportUpload(existingLogInvoiceReportUpload);
//
//					// Create the logistics debt acknowledgement journals
//					return financeJournalPosterHome
//							.postLogisticsDebtAcknowledgementJournalTransaction(invoice
//									.getApInvoiceId());
//				}
//				return true;
//			}
//		} catch (Exception e) {
//			String errMsg = "An exception occured when processin the callback for FinSalesRecordSessionEJBCallback:"
//					+ e.getMessage();
//			_log.error(errMsg);
//			e.printStackTrace();
//			return Boolean.FALSE;
//		}  finally{
//		try{
//			if(locator!=null){
//				locator.close();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
		return Boolean.TRUE;
	}

	@SuppressWarnings("unused")
	@Override
	public Boolean onPostMerge(Object businessObject) {
		LogInvoiceReportUpload logInvoiceReportUpload = (LogInvoiceReportUpload) businessObject;
		_log.debug("onPostMerge()");
		return Boolean.TRUE;
	}

	@SuppressWarnings("unused")
	@Override
	public Boolean onPreRemove(Object businessObject) {
		LogInvoiceReportUpload logInvoiceReportUpload = (LogInvoiceReportUpload) businessObject;
		_log.debug("onPreRemove()");
		return Boolean.TRUE;
	}

	@SuppressWarnings("unused")
	@Override
	public Boolean onPostRemove(Object businessObject) {
		LogInvoiceReportUpload logInvoiceReportUpload = (LogInvoiceReportUpload) businessObject;
		_log.debug("onPostRemove()");
		return Boolean.TRUE;
	}
}
