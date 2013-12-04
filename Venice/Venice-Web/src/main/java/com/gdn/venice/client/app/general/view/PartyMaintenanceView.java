package com.gdn.venice.client.app.general.view;

import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.data.PartyMaintenanceData;
import com.gdn.venice.client.app.general.presenter.PartyMaintenancePresenter;
import com.gdn.venice.client.app.general.view.handlers.PartyMaintenanceUiHandlers;
import com.gdn.venice.client.app.general.widgets.PartyAddressTab;
import com.gdn.venice.client.app.general.widgets.PartyDetailVLayout;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Party Maintenance
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PartyMaintenanceView extends
		ViewWithUiHandlers<PartyMaintenanceUiHandlers> implements
		PartyMaintenancePresenter.MyView {
	
	private static final int LIST_HEIGHT = 150;	
	
	/*
	 * The RAF layout that is used for laying out the Party Maintenance 
	 */
	RafViewLayout partyMaintenanceLayout;
	
	/*
	 * A VLayout widget that is used to display the fields on the Party Maintenance
	 * screen
	 */
	VLayout partyMaintenanceVLayout;
	
	/*
	 * A VLayout widget that is used to display the fields on the Party Maintenance detail 
	 * screen 
	 */
	VLayout partyMaintenanceDetailVLayout;
	
	/*
	 * A VLayout that is used for the party details tab
	 */
	PartyDetailVLayout partyDetailVLayout;
	
	/*
	 * The toolstrip objects for the header and the detail
	 */
	ToolStrip partyMaintenanceToolStrip;
	ToolStrip partyMaintenanceDetailToolStrip;
	
	/*
	 * The add and remove buttons for the party maintenance header
	 */
	ToolStripButton addPartyButton;
	ToolStripButton removePartyButton;
	
	/*
	 * The list grid that is used to display the parties
	 */
	ListGrid partyListGrid;
	
	PartyAddressTab partyAddressTab;

	/**
	 * Constructor to build the view
	 */
	@Inject
	public PartyMaintenanceView() {
		partyMaintenanceLayout = new RafViewLayout();
		
		partyMaintenanceToolStrip = new ToolStrip();
		partyMaintenanceToolStrip.setWidth100();

		addPartyButton = new ToolStripButton();
		addPartyButton.setIcon("[SKIN]/icons/add.png");
		addPartyButton.setTooltip("Add Party");
		addPartyButton.setTitle("Add");

		removePartyButton = new ToolStripButton();
		removePartyButton.setIcon("[SKIN]/icons/delete.png");
		removePartyButton.setTooltip("Remove Party");
		removePartyButton.setTitle("Remove");


		partyMaintenanceToolStrip.addButton(addPartyButton);
		partyMaintenanceToolStrip.addButton(removePartyButton);
		
		partyMaintenanceVLayout = new VLayout();
		partyMaintenanceVLayout.setHeight(LIST_HEIGHT);
		partyMaintenanceVLayout.setShowResizeBar(true);
		
		partyMaintenanceDetailVLayout = new VLayout();
		partyMaintenanceDetailVLayout.setWidth100();
		
		HTMLFlow partyMaintenanceDetailFlow = new HTMLFlow();
		partyMaintenanceDetailFlow.setAlign(Alignment.CENTER);
		partyMaintenanceDetailFlow.setWidth100();
		partyMaintenanceDetailFlow.setContents("<h2 align=\"center\">Please select a party to show it details</h2>");
		partyMaintenanceDetailVLayout.setMembers(partyMaintenanceDetailFlow);

		partyMaintenanceLayout.setMembers(partyMaintenanceToolStrip, partyMaintenanceVLayout, partyMaintenanceDetailVLayout);
		removePartyButton.setDisabled(true);

		buildPartyListGrid();
		bindCustomUiHandlers();
	}
	
	/**
	 * Method to build the list grid for parties
	 * @return the list grid
	 */
	public ListGrid buildPartyListGrid() {
	
		partyListGrid = new ListGrid();
		
		partyListGrid.setWidth100();
		partyListGrid.setHeight100();
//		partyListGrid.setAutoFetchData(true);

		partyListGrid.setCanResizeFields(true);
		partyListGrid.setShowFilterEditor(true);
		partyListGrid.setShowAllRecords(true);
		partyListGrid.setSortField(0);
		partyListGrid.setSelectionType(SelectionStyle.SIMPLE);
		partyListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);	
		partyListGrid.setCanSort(true);
		partyListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		partyListGrid.setShowRowNumbers(true);
		
		//set click handler for Add Party Button
		addPartyButton.addClickHandler(new ClickHandler() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt
			 * .client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				// Simply start editing a new row in the list grid
				partyListGrid.startEditingNew();
				
			}
		});
		// set click handler for Remove Party button
		removePartyButton.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to remove this data?",
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									partyListGrid.removeSelectedData();
									refreshPartyMaintenanceData();
									SC.say("Data Removed");
								} else {

								}
							}
						});
			}
		});
		//Set Save-Edit handler for inline editing on partyListGrid
		partyListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.EditCompleteHandler#onEditComplete(com.smartgwt.client.widgets.grid.events.EditCompleteEvent)
			 */
			@Override
			public void onEditComplete(EditCompleteEvent event) {				
				partyListGrid.saveAllEdits();
				refreshPartyMaintenanceData();
				
			}
		});
		//Set Refresh on Filter Editor submit handler
		partyListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshPartyMaintenanceData();
			}
		});
		
		return partyListGrid;
	}
	
	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {	
		return partyMaintenanceLayout;
	}
	
	/**
	 * Loads the list of Party data (top table in the screen)
	 * 
	 * @param dataSource party maintenance datasource
	 */
	@Override
	public void loadPartyMaintenanceData(DataSource dataSource) {
	
		partyListGrid.setDataSource(dataSource);
		partyListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		partyListGrid.getField(DataNameTokens.VENPARTY_PARTYID).setHidden(true);
		partyListGrid.getField(DataNameTokens.VENPARTY_PARTYID).setWidth("5%");
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEID).setHidden(true);
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEID).setWidth("5%");
		
		/*
		 * Initialize the datasource for party type combo box
		 */
		DataSource partyTypeComboBoxDs = PartyMaintenanceData.getPartyTypeData();
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setWidth("10%");
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setOptionDataSource(partyTypeComboBoxDs);
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setDisplayField(DataNameTokens.VENPARTYTYPE_PARTYTYPEDESC);
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setValueField(DataNameTokens.VENPARTYTYPE_PARTYTYPEID);
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setCanEdit(true);
		partyListGrid.getField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setCanFilter(false);
		partyListGrid.getField(DataNameTokens.VENPARTY_FIRSTNAME).setWidth("10%");
		partyListGrid.getField(DataNameTokens.VENPARTY_FIRSTNAME).setCanEdit(true);
		partyListGrid.getField(DataNameTokens.VENPARTY_MIDDLENAME).setWidth("10%");
		partyListGrid.getField(DataNameTokens.VENPARTY_MIDDLENAME).setCanEdit(true);
		partyListGrid.getField(DataNameTokens.VENPARTY_LASTNAME).setWidth("10%");
		partyListGrid.getField(DataNameTokens.VENPARTY_LASTNAME).setCanEdit(true);
		partyListGrid.getField(DataNameTokens.VENPARTY_FULLORLEGALNAME).setWidth("20%");
		partyListGrid.getField(DataNameTokens.VENPARTY_FULLORLEGALNAME).setCanEdit(true);
		partyListGrid.getField(DataNameTokens.VENPARTY_POSITION).setWidth("10%");
		partyListGrid.getField(DataNameTokens.VENPARTY_POSITION).setCanEdit(true);
		
		partyMaintenanceVLayout.addMember(partyListGrid);		
	}
	
	
	/**
	 * Method to bind the custom UI handlers to the view
	 */
	public void bindCustomUiHandlers() {
		partyListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] partySelectedListGridRecords = event.getSelection();		
				/*
				 * Enable the remove button if there is a selected row
				 */
				if (partySelectedListGridRecords.length == 0) {
					removePartyButton.disable();
				} else {
					removePartyButton.enable();
				}
				
				/*
				 * Either display the list grid if a party is selected
				 * or display the HTMLFLow
				 */
				if (partySelectedListGridRecords.length == 1) {
					Record record = event.getRecord();
					showPartyDetail(record);
				} else {
					HTMLFlow partyContactDetailFlow = new HTMLFlow();
					partyContactDetailFlow.setAlign(Alignment.CENTER);
					partyContactDetailFlow.setWidth100();
					if (partySelectedListGridRecords.length == 0) {
						partyContactDetailFlow.setContents("<h2 align=\"center\">Please select a Party to show the contact detail</h2>");
					} else if (partySelectedListGridRecords.length > 1) {
						partyContactDetailFlow.setContents("<h2 align=\"center\">More than one Party selected, please select only one to show the contact detail</h2>");
					}
					partyMaintenanceDetailVLayout.setMembers(partyContactDetailFlow);
				}
			}
		});
	}

	
	/**
	 *Show the Party Contact & Address detail
	 * 
	 * @param record
	 * The selected record in the Party Maintenance ListGrid
	 */
	public void showPartyDetail(Record record){
		String partyId = record.getAttribute(DataNameTokens.VENPARTY_PARTYID);
		List<DataSource> dataSources = getUiHandlers().onShowPartyDetailData(partyId);
		
		partyDetailVLayout = new PartyDetailVLayout(
				 dataSources.get(0) //Party Contact Detail
				,dataSources.get(1), // Party Address detail
				this);
		
		partyMaintenanceDetailVLayout.setMembers(partyDetailVLayout);
	}
	

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.general.presenter.PartyMaintenancePresenter.MyView#refreshPartyMaintenanceData()
	 */
	@Override
	public void refreshPartyMaintenanceData() {
		DSCallback callBack = new DSCallback() {		
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				partyListGrid.setData(response.getData());
				partyListGrid.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
		partyListGrid.getDataSource().fetchData(partyListGrid.getFilterEditorCriteria(), callBack);
	}

	/**
	 * Returns the party list grid object so that other
	 * child items can get access to it
	 * 
	 * @return the partyListGrid
	 */
	public ListGrid getPartyListGrid() {
		return partyListGrid;
	}
	
	/**
	 * Returns the selected party record so that other child
	 * items can access it.
	 * @return the selected list grid record
	 */
	public ListGridRecord[] getSelectedParty() {
		return partyListGrid.getSelection();
	}
	
	/**
	 * @return the ProviderManagementUiHandlers
	 */
	public PartyMaintenanceUiHandlers getPartyMaintenanceUiHandlers() {
		return getUiHandlers();
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.general.presenter.PartyMaintenancePresenter.MyView#refreshAddressData()
	 */
	@Override
	public void refreshAddressData() {
		@SuppressWarnings("unused")
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				partyAddressTab.getAddressListGrid().setData(response.getData());
				partyAddressTab.getAddressListGrid().setGroupStartOpen(GroupStartOpen.ALL);
			}
		};		
	}
}