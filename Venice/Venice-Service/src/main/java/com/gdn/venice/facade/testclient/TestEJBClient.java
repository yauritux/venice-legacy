package com.gdn.venice.facade.testclient;

import java.util.List;

import com.djarum.raf.facade.authorization.RafPermission;
import com.djarum.raf.facade.authorization.RafPermissionsSessionEJBRemote;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenCountrySessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenCountry;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderStatus;

public class TestEJBClient {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		//The object locators we are going to use
		Locator<VenCountrySessionEJBRemote> countryLocator = null;
		Locator<VenOrderSessionEJBRemote> orderLocator = null;
//		Locator<FrdEntityBlacklistSessionEJBRemote> blacklistLocator = null;
//		Locator<LogActivityReconRecordSessionEJBRemote> activityReconRecordLocator = null;
		
		try {
			
			Long startTime = System.currentTimeMillis();
			// Use the Locator class to locate the home object for the country EJB
			countryLocator = new Locator<VenCountrySessionEJBRemote>();

			//Lookup the country EJB using the locator instance
			//
			//Create a new country business object and persist it then look it up and print it
			VenCountry newCountry = new VenCountry();
			newCountry.setCountryName("Indonesia");
			
			VenCountrySessionEJBRemote countryHome = (VenCountrySessionEJBRemote) countryLocator
			.lookup(VenCountrySessionEJBRemote.class, "VenCountrySessionEJBBean");
			
			List<VenCountry> venCountryList = countryHome.queryByRange("select o from VenCountry o", 0, 100);
			
			VenCountry existingCountry = venCountryList.get(0);
			
			existingCountry.setCountryName("Indahnesia");

			VenCountry mergedCountry = (VenCountry) countryHome
					.mergeVenCountry(existingCountry);
			System.out.println("CountryId:" + mergedCountry.getCountryId()
					+ " CountryName:" + mergedCountry.getCountryName());
			

			orderLocator = new Locator<VenOrderSessionEJBRemote>();
			
			VenOrderSessionEJBRemote orderHome = (VenOrderSessionEJBRemote) orderLocator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");

			List<VenOrder> venOrderList = orderHome.queryByRange("select o from VenOrder o where o.orderId=100", 0, 0);
			VenOrder myOrder = venOrderList.get(0);
			
			VenOrderStatus venOrderStatus = new VenOrderStatus();
			
			venOrderStatus.setOrderStatusId(com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_FP);
			
			myOrder.setVenOrderStatus(venOrderStatus);
			
			orderHome.mergeVenOrder(myOrder);
			
			Locator<Object> orderItemLocator = new Locator<Object>();
			
			VenOrderItemSessionEJBRemote orderItemHome = (VenOrderItemSessionEJBRemote) orderItemLocator
			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			List<VenOrderItem> orderItemList = orderItemHome.queryByRange("select o from VenOrderItem o where o.orderItemId =100", 0, 0);
 			VenOrderItem venOrderItem = orderItemList.get(0);
 			
			venOrderStatus.setOrderStatusId(com.gdn.venice.util.VeniceConstants.VEN_ORDER_STATUS_ES);

			venOrderItem.setVenOrderStatus(venOrderStatus);
			
			Locator<Object> locator = new Locator<Object>();
			
			RafPermissionsSessionEJBRemote rafPermissionsHome = (RafPermissionsSessionEJBRemote) locator
			.lookup(RafPermissionsSessionEJBRemote.class, "RafPermissionsSessionEJBBean");
			
			List<RafPermission> rafPermissionList = rafPermissionsHome.getUserPermissionList("David");
			for(RafPermission permission:rafPermissionList){
				System.out.println("CanonicalName:" + permission.getApplicationObjectCanonicalName());
				System.out.println("PermissionType:" + permission.getPermissionTypeId());
			}


//
//			
//			blacklistLocator = new Locator<FrdEntityBlacklistSessionEJBRemote>();

			//Look up an entity blacklist EJB
			
//			FrdEntityBlacklistSessionEJBRemote frdEntityBlacklistSessionHome = (FrdEntityBlacklistSessionEJBRemote) blacklistLocator
//			.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
			
			// Set up an object to carry the query criteria
//			List<FrdEntityBlacklist> frdEntityBlacklistList = null;
//			FrdEntityBlacklist frdEntityBlacklist = new FrdEntityBlacklist();
			// Set the query criteria
//			frdEntityBlacklist.setBlacklistString("%");
//
//			frdEntityBlacklistList = frdEntityBlacklistSessionHome
//					.findByFrdEntityBlacklistLike(frdEntityBlacklist, new JPQLAdvancedQueryCriteria("and"), 0, 10);
//
//			Iterator<FrdEntityBlacklist> i = frdEntityBlacklistList.iterator();
//			while (i.hasNext()) {
//				FrdEntityBlacklist next = (FrdEntityBlacklist) i.next();
//				System.out.println("Blacklist String:"
//						+ next.getBlacklistString());
//			}
//			
//			Long endTime = System.currentTimeMillis();
//			Long duration = endTime - startTime;
//			
//			System.out.println("Execution time:" + duration + "ms" );
			
//			activityReconRecordLocator = new Locator<LogActivityReconRecordSessionEJBRemote>();
//
//			LogActivityReconRecordSessionEJBRemote reconRecordSessionHome = (LogActivityReconRecordSessionEJBRemote) activityReconRecordLocator
//			.lookup(LogActivityReconRecordSessionEJBRemote.class, "LogActivityReconRecordSessionEJBBean");
//			
//			// Set up an object to carry the query criteria
//			List<LogActivityReconRecord> activityReconRecordlist = reconRecordSessionHome.queryByRange("select arr from LogActivityReconRecord arr", 1, 100);
			
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
				try {
					countryLocator.close();
//					blacklistLocator.close();
//					activityReconRecordLocator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

	}

}
