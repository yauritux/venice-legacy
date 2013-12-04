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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Tab for Logistics Provider Service
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderServiceTab extends Tab{	
	ProviderManagementView providerView;
	/*
	 * The tool strip for Provider Service tab
	 */	
	ToolStrip providerServiceToolStrip;
	
	/*
	 * The add , save and remove buttons for the Service details
	 */
	ToolStripButton addServiceButton;
	ToolStripButton removeServiceButton;
	ToolStripButton saveServiceButton;
	
	/*
	 * The list grid that displays the Service details
	 */	
	ListGrid providerServiceListGrid;
	
	/*
	 * The VLayout that set for Service table
	 */	
	VLayout providerServiceLayout;

	/**
	 * Constructor to build the tab
	 *
	 * @param title is the title of the tab
	 * @param serviceData is the service data source for Logistic provider
	 * @param providerView is the view object of Logistic Provider 
	 */
	public ProviderServiceTab(String title, DataSource serviceData, ListGridRecord record, ProviderManagementView providerView) {
		super(title);
		this.providerView = providerView;
		
		providerServiceLayout = new VLayout();
		providerServiceLayout.setHeight100();
		buildServiceListGrid(record, serviceData);
		setPane(providerServiceLayout);
	}
	/**
	 * This method use for build Service detail Table
	 */
	public void buildServiceListGrid(final ListGridRecord record, final DataSource serviceData){
		providerServiceToolStrip = new ToolStrip();
		providerServiceToolStrip.setWidth100();
		providerServiceToolStrip.setPadding(3);

		addServiceButton = new ToolStripButton();
		addServiceButton.setIcon("[SKIN]/icons/add.png");
		addServiceButton.setTooltip("Add Service");
		addServiceButton.setTitle("Add");

		removeServiceButton = new ToolStripButton();
		removeServiceButton.setIcon("[SKIN]/icons/delete.png");
		removeServiceButton.setTooltip("Remove Service");
		removeServiceButton.setTitle("Remove");
	
		saveServiceButton = new ToolStripButton();
		saveServiceButton.setIcon("[SKIN]/icons/save.png");
		saveServiceButton.setTooltip("Save Service");
		saveServiceButton.setTitle("Save");
	
		providerServiceToolStrip.addButton(addServiceButton);
		providerServiceToolStrip.addButton(removeServiceButton);
		providerServiceToolStrip.addButton(saveServiceButton);
	
		providerServiceListGrid = new ListGrid();
	
		providerServiceListGrid.setWidth100();
		providerServiceListGrid.setHeight100();
		providerServiceListGrid.setShowAllRecords(true);
		providerServiceListGrid.setSaveLocally(true);
		providerServiceListGrid.setSortField(0);
		providerServiceListGrid.setAutoFetchData(false);
		providerServiceListGrid.setCanResizeFields(true);
		providerServiceListGrid.setShowRowNumbers(true);
		providerServiceListGrid.setShowFilterEditor(false);
		providerServiceListGrid.setAutoFitData(Autofit.VERTICAL);
		providerServiceListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		providerServiceListGrid.setSelectionType(SelectionStyle.SIMPLE);
		providerServiceListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
	
		providerServiceListGrid.setFields(Util.getListGridFieldsFromDataSource(serviceData));

		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID).setWidth("1%");
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID).setHidden(true);
		
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setWidth("1%");
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setHidden(false);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setTitle("Provider Id");

		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE).setWidth("2%");
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE).setHidden(false);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE).setTitle("Service Code");
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE).setCanEdit(true);
	
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC).setWidth("10%");
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC).setCanEdit(true);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC).setTitle("Description");
		
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID).setWidth("1%");
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID).setHidden(true);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID).setTitle("Service Type ID");
	
		//Set Service Type ComboBox datasource from ProviderData
		DataSource serviceTypeComboBoxDs = ProviderData.getLogisticsProviderServiceTypeData();
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC).setWidth("3%");
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC).setAlign(Alignment.RIGHT);	
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC).setOptionDataSource(serviceTypeComboBoxDs);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC).setDisplayField(DataNameTokens.LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC).setValueField(DataNameTokens.LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC).setCanEdit(true);
		providerServiceListGrid.getField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC).setTitle("Service Type");

		//Set click handler for Add Contact Detail Button
		addServiceButton.addClickHandler(new ClickHandler() {
		/* (non-Javadoc)
		 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
		 */
			@Override
			public void onClick(ClickEvent event) {
				providerServiceListGrid.startEditingNew();	
				Long providerId = new Long(providerView.getProviderListGrid().getSelectedRecord().getAttribute(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
				providerServiceListGrid.setEditValue(providerServiceListGrid.getEditRow(), providerServiceListGrid.getFieldNum(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID), providerId.toString());			
			}		
		});
	
		//Set click handler for Remove Service button handler
		removeServiceButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {				
				if(providerServiceListGrid.getSelection().length > 0){
					SC.ask("Remove Selected Rows", "Are you sure?", new BooleanCallback(){	
						@Override
						public void execute(Boolean value) {
							if(value){
								removeSelectedService();
							}
						}
					});
				}
			}	
		});
		
		//Set click handler for Save Service Button handler
		saveServiceButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> serviceDataMap = new HashMap<String, String>();
				/*
				 * A list of records from the ListGrid
				 */
				ListGridRecord[] serviceRecords = providerServiceListGrid.getRecords();

				/*
				 * This is the map of the data for the whole ListGrid
				 */
				HashMap<String, String> serviceMap = new HashMap<String, String>();
				
				/*
				 * This map is used to pass each row of data in the ListGrid
				 */
				HashMap<String, String> serviceRowMap = new HashMap<String, String>();
									
				/*
				 * If all of the validation has passed then go ahead and save the records
				 */
					for (int i=0;i<serviceRecords.length;i++) {
						serviceRowMap.put(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID, 
								serviceRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID));
						serviceRowMap.put(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE, 
								serviceRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE));
						serviceRowMap.put(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, 
								serviceRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
						serviceRowMap.put(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC, 
								serviceRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC));
						serviceRowMap.put(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID, 
								serviceRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID));
						serviceRowMap.put(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC, 
								serviceRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
						
						serviceMap.put("SERVICEDETAIL" + i, Util.formXMLfromHashMap(serviceRowMap));
					}
					serviceDataMap.put("SERVICEDETAIL", Util.formXMLfromHashMap(serviceMap));
					providerView.getProviderManagementUiHandlers().onSaveServiceClicked(serviceDataMap);
				}			
		});

		DSCallback callBack = new DSCallback() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				providerServiceListGrid.setData(response.getData());
			}
		};
		
		if (record!=null) {
			serviceData.fetchData(providerServiceListGrid.getFilterEditorCriteria(), callBack);
		}		
		providerServiceLayout.setMembers(providerServiceToolStrip, providerServiceListGrid);
	}
	
	/**
	 * Removes the selected contact detail rows 
	 */
	public void removeSelectedService(){		
		/*
		 * A list of selected records from the ListGrid
		 */
		ListGridRecord[] serviceRecords = providerServiceListGrid.getSelection();
		
		/*
		 * This is the map of the data for the whole ListGrid
		 */
		HashMap<String, String> serviceDataMap = new HashMap<String, String>();
		
		/*
		 * This map is used to pass each row of data in the ListGrid
		 */
		HashMap<String, String> serviceDetailRowMap = new HashMap<String, String>();
		
		/*
		 * This loop processes each of the selected records to be deleted adding them
		 * to the map to be passed to the back end. Only the primary key is needed for delete
		 */
		for (int i=0;i<serviceRecords.length;i++) {
			serviceDetailRowMap.put(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID, serviceRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID));
			serviceDataMap.put("REMOVEDSERVICE" + i, Util.formXMLfromHashMap(serviceDetailRowMap));				
		}
		providerView.getProviderManagementUiHandlers().onRemoveServiceClicked(serviceDataMap);
		providerServiceListGrid.removeSelectedData();
		SC.say("Data Removed");
	}
	/**
	 * @return the providerServiceListGrid
	 */
	public ListGrid getProviderServiceListGrid() {
		return providerServiceListGrid;
	}
}
