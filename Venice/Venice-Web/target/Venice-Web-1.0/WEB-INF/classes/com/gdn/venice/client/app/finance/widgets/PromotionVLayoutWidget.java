package com.gdn.venice.client.app.finance.widgets;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.PromotionData;
import com.gdn.venice.client.app.finance.view.PromotionView;
import com.gdn.venice.client.util.Util;
import com.gwtplatform.mvp.client.View;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
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
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * PromotionVLayoutWidget - a widget class for displaying promotions
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PromotionVLayoutWidget extends VLayout {
	@SuppressWarnings("unused")
	private static final int LIST_HEIGHT = 140;
	private static final int PROMOTION_DETAIL_HEIGHT = 300;

	/*
	 * The main list grid for the promotions
	 */
	ListGrid promotionListGrid;

	/*
	 * The vertical layout used to lay out the top promotion list
	 */
	VLayout promotionListLayout;

	/*
	 * The vertical layout used to lay out the details of the promotion at
	 * the bottom
	 */
	VLayout promotionShareDetailVLayout;

	/*
	 * The list grid that displays the promotion share details
	 */
	ListGrid promotionShareDetailListGrid;
	
	/*
	 * The list grid that displays List of party
	 */
	
	ListGrid promotionSelectPartyListGrid;
	
	/*
	 * The tool strip for Promotion Share Detail
	 */
	
	ToolStrip promotionShareDetailToolStrip;

	/*
	 * The promotion view object
	 */
	View view;
	
	/*
	 * The Data Source for Promotion Share Detail
	 */
	DataSource promotionShareDetailDataSource = null;
	
	/*
	 * The Data Source for Promotion Share Calc Desc
	 */
	DataSource promotionShareCalcMethodComboBoxDs = null;


	/**
	 * Constructor that takes the view and instantiates 
	 * it for reference as well as builds the UI
	 * @param view
	 */
	public PromotionVLayoutWidget(View view) {
		this.view = view;

		setWidth100();
		setHeight100();

		promotionListLayout = new VLayout();
		promotionListLayout.setHeight100();
		promotionListLayout.setShowResizeBar(true);

		HTMLFlow promotionShareDetailFlow = new HTMLFlow();
		promotionShareDetailFlow.setAlign(Alignment.CENTER);
		promotionShareDetailFlow.setWidth100();
		promotionShareDetailFlow.setContents("<h2 align=\"center\">Please select a Promotion to show the Promotion Share detail</h2>");

		promotionShareDetailVLayout = new VLayout();
		promotionShareDetailVLayout.setWidth100();
		promotionShareDetailVLayout.setHeight(PROMOTION_DETAIL_HEIGHT);
		promotionShareDetailVLayout.setMembers(promotionShareDetailFlow);

		promotionListGrid = new ListGrid();

		promotionListGrid.setShowAllRecords(true);
		promotionListGrid.setSortField(0);

		promotionListGrid.setSelectionType(SelectionStyle.SIMPLE);
		promotionListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		promotionListGrid.setShowFilterEditor(true);
		promotionListGrid.setCanEdit(false);
		promotionListGrid.setAutoFetchData(false);
		promotionListGrid.setCanResizeFields(true);
		promotionListGrid.setShowRowNumbers(true);
		promotionListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.EditCompleteHandler#onEditComplete(com.smartgwt.client.widgets.grid.events.EditCompleteEvent)
			 */
			@Override
			public void onEditComplete(EditCompleteEvent event) {				
				promotionListGrid.saveAllEdits();
				promotionListGrid.clearCriteria();				
			}
		});

		promotionListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
					 */
					@Override
					public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
						refreshPromotionData();
					}
				});
		setMembers(promotionListLayout, promotionShareDetailVLayout);
	}

	/**
	 * Refreshes the promotion data from the data source
	 */
	public void refreshPromotionData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				promotionListGrid.setData(response.getData());
				promotionListGrid.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};

		promotionListGrid.getDataSource().fetchData(promotionListGrid.getFilterEditorCriteria(), callBack);

	}

	/**
	 * Loads the list of Promotion data (top table in the screen)
	 * 
	 * @param dataSource promotion data datasource
	 */
	public void loadPromotionData(DataSource dataSource) {
		promotionListGrid.setDataSource(dataSource);
		promotionListGrid.setAutoFetchData(false);
		promotionListGrid.setCanEdit(true);
		promotionListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		promotionListGrid.setCanSort(true);
		promotionListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));

		promotionListGrid.getField(DataNameTokens.VENPROMOTION_PROMOTIONID).setWidth("5%");
		promotionListGrid.getField(DataNameTokens.VENPROMOTION_PROMOTIONID).setHidden(true);
		promotionListGrid.getField(DataNameTokens.VENPROMOTION_PROMOTIONCODE).setWidth("5%");
		promotionListGrid.getField(DataNameTokens.VENPROMOTION_PROMOTIONNAME).setWidth("15%");
		promotionListGrid.getField(DataNameTokens.VENPROMOTION_VENPROMOTIONTYPE_PROMOTIONTYPEDESC).setWidth("15%");
		promotionListGrid.getField(DataNameTokens.VENPROMOTION_GDNMARGIN).setWidth("5%");
		promotionListGrid.getField(DataNameTokens.VENPROMOTION_MERCHANTMARGIN).setWidth("5%");
		promotionListGrid.getField(DataNameTokens.VENPROMOTION_OTHERSMARGIN).setWidth("5%");

		promotionListLayout.addMember(promotionListGrid);
	}

	/*
	 * Uihandlers if Selection on promotion List (Top Table) changed
	 */
	public void bindCustomUiHandlers() {
		promotionListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = promotionListGrid.getSelection();		
				/*
				 * Enable the remove button if there is a selected row
				 */
				if (selectedRecords.length == 0) {
					((PromotionView) view).getRemoveButton().disable();
				} else {
					((PromotionView) view).getRemoveButton().enable();
				}
				if (selectedRecords.length == 1) {
					showPromotionDetail(selectedRecords[0]);
				} else {
					HTMLFlow promotionDetailFlow = new HTMLFlow();
					promotionDetailFlow.setAlign(Alignment.CENTER);
					promotionDetailFlow.setWidth100();
					if (selectedRecords.length == 0) {
						promotionDetailFlow.setContents("<h2 align=\"center\">Please select a Promotion to show the share detail</h2>");
					} else if (selectedRecords.length > 1) {
						promotionDetailFlow.setContents("<h2 align=\"center\">More than one Promotion selected, please select only one to show the Promotion share detail</h2>");
					}
					promotionShareDetailVLayout.setMembers(promotionDetailFlow);
				}
			}
		});
	}

	/**
	 * Shows the promotion details
	 * 
	 * @param record
	 *  The selected record in the Promotion ListGrid
	 */
	public void showPromotionDetail(ListGridRecord record) {
		buildPromotionShareDetail(record);
	}

	/**
	 * Loads the Promotion Share detail data
	 * 
	 * @param record
	 *  Selected record in Promotion List Grid
	 */
	public void buildPromotionShareDetail(final ListGridRecord record) {
		promotionShareDetailToolStrip = new ToolStrip();
		promotionShareDetailToolStrip.setWidth100();
		promotionShareDetailToolStrip.setPadding(2);

		final ToolStripButton addDetailButton = new ToolStripButton();
		addDetailButton.setIcon("[SKIN]/icons/add.png");
		addDetailButton.setTooltip("Add Share Detail Promotion");
		addDetailButton.setTitle("Add");

		ToolStripButton removeDetailButton = new ToolStripButton();
		removeDetailButton.setIcon("[SKIN]/icons/delete.png");
		removeDetailButton.setTooltip("Remove Share Detail Promotion");
		removeDetailButton.setTitle("Remove");
		
		ToolStripButton saveDetailButton = new ToolStripButton();
		saveDetailButton.setIcon("[SKIN]/icons/save.png");
		saveDetailButton.setTooltip("Save Share Detail Promotion");
		saveDetailButton.setTitle("Save");
		
		promotionShareDetailToolStrip.addButton(addDetailButton);
		promotionShareDetailToolStrip.addButton(removeDetailButton);
		promotionShareDetailToolStrip.addButton(saveDetailButton);
		
		promotionShareDetailListGrid = new ListGrid(){
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent(com.smartgwt.client.widgets.grid.ListGridRecord, java.lang.Integer)
			 */
			@Override  
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum){
					final String fieldName = this.getFieldName(colNum);
					if(fieldName.equals(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME)){
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
								if (fieldName.equals(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME)){
									buildPromotionSelectPartyWindow(record).show();
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
				
		// Set promotion share Datasource from PromotionData
		promotionShareDetailDataSource = PromotionData.getPromotionShareData (record.getAttributeAsString(DataNameTokens.VENPROMOTION_PROMOTIONID));			
	
		/*
		 * Note that there seems to be a problem with adding rows to the list grid
		 * when there is a component canvas used in one of the columns (createRecordComponent).
		 * The workaround is:
		 * 		o Do not use setDataSource
		 * 		o Do not use setAutoFetchdata(true)
		 * 		o implement a DSCallback, override the execute operation and use setData() - see below
		 */
		//promotionShareDetailListGrid.setSaveLocally(true);
		//promotionShareDetailListGrid.setAutoFetchData(true);
		//promotionShareDetailListGrid.setDataSource(promotionShareDetailDataSource);
	
		promotionShareDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(promotionShareDetailDataSource));
		promotionShareDetailListGrid.setShowAllRecords(true);
		promotionShareDetailListGrid.setSortField(0);				
		promotionShareDetailListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		promotionShareDetailListGrid.setShowRowNumbers(true);
		promotionShareDetailListGrid.setCanResizeFields(true);		

		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID).setHidden(false);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID).setWidth("5%");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID).setTitle("Promotion ID");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID).setAlign(Alignment.CENTER);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID).setHidden(true);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID).setTitle("PartyID");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME).setWidth("25%");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME).setTitle("Party Name");
		
		/*
		 * Set the data source for the combo box and set the display field and value field so that we get the correct bindings
		 */	
		promotionShareCalcMethodComboBoxDs = PromotionData.getPromotionShareCalcMethodData();

		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC).setWidth("15%");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC).setTitle("Calc Method");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC).setOptionDataSource(promotionShareCalcMethodComboBoxDs);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID).setHidden(true);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID).setTitle("CalcMethodID");
	
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC).setDisplayField(DataNameTokens.VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC).setValueField(DataNameTokens.VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID);

