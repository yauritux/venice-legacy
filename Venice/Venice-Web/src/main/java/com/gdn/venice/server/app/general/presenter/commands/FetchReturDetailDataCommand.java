package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenReturSessionEJBRemote;
import com.gdn.venice.persistence.VenRetur;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchReturDetailDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchReturDetailDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenReturSessionEJBRemote sessionHome = (VenReturSessionEJBRemote) locator
			.lookup(VenReturSessionEJBRemote.class, "VenReturSessionEJBBean");
						
			VenRetur retur = new VenRetur();

			//Find Retur by Retur Id			
			List<VenRetur> returList = sessionHome.queryByRange("select o from VenRetur o where o.returId="+request.getParams().get(DataNameTokens.VENRETUR_RETURID), 0, 1);

			HashMap<String, String> map = new HashMap<String, String>();
			
			retur = returList.get(0);
			
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			
			map.put(DataNameTokens.VENRETUR_RETURID, Util.isNull(retur.getReturId(), "").toString());
			map.put(DataNameTokens.VENRETUR_WCSRETURID, Util.isNull(retur.getWcsReturId(), "").toString());
			map.put(DataNameTokens.VENRETUR_RETURDATE, formatter.format(retur.getReturDate()));
			map.put(DataNameTokens.VENRETUR_VENRETURSTATUS_ORDERSTATUSCODE, retur.getVenReturStatus() != null && retur.getVenReturStatus().getOrderStatusCode() != null?retur.getVenReturStatus().getOrderStatusCode():"");
			map.put(DataNameTokens.VENRETUR_RMAACTION, Util.isNull(retur.getRmaAction(), "").toString());
			map.put(DataNameTokens.VENRETUR_VENCUSTOMER_CUSTOMERUSERNAME, retur.getVenCustomer() != null && retur.getVenCustomer().getCustomerUserName() != null?retur.getVenCustomer().getCustomerUserName():"");
			map.put(DataNameTokens.VENRETUR_AMOUNT, Util.isNull(retur.getAmount(), "").toString());
			map.put(DataNameTokens.VENRETUR_DELIVEREDDATETIME, formatter.format(retur.getDeliveredDateTime()));
			
			dataList.add(map);

			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(1);
			rafDsResponse.setEndRow(request.getStartRow()+1);
		} catch (Exception e) {
			e.printStackTrace();
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
