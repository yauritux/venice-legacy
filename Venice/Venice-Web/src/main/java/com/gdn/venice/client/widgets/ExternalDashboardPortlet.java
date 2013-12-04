package com.gdn.venice.client.widgets;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.Portlet;
import com.smartgwt.client.widgets.layout.VLayout;

public class ExternalDashboardPortlet extends Portlet{
	VLayout portletLayout;
	
	
	public ExternalDashboardPortlet(String title, String url) {
        setTitle(title);
        setShowMaximizeButton(false);
        setShowMinimizeButton(false);
        setShowCloseButton(false);

        setHeight100();
        setWidth100();
       
      
        HTMLPane htmlPane = new HTMLPane();
        htmlPane.setContentsURL(url);
        htmlPane.setOverflow(Overflow.HIDDEN);
        htmlPane.setContentsType(ContentsType.PAGE);
     
        portletLayout = new VLayout();
        portletLayout.setMembers(htmlPane);
        portletLayout.setHeight(4000);
        portletLayout.setAlign(Alignment.CENTER);
		addItem(portletLayout);
	}


	public VLayout getPortletLayout() {
		return portletLayout;
	}

	
	
}
