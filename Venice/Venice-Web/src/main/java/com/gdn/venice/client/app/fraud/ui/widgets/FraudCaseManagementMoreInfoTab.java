package com.gdn.venice.client.app.fraud.ui.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FocusEvent;
import com.smartgwt.client.widgets.form.fields.events.FocusHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Widget for Fraud Case Management 
 * 
 * @author Anto
 */

public class FraudCaseManagementMoreInfoTab extends Tab {
	DynamicForm orderDetailForm;
	TabSet moreInfoTabSet = new TabSet();
	Tab orderItemTab = new Tab("Related Order Item by Order");
	Tab fraudTab = new Tab("Related Fraud by Order");
	ListGrid selectMerchantList;
	ListGrid selectProdCatList;
	ListGrid selectProductList;
	ToolStripButton addToRelatedOrder = new ToolStripButton();
	final ListGrid relatedOrderListGrid = new ListGrid();
	final ListGrid orderItemListGrid = new ListGrid();	
	final ListGrid fraudListGrid = new ListGrid();	
	final DynamicForm advSearchform = new DynamicForm();
	final TextItem ipAddItem = new TextItem();
	final TextItem orderIdItem = new TextItem();
	final TextItem orderAmtFromItem = new TextItem();
	final TextItem orderAmtToItem = new TextItem();
	final TextItem custNameItem = new TextItem();
	final ComboBoxItem orderStatusItem = new ComboBoxItem();
	final TextItem custHPItem = new TextItem();
	final TextItem custEmailItem = new TextItem();
	final TextItem recpNameItem = new TextItem();
	final DateItem dateFromItem = new DateItem();
	final DateItem dateToItem = new DateItem();
	final TextItem userNameItem = new TextItem();
	final TextItem mercNameItem = new TextItem();
	final TextItem prodCatItem = new TextItem();
	final TextItem productItem = new TextItem();
	final TextItem recpHPItem = new TextItem();
	final TextItem recpEmailItem = new TextItem();
	final TextItem shippingAddItem = new TextItem();
	final ComboBoxItem logisticSvcItem = new ComboBoxItem();
	final TextItem creditCardMaskItem = new TextItem();
	public FraudCaseManagementMoreInfoTab(String title, DataSource orderDetailData, DataSource relatedOrder, DataSource orderItemData, DataSource relatedFraud, DataSource moreInfoData, final String caseId) {
		super(title);		
		VLayout moreInfoLayout = new VLayout();
		moreInfoLayout.setMargin(5);
				
		SectionStack moreInfoStack = new SectionStack();
		moreInfoStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		
		SectionStackSection findSection = new SectionStackSection("Find");
		findSection.setExpanded(true);
		
		VLayout advSearchLayout = new VLayout();
		advSearchLayout.setWidth100(); 
		advSearchLayout.setHeight(80);
				
		advSearchform.setWidth100();        
		
        ipAddItem.setTitle("IP Address");
        ipAddItem.setWidth("100%");
                
        orderIdItem.setTitle("Order ID");
        orderIdItem.setWidth("100%");        
        
        userNameItem.setTitle("User Name");
        userNameItem.setWidth("100%");
        
        orderStatusItem.setWidth("100%");
        orderStatusItem.setTitle("Order Status");
        orderStatusItem.setType("comboBox");
        
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("", "");
		map.put("C", "Confirmed");
		map.put("SF", "Suspicious Fraud");
		map.put("FC", "Fraud Confirmed");
		map.put("FP", "Fraud Passed");
		map.put("CX", "Closed"); 
        orderStatusItem.setValueMap(map);
               
        prodCatItem.setTitle("Product Cat.");
        prodCatItem.setWidth("100%");
        PickerIcon searchProdCatPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
			public void onFormItemClick(FormItemIconClickEvent event) {
				buildSelectProdCatWindow().show();
			}  
		});  
        
        prodCatItem.setIcons(searchProdCatPicker);
        
        prodCatItem.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				productItem.setValue("");
			}
		});
        
        productItem.setTitle("Product");
        productItem.setWidth("100%");
        PickerIcon searchProductPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
			public void onFormItemClick(FormItemIconClickEvent event) {  
				buildSelectProductWindow(prodCatItem.getValueAsString()).show();
			}  
		});  
        productItem.setIcons(searchProductPicker);
                
	    orderAmtFromItem.setTitle("Amount From");
	    orderAmtFromItem.setWidth("100%");
	    orderAmtFromItem.setKeyPressFilter("[0-9]");
	    orderAmtFromItem.setTextAlign(Alignment.RIGHT);
	    orderAmtFromItem.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (orderAmtFromItem.getValueAsString() != null) {
					NumberFormat nf = NumberFormat.getFormat("0,000");
					int amountFrom = Integer.parseInt(orderAmtFromItem.getValueAsString());
					if (Integer.toString(amountFrom).length()>3) {
						orderAmtFromItem.setValue("Rp. " + nf.format(new Double(Integer.toString(amountFrom)).doubleValue()));
					} else {
						orderAmtFromItem.setValue("Rp. " + Integer.toString(amountFrom));
					}
				}
			}
		});
	    
	    orderAmtFromItem.addFocusHandler(new FocusHandler() {			
			@Override
			public void onFocus(FocusEvent event) {
				if (orderAmtFromItem.getValueAsString() != null)
					orderAmtFromItem.setValue(orderAmtFromItem.getValueAsString().replace("Rp. ", "").replaceAll(",", ""));
			}
		});
	    	    
	    orderAmtToItem.setTitle("To");
	    orderAmtToItem.setWidth("100%");
	    orderAmtToItem.setKeyPressFilter("[0-9]"); 
	    orderAmtToItem.setTextAlign(Alignment.RIGHT);
	    orderAmtToItem.addBlurHandler(new BlurHandler() {			
			@Override
			public void onBlur(BlurEvent event) {
				if (orderAmtToItem.getValueAsString() != null) {
					NumberFormat nf = NumberFormat.getFormat("0,000");
					int amountTo = Integer.parseInt(orderAmtToItem.getValueAsString());
					if (Integer.toString(amountTo).length()>3) {
						orderAmtToItem.setValue("Rp. " + nf.format(new Double(Integer.toString(amountTo)).doubleValue()));
					} else {
						orderAmtToItem.setValue("Rp. " + Integer.toString(amountTo));
					}
				}
			}
		});
	    
	    orderAmtToItem.addFocusHandler(new FocusHandler() {			
			@Override
			public void onFocus(FocusEvent event) {
				if (orderAmtToItem.getValueAsString() != null)
					orderAmtToItem.setValue(orderAmtToItem.getValueAsString().replace("Rp. ", "").replaceAll(",", ""));
			}
		});

        creditCardMaskItem.setTitle("Credit Card No.");
        creditCardMaskItem.setWidth("100%");
        creditCardMaskItem.setMask("######-XXXXXXX-###");
        
        dateFromItem.setTitle("Date From");
        dateFromItem.setDefaultValue(new Date());
      
        dateToItem.setTitle("To");  
        dateToItem.setDefaultValue(new Date());
        
        shippingAddItem.setTitle("Shipping Add.");
        shippingAddItem.setWidth("100%");
        
        logisticSvcItem.setWidth("100%");
        logisticSvcItem.setTitle("Logistic Svc.");
        logisticSvcItem.setType("comboBox");
        logisticSvcItem.setValueMap("", "Express", "Regular");
        
        custNameItem.setTitle("Cust. Name");
        custNameItem.setWidth("100%");
        
        custHPItem.setTitle("Cust. Mobile");
        custHPItem.setWidth("100%");
        
        custEmailItem.setTitle("Cust. e-mail");
        custEmailItem.setWidth("100%");
        
        recpNameItem.setTitle("Recp. Name");
        recpNameItem.setWidth("100%");
        
        recpHPItem.setTitle("Recp. Mobile");
        recpHPItem.setWidth("100%");
        
        recpEmailItem.setTitle("Recp. e-mail");
        recpEmailItem.setWidth("100%");
        
        mercNameItem.setTitle("Merchant");
        mercNameItem.setWidth("100%");
        
        PickerIcon searchMerchantPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
			public void onFormItemClick(FormItemIconClickEvent event) {  
				buildSelectMerchantWindow().show();
			}  
		});  
        mercNameItem.setIcons(searchMerchantPicker);
        						
        advSearchform.setNumCols(6);
        advSearchform.setFields(new FormItem[] {ipAddItem, prodCatItem, dateFromItem, orderIdItem, productItem,
        		dateToItem, orderAmtFromItem, creditCardMaskItem, userNameItem, orderAmtToItem, orderStatusItem, recpNameItem, mercNameItem,
        		custNameItem, recpHPItem, shippingAddItem, custHPItem, recpEmailItem, logisticSvcItem, custEmailItem});

        advSearchLayout.setMembers(advSearchform);
        findSection.addItem(advSearchLayout);
        
        //Order detail
        final SectionStackSection detailSection = new SectionStackSection("Current Order vs. Order(s) to Relate");		
		VLayout currentorderLayout = new VLayout();
		HLayout orderDetailLayout = new HLayout();
		orderDetailLayout.setWidth(400);
		orderDetailLayout.setHeight(100);
		orderDetailForm = new DynamicForm();
		orderDetailForm.setWidth100();
		orderDetailForm.setTitleWidth(130);
		orderDetailForm.setDataSource(orderDetailData);  
		orderDetailForm.setUseAllDataSourceFields(true);  
		orderDetailForm.setNumCols(4);
		orderDetailForm.setMargin(10);
		orderDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(orderDetailData));		
		Util.formatFormItemAsCurrency(orderDetailForm.getField(DataNameTokens.VENORDERITEM_PRODUCTPRICE));
		Util.formatFormItemAsCurrency(orderDetailForm.getField(DataNameTokens.VENORDER_AMOUNT));
		orderDetailForm.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setValueFormatter(new FormItemValueFormatter() {			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (form.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).equals("true")) 
                	{
                		return "Yes";
                	} else {
                		return "No";
                	}
			}
		});
		
		orderDetailForm.getField(DataNameTokens.VENORDER_RMAFLAG).setValueFormatter(new FormItemValueFormatter() {			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (form.getField(DataNameTokens.VENORDER_RMAFLAG).equals("true")) 
                	{
                		return "Yes";
                	} else {
                		return "No";
                	}
			}
		});
		
		orderDetailForm.getField(DataNameTokens.VENORDER_FULFILLMENTSTATUS).setValueFormatter(new FormItemValueFormatter() {			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (form.getField(DataNameTokens.VENORDER_FULFILLMENTSTATUS).equals("true")) 
                	{
                		return "Yes";
                	} else {
                		return "No";
                	}
			}
		});
		
