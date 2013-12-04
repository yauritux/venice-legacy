package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote;
import com.gdn.venice.persistence.VenPromotionShareCalcMethod;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
/**
 * This Datasource-style presenter servlet command use for fetching calc method combo box data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchPromotionShareCalcMethodComboBoxData implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest rafDsRequest;
	
	/**
	 * Basic Constructor for Fetch Promotion Share Calc Method data
	 * 
	 * @param request is the request parameters passed to the command 
	 */
	public FetchPromotionShareCalcMethodComboBoxData(RafDsRequest rafDsRequest) {
		this.rafDsRequest = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<VenPromotionShareCalcMethod> calcMethodLocator = null;
		
		try {
			/*
			 * Just query all the values for the combo box from the database
			 */
			calcMethodLocator = new Locator<VenPromotionShareCalcMethod>();
			
			VenPromotionShareCalcMethodSessionEJBRemote sessionHome = (VenPromotionShareCalcMethodSessionEJBRemote) calcMethodLocator
			.lookup(VenPromotionShareCalcMethodSessionEJBRemote.class, "VenPromotionShareCalcMethodSessionEJBBean");
			
			List<VenPromotionShareCalcMethod> calcMethodList = null;
			
			calcMethodList = sessionHome.queryByRange("select o from VenPromotionShareCalcMethod o ", 0, 0);
			
			/*
			 * Map the values from the combo box
			 */
			for (VenPromotionShareCalcMethod venPromotionShareCalcMethod:calcMethodList) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put(DataNameTokens.VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID, venPromotionShareCalcMethod.getPromotionCalcMethodId() !=null? venPromotionShareCalcMethod.getPromotionCalcMethodId().toString():"");
				map.put(DataNameTokens.VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC, venPromotionShareCalcMethod.getPromotionCalcMethodDesc());
			
				dataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(rafDsRequest.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(rafDsRequest.getStartRow()+dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				calcMethodLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}
