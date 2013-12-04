package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class ReturDataViewerReturDetailTab extends Tab {
	DynamicForm returDetailForm;
	
	public ReturDataViewerReturDetailTab(String title, DataSource returDetailData) {
		super(title);
		
		VLayout returDetailLayout = new VLayout();
		
		returDetailForm = new DynamicForm();
		returDetailForm.setDataSource(returDetailData);  
		returDetailForm.setUseAllDataSourceFields(true);  
		
		returDetailForm.setNumCols(2);
		
		returDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(returDetailData));
		Util.formatFormItemAsCurrency(returDetailForm.getField(DataNameTokens.VENRETUR_AMOUNT));
		returDetailLayout.setMembers(returDetailForm);

		setPane(returDetailLayout);
	}
	
	public String getReturId() {
		return (String) returDetailForm.getField(DataNameTokens.VENRETUR_RETURID).getValue();
	}
	
	public DynamicForm getReturDetailForm() {
		return returDetailForm;

	}
}