//		orderDetailForm.getField(DataNameTokens.VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		orderDetailLayout.setMembers(orderDetailForm);
		
		currentorderLayout.setMembers(orderDetailLayout);
		setPane(currentorderLayout);        

		moreInfoTabSet.setWidth100();
		moreInfoTabSet.setHeight100();		

		//--------------------- MORE INFO ORDER TAB --------------------------------

		VLayout orderLayout = new VLayout();
		
		ToolStrip orderToolStrip = new ToolStrip();  
		orderToolStrip.setWidth100();
		orderToolStrip.setPadding(2);
		
		addToRelatedOrder.setIcon("[SKIN]/icons/pages_add.png");  
		addToRelatedOrder.setTitle("Add to Related Order");
		addToRelatedOrder.setTooltip("Add to related order");
		
		orderToolStrip.addButton(addToRelatedOrder);
		
		Tab orderTab = new Tab("Order(s) to Relate");
		orderLayout.setMembers(orderToolStrip, relatedOrderListGrid);
		orderTab.setPane(orderLayout);
		
		addToRelatedOrder.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = relatedOrderListGrid.getSelection();
				if (selectedRecords.length > 0) {
					SC.ask("Are you sure you want to relate this order?", new BooleanCallback() {					
						@Override
						public void execute(Boolean value) {
							if(value != null && value){		
								//add multiple order to related order
								ListGridRecord[] selectedRecords = relatedOrderListGrid.getSelection();
								ArrayList<String> orderIds = new ArrayList<String>();
								for (int i=0;i<selectedRecords.length;i++) {
									String caseId = selectedRecords[i].getAttributeAsString(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
									String orderId = selectedRecords[i].getAttributeAsString(DataNameTokens.VENORDER_ORDERID);
									orderIds.add(caseId + "~" + orderId);
								}
								
					           	RPCRequest request=new RPCRequest();			
					    		HashMap<String,String>map = new HashMap<String,String>( 11 );
					    		for (int i=0;i<orderIds.size();i++) {
					    			map.put(DataNameTokens.VENORDER_ORDERID+(i+1), orderIds.get(i));
					    		}
					    		
					    		String orderId = map.toString();					    		
					    		request.setData(orderId);				            	
				        		request.setActionURL(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?method=addFraudCaseRelatedOrderData&type=RPC");
				        		request.setHttpMethod("POST");
				        		request.setUseSimpleHttp(true);
				        		request.setShowPrompt(false);
				        		
				        		RPCManager.sendRequest(request, 
				    				new RPCCallback () {
				    					public void execute(RPCResponse response,	Object rawData, RPCRequest request) {
				    						String rpcResponse = rawData.toString();				    				
				    						if (rpcResponse.startsWith("0")) {
				    							SC.say("Data Added");
				    							refreshOrderListData();
				    						} else {
				    							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
				    						}
				    					}
				        		});								
								bindCustomUiHandlers();
							}
						}
					});
				}
				else
					SC.say("Please select at least 1 order to relate");				
			}
		});
		
		//--------------------- MORE INFO ORDER ITEM TAB --------------------------------

		orderItemListGrid.setGroupByField(DataNameTokens.VENORDER_WCSORDERID);
		orderItemListGrid.setGroupStartOpen(GroupStartOpen.ALL);
		orderItemListGrid.setShowRollOver(false);
		orderItemListGrid.setSelectionType(SelectionStyle.NONE);
		orderItemListGrid.setWidth100();
		orderItemListGrid.setHeight100();
		orderItemListGrid.setShowAllRecords(true);
		orderItemListGrid.setSortField(0);
		orderItemListGrid.setCanResizeFields(true);
		orderItemListGrid.setAutoFetchData(true);			
		orderItemListGrid.setDataSource(orderItemData);
		orderItemListGrid.setFields(Util.getListGridFieldsFromDataSource(orderItemData));
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setHidden(true);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_VENORDER_ORDERID).setHidden(true);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_WCSORDERITEMID).setHidden(true);
		orderItemListGrid.getField(DataNameTokens.VENORDER_WCSORDERID).setHidden(true);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC).setWidth("80");
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth("*");
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU).setWidth("140");
		orderItemListGrid.getField(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY).setWidth("100");
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_QUANTITY).setWidth("60");
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_QUANTITY).setAlign(Alignment.RIGHT);
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_PRICE).setWidth("90");
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEM_PRICE));
		orderItemListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL).setWidth("100");
		Util.formatListGridFieldAsCurrency(orderItemListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL));

		orderItemTab.setPane(orderItemListGrid);

		//--------------------- MORE INFO FRAUD TAB --------------------------------

		fraudListGrid.setGroupByField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID);
		fraudListGrid.setGroupStartOpen(GroupStartOpen.ALL);
		fraudListGrid.setShowRollOver(false);
		fraudListGrid.setSelectionType(SelectionStyle.NONE);
		fraudListGrid.setWidth100();
		fraudListGrid.setHeight100();
		fraudListGrid.setShowAllRecords(true);
		fraudListGrid.setSortField(0);
		fraudListGrid.setCanResizeFields(true);
		fraudListGrid.setAutoFetchData(true);			
		fraudListGrid.setDataSource(relatedFraud);
		fraudListGrid.setFields(Util.getListGridFieldsFromDataSource(relatedFraud));
		fraudListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).setHidden(true);
		fraudListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID).setHidden(true);
		fraudListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME).setWidth("100");
