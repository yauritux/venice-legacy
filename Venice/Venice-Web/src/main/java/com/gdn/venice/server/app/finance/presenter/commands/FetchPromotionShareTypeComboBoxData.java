package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchPromotionShareTypeComboBoxData implements RafDsCommand {
	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest rafDsRequest;
	
	/**
	 * Basic Constructor for Fetch Promotion Share Calc Method data
	 * 
	 * @param request is the request parameters passed to the command 
	 */
	public FetchPromotionShareTypeComboBoxData(RafDsRequest rafDsRequest) {
		this.rafDsRequest = rafDsRequest;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONTYPEID, "0");
		map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONTYPE, "Not Voucher");
	
		dataList.add(map);
		
		return null;
	}

}
