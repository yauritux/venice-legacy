package com.gdn.venice.client.app.finance.view;

import com.gdn.venice.client.app.finance.presenter.PeriodSetupPresenter;
import com.gdn.venice.client.app.finance.view.handlers.PeriodSetupUiHandlers;
import com.gdn.venice.client.app.finance.widgets.PeriodSetupVLayoutWidget;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * The view class for the period setup screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PeriodSetupView extends ViewWithUiHandlers<PeriodSetupUiHandlers>
		implements PeriodSetupPresenter.MyView {

	/*
	 * The RAF layout that is used for laying out the period data
	 */
	RafViewLayout periodLayout;

	/*
	 * A VLayout widget that is used to display the fields on the period data
	 * screen
	 */
	PeriodSetupVLayoutWidget periodSetupVLayoutWidget;

	/*
	 * The toolstrip objects for the header and the detail
	 */
	ToolStrip periodToolStrip;

	/*
	 * The new and remove buttons for the period setup
	 */
	ToolStripButton addButton;
	ToolStripButton removeButton;


	/*
	 * Build the view and inject it
	 */
	@Inject
	public PeriodSetupView() {

		periodLayout = new RafViewLayout();

		periodToolStrip = new ToolStrip();
		periodToolStrip.setWidth100();

		addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add Period");
		addButton.setTitle("Add");

		removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/delete.png");
		removeButton.setTooltip("Remove Period");
		removeButton.setTitle("Remove");


		periodToolStrip.addButton(addButton);
		periodToolStrip.addButton(removeButton);
	

		periodSetupVLayoutWidget = new PeriodSetupVLayoutWidget(this);

		periodLayout.setMembers(periodToolStrip, periodSetupVLayoutWidget);
		removeButton.disable();

		bindCustomUiHandlers();
	}


	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PeriodSetupPresenter.MyView#loadPeriodData(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void loadPeriodData(DataSource dataSource) {
		
		periodSetupVLayoutWidget.loadPeriodData(dataSource);
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return periodLayout;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.PromotionPresenter.MyView#refreshPromotionData()
	 */
	@Override
	public void refreshPeriodData() {
		DSCallback callBack = new DSCallback() {


			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				periodSetupVLayoutWidget.getPeriodListGrid().setData(response.getData());
			}
		};
		periodSetupVLayoutWidget.getPeriodListGrid().getDataSource().fetchData(periodSetupVLayoutWidget.getPeriodListGrid().getFilterEditorCriteria(), callBack);

	}

	
	/**
	 * This method use for binding period buttons handlers (Add Period & Remove Period)
	 */
	protected void bindCustomUiHandlers() {
 		//Click Handler Add Period Button
		addButton.addClickHandler(new ClickHandler() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt
			 * .client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				// Simply start editing a new row in the list grid
				periodSetupVLayoutWidget.getPeriodListGrid().startEditingNew();
				
			}
		});
		//ClickHandler for Remove Period Button
		removeButton.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to remove this data?",
						new BooleanCallback() {
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									periodSetupVLayoutWidget.getPeriodListGrid()
											.removeSelectedData();
									refreshPeriodData();
									SC.say("Data Removed");
								} else {

								}
							}
						});
			}
		});
		periodSetupVLayoutWidget.bindCustomUiHandlers();
	}

	/**
	 * @return the PeriodSetupUiHandlers
	 */
	public PeriodSetupUiHandlers getPeriodSetupUiHandlers() {
		return getUiHandlers();
	}

	/**
	 * @return the removeButton
	 */
	public ToolStripButton getRemoveButton() {
		return removeButton;
	}

}