package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Delivery Status Tracking
 * 
 * @author Roland
 */

public class UpdateDeliveryStatusTrackingDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateDeliveryStatusTrackingDataCommand(RafDsRequest request) {
		this.request=request;
	}
	
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		String airwayBillId = null;
		Long service =null;
		List<LogAirwayBill> logAirwayBillList = new ArrayList<LogAirwayBill>();
		List<VenOrderItem> orderItemList = new ArrayList<VenOrderItem>();
		List<LogLogisticService> logisticServiceList = new ArrayList<LogLogisticService>();
		List<HashMap<String,String >> dataList = request.getData();	

		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID)) {
					airwayBillId=data.get(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
				} else if(key.equals(DataNameTokens.LOGAIRWAYBILL_SERVICE)){
					service =new Long (data.get(key));
				}
			}								
		}

		//update delivery status tracking untuk mengubah logistic service
		//update logistic service id di order item, kemudian logistic service code dan logistic provider id di tabel log airway bill
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			LogAirwayBillSessionEJBRemote airwayBillSessionHome = (LogAirwayBillSessionEJBRemote) locator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator
			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			LogLogisticServiceSessionEJBRemote logisticServiceSessionHome = (LogLogisticServiceSessionEJBRemote) locator
			.lookup(LogLogisticServiceSessionEJBRemote.class, "LogLogisticServiceSessionEJBBean");
			
			
			//get airwaybill list
			String query = "select o from LogAirwayBill o where o.airwayBillId =  "+ airwayBillId;		
			logAirwayBillList = airwayBillSessionHome.queryByRange(query, 0, 0);	
						
			//get order item list
			String queryOrderItem = "select o from VenOrderItem o where o.orderItemId =  "+ logAirwayBillList.get(0).getVenOrderItem().getOrderItemId();		
			orderItemList = orderItemSessionHome.queryByRange(queryOrderItem, 0, 0);
						
			//get logistic service list
			String querySerivce = "select o from LogLogisticService o where o.logisticsServiceId =  "+ service;		
			logisticServiceList = logisticServiceSessionHome.queryByRange(querySerivce, 0, 0);
			
										
			//set value of logistic service in venOrderItem			
			LogLogisticService logisticService = new LogLogisticService();
			logisticService.setLogisticsServiceId(service);
			
			VenOrderItem item = orderItemList.get(0);
			item.setLogLogisticService(logisticService);
			
			//set value of logistic provider and logistic service in logAirwayBill	
			LogLogisticsProvider logisticProvider = new LogLogisticsProvider();
			logisticProvider.setLogisticsProviderId(logisticServiceList.get(0).getLogLogisticsProvider().getLogisticsProviderId());
						
			LogAirwayBill awb = logAirwayBillList.get(0);
			awb.setLogLogisticsProvider(logisticProvider);
			awb.setService(logisticServiceList.get(0).getServiceCode());
			
			airwayBillSessionHome.mergeLogAirwayBill(awb);	
			orderItemSessionHome.mergeVenOrderItem(item);
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
