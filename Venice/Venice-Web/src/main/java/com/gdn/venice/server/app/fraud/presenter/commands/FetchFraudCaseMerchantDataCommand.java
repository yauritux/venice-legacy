package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMerchantSessionEJBRemote;
import com.gdn.venice.persistence.VenMerchant;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseMerchantDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseMerchantDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenMerchantSessionEJBRemote sessionHome = (VenMerchantSessionEJBRemote) locator.lookup(VenMerchantSessionEJBRemote.class, "VenMerchantSessionEJBBean");
			List<VenMerchant> venMerchantList = sessionHome.queryByRange("select o from VenMerchant o inner join o.venParty p", request.getStartRow(), request.getEndRow());
			VenMerchant venMerchant = new VenMerchant();
			for (int i=0;i<venMerchantList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				venMerchant = venMerchantList.get(i);
				
				map.put(DataNameTokens.VENMERCHANT_MERCHANTID, Util.isNull(venMerchant.getMerchantId(), "").toString());
				map.put(DataNameTokens.VENMERCHANT_WCSMERCHANTID, Util.isNull(venMerchant.getWcsMerchantId(), "").toString());				
				map.put(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME, venMerchant.getVenParty()!=null && venMerchant.getVenParty().getFullOrLegalName()!=null?venMerchant.getVenParty().getFullOrLegalName().toString():"");
				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
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
