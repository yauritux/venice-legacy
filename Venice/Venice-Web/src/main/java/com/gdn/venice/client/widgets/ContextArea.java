package com.gdn.venice.client.widgets;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

public class ContextArea extends VLayout {

	  Label label; 	
			  	
	  public ContextArea() {
	    super();
						
	    // initialise the layout container
	    this.setWidth("*"); 
	    this.setBackgroundColor("transparent");
					
	    // initialise the context area label
	    label = new Label(); 
	    label.setContents("Context Area");  
	    label.setAlign(Alignment.CENTER);  
	    label.setOverflow(Overflow.HIDDEN);  
					    
	    // add the label to the layout container
	    this.addMember(label); 
	  }	
	}

