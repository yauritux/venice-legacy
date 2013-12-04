package com.gdn.venice.facade.finance.fundsin;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBLocal;
import com.gdn.venice.facade.FinArFundsInReportSessionEJBLocal;
import com.gdn.venice.facade.VenOrderSessionEJBLocal;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInReport;
import com.gdn.venice.persistence.FinArReconResult;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.util.VeniceConstants;

/**
 * Session bean implementation class as a helper for the funds-in 
 * reconciliation screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "FundsInReconciliationHelperSessionEJBBean")
public class FundsInReconciliationHelperSessionEJBBean implements FundsInReconciliationHelperSessionEJBRemote, FundsInReconciliationHelperSessionEJBLocal {
	protected static Logger _log = null;

	protected Locator<Object> _genericLocator = null;

    /**
     * Default constructor. 
     */
    public FundsInReconciliationHelperSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.finance.fundsin.FundsInReconciliationHelperSessionEJBBean");
		try {
			// Establish a JNDI connection when the bean is started
			this._genericLocator = new Locator<Object>();
		} catch (Exception e) {
			_log.error("An exception occured when looking instantiating the generic locator" + e.getMessage());
			e.printStackTrace();
		}
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.fundsin.FundsInReconciliationHelperSessionEJBRemote
	 * #deleteFundsInReport(String bank, String reportType, String reportIdentifier)
	 */
    public Boolean deleteFundsInReport(List<FinArFundsInReport> finArFundsInReportRecordList){
		_log.debug("deleteFundsInReport()");
		Long startTime = System.currentTimeMillis();
		try {
			FinArFundsInReportSessionEJBLocal fundsInReportHome = (FinArFundsInReportSessionEJBLocal) this._genericLocator
			.lookupLocal(FinArFundsInReportSessionEJBLocal.class, "FinArFundsInReportSessionEJBBeanLocal");

			FinArFundsInReconRecordSessionEJBLocal fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBLocal) this._genericLocator
			.lookupLocal(FinArFundsInReconRecordSessionEJBLocal.class, "FinArFundsInReconRecordSessionEJBBeanLocal");

			VenOrderSessionEJBLocal venOrderHome = (VenOrderSessionEJBLocal) this._genericLocator
			.lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");
			
			/*
			 * Get the existing report from the database using the 
			 * bank, report type and report identifier
			 */
			
//			String reportDesc = bank + " " + reportType + " Report-" + reportIdentifier;
//			_log.debug("Report to be deleted: "+reportDesc);
//			List<FinArFundsInReport> finArFundsInReportList = fundsInReportHome.queryByRange("select o from FinArFundsInReport o where o.reportDesc like '" + reportDesc + "%'", 0, 0);
//			_log.debug("Report list size: "+finArFundsInReportList.size());
			
//			if (finArFundsInReportList == null || finArFundsInReportList.size() > 1 || finArFundsInReportList.size() == 0) {
//				String errorText = "The report identifier provided did not uniquely identify a funds-in report";
//				_log.error(errorText);
//				throw new EJBException(errorText);
//			}
			
//			FinArFundsInReport finArFundsInReport = finArFundsInReportList.get(0);
			
			for(FinArFundsInReport id:finArFundsInReportRecordList){
				
		
			List<FinArFundsInReconRecord> finArFundsInReconRecordList = 
				fundsInReconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where o.finArFundsInReport.paymentReportId= "+id.getPaymentReportId(), 0, 0);
			_log.debug("Recon record list size: "+finArFundsInReconRecordList.size());
			/*
			 * No records then just delete the report and return
			 */
					if(!finArFundsInReconRecordList.isEmpty()){
		//				fundsInReportHome.removeFinArFundsInReport(id);
		//				return true;
		//			}
					
					/*
					 * Check to see if any of the orders that the reports records refer to have more than one payment. 
					 * 
					 * Check that the status each order is VA, C or X only
					 * 
					 * Check that the reconciliation record has not been processed
					 * 
					 * Check that the reconciliation record has not been submitted or approved (must be new or rejected)
					 * 
					 * For the above conditions throw an exception.
					 */
					for (FinArFundsInReconRecord record : finArFundsInReconRecordList) {
						List<VenOrder> venOrderList = venOrderHome.queryByRange("select o from VenOrder o where o.wcsOrderId = '" + record.getWcsOrderId() + "'", 0, 0);
						if(!venOrderList.isEmpty() || venOrderList.size()!=0){
							VenOrder venOrder = venOrderList.get(0);
							_log.debug("venOrderList size: "+venOrderList.size());
							
							if (venOrder.getVenOrderStatus().getOrderStatusId() != VeniceConstants.VEN_ORDER_STATUS_C 
									&& venOrder.getVenOrderStatus().getOrderStatusId() != VeniceConstants.VEN_ORDER_STATUS_VA
									&& venOrder.getVenOrderStatus().getOrderStatusId() != VeniceConstants.VEN_ORDER_STATUS_X) {
								String errorText = "The report references an order that is not status VA or C or X. Reports can only be deleted when they refer to new orders!";
								_log.error(errorText);
								throw new EJBException(errorText);
							}
							if (venOrder.getVenOrderPaymentAllocations().size() > 1) {
								String errorText = "The report references an order with more than one payment. Reports can only be deleted when they refer to orders with one payment!";
								_log.error(errorText);
								throw new EJBException(errorText);
							}
							if (record.getFinArFundsInActionApplied().getActionAppliedId() != VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE) {
								String errorText = "The report references a reconciliation record that has been processed. Reports can only be deleted when they refer to unprocessed reconciliation records!";
								_log.error(errorText);
								throw new EJBException(errorText);
							}
							if(record.getFinApprovalStatus().getApprovalStatusId().equals(VeniceConstants.FIN_APPROVAL_STATUS_NEW) || record.getFinApprovalStatus().getApprovalStatusId().equals(VeniceConstants.FIN_APPROVAL_STATUS_REJECTED) ){
							}else{
								String errorText = "The report references a reconciliation record that has been approved. Reports can only be deleted when they refer to new unapproved reconciliation records!";
								_log.error(errorText);
								throw new EJBException(errorText);
							}
						} else{
							/*
							 * If the payment is not recognized then there is no order refered by the report, so no need to check the order status, we can just delete the reconciliation record.
							 */
							_log.debug("The report references an order with payment not recognized.");
							_log.debug("venOrderList size: "+venOrderList.size());
						}
					}
								
					/*
					 * Nullify the content of the reconciliation record
					 */
					for (FinArFundsInReconRecord record : finArFundsInReconRecordList) {
						List<VenOrder> venOrderList = venOrderHome.queryByRange("select o from VenOrder o where o.wcsOrderId = '" + record.getWcsOrderId() + "'", 0, 0);
						if(!venOrderList.isEmpty() || venOrderList.size()!=0){
							_log.debug("venOrderList size: "+venOrderList.size());
							
//							VenOrder venOrder = venOrderList.get(0);
							record.setReconcilliationRecordTimestamp(null);
							//record.setVenOrderPayment(null); - commented out because this FK must remain
							record.setFinArFundsInReport(null);
							record.setProviderReportPaymentDate(null);
							record.setProviderReportPaymentId(null);
							record.setProviderReportPaidAmount(null);
							record.setProviderReportFeeAmount(null);
							record.setPaymentConfirmationNumber(null);
							record.setRemainingBalanceAmount(record.getPaymentAmount());
							FinArReconResult finArReconResult = new FinArReconResult();
							finArReconResult.setReconResultId(VeniceConstants.FIN_AR_RECON_RESULT_NONE);
							record.setFinArReconResult(finArReconResult);
								
							/*
							* Merge the record
							*/
							fundsInReconRecordHome.mergeFinArFundsInReconRecord(record);
							_log.debug("Merged the recon record");
						} else{
							/*
							 * If the payment is not recognized then there is no order refered by the report, so we can just delete the reconciliation record.
							 */
							fundsInReconRecordHome.removeFinArFundsInReconRecord(record);
							_log.debug("Removed the recon record");
						}
					}
						
						}
				/*
				 * Remove the report
				 */
				fundsInReportHome.removeFinArFundsInReport(id);
				_log.debug("Removed the report");
			}
			
			

		} catch (Exception e) {
			String errMsg = "An Exception occured when deleting a funds-in report:";
			_log.error(errMsg + e.getMessage());
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		} finally{
			try{
				if(_genericLocator!=null){
					_genericLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Long endTime = System.currentTimeMillis();
		Long duration = endTime - startTime;
		_log.debug("deleteFundsInReport()" + " completed in " + duration + "ms");
		return true;
    }
}
