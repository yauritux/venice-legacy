package com.gdn.venice.facade.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.djarum.raf.utilities.ModuleConfigUtility;
import com.gdn.awb.exchange.model.AirwayBillTransactionResource;
import com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote;
import com.gdn.venice.persistence.LogActionApplied;
import com.gdn.venice.persistence.LogActivityReconRecord;
import com.gdn.venice.persistence.LogActivityReportUpload;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.persistence.LogReconActivityRecordResult;
import com.gdn.venice.persistence.LogReconInvoiceRecordResult;
import com.gdn.venice.persistence.VenSettlementRecord;
import com.gdn.venice.util.VeniceConstants;

/**
 * Shared code for reconciling AWB entries o Note that this can be called from
 * 1) import servlets when importing activity or invoice 2) vertical integration
 * when MTA CX message arrives late <p> <b>author:</b> <a
 * href="mailto:david@pwsindonesia.com">David Forden</a> <p> <b>version:</b> 1.0
 * <p> <b>since:</b> 2011
 *
 */
public class AWBReconciliation {

    protected static Logger _log = null;
    private static final String ACTIVITY_RECON_RECORD_SELECT_BY_AIRWAYBILLID_SQL = "select * "
            + "from log_activity_recon_record arr "
            + "inner join log_recon_activity_record_result rarr on arr.recon_record_result_id = rarr.recon_record_result_id "
            + "where airway_bill_id = ?";

    public AWBReconciliation() {
        super();
        Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
        _log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.dataimport.servlet.AWBReconciliation");
    }

