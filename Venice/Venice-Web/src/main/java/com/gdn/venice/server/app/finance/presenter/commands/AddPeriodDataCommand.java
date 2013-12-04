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
 * This is Datasource-style presenter command use for adding period
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class AddPeriodDataCommand implements RafDsCommand {
	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Add Period data
	 * @param rafDsRequest
	 */
	public AddPeriodDataCommand(RafDsRequest rafDsRequest) {

		this.request=rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> periodLocator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			periodLocator = new Locator<Object>();
			FinPeriodSessionEJBRemote sessionHome = (FinPeriodSessionEJBRemote) periodLocator.lookup(FinPeriodSessionEJBRemote.class, "FinPeriodSessionEJBBean");
			dataList=request.getData();
			
			boolean cekDate=false;
			FinPeriod finPeriod = new FinPeriod();
			 for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				
				Date fromDate =new Date(new Long(data.get(DataNameTokens.FINPERIOD_FROMDATETIME)));
				Date toDate =new Date(new Long(data.get(DataNameTokens.FINPERIOD_TODATETIME)));	
				if(fromDate.before(toDate)){
					cekDate=true;
				}else{
					cekDate=false;
					break;
				}
				Iterator<String> iter=data.keySet().iterator();
     			while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FINPERIOD_PERIODID)){
						finPeriod.setPeriodId(new Long (data.get(key).toString()));
					}else if(key.equals(DataNameTokens.FINPERIOD_PERIODDESC)){
						finPeriod.setPeriodDesc(data.get(key));
					}else if(key.equals(DataNameTokens.FINPERIOD_FROMDATETIME)){
						Long dateFrom =new Long (data.get(key));
						finPeriod.setFromDatetime(new Timestamp(dateFrom));
					} else if(key.equals(DataNameTokens.FINPERIOD_TODATETIME)){
						Long dateTo =new Long (data.get(key));
						finPeriod.setToDatetime(new Timestamp(dateTo));					
					}
								
				}
			}
			if(cekDate){
				finPeriod=sessionHome.persistFinPeriod(finPeriod);
				rafDsResponse.setStatus(0);
			}else{
				rafDsResponse.setStatus(-1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(periodLocator!=null){
					periodLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}