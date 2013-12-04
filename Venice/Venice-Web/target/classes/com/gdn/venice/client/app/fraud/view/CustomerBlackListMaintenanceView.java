package com.gdn.venice.client.app.fraud.view;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.CustomerBlackListMaintenancePresenter;
import com.gdn.venice.client.app.fraud.view.handlers.CustomerBlackListMaintenanceUiHandlers;
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
import com.smartgwt.client.types.TextMatchStyle;
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
 * View for Customer Blacklist Maintenance
 * 
 * @author Roland
 */
public class CustomerBlackListMaintenanceView extends
		ViewWithUiHandlers<CustomerBlackListMaintenanceUiHandlers> implements
		CustomerBlackListMaintenancePresenter.MyView {

	RafViewLayout customerBlackListMaitenanceLayout;
	ListGrid customerBlackListListGrid = new ListGrid();

	@Inject
	public CustomerBlackListMaintenanceView() {
		customerBlackListMaitenanceLayout = new RafViewLayout();
		ToolStrip customerBlackListMaintenanceToolStrip = new ToolStrip();
		customerBlackListMaintenanceToolStrip.setWidth100();

		ToolStripButton addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add to Customer Black List");
		addButton.setTitle("Add");

		ToolStripButton removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/remove.png");
		removeButton.setTooltip("Remove from Customer Black List");
		removeButton.setTitle("Remove");

		customerBlackListMaintenanceToolStrip.addButton(addButton);
		customerBlackListMaintenanceToolStrip.addButton(removeButton);
				
		customerBlackListListGrid.setAutoFetchData(false);
		customerBlackListListGrid.setCanEdit(true);
		customerBlackListListGrid.setCanResizeFields(true);
		customerBlackListListGrid.setShowFilterEditor(true);
		customerBlackListListGrid.setCanSort(true);
		customerBlackListListGrid.setSelectionType(SelectionStyle.SIMPLE);
		customerBlackListListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		customerBlackListListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		customerBlackListListGrid.setShowRowNumbers(true);
		customerBlackListMaitenanceLayout.setMembers(customerBlackListMaintenanceToolStrip);			
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				customerBlackListListGrid.startEditingNew();
			}
		});		

		customerBlackListListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				customerBlackListListGrid.saveAllEdits();
				refreshCustomerBlackListData();
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
							customerBlackListListGrid.removeSelectedData();
							refreshCustomerBlackListData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
	}

	@Override
	public Widget asWidget() {
		return customerBlackListMaitenanceLayout;
	}

	protected void bindCustomUiHandlers() {
		customerBlackListListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshCustomerBlackListData();
			}
		});
	}

	@Override
	public void loadCustomerBlackListData(DataSource dataSource) {						
		//populate listgrid
		customerBlackListListGrid.setDataSource(dataSource);
		customerBlackListListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		customerBlackListListGrid.setSortField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID).setCanEdit(false);		
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID).setHidden(true);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_BLACKLISTTIMESTAMP).setCanEdit(false);	
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CREATEDBY).setCanEdit(false);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_BLACKLISTTIMESTAMP).setDefaultValue("2011-01-01T00:00:00");
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERBLACKLISTID).setWidth(75);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME).setWidth(150);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_ADDRESS).setWidth(250);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_PHONENUMBER).setWidth(120);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_EMAIL).setWidth(150);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_DESCRIPTION).setWidth(200);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CREATEDBY).setWidth(100);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_HANDPHONENUMBER).setWidth(120);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGPHONENUMBER).setWidth(120);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGHANDPHONENUMBER).setWidth(120);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_SHIPPINGADDRESS).setWidth(120);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CCNUMBER).setWidth(120);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_ORDERTIMESTAMP).setDefaultValue("2011-01-01T00:00:00");
		
 		
		//validation
		//required
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_DESCRIPTION).setRequired(true);
  
		//length
		LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();  
		lengthRangeValidator.setMax(50);  
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_PHONENUMBER).setValidators(lengthRangeValidator);		
		
		LengthRangeValidator lengthRangeValidator2 = new LengthRangeValidator();  
		lengthRangeValidator2.setMax(100);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_CUSTOMERFULLNAME).setValidators(lengthRangeValidator2);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_EMAIL).setValidators(lengthRangeValidator);
		
		LengthRangeValidator lengthRangeValidator3 = new LengthRangeValidator();  
		lengthRangeValidator3.setMax(1000);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_ADDRESS).setValidators(lengthRangeValidator3);
		customerBlackListListGrid.getField(DataNameTokens.FRDCUSTOMERBLACKLIST_DESCRIPTION).setValidators(lengthRangeValidator3);
		
        customerBlackListMaitenanceLayout.addMember(customerBlackListListGrid);
        bindCustomUiHandlers();
	}
	
	public void refreshCustomerBlackListData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				customerBlackListListGrid.setData(response.getData());
			}
		};
		
		customerBlackListListGrid.getDataSource().fetchData(customerBlackListListGrid.getFilterEditorCriteria(), callBack);
	}	
}
