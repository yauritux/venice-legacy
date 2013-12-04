package com.gdn.venice.client.widgets;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class SearchFormItem extends CanvasItem {
	SelectItem slctOperator;
	TextItem valueItem;
	DynamicForm form;
	
	int dataType=DATA_TYPE_OTHER;
	int mode;
	

	
	public static int DATA_TYPE_OTHER=0;
	public static int DATA_TYPE_NUMERIC=1;
	public static int DATA_TYPE_DATE=2;
	public static int DATA_TYPE_STRING=3;
	public static int DATA_TYPE_ID=4;
	
	public SearchFormItem(int dataType) {
		this.dataType = dataType;
		initFormItem();
		setMode(CrudMasterManagementLayout.DATA_MODE);
	}
	
	public SearchFormItem(int dataType, int mode) {
		this.dataType = dataType;
		initFormItem();
		setMode(mode);
	}
	
	private void initFormItem() {
		form = new DynamicForm();

		slctOperator = new SelectItem();
		slctOperator.setWidth("*");
		slctOperator.setShowTitle(false);
		slctOperator.setValueMap("<","=",">");
		slctOperator.setVisible(false);

		valueItem = (TextItem) adjustValueItemByDataType();
		form.setItems(slctOperator, valueItem);
		
		setCanvas(form);
	}
	
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	public int getDataType() {
		return this.dataType;
	}
	
	private FormItem adjustValueItemByDataType() {
		FormItem valueItem=new FormItem();
		if (dataType==DATA_TYPE_STRING) {
			valueItem = new TextItem();
			valueItem.setShowTitle(false);
			valueItem.setWidth("*");
		} else if (dataType==DATA_TYPE_ID) {
			valueItem = new TextItem();
			PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
				public void onFormItemClick(FormItemIconClickEvent event) {  
					System.out.println("Do nothing for now");
				}  
			});  
			valueItem.setIcons(searchPicker);
		} else if (dataType==DATA_TYPE_NUMERIC) {
			valueItem = new TextItem();
			valueItem.setShowTitle(false);
			valueItem.setWidth("*");
		} 
		valueItem.setShowTitle(false);
		valueItem.setWidth("*");
		return valueItem;
		
	}
	
	@Override
	public void setValue(String value) {
		valueItem.setValue(value);
	}
	
	/**
	 * This method adjust the Select Item values based on Data Type of the Item
	 * 
	 * @return true if dataType is supported and the Select Item should be display, false if dataType is not supported, thus Select Item should be hidden 
	 */
	private boolean adjustSelectItemByDataType() {
		if (dataType == DATA_TYPE_NUMERIC) {
			slctOperator.setValueMap("<","=",">", "!=", "between");
			return true;
		} else if (dataType == DATA_TYPE_STRING) {
			slctOperator.setValueMap("=","!=","like");
			return true;
		} else if (dataType == DATA_TYPE_DATE) {
			slctOperator.setValueMap("<","=",">", "!=", "between");
			return true;
		} else {
			return false;
		}
	}
	
	public void setMode(int mode) {
		this.mode = mode;
		if (mode == CrudMasterManagementLayout.SEARCH_MODE && adjustSelectItemByDataType()) {
			form.setColWidths("20%","80%");
			form.setNumCols(2);
			slctOperator.setVisible(true);
		} else if (mode == CrudMasterManagementLayout.DATA_MODE) {
			form.setColWidths("100%");
			form.setNumCols(1);
			slctOperator.setVisible(false);
		}
	}
	
	public int getMode() {
		return this.mode;
	}
	
	

}
