package com.gdn.venice.client.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface MainPageUiHandlers extends UiHandlers {
	void onNavigationPaneSectionClicked(String path, String pageName);
	void onSetInSlot(String pageName);
}
