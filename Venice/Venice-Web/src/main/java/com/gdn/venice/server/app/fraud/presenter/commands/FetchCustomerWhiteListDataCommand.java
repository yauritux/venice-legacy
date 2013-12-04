package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for CustomerWhitelist Maintenance
 * 
 * @author Arfin
 */

public class FetchCustomerWhiteListDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchCustomerWhiteListDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdCustomerWhitelistSessionEJBRemote sessionHome = (FrdCustomerWhitelistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistSessionEJBRemote.class, "FrdCustomerWhitelistSessionEJBBean");			
			List<FrdCustomerWhitelist> whiteList = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FrdCustomerWhitelist o";			
				whiteList = sessionHome.queryByRange(query, 0, 50);
			} else {
				FrdCustomerWhitelist bl = new FrdCustomerWhitelist();
				whiteList = sessionHome.findByFrdCustomerWhitelistLike(bl, criteria, 0, 0);
			}
			for(int i=0; i<whiteList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdCustomerWhitelist list = whiteList.get(i);
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID, Util.isNull(list.getId(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERID, Util.isNull(list.getOrderid(), "").toString());			
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP, Util.isNull(list.getOrdertimestamp(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME, Util.isNull(list.getCustomername(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS, Util.isNull(list.getShippingaddress(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL, Util.isNull(list.getEmail(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER, Util.isNull(list.getCreditcardnumber(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK, Util.isNull(list.getIssuerbank(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_ECI, Util.isNull(list.getEci(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE, Util.isNull(list.getExpireddate(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE, Util.isNull(list.getGenuinedate(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_REMARK, Util.isNull(list.getRemark(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_CREATEDBY, Util.isNull(list.getCreatedby(), "").toString());
				map.put(DataNameTokens.FRDCUSTOMERWHITELIST_CREATED, Util.isNull(list.getCreated(), "").toString());
						
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
