package com.gdn.venice.client.ui.data;

import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.app.NameTokens;

/**
 * This class contains the menau data definition for the finance stack section
 *
 */
public class FinanceNavigationPaneSectionData {
	private static NavigationPaneTreeNodeRecord[] records;

	  public static NavigationPaneTreeNodeRecord[] getRecords() {
		if (records == null) {
		  records = getNewRecords();
		}
		return records;
	  }

	  public static NavigationPaneTreeNodeRecord[] getNewRecords() {
		return new NavigationPaneTreeNodeRecord[]{
		  new NavigationPaneTreeNodeRecord("2", "1", "Finance Module", null, "FM1", DataWidgetNameTokens.FIN_FINANCEMODULETREENODE),
		  new NavigationPaneTreeNodeRecord("3", "2", "Finance Dashboard", NameTokens.financeDashboardPage, "FM2", DataWidgetNameTokens.FIN_FINANCEDASHBOARDREENODE),
		  new NavigationPaneTreeNodeRecord("4", "2", "Funds-In Reconciliation", NameTokens.financeFundInReconciliationPage, "FM3", DataWidgetNameTokens.FIN_SALESRECORDTREENODE),
		  new NavigationPaneTreeNodeRecord("5", "2", "Sales Record", NameTokens.salesRecordPage, "FM4", DataWidgetNameTokens.FIN_FUNDSINRECONCILIATIONTREENODE),
		  new NavigationPaneTreeNodeRecord("6", "2", "Payment Processing", NameTokens.financePaymentProcessingPage, "FM5", DataWidgetNameTokens.FIN_PAYMENTPROCESSINGTREENODE),
		  new NavigationPaneTreeNodeRecord("7", "2", "Manual Journal", NameTokens.financeManualJournalPage, "FM6", DataWidgetNameTokens.FIN_MANUALJOURNALTREENODE),
		  new NavigationPaneTreeNodeRecord("8", "2", "Journals", NameTokens.financeJournalPage, "FM7", DataWidgetNameTokens.FIN_JOURNALSTREENODE),
		  new NavigationPaneTreeNodeRecord("9", "2", "Export", NameTokens.financeExportPage, "FM8", DataWidgetNameTokens.FIN_EXPORTTREENODE),
		  new NavigationPaneTreeNodeRecord("10", "2", "Administration", null, "FM9", DataWidgetNameTokens.FIN_ADMINISTRATIONTREENODE),
		  //new NavigationPaneTreeNodeRecord("11", "10", "Journal Setup", null, "FM10", DataWidgetNameTokens.FIN_JOURNALSETUPTREENODE),
		  new NavigationPaneTreeNodeRecord("12", "10", "COA Setup", NameTokens.financeCOASetupPage, "FM11", DataWidgetNameTokens.FIN_COASETUPTREENODE),
		  //new NavigationPaneTreeNodeRecord("13", "10", "Rolled Up Journal Setup", null, "FM12", DataWidgetNameTokens.FIN_ROLLEDUPJOURNALSETUPTREENODE),
		  new NavigationPaneTreeNodeRecord("14", "10", "Period Setup", NameTokens.financePeriodSetupPage, "FM13", DataWidgetNameTokens.FIN_PERIODSETUPTREENODE),
		  new NavigationPaneTreeNodeRecord("15", "10", "Reports", NameTokens.financeReportsDialogPage, "FM14", DataWidgetNameTokens.FIN_REPORTSDIALOGTREENODE),
		  new NavigationPaneTreeNodeRecord("16", "10", "Promotion", NameTokens.financePromotionPage, "FM15", DataWidgetNameTokens.FIN_PROMOTIONTREENODE)
		};
	  }
}
