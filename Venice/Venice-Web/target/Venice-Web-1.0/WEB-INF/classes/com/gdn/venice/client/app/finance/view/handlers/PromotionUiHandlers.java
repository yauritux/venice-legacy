package com.gdn.venice.client.app.finance.view.handlers;

import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * UI handler methods for promotion data (header)
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public interface PromotionUiHandlers extends UiHandlers {
	
	/**
	 * This is called when the save promotion share button is clicked
	 * @param promotionShareDataMap a HashMap containing the data to be saved
	 */
	void onSavePromotionShareClicked(HashMap<String, String> promotionShareDataMap); // this also saves the detail

	/**
	 * Called when the delete promotion share buttion for the detail list grid is clicked
	 * @param promotionShareDataMap a HashMap of the data to be deleted.
	 */
	void onDeletePromotionShareClicked(HashMap<String, String> promotionShareDataMap);
}