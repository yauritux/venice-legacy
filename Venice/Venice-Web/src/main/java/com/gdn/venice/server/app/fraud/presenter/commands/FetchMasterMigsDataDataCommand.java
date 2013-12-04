package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMigsUploadMasterSessionEJBRemote;
import com.gdn.venice.persistence.VenMigsUploadMaster;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchMasterMigsDataDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public FetchMasterMigsDataDataCommand(RafDsRequest request){
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenMigsUploadMasterSessionEJBRemote sessionHome = (VenMigsUploadMasterSessionEJBRemote) locator.lookup(VenMigsUploadMasterSessionEJBRemote.class, "VenMigsUploadMasterSessionEJBBean");
			List<VenMigsUploadMaster> migsUploadList = null;
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from VenMigsUploadMaster o";			
				migsUploadList = sessionHome.queryByRange(query, 0, 50);
			} else {
				VenMigsUploadMaster migsUpload = new VenMigsUploadMaster();
				migsUploadList = sessionHome.findByVenMigsUploadMasterLike(migsUpload, criteria, 0, 0);
			}
			
			for (int i = 0; i < migsUploadList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenMigsUploadMaster migsUpload = migsUploadList.get(i);
				
				
				map.put(DataNameTokens.MIGSMASTER_MIGSID, Util.isNull(migsUpload.getMigsId(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_TRANSACTIONID, Util.isNull(migsUpload.getTransactionId(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_TRANSACTIONDATE, Util.isNull(migsUpload.getTransactionDate(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_MERCHANTID, Util.isNull(migsUpload.getMerchantId(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_ORDERREFERENCE, Util.isNull(migsUpload.getOrderReference(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_ORDERID, Util.isNull(migsUpload.getOrderId(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_MERCHANTTRANSACTIONREFERENCE, Util.isNull(migsUpload.getMerchantTransactionReference(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_TRANSACTIONTYPE, Util.isNull(migsUpload.getTransactionType(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_ACQUIRERID, Util.isNull(migsUpload.getAcquirerId(), "").toString());				
				map.put(DataNameTokens.MIGSMASTER_BATCHNUMBER, Util.isNull(migsUpload.getBatchNumber(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_CURRENCY, Util.isNull(migsUpload.getCurrency(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_AMOUNT, Util.isNull(migsUpload.getAmount(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_RRN, Util.isNull(migsUpload.getRrn(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_RESPONSECODE, Util.isNull(migsUpload.getResponseCode(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_ACQUIRERRESPONSECODE, Util.isNull(migsUpload.getAcquirerResponseCode(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_AUTHORISATIONCODE, Util.isNull(migsUpload.getAuthorisationCode(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_OPERATOR, Util.isNull(migsUpload.getOperator(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_MERCHANTTRANSACTIONSOURCE, Util.isNull(migsUpload.getMerchantTransactionSource(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_ORDERDATE, Util.isNull(migsUpload.getOrderDate(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_CARDTYPE, Util.isNull(migsUpload.getCardType(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_CARDNUMBER, Util.isNull(migsUpload.getCardNumber(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_CARDEXPIRYMONTH, Util.isNull(migsUpload.getCardExpiryMonth(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_CARDEXPIRYYEAR, Util.isNull(migsUpload.getCardExpiryYear(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_DIALECTCSCRESULTCODE, Util.isNull(migsUpload.getDialectCscResultCode(), "").toString());			
				map.put(DataNameTokens.MIGSMASTER_ECOMMERCEINDICATOR, Util.isNull(migsUpload.getEcommerceIndicator(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_PROBLEMDESCRIPTION, Util.isNull(migsUpload.getProblemDescription(), "").toString());
				map.put(DataNameTokens.MIGSMASTER_ACTION, Util.isNull(migsUpload.getAction(), "").toString());
			
				dataList.add(map);				
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(migsUploadList.size());
			rafDsResponse.setEndRow(request.getStartRow() + migsUploadList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator != null) {
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