package com.gdn.venice.client.ui.data;

import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.app.NameTokens;

/**
 * This class contains the menau data definition for the administration stack section
 *
 */
public class ReservationNavigationPaneSectionData {
	private static NavigationPaneTreeNodeRecord[] records;

	  public static NavigationPaneTreeNodeRecord[] getRecords() {
		if (records == null) {
		  records = getNewRecords();
		}
		return records;
	  }

	  public static NavigationPaneTreeNodeRecord[] getNewRecords() {
		return new NavigationPaneTreeNodeRecord[]{
		  new NavigationPaneTreeNodeRecord("2", "1", "Sales Center Module", null, "RSV1", DataWidgetNameTokens.RSV_SALESCENTERMODULETREENODE),
		  new NavigationPaneTreeNodeRecord("3", "2", "Order Management", NameTokens.reservationOrderManagement, "RSV2", DataWidgetNameTokens.RSV_ORDERMANAGEMENTMODULETREENODE),
		};
	  }
}
