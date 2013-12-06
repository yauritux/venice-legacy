package com.gdn.venice.facade.finance.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinApInvoiceSessionEJBLocal;
import com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBLocal;
import com.gdn.venice.facade.FinApPaymentSessionEJBLocal;
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBLocal;
import com.gdn.venice.facade.FinSalesRecordSessionEJBLocal;
import com.gdn.venice.facade.VenOrderSessionEJBLocal;
import com.gdn.venice.facade.util.FinancePeriodUtil;
import com.gdn.venice.persistence.FinAccount;
import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.persistence.FinApPaymentType;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.persistence.FinArFundsInRefund;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.reporting.SalesSettlementBatchJob;
import com.gdn.venice.util.VeniceConstants;

/**
 * Session bean implementation class for finance payment creation
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
@Stateless(mappedName = "FinancePaymentCreatorSessionEJBBean")
public class FinancePaymentCreatorSessionEJBBean implements FinancePaymentCreatorSessionEJBRemote, FinancePaymentCreatorSessionEJBLocal {

	protected static Logger _log = null;
	protected Locator<Object> _genericLocator = null;
	
    /**
     * Default constructor. 
     */
    public FinancePaymentCreatorSessionEJBBean() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.finance.payment.FinancePaymentCreatorSessionEJBBean");

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
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #createRefundPayment(			
	 * 		Long finArFundsInRefundId,
	 * 		ArrayList<Long> manualJournalTransactionList,
	 *		Double penaltyAmount,
	 *		Double paymentAmount,
	 *		Long bankAccountId)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean createRefundPayment(
			Long finArFundsInRefundId,
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId) {
		_log.debug("createRefundPayment()");
		Long startTime = System.currentTimeMillis();
		try {
			FinArFundsInRefundSessionEJBLocal refundRecordHome = (FinArFundsInRefundSessionEJBLocal) this._genericLocator
			.lookupLocal(FinArFundsInRefundSessionEJBLocal.class, "FinArFundsInRefundSessionEJBBeanLocal");

			FinApPaymentSessionEJBLocal finApPaymentHome = (FinApPaymentSessionEJBLocal) this._genericLocator
					.lookupLocal(FinApPaymentSessionEJBLocal.class, "FinApPaymentSessionEJBBeanLocal");

			FinApManualJournalTransactionSessionEJBLocal finApManualJournalTransactionHome = (FinApManualJournalTransactionSessionEJBLocal) this._genericLocator
			.lookupLocal(FinApManualJournalTransactionSessionEJBLocal.class, "FinApManualJournalTransactionSessionEJBBeanLocal");

			VenOrderSessionEJBLocal venOrderHome = (VenOrderSessionEJBLocal) this._genericLocator
			.lookupLocal(VenOrderSessionEJBLocal.class, "VenOrderSessionEJBBeanLocal");

			//Check the refund record id is valid
			List<FinArFundsInRefund> finArFundsInRefundList = refundRecordHome.queryByRange("select o from FinArFundsInRefund o where o.refundRecordId = " + finArFundsInRefundId , 0, 0);
			if (finArFundsInRefundList.isEmpty()) {
				throw new EJBException("The finArFundsInRefundId passed to createRefundPayment is invalid. Refund entry does not exist!");
			}
			
			FinArFundsInRefund finArFundsInRefund = finArFundsInRefundList.get(0);
			FinApPayment payment = new FinApPayment();
			
			payment.setAmount(new BigDecimal(paymentAmount));
			payment.setPenaltyAmount(new BigDecimal(penaltyAmount));

			/*
			 *  Get the party for the payment from the bank to which the payment was made
			 *  if the payment lines up with a VenOrderPayment
			 *  
			 *  If not than use the bank account ID to determine the bank as the party
			 *  for the refund
			 */
			if(finArFundsInRefund.getFinArFundsInReconRecord().getVenOrderPayment() != null){
				if(finArFundsInRefund.getFinArFundsInReconRecord().getFinArFundsInActionApplied().getActionAppliedId().equals(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REFUNDED_BANK)){
						payment.setVenParty(finArFundsInRefund.getFinArFundsInReconRecord().getVenOrderPayment().getVenBank().getVenParty());
				}else{
						List<VenOrder> venOrderList = venOrderHome.queryByRange("select o from VenOrder o where o.wcsOrderId= '" + finArFundsInRefund.getFinArFundsInReconRecord().getWcsOrderId()+"'" , 0, 0);
						if (venOrderList.isEmpty()) {
							throw new EJBException("The finArFundsInRefundId passed to createRefundPayment is invalid. Order entry does not exist!");
						}
						payment.setVenParty(venOrderList.get(0).getVenCustomer().getVenParty());						
				}
			}else{
				payment.setVenParty(finArFundsInRefund.getFinArFundsInReconRecord().getFinArFundsInReport().getFinArFundsInReportType().getVenBank().getVenParty());
			}

			FinApPaymentType finApPaymentType = new FinApPaymentType();
			finApPaymentType.setPaymentTypeId(VeniceConstants.FIN_AP_PAYMENT_TYPE_REFUND);
			payment.setFinApPaymentType(finApPaymentType);

			FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
			finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
			payment.setFinApprovalStatus(finApprovalStatus);

			payment.setFinPeriod(FinancePeriodUtil.getCurrentPeriod());
			
			FinAccount bankAccount = new FinAccount();
			bankAccount.setAccountId(bankAccountId);
			payment.setFinAccount(bankAccount);
						
			/*
			 * persist the payment 
			 * o Note: journaling is done on approval
			 */
			payment = finApPaymentHome.persistFinApPayment(payment);
			
			/*
			 * Set the payment to the refund record and merge it.
			 */
			finArFundsInRefund.setFinApPayment(payment);
			refundRecordHome.mergeFinArFundsInRefund(finArFundsInRefund);
			
			//Flag the manual journal entries with the payment id.
			for(Long manualJournalTransactionId:manualJournalTransactionList){
				List<FinApManualJournalTransaction> finApManualJournalTransactionList  = finApManualJournalTransactionHome.queryByRange("select o from FinApManualJournalTransaction o where o.manualJournalTransactionId = " + manualJournalTransactionId, 0, 0);
				if(finApManualJournalTransactionList.isEmpty()){
					throw new EJBException("An invalid manualJournalTransactionId was passed to createMerchantPayment. Manual journal entry does not exist!");
				}
				FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(0);
				finApManualJournalTransaction.setFinApPayment(payment);
				finApManualJournalTransactionHome.mergeFinApManualJournalTransaction(finApManualJournalTransaction);
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when creating refund payment transactions:";
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
		_log.debug("createRefundPayment()" + " completed in " + duration + "ms");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #createMerchantPayment(
	 * 		ArrayList<Long> salesRecordIdList, 
	 *		ArrayList<Long> manualJournalTransactionList,
	 *		Double penaltyAmount,
	 *		Double paymentAmount,
	 *		Long bankAccountId,
	 *		Long partyId)
	 */
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean createMerchantPayment(
			ArrayList<Long> salesRecordIdList, 
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId,
			Double pph23Amount) {
		System.out.println("createMerchantPayment()");
		Long startTime = System.currentTimeMillis();
		
		try {
			FinSalesRecordSessionEJBLocal finSalesRecordHome = (FinSalesRecordSessionEJBLocal) this._genericLocator
					.lookupLocal(FinSalesRecordSessionEJBLocal.class, "FinSalesRecordSessionEJBBeanLocal");

			FinApPaymentSessionEJBLocal finApPaymentHome = (FinApPaymentSessionEJBLocal) this._genericLocator
					.lookupLocal(FinApPaymentSessionEJBLocal.class, "FinApPaymentSessionEJBBeanLocal");

			FinApManualJournalTransactionSessionEJBLocal finApManualJournalTransactionHome = (FinApManualJournalTransactionSessionEJBLocal) this._genericLocator
					.lookupLocal(FinApManualJournalTransactionSessionEJBLocal.class, "FinApManualJournalTransactionSessionEJBBeanLocal");

			FinApPayment payment = new FinApPayment();
			FinApPaymentType finApPaymentType = new FinApPaymentType();
			finApPaymentType.setPaymentTypeId(VeniceConstants.FIN_AP_PAYMENT_TYPE_MERCHANT);
			payment.setFinApPaymentType(finApPaymentType);

			FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
			finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
			payment.setFinApprovalStatus(finApprovalStatus);

			payment.setFinJournalTransactions(new ArrayList<FinJournalTransaction>());
			payment.setFinPeriod(FinancePeriodUtil.getCurrentPeriod());
			
			//This list will be used to store the sales records termporarily for flagging later
			List<FinSalesRecord> finSalesRecordListTemp = new ArrayList<FinSalesRecord>();
			
			//iterate sales record id list, set to map
			StringBuilder salesRecordIdAll = new StringBuilder();			
			for (int i=0;i<salesRecordIdList.size();i++) {
				salesRecordIdAll = salesRecordIdAll.append(salesRecordIdList.get(i));
				if (i<salesRecordIdList.size()-1) {
					salesRecordIdAll = salesRecordIdAll.append(",");
				}
			}

			List<FinSalesRecord> finSalesRecordTempList = finSalesRecordHome.queryByRange("select o from FinSalesRecord o where o.salesRecordId in (" + salesRecordIdAll + ")", 0, 0);
			if(finSalesRecordTempList.size()>0){
				HashMap<String,String> map = new HashMap<String,String>();

				for(int i=0;i<finSalesRecordTempList.size();i++){		
					String merchantName = finSalesRecordTempList.get(i).getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName();
					String value = map.get(merchantName)!=null?map.get(merchantName):"";
					if(!value.equals("")) {
						value=value + ",";
					}
					map.put(merchantName, value + finSalesRecordTempList.get(i).getSalesRecordId());
				}
				
				Set set = map.entrySet();
				Iterator i = set.iterator();
				while(i.hasNext()) {
			         Map.Entry me = (Map.Entry)i.next();		
			         System.out.println("map value: "+me.getKey()+"-"+me.getValue());
			         
					// Check the sales record exists
					List<FinSalesRecord> finSalesRecordList = finSalesRecordHome.queryByRange("select o from FinSalesRecord o where o.salesRecordId in (" + me.getValue() + ")", 0, 0);				
					
					for(int j=0;j<finSalesRecordList.size();j++){
						System.out.println("set payment status ready to pay");
						FinSalesRecord finSalesRecord = finSalesRecordList.get(j);
						finSalesRecord.setPaymentStatus(VeniceConstants.FIN_SALES_RECORD_PAYMENT_STATUS_READY_TO_PAY);
						finSalesRecordListTemp.add(finSalesRecord);
					}
					
					payment.setVenParty(finSalesRecordList.get(0).getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty());
					payment.setAmount(new BigDecimal(paymentAmount));
					payment.setPenaltyAmount(new BigDecimal(penaltyAmount));
					payment.setPph23Amount(new BigDecimal(pph23Amount));
					
					FinAccount bankAccount = new FinAccount();
					bankAccount.setAccountId(bankAccountId);
					payment.setFinAccount(bankAccount);

					/*
					 * persist the payment 
					 * o Note: journaling is done on approval
					 */
					payment = finApPaymentHome.persistFinApPayment(payment);
					
					//Flag the manual journal entries with the payment id.
					for(Long manualJournalTransactionId:manualJournalTransactionList){
						List<FinApManualJournalTransaction> finApManualJournalTransactionList  = finApManualJournalTransactionHome.queryByRange("select o from FinApManualJournalTransaction o where o.manualJournalTransactionId = " + manualJournalTransactionId, 0, 0);
						if(finApManualJournalTransactionList.isEmpty()){
							throw new EJBException("An invalid manualJournalTransactionId was passed to createMerchantPayment. Manual journal entry does not exist!");
						}
						FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(0);
						finApManualJournalTransaction.setFinApPayment(payment);
						finApManualJournalTransactionHome.mergeFinApManualJournalTransaction(finApManualJournalTransaction);
					}
					
					//Flag the SalesRecord entries with the payment
					for(FinSalesRecord finSalesRecord:finSalesRecordListTemp){
						finSalesRecord.setFinApPayment(payment);
					}
					
					//Merge the sales record list
					finSalesRecordHome.mergeFinSalesRecordList(finSalesRecordListTemp);
					
					//generate sales settlement report
					System.out.println("generate sales settlement report from merchant payment");
					SalesSettlementBatchJob batchJob = new SalesSettlementBatchJob();
					batchJob.generateSalesSettlementReport(finSalesRecordList.get(0).getVenOrderItem().getVenMerchantProduct().getVenMerchant(), me.getValue().toString());
					System.out.println("done generate sales settlement report from merchant payment");
			     }
			}
		} catch (Exception e) {
			String errMsg = "An Exception occured when creating merchant payment transactions:";
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
		_log.debug("createMerchantPayment()" + " completed in " + duration + "ms");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote
	 * #createLogisticsPayment(			
	 * 		ArrayList<Long> finApInvoiceIdList, 
	 *		ArrayList<Long> manualJournalTransactionList,
	 *		Double penaltyAmount,
	 *		Double paymentAmount,
	 *		Long bankAccountId)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Boolean createLogisticsPayment(
			ArrayList<Long> finApInvoiceIdList, 
			ArrayList<Long> manualJournalTransactionList,
			Double penaltyAmount,
			Double paymentAmount,
			Long bankAccountId) {
		_log.debug("createLogisticsPayment()");
		Long startTime = System.currentTimeMillis();
		try {
			FinApInvoiceSessionEJBLocal finApInvoiceHome = (FinApInvoiceSessionEJBLocal) this._genericLocator
					.lookupLocal(FinApInvoiceSessionEJBLocal.class, "FinApInvoiceSessionEJBBeanLocal");

			FinApPaymentSessionEJBLocal finApPaymentHome = (FinApPaymentSessionEJBLocal) this._genericLocator
					.lookupLocal(FinApPaymentSessionEJBLocal.class, "FinApPaymentSessionEJBBeanLocal");
			
			FinApManualJournalTransactionSessionEJBLocal finApManualJournalTransactionHome = (FinApManualJournalTransactionSessionEJBLocal) this._genericLocator
			.lookupLocal(FinApManualJournalTransactionSessionEJBLocal.class, "FinApManualJournalTransactionSessionEJBBeanLocal");

			FinApPayment payment = new FinApPayment();
			VenParty providerParty = null;
			FinApPaymentType finApPaymentType = new FinApPaymentType();
			finApPaymentType.setPaymentTypeId(VeniceConstants.FIN_AP_PAYMENT_TYPE_LOGISTICS);
			payment.setFinApPaymentType(finApPaymentType);

			FinApprovalStatus finApprovalStatus = new FinApprovalStatus();
			finApprovalStatus.setApprovalStatusId(VeniceConstants.FIN_APPROVAL_STATUS_NEW);
			payment.setFinApprovalStatus(finApprovalStatus);

			payment.setFinJournalTransactions(new ArrayList<FinJournalTransaction>());
			payment.setFinPeriod(FinancePeriodUtil.getCurrentPeriod());
			
			//This list is used a temporary list to store the invoices for flagging with the payment later
			List<FinApInvoice> finApInvoiceListTemp = new ArrayList<FinApInvoice>();

			/*
			 * Iterate the list of invoices and check they are all for the same party
			 */
			for (Long apInvoiceId: finApInvoiceIdList) {
				// Check the finApInvoiceId
				List<FinApInvoice> finApInvoiceList = finApInvoiceHome.queryByRange("select o from FinApInvoice o where o.apInvoiceId = " + apInvoiceId, 0, 0);
				if (finApInvoiceList.isEmpty()) {
					throw new EJBException("The invoice id passed to createLogisticsPayment is invalid. No invoice exists!");
				}

				FinApInvoice finApInvoice = finApInvoiceList.get(0);
				
				//Add the invoice to the list for processing later
				finApInvoiceListTemp.add(finApInvoice);

				/*
				 * Check that each sales journal/record is for the same merchant
				 */
				if (providerParty == null) {
					providerParty = finApInvoice.getVenParty();
				} else {
					if (!providerParty.equals(finApInvoice.getVenParty())) {
						throw new EJBException("The logistics provider party for the selected invoice must be the same logistics provider!");
					}
				}
			}
			
			payment.setAmount(new BigDecimal(paymentAmount));
			payment.setPenaltyAmount(new BigDecimal(penaltyAmount));
			payment.setVenParty(providerParty);

			FinAccount bankAccount = new FinAccount();
			bankAccount.setAccountId(bankAccountId);
			payment.setFinAccount(bankAccount);

			/*
			 * persist the payment. Note that journaling is done on payment
			 * approval
			 */
			payment = finApPaymentHome.persistFinApPayment(payment);
			
			//Flag the manual journal entries with the payment id.
			for(Long manualJournalTransactionId:manualJournalTransactionList){
				List<FinApManualJournalTransaction> finApManualJournalTransactionList  = finApManualJournalTransactionHome.queryByRange("select o from FinApManualJournalTransaction o where o.manualJournalTransactionId = " + manualJournalTransactionId, 0, 0);
				if(finApManualJournalTransactionList.isEmpty()){
					throw new EJBException("An invalid manualJournalTransactionId was passed to createMerchantPayment. Manual journal entry does not exist!");
				}
				FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(0);
				finApManualJournalTransaction.setFinApPayment(payment);
				finApManualJournalTransactionHome.mergeFinApManualJournalTransaction(finApManualJournalTransaction);
			}
			
			//Flag the invoice entries with the payment
			for(FinApInvoice finApInvoice:finApInvoiceListTemp){
				finApInvoice.setFinApPayment(payment);				
			}
						
			//Merge the sales record list
			finApInvoiceHome.mergeFinApInvoiceList(finApInvoiceListTemp);

		} catch (Exception e) {
			String errMsg = "An Exception occured when creating logistics payment transactions:";
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
		_log.debug("createLogisticsPayment()" + " completed in " + duration + "ms");

		return true;
	}
}
