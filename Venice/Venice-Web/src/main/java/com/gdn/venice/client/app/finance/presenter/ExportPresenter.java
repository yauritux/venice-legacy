package com.gdn.venice.client.app.finance.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.ExportData;
import com.gdn.venice.client.app.finance.view.handlers.ExportUiHandlers;
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
 * Presenter for Export
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ExportPresenter extends
		Presenter<ExportPresenter.MyView, ExportPresenter.MyProxy> implements
		ExportUiHandlers {

	public final static String exportPresenterServlet = "ExportPresenterServlet";

	/**
	 * {@link ExportPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financeExportPage)
	public interface MyProxy extends Proxy<ExportPresenter>, Place {
	}

	/**
	 * {@link ExportPresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<ExportUiHandlers> {

		void loadJornalVoucherData(DataSource dataSource);

		void refreshJornalVoucherData();

	}
	/**
	 * The constructor for the presenter that sets up the 
	 * links between the presenter, view and event bus plus
	 * links the UI handlers to the view.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public ExportPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy()
				.getNameToken());

		getView().loadJornalVoucherData(ExportData.getJournalVoucherData());
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