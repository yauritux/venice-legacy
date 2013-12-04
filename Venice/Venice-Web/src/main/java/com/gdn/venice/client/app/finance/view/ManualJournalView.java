package com.gdn.venice.client.app.finance.view;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.presenter.ManualJournalPresenter;
import com.gdn.venice.client.app.finance.view.handlers.ManualJournalUiHandlers;
import com.gdn.venice.client.app.finance.widgets.JournalVLayoutWidget;
import com.gdn.venice.client.util.PrintUtility;
import com.gdn.venice.client.widgets.RafViewLayout;
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
 * View for Manual JournalVLayoutWidget
 * 
 * @author Henry Chandra
 */
public class ManualJournalView extends
ViewWithUiHandlers<ManualJournalUiHandlers> implements
ManualJournalPresenter.MyView {
	RafViewLayout manualJournalLayout;

	JournalVLayoutWidget manualJournal;

	ToolStrip manualJournalToolStrip;
	ToolStrip detailManualJournalToolStrip1;
	ToolStrip detailManualJournalToolStrip2;
	
	ToolStripButton newButton;
	ToolStripButton removeButton; 
	ToolStripButton printButton;

	@Inject
	public ManualJournalView() {
		manualJournalLayout = new RafViewLayout();

		manualJournalToolStrip = new ToolStrip();
		manualJournalToolStrip.setWidth100();

		newButton = new ToolStripButton();  
		newButton.setIcon("[SKIN]/icons/add.png");  
		newButton.setTooltip("Add New Manual Journal");
		newButton.setTitle("Add");

		removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/delete.png");  
		removeButton.setTooltip("Delete Current Manual Journal");
		removeButton.setTitle("Remove");
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Manual Journal List");
		printButton.setTitle("Print");
		
		manualJournalToolStrip.addButton(newButton);
		manualJournalToolStrip.addButton(removeButton);
		
		manualJournalToolStrip.addSeparator();
		manualJournalToolStrip.addButton(printButton);
		
		manualJournal = new JournalVLayoutWidget(true, this); 

		manualJournalLayout.setMembers(manualJournalToolStrip, manualJournal);

		bindCustomUiHandlers();
	}
	
	@Override
	public void loadJournalData(DataSource dataSource) {
		manualJournal.loadJournalData(dataSource);
	}

	@Override
	public Widget asWidget() {
		return manualJournalLayout;
	}
	
	@Override
	public void refreshManualJournalData() {
		manualJournal.refreshJournalData();
	}

	protected void bindCustomUiHandlers() {
		newButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().onNewManualJournal(FinanceData.getManualJournalDetailData(null));				
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedManualJournals = manualJournal.getSelectedJournals(); 
				
				HashMap<String, String> manualJournalMap = new HashMap<String, String>();
				
				for (int i=0;i<selectedManualJournals.length;i++) {
					manualJournalMap.put("DELETEDJOURNALAPPROVALGROUPID" + i, selectedManualJournals[i].getAttributeAsString(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
				}
				
				getUiHandlers().onDeleteManualJournalClicked(manualJournalMap);
			}
		});
		
		printButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(manualJournalLayout);	
			}
		});
		manualJournal.bindCustomUiHandlers();
	}
	
	public ManualJournalUiHandlers getManualJournalUiHandlers() {
		return getUiHandlers();
	}

	@Override
	public void loadManualJournalDetail(DataSource dataSource, LinkedHashMap<String, String> accountMap, ListGridRecord record) {
		manualJournal.createOrEditManualJournalDetail(dataSource, accountMap, record);
	}
}
