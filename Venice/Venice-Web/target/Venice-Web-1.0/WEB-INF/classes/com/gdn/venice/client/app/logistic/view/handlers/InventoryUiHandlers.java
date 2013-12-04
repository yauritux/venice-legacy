package com.gdn.venice.client.app.logistic.view.handlers;

import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;

public interface InventoryUiHandlers extends UiHandlers {
	/**
	 * Called when the delete promotion share buttion for the detail list grid is clicked
	 * @param promotionShareDataMap a HashMap of the data to be deleted.
	 */
	void onDeleteInventoryClicked(HashMap<String, String> inventoryDataMap);
}
