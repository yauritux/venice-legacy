package com.gdn.venice.server.app.fraud.presenter.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseFraudManagementDataCommand implements RafDsCommand {
	String username;
	String password;
	RafDsRequest request;
	
	public FetchFraudCaseFraudManagementDataCommand(String username, String password, RafDsRequest request){
		this.username = username;
		this.password = password;
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		//Dapatkan role dari BPM (Lombardi), yang akhirnya di gunakan untuk menentukan apakah masih bisa escalate atau tidak
		String ableToEscalate = "FALSE"; //Default nilai FALSE, tapi bisa berisi TRUE
		
		if(request.getParams().get(DataNameTokens.TASKID) != null && !request.getParams().get(DataNameTokens.TASKID).equalsIgnoreCase("")){
			//Buka koneksi ke lombardi
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(username, password);
			bpmAdapter.synchronize();

			//Ambil role saat ini dari bpm (lombardi)
			Long taskId = new Long(request.getParams().get(DataNameTokens.TASKID));
			try {
				bpmAdapter.getWebAPI().reassignTask(taskId);
				String role = bpmAdapter.getWebAPI().getTask(taskId).getParticipantDisplayName();
				bpmAdapter.getWebAPI().assignTask(taskId);
				
				Properties properties = new Properties();
				try {
					properties.load(new FileInputStream(System.getenv("VENICE_HOME") +  "/lib/WebAPIFactory.properties"));
					String[] roles = properties.getProperty("fraud.bpm.role.hierarchy").split("~");
					
					for (int i = 0; i < roles.length; i++) {
						if (roles[i].equalsIgnoreCase(role) && i < roles.length - 1) {
							ableToEscalate = "TRUE";
							break;
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();			
			FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			
			JPQLAdvancedQueryCriteria fraudCaseCriteria = request.getCriteria() != null ? request.getCriteria() : new JPQLAdvancedQueryCriteria();
			fraudCaseCriteria.setBooleanOperator("and");
			JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
			caseIdCriteria.setFieldName(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
			caseIdCriteria.setOperator("equals");
			caseIdCriteria.setValue(request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
			caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
			fraudCaseCriteria.add(caseIdCriteria);
			
			FrdFraudSuspicionCase fraudCaseSuspicion = new FrdFraudSuspicionCase();
			List<FrdFraudSuspicionCase> fraudCase = sessionHome.findByFrdFraudSuspicionCaseLike(fraudCaseSuspicion, fraudCaseCriteria, 0, 1);
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			
			for(int i = 0; i < fraudCase.size(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdFraudSuspicionCase list = fraudCase.get(i);

				map.put(DataNameTokens.TASKID, Util.isNull(request.getParams().get(DataNameTokens.TASKID), "").toString());
				map.put(DataNameTokens.TASKABLETOESCALATE, ableToEscalate);
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, Util.isNull(list.getFraudSuspicionCaseId(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID, list.getFrdFraudCaseStatus()!=null && list.getFrdFraudCaseStatus().getFraudCaseStatusId()!=null?list.getFrdFraudCaseStatus().getFraudCaseStatusId().toString():"");
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, list.getFrdFraudCaseStatus()!=null && list.getFrdFraudCaseStatus().getFraudCaseStatusDesc()!=null?list.getFrdFraudCaseStatus().getFraudCaseStatusDesc().toString():"");
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, formatter.format(list.getFraudCaseDateTime()));								 
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, Util.isNull(list.getFraudCaseDesc(), "").toString());				
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES, Util.isNull(list.getFraudSuspicionNotes(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, list.getVenOrder()!=null && list.getVenOrder().getVenOrderStatus()!=null?list.getVenOrder().getVenOrderStatus().getOrderStatusCode():"");
				if(list.getFrdFraudCaseStatus().getFraudCaseStatusDesc().equals("FC")){
					if(list.getSuspicionReason().equals("Calculated by System") || list.getSuspicionReason().equals("Fraud By Bank")){
						map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_TYPE_FC, "FC");
						map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, "Fraud By Bank");		
					}else {
						map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_TYPE_FC, "HRSF");
						map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, Util.isNull(list.getSuspicionReason(), "").toString());	
					}
				}else{
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_TYPE_FC, "");
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, Util.isNull(list.getSuspicionReason(), "").toString());		
				}
				

				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
