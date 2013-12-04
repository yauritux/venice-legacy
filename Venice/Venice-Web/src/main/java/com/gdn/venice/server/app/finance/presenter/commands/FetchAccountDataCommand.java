package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinAccountSessionEJBRemote;
import com.gdn.venice.persistence.FinAccount;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 *This is Datasource-style presenter command use for fetching account data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchAccountDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Account data
	 * @param rafDsRequest
	 */
	public FetchAccountDataCommand(RafDsRequest rafDsRequest) {

		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> finAccountLocator=null;
		
		try{
			finAccountLocator = new Locator<Object>();
			
			FinAccountSessionEJBRemote finAccountSessionHome = (FinAccountSessionEJBRemote) finAccountLocator.lookup(FinAccountSessionEJBRemote.class, "FinAccountSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the accounts
			 */
			List<FinAccount> finAccountList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FinAccount o";			
				finAccountList = finAccountSessionHome.queryByRange(query, 0, 50);
			} else {
				FinAccount finAccount = new FinAccount();
				finAccountList = finAccountSessionHome.findByFinAccountLike(finAccount, criteria, 0, 0);
			}
				
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(FinAccount account:finAccountList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.FINACCOUNT_ACCOUNTID, account.getAccountId()!=null?account.getAccountId().toString():"");
				map.put(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER, account.getAccountNumber()!=null?account.getAccountNumber().toString():"");
				map.put(DataNameTokens.FINACCOUNT_ACCOUNTDESC, account.getAccountDesc()!=null?account.getAccountDesc():"");
				map.put(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID, account.getFinAccountType().getAccountTypeId()!=null? account.getFinAccountType().getAccountTypeId().toString():"");
				map.put(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC, account.getFinAccountType().getAccountTypeDesc()!=null? account.getFinAccountType().getAccountTypeDesc():"");
				map.put(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID, account.getFinAccountCategory().getAccountCategoryId()!=null? account.getFinAccountCategory().getAccountCategoryId().toString():"");
				map.put(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC, account.getFinAccountCategory().getAccountCategoryDesc()!=null? account.getFinAccountCategory().getAccountCategoryDesc():"");
				map.put(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT, account.getSummaryAccount()!=null? account.getSummaryAccount().toString():"");

				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(finAccountList.size());
			rafDsResponse.setEndRow(request.getStartRow()+finAccountList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(finAccountLocator!=null){
					finAccountLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}