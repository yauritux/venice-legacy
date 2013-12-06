package com.gdn.venice.client.app.testrestdatasource.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.testrestdatasource.view.handlers.TestRestDataSourceUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
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

/**
 * Presenter for Test Data Source Screen
 * 
 * @author Henry Chandra
 */
public class TestRestDataSourcePresenter
		extends
		Presenter<TestRestDataSourcePresenter.MyView, TestRestDataSourcePresenter.MyProxy>
		implements TestRestDataSourceUiHandlers {
	private final DispatchAsync dispatcher;

	/**
	 * {@link LogisticsDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.testRestDataSource)
	public interface MyProxy extends Proxy<TestRestDataSourcePresenter>, Place {
	}

	/**
	 * {@link LogisticsDashboardPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<TestRestDataSourceUiHandlers> {
	}

	@Inject
	public TestRestDataSourcePresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}

}
