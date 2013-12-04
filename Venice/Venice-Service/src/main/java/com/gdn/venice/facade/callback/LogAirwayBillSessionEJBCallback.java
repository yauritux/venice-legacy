/**
 * 
 */
package com.gdn.venice.facade.callback;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.ejb.EntityManagerImpl;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinSalesRecordSessionEJBLocal;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBLocal;
import com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBLocal;
import com.gdn.venice.facade.LogActivityReportUploadSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBLocal;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBLocal;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBLocal;
import com.gdn.venice.facade.LogProviderAgreementSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBLocal;
import com.gdn.venice.facade.VenPartyPromotionShareSessionEJBLocal;
import com.gdn.venice.facade.VenSettlementRecordSessionEJBLocal;
import com.gdn.venice.facade.VenTransactionFeeSessionEJBLocal;
import com.gdn.venice.facade.kpi.KPI_TransactionPosterSessionEJBLocal;
import com.gdn.venice.facade.util.KpiPeriodUtil;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriod;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriodPK;
import com.gdn.venice.persistence.KpiPartyPeriodActual;
import com.gdn.venice.persistence.KpiPartyPeriodActualPK;
import com.gdn.venice.persistence.KpiPartyPeriodTransaction;
import com.gdn.venice.persistence.LogActivityReportUpload;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.gdn.venice.persistence.LogProviderAgreement;
import com.gdn.venice.persistence.LogReportStatus;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderItemStatusHistoryPK;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenPartyPromotionShare;
import com.gdn.venice.persistence.VenSettlementRecord;
import com.gdn.venice.persistence.VenTransactionFee;
import com.gdn.venice.util.VeniceConstants;

