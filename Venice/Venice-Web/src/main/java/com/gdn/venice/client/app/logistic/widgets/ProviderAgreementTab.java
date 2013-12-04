package com.gdn.venice.client.app.logistic.widgets;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.view.ProviderManagementView;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
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
 * Tab for Logistics Provider Agreement
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderAgreementTab extends Tab {
	ProviderManagementView providerView;
	/*
	 * The tool strip for Provider Agreement tab
	 */	
	ToolStrip providerAgreementToolStrip;
	
	/*
	 * The add, remove and save buttons for the Agreement details
	 */
	ToolStripButton addAgreementButton;
	ToolStripButton removeAgreementButton;
	ToolStripButton saveAgreementButton;

	/*
	 * The list grid that displays the Agreement details
	 */	
	ListGrid providerAgreementListGrid;
	
	/*
	 * The VLayout that set for Service table
	 */	
	VLayout providerAgreementLayout;

	/**
	 * Constructor to build the tab
	 *
	 * @param title is the title of the tab
	 * @param agreementData is the agreement data source for Logistic provider
	 * @param providerView is the view object of Logistic Provider 
	 */
	public ProviderAgreementTab(String title, DataSource agreementData, ListGridRecord record, ProviderManagementView providerView) {
		super(title);
		this.providerView = providerView;

		providerAgreementLayout = new VLayout();
		providerAgreementLayout.setHeight100();
		buildAgreementListGrid(record, agreementData);
		
		setPane(providerAgreementLayout);
	}

	/**
	 * This method use for build Agreement detail Table
	 *
	 */
	public void buildAgreementListGrid(final ListGridRecord record, final DataSource agreementData) {
		providerAgreementToolStrip = new ToolStrip();
		providerAgreementToolStrip.setWidth100();
		providerAgreementToolStrip.setPadding(3);

		addAgreementButton = new ToolStripButton();
		addAgreementButton.setIcon("[SKIN]/icons/add.png");
		addAgreementButton.setTooltip("Add Agreement");
		addAgreementButton.setTitle("Add");

		removeAgreementButton = new ToolStripButton();
		removeAgreementButton.setIcon("[SKIN]/icons/delete.png");
		removeAgreementButton.setTooltip("Remove Agreement");
		removeAgreementButton.setTitle("Remove");
		
		saveAgreementButton = new ToolStripButton();
		saveAgreementButton.setIcon("[SKIN]/icons/save.png");
		saveAgreementButton.setTooltip("Save Agreement");
		saveAgreementButton.setTitle("Save");
		
		providerAgreementToolStrip.addButton(addAgreementButton);
		providerAgreementToolStrip.addButton(removeAgreementButton);
		providerAgreementToolStrip.addButton(saveAgreementButton);
		
		providerAgreementListGrid = new ListGrid();
		
		providerAgreementListGrid.setWidth100();
		providerAgreementListGrid.setHeight100();
		providerAgreementListGrid.setShowAllRecords(true);
		providerAgreementListGrid.setSortField(0);
		providerAgreementListGrid.setSaveLocally(true);
		providerAgreementListGrid.setAutoFetchData(false);
		providerAgreementListGrid.setCanResizeFields(true);
		providerAgreementListGrid.setShowRowNumbers(true);
		providerAgreementListGrid.setShowFilterEditor(false);
		providerAgreementListGrid.setAutoFitData(Autofit.VERTICAL);
		providerAgreementListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		providerAgreementListGrid.setSelectionType(SelectionStyle.SIMPLE);
		providerAgreementListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		providerAgreementListGrid.setFields(Util.getListGridFieldsFromDataSource(agreementData));

		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID).setHidden(true);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID).setCanEdit(false);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID).setTitle("Agreement Id");

		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setHidden(false);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setCanEdit(false);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setTitle("Provider Id");

		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC).setWidth("5%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC).setCanEdit(true);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC).setTitle("Description");

		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE).setCanEdit(true);
