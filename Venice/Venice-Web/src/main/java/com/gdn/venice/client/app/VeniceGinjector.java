package com.gdn.venice.client.app;

import com.gdn.venice.client.app.administration.presenter.ModuleConfigurationPresenter;
import com.gdn.venice.client.app.administration.presenter.RoleProfileUserGroupManagementPresenter;
import com.gdn.venice.client.app.finance.presenter.CoaSetupPresenter;
import com.gdn.venice.client.app.finance.presenter.ExportPresenter;
import com.gdn.venice.client.app.finance.presenter.FinanceDashboardPresenter;
import com.gdn.venice.client.app.finance.presenter.FundInReconciliationPresenter;
import com.gdn.venice.client.app.finance.presenter.JournalPresenter;
import com.gdn.venice.client.app.finance.presenter.ManualJournalPresenter;
import com.gdn.venice.client.app.finance.presenter.PaymentProcessingPresenter;
import com.gdn.venice.client.app.finance.presenter.PeriodSetupPresenter;
import com.gdn.venice.client.app.finance.presenter.PromotionPresenter;
import com.gdn.venice.client.app.finance.presenter.ReportsLauncherPresenter;
import com.gdn.venice.client.app.finance.presenter.SalesRecordPresenter;
import com.gdn.venice.client.app.fraud.presenter.BlackListMaintenancePresenter;
import com.gdn.venice.client.app.fraud.presenter.CustomerBlackListMaintenancePresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudBinCreditLimitPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudCalculatePresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudCaseManagementPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudCaseViewerPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudCustomerWhitelistPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudDashboardPresenter;
import com.gdn.venice.client.app.fraud.presenter.FraudParameterRule31Presenter;
import com.gdn.venice.client.app.fraud.presenter.MigsMasterPresenter;
import com.gdn.venice.client.app.fraud.presenter.MigsUploadPresenter;
import com.gdn.venice.client.app.fraud.presenter.UncalculatedCreditCardOrderPresenter;
import com.gdn.venice.client.app.general.presenter.OrderDataViewerPresenter;
import com.gdn.venice.client.app.general.presenter.PartyMaintenancePresenter;
import com.gdn.venice.client.app.general.presenter.ReturDataViewerPresenter;
import com.gdn.venice.client.app.kpi.presenter.KpiDashboardPresenter;
import com.gdn.venice.client.app.kpi.presenter.KpiDetailViewerPresenter;
import com.gdn.venice.client.app.kpi.presenter.KpiSetupPresenter;
import com.gdn.venice.client.app.logistic.presenter.ActivityReportReconciliationPresenter;
import com.gdn.venice.client.app.logistic.presenter.DeliveryStatusTrackingPresenter;
import com.gdn.venice.client.app.logistic.presenter.ExportReportPresenter;
import com.gdn.venice.client.app.logistic.presenter.InventoryPresenter;
import com.gdn.venice.client.app.logistic.presenter.InvoiceReconciliationPresenter;
import com.gdn.venice.client.app.logistic.presenter.LogisticsDashboardPresenter;
import com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter;
import com.gdn.venice.client.app.reservation.presenter.ReservationOrderManagementPresenter;
import com.gdn.venice.client.app.task.presenter.AssignedTaskPresenter;
import com.gdn.venice.client.app.task.presenter.TaskLaunchpadPresenter;
import com.gdn.venice.client.app.task.presenter.TaskSummaryPresenter;
import com.gdn.venice.client.app.task.presenter.ToDoListPresenter;
import com.gdn.venice.client.app.testrestdatasource.presenter.TestRestDataSourcePresenter;
import com.gdn.venice.client.presenter.LoginPresenter;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

/**
 * This class is needed by Ginjector to get a reference elements in the application,
 * including the presenter for pages/screens
 * 
 * @author Henry Chandra
 */

//TODO: Whenever we add a new page/screen on the application, need to add a new entry in this class (add getter for presenter)
@GinModules({DispatchAsyncModule.class, VeniceClientModule.class})
public interface VeniceGinjector extends Ginjector {
  EventBus getEventBus();

  PlaceManager getPlaceManager();

  ProxyFailureHandler getProxyFailureHandler();

  Provider<MainPagePresenter> getMainPagePresenter();
  
  AsyncProvider<TestRestDataSourcePresenter> getTestRestDataSourcePresenter();
  
