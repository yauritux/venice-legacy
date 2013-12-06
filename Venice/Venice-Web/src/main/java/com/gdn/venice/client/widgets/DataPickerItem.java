package com.gdn.venice.client.widgets;

import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class DataPickerItem extends TextItem {
	public DataPickerItem() {
		//use default trigger icon here. User can customize.
		//[SKIN]/DynamicForm/default_formItem_icon.gif
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
			public void onFormItemClick(FormItemIconClickEvent event) {  
				System.out.println("Do nothing for now");
			}  
		});  
		setIcons(searchPicker);


	}

}
