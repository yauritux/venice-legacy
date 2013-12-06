package com.gdn.venice.client.app.finance.view;

import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.finance.presenter.ReportsLauncherPresenter;
import com.gdn.venice.client.app.finance.view.handlers.ReportsLauncherUiHandlers;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Launching Finance Reports
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ReportsLauncherView extends
		ViewWithUiHandlers<ReportsLauncherUiHandlers> implements
		ReportsLauncherPresenter.MyView {
	
	//The layout for the report launcher
	RafViewLayout reportsLauncherLayout;

	//The dialog for launching the subsidiary ledger report for merchants
	Window reportParameterWindow;
	
	//The dialog for launching the subsidiary ledger report for logostics providers
	Window subsidiaryLedgerReportLogisticsWindow;
	
	//Used to store the report selection
	int reportSelection;

	//A dynamic form used to submit the HTTP params
	DynamicForm form;
	
	//The button used to start the report launch dialog
	ToolStripButton launchDialogButton;
	
	//The radio button group used to seelct the report
	RadioGroupItem radioGroupItem;

	/**
	 * Basic constructor to build the view
	 */
	@Inject
	public ReportsLauncherView() {

		reportsLauncherLayout = new RafViewLayout();

		ToolStrip reportsLauncherToolStrip = new ToolStrip();
		reportsLauncherToolStrip.setWidth100();
		reportsLauncherToolStrip.setPadding(2);

		launchDialogButton = new ToolStripButton();
		launchDialogButton.setIcon("[SKIN]/icons/up.png");
		launchDialogButton.setTooltip("Launch Report");

		reportsLauncherToolStrip.addButton(launchDialogButton);
		reportsLauncherToolStrip.addSeparator();
		
	    radioGroupItem = new RadioGroupItem();
	    radioGroupItem.setTitle("Ledger Reports");
	    radioGroupItem.setValueMap("Merchant Subsidiary Ledger", "Logistics Subsidiary Ledger", "Funds In Reconcilement Report", "Sales Report","Refund Report");
	    radioGroupItem.setDefaultValue(true);
	    

		form = new DynamicForm() {};

		form.setFields(radioGroupItem);
		
		reportsLauncherLayout.setMembers(reportsLauncherToolStrip, form);

		bindCustomUiHandlers();
	}

	/**
	 * Builds the report parameter window (modal dialog style) based on the selection of the radio buttons.
	 * @param reportId is an integer signifyng the report to launch based on the raido button selection
	 * @return the new window
	 */
	private Window buildReportParameterWindow(final int reportId) {
		reportParameterWindow = new Window();
		reportParameterWindow.setWidth(360);
		reportParameterWindow.setHeight(140);
		switch (reportId) {
		case (0): {
			reportParameterWindow.setTitle("Merchant Subsidiary Ledger Report");
			break;
		}
		case (1): {
			reportParameterWindow
					.setTitle("Logistics Subsidiary Ledger Report");
			break;
		}
		case (2): {
			reportParameterWindow.setTitle("Funds In Reconciliation Report");
			break;
		}
		case (3): {
			reportParameterWindow.setTitle("Sales Report");
			break;
		}
		case (4): {
			reportParameterWindow.setTitle("Refund Report");
			break;
		}
		}
		reportParameterWindow.setShowMinimizeButton(false);
		reportParameterWindow.setIsModal(true);
		reportParameterWindow.setShowModalMask(true);
		reportParameterWindow.centerInPage();
		reportParameterWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				reportParameterWindow.destroy();
			}
		});

		VLayout reportParametersLayout = new VLayout();
		reportParametersLayout.setHeight100();
		reportParametersLayout.setWidth100();

		final DynamicForm reportParametersForm = new DynamicForm();
		reportParametersForm.setPadding(5);
		reportParametersForm.setEncoding(Encoding.MULTIPART);
		reportParametersForm.setTarget("upload_frame");
		
		final DateItem fromDateItem = new DateItem();
	    fromDateItem.setTitle("From Date");
	    fromDateItem.setHint("<nobr>Report from date</nobr>");

		final DateItem toDateItem = new DateItem();
	    toDateItem.setTitle("To Date");
	    toDateItem.setHint("<nobr>Report to date</nobr>");
	    
	    reportParametersForm.setFields(fromDateItem, toDateItem);
	    
		HLayout dialogButtons = new HLayout(5);

		IButton buttonLaunch = new IButton("Launch Report");
		IButton buttonCancel = new IButton("Cancel");

		buttonLaunch.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				String host = GWT.getHostPageBaseURL();

				//If in debug mode then change the host URL to the servlet in the server side
				if(host.contains("8889")){
					host = "http://localhost:8090/";
				}
				
				/* 
				 * Somehow when the app is deployed in Geronimo the getHostPageBaseURL call
				 * adds the context root of "Venice/" as it is the web application.
				 * This does not happen in development mode as it is running in the root
				 * of the Jetty servlet container.
				 * 
				 * Consequently the context root needs to be removed because the servlet 
				 * being called has its own context root in a different web application.
				 */
				if(host.contains("Venice/")){
					host = host.substring(0, host.indexOf("Venice/"));
				}
												
				//Comment in to see the values of the various properties of the date fields
