package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderAddressSessionEJBRemote;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenOrderAddress;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchOrderCustomerAddressDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderCustomerAddressDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();			
			VenOrderAddressSessionEJBRemote orderAddressSessionHome = (VenOrderAddressSessionEJBRemote) locator.lookup(VenOrderAddressSessionEJBRemote.class, "VenOrderAddressSessionEJBBean");
						
			//Find Order by Order Id			
			List<VenOrderAddress> orderAddressList = orderAddressSessionHome.queryByRange("select o from VenOrderAddress o where o.venOrder.orderId = " + request.getParams().get(DataNameTokens.VENORDER_ORDERID), 0, 0);
			
			if (orderAddressList.size()>0) {
				for (int i=0;i<orderAddressList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();					
					VenAddress address = orderAddressList.get(i).getVenAddress();
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, address.getAddressId()!=null?address.getAddressId().toString():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, address.getStreetAddress1()!=null?address.getStreetAddress1():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, address.getStreetAddress2()!=null?address.getStreetAddress1():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN, address.getKecamatan()!=null?address.getKecamatan():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN, address.getKelurahan()!=null?address.getKelurahan():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, address.getVenCity()!=null?address.getVenCity().getCityName():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME, address.getVenState()!=null?address.getVenState().getStateName():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE, address.getPostalCode()!=null?address.getPostalCode():"");
					map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME, address.getVenCountry()!=null?address.getVenCountry().getCountryName():"");
					
					dataList.add(map);
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(orderAddressList.size());
				rafDsResponse.setEndRow(request.getStartRow()+orderAddressList.size());
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
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