/**
 * LogAirwayBillSessionEJBCallback.java
 * 
 * This callback object is used to implement creation of sales records 
 * and clock KPI's etc. upon the approval of airway bills.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class LogAirwayBillSessionEJBCallback implements SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public LogAirwayBillSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.LogAirwayBillSessionEJBCallback");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPrePersist(java.lang
	 * .Object)
	 */
	@Override
	public Boolean onPrePersist(Object businessObject) {
		_log.debug("onPrePersist");
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPostPersist(java.lang
	 * .Object)
	 */
	@Override
	public Boolean onPostPersist(Object businessObject) {
		_log.debug("onPostPersist");
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPreMerge(java.lang.Object
	 * )
	 */
	public Boolean onPreMerge(Object businessObject, EntityManager em, LogAirwayBill existingAirwayBill) {
		_log.debug("onPreMerge");
		LogAirwayBill logAirwayBill = (LogAirwayBill) businessObject;

		Locator<Object> locator = null;
		Connection conn = null;
		PreparedStatement psAirwayBill = null;
		ResultSet rsAirwayBill = null;
		try {
			locator = new Locator<Object>();

//			try{
//				/*
//				 * Clock the KPI transactions for the Logistics provider o
//				 * Logistics Pickup Performance o Logistics Delivery Performance
//				 * o Invoice Accuracy Note that KPI's are only clocked once
//				 * based on flags
//				 */
//				if (this.clockLogisticsPickupPerformance(locator, logAirwayBill)) {
//					logAirwayBill.setKpiPickupPerfClocked(true);
//				} else {
					logAirwayBill.setKpiPickupPerfClocked(false);
//				}
//
//				if (this.clockLogisticsDeliveryPerformance(locator,
//						logAirwayBill)) {
//					logAirwayBill.setKpiDeliveryPerfClocked(true);
//				} else {
					logAirwayBill.setKpiDeliveryPerfClocked(false);
//				}
//
//				if (this.clockLogisticsInvoiceAccuracy(locator, logAirwayBill)) {
//					logAirwayBill.setKpiInvoiceAccuracyClocked(true);
//				} else {
					logAirwayBill.setKpiInvoiceAccuracyClocked(false);
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			
			conn =  (Connection) ((EntityManagerImpl)em).getSession().connection();
			/*
			 * If the invoice approval status is approved for the first time
			 */
			
			String airwayBillSQL = "select ab.*, " +
					                                       "oi.order_status_id " +
					                            "from " +
					                            	"log_airway_bill ab " +
					                            "inner join ven_order_item oi on oi.order_item_id = ab.order_item_id " +
					                            "where ab.airway_bill_id = ?";
			psAirwayBill = conn.prepareStatement(airwayBillSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			psAirwayBill.setLong(1, logAirwayBill.getAirwayBillId());
			
			rsAirwayBill = psAirwayBill.executeQuery();
						
			rsAirwayBill.next();
			
			existingAirwayBill.setAirwayBillId(rsAirwayBill.getLong("airway_bill_id"));
			existingAirwayBill.setActivityApprovedByUserId(rsAirwayBill.getString("activity_approved_by_user_id"));
			existingAirwayBill.setActivityFileNameAndLoc(rsAirwayBill.getString("activity_file_name_and_loc"));
			existingAirwayBill.setActivityResultStatus(rsAirwayBill.getString("activity_result_status"));
			existingAirwayBill.setActualPickupDate(rsAirwayBill.getDate("actual_pickup_date"));
			existingAirwayBill.setAddress(rsAirwayBill.getString("address"));
			existingAirwayBill.setAirwayBillNumber(rsAirwayBill.getString("airway_bill_number"));
			existingAirwayBill.setAirwayBillPickupDateTime(rsAirwayBill.getTimestamp("airway_bill_pickup_date_time"));
			existingAirwayBill.setAirwayBillTimestamp(rsAirwayBill.getTimestamp("airway_bill_timestamp"));
			existingAirwayBill.setConsignee(rsAirwayBill.getString("consignee"));
			existingAirwayBill.setContactPerson(rsAirwayBill.getString("contact_person"));
			existingAirwayBill.setContent(rsAirwayBill.getString("content"));
			existingAirwayBill.setDateOfReturn(rsAirwayBill.getDate("date_of_return"));
			existingAirwayBill.setDeliveryOrder(rsAirwayBill.getString("delivery_order"));
			existingAirwayBill.setDestCode(rsAirwayBill.getString("dest_code"));
			existingAirwayBill.setDestination(rsAirwayBill.getString("destination"));
			existingAirwayBill.setGdnReference(rsAirwayBill.getString("gdn_reference"));
			existingAirwayBill.setGiftWrapCharge(rsAirwayBill.getBigDecimal("gift_wrap_charge"));
			existingAirwayBill.setInsuranceCharge(rsAirwayBill.getBigDecimal("insurance_charge"));
			existingAirwayBill.setInsuredAmount(rsAirwayBill.getBigDecimal("insured_amount"));
			existingAirwayBill.setInvoiceApprovedByUserId(rsAirwayBill.getString("invoice_approved_by_user_id"));
			existingAirwayBill.setInvoiceFileNameAndLoc(rsAirwayBill.getString("invoice_file_name_and_loc"));
			existingAirwayBill.setInvoiceResultStatus(rsAirwayBill.getString("invoice_result_status"));
			existingAirwayBill.setMtaData(rsAirwayBill.getBoolean("mta_data"));
			existingAirwayBill.setNoteReturn(rsAirwayBill.getString("note_return"));
			existingAirwayBill.setNoteUndelivered(rsAirwayBill.getString("note_undelivered"));
			existingAirwayBill.setNumPackages(rsAirwayBill.getInt("num_packages"));
			existingAirwayBill.setOrigin(rsAirwayBill.getString("origin"));
			existingAirwayBill.setOtherCharge(rsAirwayBill.getBigDecimal("other_charge"));
			existingAirwayBill.setPackageWeight(rsAirwayBill.getBigDecimal("package_weight"));
			existingAirwayBill.setPricePerKg(rsAirwayBill.getBigDecimal("price_per_kg"));
			existingAirwayBill.setProviderTotalCharge(rsAirwayBill.getBigDecimal("provider_total_charge"));
			existingAirwayBill.setReceived(rsAirwayBill.getDate("received"));
			existingAirwayBill.setRecipient(rsAirwayBill.getString("recipient"));
			existingAirwayBill.setRelation(rsAirwayBill.getString("relation"));
			existingAirwayBill.setReturn_(rsAirwayBill.getString("return"));
			existingAirwayBill.setService(rsAirwayBill.getString("service"));
			existingAirwayBill.setShipper(rsAirwayBill.getString("shipper"));
			existingAirwayBill.setStatus(rsAirwayBill.getString("status"));
			existingAirwayBill.setTariff(rsAirwayBill.getString("tariff"));
			existingAirwayBill.setTotalCharge(rsAirwayBill.getBigDecimal("total_charge"));
			existingAirwayBill.setTrackingNumber(rsAirwayBill.getString("tracking_number"));
			existingAirwayBill.setType(rsAirwayBill.getString("type"));
			existingAirwayBill.setUndelivered(rsAirwayBill.getDate("undelivered"));
			existingAirwayBill.setZip(rsAirwayBill.getString("zip"));
			existingAirwayBill.setKpiPickupPerfClocked(false);
			existingAirwayBill.setKpiDeliveryPerfClocked(false);
			existingAirwayBill.setKpiInvoiceAccuracyClocked(false);
			
			LogApprovalStatus logApprovalStatus1 = new LogApprovalStatus();
			logApprovalStatus1.setApprovalStatusId(rsAirwayBill.getLong("invoice_approval_status_id"));
			existingAirwayBill.setLogApprovalStatus1(logApprovalStatus1);
				
			LogApprovalStatus logApprovalStatus2 = new LogApprovalStatus();
			logApprovalStatus2.setApprovalStatusId(rsAirwayBill.getLong("activity_approval_status_id"));
			existingAirwayBill.setLogApprovalStatus2(logApprovalStatus2);
			
			LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
			logLogisticsProvider.setLogisticsProviderId(rsAirwayBill.getLong("logistics_provider_id"));
			existingAirwayBill.setLogLogisticsProvider(logLogisticsProvider);
			
//			VenDistributionCart distributionCart = new VenDistributionCart();
//			distributionCart.setDistributionCartId(rsAirwayBill.getLong("distribution_cart_id"));
//			existingAirwayBill.setVenDistributionCart(distributionCart);
				
			VenOrderItem orderItem = new VenOrderItem();
			orderItem.setOrderItemId(rsAirwayBill.getLong("order_item_id"));
			
			VenOrderStatus orderStatus = new VenOrderStatus();
			orderStatus.setOrderStatusId(rsAirwayBill.getLong("order_status_id"));
			orderItem.setVenOrderStatus(orderStatus);
			
			existingAirwayBill.setVenOrderItem(orderItem);
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a preMerge callback for LogAirwayBillSessionEJBCallback:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			if(locator != null){
				try{
				locator.close();
				}catch(Exception e){
					String errMsg = "An exception occured closing the locator:" + e.getMessage();
					e.printStackTrace();
					_log.error(errMsg);
				}
			}
			try {
				if(rsAirwayBill != null)
					rsAirwayBill.close();
				if(psAirwayBill != null)
					psAirwayBill.close();
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return Boolean.TRUE;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPostMerge(java.lang.
	 * Object)
	 */
	
	public Boolean onPostMerge(Object businessObject, EntityManager em, LogAirwayBill existingAirwayBill) {
		_log.debug("onPostMerge");
		
		/*
		 * A couple of things to do on post merge if the various status'
		 * for the airway bill have changed.
		 * o Create or update the sales record (both activity and invoice approved)
		 * o Update the status of the activity and/or invoice report
		 */

		LogAirwayBill logAirwayBill = (LogAirwayBill) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			LogAirwayBillSessionEJBRemote logAirwayBillHome = (LogAirwayBillSessionEJBRemote) locator
					.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");

			LogInvoiceReportUploadSessionEJBRemote logInvoiceReportUploadHome = (LogInvoiceReportUploadSessionEJBRemote) locator
					.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");

			LogActivityReportUploadSessionEJBRemote logActivityReportUploadHome = (LogActivityReportUploadSessionEJBRemote) locator
					.lookup(LogActivityReportUploadSessionEJBRemote.class, "LogActivityReportUploadSessionEJBBean");
						
			/*
			 * If the airway bill invoice approval status changes to approved for the first time
			 * 
			 * Note this is mid-transaction so the existing AWB will not 
			 * yet be modified by this transaction
			 */
			if (logAirwayBill != null && logAirwayBill.getLogApprovalStatus1() != null && (existingAirwayBill.getLogApprovalStatus1().getApprovalStatusId() != VeniceConstants.LOG_APPROVAL_STATUS_APPROVED 
					&& logAirwayBill.getLogApprovalStatus1().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_APPROVED)) {

				/*
				 * Check to see if all the airway bills for the invoice report
				 * are approved If they are all approved then set the status of
				 * the invoice report to reconciled
				 * 
				 * This will trigger the logistics debt acknowledgement journal
				 * creation and the invoice creation.
				 */

				List<LogAirwayBill> airwayBillList = logAirwayBillHome.queryByRange("select o from LogAirwayBill o where o.invoiceFileNameAndLoc = '"
								+ logAirwayBill.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getFileNameAndLocation() + "' and o.logApprovalStatus1.approvalStatusId <> " + VeniceConstants.LOG_APPROVAL_STATUS_APPROVED, 0, 0);
				if (airwayBillList.size() < 1) {
					List<LogInvoiceReportUpload> logInvoiceReportUploadList = logInvoiceReportUploadHome
							.queryByRange("select o from LogInvoiceReportUpload o where o.fileNameAndLocation = '" + logAirwayBill.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getFileNameAndLocation() + "'", 0, 0);
					if (logInvoiceReportUploadList != null && !logInvoiceReportUploadList.isEmpty()) {

						LogInvoiceReportUpload logInvoiceReportUpload = logInvoiceReportUploadList.get(0);

						LogReportStatus logReportStatus = new LogReportStatus();
						logReportStatus.setReportStatusId(VeniceConstants.LOG_REPORT_STATUS_RECONCILED);
						logInvoiceReportUpload.setLogReportStatus(logReportStatus);

						logInvoiceReportUploadHome.mergeLogInvoiceReportUpload(logInvoiceReportUpload);
						
					}
				}
			}

			/**
			 * this portion of code and all that are related to are moved to LogActivityReportMDB
			 */
//			if((existingAirwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_PP || existingAirwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_ES ) && 
//				logAirwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_CX){
//				_log.info("order item status changes to CX for the first time, create sales record");
//				this.createOrUpdateSalesRecord(locator, logAirwayBill);
//
//				/**
//				 * add reconcile date in sales record
//				 */
//				List<FinSalesRecord> finSalesRecords = finSalesRecordHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.orderItemId = " + logAirwayBill.getVenOrderItem().getOrderItemId(), 0, 0);
//				
//				if(finSalesRecords.size() == 0){
//					throw new EJBException("Cannot find FinSalesRecord with order_item_id = " + logAirwayBill.getVenOrderItem().getOrderItemId());
//				}
//				
//				Date now = new Date();
//				FinSalesRecord finSalesRecord = finSalesRecords.get(0);
//				finSalesRecord.setReconcileDate(new Timestamp(now.getTime()));
//				
//				finSalesRecordHome.mergeFinSalesRecord(finSalesRecord);
//			}
				
			/*
			 * If the activity approval status changes to approved
			 */
			if (logAirwayBill != null && logAirwayBill.getLogApprovalStatus2() != null && logAirwayBill.getLogApprovalStatus2().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_APPROVED) {
				
				//add order item status history when activity report is approved for the first time as CX Finance
				if (logAirwayBill != null && logAirwayBill.getLogApprovalStatus2() != null && (existingAirwayBill.getLogApprovalStatus2().getApprovalStatusId() != VeniceConstants.LOG_APPROVAL_STATUS_APPROVED 
						&& logAirwayBill.getLogApprovalStatus2().getApprovalStatusId() == VeniceConstants.LOG_APPROVAL_STATUS_APPROVED)) {
					_log.debug("activity report is approved for the first time, add order item status history for CX Finance");
					locator = new Locator<Object>();
					VenOrderItemStatusHistorySessionEJBRemote orderItemHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
					.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");
					
					VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
					venOrderItemStatusHistoryPK.setOrderItemId(logAirwayBill.getVenOrderItem().getOrderItemId());
					
					Timestamp cxFinanceDate = new Timestamp(System.currentTimeMillis());
					venOrderItemStatusHistoryPK.setHistoryTimestamp(cxFinanceDate);
					
					VenOrderItemStatusHistory orderItemStatusHistory = new VenOrderItemStatusHistory();
					orderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
					orderItemStatusHistory.setStatusChangeReason("Updated by System (CX Finance)");
					
					VenOrderStatus statusCX = new VenOrderStatus();
					statusCX.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_CX);
					statusCX.setOrderStatusCode("CX");
					orderItemStatusHistory.setVenOrderStatus(statusCX);
					
					orderItemHistorySessionHome.persistVenOrderItemStatusHistory(orderItemStatusHistory);	
					_log.debug("done add order item status history for CX Finance");
					
					//add cx finance date to sales record
					FinSalesRecordSessionEJBRemote salesRecordSessionHome = (FinSalesRecordSessionEJBRemote) locator
					.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
					
					List<FinSalesRecord> finSalesRecords = salesRecordSessionHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.orderItemId = " + logAirwayBill.getVenOrderItem().getOrderItemId(), 0, 1);
					
					if(finSalesRecords.size()>0){
						_log.debug("sales record found, add CX Finance date in sales record");
						FinSalesRecord finSalesRecord=finSalesRecords.get(0);
						finSalesRecord.setCxFinanceDate(cxFinanceDate);
						
						salesRecordSessionHome.mergeFinSalesRecord(finSalesRecord);
					}					
				}
				
				/*
				 * Check to see if all the airway bills for the activity report
				 * are approved If they are all approved then set the status of
				 * the activity report to reconciled
				 */

				List<LogAirwayBill> airwayBillList = logAirwayBillHome.queryByRange("select o from LogAirwayBill o where o.activityFileNameAndLoc = '"
					+ logAirwayBill.getActivityFileNameAndLoc() + "' and o.logApprovalStatus2.approvalStatusId <> " + VeniceConstants.LOG_APPROVAL_STATUS_APPROVED, 0, 0);
				if (airwayBillList.size() <= 1) {
					List<LogActivityReportUpload> logActivityReportUploadList = logActivityReportUploadHome
							.queryByRange("select o from LogActivityReportUpload o where o.fileNameAndLocation = '" + logAirwayBill.getActivityFileNameAndLoc() + "'", 0, 0);

					if (logActivityReportUploadList != null && !logActivityReportUploadList.isEmpty()) {
						LogActivityReportUpload logActivityReportUpload = logActivityReportUploadList.get(0);

						LogReportStatus logReportStatus = new LogReportStatus();
						logReportStatus.setReportStatusId(VeniceConstants.LOG_REPORT_STATUS_RECONCILED);
						logActivityReportUpload.setLogReportStatus(logReportStatus);

						logActivityReportUploadHome.mergeLogActivityReportUpload(logActivityReportUpload);
					}
				}
			}
			
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a postMerge callback for LogAirwayBillSessionEJBCallback:" + e.getMessage();
			_log.error(errMsg, e);
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			if(locator != null){
				try{
				locator.close();
				}catch(Exception e){
					String errMsg = "An exception occured closing the locator:" + e.getMessage();
					e.printStackTrace();
					_log.error(errMsg);
				}
			}
		}
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPreRemove(java.lang.
	 * Object)
	 */
	@Override
	public Boolean onPreRemove(Object businessObject) {
		_log.debug("onPreRemove");
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPostRemove(java.lang
	 * .Object)
	 */
	@Override
	public Boolean onPostRemove(Object businessObject) {
		_log.debug("onPostRemove");
		return Boolean.TRUE;
	}
	
	/**
	 * createOrUpdateSalesRecord - creates or updates the sales record
	 * If the sales record associated with the airway bill does not 
	 * yet exist then create it.If it already exists then recalculate 
	 * it and update it.
	 * 
	 * @param logAirwayBill
	 * @return true if the operation succeeds else false
	 */
	private Boolean createOrUpdateSalesRecord(Locator<Object> locator, LogAirwayBill logAirwayBill) throws Exception{

		FinSalesRecordSessionEJBRemote salesRecordHome = (FinSalesRecordSessionEJBRemote) locator
				.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
				
		FinSalesRecord finSalesRecord = new FinSalesRecord();
		finSalesRecord.setVenOrderItem(logAirwayBill.getVenOrderItem());
		

		// Calculate the sales record
		finSalesRecord = this.calculateSalesRecord(locator, finSalesRecord, logAirwayBill);
		FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
		finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
		finSalesRecord.setFinApprovalStatus(finApprovalStatus);

		/*
		 * If the sales record for the order item on the AWB does not
		 * exist then create it. If the sales record exists already then
		 * update it
		 */
		List<FinSalesRecord> finSalesRecordList = salesRecordHome
				.queryByRange("select o from FinSalesRecord o where o.venOrderItem.orderItemId = " + logAirwayBill.getVenOrderItem().getOrderItemId(), 0, 0);
		if (finSalesRecordList.isEmpty()) {
			_log.info("create new sales record");
			finSalesRecord.setSalesTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
			finSalesRecord = salesRecordHome.persistFinSalesRecord(finSalesRecord);
			_log.debug("finSalesRecord => " + finSalesRecord.getSalesRecordId());
			/*
			 * Set the sales record immediately to approved and merge it
			 * This will trigger the sales journal creation
			 * 
			 * The reason we are doing this is that there are already
			 * too many business processes for the user (approvals etc.)
			 * 
			 * Later if GDN want a business process for this then it can
			 * easy be added
			 */
			finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
			finSalesRecord.setFinApprovalStatus(finApprovalStatus);
			salesRecordHome.mergeFinSalesRecord(finSalesRecord);
			return true;

		} else {
			FinSalesRecord existingFinSalesRecord = finSalesRecordList.get(0);
			/*
			 * To ensure that the sales record is only updated to
			 * approved status once
			 */
			if(existingFinSalesRecord.getFinApprovalStatus().getApprovalStatusId() == VeniceConstants.FIN_APPROVAL_STATUS_APPROVED){
				_log.info("existing sales record already approved");
				return true;
			}
			_log.info("update existing sales record");
			existingFinSalesRecord.setCustomerDownpayment(finSalesRecord.getCustomerDownpayment());
			existingFinSalesRecord.setFinApprovalStatus(finApprovalStatus);
			existingFinSalesRecord.setGdnCommissionAmount(finSalesRecord.getGdnCommissionAmount());
			existingFinSalesRecord.setGdnGiftWrapChargeAmount(finSalesRecord.getGdnGiftWrapChargeAmount());
			existingFinSalesRecord.setGdnHandlingFeeAmount(finSalesRecord.getGdnHandlingFeeAmount());
			existingFinSalesRecord.setGdnPromotionAmount(finSalesRecord.getGdnPromotionAmount());
			existingFinSalesRecord.setGdnTransactionFeeAmount(finSalesRecord.getGdnTransactionFeeAmount());
			existingFinSalesRecord.setMerchantPaymentAmount(finSalesRecord.getMerchantPaymentAmount());
			existingFinSalesRecord.setMerchantPromotionAmount(finSalesRecord.getMerchantPromotionAmount());
			existingFinSalesRecord.setThirdPartyPromotionAmount(finSalesRecord.getThirdPartyPromotionAmount());
			existingFinSalesRecord.setTotalLogisticsRelatedAmount(finSalesRecord.getTotalLogisticsRelatedAmount());
			existingFinSalesRecord.setVatAmount(finSalesRecord.getVatAmount());
			salesRecordHome.mergeFinSalesRecord(existingFinSalesRecord);

			/*
			 * Set the sales record immediately to approved and merge it
			 * This will trigger the sales journal creation
			 *    Same reasons as for persist
			 */
			finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_APPROVED);
			existingFinSalesRecord.setFinApprovalStatus(finApprovalStatus);
			salesRecordHome.mergeFinSalesRecord(existingFinSalesRecord);
			return true;
		}
	}
	

	/**
	 * calculateSalesRecord - Calculates and journals the sales record 
	 * for the order item on the airway bill
	 * 
	 * @param finSalesRecord is the existing sales record
	 * @param logAirwayBill is the airway bill to use for the calculations
	 * @return the completed calculated sales record
	 */
	private FinSalesRecord calculateSalesRecord(Locator<Object> locator,
			FinSalesRecord finSalesRecord, LogAirwayBill logAirwayBill) {
		_log.info("\n Start calculate sales record");
		try {
			VenOrderItemSessionEJBLocal orderItemHome = (VenOrderItemSessionEJBLocal) locator
					.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");

			VenOrderItemAdjustmentSessionEJBLocal orderItemAdjustmentHome = (VenOrderItemAdjustmentSessionEJBLocal) locator
					.lookupLocal(VenOrderItemAdjustmentSessionEJBLocal.class, "VenOrderItemAdjustmentSessionEJBBeanLocal");

			VenSettlementRecordSessionEJBLocal settlementRecordHome = (VenSettlementRecordSessionEJBLocal) locator
					.lookupLocal(VenSettlementRecordSessionEJBLocal.class, "VenSettlementRecordSessionEJBBeanLocal");

			/*
			 * Calculate the downpayment amount (payment must have been made
			 * already or the item would not have gone to logistics)
			 * 
			 * Calculate as the catalog price of the item multiplied by the 
			 * quantity plus the shipping (shipping cost plus insurance) plus the
			 * handling fee for the payment (only for the first order item to go
			 * to CX status) minus any adjustment.
			 */
			List<VenOrderItem> itemList = orderItemHome.queryByRange("select o from VenOrderItem o where o.orderItemId = " + logAirwayBill.getVenOrderItem().getOrderItemId(), 0, 0);
			VenOrderItem venOrderItem = itemList.get(0);
			_log.debug("\n Set customer downpayment");
			BigDecimal customerDownpaymentAmount = venOrderItem.getPrice().multiply(new BigDecimal(venOrderItem.getQuantity()));
			customerDownpaymentAmount.setScale(2, RoundingMode.HALF_UP);
			customerDownpaymentAmount = customerDownpaymentAmount.add(venOrderItem.getShippingCost()).add(venOrderItem.getInsuranceCost().add(venOrderItem.getGiftWrapPrice()));
			
			/*
			 * If it is the first order item to go CX then add 
			 * the handling fees to the downpayment amount
			 * JASA HANDLING
			 */
			if(this.isFirstCXOrderItem(locator, venOrderItem)){
				_log.debug("\n Check is first cx order item");
				customerDownpaymentAmount = customerDownpaymentAmount.add(this.getHandlingFeesFromOrderPayments(locator, venOrderItem));
			}			

			List<VenOrderItemAdjustment> adjustmentList = orderItemAdjustmentHome.queryByRange("select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);

			/*
			 *  Get any marginPromo and add them to the
			 *  customer downpayment amount (note
			 *  that they are -ve numbers)
			 */
			_log.debug("\n Get marginPromo");
			BigDecimal marginPromo = new BigDecimal(0);
			marginPromo.setScale(2, RoundingMode.HALF_UP);

			for (VenOrderItemAdjustment adjustment : adjustmentList) {
				marginPromo = marginPromo.add(adjustment.getAmount());
			}

			customerDownpaymentAmount = customerDownpaymentAmount.add(marginPromo);

			/*
			 * Set the downpayment amount in the sales record
			 */
			finSalesRecord.setCustomerDownpayment(customerDownpaymentAmount);

			/*
			 * Get the commission amount from the settlement record
			 *  - to be subtracted from merchant payment after tax taken out
			 */
			_log.debug("\n Check settlement record");
			List<VenSettlementRecord> settlementRecordList = settlementRecordHome
					.queryByRange("select o from VenSettlementRecord o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);
			
			if (settlementRecordList == null || settlementRecordList.isEmpty()) {
				throw new EJBException("No settlement record for order item " + venOrderItem.getOrderItemId() + " was found. Sales record cannot be calculated!");
			}
			
			VenSettlementRecord venSettlementRecord = settlementRecordList.get(0);
			
			/*
			 * This is the divisor to use when calulating the PPN amount
			 */			
			_log.debug("\n Set ppn divisor");
			BigDecimal gdnPPN_Divisor = new BigDecimal(VeniceConstants.VEN_GDN_PPN_RATE).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).add(new BigDecimal(1));
			
			/*
			 * This is the commission amount before the PPN is taken out
			 */
			_log.debug("\n Set commission");
			BigDecimal gdnCommissionAmountBeforeTax = venSettlementRecord.getCommissionValue();
			gdnCommissionAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);

			/*
			 * This is the commission amount after the PPN is taken out
			 */
			BigDecimal gdnCommissionAmountAfterTax = new BigDecimal(0);
			gdnCommissionAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
			if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
				gdnCommissionAmountAfterTax = gdnCommissionAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
			}
			
			/*
			 * This is the amount of PPN incurred by the commission
			 */
			BigDecimal gdnCommissionAmountPPN = gdnCommissionAmountBeforeTax.subtract(gdnCommissionAmountAfterTax);
			
			finSalesRecord.setGdnCommissionAmount(gdnCommissionAmountBeforeTax);
			
			/*
			 * This is the handling fee for the payment from the order
			 * before PPN is taken out.
			 * 
			 * Note that handling fees are only included for the 
			 * first order item to go to CX status (i.e. have a sales record).
			 */
			_log.debug("\n Set handling fee");
			BigDecimal gdnHandlingFeeAmountBeforeTax = new BigDecimal(0);
			gdnHandlingFeeAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);
			
			/*
			 * This is the handling fee after PPN is taken out
			 */
			BigDecimal gdnHandlingFeeAmountAfterTax = new BigDecimal(0);
			gdnHandlingFeeAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
			
			/*
			 * This is the amount of PPN incurred by the handling fees
			 */
			BigDecimal gdnHandlingFeePPN = new BigDecimal(0);
			gdnHandlingFeePPN.setScale(2, RoundingMode.HALF_UP);
			
			Boolean flagFirstOrderItem = isFirstCXOrderItem(locator, venOrderItem);
			_log.debug("\n Check is first CX order item");
			if(flagFirstOrderItem){
				_log.debug("\n Is first CX order item, set handling fee");
				gdnHandlingFeeAmountBeforeTax = getHandlingFeesFromOrderPayments(locator, venOrderItem);
				
				if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
					gdnHandlingFeeAmountAfterTax = gdnHandlingFeeAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
				}
				
				gdnHandlingFeePPN = gdnHandlingFeeAmountBeforeTax.subtract(gdnHandlingFeeAmountAfterTax);
			}
			
			_log.debug("gdnHandlingFeeAmountBeforeTax: "+gdnHandlingFeeAmountBeforeTax);
			finSalesRecord.setGdnHandlingFeeAmount(gdnHandlingFeeAmountBeforeTax);

			/*
			 * These are the transaction fees which are per order and per
			 * merchant (not per order item). 
			 * 
			 * These are applied to the sales record for the first order
			 * item in the order to go CX (i.e. have a sales record)
			 */
			_log.debug("\n Set transaction fee");
			BigDecimal gdnTransactionFeeAmountBeforeTax = new BigDecimal(0);
			gdnTransactionFeeAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);
			
			if(flagFirstOrderItem){
				gdnTransactionFeeAmountBeforeTax = getMerchantTransactionFees(locator, venOrderItem);
			}
						
			BigDecimal gdnTransactionFeeAmountAfterTax = new BigDecimal(0);
			gdnTransactionFeeAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
			
			if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
				gdnTransactionFeeAmountAfterTax = gdnTransactionFeeAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
			}
			BigDecimal gdnTransactionFeeAmountPPN = gdnTransactionFeeAmountBeforeTax.subtract(gdnTransactionFeeAmountAfterTax);
			
			finSalesRecord.setGdnTransactionFeeAmount(gdnTransactionFeeAmountBeforeTax);

			/*
			 * This is the amount of the promotion that is to be borne by the
			 * merchant
			 */
			_log.debug("\n Set promotion");
			BigDecimal merchantPromotionAmount = getMerchantPromotionAmount(locator, venOrderItem, adjustmentList);
			
			merchantPromotionAmount.setScale(2, RoundingMode.HALF_UP);
			finSalesRecord.setMerchantPromotionAmount(merchantPromotionAmount);

			/*
			 * Calculate the total payment amount that is 
			 * due to the merchant for the order item.
			 * 
			 * (item_price * qty) - merchantPromo - gdnCommission (with tax taken out) - gdnHandlingFee (with tax taken out)
			 */
			_log.debug("\n Set merchant payment amount");
			BigDecimal merchantPaymentAmount = venOrderItem.getPrice().multiply(new BigDecimal(venOrderItem.getQuantity()));
			merchantPaymentAmount.setScale(2, RoundingMode.HALF_UP);
			
			merchantPaymentAmount = merchantPaymentAmount.subtract(merchantPromotionAmount).subtract(gdnCommissionAmountBeforeTax).subtract(gdnTransactionFeeAmountBeforeTax);			
			finSalesRecord.setMerchantPaymentAmount(merchantPaymentAmount);

			/*
			 * This is the promotion amount that the 3rd party is accountable
			 * for.
			 */
			_log.debug("\n Set 3rd party promotion");
			BigDecimal thirdPartyPromotionAmount = this.getThirdPartyPromotionAmount(locator, venOrderItem, adjustmentList);

			thirdPartyPromotionAmount.setScale(2, RoundingMode.HALF_UP);
			finSalesRecord.setThirdPartyPromotionAmount(thirdPartyPromotionAmount);
			
			/*
			 * Calculate the GDN amount for the promotion
			 * by subtracting the 3rd party promotion amounts
			 * from the marginPromo
			 */
			_log.debug("\n Set gdn promotion amount");
			BigDecimal gdnPromotionAmount = new BigDecimal(0);
			if(marginPromo.compareTo(new BigDecimal(0)) > 0){
				gdnPromotionAmount = marginPromo.subtract(merchantPromotionAmount).subtract(thirdPartyPromotionAmount);
			}
			
			gdnPromotionAmount.setScale(2, RoundingMode.HALF_UP);
			finSalesRecord.setGdnPromotionAmount(gdnPromotionAmount);

			/*
			 * Calculate the total of the logistics charges and PPN for the order item
			 */
			_log.debug("\n Set logistic charge");
			BigDecimal totalLogisticsRelatedAmountBeforeTax = venOrderItem.getShippingCost().add(venOrderItem.getInsuranceCost());
			totalLogisticsRelatedAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);
			
			BigDecimal totalLogisticsRelatedAmountAfterTax = new BigDecimal(0);
			
			if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
				totalLogisticsRelatedAmountAfterTax = totalLogisticsRelatedAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
			}
			
			totalLogisticsRelatedAmountAfterTax.setScale(2, RoundingMode.HALF_UP);
			BigDecimal totalLogisticsRelatedAmountPPN = totalLogisticsRelatedAmountBeforeTax.subtract(totalLogisticsRelatedAmountAfterTax);
			
			finSalesRecord.setTotalLogisticsRelatedAmount(totalLogisticsRelatedAmountBeforeTax);
			
			/*
			 * Calculate the gift wrap PPN and assign the amounts
			 */
			_log.debug("\n Set gift wrap amount");
			BigDecimal gdnGiftWrapChargeAmountBeforeTax = venOrderItem.getGiftWrapPrice();
			gdnGiftWrapChargeAmountBeforeTax.setScale(2, RoundingMode.HALF_UP);
			
			BigDecimal gdnGiftWrapChargeAmountAfterTax = new BigDecimal(0);
			
			if (VeniceConstants.VEN_GDN_PPN_RATE > 0) {
				gdnGiftWrapChargeAmountAfterTax = gdnGiftWrapChargeAmountBeforeTax.divide(gdnPPN_Divisor, 2, RoundingMode.HALF_UP);
			}
			
			gdnGiftWrapChargeAmountAfterTax.setScale(2, RoundingMode.HALF_UP);			
			BigDecimal gdnGiftWrapChargeAmountPPN = gdnGiftWrapChargeAmountBeforeTax.subtract(gdnGiftWrapChargeAmountAfterTax);

			finSalesRecord.setGdnGiftWrapChargeAmount(gdnGiftWrapChargeAmountBeforeTax);

			/*
			 * Calculates the PPN amount for the sale
			 * by adding the various PPN amounts
			 * 
			 */
			_log.debug("\n Set ppn amount");
			BigDecimal vatAmount = new BigDecimal(0);

			vatAmount = totalLogisticsRelatedAmountPPN.add(gdnCommissionAmountPPN).add(gdnTransactionFeeAmountPPN).add(gdnHandlingFeePPN).add(gdnGiftWrapChargeAmountPPN);
			
			vatAmount.setScale(2, RoundingMode.HALF_UP);
			finSalesRecord.setVatAmount(vatAmount);
			_log.info("\n Done calculate sales record");
		} catch (Exception e) {
			String errMsg = "An exception occured when calculating values for the sales record:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return finSalesRecord;
	}
	
	/**
	 * Clocks the KPI for logistics pickup performance
	 * @param locator
	 * @param logAirwayBill
	 * @return true if the operation succeeds else false
	 */
	private Boolean clockLogisticsPickupPerformance(Locator<Object> locator, LogAirwayBill logAirwayBill){
		/*
		 * Return true if the KPI has already been clocked
		 */
		if(logAirwayBill.getKpiPickupPerfClocked() != null && logAirwayBill.getKpiPickupPerfClocked()){
			return true;
		}
		
		/*
		 * Return false if the airway bill activity is not approved
		 */
		if(logAirwayBill.getLogApprovalStatus2() == null 
				|| (!logAirwayBill.getLogApprovalStatus2().getApprovalStatusId().equals(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED))){
			return false;
		}
		
		/*
		 * Get the pickup time commitment for the logistics provider
		 * Compare the actual pickup performance with the commitment
		 * Clock the pickup performance KPI transaction
		 * Update the period summary for the pickup performance KPI
		 */
		try {
			KpiPartyPeriodTransactionSessionEJBLocal kpiPartyPeriodTransactionHome = (KpiPartyPeriodTransactionSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodTransactionSessionEJBLocal.class, "KpiPartyPeriodTransactionSessionEJBBeanLocal");

			LogProviderAgreementSessionEJBLocal logProviderAgreementHome = (LogProviderAgreementSessionEJBLocal) locator
			.lookupLocal(LogProviderAgreementSessionEJBLocal.class, "LogProviderAgreementSessionEJBBeanLocal");

			LogMerchantPickupInstructionSessionEJBLocal logMerchantPickupInstructionHome = (LogMerchantPickupInstructionSessionEJBLocal) locator
			.lookupLocal(LogMerchantPickupInstructionSessionEJBLocal.class, "LogMerchantPickupInstructionSessionEJBBeanLocal");

			KPI_TransactionPosterSessionEJBLocal kPI_TransactionPosterHome = (KPI_TransactionPosterSessionEJBLocal) locator
			.lookupLocal(KPI_TransactionPosterSessionEJBLocal.class, "KPI_TransactionPosterSessionEJBBeanLocal");

			KpiPartyPeriodActualSessionEJBLocal kpiPartyPeriodActualHome = (KpiPartyPeriodActualSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodActualSessionEJBLocal.class, "KpiPartyPeriodActualSessionEJBBeanLocal");
						
			Long logisticsProviderId = logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderId();
			Long logisticsProviderPartyId = logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getVenParty().getPartyId();
			
			List<LogProviderAgreement> logProviderAgreementList = logProviderAgreementHome.queryByRange("select o from LogProviderAgreement o where o.logLogisticsProvider.logisticsProviderId = " + logisticsProviderId, 0, 0);			
			LogProviderAgreement logProviderAgreement= null;
			
			if(!logProviderAgreementList.isEmpty()){
				/*
				 * Get the current provider agreement
				 */
				for(LogProviderAgreement agreement:logProviderAgreementList){
					Date currentDateTime = new Date();
					if(agreement.getAgreementDate().getTime() < currentDateTime.getTime() && agreement.getExpiryDate().getTime() > currentDateTime.getTime()){
						logProviderAgreement = agreement;
						break;
					}
				}
			}else{
				throw new EJBException("No current logistics provider agreement found for provider: " + logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderCode());
			}
			
			if(logProviderAgreement == null){
				throw new EJBException("Agreement date is already expired for provider: " + logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderCode());
			}
			
			Integer pickupTimeCommitment = logProviderAgreement.getPickupTimeCommitment();
			
			/*
			 * Lookup the pickup instructions
			 */
			
			Long orderItemId = logAirwayBill.getVenOrderItem().getOrderItemId();
			LogMerchantPickupInstruction logMerchantPickupInstruction = null;
			List<LogMerchantPickupInstruction> logMerchantPickupInstructionList = logMerchantPickupInstructionHome.queryByRange("select o from LogMerchantPickupInstruction o where o.venOrderItem.orderItemId = " + orderItemId, 0, 0);
			if(!logMerchantPickupInstructionList.isEmpty()){
				logMerchantPickupInstruction = logMerchantPickupInstructionList.get(0);
			}else{
				throw new EJBException("No pickup instructions found for order item:" + logAirwayBill.getVenOrderItem().getWcsOrderItemId());
			}
						
			Date scheduledPickupDateTime = logMerchantPickupInstruction.getPickupDateTime();			
			Date actualPickupDateTime = logAirwayBill.getActualPickupDate();
			
			/*
			 * If the difference between the scheduled pickup date/time
			 * and the actual pickup date/time differs by a margin
			 * greater than the pickup time commitment then clock a
			 * negative value on the KPI for each day late 
			 * else clock a positive value for being on time.
			 */
			
			Long timeToPickup = actualPickupDateTime.getTime() - scheduledPickupDateTime.getTime();						
			BigDecimal daysToPickup = new BigDecimal(timeToPickup/VeniceConstants.MILLISECONDS_IN_A_DAY);
			daysToPickup.setScale(0, RoundingMode.UP);			
			Integer intDaysToPickup = daysToPickup.intValue();
			
			/*
			 * Get the current KPI period
			 */
			Long currentKpiPeriodId = KpiPeriodUtil.getCurrentPeriod().getKpiPeriodId();
			
			if(intDaysToPickup > pickupTimeCommitment){
				/*
				 * For each day late clock -1
				 */
				for(int i= intDaysToPickup; i > 0; --i){
					kPI_TransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_LOGISTICS_PICKUP_PERFORMANCE, currentKpiPeriodId, logisticsProviderPartyId, -1, "Late pickup for order item:" + logAirwayBill.getVenOrderItem().getWcsOrderItemId());
				}
			}else{
				/*
				 * Clock +1
				 */
				kPI_TransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_LOGISTICS_PICKUP_PERFORMANCE, KpiPeriodUtil.getCurrentPeriod().getKpiPeriodId(), logisticsProviderPartyId, 1, "On time pickup for order item:" + logAirwayBill.getVenOrderItem().getWcsOrderItemId());				
			}
			
			/*
			 * Calculate the percentage for the period actual and
			 * then store it for the provider
			 * 	o read all the transactions into a list
			 *  o sum the penalty transactions (-1's)
			 *  o determine the +ve transactions
			 *  o calculate the integral %ge
			 */
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList =  kpiPartyPeriodTransactionHome.queryByRange("select o from KpiPartyPeriodTransaction o where o.kpiPartyMeasurementPeriod.id.partyId = " + logisticsProviderPartyId + " and o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_LOGISTICS_PICKUP_PERFORMANCE + " and o.kpiPartyMeasurementPeriod.id.kpiPeriodId = " + currentKpiPeriodId, 0, 0);			
			Integer totalTransactions = kpiPartyPeriodTransactionList.size();
			
			Integer penaltyTransactions = 0;
			for(KpiPartyPeriodTransaction transaction:kpiPartyPeriodTransactionList){
				if(transaction.getKpiTransactionValue() < 0){
					++penaltyTransactions;
				}
			}
			
			Integer positiveTransactions = totalTransactions - penaltyTransactions;
			Double dPeriodPercentage = (new Double(positiveTransactions) / new Double(totalTransactions) * 100);
			
			BigDecimal bdPeriodPercentage = new BigDecimal(dPeriodPercentage);
			bdPeriodPercentage.setScale(0, RoundingMode.UP);
			Integer periodPercentage = bdPeriodPercentage.intValue();
			
			/*
			 * If a party period actual record exists then update it else
			 * create a new record
			 */
			
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList = kpiPartyPeriodActualHome.queryByRange("select o from KpiPartyPeriodActual o where o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_LOGISTICS_PICKUP_PERFORMANCE + " and o.kpiPartyMeasurementPeriod.id.partyId = " + logisticsProviderPartyId + " and o.kpiPartyMeasurementPeriod.id.kpiPeriodId = " + currentKpiPeriodId, 0, 0);
			if(kpiPartyPeriodActualList.isEmpty()){
				KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod = new KpiPartyMeasurementPeriod();
				kpiPartyMeasurementPeriod.setVenParty(logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getVenParty());
				KpiPartyMeasurementPeriodPK kpiPartyMeasurementPeriodPK = new KpiPartyMeasurementPeriodPK();
				kpiPartyMeasurementPeriodPK.setKpiPeriodId(currentKpiPeriodId);
				kpiPartyMeasurementPeriodPK.setPartyId(logisticsProviderPartyId);
				kpiPartyMeasurementPeriod.setId(kpiPartyMeasurementPeriodPK);
				
				KpiPartyPeriodActualPK kpiPartyPeriodActualPK = new KpiPartyPeriodActualPK();
				kpiPartyPeriodActualPK.setKpiId(VeniceConstants.KPI_LOGISTICS_PICKUP_PERFORMANCE);
				kpiPartyPeriodActualPK.setKpiPeriodId(currentKpiPeriodId);
				kpiPartyPeriodActualPK.setPartyId(logisticsProviderPartyId);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicator.setKpiId(VeniceConstants.KPI_LOGISTICS_PICKUP_PERFORMANCE);
				
				KpiPartyPeriodActual kpiPartyPeriodActual = new KpiPartyPeriodActual();
				kpiPartyPeriodActual.setId(kpiPartyPeriodActualPK);
				kpiPartyPeriodActual.setKpiCalculatedValue(periodPercentage);
				kpiPartyPeriodActual.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
				kpiPartyPeriodActual.setKpiPartyMeasurementPeriod(kpiPartyMeasurementPeriod);
				
				/*
				 * Persist a new record for the actual
				 */
				kpiPartyPeriodActual = kpiPartyPeriodActualHome.persistKpiPartyPeriodActual(kpiPartyPeriodActual);
				
			}else{
				KpiPartyPeriodActual kpiPartyPeriodActual = kpiPartyPeriodActualList.get(0);
				kpiPartyPeriodActual.setKpiCalculatedValue(periodPercentage);
				
				/*
				 * Merge the existing KPI period actual record
				 */
				kpiPartyPeriodActual = kpiPartyPeriodActualHome.mergeKpiPartyPeriodActual(kpiPartyPeriodActual);
			}
			
		} catch (Exception e) {
			String errMsg = "An exception occured when clocking logistics provider pickup performance:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		}
		
		return true;
	}
	
	/**
	 * Clocks the delivery performance KPI for the logistics provider
	 * @param locator
	 * @param logAirwayBill
	 * @return true if the operation succeeds else false
	 */
	private Boolean clockLogisticsDeliveryPerformance(Locator<Object> locator, LogAirwayBill logAirwayBill){
		/*
		 * Return true if the KPI has already been clocked
		 */
		if(logAirwayBill.getKpiDeliveryPerfClocked() != null && logAirwayBill.getKpiDeliveryPerfClocked()){
			return true;
		}

		/*
		 * Return false if the airway bill activity is not approved
		 */
		if(logAirwayBill.getLogApprovalStatus2() == null || (!logAirwayBill.getLogApprovalStatus2().getApprovalStatusId().equals(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED))){
			return false;
		}
		
		/*
		 * Exit early if the goods have not been delivered
		 */
		if(logAirwayBill.getStatus() == null || !logAirwayBill.getStatus().equals(VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD)){
			return false;
		}
		
		/*
		 * Check that airway bills with status POD have a received date
		 */
		if(logAirwayBill.getReceived() == null){
			_log.info("Invalid data found... order item is status " + VeniceConstants.LOG_LOGISTICS_STATUS_CODE_POD + " but the \"received\" field of the airway bill is null... cannot generate delivery performance KPI!");
			return false;
		}

		/*
		 * Get the delivery time commitment for the logistics provider
		 * Compare the actual delivery performance with the commitment
		 * Clock the delivery performance KPI transaction
		 * Update the period summary for the delivery performance KPI
		 */
		try {
			KpiPartyPeriodTransactionSessionEJBLocal kpiPartyPeriodTransactionHome = (KpiPartyPeriodTransactionSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodTransactionSessionEJBLocal.class, "KpiPartyPeriodTransactionSessionEJBBeanLocal");

			LogProviderAgreementSessionEJBLocal logProviderAgreementHome = (LogProviderAgreementSessionEJBLocal) locator
			.lookupLocal(LogProviderAgreementSessionEJBLocal.class, "LogProviderAgreementSessionEJBBeanLocal");

			KPI_TransactionPosterSessionEJBLocal kPI_TransactionPosterHome = (KPI_TransactionPosterSessionEJBLocal) locator
			.lookupLocal(KPI_TransactionPosterSessionEJBLocal.class, "KPI_TransactionPosterSessionEJBBeanLocal");

			KpiPartyPeriodActualSessionEJBLocal kpiPartyPeriodActualHome = (KpiPartyPeriodActualSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodActualSessionEJBLocal.class, "KpiPartyPeriodActualSessionEJBBeanLocal");
			
			Long logisticsProviderId = logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderId();
			Long logisticsProviderPartyId = logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getVenParty().getPartyId();
			
			List<LogProviderAgreement> logProviderAgreementList = logProviderAgreementHome.queryByRange("select o from LogProviderAgreement o where o.logLogisticsProvider.logisticsProviderId = " + logisticsProviderId, 0, 0);			
			LogProviderAgreement logProviderAgreement= null;
			
			if(!logProviderAgreementList.isEmpty()){
				/*
				 * Get the current provider agreement
				 */
				for(LogProviderAgreement agreement:logProviderAgreementList){
					Date currentDateTime = new Date();
					if(agreement.getAgreementDate().getTime() < currentDateTime.getTime() && agreement.getExpiryDate().getTime() > currentDateTime.getTime()){
						logProviderAgreement = agreement;
						break;
					}
				}
			}else{
				throw new EJBException("No current logistics provider agreement not found for provider: " + logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getLogisticsProviderCode());
			}
			
			Date pickupDateTime = logAirwayBill.getActualPickupDate();
			Integer deliveryTimeCommitment = logProviderAgreement.getDeliveryTimeCommitment();
			
			/*
			 * Add the delivery time commitment to the pickup date
			 * to get the scheduled delivery date
			 */
			Calendar scheduledDeliveryCal = Calendar.getInstance();
			scheduledDeliveryCal.setTime(pickupDateTime);
			scheduledDeliveryCal.add(Calendar.DATE, deliveryTimeCommitment);
						
			Date scheduledDeliveryDateTime = scheduledDeliveryCal.getTime();
			
			/*
			 * Get the current KPI period
			 */
			Long currentKpiPeriodId = KpiPeriodUtil.getCurrentPeriod().getKpiPeriodId();
			
			/*
			 * If the actual delivery date is > than the scheduled
			 * delivery date then determine the number of days late.
			 */			
			Long deliveryTimeDifference = logAirwayBill.getReceived().getTime() - scheduledDeliveryDateTime.getTime();
			
			if(deliveryTimeDifference > 0){
				BigDecimal deliveryDaysOverSchedule = new BigDecimal(deliveryTimeDifference/VeniceConstants.MILLISECONDS_IN_A_DAY);
				deliveryDaysOverSchedule.setScale(0, RoundingMode.UP);
				
				Integer integerDeliveryDaysOverSchedule = deliveryDaysOverSchedule.intValue(); 
				
				/*
				 * For each day late clock -1
				 */
				for(int i= integerDeliveryDaysOverSchedule; i > 0; --i){
					kPI_TransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_LOGISTICS_DELIVERY_PERFORMANCE, currentKpiPeriodId, logisticsProviderPartyId, -1, "Late delivery for order item:" + logAirwayBill.getVenOrderItem().getWcsOrderItemId());
				}

			}else{
				/*
				 * Clock +1
				 */
				kPI_TransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_LOGISTICS_DELIVERY_PERFORMANCE, KpiPeriodUtil.getCurrentPeriod().getKpiPeriodId(), logisticsProviderPartyId, 1, "On time delivery for order item:" + logAirwayBill.getVenOrderItem().getWcsOrderItemId());				
			}
						
			/*
			 * Calculate the percentage for the period actual and
			 * then store it for the provider
			 * 	o read all the transactions into a list
			 *  o sum the penalty transactions (-1's)
			 *  o determine the +ve transactions
			 *  o calculate the integral %ge
			 */
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList =  kpiPartyPeriodTransactionHome.queryByRange("select o from KpiPartyPeriodTransaction o where o.kpiPartyMeasurementPeriod.venParty.partyId = " + logisticsProviderPartyId + " and o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_LOGISTICS_DELIVERY_PERFORMANCE + " and o.kpiPartyMeasurementPeriod.id.kpiPeriodId = " + currentKpiPeriodId, 0, 0);
			
			Integer totalTransactions = kpiPartyPeriodTransactionList.size();
			
			Integer penaltyTransactions = 0;
			for(KpiPartyPeriodTransaction transaction:kpiPartyPeriodTransactionList){
				if(transaction.getKpiTransactionValue() < 0){
					++penaltyTransactions;
				}
			}
			
			Integer positiveTransactions = totalTransactions - penaltyTransactions;
			Double dPeriodPercentage = (new Double(positiveTransactions) / new Double(totalTransactions) * 100);
			
			BigDecimal bdPeriodPercentage = new BigDecimal(dPeriodPercentage);
			bdPeriodPercentage.setScale(0, RoundingMode.UP);
			Integer periodPercentage = bdPeriodPercentage.intValue();
			
			/*
			 * If a party period actual record exists then update it else
			 * create a new record
			 */
			
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList = kpiPartyPeriodActualHome.queryByRange("select o from KpiPartyPeriodActual o where o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_LOGISTICS_DELIVERY_PERFORMANCE + " and o.kpiPartyMeasurementPeriod.id.partyId = " + logisticsProviderPartyId + " and o.kpiPartyMeasurementPeriod.id.kpiPeriodId = " + currentKpiPeriodId, 0, 0);
			if(kpiPartyPeriodActualList.isEmpty()){
				KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod = new KpiPartyMeasurementPeriod();
				kpiPartyMeasurementPeriod.setVenParty(logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getVenParty());
				KpiPartyMeasurementPeriodPK kpiPartyMeasurementPeriodPK = new KpiPartyMeasurementPeriodPK();
				kpiPartyMeasurementPeriodPK.setKpiPeriodId(currentKpiPeriodId);
				kpiPartyMeasurementPeriodPK.setPartyId(logisticsProviderPartyId);
				kpiPartyMeasurementPeriod.setId(kpiPartyMeasurementPeriodPK);
				
				KpiPartyPeriodActualPK kpiPartyPeriodActualPK = new KpiPartyPeriodActualPK();
				kpiPartyPeriodActualPK.setKpiId(VeniceConstants.KPI_LOGISTICS_DELIVERY_PERFORMANCE);
				kpiPartyPeriodActualPK.setKpiPeriodId(currentKpiPeriodId);
				kpiPartyPeriodActualPK.setPartyId(logisticsProviderPartyId);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicator.setKpiId(VeniceConstants.KPI_LOGISTICS_DELIVERY_PERFORMANCE);
				
				KpiPartyPeriodActual kpiPartyPeriodActual = new KpiPartyPeriodActual();
				kpiPartyPeriodActual.setId(kpiPartyPeriodActualPK);
				kpiPartyPeriodActual.setKpiCalculatedValue(periodPercentage);
				kpiPartyPeriodActual.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
				kpiPartyPeriodActual.setKpiPartyMeasurementPeriod(kpiPartyMeasurementPeriod);
				
				/*
				 * Persist a new record for the actual
				 */
				kpiPartyPeriodActual = kpiPartyPeriodActualHome.persistKpiPartyPeriodActual(kpiPartyPeriodActual);
				
			}else{
				KpiPartyPeriodActual kpiPartyPeriodActual = kpiPartyPeriodActualList.get(0);
				kpiPartyPeriodActual.setKpiCalculatedValue(periodPercentage);
				
				/*
				 * Merge the existing KPI period actual record
				 */
				kpiPartyPeriodActual = kpiPartyPeriodActualHome.mergeKpiPartyPeriodActual(kpiPartyPeriodActual);
			}
			
		} catch (Exception e) {
			String errMsg = "An exception occured when clocking logistics provider delivery performance:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		}
		return true;
	}
	
	/**
	 * Clocks the invoice accuracy KPI transaction for the logistics provider
	 * @param locator is a locator for finding EJBs
	 * @param logAirwayBill is the airway bill in question
	 * @return true if the operation succeeds else false
	 */
	private Boolean clockLogisticsInvoiceAccuracy(Locator<Object> locator, LogAirwayBill logAirwayBill){
		/*
		 * Return true if the KPI has already been clocked
		 */
		if(logAirwayBill.getKpiInvoiceAccuracyClocked() != null && logAirwayBill.getKpiInvoiceAccuracyClocked()){
			return true;
		}

		/*
		 * Return false if the airway bill invoice is not approved
		 */
		if(logAirwayBill.getLogApprovalStatus1() == null || (!logAirwayBill.getLogApprovalStatus1().getApprovalStatusId().equals(VeniceConstants.LOG_APPROVAL_STATUS_APPROVED))){
			return false;
		}
		try{
			LogInvoiceReportUploadSessionEJBLocal logInvoiceReportUploadHome = (LogInvoiceReportUploadSessionEJBLocal) locator
			.lookupLocal(LogInvoiceReportUploadSessionEJBLocal.class, "LogInvoiceReportUploadSessionEJBBeanLocal");

			LogInvoiceReconRecordSessionEJBLocal logInvoiceReconRecordHome = (LogInvoiceReconRecordSessionEJBLocal) locator
			.lookupLocal(LogInvoiceReconRecordSessionEJBLocal.class, "LogInvoiceReconRecordSessionEJBBeanLocal");
			
			KPI_TransactionPosterSessionEJBLocal kPI_TransactionPosterHome = (KPI_TransactionPosterSessionEJBLocal) locator
			.lookupLocal(KPI_TransactionPosterSessionEJBLocal.class, "KPI_TransactionPosterSessionEJBBeanLocal");

			KpiPartyPeriodActualSessionEJBLocal kpiPartyPeriodActualHome = (KpiPartyPeriodActualSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodActualSessionEJBLocal.class, "KpiPartyPeriodActualSessionEJBBeanLocal");
			
			KpiPartyPeriodTransactionSessionEJBLocal kpiPartyPeriodTransactionHome = (KpiPartyPeriodTransactionSessionEJBLocal) locator
			.lookupLocal(KpiPartyPeriodTransactionSessionEJBLocal.class, "KpiPartyPeriodTransactionSessionEJBBeanLocal");
		
		/* Use the following process to clock the KPI
		 * o Get the invoice report
		 * o Get the reconciliation records
		 * o determine how many inaccuracies
		 * o Clock the KPI transaction
		 * o Update the period actual
		 */
		List<LogInvoiceReportUpload> logInvoiceReportUploadList = logInvoiceReportUploadHome.queryByRange("select o from LogInvoiceReportUpload o where o.fileNameAndLocation = '" + logAirwayBill.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getFileNameAndLocation() + "'", 0, 0);
		
		if(logInvoiceReportUploadList.isEmpty()){
			throw new EJBException("Invoice report for selected airway bill not found in the database!");
		}
		
		Long logisticsProviderPartyId = logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getVenParty().getPartyId();
		
		/*
		 * Get the current KPI period
		 */
		Long currentKpiPeriodId = KpiPeriodUtil.getCurrentPeriod().getKpiPeriodId();	
		LogInvoiceReportUpload logInvoiceReportUpload = logInvoiceReportUploadList.get(0);		
		List<LogInvoiceReconRecord> logInvoiceReconRecordList = logInvoiceReconRecordHome.queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceReportUpload.invoiceReportUploadId = " + logInvoiceReportUpload.getInvoiceReportUploadId() + " and o.logAirwayBill.airwayBillId = " + logAirwayBill.getAirwayBillId(), 0, 0);
				
		/*
		 * If there are problems with the invoice clock -1 for each problem
		 */
		Integer problemCount = 0;
		for(LogInvoiceReconRecord record:logInvoiceReconRecordList){
			kPI_TransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY, currentKpiPeriodId, logisticsProviderPartyId, -1, "Invoice inaccuracy found for:" + logAirwayBill.getAirwayBillNumber() + ". Problem was:" + record.getLogReconInvoiceRecordResult().getReconRecordResultDesc());
			++problemCount;
		}
		
		/*
		 * If there are no problems with the invoice then 
		 * clock +9 because there are 9 aspects checked
		 */
		if(logInvoiceReconRecordList.isEmpty()){
			kPI_TransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY, currentKpiPeriodId, logisticsProviderPartyId, 9, "Invoice accurate for:" + logAirwayBill.getAirwayBillNumber() + ". No problems found");
		}else{
			if(problemCount < 9){
				kPI_TransactionPosterHome.postKpiTransaction(VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY, currentKpiPeriodId, logisticsProviderPartyId, 9 - problemCount, "Invoice accuracy found for:" + logAirwayBill.getAirwayBillNumber() + " " + (9 - problemCount) + " accurate aspects found");				
			}
		}
		
		/*
		 * Calculate the percentage for the period actual and
		 * then store it for the provider
		 * 	o read all the transactions into a list
		 *  o sum the penalty transactions (-1's)
		 *  o determine the +ve transactions (add up all of the values
		 *  o calculate the integral %ge
		 */
		List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList =  kpiPartyPeriodTransactionHome.queryByRange("select o from KpiPartyPeriodTransaction o where o.kpiPartyMeasurementPeriod.venParty.partyId = " + logisticsProviderPartyId + " and o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY + " and o.kpiPartyMeasurementPeriod.id.kpiPeriodId = " + currentKpiPeriodId, 0, 0);
		
		Integer totalAccurateAspects = 0;		
		Integer totalInaccurateAspects = 0;
		for(KpiPartyPeriodTransaction transaction:kpiPartyPeriodTransactionList){
			if(transaction.getKpiTransactionValue() < 0){
				++totalInaccurateAspects;
			}else{
				totalAccurateAspects = totalAccurateAspects + transaction.getKpiTransactionValue();
			}
		}
		
		Integer positiveTransactions = totalAccurateAspects - totalInaccurateAspects;
		Double dPeriodPercentage = (new Double(positiveTransactions) / new Double(totalAccurateAspects) * 100);
		
		BigDecimal bdPeriodPercentage = new BigDecimal(dPeriodPercentage);
		bdPeriodPercentage.setScale(0, RoundingMode.UP);
		Integer periodPercentage = bdPeriodPercentage.intValue();
		
		/*
		 * If a party period actual record exists then update it else
		 * create a new record
		 */
		
		List<KpiPartyPeriodActual> kpiPartyPeriodActualList = kpiPartyPeriodActualHome.queryByRange("select o from KpiPartyPeriodActual o where o.kpiKeyPerformanceIndicator.kpiId = " + VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY + " and o.kpiPartyMeasurementPeriod.id.partyId = " + logisticsProviderPartyId + " and o.kpiPartyMeasurementPeriod.id.kpiPeriodId = " + currentKpiPeriodId, 0, 0);
		if(kpiPartyPeriodActualList.isEmpty()){
			KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod = new KpiPartyMeasurementPeriod();
			kpiPartyMeasurementPeriod.setVenParty(logAirwayBill.getVenOrderItem().getLogLogisticService().getLogLogisticsProvider().getVenParty());
			KpiPartyMeasurementPeriodPK kpiPartyMeasurementPeriodPK = new KpiPartyMeasurementPeriodPK();
			kpiPartyMeasurementPeriodPK.setKpiPeriodId(currentKpiPeriodId);
			kpiPartyMeasurementPeriodPK.setPartyId(logisticsProviderPartyId);
			kpiPartyMeasurementPeriod.setId(kpiPartyMeasurementPeriodPK);
			
			KpiPartyPeriodActualPK kpiPartyPeriodActualPK = new KpiPartyPeriodActualPK();
			kpiPartyPeriodActualPK.setKpiId(VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY);
			kpiPartyPeriodActualPK.setKpiPeriodId(currentKpiPeriodId);
			kpiPartyPeriodActualPK.setPartyId(logisticsProviderPartyId);
			
			KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
			kpiKeyPerformanceIndicator.setKpiId(VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY);
			
			KpiPartyPeriodActual kpiPartyPeriodActual = new KpiPartyPeriodActual();
			kpiPartyPeriodActual.setId(kpiPartyPeriodActualPK);
			kpiPartyPeriodActual.setKpiCalculatedValue(periodPercentage);
			kpiPartyPeriodActual.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
			kpiPartyPeriodActual.setKpiPartyMeasurementPeriod(kpiPartyMeasurementPeriod);
			
			/*
			 * Persist a new record for the actual
			 */
			kpiPartyPeriodActual = kpiPartyPeriodActualHome.persistKpiPartyPeriodActual(kpiPartyPeriodActual);
			
		}else{
			KpiPartyPeriodActual kpiPartyPeriodActual = kpiPartyPeriodActualList.get(0);
			kpiPartyPeriodActual.setKpiCalculatedValue(periodPercentage);
			
			/*
			 * Merge the existing KPI period actual record
			 */
			kpiPartyPeriodActual = kpiPartyPeriodActualHome.mergeKpiPartyPeriodActual(kpiPartyPeriodActual);
		}

		} catch (Exception e) {
			String errMsg = "An exception occured when clocking logistics provider invoice accuracy:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		}
		return false;
	}
	
	/**
	 * Returns true if the order item is the first order item for the order created in the Sales record
	 * @param locator is a Locator object to locate the EJB
	 * @param venOrderItem is the order item in question
	 * @return true if the item is the first else false
	 */
	private boolean isFirstCXOrderItem(Locator<Object> locator, VenOrderItem venOrderItem){
		boolean isFirstCX = true;
		try {
			FinSalesRecordSessionEJBLocal finSalesRecordHome = (FinSalesRecordSessionEJBLocal) locator
			.lookupLocal(FinSalesRecordSessionEJBLocal.class, "FinSalesRecordSessionEJBBeanLocal");
			
			List<FinSalesRecord> finSalesRecordList = finSalesRecordHome.queryByRange("select o from FinSalesRecord o where o.venOrderItem.venOrder.orderId = " + venOrderItem.getVenOrder().getOrderId(), 0, 0);
			
			_log.debug("finSalesRecordList size: "+finSalesRecordList.size());				
			for(FinSalesRecord finSalesRecord:finSalesRecordList){
				if(finSalesRecord.getVenOrderItem().getOrderItemId() != venOrderItem.getOrderItemId()){
					isFirstCX= false;
					break;
				}
			}			
		} catch (Exception e) {
			String errorText = e.getMessage();
			_log.error(errorText);
			e.printStackTrace();
			throw new EJBException(errorText);
		}
		
		return isFirstCX;
	}
	
	/**
	 * Returns the sum of all the handling fees for payments attached to the order
	 * @param locator a locator for the EJBs
	 * @param venOrderItem the order item in question
	 * @return the handling fees
	 */
	private BigDecimal getHandlingFeesFromOrderPayments(Locator<Object> locator, VenOrderItem venOrderItem){	
		BigDecimal handlingFee = new BigDecimal(0);
		handlingFee.setScale(2, RoundingMode.HALF_UP);
		
		try {			
			VenOrderPaymentAllocationSessionEJBLocal venOrderPaymentAllocationHome = (VenOrderPaymentAllocationSessionEJBLocal) locator
			.lookupLocal(VenOrderPaymentAllocationSessionEJBLocal.class, "VenOrderPaymentAllocationSessionEJBBeanLocal");
			
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = 
				venOrderPaymentAllocationHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + venOrderItem.getVenOrder().getOrderId(), 0, 0);

			if(!venOrderPaymentAllocationList.isEmpty()){
				handlingFee = handlingFee.add(venOrderPaymentAllocationList.get(0).getVenOrderPayment().getHandlingFee());
			}						
		} catch (Exception e) {
			String errorText = e.getMessage();
			_log.error(errorText);
			e.printStackTrace();
			throw new EJBException(errorText);
		}

		return handlingFee;		
	}
	
	/**
	 * Gets the merchant specific transaction fees for the order item.
	 * @param locator is a locator to use
	 * @param venOrderItem is the order item in question
	 * @return the merchant transaction fees
	 */
	private BigDecimal getMerchantTransactionFees(Locator<Object> locator, VenOrderItem venOrderItem){
		BigDecimal transactionFeeAmount = new BigDecimal(0);
		try {
			VenTransactionFeeSessionEJBLocal transactionFeeHome = (VenTransactionFeeSessionEJBLocal) locator
			.lookupLocal(VenTransactionFeeSessionEJBLocal.class, "VenTransactionFeeSessionEJBBeanLocal");

			/*
			 * Get the transaction fees for the merchant and sums them
			 */
			List<VenTransactionFee> transactionFeeList = transactionFeeHome
					.queryByRange("select o from VenTransactionFee o where o.venOrder.orderId = "+ venOrderItem.getVenOrder().getOrderId(), 0, 0);
			
			if(!transactionFeeList.isEmpty()){
				for(VenTransactionFee venTransactionFee:transactionFeeList){
					if(venOrderItem.getVenMerchantProduct().getVenMerchant().getMerchantId().equals(venTransactionFee.getVenMerchant().getMerchantId())){
						transactionFeeAmount = transactionFeeAmount.add(venTransactionFee.getFeeAmount());
					}
				}
			}
		} catch (Exception e) {
			String errorText = e.getMessage();
			e.printStackTrace();
			throw new EJBException(errorText);
		}
		
		return transactionFeeAmount;
	}
	
	/**
	 * Returns the merchant promotion amount for the order item
	 * @param locator is a locator to use for EJB lookup
	 * @param venOrderItem is the order item in question
	 * @param adjustmentList is the list of marginPromo
	 * @return the merchant promotion amount
	 */
	private BigDecimal getMerchantPromotionAmount(Locator<Object> locator, VenOrderItem venOrderItem, List<VenOrderItemAdjustment> adjustmentList){
		BigDecimal merchantPromotionAmount = new BigDecimal(0);
		
		try {
			VenPartyPromotionShareSessionEJBLocal partyPromotionShareHome = (VenPartyPromotionShareSessionEJBLocal) locator
			.lookupLocal(VenPartyPromotionShareSessionEJBLocal.class, "VenPartyPromotionShareSessionEJBBeanLocal");

			List<VenPartyPromotionShare> merchantPromotionShareList = partyPromotionShareHome
					.queryByRange("select o from VenPartyPromotionShare o where o.venParty.partyId = " + venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId(), 0, 0);

			for (VenPartyPromotionShare share : merchantPromotionShareList) {
				for (VenOrderItemAdjustment adjustment : adjustmentList) {
					/*
					 * If flat then calculate flat amount else calculate
					 * percentage based
					 */
					if (adjustment.getVenPromotion().equals(share.getVenPromotion())) {
						if (share.getVenPromotionShareCalcMethod().getPromotionCalcMethodId().equals(VeniceConstants.VEN_PROMOTION_SHARE_CALC_METHOD_FLAT)) {
							// Add the flat amount
							merchantPromotionAmount = merchantPromotionAmount.add(new BigDecimal(share.getPromotionCalcValue()));
						} else {
							BigDecimal percentageFraction = new BigDecimal(share.getPromotionCalcValue()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
							merchantPromotionAmount = merchantPromotionAmount.add(adjustment.getAmount().multiply(percentageFraction));
						}
					}
				}
			}
		} catch (Exception e) {
			String errorText = e.getMessage();
			_log.error(errorText);
			e.printStackTrace();
			throw new EJBException(errorText);
		}
		merchantPromotionAmount.setScale(2, RoundingMode.HALF_UP);
		return merchantPromotionAmount;
	}
	
	/**
	 * Calculates the third party promotion amount for the order item
	 * @param locator is a locator to use for EJB lookup
	 * @param venOrderItem is the order item in question
	 * @param adjustmentList is a list of the marginPromo
	 * @return the third party promotion amount
	 */
	private BigDecimal getThirdPartyPromotionAmount(Locator<Object> locator, VenOrderItem venOrderItem, List<VenOrderItemAdjustment> adjustmentList){
		BigDecimal thirdPartyPromotionAmount = new BigDecimal(0);
		
		try {
			VenPartyPromotionShareSessionEJBLocal partyPromotionShareHome = (VenPartyPromotionShareSessionEJBLocal) locator
			.lookupLocal(VenPartyPromotionShareSessionEJBLocal.class, "VenPartyPromotionShareSessionEJBBeanLocal");

			List<VenPartyPromotionShare> thirdPartyPromotionShareList = partyPromotionShareHome
			.queryByRange("select o from VenPartyPromotionShare o where o.venParty.partyId <> " + venOrderItem.getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId(), 0, 0);

			for (VenPartyPromotionShare share : thirdPartyPromotionShareList) {
				for (VenOrderItemAdjustment adjustment : adjustmentList) {
					/*
					 * If flat then calculate flat amount else calculate
					 * percentage based
					 */
					if (adjustment.getVenPromotion().equals(share.getVenPromotion())) {
						if (share.getVenPromotionShareCalcMethod().getPromotionCalcMethodId().equals(VeniceConstants.VEN_PROMOTION_SHARE_CALC_METHOD_FLAT)) {
							// Add the flat amount
							thirdPartyPromotionAmount = thirdPartyPromotionAmount.add(new BigDecimal(share.getPromotionCalcValue()));
						} else {
							BigDecimal percentageFraction = new BigDecimal(share.getPromotionCalcValue()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
							thirdPartyPromotionAmount = thirdPartyPromotionAmount.add(adjustment.getAmount().multiply(percentageFraction));
						}
					}
				}
			}
		} catch (Exception e) {
			String errorText = e.getMessage();
			_log.error(errorText);
			e.printStackTrace();
			throw new EJBException(errorText);
		}
		return thirdPartyPromotionAmount;
	}

	@Override
	public Boolean onPostMerge(Object businessObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean onPreMerge(Object businessObject) {
		// TODO Auto-generated method stub
		return null;
	}
}
