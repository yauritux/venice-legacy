package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for fetching Party Contact detail data on bottom table
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchPartyContactDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch Party Contact detail data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchPartyContactDetailDataCommand(RafDsRequest rafDsRequest) {
	
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
		 * Locating VenContactDetail EJB
		 */
		Locator<Object> venContactDetailLocator=null;
		
		try{
			venContactDetailLocator = new Locator<Object>();
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) venContactDetailLocator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the contact details
			 */
			List<VenContactDetail> venContactDetailList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
			if (criteria == null) {
				if(request.getParams() != null && request.getParams().get(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID) != null){
					String query = "select o from VenContactDetail o where o.venParty.partyId = " + request.getParams().get(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID);	
					venContactDetailList = venContactDetailSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}else{

					venContactDetailList = venContactDetailSessionHome.queryByRange("select o from VenContactDetail o ", request.getStartRow(), request.getEndRow()-request.getStartRow());
				}
			} else {
				VenContactDetail venContactDetail = new VenContactDetail();
				venContactDetailList = venContactDetailSessionHome.findByVenContactDetailLike(venContactDetail, criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(VenContactDetail contactDetail:venContactDetailList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID , contactDetail.getContactDetailId().toString()!=null?contactDetail.getContactDetailId().toString():"");
				map.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, contactDetail.getVenContactDetailType().getContactDetailTypeDesc() !=null?contactDetail.getVenContactDetailType().getContactDetailTypeDesc():"");
				map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, contactDetail.getContactDetail()!=null? contactDetail.getContactDetail():"");
				map.put(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID , contactDetail.getVenParty().getPartyId().toString()!=null? contactDetail.getVenParty().getPartyId().toString():"");
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venContactDetailList.size());
			rafDsResponse.setEndRow(request.getStartRow()+venContactDetailList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venContactDetailLocator!=null){
					venContactDetailLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}