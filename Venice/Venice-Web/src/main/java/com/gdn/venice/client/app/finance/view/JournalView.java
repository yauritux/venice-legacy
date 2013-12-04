package com.gdn.venice.client.app.finance.view;

import java.util.ArrayList;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.JournalPresenter;
import com.gdn.venice.client.app.finance.view.handlers.JournalUiHandlers;
import com.gdn.venice.client.app.finance.widgets.JournalVLayoutWidget;
import com.gdn.venice.client.util.PrintUtility;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for the journal screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class JournalView extends
ViewWithUiHandlers<JournalUiHandlers> implements
JournalPresenter.MyView {

	/*
	 * View layout for the journal view
	 */
	RafViewLayout journalLayout;
	
	/*
	 * Vertical layoyut widget that lays out the basic screen
	 */
	JournalVLayoutWidget journalVLayoutWidget;
	
	ToolStripButton submitForApprovalButton;
	
	ToolStripButton printButton;
	
	ToolStripButton exportButton;
	
	/**
	 * Basic constructor to build the journal view 
	 */
	@Inject
	public JournalView() {

		journalLayout = new RafViewLayout();
		
		ToolStrip journalToolStrip = new ToolStrip();
		journalToolStrip.setWidth100();
		journalToolStrip.setPadding(2);
		
		submitForApprovalButton = new ToolStripButton();
		submitForApprovalButton.setIcon("[SKIN]/icons/process.png");
		submitForApprovalButton.setTooltip("Submit for Approval");
		submitForApprovalButton.setTitle("Submit");
		submitForApprovalButton.disable();
		
		journalToolStrip.addButton(submitForApprovalButton);
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Journal Details");
		printButton.setTitle("Print");
		
		journalToolStrip.addButton(printButton);

		exportButton = new ToolStripButton();
		exportButton.setIcon("[SKIN]/icons/notes_accept.png");
		exportButton.setTooltip("Export to Excel");
		exportButton.setTitle("Export");
		exportButton.disable();

		journalToolStrip.addButton(exportButton);
		

		journalVLayoutWidget = new JournalVLayoutWidget(false, this); 

		journalLayout.setMembers(journalToolStrip, journalVLayoutWidget);
		
		submitForApprovalButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * Get the selected journal group ids and add them to
				 * a list then pass the list top the submit for
				 * approval handler.
				 */
				ListGridRecord[] selection = journalVLayoutWidget.getJournalList().getSelection();
				ArrayList<String> journalGroupIdList = new ArrayList<String>();
				for(int i=0; i < selection.length; ++i){
					journalGroupIdList.add(selection[i].getAttribute(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
				}
				getUiHandlers().onSubmitForApproval(journalGroupIdList);				
			}
		});
		
		exportButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				ListGridRecord[] selection = journalVLayoutWidget.getJournalList().getSelection();
				String journalGroupId = selection[0].getAttribute(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID);
				String host = GWT.getHostPageBaseURL();

				//If in debug mode then change the host URL to the servlet in the server side
				if(host.contains("8889")){
					host = "http://localhost:8090/";
				}
				
				/* 
				 * Somehow when the app is deployed in Geronimo the getHostPageBaseURL call
				 * adds the context root of "Venice/" as it is the web application.
				 * This does not happen in development mode as it is running in the root
				 * of the Jetty servlet container.
				 * 
				 * Consequently the context root needs to be removed because the servlet 
				 * being called has its own context root in a different web application.
				 */
				if(host.contains("Venice/")){
					host = host.substring(0, host.indexOf("Venice/"));
				}
												
				Window.open(host + "Venice/JournalReportLauncherServlet?journalGroupId=" + journalGroupId, "_blank", null);
			}
		});
		
		printButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(journalVLayoutWidget);
			}
		});


		bindCustomUiHandlers();
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.JournalPresenter.MyView#loadJournalData(com.smartgwt.client.data.DataSource)
	 */
	@Override
	public void loadJournalData(DataSource dataSource) {
		journalVLayoutWidget.loadJournalData(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.JournalPresenter.MyView#refreshJournalData()
	 */
	@Override
	public void refreshJournalData() {
		journalVLayoutWidget.refreshJournalData();
		
	}
	
	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return journalLayout;
	}

	/**
	 * Binds the custom UI hanlders to the view
	 */
	protected void bindCustomUiHandlers() {
		journalVLayoutWidget.bindCustomUiHandlers();
	}

	/**
	 * @return the submitForApprovalButton
	 */
	public ToolStripButton getSubmitForApprovalButton() {
		return submitForApprovalButton;
	}

	public ToolStripButton getExportButton() {
		return exportButton;
	}

}
