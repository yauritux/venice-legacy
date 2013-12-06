package com.gdn.venice.client.app.finance.view.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FundInReconciliationUiHandlers extends UiHandlers {
	public void onSubmitForApproval(ArrayList<String> fundInReconRecordIds);
	public void onAllocate(String sourceVenWCSOrderId, String sourceVenOrderPaymentId, String fundsInReconRecordId, String destinationVenOrderPaymentId, String allocationAmount, String destinationVenOrderId);
	public void onRefundButtonClicked(HashMap<String, String> refundDataMap);
	public void onDelete(HashMap<String, String> deleteDataMap);
	public void onFetchComboBoxData();
	void onCancelAllocationButtonClicked(HashMap<String, String> allocationDataMap);
	public void onCreateJournalForVA(ArrayList<String> fundInReconRecordIds);
}
