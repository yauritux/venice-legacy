package com.gdn.venice.exportimport.finance.dataimport.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import com.gdn.venice.exportimport.finance.dataimport.BCA_VA_FileReader;
import com.gdn.venice.exportimport.finance.dataimport.BCA_VA_IB_Record;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsIdReportTime;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInReport;
import com.gdn.venice.persistence.FinArReconResult;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.util.VeniceConstants;

/**
 * Servlet class for importing BCA virtual account transaction reports.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class BCA_VA_ReportImportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BCA_VA_ReportImportServlet() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.BCA_VA_ReportImportServlet");
    }

	protected static Logger _log = null;
	
	@PersistenceContext(unitName = "GDN-Venice-Persistence", type = PersistenceContextType.TRANSACTION)
	protected EntityManager em;
	
	private String notificationText = "";

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		_log.debug("BCA_VA_ReportImportServlet:hello");
		
		notificationText = FinanceImportServletConstants.JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT;

		if (isMultipart) { // import
			String filePath = System.getenv("VENICE_HOME")+ "/files/import/finance/";
			
			//Upload the file
			String fileNameAndPath = FinanceImportServletHelper.upload(request, filePath, "-BCA_VA_Report.txt");
			
			if(fileNameAndPath == null){
				String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_UPLOAD_EXCEPTION;
				_log.info(errMsg);
				notificationText = notificationText.replaceFirst("REPLACE", errMsg);
				response.getOutputStream().println(notificationText);
				return;
			}

			//Read the file into an ArrayList of POJO's
			BCA_VA_FileReader reader = new BCA_VA_FileReader();			
			ArrayList<BCA_VA_IB_Record> records = null; 
			Locator<Object> locator = null;
			FinArFundsInReport finArFundsInReport =null;
			try {
				
				/*
				 * Get the uniqueId and check to see if there is already a file with this uniqueId
				 */
				String uniqueIds = reader.getUniqueReportIdentifier(fileNameAndPath);
				String[] uniqueIdSplit=uniqueIds.split("&");
				String uniqueId=uniqueIdSplit[0];
				if(!FinanceImportServletHelper.isFileUnique(uniqueId, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA)){
					String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_FILE_NOT_UNIQUE;
					_log.info(errMsg);
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
					response.getOutputStream().println(notificationText);
					return;				
				}
				_log.info("unique "+uniqueIds+" "+uniqueIdSplit[1]);
				if(uniqueIdSplit[1].equalsIgnoreCase("old")){
					_log.info("Start Old Report ");
					records = reader.readFile(fileNameAndPath); 
				}else if(uniqueIdSplit[1].equalsIgnoreCase("new")){
					_log.info("Start New Report ");
					records = reader.readNewFile(fileNameAndPath); 
				}else{
					_log.info("Start New Report 2 ");
					records = reader.readNewFile2(fileNameAndPath); 
				}
				
				
				if(records.size()==0){
					String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS;
					_log.info(errMsg);
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
					response.getOutputStream().println(notificationText);
					return;	
				}
				
				locator = new Locator<Object>();
				
				FinArFundsInReconRecordSessionEJBRemote reconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
						.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");


     			/*
				 * Create the record for the report upload
				 * 		o note that report records can be uploaded once and once only
				 */
				finArFundsInReport = FinanceImportServletHelper.createFundsInReportRecord(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA, fileNameAndPath.substring(fileNameAndPath.lastIndexOf('/')+1), filePath, uniqueId);
				if(finArFundsInReport == null){
					String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_FUNDS_IN_REPORT_CREATION_EXCEPTION;
					_log.info(errMsg);
					notificationText = notificationText.replaceFirst("REPLACE", errMsg);
					response.getOutputStream().println(notificationText);
					return;
				}
				/*ArrayList<BCA_VA_IB_Record> recordItems = new ArrayList<BCA_VA_IB_Record>();			
				String noVA="";
						for (BCA_VA_IB_Record items : records) {
							BCA_VA_IB_Record value = null;				
								if(!noVA.contains(items.getAccountNumber()) || noVA.equals("")){
									for(BCA_VA_IB_Record item : records){
										if(items.getAccountNumber().equals(item.getAccountNumber())){
											 if(value==null){
												 value=item;
											 }else{
												 value.setPaymentAmount(item.getPaymentAmount()+value.getPaymentAmount());
												 value.setBankFee(item.getBankFee()+value.getBankFee());
											 }
											 if (!noVA.contains(items.getAccountNumber()))
											 noVA=noVA+","+items.getAccountNumber();
										}							
									}
									recordItems.add(value);							
								}						
								
						}		*/
				/*
				 * Process the records from the report
				 */
				ArrayList<FinArFundsInReconRecord> newFinArFundsInReconRecord = new ArrayList<FinArFundsInReconRecord>();
				for (BCA_VA_IB_Record element : records) {
					BCA_VA_IB_Record bcaVARecord =  element;
					List<FinArFundsInReconRecord> reconRecordH1=null;
					
					if(uniqueIdSplit[1].equalsIgnoreCase("old")){								
						reconRecordH1 =FinanceImportServletHelper.cekProsesVAFundIn(em, bcaVARecord.getAccountNumber(),dt.format(bcaVARecord.getPaymentDate()));								
					}					
					if(reconRecordH1==null){				
								//Note that accountNumber is mapped to nama
								VenOrder venOrder = FinanceImportServletHelper.getPaymentRelatedOrder(bcaVARecord.getAccountNumber(), bcaVARecord.getPaymentAmount(),VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA);
			
								/*
								 * If a matching recon record can be found then use it else
								 * create a new one.
								 */
								FinArFundsInReconRecord reconRecord =null;					
								if(venOrder!=null ){
										reconRecord = FinanceImportServletHelper
												.getReconciliationRecord(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA, bcaVARecord.getAccountNumber(), new BigDecimal(bcaVARecord.getPaymentAmount()));
								}
								
								if (reconRecord == null) {
										reconRecord = new FinArFundsInReconRecord();
								}
								if(FinanceImportServletHelper.cekContinueProsesFundIn(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA,  bcaVARecord.getAccountNumber(),dt.format(bcaVARecord.getPaymentDate()), new BigDecimal(bcaVARecord.getPaymentAmount()))){
										FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
										finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
										reconRecord.setFinApprovalStatus(finApprovalStatus);
										
										FinArFundsInActionApplied finArFundsInActionApplied = new FinArFundsInActionApplied();
										finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
										reconRecord.setFinArFundsInActionApplied(finArFundsInActionApplied);
										
										reconRecord.setFinArFundsInReport(finArFundsInReport);
										if(venOrder!=null &&  reconRecord.getVenOrderPayment() != null){
											reconRecord.setOrderDate(venOrder.getOrderDate());
											reconRecord.setWcsOrderId(venOrder.getWcsOrderId());
											/*
											 * pengecekan ada tidaknya billing pada order tersebut
											 */						
											//benambahan parameter reconRecord untuk mengetahui payment yang akan di reconcile
											reconRecord.setRemainingBalanceAmount(FinanceImportServletHelper.getRemainingBalanceAfterPayment(em, venOrder, new Double(bcaVARecord.getPaymentAmount()),reconRecord));
										}else{
											//if payment not recognized (no coresponding order), set the remaining balance to the paid amount
											reconRecord.setRemainingBalanceAmount(new BigDecimal(bcaVARecord.getPaymentAmount()).negate());
											reconRecord.setNomorReff(bcaVARecord.getAccountNumber());
										}
										BigDecimal feeAmount = reconRecord.getProviderReportFeeAmount()!=null?reconRecord.getProviderReportFeeAmount(): new BigDecimal(0);
										BigDecimal paidAmount = reconRecord.getProviderReportPaidAmount()!=null?reconRecord.getProviderReportPaidAmount(): new BigDecimal(0);
										reconRecord.setProviderReportFeeAmount(feeAmount.add(new BigDecimal(bcaVARecord.getBankFee())));
										reconRecord.setProviderReportPaidAmount(paidAmount.add(new BigDecimal(bcaVARecord.getPaymentAmount())));
										reconRecord.setProviderReportPaymentDate(new java.sql.Timestamp(bcaVARecord.getPaymentDate().getTime()));
										reconRecord.setProviderReportPaymentId(bcaVARecord.getAccountNumber());		
										
										FinArFundsIdReportTime finArFundsIdReportTime = new FinArFundsIdReportTime();
										if(uniqueIdSplit[1].equalsIgnoreCase("old")){								
											finArFundsIdReportTime.setReportTimeId(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TIME_H1);								
										}else{
											finArFundsIdReportTime.setReportTimeId(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TIME_REAL_TIME);	
										}							
										reconRecord.setFinArFundsIdReportTime(finArFundsIdReportTime);
										
										reconRecord.setUniquePayment(dt.format(bcaVARecord.getPaymentDate()));
										
										reconRecord.setReconcilliationRecordTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
													
										
										VenOrderPayment venOrderPayment = FinanceImportServletHelper.getRelatedPaymentRecord(bcaVARecord.getAccountNumber(), new Double(bcaVARecord.getPaymentAmount()), VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA);
					
										/*
										 * Check if the order is recognized in venice database, it must have payment record.
										 */
										if(venOrder!=null && venOrderPayment == null){
											String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_NOT_FOUND + bcaVARecord.getAccountNumber(); 
											_log.error(errMsg);
											if(!notificationText.contains("REPLACE")){
												notificationText = notificationText.replaceFirst("VENICE:", "VENICE:" + bcaVARecord.getAccountNumber() + " ,");
											}else{
												notificationText = notificationText.replaceFirst("REPLACE", errMsg);
											}
											continue;
										}
										
										//Check the balance due and set the reconciliation status accordingly
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
										if(reconRecord.getReconciliationRecordId() == null){
											reconRecord = reconRecordHome.persistFinArFundsInReconRecord(reconRecord);
										}else{
											reconRecord = reconRecordHome.mergeFinArFundsInReconRecord(reconRecord);
										}										

										newFinArFundsInReconRecord.add(reconRecord);
								}
					}else{
						for(FinArFundsInReconRecord itemReconRecordH1 : reconRecordH1){							
								FinArFundsIdReportTime finArFundsIdReportTime = new FinArFundsIdReportTime();
								finArFundsIdReportTime.setReportTimeId(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TIME_H1);							
								itemReconRecordH1.setFinArFundsIdReportTime(finArFundsIdReportTime);		
								newFinArFundsInReconRecord.add(itemReconRecordH1);		
								itemReconRecordH1 = reconRecordHome.mergeFinArFundsInReconRecord(itemReconRecordH1);
						}
					}		
				}
				/*for( FinArFundsInReconRecord finArFundsIn : newFinArFundsInReconRecord ){
					if(finArFundsIn.getReconciliationRecordId() == null){
						finArFundsIn = reconRecordHome.persistFinArFundsInReconRecord(finArFundsIn);
					}else{
						finArFundsIn = reconRecordHome.mergeFinArFundsInReconRecord(finArFundsIn);
					}
				}*/
				String successMsg = "Report uploaded successfully... please refresh";
				notificationText = notificationText.replaceFirst("REPLACE", successMsg);
				response.getOutputStream().println(notificationText);
			} catch (Exception e) {
				if (finArFundsInReport!=null){
					_log.debug("Remove fund in Report");
					FinanceImportServletHelper.DeleteReport(finArFundsInReport);
				}
				String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_REPORT_UPLOAD_EXCEPTION;
				_log.error(errMsg + e.getMessage());			
				e.printStackTrace();
				notificationText = notificationText.replaceFirst("REPLACE", errMsg);
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
