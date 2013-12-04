package com.gdn.venice.client.app.fraud.ui.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrderDetailWindow extends Window {
	public OrderDetailWindow(final String wcsOrderId, String orderId, final String caseId) {
		VLayout mainDetaillLayout = new VLayout();
		mainDetaillLayout.setHeight100();
		mainDetaillLayout.setWidth100();
		mainDetaillLayout.setPadding(5);
		/*
		 * set tinggi dan lebar window
		 */
		setHeight("95%");
		setWidth("95%");
		setTitle("Order Detail");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				OrderDetailWindow.this.destroy();
			}
		});	

		//Generate toolbar
		ToolStrip fraudToolStrip = new ToolStrip();
		fraudToolStrip.setWidth100();
		fraudToolStrip.setPadding(2);
		/*
		 * button print to pdf
		 */

		ToolStripButton printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Order History");
		printButton.setTitle("Print");
		printButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				SC.ask("Are you sure you want to print this order detail?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value) {
							RPCRequest request = new RPCRequest();
							String caseid = caseId==null?"":caseId, 
									orderid = wcsOrderId==null?"":wcsOrderId;
			            	request.setActionURL(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?type=RPC&method=printFraudOrderHistoryData&fraudcaseid=" + caseid +"&wcsorderid=" + orderid);
			        		request.setHttpMethod("POST");
			        		request.setUseSimpleHttp(true);
			        		request.setShowPrompt(false);
			        		
			        		RPCManager.sendRequest(request, 
			    				new RPCCallback () {
			    					public void execute(RPCResponse response,
			    							Object rawData, RPCRequest request) {
			    						String rpcResponse = rawData.toString();
			    						
			    						if (rpcResponse.trim().equalsIgnoreCase("")) {
				    						SC.say("Failed generating order detail report.");
			    						}
			    						else {
			    							com.google.gwt.user.client.Window.open(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?method=downloadFraudCaseReport&filename=" + rpcResponse, "_blank", "enabled");			
			    						}
			    					}
			        		});
						}
					}
				});
			}
		});
		
		fraudToolStrip.addButton(printButton);
		
		DataSource db = FraudData.getFraudCaseOrderDetailData(caseId,orderId);

		VLayout bodyLayout = new VLayout();
		/*
		 * show informasi order 
		 */
		Label orderDetailLabel = new Label("<b>Order Detail :</b>");
		orderDetailLabel.setHeight(10);	
		DynamicForm orderDetailForm = new DynamicForm();
		orderDetailForm.setWidth100();
		orderDetailForm.setHeight100();
		orderDetailForm.setTitleWidth(130);
		orderDetailForm.setDataSource(db);  
		orderDetailForm.setUseAllDataSourceFields(false);  
		orderDetailForm.setNumCols(2);
		orderDetailForm.setMargin(10);
		orderDetailForm.setAutoFetchData(true);		
		orderDetailForm.setFields(
				new StaticTextItem(DataNameTokens.VENORDER_WCSORDERID),
				new StaticTextItem(DataNameTokens.VENORDER_ORDERDATE)
		);
		/*
		 * show info customer
		 */
		db=FraudData.getFraudCaseCustomerData(caseId,wcsOrderId);
		Label customerDetailLabel = new Label("<b>Customer Detail :</b>");
		customerDetailLabel.setHeight(10);	
		DynamicForm customerNameForm = new DynamicForm();
		customerNameForm.setWidth100();
		customerNameForm.setHeight(10);
		customerNameForm.setTitleWidth(130);
		customerNameForm.setAutoFetchData(true);		
		customerNameForm.setDataSource(db);  
		customerNameForm.setUseAllDataSourceFields(false);  
		customerNameForm.setNumCols(2);
		customerNameForm.setMargin(10);
		customerNameForm.setFields(new StaticTextItem(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME));
		/*
		 * show address customer
		 */
		db=FraudData.getFraudCaseCustomerAddressData(wcsOrderId);	
		DynamicForm customerAddressForm = new DynamicForm();
		customerAddressForm.setWidth100();
		customerAddressForm.setHeight100();
		customerAddressForm.setTitleWidth(130);
		customerAddressForm.setAutoFetchData(true);		
		customerAddressForm.setDataSource(db);  
		customerAddressForm.setUseAllDataSourceFields(false);  
		customerAddressForm.setNumCols(2);
		customerAddressForm.setMargin(10);
		customerAddressForm.setFields(new StaticTextItem(DataNameTokens.VENPARTYADDRESS_VENADDRESS));
		/*
		 * show Cp Customer
		 */		
		db=FraudData.getFraudCaseCustomerContactData(wcsOrderId);
		Label customerCpLabel = new Label("<b>Customer Contact Detail :</b>");
		customerCpLabel.setHeight(10);	
		ListGrid customerCpGrid = new ListGrid();		
		customerCpGrid.setWidth100();
		customerCpGrid.setHeight(150);
		customerCpGrid.setSortField(0);
		customerCpGrid.setShowRowNumbers(true);
		customerCpGrid.setAutoFetchData(true);		
		customerCpGrid.setDataSource(db);
		customerCpGrid.setFields(Util.getListGridFieldsFromDataSource(db));	
		customerCpGrid.getField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID).setHidden(true);		
		
		/*
		 * information Order Item
		 */
		VLayout orderItemDetailLayout = new VLayout();
		Label orderItemDetailLabel = new Label("<b>Order Item Information :</b>");
		orderItemDetailLabel.setHeight(10);	
		
		db=FraudData.getOrderItemInformation(orderId);
		DynamicForm orderItemDetailForm = new DynamicForm();
		orderItemDetailForm.setWidth100();
		orderItemDetailForm.setHeight100();
		orderItemDetailForm.setTitleWidth(130);
		orderItemDetailForm.setDataSource(db);  
		orderItemDetailForm.setUseAllDataSourceFields(false);  
		orderItemDetailForm.setAutoFetchData(true);		
		orderItemDetailForm.setNumCols(2);
		orderItemDetailForm.setMargin(10);
		orderItemDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(db));
		/*
		 * seluruh order item dengna order yang sama
		 */
		db=FraudData.getFraudCaseOrderItemData(wcsOrderId);
		ListGrid orderitemListGrid = new ListGrid();		
		orderitemListGrid.setWidth100();
		orderitemListGrid.setHeight(150);
		orderitemListGrid.setSortField(0);
		orderitemListGrid.setShowRowNumbers(true);
		orderitemListGrid.setAutoFetchData(true);		
		orderitemListGrid.setDataSource(db);
		orderitemListGrid.setFields(
				new ListGridField(DataNameTokens.VENORDERITEM_WCSORDERITEMID),
				new ListGridField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU),
				new ListGridField(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME),
				new ListGridField(DataNameTokens.VENORDERITEM_QUANTITY),
				new ListGridField(DataNameTokens.VENORDERITEM_PRICE)			
		);		
		Util.formatListGridFieldAsCurrency(orderitemListGrid.getField(DataNameTokens.VENORDERITEM_PRICE));
		
		
		orderItemDetailLayout.setMembers(orderItemDetailLabel,orderItemDetailForm,orderitemListGrid);
		/*
		 * informasi pembayaran
		 */
		db=FraudData.getFraudCasePaymentData(caseId,orderId);
		Label billingDetailLabel = new Label("<b>Billing Information :</b>");
		billingDetailLabel.setHeight(10);	
		DynamicForm billingDetailForm = new DynamicForm();
		billingDetailForm.setWidth100();
		billingDetailForm.setHeight100();
		billingDetailForm.setTitleWidth(130);
		billingDetailForm.setDataSource(db);  
		billingDetailForm.setUseAllDataSourceFields(false);  
		billingDetailForm.setAutoFetchData(true);		
		billingDetailForm.setNumCols(2);
		billingDetailForm.setMargin(10);
		billingDetailForm.setFields(		
				new StaticTextItem(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_DESCRIPTION),
				new StaticTextItem(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENWCSPAYMENTTYPE_WCSPAYMENTTYPEDESC),
				new StaticTextItem(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_OTHERINFO),
				new StaticTextItem(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_VENBANK_ISSUERBANK),				
				new StaticTextItem(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT),				
				new StaticTextItem(DataNameTokens.VENORDERPAYMENTALLOCATION_VENADDRESS),
				new StaticTextItem(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE)				
				
				);
		Util.formatFormItemAsCurrency(billingDetailForm.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT));
		Util.formatFormItemAsCurrency(billingDetailForm.getField(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE));
		
		/*
		 * informasi point (akan muncul jika order talah di hitung risk pointnya)
		 */
		Label riskPointDetailLabel = new Label("<b>Risk Point:</b>");
		riskPointDetailLabel.setHeight(10);	
		ListGrid riskPointListGrid = new ListGrid();		
		riskPointListGrid.setWidth100();
		riskPointListGrid.setHeight(500);
		riskPointListGrid.setSortField(0);
		riskPointListGrid.setShowRowNumbers(true);
		if(caseId!=null){
				riskPointListGrid.setAutoFetchData(true);	
				db=FraudData.getFraudCaseRiskScoreData(caseId);
				riskPointListGrid.setDataSource(db);
				riskPointListGrid.setFields(Util.getListGridFieldsFromDataSource(db));	
				riskPointListGrid.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONPOINTSID).setHidden(true);	
		}
		
		/*
		 * show total risk point order
		 */
		Label totRiskPointDetailLabel = new Label("<b>Total Risk : </b>");
		totRiskPointDetailLabel.setHeight(10);
		
		DynamicForm totPointForm = new DynamicForm();
		totPointForm.setWidth100();
		totPointForm.setHeight100();
		totPointForm.setTitleWidth(130);		
		totPointForm.setUseAllDataSourceFields(false);  
		totPointForm.setNumCols(2);
		totPointForm.setMargin(10);
		if(caseId!=null){
			totPointForm.setAutoFetchData(true);		
			db=FraudData.getTotalRiskPoint(caseId);
			totPointForm.setDataSource(db);  
			totPointForm.setFields(				
					new StaticTextItem(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS)					
					);
		}
		
		/*
		 * show note/komentar untuk order tersebut
		 */
		Label ketLabel = new Label("<b>Note : ");
		ketLabel.setHeight(10);
		db=FraudData.getFraudCaseFraudManagementData(caseId);
		DynamicForm ketListForm = new DynamicForm();
		ketListForm.setWidth100();
		ketListForm.setHeight100();
		ketListForm.setTitleWidth(130);		
		ketListForm.setUseAllDataSourceFields(false);  
		ketListForm.setNumCols(2);
		ketListForm.setMargin(10);
		if(caseId!=null){
				ketListForm.setAutoFetchData(true);		
				ketListForm.setDataSource(db);  
				ketListForm.setFields(				
						new StaticTextItem(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC),				
						new StaticTextItem(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON),	
						new StaticTextItem(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES)
						);
		}
		/*
		 * show alasan/keterangan mengenai alasan order tersebut di blacklist
		 */
		Label whyBlackListlLabel = new Label("<b>Keterangan BlakList  : </b>");
		whyBlackListlLabel.setHeight(10);
		db=FraudData.getWhyBlackList(orderId);
		DynamicForm reasonBlackListForm = new DynamicForm();
		reasonBlackListForm.setWidth100();
		reasonBlackListForm.setHeight100();
		reasonBlackListForm.setTitleWidth(130);
		reasonBlackListForm.setDataSource(db);  
		reasonBlackListForm.setUseAllDataSourceFields(false);  
		reasonBlackListForm.setAutoFetchData(true);		
		reasonBlackListForm.setNumCols(2);
		reasonBlackListForm.setMargin(10);
		reasonBlackListForm.setFields(				
				new StaticTextItem(DataNameTokens.FRDBLACKLIST_BLACKLIST_REASON)					
				);
		
		Label attLabel = new Label("<b>Attachment  : </b>");
		attLabel.setHeight(10);
		/*
		 * show attacament file untuk order tersebut
		 * attachment dapat di download ketika di klik recordnya
		 */				
		db=FraudData.getFraudCaseAttachmentData(caseId);
		ListGrid attachmentListGrid = new ListGrid();
		attachmentListGrid.setWidth100();
		attachmentListGrid.setHeight(100);
		attachmentListGrid.setSortField(0);
		attachmentListGrid.setShowRowNumbers(true);
		if(caseId!=null){
				attachmentListGrid.setAutoFetchData(true);		
				attachmentListGrid.setDataSource(db);
				attachmentListGrid.setFields(Util.getListGridFieldsFromDataSource(db));	
				attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID).setHidden(true);
				attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID).setHidden(true);
				attachmentListGrid.getField(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILELOCATION).setCellFormatter(new CellFormatter() {
					
					@Override
					public String format(Object value, ListGridRecord record, int rowNum,
							int colNum) {
						String cellFormat = (String) value;
						cellFormat = cellFormat.substring(cellFormat.lastIndexOf("/")+1, cellFormat.length());
						return "<a href='" + GWT.getHostPageBaseURL() +
							MainPagePresenter.fileDownloadPresenterServlet +
							"?filename=" + value + "' target='_blank'>" + 
							cellFormat + "</a>";
					}
				});
		}
		
		/*
		 * set seluruh informasi
		 */
		bodyLayout.setMembers(orderDetailLabel,orderDetailForm,customerDetailLabel,customerNameForm,customerAddressForm,customerCpLabel,customerCpGrid,orderItemDetailLayout,
				billingDetailLabel,billingDetailForm,riskPointDetailLabel,riskPointListGrid,totRiskPointDetailLabel,totPointForm,ketLabel,ketListForm,whyBlackListlLabel,reasonBlackListForm,attLabel,attachmentListGrid);
				
		HLayout closeButtonLayout = new HLayout(5);		
		IButton buttonClose = new IButton("Close");		
		buttonClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				OrderDetailWindow.this.destroy();
			}
		});
		closeButtonLayout.setAlign(Alignment.CENTER);
		closeButtonLayout.setMembers(buttonClose);
		
		mainDetaillLayout.setMembers(fraudToolStrip,bodyLayout,closeButtonLayout);		
		addItem(mainDetaillLayout);		
	}
}
