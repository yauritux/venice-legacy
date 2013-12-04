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
import com.smartgwt.client.types.ListGridFieldType;
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
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Tab for Logistics Provider Schedule
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderScheduleTab extends Tab {	
	/*
	 * The view Object of Provider Management View
	 */	
	ProviderManagementView providerView;
	/*
	 * The tool strip for Provider Service tab
	 */	
	ToolStrip providerScheduleToolStrip;
	
	/*
	 * The add and remove buttons for the Service details
	 */
	ToolStripButton addScheduleButton;
	ToolStripButton removeScheduleButton;
	ToolStripButton saveScheduleButton;
	
	VLayout providerScheduleLayout;
	/*
	 * The list grid that displays the Service details
	 */	
	ListGrid providerScheduleListGrid;
	ListGrid daysListGrid;

	public ProviderScheduleTab(String title, DataSource scheduleData, final ListGridRecord record, ProviderManagementView providerView) {		
		super(title);		
		this.providerView = providerView;
		
		providerScheduleLayout = new VLayout();
		providerScheduleLayout.setHeight100();
		buildScheduleListGrid(record, scheduleData);
		
		setPane(providerScheduleLayout);
	}

	public void buildScheduleListGrid(final ListGridRecord record, final DataSource scheduleData) {
		providerScheduleToolStrip = new ToolStrip();
		providerScheduleToolStrip.setWidth100();
		providerScheduleToolStrip.setPadding(3);

		addScheduleButton = new ToolStripButton();
		addScheduleButton.setIcon("[SKIN]/icons/add.png");
		addScheduleButton.setTooltip("Add Schedule");
		addScheduleButton.setTitle("Add");

		removeScheduleButton = new ToolStripButton();
		removeScheduleButton.setIcon("[SKIN]/icons/delete.png");
		removeScheduleButton.setTooltip("Remove Schedule");
		removeScheduleButton.setTitle("Remove");
		
		saveScheduleButton = new ToolStripButton();
		saveScheduleButton.setIcon("[SKIN]/icons/save.png");
		saveScheduleButton.setTooltip("Save Schedule");
		saveScheduleButton.setTitle("Save");
		
		providerScheduleToolStrip.addButton(addScheduleButton);
		providerScheduleToolStrip.addButton(removeScheduleButton);
		providerScheduleToolStrip.addButton(saveScheduleButton);
		
		providerScheduleListGrid = new ListGrid(){			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent(com.smartgwt.client.widgets.grid.ListGridRecord, java.lang.Integer)
			 */
			@Override  
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum){
					final String fieldName = this.getFieldName(colNum);
					if(fieldName.equals(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC)){
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
								if (fieldName.equals(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC)){
									loadDaysWindow(record).show();
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
		providerScheduleListGrid.setWidth100();
		providerScheduleListGrid.setHeight100();
		providerScheduleListGrid.setShowAllRecords(true);
		providerScheduleListGrid.setSortField(0);
		providerScheduleListGrid.setAutoFetchData(false);
		providerScheduleListGrid.setCanResizeFields(true);
		providerScheduleListGrid.setShowRecordComponents(true);
		providerScheduleListGrid.setShowRecordComponentsByCell(true);
		providerScheduleListGrid.setShowRowNumbers(true);
		providerScheduleListGrid.setShowFilterEditor(false);
		providerScheduleListGrid.setAutoFitData(Autofit.VERTICAL);
		providerScheduleListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		providerScheduleListGrid.setSelectionType(SelectionStyle.SIMPLE);
		providerScheduleListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);

		providerScheduleListGrid.setFields(Util.getListGridFieldsFromDataSource(scheduleData));
		
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID).setWidth("1%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID).setHidden(true);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID).setAlign(Alignment.LEFT);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID).setTitle("Pickup ID");
		
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setWidth("1%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setCanEdit(false);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setAlign(Alignment.LEFT);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setTitle("Provider ID");

		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME).setTitle("From");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME).setWidth("1%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME).setCanEdit(true);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME).setAlign(Alignment.LEFT);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME).setType(ListGridFieldType.TIME);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME).setRequired(true);

		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME).setTitle("To");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME).setWidth("1%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME).setCanEdit(true);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME).setAlign(Alignment.LEFT);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME).setType(ListGridFieldType.TIME);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME).setRequired(true);

		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC).setTitle("Description");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC).setWidth("3%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC).setAlign(Alignment.LEFT);		
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC).setCanEdit(true);		
		
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS).setTitle("Include Public Holidays");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS).setWidth("2%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS).setCanEdit(true);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS).setAlign(Alignment.CENTER);		
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS).setType(ListGridFieldType.BOOLEAN);
		
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID).setWidth("1%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID).setHidden(true);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID).setAlign(Alignment.LEFT);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID).setTitle("Days ID");
		
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC).setTitle("Days");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC).setWidth("5%");
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC).setCanEdit(true);
		providerScheduleListGrid.getField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC).setAlign(Alignment.LEFT);		
		
		//Set click handler for Add Contact Detail Button
		addScheduleButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				providerScheduleListGrid.startEditingNew();	
				Long providerId = new Long(providerView.getProviderListGrid().getSelectedRecord().getAttribute(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
				providerScheduleListGrid.setEditValue(providerScheduleListGrid.getEditRow(), providerScheduleListGrid.getFieldNum(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID), providerId.toString());				
			}			
		});
		
		//Set click handler for Remove Address button handler
		removeScheduleButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {				
				if(providerScheduleListGrid.getSelection().length > 0){
				SC.ask("Are you sure you want to remove this data?",
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									providerScheduleListGrid
											.removeSelectedData();
									providerScheduleListGrid
											.clearCriteria();
									SC.say("Data Removed");
								} else {

								}
							}
						});
				}
			}
		});			
		
		//Set click handler for Save Service Button handler
		saveScheduleButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> scheduleDataMap = new HashMap<String, String>();

				/*
				 * A list of records from the ListGrid
				 */
				ListGridRecord[] scheduleRecords = providerScheduleListGrid.getRecords();

				/*
				 * This is the map of the data for the whole ListGrid
				 */
				HashMap<String, String> scheduleMap = new HashMap<String, String>();
				
				/*
				 * This map is used to pass each row of data in the ListGrid
				 */
				HashMap<String, String> scheduleRowMap = new HashMap<String, String>();
									
				/*
				 * If all of the validation has passed then go ahead and save the records
				 */
					for (int i=0;i<scheduleRecords.length;i++) {
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID));
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME));
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME));
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC));
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS));
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID));
						scheduleRowMap.put(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC, 
								scheduleRecords[i].getAttributeAsString(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC));
						scheduleMap.put("SCHEDULEDETAIL" + i, Util.formXMLfromHashMap(scheduleRowMap));
					}
					scheduleDataMap.put("SCHEDULEDETAIL", Util.formXMLfromHashMap(scheduleMap));
					providerView.getProviderManagementUiHandlers().onSaveScheduleClicked(scheduleDataMap);
				}			
		});
		
		DSCallback callBack = new DSCallback() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				providerScheduleListGrid.setData(response.getData());
			}
		};
		
		if (record!=null) {
			scheduleData.fetchData(providerScheduleListGrid.getFilterEditorCriteria(), callBack);
		}
		providerScheduleLayout.setMembers(providerScheduleToolStrip, providerScheduleListGrid);
	}
	
	public Window loadDaysWindow(final ListGridRecord record) {
		final Window selectDaysWindow = new Window();
		selectDaysWindow.setWidth(300);
		selectDaysWindow.setHeight(300);
		selectDaysWindow.setTitle("Select Days of Week");
		selectDaysWindow.setShowMinimizeButton(false);
		selectDaysWindow.setIsModal(true);
		selectDaysWindow.setShowModalMask(true);
		selectDaysWindow.centerInPage();
		selectDaysWindow.addCloseClickHandler(new CloseClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.CloseClickHandler#onCloseClick(com.smartgwt.client.widgets.events.CloseClientEvent)
			 */
			public void onCloseClick(CloseClientEvent event) {
				selectDaysWindow.destroy();
			}
		});

		VLayout selectDaysLayout = new VLayout();
		selectDaysLayout.setHeight100();
		
		daysListGrid = new ListGrid();
		daysListGrid.setHeight(300);
		daysListGrid.setSelectionType(SelectionStyle.SIMPLE);
		daysListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		daysListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		daysListGrid.setCanEdit(false);
		
		/*
		 * Load Party data from LogScheduleDaysOfWeek
		 */
		DataSource daysDataSource = ProviderData.getDaysData();
		daysListGrid.setDataSource(daysDataSource);
		daysListGrid.setFields(Util.getListGridFieldsFromDataSource(daysDataSource));
		daysListGrid.setShowFilterEditor(false);
		daysListGrid.setAutoFetchData(true);
		daysListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		daysListGrid.getField(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID).setHidden(true);
		daysListGrid.getField(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID).setWidth("1%");
		daysListGrid.getField(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC).setWidth("3%");
					
		HLayout selectDaysButtons = new HLayout(5);

		final IButton buttonSelectDays = new IButton("Select");
		IButton buttonCancelDays = new IButton("Cancel");

		buttonSelectDays.setDisabled(true);
		buttonSelectDays.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String daysId = daysListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID);
				String daysName = daysListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC);
				
				record.setAttribute(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID, daysId);
				record.setAttribute(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC, daysName);
				
				providerScheduleListGrid.redraw();
				selectDaysWindow.destroy();
			}
		});
		
		//Set click handler for Cancel Party Button
		buttonCancelDays.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				selectDaysWindow.destroy();
			}
		});
		selectDaysButtons.setAlign(Alignment.CENTER);
		selectDaysButtons.setMembers(buttonSelectDays, buttonCancelDays);
		
		selectDaysLayout.setMembers(daysListGrid,selectDaysButtons );
		selectDaysWindow.addItem(selectDaysLayout);
		
		daysListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectDays.setDisabled(daysListGrid.getSelection().length==0);
			}
		});
		
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				daysListGrid.setData(response.getData());
			}
		};

		daysListGrid.getDataSource().fetchData(daysListGrid.getFilterEditorCriteria(), callBack);
		
		return selectDaysWindow;
	}
}
