package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchManualJournalPartyDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchManualJournalPartyDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<VenParty> partyLocator = null;
		
		try {
			partyLocator = new Locator<VenParty>();
			
			VenPartySessionEJBRemote sessionHome = (VenPartySessionEJBRemote) partyLocator
			.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			
			List<VenParty> partyList = null;
			
			VenParty party = new VenParty();

			if (criteria!=null) {
				partyList = sessionHome.findByVenPartyLike(party, criteria, 0, 0);
			} else {
				String select = "select o from VenParty o ";
				partyList = sessionHome.queryByRange(select, 0, 50);
			}
			
			
			for (int i=0;i<partyList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				party = partyList.get(i);
				
				map.put(DataNameTokens.VENPARTY_PARTYID, (party.getPartyId()!=null)?party.getPartyId().toString():"");
				map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, party.getFullOrLegalName());
			
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
				partyLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
