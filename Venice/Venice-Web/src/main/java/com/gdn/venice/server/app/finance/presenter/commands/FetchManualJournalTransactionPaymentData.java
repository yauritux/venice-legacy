package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinApInvoiceSessionEJBRemote;
import com.gdn.venice.facade.FinApManualJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.FinSalesRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinApManualJournalTransaction;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchManualJournalTransactionPaymentData implements RafDsCommand {
	RafDsRequest request;
	
	public FetchManualJournalTransactionPaymentData(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<FinSalesRecord> finSalesRecordLocator = null;
		Locator<FinApInvoice> finInvoiceLocator = null;
		Locator<FinApManualJournalTransaction> finApManualJournalTransactionLocator = null;
		
		try {
			
			
			finApManualJournalTransactionLocator = new Locator<FinApManualJournalTransaction>();
			
			FinApManualJournalTransactionSessionEJBRemote finApManualJournalTransactionSessionHome = (FinApManualJournalTransactionSessionEJBRemote) finApManualJournalTransactionLocator
			.lookup(FinApManualJournalTransactionSessionEJBRemote.class, "FinApManualJournalTransactionSessionEJBBean");
			
			Long partyId = null;
			
			//if Merchant Payment, we have Sales Records here
			if (request.getParams().get(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS)!=null) {
				finSalesRecordLocator = new Locator<FinSalesRecord>();
				
				FinSalesRecordSessionEJBRemote finSalesRecordSessionHome = (FinSalesRecordSessionEJBRemote) finSalesRecordLocator
				.lookup(FinSalesRecordSessionEJBRemote.class, "FinSalesRecordSessionEJBBean");
				

				Pattern p = Pattern.compile("#");
				String[] arraySalesRecordId = p.split(request.getParams().get(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS));
				
				String salesRecordIds = "";
				
				for (int i=0;i<arraySalesRecordId.length;i++) {
					salesRecordIds += arraySalesRecordId[i];
					if (i<arraySalesRecordId.length-1) {
						salesRecordIds += ",";
					}
				}
				
				List<FinSalesRecord> finSalesRecordList = null;
				
				String selectSalesRecord = "select o from FinSalesRecord o where o.salesRecordId in (" + salesRecordIds + ") order by o.venOrderItem.venMerchantProduct.venMerchant.venParty.partyId";
				
				finSalesRecordList = finSalesRecordSessionHome.queryByRange(selectSalesRecord, 0, 0);
				
				//Only get the first Sales Records they have to be sharing the same Party Id
				FinSalesRecord salesRecord = finSalesRecordList.get(0);
				
				partyId = salesRecord.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getPartyId();
			} else if (request.getParams().get(DataNameTokens.FINAPPAYMENT_FINAPINVOICES)!=null) {
				finInvoiceLocator = new Locator<FinApInvoice>();
				
				FinApInvoiceSessionEJBRemote finInvoiceSessionHome = (FinApInvoiceSessionEJBRemote) finInvoiceLocator
				.lookup(FinApInvoiceSessionEJBRemote.class, "FinApInvoiceSessionEJBBean");

				Pattern p = Pattern.compile("#");
				String[] arrayInvoiceId = p.split(request.getParams().get(DataNameTokens.FINAPPAYMENT_FINAPINVOICES));
				
				String invoiceIds = "";
				
				for (int i=0;i<arrayInvoiceId.length;i++) {
					invoiceIds += arrayInvoiceId[i];
					if (i<arrayInvoiceId.length-1) {
						invoiceIds += ",";
					}
				}
				
				List<FinApInvoice> finInvoiceList = null;
				
				String selectInvoice = "select o from FinApInvoice o where o.apInvoiceId in (" + invoiceIds + ") order by o.venParty.partyId";
				
				finInvoiceList = finInvoiceSessionHome.queryByRange(selectInvoice, 0, 0);
				
				//Only get the first Sales Records they have to be sharing the same Party Id
				FinApInvoice invoice = finInvoiceList.get(0);
				
				partyId = invoice.getVenParty().getPartyId();
			}
			

			if (partyId!=null) {
			
				String selectManualJournalTransaction = "select o from FinApManualJournalTransaction o where o.venParty.partyId = " + partyId + 
					" and o.finJournalTransaction.finAccount.accountDesc='PIUTANG MERCHANT' " +
					" and o.finApPayment is null";
				
				List<FinApManualJournalTransaction> finApManualJournalTransactionList = finApManualJournalTransactionSessionHome.queryByRange(selectManualJournalTransaction, 0, 0);
				
				for (int i=0;i<finApManualJournalTransactionList.size();i++) {
					FinApManualJournalTransaction finApManualJournalTransaction = finApManualJournalTransactionList.get(i);
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID, 
							(finApManualJournalTransaction.getManualJournalTransactionId()!=null)?
									finApManualJournalTransaction.getManualJournalTransactionId().toString():"");
					map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_TRANSACTIONAMOUNT, 
							(finApManualJournalTransaction.getFinJournalTransaction()!=null &&
							finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount()!=null)?
									finApManualJournalTransaction.getFinJournalTransaction().getTransactionAmount().toString():"");
					map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID,  
							(finApManualJournalTransaction.getVenOrder()!=null)?
									finApManualJournalTransaction.getVenOrder().getWcsOrderId():"");
					map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME,  
							(finApManualJournalTransaction.getVenParty()!=null)?
									finApManualJournalTransaction.getVenParty().getFullOrLegalName():"");
					map.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS,
							(finApManualJournalTransaction.getFinJournalTransaction()!=null)?
							finApManualJournalTransaction.getFinJournalTransaction().getComments():"");
					
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
				finSalesRecordLocator.close();
				finInvoiceLocator.close();
				finApManualJournalTransactionLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
