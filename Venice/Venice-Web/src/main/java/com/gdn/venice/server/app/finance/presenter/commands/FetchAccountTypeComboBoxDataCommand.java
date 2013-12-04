package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinAccountTypeSessionEJBRemote;
import com.gdn.venice.persistence.FinAccountType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This Datasource-style presenter servlet command use for fetching account type combo box data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchAccountTypeComboBoxDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch Account Type data
	 * 
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchAccountTypeComboBoxDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> accountTypeDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> finAccountTypeLocator=null;
		
		try{
			finAccountTypeLocator = new Locator <Object>();
			
			FinAccountTypeSessionEJBRemote finAccountTypeSessionHome = (FinAccountTypeSessionEJBRemote) finAccountTypeLocator.lookup(FinAccountTypeSessionEJBRemote.class, "FinAccountTypeSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the account's type
			 */
			List<FinAccountType> finAccountTypeList = null;
			
			finAccountTypeList = finAccountTypeSessionHome.queryByRange("select o from FinAccountType o", 0, 0);

			
			/*
			 * Map the values from the combo box (Account Type Desc field)
			 */
			for(FinAccountType accountType:finAccountTypeList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.FINACCOUNTTYPE_ACCOUNTTYPEID, accountType.getAccountTypeId()!=null?accountType.getAccountTypeId().toString():"");
				map.put(DataNameTokens.FINACCOUNTTYPE_ACCOUNTTYPEDESC,accountType.getAccountTypeDesc());
		
				accountTypeDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(accountTypeDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+accountTypeDataList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(finAccountTypeLocator!=null){
					finAccountTypeLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(accountTypeDataList);
		return rafDsResponse;
	}
}