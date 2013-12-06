package com.gdn.venice.client.app.logistic.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class DeliveryStatusTrackingDetailTab extends Tab {	
	DynamicForm orderDetailForm, orderItemDetailForm, recipientDetailForm, senderDetailForm, airwayBillDetailForm, serviceDetailForm, chargeDetailForm, orderItemStatusDetailForm, merchantDetailForm;
	Window orderItemStatusHistoryWindow;
	ListGrid orderHistoryOrderItemListGrid;
	
	public DeliveryStatusTrackingDetailTab(
			String title,
			DataSource airwayBillData,
			DataSource historyOrderItemData) {
		
		super(title);
		
		//Create summary window portlet
		Canvas canvasSummary = new Canvas();
		canvasSummary.addChild(createWindow("Order Detail", createOrderDetailWindow(airwayBillData), "360", "120", "1%", "0"));
		canvasSummary.addChild(createWindow("Order Item Detail", createOrderItemDetailWindow(airwayBillData), "360", "145", "1%", "130"));
		canvasSummary.addChild(createWindow("Merchant Detail", createMerchantDetailWindow(airwayBillData), "360", "160", "1%", "285"));
		canvasSummary.addChild(createWindow("Order Item Status Detail", createOrderItemStatusDetailWindow(airwayBillData, historyOrderItemData), "360", "120", "34%", "0"));
		canvasSummary.addChild(createWindow("Recipient Detail", createRecipientDetailWindow(airwayBillData), "360", "145", "34%", "130"));
		canvasSummary.addChild(createWindow("Sender Detail", createSenderDetailWindow(airwayBillData), "360", "160", "34%", "285"));
		canvasSummary.addChild(createWindow("Airway Bill Detail", createAirwayBillDetailWindow(airwayBillData), "360", "120", "67%", "0"));
		canvasSummary.addChild(createWindow("Service Detail", createServiceDetailWindow(airwayBillData), "360", "145", "67%", "130"));
		canvasSummary.addChild(createWindow("Charge Detail", createChargeDetailWindow(airwayBillData), "360", "160", "67%", "285"));

		setPane(canvasSummary);
	}
	
	private Layout createMerchantDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		merchantDetailForm = new DynamicForm();
		merchantDetailForm.setDataSource(airwayBillData);
		merchantDetailForm.setUseAllDataSourceFields(false);
		merchantDetailForm.setAutoFetchData(true);		
		merchantDetailForm.setTitleAlign(Alignment.LEFT);
		merchantDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME),
				new StaticTextItem(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENPARTYADDRESS_VENADDRESS),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_VENCONTACTDETAIL_PHONE),
				new StaticTextItem(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPIC),
				new StaticTextItem(DataNameTokens.LOGMERCHANTPICKUPINSTRUCTIONS_MERCHANTPICPHONE)
		);
		merchantDetailForm.setWidth(330);
		merchantDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(merchantDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	private Layout createOrderItemStatusDetailWindow(DataSource airwayBillData, final DataSource historyOrderItemData) {		
		HLayout orderLayout = new HLayout();
		
		orderItemStatusDetailForm = new DynamicForm();
		orderItemStatusDetailForm.setDataSource(airwayBillData);
		orderItemStatusDetailForm.setUseAllDataSourceFields(false);
		orderItemStatusDetailForm.setAutoFetchData(true);	
		orderItemStatusDetailForm.setTitleAlign(Alignment.LEFT);
				
		LinkItem linkItem = new LinkItem("link");  
        linkItem.setTitle("Status History");  
        linkItem.setLinkTitle("Detail");  
        linkItem.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	buildOrderItemStatusHistoryWindow(historyOrderItemData).show();
            }  
        });  
        
		orderItemStatusDetailForm.setFields(
				new StaticTextItem(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSDESC),
				new StaticTextItem(DataNameTokens.VENORDERITEMSTATUSHISTORY_HISTORYTIMESTAMP),
				linkItem
		);
				
