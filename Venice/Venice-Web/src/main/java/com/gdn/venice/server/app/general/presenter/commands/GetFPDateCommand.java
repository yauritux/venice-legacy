package com.gdn.venice.server.app.general.presenter.commands;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.util.VeniceConstants;

public class GetFPDateCommand {
	
	public String getFPDateAsString(Long orderId){
		Locator<Object> locator = null;

		
		String FPDate = "";
		DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
		
		try {	
			locator = new Locator<Object>();
			
			VenOrderStatusHistorySessionEJBRemote historySessionHome = (VenOrderStatusHistorySessionEJBRemote) locator
			.lookup(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");
			
			List<VenOrderStatusHistory> historyList = null;			
			String query = "select o from VenOrderStatusHistory o where o.venOrder.orderId="+orderId+
			" and o.venOrderStatus.orderStatusId="+VeniceConstants.VEN_ORDER_STATUS_FP +" order by o.id.historyTimestamp desc";
			historyList=historySessionHome.queryByRange(query, 0, 1);
			if(historyList.size()>0){
				FPDate=formatter.format(historyList.get(0).getId().getHistoryTimestamp());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FPDate;
	}
}
