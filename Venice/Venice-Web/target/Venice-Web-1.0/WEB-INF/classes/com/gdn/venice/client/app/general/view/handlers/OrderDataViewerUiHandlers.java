package com.gdn.venice.client.app.general.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface OrderDataViewerUiHandlers extends UiHandlers {
	public List<DataSource> onShowOrderDetailData(String orderId);
	public DataSource onShowOrderFinanceReconciliationData(String paymentId);	
	public void onUpdateOrderStatus(String orderId,String status);	
	public void onBlockOrder(String orderId, String flag, String source, String reason, String orderStatus);	
	public void onApproveVAPayment(String paymentId);
	public void onApproveCSPayment(String paymentId);
	public void onRejectCSPayment(String paymentId);
	public void onPendingCSPayment(String paymentId);
	public void onFetchComboBoxData(int startRow);
	public void onCountTotalData();
}
