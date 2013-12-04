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
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for removing contact details data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class RemovePartyContactDetailDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Remove contact details data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public RemovePartyContactDetailDataCommand(RafDsRequest rafDsRequest) {
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
		List<HashMap<String,String >> contactDetailDataList = request.getData();		
		VenContactDetail venContactDetail = new VenContactDetail();
		
		/*
		 * Nested loops to map the contact detail data to the
		 * object structure for persistence
		 */
		for(int i=0; i<contactDetailDataList.size();i++){
			Map <String, String> data = contactDetailDataList.get(i);
			Iterator<String> iter = data.keySet().iterator();
			
			while (iter.hasNext()) {
				String key = iter.next();

				if (key.equals(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID)) {
					venContactDetail.setContactDetailId(new Long(data.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID)));
				} 
			}						
			contactList.add(venContactDetail);	
 
		}
		
		Locator<Object> contactDetailLocator = null;
		
		/*
		 * Query and fetch all of the objects before removing them.
		 */
		try {
			contactDetailLocator = new Locator<Object>();
			
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) contactDetailLocator
			.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<contactList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(contactList.get(i).getContactDetailId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID));
				criteria.add(simpleCriteria);
			}
			
			contactList = venContactDetailSessionHome.findByVenContactDetailLike(venContactDetail, criteria, request.getStartRow(), request.getEndRow());
			venContactDetailSessionHome.removeVenContactDetailList((ArrayList<VenContactDetail>)contactList);		
					
			/*
			 * Initialize the data source response settings
			 */
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
		
		rafDsResponse.setData(contactDetailDataList);
		return rafDsResponse;
	}

}