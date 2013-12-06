package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.FinArFundsInRefundSessionEJBRemote;
import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.gdn.venice.persistence.FinArFundsInRefund;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchRefundPaymentDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public FetchRefundPaymentDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<FinArFundsInRefund> finRefundLocator = null;
		Locator<FinApManualJournalTransaction> finApManualJournalTransactionLocator = null;
		
		try {
			finRefundLocator = new Locator<FinArFundsInRefund>();
			
			FinArFundsInRefundSessionEJBRemote finRefundRecordSessionHome = (FinArFundsInRefundSessionEJBRemote) finRefundLocator
			.lookup(FinArFundsInRefundSessionEJBRemote.class, "FinArFundsInRefundSessionEJBBean");
			
			finApManualJournalTransactionLocator = new Locator<FinApManualJournalTransaction>();
			
			FinApManualJournalTransactionSessionEJBRemote finApManualJournalTransactionSessionHome = (FinApManualJournalTransactionSessionEJBRemote) finApManualJournalTransactionLocator
			.lookup(FinApManualJournalTransactionSessionEJBRemote.class, "FinApManualJournalTransactionSessionEJBBean");
			
			//Gets the list of selected Refund Record Ids
			HashMap<String,String> refundRecordIdMap = Util.formHashMapfromXML( request.getParams().get(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID));
			
			String refundRecordIds = "";
			
			for (int i=0;i<refundRecordIdMap.size();i++) {
				refundRecordIds += refundRecordIdMap.get("Refund"+i);
				if (i<refundRecordIdMap.size()-1) {
					refundRecordIds += ",";
				}
			}
			
			List<FinArFundsInRefund> finArFundsInRefundList = null;
			
			FinArFundsInRefund refundRecord = new FinArFundsInRefund();
	 	
			String selectRefundRecord = "select o from FinArFundsInRefund o where o.refundRecordId in (" + refundRecordIds + ") order by o.finArFundsInReconRecord.wcsOrderId";
			
			finArFundsInRefundList = finRefundRecordSessionHome.queryByRange(selectRefundRecord, 0, 0);
			
		
			for (int i=0;i<finArFundsInRefundList.size();i++) {
				refundRecord = finArFundsInRefundList.get(i);

				HashMap<String, String> map = new HashMap<String, String>();
						
				map.put(DataNameTokens.FINAPPAYMENT_APPAYMENTID, new Integer(i).toString());
				
				
//				double penaltyAmount = 0;
				String finApManualJournalTransactionsIds = "";
//				
//				String selectManualJournalTransaction = "select o from FinApManualJournalTransaction o where o.venParty.partyId = " + partyId + 
//					" and o.finJournalTransaction.finAccount.accountDesc='PIUTANG USAHA' " +
//					" and o.finApPayment is null";
//				
//				List<FinApManualJournalTransaction> finApManualJournalTransactionList = finApManualJournalTransactionSessionHome.queryByRange(selectManualJournalTransaction, 0, 0);
//				
//				for (int j=0;j<finApManualJournalTransactionList.size();j++) {
//					FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(j);
//					penaltyAmount += finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount().doubleValue();
//					finApManualJournalTransactionsIds += finApManualJournalTransaction.getManualJournalTransactionId();
//					if (j<finApManualJournalTransactionList.size()-1) {
//						finApManualJournalTransactionsIds += ",";
//					}
//				}


				map.put(DataNameTokens.FINAPPAYMENT_AMOUNT, refundRecord.getApAmount().toString());
				if(refundRecord.getArAmount() != null){
					map.put(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, refundRecord.getArAmount().toString());
					map.put(DataNameTokens.FINAPPAYMENT_BALANCE, refundRecord.getApAmount().subtract(refundRecord.getArAmount()).toString());
				}else{
					map.put(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, "0");
					map.put(DataNameTokens.FINAPPAYMENT_BALANCE, refundRecord.getApAmount().toString());
				}
				map.put(DataNameTokens.FINAPPAYMENT_FINARFUNDSINREFUNDS, refundRecord.getRefundRecordId().toString());
				map.put(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS, finApManualJournalTransactionsIds);
				
				dataList.add(map);
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
				finRefundLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
