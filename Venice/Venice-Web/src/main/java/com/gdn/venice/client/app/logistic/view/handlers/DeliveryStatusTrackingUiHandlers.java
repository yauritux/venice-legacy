package com.gdn.venice.client.app.logistic.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface DeliveryStatusTrackingUiHandlers extends UiHandlers {
	public DataSource onShowMerchantPickUpDetail(String airwayBillId);
	public List<DataSource> onShowDeliveryStatusTrackingDetail(String airwayBillId, String orderId);
	public void onFetchComboBoxData(); 
}
