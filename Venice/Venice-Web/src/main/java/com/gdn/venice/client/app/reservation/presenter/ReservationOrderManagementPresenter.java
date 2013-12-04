package com.gdn.venice.client.app.reservation.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.reservation.data.ReservationData;
import com.gdn.venice.client.app.reservation.view.handlers.ReservationOrderManagementUiHandlers;
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

public class ReservationOrderManagementPresenter
		extends Presenter<ReservationOrderManagementPresenter.MyView, ReservationOrderManagementPresenter.MyProxy>
		implements ReservationOrderManagementUiHandlers {
	
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;

	public final static String reservationOrderManagementPresenterServlet = "ReservationOrderManagementPresenterServlet";
	
	/**
	 * {@link ReservationOrderManagementPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.reservationOrderManagement)
	public interface MyProxy extends Proxy<ReservationOrderManagementPresenter>, Place {
	}

	/**
	 * {@link ReservationOrderManagementPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<ReservationOrderManagementUiHandlers> {
		
		public void loadReservationOrderManagementData(DataSource dataSource);
		public void refreshReservationOrderManagementData();
	}

	@Inject
	public ReservationOrderManagementPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadReservationOrderManagementData(ReservationData.getReservationOrderManagementData());
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, this);
	}
}