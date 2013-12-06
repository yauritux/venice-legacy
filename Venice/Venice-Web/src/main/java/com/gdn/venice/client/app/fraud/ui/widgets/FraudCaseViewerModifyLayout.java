package com.gdn.venice.client.app.fraud.ui.widgets;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class FraudCaseViewerModifyLayout extends HLayout {

	public FraudCaseViewerModifyLayout(
			final DataSource totalRiskScoreData,
			final DataSource customerSummaryData,
			final DataSource customerData, 
			final DataSource customerAddressData, 
			final DataSource customerContactData,
			final DataSource orderDetailData,
			final DataSource orderItemData,
			final DataSource paymentData,
			final DataSource riskScoreData,
			final DataSource relatedOrderData,
			final DataSource fraudManagementData,
			final DataSource actionLogData,
			final DataSource historyLogData,
			final DataSource paymentTypeData,
			final DataSource ilogRecomendationData,
			final DataSource attachmentData,
			final DataSource paymentSummary,
			final DataSource paymentDetail,
			final DataSource whiteListData,
			final DataSource filterOrderHistListData,
			String caseId,
			final DataSource contactDetailData,
			final DataSource categoryData
			) {
		TabSet fraudCaseManagementTabSet = new TabSet();

		fraudCaseManagementTabSet.setTabBarPosition(Side.TOP);
		fraudCaseManagementTabSet.setWidth100();
		fraudCaseManagementTabSet.setHeight100();
		
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementSummaryTab("Summary", totalRiskScoreData, customerSummaryData, orderDetailData, riskScoreData, paymentTypeData, ilogRecomendationData, paymentSummary, customerAddressData, contactDetailData, categoryData));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementCustomerTab("Customer Detail", customerData, customerAddressData, customerContactData));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementOrderTab("Order Detail", orderDetailData, orderItemData));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementPaymentTab("Payment Detail", paymentDetail, paymentData));
		fraudCaseManagementTabSet.addTab(new FraudCaseViewerFraudManagementModifyTab("Fraud Management", riskScoreData, relatedOrderData, fraudManagementData, totalRiskScoreData, actionLogData, historyLogData, attachmentData, caseId));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementOrderHistoryTab("Order History", whiteListData, filterOrderHistListData));
		
		fraudCaseManagementTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if (event.getTab() instanceof FraudCaseManagementCustomerTab) {
					((FraudCaseManagementCustomerTab) event.getTab()).getFraudCaseCustomerForm().fetchData();
					((FraudCaseManagementCustomerTab) event.getTab()).getFraudCaseCustomerAddressForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseManagementOrderTab) {
					((FraudCaseManagementOrderTab) event.getTab()).getFraudCaseOrderForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseManagementSummaryTab) {
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCaseCustomerForm().fetchData();
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCaseOrderForm().fetchData();
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCaseCustomerAddressForm().fetchData();
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCasePaymentForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseViewerFraudManagementModifyTab){
					((FraudCaseViewerFraudManagementModifyTab) event.getTab()).getFraudCaseManagementForm().fetchData();
					((FraudCaseViewerFraudManagementModifyTab) event.getTab()).getFraudCaseManagementTotalRiskForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseManagementPaymentTab){
					((FraudCaseManagementPaymentTab) event.getTab()).getpaymentDetailForm().fetchData();
				}else if (event.getTab() instanceof FraudCaseManagementOrderHistoryTab){
					((FraudCaseManagementOrderHistoryTab) event.getTab()).getFraudCasetOrderHistoryForm().fetchData();
				}
			}
		});
		
		setWidth100();
		setHeight100();		
		setMembers(fraudCaseManagementTabSet);
	}	
}
