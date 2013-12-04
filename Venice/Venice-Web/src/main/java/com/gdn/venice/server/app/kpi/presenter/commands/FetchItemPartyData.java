package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchItemPartyData implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchItemPartyData(RafDsRequest request) {
		this.request = request;

	}
//VenParty
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<VenPartySessionEJBRemote> venItemPartyLocator = null;
		try {
			venItemPartyLocator = new Locator<VenPartySessionEJBRemote>();
			VenPartySessionEJBRemote sessionHome = (VenPartySessionEJBRemote) venItemPartyLocator.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			List<VenParty> venItemPartyList =null;
			if(request.getParams().get(DataNameTokens.VENPARTY_PARTYID)!=""){
				venItemPartyList = sessionHome.queryByRange("select o from VenParty o where o.partyId="+request.getParams().get(DataNameTokens.VENPARTY_PARTYID), request.getStartRow(), request.getEndRow());
			}else if(request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID)!=""&&request.getParams().get(DataNameTokens.VENPARTY_PARTYID)==""){
				venItemPartyList = sessionHome.queryByRange("select o from VenParty o where o.venPartyType.partyTypeId="+request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID), request.getStartRow(), request.getEndRow());
			}else{
				venItemPartyList = sessionHome.queryByRange("select o from VenParty o where o.venPartyType.partyTypeId=1 or o.venPartyType.partyTypeId=2", request.getStartRow(), request.getEndRow());
			}

			VenParty venItemParty = new VenParty();
			for (int i=0;i<venItemPartyList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				venItemParty = venItemPartyList.get(i);
				
				map.put(DataNameTokens.VENPARTY_PARTYID, Util.isNull(venItemParty.getPartyId().toString(),"").toString());
				map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, Util.isNull(venItemParty.getFullOrLegalName(),"").toString());
				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venItemPartyList.size());
			rafDsResponse.setEndRow(request.getStartRow()+venItemPartyList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				venItemPartyLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
