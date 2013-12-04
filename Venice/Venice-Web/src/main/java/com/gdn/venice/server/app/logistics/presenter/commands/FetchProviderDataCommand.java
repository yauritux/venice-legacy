package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This Datasource-style presenter servlet command use for fetching Logistics Provider  data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchProviderDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Provider data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchProviderDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> providerDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> logProviderLocator=null;
		
		try{
			logProviderLocator = new Locator <Object>();
			
			LogLogisticsProviderSessionEJBRemote logProviderSessionHome = (LogLogisticsProviderSessionEJBRemote) logProviderLocator.lookup(LogLogisticsProviderSessionEJBRemote.class, "LogLogisticsProviderSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the providers
			 */
			List<LogLogisticsProvider> logProviderList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {	
				logProviderList = logProviderSessionHome.queryByRange("select o from LogLogisticsProvider o", request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				LogLogisticsProvider logProvider = new LogLogisticsProvider();
				logProviderList = logProviderSessionHome.findByLogLogisticsProviderLike(logProvider, criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the logProviderList
			 */
			for(LogLogisticsProvider provider:logProviderList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, provider.getLogisticsProviderId()!=null?provider.getLogisticsProviderId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID, provider.getVenParty()!=null?provider.getVenParty().getPartyId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME, provider.getVenParty()!=null?provider.getVenParty().getFullOrLegalName():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEID, provider.getVenParty().getVenPartyType()!=null?provider.getVenParty().getVenPartyType().getPartyTypeId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, provider.getVenParty().getVenPartyType()!=null? provider.getVenParty().getVenPartyType().getPartyTypeDesc():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, provider.getLogisticsProviderCode()!=null?provider.getLogisticsProviderCode():"");		
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEID, provider.getLogReportTemplate2()!=null?provider.getLogReportTemplate2().getTemplateId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC, provider.getLogReportTemplate2()!=null?provider.getLogReportTemplate2().getTemplateDesc():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEID, provider.getLogReportTemplate1() !=null?provider.getLogReportTemplate1().getTemplateId().toString():"");
				map.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC, provider.getLogReportTemplate1() !=null?provider.getLogReportTemplate1().getTemplateDesc():"");
				providerDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(logProviderList.size());
			rafDsResponse.setEndRow(request.getStartRow()+logProviderList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(logProviderLocator!=null){
					logProviderLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(providerDataList);
		return rafDsResponse;
	}
}