    /**
     * Reconciles an individual airway bill activity and creates the required
     * reconciliation records.
     *
     * @param logActivityReportUpload
     * @param providerAirwayBill
     * @param mtaAirwayBill
     * @param wcsOrderItemId
     * @param sequence
     * @return The new airway bill
     * @throws ServletException
     */
    public LogAirwayBill performActivityReconciliation(Connection conn, LogActivityReportUpload logActivityReportUpload, LogAirwayBill providerAirwayBill, LogAirwayBill mtaAirwayBill,
            String wcsOrderItemId) throws ServletException {
        Locator<Object> locator = null;
        _log.info("start performActivityReconciliation");
        try {
            locator = new Locator<Object>();

            LogActivityReconRecordSessionEJBRemote activityReconRecordHome = (LogActivityReconRecordSessionEJBRemote) locator
                    .lookup(LogActivityReconRecordSessionEJBRemote.class, "LogActivityReconRecordSessionEJBBean");

            ArrayList<LogActivityReconRecord> logActivityReconRecordList = null;
            ArrayList<LogActivityReconRecord> logActivityReconRecordListReadyToBeRemoved = null;

            if (mtaAirwayBill != null && mtaAirwayBill.getAirwayBillId() != null) {
                logActivityReconRecordList = new ArrayList<LogActivityReconRecord>();

//				logActivityReconRecordList = (ArrayList<LogActivityReconRecord>) activityReconRecordHome
//						.queryByRange("select o from LogActivityReconRecord o where o.logAirwayBill.airwayBillId =" + mtaAirwayBill.getAirwayBillId(), 0, 0);

                PreparedStatement psActivityReconRecord = conn.prepareStatement(ACTIVITY_RECON_RECORD_SELECT_BY_AIRWAYBILLID_SQL);
                psActivityReconRecord.setLong(1, mtaAirwayBill.getAirwayBillId());

                ResultSet rsActivityReconRecord = psActivityReconRecord.executeQuery();

                while (rsActivityReconRecord.next()) {
                    LogActivityReconRecord activityReconRecord = new LogActivityReconRecord();

                    activityReconRecord.setActivityReconRecordId(rsActivityReconRecord.getLong("activity_recon_record_id"));
                    activityReconRecord.setVeniceData(rsActivityReconRecord.getString("venice_data"));
                    activityReconRecord.setProviderData(rsActivityReconRecord.getString("provider_data"));

                    LogReconActivityRecordResult reconActivityRecordResult = new LogReconActivityRecordResult();
                    reconActivityRecordResult.setReconRecordResultId(rsActivityReconRecord.getLong("recon_record_result_id"));
                    reconActivityRecordResult.setReconRecordResultDesc(rsActivityReconRecord.getString("recon_record_result_desc"));
                    activityReconRecord.setLogReconActivityRecordResult(reconActivityRecordResult);

                    logActivityReconRecordList.add(activityReconRecord);
                }

                rsActivityReconRecord.close();
                psActivityReconRecord.close();

                logActivityReconRecordListReadyToBeRemoved = logActivityReconRecordList;
            }

            /**
             * Retrieve LogActivityReconRecord from this airwaybill and use it
             * to reconcile
             */
            // means that new data comes from Logistic / Provider
            if (providerAirwayBill.getLogActivityReconRecords() == null) {
                _log.debug("New Data from Provider");

                /*
                 * Populate MTA Airway Bill with data from the previous upload
                 */
                if (logActivityReconRecordList != null && logActivityReconRecordList.size() > 0) {
                    for (LogActivityReconRecord logActivityReconRecord : logActivityReconRecordList) {
                        LogReconActivityRecordResult logReconActivityRecordResult = logActivityReconRecord.getLogReconActivityRecordResult();

                        _log.debug("Mismatch Type => " + logReconActivityRecordResult.getReconRecordResultDesc());
                        _log.debug("MTA data => " + logActivityReconRecord.getVeniceData());
                        _log.debug("Logistic data => " + logActivityReconRecord.getProviderData());

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_PICKUP_DATE_MISMATCH) {
                            mtaAirwayBill.setActualPickupDate(new SimpleDateFormat("dd-MM-yyyy").parse(logActivityReconRecord.getVeniceData()));
                        }

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_SETTLEMENT_CODE_MISMATCH) {
//							if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS")) {
//								mtaAirwayBill.setAirwayBillNumber(logActivityReconRecord.getVeniceData());
//							} else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX")) {
//								mtaAirwayBill.setAirwayBillNumber(logActivityReconRecord.getVeniceData());
//							} else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")) {
                            mtaAirwayBill.setAirwayBillNumber(logActivityReconRecord.getVeniceData());
//							}
                        }

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_SERVICE_MISMATCH) {
                            mtaAirwayBill.getVenOrderItem().getLogLogisticService().setServiceCode(logActivityReconRecord.getVeniceData());
                        }

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_RECIPIENT_MISMATCH) {
                            mtaAirwayBill.getVenOrderItem().getVenRecipient().getVenParty().setFullOrLegalName(logActivityReconRecord.getVeniceData());
                        }

                    }
                }

                // new data comes from MTA	
            } else {
                _log.debug("New Data from MTA");

                /*
                 * Populate Logistic Airway Bill with data from the previous upload
                 */
                if (logActivityReconRecordList != null && logActivityReconRecordList.size() > 0) {
                    for (LogActivityReconRecord logActivityReconRecord : logActivityReconRecordList) {
                        LogReconActivityRecordResult logReconActivityRecordResult = logActivityReconRecord.getLogReconActivityRecordResult();

                        _log.debug("Mismatch Type => " + logReconActivityRecordResult.getReconRecordResultDesc());
                        _log.debug("MTA data => " + logActivityReconRecord.getVeniceData());
                        _log.debug("Logistic data => " + logActivityReconRecord.getProviderData());

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_PICKUP_DATE_MISMATCH) {
                            providerAirwayBill.setActualPickupDate(new SimpleDateFormat("dd-MM-yyyy").parse(logActivityReconRecord.getProviderData()));
                        }

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_SETTLEMENT_CODE_MISMATCH) {
//							if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS")) {
//								providerAirwayBill.setAirwayBillNumber(logActivityReconRecord.getProviderData());
//							} else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX")) {
//								providerAirwayBill.setAirwayBillNumber(logActivityReconRecord.getProviderData());
//							} else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")) {
                            providerAirwayBill.setAirwayBillNumber(logActivityReconRecord.getProviderData());
//							}
                        }

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_SERVICE_MISMATCH) {
                            providerAirwayBill.setService(logActivityReconRecord.getProviderData());
                        }

                        if (logReconActivityRecordResult.getReconRecordResultId() == VeniceConstants.LOG_ACTIVITY_RECON_RESULT_RECIPIENT_MISMATCH) {
                            providerAirwayBill.setConsignee(logActivityReconRecord.getProviderData());
                        }
                    }
                }
            }

            /*
             * Remove any existing recon records if there is an existing airway bill. They will be generated again
             */
            if (mtaAirwayBill != null && mtaAirwayBill.getAirwayBillId() != null) {
                if (logActivityReconRecordListReadyToBeRemoved != null && !logActivityReconRecordListReadyToBeRemoved.isEmpty()) {
                    activityReconRecordHome.removeLogActivityReconRecordList(logActivityReconRecordListReadyToBeRemoved);
                }
            }

            Boolean problemExists = false;

            // 00 - Tracking number does not exist (actually GDN Reference does not exist)			 
            if (mtaAirwayBill == null) {
                _log.debug("No existing airway bill found for report entry so creating new AWB:" + providerAirwayBill.getAirwayBillNumber());
                // Set the default approval status' for the AWB
                LogApprovalStatus logApprovalStatus = new LogApprovalStatus();
                logApprovalStatus.setApprovalStatusId(VeniceConstants.VEN_LOGISTICS_APPROVAL_STATUS_NEW);
                providerAirwayBill.setLogApprovalStatus1(logApprovalStatus);
                providerAirwayBill.setLogApprovalStatus2(logApprovalStatus);
                providerAirwayBill.setMtaData(false);

                // Persist the new AWB
                LogAirwayBillSessionEJBRemote airwayBillHome = (LogAirwayBillSessionEJBRemote) locator.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
                providerAirwayBill = airwayBillHome.persistLogAirwayBill(providerAirwayBill);
                LogActivityReconRecord logActivityReconRecord = new LogActivityReconRecord();
                logActivityReconRecord.setLogAirwayBill(providerAirwayBill);
                logActivityReconRecord.setLogActivityReportUpload(logActivityReportUpload);
                logActivityReconRecord.setComment("Venice identified that no GDN Reference exists for the record");

                LogReconActivityRecordResult logReconActivityRecordResult = new LogReconActivityRecordResult();
                logReconActivityRecordResult.setReconRecordResultId(com.gdn.venice.util.VeniceConstants.VEN_INVOICE_RECON_RESULT_0);

                logActivityReconRecord.setLogReconActivityRecordResult(logReconActivityRecordResult);
                logActivityReconRecord.setProviderData(providerAirwayBill.getTrackingNumber());
                logActivityReconRecord.setVeniceData(providerAirwayBill.getTrackingNumber());

                // Persist the record
                activityReconRecordHome.persistLogActivityReconRecord(logActivityReconRecord);

                providerAirwayBill.setActivityResultStatus("Invalid GDN Ref");

                /*
                 * If the airway bill has a detached DC then remove it
                 */
//				if(providerAirwayBill.getVenDistributionCart() != null){
//					if(providerAirwayBill.getVenDistributionCart().getDistributionCartId() == null){
//						providerAirwayBill.setVenDistributionCart(null);
//					}
//				}
                return providerAirwayBill;
            } else if (mtaAirwayBill.getVenOrderItem() == null) {
                /*
                 * If the airway bill has a detached DC then remove it
                 */
//				if(mtaAirwayBill.getVenDistributionCart() != null){
//					if(mtaAirwayBill.getVenDistributionCart().getDistributionCartId() == null){
//						mtaAirwayBill.setVenDistributionCart(null);
//					}
//				}
                return mtaAirwayBill;
            }

            /*
             * 2011-05-27 In accordance with JIRA VENICE-16 01 - Pickup Date	Late
             * o A flag is used in the AWB table to determine if the data is from MTA 
             * o If the existing data is from MTA then reconcile 
             * o If the existing data is from a previous report upload then update it
             * Therefore first check to see if there is data from MTA before reconciling
             */

            //  01 - Pickup date is late o According to spec - This needs to be fished from the SLA and compared 
            // o The spec is WRONG because we have actual from MTA to compare the date with. Late pickup is handled by SLA and KPI. This routine only needs to identify discrepancies.			 
            if (mtaAirwayBill.getActualPickupDate() != null && providerAirwayBill.getActualPickupDate().compareTo(mtaAirwayBill.getActualPickupDate()) != 0) {
                _log.debug("Pickup date mismatch found for AWB activity:" + mtaAirwayBill.getAirwayBillNumber());

                LogAirwayBillSessionEJBRemote airwayBillHome = (LogAirwayBillSessionEJBRemote) locator
                        .lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

                LogActivityReconRecord logActivityReconRecord = new LogActivityReconRecord();
                if (mtaAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(mtaAirwayBill);
                } else if (providerAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(providerAirwayBill);
                }
                logActivityReconRecord.setLogActivityReportUpload(logActivityReportUpload);
                logActivityReconRecord.setComment("Venice identified a pickup date mismatch");

                LogReconActivityRecordResult logReconActivityRecordResult = new LogReconActivityRecordResult();
                logReconActivityRecordResult.setReconRecordResultId(com.gdn.venice.util.VeniceConstants.VEN_ACTIVITY_RECON_RESULT_1);

                SimpleDateFormat sdf = new SimpleDateFormat("d-MM-yyyy");
                logActivityReconRecord.setLogReconActivityRecordResult(logReconActivityRecordResult);
                logActivityReconRecord.setProviderData(sdf.format(providerAirwayBill.getActualPickupDate()));
                logActivityReconRecord.setVeniceData(sdf.format(mtaAirwayBill.getActualPickupDate()));

                // when PU date mismatch occur, PU Logistic will be applied
                LogActionApplied actionApplied = new LogActionApplied();
                actionApplied.setActionAppliedId(1L);
                logActivityReconRecord.setLogActionApplied(actionApplied);
                logActivityReconRecord.setUserLogonName("System");
                logActivityReconRecord.setComment("Logistic Data Applied");
                // retrieve current airwaybill
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("select o from LogAirwayBill o where o.airwayBillId = ");

                // means that new data comes from Logistic / Provider
                if (providerAirwayBill.getLogActivityReconRecords() == null) {
                    queryBuilder.append(mtaAirwayBill.getAirwayBillId());
                    // means that new data comes from MTA
                } else if (mtaAirwayBill.getLogActivityReconRecords() == null) {
                    queryBuilder.append(providerAirwayBill.getAirwayBillId());
                }

                List<LogAirwayBill> airwayBillList = airwayBillHome.queryByRange(queryBuilder.toString(), 0, 0);
                LogAirwayBill airwayBill = airwayBillList.get(0);
                // apply PU Logistic
                airwayBill.setActualPickupDate(providerAirwayBill.getActualPickupDate());
                // merge with current airwaybill
                airwayBillHome.mergeLogAirwayBill(airwayBill);

                // Persist the recon record
                activityReconRecordHome.persistLogActivityReconRecord(logActivityReconRecord);
                problemExists = false;
            }

            // 02 - Settlement code mismatch (compare with settlement record)
			/*
             * The case for NCS uses delivery order 
             * The case for RPX uses AWB number
             * The case for JNE uses AWB number
             */

