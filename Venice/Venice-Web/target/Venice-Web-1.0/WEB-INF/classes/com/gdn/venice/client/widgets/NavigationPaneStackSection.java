package com.gdn.venice.client.widgets;

import com.gdn.venice.client.ui.data.NavigationPaneTreeNodeRecord;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;

public class NavigationPaneStackSection extends SectionStackSection {

	private NavigationPaneTreeNodeRecord[] sectionData;
	private Tree tree;
	private TreeGrid treeGrid;

	public NavigationPaneStackSection(String sectionName, String sectionId, NavigationPaneTreeNodeRecord[] sectionData) {
		super(sectionName);

		this.sectionData = sectionData;

		treeGrid = new TreeGrid();
		treeGrid.setID(sectionId);

		treeGrid.setShowHeader(false);
		treeGrid.setSelectionType(SelectionStyle.SINGLE);

		tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		
		tree.setNameProperty("NavigationName");
		tree.setIdField("NavigationId");
		tree.setParentIdField("NavigationParent");
		tree.setShowRoot(true);
		
		tree.setData(sectionData);

		treeGrid.addDrawHandler(new DrawHandler() {
			public void onDraw(DrawEvent event) {
				tree.openAll();
			}
		});
		treeGrid.setData(sectionData);

		treeGrid.setData(tree);

		this.addItem(treeGrid);
		this.setExpanded(true);  
	}

	public TreeGrid getTreeGrid() {
		return treeGrid;
	}

	public NavigationPaneTreeNodeRecord getTreeNodeForPageName(String pageName) {
		for (int i = 0; i < this.sectionData.length; i++) { 
			NavigationPaneTreeNodeRecord record = this.sectionData[i];
			if (pageName.equals(record.getPageName())) {
				return this.sectionData[i];
			}
		}
		return null;
	}

	public void setSectionData(NavigationPaneTreeNodeRecord[] sectionData) {
		this.sectionData = sectionData;
	}

	public NavigationPaneTreeNodeRecord[] getSectionData() {
		return sectionData;
	}
}
