package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudFileAttachment;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Attachment Log
 * 
 * @author Roland
 */

public class UpdateFraudCaseAttachmentDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateFraudCaseAttachmentDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdFraudFileAttachment> attachmentList = new ArrayList<FrdFraudFileAttachment>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdFraudFileAttachment attachment = new FrdFraudFileAttachment();
			
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			
			FrdFraudFileAttachmentSessionEJBRemote sessionHome = (FrdFraudFileAttachmentSessionEJBRemote) locator
			.lookup(FrdFraudFileAttachmentSessionEJBRemote.class, "FrdFraudFileAttachmentSessionEJBBean");

			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID)) {
						try{
							attachment = sessionHome.queryByRange("select o from FrdFraudFileAttachment o where o.fraudFileAttachmentId="+new Long(data.get(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							attachment.setFraudFileAttachmentId(new Long(data.get(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID)));
						}
						break;
					} 
				}			
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDFRAUDCASEATTACHMENT_DESCRIPTION)) {
						attachment.setFraudFileAttachmentDescription(data.get(key));
					}  else if (key.equals(DataNameTokens.FRDFRAUDCASEATTACHMENT_CREATEDBY)) {
						attachment.setCreatedBy(data.get(key));
					}  else if (key.equals(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILELOCATION)) {
						attachment.setFileLocation(data.get(key));
					}  else if (key.equals(DataNameTokens.FRDFRAUDCASEATTACHMENT_FILENAME)) {
						attachment.setFileName(data.get(key));
					}  else if (key.equals(DataNameTokens.FRDFRAUDCASEATTACHMENT_FRDFRAUDSUSPICIONCASE_SUSPICIONCASEID)) {
						FrdFraudSuspicionCase frdFraudSuspicionCase = new FrdFraudSuspicionCase();
						frdFraudSuspicionCase.setFraudSuspicionCaseId(new Long(data.get(key)));
						attachment.setFrdFraudSuspicionCase(frdFraudSuspicionCase);
					} 
				}						
				
				attachmentList.add(attachment);			
			}
			
				
			sessionHome.mergeFrdFraudFileAttachmentList((ArrayList<FrdFraudFileAttachment>)attachmentList);
			
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
