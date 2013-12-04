package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class FraudCaseManagementPaymentTab extends Tab {
	DynamicForm paymentDetailForm;
	ListGrid fraudCasePaymentListGrid;

	public FraudCaseManagementPaymentTab(String title, DataSource paymentDetailData, DataSource paymentData) {
		super(title);
		
		VLayout paymentLayout = new VLayout();
		
		//Header form
		HLayout paymentDetailLayout = new HLayout();
		paymentDetailLayout.setWidth(500);
		paymentDetailLayout.setHeight(100);
		paymentDetailForm = new DynamicForm();
		paymentDetailForm.setWidth100();
		paymentDetailForm.setTitleWidth(130);
		paymentDetailForm.setDataSource(paymentDetailData);  
		paymentDetailForm.setUseAllDataSourceFields(true);  
		paymentDetailForm.setNumCols(2);
		paymentDetailForm.setMargin(10);
		paymentDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(paymentDetailData));		
		Util.formatFormItemAsCurrency(paymentDetailForm.getField(DataNameTokens.VENORDERITEM_PRODUCTPRICE));
		Util.formatFormItemAsCurrency(paymentDetailForm.getField(DataNameTokens.VENORDERITEM_SHIPPINGCOST));
		Util.formatFormItemAsCurrency(paymentDetailForm.getField(DataNameTokens.VENORDERITEM_INSURANCECOST));		
		Util.formatFormItemAsCurrency(paymentDetailForm.getField(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT));
		Util.formatFormItemAsCurrency(paymentDetailForm.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE));
		Util.formatFormItemAsCurrency(paymentDetailForm.getField(DataNameTokens.VENORDERPAYMENT_TOTALTRANSACTION));
		paymentDetailLayout.setMembers(paymentDetailForm);
		
		//Grid payment detail
		VLayout fraudCasePaymentLayout = new VLayout();
		Label fraudCasePaymentItemLabel = new Label("<b>Payment Item:</b>");
		fraudCasePaymentItemLabel.setHeight(10);
		
		fraudCasePaymentListGrid = new ListGrid();
		fraudCasePaymentListGrid.setWidth100();
		fraudCasePaymentListGrid.setHeight100();		
		fraudCasePaymentListGrid.setSortField(0);
		fraudCasePaymentListGrid.setShowRowNumbers(true);
		fraudCasePaymentListGrid.setShowRollOver(false);
		fraudCasePaymentListGrid.setAutoFetchData(true);
		fraudCasePaymentListGrid.setDataSource(paymentData);
		fraudCasePaymentListGrid.setFields(Util.getListGridFieldsFromDataSource(paymentData));
		fraudCasePaymentListGrid.setFields(
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, 80),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC, 120),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO, 180),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSCODE, 80),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME, 100),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK, 100),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION, 100),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT, 100),		
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, 100),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE, 100),
			new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENADDRESS, 150)
		);

		Util.formatListGridFieldAsCurrency(fraudCasePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT));
		Util.formatListGridFieldAsCurrency(fraudCasePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(fraudCasePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE));
		
		//Put all components on the layout
		fraudCasePaymentLayout.setMembers(fraudCasePaymentItemLabel, fraudCasePaymentListGrid);	
		paymentLayout.setMembers(paymentDetailLayout, fraudCasePaymentLayout);
		setPane(paymentLayout);
	}
	
	public void refreshFinancePaymentData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudCasePaymentListGrid.setData(response.getData());
			}
		};
		
		fraudCasePaymentListGrid.getDataSource().fetchData(fraudCasePaymentListGrid.getFilterEditorCriteria(), callBack);
	}	
	
	public DynamicForm getpaymentDetailForm() {
		return paymentDetailForm;
	}	
}