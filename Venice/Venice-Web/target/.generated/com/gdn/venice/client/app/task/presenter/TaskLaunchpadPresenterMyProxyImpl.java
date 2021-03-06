package com.gdn.venice.client.app.task.presenter;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.inject.client.AsyncProvider;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.StandardProvider;
import com.gwtplatform.mvp.client.CodeSplitProvider;
import com.gwtplatform.mvp.client.CodeSplitBundleProvider;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.ProxyImpl;
import com.gwtplatform.mvp.client.proxy.ProxyPlaceImpl;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.DelayedBind;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlaceImpl;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TaskLaunchpadPresenterMyProxyImpl extends com.gwtplatform.mvp.client.proxy.ProxyPlaceImpl<TaskLaunchpadPresenter> implements com.gdn.venice.client.app.task.presenter.TaskLaunchpadPresenter.MyProxy, com.gwtplatform.mvp.client.DelayedBind {
  
  private com.gdn.venice.client.app.VeniceGinjector ginjector;
  
  public static class WrappedProxy
  extends com.gwtplatform.mvp.client.proxy.ProxyImpl<TaskLaunchpadPresenter> {
    
    public WrappedProxy() {}
    
    private void delayedBind( com.gdn.venice.client.app.VeniceGinjector ginjector ) {
      bind( ginjector.getProxyFailureHandler() );
      EventBus eventBus = ginjector.getEventBus();
      presenter = new CodeSplitProvider<TaskLaunchpadPresenter>( ginjector.getTaskLaunchpadPresenter() );
      
    }
  }
  
  public TaskLaunchpadPresenterMyProxyImpl() {
    DelayedBindRegistry.register(this);
  }
  
  @Override
  public void delayedBind( Ginjector baseGinjector ) {
    ginjector = (com.gdn.venice.client.app.VeniceGinjector)baseGinjector;
    bind(  ginjector.getProxyFailureHandler(), 
        ginjector.getPlaceManager(),
        ginjector.getEventBus() );
    WrappedProxy wrappedProxy = GWT.create(WrappedProxy.class);
    wrappedProxy.delayedBind( ginjector ); 
    proxy = wrappedProxy; 
    String nameToken = "!taskLaunchpadPage"; 
    place = new com.gwtplatform.mvp.client.proxy.PlaceImpl( nameToken );
  }
}
