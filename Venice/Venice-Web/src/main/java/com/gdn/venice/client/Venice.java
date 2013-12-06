package com.gdn.venice.client;

import com.gdn.venice.client.app.VeniceGinjector;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.smartgwt.client.util.DateUtil;

/**
 * @author Henry Chandra
 */
public class Venice implements EntryPoint {
	public final VeniceGinjector ginjector = GWT.create(VeniceGinjector.class);

	@Override
	public void onModuleLoad() {
		
		DateUtil.setDefaultDisplayTimezone("00:00"); 
		
		Window.enableScrolling(false);
		Window.setMargin("0px");

		DelayedBindRegistry.bind(ginjector);

		ginjector.getPlaceManager().revealCurrentPlace();
	}
}
