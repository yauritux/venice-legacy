package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenMigsUploadTemporary;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchMigsUploadTemporaryDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public FetchMigsUploadTemporaryDataCommand(RafDsRequest request){
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenMigsUploadTemporarySessionEJBRemote sessionHome = (VenMigsUploadTemporarySessionEJBRemote) locator.lookup(VenMigsUploadTemporarySessionEJBRemote.class, "VenMigsUploadTemporarySessionEJBBean");
			
			List<VenMigsUploadTemporary> migsUploadList = null;
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from VenMigsUploadTemporary o";			
				migsUploadList = sessionHome.queryByRange(query, 0, 0);
			} else {
				VenMigsUploadTemporary migsUpload = new VenMigsUploadTemporary();
				migsUploadList = sessionHome.findByVenMigsUploadTemporaryLike(migsUpload, criteria, 0, 0);
			}
			
			for (int i = 0; i < migsUploadList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenMigsUploadTemporary migsUpload = migsUploadList.get(i);
				
				String problemDescription = Util.isNull(migsUpload.getProblemDescription(), "").toString();
				String action = Util.isNull(migsUpload.getAction(), "").toString();
				
				//Jika order belum ter-sync maka selalu cek apakah skrg order sudah ada atau blum
				if (migsUpload.getProblemDescription() != null && migsUpload.getProblemDescription().toLowerCase().contains("order is not found on venice")) {
					VenOrderSessionEJBRemote venOrderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
					
					List<VenOrder> orderList = null;
					String query = "select o from VenOrderPaymentAllocation opa inner join opa.venOrder o inner join opa.venOrderPayment op" +
					" where o.wcsOrderId = '" + isNull(migsUpload.getMerchantTransactionReference(), "").replaceAll("-.*$", "") +
					"' and op.paymentConfirmationNumber = '" + isNull(migsUpload.getAuthorisationCode(), "") + "'";
					
					orderList = venOrderSessionHome.queryByRange(query, 0, 0);
					if (orderList.size() > 0) {
						problemDescription = "";
						action = "SUBMIT";
						
						VenMigsUploadTemporary entityMigsUpload;
						try{
							entityMigsUpload = sessionHome.queryByRange("select o from VenMigsUploadTemporary o where o.migsId="+migsUpload.getMigsId(), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityMigsUpload = new VenMigsUploadTemporary();
							entityMigsUpload.setMigsId(migsUpload.getMigsId());
						}
						entityMigsUpload.setProblemDescription(problemDescription);
						entityMigsUpload.setAction(action);
						sessionHome.mergeVenMigsUploadTemporary(entityMigsUpload);
					}
				}
				
				map.put(DataNameTokens.MIGSUPLOAD_MIGSID, Util.isNull(migsUpload.getMigsId(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_MERCHANTTRANSACTIONREFERENCE, Util.isNull(migsUpload.getMerchantTransactionReference(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_AMOUNT, Util.isNull(migsUpload.getAmount(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_AUTHORISATIONCODE, Util.isNull(migsUpload.getAuthorisationCode(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_CARDNUMBER, Util.isNull(migsUpload.getCardNumber(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_CARDTYPE, Util.isNull(migsUpload.getCardType(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_PROBLEMDESCRIPTION, problemDescription);
				map.put(DataNameTokens.MIGSUPLOAD_TRANSACTIONTYPE, Util.isNull(migsUpload.getTransactionType(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_RESPONSECODE, Util.isNull(migsUpload.getResponseCode(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_ECOMMERCEINDICATOR, Util.isNull(migsUpload.getEcommerceIndicator(), "").toString());
				map.put(DataNameTokens.MIGSUPLOAD_ACTION, action);
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

	private String isNull(Object object, String replacement) {
		return object == null ? replacement : object.toString();
	}
}