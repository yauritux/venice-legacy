package com.gdn.venice.client.app.logistic.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.logistic.view.handlers.ExportReportUiHandlers;
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
 * Presenter for export report CX, CX finance, D
 * 
 * @author Roland
 */

public class ExportReportPresenter
		extends
		Presenter<ExportReportPresenter.MyView, ExportReportPresenter.MyProxy>
		implements ExportReportUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String exportReportPresenterServlet = "ExportReportPresenterServlet";
	/**
	 * {@link ExportReportPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.logisticExportReportPage)
	public interface MyProxy extends Proxy<ExportReportPresenter>, Place {
	}

	/**
	 * {@link ExportReportPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<ExportReportUiHandlers> {
	}

	@Inject
	public ExportReportPresenter(EventBus eventBus, MyView view,
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
}
