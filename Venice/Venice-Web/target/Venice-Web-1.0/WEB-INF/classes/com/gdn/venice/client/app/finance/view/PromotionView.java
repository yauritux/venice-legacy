package com.gdn.venice.client.app.finance.view;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.finance.presenter.PromotionPresenter;
import com.gdn.venice.client.app.finance.view.handlers.PromotionUiHandlers;
import com.gdn.venice.client.app.finance.widgets.PromotionVLayoutWidget;
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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * The view class for the promotion screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian
 * Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PromotionView extends ViewWithUiHandlers<PromotionUiHandlers>
		implements PromotionPresenter.MyView {
	/*
	 * The RAF layout that is used for laying out the promotion data
	 */
	RafViewLayout promotionLayout;

	/*
	 * A VLayout widget that is used to display the fields on the promotion data
	 * screen
	 */
	PromotionVLayoutWidget promotionVLayoutWidget;

	/*
	 * The toolstrip objects for the header and the detail
	 */
	ToolStrip promotionToolStrip;
	ToolStrip promotionShareDetailToolStrip;

	/*
	 * The add and remove buttons for the promotions header
	 */
	ToolStripButton uploadPromo;
	ToolStripButton removeButton;
	/*
	 * The add  and remove buttons for the promotions share detail 
	 */
	ToolStripButton addDetailButton;
	ToolStripButton removeDetailButton;
	
	Window uploadWindow;

	/*
	 * Build the view and inject it
	 */
	@Inject
	public PromotionView() {
		promotionLayout = new RafViewLayout();

		promotionToolStrip = new ToolStrip();
		promotionToolStrip.setWidth100();

		uploadPromo = new ToolStripButton();
		uploadPromo.setIcon("[SKIN]/icons/up.png");
		uploadPromo.setTooltip("Upload Promo Code");
		uploadPromo.setTitle("Upload");
		uploadPromo.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				buildUploadWindow().show();
			}
		});
		
		removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/delete.png");
		removeButton.setTooltip("Remove  Promotion");
		removeButton.setTitle("Remove");

		promotionToolStrip.addButton(uploadPromo);
		promotionToolStrip.addButton(removeButton);
	
		promotionVLayoutWidget = new PromotionVLayoutWidget(this);

		promotionLayout.setMembers(promotionToolStrip, promotionVLayoutWidget);
		removeButton.disable();

		bindCustomUiHandlers();
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PromotionPresenter.MyView#loadPromotionData(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void loadPromotionData(DataSource dataSource) {
		promotionVLayoutWidget.loadPromotionData(dataSource);
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return promotionLayout;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PromotionPresenter.MyView#refreshPromotionData()
	 */
	@Override
	public void refreshPromotionData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				promotionVLayoutWidget.getPromotionList().setData(response.getData());
				promotionVLayoutWidget.getPromotionList().setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
		promotionVLayoutWidget.getPromotionList().getDataSource().fetchData(promotionVLayoutWidget.getPromotionList().getFilterEditorCriteria(), callBack);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PromotionPresenter.MyView#refreshPromotionShareData()
	 */
	@Override
	public void refreshPromotionShareData() {
		@SuppressWarnings("unused")
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				promotionVLayoutWidget.getPromotionShareDetailList().setData(response.getData());
				promotionVLayoutWidget.getPromotionShareDetailList().setGroupStartOpen(GroupStartOpen.ALL);
			}
		};

	}
	
	private Window buildUploadWindow() {
		uploadWindow = new Window();
		uploadWindow.setWidth(360);
		uploadWindow.setHeight(120);
		uploadWindow.setTitle("Upload Promotion Code");
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

		final SelectItem providerSelectItem = new SelectItem("PromoType");
		providerSelectItem.setTitle("Promo Type");  
		
		providerSelectItem.setValueMap(DataConstantNameTokens.promoTypes());
		
		UploadItem reportFileItem = new UploadItem();
		reportFileItem.setTitle("Promotion File");
		uploadForm.setItems(providerSelectItem, reportFileItem);
		
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
				
				if(providerSelectItem.getValue() != null || (providerSelectItem.getValue() instanceof Integer)){
					uploadForm.setAction(host + "Venice/FinanceImportPromoServlet?type=" + providerSelectItem.getValue());
					
					uploadForm.submitForm();
					uploadWindow.destroy();
				}else{
					com.google.gwt.user.client.Window.alert("Please select the Promotion Type");
				}
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
	
	/**
	 * This method use for binding promotion buttons handlers (Add Promotion & Remove Promotion)
	 */
	protected void bindCustomUiHandlers() {
 		//Click Handler Add Promotion Button
//		addButton.addClickHandler(new ClickHandler() {
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see
//			 * com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt
//			 * .client.widgets.events.ClickEvent)
//			 */
//			@Override
//			public void onClick(ClickEvent event) {
//				// Simply start editing a new row in the list grid
//				promotionVLayoutWidget.getPromotionList().startEditingNew();				
//			}
//		});
		//ClickHandler for Remove Promotion Button
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
									promotionVLayoutWidget.getPromotionList().removeSelectedData();
									refreshPromotionShareData();
									SC.say("Data Removed");
								} else {

								}
							}
						});
			}
		});
		refreshPromotionShareData();		
		promotionVLayoutWidget.bindCustomUiHandlers();
	}

	/**
	 * @return the PromotionUiHandlers
	 */
	public PromotionUiHandlers getPromotionUiHandlers() {
		return getUiHandlers();
	}

	/**
	 * @return the removeButton
	 */
	public ToolStripButton getRemoveButton() {
		return removeButton;
	}

	/**
	 * @return the removeDetailButton
	 */
	public ToolStripButton getRemoveDetailButton() {
		return removeDetailButton;
	}
}
