package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticsServiceType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for fetching Logistics Service type combo box  on Service  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchLogServiceTypeDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistic Provider Address Type combo box data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchLogServiceTypeDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> serviceTypeDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator <Object>();
			
			LogLogisticsServiceTypeSessionEJBRemote sessionHome = (LogLogisticsServiceTypeSessionEJBRemote) locator.lookup(LogLogisticsServiceTypeSessionEJBRemote.class, "LogLogisticsServiceTypeSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the service type  combo box data
			 */
			List<LogLogisticsServiceType> serviceTypeList = null;
			
			serviceTypeList = sessionHome.queryByRange("select o from LogLogisticsServiceType o", 0, 0);
			
			/*
			 * Map the values from the combo box (Address Type field)
			 */
			for(LogLogisticsServiceType service:serviceTypeList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID, service.getLogisticsServiceTypeId()!=null?service.getLogisticsServiceTypeId().toString():"");			
				map.put(DataNameTokens.LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC, service.getLogisticsServiceTypeDesc()!=null?service.getLogisticsServiceTypeDesc():"");			
				map.put(DataNameTokens.LOGLOGISTICSSERVICETYPE_EXPRESSFLAG,service.getExpressFlag() !=null? service.getExpressFlag().toString():"" );
		
				serviceTypeDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(serviceTypeDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+serviceTypeDataList.size());
			
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
		rafDsResponse.setData(serviceTypeDataList);
		return rafDsResponse;
	}

}