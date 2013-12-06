package com.gdn.venice.exportimport.finance.dataimport.servlet;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.gdn.venice.exportimport.finance.dataimport.BCA_CC_Record;
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
public class BCA_CC_ReportImportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");

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
	public BCA_CC_ReportImportServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.BCA_CC_ReportImportServlet");
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.debug("BCA_CC_ReportImportServlet:hello");

		notificationText = FinanceImportServletConstants.JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT;

		if (isMultipart) { // import
			String filePath = System.getenv("VENICE_HOME") + "/files/import/finance/";

			// Upload the file
			String fileNameAndPath = FinanceImportServletHelper.upload(request, filePath, "-BCA_CC_Report.xls");
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
				x = new ExcelToPojo(BCA_CC_Record.class, System.getenv("VENICE_HOME") + "/files/template/" + "BCA_CC_Record.xml", fileNameAndPath, 0, 0);
				x = x.getPojo();
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
				String uniqueId = ((BCA_CC_Record)result.get(0)).getAuthCd() +" "+((BCA_CC_Record)result.get(0)).getTransDate()+" "+((BCA_CC_Record)result.get(0)).getTransTime();			
				if (!FinanceImportServletHelper.isFileUnique(uniqueId, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC)) {
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
						.createFundsInReportRecord(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC, fileNameAndPath.substring(fileNameAndPath.lastIndexOf('/') + 1), filePath, uniqueId);
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
				ArrayList<BCA_CC_Record> results= new ArrayList<BCA_CC_Record>();
				_log.debug("-----start cek duplikasi row report");
				String cekRefund="",cekVoid="";
				int countRefund=0,countVoid=0;
				for (PojoInterface elements : result) {
					BCA_CC_Record bcaCcRecord = (BCA_CC_Record) elements;
					bcaCcRecord.setAuthCd(bcaCcRecord.getAuthCd().length()<6?"000000".substring(0,6-bcaCcRecord.getAuthCd().length())+""+bcaCcRecord.getAuthCd():bcaCcRecord.getAuthCd());
					BCA_CC_Record tempBcaCcRecords=null;
					boolean cek=true;
					for (BCA_CC_Record elm : results) {
						_log.debug("cek result "+bcaCcRecord.getAuthCd()+"=="+elm.getAuthCd());
						if(bcaCcRecord.getAuthCd().equals(elm.getAuthCd())){
							cek=false;
							break;
						}
						
					}		
					if(cek){
							for (PojoInterface elementC : result) {
								BCA_CC_Record bcaCcRecords = (BCA_CC_Record) elementC;
								bcaCcRecords.setAuthCd(bcaCcRecords.getAuthCd().length()<6?"000000".substring(0,6-bcaCcRecords.getAuthCd().length())+""+bcaCcRecords.getAuthCd():bcaCcRecords.getAuthCd());						
								_log.debug("cek "+bcaCcRecord.getAuthCd()+"=="+bcaCcRecords.getAuthCd());
								if(bcaCcRecord.getAuthCd().equals(bcaCcRecords.getAuthCd())){
									if(tempBcaCcRecords==null){
										tempBcaCcRecords=bcaCcRecords;
									}else{
										tempBcaCcRecords.setNettAmt(new BigDecimal(tempBcaCcRecords.getNettAmt()).add(new BigDecimal(bcaCcRecords.getNettAmt())).toString());
										tempBcaCcRecords.setGrossAmt(new BigDecimal(tempBcaCcRecords.getGrossAmt()).add(new BigDecimal(bcaCcRecords.getGrossAmt())).toString());
										tempBcaCcRecords.setDiscAmt(new BigDecimal(tempBcaCcRecords.getDiscAmt()).add(new BigDecimal(bcaCcRecords.getDiscAmt())).toString());
									}
								}
							}
							if(new BigDecimal(tempBcaCcRecords.getGrossAmt()).compareTo(new BigDecimal("0"))==1 && new BigDecimal(tempBcaCcRecords.getGrossAmt()).compareTo(new BigDecimal("0"))!=0){
								_log.debug("++ BcaCcRecords "+bcaCcRecord.getAuthCd());
								if(!cekRefund.contains(tempBcaCcRecords.getAuthCd()) && !cekVoid.contains(tempBcaCcRecords.getAuthCd())){
									results.add(tempBcaCcRecords);
								}
							}else if(new BigDecimal(tempBcaCcRecords.getGrossAmt()).compareTo(new BigDecimal("0"))<0){
								if(!cekRefund.contains(tempBcaCcRecords.getAuthCd())){
									cekRefund=cekRefund+","+tempBcaCcRecords.getAuthCd();
									countRefund++;
								}
							}else if(new BigDecimal(tempBcaCcRecords.getGrossAmt()).compareTo(new BigDecimal("0"))==0){
								if(!cekVoid.contains(tempBcaCcRecords.getAuthCd())){
									cekVoid=cekVoid+","+tempBcaCcRecords.getAuthCd();
									countVoid++;
								}
							}
					}
										
				}
				ArrayList<FinArFundsInReconRecord> newFinArFundsInReconRecord = new ArrayList<FinArFundsInReconRecord>();
				int newRowNumber=0;
				for (PojoInterface element : results) {
					BCA_CC_Record bcaCcRecord = (BCA_CC_Record) element;
					String authCd = bcaCcRecord.getAuthCd().length() < 6?"000000".substring(0,6-bcaCcRecord.getAuthCd().length())+""+bcaCcRecord.getAuthCd():bcaCcRecord.getAuthCd();
					String times = bcaCcRecord.getTransTime().length()<6?"000000".substring(0,6-bcaCcRecord.getTransTime().length())+""+bcaCcRecord.getTransTime():bcaCcRecord.getTransTime();
					VenOrder venOrder = FinanceImportServletHelper.getPaymentRelatedOrder(authCd, new Double(bcaCcRecord.getGrossAmt()), VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC);
					
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
						.getReconciliationRecord(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC, authCd, new BigDecimal(bcaCcRecord.getGrossAmt()));
					}
					
					if (reconRecord == null) {
						reconRecord = new FinArFundsInReconRecord();
					}
					
					Date tgl=sdf.parse(bcaCcRecord.getTransDate().replaceAll("\\.", "").substring(0, 8)+" "+times);		
					reconRecord.setProviderReportPaymentDate(new java.sql.Timestamp(tgl.getTime()));
					if(FinanceImportServletHelper.cekContinueProsesFundIn(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC, authCd,reconRecord.getProviderReportPaymentDate()+"",new BigDecimal(bcaCcRecord.getGrossAmt()))){
								
							FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
							finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
							reconRecord.setFinApprovalStatus(finApprovalStatus);
		
							FinArFundsInActionApplied finArFundsInActionApplied = new FinArFundsInActionApplied();
							finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
							reconRecord.setFinArFundsInActionApplied(finArFundsInActionApplied);
		
							if(venOrder!=null &&  reconRecord.getVenOrderPayment() != null){
								reconRecord.setOrderDate(venOrder.getOrderDate());
								reconRecord.setWcsOrderId(venOrder.getWcsOrderId());
								/*
								 * pengecekan ada tidaknya billing pada order tersebut
								 */								
									//benambahan parameter reconRecord untuk mengetahui payment yang akan di reconcile
									reconRecord.setRemainingBalanceAmount(FinanceImportServletHelper.getRemainingBalanceAfterPayment(em, venOrder, new Double(bcaCcRecord.getGrossAmt()),reconRecord));
								
							}else{
								//if payment not recognized (no coresponding order), set the remaining balance to the paid amount
								BigDecimal remainingAmount = reconRecord.getRemainingBalanceAmount()!=null?reconRecord.getRemainingBalanceAmount(): new BigDecimal(0);
								reconRecord.setRemainingBalanceAmount(remainingAmount.subtract(new BigDecimal(bcaCcRecord.getGrossAmt())));
								reconRecord.setNomorReff(authCd);
							}
							
							reconRecord.setPaymentConfirmationNumber(authCd);
							reconRecord.setFinArFundsInReport(finArFundsInReport);
							BigDecimal feeAmount = reconRecord.getProviderReportFeeAmount()!=null?reconRecord.getProviderReportFeeAmount(): new BigDecimal(0);
							BigDecimal paidAmount = reconRecord.getProviderReportPaidAmount()!=null?reconRecord.getProviderReportPaidAmount(): new BigDecimal(0);
							reconRecord.setProviderReportFeeAmount(feeAmount.add(new BigDecimal(bcaCcRecord.getDiscAmt())));
							reconRecord.setProviderReportPaidAmount(paidAmount.add(new BigDecimal(bcaCcRecord.getGrossAmt())));		
							reconRecord.setProviderReportPaymentId(authCd);
							reconRecord.setReconcilliationRecordTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
									
							VenOrderPayment venOrderPayment = FinanceImportServletHelper.getRelatedPaymentRecord(authCd,new Double(bcaCcRecord.getGrossAmt()),  VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC);
		
							/*
							 * Check if the order is recognized in venice database, it must have payment record.
							 */
							if(venOrder!=null && venOrderPayment == null){
								String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_NOT_FOUND + authCd;
								_log.error(errMsg);
								if(!notificationText.contains("REPLACE")){
									notificationText = notificationText.replaceFirst("VENICE:", "VENICE:" + authCd + " ,");
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
				
				String successMsg = "Report uploaded successfully... please refresh => New :"+newRowNumber+" => void : "+countVoid+" => Refund : "+countRefund;
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
