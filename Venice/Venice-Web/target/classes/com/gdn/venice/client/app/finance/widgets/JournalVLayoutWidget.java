package com.gdn.venice.client.app.finance.widgets;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.view.JournalView;
import com.gdn.venice.client.app.finance.view.ManualJournalView;
import com.gdn.venice.client.util.DateToXsdDatetimeFormatterClient;
import com.gdn.venice.client.util.Util;
import com.gwtplatform.mvp.client.View;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * A VLayout widget used in the journal screen and also in the
 * manual journal screen.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class JournalVLayoutWidget extends VLayout {
	private static final int LIST_HEIGHT = 140;
	private static final int JOURNAL_DETAIL_HEIGHT = 200;
	boolean bManualJournal = false;
	
	ListGrid journalList;
	ListGrid journalDetailList;
	VLayout journalListLayout;
	VLayout journalDetailLayout;
	VLayout journalDetailTopLayout;
	VLayout journalDetailBottomLayout;
	
	ListGrid manualJournalDetailList;
	ListGrid manualJournalSelectOrderList;
	ListGrid manualJournalSelectPartyList;
	
	View view;
	
	/**
	 * This class is used for both Journals and Manual Journal screens, when used for Manual Journal, bManualJournal has to be set to true.
	 * @param bManualJournal	set to true if this is used in Manual Journal
	 */
	public JournalVLayoutWidget(boolean bManualJournal, View view) {
		this.bManualJournal = bManualJournal;
		this.view = view;
		
		setWidth100();
		setHeight100();

		journalListLayout = new VLayout();
		journalListLayout.setHeight(LIST_HEIGHT);
		journalListLayout.setShowResizeBar(true);
		
		HTMLFlow journalDetailFlow = new HTMLFlow();
		journalDetailFlow.setAlign(Alignment.CENTER);
		journalDetailFlow.setWidth100();
		journalDetailFlow.setContents("<h2 align=\"center\">Please select a journal to show the journal detail</h2>");

		journalDetailLayout = new VLayout();
		journalDetailLayout.setWidth100();
		journalDetailLayout.setMembers(journalDetailFlow);
		
		journalDetailTopLayout = new VLayout();
		journalDetailTopLayout.setHeight(JOURNAL_DETAIL_HEIGHT);
		journalDetailTopLayout.setShowResizeBar(true);
		
		journalDetailBottomLayout = new VLayout();
		journalDetailBottomLayout.setWidth100();

		journalList = new ListGrid();

		journalList.setShowAllRecords(true);
		journalList.setSortField(0);
		
		journalList.setSelectionType(SelectionStyle.SIMPLE);
		journalList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		journalList.setShowRowNumbers(true);
		journalList.setShowFilterEditor(true);
		journalList.setAutoFetchData(false);
		journalList.setShowRowNumbers(true);
		journalList.setCanResizeFields(true);
		
		journalList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshJournalData();
			}
		});		
		setMembers(journalListLayout, journalDetailLayout);		
	}
	
	/**
	 * Refeshes the journal data
	 */
	public void refreshJournalData() {
		DSCallback callBack = new DSCallback() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				journalList.setData(response.getData());
				journalList.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};		
		journalList.getDataSource().fetchData(journalList.getFilterEditorCriteria(), callBack);		
	}
	
	/**
	 * Loads the list of Journal Approval Group data (top table in the screen)
	 * @param dataSource	Journal Approval Group data source
	 */
	public void loadJournalData(DataSource dataSource) {
		journalList.setDataSource(dataSource);
		journalList.setAutoFetchData(false);
		journalList.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID).setWidth("5%");
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID).setHidden(true);
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID).setWidth("5%");
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALID).setHidden(true);
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).setWidth("20%");
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP).setWidth("10%");
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC).setWidth("40%");
		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC).setWidth("10%");
