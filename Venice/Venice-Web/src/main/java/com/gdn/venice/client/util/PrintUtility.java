package com.gdn.venice.client.util;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Utility to print various types of items such as ListGrid (only ListGrid currently)
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2012
 */
public class PrintUtility {
	public static Boolean printComponent(Object object){
		Object[] components = new Object[]{object};
		if(object instanceof ListGrid){
			ListGrid.printComponents(components);
		}
		if(object instanceof VLayout){
			VLayout.printComponents(components);
		}
		return true;
	}
}
