package com.gdn.venice.client.app.general.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.app.general.view.handlers.OrderDataViewerUiHandlers;
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
import com.smartgwt.client.util.SC;

/**
 * Presenter for Order Data Viewer Screen
 * 
 * @author Henry Chandra
 */
public class OrderDataViewerPresenter extends Presenter<OrderDataViewerPresenter.MyView, OrderDataViewerPresenter.MyProxy> implements OrderDataViewerUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String orderDataViewerServlet = "OrderDataViewerPresenterServlet";

	/**
	 * {@link OrderDataViewerPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.generalOrderDataViewer)
	public interface MyProxy extends Proxy<OrderDataViewerPresenter>, Place {
		
	}

	/**
	 * {@link OrderDataViewerPresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<OrderDataViewerUiHandlers> {
		public void loadOrderData(DataSource dataSource, Map<String,String> status);
		void refreshOrderData();
//		public void setLastPage(int totalRows);
	}

	@Inject
	public OrderDataViewerPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		onFetchComboBoxData(0);
//		onCountTotalData();
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
		
	@Override
	public DataSource onShowOrderFinanceReconciliationData(String paymentId) {
		return GeneralData.getOrderFinanceReconciliationData(paymentId);
	}

	@Override
	public List<DataSource> onShowOrderDetailData(String orderId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		dataSources.add(GeneralData.getOrderDetailData(orderId));
		dataSources.add(GeneralData.getOrderItemData(orderId));
		dataSources.add(GeneralData.getOrderCustomerData(orderId));
		dataSources.add(GeneralData.getOrderCustomerAddressData(orderId));
		dataSources.add(GeneralData.getOrderCustomerContactData(orderId));
		dataSources.add(GeneralData.getOrderLogisticsAirwayBillData(orderId));
		dataSources.add(GeneralData.getOrderFinancePaymentData(orderId));
		dataSources.add(GeneralData.getOrderFinanceReconciliationData(null));
		dataSources.add(GeneralData.getOrderHistoryOrderData(orderId));
		dataSources.add(GeneralData.getOrderHistoryOrderItemData(orderId));
		return dataSources;
	}

	@Override
	public void onUpdateOrderStatus(String orderId, String status) {
		String method = "";
		if (status.equals(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_SF.toString())) {
			method = "updateOrderStatusToSF";
		} else if (status.equals(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FP.toString())) {
			method = "updateOrderStatusToFP";
		} else if (status.equals(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FC.toString())) {
			method = "updateOrderStatusToFC";
		}
		
		RPCRequest request=new RPCRequest();		
		request.setData(orderId);
		
		request.setActionURL(GWT.getHostPageBaseURL() + orderDataViewerServlet + "?method="+method+"&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshOrderData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
					}
			}
		);		
	}
	
	@Override
	public void onBlockOrder(String orderId, String flag, String source, String reason, String orderStatus) {		
		RPCRequest request=new RPCRequest();		
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		map.put("orderId", orderId);
		map.put("blockFlag", flag);
		map.put("blockSource", source);
		map.put("blockReason", reason);
		map.put("orderStatus", orderStatus);
				
		request.setData(Util.formXMLfromHashMap(map));
		
		request.setActionURL(GWT.getHostPageBaseURL() + orderDataViewerServlet + "?method=updateBlockOrder&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshOrderData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
					}
			}
		);		
	}

	@Override
	public void onApproveVAPayment(String paymentId) {		
		RPCRequest request=new RPCRequest();		
		request.setData(paymentId);		
		request.setActionURL(GWT.getHostPageBaseURL() + orderDataViewerServlet + "?method=approveVAPayment&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshOrderData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
					}
			}
		);		
	}	
	
	
	
	@Override
	public void onApproveCSPayment(String paymentId) {
		RPCRequest request=new RPCRequest();		
		request.setData(paymentId);		
		request.setActionURL(GWT.getHostPageBaseURL() + orderDataViewerServlet + "?method=approveCSPayment&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshOrderData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
					}
			}
		);		
	}

	@Override
	public void onRejectCSPayment(String paymentId) {
		RPCRequest request=new RPCRequest();		
		request.setData(paymentId);		
		request.setActionURL(GWT.getHostPageBaseURL() + orderDataViewerServlet + "?method=rejectCSPayment&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshOrderData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
					}
			}
		);		
	}

	@Override
	public void onPendingCSPayment(String paymentId) {
		RPCRequest request=new RPCRequest();		
		request.setData(paymentId);		
		request.setActionURL(GWT.getHostPageBaseURL() + orderDataViewerServlet + "?method=pendingCSPayment&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, new RPCCallback () {
					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshOrderData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
					}
			}
		);		
	}

	@Override
	public void onFetchComboBoxData(int startRow) {	
		//Request order status combo
		RPCRequest requestStatuse=new RPCRequest();
		requestStatuse = new RPCRequest();
		requestStatuse.setActionURL(GWT.getHostPageBaseURL() + orderDataViewerServlet+"?method=fetchOrderStatusComboBoxData&type=RPC");
		requestStatuse.setHttpMethod("POST");
		requestStatuse.setUseSimpleHttp(true);
		requestStatuse.setShowPrompt(false);
		final int start = startRow;
		RPCManager.sendRequest(requestStatuse, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseStatus = rawData.toString();
						String xmlDataStatus = rpcResponseStatus;
						final Map<String, String> statusMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataStatus));
						getView().loadOrderData(GeneralData.getOrderData(start), statusMap);
//						getView().refreshOrderData();
					}
		});
	}

	@Override
	public void onCountTotalData() {
		RPCRequest rpcRequest=new RPCRequest();
    	rpcRequest.setActionURL(GWT.getHostPageBaseURL() +  OrderDataViewerPresenter.orderDataViewerServlet + "?method=getTotalOrderData&type=RPC");
		rpcRequest.setHttpMethod("POST");
		rpcRequest.setUseSimpleHttp(true);
		rpcRequest.setShowPrompt(false);
		RPCManager.sendRequest(rpcRequest, new RPCCallback () {
				public void execute(RPCResponse response, Object rawData, RPCRequest request) {
					String total = rawData.toString().substring(14, rawData.toString().length()-18);
//					getView().setLastPage(Integer.parseInt(total));
				}
		});
	}
}
