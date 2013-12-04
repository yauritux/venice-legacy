package com.gdn.venice.client.app.finance.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.CoaSetupData;
import com.gdn.venice.client.app.finance.view.handlers.CoaSetupUiHandlers;
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
 * Presenter for COA Setup
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class CoaSetupPresenter extends
		Presenter<CoaSetupPresenter.MyView, CoaSetupPresenter.MyProxy>
		implements CoaSetupUiHandlers {

	public final static String coaSetupPresenterServlet = "CoaSetupPresenterServlet";
	
	/**
	 * {@link CoaSetupPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<CoaSetupUiHandlers> {

		void loadAccountData(DataSource dataSource);
		void refreshAccountData();	
	}
	
	/**
	 * {@link CoaSetupPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financeCOASetupPage)
	public interface MyProxy extends Proxy<CoaSetupPresenter>, Place {

	}
	
	/**
	 * Links the presenter to the view, proxy, event bus and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public CoaSetupPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
		getView().loadAccountData(CoaSetupData.getAccountData());
	}
	
	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
	 */
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);	
	}

}