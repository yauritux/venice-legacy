package com.gdn.venice.client.app.reservation.view;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.reservation.view.handlers.ReservationOrderManagementUiHandlers;
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
import com.gdn.venice.client.app.reservation.presenter.ReservationOrderManagementPresenter;

public class ReservationOrderManagementView extends
		ViewWithUiHandlers<ReservationOrderManagementUiHandlers> implements
		ReservationOrderManagementPresenter.MyView {

	RafViewLayout reservationOrderManagementLayout;
	ListGrid reservationOrderManagementListGrid = new ListGrid();

	@Inject
	public ReservationOrderManagementView() {
		reservationOrderManagementLayout = new RafViewLayout();

		ToolStrip reservationOrderManagementToolStrip = new ToolStrip();
		reservationOrderManagementToolStrip.setWidth100();
				
		reservationOrderManagementListGrid.setAutoFetchData(false);
		reservationOrderManagementListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		reservationOrderManagementListGrid.setShowRowNumbers(true);
		reservationOrderManagementListGrid.setCanResizeFields(true);
		reservationOrderManagementListGrid.setShowFilterEditor(true);
		reservationOrderManagementListGrid.setCanSort(true);
		
		reservationOrderManagementLayout.setMembers(reservationOrderManagementToolStrip);
	}

	@Override
	public Widget asWidget() {
		return reservationOrderManagementLayout;
	}

	protected void bindCustomUiHandlers() {
		reservationOrderManagementListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshReservationOrderManagementData();
			}
		});
		
		reservationOrderManagementListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				reservationOrderManagementListGrid.saveAllEdits();
				refreshReservationOrderManagementData();
			}
		});
	}

	@Override
	public void loadReservationOrderManagementData(DataSource dataSource) {		
		ListGridField listGridField[] = Util.getListGridFieldsFromDataSource(dataSource);
		
		reservationOrderManagementListGrid.setDataSource(dataSource);
		reservationOrderManagementListGrid.setFields(listGridField);
		reservationOrderManagementListGrid.setAutoFetchData(false);

		reservationOrderManagementListGrid.getField(DataNameTokens.RSV_ORDERMANAGEMENT_FIRSTNAME).setCanFilter(false);
		reservationOrderManagementListGrid.getField(DataNameTokens.RSV_ORDERMANAGEMENT_LASTNAME).setCanFilter(false);
		reservationOrderManagementListGrid.getField(DataNameTokens.RSV_ORDERMANAGEMENT_ORDERDATE).setCanFilter(false);
		reservationOrderManagementListGrid.getField(DataNameTokens.RSV_ORDERMANAGEMENT_MOBILEPHONE).setCanFilter(false);
		reservationOrderManagementListGrid.getField(DataNameTokens.RSV_ORDERMANAGEMENT_PAYMENTSTATUS).setCanFilter(false);
		
		reservationOrderManagementLayout.addMember(reservationOrderManagementListGrid);
        
        bindCustomUiHandlers();
	}
	
	public void refreshReservationOrderManagementData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				reservationOrderManagementListGrid.setData(response.getData());
			}
		};
		
		reservationOrderManagementListGrid.getDataSource().fetchData(reservationOrderManagementListGrid.getFilterEditorCriteria(), callBack);
	}
	
}
