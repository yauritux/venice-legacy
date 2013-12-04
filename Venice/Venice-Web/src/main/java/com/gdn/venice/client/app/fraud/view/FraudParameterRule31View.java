package com.gdn.venice.client.app.fraud.view;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.FraudParameterRule31Presenter;
import com.gdn.venice.client.app.fraud.view.handlers.FraudParameterRule31UiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Presenter for Fraud Parameter 31 - Genuine List Transaction
 * 
 * @author Roland
 */

public class FraudParameterRule31View extends ViewWithUiHandlers<FraudParameterRule31UiHandlers> implements FraudParameterRule31Presenter.MyView {

	RafViewLayout fraudParameterRule31Layout;
	ListGrid fraudParameterRule31ListGrid = new ListGrid();

	@Inject
	public FraudParameterRule31View() {

		fraudParameterRule31Layout = new RafViewLayout();
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();

		ToolStripButton addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add to Genuine List");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/remove.png");
		removeButton.setTooltip("Remove from Genuine List");
		removeButton.setTitle("Remove");

		toolStrip.addButton(addButton);
		toolStrip.addButton(removeButton);
				
		fraudParameterRule31ListGrid.setAutoFetchData(false);
		fraudParameterRule31ListGrid.setCanResizeFields(true);
		fraudParameterRule31ListGrid.setShowFilterEditor(true);
		fraudParameterRule31ListGrid.setCanSort(true);
		fraudParameterRule31ListGrid.setCanEdit(true);
		fraudParameterRule31ListGrid.setSelectionType(SelectionStyle.SIMPLE);
		fraudParameterRule31ListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		fraudParameterRule31ListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		fraudParameterRule31ListGrid.setShowRowNumbers(true);
		fraudParameterRule31Layout.setMembers(toolStrip);			
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fraudParameterRule31ListGrid.startEditingNew();
			}
		});		

		fraudParameterRule31ListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				fraudParameterRule31ListGrid.saveAllEdits();
				refreshFraudParameterRule31Data();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}				
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							fraudParameterRule31ListGrid.removeSelectedData(new DSCallback() {
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									refreshFraudParameterRule31Data();
									SC.say("Data Removed");
								}
							}, null);
						}
					}
				});
			}
		});
	}

	@Override
	public Widget asWidget() {
		return fraudParameterRule31Layout;
	}

	protected void bindCustomUiHandlers() {
		fraudParameterRule31ListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudParameterRule31Data();
			}
		});
	}

	@Override
	public void loadFraudParameterRule31Data(DataSource dataSource) {		
		//populate listgrid
		fraudParameterRule31ListGrid.setDataSource(dataSource);
		fraudParameterRule31ListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_ID).setCanEdit(false);
		fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_ID).setHidden(true);
		fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_ID).setWidth(70);
		fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_EMAIL).setWidth(250);
		fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_CCNUMBER).setWidth(200);
 		
		//validation
		//required
		fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_EMAIL).setRequired(true);
		fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_CCNUMBER).setRequired(true);
	
				
		//length
		LengthRangeValidator lengthRangeValidatorBinNumber = new LengthRangeValidator();  
		lengthRangeValidatorBinNumber.setMax(100);  
	    fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_EMAIL).setValidators(lengthRangeValidatorBinNumber);
	     
	    LengthRangeValidator lengthRangeValidatorDesc = new LengthRangeValidator();  
	    lengthRangeValidatorDesc.setMax(100);  
	    fraudParameterRule31ListGrid.getField(DataNameTokens.FRDPARAMETERRULE31_CCNUMBER).setValidators(lengthRangeValidatorDesc);
        
        fraudParameterRule31Layout.addMember(fraudParameterRule31ListGrid);
        bindCustomUiHandlers();
	}
	
	public void refreshFraudParameterRule31Data() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudParameterRule31ListGrid.setData(response.getData());
			}
		};
		fraudParameterRule31ListGrid.getDataSource().fetchData(fraudParameterRule31ListGrid.getFilterEditorCriteria(), callBack);
	}
}
