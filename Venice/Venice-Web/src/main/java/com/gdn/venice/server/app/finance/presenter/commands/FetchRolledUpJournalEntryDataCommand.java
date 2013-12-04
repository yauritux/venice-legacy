package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinRolledUpJournalEntrySessionEJBRemote;
import com.gdn.venice.persistence.FinRolledUpJournalEntry;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.util.VeniceConstants;
/**
 *This is Datasource-style presenter command use for fetching Account Lines data (RolledUp Journal Entry)
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchRolledUpJournalEntryDataCommand implements RafDsCommand {
	
	RafDsRequest request;
	/**
	 * Basic constructor for Fetch Account Lines data (RolledUp Journal Entry)
	 * @param request
	 */
	public FetchRolledUpJournalEntryDataCommand(RafDsRequest rafDsRequest) {

		this.request = rafDsRequest;	
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> finRolledUpJournalEntryLocator=null;
		
		try{
			finRolledUpJournalEntryLocator = new Locator<Object>();
			
			FinRolledUpJournalEntrySessionEJBRemote finRolledUpJournalEntrySessionHome = (FinRolledUpJournalEntrySessionEJBRemote) finRolledUpJournalEntryLocator.lookup(FinRolledUpJournalEntrySessionEJBRemote.class, "FinRolledUpJournalEntrySessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the account lines
			 */
			List<FinRolledUpJournalEntry> finRolledUpJournalEntryList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			if (criteria == null) {
				if(request.getParams() != null && request.getParams().get(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID) != null){
					String query = "select o from FinRolledUpJournalEntry o where o.finRolledUpJournalHeader.ruJournalHeaderId = " + request.getParams().get(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID)+
							" order by o.financeJournalEntryId Asc";	
					finRolledUpJournalEntryList = finRolledUpJournalEntrySessionHome.queryByRange(query, 0, 0);
				}else{

					finRolledUpJournalEntryList = finRolledUpJournalEntrySessionHome.queryByRange("select o from FinRolledUpJournalEntry o ", 0, 0);
				}
			} else {
				FinRolledUpJournalEntry finRolledUpJournalEntry = new FinRolledUpJournalEntry();
				finRolledUpJournalEntryList = finRolledUpJournalEntrySessionHome.findByFinRolledUpJournalEntryLike(finRolledUpJournalEntry, criteria, 0, 0);
			}
			
			Long idGroup =null;
			String bankDesc =null;
				
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(FinRolledUpJournalEntry accountLines:finRolledUpJournalEntryList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINANCEJOURNALENTRYID, accountLines.getFinanceJournalEntryId().toString()!=null?accountLines.getFinanceJournalEntryId().toString():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID, accountLines.getFinRolledUpJournalHeader() !=null? accountLines.getFinRolledUpJournalHeader().getRuJournalHeaderId().toString():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID, accountLines.getFinAccount().getAccountId().toString()!=null? accountLines.getFinAccount().getAccountNumber().toString():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC, accountLines.getFinAccount().getAccountDesc() !=null? accountLines.getFinAccount().getAccountDesc():"");
//				map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE, accountLines.getRuValue()!=null? accountLines.getRuValue().toString():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP, accountLines.getRuJournalEntryTimestamp()!=null? accountLines.getRuJournalEntryTimestamp().toString():"");
				
					if (accountLines.getRuValue()!=null) {
						if (accountLines.getCreditDebitFlag()) {
							map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT, accountLines.getRuValue().toString());
							map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET, "");
						} else {
							map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT, "");
							map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET,accountLines.getRuValue().toString());
						}
						
					}			
				
				map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC, accountLines.getFinRolledUpJournalStatus().getJournalEntryStatusDesc()!=null? accountLines.getFinRolledUpJournalStatus().getJournalEntryStatusDesc():"");
				/*
				 * hanya untuk view cash receive untuk finance
				 */
				if (accountLines.getFinRolledUpJournalHeader().getFinRolledUpJournalType().getFinanceJournalTypeId().equals(new Long(VeniceConstants.FIN_ROLLED_UP_JOURNAL_TYPE_CASH_RECEIVE))){
						if((idGroup==null && accountLines.getGroupId()!=null) || (idGroup!=null && accountLines.getGroupId()!=null && !accountLines.getGroupId().equals(idGroup))  ){
							idGroup=accountLines.getGroupId();
							bankDesc= accountLines.getFinAccount().getAccountDesc();
							map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID, bankDesc);
						}else if(accountLines.getGroupId()!=null){
							if(accountLines.getGroupId().equals(idGroup)){
								map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID, bankDesc);
							}
						}else{
							map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID,"");					
						}
				}else{
					map.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID,"");					
				}
					
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(finRolledUpJournalEntryList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finRolledUpJournalEntryList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(finRolledUpJournalEntryLocator!=null){
					finRolledUpJournalEntryLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}