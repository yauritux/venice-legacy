package com.gdn.venice.server.app.general.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

public class GetTotalOrderDataCommand implements RafRpcCommand {
	
	@Override
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();
			VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) locator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			String select = "select count(order_id) from ven_order";
			map.put("total", sessionHome.countQueryByRange(select, 0, 0));
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
