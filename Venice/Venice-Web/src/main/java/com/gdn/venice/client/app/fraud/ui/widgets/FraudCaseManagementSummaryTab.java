package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class FraudCaseManagementSummaryTab extends Tab {	
	DynamicForm fraudCaseRiskScoreForm, fraudCaseCustomerForm, fraudCaseCustomerAddressForm, fraudCaseOrderForm, fraudCaseIlogRecomendationForm, fraudCasePaymentForm;
	
	public FraudCaseManagementSummaryTab(
			String title,
			DataSource totalRiskScoreData,
			DataSource customerSummaryData,
			DataSource orderData,
			DataSource riskScoreData,
			DataSource paymentTypeData,
			DataSource ilogRecomendationData,
			DataSource paymentSummaryData,
			DataSource customerAddressData,
			DataSource contactDetailData,
			DataSource categoryData) {
		
		super(title);
		
		//Create summary window portlet
		Canvas canvasSummary = new Canvas();
		canvasSummary.addChild(createWindow("Risk Score", createRiskScoreWindow(totalRiskScoreData, ilogRecomendationData), "46%", "145", "2%", "0"));
		canvasSummary.addChild(createWindow("Order", createOrderWindow(orderData, categoryData), "46%", "145", "52%", "0"));
		canvasSummary.addChild(createWindow("Customer", createCustomerWindow(customerSummaryData, customerAddressData, contactDetailData),	"46%", "190", "2%", "150"));
		canvasSummary.addChild(createWindow("Payment and Shipment", createPaymentWindow(paymentTypeData, paymentSummaryData), "46%", "190", "52%", "150"));
		canvasSummary.addChild(createWindow("Risk Score Detail", createRiskDetailWindow(riskScoreData), "96%", "200", "2%", "350"));
		setPane(canvasSummary);
	}

	//Window Risk point summary
	private Layout createRiskScoreWindow(DataSource totalRiskScoreData, DataSource ilogRecomendationData) {
		final VLayout fraudCaseRiskScoreLayout = new VLayout();
		fraudCaseRiskScoreLayout.setPadding(3);
		
		//Grab total risk score
		totalRiskScoreData.fetchData(null, new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				final RecordList records = response.getDataAsRecordList();
				Canvas content = new Canvas() {{
					setHeight(20);
		            setContents("<div class=\"formTitle\" style=\"float:left;text-align:right;width:95px;padding:3px 5px\">Total Risk Score : </div><span style=\"font-weight:bold;font-size:16px\">" + records.get(0).getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_TOTALRISKSCORE) + "</span>");
		        }};
				fraudCaseRiskScoreLayout.addMember(content);
			}
		});
		
		//Grab iLog status (FP, SF, FC)
		ilogRecomendationData.fetchData(null, new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				final RecordList records = response.getDataAsRecordList();
				Canvas content = new Canvas() {{
					setHeight(20);
		            setContents("<div class=\"formTitle\" style=\"float:left;text-align:right;width:95px;padding:3px 5px\">Recomendation : </div><span style=\"font-weight:bold;font-size:16px\">" + records.get(0).getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_ILOGRECOMENDATION) + "</span>");
		        }};
				fraudCaseRiskScoreLayout.addMember(content);
			}
		});

		//Return window
		setPane(fraudCaseRiskScoreLayout);
		return fraudCaseRiskScoreLayout;
	}

	//Window customer detail
	private Layout createCustomerWindow(DataSource customerSummaryData, DataSource customerAddressData, DataSource contactDetailData) {		
		VLayout customerLayout = new VLayout();
		HLayout customerFormLayout = new HLayout();
		
		//Form customer detail 
		fraudCaseCustomerForm = new DynamicForm();
		fraudCaseCustomerForm.setDataSource(customerSummaryData);
		fraudCaseCustomerForm.setUseAllDataSourceFields(false);
		fraudCaseCustomerForm.setNumCols(2);
		fraudCaseCustomerForm.setFields(Util.getReadOnlyFormItemFromDataSource(customerSummaryData));
		fraudCaseCustomerForm.setWidth100();
		fraudCaseCustomerForm.setTitleWidth(70);
		
		//Form customer address detail
		fraudCaseCustomerAddressForm = new DynamicForm();
		fraudCaseCustomerAddressForm.setDataSource(customerAddressData);
		fraudCaseCustomerAddressForm.setUseAllDataSourceFields(false);
		fraudCaseCustomerAddressForm.setNumCols(2);
		fraudCaseCustomerAddressForm.setFields(Util.getReadOnlyFormItemFromDataSource(customerAddressData));
		fraudCaseCustomerAddressForm.setWidth100();
		fraudCaseCustomerAddressForm.setTitleWidth(60);
		
		//Grid contact detail
		ListGrid contactDetailListGrid = new ListGrid();
		contactDetailListGrid.setSortField(0);
		contactDetailListGrid.setShowRollOver(false);
		contactDetailListGrid.setAutoFetchData(true);			
		contactDetailListGrid.setDataSource(contactDetailData);
		contactDetailListGrid.setFields(
				new ListGridField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID),
				new ListGridField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC),
				new ListGridField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL)
				
		);
		contactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setWidth(70);
		contactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setHidden(true);
		contactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setWidth(100);
		contactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setWidth(120);
		
		//Add forms to window, and return window
		customerFormLayout.setMembers(fraudCaseCustomerForm, fraudCaseCustomerAddressForm);
		customerLayout.setMembers(customerFormLayout, contactDetailListGrid);
		setPane(customerLayout);
		return customerLayout;
	}

	//Window order detail
	private Layout createOrderWindow(DataSource orderData, DataSource categoryData) {		
		VLayout fraudCaseOrderLayout = new VLayout();
		
		//Form order detail
		fraudCaseOrderForm = new DynamicForm();
		fraudCaseOrderForm.setDataSource(orderData);
		fraudCaseOrderForm.setUseAllDataSourceFields(false);
		fraudCaseOrderForm.setNumCols(4);
		fraudCaseOrderForm.setFields(
				new StaticTextItem(DataNameTokens.VENORDER_WCSORDERID),
				new StaticTextItem(DataNameTokens.VENORDER_AMOUNT),
				new StaticTextItem(DataNameTokens.VENORDERITEM_PRODUCTPRICE)
		);
		Util.formatFormItemAsCurrency(fraudCaseOrderForm.getField(DataNameTokens.VENORDERITEM_PRODUCTPRICE));
		Util.formatFormItemAsCurrency(fraudCaseOrderForm.getField(DataNameTokens.VENORDER_AMOUNT));
		
		//Order item list grid
		ListGrid categoryListGrid = new ListGrid();
		categoryListGrid.setSortField(0);
		categoryListGrid.setShowRollOver(false);
		categoryListGrid.setAutoFetchData(true);			
		categoryListGrid.setDataSource(categoryData);
		categoryListGrid.setFields(
				new ListGridField(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY),
				new ListGridField(DataNameTokens.VENORDERITEM_TOTAL)
		);
		Util.formatListGridFieldAsCurrency(categoryListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL));
		categoryListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL).setWidth(100);
		
		//Add form and list grid to window, and return window
		fraudCaseOrderLayout.setMembers(fraudCaseOrderForm, categoryListGrid);
		setPane(fraudCaseOrderLayout);
		return fraudCaseOrderLayout;
	}
	
	//Window payment detail
	private Layout createPaymentWindow(DataSource paymentTypeData, DataSource paymentSummaryData) {
		VLayout fraudCasePaymentLayout = new VLayout();
		
		//Form payment detail
		fraudCasePaymentForm = new DynamicForm();
		fraudCasePaymentForm.setDataSource(paymentSummaryData);
		fraudCasePaymentForm.setUseAllDataSourceFields(false);
		fraudCasePaymentForm.setNumCols(4);		
		fraudCasePaymentForm.setFields(Util.getReadOnlyFormItemFromDataSource(paymentSummaryData));
		fraudCasePaymentForm.setTitleWidth(150);
		
		//Grid payment detail
		ListGrid paymentTypeListGrid = new ListGrid();
		paymentTypeListGrid.setSortField(0);
		paymentTypeListGrid.setShowRollOver(false);
		paymentTypeListGrid.setAutoFetchData(true);			
		paymentTypeListGrid.setDataSource(paymentTypeData);
		paymentTypeListGrid.setFields(
				new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID),
				new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC),
				new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO),
				new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME),
				new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK),				
				new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION),
				new ListGridField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT)
				
		);
		paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID).setWidth(70);
		paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC).setWidth(100);
		paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO).setWidth(180);
		paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME).setWidth(90);
		paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK).setWidth(90);
		paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION).setWidth(100);
		paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT).setWidth(100);
		Util.formatListGridFieldAsCurrency(paymentTypeListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_LIMIT));
		
		//Add form and list grid to window, and return window
		fraudCasePaymentLayout.setMembers(fraudCasePaymentForm, paymentTypeListGrid);
		setPane(fraudCasePaymentLayout);
		return fraudCasePaymentLayout;
	}
	
	//Window risk score detail
	private Layout createRiskDetailWindow(DataSource riskScoreData) {
		VLayout riskDetailLayout = new VLayout();
		
		//Grid risk score
		ListGrid riskDetailListGrid = new ListGrid();
		riskDetailListGrid.setSortField(0);
		riskDetailListGrid.setShowRowNumbers(true);
		riskDetailListGrid.setShowRollOver(false);
		riskDetailListGrid.setAutoFetchData(true);
		riskDetailListGrid.setDataSource(riskScoreData);
		riskDetailListGrid.setFields(
				new ListGridField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDRULENAME),
				new ListGridField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_RISKPOINTS)
		);
		riskDetailListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_RISKPOINTS).setWidth(80);
		
		//Add grid to layout and return
		riskDetailLayout.addMember(riskDetailListGrid);
		return riskDetailLayout;
	}
	
	//General function for creating summary window widget
	private Window createWindow(String title, Layout layout, String width, String height, String offsetLeft, String offsetTop) {
		Window window = new Window();
		window.setTitle(title);
		window.setWidth(width);
		window.setHeight(height);
		window.setLeft(offsetLeft);
		window.setTop(offsetTop);
		window.setShowMinimizeButton(false);
		window.setShowCloseButton(false);
		window.setCanDragReposition(false);
		window.setCanDragResize(false);
		window.addItem(layout);

		return window;
	}
	
	public DynamicForm getFraudCaseRiskScoreForm() {
		return fraudCaseRiskScoreForm;
	}
	
	public DynamicForm getFraudCaseCustomerForm() {
		return fraudCaseCustomerForm;
	}
	
	public DynamicForm getFraudCaseCustomerAddressForm() {
		return fraudCaseCustomerAddressForm;
	}
	
	public DynamicForm getFraudCaseOrderForm() {
		return fraudCaseOrderForm;
	}
	
	public DynamicForm getFraudCaseIlogrecomendationForm() {
		return fraudCaseIlogRecomendationForm;
	}
	
	public DynamicForm getFraudCasePaymentForm() {
		return fraudCasePaymentForm;
	}	
}