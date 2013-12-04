package com.gdn.venice.client.app.finance.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.ExportData;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.View;
import com.smartgwt.client.data.Criterion;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Hilite;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.SummaryFunction;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * ExportVLayoutWidget - a widget class for displaying Exports
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ExportVLayoutWidget extends VLayout {
	private static final int LIST_HEIGHT = 140;
	private static final int ACCOUNT_LINES_HEIGHT = 300;
	
	/*
	 * Set Red Color to  the Value Field cell(bottom table) if it's value less than 0
	 */
	private static Hilite[] hilites = new Hilite[] {  
        new Hilite() {{  
            setFieldNames((DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET));  
            setCriteria(new Criterion((DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET), OperatorId.LESS_THAN, 0));  
            setTextColor("#FF00FF");  
            setCssText("color:#FF00FF");  
            setId("0");  
            setFieldNames((DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT));  
            setCriteria(new Criterion((DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT), OperatorId.LESS_THAN, 0));  
            setTextColor("#FF0000");  
            setCssText("color:#FF0000");  
            setId("0");  
        }} 
 
	};
	
	/*
	 * The main list grid for the Journal voucher
	 */
	ListGrid accountLinesListGrid;

	/*
	 * The vertical layout used to lay out the top journal voucher data list
	 */
	VLayout exportVLayout;

	/*
	 * A VLayout widget that is used to display the fields on the Account Lines
	 * screen (bottom table)
	 */
	VLayout exportAccountLinesVLayout;

	/*
	 * The list grid that displays the account lines (Rolled Up journal entry)
	 */
	ListGrid exportJournalVoucherListGrid;
	
	/*
	 * The Data Source for Account Lines Detail
	 */
	DataSource accountDataSource = null;	

	/*
	 * The Export view object
	 */
	View view;
	
	/**
	 * Constructor that takes the view and instantiates 
	 * it for reference as well as builds the UI
	 * @param view the view object
	 */
	public ExportVLayoutWidget(View view) {
		this.view = view;

		setWidth100();
		setHeight100();
	    
		exportVLayout = new VLayout();
		exportVLayout.setHeight(LIST_HEIGHT);
		exportVLayout.setShowResizeBar(true);

		HTMLFlow accountLinesDetailFlow = new HTMLFlow();
		accountLinesDetailFlow.setAlign(Alignment.CENTER);
		accountLinesDetailFlow.setWidth100();
		accountLinesDetailFlow.setContents("<h2 align=\"center\">Please select a Journal to show the Account  detail</h2>");

		exportAccountLinesVLayout = new VLayout();
		exportAccountLinesVLayout.setWidth100();
		exportAccountLinesVLayout.setHeight(ACCOUNT_LINES_HEIGHT);
		exportAccountLinesVLayout.setMembers(accountLinesDetailFlow);
 
		exportJournalVoucherListGrid = new ListGrid();

		exportJournalVoucherListGrid.setShowAllRecords(true);
		exportJournalVoucherListGrid.setSortField(0);

		exportJournalVoucherListGrid.setSelectionType(SelectionStyle.SIMPLE);
		exportJournalVoucherListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		exportJournalVoucherListGrid.setShowFilterEditor(true);
		exportJournalVoucherListGrid.setAutoFetchData(true);
		exportJournalVoucherListGrid.setCanResizeFields(true);
		exportJournalVoucherListGrid.setShowRowNumbers(true);
		exportJournalVoucherListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshJournalVoucherData();
			}

		});
		setMembers( exportVLayout, exportAccountLinesVLayout);
	}
	
	/**
	 * Refreshes the journal voucher data(RolledUp Journal Header) from the data source
	 */
	public void refreshJournalVoucherData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				exportJournalVoucherListGrid.setData(response.getData());
				exportJournalVoucherListGrid.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};

		exportJournalVoucherListGrid.getDataSource().fetchData(exportJournalVoucherListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Load the list of Journal Voucher data (top table in the screen)
	 * 
	 * @param dataSource
	 *            journal voucher  datasource
	 */
	public void loadJornalVoucherData(DataSource dataSource) {
		exportJournalVoucherListGrid.setDataSource(dataSource);
		exportJournalVoucherListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		exportJournalVoucherListGrid.setCanResizeFields(true);
		exportJournalVoucherListGrid.setShowFilterEditor(true);
		exportJournalVoucherListGrid.setAutoFetchData(true);
		
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID).setHidden(true);
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID).setWidth("2%");
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALDESC).setHidden(false);
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALDESC).setWidth("10%");	
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALFILENAMEANDPATH).setWidth("15%");

		//Set "Filename" Field cell format (Rolled Up Journal Header)
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALFILENAMEANDPATH).setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum,
					int colNum) {
				String cellFormat = (String) value;
				cellFormat = cellFormat.substring(cellFormat.lastIndexOf("/")+1, cellFormat.length());
				return "<a href='" + GWT.getHostPageBaseURL() +
					MainPagePresenter.fileDownloadPresenterServlet +
					"?filename=" + value + "' target='_blank'>" + 
					cellFormat + "</a>";
			}
		});
		
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALFILENAMEANDPATH).setGroupTitleRenderer(new GroupTitleRenderer() {		
			@Override
			public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName, ListGrid grid) {
				String groupTitle = (String) groupValue;
				
				if (!groupTitle.startsWith("/")) {
					return groupTitle;
				}
				
				groupTitle = groupTitle.substring(groupTitle.lastIndexOf("/")+1, groupTitle.length());
				return "<a href='" + GWT.getHostPageBaseURL() +
				MainPagePresenter.fileDownloadPresenterServlet +
				"?filename=" + groupValue + "' target='_blank'>" + 
				groupTitle + "</a>";
			}
		});
		
		
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC).setWidth("10%");
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC).setAlign(Alignment.RIGHT);
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC).setCellAlign(Alignment.RIGHT);	
		exportJournalVoucherListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC).setValueField(DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEID);

		exportVLayout.addMember(exportJournalVoucherListGrid);
	}
	
	/**
	 * Uihandlers if Selection on journal voucher List (Top Table) changed
	 */
	public void bindCustomUiHandlers() {		
		exportJournalVoucherListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = exportJournalVoucherListGrid.getSelection();		

				if (selectedRecords.length == 1) {
					System.out.println("Row selected... RolledUp Journal iD:" + selectedRecords[0].getAttributeAsString(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID));

					showAccountLines(selectedRecords[0]);
				} else {
					HTMLFlow accountLinesDetailFlow = new HTMLFlow();
					accountLinesDetailFlow.setAlign(Alignment.CENTER);
					accountLinesDetailFlow.setWidth100();
					if (selectedRecords.length == 0) {
						accountLinesDetailFlow.setContents("<h2 align=\"center\">Please select a Journal to show the Account Lines</h2>");
					} else if (selectedRecords.length > 1) {
						accountLinesDetailFlow.setContents("<h2 align=\"center\">More than one Journal selected, please select only one to show the Account Lines</h2>");
					}
					exportAccountLinesVLayout.setMembers(accountLinesDetailFlow);
				}
			}
		});
	}
	
	/**
	 * Shows the account lines
	 * 
	 * @param record
	 *            The selected record in the Journal Voucher ListGrid
	 */
	public void showAccountLines(ListGridRecord record) {
		System.out.println("showAccountLines... RolledUp Journal iD:" + record.getAttribute(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID));
		buildAccountLines(record);
	}

	/**
	 * Loads the Account Lines data
	 * 
	 * @param record
	 *            Selected record in Journal Voucher data
	 */
	public void buildAccountLines(final ListGridRecord record) {
		accountLinesListGrid = new ListGrid();		
		
		// Set account lines Datasource		
	
		accountDataSource = ExportData.getAccountLinesData (record.getAttributeAsString(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID));
		accountLinesListGrid.setDataSource(accountDataSource);
		accountLinesListGrid.setFields(Util.getListGridFieldsFromDataSource(accountDataSource));
		accountLinesListGrid.setShowAllRecords(true);
		accountLinesListGrid.setGroupByField(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID);
		accountLinesListGrid.setShowFilterEditor(true);	
		accountLinesListGrid.setSortField(0);			
		accountLinesListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		accountLinesListGrid.setCanResizeFields(true);
		accountLinesListGrid.setHilites(hilites);
		accountLinesListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(
					FilterEditorSubmitEvent event) {
				refreshAccountLinesData();
			}
		});
	
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID).setHidden(true);
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID).setWidth("10%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID).setWidth("10%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID).setAlign(Alignment.LEFT);
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID).setCellAlign(Alignment.LEFT);	
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC).setWidth("30%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC).setValueField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID);
		//accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE).setWidth("4%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET).setWidth("20%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT).setWidth("20%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET).setCanFilter(false);
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT).setCanFilter(false);
		
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID).setHidden(true);
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP).setWidth("20%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP).setHidden(true);
		
		Util.formatListGridFieldAsCurrency(accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET));
		Util.formatListGridFieldAsCurrency(accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT));		
		
		accountLinesListGrid.setGroupStartOpen(GroupStartOpen.FIRST);
		accountLinesListGrid.setShowGroupSummary(true);
		accountLinesListGrid.setShowGridSummary(true);
		
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC).setAlign(Alignment.RIGHT);
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC).setWidth("10%");
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC).setValueField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSID);
		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC).setCellAlign(Alignment.RIGHT);

		accountLinesListGrid.getField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC).setSummaryFunction(new SummaryFunction(){

			@Override
			public Object getSummaryValue(Record[] records, ListGridField field) {
				// TODO Auto-generated method stub
				return "Total : ";
			}
			
		});
		
		
		
		DSCallback accountLinesDsCallBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				accountLinesListGrid.setData(response.getData());
			}
		};
		
		if (record!=null) {
			accountLinesListGrid.getDataSource().fetchData(accountLinesListGrid.getFilterEditorCriteria(), accountLinesDsCallBack);
		}

		exportAccountLinesVLayout.setMembers( accountLinesListGrid);
		}

	/**
	 * Refresh account lines
	 */
	private void refreshAccountLinesData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				accountLinesListGrid.setData(response.getData());
				accountLinesListGrid.setGroupStartOpen(GroupStartOpen.FIRST);
			}
		};		
		accountLinesListGrid.getDataSource().fetchData(accountLinesListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * @return the exportJournalVoucherListGrid
	 */
	
	public ListGrid getExportJournalVoucherListGrid() {
		return exportJournalVoucherListGrid;
	}
	
	/**
	 * @return the accountLinesListGrid
	 */
	public ListGrid getAccountLinesListGrid() {
		return accountLinesListGrid;
	}
	
	public ListGridRecord[] getSelectedRecord(){
		return accountLinesListGrid.getRecords();
	}
}