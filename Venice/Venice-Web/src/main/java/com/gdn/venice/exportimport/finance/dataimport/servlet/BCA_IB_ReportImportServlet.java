package com.gdn.venice.exportimport.finance.dataimport.servlet;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.exportimport.finance.dataimport.BCA_IB_FileReader;
import com.gdn.venice.exportimport.finance.dataimport.BCA_VA_IB_Record;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInReport;
import com.gdn.venice.persistence.FinArReconResult;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.util.VeniceConstants;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * Servlet class for importing BCA internet banking transaction reports.
 *
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class BCA_IB_ReportImportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BCA_IB_ReportImportServlet() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.BCA_IB_ReportImportServlet");
    }
    protected static Logger _log = null;
    @PersistenceContext(unitName = "GDN-Venice-Persistence", type = PersistenceContextType.TRANSACTION)
    protected EntityManager em;
    private String notificationText = "";

    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        _log.debug("BCA_IB_ReportImportServlet:hello");

        notificationText = FinanceImportServletConstants.JAVASCRIPT_ALERT_NOTIFICATION_TEXT_DEFAULT;

        if (isMultipart) { // import
            String filePath = System.getenv("VENICE_HOME") + "/files/import/finance/";

            //Upload the file
            String fileNameAndPath = FinanceImportServletHelper.upload(request, filePath, "-BCA_IB_Report.txt");

            if (fileNameAndPath == null) {
                String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_UPLOAD_EXCEPTION;
                _log.info(errMsg);
                notificationText = notificationText.replaceFirst("REPLACE", errMsg);
                response.getOutputStream().println(notificationText);
                return;
            }

            //Read the file into an ArrayList of POJO's
            BCA_IB_FileReader reader = new BCA_IB_FileReader();
            ArrayList<BCA_VA_IB_Record> records = null;
            Locator<Object> locator = null;
            FinArFundsInReport finArFundsInReport = null;
            try {
                /*
                 * Get the uniqueId and check to see if there is already a file with this uniqueId
                 */
                String uniqueId = reader.getUniqueReportIdentifier(fileNameAndPath);

                if (!FinanceImportServletHelper.isFileUnique(uniqueId, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB)) {
                    String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_FILE_NOT_UNIQUE;
                    _log.info(errMsg);
                    notificationText = notificationText.replaceFirst("REPLACE", errMsg);
                    response.getOutputStream().println(notificationText);
                    return;
                }

                records = reader.readFile(fileNameAndPath);

                if (records.size() == 0) {
                    String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_ZERO_ROWS_TO_PROCESS;
                    _log.info(errMsg);
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
                 * Create the record for the report upload
                 * 		o note that report records can be uploaded once and once only
                 */
                finArFundsInReport = FinanceImportServletHelper.createFundsInReportRecord(VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB, fileNameAndPath.substring(fileNameAndPath.lastIndexOf('/') + 1), filePath, uniqueId);
                if (finArFundsInReport == null) {
                    String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_FUNDS_IN_REPORT_CREATION_EXCEPTION;
                    _log.info(errMsg);
                    notificationText = notificationText.replaceFirst("REPLACE", errMsg);
                    response.getOutputStream().println(notificationText);
                    return;
                }

                ArrayList<BCA_VA_IB_Record> recordItems = new ArrayList<BCA_VA_IB_Record>();
                String noVA = "";
                for (BCA_VA_IB_Record items : records) {
                    BCA_VA_IB_Record value = null;
                    if (!noVA.contains(items.getAccountNumber()) || noVA.equals("")) {
                        for (BCA_VA_IB_Record item : records) {
                            if (items.getAccountNumber().equals(item.getAccountNumber())) {
                                if (value == null) {
                                    value = item;
                                } else {
                                    value.setPaymentAmount(item.getPaymentAmount() + value.getPaymentAmount());
                                    value.setBankFee(item.getBankFee() + value.getBankFee());
                                }
                                if (!noVA.contains(items.getAccountNumber())) {
                                    noVA = noVA + "," + items.getAccountNumber();
                                }
                            }
                        }
                        recordItems.add(value);
                    }

                }


                /*
                 * Process the records from the report
                 */
                ArrayList<FinArFundsInReconRecord> newFinArFundsInReconRecord = new ArrayList<FinArFundsInReconRecord>();
                for (BCA_VA_IB_Record element : recordItems) {
                    BCA_VA_IB_Record bcaIBRecord = element;

                    //Note that accountNumber is mapped to nama
                    VenOrder venOrder = FinanceImportServletHelper.getPaymentRelatedOrder(bcaIBRecord.getAccountNumber(), bcaIBRecord.getPaymentAmount(), VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB);

                    /*
                     * If there is no corresponding order, then it is payment not recognized
                     */
//					if(venOrder == null){
//						String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_NO_CORRESPONDING_PAYMENT + bcaIBRecord.getAccountNumber();
//						if(!notificationText.contains("REPLACE")){
//							notificationText = notificationText.replaceFirst("VENICE:", "VENICE:" + bcaIBRecord.getAccountNumber() + " ,");
//						}else{
//							notificationText = notificationText.replaceFirst("REPLACE", errMsg);
//						}
//						continue;
//					}

                    /*
                     * If a matching recon record can be found then use it else
                     * create a new one.
                     */
                    FinArFundsInReconRecord reconRecord = null;
                    if (venOrder != null) {
                        reconRecord = FinanceImportServletHelper
                                .getReconciliationRecord(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB, bcaIBRecord.getAccountNumber(), new BigDecimal(bcaIBRecord.getPaymentAmount()));

                    }
                    if (reconRecord == null) {
                        reconRecord = new FinArFundsInReconRecord();
                    }
                    if (FinanceImportServletHelper.cekContinueProsesFundIn(em, VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB, bcaIBRecord.getAccountNumber(), "", new BigDecimal(bcaIBRecord.getPaymentAmount()))) {

                        FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
                        finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
                        reconRecord.setFinApprovalStatus(finApprovalStatus);

                        FinArFundsInActionApplied finArFundsInActionApplied = new FinArFundsInActionApplied();
                        finArFundsInActionApplied.setActionAppliedId(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE);
                        reconRecord.setFinArFundsInActionApplied(finArFundsInActionApplied);

                        if (venOrder != null && reconRecord.getVenOrderPayment() != null) {
                            reconRecord.setOrderDate(venOrder.getOrderDate());
                            reconRecord.setWcsOrderId(venOrder.getWcsOrderId());
                            //benambahan parameter reconRecord untuk mengetahui payment yang akan di reconcile							
                            reconRecord.setRemainingBalanceAmount(FinanceImportServletHelper.getRemainingBalanceAfterPayment(em, venOrder, new Double(bcaIBRecord.getPaymentAmount()), reconRecord));
                        } else {
                            //if payment not recognized (no coresponding order), set the remaining balance to the paid amount
                            reconRecord.setRemainingBalanceAmount(new BigDecimal(bcaIBRecord.getPaymentAmount()).negate());
                            reconRecord.setNomorReff(bcaIBRecord.getAccountNumber());
                        }

                        reconRecord.setFinArFundsInReport(finArFundsInReport);
                        BigDecimal feeAmount = reconRecord.getProviderReportFeeAmount() != null ? reconRecord.getProviderReportFeeAmount() : new BigDecimal(0);
                        BigDecimal paidAmount = reconRecord.getProviderReportPaidAmount() != null ? reconRecord.getProviderReportPaidAmount() : new BigDecimal(0);
                        reconRecord.setProviderReportFeeAmount(feeAmount.add(new BigDecimal(bcaIBRecord.getBankFee())));
                        reconRecord.setProviderReportPaidAmount(paidAmount.add(new BigDecimal(bcaIBRecord.getPaymentAmount())));
                        //					reconRecord.setProviderReportPaymentDate(bcaIBRecord.getPaymentDate());
                        reconRecord.setProviderReportPaymentDate(new java.sql.Timestamp(bcaIBRecord.getPaymentDate().getTime()));
                        reconRecord.setProviderReportPaymentId(bcaIBRecord.getAccountNumber());
                        reconRecord.setReconcilliationRecordTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

                        VenOrderPayment venOrderPayment = FinanceImportServletHelper.getRelatedPaymentRecord(bcaIBRecord.getAccountNumber(), new Double(bcaIBRecord.getPaymentAmount()), VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB);

                        /*
                         * Check if the order is recognized in venice database, it must have payment record.
                         */
                        if (venOrder != null && venOrderPayment == null) {
                            String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_NOT_FOUND + bcaIBRecord.getAccountNumber();
                            _log.error(errMsg);
                            if (!notificationText.contains("REPLACE")) {
                                notificationText = notificationText.replaceFirst("VENICE:", "VENICE:" + bcaIBRecord.getAccountNumber() + " ,");
                            } else {
                                notificationText = notificationText.replaceFirst("REPLACE", errMsg);
                            }
                            continue;
                        }

                        //Check the balance due and set the reconciliation status accordingly
                        FinArReconResult finArReconResult = new FinArReconResult();
                        /*	if(reconRecord.getRemainingBalanceAmount().compareTo(new BigDecimal(0)) == 0){
                         finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_ALL);
                         }else if(reconRecord.getRemainingBalanceAmount().compareTo(new BigDecimal(0)) > 0){
                         finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_PARTIAL);
                         }else{
                         finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_OVERPAID);
                         }*/
                        BigDecimal remainingBalanceAmoun = reconRecord.getRemainingBalanceAmount().abs().compareTo(VeniceConstants.TRACEHOLD_RECEIVED) <= 0 ? new BigDecimal(0) : reconRecord.getRemainingBalanceAmount();

                        if (remainingBalanceAmoun.compareTo(new BigDecimal(0)) == 0) {
                            finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_ALL);
                        } else if (remainingBalanceAmoun.compareTo(new BigDecimal(0)) > 0) {
                            finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_PARTIAL);
                        } else {
                            finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_OVERPAID);
                        }

                        //payment not recognized
                        if (reconRecord.getRemainingBalanceAmount().compareTo(new BigDecimal(0)) < 0 && (reconRecord.getWcsOrderId() == null || reconRecord.getWcsOrderId() == "")) {
                            finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NOT_RECOGNIZED);
                        }

                        reconRecord.setFinArReconResult(finArReconResult);

                        /*
                         * Merge the record if it is existing
                         * this should be the case for most
                         * else persist it
                         */
                        newFinArFundsInReconRecord.add(reconRecord);
                    } else {
                        _log.debug("continue proses karena report payment sudah pernah di proses sebelumnya");
                    }
                }
                for (FinArFundsInReconRecord finArFundsIn : newFinArFundsInReconRecord) {
                    if (finArFundsIn.getReconciliationRecordId() == null) {
                        finArFundsIn = reconRecordHome.persistFinArFundsInReconRecord(finArFundsIn);
                    } else {
                        finArFundsIn = reconRecordHome.mergeFinArFundsInReconRecord(finArFundsIn);
                    }
                }
                String successMsg = "Report uploaded successfully... please refresh";
                notificationText = notificationText.replaceFirst("REPLACE", successMsg);
                response.getOutputStream().println(notificationText);
            } catch (Exception e) {
                if (finArFundsInReport != null) {
                    _log.debug("Remove fund in Report");
                    FinanceImportServletHelper.DeleteReport(finArFundsInReport);
                }
                String errMsg = FinanceImportServletConstants.EXCEPTION_TEXT_PAYMENT_REPORT_UPLOAD_EXCEPTION;
                _log.error(errMsg + e.getMessage());
                e.printStackTrace();
                notificationText = notificationText.replaceFirst("REPLACE", errMsg);
                response.getOutputStream().println(notificationText);
            } finally {
                try {
                    if (locator != null) {
                        locator.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
