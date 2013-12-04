package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenOrderStatusSessionEJBRemote;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for order status
 * 
 * @author Roland
 */

public class FetchOrderStatusComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			VenOrderStatusSessionEJBRemote sessionHome = (VenOrderStatusSessionEJBRemote) locator.lookup(VenOrderStatusSessionEJBRemote.class, "VenOrderStatusSessionEJBBean");			
			List<VenOrderStatus> status = null;
			String query = "select o from VenOrderStatus o";
			status = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<status.size();i++){
				VenOrderStatus list = status.get(i);
				map.put("data"+Util.isNull(list.getOrderStatusId(), "").toString(), Util.isNull(list.getOrderStatusCode(),"").toString());							
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return Util.formXMLfromHashMap(map);
	}	
}