//		journalList.getField(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		journalListLayout.addMember(journalList);
	}
	
	/**
	 * Binds the custom UI handlers to the view object
	 */
	public void bindCustomUiHandlers() {
		journalList.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = journalList.getSelection();
				
				/*
				 * If it is the journal view then enable/disable
				 * the submit for approval button appropriately
				 */
				if(view instanceof JournalView){
					((JournalView) view).getSubmitForApprovalButton().setDisabled(true);
					((JournalView) view).getExportButton().setDisabled(true);
					if(selectedRecords.length > 0){
						Boolean bDisabled = false;
						Boolean exportDisabled = false;
						
						if(selectedRecords.length != 1){
							exportDisabled = true;
						}
						
						for(int i=0; i< selectedRecords.length; ++i){
							
							if(!selectedRecords[i].getAttribute(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals("New")
									&& !selectedRecords[i].getAttribute(DataNameTokens.FINJOURNALAPPROVALGROUP_FINAPPROVALSTATUS_APPROVALSTATUSDESC).equals("Rejected")){
								bDisabled = true;
								break;
							}
						}
						((JournalView) view).getSubmitForApprovalButton().setDisabled(bDisabled);
						((JournalView) view).getExportButton().setDisabled(exportDisabled);
					}
				}

				/*
				 * If a single record is selected then show the details
				 * else show a HTMLFlow with a user message
				 */
				if (selectedRecords.length==1) {
					showJournalDetail(selectedRecords[0]);
				} else {
					HTMLFlow journalDetailFlow = new HTMLFlow();
					journalDetailFlow.setAlign(Alignment.CENTER);
					journalDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						journalDetailFlow.setContents("<h2 align=\"center\">Please select a journal to show the journal detail</h2>");
					} else if (selectedRecords.length>1) {
						journalDetailFlow.setContents("<h2 align=\"center\">More than one journal selected, please select only one journal to show the journal detail</h2>");
					}
					journalDetailLayout.setMembers(journalDetailFlow);					
				}				
			}
		});
	}
	
	/**
	 * Shows the journal detail in bottom layout based on journal type desc.
	 * @param record	The selected record in the Journal Approval Group ListGrid
	 * @param view		The View where this widget is attached to (i.e. JournalView or ManualJournalView)
	 */
	private void showJournalDetail(ListGridRecord record) {
		/*
		 * Instantiate the bottom layout widget builder
		 */
		JournalVLayoutBottomWidgetBuilder jvlbwb = new JournalVLayoutBottomWidgetBuilder();
		
		/*
		 * Journal Detail Records
		 * Use the widget builder to build the middle table 
		 * showing the journal transactions
		 */
		journalDetailList = jvlbwb.buildJournalDetailListGrid(record);
		journalDetailTopLayout.setMembers(journalDetailList);
		
		if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).equals("Cash Receive Journal")) {
			/*
			 * Cash Receive Journal Type
			 * Use the widget builder to build the bottom 
			 * layout for cash received/funds-in journal types.
			 */
			journalDetailBottomLayout.setMembers(jvlbwb.buildFundsInRecordListGrid(record));
			journalDetailTopLayout.setHeight("40%");
			journalDetailLayout.setMembers(journalDetailTopLayout, journalDetailBottomLayout);
		} else if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).equals("Sales Journal")) {			
			/*
			 * Sales Journal Type
			 * Use the widget builder to build the bottom
			 * layout for sales journal types.
			 */
			journalDetailBottomLayout.setMembers(jvlbwb.buildSalesRecordListGrid(record, null));
			journalDetailTopLayout.setHeight("40%");
			journalDetailLayout.setMembers(journalDetailTopLayout, journalDetailBottomLayout);
		} else if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).equals("Payment Journal")) {
			/*
			 * Payment Journal Type
			 * Use the widget builder to build the bottom
			 * layout for payment journal types
			 */
			journalDetailBottomLayout.setMembers(jvlbwb.buildPaymentListGrid(record));
			journalDetailTopLayout.setHeight("40%");
			journalDetailLayout.setMembers(journalDetailTopLayout, journalDetailBottomLayout);
		} else if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).equals("Logistics Debt Acknowledgement Journal")) {
			/*
			 * Logistics Debt Acknowledgement Journal Type
			 * Use the widget builder to build the bottom
			 * layout for the the LDA journal type (shows invoice)
			 */
			journalDetailBottomLayout.setMembers(jvlbwb.buildInvoiceListGrid(record));
			journalDetailTopLayout.setHeight("40%");
			journalDetailLayout.setMembers(journalDetailTopLayout, journalDetailBottomLayout);
		} else if (record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_FINJOURNAL_JOURNALDESC).equals("Manual Journal")) {
			//Only process Manual Journal if the view is of type ManualJournalView
			if (view instanceof ManualJournalView) {
				((ManualJournalView) view).getManualJournalUiHandlers().onManualJournalDetail(FinanceData.getManualJournalDetailData(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)), record);
			}
			journalDetailTopLayout.setHeight("40%");
			journalDetailLayout.setMembers(journalDetailTopLayout);
		} else {
			//Other journal types if they exist
			journalDetailTopLayout.setHeight("40%");
			journalDetailLayout.setMembers(journalDetailTopLayout);
		}
	}
	
	/**
	 * Creates or edits the manual journal detail (transaction) data (Manual Journal Transactions)
	 * @param dataSource	Data Source for Manual Journal Transaction
	 * @param record		Selected record in Journal Approval Group
	 */
	public void createOrEditManualJournalDetail(DataSource dataSource, Map<String, String> accountMap, ListGridRecord record) {
		ToolStrip detailManualJournalToolStrip1 = new ToolStrip();
		detailManualJournalToolStrip1.setWidth100();
		detailManualJournalToolStrip1.setPadding(2);

		ToolStripButton saveButton = new ToolStripButton();  
		saveButton.setIcon("[SKIN]/icons/save.png");  
		saveButton.setTooltip("Save Current Manual Journal Entry");
		saveButton.setTitle("Save");
		
		detailManualJournalToolStrip1.addButton(saveButton);

		DynamicForm manualJournalForm = new DynamicForm();
		manualJournalForm.setWidth100();

		final StaticTextItem journalId = new StaticTextItem();
		journalId.setTitle("Journal ID");
		
		final DateItem journalDate = new DateItem();
		journalDate.setTitle("Journal Date");
		
		final TextAreaItem description = new TextAreaItem();
		description.setTitle("Description");
		description.setHeight(50);
				
		if (record!=null) {
			journalId.setValue(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
			journalDate.setValue(record.getAttributeAsDate(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP));
			description.setValue(record.getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC));
		}

		manualJournalForm.setItems(journalId, journalDate, description);
		
		saveButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				
				HashMap<String, String> manualJournalDataMap = new HashMap<String, String>();
				
				DateToXsdDatetimeFormatterClient formatter = new DateToXsdDatetimeFormatterClient();
				
				manualJournalDataMap.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, (String) journalId.getValue());
				manualJournalDataMap.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPTIMESTAMP, formatter.format((Date) journalDate.getValue()));
				manualJournalDataMap.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPDESC, (String) description.getValue());
							
				ListGridRecord[] manualJournalDetailRecords = manualJournalDetailList.getRecords();
				
				HashMap<String, String> manualJournalTransactionMap = new HashMap<String, String>();
				HashMap<String, String> manualJournalTransactionDataMap = new HashMap<String, String>();
				
				for (int i=0;i<manualJournalDetailRecords.length;i++) {
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC));
					manualJournalTransactionMap.put(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS, 
							manualJournalDetailRecords[i].getAttributeAsString(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS));
					manualJournalTransactionDataMap.put("MANUALJOURNALTRANSACTION" + i, Util.formXMLfromHashMap(manualJournalTransactionMap));
				}
				
				manualJournalDataMap.put("MANUALJOURNALTRANSACTION", Util.formXMLfromHashMap(manualJournalTransactionDataMap));								
				((ManualJournalView) view).getManualJournalUiHandlers().onSaveManualJournalClicked(manualJournalDataMap);
			}
		});

		ToolStrip detailManualJournalToolStrip2 = new ToolStrip();
		detailManualJournalToolStrip2.setWidth100();
		detailManualJournalToolStrip2.setPadding(2);

		ToolStripButton newButton = new ToolStripButton();  
		newButton.setIcon("[SKIN]/icons/add.png");  
		newButton.setTooltip("Add New Manual Journal Entry");
		newButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/delete.png");  
		removeButton.setTooltip("Delete Current Manual Journal Entry");
		removeButton.setTitle("Remove");

		detailManualJournalToolStrip2.addButton(newButton);
		detailManualJournalToolStrip2.addButton(removeButton);
				
		manualJournalDetailList = new ListGrid() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent(com.smartgwt.client.widgets.grid.ListGridRecord, java.lang.Integer)
			 */
			@Override  
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {  
				final String fieldName = this.getFieldName(colNum);  
				if (fieldName.equals(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID) || fieldName.equals(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME)) {  
					HLayout layout = new HLayout();
					layout.setWidth100();
					layout.setHeight(18);
					
					Label label  = new Label();
					label.setHeight(18);
					label.setWidth100();
					
					IButton button = new IButton();  
					button.setHeight(18);
					button.setWidth(18);
					button.setIcon("[SKIN]/icons/search.png");  
					
					button.addClickHandler(new ClickHandler() {						
						/* (non-Javadoc)
						 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
						 */
						@Override
						public void onClick(ClickEvent event) { 
							if (fieldName.equals(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID)) {
								buildManualJournalSelectOrderWindow(record).show();
							} else if (fieldName.equals(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME)){
								buildManualJournalSelectPartyWindow(record).show();
							}							
						}
					});
					
					layout.setMembers(label, button);
					return layout; 					
				} else {
					return null;
				}
			}
		};
		
		manualJournalDetailList.setShowAllRecords(true);
		manualJournalDetailList.setSortField(0);
		manualJournalDetailList.setCanResizeFields(true);
		
		//Can not use Data Source or AutoFetchData here otherwise can not add new rows to Manual Journal
		//Workaround is to use setData by end of this function.
