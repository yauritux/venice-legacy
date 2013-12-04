package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInAllocatePaymentSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInAllocatePayment;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchAllocateDataDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchAllocateDataDataCommand(RafDsRequest request ) {
		this.request = request;
	
	}

	@Override
	public RafDsResponse execute() {
		String idReconRecordSource = request.getParams().get(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD_SOURCE);
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		if (idReconRecordSource!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<Object> locator = null;
			
			try {
				locator = new Locator<Object>();
				
				FinArFundsInReconRecordSessionEJBRemote allocationHome = (FinArFundsInReconRecordSessionEJBRemote) locator
				.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
				
				FinArFundsInAllocatePaymentSessionEJBRemote allocationPaymentHome = (FinArFundsInAllocatePaymentSessionEJBRemote) locator
				.lookup(FinArFundsInAllocatePaymentSessionEJBRemote.class, "FinArFundsInAllocatePaymentSessionEJBBean");
			
							
				
				
				String query = "Select o from FinArFundsInAllocatePayment o where o.idReconRecordSource=" + idReconRecordSource;
				
				List<FinArFundsInAllocatePayment> orderPaymentAllocationList  = allocationPaymentHome.queryByRange(query, 0, 0);
				
				for (int i=0;i<orderPaymentAllocationList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					
					String idReconRecordDest = ""+orderPaymentAllocationList.get(i).getIdReconRecordDest();
					
					List<FinArFundsInReconRecord> orderList  = allocationHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+idReconRecordDest, 0, 0);
										
					map.put(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD, orderPaymentAllocationList.get(i).getIdReconRecord().toString());
					map.put(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_AMOUNT, orderPaymentAllocationList.get(i).getAmount().toString());
					map.put(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_DEST_ORDER, orderList.get(0).getWcsOrderId());
							
					
					dataList.add(map);
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(orderPaymentAllocationList.size());
				rafDsResponse.setEndRow(request.getStartRow()+orderPaymentAllocationList.size());
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

			
		} else {
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(0);
			rafDsResponse.setEndRow(request.getStartRow());
		}
		
		return rafDsResponse;
	}
	
}
