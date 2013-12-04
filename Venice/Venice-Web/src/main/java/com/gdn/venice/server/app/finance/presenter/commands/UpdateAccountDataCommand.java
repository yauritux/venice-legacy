package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinAccountSessionEJBRemote;
import com.gdn.venice.persistence.FinAccount;
import com.gdn.venice.persistence.FinAccountCategory;
import com.gdn.venice.persistence.FinAccountType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter  command for Updating/Editing Account Data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class UpdateAccountDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Update Account data
	 * @param rafDsRequest
	 */
	public UpdateAccountDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(request.getStartRow());
		rafDsResponse.setTotalRows(0);
		rafDsResponse.setEndRow(request.getStartRow());

		List<FinAccount> accountList = new ArrayList<FinAccount>();		
		List<HashMap<String,String >> accountDataList = request.getData();		
		FinAccount finAccount = new FinAccount();
							
		Locator<Object> accountLocator = null;
		
		try {
			accountLocator = new Locator<Object>();
			
			FinAccountSessionEJBRemote finAccountSessionHome = (FinAccountSessionEJBRemote) accountLocator
			.lookup(FinAccountSessionEJBRemote.class, "FinAccountSessionEJBBean");
			
			/*
			 * Nested loops to map the account data from the request
			 * on to the object structure for persistence
			 */
			for (int i=0;i<accountDataList.size();i++) {
				Map<String, String> data = accountDataList.get(i);
				
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.FINACCOUNT_ACCOUNTID)){
						try{
							finAccount = finAccountSessionHome.queryByRange("select o from FinAccount o where o.accountId="+new Long (data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							finAccount.setAccountId(new Long (data.get(key)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER)){
						finAccount.setAccountNumber(data.get(key));
					}else if(key.equals(DataNameTokens.FINACCOUNT_ACCOUNTDESC)){
						finAccount.setAccountDesc(data.get(key));
					}else if(key.equals(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC )){
						FinAccountType finAccountType = new FinAccountType();
						finAccountType.setAccountTypeId(new Long (data.get(key)));
						finAccountType.setAccountTypeDesc(data.get(key));
						finAccount.setFinAccountType(finAccountType);
					}else if(key.equals(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC )){
						FinAccountCategory finAccountCategory = new FinAccountCategory();
						finAccountCategory.setAccountCategoryId(new Long (data.get(key)));
						finAccountCategory.setAccountCategoryDesc(data.get(key));
						finAccount.setFinAccountCategory(finAccountCategory);
					}else if(key.equals(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT )){
						finAccount.setSummaryAccount(new Boolean(data.get(key)));
					}
				}						
				
				accountList.add(finAccount);			
			}

			finAccountSessionHome.mergeFinAccountList((ArrayList<FinAccount>)accountList);
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {		
				accountList = finAccountSessionHome.queryByRange("select o from FinAccount o", request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				FinAccount account = new FinAccount();
				accountList = finAccountSessionHome.findByFinAccountLike(account, criteria, 0, 0);
			}
					
			for(int i=0; i<accountList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				
				/*
				 * Map the data into a map for populating the party data list
				 */
				map.put(DataNameTokens.FINACCOUNT_ACCOUNTID, finAccount.getAccountId()!=null?finAccount.getAccountId().toString():"");
				map.put(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER, finAccount.getAccountNumber() !=null?  finAccount.getAccountNumber():"");
				map.put(DataNameTokens.FINACCOUNT_ACCOUNTDESC, finAccount.getAccountDesc() !=null?  finAccount.getAccountDesc():"");
				map.put(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC, finAccount.getFinAccountType() != null?finAccount.getFinAccountType().getAccountTypeDesc(): "");
				map.put(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC, finAccount.getFinAccountCategory() != null?finAccount.getFinAccountCategory().getAccountCategoryDesc(): "");
				map.put(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT, finAccount.getSummaryAccount()!=null? finAccount.getSummaryAccount().toString():"");
				
				accountDataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(accountList.size());
			rafDsResponse.setEndRow(request.getStartRow()+accountList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally {
			try {
				if(accountLocator!=null){
					accountLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	

		rafDsResponse.setData(accountDataList);
		return rafDsResponse;
	}
}