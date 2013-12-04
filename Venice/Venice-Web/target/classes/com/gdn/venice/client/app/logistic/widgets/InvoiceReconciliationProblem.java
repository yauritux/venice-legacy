package com.gdn.venice.client.app.logistic.widgets;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class InvoiceReconciliationProblem extends VLayout {
	Window manualAdjWindow;
	
	String airwayBillId;
	final ToolStripButton veniceDataButton;
	final ToolStripButton providerDataButton;
	final ToolStripButton manualAdjustButton;
//	final ToolStripButton ignoreButton;
	
	Window commentHistoryWindow;
	
	public InvoiceReconciliationProblem(final String airwayBillId, DataSource dataSource, final String airwayBillApprovalStatus) {
		this.airwayBillId = airwayBillId;
	
		setPadding(5);
		setHeight(150);

		ToolStrip reconciliationToolStrip = new ToolStrip();
		reconciliationToolStrip.setWidth100();

		veniceDataButton = new ToolStripButton();
		veniceDataButton.setIcon("[SKIN]/icons/accept_venice.png");
		veniceDataButton.setTooltip("Apply Venice");

		providerDataButton = new ToolStripButton();
		providerDataButton.setIcon("[SKIN]/icons/accept_logistic.png");
		providerDataButton.setTooltip("Apply Logistic");
		
		manualAdjustButton = new ToolStripButton();
		manualAdjustButton.setIcon("[SKIN]/icons/edit.png");
		manualAdjustButton.setTooltip("Manual Adjust");
		
//		ignoreButton = new ToolStripButton();
//		ignoreButton.setIcon("[SKIN]/icons/ignore.png");
//		ignoreButton.setTooltip("Ignore Airwaybill");
		
		veniceDataButton.setDisabled(true);
		providerDataButton.setDisabled(true);
		manualAdjustButton.setDisabled(true);
//		ignoreButton.setDisabled(true);
		
		reconciliationToolStrip.addButton(veniceDataButton);
		reconciliationToolStrip.addButton(providerDataButton);
		reconciliationToolStrip.addButton(manualAdjustButton);
//		reconciliationToolStrip.addButton(ignoreButton);
		
		final ListGrid invoiceReconciliationProblemListGrid = new ListGrid();
		
		invoiceReconciliationProblemListGrid.setSelectionType(SelectionStyle.SIMPLE);
		invoiceReconciliationProblemListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		invoiceReconciliationProblemListGrid.setDataSource(dataSource);
		
		invoiceReconciliationProblemListGrid.setAutoFetchData(true);
		
		ListGridField[] fields = Util.getListGridFieldsFromDataSource(dataSource);
		
		invoiceReconciliationProblemListGrid.setFields(fields);
		
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID).setWidth("5%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID).setHidden(true);
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID).setWidth("5%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID).setHidden(true);
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTDESC).setWidth("10%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_VENICEDATA).setWidth("15%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_PROVIDERDATA).setWidth("15%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_MANUALLYENTEREDDATA).setWidth("15%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID).setWidth("5%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID).setHidden(true);
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC).setWidth("10%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_COMMENT).setWidth("35%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_COMMENT).setCanEdit(true);
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_USERLOGONNAME).setWidth("10%");
		invoiceReconciliationProblemListGrid.getField(DataNameTokens.LOGINVOICERECONRECORD_COMMENTHISTORY).setWidth(20);
				
		setMembers(reconciliationToolStrip, invoiceReconciliationProblemListGrid);
		
		invoiceReconciliationProblemListGrid.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				if (invoiceReconciliationProblemListGrid.getField(event.getColNum()).getName().equals(DataNameTokens.LOGINVOICERECONRECORD_COMMENTHISTORY)) {
					buildCommentHistoryWindow(event.getRecord().getAttributeAsString(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID)).show();
				}
				
			}
		});
		
		invoiceReconciliationProblemListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				//enabled if there is more than 1 record selected,
				//approval status is new or rejected (disabled during submitted or already approved)
				boolean enabled = invoiceReconciliationProblemListGrid.getSelection().length > 0 && 
					(airwayBillApprovalStatus.equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSDESC_NEW.toString()) ||
							airwayBillApprovalStatus.equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSDESC_REJECTED.toString()));
				
				veniceDataButton.setDisabled(!enabled);
				providerDataButton.setDisabled(!enabled);
