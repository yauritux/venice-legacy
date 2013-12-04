package com.gdn.venice.client.app.finance.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.CoaSetupData;
import com.gdn.venice.client.app.finance.view.CoaSetupView;
import com.gdn.venice.client.util.Util;
import com.gwtplatform.mvp.client.View;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * CoaSetupVLayoutWidget - a widget class for displaying Accounts
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class CoaSetupVLayoutWidget extends VLayout{
private static final int LIST_HEIGHT = 230;
	
	/*
	 * The main list grid for the accounts
	 */
	ListGrid accountListGrid;

	/*
	 * The vertical layout used to lay out the top accounts list
	 */
	VLayout accountListLayout;

	/*
	 * The account view object
	 */
	View view;
	
	
	/**
	 * Constructor that takes the view and instantiates 
	 * it for reference as well as builds the UI
	 * @param view
	 */
	public CoaSetupVLayoutWidget(View view) {

		this.view = view;

		setWidth100();
		setHeight100();

		accountListLayout = new VLayout();
		accountListLayout.setHeight(LIST_HEIGHT);
		accountListLayout.setShowResizeBar(true);

		accountListGrid = new ListGrid();

		accountListGrid.setShowAllRecords(true);
		accountListGrid.setSortField(0);

		accountListGrid.setSelectionType(SelectionStyle.SIMPLE);
		accountListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		accountListGrid.setAutoFetchData(false);
		accountListGrid.setShowFilterEditor(true);
		accountListGrid.setCanEdit(true);
		accountListGrid.setModalEditing(true);
		accountListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		accountListGrid.setCanResizeFields(true);
		accountListGrid.setShowRowNumbers(true);
		accountListGrid.addEditCompleteHandler(new EditCompleteHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.EditCompleteHandler#onEditComplete(com.smartgwt.client.widgets.grid.events.EditCompleteEvent)
			 */
			@Override
			public void onEditComplete(EditCompleteEvent event) {				
				accountListGrid.saveAllEdits();
				accountListGrid.clearCriteria();
				refreshAccountData();
				
			}
		});
		
		accountListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
					 */
					@Override
					public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
						refreshAccountData();
					}
				});
		setMembers(accountListLayout);
	}
	
	/**
	 * Refreshes the account data from the data source
	 */
	public void refreshAccountData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				accountListGrid.setData(response.getData());
			}
		};
		accountListGrid.getDataSource().fetchData(	accountListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Loads the list of Account data 
	 * 
	 * @param dataSource
	 *            account data datasource
	 */
	public void loadAccountData(DataSource dataSource) {
		
		accountListGrid.setDataSource(dataSource);
		accountListGrid.setShowFilterEditor(true);
		accountListGrid.setShowAllRecords(true);
		accountListGrid.setCanEdit(true);
		accountListGrid.setSortField(0);
		accountListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		accountListGrid.setCanSort(true);
		accountListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));

		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTID).setWidth("5%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTID).setHidden(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTID).setCanEdit(false);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER).setRequired(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER).setWidth("5%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTNUMBER).setHidden(false);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTDESC).setRequired(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTDESC).setWidth("20%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_ACCOUNTDESC).setHidden(false);
		
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC).setRequired(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC).setWidth("5%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC).setHidden(true);
		/*
		 * Initialize the datasource for account type combo box
		 */
		DataSource accountTypeComboBoxDs = CoaSetupData.getAccountTypeData();
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID).setRequired(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID).setCanEdit(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID).setWidth("10%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID).setOptionDataSource(accountTypeComboBoxDs);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID).setDisplayField(DataNameTokens.FINACCOUNTTYPE_ACCOUNTTYPEDESC);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID).setValueField(DataNameTokens.FINACCOUNTTYPE_ACCOUNTTYPEID);
		
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC).setRequired(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC).setWidth("5%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC).setHidden(true);
		
		/*
		 * Initialize the datasource for account category combo box
		 */
		DataSource accountCategoryComboBoxDs = CoaSetupData.getAccountCategoryData();
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID).setRequired(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID).setWidth("10%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID).setCanEdit(true);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID).setOptionDataSource(accountCategoryComboBoxDs);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID).setDisplayField(DataNameTokens.FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID).setValueField(DataNameTokens.FINACCOUNTCATEGORY_ACCOUNTCATEGORYID);

		accountListGrid.getField(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT).setDefaultValue(false);
		accountListGrid.getField(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT).setWidth("5%");
		accountListGrid.getField(DataNameTokens.FINACCOUNT_SUMMARYACCOUNT).setType(ListGridFieldType.BOOLEAN); 

		accountListLayout.addMember(accountListGrid);
	}
	
	/**
	 * Method to bind the custom UI handlers to the view
	 */
	public void bindCustomUiHandlers() {
		accountListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

		/* (non-Javadoc)
		 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
		 */
		@Override
		public void onSelectionChanged(SelectionEvent event) {
			ListGridRecord[] selectedRecords = accountListGrid.getSelection();		
			/*
			 * Enable the remove button if there is a selected row
			 */
				if (selectedRecords.length == 0) {
					((CoaSetupView) view).getRemoveButton().disable();
				} else {
					((CoaSetupView) view).getRemoveButton().enable();
				}
			}
		});
	}
	
	/**
	 * Returns the account list grid object
	 * @return the accountListGrid
	 */
	public ListGrid getAccountListGrid() {
		return accountListGrid;
	}
}
