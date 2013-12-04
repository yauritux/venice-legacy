package com.gdn.venice.client.app.fraud.ui.widgets;

import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
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
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.RowOverEvent;
import com.smartgwt.client.widgets.grid.events.RowOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class FraudCaseManagementOrderHistoryTab extends Tab {
	DynamicForm orderDetailForm;
	ListGrid filteOrderListGrid = null;
	VLayout pointLayout =null;
	Window reportParameterWindow;
	
	
	public FraudCaseManagementOrderHistoryTab(String title, DataSource whiteListData,DataSource filterOrderHistListData) {
		super(title);		
		VLayout orderLayout = new VLayout();
		//Form order detail
		HLayout orderDetailLayout = new HLayout();
		orderDetailLayout.setWidth100();
		orderDetailLayout.setHeight("20%");
		
		VLayout whiteLayout = new VLayout();
		whiteLayout.setHeight100();
		whiteLayout.setWidth("40%");
		/*
		 * show data Customer
		 */
		Label whiteLabel = new Label("<b>Customer White List :</b>");
		whiteLabel.setHeight(10);	
		orderDetailForm = new DynamicForm();
		orderDetailForm.setWidth100();
		orderDetailForm.setHeight100();
		orderDetailForm.setTitleWidth(130);
		orderDetailForm.setDataSource(whiteListData);  
		orderDetailForm.setUseAllDataSourceFields(false);  
		orderDetailForm.setNumCols(2);
		orderDetailForm.setMargin(10);
		orderDetailForm.setFields(Util.getReadOnlyFormItemFromDataSource(whiteListData));
		whiteLayout.setMembers(whiteLabel,orderDetailForm);
		
		VLayout headLayout = new VLayout();
		headLayout.setHeight100();
		headLayout.setWidth("60%");
		/*
		 * show filter dari order
		 */
		Label fraudOrderHistLabel = new Label("<b>Filter By:</b>");
		fraudOrderHistLabel.setHeight(10);		
		filteOrderListGrid = new ListGrid();		
		filteOrderListGrid.setWidth100();
		filteOrderListGrid.setHeight(150);
		filteOrderListGrid.setSortField(0);
		filteOrderListGrid.setShowRowNumbers(true);
		filteOrderListGrid.setAutoFetchData(true);		
		filteOrderListGrid.setDataSource(filterOrderHistListData);
		filteOrderListGrid.setFields(Util.getListGridFieldsFromDataSource(filterOrderHistListData));		
		filteOrderListGrid.getField(DataNameTokens.ORDERHISTORY_ID).setHidden(true);
		filteOrderListGrid.getField(DataNameTokens.ORDERHISTORY_STRINGFILTER).setHidden(true);
		filteOrderListGrid.getField(DataNameTokens.VENORDER_WCSORDERID).setHidden(true);		
		headLayout.setMembers(fraudOrderHistLabel,filteOrderListGrid);
		
		filteOrderListGrid.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				Record record = event.getRecord();
				/*
				 * fetch data menurut filter yang terseleksi
				 */
				setDataOrderHistori(FraudData.getSameOrderHistoryData(record.getAttributeAsString(DataNameTokens.VENORDER_WCSORDERID),
						record.getAttributeAsString(DataNameTokens.ORDERHISTORY_ID),
						record.getAttributeAsString(DataNameTokens.ORDERHISTORY_STRINGFILTER),0,10),
						record.getAttributeAsString(DataNameTokens.VENORDER_WCSORDERID),
						record.getAttributeAsString(DataNameTokens.ORDERHISTORY_ID),
						record.getAttributeAsString(DataNameTokens.ORDERHISTORY_STRINGFILTER),
						record.getAttributeAsString(DataNameTokens.ORDERHISTORY_DESCRIPTION).replace(" ", "")
						
				);
			}
		});
		
		
		orderDetailLayout.setMembers(whiteLayout,headLayout);
		
		Label riskPointtLabel = new Label("<b>View Order:</b>");
		riskPointtLabel.setHeight(10);	
		pointLayout = new VLayout();
		pointLayout.setHeight(300);	
		
	
	   orderLayout.setMembers(orderDetailLayout,riskPointtLabel,pointLayout); 
		setPane(orderLayout);
	}	

	public DynamicForm getFraudCasetOrderHistoryForm() {
		return orderDetailForm;
	}
	
	private void setDataOrderHistori(DataSource db,final String orderId,final String idFilter,final String filter ,final String desc){
		
		//button for print report
		ToolStripButton printCaseButton = new ToolStripButton();
		printCaseButton.setIcon("[SKIN]/icons/notes_accept.png");  
		printCaseButton.setTooltip("Click here to Export Order detail."); 
		printCaseButton.setTitle("Export Order");
		
		
		
		printCaseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {	
				
				String param = "filename=OrderHistoryFraudBy"+desc+"&oid="+orderId+"&filterid="+idFilter+"&filter="+filter;				
				buildReportParameterWindow(param).show();								
			}
		});
		
			/*
			 * table untuk menunjukkan data sesuai filter
			 */
		
		final ListGrid pointOrderListGrid = new ListGrid();		
		pointOrderListGrid.setWidth100();
		pointOrderListGrid.setHeight(300);
		pointOrderListGrid.setSortField(0);
		pointOrderListGrid.setAutoFetchData(true);		
		pointOrderListGrid.setShowRowNumbers(true);
		pointOrderListGrid.setDataSource(db);
		pointOrderListGrid.sort(DataNameTokens.VENORDER_WCSORDERID,SortDirection.ASCENDING);
		pointOrderListGrid.setCanSort(true);
		pointOrderListGrid.setFields(Util.getListGridFieldsFromDataSource(db));		
		pointOrderListGrid.getField(DataNameTokens.VENORDER_ORDERID).setWidth(100);
		pointOrderListGrid.getField(DataNameTokens.VENORDER_ORDERID).setHidden(true);
		pointOrderListGrid.getField(DataNameTokens.VENORDER_WCSORDERID).setWidth(100);
		pointOrderListGrid.getField(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE).setWidth(100);		
		pointOrderListGrid.getField(DataNameTokens.VENORDER_ORDERDATE).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.VENORDER_IPADDRESS).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.VENPARTY_FULLORLEGALNAME).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_PHONE_NUMBER).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_MOBILE_NUMBER).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_EMAIL).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_PRODUCT_NAME).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_QTY).setWidth(100);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_SHPIPPING_ADDRESS).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_BILLING_METHOD).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_STATUS).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_CC).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_ISSUER).setWidth(150);
		pointOrderListGrid.getField(DataNameTokens.ORDERHISTORY_ECI).setWidth(50);
		
		Util.formatListGridFieldAsCurrency(pointOrderListGrid.getField(DataNameTokens.VENORDER_AMOUNT));
	
		/*
		 * jika grid di klik maka akan mencari caseid untuk order tersebut
		 * jika case id null maka order tersebut belum di lakukan perhitungan risk point 
		 * sehingga untuk memunculkan informasi perlu order id
		 */
		pointOrderListGrid.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				final Record record = event.getRecord();
				if(!record.getAttribute(DataNameTokens.VENORDER_ORDERID).equals("")){				
					
							RPCRequest request = new RPCRequest();							
			            	request.setActionURL(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?type=RPC&method=getCaseid&orderId=" + record.getAttributeAsString(DataNameTokens.VENORDER_WCSORDERID));
			        		request.setHttpMethod("POST");
			        		request.setUseSimpleHttp(true);
			        		request.setShowPrompt(false);
			        		
			        		RPCManager.sendRequest(request, 
			    				new RPCCallback () {
			    					public void execute(RPCResponse response,
			    							Object rawData, RPCRequest request) {
			    						String rpcResponse = rawData.toString();    
			    						Map <String,String> map  = Util.formHashMapfromXML(rpcResponse);
			        						Window ordergDetailWindow = new OrderDetailWindow(record.getAttributeAsString(DataNameTokens.VENORDER_WCSORDERID),record.getAttributeAsString(DataNameTokens.VENORDER_ORDERID),!map.get("data0").equals("null")?map.get("data0"):null);
			        						ordergDetailWindow.show();	
			    					
			    					}
			        		});	
				}
				
			}
		});
		
		pointOrderListGrid.addRowOverHandler(new RowOverHandler(){

			@Override
			public void onRowOver(RowOverEvent event) {					
				if(event.getRowNum()>pointOrderListGrid.getRecords().length-5 && !pointOrderListGrid.getRecord(pointOrderListGrid.getRecords().length-1).getAttribute(DataNameTokens.VENORDER_WCSORDERID).equals("END")){			
					/*
					 * set new request dengan lazy load data
					 * penambahan new record sebesar 20
					 * set startRows dengan panjang data/record yang telah ada
					 * set andRows dengan panjang data/record yang telah ada + 20 (jumlah data berikutnya yang akan ditampilkan)
					 */
					 int startRows = pointOrderListGrid.getRecords().length;
					 int endRows = (pointOrderListGrid.getRecords().length+ 20);
					 
					DSRequest request = new DSRequest();
				    request.setStartRow(startRows);
				    request.setEndRow(endRows);
				    request.setSortBy(pointOrderListGrid.getSort());
					
					/*
					 * Fetch data sesuai dengan request 
					 */
					DSCallback callBack = new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							/*
							 * ambil panjang record yang telah ada
							 * ambil data sesuai dengan request yang baru (Lihat request yang telah dibuat)
							 * jika data baru tidak kosong maka record lama di masukkan ke dalam array dan record baru 
							 * dimasukkan di array yang sama dengan array record lama
							 */
							int lengthValue=pointOrderListGrid.getRecordList().getLength();
							Record[] rc =response.getData();			
							if(rc.length>0){	
									Record[] rcItems=new Record[rc.length+ lengthValue];							
									int j=0;
									for(j=0;j<lengthValue;j++){
										Record item=pointOrderListGrid.getRecordList().get(j);	
										rcItems[j]=item;
									}											
									j=j-1;
									for(int i=0;i<rc.length;i++){
										Record item=rc[i];					
										rcItems[j+(i+1)]=item;
									}									
									pointOrderListGrid.setData(rcItems);	
							}
						}
					};
					
					
				    /*
				     * fetch data baru sesuai dengan request
				     */
					pointOrderListGrid.getDataSource().fetchData(pointOrderListGrid.getFilterEditorCriteria(), callBack,request);
			    }				
			}	
			
		});
	
		pointLayout.setMembers(printCaseButton,pointOrderListGrid);			
		
			
	}
	
	private Window buildReportParameterWindow(final String param) {
		reportParameterWindow = new Window();
		reportParameterWindow.setWidth(360);
		reportParameterWindow.setHeight(100);
		reportParameterWindow.setShowMinimizeButton(false);
		reportParameterWindow.setIsModal(true);
		reportParameterWindow.setShowModalMask(true);
		reportParameterWindow.centerInPage();
		reportParameterWindow.setTitle("Export to Excel ?");
		reportParameterWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				reportParameterWindow.destroy();
			}
		});
		VLayout reportParametersLayout = new VLayout();
		reportParametersLayout.setHeight100();
		reportParametersLayout.setWidth100();

		final DynamicForm reportParametersForm = new DynamicForm();
		reportParametersForm.setPadding(5);
		reportParametersForm.setEncoding(Encoding.MULTIPART);
		reportParametersForm.setTarget("upload_frame");	
		    
		VLayout all = new VLayout();
		
		HLayout dialogButtons = new HLayout(5);

		Label label = new Label();
		label.setContents("Mohon Tunggu Beberapa Detik untuk Export Data Ke file Excel!! ");
		label.setAlign(Alignment.CENTER);
		label.setOverflow(Overflow.HIDDEN);
		
		IButton buttonLaunch = new IButton("Launch Report");
		IButton buttonCancel = new IButton("Cancel");
		
		buttonLaunch.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();

				//If in debug mode then change the host URL to the servlet in the server side
				if(host.contains("8889")){
					host = "http://localhost:8090/";
				}
				
				/* 
				 * Somehow when the app is deployed in Geronimo the getHostPageBaseURL call
				 * adds the context root of "Venice/" as it is the web application.
				 * This does not happen in development mode as it is running in the root
				 * of the Jetty servlet container.
				 * 
				 * Consequently the context root needs to be removed because the servlet 
				 * being called has its own context root in a different web application.
				 */
				if(host.contains("Venice/")){
					host = host.substring(0, host.indexOf("Venice/"));
				}
																		
				/*
				 * Call the relevent report servlet based ont he value of reportId
				 */
				reportParametersForm.setAction(GWT.getHostPageBaseURL()  + "FraudCaseMaintenancePresenterServlet?type=downloadFraudOrderHistory&"+param);								
				System.out.println("Form Action:" + reportParametersForm.getAction());
				reportParametersForm.submitForm();

				reportParameterWindow.destroy();
			}		
		});
		
		
		/*
		 * The click handler for the cancel button just destroys the window
		 */
		buttonCancel.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				reportParameterWindow.destroy();
			}
		});
		
		//Align and add the members to the strip and the layout		
		dialogButtons.setAlign(Alignment.CENTER);
		dialogButtons.setMembers(buttonLaunch, buttonCancel);
		all.setAlign(Alignment.CENTER);
		all.setMembers(label,dialogButtons);
		reportParametersLayout.setMembers(reportParametersForm, all);
		reportParameterWindow.addItem(reportParametersLayout);
		return reportParameterWindow;	
				
	}

}
