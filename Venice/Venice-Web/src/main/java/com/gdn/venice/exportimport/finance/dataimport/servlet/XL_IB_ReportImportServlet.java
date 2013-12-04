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
import com.gdn.venice.exportimport.finance.dataimport.XL_IB_Record;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
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
 * Servlet class for importing BCA credit card transaction reports (MIGS).
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class XL_IB_ReportImportServlet extends HttpServlet {
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
	public XL_IB_ReportImportServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.XL_IB_ReportImportServlet");
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.debug("XL_IB_ReportImportServlet:hello");

		notificationText = FinanceImportServletConstants.JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT;

		if (isMultipart) { // import
			String filePath = System.getenv("VENICE_HOME") + "/files/import/finance/";

			// Upload the file
			String fileNameAndPath = FinanceImportServletHelper.upload(request, filePath, "-XL_IB_Record.xls");
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
				x = new ExcelToPojo(XL_IB_Record.class, System.getenv("VENICE_HOME") + "/files/template/" + "XL_IB_Record.xml", fileNameAndPath,3, 1);
				x = x.getPojoToExcel(14,"ref id","GrandTotal");
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
				if (!FinanceImportServletHelper.isFileUnique(uniqueId, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB)) {
					String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_FILE_NOT_UNIQUE;
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
					response.getOutputStream().println(notificationText);
					return;
				}

				locator = new Locator<Object>();

				FinArFundsInReconRecordSessionEJBRemote reconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
						.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");

				// Fix this later once we have log4j sorted out - will need to
				// migrate servlet to Venice client project or pass user logon
				// name as a parameter
				// RafUserSessionEJBRemote rafUserHome =
				// (RafUserSessionEJBRemote) locator
				// .lookup(RafUserSessionEJBRemote.class,
				// "RafUserSessionEJBBean");

				// Get the user name from the logon state and look up the user
				// from the database
				// String loginName = request.getUserPrincipal().getName();
				// List<RafUser> rafUserList =
				// rafUserHome.queryByRange("select o from RafUser o where o.loginName ='"
				// + loginName + "'" , 0, 1);
				// if(rafUserList == null || rafUserList.isEmpty()){
				// String errMsg =
				// "User not found - please ensure that the user exists in the Venice database";
				// _log.error(errMsg);
				// }
				// RafUser rafUser = rafUserList.get(0);

				/*
				 * Create the record for the report upload o note that report
				 * records can be uploaded once and once only
				 */
				finArFundsInReport = FinanceImportServletHelper
						.createFundsInReportRecord(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB, fileNameAndPath.substring(fileNameAndPath.lastIndexOf('/') + 1), filePath, uniqueId);
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
				ArrayList<FinArFundsInReconRecord> newFinArFundsInReconRecord = new ArrayList<FinArFundsInReconRecord>();
				for (PojoInterface element : result) {
					XL_IB_Record xlIbcRecord = (XL_IB_Record) element;
					xlIbcRecord.setTransAmount(new BigDecimal(xlIbcRecord.getTransAmount()).add(new BigDecimal(3000))+"");
					VenOrder venOrder = FinanceImportServletHelper.getPaymentRelatedOrder(xlIbcRecord.getReferenceId(), new Double(xlIbcRecord.getTransAmount()),VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB);
					
					/*
					 * If there is no corresponding order, then it is payment not recognized
					 */
//					if (venOrder == null) {
//						String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_NO_CORRESPONDING_PAYMENT + bcaCcRecord.getAuthCd();
//						if(!notificationText.contains("REPLACE")){
//							notificationText = notificationText.replaceFirst("VENICE:", "VENICE:" + bcaCcRecord.getAuthCd() + " ,");
//						} else {
//							notificationText = notificationText.replaceFirst("REPLACE", errMsg);
//						}
//						continue;
//					}

					/*
					 * If a matching recon record can be found then use it else
					 * create a new one.
					 * 
					 * Note that there should be a reconciliation record for
					 * every payment created at the time the VEN_Order_Payment
					 * is created but there may be payments coming in with no
					 * existing record (no control over this).
					 */
					FinArFundsInReconRecord reconRecord = null;
					if(venOrder!=null){
						 reconRecord = FinanceImportServletHelper
							.getReconciliationRecord(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB, xlIbcRecord.getReferenceId(), new BigDecimal(xlIbcRecord.getTransAmount()).abs());
					}
					if (reconRecord == null) {
						reconRecord = new FinArFundsInReconRecord();
					}
					if(FinanceImportServletHelper.cekContinueProsesFundIn(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB, xlIbcRecord.getReferenceId(),"", new BigDecimal(xlIbcRecord.getTransAmount()).abs())){
						
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
						reconRecord.setRemainingBalanceAmount(FinanceImportServletHelper.getRemainingBalanceAfterPayment(em, venOrder, Math.abs(new Double(xlIbcRecord.getTransAmount())),reconRecord));
					}else{
						//if payment not recognized (no coresponding order), set the remaining balance to the paid amount
						_log.debug("Payment Not recognized");
						reconRecord.setRemainingBalanceAmount(new BigDecimal(xlIbcRecord.getTransAmount()).abs().negate());
						reconRecord.setNomorReff(xlIbcRecord.getReferenceId());
					}
					
					reconRecord.setPaymentConfirmationNumber(xlIbcRecord.getReferenceId());
					reconRecord.setFinArFundsInReport(finArFundsInReport);
					BigDecimal paidAmount = reconRecord.getProviderReportPaidAmount()!=null?reconRecord.getProviderReportPaidAmount(): new BigDecimal(0);
					reconRecord.setProviderReportFeeAmount(new BigDecimal(3000));
					reconRecord.setProviderReportPaidAmount(paidAmount.add(new BigDecimal(xlIbcRecord.getTransAmount())).abs());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					Date dt =sdf.parse(xlIbcRecord.getDate());
					reconRecord.setProviderReportPaymentDate(new java.sql.Timestamp(dt.getTime()));
//					reconRecord.setProviderReportPaymentDate(sdf.parse(uniqueId.replaceAll("\\.", "")));
					reconRecord.setProviderReportPaymentId(xlIbcRecord.getReferenceId());					
					reconRecord.setReconcilliationRecordTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));					

					VenOrderPayment venOrderPayment = FinanceImportServletHelper.getRelatedPaymentRecord(xlIbcRecord.getReferenceId(),Math.abs(new Double(xlIbcRecord.getTransAmount())), VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB);

					/*
					 * Check if the order is recognized in venice database, it must have payment record.
					 */
					if(venOrder!=null && venOrderPayment == null){
						String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_NOT_FOUND + xlIbcRecord.getReferenceId();
						_log.error(errMsg);
						if(!notificationText.contains("REPLACE")){
							notificationText = notificationText.replaceFirst("VENICE:", "VENICE:" + xlIbcRecord.getReferenceId() + " ,");
						}else{
							notificationText = notificationText.replaceFirst("REPLACE", errMsg);
						}
						continue;
					}

					// Check the balance due and set the reconciliation status
					// accordingly
					FinArReconResult finArReconResult = new FinArReconResult();
					/*if (reconRecord.getRemainingBalanceAmount().compareTo(new BigDecimal(0)) == 0) {
						finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_ALL);
					} else if (reconRecord.getRemainingBalanceAmount().compareTo(new BigDecimal(0)) > 0) {
						finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_PARTIAL);
					} else {
						finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_OVERPAID);
					}*/		
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
//					if(reconRecord.getReconciliationRecordId() == null){
//						reconRecord = reconRecordHome.persistFinArFundsInReconRecord(reconRecord);
//					}else{
//						reconRecord = reconRecordHome.mergeFinArFundsInReconRecord(reconRecord);
//					}
					_log.debug("Reconciliation record persisted id:" + reconRecord.getReconciliationRecordId());
				}else{
					_log.debug("continue proses karena report payment sudah pernah di proses sebelumnya");
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
				String successMsg = "Report uploaded successfully... please refresh";
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
