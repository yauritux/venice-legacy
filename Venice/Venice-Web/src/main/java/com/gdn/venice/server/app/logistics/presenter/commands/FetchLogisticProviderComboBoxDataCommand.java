package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for logistic provider
 * 
 * @author Roland
 */

public class FetchLogisticProviderComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			LogLogisticsProviderSessionEJBRemote sessionHome = (LogLogisticsProviderSessionEJBRemote) locator.lookup(LogLogisticsProviderSessionEJBRemote.class, "LogLogisticsProviderSessionEJBBean");			
			List<LogLogisticsProvider> provider = null;
			String query = "select o from LogLogisticsProvider o";
			provider = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<provider.size();i++){
				LogLogisticsProvider list = provider.get(i);
				map.put("data"+Util.isNull(list.getLogisticsProviderId(), "").toString(), Util.isNull(list.getLogisticsProviderCode(),"").toString());							
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
