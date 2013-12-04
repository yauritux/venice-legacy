package com.gdn.venice.client.widgets;

import java.util.Date;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

public class Mastfoot extends HLayout {

  private static final int MASTFOOT_HEIGHT = 20;
    
  public Mastfoot() {
    super();
		
    // initialise the layout container
    this.setHeight(MASTFOOT_HEIGHT);

    
    // initialise the Signed In User label
	Label dateTimeLabel = new Label();  
	dateTimeLabel.setWidth("200");
	dateTimeLabel.setBorder("");
	dateTimeLabel.setAlign(Alignment.RIGHT);
	dateTimeLabel.setStyleName("venice-MastHead-SignedInUser-Refresh-LogOut");  
	dateTimeLabel.setContents(new Date().toString());   
	
	Label menuCodeLabel = new Label();  
	menuCodeLabel.setWidth("200");
	menuCodeLabel.setBorder("");
	menuCodeLabel.setStyleName("venice-MastHead-SignedInUser-Refresh-LogOut");  
//	menuCodeLabel.setContents("&nbsp;<b>Menu Code:</b> FM4");   

    // initialise the East layout container
    HLayout westLayout = new HLayout();
    westLayout.setAlign(Alignment.LEFT);  
    westLayout.setHeight(MASTFOOT_HEIGHT);
    westLayout.setWidth("50%");
    westLayout.addMember(menuCodeLabel);
    
    // initialise the East layout container
    HLayout eastLayout = new HLayout();
    eastLayout.setAlign(Alignment.RIGHT);  
    eastLayout.setHeight(MASTFOOT_HEIGHT);
    eastLayout.setWidth("50%");
    eastLayout.addMember(dateTimeLabel);	
    // add the label to the layout container
    this.addMember(westLayout);
    this.addMember(eastLayout); 
  }	
}
