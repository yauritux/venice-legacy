package com.gdn.venice.client.app.fraud.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.FraudParameterRule31UiHandlers;
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
 * Presenter for Fraud Parameter 31 - Genuine List Transaction
 * 
 * @author Roland
 */

public class FraudParameterRule31Presenter extends Presenter<FraudParameterRule31Presenter.MyView, FraudParameterRule31Presenter.MyProxy>
		implements FraudParameterRule31UiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String fraudParameterRulePresenterServlet = "FraudParameterRulePresenterServlet";
	/**
	 * {@link FraudParameterRule31Presenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudParameterRule31Page)
	public interface MyProxy extends Proxy<FraudParameterRule31Presenter>, Place {
	}

	/**
	 * {@link FraudParameterRule31Presenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<FraudParameterRule31UiHandlers> {
		public void loadFraudParameterRule31Data(DataSource dataSource);
		void refreshFraudParameterRule31Data();
	}

	@Inject
	public FraudParameterRule31Presenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadFraudParameterRule31Data(FraudData.getFraudParameterRule31Data());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
}
