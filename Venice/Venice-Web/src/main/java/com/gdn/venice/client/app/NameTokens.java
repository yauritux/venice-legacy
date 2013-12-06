package com.gdn.venice.client.app;

/**
 * This class holds the list of pages in the application
 * 
 * @author Henry Chandra
 */

//TODO: Whenever we add a new page/screen on the application, need to add a new entry in this class (a new static final string and the getter)
//TODO: Don't forget to assign the corresponding NameTokens entries to the NavigationStackSection and the Presenter of the new screen
public class NameTokens {
	public static final String loginPage = "!loginPage";
	public static final String testRestDataSource = "!testRestDataSourcePage";
	
	public static final String taskSummaryPage = "!taskSummaryPage";
	public static final String toDoListPage = "!toDoListPage";
	public static final String assignedTaskPage = "!assignedTaskPage";
	public static final String taskLaunchpadPage = "!taskLaunchpadPage";
	
	public static final String fraudDashboardPage = "!fraudDashboardPage";
	public static final String fraudCaseManagementPage = "!fraudCaseManagementPage";
	public static final String fraudCaseViewerPage = "!fraudCaseViewerPage";
	public static final String fraudAdministrationPage = "!fraudAdministrationPage";
	public static final String fraudBlackListMaintenancePage = "!fraudBlackListMaintenancePage";
	public static final String fraudCustomerBlackListMaintenancePage = "!fraudCustomerBlackListMaintenancePage";
	public static final String fraudCustomerWhiteListMaintenancePage = "!fraudCustomerWhiteListMaintenancePage";
	public static final String fraudBinCreditLimitPage = "!fraudBinCreditLimitPage";
	public static final String fraudCalculatePage = "!fraudCalculatePage";
	public static final String fraudMigsUpload = "!fraudMigsUploadPage";
	public static final String fraudMasterMigs = "!fraudMasterMigsPage";
	public static final String fraudUncalculatedCreditCardOrder = "!fraudUncalculatedCreditCardOrder";
	public static final String fraudParameterRule31Page = "!fraudParameterRule31Page";
	
	public static final String logisticDashboardPage = "!logisticDashboardPage";
	public static final String logisticDeliveryStatusTrackingPage = "!logisticDeliveryStatusTrackingPage";
	public static final String logisticActivityReportReconciliationPage = "!logisticActivityReportReconciliationPage";
	public static final String logisticProviderManagementPage = "!logisticProviderManagementPage";
	public static final String logisticInvoiceReconciliationPage = "!logisticInvoiceReconciliationPage";
	public static final String logisticInventoryPage = "!logisticInventoryPage";
	public static final String logisticExportReportPage = "!logisticExportReportPage";
	
	public static final String financeDashboardPage = "!financeDashboardPage";
	public static final String financeFundInReconciliationPage = "!financeFundInReconciliationPage";
	public static final String salesRecordPage = "!salesRecordPage";
	public static final String financePaymentProcessingPage = "!financePaymentProcessingPage";
	public static final String financeManualJournalPage = "!financeManualJournalPage";
	public static final String financeJournalPage = "!financeJournalPage";
	public static final String financeReportsDialogPage = "!financeReportsDialogPage";
	public static final String financePromotionPage = "!financePromotionPage";
	public static final String financeExportPage = "!financeExportPage";
	public static final String financePeriodSetupPage = "!financePeriodSetupPage";
	public static final String financeCOASetupPage = "!financeCOASetupPage";
	
	public static final String kpiSetupPage = "!kpiSetupPage";
	public static final String kpiDashboardPage = "!kpiDashboardPage";
	public static final String kpiDetailViewerPage = "!kpiDetailViewerPage";
	
	public static final String adminRoleProfileUserGroup = "!adminRoleProfileUserGroup";
	public static final String adminModuleConfiguration = "!adminModuleConfiguration";
	
	public static final String generalOrderDataViewer = "!generalOrderDataViewer";
	public static final String generalReturDataViewer = "!generalReturDataViewer";
	public static final String generalPartyMaintenance = "!generalPartyMaintenance";

	public static final String reservationOrderManagement= "!reservationOrderManagement";

	public static String getPageAfterLogin() {
		return taskSummaryPage;
	}
}