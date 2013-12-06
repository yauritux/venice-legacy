package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote;
import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Bin Credit Limit
 * 
 * @author Roland
 */

public class FetchBinCreditLimitDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchBinCreditLimitDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();
			VenBinCreditLimitEstimateSessionEJBRemote sessionHome = (VenBinCreditLimitEstimateSessionEJBRemote) locator.lookup(VenBinCreditLimitEstimateSessionEJBRemote.class, "VenBinCreditLimitEstimateSessionEJBBean");
			List<VenBinCreditLimitEstimate> binCreditLimitEstimateList = null;
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from VenBinCreditLimitEstimate o where o.isActive=true";			
				binCreditLimitEstimateList = sessionHome.queryByRange(query, 0, 50);
			} else {
				JPQLSimpleQueryCriteria isActiveCriteria = new JPQLSimpleQueryCriteria();
				isActiveCriteria.setFieldName(DataNameTokens.VENBINCREDITLIMITESTIMATE_ISACTIVE);
				isActiveCriteria.setOperator("equals");
				isActiveCriteria.setValue("true");
				isActiveCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENBINCREDITLIMITESTIMATE_ISACTIVE));
				criteria.add(isActiveCriteria);
				VenBinCreditLimitEstimate venBinCreditLimitEstimate = new VenBinCreditLimitEstimate();
				binCreditLimitEstimateList = sessionHome.findByVenBinCreditLimitEstimateLike(venBinCreditLimitEstimate, criteria, 0, 0);
			}
			
			for(int i=0; i<binCreditLimitEstimateList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenBinCreditLimitEstimate list = binCreditLimitEstimateList.get(i);
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID, Util.isNull(list.getBinCreditLimitEstimateId(), "").toString());
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENBANK_BANKNAME, Util.isNull(list.getBankName(), "").toString());
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENCARDTYPE_CARDTYPEID, list.getVenCardType()!=null && list.getVenCardType().getCardTypeId()!=null? list.getVenCardType().getCardTypeId().toString():"");
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_DESCRIPTION, Util.isNull(list.getDescription(), "").toString());
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_SEVERITY, Util.isNull(list.getSeverity(), "").toString());
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINNUMBER, Util.isNull(list.getBinNumber(), "").toString());
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE, Util.isNull(list.getCreditLimitEstimate(), "").toString());
				map.put(DataNameTokens.VENBINCREDITLIMITESTIMATE_ISACTIVE, Util.isNull(list.getIsActive(), "false").toString());
				
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