//		manualJournalDetailList.setDataSource(dataSource);
//		manualJournalDetailList.setAutoFetchData(true);
		
		manualJournalDetailList.setSaveLocally(true);
		manualJournalDetailList.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		manualJournalDetailList.setHeaderSpans(
				new HeaderSpan("Journal Details", new String[]{
						DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC, 
						DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID,
						DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID,
						DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID,
						DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME
						}),
				new HeaderSpan("Amount", new String[]{
						DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT, 
						DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT
						}));
		
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID).setWidth("5%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID).setTitle("Id");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_MANUALJOURNALTRANSACTIONID).setHidden(true);
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC).setWidth("20%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC).setTitle("Account No. and Desc.");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC).setValueMap(accountMap);
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID).setWidth("5%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID).setTitle("Order Id");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID).setHidden(true);
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID).setWidth("20%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID).setTitle("Order Id");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID).setWidth("5%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID).setTitle("Party Id");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID).setHidden(true);
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME).setWidth("20%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME).setTitle("Party");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT).setWidth("20%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT).setTitle("Debit");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT).setWidth("20%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT).setTitle("Credit");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC).setWidth("20%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINTRANSACTIONSTATUS_TRANSACTIONSTATUSDESC).setTitle("Status");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS).setWidth("20%");
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS).setTitle("Comments");
			
		Util.formatListGridFieldAsCurrency(manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT));
		Util.formatListGridFieldAsCurrency(manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT));
		
		manualJournalDetailList.setShowRecordComponents(true);          
		manualJournalDetailList.setShowRecordComponentsByCell(true);  
		manualJournalDetailList.setHeaderHeight(40);
		manualJournalDetailList.setSelectionType(SelectionStyle.SIMPLE);  
		manualJournalDetailList.setSelectionAppearance(SelectionAppearance.CHECKBOX);

		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_DEBITTRANSACTIONMOUNT).setCanEdit(true);
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_CREDITTRANSACTIONMOUNT).setCanEdit(true);
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_FINACCOUNT_ACCOUNTNUMBER_AND_ACCOUNTDESC).setCanEdit(true);
		manualJournalDetailList.getField(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_FINJOURNALTRANSACTION_COMMENTS).setCanEdit(true);

		newButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				manualJournalDetailList.startEditingNew();
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				manualJournalDetailList.removeSelectedData();
			}

		});
		
		DSCallback manualJournalEntryDsCallBack = new DSCallback() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				manualJournalDetailList.setData(response.getData());
			}
		};
		
		if (record!=null) {
			dataSource.fetchData(manualJournalDetailList.getFilterEditorCriteria(), manualJournalEntryDsCallBack);
		}

		journalDetailLayout.setMembers(detailManualJournalToolStrip1, manualJournalForm, detailManualJournalToolStrip2, manualJournalDetailList);
	}
		
	/**
	 * Build the order selection window
	 * @param record is the list grid record to use to populate the data
	 * @return the new order selection window
	 */
	private Window buildManualJournalSelectOrderWindow(final ListGridRecord record) {
		final Window selectOrderWindow = new Window();
		selectOrderWindow.setWidth(500);
		selectOrderWindow.setHeight(350);
		selectOrderWindow.setTitle("Select Order");
		selectOrderWindow.setShowMinimizeButton(false);
		selectOrderWindow.setIsModal(true);
		selectOrderWindow.setShowModalMask(true);
		selectOrderWindow.centerInPage();
		selectOrderWindow.addCloseClickHandler(new CloseClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
			 */
			public void onCloseClick(CloseClientEvent event) {
				selectOrderWindow.destroy();
			}
		});

		VLayout manualJournalSelectOrderLayout = new VLayout();
		manualJournalSelectOrderLayout.setHeight100();
		manualJournalSelectOrderLayout.setWidth100();

		manualJournalSelectOrderList = new ListGrid();
		manualJournalSelectOrderList.setHeight(300);
		manualJournalSelectOrderList.setSelectionType(SelectionStyle.SIMPLE);
		manualJournalSelectOrderList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		manualJournalSelectOrderList.setShowFilterEditor(true);
		manualJournalSelectOrderList.setAutoFetchData(false);

		manualJournalSelectOrderList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshManualJournalSelectOrderData();
			}
		});

		DataSource manualJournalSelectOrderDataSource = FinanceData.getManualJournalOrderData();
		manualJournalSelectOrderList.setDataSource(manualJournalSelectOrderDataSource);
		manualJournalSelectOrderList.setFields(Util.getListGridFieldsFromDataSource(manualJournalSelectOrderDataSource));
		manualJournalSelectOrderList.getField(DataNameTokens.VENORDER_ORDERID).setHidden(true);		
		
		HLayout manualJournalSelectOrderButtons = new HLayout(5);

		final IButton buttonSelectOrder = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");

		buttonSelectOrder.setDisabled(true);
		buttonSelectOrder.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String orderId = manualJournalSelectOrderList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENORDER_ORDERID);
				String wCSOrderId = manualJournalSelectOrderList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENORDER_WCSORDERID);
				
				record.setAttribute(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_ORDERID, orderId);
				record.setAttribute(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENORDER_WCSORDERID, wCSOrderId);
				
				manualJournalDetailList.redraw();
				selectOrderWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				selectOrderWindow.destroy();
			}
		});
		
		manualJournalSelectOrderButtons.setAlign(Alignment.CENTER);
		manualJournalSelectOrderButtons.setMembers(buttonSelectOrder, buttonCancel);
		
		manualJournalSelectOrderLayout.setMembers(manualJournalSelectOrderList, manualJournalSelectOrderButtons);
		selectOrderWindow.addItem(manualJournalSelectOrderLayout);

		manualJournalSelectOrderList.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectOrder.setDisabled(manualJournalSelectOrderList.getSelection().length!=1);
			}
		});	
		return selectOrderWindow;
	}
	
	/**
	 * Builds the party selection window for the manual journal screen
	 * @param record is the record to use when building the data query
	 * @return the new window
	 */
	private Window buildManualJournalSelectPartyWindow(final ListGridRecord record) {
		final Window selectPartyWindow = new Window();
		selectPartyWindow.setWidth(500);
		selectPartyWindow.setHeight(350);
		selectPartyWindow.setTitle("Select Party");
		selectPartyWindow.setShowMinimizeButton(false);
		selectPartyWindow.setIsModal(true);
		selectPartyWindow.setShowModalMask(true);
		selectPartyWindow.centerInPage();
		selectPartyWindow.addCloseClickHandler(new CloseClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
			 */
			public void onCloseClick(CloseClientEvent event) {
				selectPartyWindow.destroy();
			}
		});

		VLayout manualJournalSelectPartyLayout = new VLayout();
		manualJournalSelectPartyLayout.setHeight100();
		manualJournalSelectPartyLayout.setWidth100();

		manualJournalSelectPartyList = new ListGrid();
		manualJournalSelectPartyList.setHeight(300);
		manualJournalSelectPartyList.setSelectionType(SelectionStyle.SIMPLE);
		manualJournalSelectPartyList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		manualJournalSelectPartyList.setShowFilterEditor(true);
		manualJournalSelectPartyList.setAutoFetchData(false);

		manualJournalSelectPartyList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshManualJournalSelectPartyData();
			}
		});

		DataSource manualJournalSelectPartyDataSource = FinanceData.getManualJournalPartyData();
		manualJournalSelectPartyList.setDataSource(manualJournalSelectPartyDataSource);
		manualJournalSelectPartyList.setFields(Util.getListGridFieldsFromDataSource(manualJournalSelectPartyDataSource));
		manualJournalSelectPartyList.getField(DataNameTokens.VENPARTY_PARTYID).setHidden(true);
		
		HLayout manualJournalSelectPartyButtons = new HLayout(5);

		final IButton buttonSelectParty = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");

		buttonSelectParty.setDisabled(true);
		buttonSelectParty.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String partyId = manualJournalSelectPartyList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID);
				String partyName = manualJournalSelectPartyList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_FULLORLEGALNAME);
				
				record.setAttribute(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_PARTYID, partyId);
				record.setAttribute(DataNameTokens.FINAPMANUALJOURNALTRANSACTION_VENPARTY_FULLORLEGALNAME, partyName);
				
				manualJournalDetailList.redraw();
				selectPartyWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				selectPartyWindow.destroy();
			}
		});
		manualJournalSelectPartyButtons.setAlign(Alignment.CENTER);
		manualJournalSelectPartyButtons.setMembers(buttonSelectParty, buttonCancel);
		
		manualJournalSelectPartyLayout.setMembers(manualJournalSelectPartyList, manualJournalSelectPartyButtons);
		selectPartyWindow.addItem(manualJournalSelectPartyLayout);

		manualJournalSelectPartyList.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectParty.setDisabled(manualJournalSelectPartyList.getSelection().length!=1);
			}
		});		
		return selectPartyWindow;
	}
	
	/**
	 * Refreshes the selected order data for the manual journal screen
	 */
	private void refreshManualJournalSelectOrderData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				manualJournalSelectOrderList.setData(response.getData());
			}
		};
		manualJournalSelectOrderList.getDataSource().fetchData(manualJournalSelectOrderList.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Refreshes the selected party data for the manual journal screen
	 */
	private void refreshManualJournalSelectPartyData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				manualJournalSelectPartyList.setData(response.getData());
			}
		};
		manualJournalSelectPartyList.getDataSource().fetchData(manualJournalSelectPartyList.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Returns the journals that have been selected in the list grid
	 * @return the journal list grid records
	 */
	public ListGridRecord[] getSelectedJournals() {
		return journalList.getSelection();
	}

	/**
	 * @return the journalList
	 */
	public ListGrid getJournalList() {
		return journalList;
	}
}
