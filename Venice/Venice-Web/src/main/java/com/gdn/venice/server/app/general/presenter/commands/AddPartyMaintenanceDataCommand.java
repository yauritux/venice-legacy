package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.facade.VenPartyTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 *This is Datasource-style presenter command use for adding party maintenance data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class AddPartyMaintenanceDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Add Party data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public AddPartyMaintenanceDataCommand(RafDsRequest rafDsRequest) {
		
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		/*
		 * This is the list that will be used to add the party data later
		 */
		List<HashMap<String, String>> partyDataList= new ArrayList<HashMap<String, String>>();

		/*
		 * This is the Locator for locating VenPartySessionEJBBean
		 */
		Locator<Object> venPartyLocator=null;
		
		try{
			venPartyLocator = new Locator<Object>();
			VenPartySessionEJBRemote venPartySessionHome = (VenPartySessionEJBRemote) venPartyLocator.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			VenPartyTypeSessionEJBRemote venPartyTypeSessionHome = (VenPartyTypeSessionEJBRemote) venPartyLocator.lookup(VenPartyTypeSessionEJBRemote.class, "VenPartyTypeSessionEJBBean");
			partyDataList=request.getData();
			
			VenParty venParty = new VenParty();
			
			/*
			 * Nested loops to map the party data from the requests to
			 * the object structure for persistence
			 */
			for(int i=0;i< partyDataList.size();i++){
				Map<String, String> data = partyDataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.VENPARTY_PARTYID)){
						venParty.setPartyId(new Long (data.get(key)));

					}else if(key.equals(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC)){
						/*
						 * Due to a problem with the way that combo box data in a table is 
						 * communicated party desc may be the code or the description
						 */
						VenPartyType venPartyType = new  VenPartyType();
						try{
							Long venPartyTypeId = new Long(data.get(key));
							venPartyType.setPartyTypeId(venPartyTypeId);
						}catch(NumberFormatException nfe){
							String partyTypeDesc = data.get(key);
							List<VenPartyType> venPartyTypeList = venPartyTypeSessionHome.queryByRange("select o from VenPartyType o where o.partyTypeDesc = '" + partyTypeDesc + "'", 0, 0);
							if(!venPartyTypeList.isEmpty()){
								venPartyType = venPartyTypeList.get(0);
							}
						}
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
			}
					
			venParty=venPartySessionHome.persistVenParty(venParty);
			partyDataList=new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map=new HashMap<String, String>();
			
			/*
			 * Map the data into a map for populating the party data list
			 */
			map.put(DataNameTokens.VENPARTY_PARTYID, venParty.getPartyId()!=null?venParty.getPartyId().toString():"");
			map.put(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEID, venParty.getVenPartyType().getPartyTypeId()!=null?venParty.getVenPartyType().getPartyTypeId().toString():"");
			map.put(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, venParty.getVenPartyType().getPartyTypeDesc()!=null?venParty.getVenPartyType().getPartyTypeDesc():"");
			map.put(DataNameTokens.VENPARTY_FIRSTNAME, venParty.getPartyFirstName()!=null? venParty.getPartyFirstName():"");
			map.put(DataNameTokens.VENPARTY_MIDDLENAME, venParty.getPartyMiddleName()!=null? venParty.getPartyMiddleName():"");
			map.put(DataNameTokens.VENPARTY_LASTNAME, venParty.getPartyLastName()!=null? venParty.getPartyLastName():"");
			map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, venParty.getFullOrLegalName()!=null? venParty.getFullOrLegalName():"");
			map.put(DataNameTokens.VENPARTY_POSITION, venParty.getPartyPosition()!=null? venParty.getPartyPosition():"");
			partyDataList.add(map);
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(0);
			rafDsResponse.setTotalRows(1);			
			rafDsResponse.setEndRow(1);
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venPartyLocator!=null){
					venPartyLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(partyDataList);
		return rafDsResponse;
	}
}