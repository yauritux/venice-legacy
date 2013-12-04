package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinJournalTransactionSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinJournalTransaction;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

/**
 * Command to fetch the journal related invoice data to populate the bottom
 * panel in journal screen where the journal is a logistics debt acknoeledgement
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FetchJournalRelatedLogisticsInvoiceDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	/**
	 * Copy constructor to set the request and user name
	 * @param request
	 * @param userName
	 */
	public FetchJournalRelatedLogisticsInvoiceDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<Object> locator = null;
		
		//Check for journal Group Id, it will be there if the screen is called from Journal Screen
		String journalGroupId = null;
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
			journalGroupId = request.getParams().get(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
		}
				
		try {
			List<FinJournalTransaction>  finJournalTransactionList = null;
			
			/*
			 * Check for journal Group Id, it will be there if the screen is called from Journal Screen
			 * This section queries for finJournalTransactions that are part of the finJournalApprovalGroup
			 * 
			 * we also check for the invoice
			 */
			if (journalGroupId!=null) {
				locator = new Locator<Object>();
				
				FinJournalTransactionSessionEJBRemote finJournalTransactionSessionHome = (FinJournalTransactionSessionEJBRemote) locator
				.lookup(FinJournalTransactionSessionEJBRemote.class, "FinJournalTransactionSessionEJBBean");

				LogInvoiceReportUploadSessionEJBRemote logInvoiceReportUploadSessionHome = (LogInvoiceReportUploadSessionEJBRemote) locator
				.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");

				//salah logic! benerin bsk senin aja
				//harusnya get finApInvoice yang journal group idnya sekian
				String select = "select o from FinJournalTransaction o join fetch o.finApInvoices where o.finJournalApprovalGroup.journalGroupId = " + journalGroupId;
				
				finJournalTransactionList = finJournalTransactionSessionHome.queryByRange(select, 0, 0);
				
				Set<FinApInvoice> finApInvoiceSet = new TreeSet<FinApInvoice>();
				
				if(!finJournalTransactionList.isEmpty()){
					for(FinJournalTransaction journalTransaction:finJournalTransactionList){
						if(journalTransaction.getFinApInvoices() != null){
							/*
							 * There will only be one invoice attached to the transaction
							 */
							finApInvoiceSet.add(journalTransaction.getFinApInvoices().get(0));
							break;
						}
					}
				}
				
				Iterator<FinApInvoice> i = finApInvoiceSet.iterator();
				while(i.hasNext()){
					FinApInvoice finApInvoice = i.next();
					/*
					 * We need to get the invoice number from the report upload
					 * there wuill only be one report for the invoice
					 */
					List<LogInvoiceReportUpload> logInvoiceReportUploadList = logInvoiceReportUploadSessionHome.queryByRange("select o from LogInvoiceReportUpload o where o.finApInvoice.apInvoiceId = " + finApInvoice.getApInvoiceId(), 0, 0);
					LogInvoiceReportUpload logInvoiceReportUpload = null;
					if(!logInvoiceReportUploadList.isEmpty()){
						logInvoiceReportUpload = logInvoiceReportUploadList.get(0);
					}
					
					HashMap<String, String> map = new HashMap<String, String>();
					DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();

					map.put(DataNameTokens.FINAPINVOICE_APINVOICEID, 
							(finApInvoice.getApInvoiceId()!=null)?
							finApInvoice.getApInvoiceId().toString():"");
					
					map.put(DataNameTokens.FINAPINVOICE_LOGINVOICEREPORTUPLOADS_INVOICENUMBER, 
							(logInvoiceReportUpload.getInvoiceNumber()!=null)?
									logInvoiceReportUpload.getInvoiceNumber():"");

					map.put(DataNameTokens.FINAPINVOICE_VENPARTY_FULLORLEGALNAME, 
							(finApInvoice.getVenParty()!=null)?
							finApInvoice.getVenParty().getFullOrLegalName():"");
					
					map.put(DataNameTokens.FINAPINVOICE_INVOICEDATE, 
							(finApInvoice.getInvoiceDate()!=null)?
							formatter.format(finApInvoice.getInvoiceDate()):"");
					
					map.put(DataNameTokens.FINAPINVOICE_INVOICEAMOUNT, 
							(finApInvoice.getInvoiceAmount()!=null)?
							finApInvoice.getInvoiceAmount().toString():"");

					map.put(DataNameTokens.FINAPINVOICE_INVOICEARAMOUNT, 
							(finApInvoice.getArAmount()!=null)?
							finApInvoice.getArAmount().toString():"");

					dataList.add(map);
				}
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(0);
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator!=null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
