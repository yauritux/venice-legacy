package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinPeriodSessionEJBRemote;
import com.gdn.venice.persistence.FinPeriod;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 *This is Datasource-style presenter command use for fetching period data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchPeriodDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Period data
	 * @param request
	 */
	public FetchPeriodDataCommand(RafDsRequest rafDsRequest) {

		this.request = rafDsRequest;	
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {

		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<FinPeriod> periodLocator=null;
		
		try{
			periodLocator = new Locator<FinPeriod>();
			
			FinPeriodSessionEJBRemote finPeriodSessionHome = (FinPeriodSessionEJBRemote) periodLocator.lookup(FinPeriodSessionEJBRemote.class, "FinPeriodSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the periods
			 */
			List<FinPeriod> finPeriodList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FinPeriod o";			
				finPeriodList = finPeriodSessionHome.queryByRange(query, 0, 50);
			} else {
				FinPeriod finPeriod = new FinPeriod();
				finPeriodList = finPeriodSessionHome.findByFinPeriodLike(finPeriod, criteria, 0, 0);
			}
				
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(FinPeriod period:finPeriodList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.FINPERIOD_PERIODID, period.getPeriodId()!=null?period.getPeriodId().toString():"");
				map.put(DataNameTokens.FINPERIOD_PERIODDESC, period.getPeriodDesc()!=null?period.getPeriodDesc():"");
				map.put(DataNameTokens.FINPERIOD_FROMDATETIME, period.getFromDatetime()!=null?period.getFromDatetime().toString():"");
				map.put(DataNameTokens.FINPERIOD_TODATETIME, period.getToDatetime()!=null?period.getToDatetime().toString():"");
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(finPeriodList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finPeriodList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(periodLocator!=null){
					periodLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}