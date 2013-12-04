package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter  command for removing Party data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class RemovePartyMaintenanceDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Remove Party data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public RemovePartyMaintenanceDataCommand(RafDsRequest rafDsRequest) {
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
		
		/*
		 * Nested loops to map the party data from the request
		 * to the object structure for persistence
		 */
		for(int i=0; i<partyDataList.size();i++){
			Map <String, String> data = partyDataList.get(i);
			Iterator<String> iter = data.keySet().iterator();
			
			while (iter.hasNext()) {
				String key = iter.next();

				if (key.equals(DataNameTokens.VENPARTY_PARTYID)) {
					venParty.setPartyId(new Long(data.get(DataNameTokens.VENPARTY_PARTYID)));
				} 
			}						
			partyList.add(venParty);	
 
		}
		
		Locator<Object> partyLocator = null;
		
		/*
		 * Lookup the party objects before removing them.
		 */
		try {
			partyLocator = new Locator<Object>();
			
			VenPartySessionEJBRemote venPartySessionHome = (VenPartySessionEJBRemote) partyLocator
			.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<partyList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.VENPARTY_PARTYID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(partyList.get(i).getPartyId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENPARTY_PARTYID));
				criteria.add(simpleCriteria);
			}
			
			partyList = venPartySessionHome.findByVenPartyLike(venParty, criteria, request.getStartRow(), request.getEndRow());
			venPartySessionHome.removeVenPartyList((ArrayList<VenParty>)partyList);		
					
			/*
			 * Initialize the data source response settings
			 */
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