package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinRolledUpJournalHeaderSessionEJBRemote;
import com.gdn.venice.persistence.FinRolledUpJournalHeader;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 *This is Datasource-style presenter command use for fetching export / journal voucher data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchExportDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Export / Journal Voucher data
	 * @param request
	 */
	public FetchExportDataCommand(RafDsRequest request) {
		
		this.request = request;	
	}
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {

		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> finRolledUpJournalHeaderLocator=null;
		
		try{
			finRolledUpJournalHeaderLocator = new Locator<Object>();
			
			FinRolledUpJournalHeaderSessionEJBRemote finRolledUpJournalHeaderSessionHome = (FinRolledUpJournalHeaderSessionEJBRemote) finRolledUpJournalHeaderLocator.lookup(FinRolledUpJournalHeaderSessionEJBRemote.class, "FinRolledUpJournalHeaderSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the promotions
			 */
			List<FinRolledUpJournalHeader> finRolledUpJournalHeaderList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FinRolledUpJournalHeader o";			
				finRolledUpJournalHeaderList = finRolledUpJournalHeaderSessionHome.queryByRange(query, 0, 50);
			} else {
				FinRolledUpJournalHeader finRolledUpJournalHeader = new FinRolledUpJournalHeader();
				finRolledUpJournalHeaderList = finRolledUpJournalHeaderSessionHome.findByFinRolledUpJournalHeaderLike(finRolledUpJournalHeader, criteria, 0, 0);
			}
				
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(FinRolledUpJournalHeader journalVoucher:finRolledUpJournalHeaderList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID, journalVoucher.getRuJournalHeaderId()!=null?journalVoucher.getRuJournalHeaderId().toString():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALDESC, journalVoucher.getRuJournalHeaderDesc()!=null?journalVoucher.getRuJournalHeaderDesc():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALFILENAMEANDPATH, journalVoucher.getRuJournalFilenameAndPath()!=null?journalVoucher.getRuJournalFilenameAndPath():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEID, journalVoucher.getFinRolledUpJournalType().getFinanceJournalTypeId()!=null? journalVoucher.getFinRolledUpJournalType().getFinanceJournalTypeId().toString():"");
				map.put(DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC, journalVoucher.getFinRolledUpJournalType().getFinanceJournalTypeDesc()!=null? journalVoucher.getFinRolledUpJournalType().getFinanceJournalTypeDesc():"");

				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(finRolledUpJournalHeaderList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finRolledUpJournalHeaderList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(finRolledUpJournalHeaderLocator!=null){
					finRolledUpJournalHeaderLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}