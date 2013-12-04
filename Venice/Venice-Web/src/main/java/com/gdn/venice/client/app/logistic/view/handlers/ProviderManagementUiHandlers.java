package com.gdn.venice.client.app.logistic.view.handlers;

import java.util.HashMap;
import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;


/**
 * UI handler methods for provider management data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public interface ProviderManagementUiHandlers extends UiHandlers {
		
	/**
	 * This is called when the  provider record is selected
	 * @param partyId  a PartyID 
	 * @param logProviderId  a LogProviderId
	 */
	public List<DataSource> onShowProviderDetailData(String partyId, String logProviderId);
	
	/**
	 * This is called when the save logistic provider button in form is clicked
	 * @param agreementDataMap a HashMap containing the data to be saved
	 */
	void onSaveProviderClicked(HashMap<String, String> providerDataMap);
	
	/**
	 * This is called when the delete logistic provider button in form is clicked
	 * @param providerDataMap a HashMap containing the data to be removed
	 */
	void onDeleteProviderClicked(HashMap<String, String> providerDataMap);
	
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
	
	/**
	 * This is called when the save contact button is clicked
	 * @param addressDataMap a HashMap containing the data to be saved
	 */
	void onSaveContactClicked(HashMap<String, String> contactDataMap);
	
	/**
	 * This is called when the remove contact button is clicked
	 * @param contactDataMap a HashMap containing the data to be removed
	 */
	void onRemoveContactClicked(HashMap<String, String> contactDataMap);
	
	/**
	 * This is called when the save service button is clicked
	 * @param serviceDataMap a HashMap containing the data to be saved
	 */
	void onSaveServiceClicked(HashMap<String, String> serviceDataMap);
	
	/**
	 * This is called when the remove service button is clicked
	 * @param serviceDataMap a HashMap containing the data to be removed
	 */
	void onRemoveServiceClicked(HashMap<String, String> serviceDataMap);
	
	/**
	 * This is called when the save service button is clicked
	 * @param scheduleDataMap a HashMap containing the data to be saved
	 */
	void onSaveScheduleClicked(HashMap<String, String> scheduleDataMap);
	
	/**
	 * This is called when the save agreement button is clicked
	 * @param agreementDataMap a HashMap containing the data to be saved
	 */
	void onSaveAgreementClicked(HashMap<String, String> agreementDataMap);
	
	/**
	 * This is called when the remove agreement button is clicked
	 * @param agreementDataMap a HashMap containing the data to be removed
	 */
	void onRemoveAgreementClicked(HashMap<String, String> agreementDataMap);
}
