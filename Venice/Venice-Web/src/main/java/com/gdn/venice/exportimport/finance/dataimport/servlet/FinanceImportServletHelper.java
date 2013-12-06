package com.gdn.venice.exportimport.finance.dataimport.servlet;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInReportSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.util.FinancePeriodUtil;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInReport;
import com.gdn.venice.persistence.FinArFundsInReportType;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.persistence.VenPaymentStatus;
import com.gdn.venice.util.VeniceConstants;

/**
 * Utility class for finance import servlets
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FinanceImportServletHelper {
	protected static Logger _log = null;
	public FinanceImportServletHelper() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");
	}

	public static Boolean isFileUnique(String uniqueId, long reportType) {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}

		String reportPrefix = "";
		if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC) {
			reportPrefix = "BCA CC";
		} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB) {
			reportPrefix = "BCA IB";
		} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA) {
			reportPrefix = "BCA VA";
		} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB) {
			reportPrefix = "Mandiri IB";
		} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA) {
			reportPrefix = "Mandiri VA";
		} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB) {
			reportPrefix = "Niaga IB";
		}else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB) {
			reportPrefix = "KlikPay IB";
		}else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC) {
			reportPrefix = "KlikPay Full CC";
		}else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC) {
			reportPrefix = "KlikPay Inst CC";
		}else if (reportType == VeniceConstants. FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB){
			reportPrefix = "XL IB";
		}else if (reportType == VeniceConstants. FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC){
			reportPrefix = "Mandiri Installment CC";
		}else if (reportType == VeniceConstants. FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB){
			reportPrefix = "BRI IB";
		}
		
		
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			FinArFundsInReportSessionEJBRemote reportHome = (FinArFundsInReportSessionEJBRemote) locator.lookup(FinArFundsInReportSessionEJBRemote.class, "FinArFundsInReportSessionEJBBean");
			List<FinArFundsInReport> reportList = reportHome.queryByRange(
					"select o from FinArFundsInReport o where o.reportDesc like '" + reportPrefix + "%'" + " and o.reportDesc like '" + "%" + uniqueId + "'", 0, 0);
			if (reportList != null && !reportList.isEmpty()) {
				return false;
			}
		} catch (Exception e) {
			String errMsg = "An exeption occured while checking report uniqueness:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			return null;
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * Uploads a file from the servlet request naming with a standard date based
	 * on yyyy.MM.dd HH.mm.ss + fileAppendedName
	 * 
	 * @param request
	 * @param filePath
	 * @param fileAppendedName
	 * @return the full file name and path of the uploaded file.
	 */
	@SuppressWarnings("unchecked")
	public static String upload(HttpServletRequest request, String filePath, String fileAppendedName) {

		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}

		String formatString = "yyyy.MM.dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		String fileName = sdf.format(new Date()) + fileAppendedName;

		_log.info("Opening file for writing:" + filePath + fileName);

		File uploadedFile = new File(filePath + fileName);
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> fileItemsList = null;

		try {
			fileItemsList = servletFileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			String errMsg = "An exception occured when parsing the servlet file upload:"+ e.getMessage();
			e.printStackTrace();
			_log.error(errMsg);
			return null;
		}

		Iterator<FileItem> it = fileItemsList.iterator();
		while (it.hasNext()) {
			FileItem fileItem = (FileItem) it.next();

			if (!fileItem.isFormField()) {
				try {
					fileItem.write(uploadedFile);
				} catch (Exception e) {
					String errMsg = "An exception occured when writing to file:" + fileName + " :" + e.getMessage();
					e.printStackTrace();
					_log.error(errMsg);
					return null;
				}
			}
		}
		return filePath + fileName;
	}

	/**
	 * Creates a record in the database for the funds in report.
	 * 
	 * @param reportType
	 * @param fileName
	 * @param filePath
	 * @return returns the new FinArFundsInReport object
	 */
	public static FinArFundsInReport createFundsInReportRecord(Long reportType,
			String fileName, String filePath, String uniqueId) {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}

		FinArFundsInReportSessionEJBRemote reportHome;
		FinArFundsInReport finArFundsInReport = new FinArFundsInReport();
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			reportHome = (FinArFundsInReportSessionEJBRemote) locator.lookup(FinArFundsInReportSessionEJBRemote.class, "FinArFundsInReportSessionEJBBean");

			finArFundsInReport.setFileNameAndLocation(filePath + fileName);
			FinArFundsInReportType finArFundsInReportType = new FinArFundsInReportType();
			finArFundsInReportType.setPaymentReportTypeId(reportType);
			finArFundsInReport.setFinArFundsInReportType(finArFundsInReportType);
			finArFundsInReport.setFinPeriod(FinancePeriodUtil.getCurrentPeriod());

			String reportDesc = "";
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC) {
				reportDesc = "BCA CC Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB) {
				reportDesc = "BCA IB Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA) {
				reportDesc = "BCA VA Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB) {
				reportDesc = "Mandiri IB Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA) {
				reportDesc = "Mandiri VA Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB) {
				reportDesc = "Niaga IB Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB) {
				reportDesc = "KlikPay IB Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC) {
				reportDesc = "KlikPay Full CC Report";
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC) {
				reportDesc = "KlikPay Inst CC Report";
			}  else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB) {
				reportDesc = "XL IB Report";
			}else if (reportType == VeniceConstants. FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC){
				reportDesc = "Mandiri Installment CC";
			}else if (reportType == VeniceConstants. FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB){
				reportDesc = "BRI IB Report";
			}

			finArFundsInReport.setReportDesc(reportDesc + "-" + uniqueId);
			finArFundsInReport.setReportTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
			finArFundsInReport.setUserLogonName("System");
			// Persist the report record to the database
			finArFundsInReport = reportHome.persistFinArFundsInReport(finArFundsInReport);

		} catch (Exception e) {
			String errMsg = "An exeption occured while creating the funds in report record:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			return null;
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return finArFundsInReport;

	}

	/**
	 * Gets the order that is related to a payment based on the confirmation
	 * number.
	 * 
	 * @param em
	 * @param paymentConfirmationNumber
	 * @return returns the related order
	 */
	public static VenOrder getPaymentRelatedOrder(String paymentConfirmationNumber,Double paidAmount,long reportType) {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}
		
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationHome = (VenOrderPaymentAllocationSessionEJBRemote) locator
					.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");

			VenOrderSessionEJBRemote orderHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			FinArFundsInReconRecordSessionEJBRemote reconRecordOrderHome = (FinArFundsInReconRecordSessionEJBRemote) locator.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");

			/*
			 * For BCA IB as well as Mandiri IB and Niaga IB the confirmation number
			 * is the WCS order id
			 */
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB) {
				_log.debug("paymentConfirmationNumber/ wcsOrderID = "+paymentConfirmationNumber);
				List<VenOrderPaymentAllocation> paymentAllocationList = orderPaymentAllocationHome.queryByRange(
						"select o from VenOrderPaymentAllocation o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = '" + paymentConfirmationNumber + "' and p.venPaymentType.paymentTypeId="+VeniceConstants.VEN_PAYMENT_TYPE_ID_IB, 0, 0);
				
				// If the list is null after checking both then error
				if (paymentAllocationList == null || paymentAllocationList.isEmpty()) {
					String errMsg = "A record in the BCA or Mandiri IB or Niaga IB report being processed contains an order id that has no corresponding order record in the Venice database:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				} else {

					List<FinArFundsInReconRecord> reconRecordList = reconRecordOrderHome.queryByRange("select o from FinArFundsInReconRecord o where o.nomorReff  = '" + paymentConfirmationNumber + "' and o.venOrderPayment.venPaymentType.paymentTypeId="+VeniceConstants.VEN_PAYMENT_TYPE_ID_IB +" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
					if (!reconRecordList.isEmpty() || reconRecordList!=null ) {					
						for (FinArFundsInReconRecord inReconRecord : reconRecordList){								
							if (inReconRecord.getReconcilliationRecordTimestamp()!=null && inReconRecord.getFinArFundsInReport()!=null  ){
								if(inReconRecord.getFinArFundsInReport().getFinArFundsInReportType().getPaymentReportTypeId().equals(reportType)){
									String errMsg = "A record is already exists in the Venice database recon record :" + paymentConfirmationNumber;
									_log.error(errMsg);
									return null;
								}								
								
							}		
						}
											
					}			
						
					return paymentAllocationList.get(0).getVenOrder();
				}
			}

			/*
			 * For BCA CC report the authCD is a real payment confirmation
			 * number 1) Get the payment allocation based on the authCd o should
			 * equal the paymentConfirmationNumber for CC 2) Look up the order
			 * using the order id
			 */
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC ||  
					reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC || reportType == VeniceConstants. FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC) {
				_log.debug("methode getPaymentRelatedOrder : Date payment of CC "+paymentConfirmationNumber);
							
				List<VenOrderPaymentAllocation> paymentAllocationList = orderPaymentAllocationHome.queryByRange(
					"select o from VenOrderPaymentAllocation o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = '" + paymentConfirmationNumber + "' and p.amount ="+paidAmount+
					" and o.venOrderPayment.orderPaymentId in (select u.venOrderPayment.orderPaymentId from FinArFundsInReconRecord u, VenOrderPayment r where u.venOrderPayment.orderPaymentId = r.orderPaymentId and r.referenceId = '" + paymentConfirmationNumber + "' and r.amount = "+paidAmount+" and u.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED+" and u.reconcilliationRecordTimestamp is null)", 0, 0);
				if (paymentAllocationList == null || paymentAllocationList.isEmpty()) {
					String errMsg = "A record in the report being processed contains an authCd that has no corresponding order/payment record in the Venice database:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				}						
//				
//					List<FinArFundsInReconRecord> reconRecordList = reconRecordOrderHome.queryByRange("select o from FinArFundsInReconRecord o where o.nomorReff  = '" + paymentConfirmationNumber + "' and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
//						if (!reconRecordList.isEmpty() || reconRecordList!=null ) {
//								for (FinArFundsInReconRecord inReconRecord : reconRecordList){
//									if (inReconRecord.getReconcilliationRecordTimestamp()!=null && inReconRecord.getFinArFundsInReport()!=null){
//										String errMsg = "A record is already exists in the Venice database recon record :" + paymentConfirmationNumber;
//										_log.error(errMsg);
//										return null;
//									}		
//								}
//													
//							}																		
				paymentAllocationList.get(0).getVenOrder();
				List<VenOrder> venOrderList = orderHome.queryByRange("select o from VenOrder o where o.orderId = " + paymentAllocationList.get(0).getId().getOrderId(), 0, 0);
				return venOrderList.get(0);
			}
			
			/*
			 * no reff XL tunai 
			 */			
			
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB) {
				List<VenOrderPaymentAllocation> paymentAllocationList = orderPaymentAllocationHome.queryByRange(
					"select o from VenOrderPaymentAllocation o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = '" + paymentConfirmationNumber + "'", 0, 0);
				if (paymentAllocationList == null || paymentAllocationList.isEmpty()) {
					String errMsg = "A record in the report being processed contains an authCd that has no corresponding order/payment record in the Venice database:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				}
				
				if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB){
					List<FinArFundsInReconRecord> reconRecordList = reconRecordOrderHome.queryByRange("select o from FinArFundsInReconRecord o where o.nomorReff  = '" + paymentConfirmationNumber + "' and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
						if (!reconRecordList.isEmpty() || reconRecordList!=null ) {
							for (FinArFundsInReconRecord inReconRecord : reconRecordList){
								if (inReconRecord.getReconcilliationRecordTimestamp()!=null && inReconRecord.getFinArFundsInReport()!=null){
									String errMsg = "A record is already exists in the Venice database recon record :" + paymentConfirmationNumber;
									_log.error(errMsg);
									return null;
								}		
							}
												
						}				
				}

				List<VenOrder> venOrderList = orderHome.queryByRange("select o from VenOrder o where o.orderId = " + paymentAllocationList.get(0).getId().getOrderId(), 0, 0);
				return venOrderList.get(0);
			}

			/*
			 * In the case of Mandiri or BCA VA the confirmation number is the VA
			 * number
			 */
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA) {

				List<VenOrderPaymentAllocation> paymentAllocationList = orderPaymentAllocationHome.queryByRange(
					"select o from VenOrderPaymentAllocation o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = '" + paymentConfirmationNumber + "'", 0, 0);

				// If the list is null after checking both then error
				if (paymentAllocationList == null || paymentAllocationList.isEmpty()) {
					String errMsg = "A record in the VA report being processed contains a VA number that has no corresponding order/payment record in the Venice database:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				}
				
				List<FinArFundsInReconRecord> reconRecordList = reconRecordOrderHome.queryByRange("select o from FinArFundsInReconRecord o where o.nomorReff  = '" + paymentConfirmationNumber + "' and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
				if (!reconRecordList.isEmpty() || reconRecordList!=null ) {
						for (FinArFundsInReconRecord inReconRecord : reconRecordList){
							if (inReconRecord.getReconcilliationRecordTimestamp()!=null && inReconRecord.getFinArFundsInReport()!=null){
								String errMsg = "A record is already exists in the Venice database recon record :" + paymentConfirmationNumber;
								_log.error(errMsg);
								return null;
							}		
						}
											
					}		

				List<VenOrder> venOrderList = orderHome.queryByRange("select o from VenOrder o where o.orderId = " + paymentAllocationList.get(0).getId().getOrderId(), 0, 0);
				return venOrderList.get(0);
			}

			String errMsg = "An error has occured. It is likely that an invalid reportType has been passed. Contact the systems administrator.";
			_log.error(errMsg);
			return null;
		} catch (Exception e) {
			String errMsg = "An exception occured while ascertaining the order related to a payment:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			return null;
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

	/**
	 * Approves a Mandiri VA payment by setting the status to approved o Note
	 * that this triggers integration outbound
	 * 
	 * @param virtualAccountNumber
	 * @return true if the operation succeeds else false
	 */
	public static Boolean approveMandiriVAPayment(String virtualAccountNumber) {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}

		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			VenOrderPaymentSessionEJBRemote orderPaymentHome = (VenOrderPaymentSessionEJBRemote) locator.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");

			List<VenOrderPayment> paymentList = orderPaymentHome.queryByRange("select o from VenOrderPayment o where o.referenceId = '"+ virtualAccountNumber + "'", 0, 0);
			if (paymentList == null || paymentList.isEmpty()) {
				_log.error("Payment not found for virtual account number:" + virtualAccountNumber);
				return false;
			}

			VenOrderPayment payment = paymentList.get(0);
			VenPaymentStatus paymentStatus = new VenPaymentStatus();
			paymentStatus.setPaymentStatusId(VeniceConstants.VEN_VA_PAYMENT_STATUS_ID_APPROVED);
			payment.setVenPaymentStatus(paymentStatus);
			orderPaymentHome.mergeVenOrderPayment(payment);
		} catch (Exception e) {
			_log.error("An exception occured while approving a payment:" + e.getMessage());
			e.printStackTrace();
			return false;
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Approves a BCA VA payment by setting the status to approved o Note that
	 * this triggers integration outbound
	 * 
	 * @param paymentConfirmationNumber
	 * @return true if the operation succeeds else false
	 */
	public static Boolean approveBcaVAPayment(Long orderPaymentId) {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			VenOrderPaymentSessionEJBRemote orderPaymentHome = (VenOrderPaymentSessionEJBRemote) locator
					.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");

			List<VenOrderPayment> paymentList = orderPaymentHome.queryByRange("select o from VenOrderPayment o where o.orderPaymentId = " + orderPaymentId, 0, 0);
			if (paymentList == null || paymentList.isEmpty()) {
				_log.error("Payment not found for Id:" + orderPaymentId);
				return false;
			}

			VenOrderPayment payment = paymentList.get(0);
			VenPaymentStatus paymentStatus = new VenPaymentStatus();
			paymentStatus.setPaymentStatusId(VeniceConstants.VEN_VA_PAYMENT_STATUS_ID_APPROVED);
			payment.setVenPaymentStatus(paymentStatus);
			orderPaymentHome.mergeVenOrderPayment(payment);
		} catch (Exception e) {
			_log.error("An exception occured while approving a payment:" + e.getMessage());
			e.printStackTrace();
			return false;
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Gets the existing payment record related to the incoming payment
	 * confirmation number.
	 * 
	 * @param paymentConfirmationNumber
	 * @return the related payment object
	 */
	public static VenOrderPayment getRelatedPaymentRecord(String paymentConfirmationNumber,Double paidAmount, long reportType) {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}
		
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			VenOrderPaymentSessionEJBRemote orderPaymentHome = (VenOrderPaymentSessionEJBRemote) locator
					.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");

			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationHome = (VenOrderPaymentAllocationSessionEJBRemote) locator
					.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");

			/*
			 * BCA and Mandiri IB and Niaga IB use the WCS order id as the payment
			 * confirmation number
			 */
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB 
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB) {
				List<VenOrderPaymentAllocation> paymentAllocationList = orderPaymentAllocationHome.queryByRange(
						"select o from VenOrderPaymentAllocation o where o.venOrderPayment.referenceId  = '"+ paymentConfirmationNumber+"' and o.venOrderPayment.venPaymentType.paymentTypeId ="+VeniceConstants.VEN_PAYMENT_TYPE_ID_IB, 0, 0);
				// If the list is null after checking both then error
				if (paymentAllocationList == null || paymentAllocationList.isEmpty()) {
					String errMsg = "A record in the BCA IB or Mandiri IB or Niaga IB report being processed contains an order id that has no corresponding order record in the Venice database:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				} else {
					for (VenOrderPaymentAllocation allocation : paymentAllocationList) {
						/*
						 * Select the first payment with the same payment type
						 */
						_log.debug("\n Payment type: "+allocation.getVenOrderPayment().getVenPaymentType().getPaymentTypeId());
						_log.debug("\n Bank: "+allocation.getVenOrderPayment().getVenBank().getBankId());
						if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB) {
							if (allocation.getVenOrderPayment().getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_IB
									&& allocation.getVenOrderPayment().getVenBank().getBankId() == VeniceConstants.VEN_BANK_ID_BCA) {
								return allocation.getVenOrderPayment();
							}
						} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB) {
							if (allocation.getVenOrderPayment().getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_IB
									&& allocation.getVenOrderPayment().getVenBank().getBankId() == VeniceConstants.VEN_BANK_ID_MANDIRI) {
								return allocation.getVenOrderPayment();
							}
						} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB) {
							if (allocation.getVenOrderPayment().getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_IB
									&& allocation.getVenOrderPayment().getVenBank().getBankId() == VeniceConstants.VEN_BANK_ID_NIAGA) {

								return allocation.getVenOrderPayment();
							}
						}else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB) {
							if (allocation.getVenOrderPayment().getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_IB
									&& allocation.getVenOrderPayment().getVenBank().getBankId() == VeniceConstants.VEN_BANK_ID_BCA) {

								return allocation.getVenOrderPayment();
							}
						}else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB) {
							if (allocation.getVenOrderPayment().getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_IB
									&& allocation.getVenOrderPayment().getVenBank().getBankId() == VeniceConstants.VEN_BANK_ID_BRI) {

								return allocation.getVenOrderPayment();
							}
						}
					}
				}
				String errMsg = "A payment was found in the import file that does not exist in the payment schedule:" + paymentConfirmationNumber;
				_log.error(errMsg);
				return null;
			}

			/*
			 * For BCA CC the payment confirmation number is used to find the
			 * payment
			 */
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC || 
					reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC ) {
				_log.debug("methode getRelatedPaymentRecord : Date payment of CC "+paymentConfirmationNumber);

				List<VenOrderPayment> venOrderPaymentList = orderPaymentHome.queryByRange("select o from VenOrderPayment o where o.referenceId = '" + paymentConfirmationNumber + "' and o.amount ="+paidAmount, 0, 0);
				if (venOrderPaymentList == null || venOrderPaymentList.isEmpty()) {
					String errMsg = "A payment was found in the import file that does not exist in the payment schedule:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				}
				return venOrderPaymentList.get(0);

			}
			
			
			/*
			 * For XL payment the payment reff number is used to find the
			 * payment
			 */
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB) {
				List<VenOrderPayment> venOrderPaymentList = orderPaymentHome.queryByRange("select o from VenOrderPayment o where o.referenceId= '" + paymentConfirmationNumber + "'", 0, 0);
				if (venOrderPaymentList == null || venOrderPaymentList.isEmpty()) {
					String errMsg = "A payment was found in the import file that does not exist in the payment schedule:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				}
				return venOrderPaymentList.get(0);

			}
			

			/*
			 * In the case of VA payments the confirmation number is the virtual
			 * account number. So check the VA number for a match.
			 */
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA) {
				List<VenOrderPayment> venOrderPaymentList = orderPaymentHome.queryByRange("select o from VenOrderPayment o where o.referenceId = '" + paymentConfirmationNumber + "'", 0, 0);

				if (venOrderPaymentList == null || venOrderPaymentList.isEmpty()) {
					String errMsg = "A payment was found in the import file that does not exist in the payment schedule:" + paymentConfirmationNumber;
					_log.error(errMsg);
					return null;
				}
				return venOrderPaymentList.get(0);
			}

			String errMsg = "An error has occured. It is likely that an invalid report type was passed. Please contact the systems administrator";
			_log.error(errMsg);
			return null;

		} catch (Exception e) {
			String errMsg = "An exception occured while ascertaining the related payment:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			return null;
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

	/**
	 * Gets the remaining balance due for an order after a payment is posted.
	 * 
	 * @param em
	 * @param venOrder
	 * @param paymentAmount
	 * @return the remaining balance
	 * @throws Exception
	 */
	public static BigDecimal getRemainingBalanceAfterPayment(EntityManager em,	VenOrder venOrder, Double paymentAmount, FinArFundsInReconRecord reconRecord) throws Exception {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}
		_log.debug("paymentAmount : "+paymentAmount);
		/*
		 * Get the total of the payments (includes all +ve and -ve values of
		 * course)
		 */
		if (reconRecord.getVenOrderPayment() == null) {
			String errMsg = "A record record billing is null" ;
			_log.error(errMsg);
			throw new EJBException("A Billing is null (OrderID :"+venOrder.getWcsOrderId()+" )");
		}
		// ambil paid amount untuk setiap payment ID edit by arifin
		Query query = em.createQuery("select o from FinArFundsInReconRecord o where o.wcsOrderId = '"+ venOrder.getWcsOrderId() + "' and o.venOrderPayment.orderPaymentId="+ reconRecord.getVenOrderPayment().getOrderPaymentId()+" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED);
		
		BigDecimal paymentTotal = ((FinArFundsInReconRecord)query.getResultList().get(0)).getProviderReportPaidAmount();
		_log.debug("paymentTotal: "+paymentTotal);

		 query = em.createQuery("select o from FinArFundsInReconRecord o where o.wcsOrderId = '"+ venOrder.getWcsOrderId() + "' and o.venOrderPayment.orderPaymentId="+ reconRecord.getVenOrderPayment().getOrderPaymentId()+" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED  );
		
		BigDecimal remainingBalanceAmount = ((FinArFundsInReconRecord)query.getResultList().get(0)).getRemainingBalanceAmount();
		_log.debug("remainingBalanceAmount: "+remainingBalanceAmount);
		
		if (paymentTotal == null) {
			paymentTotal = new BigDecimal("0");		

			//jumlah bayar = amount report	
			//edit by arifin					
			paymentTotal = new BigDecimal(paymentAmount);	
			_log.debug("Amount Yang Dibayar: "+paymentTotal);
					
		}
		else{
			
			//jumlah bayar = amount report	
			//edit by arifin					
			paymentTotal = paymentTotal.add(new BigDecimal(paymentAmount));	
			_log.debug("Amount Yang Dibayar: "+paymentTotal);			
			}
			_log.debug("Amount yang harus dibayar : "+remainingBalanceAmount);
	
		/*
		 * Note that the amount stored in VenOrder.amount has all the charges
		 * for the items included - subtract the total of payments from the
		 * order total amount to get the remianing balance
		 */
		BigDecimal totalOrderAmount = null;
	
		//edit by arifin
		//sisa dari pembayaran bank report
		totalOrderAmount = remainingBalanceAmount.subtract(paymentTotal) ;			
		_log.debug("sisa dari pembayaran =  : "+totalOrderAmount);
		
		//edit by arifin
		BigDecimal remainingBalance = totalOrderAmount;		
		_log.debug("RemainingBalance =  : "+remainingBalance);

		return remainingBalance;
	}

	/**
	 * Gets an existing recon record for an incoming payment based on either the
	 * WCS order ID from the report or the payment confirmation number.
	 * 
	 * @param em
	 * @param reportType
	 * @param paymentIdentifier
	 * @param paymentAmount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static FinArFundsInReconRecord getReconciliationRecord(EntityManager em,Long reportType, String paymentIdentifier, BigDecimal paymentAmount) {
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");

		}

		paymentAmount = paymentAmount.setScale(2);

		List<FinArFundsInReconRecord> reconRecordList = null;
		_log.debug("reportType: "+reportType);
		_log.debug("paymentIdentifier: "+paymentIdentifier);
		_log.debug("\n Report type: "+reportType);
		_log.debug("\n Payment identifier: "+paymentIdentifier);
		_log.debug("\n Payment amount: "+paymentAmount);
		try {
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB 
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB) {
				_log.info("Processing reportType IB");
				/*
				 * If it is a IB or BCA VA report then used the WCS order ID and the
				 * payment status. we must assume to take the first payment that has
				 * not yet had any action applied and not apporved etc.
				 */
				String tempQuery="select o from FinArFundsInReconRecord o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = :wcsOrderId " +
						" and p.venPaymentType.paymentTypeId = :paymentType and o.finArFundsInActionApplied.actionAppliedId = " + VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE + 
						" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED
						+ " and o.finApprovalStatus.approvalStatusId = " + VeniceConstants.FIN_APPROVAL_STATUS_NEW+" and o.reconcilliationRecordTimestamp is null";
				
				Query q = em.createQuery(tempQuery);
				_log.info("query = "+tempQuery);	
				
				q.setParameter("wcsOrderId", paymentIdentifier).setParameter("paymentType", VeniceConstants.VEN_PAYMENT_TYPE_ID_IB);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA) {
				_log.info("Processing reportType VA");
				/*
				 * If it is a VA report then used the VA number and the payment
				 * amount along with payment type
				 */
				Query q = em.createQuery("select o from FinArFundsInReconRecord o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = :virtualAccountNumber and p.venPaymentType.paymentTypeId = :paymentType and o.finArFundsInActionApplied.actionAppliedId = "
								+ VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE +
								" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED
								+ " and o.finApprovalStatus.approvalStatusId = "
								+ VeniceConstants.FIN_APPROVAL_STATUS_NEW+" and  o.reconcilliationRecordTimestamp is null");
				q.setParameter("virtualAccountNumber", paymentIdentifier).setParameter("paymentType", VeniceConstants.VEN_PAYMENT_TYPE_ID_VA);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC || 
					reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC) {
				_log.info("Processing reportType CC");
				
				_log.debug("methode getReconciliationRecord : Date payment of CC "+paymentIdentifier);
						
				Query q = em.createQuery("select o from FinArFundsInReconRecord o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = :paymentConfirmationNumber and p.amount = "+paymentAmount+" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED+" and o.reconcilliationRecordTimestamp is null");
		        q.setParameter("paymentConfirmationNumber", paymentIdentifier);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();				
				}else if(reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB ){
				_log.info("Processing reportType XL Tunai");
				Query q = em.createQuery("select o from FinArFundsInReconRecord o, VenOrderPayment p where o.venOrderPayment.orderPaymentId = p.orderPaymentId and p.referenceId = :paymentConfirmationNumber and o.reconcilliationRecordTimestamp is null and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED+" and o.reconcilliationRecordTimestamp is null" );
				q.setParameter("paymentConfirmationNumber", paymentIdentifier);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();			
			}
		} catch (Exception e) {
			String errMsg = "An exception occured while finding existing reconciliation record:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
		}
		if (reconRecordList.isEmpty()) {
			_log.debug("\n Recon record empty");
			_log.info("reconRecord is empty");
			return null;
		}
		_log.debug("\n Recon record not empty");
		return reconRecordList.get(0);
	}
	
	public static void DeleteReport(FinArFundsInReport r){
		FinArFundsInReportSessionEJBRemote reportHome;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			reportHome = (FinArFundsInReportSessionEJBRemote) locator.lookup(FinArFundsInReportSessionEJBRemote.class, "FinArFundsInReportSessionEJBBean");
			_log.debug("Remove reportType: "+r.getReportDesc());		
			_log.debug("\n Remove Report type: "+r.getReportDesc());
			reportHome.removeFinArFundsInReport(r);			
			
		}catch(Exception e){
			String errMsg = "An exception occured while finding existing reconciliation record:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean cekContinueProsesFundIn (EntityManager em,Long reportType, String paymentIdentifier,String uniquePayment, BigDecimal paymentAmount){
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");
		}
		paymentAmount = paymentAmount.setScale(2);

		List<FinArFundsInReconRecord> reconRecordList = null;
		_log.debug("----------start cekContinueProsesFundIn-------- ");
		_log.debug("reportType: "+reportType);
		_log.debug("paymentIdentifier: "+paymentIdentifier);
		_log.debug("\n Report type: "+reportType);
		_log.debug("\n Payment identifier: "+uniquePayment);
		_log.debug("\n Payment amount: "+paymentAmount);
		try {
			if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_IB 
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_NIAGA_IB || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_IB
					|| reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BRI_IB) {
				_log.info("Processing reportType IB");
				/*
				 * If it is a IB or BCA VA report then used the WCS order ID and the
				 * payment status. we must assume to take the first payment that has
				 * not yet had any action applied and not apporved etc.
				 */
				String TempQuery="select o from FinArFundsInReconRecord o where (o.wcsOrderId = :wcsOrderId or o.nomorReff =:wcsOrderId) and o.reconcilliationRecordTimestamp is not null and o.venOrderPayment.venPaymentType.paymentTypeId="+VeniceConstants.VEN_PAYMENT_TYPE_ID_IB;
				
				Query q = em.createQuery(TempQuery);
				_log.info("query = "+TempQuery);	
				
				q.setParameter("wcsOrderId", paymentIdentifier);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_VA || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRI_VA) {
				_log.info("Processing reportType VA");
				/*
				 * If it is a VA report then used the VA number and the payment
				 * amount along with payment type
				 */
				Query q = em.createQuery("select o from FinArFundsInReconRecord o where o.nomorReff = :virtualAccountNumber" +
						" and o.reconcilliationRecordTimestamp is not null and o.uniquePayment = '"+ uniquePayment +"'");
				q.setParameter("virtualAccountNumber", paymentIdentifier);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();
			} else if (reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_BCA_CC || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAY_CC || 
					reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_KLIKPAYINST_CC  || reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_MANDIRIINSTALLMENT_CC) {
				_log.info("Processing reportType CC");
				
				_log.debug("methode getReconciliationRecord : Date payment of CC "+paymentIdentifier);
				
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
				Date date_s= dt.parse(uniquePayment);
				String unikTgl = dt.format(date_s.getTime());
				
				Query q = em.createQuery("select o from FinArFundsInReconRecord o where o.nomorReff = :paymentConfirmationNumber and o.providerReportPaidAmount ="+paymentAmount+" and o.reconcilliationRecordTimestamp is not null"+
						" and cast(o.providerReportPaymentDate as date)  = '"+unikTgl+"'" );
		        q.setParameter("paymentConfirmationNumber", paymentIdentifier);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();				
				
				}else if(reportType == VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TYPE_XL_IB ){
				_log.info("Processing reportType XL Tunai");
				Query q = em.createQuery("select o from FinArFundsInReconRecord o where o.nomorReff = :paymentConfirmationNumber  and o.reconcilliationRecordTimestamp is not null");
				q.setParameter("paymentConfirmationNumber", paymentIdentifier);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();			
			}
		} catch (Exception e) {
			String errMsg = "An exception occured while finding existing reconciliation record:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
		}
		if (!reconRecordList.isEmpty()) {
			_log.info("reconRecord is not empty");
			_log.debug("----------end cekContinueProsesFundIn result false -------- ");
			return false;
		}
		_log.info("\n Recon record empty");
		_log.debug("----------end cekContinueProsesFundIn result true -------- ");
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static List<FinArFundsInReconRecord> cekProsesVAFundIn (EntityManager em, String paymentIdentifier,String uniquePayment){
		if (_log == null) {
			Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
			_log = loggerFactory.getLog4JLogger("com.gdn.venice.finance.dataimport.servlet.FinanceImportServletHelper");
		}

		List<FinArFundsInReconRecord> reconRecordList = null;
		_log.debug("----------start cekProsesVAFundIn-------- ");
		_log.debug("paymentIdentifier: "+paymentIdentifier);
		try {			
				_log.info("Processing reportType VA");
				/*
				 * If it is a VA report then used the VA number and the payment
				 * amount along with payment type
				 */
				Query q = em.createQuery("select o from FinArFundsInReconRecord o where o.nomorReff = :virtualAccountNumber" +
						" and o.reconcilliationRecordTimestamp is not null and o.uniquePayment = '"+ uniquePayment +"' and o.finArFundsIdReportTime.reportTimeId = "+VeniceConstants.FIN_AR_FUNDS_IN_REPORT_TIME_REAL_TIME);
				q.setParameter("virtualAccountNumber", paymentIdentifier);
				reconRecordList = (List<FinArFundsInReconRecord>) q.getResultList();
			
		} catch (Exception e) {
			String errMsg = "An exception occured while finding existing reconciliation record:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
		}
		if (!reconRecordList.isEmpty()) {
			_log.info("reconRecord is not empty");
			_log.debug("----------end cekProsesVAFundIn result false -------- ");
			return reconRecordList;
		}
		_log.info("\n Recon record empty");
		_log.debug("----------end cekProsesVAFundIn result true -------- ");
		return null;
	}
	
}