//			if ((providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS") && providerAirwayBill.getDeliveryOrder() != null && !compareSettlementCode(mtaAirwayBill.getDeliveryOrder(), providerAirwayBill.getDeliveryOrder()) )
//					|| (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX") && providerAirwayBill.getAirwayBillNumber() != null && !compareSettlementCode(mtaAirwayBill.getAirwayBillNumber(), providerAirwayBill.getAirwayBillNumber()))
//					|| (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE") && providerAirwayBill.getAirwayBillNumber() != null && !compareSettlementCode(mtaAirwayBill.getAirwayBillNumber(), providerAirwayBill.getAirwayBillNumber()))) {

            if (providerAirwayBill.getAirwayBillNumber() != null && !compareSettlementCode(mtaAirwayBill.getAirwayBillNumber(), providerAirwayBill.getAirwayBillNumber())) {


                _log.debug("Settlement code mismatch found for AWB activity:" + mtaAirwayBill.getAirwayBillNumber());
                LogActivityReconRecord logActivityReconRecord = new LogActivityReconRecord();
                if (mtaAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(mtaAirwayBill);
                } else if (providerAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(providerAirwayBill);
                }
                logActivityReconRecord.setLogActivityReportUpload(logActivityReportUpload);
                logActivityReconRecord.setComment("Venice identified a settlement code mismatch");

                LogReconActivityRecordResult logReconActivityRecordResult = new LogReconActivityRecordResult();
                logReconActivityRecordResult.setReconRecordResultId(com.gdn.venice.util.VeniceConstants.VEN_ACTIVITY_RECON_RESULT_2);
                logActivityReconRecord.setLogReconActivityRecordResult(logReconActivityRecordResult);

//				if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS")) {
//					logActivityReconRecord.setProviderData(providerAirwayBill.getDeliveryOrder());
//					logActivityReconRecord.setVeniceData(mtaAirwayBill.getDeliveryOrder());
//				} else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX")) {
//					logActivityReconRecord.setProviderData(providerAirwayBill.getAirwayBillNumber());
//					logActivityReconRecord.setVeniceData(mtaAirwayBill.getAirwayBillNumber());
//				} else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")) {
//					logActivityReconRecord.setProviderData(providerAirwayBill.getAirwayBillNumber());
//					logActivityReconRecord.setVeniceData(mtaAirwayBill.getAirwayBillNumber());
//				}

                logActivityReconRecord.setProviderData(providerAirwayBill.getAirwayBillNumber());
                logActivityReconRecord.setVeniceData(mtaAirwayBill.getAirwayBillNumber());

                // Persist the record
                activityReconRecordHome.persistLogActivityReconRecord(logActivityReconRecord);
                problemExists = true;
            }

            // 03 - Service mismatch (compare with service of existing AWB)
			/*
             * Convert the service codes over to internal codes
             */
            if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS")) {
                if (providerAirwayBill.getService().equalsIgnoreCase("REGULER") || providerAirwayBill.getService().equalsIgnoreCase("REGULAR")) {
                    providerAirwayBill.setService("NCS_REG");
                } else if (providerAirwayBill.getService().equalsIgnoreCase("KIRIMAN 1 HARI")) {
                    providerAirwayBill.setService("NCS_EXP");
                }
            } else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX")) {
                if (providerAirwayBill.getService().equalsIgnoreCase("PP")) {
                    providerAirwayBill.setService("RPX_EXP");
                } else if (providerAirwayBill.getService().equalsIgnoreCase("EP")) {
                    providerAirwayBill.setService("RPX_REG");
                }
            } else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")) {
                // leave it as it is
            } else if (providerAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("MSG")) {
                providerAirwayBill.setService("MSG");
            }

            if (mtaAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS")) {
                mtaAirwayBill.getVenOrderItem().getLogLogisticService().setServiceCode("NCS_REG");
            } else if (mtaAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX")) {
                mtaAirwayBill.getVenOrderItem().getLogLogisticService().setServiceCode("RPX_EXP");
            } else if (mtaAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")) {
                mtaAirwayBill.getVenOrderItem().getLogLogisticService().setServiceCode("JNE_REG");
            } else if (mtaAirwayBill.getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("MSG")) {
                mtaAirwayBill.getVenOrderItem().getLogLogisticService().setServiceCode("MSG");
            }

            if (mtaAirwayBill.getVenOrderItem() != null && !providerAirwayBill.getService().equalsIgnoreCase(mtaAirwayBill.getVenOrderItem().getLogLogisticService().getServiceCode())) {
                _log.debug("Service mismatch found for AWB activity:" + mtaAirwayBill.getAirwayBillNumber());
                LogActivityReconRecord logActivityReconRecord = new LogActivityReconRecord();
                if (mtaAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(mtaAirwayBill);
                } else if (providerAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(providerAirwayBill);
                }
                logActivityReconRecord.setLogActivityReportUpload(logActivityReportUpload);
                logActivityReconRecord.setComment("Venice identified a service mismatch");

                LogReconActivityRecordResult logReconActivityRecordResult = new LogReconActivityRecordResult();
                logReconActivityRecordResult.setReconRecordResultId(com.gdn.venice.util.VeniceConstants.VEN_ACTIVITY_RECON_RESULT_3);

                logActivityReconRecord.setLogReconActivityRecordResult(logReconActivityRecordResult);
                logActivityReconRecord.setProviderData(providerAirwayBill.getService());
                logActivityReconRecord.setVeniceData(mtaAirwayBill.getVenOrderItem().getLogLogisticService().getServiceCode());

                // Persist the record
                activityReconRecordHome.persistLogActivityReconRecord(logActivityReconRecord);
                problemExists = true;
            }

            //04 - Recipient mismatch (compare consignee from report with recipient party full or legal name)			 
            if (mtaAirwayBill.getVenOrderItem() != null && !(providerAirwayBill.getConsignee() != null ? providerAirwayBill.getConsignee().trim() : "").equalsIgnoreCase(mtaAirwayBill.getVenOrderItem().getVenRecipient().getVenParty().getFullOrLegalName().trim())) {
                _log.debug("Recipient mismatch found for AWB activity:" + mtaAirwayBill.getAirwayBillNumber());
                LogActivityReconRecord logActivityReconRecord = new LogActivityReconRecord();

                if (mtaAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(mtaAirwayBill);
                } else if (providerAirwayBill.getAirwayBillId() != null) {
                    logActivityReconRecord.setLogAirwayBill(providerAirwayBill);
                }

                logActivityReconRecord.setLogActivityReportUpload(logActivityReportUpload);
                logActivityReconRecord.setComment("Venice identified a recipient mismatch");

                LogReconActivityRecordResult logReconActivityRecordResult = new LogReconActivityRecordResult();
                logReconActivityRecordResult.setReconRecordResultId(com.gdn.venice.util.VeniceConstants.VEN_ACTIVITY_RECON_RESULT_4);

                logActivityReconRecord.setLogReconActivityRecordResult(logReconActivityRecordResult);
                logActivityReconRecord.setProviderData(providerAirwayBill.getConsignee() != null ? providerAirwayBill.getConsignee().trim() : "");

                logActivityReconRecord.setVeniceData(mtaAirwayBill.getVenOrderItem().getVenRecipient().getVenParty().getFullOrLegalName());

                // Persist the record
                activityReconRecordHome.persistLogActivityReconRecord(logActivityReconRecord);
                problemExists = true;
            }


