package com.gdn.venice.server.util;

import java.text.ParseException;

public class VeniceTestEJBClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
		try {
			System.out.println(formatter.parseFromDDMMYYYY("01-12-2013"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
//		
//		//The object locators we are going to use
////		Locator<VenCountrySessionEJBRemote> countryLocator = null;
////		Locator<FrdEntityBlacklistSessionEJBRemote> blacklistLocator = null;
//		Locator<VenOrderSessionEJBRemote> orderLocator = null;
//		Locator<VenContactDetailSessionEJBRemote> contactDetailLocator = null;
//		
//		try {
//			
//			Long startTime = System.currentTimeMillis();
//			
//			orderLocator = new Locator<VenOrderSessionEJBRemote>();
//			
//			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) orderLocator
//			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
//			
//			VenOrder order = new VenOrder();
//			order.setOrderId(new Long(961));
//			
//			List<VenOrder> orderList = orderSessionHome.findByVenOrderLike(order, null, 0, 1);
//			
//			VenParty venParty = orderList.get(0).getVenCustomer().getVenParty();
//			
//			contactDetailLocator = new Locator<VenContactDetailSessionEJBRemote>();
//			
//			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) contactDetailLocator
//			.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
//			
//			String select = "select cd from VenContactDetail cd where cd.venParty.partyId = " + venParty.getPartyId();
//			
//			
//			
//			List<VenContactDetail> resultList = venContactDetailSessionHome.queryByRange(select, 0, 10);
//
//			System.out.println(resultList.size());
//			System.out.println(resultList.get(0).getVenContactDetailType().getContactDetailTypeDesc());
//			
//			
//			// Use the Locator class to locate the home object for the country EJB
////			countryLocator = new Locator<VenCountrySessionEJBRemote>();
////
////			//Lookup the country EJB using the locator instance
////			VenCountrySessionEJBRemote sessionHome = (VenCountrySessionEJBRemote) countryLocator
////					.lookup(VenCountrySessionEJBRemote.class, "VenCountrySessionEJBBean");
////
////			//Create a new country business object and persist it then look it up and print it
////			VenCountry newCountry = new VenCountry();
////			newCountry.setCountryName("Indonesia");
//////			VenCountry persistedCountry = (VenCountry) sessionHome
//////					.persistVenCountry(newCountry);
//////			System.out.println("CountryId:" + persistedCountry.getCountryId()
//////					+ " CountryName:" + persistedCountry.getCountryName());
////			
////			List<VenCountry> list = sessionHome.findByVenCountryLike(newCountry, null, 0, 1);
////			
////			System.out.println(list.size());
////			System.out.println(list.get(0).getCountryName());
//
//			
////			blacklistLocator = new Locator<FrdEntityBlacklistSessionEJBRemote>();
////
////			//Look up an entity blacklist EJB
////			
////			FrdEntityBlacklistSessionEJBRemote frdEntityBlacklistSessionHome = (FrdEntityBlacklistSessionEJBRemote) blacklistLocator
////			.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
////			
////			// Set up an object to carry the query criteria
////			List<FrdEntityBlacklist> frdEntityBlacklistList = null;
////			FrdEntityBlacklist frdEntityBlacklist = new FrdEntityBlacklist();
////			// Set the query criteria
////			frdEntityBlacklist.setBlacklistString("%");
////
////			frdEntityBlacklistList = frdEntityBlacklistSessionHome
////					.findByFrdEntityBlacklistLike(frdEntityBlacklist, new JPQLAdvancedQueryCriteria("and"), 0, 10);
////
////			Iterator<FrdEntityBlacklist> i = frdEntityBlacklistList.iterator();
////			while (i.hasNext()) {
////				FrdEntityBlacklist next = (FrdEntityBlacklist) i.next();
////				System.out.println("Blacklist String:"
////						+ next.getBlacklistString());
////			}
//			
//			Long endTime = System.currentTimeMillis();
//			Long duration = endTime - startTime;
//			
//			System.out.println("Execution time:" + duration + "ms" );
//
//
//		} catch (Throwable ex) {
//			ex.printStackTrace();
//		} finally {
//				try {
//					contactDetailLocator.close();
////					countryLocator.close();
////					blacklistLocator.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		}

	}

}
