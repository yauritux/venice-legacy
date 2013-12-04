package com.gdn.venice.client.app;

import com.gdn.venice.client.app.administration.presenter.ModuleConfigurationPresenter;
import com.gdn.venice.client.app.administration.presenter.RoleProfileUserGroupManagementPresenter;
import com.gdn.venice.client.app.administration.view.ModuleConfigurationView;
import com.gdn.venice.client.app.administration.view.RoleProfileUserGroupManagementView;
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
import com.gdn.venice.client.app.finance.view.CoaSetupView;
import com.gdn.venice.client.app.finance.view.ExportView;
import com.gdn.venice.client.app.finance.view.FinanceDashboardView;
import com.gdn.venice.client.app.finance.view.FundInReconciliationView;
import com.gdn.venice.client.app.finance.view.JournalView;
import com.gdn.venice.client.app.finance.view.ManualJournalView;
import com.gdn.venice.client.app.finance.view.PaymentProcessingView;
import com.gdn.venice.client.app.finance.view.PeriodSetupView;
import com.gdn.venice.client.app.finance.view.PromotionView;
import com.gdn.venice.client.app.finance.view.ReportsLauncherView;
import com.gdn.venice.client.app.finance.view.SalesRecordView;
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
import com.gdn.venice.client.app.fraud.view.BlackListMaintenanceView;
import com.gdn.venice.client.app.fraud.view.CustomerBlackListMaintenanceView;
import com.gdn.venice.client.app.fraud.view.FraudBinCreditLimitView;
import com.gdn.venice.client.app.fraud.view.FraudCalculateView;
import com.gdn.venice.client.app.fraud.view.FraudCaseManagementView;
import com.gdn.venice.client.app.fraud.view.FraudCaseViewerView;
import com.gdn.venice.client.app.fraud.view.FraudCustomerWhitelistView;
import com.gdn.venice.client.app.fraud.view.FraudDashboardView;
import com.gdn.venice.client.app.fraud.view.FraudParameterRule31View;
import com.gdn.venice.client.app.fraud.view.MigsMasterView;
import com.gdn.venice.client.app.fraud.view.MigsUploadView;
import com.gdn.venice.client.app.fraud.view.UncalculatedCreditCardOrderView;
import com.gdn.venice.client.app.general.presenter.OrderDataViewerPresenter;
import com.gdn.venice.client.app.general.presenter.PartyMaintenancePresenter;
import com.gdn.venice.client.app.general.presenter.ReturDataViewerPresenter;
import com.gdn.venice.client.app.general.view.OrderDataViewerView;
import com.gdn.venice.client.app.general.view.PartyMaintenanceView;
import com.gdn.venice.client.app.general.view.ReturDataViewerView;
import com.gdn.venice.client.app.kpi.presenter.KpiDashboardPresenter;
import com.gdn.venice.client.app.kpi.presenter.KpiDetailViewerPresenter;
import com.gdn.venice.client.app.kpi.presenter.KpiSetupPresenter;
import com.gdn.venice.client.app.kpi.view.KpiDashboardView;
import com.gdn.venice.client.app.kpi.view.KpiDetailViewerView;
import com.gdn.venice.client.app.kpi.view.KpiSetupView;
import com.gdn.venice.client.app.logistic.presenter.ActivityReportReconciliationPresenter;
import com.gdn.venice.client.app.logistic.presenter.DeliveryStatusTrackingPresenter;
import com.gdn.venice.client.app.logistic.presenter.ExportReportPresenter;
import com.gdn.venice.client.app.logistic.presenter.InventoryPresenter;
import com.gdn.venice.client.app.logistic.presenter.InvoiceReconciliationPresenter;
import com.gdn.venice.client.app.logistic.presenter.LogisticsDashboardPresenter;
import com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter;
import com.gdn.venice.client.app.logistic.view.ActivityReportReconciliationView;
import com.gdn.venice.client.app.logistic.view.DeliveryStatusTrackingView;
import com.gdn.venice.client.app.logistic.view.ExportReportView;
import com.gdn.venice.client.app.logistic.view.InventoryView;
import com.gdn.venice.client.app.logistic.view.InvoiceReconciliationView;
import com.gdn.venice.client.app.logistic.view.LogisticsDashboardView;
import com.gdn.venice.client.app.logistic.view.ProviderManagementView;
import com.gdn.venice.client.app.reservation.presenter.ReservationOrderManagementPresenter;
import com.gdn.venice.client.app.reservation.view.ReservationOrderManagementView;
import com.gdn.venice.client.app.task.presenter.AssignedTaskPresenter;
import com.gdn.venice.client.app.task.presenter.TaskLaunchpadPresenter;
import com.gdn.venice.client.app.task.presenter.TaskSummaryPresenter;
import com.gdn.venice.client.app.task.presenter.ToDoListPresenter;
import com.gdn.venice.client.app.task.view.AssignedTaskView;
import com.gdn.venice.client.app.task.view.TaskLaunchpadView;
import com.gdn.venice.client.app.task.view.TaskSummaryView;
import com.gdn.venice.client.app.task.view.ToDoListView;
import com.gdn.venice.client.app.testrestdatasource.presenter.TestRestDataSourcePresenter;
import com.gdn.venice.client.app.testrestdatasource.view.TestRestDataSourceView;
import com.gdn.venice.client.presenter.LoginPresenter;
import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.view.LoginView;
import com.gdn.venice.client.view.MainPageView;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.DefaultEventBus;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

