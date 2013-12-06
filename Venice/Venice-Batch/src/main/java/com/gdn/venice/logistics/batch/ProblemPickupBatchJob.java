package com.gdn.venice.logistics.batch;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.bpmenablement.BPMAdapter;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.util.HolidayUtil;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogProviderAgreement;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.util.VeniceConstants;

/**
 * ProblemPickupBatchJob.java
 * 
 * This batch job identifies the order items that are problem pickup status and
 * sets the order item status thereby triggering publishing of PP
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProblemPickupBatchJob {
	private final static String ORDERID = "orderId";
	private static final String LOGISTICSPICKUPPROBLEMINVESTIGATION = "Logistics Pickup Problem Investigation";
	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public ProblemPickupBatchJob() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.logistics.batch.ProblemPickupBatchJob");
	}

	/**
	 * Finds the problem pickup order items by looking at the time they are
	 * created and the current status based on teh pickup time commitment of the
	 * logistics provider and sets their status to PP thus triggering the
	 * publish.
	 * 
	 * @return true if the method succeeds else false
	 */
	private Boolean findAndTriggerPPStatusOrderItems() {
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			LogLogisticsProviderSessionEJBRemote logisticsProviderHome = (LogLogisticsProviderSessionEJBRemote) locator
					.lookup(LogLogisticsProviderSessionEJBRemote.class, "LogLogisticsProviderSessionEJBBean");

			LogProviderAgreementSessionEJBRemote providerAgreementHome = (LogProviderAgreementSessionEJBRemote) locator
					.lookup(LogProviderAgreementSessionEJBRemote.class, "LogProviderAgreementSessionEJBBean");

			VenOrderItemSessionEJBRemote venOrderItemHome = (VenOrderItemSessionEJBRemote) locator
					.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");

			VenOrderItemStatusHistorySessionEJBRemote venOrderItemStatusHistoryHome = (VenOrderItemStatusHistorySessionEJBRemote) locator
					.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");

			//Get the logistics provider list
			_log.debug("get the logistics provider list");
			List<LogLogisticsProvider> logisticsProviderList = logisticsProviderHome.queryByRange("select o from LogLogisticsProvider o", 0, 0);
			for (LogLogisticsProvider provider : logisticsProviderList) {
				_log.debug("check provider: "+provider.getLogisticsProviderCode());
				// Get the pickup time commitment from the logistics providers
				// agreement
				List<LogProviderAgreement> providerAgreementList = providerAgreementHome
						.queryByRange("select o from LogProviderAgreement o where o.logLogisticsProvider.logisticsProviderId = " + provider.getLogisticsProviderId(), 0, 0);
				LogProviderAgreement logProviderAgreement = null;

				Integer providerCommitment = 0;
				if (providerAgreementList != null && !providerAgreementList.isEmpty()) {
					logProviderAgreement = providerAgreementList.get(0);

					providerCommitment = logProviderAgreement.getPickupTimeCommitment();
				}

				ArrayList<VenOrderItem> venOrderItemList = (ArrayList<VenOrderItem>) venOrderItemHome
						.queryByRange(
								"select o from VenOrderItem o where o.venOrderStatus.orderStatusId ="
										+ VeniceConstants.VEN_ORDER_STATUS_ES
										+ " and o.logLogisticService.logLogisticsProvider.logisticsProviderId ="
										+ provider.getLogisticsProviderId(), 0, 0);

				_log.debug("order list size: "+venOrderItemList.size());
				ArrayList<VenOrderItem> ppOrderItemItemList = new ArrayList<VenOrderItem>();
				for (VenOrderItem item : venOrderItemList) {
					// Get the status history
					List<VenOrderItemStatusHistory> venOrderItemStatusHistoryList = venOrderItemStatusHistoryHome
							.queryByRange(
									"select o from VenOrderItemStatusHistory o where o.id.orderItemId ="
											+ item.getOrderItemId()
											+ " and o.venOrderStatus.orderStatusId ="
											+ VeniceConstants.VEN_ORDER_STATUS_ES
											+ " order by o.id.historyTimestamp desc", 0, 0);
					if (venOrderItemStatusHistoryList != null && !venOrderItemStatusHistoryList.isEmpty()) {
						//Use the last element because it may have been ES multiple times
						VenOrderItemStatusHistory hist = venOrderItemStatusHistoryList.get(venOrderItemStatusHistoryList.size() -1);
						Calendar itemDueDate = Calendar.getInstance();
						Date itemESStatusDate = hist.getId().getHistoryTimestamp();						
						itemDueDate.setTime(itemESStatusDate);
						
						//Add 1 day to the due date for each holiday and weekend
						int daysToCheck = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - itemDueDate.get(Calendar.DAY_OF_YEAR);
						Calendar checkDate = Calendar.getInstance();
						checkDate.setTime(itemESStatusDate);
						for(int i = 0; i <= daysToCheck; ++i){
							//increment the day to check (first time through will be the ES date)
							checkDate.add(Calendar.DAY_OF_YEAR, i); 
							//If the check day is a holiday or weekend then add 1 to the due date
							if(HolidayUtil.isHolidayOrWeekend(checkDate.getTime())){
								itemDueDate.add(Calendar.DAY_OF_YEAR, 1);
							}
						}
						
						//Add one to the provider commitment because there is one day leeway  for lateness
						itemDueDate.add(Calendar.DAY_OF_YEAR, providerCommitment + 1);											
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
						_log.info("Item:" + item.getWcsOrderItemId() + " ItemDueDate:" + sdf.format(itemDueDate.getTime()));

						Calendar now = Calendar.getInstance();
						_log.info("Now:" + sdf.format(now.getTime()));
						_log.info("Comparison:" + itemDueDate.compareTo(now));

						if (itemDueDate.compareTo(now) < 0) {
							ppOrderItemItemList.add(item);
						}
					}
				}
				
				_log.info("ppOrderItemItemList size: "+ppOrderItemItemList.size());
				ArrayList<String> orderIdList = new ArrayList<String>();
				for (VenOrderItem lateItem : ppOrderItemItemList) {
					VenOrderStatus venOrderStatus = new VenOrderStatus();
					venOrderStatus.setOrderStatusId(VeniceConstants.VEN_ORDER_STATUS_PP);
					lateItem.setVenOrderStatus(venOrderStatus);
					lateItem.getVenOrder().getOrderId();
					Long orderId = lateItem.getVenOrder().getOrderId();
					if (!orderIdList.contains(orderId.toString())) {
						orderIdList.add(orderId.toString());
					}
				}
				// Merge the list of items... triggering the publish etc.
				if (!ppOrderItemItemList.isEmpty()) {
					_log.debug("merge order item, publish status");
					venOrderItemHome.mergeVenOrderItemList(ppOrderItemItemList);
				}
				for (String orderId : orderIdList) {
					try {
						Properties properties = new Properties();
						properties.load(new FileInputStream(BPMAdapter.WEBAPI_PROPERTIES_FILE));
						String userName = properties.getProperty("javax.xml.rpc.security.auth.username");
						BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
						bpmAdapter.synchronize();

						HashMap<String, String> taskData = new HashMap<String, String>();
						taskData.put(ORDERID, orderId);

						_log.debug("Task data:" + taskData);
						_log.debug("triggering status");
						bpmAdapter.startBusinessProcess(LOGISTICSPICKUPPROBLEMINVESTIGATION, taskData);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProblemPickupBatchJob problemPickupBatchJob = new ProblemPickupBatchJob();
		_log.info("start problemPickupBatchJob");
		problemPickupBatchJob.findAndTriggerPPStatusOrderItems();
		_log.info("done problemPickupBatchJob");
	}
}