//		orderItemStatusDetailForm.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_HISTORYTIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		orderItemStatusDetailForm.setWidth(330);
		orderItemStatusDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(orderItemStatusDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	private Window buildOrderItemStatusHistoryWindow(DataSource historyOrderItemData) {		
		orderItemStatusHistoryWindow = new Window();
		orderItemStatusHistoryWindow.setWidth(500);
		orderItemStatusHistoryWindow.setHeight(500);
		orderItemStatusHistoryWindow.setTitle("Order Item History Detail");
		orderItemStatusHistoryWindow.setShowMinimizeButton(false);
		orderItemStatusHistoryWindow.setIsModal(true);
		orderItemStatusHistoryWindow.setShowModalMask(true);
		orderItemStatusHistoryWindow.centerInPage();
		
		orderHistoryOrderItemListGrid = new ListGrid();		
		orderHistoryOrderItemListGrid.setWidth100();
		orderHistoryOrderItemListGrid.setHeight100();
		orderHistoryOrderItemListGrid.setShowAllRecords(true);

		orderHistoryOrderItemListGrid.setCanResizeFields(true);
		orderHistoryOrderItemListGrid.setShowRowNumbers(true);
		orderHistoryOrderItemListGrid.setAutoFetchData(true);			
		orderHistoryOrderItemListGrid.setDataSource(historyOrderItemData);
		orderHistoryOrderItemListGrid.setFields(Util.getListGridFieldsFromDataSource(historyOrderItemData));
		
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_ORDERITEMID).setWidth(100);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID).setWidth(100);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_TIMESTAMP).setWidth(120);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);		
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_STATUSCHANGEREASON).setWidth(200);
		
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_ORDERITEMID).setHidden(true);
		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID).setHidden(true);
		orderHistoryOrderItemListGrid.groupBy(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID);
		
