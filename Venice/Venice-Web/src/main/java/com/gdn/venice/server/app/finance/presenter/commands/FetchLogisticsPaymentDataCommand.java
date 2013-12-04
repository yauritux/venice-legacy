package com.gdn.venice.server.app.finance.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinApInvoiceSessionEJBRemote;
import com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote;
import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchLogisticsPaymentDataCommand implements RafDsCommand {
	RafDsRequest request;

	public FetchLogisticsPaymentDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<FinApInvoice> finApInvoiceLocator = null;
		Locator<FinApManualJournalTransaction> finApManualJournalTransactionLocator = null;
		
		try {
			finApInvoiceLocator = new Locator<FinApInvoice>();
			
			FinApInvoiceSessionEJBRemote finApInvoiceSessionHome = (FinApInvoiceSessionEJBRemote) finApInvoiceLocator
			.lookup(FinApInvoiceSessionEJBRemote.class, "FinApInvoiceSessionEJBBean");
			
			finApManualJournalTransactionLocator = new Locator<FinApManualJournalTransaction>();
			
			FinApManualJournalTransactionSessionEJBRemote finApManualJournalTransactionSessionHome = (FinApManualJournalTransactionSessionEJBRemote) finApManualJournalTransactionLocator
			.lookup(FinApManualJournalTransactionSessionEJBRemote.class, "FinApManualJournalTransactionSessionEJBBean");
			
			//Gets the list of selected Invoice Ids
			HashMap<String,String> invoiceMap = Util.formHashMapfromXML( request.getParams().get(DataNameTokens.FINAPINVOICE_APINVOICEID));
			
			String invoiceIds = "";
			
			for (int i=0;i<invoiceMap.size();i++) {
				invoiceIds += invoiceMap.get("Invoice"+i);
				if (i<invoiceMap.size()-1) {
					invoiceIds += ",";
				}
			}
			
			List<FinApInvoice> finApInvoiceList = null;
			
			FinApInvoice invoice = new FinApInvoice();
	 	
			String selectInvoice = "select o from FinApInvoice o where o.apInvoiceId in (" + invoiceIds + ") order by o.venParty.partyId";
			
			finApInvoiceList = finApInvoiceSessionHome.queryByRange(selectInvoice, 0, 0);
			
			List<Long> finApPaymentList = new ArrayList<Long>();
		
			for (int i=0;i<finApInvoiceList.size();i++) {
				invoice = finApInvoiceList.get(i);
				
				Long partyId = invoice.getVenParty().getPartyId();
				
				if (partyId!=null) {
					
					if (!finApPaymentList.contains(partyId)) {
						HashMap<String, String> map = new HashMap<String, String>();
						
						map.put(DataNameTokens.FINAPPAYMENT_APPAYMENTID, new Integer(i).toString());
						map.put(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, 
								(invoice.getVenParty()!=null)?
								invoice.getVenParty().getFullOrLegalName():"");
						
						double amount = 0;
						String finApInvoicesIds = "";
						
						for (int j=0;j<finApInvoiceList.size();j++) {
							FinApInvoice finApInvoice = finApInvoiceList.get(j);
							if (finApInvoice.getVenParty().getPartyId().equals(partyId)) {
								amount += finApInvoice.getInvoiceAmount().doubleValue();
								finApInvoicesIds += finApInvoice.getApInvoiceId() + "#";
							}
						}
						
						finApInvoicesIds = finApInvoicesIds.substring(0, finApInvoicesIds.length()-1);
						
						double penaltyAmount = 0;
						String finApManualJournalTransactionsIds = "";
						
						String selectManualJournalTransaction = "select o from FinApManualJournalTransaction o where o.venParty.partyId = " + partyId + 
							" and o.finJournalTransaction.finAccount.accountDesc='PIUTANG USAHA' " +
							" and o.finApPayment is null";
						
						List<FinApManualJournalTransaction> finApManualJournalTransactionList = finApManualJournalTransactionSessionHome.queryByRange(selectManualJournalTransaction, 0, 0);
						
						for (int j=0;j<finApManualJournalTransactionList.size();j++) {
							FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(j);
							penaltyAmount += finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount().doubleValue();
							finApManualJournalTransactionsIds += finApManualJournalTransaction.getManualJournalTransactionId();
							if (j<finApManualJournalTransactionList.size()-1) {
								finApManualJournalTransactionsIds += "#";
							}
						}
						
						double balance = amount - penaltyAmount;

						map.put(DataNameTokens.FINAPPAYMENT_AMOUNT, new BigDecimal(amount).toString());
						map.put(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT, new BigDecimal(penaltyAmount).toString());
						map.put(DataNameTokens.FINAPPAYMENT_BALANCE, new BigDecimal(balance).toString());
						map.put(DataNameTokens.FINAPPAYMENT_FINAPINVOICES, finApInvoicesIds);
						map.put(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS, finApManualJournalTransactionsIds);
						
						finApPaymentList.add(partyId);
						dataList.add(map);
					}
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
				finApInvoiceLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
