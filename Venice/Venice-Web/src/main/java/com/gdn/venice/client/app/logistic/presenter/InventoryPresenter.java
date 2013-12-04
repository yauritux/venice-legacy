package com.gdn.venice.client.app.logistic.presenter;

import java.util.HashMap;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.logistic.data.LogisticsData;
import com.gdn.venice.client.app.logistic.view.handlers.InventoryUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.inject.Inject;
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

public class InventoryPresenter extends
		Presenter<InventoryPresenter.MyView, InventoryPresenter.MyProxy>
		implements InventoryUiHandlers {

	public final static String inventoryServlet = "InventoryPresenterServlet";
	
	/**
	 * {@link InventoryPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.logisticInventoryPage)
	public interface MyProxy extends Proxy<InventoryPresenter>, Place {
	}

	/**
	 * {@link InventoryPresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<InventoryUiHandlers> {
		public void loadInventoryData(DataSource dataSource);
		public void refreshInventoryData();
	}
	
	@Inject
	public InventoryPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		getView().loadInventoryData(LogisticsData.getInventoryData());
	}


	@Override
	public void onDeleteInventoryClicked(
			HashMap<String, String> inventoryDataMap) {
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,this);
	}

}
