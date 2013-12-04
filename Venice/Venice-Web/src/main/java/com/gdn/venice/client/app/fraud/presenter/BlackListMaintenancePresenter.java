package com.gdn.venice.client.app.fraud.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.BlackListMaintenanceUiHandlers;
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
 * Presenter for Blacklist maintenance
 * 
 * @author Roland
 */

public class BlackListMaintenancePresenter
		extends
		Presenter<BlackListMaintenancePresenter.MyView, BlackListMaintenancePresenter.MyProxy>
		implements BlackListMaintenanceUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String blackListMaintenancePresenterServlet = "BlackListMaintenancePresenterServlet";
	/**
	 * {@link BlackListMaintenancePresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudBlackListMaintenancePage)
	public interface MyProxy extends Proxy<BlackListMaintenancePresenter>, Place {
	}

	/**
	 * {@link BlackListMaintenancePresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<BlackListMaintenanceUiHandlers> {
		public void loadBlackListData(DataSource dataSource);
		void refreshBlackListData();
	}

	@Inject
	public BlackListMaintenancePresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadBlackListData(FraudData.getBlackListData());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
}