//			// 05 - AWB number mismatch
//			/*
//			 * 
//			 */
//			
//			if (!mtaAirwayBill.getAirwayBillNumber().equals(providerAirwayBill.getAirwayBillNumber())) {
//												
//				_log.debug("Airway Bill Number mismatch found for AWB activity:" + mtaAirwayBill.getAirwayBillNumber());
//				LogActivityReconRecord logActivityReconRecord = new LogActivityReconRecord();
//				if (mtaAirwayBill.getAirwayBillId() != null) {
//					logActivityReconRecord.setLogAirwayBill(mtaAirwayBill);
//				} else if (providerAirwayBill.getAirwayBillId() != null) {
//					logActivityReconRecord.setLogAirwayBill(providerAirwayBill);
//				}
//				logActivityReconRecord.setLogActivityReportUpload(logActivityReportUpload);
//				logActivityReconRecord.setComment("Venice identified a airway bill no mismatch");
//
//				LogReconActivityRecordResult logReconActivityRecordResult = new LogReconActivityRecordResult();
//				logReconActivityRecordResult.setReconRecordResultId(com.gdn.venice.util.VeniceConstants.VEN_ACTIVITY_RECON_RESULT_2);
//				logActivityReconRecord.setLogReconActivityRecordResult(logReconActivityRecordResult);
//				
//				logActivityReconRecord.setProviderData(providerAirwayBill.getAirwayBillNumber());
//				logActivityReconRecord.setVeniceData(mtaAirwayBill.getAirwayBillNumber());
//				
//				// Persist the record
//				activityReconRecordHome.persistLogActivityReconRecord(logActivityReconRecord);
//				problemExists = true;
//			}

            String resultStatus = "OK";
            if (problemExists) {
                resultStatus = "Problem Exists";
            }
            // Set the status and return the new AWB
            providerAirwayBill.setActivityResultStatus(resultStatus);
            locator.close();

            /*
             * If the airway bill has a detached DC then remove it
             */
