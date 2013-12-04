package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinApPaymentSessionEJBRemote;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchPayablesDueDataCommand implements RafDsCommand {
	RafDsRequest request;

	public FetchPayablesDueDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

		Locator<FinApPayment> finApPaymentLocator = null;
		try {
			finApPaymentLocator = new Locator<FinApPayment>();

			FinApPaymentSessionEJBRemote finApPAymentSession = (FinApPaymentSessionEJBRemote) finApPaymentLocator.lookup(FinApPaymentSessionEJBRemote.class,
							"FinApPaymentSessionEJBBean");

			List<FinApPayment> finApPaymentList = null;

			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String quary = "Select o from FinApPayment o Order By o.venParty.partyId Asc";
				finApPaymentList = finApPAymentSession.queryByRange(quary,request.getStartRow(),request.getEndRow() - request.getStartRow());
			} else {
				FinApPayment bl = new FinApPayment();
				finApPaymentList = finApPAymentSession.findByFinApPaymentLike(bl,criteria,0,0);
			}
			if(finApPaymentList.size()!=0){
				 for(int i=0;i<finApPaymentList.size();i++){
					 HashMap<String,String> map = new HashMap<String,String>();
					 FinApPayment list = finApPaymentList.get(i);
					 map.put(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, Util.isNull(list.getVenParty().getFullOrLegalName(),"").toString());
					 map.put(DataNameTokens.FINAPPAYMENT_AMOUNT, Util.isNull(list.getAmount().toString(),"").toString());
					 map.put(DataNameTokens.FINAPPAYMENT_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, Util.isNull(list.getVenParty().getVenPartyType().getPartyTypeDesc(),"").toString());
					 dataList.add(map);
				 }
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());

		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (finApPaymentLocator != null) {
					finApPaymentLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
