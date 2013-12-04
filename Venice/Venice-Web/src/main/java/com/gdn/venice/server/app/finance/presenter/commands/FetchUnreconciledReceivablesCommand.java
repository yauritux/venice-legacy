package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchUnreconciledReceivablesCommand implements RafDsCommand {
	RafDsRequest request;
	public FetchUnreconciledReceivablesCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<FinArFundsInReconRecord> finArFundsInReconRecordLocator=null;
		List<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>();
		try {
			finArFundsInReconRecordLocator= new Locator<FinArFundsInReconRecord>();			
			FinArFundsInReconRecordSessionEJBRemote finArFundsInReconRecordSession = (FinArFundsInReconRecordSessionEJBRemote)finArFundsInReconRecordLocator.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			List<FinArFundsInReconRecord> finArFundsInReconRecordList=null;

			JPQLAdvancedQueryCriteria criteria=request.getCriteria();
			if(criteria==null){
				String quary ="select o from FinArFundsInReconRecord o where o.venOrderPayment.orderPaymentId not in (select a.venOrderPayment.orderPaymentId from FinArFundsInReconRecord a where a.finArReconResult.reconResultId=0 ) order by o.venOrderPayment.orderPaymentId,o.reconciliationRecordId Asc";
				finArFundsInReconRecordList = finArFundsInReconRecordSession.queryByRange(quary, request.getStartRow(), request.getEndRow()-request.getStartRow());
			}else{
				FinArFundsInReconRecord bl = new FinArFundsInReconRecord();
				finArFundsInReconRecordList = finArFundsInReconRecordSession.findByFinArFundsInReconRecordLike(bl, criteria, 0, 0);
			}
			
			if(finArFundsInReconRecordList.size()!=0){
				for(int i=0;i<finArFundsInReconRecordList.size();i++){
					HashMap<String,String> map = new HashMap<String,String>();
					FinArFundsInReconRecord list = finArFundsInReconRecordList.get(i);
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID, Util.isNull(list.getVenOrderPayment().getWcsPaymentId(), "").toString());
					//map.put(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT,Util.isNull(list.getVenOrderPayment().getAmount(),"").toString());
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT,Util.isNull(list.getPaymentAmount(),"").toString());
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT,Util.isNull(list.getProviderReportPaidAmount(),"").toString());
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT, Util.isNull(list.getRemainingBalanceAmount(),"").toString());
					map.put(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC,Util.isNull(list.getFinArReconResult().getReconResultDesc(),"").toString());
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
		}finally{
			if(finArFundsInReconRecordLocator!=null){
				try {
					finArFundsInReconRecordLocator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}
