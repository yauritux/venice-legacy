package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinApInvoiceSessionEJBRemote;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchLogisticsPaymentProcessingDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchLogisticsPaymentProcessingDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<FinJournalTransaction> finJournalTransactionLocator = null;
		
		//Check for journal Group Id, it will be there if the screen is called from Journal Screen
		String journalGroupId = null;
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
			journalGroupId = request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
		}
		
		Locator<FinApInvoice> finApInvoiceLocator = null;
		Locator<LogInvoiceReportUpload> logInvoiceReportUploadLocator = null;
		
		try {
			List<FinJournalTransaction>  finJournalTransactionList = null;
			
			//Check for journal Group Id, it will be there if the screen is called from Journal Screen
			//This section queries for finJournalTransactions that are part of the finJournalApprovalGroup
			if (journalGroupId!=null) {
				finJournalTransactionLocator = new Locator<FinJournalTransaction>();
				
				FinJournalTransactionSessionEJBRemote finJournalTransactionSessionHome = (FinJournalTransactionSessionEJBRemote) finJournalTransactionLocator
				.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");
				
				String select = "select o from FinJournalTransaction o where o.finJournalApprovalGroup.journalGroupId = " + journalGroupId;
				
				finJournalTransactionList = finJournalTransactionSessionHome.queryByRange(select, 0, 0);
			}
			
			finApInvoiceLocator = new Locator<FinApInvoice>();
			
			FinApInvoiceSessionEJBRemote finApInvoiceSessionHome = (FinApInvoiceSessionEJBRemote) finApInvoiceLocator
			.lookup(FinApInvoiceSessionEJBRemote.class, "FinApInvoiceSessionEJBBean");
			
			logInvoiceReportUploadLocator = new Locator<LogInvoiceReportUpload>();
			
			LogInvoiceReportUploadSessionEJBRemote logInvoiceReportUploadSessionHome = (LogInvoiceReportUploadSessionEJBRemote) logInvoiceReportUploadLocator
			.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
			
			
			List<FinApInvoice> finApInvoiceList = null;
			
			FinApInvoice finApInvoice = new FinApInvoice();

			if (criteria!=null) {
				finApInvoiceList = finApInvoiceSessionHome.findByFinApInvoiceLike(finApInvoice, criteria, 0, 0);
			} else {
				String select = "select o from FinApInvoice o where o.finApPayment is null";
				
//				finApInvoiceList = sessionHome.queryByRange(select, request.getStartRow(), request.getEndRow());
				finApInvoiceList = finApInvoiceSessionHome.queryByRange(select, 0, 0);
			}
			
			
			for (int i=0;i<finApInvoiceList.size();i++) {
				finApInvoice = finApInvoiceList.get(i);
				
				//only show Ap Invoice that do not have FinApPayment -> means not settled
				if (finApInvoice.getFinApPayment()==null) {
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
					
					map.put(DataNameTokens.FINAPINVOICE_APINVOICEID, 
							(finApInvoice.getApInvoiceId()!=null)?
							finApInvoice.getApInvoiceId().toString():"");
					
					map.put(DataNameTokens.FINAPINVOICE_VENPARTY_FULLORLEGALNAME, 
							(finApInvoice.getVenParty()!=null)?
							finApInvoice.getVenParty().getFullOrLegalName():"");
					
					if (finApInvoice.getApInvoiceId()!=null) {
						String query = "Select liru from LogInvoiceReportUpload liru where liru.finApInvoice.apInvoiceId=" + finApInvoice.getApInvoiceId();
						List<LogInvoiceReportUpload> logInvoiceReportUploadList = logInvoiceReportUploadSessionHome.queryByRange(query, 0, 0);
						if (logInvoiceReportUploadList.size()>0) {
							
							map.put(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER, logInvoiceReportUploadList.get(0).getInvoiceNumber());
							if (logInvoiceReportUploadList.get(0).getLogLogisticsProvider()!=null) {
								map.put(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, logInvoiceReportUploadList.get(0).getLogLogisticsProvider().getLogisticsProviderCode());
							}
						}
						
					}
					
					map.put(DataNameTokens.FINAPINVOICE_INVOICEDATE, 
							(finApInvoice.getInvoiceDate()!=null)?
							formatter.format(finApInvoice.getInvoiceDate()):"");
					map.put(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT, 
							(finApInvoice.getInvoiceAmount()!=null)?
							finApInvoice.getInvoiceAmount().toString():"");
					
					boolean bExclude = false;
					
					//finJournalTransactionList will not be null, if journalGroupId is set (the screen is called from Journal Screen)
					//This section filters the fundInReconRecords which are related to specific journalTransaction
					if (finJournalTransactionList!=null) { 
						bExclude = true;
						List<FinJournalTransaction> journalTransactions = finApInvoice.getFinJournalTransactions();
						for (int j=0;j<finJournalTransactionList.size();j++) {
							if (finJournalTransactionList.get(j).getTransactionId()!=null && journalTransactions!=null && !journalTransactions.isEmpty()) {
								String transactionId = finJournalTransactionList.get(j).getTransactionId().toString();
								for (int k=0;k<journalTransactions.size();k++) {
									if (journalTransactions.get(k).getTransactionId()!=null && journalTransactions.get(k).getTransactionId().toString().equals(transactionId)) {
										bExclude = false;
										break;
									}
								}
								if (!bExclude) {
									break;
								}
							}
						}
					} else if (journalGroupId!=null) {
						bExclude = true;
					}
					if (!bExclude) {
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
				if (finJournalTransactionLocator!=null) {
					finJournalTransactionLocator.close();
				}
				finApInvoiceLocator.close();
				logInvoiceReportUploadLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
