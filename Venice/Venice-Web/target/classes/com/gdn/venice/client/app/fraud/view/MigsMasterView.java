package com.gdn.venice.client.app.fraud.view;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.MigsMasterPresenter;
import com.gdn.venice.client.app.fraud.ui.widgets.MigsReportListGrid;
import com.gdn.venice.client.app.fraud.view.handlers.MigsMasterUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
/**
 * View for Migs Maste
 * 
 * @author Arifin
 */
public class MigsMasterView extends
ViewWithUiHandlers<MigsMasterUiHandlers> implements
MigsMasterPresenter.MyView {
	RafViewLayout migsUploadLayout;
	ListGrid migsReportListGrid;
	ToolStripButton processButton;
	Window uploadWindow;
	
	@Inject
	public MigsMasterView() {
		migsUploadLayout = new RafViewLayout();	
		migsReportListGrid = new MigsReportListGrid();

		bindCustomUiHandlers();
	}
	
	@Override
	public Widget asWidget() {
		return migsUploadLayout;
	}

	protected void bindCustomUiHandlers() {
		migsReportListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				migsReportListGrid.saveAllEdits();	
				refreshMigsUploadData();
			}
		});
	}


	@Override
	public void loadMigsUploadData(DataSource dataSource) {
		//Set data souce dan list field-nya	
		migsReportListGrid.setDataSource(dataSource);
		migsReportListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		migsReportListGrid.setAutoFetchData(false);
		migsReportListGrid.setShowRowNumbers(true);
		migsReportListGrid.setWidth100();
		migsReportListGrid.setHeight100();
		migsReportListGrid.scrollToBottom();
		migsReportListGrid.setSortField(0);
		
		//Rapikan width-width
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_MIGSID).setWidth("60px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_MIGSID).setHidden(true);
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_TRANSACTIONID).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_TRANSACTIONDATE).setWidth("100px");
    	migsReportListGrid.getField(DataNameTokens.MIGSMASTER_MERCHANTID).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_ORDERREFERENCE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_ORDERID).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_MERCHANTTRANSACTIONREFERENCE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_TRANSACTIONTYPE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_ACQUIRERID).setWidth("75px");		
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_BATCHNUMBER).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_CURRENCY).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_AMOUNT).setWidth("120px");
		Util.formatListGridFieldAsCurrency(migsReportListGrid.getField(DataNameTokens.MIGSMASTER_AMOUNT));
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_RRN).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_RESPONSECODE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_ACQUIRERRESPONSECODE).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_AUTHORISATIONCODE).setWidth("80px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_OPERATOR).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_MERCHANTTRANSACTIONSOURCE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_ORDERDATE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_CARDTYPE).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_CARDNUMBER).setWidth("140px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_CARDEXPIRYMONTH).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_CARDEXPIRYYEAR).setWidth("75px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_DIALECTCSCRESULTCODE).setWidth("100px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_ECOMMERCEINDICATOR).setWidth("50px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_PROBLEMDESCRIPTION).setWidth("240px");
		migsReportListGrid.getField(DataNameTokens.MIGSMASTER_ACTION).setWidth("85px");
		
		migsUploadLayout.addMember(migsReportListGrid);
	}

	@Override
	public void refreshMigsUploadData() {
		if (migsReportListGrid instanceof MigsReportListGrid) {
			((MigsReportListGrid) migsReportListGrid).refreshUploadedData();
		}
	}
}