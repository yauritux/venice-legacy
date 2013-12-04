package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenReturItemSessionEJBRemote;
import com.gdn.venice.persistence.VenReturItem;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchReturItemDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchReturItemDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<VenReturItemSessionEJBRemote> returItemLocator = null;
		
		try {
			returItemLocator = new Locator<VenReturItemSessionEJBRemote>();
			
			VenReturItemSessionEJBRemote sessionHome = (VenReturItemSessionEJBRemote) returItemLocator
			.lookup(VenReturItemSessionEJBRemote.class, "VenReturItemSessionEJBBean");
						
			List<VenReturItem> returItemList  = sessionHome.queryByRange("select o from VenReturItem o where o.venRetur.returId="+request.getParams().get(DataNameTokens.VENRETUR_RETURID), 0, 0);
			
			for (int i=0;i<returItemList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				VenReturItem returItem = new VenReturItem();
				returItem = returItemList.get(i);
				
				map.put(DataNameTokens.VENRETURITEM_WCSRETURITEMID, returItem.getVenRetur()!=null?returItem.getVenRetur().getWcsReturId():"");
				map.put(DataNameTokens.VENRETURITEM_RETURITEMID, returItem.getVenRetur()!=null?returItem.getVenRetur().getReturId().toString():"");
				map.put(DataNameTokens.VENRETURITEM_RETURITEMID, returItem.getReturItemId()!=null?returItem.getReturItemId().toString():"");
				map.put(DataNameTokens.VENRETURITEM_WCSRETURITEMID, returItem.getWcsReturItemId());
				map.put(DataNameTokens.VENRETURITEM_VENRETURSTATUS_ORDERSTATUSCODE, returItem.getVenReturStatus()!=null?returItem.getVenReturStatus().getOrderStatusCode():"");
				map.put(DataNameTokens.VENRETURITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, returItem.getVenMerchantProduct()!=null?returItem.getVenMerchantProduct().getWcsProductName():"");
				map.put(DataNameTokens.VENRETURITEM_QUANTITY, returItem.getQuantity()!=null?returItem.getQuantity().toString():"");
				map.put(DataNameTokens.VENRETURITEM_TOTAL, returItem.getTotal()!=null?returItem.getTotal().toString():"");
				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(returItemList.size());
			rafDsResponse.setEndRow(request.getStartRow()+returItemList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				returItemLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
