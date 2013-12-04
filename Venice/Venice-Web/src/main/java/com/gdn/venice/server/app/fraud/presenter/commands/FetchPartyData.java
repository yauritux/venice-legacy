package com.gdn.venice.server.app.fraud.presenter.commands;

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
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Party
 * 
 * @author Roland
 */

public class FetchPartyData implements RafDsCommand {

	RafDsRequest request;
	
	public FetchPartyData(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			VenPartySessionEJBRemote sessionHome = (VenPartySessionEJBRemote) locator.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");		
			List<VenParty> partyList = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from VenParty o where o.venPartyType.partyTypeId not in (5)";			
				partyList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				VenParty vp = new VenParty();
				partyList = sessionHome.findByVenPartyLike(vp, criteria, 0, 0);
			}
			
			for(int i=0; i<partyList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenParty list = partyList.get(i);
				map.put(DataNameTokens.VENPARTY_PARTYID, Util.isNull(list.getPartyId(), "").toString());
				map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, Util.isNull(list.getFullOrLegalName(), "").toString());				
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
