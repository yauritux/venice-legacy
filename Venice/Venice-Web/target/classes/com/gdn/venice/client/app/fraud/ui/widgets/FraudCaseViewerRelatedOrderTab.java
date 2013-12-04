package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Widget for Fraud Related Order Viewer
 * 
 * @author Roland
 */

public class FraudCaseViewerRelatedOrderTab extends Tab {
	ListGrid relatedOrderListGrid = null;
	ListGrid relatedOrderListGridPrint = null;
	
	public FraudCaseViewerRelatedOrderTab(String title, DataSource relatedOrderData, DataSource relatedOrderDataPrint) {
		super(title);		        
        
		relatedOrderListGrid = new ListGrid();
		relatedOrderListGrid.setWidth100();
		relatedOrderListGrid.setHeight100();
		relatedOrderListGrid.setShowAllRecords(true);
		relatedOrderListGrid.setSortField(0);
		relatedOrderListGrid.setShowFilterEditor(true);
		relatedOrderListGrid.setSelectionType(SelectionStyle.SIMPLE);  
		relatedOrderListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		relatedOrderListGrid.setShowRowNumbers(true);
		relatedOrderListGrid.setDataSource(relatedOrderData);
		relatedOrderListGrid.setFields(Util.getListGridFieldsFromDataSource(relatedOrderData));
		relatedOrderListGrid.setCanResizeFields(true);
		relatedOrderListGrid.setAutoFetchData(true);
		relatedOrderListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).setHidden(true);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_WCSORDERID).setWidth("80");
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT).setAlign(Alignment.RIGHT);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE).setWidth("100");
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setWidth("100");
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_IPADDRESS).setWidth("150");
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_ORDERDATE).setWidth("120");
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_ORDERID).setHidden(true);
//		relatedOrderListGrid.getField(DataNameTokens.VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		Util.formatListGridFieldAsCurrency(relatedOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT));
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
		                if (record.getAttributeAsString(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).equals("true")) 
		            	{
		            		return "Yes";
		            	} else {
		            		return "No";
            	}
			}
		});
		
		ToolStrip relatedOrderToolStrip = new ToolStrip();  
		relatedOrderToolStrip.setWidth100();
		relatedOrderToolStrip.setPadding(2);

		ToolStripButton printRelatedOrder = new ToolStripButton();
		printRelatedOrder.setIcon("[SKIN]/icons/printer.png");
		printRelatedOrder.setTooltip("Print Related Orders");
		printRelatedOrder.setTitle("Print");

		relatedOrderToolStrip.addButton(printRelatedOrder);
		
        //grid
		VLayout fraudCaseRelatedOrderLayout = new VLayout();
		fraudCaseRelatedOrderLayout.setMembers(relatedOrderToolStrip, relatedOrderListGrid);
		setPane(fraudCaseRelatedOrderLayout);
				
		//grid for print related order list
		relatedOrderListGridPrint = new ListGrid();
		relatedOrderListGridPrint.setWidth100();
		relatedOrderListGridPrint.setHeight100();
		relatedOrderListGridPrint.setShowAllRecords(true);
		relatedOrderListGridPrint.setSortField(0);
		relatedOrderListGridPrint.setDataSource(relatedOrderDataPrint);
		relatedOrderListGridPrint.setFields(Util.getListGridFieldsFromDataSource(relatedOrderDataPrint));
		relatedOrderListGridPrint.setAutoFetchData(true);
		relatedOrderListGridPrint.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).setHidden(true);
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_WCSORDERID).setWidth("80");
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_AMOUNT).setAlign(Alignment.RIGHT);
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE).setWidth("100");
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setWidth("100");
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_IPADDRESS).setWidth("150");
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_ORDERDATE).setWidth("120");
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_ORDERID).setHidden(true);
//		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		Util.formatListGridFieldAsCurrency(relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_AMOUNT));
		relatedOrderListGridPrint.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
		                if (record.getAttributeAsString(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).equals("true")) 
		            	{
		            		return "Yes";
		            	} else {
		            		return "No";
            	}
			}
		});
		
        SectionStack printStack = new SectionStack();  
        printStack.setVisibilityMode(VisibilityMode.MULTIPLE);  
        printStack.setWidth(1);  
        printStack.setHeight(1);  
   
        final VLayout printContainer = new VLayout(10);  
        
        printRelatedOrder.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Canvas.showPrintPreview(printContainer);  				
			}
		});
		
		//print preview
        SectionStackSection relatedOrderSection = new SectionStackSection("Related Order");  
        relatedOrderSection.setExpanded(true);  
        relatedOrderSection.addItem(relatedOrderListGridPrint);  
        printStack.addSection(relatedOrderSection); 
        
        printContainer.addMember(printStack);   
		printContainer.draw();
		bindCustomUiHandlers();
	}
	
	protected void bindCustomUiHandlers() {
		refreshFraudCaseRelatedOrderData();
		relatedOrderListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudCaseRelatedOrderData();
			}
		});			
	}
	
	public void refreshFraudCaseRelatedOrderData() {
		DSCallback callBack = new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				relatedOrderListGrid.setData(response.getData());
			}
		};
		
		relatedOrderListGrid.getDataSource().fetchData(relatedOrderListGrid.getFilterEditorCriteria(), callBack);

	}	
}