//				System.out.println("fromDateItem.getValueAsDate()" + fromDateItem.getValueAsDate());
//				System.out.println("fromDateItem.getDisplayField" + fromDateItem.getDisplayField());
//				System.out.println("fromDateItem.getDisplayValue" + fromDateItem.getDisplayValue());
//				System.out.println("fromDateItem.getValueField" + fromDateItem.getValueField());
//				System.out.println("fromDateItem.getValue" + fromDateItem.getValue());
				
				/*
				 * Note that the servlet had to be setup to handle various
				 * aspects of timezone formats (GMT, WIT, WIB, WITA etc.)
				 */
				String sFromDate = fromDateItem.getValueAsDate().toString();
				String sToDate = toDateItem.getValueAsDate().toString();
				
				/*
				 * Call the relevent report servlet based ont he value of reportId
				 * which has been selected in the parent window (radio buttons)
				 */
				switch(reportId){
				case(DataConstantNameTokens.VEN_REPORT_SELECTION_SUBSIDIARY_LEDGER_MERCHANT):{
					reportParametersForm.setAction(host + "Venice/DateBasedReportLauncherServlet?fromDate=" + sFromDate + "&toDate=" + sToDate + "&reportFileName=" + "SubsidiaryLedgerReportMerchants");
					break;
				}
				case(DataConstantNameTokens.VEN_REPORT_SELECTION_SUBSIDIARY_LEDGER_LOGISTICS):{
					reportParametersForm.setAction(host + "Venice/DateBasedReportLauncherServlet?fromDate=" + sFromDate + "&toDate=" + sToDate + "&reportFileName=" + "SubsidiaryLedgerReportLogistics");
					break;
				}
				case(DataConstantNameTokens.VEN_REPORT_SELECTION_FUNDS_IN_RECONCILEMENT):{
					reportParametersForm.setAction(host + "Venice/DateBasedReportLauncherServlet?fromDate=" + sFromDate + "&toDate=" + sToDate + "&reportFileName=" + "FundsInReconciliationReport");
					break;
				}
				case(DataConstantNameTokens.VEN_REPORT_SELECTION_SALES):{
					reportParametersForm.setAction(host + "Venice/DateBasedReportLauncherServlet?fromDate=" + sFromDate + "&toDate=" + sToDate + "&reportFileName=" + "SalesReport");
					break;
				}
				case(DataConstantNameTokens.VEN_REPORT_SELECTION_REFUND):{
					reportParametersForm.setAction(host + "Venice/DateBasedReportLauncherServlet?fromDate=" + sFromDate + "&toDate=" + sToDate + "&reportFileName=" + "RefundReport");
					break;
				}
				}
				
				System.out.println("Form Action:" + reportParametersForm.getAction());
				reportParametersForm.submitForm();
				
				//Comment in to check the action on the form
				//SC.say("Request submitted:" + reportParametersForm.getAction());

				reportParameterWindow.destroy();
			}		
		});
		
		/*
		 * Set the to date to default to the from date if it has not been
		 * selected this is to handle single date reports.
		 */
		fromDateItem.addBlurHandler(new BlurHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.form.fields.events.BlurHandler#onBlur(com.smartgwt.client.widgets.form.fields.events.BlurEvent)
			 */
			public void onBlur(BlurEvent event) {
				if (toDateItem.getValue() == null
						|| toDateItem.getValue().equals("")) {
					toDateItem.setValue(fromDateItem.getValueAsDate());
				}
			}
		});

		/*
		 * The click handler for the cancel button just destroys the window
		 */
		buttonCancel.addClickHandler(new ClickHandler() {

			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				reportParameterWindow.destroy();
			}
		});
		
		//Align and add the members to the strip and the layout
		dialogButtons.setAlign(Alignment.CENTER);
		dialogButtons.setMembers(buttonLaunch, buttonCancel);
		reportParametersLayout.setMembers(reportParametersForm, dialogButtons);
		reportParameterWindow.addItem(reportParametersLayout);
		return reportParameterWindow;
	}


	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.View#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return reportsLauncherLayout;
	}

	/**
	 * Binds the custom UI handlers to the components
	 */
	protected void bindCustomUiHandlers() {
		
		/*
		 * The blur hanlder is used to pick up the radio button selection
		 */
		radioGroupItem.addBlurHandler(new BlurHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.form.fields.events.BlurHandler#onBlur(com.smartgwt.client.widgets.form.fields.events.BlurEvent)
			 */
			public void onBlur(BlurEvent event) {
				if (radioGroupItem.getValueAsString().equals(
						"Merchant Subsidiary Ledger")) {
					reportSelection = DataConstantNameTokens.VEN_REPORT_SELECTION_SUBSIDIARY_LEDGER_MERCHANT;
				} else if (radioGroupItem.getValueAsString().equals(
						"Logistics Subsidiary Ledger")) {
					reportSelection = DataConstantNameTokens.VEN_REPORT_SELECTION_SUBSIDIARY_LEDGER_LOGISTICS;
				} else if (radioGroupItem.getValueAsString().equals(
						"Funds In Reconcilement Report")) {
					reportSelection = DataConstantNameTokens.VEN_REPORT_SELECTION_FUNDS_IN_RECONCILEMENT;
				} else if (radioGroupItem.getValueAsString().equals(
						"Sales Report")) {
					reportSelection = DataConstantNameTokens.VEN_REPORT_SELECTION_SALES;
				} else if (radioGroupItem.getValueAsString().equals(
						"Refund Report")) {
					reportSelection = DataConstantNameTokens.VEN_REPORT_SELECTION_REFUND;
				}
			}
		});
		
		/*
		 * The click hanlder is used to launch the report parameter dialog
		 */
		launchDialogButton.addClickHandler(new ClickHandler() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.events.ClickHandler#onClick(com.smartgwt.client.widgets.events.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
					buildReportParameterWindow(reportSelection).show();
			}
		});
	}
}
