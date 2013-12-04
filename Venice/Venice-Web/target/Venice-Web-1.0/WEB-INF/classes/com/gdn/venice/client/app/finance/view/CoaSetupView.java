package com.gdn.venice.client.app.finance.view;

import com.gdn.venice.client.app.finance.presenter.CoaSetupPresenter;
import com.gdn.venice.client.app.finance.view.handlers.CoaSetupUiHandlers;
import com.gdn.venice.client.app.finance.widgets.CoaSetupVLayoutWidget;
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
 * The view class for the COA Setup screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class CoaSetupView extends ViewWithUiHandlers<CoaSetupUiHandlers> implements
		CoaSetupPresenter.MyView {
	/*
	 * The RAF layout that is used for laying out the promotion data
	 */
	RafViewLayout coaSetupLayout;

	/*
	 * A VLayout widget that is used to display the fields on the promotion data
	 * screen
	 */
	CoaSetupVLayoutWidget coaSetupVLayoutWidget;

	/*
	 * The toolstrip objects for the header
	 */
	ToolStrip accountToolStrip;

	/*
	 * The new and remove buttons for the accounts
	 */
	ToolStripButton addButton;
	ToolStripButton removeButton;
	
	/*
	 * Build the view and inject it
	 */
	@Inject
	public CoaSetupView() {

		coaSetupLayout = new RafViewLayout();

		accountToolStrip = new ToolStrip();
		accountToolStrip.setWidth100();

		addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add Account");
		addButton.setTitle("Add");

		removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/delete.png");
		removeButton.setTooltip("Remove Account");
		removeButton.setTitle("Remove");


		accountToolStrip.addButton(addButton);
		accountToolStrip.addButton(removeButton);
	

		coaSetupVLayoutWidget = new CoaSetupVLayoutWidget(this);

		coaSetupLayout.setMembers(accountToolStrip, coaSetupVLayoutWidget);
		removeButton.disable();

		bindCustomUiHandlers();
	}

	protected void bindCustomUiHandlers() {
		//Click Handler Add Account Button
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
				coaSetupVLayoutWidget.getAccountListGrid().startEditingNew();
				
			}
		});
		//ClickHandler for Remove Account Button
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
									coaSetupVLayoutWidget.getAccountListGrid()
											.removeSelectedData();
									refreshAccountData();
									SC.say("Data Removed");
								} else {

								}
							}
						});
			}
		});
		
		coaSetupVLayoutWidget.bindCustomUiHandlers();
		
	}

	@Override
	public Widget asWidget() {

		return coaSetupLayout;
	}

	@Override
	public void loadAccountData(DataSource dataSource) {

		coaSetupVLayoutWidget.loadAccountData(dataSource);
	}

	@Override
	public void refreshAccountData() {

		DSCallback callBack = new DSCallback() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				coaSetupVLayoutWidget.getAccountListGrid().setData(response.getData());
			}
		};
		coaSetupVLayoutWidget.getAccountListGrid().getDataSource().fetchData(coaSetupVLayoutWidget.getAccountListGrid().getFilterEditorCriteria(), callBack);

	}

	/**
	 * @return the removeButton
	 */
	public ToolStripButton getRemoveButton() {
		return removeButton;
	}

}