package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.FinArFundsInRefund;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchRefundDataDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchRefundDataDataCommand(RafDsRequest request ) {
		this.request = request;
	
	}

	@Override
	public RafDsResponse execute() {
		String idReconRecordSource = request.getParams().get(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		if (idReconRecordSource!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<Object> locator = null;
			
			try {
				locator = new Locator<Object>();			
				
				FinArFundsInRefundSessionEJBRemote refundHome = (FinArFundsInRefundSessionEJBRemote) locator
				.lookup(FinArFundsInRefundSessionEJBRemote.class, "FinArFundsInRefundSessionEJBBean");			
				
				FinArFundsInReconRecordSessionEJBRemote reconHome = (FinArFundsInReconRecordSessionEJBRemote) locator
				.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
								
				String query = "Select o from FinArFundsInRefund o where o.finArFundsInReconRecord.reconciliationRecordId=" + idReconRecordSource;				
				List<FinArFundsInRefund> refundList  = refundHome.queryByRange(query, 0, 0);			
				BigDecimal refundAmount = new BigDecimal(0);
				int j=0;
				for (int i=0;i<refundList.size();i++) {
					if(refundList.get(i).getApAmount().compareTo(new BigDecimal(0))>0){
							HashMap<String, String> map = new HashMap<String, String>();													
							map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID, refundList.get(i).getRefundRecordId()+"");
							map.put(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT, refundList.get(i).getApAmount()+"");
							map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDTIMESTAMP, refundList.get(0).getRefundTimestamp()+"");
							map.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, refundList.get(0).getFinArFundsInReconRecord().getReconciliationRecordId()+"");				
							refundAmount=refundAmount.add(refundList.get(i).getApAmount());
						dataList.add(map);
					}else{
						j--;
					}
				}
				
				if(!refundList.isEmpty()){					
					if(refundList.get(0).getFinArFundsInReconRecord().getRefundAmount().subtract(refundAmount).compareTo(new BigDecimal(0))>0){
						HashMap<String, String> map = new HashMap<String, String>();													
						map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID,"-1");
						map.put(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT, refundList.get(0).getFinArFundsInReconRecord().getRefundAmount().subtract(refundAmount)+"");
						map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDTIMESTAMP, "");
						map.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, refundList.get(0).getFinArFundsInReconRecord().getReconciliationRecordId()+"");				
						dataList.add(map);
						j++;
					}					
				}else{
					List<FinArFundsInReconRecord> reconList = reconHome.queryByRange("Select o from FinArFundsInReconRecord o where o.reconciliationRecordId="+idReconRecordSource, 0, 0);
					if(reconList.get(0).getRefundAmount()!=null){
						HashMap<String, String> map = new HashMap<String, String>();													
						map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID,"-1");
						map.put(DataNameTokens.FINARFUNDSINREFUND_APAMOUNT, reconList.get(0).getRefundAmount()!=null?reconList.get(0).getRefundAmount()+"":"0");
						map.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDTIMESTAMP, "");
						map.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, reconList.get(0).getReconciliationRecordId()+"");				
						dataList.add(map);
						j++;
					}
				}
				
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(refundList.size()+j);
				rafDsResponse.setEndRow(request.getStartRow()+refundList.size()+j);
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
