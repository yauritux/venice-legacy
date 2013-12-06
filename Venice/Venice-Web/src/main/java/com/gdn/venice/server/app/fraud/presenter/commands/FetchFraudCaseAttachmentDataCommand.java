package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudFileAttachment;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Attachment
 * 
 * @author Roland
 */

public class FetchFraudCaseAttachmentDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchFraudCaseAttachmentDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdFraudFileAttachmentSessionEJBRemote sessionHome = (FrdFraudFileAttachmentSessionEJBRemote) locator.lookup(FrdFraudFileAttachmentSessionEJBRemote.class, "FrdFraudFileAttachmentSessionEJBBean");
			List<FrdFraudFileAttachment> attachmentList = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FrdFraudFileAttachment o join fetch o.frdFraudSuspicionCase where o.frdFraudSuspicionCase.fraudSuspicionCaseId =  "+ request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).toString();		
				attachmentList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				FrdFraudFileAttachment attachment = new FrdFraudFileAttachment();
				JPQLSimpleQueryCriteria caseIdCriteria = new JPQLSimpleQueryCriteria();
				caseIdCriteria.setFieldName(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID);
				caseIdCriteria.setOperator("equals");
				caseIdCriteria.setValue(request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				caseIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID));
				criteria.add(caseIdCriteria);
				attachmentList = sessionHome.findByFrdFraudFileAttachmentLike(attachment, criteria, 0, 0);
			}
			
			for(int i=0; i<attachmentList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdFraudFileAttachment list = attachmentList.get(i);
				map.put(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID, Util.isNull(list.getFraudFileAttachmentId(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILENAME, Util.isNull(list.getFileName(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEATTACHMENT_DESCRIPTION, Util.isNull(list.getFraudFileAttachmentDescription(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEATTACHMENT_CREATEDBY, Util.isNull(list.getCreatedBy(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILELOCATION, Util.isNull(list.getFileLocation(), "").toString());
				map.put(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID, list.getFrdFraudSuspicionCase()!=null && list.getFrdFraudSuspicionCase().getFraudSuspicionCaseId()!=null?list.getFrdFraudSuspicionCase().getFraudSuspicionCaseId().toString():"");
				
				dataList.add(map);				
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
