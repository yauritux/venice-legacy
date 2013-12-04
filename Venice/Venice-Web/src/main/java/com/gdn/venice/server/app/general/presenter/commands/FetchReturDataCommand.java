package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenReturSessionEJBRemote;
import com.gdn.venice.persistence.VenRetur;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchReturDataCommand implements RafDsCommand {
	
	RafDsRequest request;	
	
	public FetchReturDataCommand(RafDsRequest request) {
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
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();		
			List<VenRetur> returList = null;
			int firstResult = request.getStartRow();
			if(criteria == null){
				String select = "select o from VenRetur o";
				returList = sessionHome.queryByRange(select, firstResult, 50);
			}else{
				VenRetur retur = new VenRetur();
				returList = sessionHome.findByVenReturLike(retur, criteria, 0, 0);
			}
			
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			for (VenRetur retur : returList) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENRETUR_RETURID,  Util.isNull(retur.getReturId(), "").toString());
				map.put(DataNameTokens.VENRETUR_WCSRETURID,  Util.isNull(retur.getWcsReturId(), "").toString());
				map.put(DataNameTokens.VENRETUR_RETURDATE, formatter.format(retur.getReturDate()));
				map.put(DataNameTokens.VENRETUR_VENCUSTOMER_CUSTOMERUSERNAME, retur.getVenCustomer() != null && retur.getVenCustomer().getCustomerUserName() != null?retur.getVenCustomer().getCustomerUserName():"");
				map.put(DataNameTokens.VENRETUR_VENRETURSTATUS_ORDERSTATUSID, retur.getVenReturStatus() != null && retur.getVenReturStatus().getOrderStatusId() != null?retur.getVenReturStatus().getOrderStatusId().toString(): "");
				map.put(DataNameTokens.VENRETUR_RMAACTION, Util.isNull(retur.getRmaAction(), "").toString());
				
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