// comment this back in to see the combo box internal values
//		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC).addChangedHandler( new ChangedHandler(){
//
//			@Override
//			public void onChanged(com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
//				promotionShareDetailListGrid.getEditedRecord(promotionShareDetailListGrid.getEditRow()).getAttribute(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC);
//				System.out.println("DisplayField:" + event.getItem().getDisplayField());
//				System.out.println("ValueField:" + event.getItem().getValueField());
//				System.out.println("DisplayValue:" + event.getItem().getDisplayValue());
//				System.out.println("Value:" + event.getItem().getValue());		
//			}
//			
//		});
		
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE).setWidth("20%");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE).setCanEdit(true);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE).setAlign(Alignment.RIGHT);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE).setCellAlign(Alignment.RIGHT);
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE).setTitle("Share");
		promotionShareDetailListGrid.getField(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC).setCanEdit(true);
		
		promotionShareDetailListGrid.setShowGroupSummary(true);
		promotionShareDetailListGrid.setShowRecordComponents(true);
		promotionShareDetailListGrid.setShowRecordComponentsByCell(true);

		promotionShareDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);  
		promotionShareDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		//Set click handler for New Share Detail Button
		addDetailButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				promotionShareDetailListGrid.startEditingNew();				
				Long promotionId = new Long(promotionListGrid.getSelectedRecord().getAttribute(DataNameTokens.VENPROMOTION_PROMOTIONID));
				promotionShareDetailListGrid.setEditValue(promotionShareDetailListGrid.getEditRow(), promotionShareDetailListGrid.getFieldNum(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID), promotionId.toString());
			}
		});
		
		//Set click hanlder for Remove Detail button handler
		removeDetailButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {				
				if(promotionShareDetailListGrid.getSelection().length > 0){
					SC.ask("Remove Selected Rows", "Are you sure?", new BooleanCallback(){
						@Override
						public void execute(Boolean value) {
							if(value){
								removeSelectedPromotionShareDetails();
							}
						}
					});
				}
			}			
		});

		//Set click handler for Save Detail Button
		saveDetailButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> promotionShareDataMap = new HashMap<String, String>();

				/*
				 * A list of records from the ListGrid
				 */
				ListGridRecord[] promotionShareRecords = promotionShareDetailListGrid.getSelection();

				/*
				 * This is the map of the data for the whole ListGrid
				 */
				HashMap<String, String> promotionShareDetailMap = new HashMap<String, String>();
				
				/*
				 * This map is used to pass each row of data in the ListGrid
				 */
				HashMap<String, String> promotionShareDetailRowMap = new HashMap<String, String>();
				
				
				Boolean bValidationFailed = false;
				/*
				 * Validate that the promotion calc values are numbers
				 * Validate that only share or percentage is selected for all of the records
				 * Validate that the promotion calc values sum to 100% if the share is percentage based
				 */
				String calcMethod = null;
				
				/*
				 * The total to use for checking the sum of percentages 
				 */
				Double calcValueTotal = new Double(0);
				for (int i=0;i<promotionShareRecords.length;i++){
					/*
					 * First check that the calc value is numeric
					 */
					try{
						@SuppressWarnings("unused")
						Double promotionCalcValue = new Double(promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE));
					}catch(NumberFormatException nfe){
						SC.say("One of the promotion calc share values is not a number!");
						bValidationFailed = true;
						break;
					}
					
					/*
					 * Next check that the calc methods are consistent for all rows
					 */
					if(calcMethod == null){
						calcMethod = promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC);
					}else{
						String currentRowCalcMethod = promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC);
						if((calcMethod.equals("Percentage") && (!currentRowCalcMethod.equals("Percentage") && !currentRowCalcMethod.equals("0")))
								|| (calcMethod.equals("0") && (!currentRowCalcMethod.equals("Percentage") && !currentRowCalcMethod.equals("0")))
								|| (calcMethod.equals("Flat") && (!currentRowCalcMethod.equals("Flat") && !currentRowCalcMethod.equals("1"))) 
								|| (calcMethod.equals("1") && (!currentRowCalcMethod.equals("Flat") && !currentRowCalcMethod.equals("1"))) 
								){
							SC.say("All of the calculation methods must be either Percentage or Flat!");
							bValidationFailed = true;
							break;							
						}
					}
					
					/*
					 * Next check that the percentages sum to 100 or less for the shares
					 */
					if(calcMethod.equals("Percentage") || calcMethod.equals("0")){
						calcValueTotal = calcValueTotal + new Double(promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE));
						if(calcValueTotal > 100){
							SC.say("The values for a percentage based calculation method must sum to 100 or less!");
							bValidationFailed = true;
							break;
						}
					}
				}			
				
				/*
				 * If all of the validation has passed then go ahead and save the records
				 */
				if(!bValidationFailed){
					for (int i=0;i<promotionShareRecords.length;i++) {
						promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID, 
								promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID));
						promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID, 
								promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID));
						promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME, 
								promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME));
						promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID, 
								promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID));
						promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC, 
								promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC));
						promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE, 
								promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE));
						promotionShareDetailMap.put("PROMOTIONSHARE" + i, Util.formXMLfromHashMap(promotionShareDetailRowMap));
					}

					promotionShareDataMap.put("PROMOTIONSHARE", Util.formXMLfromHashMap(promotionShareDetailMap));

					((PromotionView) view).getPromotionUiHandlers().onSavePromotionShareClicked(promotionShareDataMap);
				}
			}
		});
		
		DSCallback promotionShareEntryDsCallBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				promotionShareDetailListGrid.setData(response.getData());
			}
		};
		
		if (record!=null) {
			promotionShareDetailDataSource.fetchData(promotionShareDetailListGrid.getFilterEditorCriteria(), promotionShareEntryDsCallBack);
		}
		
		promotionShareDetailVLayout.setMembers(promotionShareDetailToolStrip, promotionShareDetailListGrid);

	}
	
	/**
	 * Removes the selected promotion share detail rows 
	 */
	private void removeSelectedPromotionShareDetails(){
		/*
		 * A list of selected records from the ListGrid
		 */
		ListGridRecord[] promotionShareRecords = promotionShareDetailListGrid.getSelection();
		
		/*
		 * This is the map of the data for the whole ListGrid
		 */
		HashMap<String, String> promotionShareDetailMap = new HashMap<String, String>();
		
		/*
		 * This map is used to pass each row of data in the ListGrid
		 */
		HashMap<String, String> promotionShareDetailRowMap = new HashMap<String, String>();
		
		/*
		 * This loop processes each of the selected records to be deleted adding them
		 * to the map to be passed to the back end. Only the primary key is needed for delete
		 */
		for (int i=0;i<promotionShareRecords.length;i++) {
			promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID, 
					promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID));
			promotionShareDetailRowMap.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID, 
					promotionShareRecords[i].getAttributeAsString(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID));
			promotionShareDetailMap.put("DELETEDPROMOTIONSHARE" + i, Util.formXMLfromHashMap(promotionShareDetailRowMap));				
			}
		
		((PromotionView) view).getPromotionUiHandlers().onDeletePromotionShareClicked(promotionShareDetailMap);
		promotionShareDetailListGrid.removeSelectedData();
		SC.say("Data Removed");

	}
	
	/**
	 * This is the window that will be used to select a party
	 * 
	 * @param record
	 *            the record selected in the last grid
	 * @return returns the new party selection window
	 */
	private Window buildPromotionSelectPartyWindow(final ListGridRecord record) {
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

		VLayout promotionSelectPartyLayout = new VLayout();
		promotionSelectPartyLayout.setHeight100();
		
		promotionSelectPartyListGrid = new ListGrid();
		promotionSelectPartyListGrid.setHeight(300);
		promotionSelectPartyListGrid.setSelectionType(SelectionStyle.SIMPLE);
		promotionSelectPartyListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		promotionSelectPartyListGrid.setShowFilterEditor(true);
		promotionSelectPartyListGrid.setAutoFetchData(false);
		
		promotionSelectPartyListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshPromotionPartyData();
			}
		});
		
		/*
		 * Load Party data from VenParty
		 */

		DataSource promotionPartyDataSource = PromotionData.getPromotionPartyData();
		promotionSelectPartyListGrid.setDataSource(promotionPartyDataSource);
		promotionSelectPartyListGrid.setFields(Util.getListGridFieldsFromDataSource(promotionPartyDataSource));
		promotionSelectPartyListGrid.getField(DataNameTokens.VENPARTY_PARTYID).setHidden(true);
				
		HLayout promotionSelectPartyButtons = new HLayout(5);

		final IButton buttonSelectParty = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");

		buttonSelectParty.setDisabled(true);
		buttonSelectParty.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String partyId = promotionSelectPartyListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID);
				String partyName = promotionSelectPartyListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_FULLORLEGALNAME);
				
				record.setAttribute(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID, partyId);
				record.setAttribute(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME, partyName);
				
				promotionShareDetailListGrid.redraw();
				selectPartyWindow.destroy();
			}
		});
		//Set click handler for Cancel Party Button
		buttonCancel.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				selectPartyWindow.destroy();
			}
		});
		promotionSelectPartyButtons.setAlign(Alignment.CENTER);
		promotionSelectPartyButtons.setMembers(buttonSelectParty, buttonCancel);
		
		promotionSelectPartyLayout.setMembers(promotionSelectPartyListGrid,promotionSelectPartyButtons );
		selectPartyWindow.addItem(promotionSelectPartyLayout);
		
		promotionSelectPartyListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectParty.setDisabled(promotionSelectPartyListGrid.getSelection().length!=1);
			}
		});

		return selectPartyWindow;
	}
	
	/**
	 * Refresh Party List on Party Window
	 */
	private void refreshPromotionPartyData() {
		DSCallback callBack = new DSCallback() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				promotionSelectPartyListGrid.setData(response.getData());
			}
		};

		promotionSelectPartyListGrid.getDataSource().fetchData(promotionSelectPartyListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Returns the selected promotion list grid records
	 * @return the selected records
	 */
	public ListGridRecord[] getSelectedPromotions() {
		return promotionListGrid.getSelection();
	}
	
	/**
	 * Returns the selected promotion share detail records
	 * @return the selected records
	 */
	public ListGridRecord[] getSelectedPromotionShareDetails() {
		return promotionShareDetailListGrid.getSelection();
	}

	/**
	 * Returns the promotions list grid object
	 * @return the promotionListGrid
	 */
	public ListGrid getPromotionList() {
		return promotionListGrid;
	}

	/**
	 * Returns the promotion share detail list grid object
	 * @return the promotionShareDetailListGrid
	 */
	public ListGrid getPromotionShareDetailList() {
		return promotionShareDetailListGrid;
	}
}
