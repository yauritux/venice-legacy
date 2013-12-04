package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinAccountCategorySessionEJBRemote;
import com.gdn.venice.facade.FinAccountSessionEJBRemote;
import com.gdn.venice.facade.FinAccountTypeSessionEJBRemote;
import com.gdn.venice.persistence.FinAccount;
import com.gdn.venice.persistence.FinAccountCategory;
import com.gdn.venice.persistence.FinAccountType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for adding account
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class AddAccountDataCommand implements RafDsCommand {
	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Add Account data
	 * @param rafDsRequest
	 */
	public AddAccountDataCommand(RafDsRequest rafDsRequest) {
		this.request=rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		/*
		 * This is the list that will be used to add the account data later
		 */
		List<HashMap<String, String>> accountDataList= new ArrayList<HashMap<String, String>>();

		/*
		 * This is the Locator for locating FinAccountSessionEJBBean
		 */
		Locator<Object> accountLocator=null;
		
		try{
			accountLocator = new Locator<Object>();
			FinAccountSessionEJBRemote finAccountSessionHome = (FinAccountSessionEJBRemote) accountLocator.lookup(FinAccountSessionEJBRemote.class, "FinAccountSessionEJBBean");
			FinAccountTypeSessionEJBRemote finAccountTypeSessionHome = (FinAccountTypeSessionEJBRemote) accountLocator.lookup(FinAccountTypeSessionEJBRemote.class, "FinAccountTypeSessionEJBBean");
			FinAccountCategorySessionEJBRemote finAccountCategorySessionHome = (FinAccountCategorySessionEJBRemote) accountLocator.lookup(FinAccountCategorySessionEJBRemote.class, "FinAccountCategorySessionEJBBean");
			accountDataList=request.getData();
			
			FinAccount finAccount = new FinAccount();
			
			/*
			 * Nested loops to map the account data from the requests to
			 * the object structure for persistence
			 */
			for(int i=0;i< accountDataList.size();i++){
				Map<String, String> data = accountDataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FINACCOUNT_ACCOUNTID)){
						finAccount.setAccountId(new Long (data.get(key)));

					}else if(key.equals(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER)){
						finAccount.setAccountNumber(data.get(key));

					}else if(key.equals(DataNameTokens.FINACCOUNT_ACCOUNTDESC)){
						finAccount.setAccountDesc (data.get(key));

					}else if(key.equals(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC)){
					
						FinAccountType finAccountType = new  FinAccountType();
						try{
							Long finAccountTypeId = new Long(data.get(key));
							finAccountType.setAccountTypeId(finAccountTypeId);
						}catch(NumberFormatException nfe){
							String accountTypeDesc = data.get(key);
							List<FinAccountType> finAccountTypeList = finAccountTypeSessionHome.queryByRange("select o from FinAccountType o where o.accountTypeDesc = '" + accountTypeDesc + "'", 0, 0);
							if(!finAccountTypeList.isEmpty()){
								finAccountType = finAccountTypeList.get(0);
							}
						}
						finAccount.setFinAccountType(finAccountType);
					} else if(key.equals(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC)){

						FinAccountCategory finAccountCategory = new  FinAccountCategory();
						try{
							Long finAccountCategoryId = new Long(data.get(key));
							finAccountCategory.setAccountCategoryId(finAccountCategoryId);
						}catch(NumberFormatException nfe){
							String accountCategoryDesc = data.get(key);
							List<FinAccountCategory> finAccountCategoryList = finAccountCategorySessionHome.queryByRange("select o from FinAccountCategory o where o.accountCategoryDesc = '" + accountCategoryDesc + "'", 0, 0);
							if(!finAccountCategoryList.isEmpty()){
								finAccountCategory = finAccountCategoryList.get(0);
							}
						}
						finAccount.setFinAccountCategory(finAccountCategory);
					}else if(key.equals(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT )){
						finAccount.setSummaryAccount(new Boolean(data.get(key)));
					}
				}
			}
					
			finAccount=finAccountSessionHome.persistFinAccount(finAccount);
			accountDataList=new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map=new HashMap<String, String>();
			
			/*
			 * Map the data into a map for populating the account data list
			 */
			map.put(DataNameTokens.FINACCOUNT_ACCOUNTID, finAccount.getAccountId()!=null?finAccount.getAccountId().toString():"");
			map.put(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER, finAccount.getAccountNumber()!=null?finAccount.getAccountNumber():"");
			map.put(DataNameTokens.FINACCOUNT_ACCOUNTDESC, finAccount.getAccountDesc()!=null?finAccount.getAccountDesc():"");
			map.put(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID, finAccount.getFinAccountType().getAccountTypeId()!=null?finAccount.getFinAccountType().getAccountTypeId().toString():"");
			map.put(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC, finAccount.getFinAccountType().getAccountTypeDesc()!=null?finAccount.getFinAccountType().getAccountTypeDesc():"");
			map.put(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID, finAccount.getFinAccountCategory().getAccountCategoryId()!=null?finAccount.getFinAccountCategory().getAccountCategoryId().toString():"");
			map.put(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC, finAccount.getFinAccountCategory().getAccountCategoryDesc()!=null?finAccount.getFinAccountCategory().getAccountCategoryDesc():"");
			map.put(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT, finAccount.getSummaryAccount()!=null? finAccount.getSummaryAccount().toString():"");

			accountDataList.add(map);
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(0);
			rafDsResponse.setTotalRows(1);			
			rafDsResponse.setEndRow(1);
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(accountLocator!=null){
					accountLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(accountDataList);
		return rafDsResponse;
	}

}