/**
 * This class configures the binding of elements inside the application,
 * including the binding of presenters and views for each of the pages/screens
 * in the application
 * 
 * @author Henry Chandra
 */

// TODO: Whenever we add a new page/screen on the application, need to add a new
// entry in this class (bind the presenter and the view here)
public class VeniceClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(VenicePlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();
		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(Singleton.class);

		// Constants
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.taskSummaryPage);

		// Presenters
		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);

		bindPresenter(TestRestDataSourcePresenter.class,
				TestRestDataSourcePresenter.MyView.class,
				TestRestDataSourceView.class,
				TestRestDataSourcePresenter.MyProxy.class);

		bindPresenter(LoginPresenter.class, LoginPresenter.MyView.class,
				LoginView.class, LoginPresenter.MyProxy.class);

		bindPresenter(TaskSummaryPresenter.class,
				TaskSummaryPresenter.MyView.class, TaskSummaryView.class,
				TaskSummaryPresenter.MyProxy.class);

		bindPresenter(ToDoListPresenter.class, ToDoListPresenter.MyView.class,
				ToDoListView.class, ToDoListPresenter.MyProxy.class);

		bindPresenter(AssignedTaskPresenter.class,
				AssignedTaskPresenter.MyView.class, AssignedTaskView.class,
				AssignedTaskPresenter.MyProxy.class);

		bindPresenter(TaskLaunchpadPresenter.class,
				TaskLaunchpadPresenter.MyView.class, TaskLaunchpadView.class,
				TaskLaunchpadPresenter.MyProxy.class);

		bindPresenter(FraudDashboardPresenter.class,
				FraudDashboardPresenter.MyView.class, FraudDashboardView.class,
				FraudDashboardPresenter.MyProxy.class);

		bindPresenter(FraudCaseManagementPresenter.class,
				FraudCaseManagementPresenter.MyView.class,
				FraudCaseManagementView.class,
				FraudCaseManagementPresenter.MyProxy.class);

		bindPresenter(FraudCaseViewerPresenter.class,
				FraudCaseViewerPresenter.MyView.class,
				FraudCaseViewerView.class,
				FraudCaseViewerPresenter.MyProxy.class);

		bindPresenter(FraudBinCreditLimitPresenter.class,
				FraudBinCreditLimitPresenter.MyView.class,
				FraudBinCreditLimitView.class,
				FraudBinCreditLimitPresenter.MyProxy.class);
		
		bindPresenter(BlackListMaintenancePresenter.class,
				BlackListMaintenancePresenter.MyView.class,
				BlackListMaintenanceView.class,
				BlackListMaintenancePresenter.MyProxy.class);
		
		bindPresenter(CustomerBlackListMaintenancePresenter.class,
				CustomerBlackListMaintenancePresenter.MyView.class,
				CustomerBlackListMaintenanceView.class,
				CustomerBlackListMaintenancePresenter.MyProxy.class);
		
		bindPresenter(FraudCalculatePresenter.class,
				FraudCalculatePresenter.MyView.class, 
				FraudCalculateView.class,
				FraudCalculatePresenter.MyProxy.class);
		
		bindPresenter(MigsUploadPresenter.class,
				MigsUploadPresenter.MyView.class, 
				MigsUploadView.class,
				MigsUploadPresenter.MyProxy.class);
		
		bindPresenter(UncalculatedCreditCardOrderPresenter.class,
				UncalculatedCreditCardOrderPresenter.MyView.class, 
				UncalculatedCreditCardOrderView.class,
				UncalculatedCreditCardOrderPresenter.MyProxy.class);

		bindPresenter(ReservationOrderManagementPresenter.class,
				ReservationOrderManagementPresenter.MyView.class, 
				ReservationOrderManagementView.class,
				ReservationOrderManagementPresenter.MyProxy.class);

		bindPresenter(LogisticsDashboardPresenter.class,
				LogisticsDashboardPresenter.MyView.class,
				LogisticsDashboardView.class,
				LogisticsDashboardPresenter.MyProxy.class);

		bindPresenter(DeliveryStatusTrackingPresenter.class,
				DeliveryStatusTrackingPresenter.MyView.class,
				DeliveryStatusTrackingView.class,
				DeliveryStatusTrackingPresenter.MyProxy.class);

		bindPresenter(ActivityReportReconciliationPresenter.class,
				ActivityReportReconciliationPresenter.MyView.class,
				ActivityReportReconciliationView.class,
				ActivityReportReconciliationPresenter.MyProxy.class);

		bindPresenter(ProviderManagementPresenter.class,
				ProviderManagementPresenter.MyView.class,
				ProviderManagementView.class,
				ProviderManagementPresenter.MyProxy.class);

		bindPresenter(InvoiceReconciliationPresenter.class,
				InvoiceReconciliationPresenter.MyView.class,
				InvoiceReconciliationView.class,
				InvoiceReconciliationPresenter.MyProxy.class);

		bindPresenter(FinanceDashboardPresenter.class,
				FinanceDashboardPresenter.MyView.class,
				FinanceDashboardView.class,
				FinanceDashboardPresenter.MyProxy.class);

		bindPresenter(FundInReconciliationPresenter.class,
				FundInReconciliationPresenter.MyView.class,
				FundInReconciliationView.class,
				FundInReconciliationPresenter.MyProxy.class);

		bindPresenter(SalesRecordPresenter.class,
				SalesRecordPresenter.MyView.class, SalesRecordView.class,
				SalesRecordPresenter.MyProxy.class);

		bindPresenter(PaymentProcessingPresenter.class,
				PaymentProcessingPresenter.MyView.class,
				PaymentProcessingView.class,
				PaymentProcessingPresenter.MyProxy.class);

		bindPresenter(JournalPresenter.class, JournalPresenter.MyView.class,
				JournalView.class, JournalPresenter.MyProxy.class);

		bindPresenter(ManualJournalPresenter.class,
				ManualJournalPresenter.MyView.class, ManualJournalView.class,
				ManualJournalPresenter.MyProxy.class);

		bindPresenter(KpiSetupPresenter.class, KpiSetupPresenter.MyView.class,
				KpiSetupView.class, KpiSetupPresenter.MyProxy.class);

		bindPresenter(KpiDashboardPresenter.class,
				KpiDashboardPresenter.MyView.class, KpiDashboardView.class,
				KpiDashboardPresenter.MyProxy.class);

		bindPresenter(KpiDetailViewerPresenter.class,
				KpiDetailViewerPresenter.MyView.class,
				KpiDetailViewerView.class,
				KpiDetailViewerPresenter.MyProxy.class);

		bindPresenter(RoleProfileUserGroupManagementPresenter.class,
				RoleProfileUserGroupManagementPresenter.MyView.class,
				RoleProfileUserGroupManagementView.class,
				RoleProfileUserGroupManagementPresenter.MyProxy.class);

		bindPresenter(ModuleConfigurationPresenter.class,
				ModuleConfigurationPresenter.MyView.class,
				ModuleConfigurationView.class,
				ModuleConfigurationPresenter.MyProxy.class);

		bindPresenter(OrderDataViewerPresenter.class,
				OrderDataViewerPresenter.MyView.class,
				OrderDataViewerView.class,
				OrderDataViewerPresenter.MyProxy.class);
		
		bindPresenter(ReturDataViewerPresenter.class,
				ReturDataViewerPresenter.MyView.class,
				ReturDataViewerView.class,
				ReturDataViewerPresenter.MyProxy.class);
		
		bindPresenter(ReportsLauncherPresenter.class,
				ReportsLauncherPresenter.MyView.class,
				ReportsLauncherView.class,
				ReportsLauncherPresenter.MyProxy.class);

		bindPresenter(PromotionPresenter.class,
				PromotionPresenter.MyView.class,
				PromotionView.class,
				PromotionPresenter.MyProxy.class);
		
		bindPresenter(PartyMaintenancePresenter.class,
				PartyMaintenancePresenter.MyView.class,
				PartyMaintenanceView.class,
				PartyMaintenancePresenter.MyProxy.class);
		
		bindPresenter(ExportPresenter.class,
				ExportPresenter.MyView.class,
				ExportView.class,
				ExportPresenter.MyProxy.class);
		
		bindPresenter(PeriodSetupPresenter.class,
				PeriodSetupPresenter.MyView.class,
				PeriodSetupView.class,
				PeriodSetupPresenter.MyProxy.class);
		
		bindPresenter(CoaSetupPresenter.class,
				CoaSetupPresenter.MyView.class,
				CoaSetupView.class,
				CoaSetupPresenter.MyProxy.class);
		
		bindPresenter(MigsMasterPresenter.class,
				MigsMasterPresenter.MyView.class,
				MigsMasterView.class,
				MigsMasterPresenter.MyProxy.class);
		
		bindPresenter(FraudCustomerWhitelistPresenter.class,
				FraudCustomerWhitelistPresenter.MyView.class,
				FraudCustomerWhitelistView.class,
				FraudCustomerWhitelistPresenter.MyProxy.class);
		
		bindPresenter(InventoryPresenter.class, 
				InventoryPresenter.MyView.class,
				InventoryView.class,
				InventoryPresenter.MyProxy.class);
		
		bindPresenter(ExportReportPresenter.class, 
				ExportReportPresenter.MyView.class,
				ExportReportView.class,
				ExportReportPresenter.MyProxy.class);	
		
		bindPresenter(FraudParameterRule31Presenter.class, 
				FraudParameterRule31Presenter.MyView.class,
				FraudParameterRule31View.class,
				FraudParameterRule31Presenter.MyProxy.class);	
	}
}