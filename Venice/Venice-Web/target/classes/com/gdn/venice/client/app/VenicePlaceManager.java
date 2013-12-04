package com.gdn.venice.client.app;

import com.google.inject.Inject;

import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

/**
 * @author Henry Chandra
 */
public class VenicePlaceManager extends PlaceManagerImpl {
	private final PlaceRequest defaultPlaceRequest;

	@Inject
	public VenicePlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, @DefaultPlace String defaultNameToken) {
		super(eventBus, tokenFormatter);

	    this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest);
	}
	
}
