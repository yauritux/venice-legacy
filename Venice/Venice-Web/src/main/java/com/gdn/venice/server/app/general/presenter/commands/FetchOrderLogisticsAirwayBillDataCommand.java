package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchOrderLogisticsAirwayBillDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderLogisticsAirwayBillDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<LogAirwayBillSessionEJBRemote> airwayBillLocator = null;
		
		try {
			airwayBillLocator = new Locator<LogAirwayBillSessionEJBRemote>();
			
			LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) airwayBillLocator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			String orderId = request.getParams().get(DataNameTokens.VENORDER_ORDERID);
			String query = "select o from LogAirwayBill o left join o.venOrderItem.venOrder r where r.orderId="+orderId;
			
			
			List<LogAirwayBill> airwayBillList = sessionHome.queryByRange(query, 0, 50);

			LogAirwayBill airwayBill = new LogAirwayBill();
						
			for (int i=0;i<airwayBillList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				airwayBill = airwayBillList.get(i);
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBill.getAirwayBillId()!=null?airwayBill.getAirwayBillId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, 
						(airwayBill.getVenOrderItem()!=null && 
								airwayBill.getVenOrderItem().getVenOrder()!=null)?airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, airwayBill.getVenOrderItem()!=null?airwayBill.getVenOrderItem().getWcsOrderItemId():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, airwayBill.getGdnReference());
				map.put(DataNameTokens.LOGAIRWAYBILL_TRACKINGNUMBER, airwayBill.getTrackingNumber());
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, formatter.format(airwayBill.getAirwayBillPickupDateTime()));
				map.put(DataNameTokens.LOGAIRWAYBILL_RECEIVED, formatter.format(airwayBill.getReceived()));
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(airwayBillList.size());
			rafDsResponse.setEndRow(request.getStartRow()+airwayBillList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				airwayBillLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
