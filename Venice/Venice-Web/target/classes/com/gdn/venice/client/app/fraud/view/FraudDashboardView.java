package com.gdn.venice.client.app.fraud.view;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.app.fraud.presenter.FraudDashboardPresenter;
import com.gdn.venice.client.app.fraud.view.handlers.FraudDashboardUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.ExternalDashboardPortlet;
import com.gdn.venice.client.widgets.ListGridDashboardPortlet;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DataSource;
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
 * View for Fraud Dashboard
 * 
 * @author Henry Chandra
 */
public class FraudDashboardView extends
ViewWithUiHandlers<FraudDashboardUiHandlers> implements
FraudDashboardPresenter.MyView {
	private static final int TITLE_HEIGHT = 20;  	

	PortalLayout fraudDashboardPortalLayout;
	ExternalDashboardPortlet fraudProcessingHistoryPorlet ;
	RafViewLayout fraudDashboardLayout;

	@Inject
	public FraudDashboardView() {
		
		fraudDashboardLayout = new RafViewLayout();
		
		fraudDashboardPortalLayout = new PortalLayout();
		fraudDashboardPortalLayout.setShowColumnMenus(false);
		fraudDashboardPortalLayout.setWidth100();
		fraudDashboardPortalLayout.setHeight100();
		
		DataSource dataSource = FraudData.getFraudCaseData();
		ListGridField[]  fraudFields = Util.getListGridFieldsFromDataSource(dataSource);	
		
		fraudFields[0].setHidden(true);
		fraudFields[0].setCanEdit(false);
		fraudFields[5].setHidden(true);
		fraudFields[5].setCanEdit(false);
		fraudFields[6].setHidden(true);
		fraudFields[6].setCanEdit(false);

		fraudDashboardPortalLayout.addPortlet(new ListGridDashboardPortlet("Open Fraud Case", fraudFields, null, FraudData.getFraudCaseData()),0,0);		
		ShowFussionProcessingHistory(null,null,true,"0");
		bindCustomUiHandlers();
	}
	
	@Override
	public Widget asWidget() {
		return fraudDashboardLayout;
	}

	protected void bindCustomUiHandlers() {

	}
	public void ShowFussionProcessingHistory(Map mapFussion,String idMap,boolean action,String nerxOrBack){
		final String countNerxOrBack=nerxOrBack;
		if(fraudProcessingHistoryPorlet!=null&&action){
			fraudProcessingHistoryPorlet.destroy();
		}
		
		if(idMap!=null&&!action){		
			getUiHandlers().onFectFraudProcessingHistory(idMap,countNerxOrBack) ;
		}else if(idMap!=null&&action){
			fraudProcessingHistoryPorlet = new ExternalDashboardPortlet("Fraud Processing History", GWT.getHostPageBaseURL() + "FusionChartsFree/fraudDashboardFraudCaseLine.html?"+mapFussion);
		}else{
			fraudProcessingHistoryPorlet = new ExternalDashboardPortlet("Fraud Processing History", GWT.getHostPageBaseURL() + "FusionChartsFree/fraudDashboardFraudCaseLine.html?&");
		}
		if(action){
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
				ShowFussionProcessingHistory(null,timeSelectItem.getValueAsString(),false,"0");
			}
			
		});
				
		ToolStripButton btnBackward = new ToolStripButton();
		btnBackward.setIcon("[SKIN]/icons/backward.png");
		btnBackward.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				int count=new Integer(countNerxOrBack);
				count--;
				getUiHandlers().onFectFraudProcessingHistory(timeSelectItem.getValueAsString(),""+count);				
			}			
		});
		
		ToolStripButton btnForward = new ToolStripButton();
		btnForward.setIcon("[SKIN]/icons/forward.png");
		btnForward.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				int count=new Integer(countNerxOrBack);
				count++;
				getUiHandlers().onFectFraudProcessingHistory(timeSelectItem.getValueAsString(),""+count);				
			}
			
		});
		
		
		ToolStripButton btnFirst = new ToolStripButton();
		btnFirst.setIcon("[SKIN]/icons/first.png");
		btnFirst.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
				int count=new Integer(countNerxOrBack);
				count-=4;
					getUiHandlers().onFectFraudProcessingHistory(timeSelectItem.getValueAsString(),""+count);				
			}
			
		});		
		
		ToolStripButton btnLast = new ToolStripButton();
		btnLast.setIcon("[SKIN]/icons/last.png");
		btnLast.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {				
					getUiHandlers().onFectFraudProcessingHistory(timeSelectItem.getValueAsString(),"0");				
			}
			
		});

		ToolStrip fraudProcessingHistoryToolStrip = new ToolStrip();		
		fraudProcessingHistoryToolStrip.setWidth100();

		fraudProcessingHistoryToolStrip.addFormItem(timeSelectItem);
		fraudProcessingHistoryToolStrip.addSeparator();
		fraudProcessingHistoryToolStrip.addButton(btnFirst);
		fraudProcessingHistoryToolStrip.addButton(btnBackward);
		fraudProcessingHistoryToolStrip.addButton(btnForward);
		fraudProcessingHistoryToolStrip.addButton(btnLast);			
		fraudProcessingHistoryPorlet.getPortletLayout().addMember(fraudProcessingHistoryToolStrip, 0);
		fraudDashboardPortalLayout.addPortlet(fraudProcessingHistoryPorlet,0,1);
		}
	}

	public void showFussionChart(HashMap<String,String> map){	
		fraudDashboardPortalLayout.addPortlet(new ExternalDashboardPortlet("Fraud Case", GWT.getHostPageBaseURL() + "FusionChartsFree/FraudDashboardFraudCase.html?"+map),1,0);
		fraudDashboardLayout.setMembers(fraudDashboardPortalLayout);

	}

}
