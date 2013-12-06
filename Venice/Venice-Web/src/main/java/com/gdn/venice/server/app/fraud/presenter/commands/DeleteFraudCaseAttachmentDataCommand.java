package com.gdn.venice.server.app.fraud.presenter.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudFileAttachmentSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudFileAttachment;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Attachment
 * 
 * @author Roland
 */

public class DeleteFraudCaseAttachmentDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteFraudCaseAttachmentDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {				
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdFraudFileAttachment> attachmentList = new ArrayList<FrdFraudFileAttachment>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdFraudFileAttachment attachment = new FrdFraudFileAttachment();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);			
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID)) {
					attachment.setFraudFileAttachmentId(new Long(data.get(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID)));
				} 
			}						
			attachmentList.add(attachment);			
		}
				
		Locator<Object> locator = null;		
		try {
			locator = new Locator<Object>();
			
			FrdFraudFileAttachmentSessionEJBRemote sessionHome = (FrdFraudFileAttachmentSessionEJBRemote) locator
			.lookup(FrdFraudFileAttachmentSessionEJBRemote.class, "FrdFraudFileAttachmentSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<attachmentList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(attachmentList.get(i).getFraudFileAttachmentId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDCASEATTACHMENT_ATTACHMENTID));
				criteria.add(simpleCriteria);
			}
			
			//delete the file first
			FrdFraudFileAttachment frdFraudFileAttachment = new FrdFraudFileAttachment();
			List<FrdFraudFileAttachment> attachmentListDelete = sessionHome.findByFrdFraudFileAttachmentLike(frdFraudFileAttachment, criteria, 0, 1);
			frdFraudFileAttachment = attachmentListDelete.get(0);
			String fileName = frdFraudFileAttachment.getFileLocation().toString();
			System.out.println("File location and name to be deleted: "+fileName);
			
		    // A File object to represent the filename
		    File FileToDelete = new File(fileName);

		    // Make sure the file or directory exists and isn't write protected
		    if (!FileToDelete.exists()){
		    	throw new IllegalArgumentException( "Delete: no such file or directory: " + fileName);
		    }
		    if (!FileToDelete.canWrite()){
		    	throw new IllegalArgumentException("Delete: write protected: " + fileName);
			}

		    // Attempt to delete the file
		    boolean success = FileToDelete.delete();
		    if (!success){
		    	System.out.println("Delete attachment file failed");
		    	throw new IllegalArgumentException("Deletion attachment failed");
		    }else{
		    	//delete the record
				attachmentList = sessionHome.findByFrdFraudFileAttachmentLike(attachment, criteria, request.getStartRow(), request.getEndRow());
				sessionHome.removeFrdFraudFileAttachmentList((ArrayList<FrdFraudFileAttachment>)attachmentList);
				rafDsResponse.setStatus(0);
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
