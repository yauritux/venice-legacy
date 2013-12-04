package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for logistic service
 * 
 * @author Roland
 */

public class FetchLogisticServiceComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			LogLogisticServiceSessionEJBRemote sessionHome = (LogLogisticServiceSessionEJBRemote) locator.lookup(LogLogisticServiceSessionEJBRemote.class, "LogLogisticServiceSessionEJBBean");			
			List<LogLogisticService> service = null;
			String query = "select o from LogLogisticService o";
			service = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<service.size();i++){
				LogLogisticService list = service.get(i);
				map.put("data"+Util.isNull(list.getLogisticsServiceId(), "").toString(), Util.isNull(list.getLogisticsServiceDesc(),"").toString());							
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
