package com.gdn.venice.client.app.logistic.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.general.data.GeneralData;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.view.handlers.DeliveryStatusTrackingUiHandlers;
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

/**
 * Presenter for Logistics Delivery Status Tracking Screen
 * 
 * @author Henry Chandra
 */
public class DeliveryStatusTrackingPresenter
		extends
		Presenter<DeliveryStatusTrackingPresenter.MyView, DeliveryStatusTrackingPresenter.MyProxy>
		implements DeliveryStatusTrackingUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String deliveryStatusTrackingServlet = "DeliveryStatusTrackingPresenterServlet";

	/**
	 * {@link DeliveryStatusTrackingPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.logisticDeliveryStatusTrackingPage)
	public interface MyProxy extends Proxy<DeliveryStatusTrackingPresenter>, Place {
		
	}

	/**
	 * {@link DeliveryStatusTrackingPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<DeliveryStatusTrackingUiHandlers> {
		public void loadDeliveryStatusTrackingData(DataSource dataSource, Map<String,String> service, Map<String,String> status);
		public void refreshDeliveryStatusTrackingData();
	}

	/**
	 * Links the presenter to the view, proxy, event bus and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public DeliveryStatusTrackingPresenter(EventBus eventBus, MyView view,
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
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
	
	@Override
	public List<DataSource> onShowDeliveryStatusTrackingDetail(String airwayBillId, String orderItemId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		dataSources.add(LogisticsData.getDeliveryStatusTrackingDetailData(airwayBillId));
		dataSources.add(GeneralData.getOrderHistoryOrderItemDataByOrderItemId(orderItemId));
		return dataSources;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.logistic.view.handlers.DeliveryStatusTrackingUiHandlers#onShowMerchantPickUpDetail(java.lang.String)
	 */
	@Override
	public DataSource onShowMerchantPickUpDetail(String airwayBillId) {
		return LogisticsData.getMerchantPickUpInstructionData(airwayBillId);
	}
	
	@Override
	public void onFetchComboBoxData() {	
		//Request service combo
		RPCRequest requestService=new RPCRequest();
		requestService = new RPCRequest();
		requestService.setActionURL(GWT.getHostPageBaseURL() + deliveryStatusTrackingServlet+"?method=fetchServiceComboBoxData&type=RPC");
		requestService.setHttpMethod("POST");
		requestService.setUseSimpleHttp(true);
		requestService.setShowPrompt(false);
		RPCManager.sendRequest(requestService, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseService = rawData.toString();
						String xmlDataService = rpcResponseService;
						final Map<String, String> serviceMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataService));
						
						//Request order item status combo
						RPCRequest requestStatus = new RPCRequest();
						requestStatus.setActionURL(GWT.getHostPageBaseURL() + deliveryStatusTrackingServlet+"?method=fetchOrderStatusComboBoxData&type=RPC");
						requestStatus.setHttpMethod("POST");
						requestStatus.setUseSimpleHttp(true);
						requestStatus.setShowPrompt(false);
						
						RPCManager.sendRequest(requestStatus, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseStatus = rawData.toString();
										String xmlDataStatus = rpcResponseStatus;
										final Map<String, String> statusMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataStatus));
										getView().loadDeliveryStatusTrackingData(LogisticsData.getDeliveryStatusTrackingData("0"), serviceMap, statusMap);
									}
						});
				}
		});
	}
}
