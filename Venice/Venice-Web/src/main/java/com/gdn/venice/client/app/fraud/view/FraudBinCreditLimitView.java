package com.gdn.venice.client.app.fraud.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.FraudBinCreditLimitPresenter;
import com.gdn.venice.client.app.fraud.view.handlers.FraudBinCreditLimitUiHandlers;
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
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Bin Credit Limit
 * 
 * @author Roland
 */

public class FraudBinCreditLimitView extends
		ViewWithUiHandlers<FraudBinCreditLimitUiHandlers> implements
		FraudBinCreditLimitPresenter.MyView {

	RafViewLayout fraudBinCreditLimitLayout;
	ListGrid binCreditLimitListGrid = new ListGrid();

	@Inject
	public FraudBinCreditLimitView() {

		fraudBinCreditLimitLayout = new RafViewLayout();
		ToolStrip binCreditLimitToolStrip = new ToolStrip();
		binCreditLimitToolStrip.setWidth100();

		ToolStripButton addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add to BIN Credit Limit");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/remove.png");
		removeButton.setTooltip("Remove BIN Credit Limit");
		removeButton.setTitle("Remove");

		binCreditLimitToolStrip.addButton(addButton);
		binCreditLimitToolStrip.addButton(removeButton);
				
		binCreditLimitListGrid.setAutoFetchData(false);
		binCreditLimitListGrid.setCanResizeFields(true);
		binCreditLimitListGrid.setShowFilterEditor(true);
		binCreditLimitListGrid.setCanSort(true);
		binCreditLimitListGrid.setCanEdit(true);
		binCreditLimitListGrid.setSelectionType(SelectionStyle.SIMPLE);
		binCreditLimitListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		binCreditLimitListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		binCreditLimitListGrid.setShowRowNumbers(true);
		fraudBinCreditLimitLayout.setMembers(binCreditLimitToolStrip);			
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				binCreditLimitListGrid.startEditingNew();
			}
		});		

		binCreditLimitListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				binCreditLimitListGrid.saveAllEdits();
				refreshBinCreditLimitData();
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
							binCreditLimitListGrid.removeSelectedData(new DSCallback() {
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									refreshBinCreditLimitData();
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
		return fraudBinCreditLimitLayout;
	}

	protected void bindCustomUiHandlers() {
		binCreditLimitListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshBinCreditLimitData();
			}
		});
	}

	@Override
	public void loadFraudBinCreditLimitData(DataSource dataSource,  Map<String,String> card) {		
		//populate combo box
		dataSource.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENCARDTYPE_CARDTYPEID).setValueMap(card);

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();  
		map.put("HIGH", "High");
		map.put("MEDIUM", "Medium");
		map.put("LOW", "Low");
		dataSource.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_SEVERITY).setValueMap(map);
		
		//populate listgrid
		binCreditLimitListGrid.setDataSource(dataSource);
		binCreditLimitListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		Util.formatListGridFieldAsCurrency(binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE));
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID).setCanEdit(false);
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID).setHidden(true);
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINCREDITLIMITESTIMATEID).setWidth(70);
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_SEVERITY).setWidth(100);
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINNUMBER).setWidth(100);
 		
		//validation
		//required
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENBANK_BANKNAME).setRequired(true);
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_VENCARDTYPE_CARDTYPEID).setRequired(true);
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_SEVERITY).setRequired(true);
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINNUMBER).setRequired(true);		
		binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE).setRequired(true);
				
		//length
		LengthRangeValidator lengthRangeValidatorBinNumber = new LengthRangeValidator();  
		lengthRangeValidatorBinNumber.setMax(6);  
	    binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_BINNUMBER).setValidators(lengthRangeValidatorBinNumber);
	     
	    LengthRangeValidator lengthRangeValidatorDesc = new LengthRangeValidator();  
	    lengthRangeValidatorDesc.setMax(1000);  
	    binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_DESCRIPTION).setValidators(lengthRangeValidatorDesc);
	     
	    //number
        binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE).setType(ListGridFieldType.INTEGER);  

        IntegerRangeValidator integerRangeValidator2 = new IntegerRangeValidator(); 
        integerRangeValidator2.setMin(0); 
        binCreditLimitListGrid.getField(DataNameTokens.VENBINCREDITLIMITESTIMATE_CREDITLIMITESTIMATE).setValidators(integerRangeValidator2);
        
        fraudBinCreditLimitLayout.addMember(binCreditLimitListGrid);
        bindCustomUiHandlers();
	}
	
	public void refreshBinCreditLimitData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				binCreditLimitListGrid.setData(response.getData());
			}
		};
		binCreditLimitListGrid.getDataSource().fetchData(binCreditLimitListGrid.getFilterEditorCriteria(), callBack);
	}
}
