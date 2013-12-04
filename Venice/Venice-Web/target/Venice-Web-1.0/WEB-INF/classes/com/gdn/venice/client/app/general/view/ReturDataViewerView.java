package com.gdn.venice.client.app.general.view;

import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.presenter.ReturDataViewerPresenter;
import com.gdn.venice.client.app.general.view.handlers.ReturDataViewerUiHandlers;
import com.gdn.venice.client.app.general.widgets.ReturDetailContentLayout;
import com.gdn.venice.client.util.PrintUtility;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Retur Data Viewer
 * 
 * @author Roland
 */
public class ReturDataViewerView extends ViewWithUiHandlers<ReturDataViewerUiHandlers> implements ReturDataViewerPresenter.MyView {
	private static final int LIST_HEIGHT = 200;	

	RafViewLayout returDataViewerLayout;
	
	VLayout returListLayout;
	VLayout returDetailLayout;
	
	ReturDetailContentLayout returDetailContentLayout;
	
	ListGrid returListGrid;
	
	ToolStripButton printButton;
    private ToolStripButton firstButton;
    private ToolStripButton nextButton;
    private ToolStripButton previousButton;
    private ToolStripButton lastButton;
    private final Label pageNumber;
    
	@Inject
	public ReturDataViewerView() {		
		ToolStrip returDataViewerToolStrip = new ToolStrip();
		returDataViewerToolStrip.setWidth100();
		returDataViewerToolStrip.setPadding(2);
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Retur Details");
		printButton.setTitle("Print");
		
		firstButton = new ToolStripButton("1");
		firstButton.setTooltip("Current page");
		firstButton.setDisabled(true);
		
		previousButton = new ToolStripButton("Prev");
		previousButton.setTooltip("Go to previous page");
		previousButton.setDisabled(true);
		
		nextButton = new ToolStripButton("Next");
		nextButton.setTooltip("Go to next page");
		
		lastButton = new ToolStripButton();
		lastButton.setTooltip("Go to last page");
	
		pageNumber = new Label(" 1 ");
		pageNumber.setTooltip("Current page");
		pageNumber.setVisible(false);
		
		returDataViewerToolStrip.addButton(printButton);
		
//		orderDataViewerToolStrip.addSeparator();
//		
//		orderDataViewerToolStrip.addButton(previousButton);
//		orderDataViewerToolStrip.addButton(firstButton);
//		orderDataViewerToolStrip.addMember(pageNumber);
//		orderDataViewerToolStrip.addButton(lastButton);
//		orderDataViewerToolStrip.addButton(nextButton);
		
		returDataViewerLayout = new RafViewLayout();
		
		returListLayout = new VLayout();
		returListLayout.setHeight(LIST_HEIGHT);
		returListLayout.setShowResizeBar(true);
		
		returDetailLayout = new VLayout();
		returDetailLayout.setWidth100();
		
		HTMLFlow returDetailFlow = new HTMLFlow();
		returDetailFlow.setAlign(Alignment.CENTER);
		returDetailFlow.setWidth100();
		returDetailFlow.setContents("<h2 align=\"center\">Please select a retur to show the retur detail</h2>");
		returDetailLayout.setMembers(returDetailFlow);

		returDataViewerLayout.setMembers(returDataViewerToolStrip, returListLayout, returDetailLayout);
		buildReturListGrid();
		bindCustomUiHandlers();
	}
	
	private ListGrid buildReturListGrid() {
		returListGrid = new ListGrid();
		returListGrid.setWidth100();
		returListGrid.setHeight100();
		returListGrid.setShowAllRecords(true);
		returListGrid.setSortField(0);
		returListGrid.setCanResizeFields(true);
		returListGrid.setShowRowNumbers(true);
		returListGrid.setAutoFetchData(false);	
		returListGrid.setSelectionType(SelectionStyle.SIMPLE);
		returListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);		
		returListGrid.setShowFilterEditor(true);	

