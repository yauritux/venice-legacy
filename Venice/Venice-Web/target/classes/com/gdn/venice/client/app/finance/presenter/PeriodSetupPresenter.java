package com.gdn.venice.client.app.finance.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.PeriodSetupData;
import com.gdn.venice.client.app.finance.view.handlers.PeriodSetupUiHandlers;
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
 * Presenter for Period Setup
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PeriodSetupPresenter extends
		Presenter<PeriodSetupPresenter.MyView, PeriodSetupPresenter.MyProxy>
		implements PeriodSetupUiHandlers {

	/*
	 * The servlet to handle the back end side of the presenter
	 */
	public final static String periodSetupServlet = "PeriodSetupPresenterServlet";

//	/*
//	 * An async dispatcher
//	 */
//	protected final DispatchAsync dispatcher;

	/**
	 * {@link PeriodSetupPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financePeriodSetupPage)
	public interface MyProxy extends Proxy<PeriodSetupPresenter>, Place {

	}

	/**
	 * {@link PeriodSetupPresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<PeriodSetupUiHandlers> {

		/**
		 * Load Period data which is appears at top screen
		 * 
		 * @param dataSource
		 */
		void loadPeriodData(DataSource dataSource);

		void refreshPeriodData();
	}

	/**
	 * The constructor for the presenter that sets up the links between the
	 * presenter, view and event bus plus links the UI handlers to the view.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public PeriodSetupPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			DispatchAsync dispatcher) {
		super(eventBus, view, proxy);

		getView().setUiHandlers(this);

		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy()
				.getNameToken());

		getView().loadPeriodData(PeriodSetupData.getPeriodSetupData());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
	 */
	@Override
	protected void revealInParent() {

		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
}
