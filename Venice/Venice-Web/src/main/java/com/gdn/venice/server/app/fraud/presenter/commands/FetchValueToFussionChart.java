package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * 
 * @author Arifin
 */

public class FetchValueToFussionChart implements RafRpcCommand {
	public FetchValueToFussionChart() {
	}

	@Override
	public String execute() {
		HashMap<String, String> map = new HashMap<String, String>();

		Locator<VenOrderSessionEJBRemote> venOrderLocator = null;
		
		try {
			

			 venOrderLocator = new Locator<VenOrderSessionEJBRemote>();
			 VenOrderSessionEJBRemote venOrderSessionHome = (VenOrderSessionEJBRemote) venOrderLocator.lookup(VenOrderSessionEJBRemote.class,"VenOrderSessionEJBBean");

			
			List<VenOrder> fenOrderList = null;
			String SumOfStatusC =" select count(o.venOrderStatus.orderStatusId) from VenOrder o where o.venOrderStatus.orderStatusId=1 group by o.venOrderStatus.orderStatusId";
			String SumOfStatusSF =" select count(o.venOrderStatus.orderStatusId) from VenOrder o where o.venOrderStatus.orderStatusId=2 group by o.venOrderStatus.orderStatusId";
			String SumOfStatusFC =" select count(o.venOrderStatus.orderStatusId) from VenOrder o where o.venOrderStatus.orderStatusId=3 group by o.venOrderStatus.orderStatusId";
			String SumOfStatusFP =" select count(o.venOrderStatus.orderStatusId) from VenOrder o where o.venOrderStatus.orderStatusId=4 group by o.venOrderStatus.orderStatusId";
			String result="";
			fenOrderList = venOrderSessionHome.queryByRange(SumOfStatusC, 0, 0);		
			if(fenOrderList.size()!=0)	result="{Confirmed : "+fenOrderList.get(0);	
			else result="{Confirmed : 0";	
			fenOrderList = venOrderSessionHome.queryByRange(SumOfStatusSF, 0, 0);		
			if(fenOrderList.size()!=0)	result+=",Suspected Fraud : "+fenOrderList.get(0);	
			else result+=",Suspected Fraud : 0";
			fenOrderList = venOrderSessionHome.queryByRange(SumOfStatusFC, 0, 0);		
			if(fenOrderList.size()!=0) result+=",Fraud Confirmed : "+fenOrderList.get(0);			
			else result+=",Fraud Confirmed : 0";
			fenOrderList = venOrderSessionHome.queryByRange(SumOfStatusFP, 0, 0);		
			if(fenOrderList.size()!=0) result+=",Fraud Check Passed : "+fenOrderList.get(0)+"}";	
			else result+=",Fraud Check Passed : 0}";	
					
			map.put("data0",result);			
						
		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		} finally {
			try {
				if (venOrderLocator != null) {
					venOrderLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Util.formXMLfromHashMap(map);
	}
}
