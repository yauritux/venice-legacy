package com.gdn.venice.exportimport.finance.dataimport.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.exportimport.finance.dataimport.Mandiri_Installment_Record;
import com.gdn.venice.hssf.ExcelToPojo;
import com.gdn.venice.hssf.PojoInterface;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInReport;
import com.gdn.venice.persistence.FinArReconResult;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.util.VeniceConstants;
/**

 * <p>
 * <b>author:</b>Arifin
 * <p>
 * <b>version:</b> 2.0
 * <p>
 * <b>since:</b> 2012
 * 
 */
/**
 * Servlet implementation class Mandiri_Installment_ReportImportServlet
 */
public class Mandiri_Installment_ReportImportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/*
	 * The row number in the import file if an error occurs
	 */
	private Integer errorRowNumber = 1;
	
	protected static Logger _log = null;
	

	@PersistenceContext(unitName = "GDN-Venice-Persistence", type = PersistenceContextType.TRANSACTION)
	protected EntityManager em;

	private String notificationText = "";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Mandiri_Installment_ReportImportServlet() {
        super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.Mandiri_Installment_ReportImportServlet");
    }

    /**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.debug("Mandiri_Installment_ReportImportServlet:hello");

		notificationText = FinanceImportServletConstants.JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT;

		if (isMultipart) { // import
			String filePath = System.getenv("VENICE_HOME") + "/files/import/finance/";

			// Upload the file
			String fileNameAndPath = FinanceImportServletHelper.upload(request, filePath, "-Mandiri_Installment_Record.xls");
			if (fileNameAndPath == null) {
				String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_UPLOAD_EXCEPTION;
				_log.info(errMsg);
				notificationText = notificationText.replaceFirst("REPLACE", errMsg);
				response.getOutputStream().println(notificationText);
				return;
			}
			
			ExcelToPojo x = null;

			try {
				// Read the file into an ArrayList of POJO's
				x = new ExcelToPojo(Mandiri_Installment_Record.class, System.getenv("VENICE_HOME") + "/files/template/" + "Mandiri_Installment_Record.xml", fileNameAndPath,9, 0);
				x = x.getPojoToExcel(19,"MID","TOTAL");
			} catch (Exception e1) {
				String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_REPORT_UPLOAD_EXCEPTION  + e1.getMessage() + ". Processing row number:" + (x != null && x.getErrorRowNumber() != null?x.getErrorRowNumber():"1");
				_log.error(errMsg);
				e1.printStackTrace();
				notificationText = notificationText.replaceFirst("REPLACE", errMsg.replace("\"", " "));
				response.getOutputStream().println(notificationText);
			}

			ArrayList<PojoInterface> result = x.getPojoResult();
			Locator<Object> locator = null;
			FinArFundsInReport finArFundsInReport=null;
			try {						
				if(result.isEmpty()){
					String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS;
					_log.info(errMsg);
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
					response.getOutputStream().println(notificationText);
					return;	
				}
				/*
				 * Get the unique identifier from the transaction date 
				 * of the first record in the report
				 */
				String uniqueId = new Timestamp(System.currentTimeMillis()).toString().replace(".", "");			
				if (!FinanceImportServletHelper.isFileUnique(uniqueId, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC)) {
					String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_FILE_NOT_UNIQUE;
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
					response.getOutputStream().println(notificationText);
					return;
				}

				locator = new Locator<Object>();

				FinArFundsInReconRecordSessionEJBRemote reconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
						.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
				/*
				 * Create the record for the report upload o note that report
				 * records can be uploaded once and once only
				 */
				finArFundsInReport = FinanceImportServletHelper
						.createFundsInReportRecord(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC, fileNameAndPath.substring(fileNameAndPath.lastIndexOf('/') + 1), filePath, uniqueId);
				if (finArFundsInReport == null) {
					String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_FUNDS_IN_REPORT_CREATION_EXCEPTION;
					_log.info(errMsg);
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
					response.getOutputStream().println(notificationText);
					return;
				}

				/*
				 * Process the records from the report
				 */
				errorRowNumber = 1;
				int newRowNumber=0,rejected=0;
				ArrayList<FinArFundsInReconRecord> newFinArFundsInReconRecord = new ArrayList<FinArFundsInReconRecord>();
				for (PojoInterface element : result) {
					Mandiri_Installment_Record mandiriInstRecord = (Mandiri_Installment_Record) element;

					SimpleDateFormat dtts = new SimpleDateFormat("dd-MMM-yyyy");					
					Date dts = dtts.parse(mandiriInstRecord.getTrxDate());

					String authCode = mandiriInstRecord.getAuthCode().replaceAll("\'", "");
					
					VenOrder venOrder = FinanceImportServletHelper.getPaymentRelatedOrder(authCode, new Double(mandiriInstRecord.getAmount()),VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC);
					FinArFundsInReconRecord reconRecord = null;
					if(venOrder!=null){
						 reconRecord = FinanceImportServletHelper.getReconciliationRecord(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC, authCode,  new BigDecimal(mandiriInstRecord.getAmount()).abs());
					}
					if (reconRecord == null) {
						reconRecord = new FinArFundsInReconRecord();
					}
					reconRecord.setProviderReportPaymentDate(new java.sql.Timestamp(dts.getTime()));
					if(FinanceImportServletHelper.cekContinueProsesFundIn(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC,authCode,reconRecord.getProviderReportPaymentDate()+"", new BigDecimal(mandiriInstRecord.getAmount()).abs())){
						
					FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
					finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
					reconRecord.setFinApprovalStatus(finApprovalStatus);

					FinArFundsInActionApplied finArFundsInActionApplied = new FinArFundsInActionApplied();
					finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
					reconRecord.setFinArFundsInActionApplied(finArFundsInActionApplied);

					if(venOrder!=null &&  reconRecord.getVenOrderPayment() != null){
						_log.debug("Order : "+venOrder.getWcsOrderId());
						reconRecord.setOrderDate(venOrder.getOrderDate());
						reconRecord.setWcsOrderId(venOrder.getWcsOrderId());						
						//benambahan parameter reconRecord untuk mengetahui payment yang akan di reconcile
						reconRecord.setRemainingBalanceAmount(FinanceImportServletHelper.getRemainingBalanceAfterPayment(em, venOrder, Math.abs(new Double(mandiriInstRecord.getAmount())),reconRecord));
					}else{
						//if payment not recognized (no coresponding order), set the remaining balance to the paid amount
						_log.debug("Payment Not recognized");
						reconRecord.setRemainingBalanceAmount(new BigDecimal(mandiriInstRecord.getAmount()).abs().negate());
						reconRecord.setNomorReff(authCode);
					}
					
					reconRecord.setPaymentConfirmationNumber(authCode);
					reconRecord.setFinArFundsInReport(finArFundsInReport);				
					BigDecimal paidAmount = reconRecord.getProviderReportPaidAmount()!=null?reconRecord.getProviderReportPaidAmount(): new BigDecimal(0);
									
					_log.debug("MID "+mandiriInstRecord.getmId().toUpperCase());
					if(mandiriInstRecord.getmId().toUpperCase().equals(VeniceConstants.BLIBLI_PB3)){		
						reconRecord.setProviderReportFeeAmount(new BigDecimal(mandiriInstRecord.getAmount()).multiply(new BigDecimal("0.03")));
						_log.debug("Bank Fee 3 % "+reconRecord.getProviderReportFeeAmount());
					}else if(mandiriInstRecord.getmId().toUpperCase().equals(VeniceConstants.BLIBLI_PB6)){
						reconRecord.setProviderReportFeeAmount(new BigDecimal(mandiriInstRecord.getAmount()).multiply(new BigDecimal("0.045")));
						_log.debug("Bank Fee 4,5 % "+reconRecord.getProviderReportFeeAmount());
					}else if(mandiriInstRecord.getmId().toUpperCase().equals(VeniceConstants.BLIBLI_PB12)){
						reconRecord.setProviderReportFeeAmount(new BigDecimal(mandiriInstRecord.getAmount()).multiply(new BigDecimal("0.06")));
						_log.debug("Bank Fee 6 % "+reconRecord.getProviderReportFeeAmount());
					}else{
						reconRecord.setProviderReportFeeAmount(new BigDecimal(mandiriInstRecord.getAmount()).multiply(new BigDecimal("0.025")));
						_log.debug("Bank Fee 2,5 % "+reconRecord.getProviderReportFeeAmount());
					}
					
					reconRecord.setProviderReportPaidAmount(paidAmount.add(new BigDecimal(mandiriInstRecord.getAmount())).abs());					
					reconRecord.setProviderReportPaymentId(authCode);					
					reconRecord.setReconcilliationRecordTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));					

					VenOrderPayment venOrderPayment = FinanceImportServletHelper.getRelatedPaymentRecord(authCode,Math.abs(new Double(mandiriInstRecord.getAmount())), VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC);

					/*
					 * Check if the order is recognized in venice database, it must have payment record.
					 */
					if(venOrder!=null && venOrderPayment == null){
						String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_NOT_FOUND + authCode;
						_log.error(errMsg);
						if(!notificationText.contains("REPLACE")){
							notificationText = notificationText.replaceFirst("VENICE:", "VENICE:" + authCode + " ,");
						}else{
							notificationText = notificationText.replaceFirst("REPLACE", errMsg);
						}
						continue;
					}

					// Check the balance due and set the reconciliation status
					// accordingly
					FinArReconResult finArReconResult = new FinArReconResult();
					
					BigDecimal remainingBalanceAmoun =reconRecord.getRemainingBalanceAmount().abs().compareTo(VeniceConstants.TRACEHOLD_RECEIVED) <= 0 ? new BigDecimal(0):reconRecord.getRemainingBalanceAmount();
					
					if (remainingBalanceAmoun.compareTo(new BigDecimal(0)) == 0) {
						finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_ALL);
					} else if (remainingBalanceAmoun.compareTo(new BigDecimal(0)) > 0) {
						finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_PARTIAL);
					} else {
						finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_OVERPAID);
					}

					 //payment not recognized
					if(reconRecord.getRemainingBalanceAmount().compareTo(new BigDecimal(0)) < 0 && (reconRecord.getWcsOrderId()==null || reconRecord.getWcsOrderId()=="")){
						finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NOT_RECOGNIZED);
					}
					
					reconRecord.setFinArReconResult(finArReconResult);

					/*
					 * Merge the record if it is existing
					 * this should be the case for most
					 * else persist it
					 */
					newFinArFundsInReconRecord.add(reconRecord);
					newRowNumber++;
					_log.debug("Reconciliation record persisted id:" + reconRecord.getReconciliationRecordId());
				}else{
					_log.debug("continue proses karena report payment sudah pernah di proses sebelumnya");
					rejected++;
				}
					errorRowNumber++;
				}
				for( FinArFundsInReconRecord finArFundsIn : newFinArFundsInReconRecord ){
					if(finArFundsIn.getReconciliationRecordId() == null){
						finArFundsIn = reconRecordHome.persistFinArFundsInReconRecord(finArFundsIn);
					}else{
						finArFundsIn = reconRecordHome.mergeFinArFundsInReconRecord(finArFundsIn);
					}
				}				
				String successMsg = "Report uploaded successfully... please refresh => New :"+newRowNumber+" , rejected : "+rejected;
				notificationText = notificationText.replaceFirst("REPLACE", successMsg);
				response.getOutputStream().println(notificationText);
			} catch (Exception e) {
				if (finArFundsInReport!=null){
					_log.debug("Remove fund in Report");
					FinanceImportServletHelper.DeleteReport(finArFundsInReport);
				}
				String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_REPORT_UPLOAD_EXCEPTION + e.getClass().getName() + ":" + e.getMessage() + ". Processing row number:" + errorRowNumber ;
				_log.error(errMsg);
				e.printStackTrace();
				notificationText = notificationText.replaceFirst("REPLACE",	errMsg.replace("\"", " "));
				response.getOutputStream().println(notificationText);
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

}
