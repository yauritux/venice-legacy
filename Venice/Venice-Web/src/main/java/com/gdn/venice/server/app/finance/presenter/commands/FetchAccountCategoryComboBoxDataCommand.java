package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinAccountCategorySessionEJBRemote;
import com.gdn.venice.persistence.FinAccountCategory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This Datasource-style presenter servlet command use for fetching account category combo box data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchAccountCategoryComboBoxDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch Account Category data
	 * 
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchAccountCategoryComboBoxDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> accountCategoryDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> finAccountCategoryLocator=null;
		
		try{
			finAccountCategoryLocator = new Locator <Object>();
			
			FinAccountCategorySessionEJBRemote finAccountCategorySessionHome = (FinAccountCategorySessionEJBRemote) finAccountCategoryLocator.lookup(FinAccountCategorySessionEJBRemote.class, "FinAccountCategorySessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the account category
			 */
			List<FinAccountCategory> finAccountCategoryList = null;
			
			finAccountCategoryList = finAccountCategorySessionHome.queryByRange("select o from FinAccountCategory o", 0, 0);

			
			/*
			 * Map the values from the combo box (Account Category  field)
			 */
			for(FinAccountCategory accountCategory:finAccountCategoryList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.FINACCOUNTCATEGORY_ACCOUNTCATEGORYID, accountCategory.getAccountCategoryId()!=null?accountCategory.getAccountCategoryId().toString():"");
				map.put(DataNameTokens.FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC,accountCategory.getAccountCategoryDesc());
		
				accountCategoryDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(accountCategoryDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+accountCategoryDataList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(finAccountCategoryLocator!=null){
					finAccountCategoryLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(accountCategoryDataList);
		return rafDsResponse;
	}
}