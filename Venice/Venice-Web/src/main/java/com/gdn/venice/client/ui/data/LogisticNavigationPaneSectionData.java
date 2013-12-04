package com.gdn.venice.client.ui.data;

import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.app.NameTokens;

/**
 * This class contains the menu data definition for the logistics stack section
 *
 */
public class LogisticNavigationPaneSectionData {
	private static NavigationPaneTreeNodeRecord[] records;

	  public static NavigationPaneTreeNodeRecord[] getRecords() {
		if (records == null) {
		  records = getNewRecords();
		}
		return records;
	  }

	  public static NavigationPaneTreeNodeRecord[] getNewRecords() {
		return new NavigationPaneTreeNodeRecord[]{
		  new NavigationPaneTreeNodeRecord("2", "1", "Logistics Module", null, "LM1", DataWidgetNameTokens.LOG_LOGISTICSMODULETREENODE),
		  new NavigationPaneTreeNodeRecord("3", "2", "Logistics Dashboard", NameTokens.logisticDashboardPage, "LM2", DataWidgetNameTokens.LOG_LOGISTICSDASHBOARDTREENODE),
		  new NavigationPaneTreeNodeRecord("4", "2", "Delivery Status Tracking", NameTokens.logisticDeliveryStatusTrackingPage, "LM3", DataWidgetNameTokens.LOG_DELIVERYSTATUSTRACKINGTREENODE),
		  new NavigationPaneTreeNodeRecord("5", "2", "Report Reconciliation", NameTokens.logisticActivityReportReconciliationPage, "LM4", DataWidgetNameTokens.LOG_REPORTRECONCILIATIONTREENODE),
		  new NavigationPaneTreeNodeRecord("6", "2", "Invoice Reconciliation", NameTokens.logisticInvoiceReconciliationPage, "LM5", DataWidgetNameTokens.LOG_INVOICERECONCILIATIONTREENODE),
		  new NavigationPaneTreeNodeRecord("7", "2", "Provider Management", NameTokens.logisticProviderManagementPage, "LM6", DataWidgetNameTokens.LOG_PROVIDERMANAGEMENTTREENODE),
		  new NavigationPaneTreeNodeRecord("8", "2", "Inventory", NameTokens.logisticInventoryPage, "LM7", DataWidgetNameTokens.LOG_INVENTORYTREENODE),
		  new NavigationPaneTreeNodeRecord("9", "2", "Export Report", NameTokens.logisticExportReportPage, "LM8", DataWidgetNameTokens.LOG_EXPORTREPORTTREENODE)
		};
	  }
}
