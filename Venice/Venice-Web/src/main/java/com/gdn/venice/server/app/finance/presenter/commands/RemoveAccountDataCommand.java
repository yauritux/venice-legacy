package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinAccountSessionEJBRemote;
import com.gdn.venice.persistence.FinAccount;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter  command for removing account data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class RemoveAccountDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Remove Account data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	
	public RemoveAccountDataCommand(RafDsRequest rafDsRequest) {
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
		
		/*
		 * Nested loops to map the account data from the request
		 * to the object structure for persistence
		 */
		for(int i=0; i<accountDataList.size();i++){
			Map <String, String> data = accountDataList.get(i);
			Iterator<String> iter = data.keySet().iterator();
			
			while (iter.hasNext()) {
				String key = iter.next();

				if (key.equals(DataNameTokens.FINACCOUNT_ACCOUNTID)) {
					finAccount.setAccountId(new Long(data.get(DataNameTokens.FINACCOUNT_ACCOUNTID)));
				} 
			}						
			accountList.add(finAccount);	
 
		}
		
		Locator<Object> partyLocator = null;
		
		/*
		 * Lookup the party objects before removing them.
		 */
		try {
			partyLocator = new Locator<Object>();
			
			FinAccountSessionEJBRemote finAccountSessionHome = (FinAccountSessionEJBRemote) partyLocator
			.lookup(FinAccountSessionEJBRemote.class, "FinAccountSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<accountList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.FINACCOUNT_ACCOUNTID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(accountList.get(i).getAccountId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINACCOUNT_ACCOUNTID));
				criteria.add(simpleCriteria);
			}
			
			accountList = finAccountSessionHome.findByFinAccountLike(finAccount, criteria, request.getStartRow(), request.getEndRow());
			finAccountSessionHome.removeFinAccountList((ArrayList<FinAccount>)accountList);		
					
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(accountList.size());
			rafDsResponse.setEndRow(request.getStartRow()+accountList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally {
			try {
				if(partyLocator!=null){
					partyLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		rafDsResponse.setData(accountDataList);
		return rafDsResponse;
	}
}