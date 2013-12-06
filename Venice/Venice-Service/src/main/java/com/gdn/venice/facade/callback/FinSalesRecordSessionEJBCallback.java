package com.gdn.venice.facade.callback;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.bpmenablement.BPMAdapter;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBLocal;
import com.gdn.venice.facade.FinSalesRecordSessionEJBLocal;
import com.gdn.venice.facade.VenOrderItemSessionEJBLocal;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBLocal;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.util.VeniceConstants;

/**
 * FinSalesRecordSessionEJBCallback.java - a callback class for trapping 
 * approval of the sales records and creating journal entries.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FinSalesRecordSessionEJBCallback implements SessionCallback{

	protected static Logger _log = null;
	
	//Constants for BPM adapter call
	private static final String WCSORDERID = "wcsOrderId";
	private static final String FUNDINRECONRECORDID = "fundInReconRecordId";
	private static final String FINANCECASHRECEIVEANDSALESJOURNALRECONCILIATION = "Finance Cash Receive and Sales Journal Reconciliation";

	/**
	 * Default constructor.
	 */
	public FinSalesRecordSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.FinSalesRecordSessionEJBCallback");
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
	@Override
	public Boolean onPreMerge(Object businessObject) {
		_log.debug("onPreMerge");
		
		FinSalesRecord finSalesRecord = (FinSalesRecord) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
						
			FinSalesRecordSessionEJBLocal salesRecordHome = (FinSalesRecordSessionEJBLocal) locator
			.lookupLocal(FinSalesRecordSessionEJBLocal.class, "FinSalesRecordSessionEJBBeanLocal");
			
			FinanceJournalPosterSessionEJBLocal financeJournalPosterHome = (FinanceJournalPosterSessionEJBLocal) locator
			.lookupLocal(FinanceJournalPosterSessionEJBLocal.class, "FinanceJournalPosterSessionEJBBeanLocal");

			_log.debug("query existing sales record");
			List<FinSalesRecord> salesRecordList = salesRecordHome.queryByRange("select o from FinSalesRecord o where o.salesRecordId = " + finSalesRecord.getSalesRecordId(), 0, 0);
			_log.debug("salesRecordList size: "+salesRecordList.size());
			if(salesRecordList.size()>0){
				FinSalesRecord existingSalesRecord = salesRecordList.get(0);
				
				_log.debug("existingSalesRecord id: "+existingSalesRecord.getSalesRecordId());
				_log.debug("existingSalesRecord status: "+existingSalesRecord.getFinApprovalStatus().getApprovalStatusId());
				_log.debug("newSalesRecord status: "+finSalesRecord.getFinApprovalStatus().getApprovalStatusId());
				/*
				 * If the status of the sales record is approved for the first time then create the journal entries
				 */
				if(finSalesRecord.getFinApprovalStatus().getApprovalStatusId() == VeniceConstants.FIN_APPROVAL_STATUS_APPROVED
						&& existingSalesRecord.getFinApprovalStatus().getApprovalStatusId() != VeniceConstants.FIN_APPROVAL_STATUS_APPROVED){
					_log.debug("approval status change to approved");
					/*
					 * The journal transactions are only to be created once if there are
					 * no existing sales journal transactions
					 */
					if(finSalesRecord.getFinJournalTransactions() == null || finSalesRecord.getFinJournalTransactions().isEmpty()){
						_log.debug("\n Start post sales journal");
						financeJournalPosterHome.postSalesJournalTransaction(finSalesRecord.getSalesRecordId(), VeniceConstants.VEN_GDN_PPN_RATE > 0);
						_log.debug("\n Done post sales journal");										
					}
					
					/*
					 * Setelah sales journal di create.
					 * 
					 * after the last order item become cx, reconcile sales journal vs cash receive journal. 
					 * because it is the same as funds in reconcile, we just check the reconcilement status, if not all fund received, then trigger bpm.
					 */
					VenOrderItemSessionEJBLocal venOrderItemHome = (VenOrderItemSessionEJBLocal) locator
					.lookupLocal(VenOrderItemSessionEJBLocal.class, "VenOrderItemSessionEJBBeanLocal");
					
					FinArFundsInReconRecordSessionEJBLocal fundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBLocal) locator
					.lookupLocal(FinArFundsInReconRecordSessionEJBLocal.class, "FinArFundsInReconRecordSessionEJBBeanLocal");
					
					_log.info("\n Start reconcile sales journal vs cash receive journal");
					Boolean allOrderItemCx=false;
					List< VenOrderItem> venOrderItemList =  venOrderItemHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = " + finSalesRecord.getVenOrderItem().getVenOrder().getOrderId(), 0, 0);
	
					_log.debug("\n Check order item list status");
					for (VenOrderItem item : venOrderItemList) {
						if (item.getVenOrderStatus().getOrderStatusId() == VeniceConstants.VEN_ORDER_STATUS_CX) {
							_log.debug("\n Order item status is CX");
							allOrderItemCx=true;
						}else{
							_log.debug("\n Order item status is not CX");
							allOrderItemCx=false;
							break;
						}
					}
						
					if(allOrderItemCx==true){
						_log.info("\n All order item status is already CX, Check is cash receive journal exist");
						@SuppressWarnings("unused")
						Boolean cashReceiveJournalExist=false;
						List<FinArFundsInReconRecord> fundInRecordList = fundsInReconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where o.wcsOrderId = '" +  finSalesRecord.getVenOrderItem().getVenOrder().getWcsOrderId() + "'", 0, 0);
	
						//there might be an order which has multiple payment, therefore there is multiple recon record
						//here we check all recon record id must have cash receive journal.
						for (FinArFundsInReconRecord reconRecord : fundInRecordList) {
							//here we check the reconciliation status, if it's not all funds received, trigger bpm.
							long reconStatus = reconRecord.getFinArReconResult().getReconResultId();
							if(reconStatus!=VeniceConstants.FIN_AR_RECON_RESULT_ALL){
								System.out.println("\n Recon record status not all funds received");
								
								Properties properties = new Properties();
								properties.load(new FileInputStream(BPMAdapter.WEBAPI_PROPERTIES_FILE));
								String userName = properties.getProperty("javax.xml.rpc.security.auth.username");		
								String password = BPMAdapter.getUserPasswordFromLDAP(userName);
								BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, password);
								bpmAdapter.synchronize();
								
								HashMap<String, String> taskData = new HashMap<String, String>();
								taskData.put(FUNDINRECONRECORDID, reconRecord.getReconciliationRecordId().toString());
								taskData.put(WCSORDERID, reconRecord.getWcsOrderId());							
	
								try {
									System.out.println("\n Starting bpm");
									bpmAdapter.startBusinessProcess(FINANCECASHRECEIVEANDSALESJOURNALRECONCILIATION, taskData);
									System.out.println("\n Done starting bpm");
								} catch (Exception e) {
									System.out.println("\n Error when starting bpm");
									e.printStackTrace();
								}
							}else{
								_log.info("\n Recon record status all funds received, no need to trigger bpm");
							}
						}			
					}
				}
			}
		} catch (Exception e) {
			String errMsg = "An exception occured when processing the callback for FinSalesRecordSessionEJBCallback:" + e.getMessage();
			_log.error(errMsg,e);
			e.printStackTrace();
			return Boolean.FALSE;
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
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
	@Override
	public Boolean onPostMerge(Object businessObject) {
		_log.debug("onPostMerge");
		
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
}
