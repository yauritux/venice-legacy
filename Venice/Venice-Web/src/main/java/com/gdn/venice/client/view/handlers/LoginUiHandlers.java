package com.gdn.venice.client.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface LoginUiHandlers extends UiHandlers {
	void onLogin(String userName, String password);
}
