package com.gdn.venice.server.app.finance.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinPeriodSessionEJBRemote;
import com.gdn.venice.persistence.FinPeriod;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter  command for Updating/Editing Period Data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class UpdatePeriodeDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Update Period data
	 * @param rafDsRequest
	 */
	public UpdatePeriodeDataCommand(RafDsRequest rafDsRequest) {

		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();

		List<FinPeriod> periodList = new ArrayList<FinPeriod>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FinPeriod entityFinPeriod = new FinPeriod();
		boolean cekDate=true;
		boolean cek=true;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FinPeriodSessionEJBRemote sessionHome = (FinPeriodSessionEJBRemote) locator.lookup(FinPeriodSessionEJBRemote.class, "FinPeriodSessionEJBBean");
	
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();
			
				if(data.get(DataNameTokens.FINPERIOD_FROMDATETIME)!=null&&data.get(DataNameTokens.FINPERIOD_TODATETIME)!=null){
					Date fromDate =new Date(new Long(data.get(DataNameTokens.FINPERIOD_FROMDATETIME)));
					Date toDate =new Date(new Long(data.get(DataNameTokens.FINPERIOD_TODATETIME)));	
					if(fromDate.before(toDate)){
						cekDate=true;
						cek=false;
					}else{
						cekDate=false;
						break;
					}
				}else{
					cek=true;	
			
					while (iter.hasNext()) {
						String key = iter.next();
						if (key.equals(DataNameTokens.FINPERIOD_PERIODID)) {
							try{
								entityFinPeriod = sessionHome.queryByRange("select o from FinPeriod o where o.periodId="+new Long(data.get(key)), 0, 1).get(0);
							}catch(IndexOutOfBoundsException e){
								entityFinPeriod.setPeriodId(new Long(data.get(key)));
							}
							break;
						}
					}
					
					iter = data.keySet().iterator();
					while (iter.hasNext()) {
						String key = iter.next();
						if (key.equals(DataNameTokens.FINPERIOD_PERIODDESC)) {
							entityFinPeriod.setPeriodDesc(data.get(key));
						} else if (key.equals(DataNameTokens.FINPERIOD_FROMDATETIME)) {
							Long fromDate = new Long(data.get(key));
							entityFinPeriod.setFromDatetime(new Timestamp(fromDate));
							if(cek){
								List<FinPeriod> finPeriodList= sessionHome.queryByRange("select o from FinPeriod o where o.periodId="+entityFinPeriod.getPeriodId(), 0, 0);
								FinPeriod listPeriod= finPeriodList.get(0);
								if(new Date(new Long(data.get(key))).before(listPeriod.getToDatetime())){
									cekDate=true;
								}else{
									cekDate=false;
									break;
								}						
							}					
						} else if (key.equals(DataNameTokens.FINPERIOD_TODATETIME)) {
							Long dateTo = new Long(data.get(key));
							entityFinPeriod.setToDatetime(new Timestamp(dateTo));
							if(cek){
								List<FinPeriod> finPeriodList= sessionHome.queryByRange("select o from FinPeriod o where o.periodId="+entityFinPeriod.getPeriodId(), 0, 0);
								FinPeriod listPeriod= finPeriodList.get(0);
								if(new Date(new Long(data.get(key))).after(listPeriod.getFromDatetime())){
									cekDate=true;
								}else{
									cekDate=false;
									break;
								}						
							}				
						}
					}				
					periodList.add(entityFinPeriod);			
				}
	
				if(cekDate){
					sessionHome.mergeFinPeriodList((ArrayList<FinPeriod>)periodList);
					rafDsResponse.setStatus(0);
				}else{
					rafDsResponse.setStatus(-1);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}