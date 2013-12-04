package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

public class GetTotalLogAirwayBillCommand implements RafRpcCommand{

	@Override
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();
			LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) locator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			String select = "select count(airway_bill_id) from log_airway_bill where activity_file_name_and_loc is not null";
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
