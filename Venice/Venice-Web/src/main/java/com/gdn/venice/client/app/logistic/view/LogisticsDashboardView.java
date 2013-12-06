package com.gdn.venice.client.app.logistic.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.logistic.presenter.LogisticsDashboardPresenter;
import com.gdn.venice.client.app.logistic.view.handlers.LogisticsDashboardUiHandlers;
import com.gdn.venice.client.widgets.ExternalDashboardPortlet;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.PortalLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;


/**
 * View for Logistics Dashboard
 * 
 * @author Henry Chandra
 */
public class LogisticsDashboardView extends
		ViewWithUiHandlers<LogisticsDashboardUiHandlers> implements
		LogisticsDashboardPresenter.MyView {

	RafViewLayout logisticsDashboardLayout;
	ExternalDashboardPortlet logisticsOrderStatusVolHistoryPorlet;
	PortalLayout logisticsDashboardPortalLayout;
	
	@Inject
	public LogisticsDashboardView() {
		logisticsDashboardLayout = new RafViewLayout();
		
		logisticsDashboardPortalLayout = new PortalLayout();
		logisticsDashboardPortalLayout.setShowColumnMenus(false);
		logisticsDashboardPortalLayout.setWidth100();
		logisticsDashboardPortalLayout.setHeight100();
		logisticsDashboardPortalLayout.setNumColumns(1);
		OrderProcessingHistory(null,null,true,"0");		
		logisticsDashboardLayout.setMembers(logisticsDashboardPortalLayout);

		bindCustomUiHandlers();
	}


	@Override
	public Widget asWidget() {
		return logisticsDashboardLayout;
	}

	protected void bindCustomUiHandlers() {	}
	
	public void OrderProcessingHistory(Map mapFussion,String idMap,boolean action,String nerxOrBack){
		final String countNerxOrBack=nerxOrBack;
		if(logisticsOrderStatusVolHistoryPorlet!=null&&action){
			logisticsOrderStatusVolHistoryPorlet.destroy();
		}
		
		if(idMap!=null&&!action){		
		getUiHandlers().FetchOrderProcessingHistory(idMap,countNerxOrBack) ;
		}else if(idMap!=null&&action){
			logisticsOrderStatusVolHistoryPorlet = new ExternalDashboardPortlet("Order Processing History", GWT.getHostPageBaseURL() + "FusionChartsFree/LogisticsDashboardOrderStatusVol.html?"+mapFussion);
		}else{
			logisticsOrderStatusVolHistoryPorlet = new ExternalDashboardPortlet("Order Processing History", GWT.getHostPageBaseURL() + "FusionChartsFree/LogisticsDashboardOrderStatusVol.html?&");
		}
		
		if(action){
				ToolStrip logisticsOrderStatusVolToolStrip = new ToolStrip();
				logisticsOrderStatusVolToolStrip.setWidth100();
				
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
						OrderProcessingHistory(null,timeSelectItem.getValueAsString(),false,"0");
					}
					
				});
				
				ToolStripButton btnBackward = new ToolStripButton();
				btnBackward.setIcon("[SKIN]/icons/backward.png");
				btnBackward.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {				
						int count=new Integer(countNerxOrBack);
						count--;
						getUiHandlers().FetchOrderProcessingHistory(timeSelectItem.getValueAsString(),""+count);				
					}			
				});
				
				ToolStripButton btnForward = new ToolStripButton();
				btnForward.setIcon("[SKIN]/icons/forward.png");
				btnForward.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {				
						int count=new Integer(countNerxOrBack);
						count++;
						getUiHandlers().FetchOrderProcessingHistory(timeSelectItem.getValueAsString(),""+count);				
					}
					
				});
				
				
				ToolStripButton btnFirst = new ToolStripButton();
				btnFirst.setIcon("[SKIN]/icons/first.png");
				btnFirst.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {				
						int count=new Integer(countNerxOrBack);
						count-=4;
							getUiHandlers().FetchOrderProcessingHistory(timeSelectItem.getValueAsString(),""+count);				
					}
					
				});		
				
				ToolStripButton btnLast = new ToolStripButton();
				btnLast.setIcon("[SKIN]/icons/last.png");
				btnLast.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {				
							getUiHandlers().FetchOrderProcessingHistory(timeSelectItem.getValueAsString(),"0");				
					}
					
				});

				
				logisticsOrderStatusVolToolStrip.addFormItem(timeSelectItem);
				logisticsOrderStatusVolToolStrip.addSeparator();
				logisticsOrderStatusVolToolStrip.addButton(btnFirst);
				logisticsOrderStatusVolToolStrip.addButton(btnBackward);
				logisticsOrderStatusVolToolStrip.addButton(btnForward);
				logisticsOrderStatusVolToolStrip.addButton(btnLast);
				
				logisticsOrderStatusVolHistoryPorlet.getPortletLayout().addMember(logisticsOrderStatusVolToolStrip, 0);
				logisticsDashboardPortalLayout.addPortlet(logisticsOrderStatusVolHistoryPorlet,0,0);
				}
	}
	
}
