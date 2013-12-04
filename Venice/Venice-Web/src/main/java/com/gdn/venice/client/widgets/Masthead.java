package com.gdn.venice.client.widgets;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Masthead extends HLayout {

  private static final int MASTHEAD_HEIGHT = 20;
    
  Label breadCrumb;
  Label signedInUser;
  Label versionLabel;
  	
  public Masthead() {
    super();

    // initialise the layout container
    this.setHeight(MASTHEAD_HEIGHT);
    this.setPadding(2);
    
    Img logo = new Img("VeniceLogo.png");
    logo.setSize("200", "20");
    
    // initialise the Logo layout container
    HLayout logoLayout = new HLayout();
    logoLayout.setHeight(MASTHEAD_HEIGHT);	
    logoLayout.setWidth("200");
    logoLayout.addMember(logo);
    
    // initialise the Signed In User label
	signedInUser = new Label();  
	signedInUser.setWidth("400");
	signedInUser.setAlign(Alignment.LEFT);
	signedInUser.setHeight(10);
	signedInUser.setStyleName("venice-MastHead-SignedInUser-Refresh-LogOut");  
	signedInUser.setContents("<b>Welcome, John Doe!</b>");
	
	breadCrumb = new Label();  
	breadCrumb.setWidth("400");
	breadCrumb.setHeight(10);
	breadCrumb.setAlign(Alignment.LEFT);
	breadCrumb.setStyleName("venice-MastHead-SignedInUser-Refresh-LogOut");  
	breadCrumb.setContents("<b>You are here: </b>");
	
	Label navigationLinks = new Label();  
	navigationLinks.setWidth("100%");
	navigationLinks.setHeight(10);
	navigationLinks.setAlign(Alignment.RIGHT);
	navigationLinks.setStyleName("venice-MastHead-SignedInUser-Refresh-LogOut");  
	navigationLinks.setContents("<a href=\"javascript:window.location.reload()\">Refresh</a> | <a href=\""+GWT.getHostPageBaseURL() + "MainPagePresenterServlet?method=signOut&type=RPC\">Sign Out</a>&nbsp;&nbsp;	");
	
	versionLabel = new Label();  
	versionLabel.setHeight(10);
	versionLabel.setAlign(Alignment.RIGHT);
	versionLabel.setStyleName("venice-MastHead-SignedInUser-Refresh-LogOut");  
	versionLabel.setContents("<b>Version: &nbsp;&nbsp;	</b>");
	
    VLayout westLayout = new VLayout();
    westLayout.setAlign(Alignment.RIGHT);  
    westLayout.setHeight(MASTHEAD_HEIGHT);
    westLayout.setWidth("400");
    westLayout.addMember(signedInUser);
    westLayout.addMember(breadCrumb);
    
    VLayout eastLayout = new VLayout();
    eastLayout.setAlign(Alignment.RIGHT);  
    eastLayout.setHeight(MASTHEAD_HEIGHT);
    eastLayout.addMember(navigationLinks);
    eastLayout.addMember(versionLabel);
    
	this.addMember(logoLayout);  	
	this.addMember(westLayout); 
	this.addMember(eastLayout);	

	
  }	
  
  public void updateBreadCrumb(String path) {
	  path = path.substring(path.indexOf("/") + 1, path.length());
	  path = path.substring(0, path.indexOf("\""));
	  path = path.replaceAll("/", " &gt; ");
	  breadCrumb.setContents("<b>You are here: </b>" + path);
	  breadCrumb.redraw();
  }
  
  public void updateSignedInUser(String user) {
	  signedInUser.setContents("<b>Welcome, "+user+"</b>");
	  signedInUser.redraw();
  }
  
  public void updateVersion(String version) {
	  versionLabel.setContents("<b>Version: "+version+"&nbsp;&nbsp;	</b>");
	  versionLabel.redraw();
  }
  
}
