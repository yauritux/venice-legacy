package com.gdn.venice.client.app.logistic.widgets;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.data.ProviderData;
import com.gdn.venice.client.app.logistic.view.ProviderManagementView;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Tab for Logistics Provider Location Address detail
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderAddressTab extends Tab{	
	ProviderManagementView providerView;
	/*
	 * The tool strip for Provider Address tab
	 */	
	ToolStrip providerAddressToolStrip;
	
	/*
	 * The add, save, and remove buttons for the Address  details
	 */
	ToolStripButton addAddressButton;
	ToolStripButton removeAddressButton;
	ToolStripButton saveAddressButton;
	
	/*
	 * The list grid that displays the Address details
	 */	
	ListGrid providerAddressDetailListGrid;
	
	/*
	 * The list grid that displays the City data
	 */	
	ListGrid cityListGrid;
	
	/*
	 * The VLayout that set for Address table
	 */	
	VLayout providerAddressLayout;
	
	/**
	 * Constructor to build the tab
	 *
	 * @param title is the title of the tab
	 * @param addressData is the address data source for Logistic provider
	 * @param providerView is the view object of Logistic Provider 
	 */
	public ProviderAddressTab(String title, DataSource addressData, final ListGridRecord record, final ProviderManagementView providerView) {
		super(title);
		this.providerView = providerView;
		
		providerAddressLayout = new VLayout();
		providerAddressLayout.setHeight100();
		buildAddressListGrid(record, addressData);
		setPane(providerAddressLayout);		
	}
	
	/**
	 * This method use for build Address Table
	 */
	public void buildAddressListGrid(final ListGridRecord record, final DataSource addressData){
		providerAddressToolStrip = new ToolStrip();
		providerAddressToolStrip.setWidth100();
		providerAddressToolStrip.setPadding(3);

		addAddressButton = new ToolStripButton();
		addAddressButton.setIcon("[SKIN]/icons/add.png");
		addAddressButton.setTooltip("Add Address");
		addAddressButton.setTitle("Add");

		removeAddressButton = new ToolStripButton();
		removeAddressButton.setIcon("[SKIN]/icons/delete.png");
		removeAddressButton.setTooltip("Remove Address");
		removeAddressButton.setTitle("Remove");
		
		saveAddressButton = new ToolStripButton();
		saveAddressButton.setIcon("[SKIN]/icons/save.png");
		saveAddressButton.setTooltip("Save Address");
		saveAddressButton.setTitle("Save");
		
		providerAddressToolStrip.addButton(addAddressButton);
		providerAddressToolStrip.addButton(removeAddressButton);
		providerAddressToolStrip.addButton(saveAddressButton);
		
		providerAddressDetailListGrid = new ListGrid(){			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent(com.smartgwt.client.widgets.grid.ListGridRecord, java.lang.Integer)
			 */
			@Override  
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum){
					final String fieldName = this.getFieldName(colNum);
					if(fieldName.equals(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME)){
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
						
						button.addClickHandler(new ClickHandler(){
							/* (non-Javadoc)
							 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
							 */
							@Override
							public void onClick(ClickEvent event) { 
								if (fieldName.equals(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME)){
									buildProviderSelectCityWindow(record).show();
								}														
							}
						});						
						layout.setMembers(label, button);
						return layout; 
					}else {
						return super.createRecordComponent(record, colNum);
					}
				}
		};
		
		providerAddressDetailListGrid.setWidth100();
		providerAddressDetailListGrid.setHeight100();
		providerAddressDetailListGrid.setShowAllRecords(true);
		providerAddressDetailListGrid.setSortField(0);
		providerAddressDetailListGrid.setAutoFetchData(false);
		providerAddressDetailListGrid.setCanResizeFields(true);
		providerAddressDetailListGrid.setShowRowNumbers(true);
		providerAddressDetailListGrid.setAutoFitData(Autofit.VERTICAL);
		providerAddressDetailListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		providerAddressDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		providerAddressDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		providerAddressDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(addressData));

		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID).setWidth(75);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID).setHidden(false);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID).setTitle("Party ID");
		
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setWidth(75);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setHidden(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setCanEdit(false);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setTitle("Address ID");

		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1).setWidth(120);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1).setCanEdit(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1).setTitle("Street Address 1");
		
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2).setWidth(120);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2).setCanEdit(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2).setTitle("Street Address 2");
		
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN).setWidth(100);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN).setCanEdit(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN).setTitle("Kelurahan");
		
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN).setWidth(100);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN).setCanEdit(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN).setTitle("Kecamatan");

		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID).setWidth(75);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID).setHidden(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID).setTitle("City ID");

		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME).setWidth(100);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME).setTitle("City");
		
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID).setWidth(75);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID).setHidden(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID).setTitle("Province ID");
		
		/*
		 * Initialize the datasource for province combo box
		 */
		DataSource provinceComboBoxDs = ProviderData.getProvinceData();
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setOptionDataSource(provinceComboBoxDs);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setDisplayField(DataNameTokens.VENSTATE_STATENAME);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setValueField(DataNameTokens.VENSTATE_STATEID);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setWidth(100);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setCanEdit(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setTitle("Province");
		
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE).setWidth(75);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE).setCanEdit(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE).setTitle("Postal Code");
		
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID).setWidth(75);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID).setHidden(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID).setTitle("Country ID");

		/*
		 * Initialize the datasource for province combo box
		 */
		DataSource countryComboBoxDs = ProviderData.getCountryData();
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setOptionDataSource(countryComboBoxDs);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setDisplayField(DataNameTokens.VENCOUNTRY_COUNTRYNAME);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setValueField(DataNameTokens.VENCOUNTRY_COUNTRYID );
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setWidth(100);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setCanEdit(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setTitle("Country");

		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID).setWidth(75);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID).setHidden(true);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID).setTitle("Address Type ID");
		/*
		 * Initialize the datasource for province combo box
		 */
		DataSource addressTypeComboBoxDs = ProviderData.getAddressTypeData();
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC).setOptionDataSource(addressTypeComboBoxDs);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC).setDisplayField(DataNameTokens.VENADDRESSTYPE_ADDRESSTYPEDESC);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC).setValueField(DataNameTokens.VENADDRESSTYPE_ADDRESSTYPEID );
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC).setWidth(100);
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC).setTitle("Address Type");
		providerAddressDetailListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC).setCanEdit(true);
		
		providerAddressDetailListGrid.setShowGroupSummary(true);
		providerAddressDetailListGrid.setShowRecordComponents(true);
		providerAddressDetailListGrid.setShowRecordComponentsByCell(true);
		
		//Set click handler for Add Address  Button
		addAddressButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				providerAddressDetailListGrid.startEditingNew();
				Long partyId = new Long(providerView.getProviderListGrid().getSelectedRecord().getAttribute(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID));
				providerAddressDetailListGrid.setEditValue(providerAddressDetailListGrid.getEditRow(), providerAddressDetailListGrid.getFieldNum(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID), partyId.toString());			
			}		
		});
		
		//Set click handler for Remove Address button handler
		removeAddressButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {				
				if(providerAddressDetailListGrid.getSelection().length > 0){
					SC.ask("Remove Selected Rows", "Are you sure?", new BooleanCallback(){
						@Override
						public void execute(Boolean value) {
							if(value){
								removeSelectedAddress();
							}
						}
					});
				}
			}	
		});
		
		//Set click handler for Save Address Button
		saveAddressButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> addressDataMap = new HashMap<String, String>();

				/*
				 * A list of records from the ListGrid
				 */
				ListGridRecord[] addressRecords = providerAddressDetailListGrid.getSelection();

				/*
				 * This is the map of the data for the whole ListGrid
				 */
				HashMap<String, String> addressMap = new HashMap<String, String>();
				
				/*
				 * This map is used to pass each row of data in the ListGrid
				 */
				HashMap<String, String> addressRowMap = new HashMap<String, String>();
									
				/*
				 * If all of the validation has passed then go ahead and save the records
				 */

					for (int i=0;i<addressRecords.length;i++) {
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID));
						addressRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC, 
								addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC));
						addressMap.put("ADDRESS" + i, Util.formXMLfromHashMap(addressRowMap));
					}
					addressDataMap.put("ADDRESS", Util.formXMLfromHashMap(addressMap));
					providerView.getProviderManagementUiHandlers().onSaveAddressClicked(addressDataMap);
				}		
		});
		
		DSCallback callBack = new DSCallback() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				providerAddressDetailListGrid.setData(response.getData());
			}
		};
		
		if (record!=null) {
			addressData.fetchData(providerAddressDetailListGrid.getFilterEditorCriteria(), callBack);
		}
		providerAddressLayout.setMembers(providerAddressToolStrip, providerAddressDetailListGrid);
	}

	/**
	 * Removes the selected address
	 */
	public void removeSelectedAddress(){		
		/*
		 * A list of selected records from the ListGrid
		 */
		ListGridRecord[] addressRecords = providerAddressDetailListGrid.getSelection();
		
		/*
		 * This is the map of the data for the whole ListGrid
		 */
		HashMap<String, String> addressMap = new HashMap<String, String>();
		
		/*
		 * This map is used to pass each row of data in the ListGrid
		 */
		HashMap<String, String> addressDetailRowMap = new HashMap<String, String>();
		
		/*
		 * This loop processes each of the selected records to be removied ,adding them
		 * to the map to be passed to the back end. Only the primary key is needed for delete
		 */
		for (int i=0;i<addressRecords.length;i++) {
			addressDetailRowMap.put(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID, 
					addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID));
			addressDetailRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, 
					addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID));
			addressDetailRowMap.put(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID, 
					addressRecords[i].getAttributeAsString(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID));
			addressMap.put("REMOVEDADDRESS" + i, Util.formXMLfromHashMap(addressDetailRowMap));				
			}
		providerView.getProviderManagementUiHandlers().onRemoveAddressClicked(addressMap);
		providerAddressDetailListGrid.removeSelectedData();
		SC.say("Data Removed");
	}
	
	/**
	 * This Windows use for City Popup Screen
	 */
	public Window buildProviderSelectCityWindow(final ListGridRecord record) {
		final Window selectCityWindow = new Window();
		selectCityWindow.setWidth(400);
		selectCityWindow.setHeight(400);
		selectCityWindow.setTitle("Select City/Regency");
		selectCityWindow.setShowMinimizeButton(false);
		selectCityWindow.setIsModal(true);
		selectCityWindow.setShowModalMask(true);
		selectCityWindow.centerInPage();
		selectCityWindow.addCloseClickHandler(new CloseClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
			 */
			public void onCloseClick(CloseClientEvent event) {
				selectCityWindow.destroy();
			}
		});

		VLayout providerSelectCityLayout = new VLayout();
		providerSelectCityLayout.setHeight100();
		
		ToolStrip providerCityToolStrip = new ToolStrip();
		providerCityToolStrip.setWidth100();
		providerCityToolStrip.setPadding(3);
		
		ToolStripButton addCityButton = new ToolStripButton();
		addCityButton.setIcon("[SKIN]/icons/add.png");
		addCityButton.setTooltip("Add City/Region");
		addCityButton.setTitle("Add");

		final ToolStripButton removeCityButton = new ToolStripButton();
		removeCityButton.setIcon("[SKIN]/icons/delete.png");
		removeCityButton.setTooltip("Remove City");
		removeCityButton.setTitle("Remove");
		
		providerCityToolStrip.addButton(addCityButton);
		providerCityToolStrip.addButton(removeCityButton);
		removeCityButton.setDisabled(true);
		
		cityListGrid = new ListGrid();
		cityListGrid.setHeight(300);
		cityListGrid.setSelectionType(SelectionStyle.SIMPLE);
		cityListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		cityListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		cityListGrid.setCanEdit(true);

		cityListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.EditCompleteHandler#onEditComplete(com.smartgwt.client.widgets.grid.events.EditCompleteEvent)
			 */
			@Override
			public void onEditComplete(EditCompleteEvent event) {	
				cityListGrid.saveAllEdits();
				cityListGrid.clearCriteria();
				refreshProviderCityData();				
			}
		});
		
		cityListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshProviderCityData();
			}
		});
		
		/*
		 * Load Party data from VenParty
		 */

		DataSource cityDataSource = ProviderData.getCityData();
		cityListGrid.setDataSource(cityDataSource);
		cityListGrid.setFields(Util.getListGridFieldsFromDataSource(cityDataSource));
		cityListGrid.setShowFilterEditor(true);
		cityListGrid.setAutoFetchData(true);
		cityListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		cityListGrid.getField(DataNameTokens.VENCITY_CITYID).setHidden(true);
		cityListGrid.getField(DataNameTokens.VENCITY_CITYID).setWidth("1%");
		cityListGrid.getField(DataNameTokens.VENCITY_CITYCODE).setWidth("3%");
		cityListGrid.getField(DataNameTokens.VENCITY_CITYNAME).setWidth("5%");
		
		//Set handler for Add City button
		addCityButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				cityListGrid.startEditingNew();					
			}			
		});
		
		//Set handler for Remove City button
		removeCityButton.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?",
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									cityListGrid.removeSelectedData();
									cityListGrid.clearCriteria();
									SC.say("Data Removed");
								} else {

								}
							}
						});
			}
		});
		
		
		HLayout providerSelectCityButtons = new HLayout(5);

		final IButton buttonSelectCity = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");

		buttonSelectCity.setDisabled(true);
		buttonSelectCity.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String cityId = cityListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENCITY_CITYID);
				String cityName = cityListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENCITY_CITYNAME);
				
				record.setAttribute(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID, cityId);
				record.setAttribute(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, cityName);
				
				providerAddressDetailListGrid.redraw();
				selectCityWindow.destroy();
			}
		});
		
		//Set click handler for Cancel Button
		buttonCancel.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				selectCityWindow.destroy();
			}
		});
		providerSelectCityButtons.setAlign(Alignment.CENTER);
		providerSelectCityButtons.setMembers(buttonSelectCity, buttonCancel);
		
		providerSelectCityLayout.setMembers(providerCityToolStrip, cityListGrid,providerSelectCityButtons );
		selectCityWindow.addItem(providerSelectCityLayout);
		
		cityListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (cityListGrid.getSelection().length>=1) {
					removeCityButton.enable();
				}else {
					removeCityButton.disable();
				}				
				buttonSelectCity.setDisabled(cityListGrid.getSelection().length!=1);
			}
		});
		
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				cityListGrid.setData(response.getData());
			}
		};

		cityListGrid.getDataSource().fetchData(cityListGrid.getFilterEditorCriteria(), callBack);
		return selectCityWindow;
	}
	
	/**
	 * Refresh City/Region List on City/Region Window
	 */
	private void refreshProviderCityData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				cityListGrid.setData(response.getData());
			}
		};
		cityListGrid.getDataSource().fetchData(cityListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * @return the addressDetailListGrid
	 */
	public ListGrid getProviderAddressDetailListGrid() {
		return providerAddressDetailListGrid;
	}
}
