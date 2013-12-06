package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
import com.lombardisoftware.webapi.Task;

public class FetchClaimedFraudCaseDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	String password;
	
	public FetchClaimedFraudCaseDataCommand(RafDsRequest request, String userName, String password) {
		this.request = request;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public RafDsResponse execute() {
		BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, password);
		bpmAdapter.synchronize();
		
		//Variable untuk menampung hasil
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		RafDsResponse rafDsResponse = new RafDsResponse();
		int totalRowResult = 0;
		
		try {
			List<Long> taskIds = bpmAdapter.getClientRepository().loadTaskIdsForSavedSearch(1);
			
			for (int i = 0;i < taskIds.size(); i++) {
				Task task = bpmAdapter.getClientRepository().loadTask(taskIds.get(i));
				
				if (task.getProcessInstance() != null) {
					if (task.getProcessInstance().getProcess().getName().equalsIgnoreCase(ProcessNameTokens.FRAUDMANAGEMENTPROCESS)
							&& task.getParticipantDisplayName().equalsIgnoreCase(userName)) {
						
						String fraudCaseId = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.FRAUDCASEID);
						//Interface EJB
						Locator<Object> locator = null;

						try {
							locator = new Locator<Object>();
							FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
							List<FrdFraudSuspicionCase> fraudCaseList = null;

							JPQLAdvancedQueryCriteria masterCriteria = request.getCriteria();
							
							if (masterCriteria == null) {
								String query = "select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId = " + new Long(fraudCaseId);
								fraudCaseList = sessionHome.queryByRange(query, 0, 50);
							} else {
								JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
								criteria.setBooleanOperator(masterCriteria.getBooleanOperator());
								
								for (JPQLSimpleQueryCriteria simpleCriteria : masterCriteria.getSimpleCriteria()) {
									criteria.add(simpleCriteria);
								}
								
								JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
								caseIdCriteria.setFieldName(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
								caseIdCriteria.setOperator("equals");
								caseIdCriteria.setValue(fraudCaseId);
								caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
								criteria.add(caseIdCriteria);
								FrdFraudSuspicionCase fraudCase = new FrdFraudSuspicionCase();
								fraudCaseList = sessionHome.findByFrdFraudSuspicionCaseLike(fraudCase, criteria, 0, 0);
							}
							
							for(int j = 0; j < fraudCaseList.size(); j++) {
								HashMap<String, String> map = new HashMap<String, String>();
								FrdFraudSuspicionCase list = fraudCaseList.get(j);
								
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, list.getVenOrder()!=null && list.getVenOrder().getWcsOrderId()!=null?list.getVenOrder().getWcsOrderId().toString():"");
								map.put(DataNameTokens.TASKID, Util.isNull(new Long(task.getId()), "").toString());
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, Util.isNull(list.getFraudSuspicionCaseId(), "").toString());								
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, Util.isNull(list.getFraudCaseDateTime(), "").toString());						
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, Util.isNull(list.getFraudCaseDesc(), "").toString());
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID, list.getFrdFraudCaseStatus()!=null && list.getFrdFraudCaseStatus().getFraudCaseStatusId()!=null?list.getFrdFraudCaseStatus().getFraudCaseStatusId().toString():"");
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, list.getFrdFraudCaseStatus()!=null && list.getFrdFraudCaseStatus().getFraudCaseStatusDesc()!=null?list.getFrdFraudCaseStatus().getFraudCaseStatusDesc().toString():"");
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE, list.getVenOrder()!=null && list.getVenOrder().getOrderDate()!=null?list.getVenOrder().getOrderDate().toString():"");
								map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, Util.isNull(list.getFraudTotalPoints(), "").toString());
								
								dataList.add(map);
								totalRowResult++;
							}
						} catch(Exception e) {
							e.printStackTrace();
							rafDsResponse.setStatus(-1);
						} finally {
							try {
								if(locator!=null){
									locator.close();
								}
							} catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}
		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(0);
		rafDsResponse.setTotalRows(totalRowResult);
		rafDsResponse.setEndRow(totalRowResult);
		rafDsResponse.setData(dataList);
		
		return rafDsResponse;
	}
}
