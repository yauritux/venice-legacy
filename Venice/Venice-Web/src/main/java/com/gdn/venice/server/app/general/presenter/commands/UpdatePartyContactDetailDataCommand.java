package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenContactDetailType;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for updating contact details data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class UpdatePartyContactDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	/**
	 * Basic Constructor for update Contact detail  data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public UpdatePartyContactDetailDataCommand(RafDsRequest rafDsRequest) {
		
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(request.getStartRow());
		rafDsResponse.setTotalRows(0);
		rafDsResponse.setEndRow(request.getStartRow());

		List<VenContactDetail> contactList = new ArrayList<VenContactDetail>();		
		List<HashMap<String,String >> contactDataList = request.getData();		
		VenContactDetail venContactDetail = new VenContactDetail();
		
		Locator<Object> contactDetailLocator = null;
		
		try {
			contactDetailLocator = new Locator<Object>();
			
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) contactDetailLocator
			.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
			
			/*
			 * Nested loops to map the contact detail data from the request
			 * on to the object structure for persistence
			 */
			for (int i=0;i<contactDataList.size();i++) {
				Map<String, String> data = contactDataList.get(i);
				
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if(key.equals(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID)){
						try{
							venContactDetail = venContactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.contactDetailId="+new Long (data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							venContactDetail.setContactDetailId(new Long (data.get(key)));
						}
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if(key.equals(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID )){
						venContactDetail.getVenContactDetailType().setContactDetailTypeId(new Long(data.get(key)));
					}
					else if(key.equals(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC )){
						VenContactDetailType venContactDetailType = new VenContactDetailType();
						venContactDetailType.setContactDetailTypeId(new Long(data.get(key)));
						venContactDetailType.setContactDetailTypeDesc(data.get(key));
						venContactDetail.setVenContactDetailType(venContactDetailType);
					}else if(key.equals(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL)){
						venContactDetail.setContactDetail(data.get(key));
					}else if(key.equals(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID )){
						VenParty venParty = new VenParty();
						venParty.setPartyId(new Long (data.get(key)));
						venContactDetail.setVenParty(venParty);
						
					}
				}						
				
				contactList.add(venContactDetail);			
			}
					
			venContactDetailSessionHome.mergeVenContactDetailList((ArrayList<VenContactDetail>)contactList);
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {		
				contactList = venContactDetailSessionHome.queryByRange("select o from VenContactDetail o", request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				VenContactDetail contactDetail = new VenContactDetail();
				contactList = venContactDetailSessionHome.findByVenContactDetailLike(contactDetail, criteria, 0, 0);
			}
			
			
			for(int i=0; i<contactList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				
				/*
				 * Map the data into a map for populating the contact detail data list
				 */
				map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, venContactDetail.getContactDetailId().toString());
				map.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, venContactDetail.getVenContactDetailType() !=null? venContactDetail.getVenContactDetailType().getContactDetailTypeDesc():"");
				map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL,  venContactDetail.getContactDetail());
				
				contactDataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(contactList.size());
			rafDsResponse.setEndRow(request.getStartRow()+contactList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally {
			try {
				if(contactDetailLocator!=null){
					contactDetailLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	

		rafDsResponse.setData(contactDataList);
		return rafDsResponse;
	}

}