package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenReturContactDetail;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchReturCustomerContactDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchReturCustomerContactDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();				
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenReturContactDetailSessionEJBRemote orderContactDetailSessionHome = (VenReturContactDetailSessionEJBRemote) locator.lookup(VenReturContactDetailSessionEJBRemote.class, "VenReturContactDetailSessionEJBBean");
			VenContactDetailSessionEJBRemote contactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");			
			
			//Find Retur by Retur Id			
			List<VenReturContactDetail> returContactDetailList = orderContactDetailSessionHome.queryByRange("select o from VenReturContactDetail o where o.venRetur.returId = " + request.getParams().get(DataNameTokens.VENRETUR_RETURID), 0, 0);
			
			if (returContactDetailList.size()>0) {								
				List<VenContactDetail> contactDetailList =null;
				for (int i=0;i<returContactDetailList.size();i++) {					
					contactDetailList = contactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.contactDetailId ="+returContactDetailList.get(i).getVenContactDetail().getContactDetailId(), 0, 0);
					
					if(contactDetailList.size()>0){
						for (int j=0;j<contactDetailList.size();j++) {
							HashMap<String, String> map = new HashMap<String, String>();							
							map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, contactDetailList.get(j).getContactDetailId()!=null?contactDetailList.get(j).getContactDetailId().toString():"");
							map.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, contactDetailList.get(j).getVenContactDetailType()!=null?contactDetailList.get(j).getVenContactDetailType().getContactDetailTypeDesc():"");
							map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, contactDetailList.get(j).getContactDetail());
							
							dataList.add(map);
						}
					}
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(contactDetailList.size());
				rafDsResponse.setEndRow(request.getStartRow()+contactDetailList.size());
			} else {
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(0);
				rafDsResponse.setEndRow(request.getStartRow());
			}
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
