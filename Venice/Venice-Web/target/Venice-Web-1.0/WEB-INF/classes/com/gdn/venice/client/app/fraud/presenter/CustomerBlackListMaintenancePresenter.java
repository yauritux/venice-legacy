package com.gdn.venice.client.app.fraud.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.CustomerBlackListMaintenanceUiHandlers;
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
 * Presenter for Customer Blacklist maintenance
 * 
 * @author Roland
 */

public class CustomerBlackListMaintenancePresenter
		extends
		Presenter<CustomerBlackListMaintenancePresenter.MyView, CustomerBlackListMaintenancePresenter.MyProxy>
		implements CustomerBlackListMaintenanceUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String customerBlackListMaintenancePresenterServlet = "CustomerBlackListMaintenancePresenterServlet";
	/**
	 * {@link CustomerBlackListMaintenancePresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudCustomerBlackListMaintenancePage)
	public interface MyProxy extends Proxy<CustomerBlackListMaintenancePresenter>, Place {
	}

	/**
	 * {@link CustomerBlackListMaintenancePresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<CustomerBlackListMaintenanceUiHandlers> {
		public void loadCustomerBlackListData(DataSource dataSource);
		void refreshCustomerBlackListData();
	}

	@Inject
	public CustomerBlackListMaintenancePresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadCustomerBlackListData(FraudData.getCustomerBlackListData());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
}
