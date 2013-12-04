package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetailType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for fetching contact details type combo box data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchContactDetailTypeDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch Contact detail type combo box data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchContactDetailTypeDataCommand(RafDsRequest rafDsRequest) {

		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {

		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> contactDetailTypeDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> venContactDetailTypeLocator=null;
		
		try{
			venContactDetailTypeLocator = new Locator <Object>();
			
			VenContactDetailTypeSessionEJBRemote venContactDetailTypeSessionHome = (VenContactDetailTypeSessionEJBRemote) venContactDetailTypeLocator.lookup(VenContactDetailTypeSessionEJBRemote.class, "VenContactDetailTypeSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the contact's type
			 */
			List<VenContactDetailType> venContactDetailTypeList = null;
			
			venContactDetailTypeList = venContactDetailTypeSessionHome.queryByRange("select o from VenContactDetailType o", 0, 0);

			
			/*
			 * Map the values from the combo box (Contact Detail Type Desc field)
			 */
			for(VenContactDetailType contactDetailType:venContactDetailTypeList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID, contactDetailType.getContactDetailTypeId()!=null? contactDetailType.getContactDetailTypeId().toString():"");
				map.put(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC,contactDetailType.getContactDetailTypeDesc());
		
				contactDetailTypeDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(contactDetailTypeDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+contactDetailTypeDataList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venContactDetailTypeLocator!=null){
					venContactDetailTypeLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(contactDetailTypeDataList);
		return rafDsResponse;
	}

}