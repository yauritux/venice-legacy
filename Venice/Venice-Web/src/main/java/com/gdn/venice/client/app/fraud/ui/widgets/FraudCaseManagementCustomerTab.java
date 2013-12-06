package com.gdn.venice.client.app.fraud.ui.widgets;

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
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class FraudCaseManagementCustomerTab extends Tab {
	DynamicForm fraudCaseCustomerForm;
	DynamicForm fraudCaseCustomerAddressForm;
	ListGrid fraudCaseCustomerAddressListGrid = null;
	ListGrid fraudCaseCustomerContactListGrid = null;
	
	public FraudCaseManagementCustomerTab(String title, DataSource customerData, DataSource customerAddressData, DataSource customerContactData) {
		super(title);
		
		VLayout fraudCaseCustomerLayout = new VLayout();
		
		//Form customer detail (header)
		fraudCaseCustomerForm = new DynamicForm();
		fraudCaseCustomerForm.setDataSource(customerData);
		fraudCaseCustomerForm.setUseAllDataSourceFields(false);
		fraudCaseCustomerForm.setTitleWidth(100);
		fraudCaseCustomerForm.setWidth("300");
		fraudCaseCustomerForm.setNumCols(2);				
		fraudCaseCustomerForm.setFields(Util.getReadOnlyFormItemFromDataSource(customerData));
		fraudCaseCustomerForm.getField(DataNameTokens.VENCUSTOMER_DATEOFBIRTH).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		fraudCaseCustomerForm.getField(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG).setValueFormatter(new FormItemValueFormatter() {			
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
		
		//Form customer address (header)
		fraudCaseCustomerAddressForm = new DynamicForm();
		fraudCaseCustomerAddressForm.setDataSource(customerAddressData);
		fraudCaseCustomerAddressForm.setUseAllDataSourceFields(false);
		fraudCaseCustomerAddressForm.setTitleWidth(100);
		fraudCaseCustomerAddressForm.setWidth("*");
		fraudCaseCustomerAddressForm.setNumCols(2);		
		fraudCaseCustomerAddressForm.setFields(Util.getReadOnlyFormItemFromDataSource(customerAddressData));
		
		//Header layout
		HLayout fraudCaseCustomerHeaderLayout = new HLayout();
		fraudCaseCustomerHeaderLayout.setWidth100();
		fraudCaseCustomerHeaderLayout.setMargin(10);
		fraudCaseCustomerHeaderLayout.setMembers(fraudCaseCustomerForm, fraudCaseCustomerAddressForm);
		
		//Grid customer's contact detail
		Label fraudCaseCustomerContactLabel = new Label("<b>Contact:</b>");
		fraudCaseCustomerContactLabel.setHeight(10);		
		fraudCaseCustomerContactListGrid = new ListGrid();		
		fraudCaseCustomerContactListGrid.setWidth100();
		fraudCaseCustomerContactListGrid.setHeight100();
		fraudCaseCustomerContactListGrid.setSortField(0);
		fraudCaseCustomerContactListGrid.setShowRowNumbers(true);
		fraudCaseCustomerContactListGrid.setAutoFetchData(true);		
		fraudCaseCustomerContactListGrid.setDataSource(customerContactData);
		fraudCaseCustomerContactListGrid.setFields(Util.getListGridFieldsFromDataSource(customerContactData));		
		fraudCaseCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setWidth("10%");
		fraudCaseCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setHidden(true);
		fraudCaseCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC).setWidth("10%");
		fraudCaseCustomerContactListGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL).setWidth("10%");
		
		fraudCaseCustomerLayout.setMembers(fraudCaseCustomerHeaderLayout, fraudCaseCustomerContactLabel, fraudCaseCustomerContactListGrid);
		setPane(fraudCaseCustomerLayout);
	}
		
	public void refreshCustomerContactData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudCaseCustomerContactListGrid.setData(response.getData());
			}
		};
		
		fraudCaseCustomerContactListGrid.getDataSource().fetchData(fraudCaseCustomerContactListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public DynamicForm getFraudCaseCustomerForm() {
		return fraudCaseCustomerForm;
	}
	
	public DynamicForm getFraudCaseCustomerAddressForm() {
		return fraudCaseCustomerAddressForm;
	}
}