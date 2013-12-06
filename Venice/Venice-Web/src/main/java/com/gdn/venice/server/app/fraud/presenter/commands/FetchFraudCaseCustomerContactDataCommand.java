package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchFraudCaseCustomerContactDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseCustomerContactDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;		
		
		try {			
			locator = new Locator<Object>();
			
			VenContactDetailSessionEJBRemote contactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");			
			
			String orderId= request.getParams().get(DataNameTokens.VENORDER_ORDERID);
			//Find Order by Order Id			
			List<VenContactDetail> contactDetailList = null;
			contactDetailList = contactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.contactDetailId in (select o.venContactDetail.contactDetailId from VenOrderContactDetail o where o.venOrder.wcsOrderId = '"+orderId+"')",0,0);
			if(contactDetailList.size()<1){
				contactDetailList = contactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.venParty.partyId in (select a.venParty.partyId from VenCustomer a where a.customerId = (select b.venCustomer.customerId from VenOrder b where b.wcsOrderId ='"+orderId+"'))",0,0);
			}
			if (contactDetailList.size()>0) {								
				for (int i=0;i<contactDetailList.size();i++) {					

							HashMap<String, String> map = new HashMap<String, String>();							
							map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, contactDetailList.get(i).getContactDetailId()!=null?contactDetailList.get(i).getContactDetailId().toString():"");
							map.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, contactDetailList.get(i).getVenContactDetailType()!=null?contactDetailList.get(i).getVenContactDetailType().getContactDetailTypeDesc():"");
							map.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, contactDetailList.get(i).getContactDetail());					
							dataList.add(map);

				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(dataList.size());
				rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
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
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
