package com.gdn.venice.client.presenter;

import java.util.HashMap;

import com.gdn.venice.client.app.NameTokens;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.view.handlers.LoginUiHandlers;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;

/**
 * @author Henry Chandra
 */
public class LoginPresenter extends
Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy> implements LoginUiHandlers{
	private final PlaceManager placeManager;
	
	/**
	 * {@link LoginPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.loginPage)
	public interface MyProxy extends Proxy<LoginPresenter>, Place {
	}

	/**
	 * {@link LoginPresenter}'s view.
	 */ 
	public interface MyView extends View, HasUiHandlers<LoginUiHandlers> {

	}

	@Inject
	public LoginPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager) {
		super(eventBus, view, proxy);
		
		getView().setUiHandlers(this);
		checkLogin();
		this.placeManager = placeManager;
	}


	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_LoginScreen, 
				this);
	}


	@Override
	public void onLogin(String userName, String password) {
		RPCRequest request=new RPCRequest();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("j_username", userName);
		params.put("j_password", password);
		
		request.setParams(params);
		request.setActionURL("j_security_check");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, null);

	}
	
	private void checkLogin() {
		RPCRequest request=new RPCRequest();
		
		request.setActionURL(GWT.getHostPageBaseURL() + "MainPagePresenterServlet?method=getSignedInUser&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						
						String[] userInfo = rawData.toString().split(":");
						
						if (userInfo.length>0) {
							String pageName = NameTokens.getPageAfterLogin();
							
							PlaceRequest placeRequest = new PlaceRequest(pageName); 
							placeManager.revealPlace(placeRequest); 
							
						}
					}
		}
		);

	}


}