//		orderHistoryOrderItemListGrid.getField(DataNameTokens.VENORDERITEMSTATUSHISTORY_TIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
				
		HLayout historyStatusButtons = new HLayout(5);
		IButton buttonClose = new IButton("Close");
		
		buttonClose.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				orderItemStatusHistoryWindow.destroy();				
			}
		});
		
		historyStatusButtons.setAlign(Alignment.CENTER);
		historyStatusButtons.setMembers(buttonClose);
		
		VLayout orderHistoryOrderItemLayout = new VLayout();
		orderHistoryOrderItemLayout.setMembers(orderHistoryOrderItemListGrid, historyStatusButtons);
				
		orderItemStatusHistoryWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				orderItemStatusHistoryWindow.destroy();
			}
		});

		orderItemStatusHistoryWindow.addItem(orderHistoryOrderItemLayout);

		return orderItemStatusHistoryWindow;
	}
	
	private Layout createChargeDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		chargeDetailForm = new DynamicForm();
		chargeDetailForm.setDataSource(airwayBillData);
		chargeDetailForm.setUseAllDataSourceFields(false);
		chargeDetailForm.setAutoFetchData(true);		
		chargeDetailForm.setTitleAlign(Alignment.LEFT);
		chargeDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_SHIPPINGCHARGE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE)
		);
		Util.formatFormItemAsCurrency(chargeDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_TOTALCHARGE));
		Util.formatFormItemAsCurrency(chargeDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_SHIPPINGCHARGE));
		Util.formatFormItemAsCurrency(chargeDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_INSURANCECHARGE));
		Util.formatFormItemAsCurrency(chargeDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_OTHERCHARGE_PACKAGINGKAYU));
		Util.formatFormItemAsCurrency(chargeDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_GIFTWRAPCHARGE));
		chargeDetailForm.setWidth(330);
		chargeDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(chargeDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	private Layout createServiceDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		serviceDetailForm = new DynamicForm();
		serviceDetailForm.setDataSource(airwayBillData);
		serviceDetailForm.setUseAllDataSourceFields(false);
		serviceDetailForm.setAutoFetchData(true);		
		serviceDetailForm.setTitleAlign(Alignment.LEFT);
		serviceDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_SERVICEDESC),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_PICKUPDATE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_ACTUALPICKUPDATE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MIN_EST_DATE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MAX_EST_DATE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTWRAP),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTCARD),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_GIFTNOTE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_SPECIALHANDLING),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTCOURIER),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTDELIVEREDDATESTARTEND),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALATIONDATESTARTEND),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLOFFICER),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLMOBILE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MERCHANTINSTALLNOTE)
		);
		serviceDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_PICKUPDATE).setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
		serviceDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MIN_EST_DATE).setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
		serviceDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_MAX_EST_DATE).setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
		serviceDetailForm.setWidth(330);
		serviceDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(serviceDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	private Layout createAirwayBillDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		airwayBillDetailForm = new DynamicForm();
		airwayBillDetailForm.setDataSource(airwayBillData);
		airwayBillDetailForm.setUseAllDataSourceFields(false);
		airwayBillDetailForm.setAutoFetchData(true);		
		airwayBillDetailForm.setTitleAlign(Alignment.LEFT);
		airwayBillDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_STATUS),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_RECEIVED),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_RECIPIENT),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_RELATION)
		);
		airwayBillDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_RECEIVED).setDateFormatter(DateDisplayFormat.TOUSSHORTDATE);
		airwayBillDetailForm.setWidth(330);
		airwayBillDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(airwayBillDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	private Layout createSenderDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		senderDetailForm = new DynamicForm();
		senderDetailForm.setDataSource(airwayBillData);
		senderDetailForm.setUseAllDataSourceFields(false);
		senderDetailForm.setAutoFetchData(true);		
		senderDetailForm.setTitleAlign(Alignment.LEFT);
		senderDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_FULLORLEGALNAME),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENPARTYADDRESS_VENADDRESS),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_PHONE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_MOBILE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENCUSTOMER_VENPARTY_VENCONTACTDETAIL_EMAIL)
		);
		senderDetailForm.setWidth(330);
		senderDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(senderDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}

	private Layout createRecipientDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		recipientDetailForm = new DynamicForm();
		recipientDetailForm.setDataSource(airwayBillData);
		recipientDetailForm.setUseAllDataSourceFields(false);
		recipientDetailForm.setAutoFetchData(true);		
		recipientDetailForm.setTitleAlign(Alignment.LEFT);
		recipientDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_FULLORLEGALNAME),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENPARTYADDRESS_VENADDRESS),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_PHONE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENRECIPIENT_VENPARTY_VENCONTACTDETAIL_MOBILE)
		);
		recipientDetailForm.setWidth(330);
		recipientDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(recipientDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	private Layout createOrderItemDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		orderItemDetailForm = new DynamicForm();
		orderItemDetailForm.setDataSource(airwayBillData);
		orderItemDetailForm.setUseAllDataSourceFields(false);
		orderItemDetailForm.setAutoFetchData(true);	
		orderItemDetailForm.setTitleAlign(Alignment.LEFT);
		orderItemDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_QUANTITY),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT)
		);
		Util.formatFormItemAsCurrency(orderItemDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_INSUREDAMOUNT));
		orderItemDetailForm.setWidth(330);
		orderItemDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(orderItemDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	private Layout createOrderDetailWindow(DataSource airwayBillData) {		
		HLayout orderLayout = new HLayout();
		
		orderDetailForm = new DynamicForm();
		orderDetailForm.setDataSource(airwayBillData);
		orderDetailForm.setUseAllDataSourceFields(false);
		orderDetailForm.setAutoFetchData(true);	
		orderDetailForm.setTitleAlign(Alignment.LEFT);
		orderDetailForm.setFields(
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID),
				new StaticTextItem(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE)
		);

//		orderDetailForm.getField(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE).setDisplayFormat(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		orderDetailForm.setWidth(330);
		orderDetailForm.setTitleWidth(120);	
		
		//Add forms to window, and return window
		orderLayout.setMembers(orderDetailForm);
		setPane(orderLayout);
		return orderLayout;
	}
	
	//General function for creating summary window widget
	private Window createWindow(String title, Layout layout, String width, String height, String offsetLeft, String offsetTop) {
		Window window = new Window();
		window.setTitle(title);
		window.setWidth(width);
		window.setHeight(height);
		window.setLeft(offsetLeft);
		window.setTop(offsetTop);
		window.setShowMinimizeButton(false);
		window.setShowCloseButton(false);
		window.setCanDragReposition(false);
		window.setCanDragResize(false);
		window.addItem(layout);

		return window;
	}
	
	public DynamicForm getOrderDetailForm() {
		return orderDetailForm;
	}
	
	public DynamicForm getOrderItemDetailForm() {
		return orderItemDetailForm;
	}
	
	public DynamicForm getRecipientDetailForm() {
		return recipientDetailForm;
	}
	
	public DynamicForm getSenderDetailForm() {
		return senderDetailForm;
	}
	
	public DynamicForm getAirwayBillDetailForm() {
		return airwayBillDetailForm;
	}
	
	public DynamicForm getServiceDetailForm() {
		return serviceDetailForm;
	}
	
	public DynamicForm getChargeDetailForm() {
		return chargeDetailForm;
	}
	
	public DynamicForm getOrderItemStatusDetailForm() {
		return orderItemStatusDetailForm;
	}
	
	public DynamicForm getMerchantDetailForm() {
		return merchantDetailForm;
	}
}