package com.gdn.venice.client.app.logistic.presenter;

import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.view.handlers.InvoiceReconciliationUiHandlers;
import com.gdn.venice.client.data.RafDataSource;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.SC;

/**
 * Presenter for Logistics Invoice Reconciliation Screen
 * 
 * @author Henry Chandra
 */
public class InvoiceReconciliationPresenter
		extends
		Presenter<InvoiceReconciliationPresenter.MyView, InvoiceReconciliationPresenter.MyProxy>
		implements InvoiceReconciliationUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	public final static String invoiceReconciliationServlet = "InvoiceReconciliationPresenterServlet";

	/**
	 * {@link InvoiceReconciliationPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.logisticInvoiceReconciliationPage)
	public interface MyProxy extends Proxy<InvoiceReconciliationPresenter>, Place {
		
	}

	/**
	 * {@link InvoiceReconciliationPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<InvoiceReconciliationUiHandlers> {
//		public void loadAirwayBillData(DataSource dataSource);
//		public void refreshAirwayBillData();
		public void refreshUploadLogListGridData();
		public void loadUploadLogData(DataSource dataSource);
//		public void loadInvoiceAirwayRecordData(DataSource dataSource, Map<String,String> approval);
//		public void refreshInvoiceAirwayRecordData();
		public void loadInvoiceReportUploadData(DataSource dataSource, Map<String,String> approval);
		public void refreshInvoiceReportUploadData();
	}

	/**
	 * Links the presenter to the view, proxy, event bus and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public InvoiceReconciliationPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchComboBoxData();
		this.dispatcher = dispatcher;
	}

	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
	 */
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.InvoiceReconciliationUiHandlers#onExpandAirwayBillRow(java.lang.String)
	 */
	@Override
	public DataSource onExpandAirwayBillRow(String invoiceAirwaybillId) {
		RafDataSource activityReportsReconciliationProblemData = LogisticsData.getInvoiceReconciliationProblemData(invoiceAirwaybillId);
		
		return activityReportsReconciliationProblemData;
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.InvoiceReconciliationUiHandlers#onSubmitForApproval(java.util.List)
	 */
	@Override
	public void onSubmitForApproval(List<String> invoiceNumbers) {
		try {
			String ids="";
			for (int i = 0; i < invoiceNumbers.size(); i++) {
				ids += "'"+invoiceNumbers.get(i)+"'";
				if(i != invoiceNumbers.size()-1)
					ids += ",";
			}			
			
			RPCRequest rpcRequest=new RPCRequest();
	    	rpcRequest.setActionURL(GWT.getHostPageBaseURL() +  invoiceReconciliationServlet + "?method=getInvoiceReportUploadIdToSubmit&type=RPC&invoiceNumbers="+ids);
			rpcRequest.setHttpMethod("POST");
			rpcRequest.setUseSimpleHttp(true);
			rpcRequest.setShowPrompt(false);
			RPCManager.sendRequest(rpcRequest, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String invoiceReportUploadId = rawData.toString().replace("\r\n", "");
						if(!invoiceReportUploadId.equals("{}")){
							request.setData(invoiceReportUploadId);						
							request.setActionURL(GWT.getHostPageBaseURL() + invoiceReconciliationServlet + "?method=submitForApproval&type=RPC");
							request.setHttpMethod("POST");
							request.setUseSimpleHttp(true);
							request.setPromptStyle(PromptStyle.DIALOG);
							request.setPrompt("Submitting invoice reconciliation records for approval...");
							request.setShowPrompt(true);
	
							RPCManager.sendRequest(request, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponse = rawData.toString();
										if (rpcResponse.startsWith("0")) {
											getView().refreshInvoiceReportUploadData() ;
											SC.say(DataMessageTokens.SUBMITTED_FOR_APPROVAL);
										} else {
											SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
										}
									}
								}
							);
						} else SC.warn(DataMessageTokens.NO_RECORD_SUBMITTED);
					}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.InvoiceReconciliationUiHandlers#onShowMerchantPickUpDetail(java.lang.String)
	 */
	@Override
	public DataSource onShowMerchantPickUpDetail(String airwayBillId) {
		return LogisticsData.getMerchantPickUpInstructionData(airwayBillId);
	}

	@Override
	public void onFetchComboBoxData() {	
		//Request approval status combo
		RPCRequest requestApproval=new RPCRequest();
		requestApproval = new RPCRequest();
		requestApproval.setActionURL(GWT.getHostPageBaseURL() + invoiceReconciliationServlet+"?method=fetchApprovalStatusComboBoxData&type=RPC");
		requestApproval.setHttpMethod("POST");
		requestApproval.setUseSimpleHttp(true);
		requestApproval.setShowPrompt(false);
		RPCManager.sendRequest(requestApproval, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseApproval = rawData.toString();
						String xmlDataApproval = rpcResponseApproval;
						final Map<String, String> approvalMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataApproval));
						getView().loadUploadLogData(LogisticsData.getInvoiceUploadLogData());
						getView().loadInvoiceReportUploadData(LogisticsData.getInvoiceReportUploadData(""), approvalMap);
				}
		});
	}
}
