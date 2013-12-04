package com.gdn.venice.client.app.task.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.task.data.TaskManagementData;
import com.gdn.venice.client.app.task.view.handlers.TaskSummaryUiHandlers;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
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
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.SC;

/**
 * Presenter for Task Summary
 * 
 * @author Henry Chandra
 */
public class TaskSummaryPresenter extends
Presenter<TaskSummaryPresenter.MyView, TaskSummaryPresenter.MyProxy> implements TaskSummaryUiHandlers{
	public final String taskSummaryServlet = "TaskSummaryPresenterServlet";
	
	/**
	 * {@link TaskSummaryPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.taskSummaryPage)
	public interface MyProxy extends Proxy<TaskSummaryPresenter>, Place {
	}

	/**
	 * {@link TaskSummaryPresenter}'s view.
	 */ 
	public interface MyView extends View, HasUiHandlers<TaskSummaryUiHandlers> {
		public void updateTaskSummaryData(String taskSummaryData);
	}

	@Inject
	public TaskSummaryPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
		retrieveTaskSummaryData();
		
	}
	
	public void retrieveTaskSummaryData() {
		
		
		RPCRequest request=new RPCRequest();
		
		request.setActionURL(GWT.getHostPageBaseURL() + taskSummaryServlet + "?method=retrieveTaskSummaryData&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							rpcResponse = rpcResponse.substring(2);
							getView().updateTaskSummaryData(rpcResponse);
						} else {
							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
						}
					}
		}
		);
		
	}


	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea, 
				this);
	}


}
