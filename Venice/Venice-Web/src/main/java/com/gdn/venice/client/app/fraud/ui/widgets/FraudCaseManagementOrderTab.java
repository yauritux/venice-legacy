package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class FraudCaseManagementOrderTab extends Tab {
	DynamicForm orderDetailForm;
	ListGrid orderItemListGrid = null;
	
	public FraudCaseManagementOrderTab(String title, DataSource orderDetailData, DataSource orderItemData) {
		super(title);		
		VLayout orderLayout = new VLayout();
		
		//Form order detail
		HLayout orderDetailLayout = new HLayout();
		orderDetailLayout.setWidth(500);
		orderDetailLayout.setHeight(100);
		orderDetailForm = new DynamicForm();
		orderDetailForm.setWidth100();
		orderDetailForm.setTitleWidth(130);
		orderDetailForm.setDataSource(orderDetailData);  
		orderDetailForm.setUseAllDataSourceFields(true);  
		orderDetailForm.setNumCols(4);
		orderDetailForm.setMargin(10);
		orderDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(orderDetailData));		
//		orderDetailForm.getField(DataNameTokens.VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		orderDetailForm.getField(DataNameTokens.VENORDER_RMAFLAG).setValueFormatter(new FormItemValueFormatter() {			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (form.getField(DataNameTokens.VENORDER_RMAFLAG).equals("true")) 
                	{
                		return "Yes";
                	} else {
                		return "No";
                	}
			}
		});
		
		orderDetailForm.getField(DataNameTokens.VENORDER_FULFILLMENTSTATUS).setValueFormatter(new FormItemValueFormatter() {			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (form.getField(DataNameTokens.VENORDER_FULFILLMENTSTATUS).equals("true")) 
                	{
                		return "Yes";
                	} else {
                		return "No";
                	}
			}
		});
		
		orderDetailForm.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setValueFormatter(new FormItemValueFormatter() {			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (form.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).equals("true")) 
                	{
                		return "Yes";
                	} else {
                		return "No";
                	}
			}
		});

		Util.formatFormItemAsCurrency(orderDetailForm.getField(DataNameTokens.VENORDERITEM_PRODUCTPRICE));
		Util.formatFormItemAsCurrency(orderDetailForm.getField(DataNameTokens.VENORDER_AMOUNT));
		orderDetailLayout.setMembers(orderDetailForm);
		
		//Grid order items
		VLayout fraudCaseOrderItemLayout  = new VLayout();
		Label fraudCaseOrderItemLabel = new Label("<b>Order Item:</b>");
		fraudCaseOrderItemLabel.setHeight(10);
		
		orderItemListGrid = new ListGrid();
		orderItemListGrid.setShowRollOver(false);
		orderItemListGrid.setWidth100();
		orderItemListGrid.setHeight100();
		orderItemListGrid.setSortField(0);
		orderItemListGrid.setShowRowNumbers(true);
		orderItemListGrid.setAutoFetchData(true);			
		orderItemListGrid.setDataSource(orderItemData);
		orderItemListGrid.setFields(
			new ListGridField(DataNameTokens.VENORDERITEM_WCSORDERITEMID, 60),
			new ListGridField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_SUMMARY),
			new ListGridField(DataNameTokens.VENORDERITEM_QUANTITY, 40),
			new ListGridField(DataNameTokens.VENORDERITEM_PRICE, 95),
			new ListGridField(DataNameTokens.VENORDERITEM_SHIPPINGCOST, 75),
			new ListGridField(DataNameTokens.VENORDERITEM_INSURANCECOST, 75),
			new ListGridField(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT, 75),
			new ListGridField(DataNameTokens.VENORDERITEM_TOTAL, 95),
			new ListGridField(DataNameTokens.VENORDERITEMADJUSTMENT_VENPROMOTION_PROMOCODE, 75),
			new ListGridField(DataNameTokens.VENORDERITEM_VENADDRESS)
		);
		
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_QUANTITY).setAlign(Alignment.RIGHT);
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEM_PRICE));
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL));
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEM_SHIPPINGCOST));
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEM_INSURANCECOST));
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT));
		
		//Put all components into layout
		fraudCaseOrderItemLayout.setMembers(fraudCaseOrderItemLabel, orderItemListGrid);
		orderLayout.setMembers(orderDetailLayout, fraudCaseOrderItemLayout);
		setPane(orderLayout);
	}
	
	public DynamicForm getFraudCaseOrderForm() {
		return orderDetailForm;
	}
}
