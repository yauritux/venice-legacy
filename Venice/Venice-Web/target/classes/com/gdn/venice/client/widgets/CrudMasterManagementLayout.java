package com.gdn.venice.client.widgets;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CrudMasterManagementLayout extends VLayout {
	public static int DATA_MODE=0;
	public static int SEARCH_MODE=1;
	
	
	
	private final DynamicForm crudMasterForm;
	private ToolStripButton saveButton;
	private ToolStripButton removeButton;
	private ToolStripButton searchModeButton;
	private ToolStripButton searchButton;
	private ToolStrip crudMasterToolStrip;
	private TabSet crudDetailTabSet;

	public CrudMasterManagementLayout() {
		crudMasterToolStrip = new ToolStrip();  
		crudMasterToolStrip.setWidth100();
		crudMasterToolStrip.setPadding(2);
		
//		newButton = new ToolStripButton();  
//		newButton.setIcon("[SKIN]/icons/add.png");  
//		newButton.setTooltip("Add New Logistic Provider");
		
		saveButton = new ToolStripButton();  
		saveButton.setIcon("[SKIN]/icons/save.png");  
		saveButton.setTooltip("Save Current Logistic Provider");
		
		removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/delete.png");  
		removeButton.setTooltip("Delete Current Logistic Provider");
		
		searchModeButton = new ToolStripButton();  
		searchModeButton.setIcon("[SKIN]/icons/page_search.png");  
		searchModeButton.setTooltip("Search Mode");
		searchModeButton.setActionType(SelectionType.CHECKBOX);  
		
		searchModeButton.setSelected(false);
		searchModeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setSearchMode();
			}
			
		});
		
		searchButton = new ToolStripButton();  
		searchButton.setIcon("[SKIN]/icons/search.png");  
		searchButton.setTooltip("Search Mode");
		searchButton.setActionType(SelectionType.CHECKBOX);  
		
		setToolStripButtons(new ToolStripButton[] {searchModeButton, new ToolStripButton("|"), saveButton, removeButton, new ToolStripButton("|"), searchButton});
		
		crudMasterForm = new DynamicForm();
		
		crudDetailTabSet = new TabSet();

		crudDetailTabSet.setTabBarPosition(Side.TOP);
		crudDetailTabSet.setWidth100();
		crudDetailTabSet.setHeight100();
		
		setWidth100();
		setHeight100();
		setMembers(crudMasterToolStrip, crudMasterForm, crudDetailTabSet);
		
		setSearchMode();
	}
	
	private void setSearchMode() {
		if (searchModeButton.getSelected()) {
			FormItem[] formItems = crudMasterForm.getFields();
			for (int i=0;i<formItems.length;i++) {
				if (formItems[i] instanceof SearchFormItem) {
					((SearchFormItem) formItems[i]).setMode(SEARCH_MODE);
				} else {
					formItems[i].setDisabled(true);
				}
			}
		} else {
			FormItem[] formItems = crudMasterForm.getFields();
			for (int i=0;i<formItems.length;i++) {
				if (formItems[i] instanceof SearchFormItem) {
					((SearchFormItem) formItems[i]).setMode(DATA_MODE);
				} else {
					formItems[i].setDisabled(false);
				}
			}
		}
//		newButton.setDisabled(searchModeButton.getSelected());
		saveButton.setDisabled(searchModeButton.getSelected());
		removeButton.setDisabled(searchModeButton.getSelected());
		
		searchButton.setDisabled(!searchModeButton.getSelected());
		
		
		crudDetailTabSet.setDisabled(searchModeButton.getSelected());
	}
	
	
	public void setToolStripButtons(ToolStripButton... toolbarButtons) {
		for (int i=0;i<toolbarButtons.length;i++) {
			if (toolbarButtons[i].getTitle()!= null && toolbarButtons[i].getTitle().equals("|")) {
				crudMasterToolStrip.addSeparator();
			} else {
				crudMasterToolStrip.addButton(toolbarButtons[i]);
			}
		} 
	}
	
	public void setFormItems(FormItem... formItems) {
		crudMasterForm.setItems(formItems);
	}
	
	public void setDetailTabs(Tab... tabs ) {
		for (int i=0;i<tabs.length;i++) {
			crudDetailTabSet.addTab(tabs[i]);  
		} 

	}
	
	
}
