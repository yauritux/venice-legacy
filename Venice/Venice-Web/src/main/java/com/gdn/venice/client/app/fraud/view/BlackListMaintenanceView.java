package com.gdn.venice.client.app.fraud.view;

import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.BlackListMaintenancePresenter;
import com.gdn.venice.client.app.fraud.view.handlers.BlackListMaintenanceUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Blacklist Maintenance
 * 
 * @author Roland
 */
public class BlackListMaintenanceView extends
		ViewWithUiHandlers<BlackListMaintenanceUiHandlers> implements
		BlackListMaintenancePresenter.MyView {

	RafViewLayout blackListMaitenanceLayout;
	ListGrid blackListListGrid = new ListGrid();

	@Inject
	public BlackListMaintenanceView() {
		blackListMaitenanceLayout = new RafViewLayout();
		ToolStrip blackListMaintenanceToolStrip = new ToolStrip();
		blackListMaintenanceToolStrip.setWidth100();

		ToolStripButton addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add to IP White List / Black List");
		addButton.setTitle("Add");

		ToolStripButton removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/remove.png");
		removeButton.setTooltip("Remove from IP White List / Black List");
		removeButton.setTitle("Remove");

		blackListMaintenanceToolStrip.addButton(addButton);
		blackListMaintenanceToolStrip.addButton(removeButton);
				
		blackListListGrid.setAutoFetchData(false);
		blackListListGrid.setCanEdit(true);
		blackListListGrid.setCanResizeFields(true);
		blackListListGrid.setShowFilterEditor(true);
		blackListListGrid.setCanSort(true);
		blackListListGrid.setSelectionType(SelectionStyle.SIMPLE);
		blackListListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		blackListListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		blackListListGrid.setShowRowNumbers(true);
		blackListMaitenanceLayout.setMembers(blackListMaintenanceToolStrip);			
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				blackListListGrid.startEditingNew();
			}
		});		

		blackListListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				blackListListGrid.saveAllEdits();
				refreshBlackListData();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}				
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){							
							blackListListGrid.removeSelectedData();
							refreshBlackListData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
	}

	@Override
	public Widget asWidget() {
		return blackListMaitenanceLayout;
	}

	protected void bindCustomUiHandlers() {
		blackListListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshBlackListData();
			}
		});
	}

	@Override
	public void loadBlackListData(DataSource dataSource) {				
		LinkedHashMap<String, String> blackOrWhiteListMap = new LinkedHashMap<String, String>();  
		blackOrWhiteListMap.put("BLACKLIST", "Black List");
		blackOrWhiteListMap.put("WHITELIST", "White List");
		dataSource.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKORWHITELIST).setValueMap(blackOrWhiteListMap);
		
		//populate listgrid
		blackListListGrid.setDataSource(dataSource);
		blackListListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		blackListListGrid.setSortField(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID).setCanEdit(false);		
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID).setHidden(true);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP).setCanEdit(false);	
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_CREATEDBY).setCanEdit(false);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP).setDefaultValue("2011-01-01T00:00:00");
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID).setWidth(75);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_DESCRIPTION).setWidth(250);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_CREATEDBY).setWidth(100);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKORWHITELIST).setWidth(150);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP).setWidth(120);
//		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
 		
		//validation
		//required
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTSTRING).setRequired(true);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKORWHITELIST).setRequired(true);
  
		//length
		LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();  
		lengthRangeValidator.setMax(200);  
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTSTRING).setValidators(lengthRangeValidator);
		
		LengthRangeValidator lengthRangeValidator2 = new LengthRangeValidator();  
		lengthRangeValidator2.setMax(1000);
		blackListListGrid.getField(DataNameTokens.FRDENTITYBLACKLIST_DESCRIPTION).setValidators(lengthRangeValidator2);
		
        blackListMaitenanceLayout.addMember(blackListListGrid);
        bindCustomUiHandlers();
	}
	
	public void refreshBlackListData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				blackListListGrid.setData(response.getData());
			}
		};
		
		blackListListGrid.getDataSource().fetchData(blackListListGrid.getFilterEditorCriteria(), callBack);
	}	
}
