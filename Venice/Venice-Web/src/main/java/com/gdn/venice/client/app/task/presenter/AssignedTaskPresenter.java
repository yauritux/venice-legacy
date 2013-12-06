package com.gdn.venice.client.app.task.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.task.view.handlers.AssignedTaskUiHandlers;
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
 * Presenter for Assigned Task
 * 
 * @author Henry Chandra
 */
public class AssignedTaskPresenter
		extends
		Presenter<AssignedTaskPresenter.MyView, AssignedTaskPresenter.MyProxy>
		implements AssignedTaskUiHandlers {
	private final DispatchAsync dispatcher;

	/**
	 * {@link LogisticsDashboardPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.assignedTaskPage)
	public interface MyProxy extends Proxy<AssignedTaskPresenter>, Place {
	}

	/**
	 * {@link LogisticsDashboardPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<AssignedTaskUiHandlers> {
	}

	@Inject
	public AssignedTaskPresenter(EventBus eventBus, MyView view,
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
