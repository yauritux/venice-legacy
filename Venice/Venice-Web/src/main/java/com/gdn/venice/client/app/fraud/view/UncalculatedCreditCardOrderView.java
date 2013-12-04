package com.gdn.venice.client.app.fraud.view;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.UncalculatedCreditCardOrderPresenter;
import com.gdn.venice.client.app.fraud.view.handlers.UncalculatedCreditCardOrderUiHandlers;
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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class UncalculatedCreditCardOrderView extends
		ViewWithUiHandlers<UncalculatedCreditCardOrderUiHandlers> implements
		UncalculatedCreditCardOrderPresenter.MyView {

	RafViewLayout uncalculatedCreditCardOrderLayout;
	ListGrid uncalculatedCreditCardOrdertListGrid = new ListGrid();


	@Inject
	public UncalculatedCreditCardOrderView() {
		uncalculatedCreditCardOrderLayout = new RafViewLayout();

		ToolStrip uncalculatedCreditCardOrderToolStrip = new ToolStrip();
		uncalculatedCreditCardOrderToolStrip.setWidth100();
				
		uncalculatedCreditCardOrdertListGrid.setAutoFetchData(false);
		uncalculatedCreditCardOrdertListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		uncalculatedCreditCardOrdertListGrid.setShowRowNumbers(true);
		uncalculatedCreditCardOrdertListGrid.setCanResizeFields(true);
		uncalculatedCreditCardOrdertListGrid.setShowFilterEditor(true);
		uncalculatedCreditCardOrdertListGrid.setCanSort(true);
		
		uncalculatedCreditCardOrderLayout.setMembers(uncalculatedCreditCardOrderToolStrip);
	}

	@Override
	public Widget asWidget() {
		return uncalculatedCreditCardOrderLayout;
	}

	protected void bindCustomUiHandlers() {
		uncalculatedCreditCardOrdertListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshUncalculatedCreditCardOrderData();
			}
		});
		
		uncalculatedCreditCardOrdertListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				uncalculatedCreditCardOrdertListGrid.saveAllEdits();
				refreshUncalculatedCreditCardOrderData();
			}
		});
	}

	@Override
	public void loadUncalculatedCreditCardOrderData(DataSource dataSource) {		
		ListGridField listGridField[] = Util.getListGridFieldsFromDataSource(dataSource);
		ListGridField finalListGridField[] = {listGridField[2], listGridField[3], listGridField[4], listGridField[5]};
		
		uncalculatedCreditCardOrdertListGrid.setDataSource(dataSource);
		uncalculatedCreditCardOrdertListGrid.setFields(finalListGridField);
		uncalculatedCreditCardOrdertListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER).setCanEdit(true);
		Util.formatListGridFieldAsCurrency(uncalculatedCreditCardOrdertListGrid.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT));
		
		uncalculatedCreditCardOrderLayout.addMember(uncalculatedCreditCardOrdertListGrid);
        
        bindCustomUiHandlers();
	}
	
	public void refreshUncalculatedCreditCardOrderData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				uncalculatedCreditCardOrdertListGrid.setData(response.getData());
			}
		};
		
		uncalculatedCreditCardOrdertListGrid.getDataSource().fetchData(uncalculatedCreditCardOrdertListGrid.getFilterEditorCriteria(), callBack);
	}	
	
}
