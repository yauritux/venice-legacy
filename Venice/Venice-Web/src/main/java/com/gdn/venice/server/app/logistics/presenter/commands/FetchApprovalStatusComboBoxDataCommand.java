package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.LogApprovalStatusSessionEJBRemote;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for approval status
 * 
 * @author Roland
 */

public class FetchApprovalStatusComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			LogApprovalStatusSessionEJBRemote sessionHome = (LogApprovalStatusSessionEJBRemote) locator.lookup(LogApprovalStatusSessionEJBRemote.class, "LogApprovalStatusSessionEJBBean");			
			List<LogApprovalStatus> status = null;
			String query = "select o from LogApprovalStatus o";
			status = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<status.size();i++){
				LogApprovalStatus list = status.get(i);
				map.put("data"+Util.isNull(list.getApprovalStatusId(), "").toString(), Util.isNull(list.getApprovalStatusDesc(),"").toString());							
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
