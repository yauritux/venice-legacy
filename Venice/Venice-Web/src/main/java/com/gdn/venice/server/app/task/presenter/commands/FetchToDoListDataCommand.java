package com.gdn.venice.server.app.task.presenter.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import teamworks.samples.client.repository.ClientRepositoryException;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.app.task.StatusNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.util.VeniceConstants;
import com.lombardisoftware.webapi.Task;

public class FetchToDoListDataCommand implements RafDsCommand {
	RafDsRequest request;
	boolean isSynchronizerCompleted = false;
	String userName;
	String fraudCaseId;
	String fraudStatusId;
	String wcsOrderId;
	
	public FetchToDoListDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
		bpmAdapter.synchronize();
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<Long> taskIds = null;
		
		JPQLAdvancedQueryCriteria criteria = null;
		criteria = request.getCriteria();
		if(criteria!=null){
			List<JPQLSimpleQueryCriteria> simpleCriteria = criteria.getSimpleCriteria();
			if(simpleCriteria.size()>0){
				String criteriaValue = simpleCriteria.get(0).getValue();
				System.out.println("criteria value: "+criteriaValue);
				
				try {
					taskIds = bpmAdapter.getClientRepository().loadTaskIdsForSavedSearch(1);
					System.out.println("taskIds awal size: "+taskIds.size());
					int taskSize = taskIds.size();
					
					for (int i=0;i<taskSize;i++) {
						Task task = bpmAdapter.getClientRepository().loadTask(taskIds.get(i));
						if(!task.getProcessInstance().getProcess().getName().equals(criteriaValue)){
							taskIds.remove(taskIds.get(i));
							--i;
							--taskSize;
							continue;
						}
					}
										
					rafDsResponse.setStatus(0);
					rafDsResponse.setStartRow(0);
					
					List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
					
					try {
						Locator<Object> locator = null;
						System.out.println("taskIds akhir size: "+taskIds.size());
						
						String desc="";
						for (int i=taskIds.size()-1;i>=0;i--) {
							if(i==taskIds.size()-50) {
								System.out.println("taskIds size already 50, break!");
								break;
							}
							
							Task task = bpmAdapter.getClientRepository().loadTask(taskIds.get(i));
							if (task.getProcessInstance() != null) {
								LinkedHashMap<String, String> map = createHashMapEntry(task, null);
								
								//excluded from current user is set when the currently logged in user is excluded within the task inbox.
								//Example for this would be peer-approving process for Logistics Activity Report Approval 
								boolean excludedFromCurrentUser = false; 
								fraudCaseId = "";
								fraudStatusId = "";
								
								if (task.getProcessInstance().getProcess().getName().equals(ProcessNameTokens.LOGISTICSACTIVITYREPORTAPPROVAL)) {
									String submittedBy = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.SUBMITTEDBY);
									excludedFromCurrentUser = userName.equals(submittedBy);
									String wcsOrderIdWcsOrderItemId="";
									String airwayBillIdAll = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.AIRWAYBILLID);
									
									Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
									String[] split = p.split(airwayBillIdAll);

									HashMap<String, String> airwayBillIds;
									airwayBillIds = new HashMap<String, String>();
									for (int j = 1; j < split.length; j += 2) {
										airwayBillIds.put(split[j], split[j + 1]);
									}						
									
									String airwayBillId="";
									for (int k = 0; k < airwayBillIds.size(); k++) {
										airwayBillId = airwayBillIds.get(ProcessNameTokens.AIRWAYBILLID + (k + 1));
																	
										try {
											locator = new Locator<Object>();
											LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) locator.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
											
											List<LogAirwayBill> airwayBillList = null;
											airwayBillList = sessionHome.queryByRange("select o from LogAirwayBill o where o.airwayBillId = " + airwayBillId, 0, 1);
											if (airwayBillList.size() > 0) {
												wcsOrderIdWcsOrderItemId += airwayBillList.get(0).getVenOrderItem().getVenOrder().getWcsOrderId() +" - "+airwayBillList.get(0).getVenOrderItem().getWcsOrderItemId();
												wcsOrderIdWcsOrderItemId+=", ";
											}
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											try {
												if (locator != null) {
													locator.close();
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}							
									}
									
									if (map != null) {
										map.clear();
									}
									
									if(wcsOrderIdWcsOrderItemId.contains(",")){
										wcsOrderIdWcsOrderItemId=wcsOrderIdWcsOrderItemId.substring(0, wcsOrderIdWcsOrderItemId.lastIndexOf(","));
									}
									desc=task.getActivityName()==null?"":task.getActivityName()+" (" + wcsOrderIdWcsOrderItemId + ")";
									map = createHashMapEntry(task, desc);						
								} else if (task.getProcessInstance().getProcess().getName().equals(ProcessNameTokens.LOGISTICSINVOICEAPPROVAL)) {
									String invoiceNumberAll = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.INVOICERENUMBER);
									
									Pattern p = Pattern.compile("[\\{\\}\\=\\,]++");
									String[] split = p.split(invoiceNumberAll);

									HashMap<String, String> invoiceNumbers;
									invoiceNumbers = new HashMap<String, String>();
									for (int j = 1; j < split.length; j += 2) {
										invoiceNumbers.put((split[j]).trim(), (split[j + 1]).trim());
									}	
									String invoiceNumber="";
									for (String invoiceNo : invoiceNumbers.values()) {
										invoiceNumber += invoiceNo + ",";
									}			
									
									invoiceNumber = invoiceNumber.substring(0, invoiceNumber.length()-1);
									desc=task.getActivityName()==null?"":task.getActivityName()+" (" + invoiceNumber + ")";
									map = createHashMapEntry(task, desc);		
								} else if (task.getProcessInstance().getProcess().getName().equals(ProcessNameTokens.LOGISTICSMTADATAACTIVITYRECONCILIATION) || 
										task.getProcessInstance().getProcess().getName().equals(ProcessNameTokens.LOGISTICSMTADATAINVOICERECONCILIATION)) {
									String wcsOrderIdWcsOrderItemId="";
									String airwayBillId = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.AIRWAYBILLID);
																	
									try {
										locator = new Locator<Object>();
										LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) locator.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
										
										List<LogAirwayBill> airwayBillList = null;
										airwayBillList = sessionHome.queryByRange("select o from LogAirwayBill o where o.airwayBillId = " + airwayBillId, 0, 1);
										if (airwayBillList.size() > 0) {
											wcsOrderIdWcsOrderItemId += airwayBillList.get(0).getVenOrderItem().getVenOrder().getWcsOrderId() +" - "+airwayBillList.get(0).getVenOrderItem().getWcsOrderItemId();
											wcsOrderIdWcsOrderItemId+=", ";
										}
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										try {
											if (locator != null) {
												locator.close();
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									
									if (map != null) {
										map.clear();
									}
												
									if(wcsOrderIdWcsOrderItemId.contains(",")){
										wcsOrderIdWcsOrderItemId=wcsOrderIdWcsOrderItemId.substring(0, wcsOrderIdWcsOrderItemId.lastIndexOf(","));
									}
									desc=task.getActivityName()==null?"":task.getActivityName()+" (" + wcsOrderIdWcsOrderItemId + ")";
									map = createHashMapEntry(task, desc);	
								} else if (task.getProcessInstance().getProcess().getName().equals(ProcessNameTokens.LOGISTICSPICKUPPROBLEMINVESTIGATION)) {
									String wcsOrderId="";
									String orderId = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.ORDERID);
																	
									try {
										locator = new Locator<Object>();
										VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
										
										List<VenOrder> orderList = null;
										orderList = sessionHome.queryByRange("select o from VenOrder o where o.orderId = " + orderId, 0, 1);
										if (orderList.size() > 0) {
											wcsOrderId = orderList.get(0).getWcsOrderId();
										}
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										try {
											if (locator != null) {
												locator.close();
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									
									if (map != null) {
										map.clear();
									}

									desc=task.getActivityName()==null?"":task.getActivityName()+" (" + wcsOrderId + ")";
									map = createHashMapEntry(task, desc);						
								} else if (task.getProcessInstance().getProcess().getName().equalsIgnoreCase(ProcessNameTokens.FRAUDMANAGEMENTPROCESS)) {
									wcsOrderId = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.WCSORDERID);
									fraudCaseId = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.FRAUDCASEID);
									fraudStatusId = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.FRAUDSTATUSID);
									
									String iLogStatus = "", fraudPoint = "", orderDate="", FpDate=null, FpDateString="";
									
									try {
										locator = new Locator<Object>();
										FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
										
										List<FrdFraudSuspicionCase> fraudCaseList = null;
										String query = "select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId = " + new Long(fraudCaseId);
										fraudCaseList = sessionHome.queryByRange(query, 0, 1);
										if (fraudCaseList.size() > 0) {
											iLogStatus = fraudCaseList.get(0).getIlogFraudStatus();
											fraudPoint = fraudCaseList.get(0).getFraudTotalPoints().toString();
										}
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										try {
											if (locator != null) {
												locator.close();
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
										
										//get order date
										try {
											locator = new Locator<Object>();
											VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
											
											List<VenOrder> orderList = null;
											String query = "select o from VenOrder o where o.wcsOrderId = '" + wcsOrderId + "'";
											orderList = sessionHome.queryByRange(query, 0, 1);
											if (orderList.size() > 0) {
												SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
												String s =  "";
												if(orderList.get(0).getOrderDate()!=null){
													s =  sdf.format(orderList.get(0).getOrderDate());
												}
										        StringBuilder sb = new StringBuilder(s);
												orderDate = sb.toString();
											}
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											try {
												if (locator != null) {
													locator.close();
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										
										//get FP date
										try {
											locator = new Locator<Object>();
											List<VenOrderStatusHistory> historyList = null;			
											VenOrderStatusHistorySessionEJBRemote historySessionHome = (VenOrderStatusHistorySessionEJBRemote) locator
											.lookup(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");
											
											String query = "select o from VenOrderStatusHistory o where o.venOrder.wcsOrderId='"+wcsOrderId+"'"+
																	" and o.venOrderStatus.orderStatusId="+VeniceConstants.VEN_ORDER_STATUS_FP +" order by o.id.historyTimestamp desc";
											historyList=historySessionHome.queryByRange(query, 0, 0);
											if(historyList.size()>0){
												SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
												String s =  sdf.format(historyList.get(0).getId().getHistoryTimestamp());
										        StringBuilder sb = new StringBuilder(s);
										        FpDate = sb.toString();
											}			
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											try {
												if (locator != null) {
													locator.close();
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										
										if (map != null) {
											map.clear();
										}
										
										FpDateString=FpDate!=null?", FP Date: "+FpDate:"";
										desc = task.getProcessInstance().getProcess().getName() == null ? "" : task.getProcessInstance().getProcess().getName() + " (" + iLogStatus + ", " + fraudPoint + ", " + wcsOrderId + ", Order Date: " + orderDate + FpDateString+")";
										map = createHashMapEntry(task, desc);							
									}
								}
								
								if (map != null && !excludedFromCurrentUser) {
									map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, fraudCaseId);
									map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID, fraudStatusId);
									map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, wcsOrderId);						
									dataList.add(map);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
							
					rafDsResponse.setData(dataList);		
					rafDsResponse.setEndRow(dataList.size());
					rafDsResponse.setTotalRows(dataList.size());
					
					BPMAdapter.removeBPMAdapter(userName);			
				} catch (ClientRepositoryException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("criteria null");
			rafDsResponse=null;
		}
				
		return rafDsResponse;
	}
	
	private boolean isIncludedAccordingToFilter(String key, String value) {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		//if there's no criteria at all, it's not filtered, include it in the map
		if (criteria == null) {
			return true;
		}
		List<JPQLSimpleQueryCriteria> simpleCriteriaList =criteria.getSimpleCriteria();
		List<JPQLSimpleQueryCriteria> simpleCriteriaListWithKeyFieldName = getSimpleCriteriaWithFieldName(key, simpleCriteriaList);
		
		if (simpleCriteriaListWithKeyFieldName.size()==1) {
			//if there is only one, this is simple Criteria which is not part of internal advanced criteria
			JPQLSimpleQueryCriteria simpleCriteria = simpleCriteriaListWithKeyFieldName.get(0);
			if (simpleCriteria.getOperator()!=null && simpleCriteria.getOperator().equals("iContains")) {
				if (value.contains(simpleCriteria.getValue())) {
					return true;
				}
			} else if (simpleCriteria.getOperator()!=null && simpleCriteria.getOperator().equals("equals")) {
				if (value.equals(simpleCriteria.getValue())) {
					return true;
				}
			}
		} else if (simpleCriteriaListWithKeyFieldName.size()==2) {
			//if there are two, this is simple Criteria which is part of internal advanced criteria
			JPQLSimpleQueryCriteria simpleCriteria1 = simpleCriteriaListWithKeyFieldName.get(0);
			JPQLSimpleQueryCriteria simpleCriteria2 = simpleCriteriaListWithKeyFieldName.get(1);
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			try {
				Date criteria1Date = formatter.parse(simpleCriteria1.getValue());
				Date criteria2Date = formatter.parse(simpleCriteria2.getValue());				
				Date valueDate = formatter.parse(value);
				if (simpleCriteria1.getOperator()!=null &&
					simpleCriteria1.getOperator().equals("greaterOrEqual") &&
					valueDate.after(criteria1Date)) {
					if (simpleCriteria2.getOperator()!=null &&
						simpleCriteria2.getOperator().equals("lessOrEqual") &&
						valueDate.before(criteria2Date)) {
							return true;
						}
				}
				if (simpleCriteria1.getOperator()!=null &&
					simpleCriteria1.getOperator().equals("lessOrEqual") &&
					valueDate.before(criteria1Date)) {
					if (simpleCriteria2.getOperator()!=null &&
						simpleCriteria2.getOperator().equals("greaterOrEqual") &&
							valueDate.after(criteria2Date)) {
							return true;
						}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return true;
			}
		} else {
			//if criteria doesn't even specify it, it's not filtered, include it in the map
			return true;
		}
		return false;
	}
	
	private List<JPQLSimpleQueryCriteria> getSimpleCriteriaWithFieldName(String fieldName, List<JPQLSimpleQueryCriteria> simpleCriteriaList) {
		ArrayList<JPQLSimpleQueryCriteria> simpleCriteriaListWithFieldName = new ArrayList<JPQLSimpleQueryCriteria>();
		for (int i=0;i<simpleCriteriaList.size();i++) {
			if (simpleCriteriaList.get(i).getFieldName().equals(fieldName)) {
				simpleCriteriaListWithFieldName.add(simpleCriteriaList.get(i));
			}
		}
		return simpleCriteriaListWithFieldName;
	}
	
	private LinkedHashMap<String, String> createHashMapEntry(Task task, String desc) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		String key = DataNameTokens.TASKID;
		String value = new Long(task.getId()).toString();
		if (isIncludedAccordingToFilter(key,value)) {
			map.put(key, value);
		} else {
			return null;
		}
		
		key = DataNameTokens.TASKTYPE;
		value = task.getProcessInstance().getProcess().getName();
		if (isIncludedAccordingToFilter(key,value)) {
			map.put(key, value);
		} else {
			return null;
		}
		
		key = DataNameTokens.TASKDESCRIPTION;
		if(desc!=null){
			value=desc;
		}else{
			value = task.getActivityName();
		}
		if (isIncludedAccordingToFilter(key,value)) {
			map.put(key, value);
		} else {
			return null;
		}
		
		if (task.getAssignedToRole()!=null && !task.getAssignedToRole().isEmpty()) {			
			key = DataNameTokens.TASKSTATUS;
			value =  StatusNameTokens.OPEN;
			if (isIncludedAccordingToFilter(key,value)) {
				map.put(key, value);
			} else {
				return null;
			}
			
			key = DataNameTokens.TASKASSIGNEE;
			value = task.getParticipantDisplayName();
			if (isIncludedAccordingToFilter(key,value)) {
				map.put(key, value);
			} else {
				return null;
			}
		} else {
			key = DataNameTokens.TASKSTATUS;
			value = StatusNameTokens.INPROCESS;
			if (isIncludedAccordingToFilter(key,value)) {
				map.put(key, value);
			} else {
				return null;
			}
			
			key = DataNameTokens.TASKASSIGNEE;
			value = task.getParticipantDisplayName();
			if (isIncludedAccordingToFilter(key,value)) {
				map.put(key, value);
			} else {
				return null;
			}
		}
		
		DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
		
		key = DataNameTokens.TASKCREATEDDATE;
		value = formatter.format(task.getReceivedDate().getTime());
		if (isIncludedAccordingToFilter(key,value)) {
			map.put(key, value);
		} else {
			return null;
		}
		
		key = DataNameTokens.TASKDUEDATE;
		value = formatter.format(task.getDueDate().getTime());
		if (isIncludedAccordingToFilter(key,value)) {
			map.put(key, value);
		} else {
			return null;
		}
		
		return map;
	}
}