//				ignoreButton.setDisabled(!enabled);
				
				//enabled if there is exactly 1 record selected,
				//approval status is new or rejected (disabled during submitted or already approved)
				boolean manualDataEnabled = invoiceReconciliationProblemListGrid.getSelection().length == 1 && enabled;
			
				
				manualAdjustButton.setDisabled(!manualDataEnabled);
				
			}
		});
		
		veniceDataButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = invoiceReconciliationProblemListGrid.getSelection();
				
				for (int i=0;i<selectedRecords.length;i++) {
					selectedRecords[i].setAttribute(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_VENICEDATAPPLIED);
					selectedRecords[i].setAttribute(DataNameTokens.LOGINVOICERECONRECORD_COMMENT, DataConstantNameTokens.INVOICE_LOGACTIONAPPLIED_ACTIONAPPLIEDID_VENICEDATAPPLIED_DESC);
					invoiceReconciliationProblemListGrid.updateData(selectedRecords[i]);
					veniceDataButton.setDisabled(true);
					providerDataButton.setDisabled(true);
//					ignoreButton.setDisabled(true);
					manualAdjustButton.setDisabled(true);
				}				
			}
		});
		
		providerDataButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = invoiceReconciliationProblemListGrid.getSelection();
				
				for (int i=0;i<selectedRecords.length;i++) {
					selectedRecords[i].setAttribute(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_PROVIDERDATAPPLIED);
					selectedRecords[i].setAttribute(DataNameTokens.LOGINVOICERECONRECORD_COMMENT, DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_PROVIDERDATAPPLIED_DESC);
					invoiceReconciliationProblemListGrid.updateData(selectedRecords[i]);
					veniceDataButton.setDisabled(true);
					providerDataButton.setDisabled(true);
//					ignoreButton.setDisabled(true);
					manualAdjustButton.setDisabled(true);
				}
			}
		});
		
		manualAdjustButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				buildManualAdjustmentWindow(airwayBillId, invoiceReconciliationProblemListGrid).show();
			}
		});
		
//		ignoreButton.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				ListGridRecord[] selectedRecords = invoiceReconciliationProblemListGrid.getSelection();
//				
//				for (int i=0;i<selectedRecords.length;i++) {
//					selectedRecords[i].setAttribute(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_IGNORED);
//					invoiceReconciliationProblemListGrid.updateData(selectedRecords[i]);
//					veniceDataButton.setDisabled(true);
//					providerDataButton.setDisabled(true);
//					ignoreButton.setDisabled(true);
//					manualAdjustButton.setDisabled(true);
//				}
//			}
//		});
	}
	
	private Window buildManualAdjustmentWindow(String airwayBillId, final ListGrid airwayBillReconciliationProblemListGrid) {
		manualAdjWindow = new Window();
		manualAdjWindow.setWidth(280);
		manualAdjWindow.setHeight(230);
		manualAdjWindow.setTitle("Manual Adjustment");
		manualAdjWindow.setShowMinimizeButton(false);
		manualAdjWindow.setIsModal(true);
		manualAdjWindow.setShowModalMask(true);
		manualAdjWindow.centerInPage();
		manualAdjWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				manualAdjWindow.destroy();
			}
		});
		
		VLayout manualAdjLayout = new VLayout();
		manualAdjLayout.setHeight100();
		manualAdjLayout.setWidth100();

		DynamicForm manualAdjForm = new DynamicForm();
		manualAdjForm.setPadding(5);
		
		final TextItem dataItem = new TextItem();
		dataItem.setTitle("Manual Data");
		
		final TextAreaItem commentItem = new TextAreaItem();
		commentItem.setTitle("Comment");
		
		StaticTextItem userItem = new StaticTextItem();
		userItem.setTitle("By");
		userItem.setValue(MainPagePresenter.signedInUser);
