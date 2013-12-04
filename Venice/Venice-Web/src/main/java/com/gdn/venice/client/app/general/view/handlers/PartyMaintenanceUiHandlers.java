package com.gdn.venice.client.app.general.view.handlers;

import java.util.HashMap;
import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

/**
 * UI handler methods for party data (header)
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public interface PartyMaintenanceUiHandlers extends UiHandlers {

	/**
	 * This is called when the  party record is selected
	 * @param partyId 
	 */
	public List<DataSource> onShowPartyDetailData(String partyId);
	
	/**
	 * This is called when the save address button is clicked
	 * @param addressDataMap a HashMap containing the data to be saved
	 */
	void onSaveAddressClicked(HashMap<String, String> addressDataMap); // this also saves the detail
	
	/**
	 * This is called when the remove address button is clicked
	 * @param addressDataMap a HashMap containing the data to be removed
	 */
	void onRemoveAddressClicked(HashMap<String, String> addressDataMap); 

}
