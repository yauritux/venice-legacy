package com.gdn.venice.client.app.finance.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.view.handlers.ReportsLauncherUiHandlers;
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

/**
 * Presenter for Reports Dialog
 * 
 * @author David Forden
 */
public class ReportsLauncherPresenter
		extends
		Presenter<ReportsLauncherPresenter.MyView, ReportsLauncherPresenter.MyProxy>
		implements ReportsLauncherUiHandlers {
	private final DispatchAsync dispatcher;
	
	public final static String reportsDialogServlet = "ReportsDialogPresenterServlet";

	/**
	 * {@link ReportsLauncherPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financeReportsDialogPage)
	public interface MyProxy extends Proxy<ReportsLauncherPresenter>, Place {
	}

	/**
	 * {@link ReportsLauncherPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<ReportsLauncherUiHandlers> {
	}

	@Inject
	public ReportsLauncherPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
	
}
