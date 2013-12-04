package com.gdn.venice.client.widgets;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CrudDetailManagementTab extends Tab {
	ToolStrip crudDetailToolStrip;
	private ListGrid crudDetailListGrid;
	private ToolStripButton saveButton;
	
	
	public CrudDetailManagementTab(ListGrid customCrudDetailListGrid, String title, DataSource detailDataSource,ListGridField... detailListGridFields) {
		super(title);
		
		VLayout layout = new VLayout();
		layout.setMargin(5);
		
		if (customCrudDetailListGrid!=null) {
			crudDetailListGrid = customCrudDetailListGrid;
		} else {
			crudDetailListGrid = new ListGrid();
		}
		
		crudDetailListGrid.setWidth100();
		crudDetailListGrid.setHeight100();
		crudDetailListGrid.setShowAllRecords(true);
		crudDetailListGrid.setSortField(0);
		crudDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);  
		crudDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		crudDetailListGrid.setDataSource(detailDataSource);
		crudDetailListGrid.setAutoFetchData(true);
		crudDetailListGrid.setFields(detailListGridFields);
		
		crudDetailListGrid.setCanResizeFields(true);
		crudDetailListGrid.setCanEdit(true);  
		crudDetailListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);  
		crudDetailListGrid.setListEndEditAction(RowEndEditAction.NEXT);
		
		crudDetailToolStrip = new ToolStrip();  
		crudDetailToolStrip.setWidth100();
		crudDetailToolStrip.setPadding(2);
		
		saveButton = new ToolStripButton(); 
		saveButton.setIcon("[SKIN]/actions/save.png");  
		saveButton.setTooltip("Save");
		
		
		ToolStripButton newButton = new ToolStripButton();  
		newButton.setIcon("[SKIN]/icons/add.png");  
		newButton.setTooltip("Add New");
		
		newButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				crudDetailListGrid.startEditingNew();
			}
			
		});
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/delete.png");  
		removeButton.setTooltip("Delete");
		
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				crudDetailListGrid.removeSelectedData();
			}
			
		});
		
		setToolStripButtons(new ToolStripButton[] {newButton, removeButton, saveButton});

		layout.setMembers(crudDetailToolStrip, crudDetailListGrid);
		setPane(layout);
	}
	
	public ToolStripButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(ToolStripButton saveButton) {
		this.saveButton = saveButton;
	}

	private void setToolStripButtons(ToolStripButton... toolbarButtons) {
		for (int i=0;i<toolbarButtons.length;i++) {
			crudDetailToolStrip.addButton(toolbarButtons[i]);  
		} 
	}
	
	public ListGrid getCrudDetailListGrid() {
		return crudDetailListGrid;
	}

	public void setCrudDetailListGrid(ListGrid crudDetailListGrid) {
		this.crudDetailListGrid = crudDetailListGrid;
	}



}
