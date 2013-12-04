package com.gdn.venice.client.app.fraud.presenter;

import java.util.ArrayList;
import java.util.List;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCaseViewerUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.widgets.RafViewLayout;
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

/**
 * Presenter for Fraud Case Viewer
 * 
 * @author Roland
 */

public class FraudCaseViewerPresenter
		extends
		Presenter<FraudCaseViewerPresenter.MyView, FraudCaseViewerPresenter.MyProxy>
		implements FraudCaseViewerUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String fraudCaseMaintenancePresenterServlet = "FraudCaseMaintenancePresenterServlet";

	/**
	 * {@link FraudCaseViewerPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudCaseViewerPage)
	public interface MyProxy extends Proxy<FraudCaseViewerPresenter>, Place {
	}

	/**
	 * {@link FraudCaseViewerPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<FraudCaseViewerUiHandlers> {
		public void loadFraudCaseData(DataSource dataSource);
		void refreshFraudCaseData();
	}

	@Inject
	public FraudCaseViewerPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadFraudCaseData(FraudData.getFraudCaseData());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}

	@Override
	public List<DataSource> onShowFraudCaseDetailData(String caseId, String orderId) {
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
		dataSources.add(FraudData.getFraudCaseFraudManagementData(caseId));
		dataSources.add(FraudData.getFraudCaseActionLogData(caseId));
		dataSources.add(FraudData.getFraudCaseHistoryLogData(caseId));
		dataSources.add(FraudData.getFraudCasePaymentTypeData(caseId));
		dataSources.add(FraudData.getFraudCaseIlogFraudStatusData(caseId));
		dataSources.add(FraudData.getFraudCaseAttachmentData(caseId));	
		dataSources.add(FraudData.getFraudCasePaymentSummaryData(caseId));
		dataSources.add(FraudData.getFraudCasePaymentDetailData(caseId));
		dataSources.add(FraudData.getWhiteListOrderHistoryData(orderId));
		dataSources.add(FraudData.getOrderHistoryFilterData(orderId));
		dataSources.add(FraudData.getFraudCaseCustomerContactData(orderId));
		dataSources.add(FraudData.getFraudCaseCategoryData(orderId));
		return dataSources;
	}
}
