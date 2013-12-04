package com.gdn.venice.client.app.logistic.view;

import java.util.Date;

import com.gdn.venice.client.app.logistic.presenter.ExportReportPresenter;
import com.gdn.venice.client.app.logistic.view.handlers.ExportReportUiHandlers;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * view for export report CX, CX finance, D
 * 
 * @author Roland
 */

public class ExportReportView extends
ViewWithUiHandlers<ExportReportUiHandlers> implements
ExportReportPresenter.MyView {
	RafViewLayout exportReportLayout;
	DateItem dateFromItem = new DateItem();
	DateItem dateToItem = new DateItem();
	
	@Inject
	public ExportReportView() {
		exportReportLayout = new RafViewLayout();
		
		//Toolstrip atas
		ToolStrip exportReportToolStrip = new ToolStrip();
		exportReportToolStrip.setWidth100();
		
		//Form
		final DynamicForm form = new DynamicForm();
		form.setTitleWidth(110);
		form.setMargin(5);
		form.setWidth(275);
		
		dateFromItem.setTitle("Date From");
		dateFromItem.setDefaultValue(new Date());
		dateFromItem.setTitleAlign(Alignment.LEFT);
		dateFromItem.setAlign(Alignment.RIGHT);
		dateFromItem.setVisible(true);
		
		dateToItem.setTitle("Date To");
		dateToItem.setDefaultValue(new Date());
		dateToItem.setTitleAlign(Alignment.LEFT);
		dateToItem.setAlign(Alignment.RIGHT);
		dateToItem.setVisible(true);
				
		final ButtonItem buttonCXItem = new ButtonItem(); 
		buttonCXItem.setWidth(170); 
		buttonCXItem.setHeight(50);
		buttonCXItem.setTitle("Export Report CX");  
		buttonCXItem.setColSpan(2);
		buttonCXItem.setRowSpan(10);
		buttonCXItem.setAlign(Alignment.RIGHT);
		buttonCXItem.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {        		      
            	Date dateFrom = dateFromItem.getValueAsDate();
            	Date dateTo = dateToItem.getValueAsDate();
            	DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
            	final String dateParamFrom = dateFormat.format(dateFrom);
            	final String dateParamTo= dateFormat.format(dateTo);
            	
    			//If in debug mode then change the host URL to the servlet in the server side
    			String host = GWT.getHostPageBaseURL();
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
    					
    			form.setAction(host + "Venice/CXCXFinDReportExportServlet?type=reportCX&dateFrom=" + dateParamFrom+"&dateTo="+dateParamTo);
    			form.submitForm();    
            }  
        });
		
		final ButtonItem buttonCXFinanceItem = new ButtonItem(); 
		buttonCXFinanceItem.setWidth(170); 
		buttonCXFinanceItem.setHeight(50);
		buttonCXFinanceItem.setTitle("Export Report CX Finance");  
		buttonCXFinanceItem.setColSpan(2);
		buttonCXFinanceItem.setRowSpan(10);
		buttonCXFinanceItem.setAlign(Alignment.RIGHT);
		buttonCXFinanceItem.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {            	      
            	Date dateFrom = dateFromItem.getValueAsDate();
            	Date dateTo = dateToItem.getValueAsDate();
            	DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
            	final String dateParamFrom = dateFormat.format(dateFrom);
            	final String dateParamTo= dateFormat.format(dateTo);
            	
    			//If in debug mode then change the host URL to the servlet in the server side
    			String host = GWT.getHostPageBaseURL();
    			if(host.contains("8889")){
    				host = "http://localhost:8090/";
    			}

    			if(host.contains("Venice/")){
    				host = host.substring(0, host.indexOf("Venice/"));
    			}
    					
    			form.setAction(host + "Venice/CXCXFinDReportExportServlet?type=reportCXFinance&dateFrom=" + dateParamFrom+"&dateTo="+dateParamTo);
    			form.submitForm();  
            }  
        });
		
		final ButtonItem buttonDItem = new ButtonItem(); 
		buttonDItem.setWidth(170); 
		buttonDItem.setHeight(50);
		buttonDItem.setTitle("Export Report D");  
		buttonDItem.setColSpan(2);
		buttonDItem.setRowSpan(10);
		buttonDItem.setAlign(Alignment.RIGHT);
		buttonDItem.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {        		  
            	Date dateFrom = dateFromItem.getValueAsDate();
            	Date dateTo = dateToItem.getValueAsDate();
            	DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
            	final String dateParamFrom = dateFormat.format(dateFrom);
            	final String dateParamTo= dateFormat.format(dateTo);
            	
    			//If in debug mode then change the host URL to the servlet in the server side
    			String host = GWT.getHostPageBaseURL();
    			if(host.contains("8889")){
    				host = "http://localhost:8090/";
    			}
    			
    			if(host.contains("Venice/")){
    				host = host.substring(0, host.indexOf("Venice/"));
    			}
    					
    			form.setAction(host + "Venice/CXCXFinDReportExportServlet?type=reportD&dateFrom=" + dateParamFrom+"&dateTo="+dateParamTo);
    			form.submitForm();            	
            }  
        });
        
		form.setFields(dateFromItem, dateToItem, buttonCXItem, buttonCXFinanceItem, buttonDItem);		
		exportReportLayout.setMembers(exportReportToolStrip, form);		
		bindCustomUiHandlers();
	}

	@Override
	public Widget asWidget() {
		return exportReportLayout;
	}

	protected void bindCustomUiHandlers() {

	}
}