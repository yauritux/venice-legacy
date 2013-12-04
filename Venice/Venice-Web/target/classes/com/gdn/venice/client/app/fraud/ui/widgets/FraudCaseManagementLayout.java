package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class FraudCaseManagementLayout extends HLayout {

	public FraudCaseManagementLayout(
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
			final DataSource moreInfoData,
			final DataSource ilogRecomendationData,
			final DataSource attachmentData,
			final DataSource paymentSummaryData,
			final DataSource paymentDetailData,
			final DataSource relatedOrderItemData,
			final DataSource relatedFraudData,
			final DataSource whiteListData,
			final DataSource filterOrderHistListData,
			final String caseId,
			final DataSource contactDetailData,
			final DataSource categoryData
			) {
		TabSet fraudCaseManagementTabSet = new TabSet();

		fraudCaseManagementTabSet.setTabBarPosition(Side.TOP);
		fraudCaseManagementTabSet.setWidth100();
		fraudCaseManagementTabSet.setHeight100();
		
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementSummaryTab("Summary", totalRiskScoreData, customerSummaryData, orderDetailData, riskScoreData, paymentTypeData, ilogRecomendationData, paymentSummaryData, customerAddressData, contactDetailData, categoryData));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementCustomerTab("Customer Detail", customerData, customerAddressData, customerContactData));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementOrderTab("Order Detail", orderDetailData, orderItemData));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementPaymentTab("Payment Detail", paymentDetailData, paymentData));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementFraudTab("Fraud Management", riskScoreData, relatedOrderData, fraudManagementData, totalRiskScoreData, actionLogData, historyLogData, attachmentData, caseId));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementMoreInfoTab("More Information", orderDetailData, relatedOrderData, relatedOrderItemData, relatedFraudData, moreInfoData, caseId));
		fraudCaseManagementTabSet.addTab(new FraudCaseManagementOrderHistoryTab("Order History", whiteListData, filterOrderHistListData));
		
		fraudCaseManagementTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(final TabSelectedEvent event) {
				if (event.getTab() instanceof FraudCaseManagementCustomerTab) {
					((FraudCaseManagementCustomerTab) event.getTab()).getFraudCaseCustomerForm().fetchData();
					((FraudCaseManagementCustomerTab) event.getTab()).getFraudCaseCustomerAddressForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseManagementOrderTab) {
					((FraudCaseManagementOrderTab) event.getTab()).getFraudCaseOrderForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseManagementSummaryTab) {
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCaseCustomerForm().fetchData();
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCaseCustomerAddressForm().fetchData();
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCaseOrderForm().fetchData();
					((FraudCaseManagementSummaryTab) event.getTab()).getFraudCasePaymentForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseManagementMoreInfoTab) {
					((FraudCaseManagementMoreInfoTab) event.getTab()).getFraudCaseOrderForm().fetchData();
				} else if (event.getTab() instanceof FraudCaseManagementFraudTab){
					final DynamicForm fraudCaseManagementForm = ((FraudCaseManagementFraudTab) event.getTab()).getFraudCaseManagementForm();
					fraudCaseManagementForm.fetchData(new Criteria(), new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if(fraudCaseManagementForm.getFields()[1].getDisplayValue().equalsIgnoreCase("false")) {
								ToolStrip toolbarFraud = (ToolStrip) fraudCaseManagementForm.getParentElement().getChildren()[0];
								toolbarFraud.getChildren()[3].setVisible(false);
								toolbarFraud.getChildren()[4].setVisible(false);
								toolbarFraud.getChildren()[5].setVisible(false);
							}
							
							//if status order already FP or FC, disable close case button so it does not publish again to WCS and MTA
							String orderStatus = fraudCaseManagementForm.getValue(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE).toString();
							if(orderStatus.equals("FP") || orderStatus.equals("FC") || orderStatus.equals("X")){								
								((FraudCaseManagementFraudTab) event.getTab()).getCloseCaseButton().setDisabled(true);	
							}else{
								((FraudCaseManagementFraudTab) event.getTab()).getCloseCaseButton().setDisabled(false);
							}
						}
					});				
					((FraudCaseManagementFraudTab) event.getTab()).getFraudCaseManagementTotalRiskForm().fetchData();
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