package com.gdn.venice.client.app.finance.view;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.ExportPresenter;
import com.gdn.venice.client.app.finance.view.handlers.ExportUiHandlers;
import com.gdn.venice.client.app.finance.widgets.ExportVLayoutWidget;
import com.gdn.venice.client.util.PrintUtility;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
/**
 * The view class for the Export screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ExportView extends ViewWithUiHandlers<ExportUiHandlers> implements
		ExportPresenter.MyView {

	/*
	 * The RAF layout that is used for laying out the export data
	 */
	RafViewLayout exportLayout;

	/*
	 * A VLayout widget that is used to display the fields on the Export screen
	 * (Journal Voucher)
	 */
	VLayout exportVLayout;

	/*
	 * A VLayout widget that is used to display the fields on the Account Lines
	 * screen (bottom table)
	 */
	VLayout exportAccontLinesVLayout;
	/*
	 * A VLayout widget that is used to display the fields on the export data
	 * screen
	 */
	ExportVLayoutWidget exportVLayoutWidget;

	/*
	 * The list grid that is used to display the journal voucher
	 */
	ListGrid exportJournalVoucherListGrid;

	/*
	 * The toolstrip objects for the header
	 */
	ToolStrip journalVoucherTypeToolStrip;
	
	/*
	 * The printing toolstrip button
	 */
	ToolStripButton printButton;	
	/*
	 * The Export toolstrip button
	 */
	ToolStripButton exportButton;

	/*
	 * Constructor to build the view and inject it
	 */
	@Inject
	public ExportView() {

		exportLayout = new RafViewLayout();

		journalVoucherTypeToolStrip = new ToolStrip();
		journalVoucherTypeToolStrip.setWidth100();
		journalVoucherTypeToolStrip.setPadding(2);
		journalVoucherTypeToolStrip.setHeight(30);
		
		HTMLFlow titleFlow = new HTMLFlow();
		
		titleFlow
				.setContents("<center><b>EXPORTED JOURNAL VOUCHERS</b></center>");
		titleFlow.setAlign(Alignment.CENTER);
		
		printButton = new ToolStripButton();
		printButton.setIcon("[SKIN]/icons/printer.png");
		printButton.setTooltip("Print Journal Voucher Details");
		printButton.setTitle("Print");
		
		exportButton = new ToolStripButton();
		exportButton.setIcon("[SKIN]/icons/up.png");
		exportButton.setTooltip("Export Journal Voucher Details");
		exportButton.setTitle("Export");
		
		journalVoucherTypeToolStrip.addButton(printButton);
		
		journalVoucherTypeToolStrip.addSeparator();
		
		journalVoucherTypeToolStrip.addButton(exportButton);
		
		journalVoucherTypeToolStrip.addSeparator();

		//journalVoucherTypeToolStrip.addChild(titleFlow);

		exportVLayoutWidget = new ExportVLayoutWidget(this);

		exportLayout.setMembers(journalVoucherTypeToolStrip, titleFlow,
				exportVLayoutWidget);

		bindCustomUiHandlers();
	}

	/**
	 * Loads the list of Export Journal voucher data (top table in the screen)
	 * 
	 * @param dataSource
	 *            export Journal Voucher datasource
	 */
	@Override
	public void loadJornalVoucherData(DataSource dataSource) {

		exportVLayoutWidget.loadJornalVoucherData(dataSource);

	}

	/**
	 * This method use for binding
	 */
	protected void bindCustomUiHandlers() {

		exportVLayoutWidget.bindCustomUiHandlers();
		
		printButton.addClickHandler(new ClickHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				PrintUtility.printComponent(exportVLayoutWidget);
			}
		});
		
		exportButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					ListGridRecord[] selection = exportVLayoutWidget.getExportJournalVoucherListGrid().getSelection();
					String journalGroupId = selection[0].getAttribute(DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID);
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
													
					Window.open(host + "Venice/JournalRollupReportLauncherServlet?journalGroupId=" + journalGroupId, "_blank", null);
				}
			});
		
//		exportButton.addClickHandler(new ClickHandler() {
//			
//			/* (non-Javadoc)
//			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
//			 */
//			@Override
//			public void onClick(ClickEvent event) {
//				ListGridRecord[] entryList = exportVLayoutWidget.getSelectedRecord();
//				String  group=null;
//				Double debet=null;
//				Double credit=null;
//				int count=0;
//				int i=0;
//					
//				HashMap<String, String> mapList = new HashMap<String, String>();				
//				HashMap<String,String> m = new HashMap<String,String> ();
//
//				for (ListGridRecord list : entryList){
//					count++;
//					if (group==null || !list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID).equals(group) ){
//						if (group!=null){
//							System.out.println("group = "+group);
//							System.out.println("Debet = "+debet+" Credit = "+credit);
//							m.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET,""+debet.intValue());
//							m.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT,""+credit.intValue());
//							mapList.put("data"+i++, Util.formXMLfromHashMap(m));
//						}						
//						group=list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID);
//						credit=Math.abs(new Double(!list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT).isEmpty()?list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT) : "0"));
//						debet=Math.abs(new Double(!list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET).isEmpty()?list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET) : "0"));
//						m.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID, list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID));
//						m.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC, list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC));
//						m.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP, list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP));
//						
//					}else{
//								credit = credit + Math.abs(new Double(!list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT).isEmpty()?list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT):"0"));
//								debet = debet + Math.abs(new Double(!list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET).isEmpty()? list.getAttribute(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET):"0"));							
//															
//								if (entryList.length<=count){
//									System.out.println("group = "+group);
//									System.out.println("Debet = "+debet+" Credit = "+credit);
//									
//									m.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET,""+debet.intValue());
//									m.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT,""+credit.intValue());
//									mapList.put("data"+i++, Util.formXMLfromHashMap(m));
//								}
//					}
//					
//				}		
//				
//				/*RPCRequest request=new RPCRequest();
//				request.setData(Util.formXMLfromHashMap(mapList));
//				request.setActionURL(GWT.getHostPageBaseURL() + "JournalRolledUpReportServlet");
//				request.setHttpMethod("POST");
//				request.setUseSimpleHttp(true);
//				request.setShowPrompt(false);
//				RPCManager.sendRequest(request, 
//						new RPCCallback () {
//					
//							public void execute(RPCResponse response,
//									Object rawData, RPCRequest request) {
//								String rpcResponse = rawData.toString();						
//								String xmlData = rpcResponse;					
//												
//							}
//				});*/
//				
//				
//							
//			}
//			
//			
//			
//			
//			
//			
//		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {

		return exportLayout;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.presenter.ExportPresenter.MyView#refreshJornalVoucherData()
	 */
	@Override
	public void refreshJornalVoucherData() {

		DSCallback callBack = new DSCallback() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client
			 * .data.DSResponse, java.lang.Object,
			 * com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				exportVLayoutWidget.getExportJournalVoucherListGrid().setData(
						response.getData());
				exportVLayoutWidget.getExportJournalVoucherListGrid()
						.setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
		exportVLayoutWidget
				.getExportJournalVoucherListGrid()
				.getDataSource()
				.fetchData(
						exportVLayoutWidget.getExportJournalVoucherListGrid()
								.getFilterEditorCriteria(), callBack);

	}
}