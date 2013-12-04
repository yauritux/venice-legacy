package com.gdn.venice.client.app.fraud.view;

import java.util.Date;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.FraudCustomerWhitelistPresenter;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCustomerWhitelistMaintenanceUiHandlers;
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
 * View for Customer White list Maintenance
 * 
 * @author Arifin
 */
public class FraudCustomerWhitelistView extends
		ViewWithUiHandlers<FraudCustomerWhitelistMaintenanceUiHandlers> implements
		FraudCustomerWhitelistPresenter.MyView {

	RafViewLayout customerWhiteListMaitenanceLayout;
	ListGrid customerWhiteListListGrid = new ListGrid();

	@Inject
	public FraudCustomerWhitelistView() {
		customerWhiteListMaitenanceLayout = new RafViewLayout();
		ToolStrip customerWhiteListMaintenanceToolStrip = new ToolStrip();
		customerWhiteListMaintenanceToolStrip.setWidth100();

		ToolStripButton addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add to Customer White List");
		addButton.setTitle("Add");

		ToolStripButton removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/remove.png");
		removeButton.setTooltip("Remove from Customer White List");
		removeButton.setTitle("Remove");

		customerWhiteListMaintenanceToolStrip.addButton(addButton);
		customerWhiteListMaintenanceToolStrip.addButton(removeButton);
				
		customerWhiteListListGrid.setAutoFetchData(false);
		customerWhiteListListGrid.setCanEdit(true);
		customerWhiteListListGrid.setCanResizeFields(true);
		customerWhiteListListGrid.setShowFilterEditor(true);
		customerWhiteListListGrid.setCanSort(true);
		customerWhiteListListGrid.setSelectionType(SelectionStyle.SIMPLE);
		customerWhiteListListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		customerWhiteListListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		customerWhiteListListGrid.setShowRowNumbers(true);
		customerWhiteListMaitenanceLayout.setMembers(customerWhiteListMaintenanceToolStrip);			
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				customerWhiteListListGrid.startEditingNew();
			}
		});		

		customerWhiteListListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				customerWhiteListListGrid.saveAllEdits();
				refreshCustomerWhiteListData();
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
							customerWhiteListListGrid.removeSelectedData();
							refreshCustomerWhiteListData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
	}

	@Override
	public Widget asWidget() {
		return customerWhiteListMaitenanceLayout;
	}

	protected void bindCustomUiHandlers() {
		customerWhiteListListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshCustomerWhiteListData();
			}
		});
	}

	public void loadCustomerWhiteListData(DataSource dataSource) {				

		//populate listgrid
		customerWhiteListListGrid.setDataSource(dataSource);
		customerWhiteListListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));		
		
		customerWhiteListListGrid.setSortField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID).setCanEdit(false);		
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERWHITELISTID).setHidden(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERID).setCanEdit(true);		
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERID).setWidth("70px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP).setCanEdit(true);	
		Date date = new Date();
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP).setDefaultValue(date);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP).setWidth("100px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME).setWidth("150px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS).setWidth("250px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL).setWidth("150px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CREDITCARDNUMBER).setWidth("150px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ISSUERBANK).setWidth("70px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ECI).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ECI).setWidth("50px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_EXPIREDDATE).setWidth("100px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE).setWidth("100px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_REMARK).setCanEdit(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_REMARK).setWidth("250px");
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CREATEDBY).setCanEdit(false);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CREATED).setCanEdit(false);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CREATEDBY).setHidden(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CREATED).setHidden(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE).setDefaultValue(date);
		
 		
		//validation
		//required
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_ORDERTIMESTAMP).setRequired(true);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_GENUINEDATE).setRequired(true);
  
		
		LengthRangeValidator lengthRangeValidator2 = new LengthRangeValidator();  
		lengthRangeValidator2.setMax(100);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_CUSTOMERFULLNAME).setValidators(lengthRangeValidator2);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_EMAIL).setValidators(lengthRangeValidator2);
		
		LengthRangeValidator lengthRangeValidator3 = new LengthRangeValidator();  
		lengthRangeValidator3.setMax(1000);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_REMARK).setValidators(lengthRangeValidator3);
		customerWhiteListListGrid.getField(DataNameTokens.FRDCUSTOMERWHITELIST_SHIPPINGADDRESS).setValidators(lengthRangeValidator3);
		
        customerWhiteListMaitenanceLayout.addMember(customerWhiteListListGrid);
        bindCustomUiHandlers();
	}
	
	public void refreshCustomerWhiteListData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				customerWhiteListListGrid.setData(response.getData());
			}
		};
		
		customerWhiteListListGrid.getDataSource().fetchData(customerWhiteListListGrid.getFilterEditorCriteria(), callBack);
	}	
}
