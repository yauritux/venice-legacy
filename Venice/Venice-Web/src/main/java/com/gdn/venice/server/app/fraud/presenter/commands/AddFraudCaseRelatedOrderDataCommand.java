package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.regex.Pattern;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.gdn.venice.persistence.FrdFraudRelatedOrderInfoPK;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Add Command for Related Order 
 * 
 * @author Roland
 */

public class AddFraudCaseRelatedOrderDataCommand implements RafRpcCommand {

	HashMap<String,String> orderId;
	
	public AddFraudCaseRelatedOrderDataCommand(String requestBody){
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split( requestBody );

		orderId = new HashMap<String,String>();
		for ( int i=1; i< split.length; i+=2 ) {
			orderId.put( split[i], split[i+1] );
		}
	}
	
	@Override
	public String execute() {
		Locator<Object> locator=null;
		try{
			locator = new Locator<Object>();
			FrdFraudRelatedOrderInfoSessionEJBRemote sessionHome = (FrdFraudRelatedOrderInfoSessionEJBRemote) locator.lookup(FrdFraudRelatedOrderInfoSessionEJBRemote.class, "FrdFraudRelatedOrderInfoSessionEJBBean");			
			FrdFraudRelatedOrderInfo relatedOrder = new FrdFraudRelatedOrderInfo();
			for(int i=0;i< orderId.size();i++){
				String[] params = orderId.get(DataNameTokens.VENORDER_ORDERID + (i + 1)).split("~");
				FrdFraudRelatedOrderInfoPK frdFraudSuspicionCase = new FrdFraudRelatedOrderInfoPK();							
						frdFraudSuspicionCase.setFraudSuspicionCaseId(new Long(params[0]));
						frdFraudSuspicionCase.setOrderId(new Long(params[1]));
						relatedOrder.setId(frdFraudSuspicionCase);
						relatedOrder=sessionHome.persistFrdFraudRelatedOrderInfo(relatedOrder);
					}					
		}catch(Exception e){
			e.printStackTrace();
			return "-1";
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return "0";
	}
}