  AsyncProvider<LoginPresenter> getLoginPresenter();
    
  AsyncProvider<TaskSummaryPresenter> getTaskSummaryPresenter();
  
  AsyncProvider<ToDoListPresenter> getToDoListPresenter();
  
  AsyncProvider<AssignedTaskPresenter> getAssignedTaskPresenter();
  
  AsyncProvider<TaskLaunchpadPresenter> getTaskLaunchpadPresenter();
  
  AsyncProvider<FraudDashboardPresenter> getFraudDashboardPresenter();
  
  AsyncProvider<FraudCaseManagementPresenter> getFraudCaseManagementPresenter();
  
  AsyncProvider<FraudCaseViewerPresenter> getFraudCaseViewerPresenter();
  
  AsyncProvider<FraudBinCreditLimitPresenter> getFraudBinCreditLimitPresenter();
  
  AsyncProvider<BlackListMaintenancePresenter> getBlackListMaintenancePresenter();
  
  AsyncProvider<CustomerBlackListMaintenancePresenter> getCustomerBlackListMaintenancePresenter();
    
  AsyncProvider<FraudCalculatePresenter> getFraudCalulatePresenter();
  
  AsyncProvider<MigsUploadPresenter> getMigsUploadPresenter();
  
  AsyncProvider<UncalculatedCreditCardOrderPresenter> getUncalculatedCreditCardOrderPresenter();

  AsyncProvider<ReservationOrderManagementPresenter> getReservationOrderManagementPresenter();
  
  AsyncProvider<LogisticsDashboardPresenter> getLogisticsDashboardPresenter();
  
  AsyncProvider<ProviderManagementPresenter> getProviderManagementPresenter();
  
  AsyncProvider<DeliveryStatusTrackingPresenter> getLogisticDeliveryStatusTrackingPresenter();
  
  AsyncProvider<ActivityReportReconciliationPresenter> getLogisticActivityReportReconciliationPresenter();
  
  AsyncProvider<InvoiceReconciliationPresenter> getLogisticInvoiceReconciliationPresenter();
  
  AsyncProvider<FinanceDashboardPresenter> getFinanceDashboardPresenter();
  
  AsyncProvider<FundInReconciliationPresenter> getFundInReconciliationPresenter();
  
  AsyncProvider<SalesRecordPresenter> getSalesRecordPresenter();
  
  AsyncProvider<PaymentProcessingPresenter> getPaymentProcessingPresenter();
  
  AsyncProvider<ManualJournalPresenter> getManualJournalPresenter();
  
  AsyncProvider<JournalPresenter> getJournalPresenter();
  
  AsyncProvider<KpiSetupPresenter> getKpiSetupPresenter();
  
  AsyncProvider<KpiDashboardPresenter> getKpiDashboardPresenter();
  
  AsyncProvider<KpiDetailViewerPresenter> getKpiDetailViewerPresenter();
  
  AsyncProvider<RoleProfileUserGroupManagementPresenter> getRoleProfileUserGroupManagementPresenter();
  
  AsyncProvider<ModuleConfigurationPresenter> getModuleConfigurationPresenter();
  
  AsyncProvider<OrderDataViewerPresenter> getOrderDataViewerPresenter();
  
  AsyncProvider<ReturDataViewerPresenter> getReturDataViewerPresenter();
  
  AsyncProvider<ReportsLauncherPresenter> getReportsLauncherPresenter();

  AsyncProvider<PromotionPresenter> getPromotionPresenter();
  
  AsyncProvider<PartyMaintenancePresenter> getPartyMaintenancePresenter();
  
  AsyncProvider<ExportPresenter> getExportPresenter();
  
  AsyncProvider<PeriodSetupPresenter> getPeriodSetupPresenter();

  AsyncProvider<CoaSetupPresenter> getCoaSetupPresenter();
  
  AsyncProvider<MigsMasterPresenter> getMigsMasterPresenter();
  
  AsyncProvider<FraudCustomerWhitelistPresenter> getFraudCustomerWhitelistPresenter();
  
  AsyncProvider<InventoryPresenter> getInventoryPresenter();
  
  AsyncProvider<ExportReportPresenter> getExportReportPresenter();
  
  AsyncProvider<FraudParameterRule31Presenter> getFraudParameterRule31Presenter();
}