package com.gdn.venice.server.app.fraud.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.fraud.FraudDashBoardSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafRpcCommand;

public class FetchFraudProcessingHistory implements RafRpcCommand {
	String[] action;
	public FetchFraudProcessingHistory(String parameter){
		this.action=parameter.split("&");
	}
	@Override
	public String execute() {
		Locator<Object> locator=null;		 
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<Integer, String> mapTem = new HashMap<Integer, String>();

		try{
			locator = new Locator<Object>();			
			FraudDashBoardSessionEJBRemote sessionHome = (FraudDashBoardSessionEJBRemote) locator.lookup(FraudDashBoardSessionEJBRemote.class, "FraudDashBoardSessionEJBBean");			
			
			List<VenOrder> venOrderRecordListSource = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
			SimpleDateFormat sdff = new SimpleDateFormat("dd-MM-yyyy");	

			Double sumC,sumSF,sumFC,sumFP;
			Double result;
			String SumOfStatusC="", SumOfStatusSF="",SumOfStatusFC="",SumOfStatusFP="";
			if(action[0].equals("1")){				
							String strMap="";
							for(int i=0;i<5;i++){								
									Date toDayStart = DateUtils.addDays(new Date(),new Integer(action[1])-i);		
									String day = ""+sdf.format(toDayStart);
									SumOfStatusC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date)='"+day+"' and order_status_id=1";	
									SumOfStatusSF = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date)='"+day+"' and order_status_id=2";	
									SumOfStatusFC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date)='"+day+"' and order_status_id=3";	
									SumOfStatusFP = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date)='"+day+"' and order_status_id=4";	
								
									sumC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusC);
									sumSF = sessionHome.queryByRangeWithNativeQuery(SumOfStatusSF);
									sumFC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFC);
									sumFP = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFP);					
									
									
									strMap+="{ Confirmed : "+sumC;
									strMap+="/Suspected Fraud : "+sumSF;
									strMap+="/Fraud Confirmed : "+sumFC;
									strMap+="/Fraud Check Passed: "+sumFP+"}";	
									if(i!=0) strMap+="&";								
									mapTem.put(i, sdff.format(toDayStart)+"="+strMap);
									strMap="";
								}
							map=Util.SortedHashMap(mapTem, "desc");

			}	else if(action[0].equals("2")){
				String strMap="";
				Calendar cal = Calendar.getInstance();
				int date = cal.get(Calendar.DAY_OF_WEEK);
				Date toWeek = DateUtils.addDays(new Date(),+(8-date));	
				toWeek=DateUtils.addWeeks(toWeek,new Integer(action[1]));
				for(int i=0;i<5;i++){											
						Date toWeekStart = DateUtils.addWeeks(toWeek,-i);	
						Date toWeekBack = DateUtils.addWeeks(toWeek,-(i+1));							
						toWeekBack = DateUtils.addDays(toWeekBack, +1);
						String startWeek = ""+sdf.format(toWeekStart);
						String backWeek=""+sdf.format(toWeekBack);
						SumOfStatusC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+backWeek+"' and '"+startWeek+"' and order_status_id=1";	
						SumOfStatusSF = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+backWeek+"' and '"+startWeek+"' and order_status_id=2";	
						SumOfStatusFC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+backWeek+"' and '"+startWeek+"' and order_status_id=3";	
						SumOfStatusFP = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+backWeek+"' and '"+startWeek+"' and order_status_id=4";	
					
						sumC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusC);
						sumSF = sessionHome.queryByRangeWithNativeQuery(SumOfStatusSF);
						sumFC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFC);
						sumFP = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFP);										
						
						strMap+="{ Confirmed : "+sumC;
						strMap+="/Suspected Fraud : "+sumSF;
						strMap+="/Fraud Confirmed : "+sumFC;
						strMap+="/Fraud Check Passed: "+sumFP+"}";	
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
					Date monthToday = DateUtils.addMonths(toMonth,-i);
					Date monthBack = DateUtils.addMonths(toMonth,-(i+1));
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					SumOfStatusC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=1";	
					SumOfStatusSF = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=2";	
					SumOfStatusFC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=3";	
					SumOfStatusFP = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=4";	
					
					sumC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusC);
					sumSF = sessionHome.queryByRangeWithNativeQuery(SumOfStatusSF);
					sumFC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFC);
					sumFP = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFP);										
				
					strMap+="{ Confirmed : "+sumC;
					strMap+="/Suspected Fraud : "+sumSF;
					strMap+="/Fraud Confirmed : "+sumFC;
					strMap+="/Fraud Check Passed: "+sumFP+"}";	
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
					
					Date monthToday = DateUtils.addMonths(toMonth,-(i*3));
					Date monthBack = DateUtils.addMonths(toMonth,-(i*3)-3);
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					SumOfStatusC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=1";	
					SumOfStatusSF = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=2";	
					SumOfStatusFC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=3";	
					SumOfStatusFP = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=4";	
													
					sumC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusC);
					sumSF = sessionHome.queryByRangeWithNativeQuery(SumOfStatusSF);
					sumFC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFC);
					sumFP = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFP);			
					
					strMap+="{ Confirmed : "+sumC;
					strMap+="/Suspected Fraud : "+sumSF;
					strMap+="/Fraud Confirmed : "+sumFC;
					strMap+="/Fraud Check Passed: "+sumFP+"}";	
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
					
					Date monthToday = DateUtils.addMonths(toMonth,-(i*6));
					Date monthBack = DateUtils.addMonths(toMonth,-(i*6)-6);
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					SumOfStatusC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=1";	
					SumOfStatusSF = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=2";	
					SumOfStatusFC = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=3";	
					SumOfStatusFP = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id=4";	
				
					sumC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusC);
					sumSF = sessionHome.queryByRangeWithNativeQuery(SumOfStatusSF);
					sumFC = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFC);
					sumFP = sessionHome.queryByRangeWithNativeQuery(SumOfStatusFP);				
					
					strMap+="{ Confirmed : "+sumC;
					strMap+="/Suspected Fraud : "+sumSF;
					strMap+="/Fraud Confirmed : "+sumFC;
					strMap+="/Fraud Check Passed: "+sumFP+"}";	
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



