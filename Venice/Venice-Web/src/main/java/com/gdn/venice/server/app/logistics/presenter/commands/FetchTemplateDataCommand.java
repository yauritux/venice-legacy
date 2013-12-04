package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogReportTemplateSessionEJBRemote;
import com.gdn.venice.persistence.LogReportTemplate;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for fetching template combo box data in Logistics Provider form
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchTemplateDataCommand implements RafDsCommand {
	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistic Provider Report/Invoice Template Type combo box data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchTemplateDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;	
	}
	

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> templateDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator <Object>();
			
			LogReportTemplateSessionEJBRemote sessionHome = (LogReportTemplateSessionEJBRemote) locator.lookup(LogReportTemplateSessionEJBRemote.class, "LogReportTemplateSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the template type  data
			 */
			List<LogReportTemplate> logReportTemplateList = sessionHome.queryByRange("select o from LogReportTemplate o", 0, 0);
			
			/*
			 * Map the values from the combo box (Report Template)
			 */
			for(LogReportTemplate logReportTemplate:logReportTemplateList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEID, logReportTemplate.getTemplateId()!=null?logReportTemplate.getTemplateId().toString():"");
				map.put(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEDESC,logReportTemplate.getTemplateDesc() !=null? logReportTemplate.getTemplateDesc():"" );
		
				templateDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(templateDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+templateDataList.size());
			
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
		rafDsResponse.setData(templateDataList);
		return rafDsResponse;
	}

}
