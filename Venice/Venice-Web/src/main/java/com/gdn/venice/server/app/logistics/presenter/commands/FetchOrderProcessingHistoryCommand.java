package com.gdn.venice.server.app.logistics.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.logistics.dashboard.LogisticsDashBoardSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafRpcCommand;

public class FetchOrderProcessingHistoryCommand implements RafRpcCommand {
	String[] action;
	
	public FetchOrderProcessingHistoryCommand(String parameter){
		this.action=parameter.split("&");
	}
	@Override
	public String execute() {
		Locator<Object> locator=null;		 
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<Integer, String> mapTem = new HashMap<Integer, String>();

		try{
			locator = new Locator<Object>();			
			LogisticsDashBoardSessionEJBRemote sessionHome = (LogisticsDashBoardSessionEJBRemote) locator.lookup(LogisticsDashBoardSessionEJBRemote.class, "LogisticsDashBoardSessionEJBBean");			
			
			List<VenOrder> venOrderRecordListSource = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
			SimpleDateFormat sdff = new SimpleDateFormat("dd-MM-yyyy");	

			Double sumOrder[] = new Double[4];
			Double priceOrder[] = new Double[4];
			String SumOfStatusFP="", SumOfStatusPU="",SumOfStatusCX="",SumOfStatusRT="";
			if(action[0].equals("1")){				
							String strMap="";
							for(int i=0;i<5;i++){		
								for(int h=0;h<4;h++){
									sumOrder[h]=0.0;
									priceOrder[h]=0.0;
								}
									Date toDayStart = DateUtils.addDays(new Date(),new Integer(action[1])-i);		
									String day = ""+sdf.format(toDayStart);						
									
									String 	SumOfAmount = "SELECT sum(amount) FROM ven_order WHERE Date(order_date)='"+day+"' and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	
									String 	SumOfOrder = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date)='"+day+"' and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	

									List<Double> listAmount =sessionHome.queryByRangeWithNativeQuery(SumOfAmount);
									List<Double> listOrder =sessionHome.queryByRangeWithNativeQuery(SumOfOrder);				
																		
									if(listAmount.size()!=0 && listOrder.size()!=0){
										for(int h=0;h<4;h++){
											if(listAmount.size()>h && listOrder.size()>h){
												sumOrder[h]=listOrder.get(h);
												priceOrder[h]=listAmount.get(h);
											}
										}
											strMap+="{ Fraud Passed (FP) : "+sumOrder[0]+":"+priceOrder[0];
											strMap+="/Picked Up (PU) : "+sumOrder[1]+":"+priceOrder[1];
											strMap+="/Returned (RT) : "+sumOrder[2]+":"+priceOrder[2];
											strMap+="/Delivered (CX) : "+sumOrder[3]+":"+priceOrder[3]+"}";							
													if(i!=0) strMap+="&";																	
									}else{
										strMap+="{ Fraud Passed (FP) :0:0";
										strMap+="/Picked Up (PU) :0:0";
										strMap+="/Returned (RT) :0:0";
										strMap+="/Delivered (CX) :0:0";
										if(i!=0) strMap+="&";	
									}
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
						for(int h=0;h<4;h++){
							sumOrder[h]=0.0;
							priceOrder[h]=0.0;
						}
						Date toWeekStart = DateUtils.addWeeks(toWeek,-i);	
						Date toWeekBack = DateUtils.addWeeks(toWeek,-(i+1));							
						toWeekBack = DateUtils.addDays(toWeekBack, +1);
						String startWeek = ""+sdf.format(toWeekStart);
						String backWeek=""+sdf.format(toWeekBack);
						
						String 	SumOfAmount = "SELECT sum(amount) FROM ven_order WHERE Date(order_date) Between '"+backWeek+"' and '"+startWeek+"' and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	
						String 	SumOfOrder = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+backWeek+"' and '"+startWeek+"' and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	

						List<Double> listAmount =sessionHome.queryByRangeWithNativeQuery(SumOfAmount);
						List<Double> listOrder =sessionHome.queryByRangeWithNativeQuery(SumOfOrder);
						
						if(listAmount.size()!=0 && listOrder.size()!=0){
							for(int h=0;h<4;h++){
								if(listAmount.size()>h && listOrder.size()>h){
									sumOrder[h]=listOrder.get(h);
									priceOrder[h]=listAmount.get(h);
								}
							}
								strMap+="{ Fraud Passed (FP) : "+sumOrder[0]+":"+priceOrder[0];
								strMap+="/Picked Up (PU) : "+sumOrder[1]+":"+priceOrder[1];
								strMap+="/Returned (RT) : "+sumOrder[2]+":"+priceOrder[2];
								strMap+="/Delivered (CX) : "+sumOrder[3]+":"+priceOrder[3]+"}";							
										if(i!=0) strMap+="&";				
						}else{
							strMap+="{ Fraud Passed (FP) :0:0";
							strMap+="/Picked Up (PU) :0:0";
							strMap+="/Returned (RT) :0:0";
							strMap+="/Delivered (CX) :0:0";
							if(i!=0) strMap+="&";	
						}						
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
					for(int h=0;h<4;h++){
						sumOrder[h]=0.0;
						priceOrder[h]=0.0;
					}
					Date monthToday = DateUtils.addMonths(toMonth,-i);
					Date monthBack = DateUtils.addMonths(toMonth,-(i+1));
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					
					String 	SumOfAmount = "SELECT sum(amount) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"'  and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	
					String 	SumOfOrder = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	

					List<Double> listAmount =sessionHome.queryByRangeWithNativeQuery(SumOfAmount);
					List<Double> listOrder =sessionHome.queryByRangeWithNativeQuery(SumOfOrder);
					
					if(listAmount.size()!=0 && listOrder.size()!=0){
						for(int h=0;h<4;h++){
							if(listAmount.size()>h && listOrder.size()>h){
								sumOrder[h]=listOrder.get(h);
								priceOrder[h]=listAmount.get(h);
							}
						}
							strMap+="{ Fraud Passed (FP) : "+sumOrder[0]+":"+priceOrder[0];
							strMap+="/Picked Up (PU) : "+sumOrder[1]+":"+priceOrder[1];
							strMap+="/Returned (RT) : "+sumOrder[2]+":"+priceOrder[2];
							strMap+="/Delivered (CX) : "+sumOrder[3]+":"+priceOrder[3]+"}";							
									if(i!=0) strMap+="&";			
					}else{
						strMap+="{ Fraud Passed (FP) :0:0";
						strMap+="/Picked Up (PU) :0:0";
						strMap+="/Returned (RT) :0:0";
						strMap+="/Delivered (CX) :0:0";
						if(i!=0) strMap+="&";	
					}											
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
					for(int h=0;h<4;h++){
						sumOrder[h]=0.0;
						priceOrder[h]=0.0;
					}
					Date monthToday = DateUtils.addMonths(toMonth,-(i*3));
					Date monthBack = DateUtils.addMonths(toMonth,-(i*3)-3);
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					
					String 	SumOfAmount = "SELECT sum(amount) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"'  and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	
					String 	SumOfOrder = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	

					List<Double> listAmount =sessionHome.queryByRangeWithNativeQuery(SumOfAmount);
					List<Double> listOrder =sessionHome.queryByRangeWithNativeQuery(SumOfOrder);
					
					if(listAmount.size()!=0 && listOrder.size()!=0){
						for(int h=0;h<4;h++){
							if(listAmount.size()>h && listOrder.size()>h){
								sumOrder[h]=listOrder.get(h);
								priceOrder[h]=listAmount.get(h);
							}
						}
							strMap+="{ Fraud Passed (FP) : "+sumOrder[0]+":"+priceOrder[0];
							strMap+="/Picked Up (PU) : "+sumOrder[1]+":"+priceOrder[1];
							strMap+="/Returned (RT) : "+sumOrder[2]+":"+priceOrder[2];
							strMap+="/Delivered (CX) : "+sumOrder[3]+":"+priceOrder[3]+"}";							
									if(i!=0) strMap+="&";						
					}else{
						strMap+="{ Fraud Passed (FP) :0:0";
						strMap+="/Picked Up (PU) :0:0";
						strMap+="/Returned (RT) :0:0";
						strMap+="/Delivered (CX) :0:0";
						if(i!=0) strMap+="&";	
					}														
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
					for(int h=0;h<4;h++){
						sumOrder[h]=0.0;
						priceOrder[h]=0.0;
					}
					Date monthToday = DateUtils.addMonths(toMonth,-(i*6));
					Date monthBack = DateUtils.addMonths(toMonth,-(i*6)-6);
					Date dayMonth = DateUtils.addDays(monthToday,-1);
					
					String 	SumOfAmount = "SELECT sum(amount) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"'   and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	
					String 	SumOfOrder = "SELECT count(order_status_id) FROM ven_order WHERE Date(order_date) Between '"+sdf.format(monthBack)+"' and '"+sdf.format(dayMonth)+"' and order_status_id in (4,8,15,16) group by order_status_id order by order_status_id Asc";	

					List<Double> listAmount =sessionHome.queryByRangeWithNativeQuery(SumOfAmount);
					List<Double> listOrder =sessionHome.queryByRangeWithNativeQuery(SumOfOrder);
					
					if(listAmount.size()!=0 && listOrder.size()!=0){
						for(int h=0;h<4;h++){
							if(listAmount.size()>h && listOrder.size()>h){
								sumOrder[h]=listOrder.get(h);
								priceOrder[h]=listAmount.get(h);
							}
						}
							strMap+="{ Fraud Passed (FP) : "+sumOrder[0]+":"+priceOrder[0];
							strMap+="/Picked Up (PU) : "+sumOrder[1]+":"+priceOrder[1];
							strMap+="/Returned (RT) : "+sumOrder[2]+":"+priceOrder[2];
							strMap+="/Delivered (CX) : "+sumOrder[3]+":"+priceOrder[3]+"}";							
									if(i!=0) strMap+="&";						
					}else{
						strMap+="{ Fraud Passed (FP) :0:0";
						strMap+="/Picked Up (PU) :0:0";
						strMap+="/Returned (RT) :0:0";
						strMap+="/Delivered (CX) :0:0";
						if(i!=0) strMap+="&";	
					}												
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
