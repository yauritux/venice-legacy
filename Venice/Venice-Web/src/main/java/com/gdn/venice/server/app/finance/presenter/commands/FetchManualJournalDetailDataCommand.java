package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote;
import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchManualJournalDetailDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchManualJournalDetailDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
	
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<FinApManualJournalTransaction> finApManualJournalTransactionLocator = null;
		
		try {
			finApManualJournalTransactionLocator = new Locator<FinApManualJournalTransaction>();
			
			FinApManualJournalTransactionSessionEJBRemote sessionHome = (FinApManualJournalTransactionSessionEJBRemote) finApManualJournalTransactionLocator
			.lookup(FinApManualJournalTransactionSessionEJBRemote.class, "FinApManualJournalTransactionSessionEJBBean");
			
			String journalGroupId = request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
			
			String select = "select o from FinApManualJournalTransaction o where o.finJournalTransaction.finJournalApprovalGroup.journalGroupId = " + journalGroupId;
			
			List<FinApManualJournalTransaction>  finApManualJournalTransactionList = sessionHome.queryByRange(select, 0, 0);
			
			
			for (int i=0;i<finApManualJournalTransactionList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(i);
				
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID, finApManualJournalTransaction.getManualJournalTransactionId()!=null?finApManualJournalTransaction.getManualJournalTransactionId().toString():"");
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC,
						(finApManualJournalTransaction.getFinJournalTransaction()!=null &&
						finApManualJournalTransaction.getFinJournalTransaction().getFinAccount()!=null)?
								finApManualJournalTransaction.getFinJournalTransaction().getFinAccount().getAccountNumber() + "-" + finApManualJournalTransaction.getFinJournalTransaction().getFinAccount().getAccountDesc() :"");
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID,
						(finApManualJournalTransaction.getVenOrder()!=null &&
						finApManualJournalTransaction.getVenOrder().getOrderId()!=null)?
								finApManualJournalTransaction.getVenOrder().getOrderId().toString():"");
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID,
						(finApManualJournalTransaction.getVenOrder()!=null)?
								finApManualJournalTransaction.getVenOrder().getWcsOrderId():"");
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID,
						(finApManualJournalTransaction.getVenParty()!=null &&
						finApManualJournalTransaction.getVenParty().getPartyId()!=null)?
								finApManualJournalTransaction.getVenParty().getPartyId().toString():"");
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME,
						(finApManualJournalTransaction.getVenParty()!=null)?
								finApManualJournalTransaction.getVenParty().getFullOrLegalName():"");
				
							
				if (finApManualJournalTransaction.getFinJournalTransaction()!=null && finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount()!=null) {
					if (finApManualJournalTransaction.getFinJournalTransaction().getCreditDebitFlag()) {
						map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount().toString());
						map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, "");
					} else {
						map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, "");
						map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount().toString());
					}
					
				}
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC, 
						(finApManualJournalTransaction.getFinJournalTransaction()!=null &&
						finApManualJournalTransaction.getFinJournalTransaction().getFinTransactionStatus()!=null)?
								finApManualJournalTransaction.getFinJournalTransaction().getFinTransactionStatus().getTransactionStatusDesc():"");
				
				map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS, 
						(finApManualJournalTransaction.getFinJournalTransaction()!=null)?finApManualJournalTransaction.getFinJournalTransaction().getComments():"");
								
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
				finApManualJournalTransactionLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
