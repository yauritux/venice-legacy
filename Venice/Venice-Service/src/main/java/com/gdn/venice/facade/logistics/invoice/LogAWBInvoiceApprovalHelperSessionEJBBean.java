package com.gdn.venice.facade.logistics.invoice;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinApInvoiceSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBLocal;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBLocal;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBLocal;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.util.VeniceConstants;

/**
 * LogAWBInvoiceApprovalHelperSessionEJBBean.java
 * 
 * Session Bean implementation class LogAWBInvoiceApprovalHelperSessionEJBBean
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
@Stateless(mappedName = "LogAWBInvoiceApprovalHelperSessionEJBBean")
public class LogAWBInvoiceApprovalHelperSessionEJBBean implements LogAWBInvoiceApprovalHelperSessionEJBRemote, LogAWBInvoiceApprovalHelperSessionEJBLocal{
	protected static Logger _log = null;
	protected static final String AIRWAYBILL_ENGINE_PROPERTIES_FILE = System.getenv("VENICE_HOME") +  "/conf/airwaybill-engine.properties";
	
    /**
     * Default constructor. 
     */
    public LogAWBInvoiceApprovalHelperSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.logistics.invoice.LogAWBInvoiceApprovalHelperSessionEJBBean");
    }
    
    private  Properties getAirwayBillEngineProperties() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(AIRWAYBILL_ENGINE_PROPERTIES_FILE));
		} catch (Exception e) {
			_log.error("Error getting airwaybill-engine.properties", e);
			e.printStackTrace();
			return null;
		}
		return properties;
	}
    
	/**
	 * Applies the invoice reconciliation data to the airway bill.
	 * Should be called when the AWB reconciliation has been approved.
	 * @param logAirwayBill
	 * @return
	 */
	public LogInvoiceReportUpload applyInvoiceReconciliationData(LogInvoiceReportUpload logInvoiceReportUpload){
		Locator<Object> locator = null;
//		NameValuePair formparams[] = null;
//		HttpClient client = null;
//		PostMethod method = null;
//		Credentials credentials = null;
//		int statusCode;
		AirwayBillEngineClientConnector awbConnector = null;
		
		try {
			locator = new Locator<Object>();
			awbConnector = new AirwayBillEngineClientConnector();
//			client = new HttpClient();
//			method = new PostMethod(getAirwayBillEngineProperties().getProperty("address")+"awbt/sec/approve/");
//			credentials = new UsernamePasswordCredentials(getAirwayBillEngineProperties().getProperty("username"), getAirwayBillEngineProperties().getProperty("password"));
//			client.getState().setCredentials(AuthScope.ANY, credentials);
			
			LogInvoiceReconRecordSessionEJBLocal logInvoiceReconRecordHome = (LogInvoiceReconRecordSessionEJBLocal) locator
			.lookupLocal(LogInvoiceReconRecordSessionEJBLocal.class, "LogInvoiceReconRecordSessionEJBBeanLocal");
			
			LogAirwayBillSessionEJBLocal airwayBillHome = (LogAirwayBillSessionEJBLocal) locator
			.lookupLocal(LogAirwayBillSessionEJBLocal.class, "LogAirwayBillSessionEJBBeanLocal");
			
			LogInvoiceAirwaybillRecordSessionEJBLocal awbRecordHome = (LogInvoiceAirwaybillRecordSessionEJBLocal) locator
			.lookupLocal(LogInvoiceAirwaybillRecordSessionEJBLocal.class, "LogInvoiceAirwaybillRecordSessionEJBBeanLocal");
			
			List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList = awbRecordHome.queryByRange("select o from LogInvoiceAirwaybillRecord o " +
					"where o.logInvoiceReportUpload.invoiceReportUploadId = " + logInvoiceReportUpload.getInvoiceReportUploadId(), 0, 0);

			String veniceData, providerData, manualData;
			LogApprovalStatus approvalStatus = new LogApprovalStatus();
			approvalStatus.setApprovalStatusId(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED);
			
			for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : logInvoiceAirwaybillRecordList) {
//				formparams = new NameValuePair[]{new NameValuePair("awb", logInvoiceAirwaybillRecord.getAirwayBillNumber()),
//						new NameValuePair("lp", logInvoiceReportUpload.getLogLogisticsProvider().getLogisticsProviderCode())};
//				method.setRequestBody(formparams);
//				
//				statusCode = client.executeMethod(method);
//				if (statusCode != HttpStatus.SC_OK) {
//					_log.error("Approve airwaybill " + logInvoiceAirwaybillRecord.getAirwayBillNumber() + " failed: " + method.getStatusLine());
//			    } else {
				if(!awbConnector.approveInvoice(logInvoiceAirwaybillRecord.getAirwayBillNumber(), logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getLogLogisticsProvider().getLogisticsProviderCode())){
					_log.error("Approve airwaybill " + logInvoiceAirwaybillRecord.getAirwayBillNumber() + " failed");
				} else {
					List<LogInvoiceReconRecord> logInvoiceReconRecordList = logInvoiceReconRecordHome.queryByRange("select o from LogInvoiceReconRecord o " +
							"where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId = " + logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId(), 0, 0);
	
					for (LogInvoiceReconRecord reconRecord : logInvoiceReconRecordList) {
						veniceData = reconRecord.getVeniceData();
						providerData = reconRecord.getProviderData();
						manualData = reconRecord.getManuallyEnteredData();
						if (reconRecord.getLogActionApplied().getActionAppliedId() == VeniceConstants.LOG_ACTION_APPLIED_VENICE) {
							switch (reconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().intValue()) {
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_WEIGHT_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedPackageWeight(new BigDecimal(veniceData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_PRICE_PER_KG_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedPricePerKg(new BigDecimal(veniceData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_INSURANCE_COST_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedInsuranceCharge(new BigDecimal(veniceData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_BIAYA_KAYU_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedOtherCharge(new BigDecimal(veniceData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_GIFT_WRAP_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedGiftWrapCharge(new BigDecimal(veniceData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_TOTAL_CHARGE_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedTotalCharge(new BigDecimal(veniceData));
									break;
								default:
									break;
							}			
						} else if (reconRecord.getLogActionApplied().getActionAppliedId() == VeniceConstants.LOG_ACTION_APPLIED_PROVIDER) {
							switch (reconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().intValue()) {
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_WEIGHT_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedPackageWeight(new BigDecimal(providerData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_PRICE_PER_KG_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedPricePerKg(new BigDecimal(providerData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_INSURANCE_COST_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedInsuranceCharge(new BigDecimal(providerData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_BIAYA_KAYU_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedOtherCharge(new BigDecimal(providerData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_GIFT_WRAP_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedGiftWrapCharge(new BigDecimal(providerData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_TOTAL_CHARGE_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedTotalCharge(new BigDecimal(providerData));
									break;
								default:
									break;
							}			
						} else if (reconRecord.getLogActionApplied().getActionAppliedId() == VeniceConstants.LOG_ACTION_APPLIED_MANUAL) {
							switch (reconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().intValue()) {
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_WEIGHT_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedPackageWeight(new BigDecimal(manualData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_PRICE_PER_KG_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedPricePerKg(new BigDecimal(manualData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_INSURANCE_COST_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedInsuranceCharge(new BigDecimal(manualData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_BIAYA_KAYU_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedOtherCharge(new BigDecimal(manualData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_GIFT_WRAP_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedGiftWrapCharge(new BigDecimal(manualData));
									break;
								case VeniceConstants.LOG_INVOICE_RECON_RESULT_TOTAL_CHARGE_MISMATCH:
									logInvoiceAirwaybillRecord.setApprovedTotalCharge(new BigDecimal(manualData));
									break;
								default:
									break;
							}
						}
					}	
					List<LogAirwayBill> airwayBillList = airwayBillHome.queryByRange("select o from LogAirwayBill o " +
							"where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId = " + logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId(), 0, 0);
					for (LogAirwayBill logAirwayBill : airwayBillList) {
						logAirwayBill.setLogApprovalStatus1(approvalStatus);
						if(logInvoiceAirwaybillRecord.getVenicePackageWeight().compareTo(BigDecimal.ZERO) == 0)
							logAirwayBill.setPackageWeight(BigDecimal.ZERO);
						else
						logAirwayBill.setPackageWeight((logAirwayBill.getVenOrderItem().getShippingWeight()).divide(logInvoiceAirwaybillRecord.getVenicePackageWeight(), 2, RoundingMode.HALF_UP).multiply(logInvoiceAirwaybillRecord.getApprovedPackageWeight()));
						
						logAirwayBill.setPricePerKg(logInvoiceAirwaybillRecord.getApprovedPricePerKg());
						
						if(logInvoiceAirwaybillRecord.getVeniceInsuranceCharge().compareTo(BigDecimal.ZERO) == 0)
							logAirwayBill.setInsuranceCharge(BigDecimal.ZERO);
						else
							logAirwayBill.setInsuranceCharge((logAirwayBill.getVenOrderItem().getInsuranceCost()).divide(logInvoiceAirwaybillRecord.getVeniceInsuranceCharge(), 2, RoundingMode.HALF_UP).multiply(logInvoiceAirwaybillRecord.getApprovedInsuranceCharge()));
						
						if(logInvoiceAirwaybillRecord.getVeniceOtherCharge().compareTo(BigDecimal.ZERO) == 0)
							logAirwayBill.setOtherCharge(BigDecimal.ZERO);
						else
							logAirwayBill.setOtherCharge((logAirwayBill.getVenOrderItem().getShippingCost().subtract(logAirwayBill.getVenOrderItem().getShippingWeight().multiply(logAirwayBill.getVenOrderItem().getLogisticsPricePerKg()))).divide(logInvoiceAirwaybillRecord.getVeniceOtherCharge(), 2, RoundingMode.HALF_UP).multiply(logInvoiceAirwaybillRecord.getApprovedOtherCharge()));
						
						if(logInvoiceAirwaybillRecord.getVeniceGiftWrapCharge().compareTo(BigDecimal.ZERO) == 0)
							logAirwayBill.setGiftWrapCharge(BigDecimal.ZERO);
						else
							logAirwayBill.setGiftWrapCharge((logAirwayBill.getVenOrderItem().getGiftWrapPrice()).divide(logInvoiceAirwaybillRecord.getVeniceGiftWrapCharge(), 2, RoundingMode.HALF_UP).multiply(logInvoiceAirwaybillRecord.getApprovedGiftWrapCharge()));
						
						if(logInvoiceAirwaybillRecord.getVeniceTotalCharge().compareTo(BigDecimal.ZERO) == 0)
							logAirwayBill.setTotalCharge(BigDecimal.ZERO);
						else
							logAirwayBill.setTotalCharge((logAirwayBill.getVenOrderItem().getShippingCost().add(logAirwayBill.getVenOrderItem().getInsuranceCost())).divide(logInvoiceAirwaybillRecord.getVeniceTotalCharge(), 2, RoundingMode.HALF_UP).multiply(logInvoiceAirwaybillRecord.getApprovedTotalCharge()));
					}
					airwayBillHome.mergeLogAirwayBillList(airwayBillList);
					awbRecordHome.mergeLogInvoiceAirwaybillRecord(logInvoiceAirwaybillRecord);
			    }
			}			
		} catch (Exception e) {
			String errMsg = "An exception occured while assigning reconciled values to the airway bill:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
//				method.releaseConnection();
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
		return logInvoiceReportUpload;
	}

	/**
	 * Created a logistics finance invoice based on the report name that has been reconciled.
	 * @param fileName
	 * @return
	 */
	public Boolean createLogisticsFinanceInvoice(LogInvoiceReportUpload logInvoiceReportUpload){
		
		/*
		 * Get the existing logInvoiceReportUpload record and determine if this
		 * is the state transition to RECONCILED. 
		 * 
		 * If it is then post then create the logistics debt 
		 * acknowledgement journal and the invoice record for
		 * finance.
		 */
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			LogInvoiceReportUploadSessionEJBRemote logInvoiceReportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
				.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");

			FinApInvoiceSessionEJBRemote finApInvoiceHome = (FinApInvoiceSessionEJBRemote) locator
				.lookup(FinApInvoiceSessionEJBRemote.class, "FinApInvoiceSessionEJBBean");

			FinanceJournalPosterSessionEJBRemote financeJournalPosterHome = (FinanceJournalPosterSessionEJBRemote) locator
				.lookup(FinanceJournalPosterSessionEJBRemote.class, "FinanceJournalPosterSessionEJBBean");
			
			LogAirwayBillSessionEJBRemote logAirwayBillHome = (LogAirwayBillSessionEJBRemote) locator
				.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

			/*
			 * If the status of the invoice report is fully reconciled for the
			 * first time then create the invoice and create the debt
			 * acknowledgement journal entries
			 */
			if (logInvoiceReportUpload.getFinApInvoice() == null) {

				/*
				 * Only create the invoice and the LDA journals once
				 * if somehow it gets reconciled a second time from 
				 * database twiddling etc. then the twiddler will have
				 * to also twiddle the LDA and invoice records.
				 */
				
				/*
				 * Sum the AWB amounts
				 */
				List<LogAirwayBill> logAirwayBillList = logAirwayBillHome.queryByRange("select o from LogAirwayBill o where " +
						"o.logInvoiceAirwaybillRecord.logInvoiceReportUpload.invoiceReportUploadId = " + logInvoiceReportUpload.getInvoiceReportUploadId(), 0, 0);

				BigDecimal invoiceTotal = new BigDecimal(0);
				
				FinApInvoice invoice = new FinApInvoice();
				invoice.setInvoiceDate(logInvoiceReportUpload.getReportTimestamp());
				invoice.setInvoiceAmount(invoiceTotal);
				/*
				 * Not in requirements but make the due date of the invoice
				 * 14 days from reconciliation
				 */
				Calendar invoiceDueDate = Calendar.getInstance();
				invoiceDueDate.setTime(invoice.getInvoiceDate());
				invoiceDueDate.add(14, Calendar.DAY_OF_YEAR);
				invoice.setInvoiceDueDate(invoiceDueDate.getTime());
				
				/*
				 * Set the tax amount for invoice to zero
				 * because there is no requiremnet for tax 
				 * accounting on invoices.
				 */
				invoice.setInvoiceTaxAmount(new BigDecimal(0)); 
				invoice.setInvoiceTimestamp(new Timestamp(System.currentTimeMillis()));
//				invoice.setLogInvoiceReportUploads(logInvoiceReportUploadList);
				invoice.setVenParty(logInvoiceReportUpload.getLogLogisticsProvider().getVenParty());

				// Persist the invoice
				invoice = finApInvoiceHome.persistFinApInvoice(invoice);

				// Merge the invoice ID into the report. 
				//in this design only 1 record is referred to the report, because  1 record in log_invoice_report_upload can only refer to 1 fin_ ap_invoice record 
				logInvoiceReportUpload.setFinApInvoice(invoice);
				logInvoiceReportUpload = logInvoiceReportUploadHome.mergeLogInvoiceReportUpload(logInvoiceReportUpload);
				
				for (LogAirwayBill logAirwayBill : logAirwayBillList) {
					/*
					 * Note that the invoice will only include approved airway bills
					 */
					if(logAirwayBill.getLogApprovalStatus1() != null && logAirwayBill.getLogApprovalStatus1().getApprovalStatusId() != null 
							&& logAirwayBill.getLogApprovalStatus1().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_APPROVED){

						invoiceTotal = invoiceTotal.add(logAirwayBill.getTotalCharge());

						// Create the logistics debt acknowledgement journals
						financeJournalPosterHome.postLogisticsDebtAcknowledgementJournalTransaction(invoice, logAirwayBill);
					}
				}			

				invoice.setInvoiceAmount(invoiceTotal);
				finApInvoiceHome.mergeFinApInvoice(invoice);
			}
			return true;
		} catch (Exception e) {
			String errMsg = "An exception occured when creating a finance invoice:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			return Boolean.FALSE;
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
