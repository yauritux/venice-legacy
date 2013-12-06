package com.gdn.venice.client.app.fraud.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.view.handlers.FraudCalculateUiHandlers;
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

public class FraudCalculatePresenter
		extends
		Presenter<FraudCalculatePresenter.MyView, FraudCalculatePresenter.MyProxy>
		implements FraudCalculateUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String fraudCalculatePresenterServlet = "FraudCalculatePresenterServlet";
	/**
	 * {@link FraudCalculatePresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.fraudCalculatePage)
	public interface MyProxy extends Proxy<FraudCalculatePresenter>, Place {
	}

	/**
	 * {@link FraudCalculatePresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<FraudCalculateUiHandlers> {
	}

	@Inject
	public FraudCalculatePresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
	
	@Override
	public DataSource onShowFraudCalculateOrder(String dateParam) {
		DataSource dataSources = FraudData.getOrderCalculateFraud(dateParam);
		return dataSources;
	}
}
