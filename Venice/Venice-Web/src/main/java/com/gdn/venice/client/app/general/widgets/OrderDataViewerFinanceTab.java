package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class OrderDataViewerFinanceTab extends Tab {
	ListGrid orderFinancePaymentListGrid;
	ListGrid orderFinanceReconciliationListGrid;
	
	IButton buttonApproveVA;
	IButton buttonApproveCS;
	IButton buttonRejectCS;
	IButton buttonPendingCS;


	public OrderDataViewerFinanceTab(String title, DataSource financePaymentData, DataSource financeReconciliationData) {
		super(title);
		
		VLayout orderFinanceVLayout = new VLayout();
		
		HLayout paymentStatusButtons = new HLayout(5);
		paymentStatusButtons.setHeight(20);
		
		buttonApproveVA = new IButton("Approve VA");
		
		buttonApproveCS = new IButton("Approve CS Payment");
		buttonRejectCS = new IButton("Reject CS Payment");
		buttonPendingCS = new IButton("Pending CS Payment");
		
		paymentStatusButtons.addMember(buttonApproveVA);
		paymentStatusButtons.addMember(buttonApproveCS);
		paymentStatusButtons.addMember(buttonRejectCS);
		paymentStatusButtons.addMember(buttonPendingCS);
		
		buttonApproveVA.setDisabled(true);
		
		buttonApproveCS.setDisabled(true);
		buttonApproveCS.setAutoFit(true);
		
		buttonRejectCS.setDisabled(true);
		buttonRejectCS.setAutoFit(true);
		
		buttonPendingCS.setDisabled(true);
		buttonPendingCS.setAutoFit(true);
		
		HLayout orderFinanceLayout  = new HLayout();
		
		Label orderFinancePaymentLabel = new Label("<b>Payments:</b>");
		orderFinancePaymentLabel.setHeight(10);
		
		orderFinancePaymentListGrid = new ListGrid();
		
		orderFinancePaymentListGrid.setWidth100();
		orderFinancePaymentListGrid.setHeight100();
		orderFinancePaymentListGrid.setShowAllRecords(true);
		orderFinancePaymentListGrid.setSortField(0);

		orderFinancePaymentListGrid.setCanResizeFields(true);
		orderFinancePaymentListGrid.setShowRowNumbers(true);
		orderFinancePaymentListGrid.setAutoFetchData(true);
	
		orderFinancePaymentListGrid.setShowFilterEditor(true);	
		
		orderFinancePaymentListGrid.setDataSource(financePaymentData);
		orderFinancePaymentListGrid.setFields(Util.getListGridFieldsFromDataSource(financePaymentData));
		
		orderFinancePaymentListGrid.setSelectionType(SelectionStyle.SIMPLE);
		orderFinancePaymentListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID).setHidden(true);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID).setWidth(80);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_BANKSHORTNAME).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE).setHidden(true);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_REFERENCEID).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_REFERENCEID).setHidden(true);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_WCSORDERID).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OLDVENORDER_VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC).setWidth(150);		
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VIRTUALACCOUNTNUMBER).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT).setWidth(120);
		Util.formatListGridFieldAsCurrency(orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT));
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID).setWidth(100);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID).setHidden(true);
		orderFinancePaymentListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC).setWidth(180);
		
		VLayout orderFinancePaymentLayout = new VLayout();
		orderFinancePaymentLayout.setMembers(orderFinancePaymentLabel, orderFinancePaymentListGrid);
		
		Label orderFinanceReconciliationLabel = new Label("<b>Reconciliation:</b>");
		orderFinanceReconciliationLabel.setHeight(10);
		
		orderFinanceReconciliationListGrid = new ListGrid();
		
		orderFinanceReconciliationListGrid.setWidth100();
		orderFinanceReconciliationListGrid.setHeight100();
		orderFinanceReconciliationListGrid.setShowAllRecords(true);
		orderFinanceReconciliationListGrid.setSortField(0);

		orderFinanceReconciliationListGrid.setCanResizeFields(true);
		orderFinanceReconciliationListGrid.setShowRowNumbers(true);
	
		orderFinanceReconciliationListGrid.setShowFilterEditor(true);	
		
		orderFinanceReconciliationListGrid.setDataSource(financeReconciliationData);
		orderFinanceReconciliationListGrid.setFields(Util.getListGridFieldsFromDataSource(financeReconciliationData));
		
		orderFinanceReconciliationListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID).setWidth("10%");
		orderFinanceReconciliationListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setWidth("10%");
		orderFinanceReconciliationListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC).setWidth("10%");
		
		orderFinanceReconciliationListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				if (value != null) {
					String cellFormat = (String) value;
					cellFormat = cellFormat.substring(
							cellFormat.lastIndexOf("/") + 1,
							cellFormat.length());
					return "<a href='"
							+ GWT.getHostPageBaseURL()
							+ MainPagePresenter.fileDownloadPresenterServlet
							+ "?filename=" + value
							+ "' target='_blank'>" + cellFormat
							+ "</a>";
				}
				return (String) value;
			}
		});

		orderFinanceReconciliationListGrid.getField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARFUNDSINREPORT_FILENAMEANDLOCATION).setGroupTitleRenderer(new GroupTitleRenderer() {
			@Override
			public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName, ListGrid grid) {
				String groupTitle = (String) groupValue;

				if (!groupTitle.startsWith("/")) {
					return groupTitle;
				}

				groupTitle = groupTitle.substring(
						groupTitle.lastIndexOf("/") + 1,
						groupTitle.length());
				return "<a href='"
						+ GWT.getHostPageBaseURL()
						+ MainPagePresenter.fileDownloadPresenterServlet
						+ "?filename=" + groupValue
						+ "' target='_blank'>" + groupTitle + "</a>";
			}
		});
				
		VLayout orderFinanceReconciliationLayout = new VLayout();
		orderFinanceReconciliationLayout.setMembers(orderFinanceReconciliationLabel, orderFinanceReconciliationListGrid);
		
		orderFinanceLayout.setPadding(5);
		orderFinanceLayout.setMembersMargin(5);
		orderFinanceLayout.setMembers(orderFinancePaymentLayout, orderFinanceReconciliationLayout);
		
		orderFinanceVLayout.setMembers(orderFinanceLayout, paymentStatusButtons);
		
		setPane(orderFinanceVLayout);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		orderFinancePaymentListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (event.getSelection()!=null && event.getSelection().length == 1) {
					String paymentType = event.getSelection()[0].getAttributeAsString(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPECODE);
					String statusId = event.getSelection()[0].getAttributeAsString(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSID);
					
					if(paymentType.equals("VA")){
					
						if (statusId.equals(DataConstantNameTokens.VENORDERPAYMENT_PAYMENTSTATUSID_APPROVED.toString())) {
							buttonApproveVA.setDisabled(true);
						} else {
							buttonApproveVA.setDisabled(false);
						}
					
					}
					
					if(paymentType.equals("CS")){
						if (statusId.equals(DataConstantNameTokens.VENORDERPAYMENT_PAYMENTSTATUSID_APPROVED.toString()) 
								|| statusId.equals(DataConstantNameTokens.VENORDERPAYMENT_PAYMENTSTATUSID_REJECTED.toString())) {
							buttonApproveCS.setDisabled(true);
							buttonPendingCS.setDisabled(true);
							buttonRejectCS.setDisabled(true);
						} if (statusId.equals(DataConstantNameTokens.VENORDERPAYMENT_PAYMENTSTATUSID_PENDING.toString())) {
							buttonApproveCS.setDisabled(false);
							buttonPendingCS.setDisabled(true);
							buttonRejectCS.setDisabled(false);
						} if (statusId.equals(DataConstantNameTokens.VENORDERPAYMENT_PAYMENTSTATUSID_NOT_APPROVED.toString())) {
							buttonApproveCS.setDisabled(false);
							buttonPendingCS.setDisabled(false);
							buttonRejectCS.setDisabled(false);
						} 
					
					}
					
				} else {
					buttonApproveVA.setDisabled(true);
				}
			}
		});
		
		
		orderFinancePaymentListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFinancePaymentData();
			}
		});
		
		orderFinanceReconciliationListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFinanceReconciliationData();
			}
		});
		
	}
	
	public void refreshFinancePaymentData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderFinancePaymentListGrid.setData(response.getData());
			}
		};
		
		orderFinancePaymentListGrid.getDataSource().fetchData(orderFinancePaymentListGrid.getFilterEditorCriteria(), callBack);

	}
	
	public void refreshFinanceReconciliationData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderFinanceReconciliationListGrid.setData(response.getData());
			}
		};
		
		orderFinanceReconciliationListGrid.getDataSource().fetchData(orderFinanceReconciliationListGrid.getFilterEditorCriteria(), callBack);

	}
	
	public void loadFinanceReconciliationData(DataSource financeReconciliationData) {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderFinanceReconciliationListGrid.setData(response.getData());
			}
		};
		
		financeReconciliationData.fetchData(orderFinanceReconciliationListGrid.getFilterEditorCriteria(), callBack);

	}
	
	public ListGrid getOrderFinancePaymentListGrid() {
		return orderFinancePaymentListGrid;
	}
	

	/**
	 * @return the buttonApproveVA
	 */
	public IButton getButtonApproveVA() {
		return buttonApproveVA;
	}
	
	public IButton getButtonApproveCS() {
		return buttonApproveCS;
	}

	public IButton getButtonRejectCS() {
		return buttonRejectCS;
	}

	public IButton getButtonPendingCS() {
		return buttonPendingCS;
	}

	public String getSelectedPaymentId() {
		if (orderFinancePaymentListGrid.getSelection().length == 1) {
			return orderFinancePaymentListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID);
		} else {
			return null;
		}
	}
	
}
