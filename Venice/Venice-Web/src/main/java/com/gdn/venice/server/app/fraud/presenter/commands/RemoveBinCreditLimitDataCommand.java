package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Remove Command for Bin Credit Limit
 * 
 * @author Roland
 */

public class RemoveBinCreditLimitDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public RemoveBinCreditLimitDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<VenBinCreditLimitEstimate> binCreditLimitList = new ArrayList<VenBinCreditLimitEstimate>();		
		List<HashMap<String,String >> dataList = request.getData();		
		VenBinCreditLimitEstimate binCreditLimit = new VenBinCreditLimitEstimate();
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenBinCreditLimitEstimateSessionEJBRemote sessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					//remove bin credit limit just set isActive to false and hide it
					if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID)){
						try{
							binCreditLimit = sessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.binCreditLimitEstimateId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							binCreditLimit.setBinCreditLimitEstimateId(new Long(data.get(key)));
						}
						binCreditLimit.setIsActive(false);
					} 
				}						
				binCreditLimitList.add(binCreditLimit);			
			}

			sessionHome.mergeVenBinCreditLimitEstimateList((ArrayList<VenBinCreditLimitEstimate>)binCreditLimitList);
			
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
