package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class DeleteFundInReconciliationDataCommand implements RafDsCommand {
	
	RafDsRequest request;
	
	public DeleteFundInReconciliationDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<FinArFundsInReconRecord> fundInReconRecordList = new ArrayList<FinArFundsInReconRecord>();	
		List<HashMap<String,String >> dataList = request.getData();	
		FinArFundsInReconRecord fundInReconRecord = new FinArFundsInReconRecord();
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID)) {
					fundInReconRecord.setReconciliationRecordId(new Long(data.get(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID)));
				} 
			}						
			fundInReconRecordList.add(fundInReconRecord);			
		}
		
		Locator<FinArFundsInReconRecordSessionEJBRemote> fundInReconRecordLocator = null;	
		
		try {
			
			fundInReconRecordLocator = new Locator<FinArFundsInReconRecordSessionEJBRemote>();
			
			FinArFundsInReconRecordSessionEJBRemote fundInReconRecordSessionHome = (FinArFundsInReconRecordSessionEJBRemote) fundInReconRecordLocator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");			
				
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<fundInReconRecordList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(fundInReconRecordList.get(i).getReconciliationRecordId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				criteria.add(simpleCriteria);
			}
			fundInReconRecordList = fundInReconRecordSessionHome.findByFinArFundsInReconRecordLike(fundInReconRecord, criteria, request.getStartRow(), request.getEndRow());
			fundInReconRecordSessionHome.removeFinArFundsInReconRecordList((ArrayList<FinArFundsInReconRecord>)fundInReconRecordList);			
			
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(fundInReconRecordLocator!=null){
					fundInReconRecordLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}
