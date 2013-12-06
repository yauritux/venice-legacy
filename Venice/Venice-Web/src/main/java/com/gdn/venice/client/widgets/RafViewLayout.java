package com.gdn.venice.client.widgets;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.widgets.layout.VLayout;

public class RafViewLayout extends VLayout{
	private String viewPageName;
	
	public RafViewLayout(){
        super();
    }

    public RafViewLayout(JavaScriptObject jsObj){
        super(jsObj);
    }

    public RafViewLayout(int membersMargin) {
    	super(membersMargin);
    }

	public String getViewPageName() {
		return viewPageName;
	}

	public void setViewPageName(String viewPageName) {
		this.viewPageName = viewPageName;
	}
}
