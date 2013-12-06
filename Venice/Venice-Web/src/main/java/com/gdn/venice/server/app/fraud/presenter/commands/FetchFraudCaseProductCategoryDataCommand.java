package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenProductCategorySessionEJBRemote;
import com.gdn.venice.persistence.VenProductCategory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseProductCategoryDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseProductCategoryDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenProductCategorySessionEJBRemote sessionHome = (VenProductCategorySessionEJBRemote) locator.lookup(VenProductCategorySessionEJBRemote.class, "VenProductCategorySessionEJBBean");
			List<VenProductCategory> venProductCategoryList = sessionHome.queryByRange("select o from VenProductCategory o where o.level = 3", request.getStartRow(), request.getEndRow());
			VenProductCategory venProductCategory = new VenProductCategory();
			for (int i=0;i<venProductCategoryList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				venProductCategory = venProductCategoryList.get(i);
				
				map.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORYID, Util.isNull(venProductCategory.getProductCategoryId(), "").toString());
				map.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, Util.isNull(venProductCategory.getProductCategory(), "").toString());				
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
