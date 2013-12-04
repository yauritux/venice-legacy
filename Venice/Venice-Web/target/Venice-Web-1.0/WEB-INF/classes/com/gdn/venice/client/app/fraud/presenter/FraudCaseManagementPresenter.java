package com.gdn.venice.client.app.fraud.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCaseManagementUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
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
 * Presenter for Fraud Case Management
 * 
 * @author Yusrin
 */

public class FraudCaseManagementPresenter
		extends
		Presenter<FraudCaseManagementPresenter.MyView, FraudCaseManagementPresenter.MyProxy>
		implements FraudCaseManagementUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String fraudCaseMaintenancePresenterServlet = "FraudCaseMaintenancePresenterServlet";
	public final static String toDoListServlet = "ToDoListPresenterServlet";

	/**
	 * {@link FraudCaseManagementPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudCaseManagementPage)
	public interface MyProxy extends Proxy<FraudCaseManagementPresenter>, Place {
	}

	/**
	 * {@link FraudCaseManagementPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<FraudCaseManagementUiHandlers> {
		public void loadFraudCaseData(DataSource dataSource);
		void refreshFraudCaseData();
	}

	@Inject
	public FraudCaseManagementPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadFraudCaseData(FraudData.getClaimedFraudCaseData());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}

	@Override
	public List<DataSource> onShowFraudCaseDetailData(String taskId, String caseId, String orderId) {
		ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
		dataSources.add(FraudData.getFraudCaseTotalRiskScoreData(caseId));
		dataSources.add(FraudData.getFraudCaseCustomerSummaryData(orderId));
		dataSources.add(FraudData.getFraudCaseCustomerData(caseId,orderId));
		dataSources.add(FraudData.getFraudCaseCustomerAddressData(orderId));
		dataSources.add(FraudData.getFraudCaseCustomerContactData(orderId));
		dataSources.add(FraudData.getFraudCaseOrderDetailData(caseId,orderId));
		dataSources.add(FraudData.getFraudCaseOrderItemData(orderId));
		dataSources.add(FraudData.getFraudCasePaymentData(caseId,orderId));
		dataSources.add(FraudData.getFraudCaseRiskScoreData(caseId));
		dataSources.add(FraudData.getFraudCaseRelatedOrderData(caseId));
		dataSources.add(FraudData.getFraudCaseClaimedFraudManagementData(taskId, caseId));
		dataSources.add(FraudData.getFraudCaseActionLogData(caseId));
		dataSources.add(FraudData.getFraudCaseHistoryLogData(caseId));
		dataSources.add(FraudData.getFraudCasePaymentTypeData(caseId));
		dataSources.add(FraudData.getFraudCaseMoreInfoOrderData(caseId));
		dataSources.add(FraudData.getFraudCaseIlogFraudStatusData(caseId));
		dataSources.add(FraudData.getFraudCaseAttachmentData(caseId));	
		dataSources.add(FraudData.getFraudCasePaymentSummaryData(caseId));
		dataSources.add(FraudData.getFraudCasePaymentDetailData(caseId));
		dataSources.add(FraudData.getFraudCaseRelatedOrderItemData(caseId));
		dataSources.add(FraudData.getFraudCaseRelatedData(caseId));
		dataSources.add(FraudData.getWhiteListOrderHistoryData(orderId));
		dataSources.add(FraudData.getOrderHistoryFilterData(orderId));
		dataSources.add(FraudData.getFraudCaseCustomerContactData(orderId));
		dataSources.add(FraudData.getFraudCaseCategoryData(orderId));
		return dataSources;
	}
	
	@Override
	public void onCloseTask(List<String> taskIds) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String,String>map = new HashMap<String,String>( 11 );
		for (int i=0;i<taskIds.size();i++) {
			map.put(DataNameTokens.TASKID+(i+1), taskIds.get(i));
		}
		String taskId = map.toString();
		
		request.setData(taskId);
		request.setActionURL(GWT.getHostPageBaseURL() + toDoListServlet + "?method=completeToDoListTask&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshFraudCaseData();
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}

					}
		}
		);
	}
}
