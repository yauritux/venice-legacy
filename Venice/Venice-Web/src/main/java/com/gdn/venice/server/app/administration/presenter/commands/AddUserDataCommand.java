package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserSessionEJBRemote;
import com.gdn.venice.persistence.RafUser;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Add Command for User Maintenance
 * 
 * @author Anto
 */

public class AddUserDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddUserDataCommand(RafDsRequest request){
		this.request=request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			RafUserSessionEJBRemote sessionHome = (RafUserSessionEJBRemote) locator.lookup(RafUserSessionEJBRemote.class, "RafUserSessionEJBBean");
			dataList=request.getData();
			
			RafUser rafUser = new RafUser();
			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					
					//set the party to 7 - Internal Dummy user, because user login info is from ldap and doesn't need party.
					VenParty party = new VenParty();
					party.setPartyId(new Long(7));
					rafUser.setVenParty(party);
					
					if(key.equals(DataNameTokens.RAFUSER_USERID)){
						rafUser.setUserId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.RAFUSER_LOGINNAME)){
						rafUser.setLoginName(data.get(key));
					} 
				}
			}
					
			rafUser=sessionHome.persistRafUser(rafUser);
			rafDsResponse.setStatus(0);
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
