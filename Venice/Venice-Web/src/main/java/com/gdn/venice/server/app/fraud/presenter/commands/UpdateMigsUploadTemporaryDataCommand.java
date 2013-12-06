package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMigsUploadTemporarySessionEJBRemote;
import com.gdn.venice.persistence.VenMigsUploadTemporary;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class UpdateMigsUploadTemporaryDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateMigsUploadTemporaryDataCommand(RafDsRequest request){
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = request.getData();
		VenMigsUploadTemporary entityMigsUpload = new VenMigsUploadTemporary();

		//Calling EJB and merge the entity
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenMigsUploadTemporarySessionEJBRemote sessionHome = (VenMigsUploadTemporarySessionEJBRemote) locator.lookup(VenMigsUploadTemporarySessionEJBRemote.class, "VenMigsUploadTemporarySessionEJBBean");
			
			//Looping through data sent from client, and set to the entity
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> data = dataList.get(i);

				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.MIGSUPLOAD_MIGSID)) {
						try{
							entityMigsUpload = sessionHome.queryByRange("select o from VenMigsUploadTemporary o where o.migsId="+new Long(data.get(DataNameTokens.MIGSUPLOAD_MIGSID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityMigsUpload.setMigsId(new Long(data.get(DataNameTokens.MIGSUPLOAD_MIGSID)));
						}
						break;
					}
				}
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.MIGSUPLOAD_ACTION)) {
						entityMigsUpload.setAction(data.get(DataNameTokens.MIGSUPLOAD_ACTION));
					}
				}
			}
			
			sessionHome.mergeVenMigsUploadTemporary(entityMigsUpload);
			rafDsResponse.setStatus(0);
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

		rafDsResponse.setData(new ArrayList<HashMap<String, String>>());
		return rafDsResponse;
	}
}