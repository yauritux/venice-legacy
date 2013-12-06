package com.gdn.venice.client.app.finance.widgets;

import java.util.ArrayList;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A widget that builds a window for picking the AR Amount
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ArAmountManualJournalTransactionPickerWindowWidget extends Window{
	
	/**
	 * Constructor that builds the window
	 * 
	 * @param dataSource is the data source to use
	 * @param finApManualJournalTransctionIds has the AP manual journal ID's that are used to determine the AR amount
	 * @param paymentListGrid the list grid of payments
	 * @param paymentListGridRecord the selected record within the payments list grid
	 */
	public ArAmountManualJournalTransactionPickerWindowWidget(DataSource dataSource, final String finApManualJournalTransctionIds, final ListGrid paymentListGrid, final ListGridRecord paymentListGridRecord) {
		VLayout finApManualJournalTransactionLayout = new VLayout();
		finApManualJournalTransactionLayout.setHeight100();
		finApManualJournalTransactionLayout.setWidth100();
		finApManualJournalTransactionLayout.setPadding(5);
		
		
		setWidth(600);
		setHeight(180);
		setTitle("A/R Amount");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				ArAmountManualJournalTransactionPickerWindowWidget.this.destroy();
			}
		});
		
		/*
		 * A list grid that contains the AP manual journal transactions
		 */
		final ListGrid finApManualJournalTransactionListGrid = new ListGrid();
		
		finApManualJournalTransactionListGrid.setHeight(120);
		finApManualJournalTransactionListGrid.setSelectionType(SelectionStyle.SIMPLE);
		finApManualJournalTransactionListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		finApManualJournalTransactionListGrid.setDataSource(dataSource);
		finApManualJournalTransactionListGrid.setAutoFetchData(false);
		finApManualJournalTransactionListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		finApManualJournalTransactionListGrid.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID).setWidth("5%");
		finApManualJournalTransactionListGrid.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID).setHidden(true);
		finApManualJournalTransactionListGrid.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_TRANSACTIONAMOUNT).setWidth("20%");
		finApManualJournalTransactionListGrid.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID).setWidth("20%");
		finApManualJournalTransactionListGrid.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		finApManualJournalTransactionListGrid.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS).setWidth("40%");
		
		Util.formatListGridFieldAsCurrency(finApManualJournalTransactionListGrid.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_TRANSACTIONAMOUNT));
		
		HLayout buttonsLayout = new HLayout(5);
		
		IButton buttonOK = new IButton("OK");
		IButton buttonCancel = new IButton("Cancel");
	
		buttonOK.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = finApManualJournalTransactionListGrid.getSelection();
				
				/*
				 * Calculate the default penalty amount from the manual journal
				 * transactions in the list grid records
				 */
				double penaltyAmount = 0;
				String finApManualJournalTransactions = "";
				
				for (int i=0;i<selectedRecords.length;i++) {
					penaltyAmount += selectedRecords[i].getAttributeAsDouble(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_TRANSACTIONAMOUNT);
					finApManualJournalTransactions += selectedRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID);
					
					if (i<selectedRecords.length-1) {
						finApManualJournalTransactions += "#";
					}
				}
				paymentListGridRecord.setAttribute(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT,penaltyAmount);
				paymentListGridRecord.setAttribute(DataNameTokens.FINAPPAYMENT_FINAPMANUALJOURNALTRANSACTIONS,finApManualJournalTransactions);
				
				paymentListGrid.updateData(paymentListGridRecord);
				
				ArAmountManualJournalTransactionPickerWindowWidget.this.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				ArAmountManualJournalTransactionPickerWindowWidget.this.destroy();
			}
		});

		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setMembers(buttonOK, buttonCancel);
		
		finApManualJournalTransactionLayout.setMembers(finApManualJournalTransactionListGrid, buttonsLayout);
		
		addItem(finApManualJournalTransactionLayout);
		
		DSCallback callBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				finApManualJournalTransactionListGrid.setData(response.getData());
				
				String[] arrayFinApManualJournalTransctionIds = finApManualJournalTransctionIds.split("#");
				
				ArrayList<String> arrayListFinApManualJournalTransctionIds = new ArrayList<String>();
				for (int i=0;i<arrayFinApManualJournalTransctionIds.length;i++) {
					arrayListFinApManualJournalTransctionIds.add(arrayFinApManualJournalTransctionIds[i]);
				}
				
				ListGridRecord[] records = finApManualJournalTransactionListGrid.getRecords();
				ArrayList<ListGridRecord> selectedRecords = new ArrayList<ListGridRecord>();
				
				for (int i=0;i<records.length;i++) {
					if (arrayListFinApManualJournalTransctionIds.contains(records[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID))) {
						selectedRecords.add(records[i]);
					}
				}
				
				Record[] arraySelectedRecords = selectedRecords.toArray(new Record[selectedRecords.size()]);
				
				finApManualJournalTransactionListGrid.selectRecords(arraySelectedRecords);
			}
		};
		
		finApManualJournalTransactionListGrid.getDataSource().fetchData(finApManualJournalTransactionListGrid.getFilterEditorCriteria(), callBack);
	}
}
