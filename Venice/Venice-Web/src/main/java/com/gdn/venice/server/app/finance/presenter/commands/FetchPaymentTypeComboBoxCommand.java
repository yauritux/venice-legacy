package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenWcsPaymentType;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Payment Type
 * 
 * @author Roland
 */

public class FetchPaymentTypeComboBoxCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			VenWcsPaymentTypeSessionEJBRemote sessionHome = (VenWcsPaymentTypeSessionEJBRemote) locator.lookup(VenWcsPaymentTypeSessionEJBRemote.class, "VenWcsPaymentTypeSessionEJBBean");			
			List<VenWcsPaymentType> paymentType = null;
			String query = "select o from VenWcsPaymentType o";
			
			paymentType = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<paymentType.size();i++){
				VenWcsPaymentType list = paymentType.get(i);
				map.put("data"+Util.isNull(list.getWcsPaymentTypeId(), "").toString(), Util.isNull(list.getWcsPaymentTypeDesc(), "").toString());							
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
