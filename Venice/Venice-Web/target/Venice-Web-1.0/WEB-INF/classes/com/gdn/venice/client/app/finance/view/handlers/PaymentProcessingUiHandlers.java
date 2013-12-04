package com.gdn.venice.client.app.finance.view.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface PaymentProcessingUiHandlers extends UiHandlers {

	void onProcessPaymentButtonClicked(DataSource dataSource, String paymentData);

	void onMakePaymentButtonClicked(HashMap<String, String> paymentDataMap);

	void onSubmitForApproval(ArrayList<String> apPaymentIdList);
	
	void onDoneButtonClicked(ArrayList<String> apPaymentIdList);
	
	void onExportButtonClicked(ArrayList<String> apPaymentIdList);
}

