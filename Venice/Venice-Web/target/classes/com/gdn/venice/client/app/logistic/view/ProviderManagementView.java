package com.gdn.venice.client.app.logistic.view;

import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.data.ProviderData;
import com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter;
import com.gdn.venice.client.app.logistic.view.handlers.ProviderManagementUiHandlers;
import com.gdn.venice.client.app.logistic.widgets.ProviderAddressTab;
import com.gdn.venice.client.app.logistic.widgets.ProviderAgreementTab;
import com.gdn.venice.client.app.logistic.widgets.ProviderContactTab;
import com.gdn.venice.client.app.logistic.widgets.ProviderManagementVLayoutWidget;
import com.gdn.venice.client.app.logistic.widgets.ProviderServiceTab;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
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
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
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
 * View for Logistics Provider Management
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderManagementView extends
		ViewWithUiHandlers<ProviderManagementUiHandlers> implements
		ProviderManagementPresenter.MyView {
	private static final int LIST_HEIGHT = 120;

	RafViewLayout providerManagementLayout;
	ProviderManagementVLayoutWidget providerVLayoutWidget;
	
	/*
	 * A VLayout that is used for the provider management
	 */
	VLayout providerManagementVLayout;
	VLayout providerManagementDetailVLayout;
	VLayout providerFormVLayout;
	VLayout providerTabVLayout;
	HLayout providerTabHLayout;
	
	/*
	 * The toolstrip objects for the header and the detail
	 */
	ToolStrip providerManagementToolStrip;
	
	/*
	 * The add and delete buttons for the provider management header
	 */
	ToolStripButton addButton;
	ToolStripButton deleteButton;

	ListGrid providerListGrid;
	
	DynamicForm logProviderForm;
	ToolStrip providerFormToolStrip ;
	
	ProviderAddressTab addressTab;
	ProviderContactTab contactTab;
	ProviderServiceTab serviceTab;
	ProviderAgreementTab agreementTab;

	/**
	 * Build the Constructor for Provider Management View and inject it.
	 */
	@Inject
	public ProviderManagementView() {
		providerManagementLayout = new RafViewLayout();
		
		providerManagementToolStrip = new ToolStrip();
		providerManagementToolStrip.setWidth100();
		providerManagementToolStrip.setPadding(2);

		addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add New Provider");
		addButton.setTitle("Add");
		
		deleteButton = new ToolStripButton();
		deleteButton.setIcon("[SKIN]/icons/delete.png");
		deleteButton.setTooltip("Remove Provider");
		deleteButton.setTitle("Remove");
		
		providerManagementToolStrip.addButton(addButton);
		providerManagementToolStrip.addButton(deleteButton);

		providerManagementVLayout = new VLayout();
		providerManagementVLayout.setHeight(LIST_HEIGHT);
		providerManagementVLayout.setShowResizeBar(true);
		
		providerManagementDetailVLayout = new VLayout();

		providerManagementLayout.setMembers(providerManagementToolStrip, providerManagementVLayout, providerManagementDetailVLayout);
		
		buildLogisticsProviderListGrid();
		bindCustomUiHandlers();
	}

	/**
	 * Method to build the list grid for providers
	 * @return the list grid
	 */
	public ListGrid buildLogisticsProviderListGrid() {
		providerListGrid = new ListGrid();
		
		providerListGrid.setWidth100();
		providerListGrid.setHeight100();
		providerListGrid.setAutoFetchData(true);
		providerListGrid.setShowRowNumbers(true);
		providerListGrid.setCanResizeFields(true);
		providerListGrid.setShowFilterEditor(true);
		providerListGrid.setShowAllRecords(true);
		providerListGrid.setSortField(0);
		providerListGrid.setSelectionType(SelectionStyle.SIMPLE);
		providerListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);	
		providerListGrid.setCanSort(true);
		providerListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		providerListGrid.setCanResizeFields(true);	
		providerListGrid.setShowFilterEditor(true);
		providerListGrid.setFilterOnKeypress(false);
		
		providerListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
					 */
					@Override
					public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
						refreshProviderData();
					}
				});
		return providerListGrid;
	}

	@Override
	public Widget asWidget() {
		return providerManagementLayout;
	}
	
	/**
	 * Loads the list of provider data (top table in the screen)
	 * 
	 * @param dataSource
	 *            provider management datasource
	 */
	@Override
	public void loadLogisticsProviderData(final DataSource dataSource) {
		providerListGrid.setDataSource(dataSource);
		providerListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setHidden(true);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID).setWidth("5%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID).setHidden(true);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID).setWidth("5%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME).setHidden(true);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME).setWidth("5%");	
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEID).setHidden(true);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEID).setWidth("5%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setHidden(true);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC).setWidth("5%");	
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE).setWidth("10%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE).setHidden(false);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEID).setWidth("2%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEID).setHidden(true);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC).setWidth("5%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC).setHidden(false);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEID).setWidth("2%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEID).setHidden(true);
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC).setWidth("5%");
		providerListGrid.getField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC).setHidden(false);
		providerManagementVLayout.addMember(providerListGrid);
	}
	
	/**
	 * Method to bind the custom UI handlers to the view
	 */
	public void bindCustomUiHandlers() {
		providerListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
			 */
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = providerListGrid.getSelection();
				
				if (selectedRecords.length==1) {
					showProviderDetail(selectedRecords[0]);
				} else {
					HTMLFlow providerDetailFlow = new HTMLFlow();
					providerDetailFlow.setAlign(Alignment.CENTER);
					providerDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						providerDetailFlow.setContents("<h2 align=\"center\">Please select a provider to show the provider detail</h2>");
					} else if (selectedRecords.length>1) {
						providerDetailFlow.setContents("<h2 align=\"center\">More than one provider selected, please select only one provider to show the provider detail</h2>");
					}
					providerManagementDetailVLayout.setMembers(providerDetailFlow);
				}
			}
		});
		
		//Set Add Button click handler
		addButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				showProviderDetail(null);		
			}
		});
		
		//Set Delete Button click handler
		deleteButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {				
				if(providerListGrid.getSelection().length > 0){
					SC.ask("Remove Selected Rows", "Are you sure?", new BooleanCallback(){	
						@Override
						public void execute(Boolean value) {
							if(value){
								removeSelectedProvider();
							}
						}
					});
				}
			}	
		});
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter.MyView#showProviderDetail(com.smartgwt.client.widgets.grid.ListGridRecord)
	 */
	@Override
	public void showProviderDetail(final ListGridRecord record) {
		if (record!=null) {
			providerFormToolStrip = new ToolStrip();
			providerFormToolStrip.setWidth100();
			providerFormToolStrip.setPadding(3);

			ToolStripButton saveButton = new ToolStripButton();  
			saveButton.setIcon("[SKIN]/icons/save.png");  
			saveButton.setTooltip("Save Logistics Provider Detail");
			saveButton.setTitle("Save");
		
			providerFormToolStrip.addButton(saveButton);
		
			logProviderForm = new DynamicForm();
			logProviderForm.setWidth100();

			final StaticTextItem providerId = new StaticTextItem();
			providerId.setTitle("Provider ID");
			providerId.setValue(record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
		
			final StaticTextItem logPartyId = new StaticTextItem();
			logPartyId.setTitle("Party ID ");
			logPartyId.setValue(record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID));
		
			final SelectItem partyType = new SelectItem();
			partyType.setTitle("Party Type");
			partyType.setDisplayField(DataNameTokens.VENPARTYTYPE_PARTYTYPEDESC);
			partyType.setValueField(DataNameTokens.VENPARTYTYPE_PARTYTYPEID);
			partyType.setValue(record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC));
			
			final SelectItem activityReportTemplate = new SelectItem();
			activityReportTemplate.setValue(record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC));
			activityReportTemplate.setTitle("Report Template");
			activityReportTemplate.setOptionDataSource(ProviderData.getTemplateData());
			activityReportTemplate.setDisplayField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEDESC);
			activityReportTemplate.setValueField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEID);
			
			final SelectItem invoiceReportTemplate = new SelectItem();
			invoiceReportTemplate.setValue(record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC));
			invoiceReportTemplate.setTitle("Invoice Template");
			invoiceReportTemplate.setOptionDataSource(ProviderData.getTemplateData());
			invoiceReportTemplate.setDisplayField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEDESC);
			invoiceReportTemplate.setValueField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEID);
			
			final TextItem providerCode = new TextItem();
			providerCode.setTitle("Provider Code");
			providerCode.setValue(record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE));
	
			logProviderForm.setItems(providerId,logPartyId, providerCode,  activityReportTemplate,  invoiceReportTemplate );
		
			//Set Save Button click handler
			saveButton.addClickHandler(new ClickHandler() {	
				/* (non-Javadoc)
			 	* @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 	*/
				@Override
				public void onClick(ClickEvent event) {			
					HashMap<String, String> logProviderDataMap = new HashMap<String, String>();				
					HashMap<String, String> providerDataMap = new HashMap<String, String>();
				
					logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, (String) providerId.getValue());
					logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID,(String) logPartyId.getValue());
					logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME,(String) providerCode.getValue());
					logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC,(String) partyType.getValue().toString());
					logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, (String) providerCode.getValue());
					logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC, (String) activityReportTemplate.getValue().toString());
					logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC, (String) invoiceReportTemplate.getValue().toString());
		
					providerDataMap.put("LOGPROVIDER", Util.formXMLfromHashMap(logProviderDataMap));
					getProviderManagementUiHandlers().onSaveProviderClicked(providerDataMap);
				}
			});
		
			String partyId = record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID);
			String logProviderId = record.getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID);
			List<DataSource> dataSources = getUiHandlers().onShowProviderDetailData(partyId, logProviderId);
			providerVLayoutWidget = new ProviderManagementVLayoutWidget(dataSources.get(0), dataSources.get(1), dataSources.get(2),  dataSources.get(3), dataSources.get(4),record,  this);
			}else{
				providerFormToolStrip = new ToolStrip();
				providerFormToolStrip.setWidth100();
				providerFormToolStrip.setPadding(3);

				ToolStripButton saveButton = new ToolStripButton();  
				saveButton.setIcon("[SKIN]/icons/save.png");  
				saveButton.setTooltip("Save Current Logistics Provider Detail");
			
				providerFormToolStrip.addButton(saveButton);
			
				logProviderForm = new DynamicForm();
				logProviderForm.setWidth100();

				final StaticTextItem providerId = new StaticTextItem();
				providerId.setTitle("Provider ID");
			
				final StaticTextItem logPartyId = new StaticTextItem();
				logPartyId.setTitle("Party ID ");
		
				final SelectItem partyType = new SelectItem();
				partyType.setTitle("Party Type");
				partyType.setValue(2);
				
				final SelectItem activityReportTemplate = new SelectItem();
				activityReportTemplate.setTitle("Report Template");
				activityReportTemplate.setOptionDataSource(ProviderData.getTemplateData());
				activityReportTemplate.setDisplayField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEDESC);
				activityReportTemplate.setValueField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEID);
				activityReportTemplate.setRequired(true);
	
				final SelectItem invoiceReportTemplate = new SelectItem();
				invoiceReportTemplate.setTitle("Invoice Template");
				invoiceReportTemplate.setOptionDataSource(ProviderData.getTemplateData());
				invoiceReportTemplate.setDisplayField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEDESC);
				invoiceReportTemplate.setValueField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEID);
				invoiceReportTemplate.setRequired(true);
				
				final TextItem providerCode = new TextItem();
				providerCode.setTitle("Provider Code");
				providerCode.setRequired(true);
			
				logProviderForm.setItems(providerId,logPartyId,  providerCode, activityReportTemplate,  invoiceReportTemplate );
			
				saveButton.addClickHandler(new ClickHandler() {				
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
					 */
					@Override
					public void onClick(ClickEvent event) {					
						HashMap<String, String> logProviderDataMap = new HashMap<String, String>();					
						HashMap<String, String> providerDataMap = new HashMap<String, String>();

						logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, (String) providerId.getValue());
						logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID,(String) logPartyId.getValue());
						logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME,(String) providerCode.getValue());
						logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC,partyType.getValue().toString());
						logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, (String) providerCode.getValue() != null ? (String) providerCode.getValue():"");
						logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC, (String) activityReportTemplate.getValue().toString() != null ? (String) activityReportTemplate.getValue().toString() :"Missing Field");
						logProviderDataMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC, (String) invoiceReportTemplate.getValue().toString() != null ? (String) invoiceReportTemplate.getValue().toString() :" Missing Field");
			
						providerDataMap.put("LOGPROVIDER", Util.formXMLfromHashMap(logProviderDataMap));
						getProviderManagementUiHandlers().onSaveProviderClicked(providerDataMap);
					}
				});
			
				List<DataSource> dataSources = getUiHandlers().onShowProviderDetailData(null, null);			
				providerVLayoutWidget = new ProviderManagementVLayoutWidget(dataSources.get(0), dataSources.get(1), dataSources.get(2),  dataSources.get(3), dataSources.get(4),record,  this);
			}
				
		providerFormVLayout = new VLayout();
		providerFormVLayout.setMembers(providerFormToolStrip, logProviderForm);
		providerFormVLayout.setWidth("25%");
		providerFormVLayout.setShowResizeBar(true);
		
		providerTabVLayout = new VLayout();		
		providerTabVLayout.setMembers(providerVLayoutWidget);
		
		HLayout providerDetailHLayout = new HLayout();
		if (record !=null){
			providerDetailHLayout.setMembers(providerFormVLayout, providerTabVLayout);
		}else{
			providerFormVLayout.setWidth("25%");
			HTMLFlow detailFlow = new HTMLFlow();
			detailFlow.setAlign(Alignment.CENTER);
			detailFlow.setWidth("75%");
			detailFlow.setContents("<h2 align=\"center\">Please fill completely the form first, to load the details</h2>");
			
			providerDetailHLayout.setMembers(providerFormVLayout, detailFlow);
		}						
		providerManagementDetailVLayout.setMembers(providerDetailHLayout);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter.MyView#refreshAddressData()
	 */
	@Override
	public void refreshAddressData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				addressTab.getProviderAddressDetailListGrid().setData(response.getData());
				addressTab.getProviderAddressDetailListGrid().setGroupStartOpen(GroupStartOpen.ALL);
			}
		};		
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter.MyView#refreshContactData()
	 */
	@Override
	public void refreshContactData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				contactTab.getProviderContactDetailListGrid().setData(response.getData());
				contactTab.getProviderContactDetailListGrid().setGroupStartOpen(GroupStartOpen.ALL);
			}
		};	
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter.MyView#refreshServiceData()
	 */
	@Override
	public void refreshServiceData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				serviceTab.getProviderServiceListGrid().setData(response.getData());
				serviceTab.getProviderServiceListGrid().setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter.MyView#refreshAgreementData()
	 */
	@Override
	public void refreshAgreementData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				agreementTab.getProviderAgreementListGrid().setData(response.getData());
				agreementTab.getProviderAgreementListGrid().setGroupStartOpen(GroupStartOpen.ALL);
			}
		};	
	}
	/**
	 * @return the ProviderManagementUiHandlers
	 */
	public ProviderManagementUiHandlers getProviderManagementUiHandlers() {
		return getUiHandlers();
	}

	/**
	 * @return the providerListGrid
	 */
	public ListGrid getProviderListGrid() {
		return providerListGrid;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter.MyView#refreshProviderData()
	 */
	@Override
	public void refreshProviderData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				providerListGrid.setData(response.getData());
				providerListGrid.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
		providerListGrid.getDataSource().fetchData(providerListGrid.getFilterEditorCriteria(), callBack);
	}

	/**
	 * Returns the provider that have been selected in the list grid
	 * @return the provider list grid records
	 */
	public ListGridRecord[] getSelectedProviders() {
		return providerListGrid.getSelection();
	}

	/**
	 * Removes the selected logistic provider rows 
	 */
	public void removeSelectedProvider(){		
		/*
		 * A list of selected records from the ListGrid
		 */
		ListGridRecord[] providerRecords = providerListGrid.getSelection();
		
		/*
		 * This is the map of the data for the whole ListGrid
		 */
		HashMap<String, String> providerDataMap = new HashMap<String, String>();
		
		/*
		 * This map is used to pass each row of data in the ListGrid
		 */
		HashMap<String, String> providerRowMap = new HashMap<String, String>();
		
		/*
		 * This loop processes each of the selected records to be deleted adding them
		 * to the map to be passed to the back end. Only the primary key is needed for delete
		 */
		for (int i=0;i<providerRecords.length;i++) {
			providerRowMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, providerRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
			providerRowMap.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID, providerRecords[i].getAttributeAsString(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID));
			providerDataMap.put("DELETEDLOGPROVIDERID" + i, Util.formXMLfromHashMap(providerRowMap));				
			}
		getProviderManagementUiHandlers().onDeleteProviderClicked(providerDataMap);
		providerListGrid.removeSelectedData();
		SC.say("Data Removed");
	}
}