		return returListGrid;
	}
	
	
	@Override
	public Widget asWidget() {
		return returDataViewerLayout;
	}

	protected void bindCustomUiHandlers() {		
		printButton.addClickHandler(new ClickHandler() {			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(returDataViewerLayout);
			}
		});
		
		returListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = event.getSelection();
				if (selectedRecords.length==1) {
					Record record = event.getRecord();  
					showReturDetail(record);
				} else {
					HTMLFlow returDetailFlow = new HTMLFlow();
					returDetailFlow.setAlign(Alignment.CENTER);
					returDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						returDetailFlow.setContents("<h2 align=\"center\">Please select a retur to show the retur detail</h2>");
					} else if (selectedRecords.length>1) {
						returDetailFlow.setContents("<h2 align=\"center\">More than one retur selected, please select only one retur to show the retur detail</h2>");
					}
					returDetailLayout.setMembers(returDetailFlow);
				}
			}
		});
		
		returListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshReturData();
			}
		});
		
		firstButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				goToPage(1);
			}
		});
		
		nextButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				goToPage(Integer.parseInt(pageNumber.getContents().trim()) + 1);
			}
		});
		
		lastButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				goToPage(Integer.parseInt(lastButton.getTitle()));
			}
		});
		
		previousButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				goToPage(Integer.parseInt(pageNumber.getContents().trim()) - 1);
			}
		});
	}

	private void showReturDetail(Record record) {
		String returId = record.getAttributeAsString(DataNameTokens.VENRETUR_RETURID);	
		List<DataSource> dataSources = getUiHandlers().onShowReturDetailData(returId);
		
		returDetailContentLayout = new ReturDetailContentLayout(
				dataSources.get(0), //Retur Detail
				dataSources.get(1), //Retur Item
				dataSources.get(2), //Retur Customer
				dataSources.get(3), //Retur Customer Address
				dataSources.get(4), //Retur Customer Contact
				dataSources.get(5), //Retur Logistics Airway Bill
				dataSources.get(6), //Retur History Retur
				dataSources.get(7) //Retur History Retur Item
				);			
		
		returDetailLayout.setMembers(returDetailContentLayout);
	}
	
	@Override
	public void loadReturData(DataSource dataSource, Map<String,String> status) {
		dataSource.getField(DataNameTokens.VENRETUR_VENRETURSTATUS_ORDERSTATUSID).setValueMap(status);
		
		returListGrid.setDataSource(dataSource);
		returListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));		
		returListGrid.getField(DataNameTokens.VENRETUR_RETURID).setWidth("10%");
		returListGrid.getField(DataNameTokens.VENRETUR_RETURID).setHidden(true);
		returListGrid.getField(DataNameTokens.VENRETUR_WCSRETURID).setWidth("20%");
		returListGrid.getField(DataNameTokens.VENRETUR_RETURDATE).setWidth("20%");
		returListGrid.getField(DataNameTokens.VENRETUR_VENCUSTOMER_CUSTOMERUSERNAME).setWidth("30%");		
		returListGrid.getField(DataNameTokens.VENRETUR_VENRETURSTATUS_ORDERSTATUSID).setWidth("10%");
		returListGrid.getField(DataNameTokens.VENRETUR_RMAACTION).setWidth("20%");
		
		returListLayout.addMember(returListGrid);		
	}

	@Override
	public void refreshReturData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				returListGrid.setData(response.getData());
			}
		};		
		returListGrid.getDataSource().fetchData(returListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void goToPage(int pageNum) {
        if (pageNum < 1)
                pageNum = 1;
        pageNumber.setContents(" " + pageNum + " ");
        updatePage(pageNum);
	}
	
	public void updatePage(int pageNum) {
		getUiHandlers().onFetchComboBoxData((pageNum - 1) * 50);
		
		if(pageNum == 1 && pageNum == Integer.parseInt(lastButton.getTitle())){
   			previousButton.setDisabled(true);
   			firstButton.setDisabled(true);
   			lastButton.setDisabled(true);
   			nextButton.setDisabled(true);
   			pageNumber.setVisible(false);
		}
		else if(pageNum == 1){
   			previousButton.setDisabled(true);
   			firstButton.setDisabled(true);
   			lastButton.setDisabled(false);
   			nextButton.setDisabled(false);
   			pageNumber.setVisible(false);
   		}
   		else{ 
   			if(pageNum == Integer.parseInt(lastButton.getTitle())){
   			previousButton.setDisabled(false);
   			firstButton.setDisabled(false);
   			lastButton.setDisabled(true);
   			nextButton.setDisabled(true);
   			pageNumber.setVisible(false);
	   		}
	   		else{
	   			previousButton.setDisabled(false);
	   			firstButton.setDisabled(false);
	   			lastButton.setDisabled(false);
	   			nextButton.setDisabled(false);
	   			pageNumber.setVisible(true);
	   		}
   		}
	}
	
//	@Override
//	public void setLastPage(int totalRows){
//		lastButton.setTitle(totalRows/50+1+"");
//		if(Integer.parseInt(lastButton.getTitle()) == 1)
//			lastButton.setVisible(false);
//		else 
//			lastButton.setVisible(true);
//	}	
}
