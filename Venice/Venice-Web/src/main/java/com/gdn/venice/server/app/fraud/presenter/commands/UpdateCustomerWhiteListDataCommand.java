package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote;
import com.gdn.venice.persistence.FrdCustomerWhitelist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Customer Whitelist Maintenance
 * 
 * @author Arifin
 */

public class UpdateCustomerWhiteListDataCommand implements RafDsCommand {
	RafDsRequest request;
	String username;
	
	public UpdateCustomerWhiteListDataCommand(RafDsRequest request, String username) {
		this.request = request;
		this.username = username;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdCustomerWhitelist> whitelistList = new ArrayList<FrdCustomerWhitelist>();		
		List<HashMap<String,String >> dataList = request.getData();		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdCustomerWhitelistSessionEJBRemote sessionHome = (FrdCustomerWhitelistSessionEJBRemote) locator.lookup(FrdCustomerWhitelistSessionEJBRemote.class, "FrdCustomerWhitelistSessionEJBBean");
			FrdCustomerWhitelist whiteList = new FrdCustomerWhitelist();
			whiteList.setCreatedby(username);
			whiteList.setCreated(new Timestamp(System.currentTimeMillis()));
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID)){
						try{
							whiteList = sessionHome.queryByRange("select o from FrdCustomerWhitelist o where o.id="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							whiteList.setId(new Long(data.get(key)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERID)){
						//whiteList.setOrderid(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP)){
						Long date =new Long (data.get(key));
						whiteList.setOrdertimestamp(new Timestamp(date));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME)){
						whiteList.setCustomername(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS)){
						whiteList.setShippingaddress(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL)){
						whiteList.setEmail(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER)){
						whiteList.setCreditcardnumber(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK)){
						whiteList.setIssuerbank(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_ECI)){
						whiteList.setEci(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE)){
						whiteList.setExpireddate(data.get(key));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE)){
						Long dates =new Long (data.get(key));
						whiteList.setGenuinedate(new Timestamp(dates));
					} else if(key.equals(DataNameTokens.FRDCUSTOMERWHITELIST_REMARK)){
						whiteList.setRemark(data.get(key));
					} 
					
				}						
				
				whitelistList.add(whiteList);			
			}		
			
			sessionHome.mergeFrdCustomerWhitelistList((ArrayList<FrdCustomerWhitelist>)whitelistList);
			
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
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
