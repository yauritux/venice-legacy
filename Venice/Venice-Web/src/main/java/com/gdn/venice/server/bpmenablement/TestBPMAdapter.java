package com.gdn.venice.server.bpmenablement;


public class TestBPMAdapter {
	
	static boolean isCompleted = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter("Henry", BPMAdapter.getUserPasswordFromLDAP("Henry"));
		
		bpmAdapter.synchronize();
//		
//		try {
//			List<Long> taskIds = bpmAdapter.getClientRepository().loadTaskIdsForSavedSearch(1);
//			
		
//			Task task = bpmAdapter.getClientRepository().loadTask(658);
//			
			
//			com.lombardisoftware.webapi.Process process = new com.lombardisoftware.webapi.Process();
//			process.setName("Logistics Activity Report Approval");
//			
//			bpmAdapter.getWebAPI().startProcess(process, null);
//			
//			HashMap map = new HashMap();
//			map.put("orderId", "FROM APPLICATION");
//			bpmAdapter.startBusinessProcess("Logistics Activity Report Approval", map);
//			
			
//			
			
//			bpmAdapter.getWebAPI().assignTask(6);
//			Variable[] vars = task.getAttachedExternalActivity().getData().getVariables();

//			
//			bpmAdapter.getWebAPI().reassignTask(task.getId());
//			
			
//			Variable[] outputs = new Variable[] {
//	                new Variable("justAString", "STRING FROM ADAPTER")                
//	        };
//			
//			bpmAdapter.getWebAPI().completeTask(task.getId(), outputs);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
			
	}

}
