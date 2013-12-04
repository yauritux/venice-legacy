package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote;
import com.gdn.venice.persistence.LogProviderAgreement;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.GwtDateFormatter;

/**
 * This is Datasource-style presenter command use for fetching Logistics Provider Agreement data on Agreement  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchLogisticProviderAgreementDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistic Provider Agreement data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchLogisticProviderAgreementDataCommand(RafDsRequest rafDsRequest) {
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
		 * Locating LogisticPickupSchedule EJB
		 */
		Locator<Object> logProviderAgreementLocator=null;
		
		try{
			logProviderAgreementLocator = new Locator<Object>();
			LogProviderAgreementSessionEJBRemote logProviderAgreementSessionHome = (LogProviderAgreementSessionEJBRemote) logProviderAgreementLocator.lookup(LogProviderAgreementSessionEJBRemote.class, "LogProviderAgreementSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch logistic provider agreement
			 */
			List<LogProviderAgreement> logProviderAgreementList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
			if (criteria == null) {
				if(request.getParams() != null && request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID) != null){
					String query = "select o from LogProviderAgreement o where o.logLogisticsProvider.logisticsProviderId = " + request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID);	
					logProviderAgreementList = logProviderAgreementSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}else{

					logProviderAgreementList = logProviderAgreementSessionHome.queryByRange("select o from LogProviderAgreement o ", request.getStartRow(), request.getEndRow()-request.getStartRow());
				}
			} else {
				LogProviderAgreement logProviderAgreement = new LogProviderAgreement();
				logProviderAgreementList = logProviderAgreementSessionHome.findByLogProviderAgreementLike(logProviderAgreement,criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(LogProviderAgreement logAgreement:logProviderAgreementList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID , logAgreement.getProviderAgreementId().toString()!=null? logAgreement.getProviderAgreementId().toString():"" );
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID , logAgreement.getLogLogisticsProvider().getLogisticsProviderId().toString() !=null? logAgreement.getLogLogisticsProvider().getLogisticsProviderId().toString():"" );
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC , logAgreement.getAgreementDesc() !=null? logAgreement.getAgreementDesc():"" );
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT , logAgreement.getPickupTimeCommitment() !=null? logAgreement.getPickupTimeCommitment().toString():"" );
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT , logAgreement.getDeliveryTimeCommitment() !=null? logAgreement.getDeliveryTimeCommitment().toString():"" );
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE , logAgreement.getAgreementDate().toString() !=null? GwtDateFormatter.format(logAgreement.getAgreementDate()):"");
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE,  logAgreement.getExpiryDate().toString() !=null? GwtDateFormatter.format(logAgreement.getExpiryDate()):"");
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT, logAgreement.getDiscountLevelPct().toString() !=null? logAgreement.getDiscountLevelPct().toString():"");
				map.put(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE, logAgreement.getPpnPercentage().toString() !=null? logAgreement.getPpnPercentage().toString():"");	
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(logProviderAgreementList.size());
			rafDsResponse.setEndRow(request.getStartRow()+logProviderAgreementList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(logProviderAgreementLocator!=null){
					logProviderAgreementLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}