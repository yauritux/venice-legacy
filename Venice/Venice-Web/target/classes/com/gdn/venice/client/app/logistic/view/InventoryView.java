package com.gdn.venice.client.app.logistic.view;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.presenter.InventoryPresenter;
import com.gdn.venice.client.app.logistic.view.handlers.InventoryUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class InventoryView extends ViewWithUiHandlers<InventoryUiHandlers>
		implements InventoryPresenter.MyView {
	/*
	 * The RAF layout that is used for laying out the inventory data
	 */
	RafViewLayout inventoryLayout;
	
	ListGrid inventoryListGrid;

	ToolStrip inventoryToolStrip;
	ToolStripButton uploadButton;
	ToolStripButton removeButton;
	
	Window uploadWindow;
	
	@Inject
	public InventoryView(){
		inventoryLayout = new RafViewLayout();

		inventoryToolStrip = new ToolStrip();
		inventoryToolStrip.setWidth100();

		uploadButton = new ToolStripButton();
		uploadButton.setIcon("[SKIN]/icons/up.png");
		uploadButton.setTooltip("Upload Inventory");
		uploadButton.setTitle("Upload");
		uploadButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				buildUploadWindow().show();
			}
		});
		
		removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/delete.png");
		removeButton.setTooltip("Remove  Inventory");
		removeButton.setTitle("Remove");

		inventoryToolStrip.addButton(uploadButton);
		inventoryToolStrip.addSeparator();
		inventoryToolStrip.addButton(removeButton);

		inventoryListGrid = new ListGrid();
		
		inventoryListGrid.setSelectionType(SelectionStyle.SIMPLE);
		inventoryListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		inventoryListGrid.setShowFilterEditor(true);

		inventoryLayout.setMembers(inventoryToolStrip);
		removeButton.disable();

		bindCustomUiHandlers();
	}
	
	
	
	@Override
	public void loadInventoryData(DataSource dataSource) {
		inventoryListGrid.setDataSource(dataSource);
		inventoryListGrid.setAutoFetchData(false);
		inventoryListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		
		inventoryListGrid.getField(DataNameTokens.VENMERCHANTPRODUCT_PRODUCTID).setHidden(true);
		inventoryListGrid.getField(DataNameTokens.VENMERCHANTPRODUCT_WCSPRODUCTSKU).setWidth(150);
		inventoryListGrid.getField(DataNameTokens.VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth(150);
		inventoryListGrid.getField(DataNameTokens.VENMERCHANTPRODUCT_COSTOFGOODSSOLD).setWidth(150);
		
		inventoryLayout.addMember(inventoryListGrid);
	}



	private Window buildUploadWindow() {
		uploadWindow = new Window();
		uploadWindow.setWidth(360);
		uploadWindow.setHeight(120);
		uploadWindow.setTitle("Upload Inventory");
		uploadWindow.setShowMinimizeButton(false);
		uploadWindow.setIsModal(true);
		uploadWindow.setShowModalMask(true);
		uploadWindow.centerInPage();
		uploadWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				uploadWindow.destroy();
			}
		});
		
		VLayout uploadLayout = new VLayout();
		uploadLayout.setHeight100();
		uploadLayout.setWidth100();

		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setPadding(5);
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setTarget("upload_frame");

		UploadItem reportFileItem = new UploadItem();
		reportFileItem.setTitle("Inventory File");
		uploadForm.setItems(reportFileItem);
		
		HLayout uploadCancelButtons = new HLayout(5);
		
		IButton buttonUpload = new IButton("Upload");
		IButton buttonCancel = new IButton("Cancel");
		
		buttonUpload.addClickHandler(new ClickHandler() {
			
			@Override	
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();
				/*
				 * Change host to use Geronimo servlet URL in development
				 */
				if(host.contains(":8889")){
					host = "http://localhost:8090/";
				}else{
					host = host.substring(0, host.lastIndexOf("/", host.length()-2)+1);
				}
				
				uploadForm.setAction(host + "Venice/LogisticsInventoryImportServlet");
				
				uploadForm.submitForm();
				uploadWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				uploadWindow.destroy();
			}
		});
		uploadCancelButtons.setAlign(Alignment.CENTER);
		uploadCancelButtons.setMembers(buttonUpload, buttonCancel);
		
		uploadLayout.setMembers(uploadForm, uploadCancelButtons);
		uploadWindow.addItem(uploadLayout);
		return uploadWindow;
	}
	
	
	
	@Override
	public void refreshInventoryData() {
		@SuppressWarnings("unused")
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				inventoryListGrid.setData(response.getData());
				inventoryListGrid.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
		
	}


	protected void bindCustomUiHandlers() {
		inventoryListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				// Get selected record
				ListGridRecord[] selectedInventories = inventoryListGrid.getSelection();
				
				// when no record is selected, top buttons are disabled
				if(selectedInventories.length == 0){
					removeButton.disable();
				// enable top buttons after a record is selected	
				}else{
					removeButton.enable();
				}
			}
		});
		
		//ClickHandler for Remove Inventory Button
		removeButton.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?",
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									inventoryListGrid.removeSelectedData();
									SC.say("Data Removed");
									refreshInventoryData();
								} else {

								}
							}
						});
			}
		});
		
		
		
	}
	
	@Override
	public Widget asWidget() {
		return inventoryLayout;
	}
	
	/**
	 * @return the InventoryUiHandlers
	 */
	public InventoryUiHandlers getInventoryUiHandlers() {
		return getUiHandlers();
	}

	/**
	 * @return the removeButton
	 */
	public ToolStripButton getRemoveButton() {
		return removeButton;
	}

}
