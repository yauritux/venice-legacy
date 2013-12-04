package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote;
import com.gdn.venice.persistence.LogAirwayBillRetur;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchReturLogisticsAirwayBillDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchReturLogisticsAirwayBillDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<LogAirwayBillReturSessionEJBRemote> locator = null;
		
		try {
			locator = new Locator<LogAirwayBillReturSessionEJBRemote>();
			
			LogAirwayBillReturSessionEJBRemote sessionHome = (LogAirwayBillReturSessionEJBRemote) locator
			.lookup(LogAirwayBillReturSessionEJBRemote.class, "LogAirwayBillReturSessionEJBBean");
			
			String returId = request.getParams().get(DataNameTokens.VENRETUR_RETURID);
			String query = "select o from LogAirwayBillRetur o left join o.venReturItem.venRetur r where r.returId="+returId;
			
			
			List<LogAirwayBillRetur> airwayBillReturList = sessionHome.queryByRange(query, 0, 50);
			
			for (int i=0;i<airwayBillReturList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				LogAirwayBillRetur airwayBill = new LogAirwayBillRetur();
				airwayBill = airwayBillReturList.get(i);
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.LOGAIRWAYBILLRETUR_AIRWAYBILLID, airwayBill.getAirwayBillReturId()!=null?airwayBill.getAirwayBillReturId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILLRETUR_VENRETURITEM_VENRETUR_WCSRETURID, 
						(airwayBill.getVenReturItem()!=null && airwayBill.getVenReturItem().getVenRetur()!=null)?airwayBill.getVenReturItem().getVenRetur().getWcsReturId():"");
				map.put(DataNameTokens.LOGAIRWAYBILLRETUR_VENRETURITEM_WCSRETURITEMID, airwayBill.getVenReturItem()!=null?airwayBill.getVenReturItem().getWcsReturItemId():"");
				map.put(DataNameTokens.LOGAIRWAYBILLRETUR_GDNREFERENCE, airwayBill.getGdnReference());
				map.put(DataNameTokens.LOGAIRWAYBILLRETUR_TRACKINGNUMBER, airwayBill.getTrackingNumber());
				map.put(DataNameTokens.LOGAIRWAYBILLRETUR_AIRWAYBILLPICKUPDATETIME, formatter.format(airwayBill.getAirwayBillPickupDateTime()));
				map.put(DataNameTokens.LOGAIRWAYBILLRETUR_RECEIVED, formatter.format(airwayBill.getReceived()));
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(airwayBillReturList.size());
			rafDsResponse.setEndRow(request.getStartRow()+airwayBillReturList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
