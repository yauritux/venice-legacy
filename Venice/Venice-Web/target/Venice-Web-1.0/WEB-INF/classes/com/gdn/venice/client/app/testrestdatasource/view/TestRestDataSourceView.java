package com.gdn.venice.client.app.testrestdatasource.view;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.testrestdatasource.data.TestRestDataSourceData;
import com.gdn.venice.client.app.testrestdatasource.presenter.TestRestDataSourcePresenter;
import com.gdn.venice.client.app.testrestdatasource.view.handlers.TestRestDataSourceUiHandlers;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * @author Henry Chandra
 */
public class TestRestDataSourceView extends
ViewWithUiHandlers<TestRestDataSourceUiHandlers> implements
TestRestDataSourcePresenter.MyView {
	private static final int TITLE_HEIGHT = 20;  	

	VLayout testRestDataSourceLayout;
	
	ListGrid testRestDataSourceListGrid; 

	@Inject
	public TestRestDataSourceView() {
		
		testRestDataSourceLayout = new VLayout();
		
		HTMLFlow testRestDataSourceTitle = new HTMLFlow();  
		testRestDataSourceTitle.setOverflow(Overflow.AUTO);  
		testRestDataSourceTitle.setPadding(2);  
		testRestDataSourceTitle.setWidth100();
		
		testRestDataSourceTitle.setHeight(TITLE_HEIGHT);
		testRestDataSourceTitle.setContents("<span style=\"font-size:14px;font-weight:bold;\">Test Rest Data Source</span>");
		
		Button buttonRPC = new Button("Test RPC");
		buttonRPC.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				RPCRequest request=new RPCRequest();
				request.setData("DATA");
				request.setActionURL(GWT.getHostPageBaseURL() + "TestRestDataSourceServlet?method=rpc");
				request.setHttpMethod("POST");
				request.setUseSimpleHttp(true);
				request.setShowPrompt(false);
				
				RPCManager.sendRequest(request, 
						new RPCCallback () {
							public void execute(RPCResponse response,
									Object rawData, RPCRequest request) {
								SC.say(DataMessageTokens.GENERAL_ERROR_MESSAGE);
		
							}
				}
				);
					 
			}
		});
		
		ToolStrip testRestDataToolStrip = new ToolStrip();
		ToolStripButton addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");  
		addButton.setTooltip("Add");
		
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				testRestDataSourceListGrid.startEditingNew();
			}
			
		});
		
		testRestDataToolStrip.addButton(addButton);
		
		

		testRestDataSourceLayout.setMembers(testRestDataSourceTitle, buttonRPC, testRestDataToolStrip, buildTestRestDataSourceListGrid());
//		buildWSDataSourceListGrid();


		bindCustomUiHandlers();
	}
	
	private ListGrid buildTestRestDataSourceListGrid() {

		testRestDataSourceListGrid = new ListGrid();

		testRestDataSourceListGrid.setWidth100();
		testRestDataSourceListGrid.setHeight100();
		testRestDataSourceListGrid.setShowAllRecords(true);
		testRestDataSourceListGrid.setSortField(0);

		ListGridField data1Field = new ListGridField("data1");
		ListGridField data2Field = new ListGridField("data2");
		ListGridField data3Field = new ListGridField("data3");
		ListGridField data4Field = new ListGridField("data4");

		testRestDataSourceListGrid.setFields(data1Field, data2Field, data3Field, data4Field);
		testRestDataSourceListGrid.setCanResizeFields(true);
		testRestDataSourceListGrid.setShowRowNumbers(true);
		
		testRestDataSourceListGrid.setDataSource(TestRestDataSourceData.getTestRestDataSourceData());
		testRestDataSourceListGrid.setAutoFetchData(true);
		testRestDataSourceListGrid.setSortField(0);
		testRestDataSourceListGrid.setAutoFetchData(true);
		
		testRestDataSourceListGrid.setShowFilterEditor(true);
		testRestDataSourceListGrid.setFilterOnKeypress(true);
		
		return testRestDataSourceListGrid;
	}
	
//	public void buildWSDataSourceListGrid() {
//		 final String wsdlURL = "http://ws.cdyne.com/WeatherWS/Weather.asmx?wsdl";
//	        final String namespaceURL = "http://ws.cdyne.com/WeatherWS/";
//	        final String wsOperation = "GetCityForecastByZIP";
//	        SC.showPrompt("Loading WSDL from: " + wsdlURL);
//	        XMLTools.loadWSDL(wsdlURL, new WSDLLoadCallback() {
//	            public void execute(WebService service) {
//	                if(service == null) {
//	                    SC.warn("WSDL not currently available from CDYNE (tried "+ wsdlURL+ ")", new BooleanCallback() {
//	                        public void execute(Boolean value) {
//	                        }
//	                    });
//	                    return;
//	                }
//
//	                DataSource inputDS = service.getInputDS(wsOperation);
//	                
//	                XmlNamespaces ns = new XmlNamespaces();
//	                ns.addNamespace("cdyne", namespaceURL);
//	                
//	                DataSource resultDS = new DataSource();
//	                resultDS.setServiceNamespace(namespaceURL);
//	                resultDS.setXmlNamespaces(ns);
//	                resultDS.setRecordName("Forecast");
//	                OperationBinding opb = new OperationBinding(DSOperationType.FETCH, wsdlURL);
//	                opb.setXmlNamespaces(ns);
//	                opb.setWsOperation(wsOperation);
//	                opb.setRecordName("Forecast");
//	                resultDS.setOperationBindings(opb);
//	                
//	                resultDS.setFields(
//	                    new DataSourceField("Date", FieldType.DATE) {{ 
//	                        setType(FieldType.DATE);
//	                        setAttribute("align", "left");
//	                    }},
//	                    new DataSourceField("Desciption", FieldType.TEXT),
//	                    new DataSourceField("MorningLow", FieldType.INTEGER, "Morning Low", 80)
//	                        {{ setValueXPath("cdyne:Temperatures/cdyne:MorningLow"); }},
//	                    new DataSourceField("DaytimeHigh", FieldType.INTEGER, "Daytime High", 80)
//	                        {{ setValueXPath("cdyne:Temperatures/cdyne:DaytimeHigh"); }}
//	                    );
//
//	                VLayout layout = new VLayout(20);
//	                layout.setWidth100();
//	                layout.setHeight100();
//	                layout.setLayoutMargin(40);
//
//	                final DynamicForm searchForm = new DynamicForm();
//	                searchForm.setNumCols(4);
//	                searchForm.setWidth(500);
//	                searchForm.setDataSource(inputDS);
//
//	                final ListGrid searchResults = new ListGrid();
//	                searchResults.setWidth100();
//	                searchResults.setDataSource(resultDS);
//
//	                IButton searchButton = new IButton("Search");
//	                searchButton.addClickHandler(new ClickHandler() {
//	                    public void onClick(ClickEvent event) {
//	                        searchResults.fetchData(searchForm.getValuesAsCriteria());
//	                    }
//	                });
//
//	                layout.addMember(searchForm);
//	                layout.addMember(searchButton);
//	                layout.addMember(searchResults);
//	                testRestDataSourceLayout.addChild(layout);
//
//	                SC.clearPrompt();
//	            }
//	        });
//	}
	
	@Override
	public Widget asWidget() {
		return testRestDataSourceLayout;
	}

	protected void bindCustomUiHandlers() {

	}
	
	

}
