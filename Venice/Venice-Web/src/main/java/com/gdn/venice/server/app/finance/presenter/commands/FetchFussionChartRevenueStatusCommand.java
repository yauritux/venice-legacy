package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinPeriod;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Blacklist Source
 * 
 * @author Roland
 */

public class FetchFussionChartRevenueStatusCommand implements RafRpcCommand{
	String idPeriod;

	public FetchFussionChartRevenueStatusCommand(String parameter) {
		idPeriod = parameter;
	}
	
	public String execute() {
		Locator<Object> locator=null;
		 
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			FinSalesRecordSessionEJBRemote sessionHome = (FinSalesRecordSessionEJBRemote) locator.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");			
			List<FinSalesRecord> finSalesRecordListSource = null;
						
			String querySumOfCommission = "select sum(o.gdnCommissionAmount) from FinSalesRecord o where o.finApPayment.finPeriod.periodId="+idPeriod;
			String querySumOfTransactionFee= "select sum(o.gdnTransactionFeeAmount) from FinSalesRecord o where o.finApPayment.finPeriod.periodId="+idPeriod;
			String querySumOfHandlingFee= "select sum(o.gdnHandlingFeeAmount) from FinSalesRecord o where o.finApPayment.finPeriod.periodId="+idPeriod;
						
			finSalesRecordListSource = sessionHome.queryByRange(querySumOfCommission, 0, 0);
			if(finSalesRecordListSource.get(0)!=null){
			map.put("data0","{ Commission : "+finSalesRecordListSource.get(0)+"}");	
			}
			finSalesRecordListSource = sessionHome.queryByRange(querySumOfTransactionFee, 0, 0);	
			if(finSalesRecordListSource.get(0)!=null){
			map.put("data1","{ Transaction Fee : "+finSalesRecordListSource.get(0)+"}");	
			}
			finSalesRecordListSource = sessionHome.queryByRange(querySumOfHandlingFee, 0, 0);	
			if(finSalesRecordListSource.get(0)!=null){
			map.put("data2","{ Handling Fee : "+finSalesRecordListSource.get(0)+"}");	
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
