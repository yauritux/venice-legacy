package com.gdn.venice.client.app.fraud.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.UncalculatedCreditCardOrderUiHandlers;
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
 * Presenter for Fraud Calculate
 * 
 * @author Yusrin
 */

public class UncalculatedCreditCardOrderPresenter
		extends Presenter<UncalculatedCreditCardOrderPresenter.MyView, UncalculatedCreditCardOrderPresenter.MyProxy>
		implements UncalculatedCreditCardOrderUiHandlers {
	
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String uncalculatedCreditCardOrderPresenterServlet = "UncalculatedCreditCardOrderPresenterServlet";
	
	/**
	 * {@link UncalculatedCreditCardOrderPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudUncalculatedCreditCardOrder)
	public interface MyProxy extends Proxy<UncalculatedCreditCardOrderPresenter>, Place {
	}

	/**
	 * {@link UncalculatedCreditCardOrderPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<UncalculatedCreditCardOrderUiHandlers> {
		
		public void loadUncalculatedCreditCardOrderData(DataSource dataSource);
		public void refreshUncalculatedCreditCardOrderData();
	}

	@Inject
	public UncalculatedCreditCardOrderPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadUncalculatedCreditCardOrderData(FraudData.getUncalculatedCreditCardOrderData());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
}