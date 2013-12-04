package com.gdn.venice.client.app.logistic.widgets;

import com.gdn.venice.client.app.logistic.view.ProviderManagementView;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * ProviderManagementVLayoutWidget - a widget class for displaying logistics providers
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ProviderManagementVLayoutWidget extends VLayout{
	
	/*
	 * TabSet for Logistics Provider Management
	 */
	TabSet providerManagementTabSet;
	
	ProviderAddressTab providerAddressTab;
	ProviderContactTab providerContactTab;
	ProviderServiceTab providerServiceTab;
	ProviderScheduleTab providerScheduleTab;
	ProviderAgreementTab providerAgreementTab;

	/*
	 * The view Object of Provider Management View
	 */	
	ProviderManagementView providerView;

	/**
	 * Constructor to build view layout for the Logistic Provider Management Widgets (Form & Tab)
	 * @param addressData the data source to use for Logistic Provider Address
	 * @param contactData the data source to use for Logistic Provider Contact
	 * @param serviceData the data source to use for Logistic Provider Service
	 * @param scheduleData the data source to use for Logistic Provider Schedule
	 * @param agreementData the data source to use for Logistic Provider Agreement
	 * @param providerView the Logistic Provider Management view object
	 */
	public ProviderManagementVLayoutWidget( final DataSource addressData, final DataSource contactData, final DataSource serviceData,final DataSource scheduleData, final DataSource agreementData, final ListGridRecord record, final ProviderManagementView providerView) {

		this.providerView = providerView;

		providerManagementTabSet = new TabSet();
		
		providerManagementTabSet.setTabBarPosition(Side.TOP);
		providerManagementTabSet.setWidth100();
		
		providerAddressTab = new ProviderAddressTab("Address", addressData, record, providerView);
		providerContactTab = new ProviderContactTab("Contact", contactData, record, providerView);
		providerServiceTab = new ProviderServiceTab("Service", serviceData, record, providerView);
		providerScheduleTab = new ProviderScheduleTab("Schedule", scheduleData, record, providerView);
		providerAgreementTab = new ProviderAgreementTab("Agreement", agreementData, record, providerView);
		providerManagementTabSet.addTab(providerAddressTab);
		providerManagementTabSet.addTab(providerContactTab);
		providerManagementTabSet.addTab(providerServiceTab);
		providerManagementTabSet.addTab(providerAgreementTab);
		setMembers(providerManagementTabSet);
		
	}

	/**
	 * @return the providerView
	 */
	public ProviderManagementView getProviderView() {
		return providerView;
	}
}