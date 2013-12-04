package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
/**
 * Fetch Order Item Info
 * 
 * @author Arifin
 */

public class FetchOrderItemInformation implements RafDsCommand {

	RafDsRequest request;
	
	public FetchOrderItemInformation(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			VenOrderItemSessionEJBRemote sessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");		
			List<VenOrderItem> partyList = null;				
				/*
				 * fetch data order item sesuai denga order id
				 */			
				String query = "select o from VenOrderItem o where o.venOrder.orderId="+request.getParams().get(DataNameTokens.VENORDER_ORDERID);	
				partyList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			
			for(int i=0; i<partyList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderItem list = partyList.get(i);
				map.put(DataNameTokens.VENORDERITEM_VENADDRESS_STREETADDRESS1, Util.isNull(list.getVenAddress().getStreetAddress1(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_VENADDRESS_KECAMATAN, Util.isNull(list.getVenAddress().getKelurahan(), "").toString()+"/"+Util.isNull(list.getVenAddress().getKecamatan(), "").toString());		
				map.put(DataNameTokens.VENORDERITEM_VENADDRESS_VENCITY_CITYNAME, Util.isNull((list.getVenAddress().getVenCity()!=null?list.getVenAddress().getVenCity().getCityName():""), "").toString());
				map.put(DataNameTokens.VENORDERITEM_VENADDRESS_VENCOUNTRY_STATE, Util.isNull((list.getVenAddress().getVenState()!=null?list.getVenAddress().getVenState().getStateName():""), "").toString());	
				map.put(DataNameTokens.VENORDERITEM_VENADDRESS_VENCOUNTRY_COUNTRYNAME, Util.isNull(list.getVenAddress().getVenCountry().getCountryName(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_VENADDRESS_POSTALCODE, Util.isNull(list.getVenAddress().getPostalCode(), "").toString());	
				map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, Util.isNull(list.getLogLogisticService().getLogisticsServiceDesc(), "").toString());
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
