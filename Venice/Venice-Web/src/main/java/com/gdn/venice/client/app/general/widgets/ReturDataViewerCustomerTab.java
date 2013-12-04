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

public class ReturDataViewerCustomerTab extends Tab {
	DynamicForm returCustomerForm;
	ListGrid returCustomerAddressListGrid = null;
	ListGrid returCustomerContactListGrid = null;
	
	public ReturDataViewerCustomerTab(String title, DataSource customerData, DataSource customerAddressData, DataSource customerContactData) {
		super(title);
		
		VLayout returCustomerLayout = new VLayout();
		
		returCustomerForm = new DynamicForm();
		returCustomerForm.setDataSource(customerData);  
		returCustomerForm.setUseAllDataSourceFields(true);  
		
		returCustomerForm.setNumCols(4);
		
		returCustomerForm.setFields(Util.getReadOnlyFormItemFromDataSource(customerData));
		returCustomerForm.getField(DataNameTokens.VENCUSTOMER_DATEOFBIRTH).setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
		
		returCustomerForm.getField(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG).setValueFormatter(new FormItemValueFormatter() {			
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
		
		returCustomerAddressListGrid = new ListGrid();
		
		returCustomerAddressListGrid.setWidth100();
		returCustomerAddressListGrid.setHeight100();
		returCustomerAddressListGrid.setShowAllRecords(true);
		returCustomerAddressListGrid.setSortField(0);

		returCustomerAddressListGrid.setCanResizeFields(true);
		returCustomerAddressListGrid.setShowRowNumbers(true);
		returCustomerAddressListGrid.setAutoFetchData(true);
	
		returCustomerAddressListGrid.setShowFilterEditor(false);	
		
		returCustomerAddressListGrid.setDataSource(customerAddressData);
		returCustomerAddressListGrid.setFields(Util.getListGridFieldsFromDataSource(customerAddressData));
		
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setWidth(100);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID).setHidden(true);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1).setWidth(150);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2).setWidth(150);		
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN).setWidth(100);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN).setWidth(100);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME).setWidth(100);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME).setWidth(100);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE).setWidth(75);
		returCustomerAddressListGrid.getField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME).setWidth(120);
		
		VLayout orderCustomerAddressLayout = new VLayout();
		orderCustomerAddressLayout.setMembers(orderCustomerAddressLabel, returCustomerAddressListGrid);
		
		Label orderCustomerContactLabel = new Label("<b>Contact:</b>");
		orderCustomerContactLabel.setHeight(10);
		
		returCustomerContactListGrid = new ListGrid();
		
		returCustomerContactListGrid.setWidth100();
		returCustomerContactListGrid.setHeight100();
		returCustomerContactListGrid.setShowAllRecords(true);
		returCustomerContactListGrid.setSortField(0);

		returCustomerContactListGrid.setCanResizeFields(true);
		returCustomerContactListGrid.setShowRowNumbers(true);
		returCustomerContactListGrid.setAutoFetchData(true);
	
		returCustomerContactListGrid.setShowFilterEditor(false);	
		
		returCustomerContactListGrid.setDataSource(customerContactData);
		returCustomerContactListGrid.setFields(Util.getListGridFieldsFromDataSource(customerContactData));
		
		returCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setWidth(100);
		returCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setHidden(true);
		returCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setWidth("10%");
		returCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setWidth("10%");
		
		VLayout orderCustomerContactLayout = new VLayout();
		orderCustomerContactLayout.setMembers(orderCustomerContactLabel, returCustomerContactListGrid);
		
		orderCustomerAddressContactLayout.setPadding(5);
		orderCustomerAddressContactLayout.setMembersMargin(5);
		orderCustomerAddressContactLayout.setMembers(orderCustomerAddressLayout, orderCustomerContactLayout);
		
		returCustomerLayout.setMembers(returCustomerForm, orderCustomerAddressContactLayout);
		
		setPane(returCustomerLayout);
		
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		returCustomerAddressListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshCustomerAddressData();
			}
		});
		
		returCustomerContactListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
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
				returCustomerAddressListGrid.setData(response.getData());
			}
		};
		
		returCustomerAddressListGrid.getDataSource().fetchData(returCustomerAddressListGrid.getFilterEditorCriteria(), callBack);

	}
	
	public void refreshCustomerContactData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				returCustomerContactListGrid.setData(response.getData());
			}
		};
		
		returCustomerContactListGrid.getDataSource().fetchData(returCustomerContactListGrid.getFilterEditorCriteria(), callBack);

	}
	
	public DynamicForm getReturCustomerForm() {
		return returCustomerForm;

	}
	
}
