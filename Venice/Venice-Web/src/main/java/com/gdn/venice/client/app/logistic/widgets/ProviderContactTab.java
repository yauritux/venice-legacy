package com.gdn.venice.client.app.logistic.widgets;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.data.PartyMaintenanceData;
import com.gdn.venice.client.app.logistic.view.ProviderManagementView;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Tab for Logistics Provider Location Contact detail
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderContactTab extends Tab {
	ProviderManagementView providerView;
	/*
	 * The tool strip for Provider Contact tab
	 */	
	ToolStrip providerContactToolStrip;
	
	/*
	 * The add , save, and remove buttons for the Contact details
	 */

	ToolStripButton addContactDetailButton;
	ToolStripButton removeContactDetailButton;
	ToolStripButton saveContactDetailButton;
	
	/*
	 * The list grid that displays the Contact details
	 */	
	ListGrid providerContactDetailListGrid;
	
	/*
	 * The VLayout that set for Contact table
	 */	
	VLayout providerContactLayout;
	
	/**
	 * Constructor to build the tab
	 *
	 * @param title is the title of the tab
	 * @param contactData is the contact data source for Logistic provider
	 * @param providerView is the view object of Logistic Provider 
	 */
	public ProviderContactTab(String title, DataSource contactData, final ListGridRecord record, final ProviderManagementView providerView) {
		super(title);
		this.providerView = providerView;
		
		providerContactLayout = new VLayout();
		providerContactLayout.setHeight100();
		buildContactListGrid(record, contactData);
		setPane(providerContactLayout);		
	}
	
	/**
	 * This method use for build Contact detail Table
	 *
	 */
	public void buildContactListGrid(final ListGridRecord record, final DataSource contactData) {
		providerContactToolStrip = new ToolStrip();
		providerContactToolStrip.setWidth100();
		providerContactToolStrip.setPadding(3);
		
		addContactDetailButton = new ToolStripButton();
		addContactDetailButton.setIcon("[SKIN]/icons/add.png");
		addContactDetailButton.setTooltip("Add Contact Detail");
		addContactDetailButton.setTitle("Add");

		removeContactDetailButton = new ToolStripButton();
		removeContactDetailButton.setIcon("[SKIN]/icons/delete.png");
		removeContactDetailButton.setTooltip("Remove Contact Detail");
		removeContactDetailButton.setTitle("Remove");
		
		saveContactDetailButton = new ToolStripButton();
		saveContactDetailButton.setIcon("[SKIN]/icons/save.png");
		saveContactDetailButton.setTooltip("Save Contact Detail");
		saveContactDetailButton.setTitle("Save");
		
		providerContactToolStrip.addButton(addContactDetailButton);
		providerContactToolStrip.addButton(removeContactDetailButton);
		providerContactToolStrip.addButton(saveContactDetailButton);

		providerContactDetailListGrid = new ListGrid();
		
		providerContactDetailListGrid.setWidth100();
		providerContactDetailListGrid.setHeight100();
		providerContactDetailListGrid.setShowAllRecords(true);
		providerContactDetailListGrid.setSaveLocally(true);
		providerContactDetailListGrid.setSortField(0);
		providerContactDetailListGrid.setAutoFetchData(false);
		providerContactDetailListGrid.setCanResizeFields(true);
		providerContactDetailListGrid.setShowRowNumbers(true);
		providerContactDetailListGrid.setAutoFitData(Autofit.VERTICAL);
		providerContactDetailListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		providerContactDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		providerContactDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		providerContactDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(contactData));
	
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setWidth("2%");
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setHidden(true);
		
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID).setWidth("2%");
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID).setHidden(false);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID).setTitle("Party ID");

		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID).setWidth("2%");
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID).setHidden(true);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID).setTitle("Type ID");
		
		// Set Contact Detail Type Datasource from PartyMaintenanceData
		DataSource contactDetailTypeComboBoxDs = PartyMaintenanceData.getContactDetailTypeData();
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setTitle("Type");
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setWidth("5%");
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setOptionDataSource(contactDetailTypeComboBoxDs);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setDisplayField(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setValueField(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setCanEdit(true);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setCanFilter(false);
		
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setTitle("Detail");
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setWidth("10%");
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setHidden(false);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setCanEdit(true);
		providerContactDetailListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setRequired(true);
		
		//Set click handler for Add Contact  Button
		addContactDetailButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				providerContactDetailListGrid.startEditingNew();
				Long partyId = new Long(providerView.getProviderListGrid().getSelectedRecord().getAttribute(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID));
				providerContactDetailListGrid.setEditValue(providerContactDetailListGrid.getEditRow(), providerContactDetailListGrid.getFieldNum(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID), partyId.toString());				
			}			
		});
		
		//Set click handler for Remove Contact button handler
		removeContactDetailButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {				
				if(providerContactDetailListGrid.getSelection().length > 0){
					SC.ask("Remove Selected Rows", "Are you sure?", new BooleanCallback(){
						@Override
						public void execute(Boolean value) {
							if(value){
								removeSelectedContact();
							}
						}
					});
				}
			}	
		});
		
		//Set click handler for Save Contact Button
		saveContactDetailButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> contactDataMap = new HashMap<String, String>();

				/*
				 * A list of records from the ListGrid
				 */
				ListGridRecord[] contactRecords = providerContactDetailListGrid.getRecords();

				/*
				 * This is the map of the data for the whole ListGrid
				 */
				HashMap<String, String> contactMap = new HashMap<String, String>();
				
				/*
				 * This map is used to pass each row of data in the ListGrid
				 */
				HashMap<String, String> contactRowMap = new HashMap<String, String>();
									
				/*
				 * If all of the validation has passed then go ahead and save the records
				 */
					for (int i=0;i<contactRecords.length;i++) {
						contactRowMap.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, 
								contactRecords[i].getAttributeAsString(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID));
						contactRowMap.put(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID, 
								contactRecords[i].getAttributeAsString(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID));
						contactRowMap.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID, 
								contactRecords[i].getAttributeAsString(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID));
						contactRowMap.put(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, 
								contactRecords[i].getAttributeAsString(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC));
						contactRowMap.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, 
								contactRecords[i].getAttributeAsString(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL));
						
						contactMap.put("CONTACTDETAIL" + i, Util.formXMLfromHashMap(contactRowMap));
					}
					contactDataMap.put("CONTACTDETAIL", Util.formXMLfromHashMap(contactMap));
					providerView.getProviderManagementUiHandlers().onSaveContactClicked(contactDataMap);
				}		
		});

		DSCallback callBack = new DSCallback() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				providerContactDetailListGrid.setData(response.getData());
			}
		};
		
		if (record!=null) {
			contactData.fetchData(providerContactDetailListGrid.getFilterEditorCriteria(), callBack);
		}
		
		providerContactLayout.setMembers(providerContactToolStrip, providerContactDetailListGrid);			
	}

	/**
	 * Removes the selected contact detail rows 
	 */
	public void removeSelectedContact(){	
		/*
		 * A list of selected records from the ListGrid
		 */
		ListGridRecord[] addressRecords = providerContactDetailListGrid.getSelection();
		
		/*
		 * This is the map of the data for the whole ListGrid
		 */
		HashMap<String, String> contactDataMap = new HashMap<String, String>();
		
		/*
		 * This map is used to pass each row of data in the ListGrid
		 */
		HashMap<String, String> contactDetailRowMap = new HashMap<String, String>();
		
		/*
		 * This loop processes each of the selected records to be removed,  adding them
		 * to the map to be passed to the back end. Only the primary key is needed for delete
		 */
		for (int i=0;i<addressRecords.length;i++) {
			contactDetailRowMap.put(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, addressRecords[i].getAttributeAsString(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID));
			contactDataMap.put("REMOVEDCONTACT" + i, Util.formXMLfromHashMap(contactDetailRowMap));				
			}
		providerView.getProviderManagementUiHandlers().onRemoveContactClicked(contactDataMap);
		providerContactDetailListGrid.removeSelectedData();
		SC.say("Data Removed");
	}

	/**
	 * @return the providerContactDetailListGrid
	 */
	public ListGrid getProviderContactDetailListGrid() {
		return providerContactDetailListGrid;
	}
}
