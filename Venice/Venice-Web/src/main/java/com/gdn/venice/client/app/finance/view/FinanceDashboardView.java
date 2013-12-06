package com.gdn.venice.client.app.finance.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.finance.data.FinDasboardData;
import com.gdn.venice.client.app.finance.presenter.FinanceDashboardPresenter;
import com.gdn.venice.client.app.finance.view.handlers.FinanceDashboardUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.ExternalDashboardPortlet;
import com.gdn.venice.client.widgets.ListGridDashboardPortlet;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.Criterion;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Hilite;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.PortalLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Finance Dashboard
 * 
 * @author Henry Chandra
 */
public class FinanceDashboardView extends
ViewWithUiHandlers<FinanceDashboardUiHandlers> implements
FinanceDashboardPresenter.MyView {
	private static final int TITLE_HEIGHT = 20;  	
	ExternalDashboardPortlet statusHistoryPortlet ;
	ExternalDashboardPortlet revenueHistoryPortlet ;
	PortalLayout financeDashboardPortalLayout;
	
	RafViewLayout financeDashboardLayout;

	@Inject
	public FinanceDashboardView() {		
		financeDashboardLayout = new RafViewLayout();
}
	
public void onLoadFinanceDashboard(Map<String,String> map){
		financeDashboardPortalLayout = new PortalLayout();
		financeDashboardPortalLayout.setShowColumnMenus(false);
		financeDashboardPortalLayout.setWidth100();
		financeDashboardPortalLayout.setHeight100();								
		ShowFussionChartRevenueStatus(map,null,null,true);
		ShowFussionChartRevenueHistory(null,null,true,"0");
		ShowUnreconciledReceivables(null);
		ShowPayablesDue(null);
			
		financeDashboardLayout.setMembers(financeDashboardPortalLayout);
		bindCustomUiHandlers();
	}
	@Override
	public Widget asWidget() {
		return financeDashboardLayout;
	}
	protected void bindCustomUiHandlers() {	}
	
	public void ShowFussionChartRevenueStatus(final Map mapPeriod,Map mapFussion,String idPeriod,boolean action){
		if(statusHistoryPortlet!=null&&action){
			statusHistoryPortlet.destroy();
		}
		if(idPeriod!=null&&action==false){
			getUiHandlers().onFetchFussionChartRevenueStatus(mapPeriod,idPeriod);
		}else if(idPeriod!=null&&action==true){
			statusHistoryPortlet = new ExternalDashboardPortlet("Revenue Status", GWT.getHostPageBaseURL() + "FusionChartsFree/NewFinanceDashboardGauges.html?"+mapFussion+"&"+mapPeriod.get(idPeriod));
		}else{
			statusHistoryPortlet = new ExternalDashboardPortlet("Revenue Status", GWT.getHostPageBaseURL() + "FusionChartsFree/NewFinanceDashboardGauges.html?&");
		}
		if(action){
				ToolStrip statusStatusToolStrip = new ToolStrip();
				statusStatusToolStrip.setWidth100();		
				final SelectItem statusSelectItem = new SelectItem();
				LinkedHashMap<String, String> valueMapPeriod = new LinkedHashMap<String, String>(); 
				valueMapPeriod.putAll(mapPeriod);
				statusSelectItem.setTitle("Period:&nbsp;This");	
				statusSelectItem.setValueMap(valueMapPeriod);		
				statusSelectItem.setDefaultValue(idPeriod);
				statusSelectItem.addChangedHandler(new ChangedHandler(){
					@Override
					public void onChanged(ChangedEvent event) {
						ShowFussionChartRevenueStatus(mapPeriod,null,statusSelectItem.getValueAsString(),false);
					}			
				});		
				statusStatusToolStrip.addFormItem(statusSelectItem);		
				statusHistoryPortlet.getPortletLayout().addMember(statusStatusToolStrip, 0);	
				financeDashboardPortalLayout.addPortlet(statusHistoryPortlet,0,0);
		}
	}
	public void ShowFussionChartRevenueHistory(Map mapFussion,final String idMap,boolean action,String nerxOrBack){
		final String countNerxOrBack=nerxOrBack;
		if(revenueHistoryPortlet!=null&&action){
			revenueHistoryPortlet.destroy();
		}
		if(idMap!=null&&!action){
			getUiHandlers().onFetchFussionChartRevenueHistory(idMap,countNerxOrBack);
		}else if(idMap!=null&&action){
			revenueHistoryPortlet = new ExternalDashboardPortlet("Revenue History", GWT.getHostPageBaseURL() + "FusionChartsFree/NewFinanceDashboardRevenueLine.html?"+mapFussion);
		}else{
			revenueHistoryPortlet = new ExternalDashboardPortlet("Revenue History", GWT.getHostPageBaseURL() + "FusionChartsFree/NewFinanceDashboardRevenueLine.html?&");
		}
		if(action){
		ToolStrip revenueHistoryToolStrip = new ToolStrip();
		revenueHistoryToolStrip.setWidth100();		
			
		final SelectItem timeSelectItem = new SelectItem();
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>(); 
		valueMap.put("1", "day");
		valueMap.put("2", "week");
		valueMap.put("3", "month");
		valueMap.put("4", "3 months");
		valueMap.put("5", "6 months");
		timeSelectItem.setValueMap(valueMap);
		timeSelectItem.setDefaultValue(idMap);
		timeSelectItem.setTitle("Time:&nbsp;This");
		
		timeSelectItem.addChangedHandler(new ChangedHandler(){
			@Override
			public void onChanged(ChangedEvent event) {
				ShowFussionChartRevenueHistory(null,timeSelectItem.getValueAsString(),false,"0");
			}
			
		});
				
		ToolStripButton btnBackward = new ToolStripButton();
		btnBackward.setIcon("[SKIN]/icons/backward.png");
		btnBackward.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				int count=new Integer(countNerxOrBack);
				count--;
				getUiHandlers().onFetchFussionChartRevenueHistory(timeSelectItem.getValueAsString(),""+count);				
			}			
		});
		
		ToolStripButton btnForward = new ToolStripButton();
		btnForward.setIcon("[SKIN]/icons/forward.png");
		btnForward.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				int count=new Integer(countNerxOrBack);
				count++;
				getUiHandlers().onFetchFussionChartRevenueHistory(timeSelectItem.getValueAsString(),""+count);				
			}
			
		});
		
		
		ToolStripButton btnFirst = new ToolStripButton();
		btnFirst.setIcon("[SKIN]/icons/first.png");
		btnFirst.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				int count=new Integer(countNerxOrBack);
				count-=4;
					getUiHandlers().onFetchFussionChartRevenueHistory(timeSelectItem.getValueAsString(),""+count);				
			}
			
		});		
		
		ToolStripButton btnLast = new ToolStripButton();
		btnLast.setIcon("[SKIN]/icons/last.png");
		btnLast.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
					getUiHandlers().onFetchFussionChartRevenueHistory(timeSelectItem.getValueAsString(),"0");				
			}
			
		});

		
		revenueHistoryToolStrip.addFormItem(timeSelectItem);
		revenueHistoryToolStrip.addSeparator();
		revenueHistoryToolStrip.addButton(btnFirst);
		revenueHistoryToolStrip.addButton(btnBackward);
		revenueHistoryToolStrip.addButton(btnForward);
		revenueHistoryToolStrip.addButton(btnLast);		
		
		revenueHistoryPortlet.getPortletLayout().addMember(revenueHistoryToolStrip, 0);
		financeDashboardPortalLayout.addPortlet(revenueHistoryPortlet,0,1);
		}

	}
	
	public void ShowPayablesDue(String idPeriod){
		DataSource dataSource = FinDasboardData.getPayablesDue();

		final ListGridField[] payablesDueFields = Util.getListGridFieldsFromDataSource(dataSource);		
		Hilite[] payablesDueHilites = new Hilite[] { new Hilite() {
			{
				setFieldNames(""+payablesDueFields[0].getName(),""+payablesDueFields[1].getName(),""+payablesDueFields[2].getName());
				setCriteria(new Criterion(""+payablesDueFields[2].getName(), OperatorId.EQUALS,
				"Merchant"));
				setCssText("background-color:#adadad;");
				setId("4");
			}
		}, new Hilite() {
			{
				setCriteria(new Criterion(""+payablesDueFields[2].getName(), OperatorId.EQUALS,
				"Logistics Partner"));
				setCssText("background-color:#d4d4d4;");
				setId("5");
			}
		}, new Hilite() {
			{
				setFieldNames(""+payablesDueFields[0].getName(),""+payablesDueFields[1].getName(),""+payablesDueFields[2].getName());
				setCriteria(new Criterion(""+payablesDueFields[2].getName(), OperatorId.EQUALS,
				"refund"));
				setCssText("background-color:#ffffff;");
				setId("6");
			}
		}};		
		Util.formatListGridFieldAsCurrency(payablesDueFields[1]);
		financeDashboardPortalLayout.addPortlet(new ListGridDashboardPortlet("Payables Due", payablesDueFields, payablesDueHilites, dataSource),1,0);

	}
	
	private void ShowUnreconciledReceivables(String idPeriod) {
		DataSource dataSource = FinDasboardData.getUnreconciledReceivable();
		
		final ListGridField[] unReconciledItemFields = Util.getListGridFieldsFromDataSource(dataSource);

		Hilite[] unReconciledItemHilites = new Hilite[] { new Hilite() {
			{
				setFieldNames(unReconciledItemFields[0].getName(),unReconciledItemFields[1].getName(),unReconciledItemFields[2].getName(),unReconciledItemFields[3].getName(),unReconciledItemFields[4].getName());
				setCriteria(new Criterion(unReconciledItemFields[4].getName(), OperatorId.EQUALS,
				"Payment Timeout"));
				setCssText("background-color:#00FF00;");
				setId("0");
			}
		}, new Hilite() {
			{
				setFieldNames(unReconciledItemFields[0].getName(),unReconciledItemFields[1].getName(),unReconciledItemFields[2].getName(),unReconciledItemFields[3].getName(),unReconciledItemFields[4].getName());
				setCriteria(new Criterion(unReconciledItemFields[4].getName(), OperatorId.EQUALS, "No Payment"));
				setCssText("color:#FFFFFF;background-color:#FF0000;");
				setId("1");
			}
		}, new Hilite() {
			{
				setFieldNames(unReconciledItemFields[0].getName(),unReconciledItemFields[1].getName(),unReconciledItemFields[2].getName(),unReconciledItemFields[3].getName(),unReconciledItemFields[4].getName());
				setCriteria(new Criterion(unReconciledItemFields[4].getName(), OperatorId.EQUALS,
				"Partial Funds Received"));
				setCssText("background-color:#ece355;");
				setId("2");
			}
		}, new Hilite() {
			{
				setFieldNames(unReconciledItemFields[0].getName(),unReconciledItemFields[1].getName(),unReconciledItemFields[2].getName(),unReconciledItemFields[3].getName(),unReconciledItemFields[4].getName());
				setCriteria(new Criterion(unReconciledItemFields[4].getName(), OperatorId.EQUALS,
				"Overpaid Funds Received"));
				setCssText("color:#FFFFFF;background-color:#0000FF;");
				setId("3");
			}
		} };
			
		
		Util.formatListGridFieldAsCurrency(unReconciledItemFields[1]);
		Util.formatListGridFieldAsCurrency(unReconciledItemFields[2]);
		Util.formatListGridFieldAsCurrency(unReconciledItemFields[3]);		
	
		financeDashboardPortalLayout.addPortlet(new ListGridDashboardPortlet("Unreconciled Receivables", unReconciledItemFields, unReconciledItemHilites, dataSource),1,1);

		
	}
}
