package com.gdn.venice.client.app.finance.widgets;

import com.gdn.venice.client.util.PrintUtility;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class PaymentProcessingTabWidget extends Tab{
	
	VLayout paymentLayout;
	VLayout paymentDetailLayout;
	IButton buttonProcessDetailPayment;
	
	ToolStripButton printButton;

	ListGrid paymentListGrid;
	
	public PaymentProcessingTabWidget(String title, ClickHandler buttonProcessDetailPaymentClickHandler) {
		super(title);
		
		VLayout paymentProcessingLayout = new VLayout();
		
		ToolStrip paymentToolStrip = new ToolStrip();
		paymentToolStrip.setWidth100();
		paymentToolStrip.setPadding(2);
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Pre-Payment List");
		printButton.setTitle("Print");
		
		paymentToolStrip.addButton(printButton);

		HLayout paymentDetailProcessingButtonLayout = new HLayout();
		paymentDetailProcessingButtonLayout.setHeight(20);
		paymentDetailProcessingButtonLayout.setPadding(2);
		paymentDetailProcessingButtonLayout.setAlign(Alignment.CENTER);
		
		buttonProcessDetailPayment = new IButton("Process Payment");
		buttonProcessDetailPayment.setIcon("[SKIN]/icons/down.png");
		buttonProcessDetailPayment.setWidth(150);
		buttonProcessDetailPayment.addClickHandler(buttonProcessDetailPaymentClickHandler);
		buttonProcessDetailPayment.setDisabled(true);
		
		paymentDetailProcessingButtonLayout.setMembers(buttonProcessDetailPayment);
		
		paymentDetailLayout = new VLayout();
		paymentLayout = new VLayout();
		
		
		paymentProcessingLayout.setMembers(paymentToolStrip, paymentDetailLayout, paymentDetailProcessingButtonLayout, paymentLayout);
		
		setPane(paymentProcessingLayout);
		
		printButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(paymentListGrid);	
			}
		});

	}
	
	public VLayout getPaymentDetailLayout() {
		return paymentDetailLayout;
	}
	
	public VLayout getPaymentLayout() {
		return paymentLayout;
	}
	
	public void setButtonProcessDetailPaymentDisabled(boolean disabled) {
		buttonProcessDetailPayment.setDisabled(disabled);
		
	}

	/**
	 * @return the paymentListGrid
	 */
	public ListGrid getPaymentListGrid() {
		return paymentListGrid;
	}

	/**
	 * @param paymentListGrid the paymentListGrid to set
	 */
	public void setPaymentListGrid(ListGrid paymentListGrid) {
		this.paymentListGrid = paymentListGrid;
	}
}
