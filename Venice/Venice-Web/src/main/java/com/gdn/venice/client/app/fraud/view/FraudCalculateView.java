package com.gdn.venice.client.app.fraud.view;

import java.util.Date;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.presenter.FraudCalculatePresenter;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCalculateUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class FraudCalculateView extends
ViewWithUiHandlers<FraudCalculateUiHandlers> implements
FraudCalculatePresenter.MyView {
	RafViewLayout fraudCalculateLayout;
	DateItem dateItem = new DateItem();
	  VLayout layout =null;
	
	@Inject
	public FraudCalculateView() {
		fraudCalculateLayout = new RafViewLayout();
		
		//Toolstrip atas
		ToolStrip fraudCalculateToolStrip = new ToolStrip();
		fraudCalculateToolStrip.setWidth100();
		
		//Form
		DynamicForm form = new DynamicForm();
		form.setTitleWidth(50);
		form.setMargin(10);
		form.setWidth(150);
		
		dateItem.setTitle("Date");
		dateItem.setDefaultValue(new Date());
		dateItem.setTitleAlign(Alignment.LEFT);
		dateItem.setAlign(Alignment.RIGHT);
		
		 HLayout layoutButton = new HLayout();
		 layoutButton.setHeight("5%");
		
		final ToolStripButton buttonItem = new ToolStripButton(); 
        buttonItem.setWidth(150);  
        buttonItem.setTitle("Calculate Fraud");  
        buttonItem.setIcon("[SKIN]/icons/notes_accept.png");  
        buttonItem.setAlign(Alignment.RIGHT);
        buttonItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				buttonItem.setDisabled(true);
	        	RPCRequest request=new RPCRequest();
	    		
	        	Date date = dateItem.getValueAsDate();
	        	DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
	        	String dateParam = dateFormat.format(date);
	        	
	    		request.setActionURL(GWT.getHostPageBaseURL() + "FraudCalculatePresenterServlet?Date=" + dateParam);
	    		request.setHttpMethod("POST");
	    		request.setUseSimpleHttp(true);
	    		
	    		RPCManager.setPromptStyle(PromptStyle.DIALOG);
	    		RPCManager.setDefaultPrompt("Calculating risk points...");
	    		RPCManager.setShowPrompt(true);
	    		
	    		RPCManager.sendRequest(request, 
					new RPCCallback () {
						public void execute(RPCResponse response,
								Object rawData, RPCRequest request) {
							String rpcResponse = rawData.toString();    						
							SC.say(rpcResponse);
							buttonItem.setDisabled(false);
							RPCManager.setDefaultPrompt(DataMessageTokens.RETRIEVING_RECORDS_MESSAGE);
						}
	    		});
	          
				
			}
        	
        });       
      
        ToolStripButton printCaseButton = new ToolStripButton();
		printCaseButton.setIcon("[SKIN]/icons/notes_accept.png");  
		printCaseButton.setTooltip("Click here to Show Order detail."); 
		printCaseButton.setTitle("Show Order");
		
		printCaseButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				Date date = dateItem.getValueAsDate();
				DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
	        	String dateParam = dateFormat.format(date);	        	
	        	
	        	setTableorderCalculated(getUiHandlers().onShowFraudCalculateOrder(dateParam));
	         		
			}

		});
		
		form.setFields(dateItem);
		layoutButton.setMembers(buttonItem,printCaseButton);		

        layout = new VLayout(); 
		//Masukkan semua ke dalam layout
		fraudCalculateLayout.setMembers(fraudCalculateToolStrip, form,layoutButton,layout);

		bindCustomUiHandlers();
	}
	
	public void setTableorderCalculated(DataSource dataSource){
		 
		ListGrid  order = new ListGrid();
		order.setAutoFetchData(true);
		order.setShowFilterEditor(true);
		order.setCanSort(true);
		order.setShowRowNumbers(true);
	    order.setDataSource(dataSource);
	    order.setFields(Util.getListGridFieldsFromDataSource(dataSource));	         
       order.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_VENORDERSTATUS_ORDERSTATUSCODE).setWidth("50px");
	   Util.formatListGridFieldAsCurrency(order.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_AMOUNT));
	         
	    layout.setMembers(order);
	}

	@Override
	public Widget asWidget() {
		return fraudCalculateLayout;
	}

	protected void bindCustomUiHandlers() {

	}
}