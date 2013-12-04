	
package com.gdn.venice.server.app.finance.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.finance.Dashboard.FinSalesRecordWithNativeQuerySessionEJBRemote;
import com.gdn.venice.persistence.FinSalesRecord;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Blacklist Source
 * 
 * @author Roland
 */

public class FetchFussionChartRevenueHistoryCommand implements RafRpcCommand{
	String[] action;

	public FetchFussionChartRevenueHistoryCommand(String parameter) {
		action = parameter.split("&");
	}
	
	public String execute() {
		Locator<Object> locator=null;
		 
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<Integer, String> mapTem = new HashMap<Integer, String>();

		try{
			locator = new Locator<Object>();			
			FinSalesRecordWithNativeQuerySessionEJBRemote sessionHome = (FinSalesRecordWithNativeQuerySessionEJBRemote) locator.lookup(FinSalesRecordWithNativeQuerySessionEJBRemote.class, "FinSalesRecordWithNativeQuerySessionEJBBean");			
			
			List<FinSalesRecord> finSalesRecordListSource = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
			SimpleDateFormat sdff = new SimpleDateFormat("dd-MM-yyyy");	

			double sumCF=0,sumTF=0,sumHF=0;
			if(action[0].equals("1")){				
							String strMap="";
							for(int i=0;i<5;i++){
									sumCF=0;
									sumTF=0;
									sumHF=0;
									Date toDayStart = DateUtils.addDays(new Date(),new Integer(action[1])-i);		
									String day = ""+sdf.format(toDayStart);
									String query = "SELECT * FROM fin_sales_record WHERE Date(sales_timestamp)='"+day+"'";			
									
									finSalesRecordListSource = sessionHome.queryByRangeWithNativeQueryUseToFinanceDashboard(query);
									if(finSalesRecordListSource.size()!=0){
												for(int j=0;j<finSalesRecordListSource.size();j++){
													FinSalesRecord list = finSalesRecordListSource.get(j);
													sumCF+=new Double(list.getGdnCommissionAmount().toString());
													sumTF+=new Double(list.getGdnTransactionFeeAmount().toString());
													sumHF+=new Double(list.getGdnHandlingFeeAmount().toString());				
												}
									}
									strMap+="{ Commission : "+sumCF;
									strMap+="/Transaction Fee : "+sumTF;
									strMap+="/Handling Fee : "+sumHF+"}";	
									if(i!=0) strMap+="&";								
									mapTem.put(i, sdff.format(toDayStart)+"="+strMap);
									strMap="";
								}
							map=Util.SortedHashMap(mapTem, "desc");

			}else if(action[0].equals("2")){
				String strMap="";
				Calendar cal = Calendar.getInstance();
				int date = cal.get(Calendar.DAY_OF_WEEK);
				Date toWeek = DateUtils.addDays(new Date(),+(8-date));	
				toWeek=DateUtils.addWeeks(toWeek,new Integer(action[1]));
				for(int i=0;i<5;i++){
						sumCF=0;
						sumTF=0;
						sumHF=0;
						
						Date toWeekStart = DateUtils.addWeeks(toWeek,-i);	
						Date toWeekBack = DateUtils.addWeeks(toWeek,-(i+1));							
						toWeekBack = DateUtils.addDays(toWeekBack, +1);
						String startWeek = ""+sdf.format(toWeekStart);
						String backWeek=""+sdf.format(toWeekBack);
						String query = "SELECT * FROM fin_sales_record WHERE Date(sales_timestamp) Between '"+backWeek+"' and '"+startWeek+"'";			

						finSalesRecordListSource = sessionHome.queryByRangeWithNativeQueryUseToFinanceDashboard(query);
					if(finSalesRecordListSource.size()!=0){
									for(int j=0;j<finSalesRecordListSource.size();j++){
									FinSalesRecord list = finSalesRecordListSource.get(j);
										sumCF+=new Double(list.getGdnCommissionAmount().toString());
										sumTF+=new Double(list.getGdnTransactionFeeAmount().toString());
										sumHF+=new Double(list.getGdnHandlingFeeAmount().toString());	
									}
					}
						strMap+="{ Commission : "+sumCF;
						strMap+="/Transaction Fee : "+sumTF;
						strMap+="/Handling Fee : "+sumHF+"}";	

						if(i!=0) strMap+="&";									

						mapTem.put(i, sdff.format(toWeekStart)+"="+strMap);
						strMap="";
				}
				map=Util.SortedHashMap(mapTem, "desc");

			}else if(action[0].equals("3")){
				String strMap="";											
				Date startMonth = new Date();
				startMonth.setDate(1);
				Date toMonth=DateUtils.addMonths(startMonth,new Integer(action[1])+1);
				for(int i=0;i<5;i++){
					sumCF=0;
					sumTF=0;
					sumHF=0;
					Date monthToday = DateUtils.addMonths(toMonth,-i);
					Date monthBack = DateUtils.addMonths(toMonth,-(i+1));
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					String query = "SELECT * FROM fin_sales_record WHERE Date(sales_timestamp) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"'";	
					finSalesRecordListSource = sessionHome.queryByRangeWithNativeQueryUseToFinanceDashboard(query);
				if(finSalesRecordListSource.size()!=0){
								for(int j=0;j<finSalesRecordListSource.size();j++){
								FinSalesRecord list = finSalesRecordListSource.get(j);
									sumCF+=new Double(list.getGdnCommissionAmount().toString());
									sumTF+=new Double(list.getGdnTransactionFeeAmount().toString());
									sumHF+=new Double(list.getGdnHandlingFeeAmount().toString());	
								}
				}
					strMap+="{ Commission : "+sumCF;
					strMap+="/Transaction Fee : "+sumTF;
					strMap+="/Handling Fee : "+sumHF+"}";	

					if(i!=0) strMap+="&";									

					mapTem.put(i, sdff.format(dayMonth)+"="+strMap);
					strMap="";
			}
			map=Util.SortedHashMap(mapTem, "desc");

			}else if(action[0].equals("4")){
				String strMap="";											
				Date startMonth = new Date();
				startMonth.setDate(1);
				Date toMonth=DateUtils.addMonths(startMonth,(new Integer(action[1])*3)+1);
				for(int i=0;i<5;i++){
					sumCF=0;
					sumTF=0;
					sumHF=0;
					Date monthToday = DateUtils.addMonths(toMonth,-(i*3));
					Date monthBack = DateUtils.addMonths(toMonth,-(i*3)-3);
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					String query = "SELECT * FROM fin_sales_record WHERE Date(sales_timestamp) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"'";	
					finSalesRecordListSource = sessionHome.queryByRangeWithNativeQueryUseToFinanceDashboard(query);
				if(finSalesRecordListSource.size()!=0){
								for(int j=0;j<finSalesRecordListSource.size();j++){
								FinSalesRecord list = finSalesRecordListSource.get(j);
									sumCF+=new Double(list.getGdnCommissionAmount().toString());
									sumTF+=new Double(list.getGdnTransactionFeeAmount().toString());
									sumHF+=new Double(list.getGdnHandlingFeeAmount().toString());	
								}
				}
					strMap+="{ Commission : "+sumCF;
					strMap+="/Transaction Fee : "+sumTF;
					strMap+="/Handling Fee : "+sumHF+"}";	
					if(i!=0) strMap+="&";									
					mapTem.put(i, sdff.format(dayMonth)+"="+strMap);
					strMap="";
			}
			map=Util.SortedHashMap(mapTem, "desc");

			}else if(action[0].equals("5")){
				String strMap="";											
				Date startMonth = new Date();
				startMonth.setDate(1);
				Date toMonth=DateUtils.addMonths(startMonth,(new Integer(action[1])*6)+1);
				for(int i=0;i<5;i++){
					sumCF=0;
					sumTF=0;
					sumHF=0;
					Date monthToday = DateUtils.addMonths(toMonth,-(i*6));
					Date monthBack = DateUtils.addMonths(toMonth,-(i*6)-6);
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					String query = "SELECT * FROM fin_sales_record WHERE Date(sales_timestamp) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"'";	
					finSalesRecordListSource = sessionHome.queryByRangeWithNativeQueryUseToFinanceDashboard(query);
				if(finSalesRecordListSource.size()!=0){
								for(int j=0;j<finSalesRecordListSource.size();j++){
								FinSalesRecord list = finSalesRecordListSource.get(j);
									sumCF+=new Double(list.getGdnCommissionAmount().toString());
									sumTF+=new Double(list.getGdnTransactionFeeAmount().toString());
									sumHF+=new Double(list.getGdnHandlingFeeAmount().toString());	
								}
				}
					strMap+="{ Commission : "+sumCF;
					strMap+="/Transaction Fee : "+sumTF;
					strMap+="/Handling Fee : "+sumHF+"}";	
					if(i!=0) strMap+="&";									
					mapTem.put(i, sdff.format(dayMonth)+"="+strMap);
					strMap="";
			}
			map=Util.SortedHashMap(mapTem, "desc");
			}						
	}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return Util.formXMLfromHashMap(map);
	}	
}
