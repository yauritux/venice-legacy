package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class OrderDataViewerCustomerTab extends Tab {
	DynamicForm orderCustomerForm;
	ListGrid orderCustomerAddressListGrid = null;
	ListGrid orderCustomerContactListGrid = null;
	
	public OrderDataViewerCustomerTab(String title, DataSource customerData, DataSource customerAddressData, DataSource customerContactData) {
		super(title);
		
		VLayout orderCustomerLayout = new VLayout();
		
		orderCustomerForm = new DynamicForm();
		orderCustomerForm.setDataSource(customerData);  
		orderCustomerForm.setUseAllDataSourceFields(true);  
		
		orderCustomerForm.setNumCols(4);
		
		orderCustomerForm.setFields(Util.getReadOnlyFormItemFromDataSource(customerData));
		orderCustomerForm.getField(DataNameTokens.VENCUSTOMER_DATEOFBIRTH).setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
		
		orderCustomerForm.getField(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG).setValueFormatter(new FormItemValueFormatter() {			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (form.getField(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG).equals("true")) 
                	{
                		return "Yes";
                	} else {
                		return "No";
                	}
			}
		});
		
		HLayout orderCustomerAddressContactLayout  = new HLayout();
		
		Label orderCustomerAddressLabel = new Label("<b>Address:</b>");
		orderCustomerAddressLabel.setHeight(10);
		
		orderCustomerAddressListGrid = new ListGrid();
		
		orderCustomerAddressListGrid.setWidth100();
		orderCustomerAddressListGrid.setHeight100();
		orderCustomerAddressListGrid.setShowAllRecords(true);
		orderCustomerAddressListGrid.setSortField(0);

		orderCustomerAddressListGrid.setCanResizeFields(true);
		orderCustomerAddressListGrid.setShowRowNumbers(true);
		orderCustomerAddressListGrid.setAutoFetchData(true);
	
		orderCustomerAddressListGrid.setShowFilterEditor(false);	
		
		orderCustomerAddressListGrid.setDataSource(customerAddressData);
		orderCustomerAddressListGrid.setFields(Util.getListGridFieldsFromDataSource(customerAddressData));
		
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setWidth(100);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setHidden(true);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1).setWidth(150);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2).setWidth(150);		
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN).setWidth(100);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN).setWidth(100);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME).setWidth(100);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setWidth(100);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE).setWidth(75);
		orderCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setWidth(120);
		
		VLayout orderCustomerAddressLayout = new VLayout();
		orderCustomerAddressLayout.setMembers(orderCustomerAddressLabel, orderCustomerAddressListGrid);
		
		Label orderCustomerContactLabel = new Label("<b>Contact:</b>");
		orderCustomerContactLabel.setHeight(10);
		
		orderCustomerContactListGrid = new ListGrid();
		
		orderCustomerContactListGrid.setWidth100();
		orderCustomerContactListGrid.setHeight100();
		orderCustomerContactListGrid.setShowAllRecords(true);
		orderCustomerContactListGrid.setSortField(0);

		orderCustomerContactListGrid.setCanResizeFields(true);
		orderCustomerContactListGrid.setShowRowNumbers(true);
		orderCustomerContactListGrid.setAutoFetchData(true);
	
		orderCustomerContactListGrid.setShowFilterEditor(false);	
		
		orderCustomerContactListGrid.setDataSource(customerContactData);
		orderCustomerContactListGrid.setFields(Util.getListGridFieldsFromDataSource(customerContactData));
		
		orderCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setWidth(100);
		orderCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setHidden(true);
		orderCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setWidth("10%");
		orderCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setWidth("10%");
		
		VLayout orderCustomerContactLayout = new VLayout();
		orderCustomerContactLayout.setMembers(orderCustomerContactLabel, orderCustomerContactListGrid);
		
		orderCustomerAddressContactLayout.setPadding(5);
		orderCustomerAddressContactLayout.setMembersMargin(5);
		orderCustomerAddressContactLayout.setMembers(orderCustomerAddressLayout, orderCustomerContactLayout);
		
		orderCustomerLayout.setMembers(orderCustomerForm, orderCustomerAddressContactLayout);
		
		setPane(orderCustomerLayout);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		orderCustomerAddressListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshCustomerAddressData();
			}
		});
		
		orderCustomerContactListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshCustomerContactData();
			}
		});
		
	}
	
	public void refreshCustomerAddressData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderCustomerAddressListGrid.setData(response.getData());
			}
		};
		
		orderCustomerAddressListGrid.getDataSource().fetchData(orderCustomerAddressListGrid.getFilterEditorCriteria(), callBack);

	}
	
	public void refreshCustomerContactData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderCustomerContactListGrid.setData(response.getData());
			}
		};
		
		orderCustomerContactListGrid.getDataSource().fetchData(orderCustomerContactListGrid.getFilterEditorCriteria(), callBack);

	}
	
	public DynamicForm getOrderCustomerForm() {
		return orderCustomerForm;

	}
	
}
