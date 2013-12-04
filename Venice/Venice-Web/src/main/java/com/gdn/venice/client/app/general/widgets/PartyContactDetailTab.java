package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.data.PartyMaintenanceData;
import com.gdn.venice.client.app.general.view.PartyMaintenanceView;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Tab for Party contact detail
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PartyContactDetailTab extends Tab{
	
	/*
	 * The tool strip for Party Contact Detail tab
	 */	
	ToolStrip contactDetailToolStrip;
	
	/*
	 * The new and remove buttons for the contact details
	 */
	ToolStripButton addContactDetailButton;
	ToolStripButton removeContactDetailButton;
	
	/*
	 * The Party maintenance view object
	 */	
	PartyMaintenanceView partyView = null;
	/*
	 * The list grid that displays the contact details
	 */	
	ListGrid partyContactDetailListGrid;
	
	/**
	 * Constructor to build the tab
	 * @param title is the title of the tab
	 * @param contactDetailData the data source to be used
	 * @param partyView the view object
	 */

	public PartyContactDetailTab(String title, DataSource contactDetailData, final PartyMaintenanceView partyView) {
		super(title);
		this.partyView = partyView;
		contactDetailToolStrip = new ToolStrip();
		contactDetailToolStrip.setWidth("60%");
		contactDetailToolStrip.setPadding(2);

		addContactDetailButton = new ToolStripButton();
		addContactDetailButton.setIcon("[SKIN]/icons/add.png");
		addContactDetailButton.setTooltip("Add Contact Detail");
		addContactDetailButton.setTitle("Add");

		removeContactDetailButton = new ToolStripButton();
		removeContactDetailButton.setIcon("[SKIN]/icons/delete.png");
		removeContactDetailButton.setTooltip("Remove Contact Detail");
		removeContactDetailButton.setTitle("Remove");
		
		contactDetailToolStrip.addButton(addContactDetailButton);
		contactDetailToolStrip.addButton(removeContactDetailButton);
		
		partyContactDetailListGrid = new ListGrid();
		
		partyContactDetailListGrid.setWidth("60%");
		partyContactDetailListGrid.setHeight100();
		partyContactDetailListGrid.setShowAllRecords(true);
		partyContactDetailListGrid.setSortField(0);
		partyContactDetailListGrid.setAutoFetchData(true);
		partyContactDetailListGrid.setCanResizeFields(true);
	
		partyContactDetailListGrid.setShowFilterEditor(true);
		partyContactDetailListGrid.setAutoFitData(Autofit.VERTICAL);
		partyContactDetailListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		partyContactDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		partyContactDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		partyContactDetailListGrid.setShowRowNumbers(true);
		
		// Set Contact Detail Datasource 
		partyContactDetailListGrid.setDataSource(contactDetailData);
		partyContactDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(contactDetailData));

		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setWidth("5%");
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setHidden(true);
		// Set Contact Detail Type Datasource from PartyMaintenanceData
		DataSource contactDetailTypeComboBoxDs = PartyMaintenanceData.getContactDetailTypeData();
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setWidth("7%");
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setOptionDataSource(contactDetailTypeComboBoxDs);
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setDisplayField(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC);
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setValueField(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID);
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setCanEdit(true);
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setCanFilter(false);

		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setWidth("33%");
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setCanEdit(true);
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setValidateOnChange(true);
		
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID).setWidth("5%");
		partyContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID).setHidden(false);		
		
		//Set click handler for Add Contact Detail Button
		addContactDetailButton.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				partyContactDetailListGrid.startEditingNew();	
				Long partyId = new Long(partyView.getPartyListGrid().getSelectedRecord().getAttribute(DataNameTokens.VENPARTY_PARTYID));			
				partyContactDetailListGrid.setEditValue(partyContactDetailListGrid.getEditRow(), partyContactDetailListGrid.getFieldNum(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID), partyId.toString());				
			}			
		});
		
		//Set click handler for Remove Contact Detail button handler
		removeContactDetailButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {				
				if(partyContactDetailListGrid.getSelection().length > 0){
				SC.ask("Are you sure you want to remove this data?",
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									partyContactDetailListGrid
											.removeSelectedData();
									partyContactDetailListGrid
											.clearCriteria();
									SC.say("Data Removed");
								} else {

								}
							}
						});
				}
			}
		});
		
		partyContactDetailListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshContactDetailData();
			}
		});
		
		partyContactDetailListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.EditCompleteHandler#onEditComplete(com.smartgwt.client.widgets.grid.events.EditCompleteEvent)
			 */
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				partyContactDetailListGrid.saveAllEdits();
				partyContactDetailListGrid.clearCriteria();
				refreshContactDetailData();
			}
		});
		
		VLayout partyContactDetailLayout = new VLayout();
		partyContactDetailLayout.setMembers(contactDetailToolStrip, partyContactDetailListGrid);
		setPane(partyContactDetailLayout);
	}
	
	/**
	 * Refresh Contact detail List
	 */
	private void refreshContactDetailData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				partyContactDetailListGrid.setData(response.getData());
				partyContactDetailListGrid.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};		
		partyContactDetailListGrid.getDataSource().fetchData(partyContactDetailListGrid.getFilterEditorCriteria(), callBack);
	}
}