package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.LogActivityReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchActivityReportAirwayBillReconciliationProblemDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public FetchActivityReportAirwayBillReconciliationProblemDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		String airwayBillId = request.getParams().get(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);				
		RafDsResponse rafDsResponse = new RafDsResponse();		
		if (airwayBillId!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();			
			Locator<LogActivityReconRecord> activityReportAirwayBillReconciliationLocator = null;
			
			try {
				activityReportAirwayBillReconciliationLocator = new Locator<LogActivityReconRecord>();				
				LogActivityReconRecordSessionEJBRemote activityReportAirwayBillReconciliationHome = (LogActivityReconRecordSessionEJBRemote) activityReportAirwayBillReconciliationLocator
				.lookup(LogActivityReconRecordSessionEJBRemote.class, "LogActivityReconRecordSessionEJBBean");
				
				JPQLAdvancedQueryCriteria criteria = request.getCriteria()!=null?request.getCriteria():new JPQLAdvancedQueryCriteria();
				criteria.setBooleanOperator("and");
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.LOGACTIVITYRECONRECORD_LOGAIRWAYBILL_AIRWAYBILLID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(airwayBillId);
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGACTIVITYRECONRECORD_LOGAIRWAYBILL_AIRWAYBILLID));
				criteria.add(simpleCriteria);
				
							
				LogActivityReconRecord logActivityReconRecord = new LogActivityReconRecord();
				
				List<LogActivityReconRecord> logActivityReconRecordList  = activityReportAirwayBillReconciliationHome.findByLogActivityReconRecordLike(logActivityReconRecord, criteria, 0, 0);
				
				for (int i=0;i<logActivityReconRecordList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					
					logActivityReconRecord = logActivityReconRecordList.get(i);
					
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID, logActivityReconRecord.getActivityReconRecordId()!=null?logActivityReconRecord.getActivityReconRecordId().toString():"");
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID, 
							logActivityReconRecord.getLogReconActivityRecordResult()!=null && 
							logActivityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId() != null?
									logActivityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString():"");
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTDESC, logActivityReconRecord.getLogReconActivityRecordResult()!=null?logActivityReconRecord.getLogReconActivityRecordResult().getReconRecordResultDesc():"");
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_VENICEDATA, logActivityReconRecord.getVeniceData());
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_PROVIDERDATA, logActivityReconRecord.getProviderData());
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_MANUALLYENTEREDDATA, logActivityReconRecord.getManuallyEnteredData());
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_USERLOGONNAME, logActivityReconRecord.getUserLogonName());
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, logActivityReconRecord.getLogActionApplied()!=null && logActivityReconRecord.getLogActionApplied().getActionAppliedId()!=null?logActivityReconRecord.getLogActionApplied().getActionAppliedId().toString():"");
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, logActivityReconRecord.getLogActionApplied()!=null?logActivityReconRecord.getLogActionApplied().getActionAppliedDesc():"");
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_COMMENT, logActivityReconRecord.getComment());
					map.put(DataNameTokens.LOGACTIVITYRECONRECORD_COMMENTHISTORY, "&#9658");
					
					dataList.add(map);
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(logActivityReconRecordList.size());
				rafDsResponse.setEndRow(request.getStartRow()+logActivityReconRecordList.size());
			} catch (Exception e) {
				e.printStackTrace();
				rafDsResponse.setStatus(-1);
			} finally {
				try {
					activityReportAirwayBillReconciliationLocator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			rafDsResponse.setData(dataList);
			
		} else {
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(0);
			rafDsResponse.setEndRow(request.getStartRow());
		}
		return rafDsResponse;
	}
}
