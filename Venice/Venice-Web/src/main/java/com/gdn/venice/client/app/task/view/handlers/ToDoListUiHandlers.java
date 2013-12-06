package com.gdn.venice.client.app.task.view.handlers;

import java.util.HashMap;
import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ToDoListUiHandlers extends UiHandlers {
	
	/*
	 * Modified this interface to pass the task type down - DF
	 */
	public void onClaimTask(Long taskType, List<String> taskIds);
	public void onUnclaimTask(Long taskType, List<String> taskIds);
	public void onCompleteTask(Long taskType, List<String> taskIds);
	public void onRefundButtonClicked(HashMap<String, String> refundDataMap);
	public void onAllocate(String sourceVenWCSOrderId,String sourceVenOrderPaymentId, String fundsInReconRecordId, String destinationVenOrderPaymentId, String allocationAmount, String destinationVenOrderId);
	public void onGetUserRoleData(); 
}
