package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchOrderFinanceReconciliationDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderFinanceReconciliationDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<FinArFundsInReconRecordSessionEJBRemote> fundsInReconRecordLocator = null;
		
		try {
			fundsInReconRecordLocator = new Locator<FinArFundsInReconRecordSessionEJBRemote>();
			
			FinArFundsInReconRecordSessionEJBRemote sessionHome = (FinArFundsInReconRecordSessionEJBRemote) fundsInReconRecordLocator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			//Find Funds-In Reconciliation Record by the Order Payment Id
			String select = "select fafirr from FinArFundsInReconRecord fafirr where fafirr.venOrderPayment.orderPaymentId = " + request.getParams().get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID);
			
			List<FinArFundsInReconRecord> finArFundsInReconRecordList = sessionHome.queryByRange(select, 0, 0);
			
			for (int i=0;i<finArFundsInReconRecordList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				FinArFundsInReconRecord finArFundsInReconRecord = finArFundsInReconRecordList.get(i);
				
				map.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, finArFundsInReconRecord.getReconciliationRecordId()!=null?finArFundsInReconRecord.getReconciliationRecordId().toString():"");
				map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION, finArFundsInReconRecord.getFinArFundsInReport()!=null?finArFundsInReconRecord.getFinArFundsInReport().getFileNameAndLocation():"");
				map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC, finArFundsInReconRecord.getFinArReconResult()!=null?finArFundsInReconRecord.getFinArReconResult().getReconResultDesc():"");
				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(finArFundsInReconRecordList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finArFundsInReconRecordList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				fundsInReconRecordLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
