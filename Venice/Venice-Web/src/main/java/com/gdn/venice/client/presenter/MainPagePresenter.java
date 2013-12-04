package com.gdn.venice.client.presenter;

import java.util.HashMap;

import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.view.MainPageView;
import com.gdn.venice.client.view.handlers.MainPageUiHandlers;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;

/**
 * @author Henry Chandra
 */
public class MainPagePresenter extends
Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> implements
MainPageUiHandlers {
	
	public static String signedInUser;
	public static String signedInUserRole;
	public static String fileDownloadPresenterServlet = "FileDownloadPresenterServlet";

	private final PlaceManager placeManager;
	
	HashMap<String, String> userPermission;

	/**
	 * {@link MainPagePresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<MainPagePresenter> {
	}

	/**
	 * {@link MainPagePresenter}'s view.
	 */
	public interface MyView extends View, HasUiHandlers<MainPageUiHandlers> {
		public void updateBreadCrumb(String path);
		public void updateSignedInUser(String user);
		public void updateVersion(String version);
		public void updateMenuBasedOnUserPermission(HashMap<String, String> userPermission);
	}
	
	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetContextArea = new Type<RevealContentHandler<?>>();
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_LoginScreen = new Type<RevealContentHandler<?>>();


	@Inject
	public MainPagePresenter(EventBus eventBus, MyView view, MyProxy proxy,
			PlaceManager placeManager) {
		super(eventBus, view, proxy);

		getView().setUiHandlers(this);
		updateSignedInUser();
		updateVersion();
		this.placeManager = placeManager;
	}
	

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}
	

	@Override
	public void onNavigationPaneSectionClicked(String path, String pageName) {
		if (pageName != null && pageName.length() != 0) { 
			PlaceRequest placeRequest = new PlaceRequest(pageName); 
			placeManager.revealPlace(placeRequest);
			getView().updateBreadCrumb(pageName);
		}
	}
	
	private void updateSignedInUser() {
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
							MainPagePresenter.signedInUser = userInfo[0];
							getView().updateSignedInUser(MainPagePresenter.signedInUser);
						}
					}
		}
		);
	}
	
	
	/**
	 * This method performs RPC call to server to get the user permission and update menu based on the permission
	 * 
	 * @param pageName	the pageName to update the bread crumb
	 */
	private void getUserPermissionsAndUpdateMenuBasedOnPermission(final String pageName) {
		RPCRequest request=new RPCRequest();

		request.setData("");
		request.setActionURL(GWT.getHostPageBaseURL() + Util.authorizationUtilServlet + "?method=fetchUserPermissionList&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponse = rawData.toString();
						
						userPermission = Util.formHashMapfromXML(rpcResponse);
						
						MainPageView myView = (MainPageView)getView();
						myView.getNavigationPane().setInitialMenuPermissions(userPermission);

						getView().updateMenuBasedOnUserPermission(userPermission);
						getView().updateBreadCrumb(pageName);
						
					}
		}
		);
		
	}
	
	/**
	 * Updates the version number on the main page view
	 */
	private void updateVersion() {
		RPCRequest request=new RPCRequest();
		
		request.setActionURL(GWT.getHostPageBaseURL() + "MainPagePresenterServlet?method=getVersion&type=RPC");
		request.setHttpMethod("POST");
		request.setUseSimpleHttp(true);
		request.setShowPrompt(false);
		
		RPCManager.sendRequest(request, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						
						String version = rawData.toString();
						getView().updateVersion(version);
					}
		}
		);
	}


	/* (non-Javadoc)
	 * @see com.gdn.venice.client.view.handlers.MainPageUiHandlers#onSetInSlot(java.lang.String)
	 */
	@Override
	public void onSetInSlot(String pageName) {
		getUserPermissionsAndUpdateMenuBasedOnPermission(pageName);
	}
	
	/**
	 * This method performs RPC call to server to get the user permissions
	 * and sets the initial user permission on the menus
	 * 
	 * @param pageName	the pageName to update the bread crumb
	 */
//	private void getUserPermissionsAndSetInitialMenuPermssions() {
//		RPCRequest request=new RPCRequest();
//
//		request.setData("");
//		request.setActionURL(GWT.getHostPageBaseURL() + Util.authorizationUtilServlet + "?method=fetchUserPermissionList&type=RPC");
//		request.setHttpMethod("POST");
//		request.setUseSimpleHttp(true);
//		request.setShowPrompt(false);
//		
//		RPCManager.sendRequest(request, 
//				new RPCCallback () {
//					public void execute(RPCResponse response,
//							Object rawData, RPCRequest request) {
//						String rpcResponse = rawData.toString();
//						
//						userPermission = Util.formHashMapfromXML(rpcResponse);
//						
//						MainPageView myView = (MainPageView)getView();
//						myView.getNavigationPane().setInitialMenuPermissions(userPermission);
//					}
//		}
//		);
//		
//	}


}
