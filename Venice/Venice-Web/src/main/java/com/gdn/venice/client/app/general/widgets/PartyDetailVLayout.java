package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.general.view.PartyMaintenanceView;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * PartyDetailVLayout - a widget class for displaying party and its details.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PartyDetailVLayout extends VLayout{
	
	PartyMaintenanceView partyView = null;
	
	PartyContactDetailTab partyContactDetailTab;
	PartyAddressTab partyAddressTab;
	
	/**
	 * Constructor to build the view layout
	 * @param contactDetailData the data source to use
	 * @param addressData the data source to use
	 * @param partyView the view object
	 */
	public PartyDetailVLayout(final DataSource contactDetailData, final DataSource addressData, final PartyMaintenanceView partyView){
		this.partyView = partyView;

		TabSet partyDetailTabSet = new TabSet();

		partyDetailTabSet.setTabBarPosition(Side.TOP);
		partyDetailTabSet.setWidth100();

		partyContactDetailTab = new PartyContactDetailTab("Contact Detail",contactDetailData, partyView);
		partyAddressTab = new PartyAddressTab("Address", addressData, partyView);

		partyDetailTabSet.addTab(partyContactDetailTab);
		partyDetailTabSet.addTab(partyAddressTab);
		
		setMembers(partyDetailTabSet);
	}

	/**
	 * @return the partyView
	 */
	public PartyMaintenanceView getPartyView() {
		return partyView;
	}

}