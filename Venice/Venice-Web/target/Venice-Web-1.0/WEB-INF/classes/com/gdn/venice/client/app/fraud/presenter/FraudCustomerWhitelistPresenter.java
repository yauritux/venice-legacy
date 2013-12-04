package com.gdn.venice.client.app.fraud.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCustomerWhitelistMaintenanceUiHandlers;
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
 * Presenter for Customer Whitelist maintenance
 * 
 * @author Arifin
 */

public class FraudCustomerWhitelistPresenter
		extends
		Presenter<FraudCustomerWhitelistPresenter.MyView, FraudCustomerWhitelistPresenter.MyProxy>
		implements FraudCustomerWhitelistMaintenanceUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String customerBlackListMaintenancePresenterServlet = "CustomerBlackListMaintenancePresenterServlet";
	/**
	 * {@link FraudCustomerWhitelistPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudCustomerWhiteListMaintenancePage)
	public interface MyProxy extends Proxy<FraudCustomerWhitelistPresenter>, Place {
	}

	/**
	 * {@link FraudCustomerWhitelistPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<FraudCustomerWhitelistMaintenanceUiHandlers> {
		public void loadCustomerWhiteListData(DataSource dataSource);
		void refreshCustomerWhiteListData();
	}

	@Inject
	public FraudCustomerWhitelistPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadCustomerWhiteListData(FraudData.getCustomerWhiteListData());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
}
