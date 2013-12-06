package com.gdn.venice.client.app.fraud.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface FraudCaseViewerUiHandlers extends UiHandlers {
	public List<DataSource> onShowFraudCaseDetailData(String caseId, String orderId);
}