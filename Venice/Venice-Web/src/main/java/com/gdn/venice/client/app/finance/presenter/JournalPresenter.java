package com.gdn.venice.client.app.finance.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.client.app.finance.view.handlers.JournalUiHandlers;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
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
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.SC;


/**
 * Presenter for Journal
 * 
 * @author Henry Chandra
 */
public class JournalPresenter
		extends
		Presenter<JournalPresenter.MyView, JournalPresenter.MyProxy>
		implements JournalUiHandlers {
	@SuppressWarnings("unused")
	private final DispatchAsync dispatcher;
	
	public final static String journalPresenterServlet = "JournalPresenterServlet";

	/**
	 * {@link JournalPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.financeJournalPage)
	public interface MyProxy extends Proxy<JournalPresenter>, Place {
	}

	/**
	 * {@link JournalPresenter}'s view.
	 */
	public interface MyView extends View,
			HasUiHandlers<JournalUiHandlers> {

		void loadJournalData(DataSource dataSource);
		void refreshJournalData();
	}

	/**
	 * Links the presenter the the event bus, view, proxy and dispatcher
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param dispatcher
	 */
	@Inject
	public JournalPresenter(EventBus eventBus, MyView view,
			MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		((RafViewLayout) getView().asWidget()).setViewPageName(getProxy().getNameToken());
		
		getView().loadJournalData(FinanceData.getJournalData());
		
		this.dispatcher = dispatcher;
	}
	
	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.Presenter#revealInParent()
	 */
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextArea,
				this);
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.client.app.finance.view.handlers.PaymentProcessingUiHandlers#onSubmitForApproval(java.util.ArrayList)
	 */
	@Override
	public void onSubmitForApproval(ArrayList<String> journalGroupIdList) {
		/*
		 * Use RPC style to make the call to submit the payments for approval
		 */
		RPCRequest request=new RPCRequest();
		
		/*
		 * Extract the payment ids from the 
		 */
		HashMap<String,String>map = new HashMap<String,String>();
		for (int i=0;i<journalGroupIdList.size();i++) {
			map.put(ProcessNameTokens.JOURNALGROUPID+(i+1), journalGroupIdList.get(i));
		}
		String apPaymentIds = map.toString();
		
		request.setData(apPaymentIds);
		
		request.setActionURL(GWT.getHostPageBaseURL() + journalPresenterServlet + "?method=submitJournalForApproval&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setWillHandleError(true);
		RPCManager.setPromptStyle(PromptStyle.DIALOG);
		RPCManager.setDefaultPrompt("Submitting journals for approval...");
		RPCManager.setShowPrompt(true);

		RPCManager.sendRequest(request, 
				new RPCCallback () {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.rpc.RPCCallback#execute(com.smartgwt.client.rpc.RPCResponse, java.lang.Object, com.smartgwt.client.rpc.RPCRequest)
					 */
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						if (rpcResponse.startsWith("0")) {
							getView().refreshJournalData();
							SC.say(DataMessageTokens.SUBMITTED_FOR_APPROVAL);
						} else {
							/*
							 * Use the 2nd positional split on ":" as the error message
							 */
							String[] split = rpcResponse.split(":");
							if(split.length>1){
								SC.warn(split[1]);
							}else{
								SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
							}
						}
						RPCManager.setDefaultPrompt("Retrieving records...");
						RPCManager.setShowPrompt(false);
					}
		}
		);
		
	}


}
