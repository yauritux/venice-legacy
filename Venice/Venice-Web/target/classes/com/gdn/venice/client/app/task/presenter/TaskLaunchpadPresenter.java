package com.gdn.venice.client.app.task.presenter;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.task.view.handlers.TaskLaunchpadUiHandlers;
import com.gdn.venice.client.app.task.view.handlers.TaskSummaryUiHandlers;
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

/**
 * Presenter for Task Launchpad
 * 
 * @author Henry Chandra
 */
public class TaskLaunchpadPresenter extends
Presenter<TaskLaunchpadPresenter.MyView, TaskLaunchpadPresenter.MyProxy> implements TaskLaunchpadUiHandlers{
	/**
	 * {@link TaskLaunchpadPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.taskLaunchpadPage)
	public interface MyProxy extends Proxy<TaskLaunchpadPresenter>, Place {
	}

	/**
	 * {@link TaskLaunchpadPresenter}'s view.
	 */ 
	public interface MyView extends View, HasUiHandlers<TaskLaunchpadUiHandlers> {

	}

	@Inject
	public TaskLaunchpadPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
		
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
	}


	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, 
				this);
	}


}
