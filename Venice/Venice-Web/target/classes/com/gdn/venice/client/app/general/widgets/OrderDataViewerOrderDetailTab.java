package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class OrderDataViewerOrderDetailTab extends Tab {
	DynamicForm orderDetailForm;
	
	IButton buttonSuspiciousFraud;
	IButton buttonFraudPassed;
	IButton buttonFraudConfirmed;
	IButton buttonBlock;
	IButton buttonBlockOK;
	
	SelectItem blockFlagItem;
	SelectItem blockSourceItem;
	TextAreaItem blockReasonItem;
	
	Window blockWindow;
	
	public OrderDataViewerOrderDetailTab(String title, DataSource orderDetailData) {
		super(title);
		
		VLayout orderDetailLayout = new VLayout();
		
		orderDetailForm = new DynamicForm();
		orderDetailForm.setDataSource(orderDetailData);  
		orderDetailForm.setUseAllDataSourceFields(true);  
		
		orderDetailForm.setNumCols(4);
		
		orderDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(orderDetailData));
//		orderDetailForm.getField(DataNameTokens.VENORDER_BLOCKEDTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
//		orderDetailForm.getField(DataNameTokens.VENORDER_ORDERDATE).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		Util.formatFormItemAsCurrency(orderDetailForm.getField(DataNameTokens.VENORDER_AMOUNT));
		
		HLayout orderStatusButtons = new HLayout(5);
		
		buttonSuspiciousFraud = new IButton("SF");
		buttonFraudPassed = new IButton("FP");
		buttonFraudConfirmed = new IButton("FC");
		buttonBlock = new IButton("Block Order");
		
		orderStatusButtons.setMembers(buttonSuspiciousFraud, buttonFraudPassed, buttonFraudConfirmed, buttonBlock);

		orderDetailLayout.setMembers(orderDetailForm,orderStatusButtons);

		
		
		
		setPane(orderDetailLayout);
	}
	
	public String getOrderId() {
		return (String) orderDetailForm.getField(DataNameTokens.VENORDER_ORDERID).getValue();
	}
	
	public DynamicForm getOrderDetailForm() {
		return orderDetailForm;

	}
	
	

	/**
	 * @return the buttonBlock
	 */
	public IButton getButtonBlock() {
		return buttonBlock;
	}
	
	/**
	 * @return the buttonBlockOK
	 */
	public IButton getButtonBlockOK() {
		return buttonBlockOK;
	}
	
	

	/**
	 * @return the blockFlagItem
	 */
	public SelectItem getBlockFlagItem() {
		return blockFlagItem;
	}

	/**
	 * @return the blockSourceItem
	 */
	public SelectItem getBlockSourceItem() {
		return blockSourceItem;
	}

	/**
	 * @return the blockReasonItem
	 */
	public TextAreaItem getBlockReasonItem() {
		return blockReasonItem;
	}

	/**
	 * @return the buttonSuspiciousFraud
	 */
	public IButton getButtonSuspiciousFraud() {
		return buttonSuspiciousFraud;
	}

	/**
	 * @return the buttonFraudPassed
	 */
	public IButton getButtonFraudPassed() {
		return buttonFraudPassed;
	}

	/**
	 * @return the buttonFraudConfirmed
	 */
	public IButton getButtonFraudConfirmed() {
		return buttonFraudConfirmed;
	}
	
	public Window buildBlockWindow() {
		blockWindow = new Window();
		blockWindow.setWidth(280);
		blockWindow.setHeight(230);
		blockWindow.setTitle("Block Order");
		blockWindow.setShowMinimizeButton(false);
		blockWindow.setIsModal(true);
		blockWindow.setShowModalMask(true);
		blockWindow.centerInPage();
		blockWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				blockWindow.destroy();
			}
		});
		
		VLayout blockLayout = new VLayout();
		blockLayout.setHeight100();
		blockLayout.setWidth100();

		DynamicForm blockForm = new DynamicForm();
		blockForm.setPadding(5);
		
		blockFlagItem = new SelectItem();
		blockFlagItem.setTitle("Block Flag");
		blockFlagItem.setValueMap("True", "False");
		blockFlagItem.setValue("True");
				
		blockSourceItem = new SelectItem();
		blockSourceItem.setTitle("Source");
		blockSourceItem.setValueMap("Fraud", "Finance");
		blockSourceItem.setValue("Fraud");
		
		blockReasonItem = new TextAreaItem();
		blockReasonItem.setTitle("Reason");
		
		blockForm.setItems(blockFlagItem, blockSourceItem, blockReasonItem);		

		HLayout okCancelButtons = new HLayout(5);
		
		buttonBlockOK = new IButton("OK");
		IButton buttonBlockCancel = new IButton("Cancel");
		
		
		buttonBlockCancel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				blockWindow.destroy();
			}
		});
		okCancelButtons.setAlign(Alignment.CENTER);
		okCancelButtons.setMembers(buttonBlockOK, buttonBlockCancel);
		
		blockLayout.setMembers(blockForm, okCancelButtons);
		blockWindow.addItem(blockLayout);
		return blockWindow;
	}
	
}