//		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE).setType(ListGridFieldType.DATE);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE).setTitle("Date");
		
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT).setCanEdit(true);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT).setType(ListGridFieldType.INTEGER);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT).setTitle("Pickup Days");

		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT).setCanEdit(true);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT).setType(ListGridFieldType.INTEGER);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT).setTitle("Delivery Days");
		
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE).setCanEdit(true);
//		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE).setType(ListGridFieldType.DATE);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE).setTitle("Exp. Date");

		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT).setCanEdit(true);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT).setType(ListGridFieldType.FLOAT);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT).setTitle("Disc. Level(%)");

		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE).setWidth("2%");
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE).setCanEdit(true);
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE).setType(ListGridFieldType.FLOAT);	
		providerAgreementListGrid.getField(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE).setTitle("PPN (%)");

		//Set click handler for Add Agreement Button 
		addAgreementButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				providerAgreementListGrid.startEditingNew();	
				Long providerId = new Long(providerView.getProviderListGrid().getSelectedRecord().getAttribute(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
				providerAgreementListGrid.setEditValue(providerAgreementListGrid.getEditRow(), providerAgreementListGrid.getFieldNum(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID), providerId.toString());
			}			
		});
		
		//Set click handler for Remove Agreement button 
		removeAgreementButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {				
				if(providerAgreementListGrid.getSelection().length > 0){
					SC.ask("Remove Selected Rows", "Are you sure?", new BooleanCallback(){	
						@Override
						public void execute(Boolean value) {
							if(value){
								removeSelectedAgreement();
							}
						}
					});
				}
			}	
		});
		
		//Set click handler for Save Agreement Button  handler
		saveAgreementButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> agreementDataMap = new HashMap<String, String>();

				/*
				 * A list of records from the ListGrid
				 */
				ListGridRecord[] agreementRecords = providerAgreementListGrid.getSelection();

				/*
				 * This is the map of the data for the whole ListGrid
				 */
				HashMap<String, String> agreementMap = new HashMap<String, String>();
				
				/*
				 * This map is used to pass each row of data in the ListGrid
				 */
				HashMap<String, String> agreementRowMap = new HashMap<String, String>();					
				
				/*
				 * If all of the validation has passed then go ahead and save the records
				 */
					for (int i=0;i<agreementRecords.length;i++) {
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID));
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC));
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT));
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT));
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE));
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE));
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT));						
						agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE, 
								agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE));
						agreementMap.put("AGREEMENTDETAIL" + i, Util.formXMLfromHashMap(agreementRowMap));
					}
					agreementDataMap.put("AGREEMENTDETAIL", Util.formXMLfromHashMap(agreementMap));
					providerView.getProviderManagementUiHandlers().onSaveAgreementClicked(agreementDataMap);
				}		
		});
		
		
		DSCallback callBack = new DSCallback() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				providerAgreementListGrid.setData(response.getData());
			}
		};
		
		if (record!=null) {
			agreementData.fetchData(providerAgreementListGrid.getFilterEditorCriteria(), callBack);
		}
		
		providerAgreementLayout.setMembers(providerAgreementToolStrip, providerAgreementListGrid);			
	}
	
	/**
	 * Removes the selected agreement detail rows 
	 */
	public void removeSelectedAgreement(){		
		/*
		 * A list of selected records from the ListGrid
		 */
		ListGridRecord[] agreementRecords = providerAgreementListGrid.getSelection();
		
		/*
		 * This is the map of the data for the whole ListGrid
		 */
		HashMap<String, String> agreementDataMap = new HashMap<String, String>();
		
		/*
		 * This map is used to pass each row of data in the ListGrid
		 */
		HashMap<String, String> agreementRowMap = new HashMap<String, String>();
		
		/*
		 * This loop processes each of the selected records to be removed, adding them
		 * to the map to be passed to the back end. Only the primary key is needed for delete
		 */
		for (int i=0;i<agreementRecords.length;i++) {
			agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID, 
					agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID));
			agreementRowMap.put(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, 
					agreementRecords[i].getAttributeAsString(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));

			agreementDataMap.put("REMOVEDAGREEMENT" + i, Util.formXMLfromHashMap(agreementRowMap));				
			}
		providerView.getProviderManagementUiHandlers().onRemoveAgreementClicked(agreementDataMap);
		providerAgreementListGrid.removeSelectedData();
		SC.say("Data Removed");
	}

	/**
	 * @return the providerAgreementListGrid
	 */
	public ListGrid getProviderAgreementListGrid() {
		return providerAgreementListGrid;
	}
}
