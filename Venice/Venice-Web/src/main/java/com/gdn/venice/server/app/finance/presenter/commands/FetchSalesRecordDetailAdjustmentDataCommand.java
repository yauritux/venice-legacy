package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.data.FinanceData;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Sales Record Detail Adjustment
 * 
 * @author Roland
 */

public class FetchSalesRecordDetailAdjustmentDataCommand implements RafDsCommand {

	RafDsRequest request;
	protected static Logger _log = null;
	private Long startTime, endTime, duration;
	
	public FetchSalesRecordDetailAdjustmentDataCommand(RafDsRequest request){
		this.request=request;
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.server.app.finance.presenter.commands.FetchSalesRecordDetailAdjustmentDataCommand");
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		String wcsOrderItemId=request.getParams().get(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID);
		try{
			locator = new Locator<Object>();			
			VenOrderItemAdjustmentSessionEJBRemote sessionHome = (VenOrderItemAdjustmentSessionEJBRemote) locator.lookup(VenOrderItemAdjustmentSessionEJBRemote.class, "VenOrderItemAdjustmentSessionEJBBean");
				
			List<VenOrderItemAdjustment> adjustmentList = null;
						
			String query = "select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId =(select b.orderItemId from VenOrderItem b where b.wcsOrderItemId='"+ wcsOrderItemId+"')";		
			
			_log.debug("loadSalesRecordData()");
			startTime = System.currentTimeMillis();
			adjustmentList = sessionHome.queryByRange(query, 0, 0);
			endTime = System.currentTimeMillis();
			duration = startTime - endTime;
			_log.debug("loadSalesRecordData() duration:" + duration + "ms");
			
			if(adjustmentList !=null){
				for(int i=0; i<adjustmentList.size();i++){
					HashMap<String, String> map = new HashMap<String, String>();
					VenOrderItemAdjustment list = adjustmentList.get(i);		
					map.put(DataNameTokens.VENORDERITEMADJUSTMENT_VENPROMOTION_PROMOCODE, list.getVenPromotion()!=null && list.getVenPromotion().getPromotionName()!=null?list.getVenPromotion().getPromotionName().toString():"");
					map.put(DataNameTokens.VENORDERITEMADJUSTMENT_PROMOTIONVOUCHERCODE, Util.isNull(list.getVenPromotion().getPromotionCode(), "").toString());
					map.put(DataNameTokens.VENORDERITEMADJUSTMENT_VENORDERITEM_ORDERITEMID, wcsOrderItemId);
					map.put(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT, Util.isNull(list.getAmount(), "").toString());
					map.put(DataNameTokens.VENORDERITEMADJUSTMENT_ADMINDESC, Util.isNull(list.getAdminDesc(), "").toString());		
					dataList.add(map);				
				}
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