//			if(providerAirwayBill.getVenDistributionCart() != null){
//				if(providerAirwayBill.getVenDistributionCart().getDistributionCartId() == null){
//					providerAirwayBill.setVenDistributionCart(null);
//				}
//			}

            return providerAirwayBill;
        } catch (Exception e) {
            String errMsg = "An exception occured when performing activity reconciliation for an airway bill: ";
            _log.error(errMsg + e.getMessage(), e);
            e.printStackTrace();
            throw new ServletException(errMsg);
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

    /**
     * Reconciles an individual airway number for several airwaybill records'
     * invoice and creates the required reconciliation records.
     *
     * @param logInvoiceReportUpload
     * @param providerAirwayBill
     * @param venAirwayBills
     * @throws ServletException
     */
    public void performInvoiceReconciliation(LogInvoiceReportUpload logInvoiceReportUpload, LogAirwayBill providerAirwayBill, AirwayBillTransactionResource mtaAirwayBill, HashMap<String, String> failedRecord) throws ServletException {
        //Invoice reconciliation is on airwaybill level
        Locator<Object> locator = null;

        String awbNumber = providerAirwayBill.getAirwayBillNumber(),
                reRecon = "";

        ModuleConfigUtility moduleConfigUtility = new ModuleConfigUtility();

        BigDecimal weightTolerance = BigDecimal.valueOf(moduleConfigUtility.getDoubleValue("invoiceReconciliation.invoiceTolerance.weightTolerance")),
                priceTolerance = BigDecimal.valueOf(moduleConfigUtility.getDoubleValue("invoiceReconciliation.invoiceTolerance.priceTolerance")),
                insuranceTolerance = BigDecimal.valueOf(moduleConfigUtility.getDoubleValue("invoiceReconciliation.invoiceTolerance.insuranceTolerance")),
                otherChargeTolerance = BigDecimal.valueOf(moduleConfigUtility.getDoubleValue("invoiceReconciliation.invoiceTolerance.otherChargeTolerance")),
                giftWrapTolerance = BigDecimal.valueOf(moduleConfigUtility.getDoubleValue("invoiceReconciliation.invoiceTolerance.giftWrapTolerance")),
                totalChargeTolerance = BigDecimal.valueOf(moduleConfigUtility.getDoubleValue("invoiceReconciliation.invoiceTolerance.totalChargeTolerance")),
                initialValue = BigDecimal.ZERO;

        boolean firstReconAttemp;

        try {
            locator = new Locator<Object>();

            LogInvoiceReconRecordSessionEJBRemote invoiceReconRecordHome = (LogInvoiceReconRecordSessionEJBRemote) locator
                    .lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");

            LogInvoiceReportUploadSessionEJBRemote invoiceReportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
                    .lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");

            LogInvoiceAirwaybillRecordSessionEJBRemote invoiceAWBRecordHome = (LogInvoiceAirwaybillRecordSessionEJBRemote) locator
                    .lookup(LogInvoiceAirwaybillRecordSessionEJBRemote.class, "LogInvoiceAirwaybillRecordSessionEJBBean");

            LogAirwayBillSessionEJBRemote airwayBillHome = (LogAirwayBillSessionEJBRemote) locator
                    .lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

            LogInvoiceAirwaybillRecord invoiceAWBRecord;
            try {
                invoiceAWBRecord = invoiceAWBRecordHome.queryByRange("select o from LogInvoiceAirwaybillRecord o where o.airwayBillNumber = '"
                        + awbNumber + "' and o.logInvoiceReportUpload.logLogisticsProvider.logisticsProviderId = " + logInvoiceReportUpload.getLogLogisticsProvider().getLogisticsProviderId(), 0, 1).get(0);
                invoiceAWBRecord.setLogInvoiceReportUpload(logInvoiceReportUpload);
            } catch (IndexOutOfBoundsException e) {
                invoiceAWBRecord = new LogInvoiceAirwaybillRecord();
                invoiceAWBRecord.setAirwayBillNumber(awbNumber);
                invoiceAWBRecord.setLogInvoiceReportUpload(logInvoiceReportUpload);
                invoiceAWBRecord = invoiceAWBRecordHome.persistLogInvoiceAirwaybillRecord(invoiceAWBRecord);
            }

            if (invoiceAWBRecord.getProviderGiftWrapCharge() == null) {
                invoiceAWBRecord.setProviderGiftWrapCharge(new BigDecimal(-1));
            }
            if (invoiceAWBRecord.getProviderInsuranceCharge() == null) {
                invoiceAWBRecord.setProviderInsuranceCharge(new BigDecimal(-1));
            }
            if (invoiceAWBRecord.getProviderOtherCharge() == null) {
                invoiceAWBRecord.setProviderOtherCharge(new BigDecimal(-1));
            }
            if (invoiceAWBRecord.getProviderPackageWeight() == null) {
                invoiceAWBRecord.setProviderPackageWeight(new BigDecimal(-1));
            }
            if (invoiceAWBRecord.getProviderPricePerKg() == null) {
                invoiceAWBRecord.setProviderPricePerKg(new BigDecimal(-1));
            }
            if (invoiceAWBRecord.getProviderTotalCharge() == null) {
                invoiceAWBRecord.setProviderTotalCharge(new BigDecimal(-1));
            }

            // remove any existing recon records
//			if (awbNumber != null && !awbNumber.isEmpty()) {
//				ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
//					.queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId(), 0, 0);
//				if (logInvoiceReconRecordList != null && !logInvoiceReconRecordList.isEmpty())
//					invoiceReconRecordHome.removeLogInvoiceReconRecordList(logInvoiceReconRecordList);
//			}

            //If logistic partner charge us more than what's saved on engine
            //report that there's problem exist, other's marked as OK
            Boolean problemExists = false;

            // 01 - Weight Mismatch
            BigDecimal correctWeight = new BigDecimal("0");
            correctWeight = correctWeight.setScale(2, RoundingMode.UP);
            correctWeight = BigDecimal.valueOf(mtaAirwayBill.getTotalWeight());

            BigDecimal providerWeight = new BigDecimal("0");
            providerWeight = providerWeight.setScale(2, RoundingMode.UP);
            providerWeight = providerAirwayBill.getPackageWeight();

            if (providerWeight.compareTo(invoiceAWBRecord.getProviderPackageWeight()) != 0) {
                if (invoiceAWBRecord.getProviderPackageWeight().compareTo(new BigDecimal(-1)) != 0) {
                    firstReconAttemp = false;
                    initialValue = invoiceAWBRecord.getProviderPackageWeight();
                } else {
                    firstReconAttemp = true;
                }

                invoiceAWBRecord.setVenicePackageWeight(correctWeight);
                invoiceAWBRecord.setProviderPackageWeight(providerWeight);

                ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
                        .queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId()
                        + " and o.logReconInvoiceRecordResult.reconRecordResultId = " + VeniceConstants.LOG_INVOICE_RECON_RESULT_WEIGHT_MISMATCH, 0, 0);
                if (!logInvoiceReconRecordList.isEmpty()) {
                    invoiceReconRecordHome.removeLogInvoiceReconRecordList(logInvoiceReconRecordList);
                }

                if (providerWeight.compareTo(correctWeight) == 1 && (providerWeight.subtract(correctWeight).abs()).compareTo(weightTolerance) == 1) {
                    _log.debug("Weight mismatch found for AWB invoice:" + mtaAirwayBill.getAirwayBill().getNumber());

                    if (!firstReconAttemp) {
                        reRecon = reRecon.concat(reRecon.equals("") ? "weight" : ", weight").concat("(" + initialValue + " -> " + providerWeight + ")");
                    }

                    LogInvoiceReconRecord logInvoiceReconRecord = new LogInvoiceReconRecord();

                    logInvoiceReconRecord.setLogInvoiceAirwaybillRecord(invoiceAWBRecord);
                    logInvoiceReconRecord.setComment("Venice identified a weight mismatch");

                    LogReconInvoiceRecordResult logReconInvoiceRecordResult = new LogReconInvoiceRecordResult();
                    logReconInvoiceRecordResult.setReconRecordResultId(new Long(VeniceConstants.LOG_INVOICE_RECON_RESULT_WEIGHT_MISMATCH));

                    logInvoiceReconRecord.setLogReconInvoiceRecordResult(logReconInvoiceRecordResult);
                    BigDecimal providerPackageWeight = providerAirwayBill.getPackageWeight();
                    providerPackageWeight = providerPackageWeight.setScale(2, RoundingMode.UP);
                    logInvoiceReconRecord.setProviderData(providerPackageWeight.toString());
                    logInvoiceReconRecord.setVeniceData(correctWeight.toString());

                    // Persist the record
                    invoiceReconRecordHome.persistLogInvoiceReconRecord(logInvoiceReconRecord);
                    problemExists = true;
                    providerWeight = null;
                } else {
                    invoiceAWBRecord.setApprovedPackageWeight(providerWeight);
                }
            }

            // 02 - Price/Kg Mismatch
            BigDecimal correctPricePerKg = new BigDecimal("0");
            correctPricePerKg = correctPricePerKg.setScale(2, RoundingMode.UP);
            correctPricePerKg = BigDecimal.valueOf(mtaAirwayBill.getItems()[0].getPricePerKg());

            BigDecimal providerPricePerKg = providerAirwayBill.getPricePerKg();
            providerPricePerKg = providerPricePerKg.setScale(2, RoundingMode.UP);

            if (providerPricePerKg.compareTo(invoiceAWBRecord.getProviderPricePerKg()) != 0) {
                if (invoiceAWBRecord.getProviderPricePerKg().compareTo(new BigDecimal(-1)) != 0) {
                    firstReconAttemp = false;
                    initialValue = invoiceAWBRecord.getProviderPricePerKg();
                } else {
                    firstReconAttemp = true;
                }

                invoiceAWBRecord.setVenicePricePerKg(correctPricePerKg);
                invoiceAWBRecord.setProviderPricePerKg(providerPricePerKg);

                ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
                        .queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId()
                        + " and o.logReconInvoiceRecordResult.reconRecordResultId = " + VeniceConstants.LOG_INVOICE_RECON_RESULT_PRICE_PER_KG_MISMATCH, 0, 0);
                if (!logInvoiceReconRecordList.isEmpty()) {
                    invoiceReconRecordHome.removeLogInvoiceReconRecordList(logInvoiceReconRecordList);
                }

                if (correctPricePerKg != null && providerPricePerKg.compareTo(correctPricePerKg) == 1) {
                    BigDecimal selisih = correctPricePerKg.subtract(providerPricePerKg).abs();
                    if (selisih.compareTo(priceTolerance) <= 0) {
                        _log.debug("selisih is less than or equal to tolerance, so don't set to mismatch");
                        invoiceAWBRecord.setApprovedPricePerKg(providerPricePerKg);
                    } else {
                        _log.debug("Price per KG mismatch found for AWB invoice:" + mtaAirwayBill.getAirwayBill().getNumber());

                        if (!firstReconAttemp) {
                            reRecon = reRecon.concat(reRecon.equals("") ? "price per kg" : ", price per kg").concat("(" + initialValue + " -> " + providerPricePerKg + ")");
                        }

                        LogInvoiceReconRecord logInvoiceReconRecord = new LogInvoiceReconRecord();

                        logInvoiceReconRecord.setLogInvoiceAirwaybillRecord(invoiceAWBRecord);
                        logInvoiceReconRecord.setComment("Venice identified a price per Kg mismatch");

                        LogReconInvoiceRecordResult logReconInvoiceRecordResult = new LogReconInvoiceRecordResult();
                        logReconInvoiceRecordResult.setReconRecordResultId(new Long(VeniceConstants.LOG_INVOICE_RECON_RESULT_PRICE_PER_KG_MISMATCH));

                        logInvoiceReconRecord.setLogReconInvoiceRecordResult(logReconInvoiceRecordResult);
                        logInvoiceReconRecord.setProviderData(providerPricePerKg.toString());
                        logInvoiceReconRecord.setVeniceData(correctPricePerKg.toString());

                        // Persist the record
                        invoiceReconRecordHome.persistLogInvoiceReconRecord(logInvoiceReconRecord);
                        problemExists = true;
                        providerPricePerKg = null;
                    }
                } else {
                    invoiceAWBRecord.setApprovedPricePerKg(providerPricePerKg);
                }
            }

            // 03 - Insurance Cost Mismatch
//			Double distributionCartDivisor = mtaAirwayBill.getVenDistributionCart().getQuantity().doubleValue() / mtaAirwayBill.getVenOrderItem().getQuantity().doubleValue();
            BigDecimal correctInsuranceCharge = new BigDecimal("0");
            correctInsuranceCharge = correctInsuranceCharge.setScale(2, RoundingMode.UP);
            correctInsuranceCharge = BigDecimal.valueOf(mtaAirwayBill.getTotalInsuranceCost());

            BigDecimal providerInsuranceCharge = providerAirwayBill.getInsuranceCharge();
            providerInsuranceCharge = providerInsuranceCharge.setScale(2, RoundingMode.UP);

            if (providerInsuranceCharge.compareTo(invoiceAWBRecord.getProviderInsuranceCharge()) != 0) {
                if (invoiceAWBRecord.getProviderInsuranceCharge().compareTo(new BigDecimal(-1)) != 0) {
                    firstReconAttemp = false;
                    initialValue = invoiceAWBRecord.getProviderInsuranceCharge();
                } else {
                    firstReconAttemp = true;
                }

                invoiceAWBRecord.setVeniceInsuranceCharge(correctInsuranceCharge);
                invoiceAWBRecord.setProviderInsuranceCharge(providerInsuranceCharge);

                ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
                        .queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId()
                        + " and o.logReconInvoiceRecordResult.reconRecordResultId = " + VeniceConstants.LOG_INVOICE_RECON_RESULT_INSURANCE_COST_MISMATCH, 0, 0);
                if (!logInvoiceReconRecordList.isEmpty()) {
                    invoiceReconRecordHome.removeLogInvoiceReconRecordList(logInvoiceReconRecordList);
                }

                if (providerInsuranceCharge.compareTo(correctInsuranceCharge) == 1) {
                    //selisih 1 dianggap match
                    BigDecimal selisih = correctInsuranceCharge.subtract(providerInsuranceCharge).abs();
                    if (selisih.compareTo(insuranceTolerance) <= 0) {
                        _log.debug("selisih is less than or equal to tolerance, so don't set to mismatch");
                        invoiceAWBRecord.setApprovedInsuranceCharge(providerInsuranceCharge);
                    } else {
                        _log.debug("Insurance charge mismatch found for AWB invoice:" + mtaAirwayBill.getAirwayBill().getNumber());

                        if (!firstReconAttemp) {
                            reRecon = reRecon.concat(reRecon.equals("") ? "insurance charge" : ", insurance charge").concat("(" + initialValue + " -> " + providerInsuranceCharge + ")");
                        }

                        LogInvoiceReconRecord logInvoiceReconRecord = new LogInvoiceReconRecord();

                        logInvoiceReconRecord.setLogInvoiceAirwaybillRecord(invoiceAWBRecord);
                        logInvoiceReconRecord.setComment("Venice identified an insurance charges mismatch");

                        LogReconInvoiceRecordResult logReconInvoiceRecordResult = new LogReconInvoiceRecordResult();
                        logReconInvoiceRecordResult.setReconRecordResultId(new Long(VeniceConstants.LOG_INVOICE_RECON_RESULT_INSURANCE_COST_MISMATCH));

                        logInvoiceReconRecord.setLogReconInvoiceRecordResult(logReconInvoiceRecordResult);
                        logInvoiceReconRecord.setProviderData(providerInsuranceCharge.toString());
                        logInvoiceReconRecord.setVeniceData(correctInsuranceCharge.toString());

                        // Persist the record
                        invoiceReconRecordHome.persistLogInvoiceReconRecord(logInvoiceReconRecord);
                        problemExists = true;
                        providerInsuranceCharge = null;
                    }
                } else {
                    invoiceAWBRecord.setApprovedInsuranceCharge(providerInsuranceCharge);
                }
            }

            // 04 - Other Charge Mismatch
            BigDecimal correctOtherCharge = new BigDecimal("0");
            correctOtherCharge = correctOtherCharge.setScale(2, RoundingMode.UP);
            correctOtherCharge = BigDecimal.valueOf(mtaAirwayBill.getTotalFixPrice());

            BigDecimal providerOtherCharge = new BigDecimal(0);
            providerOtherCharge = providerOtherCharge.setScale(2, RoundingMode.UP);
            providerOtherCharge = providerAirwayBill.getOtherCharge();

            if (providerOtherCharge.compareTo(invoiceAWBRecord.getProviderOtherCharge()) != 0) {
                if (invoiceAWBRecord.getProviderOtherCharge().compareTo(new BigDecimal(-1)) != 0) {
                    firstReconAttemp = false;
                    initialValue = invoiceAWBRecord.getProviderOtherCharge();
                } else {
                    firstReconAttemp = true;
                }

                invoiceAWBRecord.setVeniceOtherCharge(correctOtherCharge);
                invoiceAWBRecord.setProviderOtherCharge(providerOtherCharge);

                ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
                        .queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId()
                        + " and o.logReconInvoiceRecordResult.reconRecordResultId = " + VeniceConstants.LOG_INVOICE_RECON_RESULT_BIAYA_KAYU_MISMATCH, 0, 0);
                if (!logInvoiceReconRecordList.isEmpty()) {
                    invoiceReconRecordHome.removeLogInvoiceReconRecordList(logInvoiceReconRecordList);
                }

                if (providerOtherCharge.compareTo(correctOtherCharge) == 1) {
                    //selisih 1 dianggap match
                    BigDecimal selisih = correctOtherCharge.subtract(providerOtherCharge).abs();
                    if (selisih.compareTo(otherChargeTolerance) <= 0) {
                        _log.debug("selisih is less than or equal to tolerance, so don't set to mismatch");
                        invoiceAWBRecord.setApprovedOtherCharge(providerOtherCharge);
                    } else {
                        _log.debug("Other (Biaya Packaging Kayu) charge mismatch found for AWB invoice:" + mtaAirwayBill.getAirwayBill().getNumber());

                        if (!firstReconAttemp) {
                            reRecon = reRecon.concat(reRecon.equals("") ? "other charge" : ", other charge").concat("(" + initialValue + " -> " + providerOtherCharge + ")");
                        }

                        LogInvoiceReconRecord logInvoiceReconRecord = new LogInvoiceReconRecord();

                        logInvoiceReconRecord.setLogInvoiceAirwaybillRecord(invoiceAWBRecord);
                        logInvoiceReconRecord.setComment("Venice identified an other (Biaya Packaging Kayu) charges mismatch");

                        LogReconInvoiceRecordResult logReconInvoiceRecordResult = new LogReconInvoiceRecordResult();
                        logReconInvoiceRecordResult.setReconRecordResultId(new Long(VeniceConstants.LOG_INVOICE_RECON_RESULT_BIAYA_KAYU_MISMATCH));

                        logInvoiceReconRecord.setLogReconInvoiceRecordResult(logReconInvoiceRecordResult);
                        logInvoiceReconRecord.setProviderData(providerOtherCharge.toString());
                        logInvoiceReconRecord.setVeniceData(correctOtherCharge.toString());

                        // Persist the record
                        invoiceReconRecordHome.persistLogInvoiceReconRecord(logInvoiceReconRecord);
                        problemExists = true;
                        providerOtherCharge = null;
                    }
                } else {
                    invoiceAWBRecord.setApprovedOtherCharge(providerOtherCharge);
                }
            }

            // 05 - Gift Wrap Charge Mismatch
            BigDecimal correctGiftWrapCharge = new BigDecimal("0");
            correctGiftWrapCharge = correctGiftWrapCharge.setScale(2, RoundingMode.UP);
            correctGiftWrapCharge = BigDecimal.valueOf(mtaAirwayBill.getTotalGiftWrap());

            BigDecimal providerGiftWrapCharge = providerAirwayBill.getGiftWrapCharge();
            providerGiftWrapCharge = providerGiftWrapCharge.setScale(2, RoundingMode.UP);

            if (providerGiftWrapCharge.compareTo(invoiceAWBRecord.getProviderGiftWrapCharge()) != 0) {
                if (invoiceAWBRecord.getProviderGiftWrapCharge().compareTo(new BigDecimal(-1)) != 0) {
                    firstReconAttemp = false;
                    initialValue = invoiceAWBRecord.getProviderGiftWrapCharge();
                } else {
                    firstReconAttemp = true;
                }

                invoiceAWBRecord.setVeniceGiftWrapCharge(correctGiftWrapCharge);
                invoiceAWBRecord.setProviderGiftWrapCharge(providerGiftWrapCharge);

                ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
                        .queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId()
                        + " and o.logReconInvoiceRecordResult.reconRecordResultId = " + VeniceConstants.LOG_INVOICE_RECON_RESULT_GIFT_WRAP_MISMATCH, 0, 0);
                if (!logInvoiceReconRecordList.isEmpty()) {
                    invoiceReconRecordHome.removeLogInvoiceReconRecordList(logInvoiceReconRecordList);
                }

                if (providerGiftWrapCharge.compareTo(correctGiftWrapCharge) == 1) {
                    BigDecimal selisih = correctGiftWrapCharge.subtract(providerGiftWrapCharge).abs();
                    if (selisih.compareTo(giftWrapTolerance) <= 0) {
                        _log.debug("selisih is less than or equal to tolerance, so don't set to mismatch");
                        invoiceAWBRecord.setApprovedGiftWrapCharge(providerGiftWrapCharge);
                    } else {
                        _log.debug("Gift wrap charge mismatch found for AWB invoice:" + mtaAirwayBill.getAirwayBill().getNumber());

                        if (!firstReconAttemp) {
                            reRecon = reRecon.concat(reRecon.equals("") ? "gift wrap charge" : ", gift wrap charge").concat("(" + initialValue + " -> " + providerGiftWrapCharge + ")");
                        }

                        LogInvoiceReconRecord logInvoiceReconRecord = new LogInvoiceReconRecord();

                        logInvoiceReconRecord.setLogInvoiceAirwaybillRecord(invoiceAWBRecord);
                        logInvoiceReconRecord.setComment("Venice identified a gift wrap charges mismatch");

                        LogReconInvoiceRecordResult logReconInvoiceRecordResult = new LogReconInvoiceRecordResult();
                        logReconInvoiceRecordResult.setReconRecordResultId(new Long(VeniceConstants.LOG_INVOICE_RECON_RESULT_GIFT_WRAP_MISMATCH));

                        logInvoiceReconRecord.setLogReconInvoiceRecordResult(logReconInvoiceRecordResult);
                        logInvoiceReconRecord.setProviderData(providerGiftWrapCharge.toString());
                        logInvoiceReconRecord.setVeniceData(correctGiftWrapCharge.toString());

                        // Persist the record
                        invoiceReconRecordHome.persistLogInvoiceReconRecord(logInvoiceReconRecord);
                        problemExists = true;
                        providerGiftWrapCharge = null;
                    }
                } else {
                    invoiceAWBRecord.setApprovedGiftWrapCharge(providerGiftWrapCharge);
                }
            }

            // 06 - Total Charge Mismatch
            BigDecimal correctTotalCharge = new BigDecimal("0");
            correctTotalCharge = correctTotalCharge.setScale(2, RoundingMode.UP);

            //add insurance cost in total charge
            correctTotalCharge = (BigDecimal.valueOf(mtaAirwayBill.getTotalShippingCost())).add(BigDecimal.valueOf(mtaAirwayBill.getTotalInsuranceCost()));

            BigDecimal providerTotalCharge = providerAirwayBill.getTotalCharge();
            providerTotalCharge = providerTotalCharge.setScale(2, RoundingMode.UP);

            if (providerTotalCharge.compareTo(invoiceAWBRecord.getProviderTotalCharge()) != 0) {
                if (invoiceAWBRecord.getProviderTotalCharge().compareTo(new BigDecimal(-1)) != 0) {
                    firstReconAttemp = false;
                    initialValue = invoiceAWBRecord.getProviderTotalCharge();
                } else {
                    firstReconAttemp = true;
                }

                invoiceAWBRecord.setVeniceTotalCharge(correctTotalCharge);
                invoiceAWBRecord.setProviderTotalCharge(providerTotalCharge);

                ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
                        .queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId()
                        + " and o.logReconInvoiceRecordResult.reconRecordResultId = " + VeniceConstants.LOG_INVOICE_RECON_RESULT_TOTAL_CHARGE_MISMATCH, 0, 0);
                if (!logInvoiceReconRecordList.isEmpty()) {
                    invoiceReconRecordHome.removeLogInvoiceReconRecordList(logInvoiceReconRecordList);
                }

                if (providerTotalCharge.compareTo(correctTotalCharge) == 1) {
                    BigDecimal selisih = correctTotalCharge.subtract(providerTotalCharge).abs();
                    if (selisih.compareTo(totalChargeTolerance) <= 0) {
                        _log.debug("selisih is less than or equal to tolerance, so don't set to mismatch");
                        invoiceAWBRecord.setApprovedTotalCharge(providerTotalCharge);
                    } else {
                        _log.debug("Total charge mismatch found for AWB invoice:" + mtaAirwayBill.getAirwayBill().getNumber());

                        if (!firstReconAttemp) {
                            reRecon = reRecon.concat(reRecon.equals("") ? "total charge" : ", total charge").concat("(" + initialValue + " -> " + providerTotalCharge + ")");
                        }

                        LogInvoiceReconRecord logInvoiceReconRecord = new LogInvoiceReconRecord();

                        logInvoiceReconRecord.setLogInvoiceAirwaybillRecord(invoiceAWBRecord);
                        logInvoiceReconRecord.setComment("Venice identified a total charges mismatch");

                        LogReconInvoiceRecordResult logReconInvoiceRecordResult = new LogReconInvoiceRecordResult();
                        logReconInvoiceRecordResult.setReconRecordResultId(new Long(VeniceConstants.LOG_INVOICE_RECON_RESULT_TOTAL_CHARGE_MISMATCH));

                        logInvoiceReconRecord.setLogReconInvoiceRecordResult(logReconInvoiceRecordResult);
                        logInvoiceReconRecord.setProviderData(providerTotalCharge.toString());
                        logInvoiceReconRecord.setVeniceData(correctTotalCharge.toString());

                        // Persist the record
                        invoiceReconRecordHome.persistLogInvoiceReconRecord(logInvoiceReconRecord);
                        problemExists = true;
                    }
                } else {
                    invoiceAWBRecord.setApprovedTotalCharge(providerTotalCharge);
                }
            }

            if (!reRecon.equals("")) {
                failedRecord.put(awbNumber, "Airwaybill number has been re-uploaded with changes: " + reRecon);
            }

            String resultStatus = invoiceAWBRecord.getInvoiceResultStatus() == null || invoiceAWBRecord.getInvoiceResultStatus().equals("") ? "OK" : invoiceAWBRecord.getInvoiceResultStatus();
            if (problemExists) {
                resultStatus = "Problem Exists";
            } else {
                ArrayList<LogInvoiceReconRecord> logInvoiceReconRecordList = (ArrayList<LogInvoiceReconRecord>) invoiceReconRecordHome
                        .queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId =" + invoiceAWBRecord.getInvoiceAirwaybillRecordId(), 0, 0);
                if (logInvoiceReconRecordList.isEmpty()) {
                    resultStatus = "OK";
                }
            }

            invoiceAWBRecord.setInvoiceResultStatus(resultStatus);
            invoiceAWBRecord = invoiceAWBRecordHome.mergeLogInvoiceAirwaybillRecord(invoiceAWBRecord);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            //Save the tolerance values for history
            String tolerance = "Reconciled on " + sdf.format(new Date())
                    + " with tolerance values: weight = " + weightTolerance
                    + "; price = " + priceTolerance
                    + "; insurance = " + insuranceTolerance
                    + "; packaging = " + otherChargeTolerance
                    + "; gift wrap = " + giftWrapTolerance;
            logInvoiceReportUpload.setInvoiceReconTolerance(tolerance);
            logInvoiceReportUpload.setReportReconciliationTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
            invoiceReportUploadHome.mergeLogInvoiceReportUpload(logInvoiceReportUpload);

            LogAirwayBill logAirwayBill = null;
            for (int i = 0; i < mtaAirwayBill.getItems().length; i++) {
                List<LogAirwayBill> logAirwayBillList = airwayBillHome.queryByRange("select o from LogAirwayBill o where o.venOrderItem.wcsOrderItemId = '" + mtaAirwayBill.getItems()[i].getOrderItemId() + "'", 0, 0);
                if (logAirwayBillList.size() == 1) {
                    logAirwayBill = logAirwayBillList.get(0);
                } else if (logAirwayBillList.size() > 1) {
                    for (LogAirwayBill logAirwayBill2 : logAirwayBillList) {
                        if (logAirwayBill2.getSequence().toString().equals(mtaAirwayBill.getItems()[i].getGdnRefNo().split("-")[3])) {
                            logAirwayBill = logAirwayBill2;
                            break;
                        }
                    }
                } else {
                    logAirwayBill = null;
                }

                if (logAirwayBill != null) {
                    logAirwayBill.setInvoiceResultStatus(resultStatus);
                    logAirwayBill.setLogInvoiceAirwaybillRecord(invoiceAWBRecord);

                    //Set the correct charges here to pass back to the callerfore
                    logAirwayBill.setTotalCharge(correctTotalCharge);
                    logAirwayBill.setProviderTotalCharge(providerTotalCharge);
                    logAirwayBill.setInvoiceFileNameAndLoc(providerAirwayBill.getInvoiceFileNameAndLoc());
                    logAirwayBill.setAirwayBillTimestamp(providerAirwayBill.getAirwayBillTimestamp());

                    airwayBillHome.mergeLogAirwayBill(logAirwayBill);
                }
            }
        } catch (Exception e) {
            String errMsg = "An exception occured when performing invoice reconciliation for an airway bill";
            _log.error(errMsg + e.getMessage(), e);
            e.printStackTrace();
            throw new ServletException(errMsg);
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

    /**
     * Returns true if a settlement record exists for an order item. This is how
     * we can determine if CX has been sent by MTA or not.
     *
     * @param orderItemId
     * @return
     * @throws ServletException
     */
    @SuppressWarnings("unused")
    private Boolean settlementRecordExists(Long orderItemId)
            throws ServletException {
        Locator<Object> locator = null;
        try {
            locator = new Locator<Object>();

            VenSettlementRecordSessionEJBRemote settlementRecordHome = (VenSettlementRecordSessionEJBRemote) locator
                    .lookup(VenSettlementRecordSessionEJBRemote.class, "VenSettlementRecordSessionEJBBean");

            List<VenSettlementRecord> venSettlementRecordList = settlementRecordHome
                    .queryByRange("select o from VenSettlementRecord o where o.venOrderItem.orderItemId = " + orderItemId, 0, 0);
            if (venSettlementRecordList == null || venSettlementRecordList.isEmpty()) {
                return Boolean.FALSE;
            }

        } catch (Exception e) {
            String errMsg = "An exception occured when looking up settlement records for an order item";
            _log.error(errMsg + e.getMessage(), e);
            e.printStackTrace();
            throw new ServletException(errMsg);
        } finally {
            try {
                if (locator != null) {
                    locator.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Boolean.TRUE;
    }

    //compare settlement code, ignore tanda titik (.), strip (-), ataupun spasi ( ) ketika compare
    private Boolean compareSettlementCode(String mtaData, String providerData) {
        _log.debug("compare settlement code");
        _log.debug("original mtaData: " + mtaData);
        _log.debug("original providerData: " + providerData);

        try {
            mtaData = mtaData.replace(".", "").replace("-", "").replace(" ", "");
        } catch (Exception e) {
            _log.error("Problem parsing MTA Data", e);

            mtaData = "";
        }

        try {
            providerData = providerData.replace(".", "").replace("-", "").replace(" ", "");
        } catch (Exception e) {
            _log.error("Problem parsing Provider Data", e);

            providerData = "";
        }

        _log.debug("edited mtaData: " + mtaData);
        _log.debug("edited providerData: " + providerData);

        if (providerData.equalsIgnoreCase(mtaData)) {
            _log.debug("settlement code match");
            return Boolean.TRUE;
        } else {
            _log.debug("compare settlement code not match");
            return Boolean.FALSE;
        }
    }
}
