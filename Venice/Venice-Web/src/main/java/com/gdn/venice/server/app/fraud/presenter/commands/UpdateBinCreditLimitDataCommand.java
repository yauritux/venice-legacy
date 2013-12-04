package com.gdn.venice.server.app.fraud.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.persistence.VenCardType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Bin Credit Limit
 * 
 * @author Roland
 */

public class UpdateBinCreditLimitDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateBinCreditLimitDataCommand(RafDsRequest request) {
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
					if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID)){
						try
						{
							binCreditLimit = sessionHome.queryByRange("select o from VenBinCreditLimitEstimate o where o.binCreditLimitEstimateId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							binCreditLimit.setBinCreditLimitEstimateId(new Long(data.get(key)));
						}
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENBANK_BANKNAME)){
						binCreditLimit.setBankName(data.get(key));
					} else if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENCARDTYPE_CARDTYPEID)){
						VenCardType card = new VenCardType();
						card.setCardTypeId(new Long(data.get(key)));
						binCreditLimit.setVenCardType(card);
					} else if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_DESCRIPTION)){
						binCreditLimit.setDescription(data.get(key));
					} else if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_SEVERITY)){
						binCreditLimit.setSeverity(data.get(key));
					} else if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINNUMBER)){
						binCreditLimit.setBinNumber(data.get(key));
					} else if(key.equals(DataNameTokens.VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE)){					
						binCreditLimit.setCreditLimitEstimate(new BigDecimal(data.get(key)));
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
