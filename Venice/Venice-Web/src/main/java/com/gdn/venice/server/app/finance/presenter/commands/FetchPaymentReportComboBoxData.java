package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FinArFundsInReportTypeSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReportType;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Bank
 * 
 * @author Roland
 */

public class FetchPaymentReportComboBoxData implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			FinArFundsInReportTypeSessionEJBRemote sessionHome = (FinArFundsInReportTypeSessionEJBRemote) locator.lookup(FinArFundsInReportTypeSessionEJBRemote.class, "FinArFundsInReportTypeSessionEJBBean");			
			List<FinArFundsInReportType> paymentReport = null;
			String query = "select o from FinArFundsInReportType o";
			paymentReport = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<paymentReport.size();i++){
				FinArFundsInReportType list = paymentReport.get(i);
				map.put("data"+Util.isNull(list.getPaymentReportTypeId(), "").toString(), Util.isNull(list.getPaymentReportTypeDesc(),"").toString());							
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
