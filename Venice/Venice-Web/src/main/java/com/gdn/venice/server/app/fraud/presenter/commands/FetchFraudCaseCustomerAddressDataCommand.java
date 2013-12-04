package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseCustomerAddressDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseCustomerAddressDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			String orderId=request.getParams().get(DataNameTokens.VENORDER_ORDERID);			
			VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");			
			VenPartyAddressSessionEJBRemote partyAddressSessionHome = (VenPartyAddressSessionEJBRemote) locator.lookup(VenPartyAddressSessionEJBRemote.class, "VenPartyAddressSessionEJBBean");			
			List<VenOrderAddress> orderAddressList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.wcsOrderId = '"+orderId+"'", 0, 1);
			VenAddress address =null;
				if (orderAddressList.size()>0) {
					address = orderAddressList.get(0).getVenAddress();
				}else{
					List<VenPartyAddress> partyAddressList = partyAddressSessionHome.queryByRange("select o from VenPartyAddress o where o.venParty.partyId = (select a.venParty.partyId from VenCustomer a where a.customerId = (select b.venCustomer.customerId from VenOrder b where b.wcsOrderId ='"+orderId+"')) ", 0, 1);
					address = partyAddressList.get(0).getVenAddress();
				}
				if (address!=null) {
					HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS,
						(Util.isNull(address.getStreetAddress1(), "").toString()) +
						(" "+Util.isNull(address.getStreetAddress2(), "").toString()) + "<br />" + 
						(Util.isNull(address.getKelurahan(), "").toString()) + ", " +
						(Util.isNull(address.getKecamatan(), "").toString()) + "<br />" +
						(address.getVenCity()!=null ?(address.getVenCity().getCityName()!=null?address.getVenCity().getCityName():""):"") + " " +
						(Util.isNull(address.getPostalCode(), "").toString()) + "<br />" + 
						(address.getVenState()!=null?(address.getVenState().getStateName()!=null?address.getVenState().getStateName():""):"") + ", " + 
						(address.getVenCountry()!=null && address.getVenCountry().getCountryName()!=null?address.getVenCountry().getCountryName().toString():""));					
				dataList.add(map);
				
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(dataList.size());
				rafDsResponse.setEndRow(request.getStartRow() + dataList.size());
			} else {
			
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(0);
				rafDsResponse.setEndRow(request.getStartRow());
			}
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