//		
//		StaticTextItem dateTimeItem = new StaticTextItem();
//		dateTimeItem.setTitle("TimeStamp");
//		dateTimeItem.setValue("01/01/2011 17:00");
//		
//		manualAdjForm.setItems(dataItem, commentItem, userItem, dateTimeItem);
		manualAdjForm.setItems(dataItem, commentItem);

		HLayout okCancelButtons = new HLayout(5);
		
		IButton buttonOK = new IButton("OK");
		IButton buttonCancel = new IButton("Cancel");
		
		buttonOK.addClickHandler(new ClickHandler() {
			
			@Override	
			public void onClick(ClickEvent event) {
				ListGridRecord selectedRecord = airwayBillReconciliationProblemListGrid.getSelectedRecord();
				selectedRecord.setAttribute(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_MANUALDATAPPLIED);
				selectedRecord.setAttribute(DataNameTokens.LOGINVOICERECONRECORD_MANUALLYENTEREDDATA, dataItem.getValueAsString());
				selectedRecord.setAttribute(DataNameTokens.LOGINVOICERECONRECORD_COMMENT, commentItem.getValueAsString());
				airwayBillReconciliationProblemListGrid.updateData(selectedRecord);
				veniceDataButton.setDisabled(true);
				providerDataButton.setDisabled(true);
//				ignoreButton.setDisabled(true);
				manualAdjustButton.setDisabled(true);
				manualAdjWindow.destroy();
			}
		});
		buttonCancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				manualAdjWindow.destroy();
			}
		});
		okCancelButtons.setAlign(Alignment.CENTER);
		okCancelButtons.setMembers(buttonOK, buttonCancel);
		
		manualAdjLayout.setMembers(manualAdjForm, okCancelButtons);
		manualAdjWindow.addItem(manualAdjLayout);
		return manualAdjWindow;
	}
	
	private Window buildCommentHistoryWindow(String invoiceReconRecordId) {
		commentHistoryWindow = new Window();
		commentHistoryWindow.setWidth(400);
		commentHistoryWindow.setHeight(150);
		commentHistoryWindow.setTitle("Comment History");
		commentHistoryWindow.setShowMinimizeButton(false);
		commentHistoryWindow.setIsModal(true);
		commentHistoryWindow.setShowModalMask(true);
		commentHistoryWindow.centerInPage();
		commentHistoryWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				commentHistoryWindow.destroy();
			}
		});
		
		VLayout commentHistoryLayout = new VLayout(5);
		commentHistoryLayout.setHeight100();
		commentHistoryLayout.setWidth100();

		ListGrid commentHistoryListGrid = new ListGrid();
		commentHistoryListGrid.setHeight100();
		commentHistoryListGrid.setWidth100();
		
		DataSource dataSource = LogisticsData.getInvoiceCommentHistoryData(invoiceReconRecordId);
		
		commentHistoryListGrid.setDataSource(dataSource);
		
		commentHistoryListGrid.setAutoFetchData(true);
		
		
		commentHistoryListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		commentHistoryListGrid.setSortField(0);
		commentHistoryListGrid.setSortDirection(SortDirection.DESCENDING);
		
		HLayout closeButtonLayout = new HLayout(0);
		closeButtonLayout.setHeight(30);
		
		IButton buttonClose = new IButton("Close");
		
		
		buttonClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				commentHistoryWindow.destroy();
			}
		});
		closeButtonLayout.setAlign(Alignment.CENTER);
		closeButtonLayout.setMembers(buttonClose);
		
		commentHistoryLayout.setMembers(commentHistoryListGrid, closeButtonLayout);
		commentHistoryWindow.addItem(commentHistoryLayout);
		return commentHistoryWindow;
	}

}
