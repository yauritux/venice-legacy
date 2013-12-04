package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FinPeriodSessionEJBRemote;
import com.gdn.venice.persistence.FinPeriod;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter  command for Deleting Period Data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class DeletePeriodeDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public DeletePeriodeDataCommand(RafDsRequest rafDsRequest) {
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
		FinPeriod entityPeriod = new FinPeriod();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.FINPERIOD_PERIODID)) {
					entityPeriod.setPeriodId(new Long(data.get(DataNameTokens.FINPERIOD_PERIODID)));
				} 
			}						
			periodList.add(entityPeriod);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FinPeriodSessionEJBRemote finPeriodSessionHome = (FinPeriodSessionEJBRemote) locator.lookup(FinPeriodSessionEJBRemote.class, "FinPeriodSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<periodList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.FINPERIOD_PERIODID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(periodList.get(i).getPeriodId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FINPERIOD_PERIODID));
				criteria.add(simpleCriteria);
			}
			periodList = finPeriodSessionHome.findByFinPeriodLike(entityPeriod, criteria, request.getStartRow(), request.getEndRow());
			finPeriodSessionHome.removeFinPeriodList((ArrayList<FinPeriod>)periodList);			
									
			rafDsResponse.setStatus(0);
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