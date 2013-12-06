package com.gdn.venice.client.app.task.widgets;

import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.app.general.widgets.OrderDetailContentLayout;
import com.gdn.venice.client.app.task.view.command.PickupProblemInvestigationTaskDetail;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class LogisticsPickUpProblemInvestigation extends VLayout {
	public LogisticsPickUpProblemInvestigation(Record record) {
		String taskId = record.getAttributeAsString(DataNameTokens.TASKID);
		
		List<DataSource> dataSources = PickupProblemInvestigationTaskDetail.getOrderDetailDataSources(taskId);
		
		final OrderDetailContentLayout orderDetailContentLayout = new OrderDetailContentLayout(
				dataSources.get(0), //Order Detail
				dataSources.get(1), //Order Item
				dataSources.get(2), //Order Customer
				dataSources.get(3), //Order Customer Address
				dataSources.get(4), //Order Customer Contact
				dataSources.get(5), //Order Logistics Airway Bill
				dataSources.get(6), //Order Finance Payment
				dataSources.get(7), //Order Finance Reconciliation
				dataSources.get(8), //Order History Order
				dataSources.get(9) //Order History Order Item
				);
		
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonSuspiciousFraud().setVisibility(Visibility.HIDDEN);
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonFraudPassed().setVisibility(Visibility.HIDDEN);
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonFraudConfirmed().setVisibility(Visibility.HIDDEN);
		orderDetailContentLayout.getOrderDataOrderDetailTab().getButtonBlock().setVisibility(Visibility.HIDDEN);
		
		orderDetailContentLayout.getOrderDataFinanceTab().getOrderFinancePaymentListGrid().addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				String paymentId = event.getRecord().getAttributeAsString(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID);
				orderDetailContentLayout.getOrderDataFinanceTab().loadFinanceReconciliationData(GeneralData.getOrderFinanceReconciliationData(paymentId));
			}
		});
		
		orderDetailContentLayout.getOrderDataFinanceTab().getButtonApproveVA().setVisibility(Visibility.HIDDEN);
		
		setMembers(orderDetailContentLayout);
	}

}
