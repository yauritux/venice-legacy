package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter  command for Updating/Editing Party Data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class UpdatePartyMaintenanceDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Update Party data
	 * @param rafDsRequest
	 */
	public UpdatePartyMaintenanceDataCommand(RafDsRequest rafDsRequest) {
		
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

		List<VenParty> partyList = new ArrayList<VenParty>();		
		List<HashMap<String,String >> partyDataList = request.getData();		
		VenParty venParty = new VenParty();
		
		Locator<Object> partyLocator = null;
		
		try {
			partyLocator = new Locator<Object>();
			
			VenPartySessionEJBRemote venPartySessionHome = (VenPartySessionEJBRemote) partyLocator
			.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			
			/*
			 * Nested loops to map the party data from the request
			 * on to the object structure for persistence
			 */
			for (int i=0;i<partyDataList.size();i++) {
				Map<String, String> data = partyDataList.get(i);
				
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if(key.equals(DataNameTokens.VENPARTY_PARTYID)){
						try{
							venParty = venPartySessionHome.queryByRange("select o from VenParty o where o.partyId="+new Long (data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							venParty.setPartyId(new Long (data.get(key)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if(key.equals(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC )){
						VenPartyType venPartyType = new VenPartyType();
						venPartyType.setPartyTypeId(new Long (data.get(key)));
						venPartyType.setPartyTypeDesc(data.get(key));
						venParty.setVenPartyType(venPartyType);
					} else if(key.equals(DataNameTokens.VENPARTY_FIRSTNAME)){
						venParty.setPartyFirstName(data.get(key));
					}else if(key.equals(DataNameTokens.VENPARTY_MIDDLENAME )){
						venParty.setPartyMiddleName(data.get(key));
					}else if(key.equals(DataNameTokens.VENPARTY_LASTNAME )){
						venParty.setPartyLastName(data.get(key));
					}else if(key.equals(DataNameTokens.VENPARTY_FULLORLEGALNAME )){
						venParty.setFullOrLegalName(data.get(key));
					}else if(key.equals(DataNameTokens.VENPARTY_POSITION )){
						venParty.setPartyPosition(data.get(key));
					}
				}						
				
				partyList.add(venParty);			
			}
					
			venPartySessionHome.mergeVenPartyList((ArrayList<VenParty>)partyList);
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {		
				partyList = venPartySessionHome.queryByRange("select o from VenParty o", request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				VenParty party = new VenParty();
				partyList = venPartySessionHome.findByVenPartyLike(party, criteria, 0, 0);
			}
			
			
			for(int i=0; i<partyList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				
				/*
				 * Map the data into a map for populating the party data list
				 */
				map.put(DataNameTokens.VENPARTY_PARTYID, venParty.getPartyId()!=null?venParty.getPartyId().toString():"");
				map.put(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, venParty.getVenPartyType() != null?venParty.getVenPartyType().getPartyTypeDesc(): "");
				map.put(DataNameTokens.VENPARTY_FIRSTNAME, venParty.getPartyFirstName() !=null? venParty.getPartyFirstName():"");
				map.put(DataNameTokens.VENPARTY_MIDDLENAME, venParty.getPartyMiddleName() !=null?  venParty.getPartyMiddleName():"");
				map.put(DataNameTokens.VENPARTY_LASTNAME,  venParty.getPartyLastName() !=null? venParty.getPartyLastName():"");
				map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, venParty.getFullOrLegalName() !=null? venParty.getFullOrLegalName():"");
				map.put(DataNameTokens.VENPARTY_POSITION,  venParty.getPartyPosition()!=null? venParty.getPartyPosition():"");
				
				partyDataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(partyList.size());
			rafDsResponse.setEndRow(request.getStartRow()+partyList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally {
			try {
				if(partyLocator!=null){
					partyLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	

		rafDsResponse.setData(partyDataList);
		return rafDsResponse;
	}
}