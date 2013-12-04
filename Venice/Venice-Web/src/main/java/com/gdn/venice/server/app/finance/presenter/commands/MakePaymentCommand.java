package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.finance.payment.FinancePaymentCreatorSessionEJBRemote;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * COmmand to process the make-payment action
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class MakePaymentCommand implements RafRpcCommand{
	HashMap<String, String> paymentDataMap;
	
	/**
	 * Constructor to extract the form parameters
	 * @param parameter
	 */
	public MakePaymentCommand(String parameter) {
		paymentDataMap = Util.formHashMapfromXML(parameter);
	}
	
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	public String execute() {
		System.out.println("make payment");
		String paymentTo = paymentDataMap.get("PAYMENTTO");
		
		if (paymentTo.equals("MERCHANT")) {
			for (int i=0;i<paymentDataMap.size();i++) {
				String merchantPaymentString = paymentDataMap.get("PAYMENTDATA" + i);
				
				if (merchantPaymentString!=null) {
				System.out.println("merchantPaymentString: "+merchantPaymentString);
					Pattern p1 = Pattern.compile("[\\{\\}\\=\\, ]++");
					String[] split = p1.split( merchantPaymentString );
		
					HashMap<String,String> merchantPaymentMap = new HashMap<String,String>();
					for ( int j=1; j< split.length; j+=2 ) {
						merchantPaymentMap.put( split[j], split[j+1] );
					}
					
					Pattern p2 = Pattern.compile("#");
					
					String salesRecordIdParam = merchantPaymentMap.get("SALESRECORDIDS");
					System.out.println("salesRecordIdParam di command: "+salesRecordIdParam);
					String[] salesRecordIds = p2.split(salesRecordIdParam);
					
					ArrayList<Long> salesRecordIdList = new ArrayList<Long>();
					for (int j=0;j<salesRecordIds.length;j++) {
						salesRecordIdList.add(new Long(salesRecordIds[j]));
						System.out.println("sales record id ke: "+j+" - "+salesRecordIds[j]);
					}
					System.out.println("salesRecordIdList di command: "+salesRecordIdList.size());
					
					ArrayList<Long> manualJournalTransactionList = new ArrayList<Long>();;
					
					String finApManualJournalIdParam = merchantPaymentMap.get("FINAPMANUALJOURNALIDS");
					if (finApManualJournalIdParam!=null) {
						String[] finApManualJournalIds = p2.split(finApManualJournalIdParam);
						
						for (int j=0;j<finApManualJournalIds.length;j++) {
							manualJournalTransactionList.add(new Long(finApManualJournalIds[j]));
						}
					} 
					System.out.println("set param");
					Double apAmount = new Double(merchantPaymentMap.get("AMOUNT"));
					Double arAmount = new Double(merchantPaymentMap.get("PENALTYAMOUNT"));
					Long bankAccountId = new Long(merchantPaymentMap.get("BANKACCOUNTID"));
					Double pph23Amount = new Double(merchantPaymentMap.get("PPH23AMOUNT"));
					
					Locator<FinancePaymentCreatorSessionEJBRemote> financePaymentCreatorLocator = null;
	
					try {
						financePaymentCreatorLocator = new Locator<FinancePaymentCreatorSessionEJBRemote>();
						FinancePaymentCreatorSessionEJBRemote sessionHome = (FinancePaymentCreatorSessionEJBRemote) financePaymentCreatorLocator
								.lookup(FinancePaymentCreatorSessionEJBRemote.class, "FinancePaymentCreatorSessionEJBBean");
						System.out.println("create payment");
						sessionHome.createMerchantPayment(salesRecordIdList, manualJournalTransactionList, arAmount, apAmount, bankAccountId, pph23Amount);
						System.out.println("done create payment");
					} catch (Exception ex) {
						ex.printStackTrace();
						try {
							financePaymentCreatorLocator.close();
						} catch (Exception e) {
							String errorMsg = Util.extractMessageFromEJBExceptionText(e.getMessage());
							e.printStackTrace();
							return "-1" + ":" + errorMsg;
						}
					} finally {
						try {
							financePaymentCreatorLocator.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		} else if (paymentTo.equals("LOGISTICS")) {
			for (int i=0;i<paymentDataMap.size();i++) {
				String logisticsPaymentString = paymentDataMap.get("PAYMENTDATA" + i);
				
				if (logisticsPaymentString!=null) {
					
					Pattern p1 = Pattern.compile("[\\{\\}\\=\\, ]++");
					String[] split = p1.split( logisticsPaymentString );
		
					HashMap<String,String> logisticsPaymentMap = new HashMap<String,String>();
					for ( int j=1; j< split.length; j+=2 ) {
						logisticsPaymentMap.put( split[j], split[j+1] );
					}
					
					Pattern p2 = Pattern.compile("#");
					
					String invoiceIdParam = logisticsPaymentMap.get("INVOICEIDS");
					String[] invoiceIds = p2.split(invoiceIdParam);
					
					ArrayList<Long> finApInvoiceIdList = new ArrayList<Long>();
					for (int j=0;j<invoiceIds.length;j++) {
						finApInvoiceIdList.add(new Long(invoiceIds[j]));
					}
					
					ArrayList<Long> manualJournalTransactionList = new ArrayList<Long>();;
					
					String finApManualJournalIdParam = logisticsPaymentMap.get("FINAPMANUALJOURNALIDS");
					if (finApManualJournalIdParam!=null) {
						String[] finApManualJournalIds = p2.split(finApManualJournalIdParam);
						
						for (int j=0;j<finApManualJournalIds.length;j++) {
							manualJournalTransactionList.add(new Long(finApManualJournalIds[j]));
						}
					} 
					
					Double paymentAmount = new Double(logisticsPaymentMap.get("AMOUNT"));
					Double penaltyAmount = new Double(logisticsPaymentMap.get("PENALTYAMOUNT"));
					Long bankAccountId = new Long(logisticsPaymentMap.get("BANKACCOUNTID"));
					
					Locator<FinancePaymentCreatorSessionEJBRemote> financePaymentCreatorLocator = null;
	
					try {
						financePaymentCreatorLocator = new Locator<FinancePaymentCreatorSessionEJBRemote>();
						FinancePaymentCreatorSessionEJBRemote sessionHome = (FinancePaymentCreatorSessionEJBRemote) financePaymentCreatorLocator
								.lookup(FinancePaymentCreatorSessionEJBRemote.class,
										"FinancePaymentCreatorSessionEJBBean");
	
						sessionHome.createLogisticsPayment(finApInvoiceIdList, manualJournalTransactionList, penaltyAmount, paymentAmount, bankAccountId);
					} catch (Exception ex) {
						String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
						ex.printStackTrace();
						return "-1" + ":" + errorMsg;
					} finally{
						try {
							financePaymentCreatorLocator.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} 
				}
			}
		} else if (paymentTo.equals("REFUND")) {
			for (int i=0;i<paymentDataMap.size();i++) {
				String logisticsPaymentString = paymentDataMap.get("PAYMENTDATA" + i);
				
					if (logisticsPaymentString!=null) {
					
					Pattern p1 = Pattern.compile("[\\{\\}\\=\\, ]++");
					String[] split = p1.split( logisticsPaymentString );
		
					HashMap<String,String> refundPaymentMap = new HashMap<String,String>();
					for ( int j=1; j< split.length; j+=2 ) {
						refundPaymentMap.put( split[j], split[j+1] );
					}
					
					Pattern p2 = Pattern.compile("#");
					
					String refundRecordIdParam = refundPaymentMap.get("REFUNDRECORDIDS");
					Long finArFundsInRefundId = new Long(refundRecordIdParam);
					
					ArrayList<Long> manualJournalTransactionList = new ArrayList<Long>();;
					
					String finApManualJournalIdParam = refundPaymentMap.get("FINAPMANUALJOURNALIDS");
					if (finApManualJournalIdParam!=null) {
						String[] finApManualJournalIds = p2.split(finApManualJournalIdParam);
						
						for (int j=0;j<finApManualJournalIds.length;j++) {
							manualJournalTransactionList.add(new Long(finApManualJournalIds[j]));
						}
					} 
					
					Double paymentAmount = new Double(refundPaymentMap.get("AMOUNT"));
					Double penaltyAmount = new Double(refundPaymentMap.get("PENALTYAMOUNT"));
					Long bankAccountId = new Long(refundPaymentMap.get("BANKACCOUNTID"));
					
					Locator<FinancePaymentCreatorSessionEJBRemote> financePaymentCreatorLocator = null;
	
					try {
						financePaymentCreatorLocator = new Locator<FinancePaymentCreatorSessionEJBRemote>();
						FinancePaymentCreatorSessionEJBRemote sessionHome = (FinancePaymentCreatorSessionEJBRemote) financePaymentCreatorLocator
								.lookup(FinancePaymentCreatorSessionEJBRemote.class,
										"FinancePaymentCreatorSessionEJBBean");
	
						sessionHome.createRefundPayment(finArFundsInRefundId, manualJournalTransactionList, penaltyAmount, paymentAmount, bankAccountId);
					} catch (Exception ex) {
						String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
						ex.printStackTrace();
						return "-1" + ":" + errorMsg;
					} finally {
						try {
							financePaymentCreatorLocator.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}	
		}
		return "0";
	}	
}
