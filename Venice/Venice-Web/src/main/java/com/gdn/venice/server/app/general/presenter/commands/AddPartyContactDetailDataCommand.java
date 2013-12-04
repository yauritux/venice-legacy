package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenContactDetailType;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for adding contact details
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class AddPartyContactDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Add contact detail data
	 * @param rafDsRequest is the request parameters passed to the command 
	 * 					
	 */
	public AddPartyContactDetailDataCommand(RafDsRequest rafDsRequest) {

		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();

		/*
		 * This is the list that will be used to add the contact detail data later
		 */
		List<HashMap<String, String>> contactDataList= new ArrayList<HashMap<String, String>>();
		/*
		 * This is the Locator for locating VenContactDetailSessionEJBBean
		 */
		Locator<Object> venContactDetailLocator=null;
	
		try{
			venContactDetailLocator = new Locator<Object>();
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) venContactDetailLocator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean"); 
			VenContactDetailTypeSessionEJBRemote venContactDetailTypeSessionHome = (VenContactDetailTypeSessionEJBRemote) venContactDetailLocator.lookup(VenContactDetailTypeSessionEJBRemote.class, "VenContactDetailTypeSessionEJBBean");
			contactDataList = request.getData();
			
			VenContactDetail venContactDetail = new VenContactDetail();
			
			Long venPartyId=null;
			
			/*
			 * Nested loops to map the contact detail data coming from the request to the
			 * objedct structure for persistence 
			 */
			for (int i = 0; i< contactDataList.size(); i++ ){
				Map <String, String> data = contactDataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID)){
						venContactDetail.setContactDetailId(new Long (data.get(key)));
					}else if(key.equals(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC)){

						VenContactDetailType venContactDetailType = new  VenContactDetailType();
						try{
							Long venContactDetailTypeId = new Long(data.get(key));
							venContactDetailType.setContactDetailTypeId(venContactDetailTypeId);
						}catch(NumberFormatException nfe){
							String contactDetailTypeDesc = data.get(key);
							List<VenContactDetailType> venContactDetailTypeList = venContactDetailTypeSessionHome.queryByRange("select o from VenContactDetailType o where o.contactDetailTypeDesc = '" + contactDetailTypeDesc + "'", 0, 0);
							if(!venContactDetailTypeList.isEmpty()){
								venContactDetailType = venContactDetailTypeList.get(0);
							}
						}
						venContactDetail.setVenContactDetailType(venContactDetailType);
					} else if(key.equals(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL)){
						venContactDetail.setContactDetail(data.get(key));
					}else if(key.equals(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID)){
						VenParty venParty = new VenParty();
						venParty.setPartyId(new Long (data.get(key)));
						venContactDetail.setVenParty(venParty);
						venPartyId = new Long(data.get(key));
					}
				}
			}
			
			venContactDetail = venContactDetailSessionHome.persistVenContactDetail(venContactDetail);
			contactDataList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map=new HashMap<String, String>();
		
			/*
			 * Map the data into a map for populating the party data list
			 */
			map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, venContactDetail.getContactDetailId()!=null?venContactDetail.getContactDetailId().toString():"");
			map.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID, venContactDetail.getVenContactDetailType().getContactDetailTypeId()!=null? venContactDetail.getVenContactDetailType().getContactDetailTypeId().toString():"");
			map.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, venContactDetail.getVenContactDetailType().getContactDetailTypeDesc()!=null? venContactDetail.getVenContactDetailType().getContactDetailTypeDesc():"");
			map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, venContactDetail.getContactDetail()!=null? venContactDetail.getContactDetail():"");
			map.put(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID, venPartyId.toString());
			contactDataList.add(map);
		}catch(Exception e){
			e.printStackTrace();
		}
		rafDsResponse.setData(contactDataList);
		return rafDsResponse;
	}

}
