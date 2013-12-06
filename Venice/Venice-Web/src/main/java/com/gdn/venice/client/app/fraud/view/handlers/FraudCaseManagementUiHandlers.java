package com.gdn.venice.client.app.fraud.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface FraudCaseManagementUiHandlers extends UiHandlers {
	public List<DataSource> onShowFraudCaseDetailData(String taskId, String caseId, String orderId);
	public void onCloseTask(List<String> taskIds);
}