//		fraudListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		fraudTab.setPane(fraudListGrid);

		moreInfoTabSet.addTab(orderTab);
		moreInfoTabSet.addTab(orderItemTab);
		moreInfoTabSet.addTab(fraudTab);
		
		Button btnFind = new Button("Find");
		btnFind.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
				String dateFrom = dateFormat.format(dateFromItem.getValueAsDate());
				String dateTo = dateFormat.format(dateToItem.getValueAsDate());
				if (orderAmtFromItem.getValueAsString() != null)
					orderAmtFromItem.setValue(orderAmtFromItem.getValueAsString().replace("Rp. ", "").replaceAll(",", ""));
				if (orderAmtToItem.getValueAsString() != null)
					orderAmtToItem.setValue(orderAmtToItem.getValueAsString().replace("Rp. ", "").replaceAll(",", ""));
				
				showOrderDetails(caseId, ipAddItem.getValueAsString(), orderIdItem.getValueAsString(), orderAmtFromItem.getValueAsString(), orderAmtToItem.getValueAsString(), 
						orderStatusItem.getValueAsString(), custNameItem.getValueAsString(), custHPItem.getValueAsString(), custEmailItem.getValueAsString(), 
						prodCatItem.getValueAsString(), productItem.getValueAsString(), creditCardMaskItem.getDisplayValue(), recpNameItem.getValueAsString(),
						recpHPItem.getValueAsString(), recpEmailItem.getValueAsString(), dateFrom, dateTo, mercNameItem.getValueAsString(), 
						userNameItem.getValueAsString(), shippingAddItem.getValueAsString(), logisticSvcItem.getValueAsString());
				
				if (orderAmtFromItem.getValueAsString() != null) {
					NumberFormat nf = NumberFormat.getFormat("0,000");
					int amountFrom = Integer.parseInt(orderAmtFromItem.getValueAsString());
					if (Integer.toString(amountFrom).length()>3) {
						orderAmtFromItem.setValue("Rp. " + nf.format(new Double(Integer.toString(amountFrom)).doubleValue()));
					} else {
						orderAmtFromItem.setValue("Rp. " + Integer.toString(amountFrom));
					}
				}
				if (orderAmtToItem.getValueAsString() != null) {
					NumberFormat nf = NumberFormat.getFormat("0,000");
					int amountTo = Integer.parseInt(orderAmtToItem.getValueAsString());
					if (Integer.toString(amountTo).length()>3) {
						orderAmtToItem.setValue("Rp. " + nf.format(new Double(Integer.toString(amountTo)).doubleValue()));
					} else {
						orderAmtToItem.setValue("Rp. " + Integer.toString(amountTo));
					}
				}
			}
		});
		
		Button btnClear = new Button("Clear");
		btnClear.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				relatedOrderListGrid.setData(new ListGridRecord[]{});
				orderItemListGrid.setData(new ListGridRecord[]{});
				fraudListGrid.setData(new ListGridRecord[]{});
			}	
		});
		
		findSection.setControls(btnFind, btnClear);
		
		detailSection.addItem(orderDetailForm);
		detailSection.addItem(moreInfoTabSet);
		detailSection.setExpanded(true);
		
		moreInfoStack.addSection(findSection);
		moreInfoStack.addSection(detailSection);		
		moreInfoLayout.setMembers(moreInfoStack);

		setPane(moreInfoLayout);
	}	
	
	private Window buildSelectProdCatWindow() {
		final Window selectProdCatWindow = new Window();
		selectProdCatWindow.setWidth(500);
		selectProdCatWindow.setHeight(350);
		selectProdCatWindow.setTitle("Select Product Category");
		selectProdCatWindow.setShowMinimizeButton(false);
		selectProdCatWindow.setIsModal(true);
		selectProdCatWindow.setShowModalMask(true);
		selectProdCatWindow.centerInPage();
		selectProdCatWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				selectProdCatWindow.destroy();
			}
		});

		VLayout selectProdCatLayout = new VLayout();
		selectProdCatLayout.setHeight100();
		selectProdCatLayout.setWidth100();

		selectProdCatList = new ListGrid();
		selectProdCatList.setHeight(300);
		selectProdCatList.setSelectionType(SelectionStyle.SIMPLE);
		selectProdCatList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		selectProdCatList.setShowFilterEditor(true);
		selectProdCatList.setAutoFetchData(true);

		selectProdCatList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshSelectProdCatData();
			}
		});

		DataSource selectProdCatDataSource = FraudData.getProductCategory();
		selectProdCatList.setDataSource(selectProdCatDataSource);
		selectProdCatList.setFields(Util.getListGridFieldsFromDataSource(selectProdCatDataSource));
		selectProdCatList.getField(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORYID).setHidden(true);
		
		HLayout selectProdCatButtons = new HLayout(5);

		final IButton buttonSelectProdCat = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");

		buttonSelectProdCat.setDisabled(true);
		buttonSelectProdCat.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				prodCatItem.setValue(selectProdCatList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY));
				productItem.setValue("");
				selectProdCatList.redraw();
				selectProdCatWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectProdCatWindow.destroy();
			}
		});
		selectProdCatButtons.setAlign(Alignment.CENTER);
		selectProdCatButtons.setMembers(buttonSelectProdCat, buttonCancel);
		
		selectProdCatLayout.setMembers(selectProdCatList, selectProdCatButtons);
		selectProdCatWindow.addItem(selectProdCatLayout);

		selectProdCatList.addSelectionChangedHandler(new SelectionChangedHandler() {			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectProdCat.setDisabled(selectProdCatList.getSelection().length!=1);
			}
		});		
		return selectProdCatWindow;
	}
	
	private void refreshSelectProdCatData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				selectProdCatList.setData(response.getData());
			}
		};
		selectProdCatList.getDataSource().fetchData(selectProdCatList.getFilterEditorCriteria(), callBack);
	}
	
	private Window buildSelectProductWindow(String prodCatId) {
		final Window selectProductWindow = new Window();
		selectProductWindow.setWidth(500);
		selectProductWindow.setHeight(350);
		selectProductWindow.setTitle("Select Product");
		selectProductWindow.setShowMinimizeButton(false);
		selectProductWindow.setIsModal(true);
		selectProductWindow.setShowModalMask(true);
		selectProductWindow.centerInPage();
		selectProductWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				selectProductWindow.destroy();
			}
		});

		VLayout selectProductLayout = new VLayout();
		selectProductLayout.setHeight100();
		selectProductLayout.setWidth100();

		selectProductList = new ListGrid();
		selectProductList.setHeight(300);
		selectProductList.setSelectionType(SelectionStyle.SIMPLE);
		selectProductList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		selectProductList.setShowFilterEditor(true);
		selectProductList.setAutoFetchData(true);

		selectProductList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshSelectProductData();
			}
		});

		DataSource selectProductDataSource = FraudData.getProduct(prodCatId);
		selectProductList.setDataSource(selectProductDataSource);
		selectProductList.setFields(Util.getListGridFieldsFromDataSource(selectProductDataSource));
		selectProductList.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_PRODUCTID).setHidden(true);
		
		HLayout selectProductButtons = new HLayout(5);

		final IButton buttonSelectProduct = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");

		buttonSelectProduct.setDisabled(true);
		buttonSelectProduct.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				productItem.setValue(selectProductList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME));
				selectProductList.redraw();
				selectProductWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectProductWindow.destroy();
			}
		});
		selectProductButtons.setAlign(Alignment.CENTER);
		selectProductButtons.setMembers(buttonSelectProduct, buttonCancel);
	
		selectProductLayout.setMembers(selectProductList, selectProductButtons);
		selectProductWindow.addItem(selectProductLayout);

		selectProductList.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectProduct.setDisabled(selectProductList.getSelection().length!=1);
			}
		});
		return selectProductWindow;
	}
	
	private void refreshSelectProductData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				selectProductList.setData(response.getData());
			}
		};
		selectProductList.getDataSource().fetchData(selectProductList.getFilterEditorCriteria(), callBack);
	}
	
	private Window buildSelectMerchantWindow() {
		final Window selectMerchantWindow = new Window();
		selectMerchantWindow.setWidth(500);
		selectMerchantWindow.setHeight(350);
		selectMerchantWindow.setTitle("Select Merchant");
		selectMerchantWindow.setShowMinimizeButton(false);
		selectMerchantWindow.setIsModal(true);
		selectMerchantWindow.setShowModalMask(true);
		selectMerchantWindow.centerInPage();
		selectMerchantWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				selectMerchantWindow.destroy();
			}
		});

		VLayout selectMerchantLayout = new VLayout();
		selectMerchantLayout.setHeight100();
		selectMerchantLayout.setWidth100();

		selectMerchantList = new ListGrid();
		selectMerchantList.setHeight(300);
		selectMerchantList.setSelectionType(SelectionStyle.SIMPLE);
		selectMerchantList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		selectMerchantList.setShowFilterEditor(true);
		selectMerchantList.setAutoFetchData(true);

		selectMerchantList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshSelectMerchantData();
			}
		});

		DataSource selectMerchantDataSource = FraudData.getVenMerchant();
		selectMerchantList.setDataSource(selectMerchantDataSource);
		selectMerchantList.setFields(Util.getListGridFieldsFromDataSource(selectMerchantDataSource));
		selectMerchantList.getField(DataNameTokens.VENMERCHANT_MERCHANTID).setHidden(true);
		selectMerchantList.getField(DataNameTokens.VENMERCHANT_WCSMERCHANTID).setHidden(true);
		
		HLayout selectMerchantButtons = new HLayout(5);

		final IButton buttonSelectMerchant = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");
		buttonSelectMerchant.setDisabled(true);
		buttonSelectMerchant.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mercNameItem.setValue(selectMerchantList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENMERCHANT_VENPARTY_FULLORLEGALNAME));
				
				selectMerchantList.redraw();
				selectMerchantWindow.destroy();
			}
		});
		
		buttonCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectMerchantWindow.destroy();
			}
		});
		
		selectMerchantButtons.setAlign(Alignment.CENTER);
		selectMerchantButtons.setMembers(buttonSelectMerchant, buttonCancel);
		
		selectMerchantLayout.setMembers(selectMerchantList, selectMerchantButtons);
		selectMerchantWindow.addItem(selectMerchantLayout);

		selectMerchantList.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectMerchant.setDisabled(selectMerchantList.getSelection().length!=1);
			}
		});
		return selectMerchantWindow;
	}
	
	private void refreshSelectMerchantData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				selectMerchantList.setData(response.getData());
			}
		};
		selectMerchantList.getDataSource().fetchData(selectMerchantList.getFilterEditorCriteria(), callBack);
	}
	
	protected void bindCustomUiHandlers() {	
		orderItemListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudCaseRelatedOrderItemData();
			}
		});
	}
	
	public void refreshOrderListData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				relatedOrderListGrid.setData(response.getData());
			}
		};
		relatedOrderListGrid.getDataSource().fetchData(relatedOrderListGrid.getFilterEditorCriteria(), callBack);
	}	
	
	public void refreshFraudCaseRelatedOrderItemData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				orderItemListGrid.setData(response.getData());
			}
		};
		orderItemListGrid.getDataSource().fetchData(orderItemListGrid.getFilterEditorCriteria(), callBack);
	}	
	
	private void showInitialOrderDetails(DataSource moreInfoData) {
		ListGridField listGridField[] = Util.getListGridFieldsFromDataSource(moreInfoData);
		ListGridField finalListGridField[] = {listGridField[2], listGridField[3], listGridField[4], listGridField[5], listGridField[6], listGridField[7]};
		
		relatedOrderListGrid.setDataSource(moreInfoData);
		relatedOrderListGrid.setFields(finalListGridField);
		relatedOrderListGrid.setSelectionType(SelectionStyle.SIMPLE);  
		relatedOrderListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT).setAlign(Alignment.RIGHT);
		Util.formatListGridFieldAsCurrency(relatedOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT));
						
		relatedOrderListGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				ListGridRecord record = event.getRecord();
				String title = "Order: " + record.getAttribute(DataNameTokens.VENORDER_WCSORDERID);
				Tab[] tabs = moreInfoTabSet.getTabs();

				boolean exist = false;
				int existTab = 0;
				for (int i=0;i<tabs.length;i++) {
					if (tabs[i].getTitle().equals(title)) {
						exist = true;
						existTab = i;
						break;
					}
				}

				if (!exist) {
					Tab tTab = new Tab(title);
					tTab.setCanClose(true);
					DataSource dataSource = GeneralData.getOrderDetailData(record.getAttribute(DataNameTokens.VENORDER_ORDERID));
					VLayout currentorderLayout = new VLayout();
					
					HLayout orderDetailLayout = new HLayout();
					orderDetailLayout.setWidth(400);
					orderDetailLayout.setHeight(100);
					orderDetailForm = new DynamicForm();
					orderDetailForm.setWidth100();
					orderDetailForm.setTitleWidth(130);
					orderDetailForm.setDataSource(dataSource);  
					orderDetailForm.setUseAllDataSourceFields(true);  
					orderDetailForm.setNumCols(4);
					orderDetailForm.setMargin(10);
					orderDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(dataSource));
					Util.formatFormItemAsCurrency(orderDetailForm.getField(DataNameTokens.VENORDER_AMOUNT));					
					orderDetailLayout.setMembers(orderDetailForm);
					
					VLayout fraudCaseOrderItemLayout  = new VLayout();
					Label fraudCaseOrderItemLabel = new Label("<b>Order Item:</b>");
					fraudCaseOrderItemLabel.setHeight(10);
					
					DataSource orderItemData = GeneralData.getOrderItemData(record.getAttribute(DataNameTokens.VENORDER_ORDERID));
					ListGrid orderDetailListGrid = new ListGrid();
					orderDetailListGrid.setShowRollOver(false);
					orderDetailListGrid.setSelectionType(SelectionStyle.NONE);
					orderDetailListGrid.setWidth100();
					orderDetailListGrid.setHeight100();
					orderDetailListGrid.setShowAllRecords(true);
					orderDetailListGrid.setSortField(0);
					orderDetailListGrid.setCanResizeFields(true);
					orderDetailListGrid.setAutoFetchData(true);			
					orderDetailListGrid.setDataSource(orderItemData);
					orderDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(orderItemData));
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setHidden(true);
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_WCSORDERITEMID).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth("10%");		
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_QUANTITY).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL).setWidth("10%");
					Util.formatListGridFieldAsCurrency(orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL));
					
					fraudCaseOrderItemLayout.setMembers(fraudCaseOrderItemLabel, orderDetailListGrid);
					currentorderLayout.setMembers(orderDetailLayout, fraudCaseOrderItemLayout);
					tTab.setPane(currentorderLayout);        

					moreInfoTabSet.addTab(tTab, moreInfoTabSet.getTabs().length);
					moreInfoTabSet.selectTab(tTab);
					orderDetailForm.fetchData();
				} else {
					moreInfoTabSet.selectTab(existTab);
				}
			}
		});
		
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				relatedOrderListGrid.setData(response.getData());
			}
		};
		relatedOrderListGrid.getDataSource().fetchData(relatedOrderListGrid.getFilterEditorCriteria(), callBack);
	}
	
	private void showOrderDetails(String caseId, String ipAddress, String wcsOrderId, String orderAmtFrom, String orderAmtTo, String orderStatusItem, String customerName, 
			String customerMobile, String customerEmail, String prodCat, String product, String creditCardNumber, String recipientName, String recipientHp, String recipientEmail, 
			String dateFrom, String dateTo, String merchName, String userName, String shippingAdd, String logisticSvc) {
		DataSource dataSource = FraudData.getFraudCaseMoreInfoOrderData(caseId, ipAddress, wcsOrderId, orderAmtFrom, orderAmtTo, orderStatusItem, customerName, customerMobile, 
				customerEmail, prodCat, product, creditCardNumber, recipientName, recipientHp, recipientEmail, dateFrom, dateTo, merchName, userName, shippingAdd, logisticSvc);
		
		ListGridField listGridField[] = Util.getListGridFieldsFromDataSource(dataSource);
		ListGridField finalListGridField[] = {listGridField[2], listGridField[3], listGridField[4], listGridField[5], listGridField[6], listGridField[7]};

		relatedOrderListGrid.setDataSource(dataSource);
		relatedOrderListGrid.setFields(finalListGridField);
		relatedOrderListGrid.setSelectionType(SelectionStyle.SIMPLE);  
		relatedOrderListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		relatedOrderListGrid.setShowRowNumbers(true);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT).setAlign(Alignment.RIGHT);
		Util.formatListGridFieldAsCurrency(relatedOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT));
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_ORDERDATE).setWidth(150);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_WCSORDERID).setWidth(100);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setWidth(100);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_IPADDRESS).setWidth(150);
//		relatedOrderListGrid.getField(DataNameTokens.VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		relatedOrderListGrid.getField(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).setCellFormatter(new CellFormatter() {			
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
		                if (record.getAttributeAsString(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG).equals("true")) 
		            	{
		            		return "Yes";
		            	} else {
		            		return "No";
            	}
			}
		});
		relatedOrderListGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				ListGridRecord record = event.getRecord();
				String title = "Order: " + record.getAttribute(DataNameTokens.VENORDER_WCSORDERID);

				Tab[] tabs = moreInfoTabSet.getTabs();

				boolean exist = false;
				int existTab = 0;
				for (int i=0;i<tabs.length;i++) {
					if (tabs[i].getTitle().equals(title)) {
						exist = true;
						existTab = i;
						break;
					}
				}

				if (!exist) {
					Tab tTab = new Tab(title);
					tTab.setCanClose(true);
					DataSource dataSource = GeneralData.getOrderDetailData(record.getAttribute(DataNameTokens.VENORDER_ORDERID));
					VLayout currentorderLayout = new VLayout();
					
					HLayout orderDetailLayout = new HLayout();
					orderDetailLayout.setWidth100();
					orderDetailLayout.setHeight(100);
					orderDetailForm = new DynamicForm();
					orderDetailForm.setWidth100();
					orderDetailForm.setTitleWidth(130);
					orderDetailForm.setDataSource(dataSource);  
					orderDetailForm.setUseAllDataSourceFields(true);  
					orderDetailForm.setNumCols(4);
					orderDetailForm.setMargin(10);
					orderDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(dataSource));
					Util.formatFormItemAsCurrency(orderDetailForm.getField(DataNameTokens.VENORDER_AMOUNT));					
					orderDetailLayout.setMembers(orderDetailForm);
					
					VLayout fraudCaseOrderItemLayout  = new VLayout();
					
					Label fraudCaseOrderItemLabel = new Label("<b>Order Item:</b>");
					fraudCaseOrderItemLabel.setHeight(10);
					
					DataSource orderItemData = GeneralData.getOrderItemData(record.getAttribute(DataNameTokens.VENORDER_ORDERID));
					ListGrid orderDetailListGrid = new ListGrid();
					orderDetailListGrid.setShowRollOver(false);
					orderDetailListGrid.setSelectionType(SelectionStyle.NONE);
					orderDetailListGrid.setWidth100();
					orderDetailListGrid.setHeight100();
					orderDetailListGrid.setShowAllRecords(true);
					orderDetailListGrid.setSortField(0);
					orderDetailListGrid.setCanResizeFields(true);
					orderDetailListGrid.setAutoFetchData(true);			
					orderDetailListGrid.setDataSource(orderItemData);
					orderDetailListGrid.setFields(Util.getListGridFieldsFromDataSource(orderItemData));
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_ORDERITEMID).setHidden(true);
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_WCSORDERITEMID).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE).setWidth("10%");		
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_QUANTITY).setWidth("10%");
					orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL).setWidth("10%");
					Util.formatListGridFieldAsCurrency(orderDetailListGrid.getField(DataNameTokens.VENORDERITEM_TOTAL));
					
					fraudCaseOrderItemLayout.setMembers(fraudCaseOrderItemLabel, orderDetailListGrid);
					
					currentorderLayout.setMembers(orderDetailLayout, fraudCaseOrderItemLayout);
					tTab.setPane(currentorderLayout);        

					moreInfoTabSet.addTab(tTab, moreInfoTabSet.getTabs().length);
					moreInfoTabSet.selectTab(tTab);
					orderDetailForm.fetchData();
				} else {
					moreInfoTabSet.selectTab(existTab);
				}
			}
		});
		
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				relatedOrderListGrid.setData(response.getData());
			}
		};
		relatedOrderListGrid.getDataSource().fetchData(relatedOrderListGrid.getFilterEditorCriteria(), callBack);		
	}

	public DynamicForm getFraudCaseOrderForm() {
		return orderDetailForm;
	}
}
