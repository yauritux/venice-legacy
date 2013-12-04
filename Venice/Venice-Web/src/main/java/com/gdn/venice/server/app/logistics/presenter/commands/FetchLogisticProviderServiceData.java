package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for fetching Logistics Provider Service data on Service  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchLogisticProviderServiceData implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistic Provider Service data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchLogisticProviderServiceData(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		
		/* 
		 * Locating LogLogisticService EJB
		 */
		Locator<Object> logLogisticServiceLocator=null;
		
		try{
			logLogisticServiceLocator = new Locator<Object>();
			LogLogisticServiceSessionEJBRemote logLogisticServiceSessionHome = (LogLogisticServiceSessionEJBRemote) logLogisticServiceLocator.lookup(LogLogisticServiceSessionEJBRemote.class, "LogLogisticServiceSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the logistic provider service
			 */
			List<LogLogisticService> logLogisticServiceList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
			if (criteria == null) {
				if(request.getParams() != null && request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID) != null){
					String query = "select o from LogLogisticService o where o.logLogisticsProvider.logisticsProviderId = " + request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID);	
					logLogisticServiceList = logLogisticServiceSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}else{

					logLogisticServiceList = logLogisticServiceSessionHome.queryByRange("select o from LogLogisticService o ", request.getStartRow(), request.getEndRow()-request.getStartRow());
				}
			} else {
				LogLogisticService logLogisticService = new LogLogisticService();
				logLogisticServiceList = logLogisticServiceSessionHome.findByLogLogisticServiceLike(logLogisticService, criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(LogLogisticService logService:logLogisticServiceList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID , logService.getLogisticsServiceId().toString() !=null? logService.getLogisticsServiceId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID , logService.getLogLogisticsProvider().getLogisticsProviderId().toString() !=null? logService.getLogLogisticsProvider().getLogisticsProviderId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE , logService.getServiceCode()!=null? logService.getServiceCode():"");
				map.put(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC, logService.getLogisticsServiceDesc() !=null? logService.getLogisticsServiceDesc():"");
				map.put(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID, logService.getLogLogisticsServiceType().getLogisticsServiceTypeId() !=null? logService.getLogLogisticsServiceType().getLogisticsServiceTypeId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC, logService.getLogLogisticsServiceType().getLogisticsServiceTypeDesc());
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(logLogisticServiceList.size());
			rafDsResponse.setEndRow(request.getStartRow()+logLogisticServiceList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(logLogisticServiceLocator!=null){
					logLogisticServiceLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}
